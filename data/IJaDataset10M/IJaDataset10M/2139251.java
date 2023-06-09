package com.lowagie.examples.objects.tables;

import java.io.FileOutputStream;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Changing the padding and the leading of the content of a PdfPCell.
 */
public class CellPaddingLeading {

    /**
	 * Changing padding and leading.
	 * 
	 * @param args
	 *            no arguments needed
	 */
    public static void main(String[] args) {
        System.out.println("padding - leading");
        Document document = new Document(PageSize.A4.rotate(), 10, 10, 10, 10);
        try {
            PdfWriter.getInstance(document, new FileOutputStream("PaddingLeading.pdf"));
            document.open();
            PdfPTable table = new PdfPTable(2);
            PdfPCell cell;
            Paragraph p = new Paragraph("Quick brown fox jumps over the lazy dog. Quick brown fox jumps over the lazy dog.");
            table.addCell("default");
            table.addCell(p);
            table.addCell("padding 10");
            cell = new PdfPCell(p);
            cell.setPadding(10f);
            table.addCell(cell);
            table.addCell("no padding at all");
            cell = new PdfPCell(p);
            cell.setPadding(0f);
            table.addCell(cell);
            table.addCell("no padding at the top; large padding at the left");
            cell = new PdfPCell(p);
            cell.setPaddingTop(0f);
            cell.setPaddingLeft(20f);
            table.addCell(cell);
            document.add(table);
            document.newPage();
            table = new PdfPTable(2);
            table.addCell("no leading at all");
            table.getDefaultCell().setLeading(0f, 0f);
            table.addCell("blah blah\nblah blah blah\nblah blah\nblah blah blah\nblah blah\nblah blah blah\nblah blah\nblah blah blah\n");
            table.getDefaultCell().setLeading(14f, 0f);
            table.addCell("fixed leading of 14pt");
            table.addCell("blah blah\nblah blah blah\nblah blah\nblah blah blah\nblah blah\nblah blah blah\nblah blah\nblah blah blah\n");
            table.addCell("relative leading of 1.0 times the fontsize");
            table.getDefaultCell().setLeading(0f, 1.0f);
            table.addCell("blah blah\nblah blah blah\nblah blah\nblah blah blah\nblah blah\nblah blah blah\nblah blah\nblah blah blah\n");
            document.add(table);
        } catch (Exception de) {
            de.printStackTrace();
        }
        document.close();
    }
}
