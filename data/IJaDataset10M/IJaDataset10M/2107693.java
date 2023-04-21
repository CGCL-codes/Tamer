package org.jcompany.jdoc.struts;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletContext;
import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

public class PlcStrutsConfigDigester {

    protected PlcStrutsConfig plcStrutsConfig;

    protected Logger log;

    public PlcStrutsConfigDigester(ServletContext servletContext) {
        log = Logger.getLogger(this.getClass());
        try {
            this.parse(servletContext);
        } catch (SAXException saex) {
            saex.printStackTrace();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }

    public void parse(ServletContext servletContext) throws IOException, SAXException {
        Digester strutsConfigDigester = new Digester();
        strutsConfigDigester.addObjectCreate("struts-config", PlcStrutsConfig.class);
        strutsConfigDigester.addObjectCreate("struts-config/data-source", PlcStrutsDataSource.class);
        strutsConfigDigester.addSetProperties("struts-config/data-source", "className", "className");
        strutsConfigDigester.addSetProperties("struts-config/data-source", "key", "key");
        strutsConfigDigester.addSetProperties("struts-config/data-source", "type", "type");
        strutsConfigDigester.addObjectCreate("struts-config/data-source/set-property", PlcSetProperty.class);
        strutsConfigDigester.addSetProperties("struts-config/data-source/set-property", "property", "property");
        strutsConfigDigester.addSetProperties("struts-config/data-source/set-property", "value", "value");
        strutsConfigDigester.addSetNext("struts-config/data-source/set-property", "addSetProperty");
        strutsConfigDigester.addSetNext("struts-config/data-source", "addStrutsDataSource");
        strutsConfigDigester.addObjectCreate("struts-config/form-beans/form-bean", PlcFormBean.class);
        strutsConfigDigester.addSetProperties("struts-config/form-beans/form-bean", "dynamic", "dynamic");
        strutsConfigDigester.addSetProperties("struts-config/form-beans/form-bean", "name", "name");
        strutsConfigDigester.addSetProperties("struts-config/form-beans/form-bean", "type", "type");
        strutsConfigDigester.addBeanPropertySetter("struts-config/form-beans/form-bean/description", "description");
        strutsConfigDigester.addObjectCreate("struts-config/form-beans/form-bean/form-property", PlcFormProperty.class);
        strutsConfigDigester.addSetProperties("struts-config/form-beans/form-bean/form-property", "name", "name");
        strutsConfigDigester.addSetProperties("struts-config/form-beans/form-bean/form-property", "type", "type");
        strutsConfigDigester.addSetProperties("struts-config/form-beans/form-bean/form-property", "size", "size");
        strutsConfigDigester.addSetProperties("struts-config/form-beans/form-bean/form-property", "className", "className");
        strutsConfigDigester.addSetProperties("struts-config/form-beans/form-bean/form-property", "initial", "initial");
        strutsConfigDigester.addSetNext("struts-config/form-beans/form-bean/form-property", "addFormProperty");
        strutsConfigDigester.addSetNext("struts-config/form-beans/form-bean", "addFormBean");
        strutsConfigDigester.addObjectCreate("struts-config/global-exceptions/exception", PlcStrutsGlobalException.class);
        strutsConfigDigester.addSetProperties("struts-config/global-exceptions/exception", "bundle", "bundle");
        strutsConfigDigester.addSetProperties("struts-config/global-exceptions/exception", "className", "className");
        strutsConfigDigester.addSetProperties("struts-config/global-exceptions/exception", "key", "key");
        strutsConfigDigester.addSetProperties("struts-config/global-exceptions/exception", "path", "path");
        strutsConfigDigester.addSetProperties("struts-config/global-exceptions/exception", "scope", "scope");
        strutsConfigDigester.addSetProperties("struts-config/global-exceptions/exception", "type", "type");
        strutsConfigDigester.addBeanPropertySetter("struts-config/global-exceptions/exception/description", "description");
        strutsConfigDigester.addSetNext("struts-config/global-exceptions/exception", "addStrutsGlobalException");
        strutsConfigDigester.addObjectCreate("struts-config/global-forwards/forward", PlcStrutusGlobalForward.class);
        strutsConfigDigester.addSetProperties("struts-config/global-forwards/forward", "className", "className");
        strutsConfigDigester.addSetProperties("struts-config/global-forwards/forward", "contextRelative", "contextRelative");
        strutsConfigDigester.addSetProperties("struts-config/global-forwards/forward", "name", "name");
        strutsConfigDigester.addSetProperties("struts-config/global-forwards/forward", "path", "path");
        strutsConfigDigester.addSetProperties("struts-config/global-forwards/forward", "redirect", "redirect");
        strutsConfigDigester.addSetNext("struts-config/global-forwards/forward", "addStrutsGlobalForward");
        strutsConfigDigester.addObjectCreate("struts-config/action-mappings/action", PlcAction.class);
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action", "attribute", "attribute");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action", "className", "className");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action", "forward", "forward");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action", "include", "include");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action", "input", "input");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action", "name", "name");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action", "path", "path");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action", "parameter", "parameter");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action", "prefix", "attribute");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action", "attribute", "prefix");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action", "roles", "roles");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action", "scope", "scope");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action", "suffix", "suffix");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action", "type", "type");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action", "unknown", "unknown");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action", "validate", "validate");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action", "icon", "icon");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action", "displayName", "displayName");
        strutsConfigDigester.addBeanPropertySetter("struts-config/action-mappings/action/description", "description");
        strutsConfigDigester.addObjectCreate("struts-config/action-mappings/action/set-property", PlcSetProperty.class);
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action/set-property", "property", "property");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action/set-property", "value", "value");
        strutsConfigDigester.addSetNext("struts-config/action-mappings/action/set-property", "addSetProperty");
        strutsConfigDigester.addObjectCreate("struts-config/action-mappings/action/exception", PlcStrutsActionException.class);
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action/exception", "bundle", "bundle");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action/exception", "className", "className");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action/exception", "handler", "handler");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action/exception", "key", "key");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action/exception", "path", "path");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action/exception", "scope", "scope");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action/exception", "type", "type");
        strutsConfigDigester.addSetNext("struts-config/action-mappings/action/exception", "addStrutsActionException");
        strutsConfigDigester.addObjectCreate("struts-config/action-mappings/action/forward", PlcStrutsActionForward.class);
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action/forward", "className", "className");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action/forward", "contextRelative", "contextRelative");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action/forward", "name", "name");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action/forward", "path", "path");
        strutsConfigDigester.addSetProperties("struts-config/action-mappings/action/forward", "redirect", "redirect");
        strutsConfigDigester.addBeanPropertySetter("struts-config/action-mappings/action/forward/description", "description");
        strutsConfigDigester.addSetNext("struts-config/action-mappings/action/forward", "addStrutsActionForward");
        strutsConfigDigester.addSetNext("struts-config/action-mappings/action", "addStrutsAction");
        strutsConfigDigester.addObjectCreate("struts-config/controller", PlcStrutsController.class);
        strutsConfigDigester.addSetProperties("struts-config/controller", "bufferSize", "bufferSize");
        strutsConfigDigester.addSetProperties("struts-config/controller", "className", "className");
        strutsConfigDigester.addSetProperties("struts-config/controller", "contentType", "contentType");
        strutsConfigDigester.addSetProperties("struts-config/controller", "debug", "debug");
        strutsConfigDigester.addSetProperties("struts-config/controller", "forwardPattern", "forwardPattern");
        strutsConfigDigester.addSetProperties("struts-config/controller", "inputForward", "inputForward");
        strutsConfigDigester.addSetProperties("struts-config/controller", "multipartClass", "multipartClass");
        strutsConfigDigester.addSetProperties("struts-config/controller", "nocache", "nocache");
        strutsConfigDigester.addSetProperties("struts-config/controller", "pagePattern", "pagePattern");
        strutsConfigDigester.addSetProperties("struts-config/controller", "processorClass", "processorClass");
        strutsConfigDigester.addSetProperties("struts-config/controller", "tempDir", "tempDir");
        strutsConfigDigester.addObjectCreate("struts-config/controller/set-property", PlcSetProperty.class);
        strutsConfigDigester.addSetProperties("struts-config/controller/set-property", "property", "property");
        strutsConfigDigester.addSetProperties("struts-config/controller/set-property", "value", "value");
        strutsConfigDigester.addSetNext("struts-config/controller/set-property", "addSetProperty");
        strutsConfigDigester.addSetNext("struts-config/controller", "addStrutsController");
        strutsConfigDigester.addObjectCreate("struts-config/message-resources", PlcMessageResources.class);
        strutsConfigDigester.addSetProperties("struts-config/message-resources", "className", "className");
        strutsConfigDigester.addSetProperties("struts-config/message-resources", "factory", "factory");
        strutsConfigDigester.addSetProperties("struts-config/message-resources", "key", "key");
        strutsConfigDigester.addSetProperties("struts-config/message-resources", "nullString", "nullString");
        strutsConfigDigester.addSetProperties("struts-config/message-resources", "parameter", "parameter");
        strutsConfigDigester.addObjectCreate("struts-config/message-resources/set-property", PlcSetProperty.class);
        strutsConfigDigester.addSetProperties("struts-config/message-resources/set-property", "property", "property");
        strutsConfigDigester.addSetProperties("struts-config/message-resources/set-property", "value", "value");
        strutsConfigDigester.addSetNext("struts-config/message-resources/set-property", "addSetProperty");
        strutsConfigDigester.addSetNext("struts-config/message-resources", "addMessageResource");
        strutsConfigDigester.addObjectCreate("struts-config/plug-in", PlcPlugIn.class);
        strutsConfigDigester.addSetProperties("struts-config/plug-in", "className", "className");
        strutsConfigDigester.addObjectCreate("struts-config/plug-in/set-property", PlcSetProperty.class);
        strutsConfigDigester.addSetProperties("struts-config/plug-in/set-property", "property", "property");
        strutsConfigDigester.addSetProperties("struts-config/plug-in/set-property", "value", "value");
        strutsConfigDigester.addSetNext("struts-config/plug-in/set-property", "addSetProperty");
        strutsConfigDigester.addSetNext("struts-config/plug-in", "addPlugIn");
        InputStream is = servletContext.getResourceAsStream("/WEB-INF/struts-config.xml");
        if (is != null) {
            plcStrutsConfig = (PlcStrutsConfig) strutsConfigDigester.parse(is);
        } else {
            log.warn("jDoc - Nao conseguiu recuperar o arquivo struts-config.xml");
        }
    }

    public PlcStrutsConfig getPlcStrutsConfig() {
        return plcStrutsConfig;
    }

    public void setPlcStrutsConfig(PlcStrutsConfig newPlcStrutsConfig) {
        plcStrutsConfig = newPlcStrutsConfig;
    }
}
