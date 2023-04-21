package org.exist.xslt;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;
import org.exist.EXistException;
import org.exist.dom.DocumentAtExist;
import org.exist.dom.ElementAtExist;
import org.exist.storage.BrokerPool;
import org.exist.storage.DBBroker;
import org.exist.xquery.XPathException;
import org.xml.sax.XMLFilter;

/**
 * @author <a href="mailto:shabanovd@gmail.com">Dmitriy Shabanov</a>
 *
 */
public class TransformerFactoryImpl extends SAXTransformerFactory {

    private static final Logger LOG = Logger.getLogger(TransformerFactoryImpl.class);

    private BrokerPool pool = null;

    private URIResolver resolver;

    private Map<String, Object> attributes = new HashMap<String, Object>();

    private ErrorListener errorListener = null;

    public TransformerFactoryImpl() {
    }

    public void setBrokerPool(BrokerPool pool) {
        this.pool = pool;
    }

    public DBBroker getBroker() throws EXistException {
        if (pool != null) return pool.get(null);
        throw new EXistException("that shouldn't happend. internal error.");
    }

    public void releaseBroker(DBBroker broker) throws EXistException {
        if (pool == null) throw new EXistException("Database wan't set properly.");
        pool.release(broker);
    }

    @Override
    public Source getAssociatedStylesheet(Source source, String media, String title, String charset) throws TransformerConfigurationException {
        throw new RuntimeException("Not implemented: TransformerFactory.getAssociatedStylesheet");
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public ErrorListener getErrorListener() {
        return errorListener;
    }

    @Override
    public boolean getFeature(String name) {
        throw new RuntimeException("Not implemented: TransformerFactory.getFeature");
    }

    @Override
    public URIResolver getURIResolver() {
        return resolver;
    }

    @Override
    public Templates newTemplates(Source source) throws TransformerConfigurationException {
        if (source instanceof SourceImpl) {
            try {
                return XSL.compile((ElementAtExist) ((DocumentAtExist) ((SourceImpl) source).source).getDocumentElement());
            } catch (XPathException e) {
                LOG.debug(e);
                throw new TransformerConfigurationException("Compilation error.", e);
            }
        } else if (source instanceof ElementAtExist) {
            try {
                return XSL.compile((ElementAtExist) source);
            } catch (XPathException e) {
                LOG.debug(e);
                throw new TransformerConfigurationException("Compilation error.", e);
            }
        } else if (source instanceof InputStream) {
            DBBroker broker = null;
            try {
                broker = getBroker();
                return XSL.compile((InputStream) source, broker);
            } catch (XPathException e) {
                LOG.debug(e);
                throw new TransformerConfigurationException("Compilation error.", e);
            } catch (EXistException e) {
                LOG.debug(e);
                throw new TransformerConfigurationException("Compilation error.", e);
            } finally {
                try {
                    releaseBroker(broker);
                } catch (EXistException e) {
                    throw new TransformerConfigurationException("Compilation error.", e);
                }
            }
        } else if (source instanceof StreamSource) {
            DBBroker broker = null;
            try {
                broker = getBroker();
                return XSL.compile(((StreamSource) source).getInputStream(), broker);
            } catch (XPathException e) {
                LOG.debug(e);
                throw new TransformerConfigurationException("Compilation error.", e);
            } catch (EXistException e) {
                LOG.debug(e);
                throw new TransformerConfigurationException("Compilation error.", e);
            } finally {
                try {
                    releaseBroker(broker);
                } catch (EXistException e) {
                    throw new TransformerConfigurationException("Compilation error.", e);
                }
            }
        }
        throw new TransformerConfigurationException("Not supported source " + source.getClass());
    }

    @Override
    public Transformer newTransformer() throws TransformerConfigurationException {
        return new org.exist.xslt.TransformerImpl();
    }

    @Override
    public Transformer newTransformer(Source source) throws TransformerConfigurationException {
        throw new RuntimeException("Not implemented: TransformerFactory.newTransformer");
    }

    @Override
    public void setAttribute(String name, Object value) {
        if (name.equals(TransformerFactoryAllocator.PROPERTY_BROKER_POOL)) pool = (BrokerPool) value;
        attributes.put(name, value);
    }

    @Override
    public void setErrorListener(ErrorListener listener) {
        errorListener = listener;
    }

    @Override
    public void setFeature(String name, boolean value) throws TransformerConfigurationException {
        throw new RuntimeException("Not implemented: TransformerFactory.setFeature");
    }

    @Override
    public void setURIResolver(URIResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public TemplatesHandler newTemplatesHandler() throws TransformerConfigurationException {
        return new TemplatesHandlerImpl();
    }

    @Override
    public TransformerHandler newTransformerHandler() throws TransformerConfigurationException {
        return new TransformerHandlerImpl(new XSLContext(pool), newTransformer());
    }

    @Override
    public TransformerHandler newTransformerHandler(Source src) throws TransformerConfigurationException {
        throw new RuntimeException("Not implemented: TransformerFactory.newTransformerHandler");
    }

    @Override
    public TransformerHandler newTransformerHandler(Templates templates) throws TransformerConfigurationException {
        if (templates == null) throw new TransformerConfigurationException("Templates object can not be null.");
        if (!(templates instanceof XSLStylesheet)) throw new TransformerConfigurationException("Templates object was not created by exist xslt (" + templates.getClass() + ")");
        return new TransformerHandlerImpl(new XSLContext(pool), templates.newTransformer());
    }

    @Override
    public XMLFilter newXMLFilter(Source src) throws TransformerConfigurationException {
        throw new RuntimeException("Not implemented: TransformerFactory.newXMLFilter");
    }

    @Override
    public XMLFilter newXMLFilter(Templates templates) throws TransformerConfigurationException {
        throw new RuntimeException("Not implemented: TransformerFactory.newXMLFilter");
    }
}
