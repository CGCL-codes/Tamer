package in_action.chapter06;

import java.io.FileOutputStream;
import java.io.IOException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */
public class PdfPTableImages {

    /**
	 * Generates a PDF file with a table.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
    public static void main(String[] args) {
        System.out.println("Chapter 6: example PdfPTableImages");
        System.out.println("-> Creates a PDF file with a PdfPTable that contains images.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> resources needed: foxdog.jpg");
        System.out.println("-> resulting PDF: pdfptable_images.pdf");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter06/pdfptable_images.pdf"));
            document.open();
            Image img = Image.getInstance("resources/in_action/chapter06/foxdog.jpg");
            PdfPTable table = new PdfPTable(1);
            table.addCell("This image was added with addCell(Image); the image is scaled + there is the default padding of getDefaultCell.");
            table.addCell(img);
            table.addCell("This image was added with addCell(PdfPCell); scaled, no padding");
            table.addCell(new PdfPCell(img, true));
            table.addCell("This image was added with addCell(PdfPCell); not scaled");
            table.addCell(new PdfPCell(img, false));
            document.add(table);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
}
