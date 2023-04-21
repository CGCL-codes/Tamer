package net.sf.exorcist.tree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import org.w3c.dom.Document;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import net.sf.exorcist.api.AttachmentStream;
import net.sf.exorcist.api.ContentException;
import net.sf.exorcist.api.ContentTree;

public class MultiContentTree implements ContentTree {

    private static final String MULTI = "multi";

    public static final String NAMESPACE = "http://exorcist.sf.net/ns/2005/multi";

    private ContentTree tree;

    private ContentHandler handler;

    public MultiContentTree(ContentTree tree) {
        this.tree = tree;
    }

    public void init() throws ContentException {
        handler = null;
    }

    public void done() throws ContentException {
        try {
            if (handler != null) {
                handler.endElement(NAMESPACE, MULTI, MULTI);
                handler.endPrefixMapping("");
                handler.endDocument();
            }
        } catch (SAXException e) {
            throw new ContentException("Error creating content document", e);
        }
    }

    public InputStream getContentStream() throws ContentException {
        return tree.getContentStream();
    }

    public void getContentByOutputStream(OutputStream stream) throws ContentException, IOException {
        tree.getContentByOutputStream(stream);
    }

    public Document getContentDocument() throws ContentException {
        return tree.getContentDocument();
    }

    public void getContentByContentHandler(ContentHandler handler) throws ContentException {
        tree.getContentByContentHandler(handler);
    }

    private void startContent() throws ContentException {
        try {
            if (handler == null) {
                handler = tree.setContentByContentHandler();
                handler.startDocument();
                handler.startPrefixMapping("", NAMESPACE);
                handler.startElement(NAMESPACE, MULTI, MULTI, new AttributesImpl());
            }
        } catch (SAXException e) {
            throw new ContentException("Error creating content document", e);
        }
    }

    public void setContentStream(InputStream stream) throws ContentException, IOException {
        startContent();
        try {
            ContentHandler handler = setContentByContentHandler();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(stream, new ContentHandlerWrapper(handler));
        } catch (SAXException e) {
            throw new ContentException("Error parsing content stream", e);
        } catch (ParserConfigurationException e) {
            throw new ContentException("Error creating content parser", e);
        }
    }

    public OutputStream setContentByOutputStream() throws ContentException {
        startContent();
        try {
            final File file = File.createTempFile("exorcist", ".xml");
            return new FileOutputStream(file) {

                public void close() throws IOException {
                    try {
                        super.close();
                        InputStream stream = new FileInputStream(file);
                        try {
                            setContentStream(stream);
                        } catch (ContentException e) {
                            throw new IOException(e.getMessage());
                        } finally {
                            stream.close();
                        }
                    } finally {
                        file.delete();
                    }
                }
            };
        } catch (IOException e) {
            throw new ContentException("Error creating content stream", e);
        }
    }

    public void setContentDocument(Document document) throws ContentException {
        startContent();
        try {
            ContentHandler handler = setContentByContentHandler();
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(new DOMSource(document), new SAXResult(handler));
        } catch (TransformerConfigurationException e) {
            throw new ContentException("Error creating content transformer", e);
        } catch (TransformerException e) {
            throw new ContentException("Error transforming content document", e);
        }
    }

    public ContentHandler setContentByContentHandler() throws ContentException {
        startContent();
        return new MultiContentHandler(handler);
    }

    public boolean containsAttachment(String hash) throws ContentException {
        return tree.containsAttachment(hash);
    }

    public Set getAttachmentHashes() throws ContentException {
        return tree.getAttachmentHashes();
    }

    public InputStream getAttachmentStream(String hash) throws IllegalArgumentException, ContentException {
        return tree.getAttachmentStream(hash);
    }

    public void getAttachmentByOutputStream(String hash, OutputStream stream) throws IllegalArgumentException, ContentException, IOException {
        tree.getAttachmentByOutputStream(hash, stream);
    }

    public String setAttachmentStream(InputStream stream) throws ContentException, IOException {
        return tree.setAttachmentStream(stream);
    }

    public AttachmentStream setAttachmentByOutputStream() throws ContentException {
        return tree.setAttachmentByOutputStream();
    }

    public String setAttachmentFile(File file) throws ContentException, IOException {
        return tree.setAttachmentFile(file);
    }

    public void setAttachmentFile(String hash, File file) throws ContentException {
        tree.setAttachmentFile(hash, file);
    }

    public void removeAttachment(String hash) throws IllegalArgumentException, ContentException {
        tree.removeAttachment(hash);
    }
}
