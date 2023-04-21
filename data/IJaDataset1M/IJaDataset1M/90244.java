package com.lowagie.examples.general.faq;

import java.io.FileOutputStream;
import java.io.IOException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Demonstrates the use of getVersion.
 * @author blowagie
 */
public class iTextVersion {

    /**
     * Creates a PDF document and shows the iText version.
     * @param args no arguments needed here
     */
    public static void main(String[] args) {
        System.out.println("iText version " + Document.getVersion());
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("version.pdf"));
            document.open();
            document.add(new Paragraph("This page was made using " + Document.getVersion()));
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
}
