package com.intel.gpe.services.tss.common.staticproperties.jdom;

import org.w3c.dom.Element;
import com.intel.gpe.constants.XMLIDBConstants;
import com.intel.gpe.services.tss.common.incarnations.Template;
import com.intel.gpe.services.tss.common.incarnations.jdom.W3CElementTemplateReader;
import com.intel.gpe.services.tss.common.staticproperties.Application;
import com.intel.gpe.tsi.common.BSSProcessControllerFactory;
import com.intel.gpe.tsi.common.DefaultProcessControllerFactory;
import com.intel.gpe.tsi.common.GenericProcessControllerFactory;
import com.intel.gpe.tsi.common.IProcessControllerFactory;
import com.intel.gpe.tsi.common.JRTProcessControllerFactory;
import com.intel.util.xml.ElementUtil;

/**
 * @version $Id: W3CElementApplicationReader.java,v 1.9 2007/02/27 10:03:35 dnpetrov Exp $
 * @author Dmitry Petrov
 */
public class W3CElementApplicationReader implements Application.Reader {

    private Element element;

    private Element templateElement;

    private Element jobControlMethodElement;

    public W3CElementApplicationReader(Element element) {
        this.element = element;
        this.jobControlMethodElement = (Element) element.getElementsByTagNameNS(XMLIDBConstants.IDB_NS, XMLIDBConstants.JOBCONTROL_TAG).item(0);
        this.templateElement = (Element) element.getElementsByTagNameNS(XMLIDBConstants.IDB_NS, XMLIDBConstants.SCRIPT_TEMPLATE_TAG).item(0);
    }

    public String getName() {
        return ElementUtil.getChildValueString(element, XMLIDBConstants.JSDL_NS, XMLIDBConstants.APPLICATION_NAME_TAG, null);
    }

    public String getVersion() {
        return ElementUtil.getChildValueString(element, XMLIDBConstants.JSDL_NS, XMLIDBConstants.APPLICATION_VERSION_TAG, null);
    }

    public String getDescr() {
        return ElementUtil.getChildValueString(element, XMLIDBConstants.JSDL_NS, XMLIDBConstants.DESCRIPTION_TAG, null);
    }

    public IProcessControllerFactory getProcessControllerFactory() {
        if (jobControlMethodElement == null) {
            return new DefaultProcessControllerFactory();
        } else {
            String method = ElementUtil.getAttributeValue(jobControlMethodElement, XMLIDBConstants.JOBCONTROLMETHOD_ATTR, null);
            if (method == null || method.equals(XMLIDBConstants.JOBCONTROLMETHOD_DEFAULT)) {
                return null;
            } else if (method.equals(XMLIDBConstants.JOBCONTROLMETHOD_JAVA)) {
                return new JRTProcessControllerFactory();
            } else if (method.equals(XMLIDBConstants.JOBCONTROLMETHOD_BSS)) {
                return new BSSProcessControllerFactory();
            } else if (method.equals(XMLIDBConstants.JOBCONTROLMETHOD_CUSTOM)) {
                Template startTemplate = null;
                Element startTemplateElement = (Element) jobControlMethodElement.getElementsByTagNameNS(XMLIDBConstants.IDB_NS, XMLIDBConstants.START_TEMPLATE_TAG).item(0);
                if (startTemplateElement == null) {
                    startTemplate = new Template(new W3CElementTemplateReader(startTemplateElement));
                }
                Template getStatusTemplate = null;
                Element getStatusTemplateElement = (Element) jobControlMethodElement.getElementsByTagNameNS(XMLIDBConstants.IDB_NS, XMLIDBConstants.GETSTATUS_TEMPLATE_TAG).item(0);
                if (getStatusTemplateElement != null) {
                    getStatusTemplate = new Template(new W3CElementTemplateReader(getStatusTemplateElement));
                }
                Template holdTemplate = null;
                Element holdTemplateElement = (Element) jobControlMethodElement.getElementsByTagNameNS(XMLIDBConstants.IDB_NS, XMLIDBConstants.HOLD_TEMPLATE_TAG).item(0);
                if (holdTemplateElement != null) {
                    holdTemplate = new Template(new W3CElementTemplateReader(holdTemplateElement));
                }
                Template resumeTemplate = null;
                Element resumeTemplateElement = (Element) jobControlMethodElement.getElementsByTagNameNS(XMLIDBConstants.IDB_NS, XMLIDBConstants.RESUME_TEMPLATE_TAG).item(0);
                if (resumeTemplateElement != null) {
                    resumeTemplate = new Template(new W3CElementTemplateReader(resumeTemplateElement));
                }
                Template abortTemplate = null;
                Element abortTemplateElement = (Element) jobControlMethodElement.getElementsByTagNameNS(XMLIDBConstants.IDB_NS, XMLIDBConstants.ABORT_TEMPLATE_TAG).item(0);
                if (abortTemplateElement != null) {
                    abortTemplate = new Template(new W3CElementTemplateReader(abortTemplateElement));
                }
                return new GenericProcessControllerFactory(startTemplate, getStatusTemplate, holdTemplate, resumeTemplate, abortTemplate);
            } else {
                return new DefaultProcessControllerFactory();
            }
        }
    }

    public boolean hasScriptTemplateReader() {
        return templateElement != null;
    }

    public Template.Reader getScriptTemplateReader() {
        return templateElement != null ? new W3CElementTemplateReader(templateElement) : null;
    }
}
