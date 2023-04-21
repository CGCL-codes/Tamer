package com.lowagie.examples.objects.tables.alternatives;

import java.awt.Color;
import java.io.FileOutputStream;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.SimpleCell;
import com.lowagie.text.SimpleTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;

/**
 * Example that is used to test the TableAttributes class.
 */
public class TablePdfPTable {

    /**
     * Example that is used to test the TableAttributes class.
     * @param args no arguments needed
     */
    public static void main(String[] args) {
        System.out.println("TableAttributes");
        Document document = new Document(PageSize.A4.rotate(), 50, 50, 50, 50);
        try {
            PdfWriter.getInstance(document, new FileOutputStream("tableattributes.pdf"));
            RtfWriter2.getInstance(document, new FileOutputStream("tableattributes.rtf"));
            document.open();
            SimpleTable table = new SimpleTable();
            table.setCellpadding(5);
            table.setCellspacing(8);
            SimpleCell row = new SimpleCell(SimpleCell.ROW);
            row.setBackgroundColor(Color.yellow);
            SimpleCell cell = new SimpleCell(SimpleCell.CELL);
            cell.setWidth(100f);
            cell.add(new Paragraph("rownumber"));
            row.add(cell);
            cell = new SimpleCell(SimpleCell.CELL);
            cell.setWidth(50f);
            cell.add(new Paragraph("A"));
            row.add(cell);
            cell = new SimpleCell(SimpleCell.CELL);
            cell.setWidth(50f);
            cell.add(new Paragraph("B"));
            row.add(cell);
            cell = new SimpleCell(SimpleCell.CELL);
            cell.setWidth(50f);
            cell.add(new Paragraph("C"));
            row.add(cell);
            table.addElement(row);
            for (int i = 0; i < 100; i++) {
                row = new SimpleCell(SimpleCell.ROW);
                switch(i % 3) {
                    case 0:
                        row.setBackgroundColor(Color.red);
                        break;
                    case 1:
                        row.setBackgroundColor(Color.green);
                        break;
                    case 2:
                        row.setBackgroundColor(Color.blue);
                        break;
                }
                if (i % 2 == 1) {
                    row.setBorderWidth(3f);
                }
                cell = new SimpleCell(SimpleCell.CELL);
                cell.add(new Paragraph("Row " + (i + 1)));
                cell.setSpacing_left(20f);
                row.add(cell);
                if (i % 5 == 4) {
                    cell = new SimpleCell(SimpleCell.CELL);
                    cell.setColspan(3);
                    cell.setBorderColor(Color.orange);
                    cell.setBorderWidth(5f);
                    cell.add(new Paragraph("Hello!"));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    row.add(cell);
                } else {
                    cell = new SimpleCell(SimpleCell.CELL);
                    cell.add(new Paragraph("A"));
                    row.add(cell);
                    cell = new SimpleCell(SimpleCell.CELL);
                    cell.add(new Paragraph("B"));
                    cell.setBackgroundColor(Color.gray);
                    row.add(cell);
                    cell = new SimpleCell(SimpleCell.CELL);
                    cell.add(new Paragraph("C"));
                    row.add(cell);
                }
                table.addElement(row);
            }
            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        }
        document.close();
    }
}
