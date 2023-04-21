package net.sf.doolin.oxml.source;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * DOM extracted from an IO stream.
 * 
 * @author Damien Coraboeuf
 */
public class StreamOXMLSource extends AbstractOXMLSource {

    private InputStream stream;

    /**
	 * Constructs a source from a stream.
	 * 
	 * @param in
	 *            Stream to read
	 */
    public StreamOXMLSource(InputStream in) {
        this.stream = in;
    }

    /**
	 * Does nothing. This is the responsability of the one which has created the
	 * stream to close it.
	 * 
	 * @see net.sf.doolin.oxml.OXMLSource#close()
	 */
    public void close() throws IOException {
    }

    /**
	 * Internal protected method to close the stream.
	 * 
	 * @throws IOException
	 *             If the stream cannot be closed.
	 * 
	 */
    protected void closeStream() throws IOException {
        if (this.stream != null) {
            this.stream.close();
        }
    }

    public Document getSource() throws IOException {
        DocumentBuilder builder = getBuilder();
        try {
            return builder.parse(this.stream);
        } catch (SAXException e) {
            IOException ioex = new IOException("Cannot parse the XML stream");
            ioex.initCause(e);
            throw ioex;
        }
    }
}
