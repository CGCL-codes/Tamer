package org.jxul.swing;

import org.jxul.XulTabPanel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Will Etson
 */
public class XulTabPanelImpl extends XulBoxImpl implements XulTabPanel {

    /**
	 * @author Will Etson
	 */
    public static class Factory implements org.jxul.ElementFactory {

        /**
		 * @see org.jxul.ElementFactory#create(org.w3c.dom.Document, String, java.lang.String, String)
		 */
        public Element create(Document ownerDocument, String namespaceURI, String prefix, String localName) {
            return new XulTabPanelImpl(ownerDocument, prefix);
        }
    }

    /**
	 * @param ownerDocument
	 * @param prefix
	 */
    public XulTabPanelImpl(Document ownerDocument, String prefix) {
        super(ownerDocument, prefix, XulTabPanel.ELEMENT);
    }
}
