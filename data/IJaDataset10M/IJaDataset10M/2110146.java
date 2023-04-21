package de.schlund.pfixcore.webservice.beans.metadata;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Iterator;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author mleidig@schlund.de
 */
public class DOMInit {

    static Logger LOG = Logger.getLogger(DOMInit.class);

    public static final String XMLNS_BEANMETADATA = "http://pustefix.sourceforge.net/beanmetadata";

    private Beans beans;

    public DOMInit() {
        beans = new Beans();
    }

    public DOMInit(Beans beans) {
        this.beans = beans;
    }

    public Beans getBeans() {
        return beans;
    }

    public void update(Document doc) throws DOMInitException {
        NodeList beanNodeList = doc.getElementsByTagName("bean");
        for (int i = 0; i < beanNodeList.getLength(); i++) {
            Element beanElem = (Element) beanNodeList.item(i);
            String val = getAttribute("class", beanElem);
            if (val == null) throw new DOMInitException("Missing attribute: /bean-metadata/bean[" + i + "]/@class");
            Bean bean = new Bean(val);
            if (beans.getBean(val) != null) LOG.warn("Override metadata for bean '" + val + "'.");
            beans.setBean(bean);
            val = getAttribute("exclude-by-default", beanElem);
            if (val != null && Boolean.parseBoolean(val)) bean.excludeByDefault();
            NodeList propNodeList = beanElem.getElementsByTagName("property");
            for (int j = 0; j < propNodeList.getLength(); j++) {
                Element propElem = (Element) propNodeList.item(j);
                val = getAttribute("name", propElem);
                if (val == null) throw new DOMInitException("Missing attribute: /bean-metadata/bean[" + i + "]/property[" + j + "]/@name");
                Property prop = new Property(val);
                bean.setProperty(prop);
                val = getAttribute("alias", propElem);
                if (val != null) prop.setAlias(val);
                val = getAttribute("exclude", propElem);
                if (val != null && Boolean.parseBoolean(val)) prop.exclude();
                val = getAttribute("include", propElem);
                if (val != null && Boolean.parseBoolean(val)) prop.include();
            }
        }
    }

    private String getAttribute(String name, Element element) {
        String val = element.getAttribute(name);
        if (val != null) {
            val = val.trim();
            if (val.equals("")) val = null;
        }
        return val;
    }

    public void update(URL metadataUrl) throws DOMInitException {
        if (LOG.isDebugEnabled()) LOG.debug("Update metadata from " + metadataUrl);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        dbf.setNamespaceAware(true);
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new MyErrorHandler());
            Document doc = db.parse(metadataUrl.openStream());
            update(doc);
        } catch (FileNotFoundException x) {
            if (LOG.isDebugEnabled()) LOG.debug("Metadata file not found", x);
        } catch (Exception x) {
            throw new DOMInitException("Can't read metadata from '" + metadataUrl + "'.", x);
        }
    }

    class MyNamespaceContext implements NamespaceContext {

        public Iterator getPrefixes(String namespaceURI) {
            return null;
        }

        public String getNamespaceURI(String prefix) {
            if (prefix.equals("bmd")) return XMLNS_BEANMETADATA;
            return XMLConstants.NULL_NS_URI;
        }

        public String getPrefix(String namespace) {
            if (namespace.equals(XMLNS_BEANMETADATA)) return "bmd";
            return null;
        }
    }

    static class MyErrorHandler implements ErrorHandler {

        public void error(SAXParseException exception) throws SAXException {
            LOG.error(exception.getMessage());
            throw exception;
        }

        public void fatalError(SAXParseException exception) throws SAXException {
            LOG.error(exception.getMessage());
            throw exception;
        }

        public void warning(SAXParseException exception) throws SAXException {
            LOG.warn(exception.getMessage());
        }
    }
}
