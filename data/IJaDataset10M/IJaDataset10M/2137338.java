package com.itextpdf.demo.talks;

import java.io.IOException;
import java.util.List;
import com.itextpdf.devoxx.helpers.MyObjectFactory;
import com.itextpdf.devoxx.pojos.Presentation;
import com.itextpdf.devoxx.pojos.ScheduleItem;
import com.itextpdf.devoxx.properties.MyFonts;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.draw.LineSeparator;

public class PresentationGuide2 extends PresentationGuide1 {

    public static final String OUTPUT = "demo/presentations2.pdf";

    public static void main(String args[]) throws IOException, DocumentException {
        new PresentationGuide2().createPdf(OUTPUT);
    }

    /**
	 * Gets the title bar.
	 * @throws IOException 
	 */
    public PdfPTable getTitleBar(ScheduleItem item, Presentation p, LineSeparator line, Font font_color_bold, Font font_color_italic) throws IOException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.addElement(MyObjectFactory.getRoomAndTime(item, MyFonts.BOLD));
        cell.addElement(line);
        if (p == null) {
            cell.addElement(new Paragraph("TBA", MyFonts.REGULAR));
        } else {
            Paragraph title = MyObjectFactory.formatTitle(p.getTitle(), font_color_bold);
            title.setSpacingBefore(4);
            cell.addElement(title);
            Paragraph speaker = MyObjectFactory.getAuthorParagraph(p.getSpeakers(), font_color_italic);
            cell.addElement(speaker);
            table.addCell(cell);
        }
        return table;
    }

    /**
	 * Gets the summary
	 * @throws IOException 
	 */
    public PdfPTable getSummary(Presentation p) throws IOException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.setSplitLate(false);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        List<Element> list = MyObjectFactory.formatText(p.getSummary(), MyFonts.REGULAR);
        for (Element element : list) {
            cell.addElement(element);
        }
        table.addCell(cell);
        return table;
    }
}
