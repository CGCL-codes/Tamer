package xbrowser.doc;

import xbrowser.doc.view.XDocumentView;

public final class XDocumentFactory {

    public static XDocument createNewDocument() {
        return new XDocumentView();
    }
}
