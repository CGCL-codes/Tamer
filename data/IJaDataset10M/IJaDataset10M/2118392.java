package org.akrogen.tkui.dom.xul.internal.dom.tabboxes;

import org.akrogen.core.xml.utils.DOMUtils;
import org.akrogen.tkui.dom.xul.XULConstants;
import org.akrogen.tkui.dom.xul.dom.tabboxes.Tab;
import org.akrogen.tkui.dom.xul.dom.tabboxes.Tabbox;
import org.akrogen.tkui.dom.xul.dom.tabboxes.Tabs;
import org.akrogen.tkui.dom.xul.internal.dom.XULElementImpl;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XUL {@link Tabs} implementation.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 * @see http://developer.mozilla.org/en/docs/XUL:tabs
 */
public class TabsImpl extends XULElementImpl implements Tabs {

    private Tabbox tabbox;

    public TabsImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName, String localName) throws DOMException {
        super(ownerDocument, namespaceURI, qualifiedName, localName);
    }

    public String getUIElementId() {
        return null;
    }

    public Tab appendItem(String label, String value) {
        Tab tab = createTab(label, value);
        super.appendChild(tab);
        return tab;
    }

    private Tab createTab(String label, String value) {
        Tab tab = (Tab) super.getOwnerDocument().createElementNS(XULConstants.XUL_NAMESPACE_URI, XULConstants.TAB_ELEMENT);
        tab.setLabel(label);
        tab.setValue(value);
        return tab;
    }

    public int getIndexOfItem(Tab item) {
        int index = -1;
        NodeList nodes = super.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Tab) index++;
            if (node.equals(item)) return index;
        }
        return index;
    }

    public Tab getItemAtIndex(int index) {
        int j = -1;
        NodeList nodes = super.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Tab) j++;
            if (index == j) return (Tab) node;
        }
        return null;
    }

    public Tab insertItemAt(int index, String label, String value) {
        Tab refTab = getItemAtIndex(index);
        if (refTab == null) return null;
        Tab tab = createTab(label, value);
        super.insertBefore(tab, refTab);
        return tab;
    }

    public Tab removeItemAt(int index) {
        Tab tab = getItemAtIndex(index);
        if (tab != null) {
            super.removeChild(tab);
        }
        return tab;
    }

    /**
	 * Return owner {@link Tabbox}.
	 * 
	 * @return
	 */
    protected Tabbox getTabbox() {
        if (tabbox == null) {
            tabbox = (Tabbox) DOMUtils.getFirstMatchingAncestor(this, XULConstants.XUL_NAMESPACE_URI, XULConstants.TABBOX_ELEMENT);
        }
        return tabbox;
    }
}
