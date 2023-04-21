package org.fit.cssbox.demo;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.fit.cssbox.css.CSSNorm;
import org.fit.cssbox.css.DOMAnalyzer;
import org.fit.cssbox.layout.Box;
import org.fit.cssbox.layout.BrowserCanvas;
import org.fit.cssbox.layout.ElementBox;
import org.fit.cssbox.layout.ReplacedImage;
import org.fit.cssbox.layout.TextBox;
import org.w3c.dom.Document;

/**
 * This demo shows how the rendered box tree can be accessed. It renders the document 
 * and prints the list of text boxes together with their positions on the page.
 * 
 * @author burgetr
 */
public class TextBoxes {

    /**
     * Recursively prints the text boxes from the specified tree
     */
    private static void printTextBoxes(Box root) {
        if (root instanceof TextBox) {
            TextBox text = (TextBox) root;
            System.out.println("x=" + text.getAbsoluteBounds().x + " y=" + text.getAbsoluteBounds().y + " text=" + text.getText());
        } else if (root instanceof ElementBox) {
            ElementBox el = (ElementBox) root;
            for (int i = el.getStartChild(); i < el.getEndChild(); i++) printTextBoxes(el.getSubBox(i));
        }
    }

    /**
     * main method
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: TextBoxes <url>");
            System.exit(0);
        }
        try {
            URL url = new URL(args[0]);
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            DOMSource parser = new DOMSource(is);
            Document doc = parser.parse();
            DOMAnalyzer da = new DOMAnalyzer(doc, url);
            da.attributesToStyles();
            da.addStyleSheet(null, CSSNorm.stdStyleSheet());
            da.addStyleSheet(null, CSSNorm.userStyleSheet());
            da.getStyleSheets();
            ReplacedImage.setLoadImages(false);
            BrowserCanvas browser = new BrowserCanvas(da.getRoot(), da, new java.awt.Dimension(1000, 600), url);
            printTextBoxes(browser.getViewport());
            is.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
