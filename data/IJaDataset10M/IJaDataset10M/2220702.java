package com.lowagie.examples.general;

import java.io.FileOutputStream;
import java.io.IOException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Generates a simple 'Hello World' PDF file.
 * 
 * @author blowagie
 */
public class HelloWorld {

    /**
	 * Generates a PDF file with the text 'Hello World'
	 * 
	 * @param args no arguments needed here
	 */
    public static void main(String[] args) {
        System.out.println("Hello World");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("HelloWorld.pdf"));
            document.open();
            document.add(new Paragraph("Hello World"));
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
}
