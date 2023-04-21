package info.kmm.retriever.collector.handler;

import info.kmm.retriever.collector.handler.Document.DocumentType;
import java.net.URLConnection;

/**
 * TODO Write javadoc.
 */
public class JsContentHandler extends PlainTextContentHandler {

    /**
	 * TODO Write javadoc.
	 * 
	 * @param urlConnection .
	 * @return .
	 */
    public Object getContent(final URLConnection urlConnection) {
        final Document document = (Document) super.getContent(urlConnection);
        document.setType(DocumentType.js);
        return document;
    }
}
