package org.xorm.datastore.xml;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Wraps a W3C Document and allows transactional access.
 *
 * @author Dan Checkoway
 */
public class W3CDocumentHolder implements DocumentHolder {

    private DocumentBuilder docBuilder;

    private URL url;

    private Document document;

    public W3CDocumentHolder() {
    }

    public void setURL(URL url) {
        this.url = url;
        try {
            docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = docBuilder.parse(url.openStream());
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a cloned copy of the document.
     */
    public Object checkout() {
        Document copy = docBuilder.newDocument();
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(document), new DOMResult(copy));
        } catch (javax.xml.transform.TransformerException e) {
            e.printStackTrace();
        }
        return copy;
    }

    /**
     * Accepts the changes from the document.  If possible, rewrites
     * the content.  Changes are synchronized but not checked; if two
     * concurrent threads make different changes, the last one to call
     * checkin() will win.  This effectively gives the process a
     * transaction isolation level equivalent to TRANSACTION_READ_COMMITTED.
     */
    public void checkin(Object _document) {
        this.document = (Document) _document;
        synchronized (url) {
            OutputStream outputStream = null;
            try {
                if ("file".equals(url.getProtocol())) {
                    outputStream = new FileOutputStream(url.getFile());
                } else {
                    URLConnection connection = url.openConnection();
                    connection.setDoOutput(true);
                    outputStream = connection.getOutputStream();
                }
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(new DOMSource(document), new StreamResult(outputStream));
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (javax.xml.transform.TransformerException e) {
                e.printStackTrace();
            }
        }
    }
}
