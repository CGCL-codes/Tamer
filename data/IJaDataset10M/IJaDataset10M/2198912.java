package com.pbonhomme.xf.xml.processor;

import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import com.pbonhomme.xf.san.mimetype.MimeTypeBean;
import com.pbonhomme.xf.san.mimetype.eMimeType;

public abstract class AbstractXMLTemplate {

    private DefaultHandler defaultHandler;

    private static SAXParserFactory saxParserFactory;

    protected static String GENERIC_TEMPLATE_TRANSFORMER = "generic-template";

    static {
        saxParserFactory = createSAXParserFactory(false, false);
    }

    ;

    protected String templateName;

    protected Map<String, String> parameters;

    protected String filePath;

    protected String xmlTransformer;

    protected String htmlTransformer;

    protected String writer;

    protected String mimetype;

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public void setXMLTransformer(String nameTransformer) {
        this.xmlTransformer = nameTransformer;
    }

    public void setHTMLTransformer(String nameTransformer) {
        this.htmlTransformer = nameTransformer;
    }

    protected void setFilePath(String path) {
        this.filePath = path;
    }

    protected void setWriter(String writer, String mimetype) {
        this.writer = writer;
        this.mimetype = mimetype;
    }

    public MimeTypeBean getMimeTypeBean() {
        return eMimeType.getMimeTypeBean(mimetype);
    }

    public int processAndSave(String archiveXmlPath, Map<String, Object> parameters) throws XMLProcessorException {
        return 0;
    }

    public void setDefaultHandler(DefaultHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    public void loadTemplate() {
        if (defaultHandler != null) {
            XMLReader _reader = null;
            try {
                SAXParser saxParser = saxParserFactory.newSAXParser();
                _reader = saxParser.getXMLReader();
                _reader.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
                _reader.setContentHandler(defaultHandler);
                _reader.parse(new org.xml.sax.InputSource(filePath));
            } catch (Exception e) {
                logger.error("XMLPrinter.getNewURLReader():", e);
            } finally {
                _reader = null;
            }
        }
    }

    private static SAXParserFactory createSAXParserFactory(boolean namespaceAware, boolean validating) {
        SAXParserFactory _saxParserFactory = SAXParserFactory.newInstance();
        _saxParserFactory.setNamespaceAware(namespaceAware);
        _saxParserFactory.setValidating(validating);
        return _saxParserFactory;
    }

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AbstractXMLTemplate.class);
}
