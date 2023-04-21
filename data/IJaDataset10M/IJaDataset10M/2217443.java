package org.equanda.util.xml;

import org.equanda.util.xml.tree.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * test program to check processing of XML files
 *
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
public class Test {

    public Test() {
    }

    public static void main(String[] args) {
        final String filename = "test.xml";
        Document doc = null;
        try {
            XMLParser parser = new XMLParser(true, null, null);
            parser.setErrorHandler(new ErrorHandler() {

                public void error(SAXParseException x) throws SAXException {
                    throw x;
                }

                public void fatalError(SAXParseException x) throws SAXException {
                    throw x;
                }

                public void warning(SAXParseException x) throws SAXException {
                    System.err.println("parse format file warning file " + filename + " line " + x.getLineNumber() + " col " + x.getColumnNumber() + "\n" + x.toString());
                }
            });
            doc = parser.parse(filename);
            if (doc != null) System.out.println(doc);
        } catch (SAXParseException e) {
            Exception x = e.getException();
            System.err.println("parse format file file " + filename + " line " + e.getLineNumber() + " col " + e.getColumnNumber() + "\n" + (x == null ? "" : x.toString() + "\n") + e.toString());
        } catch (SAXException e) {
            Exception x = e.getException();
            System.err.println("parse format file " + filename + " " + x.toString() + "\n" + e.toString());
        } catch (Throwable t) {
            System.err.println("parse format file " + filename + " " + t.toString());
        }
        System.exit(0);
    }
}
