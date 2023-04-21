package alice.cartagowsapi;

import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.llom.util.AXIOMUtil;

public class XMLibJason {

    private OMFactory factory;

    public XMLibJason() {
        factory = OMAbstractFactory.getOMFactory();
    }

    public OMElement buildElement(String nodeName) {
        return factory.createOMElement(new QName(nodeName));
    }

    public OMElement buildElement(String nodeName, String nodeContent) {
        OMElement el = factory.createOMElement(new QName(nodeName));
        el.setText(nodeContent);
        return el;
    }

    public OMElement buildElement(String nodeName, String namespace, String nsPrefix) {
        OMNamespace ns = factory.createOMNamespace(namespace, nsPrefix);
        return factory.createOMElement(nodeName, ns);
    }

    public OMElement buildElement(String nodeName, String nodeContent, String namespace, String nsPrefix) {
        OMNamespace ns = factory.createOMNamespace(namespace, nsPrefix);
        OMElement el = factory.createOMElement(nodeName, ns);
        el.setText(nodeContent);
        return el;
    }

    public OMElement addChildNode(OMElement parent, OMElement child) {
        parent.addChild(child);
        return parent;
    }

    public String getElementValue(String elemName, OMElement node) {
        Iterator<OMElement> it = (Iterator<OMElement>) node.getChildElements();
        while (it.hasNext()) {
            OMElement el = it.next();
            if (el.getLocalName().equals(elemName)) return el.getText();
        }
        return null;
    }

    public String getElementValue(String firstElemName, String secondElemName, OMElement node) {
        Iterator<OMElement> it = (Iterator<OMElement>) node.getChildElements();
        while (it.hasNext()) {
            OMElement el = it.next();
            if (el.getLocalName().equals(firstElemName)) {
                Iterator<OMElement> it2 = el.getChildElements();
                while (it2.hasNext()) {
                    OMElement el2 = it2.next();
                    if (el2.getLocalName().equals(secondElemName)) return el2.getText();
                }
            }
        }
        return null;
    }

    public String getElementValue(String firstElemName, String secondElemName, String thirdElemName, OMElement node) {
        Iterator<OMElement> it = (Iterator<OMElement>) node.getChildElements();
        while (it.hasNext()) {
            OMElement el = it.next();
            if (el.getLocalName().equals(firstElemName)) {
                Iterator<OMElement> it2 = el.getChildElements();
                while (it2.hasNext()) {
                    OMElement el2 = it2.next();
                    if (el2.getLocalName().equals(secondElemName)) {
                        Iterator<OMElement> it3 = el2.getChildElements();
                        while (it3.hasNext()) {
                            OMElement el3 = it3.next();
                            if (el3.getLocalName().equals(thirdElemName)) return el3.getText();
                        }
                    }
                }
            }
        }
        return null;
    }

    public String getElementValue(String firstElemName, String secondElemName, String thirdElemName, String forthElemName, OMElement node) {
        Iterator<OMElement> it = (Iterator<OMElement>) node.getChildElements();
        while (it.hasNext()) {
            OMElement el = it.next();
            if (el.getLocalName().equals(firstElemName)) {
                Iterator<OMElement> it2 = el.getChildElements();
                while (it2.hasNext()) {
                    OMElement el2 = it2.next();
                    if (el2.getLocalName().equals(secondElemName)) {
                        Iterator<OMElement> it3 = el2.getChildElements();
                        while (it3.hasNext()) {
                            OMElement el3 = it3.next();
                            if (el3.getLocalName().equals(thirdElemName)) {
                                Iterator<OMElement> it4 = el3.getChildElements();
                                while (it4.hasNext()) {
                                    OMElement el4 = it4.next();
                                    if (el4.getLocalName().equals(forthElemName)) {
                                        return el4.getText();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public OMElement getElement(String elemName, OMElement node) {
        Iterator<OMElement> it = (Iterator<OMElement>) node.getChildElements();
        while (it.hasNext()) {
            OMElement el = it.next();
            if (el.getLocalName().equals(elemName)) return el;
        }
        return null;
    }

    public OMElement getElement(String firstElemName, String secondElemName, OMElement node) {
        Iterator<OMElement> it = (Iterator<OMElement>) node.getChildElements();
        while (it.hasNext()) {
            OMElement el = it.next();
            if (el.getLocalName().equals(firstElemName)) {
                Iterator<OMElement> it2 = el.getChildElements();
                while (it2.hasNext()) {
                    OMElement el2 = it2.next();
                    if (el2.getLocalName().equals(secondElemName)) return el2;
                }
            }
        }
        return null;
    }

    public OMElement getElement(String firstElemName, String secondElemName, String thirdElemName, OMElement node) {
        Iterator<OMElement> it = (Iterator<OMElement>) node.getChildElements();
        while (it.hasNext()) {
            OMElement el = it.next();
            if (el.getLocalName().equals(firstElemName)) {
                Iterator<OMElement> it2 = el.getChildElements();
                while (it2.hasNext()) {
                    OMElement el2 = it2.next();
                    if (el2.getLocalName().equals(secondElemName)) {
                        Iterator<OMElement> it3 = el2.getChildElements();
                        while (it3.hasNext()) {
                            OMElement el3 = it3.next();
                            if (el3.getLocalName().equals(thirdElemName)) return el3;
                        }
                    }
                }
            }
        }
        return null;
    }

    public OMElement getElement(String firstElemName, String secondElemName, String thirdElemName, String forthElemName, OMElement node) {
        Iterator<OMElement> it = (Iterator<OMElement>) node.getChildElements();
        while (it.hasNext()) {
            OMElement el = it.next();
            if (el.getLocalName().equals(firstElemName)) {
                Iterator<OMElement> it2 = el.getChildElements();
                while (it2.hasNext()) {
                    OMElement el2 = it2.next();
                    if (el2.getLocalName().equals(secondElemName)) {
                        Iterator<OMElement> it3 = el2.getChildElements();
                        while (it3.hasNext()) {
                            OMElement el3 = it3.next();
                            if (el3.getLocalName().equals(thirdElemName)) {
                                Iterator<OMElement> it4 = el3.getChildElements();
                                while (it4.hasNext()) {
                                    OMElement el4 = it4.next();
                                    if (el4.getLocalName().equals(forthElemName)) {
                                        return el4;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public ArrayList<String> getChildsValue(String childElemName, OMElement node) {
        ArrayList<String> returnValue = new ArrayList<String>();
        Iterator<OMElement> it = (Iterator<OMElement>) node.getChildrenWithName(new QName(childElemName));
        while (it.hasNext()) {
            String childValue = it.next().getText();
            returnValue.add(childValue);
        }
        return returnValue;
    }

    public ArrayList<String> getChildsValue(String firstElemName, String childElemName, OMElement node) {
        ArrayList<String> returnValue = new ArrayList<String>();
        Iterator<OMElement> it = (Iterator<OMElement>) node.getChildElements();
        while (it.hasNext()) {
            OMElement el = it.next();
            if (el.getLocalName().equals(firstElemName)) {
                Iterator<OMElement> it2 = (Iterator<OMElement>) el.getChildrenWithName(new QName(childElemName));
                while (it2.hasNext()) {
                    String childValue = it2.next().getText();
                    returnValue.add(childValue);
                }
            }
        }
        return returnValue;
    }

    public OMElement buildElementFromString(String msg) throws Exception {
        return AXIOMUtil.stringToOM(msg);
    }

    public String XMLEementToString(OMElement em) {
        return em.toString();
    }
}
