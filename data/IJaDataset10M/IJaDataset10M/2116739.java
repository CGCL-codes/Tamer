package com.gtdfree.test;

import java.awt.Color;
import java.io.FileOutputStream;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Demonstrates different borderstyles.
 */
public class TableBorders {

    /**
		 * Demonstrates different borderstyles.
		 * 
		 * @param args
		 *            the number of rows for each table fragment.
		 */
    public static void main(String[] args) {
        System.out.println("Table Borders");
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("TableBorders.pdf"));
            document.open();
            Font tableFont = FontFactory.getFont("Helvetica", 8, Font.BOLD, Color.BLACK);
            float padding = 0f;
            Rectangle border = new Rectangle(0f, 0f);
            border.setBorderWidthLeft(6f);
            border.setBorderWidthBottom(5f);
            border.setBorderWidthRight(4f);
            border.setBorderWidthTop(2f);
            border.setBorderColorLeft(Color.RED);
            border.setBorderColorBottom(Color.ORANGE);
            border.setBorderColorRight(Color.YELLOW);
            border.setBorderColorTop(Color.GREEN);
            makeTestPage(tableFont, border, writer, document, padding, true, true);
            Font font = FontFactory.getFont("Helvetica", 10);
            Paragraph p;
            p = new Paragraph("\nVarious border widths and colors\nuseAscender=true, useDescender=true", font);
            document.add(p);
            document.newPage();
            padding = 2f;
            border = new Rectangle(0f, 0f);
            border.setBorderWidthLeft(1f);
            border.setBorderWidthBottom(2f);
            border.setBorderWidthRight(1f);
            border.setBorderWidthTop(2f);
            border.setBorderColor(Color.BLACK);
            makeTestPage(tableFont, border, writer, document, padding, true, true);
            p = new Paragraph("More typical use - padding of 2\nuseBorderPadding=true, useAscender=true, useDescender=true", font);
            document.add(p);
            document.newPage();
            padding = 0f;
            border = new Rectangle(0f, 0f);
            border.setBorderWidthLeft(1f);
            border.setBorderWidthBottom(2f);
            border.setBorderWidthRight(1f);
            border.setBorderWidthTop(2f);
            border.setBorderColor(Color.BLACK);
            makeTestPage(tableFont, border, writer, document, padding, false, true);
            p = new Paragraph("\nuseBorderPadding=true, useAscender=false, useDescender=true", font);
            document.add(p);
            document.newPage();
            padding = 0f;
            border = new Rectangle(0f, 0f);
            border.setBorderWidthLeft(1f);
            border.setBorderWidthBottom(2f);
            border.setBorderWidthRight(1f);
            border.setBorderWidthTop(2f);
            border.setBorderColor(Color.BLACK);
            makeTestPage(tableFont, border, writer, document, padding, false, false);
            p = new Paragraph("\nuseBorderPadding=true, useAscender=false, useDescender=false", font);
            document.add(p);
            document.newPage();
            padding = 0f;
            border = new Rectangle(0f, 0f);
            border.setBorderWidthLeft(1f);
            border.setBorderWidthBottom(2f);
            border.setBorderWidthRight(1f);
            border.setBorderWidthTop(2f);
            border.setBorderColor(Color.BLACK);
            makeTestPage(tableFont, border, writer, document, padding, true, false);
            p = new Paragraph("\nuseBorderPadding=true, useAscender=true, useDescender=false", font);
            document.add(p);
        } catch (Exception de) {
            de.printStackTrace();
        }
        document.close();
    }

    private static void makeTestPage(Font tableFont, Rectangle borders, @SuppressWarnings("unused") PdfWriter writer, Document document, float padding, boolean ascender, boolean descender) throws BadElementException, DocumentException {
        document.newPage();
        PdfPTable table = null;
        table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        float leading = tableFont.getSize() * 1.2f;
        table.addCell(makeCell("1-Top", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, borders, ascender, descender));
        table.addCell(makeCell("2-Middle", Element.ALIGN_MIDDLE, Element.ALIGN_LEFT, tableFont, leading, padding, borders, ascender, descender));
        table.addCell(makeCell("3-Bottom", Element.ALIGN_BOTTOM, Element.ALIGN_LEFT, tableFont, leading, padding, borders, ascender, descender));
        table.addCell(makeCell("4-Has a y", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, borders, ascender, descender));
        table.addCell(makeCell("5-Abcdy", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, borders, ascender, descender));
        table.addCell(makeCell("6-Abcdy", Element.ALIGN_MIDDLE, Element.ALIGN_LEFT, tableFont, leading, padding, borders, ascender, descender));
        table.addCell(makeCell("7-Abcdy", Element.ALIGN_BOTTOM, Element.ALIGN_LEFT, tableFont, leading, padding, borders, ascender, descender));
        table.addCell(makeCell("8-This\nis\na little\ntaller", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, borders, ascender, descender));
        document.add(table);
    }

    private static PdfPCell makeCell(String text, int vAlignment, int hAlignment, Font font, float leading, float padding, Rectangle borders, boolean ascender, boolean descender) {
        Paragraph p = new Paragraph(text, font);
        p.setLeading(leading);
        PdfPCell cell = new PdfPCell(p);
        cell.setLeading(leading, 0);
        cell.setVerticalAlignment(vAlignment);
        cell.setHorizontalAlignment(hAlignment);
        cell.cloneNonPositionParameters(borders);
        cell.setUseAscender(ascender);
        cell.setUseDescender(descender);
        cell.setUseBorderPadding(true);
        cell.setPadding(padding);
        return cell;
    }
}
