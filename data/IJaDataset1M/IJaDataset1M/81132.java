package com.lowagie.examples.objects.images;

import java.io.FileOutputStream;
import java.io.IOException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Rotating images.
 */
public class Rotating {

    /**
     * Rotating images.
     * @param args No arguments needed
     */
    public static void main(String[] args) {
        System.out.println("Rotating an Image");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("rotating.pdf"));
            document.open();
            Image jpg = Image.getInstance("otsoe.jpg");
            jpg.setAlignment(Image.MIDDLE);
            jpg.setRotation((float) Math.PI / 6);
            document.add(new Paragraph("rotate 30 degrees"));
            document.add(jpg);
            document.newPage();
            jpg.setRotation((float) Math.PI / 4);
            document.add(new Paragraph("rotate 45 degrees"));
            document.add(jpg);
            document.newPage();
            jpg.setRotation((float) Math.PI / 2);
            document.add(new Paragraph("rotate pi/2 radians"));
            document.add(jpg);
            document.newPage();
            jpg.setRotation((float) (Math.PI * 0.75));
            document.add(new Paragraph("rotate 135 degrees"));
            document.add(jpg);
            document.newPage();
            jpg.setRotation((float) Math.PI);
            document.add(new Paragraph("rotate pi radians"));
            document.add(jpg);
            document.newPage();
            jpg.setRotation((float) (2.0 * Math.PI));
            document.add(new Paragraph("rotate 2 x pi radians"));
            document.add(jpg);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
}
