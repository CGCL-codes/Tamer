package net.sourceforge.retriever.collector.handler;

import java.net.URLConnection;
import net.sourceforge.retriever.collector.handler.Document.DocumentType;

/**
 * TODO Write javadoc.
 */
class PascalContentHandler extends PlainTextContentHandler {

    /**
	 * TODO Write javadoc.
	 * 
	 * @param urlConnection .
	 * @return .
	 */
    public Object getContent(final URLConnection urlConnection) {
        final Document document = (Document) super.getContent(urlConnection);
        document.setType(DocumentType.pascal);
        return document;
    }
}
