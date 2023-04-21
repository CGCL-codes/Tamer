package in_action.chapter06;

import java.io.FileOutputStream;
import java.io.IOException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
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
public class PdfPTableSplit {

    /**
	 * Generates a PDF file with a table.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
    public static void main(String[] args) {
        System.out.println("Chapter 6: example PdfPTableSplit");
        System.out.println("-> Creates three PDF files");
        System.out.println("   that split a PdfPTable in three different ways.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> resulting PDF: SplitRowsBetween.pdf, SplitRowsWithin.pdf and OmitRows.pdf");
        Document document1 = new Document(PageSize.A4.rotate());
        Document document2 = new Document(PageSize.A4.rotate());
        Document document3 = new Document(PageSize.A4.rotate());
        try {
            PdfWriter.getInstance(document1, new FileOutputStream("results/in_action/chapter06/SplitRowsBetween.pdf"));
            PdfWriter.getInstance(document2, new FileOutputStream("results/in_action/chapter06/SplitRowsWithin.pdf"));
            PdfWriter.getInstance(document3, new FileOutputStream("results/in_action/chapter06/OmitRows.pdf"));
            document1.open();
            document2.open();
            document3.open();
            String text = ". Quick brown fox jumps over the lazy dog.";
            PdfPTable table = new PdfPTable(2);
            PdfPCell largeCell = new PdfPCell();
            for (int i = 1; i < 13; i++) {
                largeCell.addElement(new Paragraph(String.valueOf(i) + text));
            }
            for (int i = 1; i < 11; i++) {
                table.addCell(String.valueOf(i));
                table.addCell(largeCell);
                if (i == 8) {
                    for (int j = 13; j < 31; j++) {
                        largeCell.addElement(new Paragraph(String.valueOf(j) + text));
                    }
                }
            }
            document1.add(table);
            table.setSplitLate(false);
            document2.add(table);
            table.setSplitRows(false);
            document3.add(table);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document1.close();
        document2.close();
        document3.close();
    }
}
