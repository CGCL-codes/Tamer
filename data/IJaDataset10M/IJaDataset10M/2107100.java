package org.spbu.publishutil;

import java.io.File;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.Configuration;
import net.sf.saxon.Controller;
import net.sf.saxon.StandardURIResolver;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceExtent;
import net.sf.saxon.value.SingletonNode;
import net.sf.saxon.value.Value;
import org.spbu.publishutil.cache.ControllerCache;
import org.spbu.publishutil.utils.EventLogger;
import org.spbu.publishutil.utils.ProjectRegistry;
import org.spbu.publishutil.utils.RegisteredLocation;

public class DirectoryXSLApplyAction {

    protected static final ControllerCache CONTROLLER_CACHE = new ControllerCache();

    protected static final File DRLIMPORT_FILE;

    protected static final File DRLEXPORT_FILE;

    static {
        DRLIMPORT_FILE = new File(PublishUtil.getRunningAppPath() + "/xsl/drl/drlimport.xsl");
        DRLEXPORT_FILE = new File(PublishUtil.getRunningAppPath() + "/xsl/drl/drlexport.xsl");
    }

    protected final Controller xslFile;

    protected ProjectRegistry registry;

    protected EventLogger logger;

    protected final URIResolver uriResolver = new StandardURIResolver(new Configuration()) {

        private static final long serialVersionUID = -7919352677909462305L;

        public Source resolve(String href, String base) throws XPathException {
            if (registry != null) {
                RegisteredLocation loc = registry.getRegisteredLocation(href);
                if (loc != null) return new StreamSource(loc.getFile().toURI().toString());
            }
            return super.resolve(href, base);
        }
    };

    protected final ErrorListener errorListener = new ErrorListener() {

        public void error(TransformerException exception) throws TransformerException {
            processTransformerException(exception, EventLogger.SEVERITY_ERROR);
        }

        public void fatalError(TransformerException exception) throws TransformerException {
            processTransformerException(exception, EventLogger.SEVERITY_ERROR);
        }

        public void warning(TransformerException exception) throws TransformerException {
            processTransformerException(exception, EventLogger.SEVERITY_WARNING);
        }
    };

    public void setRegistry(ProjectRegistry registry) {
        this.registry = registry;
    }

    public void setLogger(EventLogger logger) {
        this.logger = logger;
    }

    public DirectoryXSLApplyAction(File xslFile) throws Exception {
        this.xslFile = CONTROLLER_CACHE.getController(xslFile);
    }

    public void run(File srcFile) throws RuntimeException {
        try {
            File dstFile = File.createTempFile("xsltempresult", null);
            doTransform(srcFile, dstFile);
            srcFile.delete();
            dstFile.renameTo(srcFile);
        } catch (Exception e) {
            logger.logException(e, true);
        }
    }

    protected void doTransform(File source, File result) throws Exception {
        if (source == null || result == null) return;
        try {
            logger.logEvent("Applying XSL transformation to " + source.getName());
            xslFile.reset();
            transform(xslFile, new StreamSource(source.toURI().toString()), new StreamResult(result.toURI().toString()));
        } catch (Exception e) {
            throw e;
        }
    }

    protected void transform(Controller transformer, Source source, Result result) throws TransformerException {
        transformer.clearDocumentPool();
        transformer.setErrorListener(errorListener);
        transformer.setURIResolver(uriResolver);
        transformer.transform(source, result);
    }

    private void processTransformerException(TransformerException e, int severity) throws XPathException {
        if (e instanceof XPathException && !((XPathException) e).isStaticError()) {
            processXPathException((XPathException) e, severity);
        } else {
            SourceLocator loc = e.getLocator();
            logger.logError(loc.getSystemId(), loc.getLineNumber(), e.getMessage(), severity);
            e.printStackTrace();
        }
    }

    private void processXPathException(XPathException xpathError, int severity) throws XPathException {
        Value errorObject = xpathError.getErrorObject();
        if (errorObject instanceof SingletonNode) {
            NodeInfo node = ((SingletonNode) errorObject).getNode();
            logger.logError(node.getSystemId(), node.getLineNumber(), xpathError.getMessage(), severity);
        } else if (errorObject instanceof SequenceExtent) {
            SequenceExtent seq = (SequenceExtent) errorObject;
            SequenceIterator it = seq.iterate(xpathError.getXPathContext());
            Item item;
            while ((item = it.next()) != null) if (item instanceof NodeInfo) {
                NodeInfo node = (NodeInfo) item;
                logger.logError(node.getSystemId(), node.getLineNumber(), xpathError.getMessage(), severity);
            }
        } else {
        }
    }
}
