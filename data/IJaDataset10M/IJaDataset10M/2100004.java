package com.google.gwt.user.client.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Element;

/**
 * Native implementation used by {@link WindowImpl} and {@link DOMImpl} to
 * access the appropriate documentRoot element, which varies based on the render
 * mode of the browser.
 * 
 * @deprecated use the direct methods provided in {@link Document} instead
 */
@Deprecated
public class DocumentRootImpl {

    protected static Element documentRoot = ((DocumentRootImpl) GWT.create(DocumentRootImpl.class)).getDocumentRoot();

    protected Element getDocumentRoot() {
        Document doc = Document.get();
        return (doc.isCSS1Compat() ? doc.getDocumentElement() : doc.getBody()).cast();
    }
}
