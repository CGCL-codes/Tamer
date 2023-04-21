package org.orbeon.oxf.processor.serializer;

import org.orbeon.oxf.common.OXFException;
import org.orbeon.oxf.xml.NamespaceCleanupContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * A forwarding content handler that flushed the output when receiving a
 * given processing instruction.
 *
 * Also clean invalid XML 1.0 namespace declarations if needed.
 */
public class SerializerContentHandler extends NamespaceCleanupContentHandler {

    private Writer writer;

    private OutputStream os;

    public SerializerContentHandler(ContentHandler contentHandler, boolean serializeXML11) {
        super(contentHandler, serializeXML11);
    }

    public SerializerContentHandler(ContentHandler contentHandler, Writer writer, boolean serializeXML11) {
        this(contentHandler, serializeXML11);
        this.writer = writer;
    }

    public SerializerContentHandler(ContentHandler contentHandler, OutputStream os, boolean serializeXML11) {
        this(contentHandler, serializeXML11);
        this.os = os;
    }

    public void processingInstruction(String target, String data) throws SAXException {
        if ("oxf-serializer".equals(target) && "flush".equals(data)) {
            try {
                if (writer != null) writer.flush();
                if (os != null) os.flush();
            } catch (IOException e) {
                throw new OXFException(e);
            }
        }
        super.processingInstruction(target, data);
    }
}
