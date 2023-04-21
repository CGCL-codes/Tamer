package org.yaoqiang.bpmn.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.yaoqiang.bpmn.model.elements.XMLAttribute;
import org.yaoqiang.bpmn.model.elements.XMLCollection;
import org.yaoqiang.bpmn.model.elements.XMLComplexChoice;
import org.yaoqiang.bpmn.model.elements.XMLComplexElement;
import org.yaoqiang.bpmn.model.elements.XMLElement;
import org.yaoqiang.bpmn.model.elements.XMLExtensionElement;
import org.yaoqiang.bpmn.model.elements.XMLFactory;
import org.yaoqiang.bpmn.model.elements.XMLTextElement;
import org.yaoqiang.bpmn.model.elements.activities.ResourceAssignmentExpression;
import org.yaoqiang.bpmn.model.elements.core.common.BPMNError;
import org.yaoqiang.bpmn.model.elements.core.common.Expression;
import org.yaoqiang.bpmn.model.elements.core.common.Expressions;
import org.yaoqiang.bpmn.model.elements.core.common.ItemDefinition;
import org.yaoqiang.bpmn.model.elements.core.common.Message;
import org.yaoqiang.bpmn.model.elements.core.foundation.Documentation;
import org.yaoqiang.bpmn.model.elements.core.infrastructure.Definitions;
import org.yaoqiang.bpmn.model.elements.core.service.Interface;
import org.yaoqiang.bpmn.model.elements.events.EventDefinition;

/**
 * BPMNModelCodec
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class BPMNModelCodec {

    public static final String VERSION = "201203051730";

    protected Map<String, XMLElement> bpmnElementMap = new HashMap<String, XMLElement>();

    public Element encode(Document doc, Definitions BPMNModel) {
        Map<String, String> namespaces = BPMNModel.getNamespaces();
        String prefix = namespaces.get(BPMNModelConstants.BPMN_SEMANTIC_MODEL_NS);
        if (prefix == null) {
            prefix = "";
        } else if (prefix.length() != 0) {
            prefix += ":";
        }
        Element defs = doc.createElement(prefix + "definitions");
        doc.appendChild(defs);
        defs.setAttribute("id", BPMNModel.getId());
        defs.setAttribute("name", BPMNModel.getName());
        defs.setAttribute("targetNamespace", BPMNModel.getTargetNamespace());
        defs.setAttribute("expressionLanguage", BPMNModel.getExpressionLanguage());
        defs.setAttribute("typeLanguage", BPMNModel.getTypeLanguage());
        defs.setAttribute("exporter", BPMNModel.getExporter());
        defs.setAttribute("exporterVersion", BPMNModel.getExporterVersion());
        for (String location : namespaces.keySet()) {
            String name = namespaces.get(location);
            defs.setAttribute("xmlns:" + name, location);
        }
        if (prefix.length() == 0) {
            defs.setAttribute("xmlns", BPMNModelConstants.BPMN_SEMANTIC_MODEL_NS);
        }
        defs.setAttribute("xsi:schemaLocation", BPMNModelConstants.XSI_SCHEMA_LOCATION);
        encodeSemantic(defs, BPMNModel, prefix);
        return defs;
    }

    public void encodeSemantic(Element defs, Definitions BPMNModel, String prefix) {
        Element node = null;
        List<XMLElement> importElements = BPMNModel.getImportList();
        for (XMLElement el : importElements) {
            if (el.isEmpty()) {
                continue;
            }
            node = defs.getOwnerDocument().createElement(prefix + el.toName());
            defs.appendChild(node);
            encodeComplexElement(node, el, prefix);
        }
        List<XMLElement> roots = new ArrayList<XMLElement>();
        List<XMLElement> messageElements = new ArrayList<XMLElement>();
        List<XMLElement> errorElements = new ArrayList<XMLElement>();
        List<XMLElement> interfaceElements = new ArrayList<XMLElement>();
        List<XMLElement> eventDefElements = new ArrayList<XMLElement>();
        List<XMLElement> rootElements = BPMNModel.getRootElementList();
        for (XMLElement root : rootElements) {
            if (root.isEmpty()) {
                continue;
            }
            if (root instanceof ItemDefinition) {
                node = defs.getOwnerDocument().createElement(prefix + root.toName());
                defs.appendChild(node);
                encodeComplexElement(node, root, prefix);
            } else if (root instanceof BPMNError) {
                errorElements.add(root);
                continue;
            } else if (root instanceof Message) {
                messageElements.add(root);
                continue;
            } else if (root instanceof Interface) {
                interfaceElements.add(root);
                continue;
            } else if (root instanceof EventDefinition) {
                eventDefElements.add(root);
                continue;
            } else {
                roots.add(root);
                continue;
            }
        }
        for (XMLElement error : errorElements) {
            node = defs.getOwnerDocument().createElement(prefix + error.toName());
            defs.appendChild(node);
            encodeComplexElement(node, error, prefix);
        }
        for (XMLElement message : messageElements) {
            node = defs.getOwnerDocument().createElement(prefix + message.toName());
            defs.appendChild(node);
            encodeComplexElement(node, message, prefix);
        }
        for (XMLElement _interface : interfaceElements) {
            node = defs.getOwnerDocument().createElement(prefix + _interface.toName());
            defs.appendChild(node);
            encodeComplexElement(node, _interface, prefix);
        }
        for (XMLElement eventDef : eventDefElements) {
            node = defs.getOwnerDocument().createElement(prefix + eventDef.toName());
            defs.appendChild(node);
            encodeComplexElement(node, eventDef, prefix);
        }
        for (XMLElement id : roots) {
            node = defs.getOwnerDocument().createElement(prefix + id.toName());
            defs.appendChild(node);
            encodeComplexElement(node, id, prefix);
        }
    }

    public void encodeComplexElement(Element parent, XMLElement el, String prefix) {
        Element node = null;
        if (el.toValue() != null && el.toValue().length() > 0) {
            if (el instanceof Expression || el instanceof Documentation) {
                if (!el.toValue().equals("")) {
                    Node textNode = parent.getOwnerDocument().createCDATASection(el.toValue());
                    parent.appendChild(textNode);
                }
            }
        }
        for (XMLElement e : ((XMLComplexElement) el).toElements()) {
            if (e instanceof XMLComplexElement) {
                if (e.isEmpty()) {
                    continue;
                }
                node = parent.getOwnerDocument().createElement(prefix + e.toName());
                parent.appendChild(node);
                encodeComplexElement(node, e, prefix);
            } else if (e instanceof XMLCollection) {
                if (e.isEmpty()) {
                    continue;
                }
                encodeCollection(parent, e, prefix);
            } else if (e instanceof XMLComplexChoice) {
                if (e.isEmpty()) {
                    continue;
                }
                encodeComplexChoice(parent, e, prefix);
            } else if (e instanceof XMLTextElement) {
                encodeText(parent, e, prefix);
            } else if (e instanceof XMLAttribute) {
                encodeAttribute(parent, e);
            } else if (e instanceof XMLExtensionElement) {
                if (e.isEmpty()) {
                    continue;
                }
                node = parent.getOwnerDocument().createElement(prefix + e.toName());
                parent.appendChild(node);
                encodeExtensionElement(node, e);
            }
        }
    }

    public void encodeComplexChoice(Element parent, XMLElement el, String prefix) {
        XMLElement choosen = ((XMLComplexChoice) el).getChoosen();
        if (choosen != null) {
            if (choosen.toName().equals("parameterAssignment")) {
                encodeComplexElement(parent, choosen, prefix);
            } else if (choosen instanceof XMLComplexElement) {
                Element node = parent.getOwnerDocument().createElement(prefix + choosen.toName());
                parent.appendChild(node);
                encodeComplexElement(node, choosen, prefix);
            } else if (choosen instanceof XMLFactory) {
                encodeFactoryElement(parent, choosen, prefix);
            }
        }
    }

    public void encodeFactoryElement(Node parent, XMLElement el, String prefix) {
        Element node = null;
        for (XMLElement e : ((XMLFactory) el).toElements()) {
            node = parent.getOwnerDocument().createElement(prefix + e.toName());
            parent.appendChild(node);
            encodeComplexElement(node, e, prefix);
        }
    }

    public void encodeCollection(Element parent, XMLElement el, String prefix) {
        Element node = null;
        for (XMLElement e : ((XMLCollection) el).toElements()) {
            if (e instanceof XMLComplexElement) {
                node = parent.getOwnerDocument().createElement(prefix + e.toName());
                parent.appendChild(node);
                encodeComplexElement(node, e, prefix);
            } else if (e instanceof XMLTextElement) {
                encodeText(parent, e, prefix);
            }
        }
    }

    public void encodeExtensionElement(Element parent, XMLElement el) {
        Element node = null;
        for (XMLElement e : ((XMLExtensionElement) el).toElements()) {
            if (e instanceof XMLAttribute) {
                encodeAttribute(parent, e);
            } else if (e.toName().equals("#text")) {
                Node textNode = parent.getOwnerDocument().createTextNode(e.toValue());
                parent.appendChild(textNode);
            } else if (e.toName().equals("#cdata-section")) {
                Node textNode = parent.getOwnerDocument().createCDATASection(e.toValue());
                parent.appendChild(textNode);
            } else {
                node = parent.getOwnerDocument().createElement(e.toName());
                parent.appendChild(node);
                encodeExtensionElement(node, e);
            }
        }
    }

    public void encodeText(Element parent, XMLElement el, String prefix) {
        if (!el.isEmpty()) {
            Node textNode = null;
            Node node = parent.getOwnerDocument().createElement(prefix + el.toName());
            if (el.toName().equals("script")) {
                textNode = parent.getOwnerDocument().createCDATASection(el.toValue());
            } else {
                textNode = parent.getOwnerDocument().createTextNode(el.toValue());
            }
            node.appendChild(textNode);
            parent.appendChild(node);
        }
    }

    public void encodeAttribute(Element parent, XMLElement el) {
        if (!el.isEmpty()) {
            Attr node = parent.getOwnerDocument().createAttribute(el.toName());
            node.setValue(el.toValue().trim());
            parent.setAttributeNode(node);
        }
    }

    public void decode(Element node, Definitions defs) {
        NamedNodeMap attribs = node.getAttributes();
        Map<String, String> nss = defs.getNamespaces();
        for (int i = 0; i < attribs.getLength(); i++) {
            Node n = attribs.item(i);
            String nn = n.getNodeName();
            if (nn.startsWith("xmlns:")) {
                nss.put(n.getNodeValue(), nn.substring(6, nn.length()));
            }
        }
        decode(node, (XMLComplexElement) defs);
        BPMNModelUtils.refreshTypes(defs);
    }

    public void decode(Node node, XMLComplexElement cel) {
        if (node == null || (!node.hasChildNodes() && !node.hasAttributes())) return;
        if (node.hasAttributes()) {
            NamedNodeMap attribs = node.getAttributes();
            for (int i = 0; i < attribs.getLength(); ++i) {
                Node attrib = attribs.item(i);
                XMLAttribute attr = (XMLAttribute) cel.get(attrib.getNodeName());
                if (attr == null) {
                    attr = new XMLAttribute(cel, attrib.getNodeName());
                    cel.add(attr);
                }
                decode(attrib, attr);
            }
        }
        if (node.hasChildNodes()) {
            if (cel instanceof Documentation || cel instanceof Expression) {
                cel.setValue(BPMNModelUtils.getChildNodesContent(node));
            }
            for (XMLElement el : cel.getXMLElements()) {
                String elName = el.toName();
                if (el instanceof XMLComplexElement) {
                    Node child = BPMNModelUtils.getChildByName(node, elName);
                    decode(child, (XMLComplexElement) el);
                } else if (el instanceof XMLFactory) {
                    decode(node, (XMLFactory) el);
                } else if (el instanceof XMLCollection) {
                    decode(node, (XMLCollection) el);
                } else if (el instanceof XMLComplexChoice) {
                    decode(node, (XMLComplexChoice) el);
                } else if (el instanceof XMLTextElement) {
                    Node child = BPMNModelUtils.getChildByName(node, elName);
                    decode(child, (XMLTextElement) el);
                } else if (el instanceof XMLExtensionElement) {
                    Node child = BPMNModelUtils.getChildByName(node, elName);
                    decode(child, (XMLExtensionElement) el);
                } else {
                    Node child = BPMNModelUtils.getChildByName(node, elName);
                    decode(child, el);
                }
            }
        }
    }

    public void decode(Node node, XMLComplexChoice el) {
        List<XMLElement> ch = el.getChoices();
        for (int i = 0; i < ch.size(); i++) {
            XMLElement chc = ch.get(i);
            String chname = chc.toName();
            if (chname.equals("parameterAssignment")) {
                decode(node, (XMLComplexElement) chc);
            } else {
                Node child = BPMNModelUtils.getChildByName(node, chname);
                if (child != null) {
                    if (chc instanceof ResourceAssignmentExpression || chc instanceof Expressions) {
                        if (chc instanceof XMLComplexElement) {
                            decode(child, (XMLComplexElement) chc);
                        } else {
                            decode(node, (Expressions) chc);
                        }
                    } else {
                        decode(child, (XMLComplexElement) chc);
                    }
                    el.setChoosen(chc);
                    break;
                }
            }
        }
    }

    public void decode(Node node, XMLFactory cel) {
        if (node == null || !node.hasChildNodes()) return;
        XMLElement newOne = null;
        NodeList children = node.getChildNodes();
        int lng = children.getLength();
        for (int i = 0; i < lng; i++) {
            Node child = children.item(i);
            if (child.getLocalName() != null) {
                cel.setType(child.getLocalName());
                newOne = cel.generateNewElement();
                if (newOne != null) {
                    decode(child, (XMLComplexElement) newOne);
                    cel.add(newOne);
                }
            }
        }
    }

    public void decode(Node node, XMLCollection cel) {
        if (node == null || !node.hasChildNodes()) return;
        XMLElement newOne = null;
        String elName = cel.getElementName();
        NodeList children = node.getChildNodes();
        int lng = children.getLength();
        for (int i = 0; i < lng; i++) {
            Node child = children.item(i);
            if (child.getLocalName() != null) {
                if (child.getLocalName().equals(elName)) {
                    newOne = cel.generateNewElement();
                    if (newOne instanceof XMLComplexElement) {
                        decode(child, (XMLComplexElement) newOne);
                    } else if (newOne instanceof XMLTextElement) {
                        decode(child, (XMLTextElement) newOne);
                    }
                    cel.add(newOne);
                }
            }
        }
    }

    public void decode(Node node, XMLExtensionElement cel) {
        if (node == null || (!node.hasChildNodes() && !node.hasAttributes())) return;
        if (node.hasAttributes()) {
            NamedNodeMap attribs = node.getAttributes();
            for (int i = 0; i < attribs.getLength(); ++i) {
                Node attrib = attribs.item(i);
                XMLAttribute attr = new XMLAttribute(cel, attrib.getNodeName());
                cel.addAttribute(attr);
                decode(attrib, attr);
            }
        }
        if (node.hasChildNodes()) {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); ++i) {
                Node child = children.item(i);
                if (child.getNodeName() != null) {
                    XMLExtensionElement childElement = new XMLExtensionElement(cel, child.getNodeName());
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        String content = ((Text) child).getData();
                        if (content != null && content.trim().length() != 0) {
                            childElement = new XMLExtensionElement(cel, child.getNodeName(), content);
                            cel.addChildElement(childElement);
                        }
                    } else if (child.getNodeType() == Node.CDATA_SECTION_NODE) {
                        String content = ((CDATASection) child).getData();
                        if (content != null && content.trim().length() != 0) {
                            childElement = new XMLExtensionElement(cel, child.getNodeName(), content);
                            cel.addChildElement(childElement);
                        }
                    } else {
                        cel.addChildElement(childElement);
                        decode(child, childElement);
                    }
                }
            }
        }
    }

    public void decode(Node node, XMLTextElement el) {
        setTextFieldValue(node, el);
    }

    public void decode(Node node, XMLElement el) {
        setFieldValue(node, el);
    }

    public void decode(Node node, XMLAttribute el) {
        if (el != null) {
            if (el.toName().equals("id") && node.getNodeValue() != null && node.getNodeValue().length() != 0) {
                bpmnElementMap.put(node.getNodeValue(), el.getParent());
            }
            setFieldValue(node, el);
        }
    }

    public void setTextFieldValue(Node node, XMLTextElement el) {
        if (node != null) {
            el.setValue(BPMNModelUtils.getChildNodesContent(node));
        }
    }

    public void setFieldValue(Node node, XMLElement el) {
        if (node != null) {
            String newVal = node.getNodeValue();
            if (newVal != null) {
                el.setValue(newVal);
            }
        }
    }

    public Map<String, XMLElement> getBPMNElementMap() {
        return bpmnElementMap;
    }

    public static void main(String[] args) {
        System.out.println("BPMN 2.0 Model Version " + BPMNModelCodec.VERSION);
    }
}
