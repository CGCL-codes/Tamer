package de.fmui.cmis.fileshare.bindings.atompub;

import java.io.IOException;
import java.io.OutputStream;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class CMISDocument extends CMISDocumentBase {

    private Document fDoc;

    public CMISDocument() {
        fDoc = new Document();
    }

    /**
	 * Returns the Document object.
	 */
    public Document getDocument() {
        return fDoc;
    }

    public void setRootElement(Element root) {
        if (root == null) {
            throw new IllegalArgumentException("Root must not be null.");
        }
        fDoc.setRootElement(root);
        root.addNamespaceDeclaration(CMISAtomPubCommons.NAMESPACE_ATOM);
        root.addNamespaceDeclaration(CMISAtomPubCommons.NAMESPACE_CMIS);
        root.addNamespaceDeclaration(CMISAtomPubCommons.NAMESPACE_RESTATOM);
        root.addNamespaceDeclaration(CMISAtomPubCommons.NAMESPACE_APP);
    }

    /**
	 * Writes the XML to a stream.
	 */
    public void write(OutputStream out) throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(Format.getPrettyFormat());
        outputter.output(fDoc, out);
    }
}
