package com.bluebrim.xml.impl.server.test;

import java.io.*;
import org.apache.crimson.tree.*;
import org.w3c.dom.*;

public class CoXmlTestExample {

    /**
 * Insert the method's description here.
 * Creation date: (9/6/99 10:45:16 AM)
 */
    private static void doClones(Writer out, ElementNode root) throws IOException {
        out.write("ORIGINAL:\t");
        root.write(out);
        out.write("\n");
        out.flush();
        {
            ElementNode clone1;
            clone1 = (ElementNode) root.cloneNode(false);
            out.write("SHALLOW CLONE:\t");
            clone1.write(out);
            out.write("\n");
            out.flush();
        }
        {
            ElementNode clone2;
            clone2 = (ElementNode) root.cloneNode(true);
            out.write("DEEP CLONE:\t");
            clone2.write(out);
            out.write("\n");
            out.flush();
        }
    }

    /**
 * Insert the method's description here.
 * Creation date: (9/6/99 10:44:20 AM)
 */
    public static void main(String argv[]) throws IOException, DOMException {
        XmlDocument doc = new XmlDocument();
        ElementNode root = (ElementNode) doc.createElement("root");
        Attr tmp;
        Writer out = new OutputStreamWriter(System.out);
        doc.appendChild(root);
        out.write("No children ...\n");
        doClones(out, root);
        out.write("\n");
        root.appendChild(doc.createElement("header"));
        root.appendChild(doc.createTextNode("\n  some data is text\n  "));
        root.appendChild(doc.createElement("footer"));
        Text text;
        text = doc.createTextNode("\n");
        root.insertBefore(text, root.item(0));
        text = doc.createTextNode("\n");
        root.appendChild(text);
        out.write("... mixed content model ...\n");
        doClones(out, root);
        out.write("\n");
        tmp = doc.createAttribute("attr1");
        tmp.setNodeValue("value of attribute 1");
        root.getAttributes().setNamedItem(tmp);
        root.setAttribute("attr2", "another string");
        out.write("... and attributes...\n");
        doClones(out, root);
        out.write("\n");
        doc.setDoctype(null, "http://www.example.com/xml/dtd1.dtd", null);
        doc.write(out);
        out.write("\n");
        out.flush();
        System.exit(0);
    }
}
