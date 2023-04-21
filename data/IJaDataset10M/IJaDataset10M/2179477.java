package org.frontburner.xml;

import java.io.*;
import org.frontburner.core.*;
import org.jdom.*;
import org.jdom.output.XMLOutputter;

/**
 *
 *
 * @author Marc Hedlund &lt;marc@precipice.org&gt;
 * @version $Revision: 1.6 $
 */
public class ListOutputter {

    private Document doc = null;

    public ListOutputter(ToDoList list) {
        this.doc = new Document(list.buildXMLElement());
    }

    public void output(OutputStream out) throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        outputter.setNewlines(true);
        outputter.setIndent(true);
        outputter.output(doc, out);
        out.flush();
        out.close();
    }
}
