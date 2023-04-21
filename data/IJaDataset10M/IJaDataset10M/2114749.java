package net.sourceforge.retriever.collector.handler;

import java.net.URLConnection;
import java.util.Date;
import java.util.StringTokenizer;
import net.sourceforge.retriever.collector.handler.Document.DocumentType;

/**
 * Extracts information from plain text files, filling and returning a
 * <code>info.iskmm.handler.entity.Document</code> object with the
 * extracted data.
 */
class PlainTextContentHandler extends AbstractHandler {

    /**
     * Retrieves an <code>info.iskmm.handler.entity.Document</code> 
     * object, containing the content retrieved from the url.
     * 
     * @param urlConnection the url to the resource.
     * @return a <code>Document</code> object, with the content 
     *         retrieved using the url.
     */
    @Override
    public Object getContent(final URLConnection urlConnection) {
        try {
            final StringBuilder content = new StringBuilder();
            this.fillStringBuilderWithInputStreamContent(urlConnection.getInputStream(), content);
            final Document doc = new Document(urlConnection.getURL().toString(), DocumentType.txt);
            doc.setContent(content.toString());
            doc.setTitle(this.getNameFromPath(urlConnection.getURL().toString()));
            doc.setLength(urlConnection.getContentLength());
            doc.setModificationDate(new Date(urlConnection.getLastModified()));
            doc.setCreationDate(new Date(urlConnection.getDate()));
            return doc;
        } catch (final Exception e) {
            super.getLogger().severe("Exception trying to read the content of the txt " + urlConnection.getURL().toString() + "\nError message: " + e.getMessage() + "\nError cause: " + e.getCause());
            return null;
        } finally {
            super.closeStreamInsideUrlConnection(urlConnection);
        }
    }

    private String getNameFromPath(final String path) {
        String name = "";
        final StringTokenizer pathTokens = new StringTokenizer(path, "/");
        while (pathTokens.hasMoreTokens()) {
            name = pathTokens.nextToken();
        }
        return name;
    }
}
