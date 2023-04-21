package net.sourceforge.retriever.collector.handler;

import java.io.IOException;
import java.net.URLConnection;
import net.sourceforge.retriever.collector.handler.Document.DocumentType;

/**
 * TODO Write javadoc.
 */
class Mp3ContentHandler extends AbstractHandler {

    /**
	 * TODO Write javadoc.
	 * 
	 * @param urlc .
	 * @return .
	 * @throws IOException .
	 */
    @Override
    public Object getContent(final URLConnection urlc) throws IOException {
        final Document document = new Document(urlc.getURL().toString(), DocumentType.mp3);
        document.setTitle(super.getFileName(urlc.getURL().getFile()));
        return document;
    }
}
