package org.toobsframework.transformpipeline.transformer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.transform.Result;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.impl.Version;
import org.apache.xml.utils.DefaultErrorHandler;
import org.apache.xml.utils.XMLReaderManager;
import org.toobsframework.util.Configuration;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public abstract class BaseXMLTransformer implements IXMLTransformer {

    protected final Log log = LogFactory.getLog(getClass());

    protected URIResolver uriResolver;

    protected Configuration configuration;

    protected SAXTransformerFactory saxTFactory;

    protected Map<String, Templates> templateCache;

    private boolean doReload;

    public void init() throws Exception {
        if (uriResolver == null) {
            throw new Exception("uriResolver property must be set");
        }
        TransformerFactory tFactory = TransformerFactory.newInstance();
        tFactory.setURIResolver(uriResolver);
        tFactory.setErrorListener(new DefaultErrorHandler());
        tFactory.setAttribute("http://xml.apache.org/xalan/features/incremental", java.lang.Boolean.TRUE);
        if (tFactory.getFeature(SAXSource.FEATURE) && tFactory.getFeature(SAXResult.FEATURE)) {
            saxTFactory = ((SAXTransformerFactory) tFactory);
        } else {
            throw new Exception("Features [" + SAXSource.FEATURE + "][" + SAXResult.FEATURE + "] are not available with current transformer factory");
        }
        templateCache = new ConcurrentHashMap<String, Templates>();
        doReload = configuration.doReload();
    }

    protected void debugParams(Map<String, Object> inputParams) {
        if (log.isDebugEnabled()) {
            log.debug("TRANSFORM XML STARTED");
            log.debug("Get input XMLs");
            Iterator<Map.Entry<String, Object>> iter = inputParams.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iter.next();
                log.debug("  Transform Param - name: " + entry.getKey() + " value: " + entry.getValue());
            }
        }
    }

    protected Templates getTemplates(String xsl, XMLReader reader) throws TransformerConfigurationException, TransformerException, IOException, SAXException {
        Templates templates = null;
        if (templateCache.containsKey(xsl) && !doReload) {
            templates = templateCache.get(xsl);
        } else {
            if (reader == null) {
                reader = XMLReaderManager.getInstance().getXMLReader();
            }
            TemplatesHandler templatesHandler = saxTFactory.newTemplatesHandler();
            reader.setContentHandler(templatesHandler);
            try {
                reader.parse(getInputSource(xsl));
            } catch (MalformedURLException e) {
                log.info("Xerces Version: " + Version.getVersion());
                throw e;
            }
            templates = templatesHandler.getTemplates();
            templateCache.put(xsl, templates);
        }
        return templates;
    }

    protected InputSource getInputSource(String xsl) throws TransformerException {
        StreamSource source = ((StreamSource) uriResolver.resolve(xsl + ".xsl", ""));
        InputSource iSource = new InputSource(source.getInputStream());
        iSource.setSystemId(source.getSystemId());
        return iSource;
    }

    public void preloadTransform(String transformName) throws Exception {
        if (!doReload) {
            log.info("Preloading xsl: " + transformName);
            this.getTemplates(transformName, null);
        }
    }

    public void transform(Result result, List<String> inputXSLs, Object xmlObject, Map<String, Object> inputParams, IXMLTransformerHelper transformerHelper) throws ToobsTransformerException {
        log.info("Result transform not implemented");
    }

    public void setUriResolver(URIResolver resolver) {
        this.uriResolver = resolver;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
