package part3.chapter10;

import java.awt.Toolkit;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

public class CompressAwt {

    /** The resulting PDF file. */
    public static final String RESULT1 = "results/part3/chapter10/hitchcock100.pdf";

    /** The resulting PDF file. */
    public static final String RESULT2 = "results/part3/chapter10/hitchcock20.pdf";

    /** The resulting PDF file. */
    public static final String RESULT3 = "results/part3/chapter10/hitchcock10.pdf";

    /** One of the resources. */
    public static final String RESOURCE = "resources/img/hitchcock.png";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename, float quality) throws IOException, DocumentException {
        Document document = new Document(new Rectangle(200, 280));
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        java.awt.Image awtImage = Toolkit.getDefaultToolkit().createImage(RESOURCE);
        Image img = Image.getInstance(writer, awtImage, quality);
        img.setAbsolutePosition(15, 15);
        document.add(img);
        document.close();
    }

    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new CompressAwt().createPdf(RESULT1, 1);
        new CompressAwt().createPdf(RESULT2, 0.2f);
        new CompressAwt().createPdf(RESULT3, 0.1f);
    }
}
