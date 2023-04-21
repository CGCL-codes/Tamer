package com.ubs.swidBHT.v2.client.export;

import java.util.*;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import com.ubs.swidBHT.v2.client.Assert;
import com.ubs.swidBHT.v2.client.Tracing;
import com.ubs.swidBHT.v2.client.export.congen.IContentGenerator;

/**
 * @author Michael Lin
 * created at 1:34:01 PM Dec 5, 2007 
 * 
 */
public class ExportCfgManager {

    public static final String ELEMENT_PRINT_CONFIG = "export-config";

    public static final String ELEMENT_EXPORTTER_MAPPING = "exporter-mappings";

    public static final String ELEMENT_SUPPORT_FILE_LIST = "supported-file-list";

    public static final String ELEMENT_SUPPORT_FILE = "supported-file";

    public static final String ATTRIBUTE_EXTENSION = "extension";

    public static final String ELEMENT_HANDLER = "handler";

    public static final String ELEMENT_EXPORTER = "exporter";

    public static final String ATTRIBUTE_PATH = "path";

    public static final String ATTRIBUTE_TYPE = "type";

    public static final String ATTRIBUTE_NAME = "name";

    private final Map exporterMap = new HashMap(10);

    private final Map contentGeneratorMap = new HashMap(10);

    private boolean isInitialized = false;

    /**
     * 
     */
    public ExportCfgManager() {
        super();
    }

    public synchronized boolean isInitialized() {
        return isInitialized;
    }

    public IExporter getExporter(String path) throws Exception {
        return (IExporter) exporterMap.get(path);
    }

    public IContentGenerator getContentGenerator(String key) throws Exception {
        return (IContentGenerator) contentGeneratorMap.get(key);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass()).append("=");
        sb.append("{exporter{");
        Set keys = exporterMap.keySet();
        for (Iterator iter = keys.iterator(); iter.hasNext(); ) {
            String key = (String) iter.next();
            sb.append("[");
            sb.append(key).append(", ").append(exporterMap.get(key));
            sb.append("]");
        }
        sb.append("}");
        sb.append(";content generator{");
        keys = contentGeneratorMap.keySet();
        for (Iterator iter = keys.iterator(); iter.hasNext(); ) {
            String key = (String) iter.next();
            sb.append("[");
            sb.append(key).append(", ").append(contentGeneratorMap.get(key));
            sb.append("]");
        }
        sb.append("}");
        sb.append("}");
        return sb.toString();
    }

    public synchronized void initConfig(String configPath, ServletContext servletContext) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document configurationDOM = builder.parse(servletContext.getRealPath(configPath));
            processConfiguration(configurationDOM);
            isInitialized = true;
            if (Tracing.isDebug()) {
                Tracing.debug(this, "initConfig", "Exporting configuration done. " + this.toString());
            }
        } catch (Exception e) {
            Tracing.error(this, "initConfig", "Can not do initialization", e);
            e.printStackTrace();
        }
    }

    private void processConfiguration(Document configurationDOM) throws Exception {
        final Element rootElement = configurationDOM.getDocumentElement();
        if (!Assert.isTrue(rootElement.getNodeName().equals(ELEMENT_PRINT_CONFIG), "Configuration File is not a export-config file")) {
            throw new Exception("Configuration File is not a export-config file");
        }
        final NodeList mappingElementList = rootElement.getElementsByTagName(ELEMENT_EXPORTTER_MAPPING);
        Assert.isTrue(mappingElementList.getLength() == 1, "Tag 'export-config' must contain exactly one 'exporter-mappings' element");
        final Element mappingElement = (Element) mappingElementList.item(0);
        processMappingElement(mappingElement);
        NodeList supportedFileList = rootElement.getElementsByTagName(ELEMENT_SUPPORT_FILE_LIST);
        Assert.isTrue(supportedFileList.getLength() == 1, "Tag 'export-config' must contain exactly one 'supported-file-list' element");
        Element supportedFileListElement = (Element) supportedFileList.item(0);
        processSupportedFileListElement(supportedFileListElement);
    }

    private void processSupportedFileListElement(Element supportedFileListElement) throws Exception {
        NodeList supportedFileElementList = supportedFileListElement.getElementsByTagName(ELEMENT_SUPPORT_FILE);
        for (int i = 0, n = supportedFileElementList.getLength(); i < n; i++) {
            Element suppprtFileElement = (Element) supportedFileElementList.item(i);
            processSupportedFileElement(suppprtFileElement);
        }
    }

    private void processSupportedFileElement(Element supportedFileElement) throws Exception {
        String ext = supportedFileElement.getAttribute(ATTRIBUTE_EXTENSION);
        if (!Assert.notNull(ext, "Attribute 'extension' must be defined")) {
            throw new Exception("Attribute 'extension' must be defined");
        }
        NodeList handerElementList = supportedFileElement.getElementsByTagName(ELEMENT_HANDLER);
        for (int i = 0, n = handerElementList.getLength(); i < n; i++) {
            Element handlerElement = (Element) handerElementList.item(i);
            processHandlerElement(ext, handlerElement);
        }
    }

    private void processHandlerElement(String fileExt, Element handlerElement) throws Exception {
        String handler = handlerElement.getAttribute(ATTRIBUTE_TYPE);
        if (!Assert.notNull(handler, "Attribute 'handler' must be defined")) {
            throw new Exception("Attribute 'handler' must be defined");
        }
        String name = handlerElement.getAttribute(ATTRIBUTE_NAME);
        IContentGenerator contentGenerator = null;
        try {
            contentGenerator = (IContentGenerator) Class.forName(handler).newInstance();
        } catch (Exception e) {
            if (Tracing.isInfo()) {
                Tracing.info(this, "processHandlerElement", "can not create IContentGenerator named \'" + handler + "\'", e);
            }
        }
        contentGeneratorMap.put(fileExt + "." + name, contentGenerator);
    }

    private void processMappingElement(Element mappingElement) throws Exception {
        final NodeList exporterElementList = mappingElement.getElementsByTagName(ELEMENT_EXPORTER);
        for (int i = 0, n = exporterElementList.getLength(); i < n; i++) {
            final Element exporterElement = (Element) exporterElementList.item(i);
            processExporterElement(exporterElement);
        }
    }

    private void processExporterElement(Element exporterElement) throws Exception {
        final String path = exporterElement.getAttribute(ATTRIBUTE_PATH);
        if (!Assert.notNull(path, "Attribute 'path' must be defined")) {
            throw new Exception("Attribute 'path' must be defined");
        }
        final String type = exporterElement.getAttribute(ATTRIBUTE_TYPE);
        if (!Assert.notNull(type, "Attribute 'type' must be defined")) {
            throw new Exception("Attribute 'type' must be defined");
        }
        IExporter exporter = null;
        try {
            exporter = (IExporter) Class.forName(type).newInstance();
        } catch (Exception e) {
            if (Tracing.isInfo()) {
                Tracing.info(this, "processExporterElement", "can not create IExporter named \'" + type + "\'", e);
            }
        }
        this.exporterMap.put(path, exporter);
    }
}
