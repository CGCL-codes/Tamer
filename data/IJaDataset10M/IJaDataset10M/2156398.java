package com.intel.gpe.services.tss.common.incarnations.jdom;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.intel.gpe.constants.XMLIDBConstants;
import com.intel.gpe.services.tss.common.incarnations.ActionTemplate;
import com.intel.gpe.services.tss.common.incarnations.InterpretedScriptTemplate;
import com.intel.gpe.services.tss.common.incarnations.Invocation;
import com.intel.gpe.services.tss.common.incarnations.SimpleCommandTemplate;
import com.intel.gpe.services.tss.common.incarnations.SystemCommandTemplate;
import com.intel.util.xml.ElementUtil;

public class W3CElementInvocationReader implements Invocation.Reader {

    private Element element;

    public W3CElementInvocationReader(Element element) {
        this.element = element;
    }

    public String getName() {
        return ElementUtil.getAttributeValue(element, XMLIDBConstants.NAME_ATTR, "");
    }

    public String getDescr() {
        return ElementUtil.getChildValueString(element, XMLIDBConstants.IDB_NS, XMLIDBConstants.DESCRIPTION_TAG, null);
    }

    public ActionTemplate getBody() {
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node childNode = children.item(i);
            if (!(childNode instanceof Element)) {
                continue;
            }
            Element child = (Element) childNode;
            if (child.getNamespaceURI() == null || !child.getNamespaceURI().equals(XMLIDBConstants.IDB_NS)) {
                continue;
            }
            if (child.getLocalName().equals(XMLIDBConstants.BODY_TAG)) {
                return new SimpleCommandTemplate(ElementUtil.getTextValue(child));
            } else if (child.getLocalName().equals(XMLIDBConstants.COMMAND_TAG)) {
                SystemCommandTemplate result = new SystemCommandTemplate();
                NodeList children2 = child.getChildNodes();
                for (int j = 0; j < children2.getLength(); j++) {
                    Node child2Node = children2.item(j);
                    if (!(child2Node instanceof Element)) {
                        continue;
                    }
                    Element child2 = (Element) child2Node;
                    if (child2.getNamespaceURI() == null) {
                        continue;
                    }
                    if (child2.getNamespaceURI().equals(XMLIDBConstants.IDB_NS)) {
                        if (child2.getLocalName().equals(XMLIDBConstants.ARG_TAG)) {
                            result.addCmd(ElementUtil.getTextValue(child2));
                        } else if (child2.getLocalName().equals(XMLIDBConstants.ENV_TAG)) {
                            result.addEnv(ElementUtil.getAttributeValue(child2, XMLIDBConstants.NAME_ATTR, ""), ElementUtil.getTextValue(child2));
                        }
                    }
                }
                return result;
            } else if (child.getLocalName().equals(XMLIDBConstants.SCRIPT_TAG)) {
                InterpretedScriptTemplate result = new InterpretedScriptTemplate();
                result.setFilePrefix(ElementUtil.getAttributeValue(child, XMLIDBConstants.FILEPREFIX_ATTR, ""));
                result.setFileSuffix(ElementUtil.getAttributeValue(child, XMLIDBConstants.FILESUFFIX_ATTR, ""));
                result.setInterpreterName(ElementUtil.getAttributeValue(child, XMLIDBConstants.INTERPRETER_ATTR, ""));
                result.setTemplate(ElementUtil.getTextValue(child));
                return result;
            }
        }
        return null;
    }
}
