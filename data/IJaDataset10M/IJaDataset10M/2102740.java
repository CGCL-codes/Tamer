package org.jxul.swing;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import org.jxul.XulTab;
import org.jxul.XulTabs;
import org.jxul.xml.AbstractXulElement;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * A XUL Tab Box.
 */
public class XulTabsImpl extends AbstractXulElement implements XulTabs {

    /**
	 * @author Will Etson
	 */
    public static class Factory implements org.jxul.ElementFactory {

        /**
		 * @see org.jxul.ElementFactory#create(org.w3c.dom.Document, String, java.lang.String, String)
		 */
        public Element create(Document ownerDocument, String namespaceURI, String prefix, String localName) {
            return new XulTabsImpl(ownerDocument, prefix);
        }
    }

    private List tabs = new ArrayList();

    /**
	 * @param ownerDocument
	 * @param prefix
	 */
    public XulTabsImpl(Document ownerDocument, String prefix) {
        super(ownerDocument, prefix, ELEMENT);
    }

    /**
	 * @see org.w3c.dom.Node#appendChild(org.w3c.dom.Node)
	 */
    public Node appendChild(Node node) throws DOMException {
        if (node instanceof XulTabImpl) {
            XulTabImpl tab = (XulTabImpl) node;
            tabs.add(tab);
            XulTabBoxImpl tabBox = (XulTabBoxImpl) parent;
            JTabbedPane pane = (JTabbedPane) tabBox.getComponent();
            pane.add(tab.getLabel(), new JPanel());
        }
        return super.appendChild(node);
    }

    /**
	 * @see org.w3c.dom.Element#setAttributeNode(org.w3c.dom.Attr)
	 */
    public Attr setAttributeNode(Attr attr) throws DOMException {
        if (ATTR_ALIGN.equals(attr.getName())) {
            XulTabBoxImpl tabBox = (XulTabBoxImpl) parent;
            JTabbedPane pane = (JTabbedPane) tabBox.getComponent();
            if (ATTR_ALIGN_HORIZONTAL.equals(attr.getValue())) {
                pane.setTabPlacement(SwingConstants.TOP);
            } else if (getAlign().equals(ATTR_ALIGN_VERTICAL)) {
                pane.setTabPlacement(SwingConstants.LEFT);
            }
        }
        return super.setAttributeNode(attr);
    }

    /**
	 * @param selectedTab
	 * @return the index
	 */
    public int indexOf(XulTab selectedTab) {
        return this.tabs.indexOf(selectedTab);
    }

    /**
	 * @param selectedIndex
	 * @return the tab
	 */
    public XulTab getTab(int selectedIndex) {
        return (XulTab) this.tabs.get(selectedIndex);
    }
}
