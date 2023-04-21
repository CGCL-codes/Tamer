package org.akrogen.tkui.dom.xul.internal.dom.tabboxes;

import org.akrogen.tkui.dom.xul.dom.tabboxes.Tabpanel;
import org.akrogen.tkui.dom.xul.dom.tabboxes.Tabpanels;
import org.akrogen.tkui.dom.xul.internal.dom.XULElementImpl;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XUL {@link Tabpanels} implementation.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 * @see http://developer.mozilla.org/en/docs/XUL:tabpanels
 */
public class TabpanelsImpl extends XULElementImpl implements Tabpanels {

    public TabpanelsImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName, String localName) throws DOMException {
        super(ownerDocument, namespaceURI, qualifiedName, localName);
    }

    public String getUIElementId() {
        return null;
    }

    public int getIndexOfItem(Tabpanel item) {
        int index = -1;
        NodeList nodes = super.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Tabpanel) index++;
            if (node.equals(item)) return index;
        }
        return index;
    }

    public Tabpanel getItemAtIndex(int index) {
        int j = -1;
        NodeList nodes = super.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Tabpanel) j++;
            if (index == j) return (Tabpanel) node;
        }
        return null;
    }
}
