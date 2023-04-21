package org.springframework.web.servlet.view.json.writer.xstream;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.json.JsonStringWriter;
import org.springframework.web.servlet.view.json.JsonWriterConfiguratorTemplateRegistry;

public class XStreamJsonStringWriter implements JsonStringWriter {

    public static final String DEFAULT_ROOTNAME = "model";

    private boolean enableJsonConfigSupport;

    private boolean convertAllMapValues;

    private String keepValueTypesMode = MODE_KEEP_VALUETYPES_NONE;

    private String rootname = DEFAULT_ROOTNAME;

    private boolean enableAnnotationConf;

    private Class[] processAnnotationsForTypes;

    private Boolean autodetectAnnotations = false;

    public void convertAndWrite(Map modelMap, JsonWriterConfiguratorTemplateRegistry configuratorTemplateRegistry, Writer writer, BindingResult br) throws IOException {
        XStreamJsonWriterConfiguratorTemplate configuratorTemplate = (XStreamJsonWriterConfiguratorTemplate) configuratorTemplateRegistry.findConfiguratorTemplate(XStreamJsonWriterConfiguratorTemplate.class.getName());
        SpringJsonXStream xstream = null;
        if (enableJsonConfigSupport && configuratorTemplate != null) {
            xstream = (SpringJsonXStream) configuratorTemplate.getConfigurator();
        }
        if (xstream == null) {
            Class commandClass = getCommandClass(modelMap, br);
            if (enableAnnotationConf) {
                xstream = SpringJsonXStreamBuilder.build(convertAllMapValues, keepValueTypesMode, rootname, processAnnotationsForTypes, autodetectAnnotations, commandClass);
            } else {
                xstream = SpringJsonXStreamBuilder.build(convertAllMapValues, keepValueTypesMode, rootname);
            }
        }
        xstream.setBindingResult(br);
        xstream.toXML(modelMap, writer);
    }

    private Class getCommandClass(Map modelMap, BindingResult br) {
        if (br == null) return null;
        String name = br.getObjectName();
        Object command = modelMap.get(name);
        if (command != null) return command.getClass(); else return null;
    }

    public boolean getEnableJsonConfigSupport() {
        return enableJsonConfigSupport;
    }

    public void setEnableJsonConfigSupport(boolean enableJsonConfigSupport) {
        this.enableJsonConfigSupport = enableJsonConfigSupport;
    }

    public boolean getConvertAllMapValues() {
        return convertAllMapValues;
    }

    public void setConvertAllMapValues(boolean convertAllMapValues) {
        this.convertAllMapValues = convertAllMapValues;
    }

    public String getKeepValueTypesMode() {
        return keepValueTypesMode;
    }

    public void setKeepValueTypesMode(String keepValueTypesMode) {
        this.keepValueTypesMode = keepValueTypesMode;
    }

    public String getRootname() {
        return rootname;
    }

    public void setRootname(String rootname) {
        this.rootname = rootname;
    }

    public Class[] getProcessAnnotationsForTypes() {
        return processAnnotationsForTypes;
    }

    public void setProcessAnnotationsForTypes(Class[] types) {
        this.processAnnotationsForTypes = types;
    }

    public void setProcessAnnotationsForType(Class type) {
        this.processAnnotationsForTypes = new Class[] { type };
    }

    public Boolean getAutodetectAnnotations() {
        return autodetectAnnotations;
    }

    public void setAutodetectAnnotations(Boolean autodetectAnnotations) {
        this.autodetectAnnotations = autodetectAnnotations;
    }

    public boolean isEnableAnnotationConf() {
        return enableAnnotationConf;
    }

    public void setEnableAnnotationConf(boolean enableAnnotationConf) {
        this.enableAnnotationConf = enableAnnotationConf;
    }
}
