package org.eml.MMAX2.discourse;

import java.io.File;
import javax.swing.JOptionPane;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.parsers.DOMParser;
import org.xml.sax.InputSource;

/** Helper class for loading discourse element XML files and converting them to DiscourseElement objects. */
public class DiscourseElementFileLoader {

    /** Name of the discourse element xml file currently loaded. */
    protected String deFileName = "";

    /** Type of discourse elements in currently loaded file: word, gesture, keyaction. */
    protected String type = "";

    /** DOM representation of the currently loaded discourse element file. */
    protected DocumentImpl deDOM = null;

    /** Creates new DiscourseElementFileLoader */
    public DiscourseElementFileLoader() {
    }

    /** Main class for testing purposes. Usage: DiscourseElementFileLoader [words.xml|gestures.xml|keyactions.xml] */
    public static void main(String args[]) {
        if (args.length != 1) {
            System.out.println("Usage: DiscourseElementFileLoader [words.xml|gestures.xml|keyactions.xml]");
            System.exit(0);
        }
        DiscourseElementFileLoader defl = new DiscourseElementFileLoader();
        defl.load(args[0]);
    }

    /** Load and parse discourse element file of name fileName. */
    public final void load(String fileName) {
        File file = new File(fileName);
        deFileName = file.getAbsolutePath();
        DOMParser parser = new DOMParser();
        deDOM = null;
        try {
            parser.setFeature("http://xml.org/sax/features/validation", false);
        } catch (org.xml.sax.SAXNotRecognizedException ex) {
            ex.printStackTrace();
            return;
        } catch (org.xml.sax.SAXNotSupportedException ex) {
            ex.printStackTrace();
            return;
        }
        try {
            parser.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", false);
        } catch (org.xml.sax.SAXNotRecognizedException ex) {
            ex.printStackTrace();
            return;
        } catch (org.xml.sax.SAXNotSupportedException ex) {
            ex.printStackTrace();
            return;
        }
        try {
            parser.parse(new InputSource(new File(fileName).toURI().toString()));
        } catch (org.xml.sax.SAXParseException exception) {
            String error = "Line: " + exception.getLineNumber() + " Column: " + exception.getColumnNumber() + "\n" + exception.toString();
            JOptionPane.showMessageDialog(null, error, "DiscourseElementFileLoader: " + fileName, JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (org.xml.sax.SAXException exception) {
            String error = exception.toString();
            JOptionPane.showMessageDialog(null, error, "DiscourseElementFileLoader: " + fileName, JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (java.io.IOException exception) {
            String error = exception.toString();
            JOptionPane.showMessageDialog(null, error, "DiscourseElementFileLoader: " + fileName, JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        deDOM = (DocumentImpl) parser.getDocument();
    }

    /** Get the entire DOM representation of the currently loaded discourse elements file. */
    public final DocumentImpl getDOM() {
        return deDOM;
    }
}
