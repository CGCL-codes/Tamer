package com.bluebrim.xml.impl.server.test;

import java.io.*;
import org.apache.crimson.parser.*;
import org.apache.crimson.tree.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * A first very simple testparser all in main().
 */
public class CoBasicXmlViewer {

    /**
 * BasicXmlViewer constructor comment.
 */
    public CoBasicXmlViewer() {
        super();
    }

    /**
  * Opens a XML-file, walks the tree and writes nodenames on System.out.
  *   
  */
    public static void main(java.lang.String[] argv) {
        if (argv.length != 1) {
            System.err.println("Usage: cmd filename");
            System.exit(1);
        }
        try {
            InputSource input = Resolver.createInputSource(new File(argv[0]));
            XmlDocument doc = XmlDocument.createXmlDocument(input, true);
            TreeWalker walker = new TreeWalker(doc.getDocumentElement());
            Node node = walker.getCurrent();
            while (node != null) {
                node = walker.getNext();
            }
        } catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println("   " + err.getMessage());
        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(0);
    }
}
