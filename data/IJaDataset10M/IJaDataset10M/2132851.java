package org.akrogen.core.xml.entitygen.impl.xs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.akrogen.core.xml.entitygen.xs.ExtendedXSModel;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeGroupDefinition;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSModelGroupDefinition;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSNamespaceItemList;
import org.apache.xerces.xs.XSNotationDeclaration;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTerm;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ExtendedXSModelImpl implements ExtendedXSModel {

    private XSModel model;

    private XSElementDeclaration elementDeclarationRoot;

    private Map elementDeclarations;

    public ExtendedXSModelImpl(XSModel model) {
        this.model = model;
    }

    public XSObjectList getAnnotations() {
        return model.getAnnotations();
    }

    public XSAttributeDeclaration getAttributeDeclaration(String name, String namespace) {
        return model.getAttributeDeclaration(name, namespace);
    }

    public XSAttributeGroupDefinition getAttributeGroup(String name, String namespace) {
        return model.getAttributeGroup(name, namespace);
    }

    public XSNamedMap getComponents(short objectType) {
        return model.getComponents(objectType);
    }

    public XSNamedMap getComponentsByNamespace(short objectType, String namespace) {
        return model.getComponentsByNamespace(objectType, namespace);
    }

    public XSElementDeclaration getElementDeclaration(String name, String namespace) {
        return model.getElementDeclaration(name, namespace);
    }

    public XSModelGroupDefinition getModelGroupDefinition(String name, String namespace) {
        return model.getModelGroupDefinition(name, namespace);
    }

    public XSNamespaceItemList getNamespaceItems() {
        return model.getNamespaceItems();
    }

    public StringList getNamespaces() {
        return model.getNamespaces();
    }

    public XSNotationDeclaration getNotationDeclaration(String name, String namespace) {
        return model.getNotationDeclaration(name, namespace);
    }

    public XSObjectList getSubstitutionGroup(XSElementDeclaration head) {
        return null;
    }

    public XSTypeDefinition getTypeDefinition(String name, String namespace) {
        return model.getTypeDefinition(name, namespace);
    }

    public XSElementDeclaration getElementDeclarationRoot() {
        computeIfNeed();
        return elementDeclarationRoot;
    }

    private void computeIfNeed() {
        if (elementDeclarations != null) return;
        elementDeclarations = new HashMap();
        XSNamedMap allElementDeclarations = model.getComponents(XSConstants.ELEMENT_DECLARATION);
        for (int i = 0; i < allElementDeclarations.getLength(); i++) {
            XSElementDeclaration elementDeclaration = (XSElementDeclaration) allElementDeclarations.item(i);
            compute(elementDeclaration);
        }
        if (!elementDeclarations.isEmpty()) {
            XSElementDeclaration firstElementDeclaration = null;
            for (Iterator iterator = elementDeclarations.keySet().iterator(); iterator.hasNext(); ) {
                firstElementDeclaration = (XSElementDeclaration) iterator.next();
                break;
            }
            elementDeclarationRoot = getParent(firstElementDeclaration);
        }
    }

    protected XSElementDeclaration getParent(XSElementDeclaration elementDeclaration) {
        for (Iterator iterator = elementDeclarations.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iterator.next();
            XSElementDeclaration child = (XSElementDeclaration) entry.getKey();
            if (!child.equals(elementDeclaration)) {
                List children = (List) entry.getValue();
                if (children.contains(elementDeclaration)) {
                    XSElementDeclaration parent = getParent(child);
                    if (parent == null) return child; else return parent;
                }
            }
        }
        return null;
    }

    private List getChildElementDeclarations(XSElementDeclaration elementDeclaration) {
        List childElementDeclarations = (List) elementDeclarations.get(elementDeclaration);
        if (childElementDeclarations == null) {
            childElementDeclarations = new ArrayList();
            elementDeclarations.put(elementDeclaration, childElementDeclarations);
        }
        return childElementDeclarations;
    }

    private void compute(XSElementDeclaration elementDeclaration) {
        XSComplexTypeDefinition complexTypeDefinition = (XSComplexTypeDefinition) elementDeclaration.getTypeDefinition();
        List childElementDeclarations = getChildElementDeclarations(elementDeclaration);
        if (complexTypeDefinition.getContentType() == XSComplexTypeDefinition.CONTENTTYPE_ELEMENT) {
            XSParticle particle = complexTypeDefinition.getParticle();
            XSTerm term = particle.getTerm();
            if (term instanceof XSModelGroup) {
                XSModelGroup modelGroup = (XSModelGroup) term;
                XSObjectList particles = modelGroup.getParticles();
                for (int i = 0; i < particles.getLength(); i++) {
                    XSParticle p = (XSParticle) particles.item(i);
                    XSTerm t = p.getTerm();
                    if (t instanceof XSElementDeclaration) {
                        XSElementDeclaration e = (XSElementDeclaration) t;
                        if (!childElementDeclarations.contains(e)) childElementDeclarations.add(e);
                        compute(e);
                    }
                }
                return;
            }
        }
    }

    public XSElementDeclaration getElementDeclaration(Element element) {
        Element parent = getParentElement(element);
        return getElementDeclaration(parent, element.getLocalName(), element.getNamespaceURI());
    }

    public XSElementDeclaration getElementDeclaration(Element parent, String localName, String uri) {
        if (parent == null) {
            return model.getElementDeclaration(localName, uri);
        }
        XSElementDeclaration elementDeclaration = null;
        List elements = createElements(parent);
        for (Iterator iterator = elements.iterator(); iterator.hasNext(); ) {
            Element element = (Element) iterator.next();
            Element parentElement = getParentElement(element);
            elementDeclaration = getElementDeclaration(parentElement, element.getLocalName(), element.getNamespaceURI());
        }
        if (elementDeclaration != null) {
            XSTypeDefinition typeDefinition = elementDeclaration.getTypeDefinition();
            if (typeDefinition instanceof XSComplexTypeDefinition) {
                XSComplexTypeDefinition complexTypeDefinition = (XSComplexTypeDefinition) typeDefinition;
                XSParticle particle = complexTypeDefinition.getParticle();
                XSTerm term = particle.getTerm();
                if (term instanceof XSModelGroup) {
                    XSModelGroup modelGroup = (XSModelGroup) term;
                    XSObjectList objectList = modelGroup.getParticles();
                    for (int i = 0; i < objectList.getLength(); i++) {
                        XSObject object = objectList.item(i);
                        if (object instanceof XSParticle) {
                            XSParticle particle2 = (XSParticle) object;
                            XSTerm term2 = particle2.getTerm();
                            if (term2 instanceof XSElementDeclaration) {
                                XSElementDeclaration elementDeclaration2 = (XSElementDeclaration) term2;
                                if (elementDeclaration2.getName().equals(localName)) {
                                    if (uri != null) {
                                        if (uri.equals(elementDeclaration2.getNamespace())) return elementDeclaration2;
                                    } else {
                                        if (elementDeclaration2.getNamespace() == null) return elementDeclaration2;
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

    public XSParticle getParticle(XSElementDeclaration elementDeclaration) {
        XSComplexTypeDefinition complexTypeDefinition = elementDeclaration.getEnclosingCTDefinition();
        if (complexTypeDefinition != null) {
            String elementName = elementDeclaration.getName();
            XSTerm term = complexTypeDefinition.getParticle().getTerm();
            if (term instanceof XSModelGroup) {
                XSModelGroup modelGroup = (XSModelGroup) term;
                XSObjectList objectList = modelGroup.getParticles();
                for (int i = 0; i < objectList.getLength(); i++) {
                    XSParticle object = (XSParticle) objectList.item(i);
                    XSTerm t = object.getTerm();
                    if (t != null) {
                        if (elementName.equals(t.getName())) return (XSParticle) object;
                    }
                }
            }
        }
        return null;
    }

    private List createElements(Element parent) {
        List elements = new ArrayList();
        while (parent != null) {
            elements.add(0, parent);
            parent = getParentElement(parent);
        }
        return elements;
    }

    private Element getParentElement(Element element) {
        Node node = element.getParentNode();
        if (node.getNodeType() == Node.ELEMENT_NODE) return (Element) node;
        return null;
    }
}
