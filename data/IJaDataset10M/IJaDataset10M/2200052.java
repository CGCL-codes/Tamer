package com.lowagie.examples.rtf.features.styles;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.style.RtfParagraphStyle;

/**
 * The ChangingStylesheets example shows how to change the properties of
 * the predefined stylesheets.
 * 
 * @version $Revision: 3373 $
 * @author Mark Hall (Mark.Hall@mail.room3b.eu)
 */
public class ChangingStylesheets {

    /**
     * Changing paragraph stylesheets properties.
     * 
     * @param args Unused
     */
    public static void main(String[] args) {
        System.out.println("Demonstrates using changing the properties of paragraph stylesheets");
        try {
            Document document = new Document();
            RtfWriter2.getInstance(document, new FileOutputStream("ChangingStylesheets.rtf"));
            RtfParagraphStyle.STYLE_NORMAL.setFontName("Times New Roman");
            RtfParagraphStyle.STYLE_HEADING_1.setColor(Color.BLUE);
            RtfParagraphStyle.STYLE_HEADING_2.setFontName("Arial");
            RtfParagraphStyle.STYLE_HEADING_2.setSize(12);
            document.open();
            document.add(new Paragraph("This is a heading level 1", RtfParagraphStyle.STYLE_HEADING_1));
            document.add(new Paragraph("This is a heading level 2", RtfParagraphStyle.STYLE_HEADING_2));
            document.add(new Paragraph("Just some text that is formatted " + "in the default style.", RtfParagraphStyle.STYLE_NORMAL));
            document.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (DocumentException de) {
            de.printStackTrace();
        }
    }
}
