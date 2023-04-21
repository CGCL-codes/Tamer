package in_action.chapter06;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
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
public class PdfPTableRepeatHeaderFooter {

    /**
	 * Generates a PDF file with a table.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
    public static void main(String[] args) {
        System.out.println("Chapter 6: example PdfPTableRepeatHeaderFooter");
        System.out.println("-> Creates a PDF file with a large PdfPTable.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> resulting PDF: pdfptable_repeated.pdf");
        Document document = new Document(PageSize.A4.rotate());
        try {
            PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter06/pdfptable_repeated2.pdf"));
            document.open();
            PdfPTable datatable = new PdfPTable(10);
            int headerwidths[] = { 10, 24, 12, 12, 7, 7, 7, 7, 7, 7 };
            datatable.setWidths(headerwidths);
            datatable.setWidthPercentage(100);
            datatable.getDefaultCell().setPadding(5);
            PdfPCell cell = new PdfPCell(new Phrase("Administration - System Users Report", FontFactory.getFont(FontFactory.HELVETICA, 24, Font.BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderWidth(2);
            cell.setColspan(10);
            cell.setBackgroundColor(new Color(0xC0, 0xC0, 0xC0));
            cell.setUseDescender(true);
            datatable.addCell(cell);
            datatable.getDefaultCell().setBorderWidth(2);
            datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            datatable.addCell("User Id");
            datatable.addCell("Name\nAddress");
            datatable.addCell("Company");
            datatable.addCell("Department");
            PdfPTable permissions = new PdfPTable(6);
            permissions.getDefaultCell().setBorderWidth(2);
            permissions.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            permissions.getDefaultCell().setColspan(6);
            permissions.addCell("Permissions");
            permissions.getDefaultCell().setColspan(1);
            permissions.addCell("Admin");
            permissions.addCell("Data");
            permissions.addCell("Expl");
            permissions.addCell("Prod");
            permissions.addCell("Proj");
            permissions.addCell("Online");
            PdfPCell permission = new PdfPCell(permissions);
            permission.setColspan(6);
            datatable.addCell(permission);
            PdfPCell empty = new PdfPCell();
            empty.setColspan(4);
            datatable.addCell(empty);
            datatable.addCell("Admin");
            datatable.addCell("Data");
            datatable.addCell("Expl");
            datatable.addCell("Prod");
            datatable.addCell("Proj");
            datatable.addCell("Online");
            datatable.setHeaderRows(3);
            datatable.setFooterRows(1);
            datatable.getDefaultCell().setBorderWidth(1);
            for (int i = 1; i < 30; i++) {
                datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                datatable.addCell("myUserId");
                datatable.addCell("Somebody with a very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very long long name");
                datatable.addCell("No Name Company");
                datatable.addCell("D" + i);
                datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                for (int j = 0; j < 6; j++) datatable.addCell(Math.random() > .5 ? "Yes" : "No");
            }
            datatable.setSplitLate(false);
            document.add(datatable);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
}
