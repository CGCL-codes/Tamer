package info.kmm.retriever.collector.handler;

import info.kmm.retriever.collector.handler.Document.DocumentType;
import java.io.IOException;
import java.net.URLConnection;

/**
 * TODO Write javadoc.
 */
public class QuickTimeContentHandler extends AbstractHandler {

    /**
	 * TODO Write javadoc.
	 * 
	 * @param urlc .
	 * @return .
	 * @throws IOException .
	 */
    @Override
    public Object getContent(final URLConnection urlc) throws IOException {
        final Document document = new Document(urlc.getURL().toString(), DocumentType.qt);
        document.setTitle(super.getFileName(urlc.getURL().getFile()));
        return document;
    }
}
