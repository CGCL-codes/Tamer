package com.itextpdf.demo.speakers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class TalksPresentations {

    public static final String OUTPUT = "demo/guide_numbered.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        new SpeakersForm().createPdf();
        PresentationData data = new PresentationData();
        data.createPdf(PresentationData.OUTPUT);
        Map<String, ArrayList<Integer>> pageNumbers = data.getRegisteredPageNumbers();
        new SpeakersPages().manipulatePdf(pageNumbers);
        PdfReader reader = new PdfReader("resources/odd.pdf");
        Document document = new Document(reader.getPageSize(1));
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(OUTPUT));
        BackgroundAndPageNumber event = new BackgroundAndPageNumber();
        writer.setPageEvent(event);
        TabColoring tabs = new TabColoring();
        writer.setPageEvent(tabs);
        writer.setViewerPreferences(PdfWriter.PageLayoutTwoPageRight);
        document.open();
        PdfContentByte canvas = writer.getDirectContent();
        Font font = new Font(FontFamily.HELVETICA, 16);
        Image page;
        boolean odd;
        tabs.setTabs("speakers");
        reader = new PdfReader(SpeakersPages.OUTPUT);
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            odd = i % 2 == 1;
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("SPEAKERS", font), event.getTitleOffset(odd)[0], event.getTitleOffset(odd)[1], 0);
            page = Image.getInstance(writer.getImportedPage(reader, i));
            page.scaleToFit(event.getBodyWidth(odd), event.getBodyHeight(odd));
            page.setAbsolutePosition(event.getBodyOffset(odd)[0], event.getBodyOffset(odd)[1]);
            canvas.addImage(page);
            document.newPage();
        }
        reader = new PdfReader(PresentationData.OUTPUT);
        tabs.setTabs("university");
        Phrase title = null;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            odd = i % 2 == 1;
            title = data.getTitle(i, font, title);
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, title, event.getTitleOffset(odd)[0], event.getTitleOffset(odd)[1], 0);
            page = Image.getInstance(writer.getImportedPage(reader, i));
            page.scaleToFit(event.getBodyWidth(odd), event.getBodyHeight(odd));
            page.setAbsolutePosition(event.getBodyOffset(odd)[0], event.getBodyOffset(odd)[1]);
            canvas.addImage(page);
            document.newPage();
        }
        document.close();
    }
}
