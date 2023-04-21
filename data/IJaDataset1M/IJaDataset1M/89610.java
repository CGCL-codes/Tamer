package part4.chapter14;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class Text2ToPdf3 {

    /** The resulting PDF. */
    public static final String RESULT = "results/part4/chapter14/text23.pdf";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
        Document document = new Document(new Rectangle(300, 150));
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        PdfContentByte canvas = writer.getDirectContent();
        Graphics2D g2 = canvas.createGraphicsShapes(300, 150);
        TextExample2 text = new TextExample2();
        text.setSize(new Dimension(300, 150));
        text.paint(g2);
        g2.dispose();
        document.close();
    }

    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new Text2ToPdf3().createPdf(RESULT);
    }
}
