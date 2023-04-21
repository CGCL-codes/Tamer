package org.zkoss.idom.impl;

import org.zkoss.idom.*;

/**
 * The iDOM's implementation of DOMImplementation.
 *
 * @author tomyeh
 */
public class DOMImplementation implements org.w3c.dom.DOMImplementation {

    /** DOM implementation singleton.
	 */
    public static final DOMImplementation THE = new DOMImplementation();

    protected DOMImplementation() {
    }

    public boolean hasFeature(String feature, String version) {
        return "XML".equalsIgnoreCase(feature) && (version == null || "2.0".equals(version) || "1.0".equals(version));
    }

    public org.w3c.dom.DocumentType createDocumentType(String tname, String publicId, String systemId) {
        return new DocType(tname, publicId, systemId);
    }

    public org.w3c.dom.Document createDocument(String nsURI, String tname, org.w3c.dom.DocumentType docType) {
        return new Document(new Element(nsURI, tname), (DocType) docType);
    }

    public Object getFeature(String feature, String version) {
        return null;
    }
}
