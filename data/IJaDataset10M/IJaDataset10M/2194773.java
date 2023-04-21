package net.javacrumbs.springws.test.lookup;

import java.io.IOException;
import java.net.URI;
import net.javacrumbs.springws.test.template.TemplateProcessor;
import net.javacrumbs.springws.test.template.XsltTemplateProcessor;
import net.javacrumbs.springws.test.util.DefaultXmlUtil;
import net.javacrumbs.springws.test.util.XmlUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.w3c.dom.Document;

/**
 * Superclass for ResourceLookup classes that need to procees the resource using template.
 * @author Lukas Krecan
 *
 */
public abstract class AbstractTemplateProcessingResourceLookup implements ResourceLookup {

    protected final Log logger = LogFactory.getLog(getClass());

    private XmlUtil xmlUtil = DefaultXmlUtil.getInstance();

    private TemplateProcessor templateProcessor = new XsltTemplateProcessor();

    protected Document loadDocument(WebServiceMessage message) {
        return getXmlUtil().loadDocument(message);
    }

    /**
	 * Processes the resource using template processor.
	 * @param uri
	 * @param message
	 * @param resource
	 * @return
	 * @throws IOException
	 */
    protected Resource processResource(URI uri, WebServiceMessage message, Resource resource) throws IOException {
        return getTemplateProcessor().processTemplate(resource, message);
    }

    public XmlUtil getXmlUtil() {
        return xmlUtil;
    }

    public void setXmlUtil(XmlUtil xmlUtil) {
        this.xmlUtil = xmlUtil;
    }

    public TemplateProcessor getTemplateProcessor() {
        return templateProcessor;
    }

    public void setTemplateProcessor(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }
}
