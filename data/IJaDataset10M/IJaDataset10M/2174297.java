package net.sf.clairv.index.builder.impl;

import java.io.InputStream;
import net.sf.clairv.index.builder.DocumentBuilder;
import net.sf.clairv.index.builder.DocumentHandlerException;
import net.sf.clairv.index.document.Document;
import net.sf.clairv.index.document.IndexOption;
import net.sf.clairv.index.document.StoreOption;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.tidy.Tidy;

/**
 * @author qiuyin
 *
 */
public class JTidyHtmlBuilder implements DocumentBuilder {

    public void buildDocument(InputStream is, Document doc) throws DocumentHandlerException {
        Tidy tidy = new Tidy();
        tidy.setQuiet(true);
        tidy.setShowWarnings(false);
        org.w3c.dom.Document root = tidy.parseDOM(is, null);
        Element rawDoc = root.getDocumentElement();
        String title = getTitle(rawDoc);
        String body = getBody(rawDoc);
        if ((title != null) && (!title.equals(""))) {
            doc.addField("title", title, StoreOption.YES, IndexOption.TOKENIZED);
        }
        if ((body != null) && (!body.equals(""))) {
            doc.addField("body", body, StoreOption.COMPRESS, IndexOption.TOKENIZED);
        }
    }

    /**
	 * Gets the title text of the HTML document.
	 * 
	 * @rawDoc the DOM Element to extract title Node from
	 * @return the title text
	 */
    protected String getTitle(Element rawDoc) {
        if (rawDoc == null) {
            return null;
        }
        String title = "";
        NodeList children = rawDoc.getElementsByTagName("title");
        if (children.getLength() > 0) {
            Element titleElement = ((Element) children.item(0));
            Text text = (Text) titleElement.getFirstChild();
            if (text != null) {
                title = text.getData();
            }
        }
        return title;
    }

    protected String getBody(Element rawDoc) {
        if (rawDoc == null) {
            return null;
        }
        String body = "";
        NodeList children = rawDoc.getElementsByTagName("body");
        if (children.getLength() > 0) {
            body = getText(children.item(0));
        }
        return body;
    }

    /**
	 * Extracts text from the DOM node.
	 * 
	 * @param node
	 *            a DOM node
	 * @return the text value of the node
	 */
    protected String getText(Node node) {
        NodeList children = node.getChildNodes();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            switch(child.getNodeType()) {
                case Node.ELEMENT_NODE:
                    sb.append(getText(child));
                    sb.append(" ");
                    break;
                case Node.TEXT_NODE:
                    sb.append(((Text) child).getData());
                    break;
            }
        }
        return sb.toString();
    }
}
