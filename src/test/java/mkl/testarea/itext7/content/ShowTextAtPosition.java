package mkl.testarea.itext7.content;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.BeforeClass;
import org.junit.Test;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.util.StreamUtil;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

/**
 * @author mkl
 */
public class ShowTextAtPosition {
    final static File RESULT_FOLDER = new File("target/test-outputs", "content");

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        RESULT_FOLDER.mkdirs();
    }

    /**
     * <a href="https://stackoverflow.com/questions/57602156/how-to-add-paragraph-and-border-using-openpdf">
     * How to add paragraph and border using OpenPDF
     * </a>
     * <p>
     * This test shows how to create a vertically and horizontally
     * centered paragraph with a dark blue border.
     * </p>
     */
    @Test
    public void testShowCenteredBorderedParagraph() throws IOException {
        String firstName = "Mister";
        String lastName = "Nine";
        try (   PdfWriter pdfWriter = new PdfWriter(new File(RESULT_FOLDER, "CenterParagraph.pdf"));
                PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                Document document = new Document(pdfDocument)   ) {
            Paragraph paragraph = new Paragraph("Hello! This PDF is created for "+firstName+" "+lastName);
            paragraph.setWidth(100).setBorder(new SolidBorder(new DeviceRgb(0f, 0f, 0.6f), 3));
            PageSize box = pdfDocument.getDefaultPageSize();
            document.showTextAligned(paragraph, (box.getLeft() + box.getRight()) / 2, (box.getTop() + box.getBottom()) / 2,
                    TextAlignment.CENTER, VerticalAlignment.MIDDLE);
        }
    }

    /**
     * <a href="https://stackoverflow.com/questions/57602156/how-to-add-paragraph-and-border-using-openpdf">
     * How to add paragraph and border using OpenPDF
     * </a>
     * <p>
     * This test shows how to create a vertically and horizontally
     * centered paragraph, decorated with header and footer images
     * and surrounded by a dark blue border.
     * </p>
     */
    @Test
    public void testShowCenteredParagraphWithExtras() throws IOException {
        String firstName = "Mister";
        String lastName = "Nine";

        Image img = null;
        try (   InputStream imageResource = getClass().getResourceAsStream("/mkl/testarea/itext7/annotate/Willi-1.jpg") ) {
            ImageData data = ImageDataFactory.create(StreamUtil.inputStreamToArray(imageResource));
            img = new Image(data);
            img.scaleToFit(100f, 100f);
        }

        try (   PdfWriter pdfWriter = new PdfWriter(new File(RESULT_FOLDER, "CenterParagraphWithExtras.pdf"));
                PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                Document document = new Document(pdfDocument)   ) {
            PageSize box = pdfDocument.getDefaultPageSize();

            Paragraph paragraph = new Paragraph("Hello! This PDF is created for "+firstName+" "+lastName);
            paragraph.setWidth(100);
            document.showTextAligned(paragraph, (box.getLeft() + box.getRight()) / 2, (box.getTop() + box.getBottom()) / 2,
                    TextAlignment.CENTER, VerticalAlignment.MIDDLE);

            PdfCanvas pdfCanvas = new PdfCanvas(pdfDocument.getLastPage());
            Rectangle borderRectangle = new Rectangle(box.getLeft() + 5, box.getBottom() + 5, box.getWidth() - 10, box.getHeight() - 10);
            pdfCanvas.setColor(new DeviceRgb(0f, 0f, 0.6f), false);
            pdfCanvas.setLineWidth(3);
            pdfCanvas.rectangle(borderRectangle);
            pdfCanvas.stroke();

            img.setFixedPosition(box.getLeft() + 40, box.getTop() - 150);
            document.add(img);
            img.setFixedPosition(box.getLeft() + 40, box.getBottom() + 50);
            document.add(img);
        }
    }
}
