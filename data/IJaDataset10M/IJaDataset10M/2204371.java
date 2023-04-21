package org.apache.ws.jaxme.generator.sg.impl;

import org.apache.ws.jaxme.generator.sg.AttributeSG;
import org.apache.ws.jaxme.generator.sg.AttributeSGChain;
import org.apache.ws.jaxme.generator.sg.Context;
import org.apache.ws.jaxme.generator.sg.ParticleSG;
import org.apache.ws.jaxme.generator.sg.PropertySG;
import org.apache.ws.jaxme.generator.sg.PropertySGChain;
import org.apache.ws.jaxme.generator.sg.SGFactory;
import org.apache.ws.jaxme.generator.sg.SGlet;
import org.apache.ws.jaxme.generator.sg.SchemaSG;
import org.apache.ws.jaxme.generator.sg.TypeSG;
import org.apache.ws.jaxme.js.DirectAccessible;
import org.apache.ws.jaxme.js.JavaMethod;
import org.apache.ws.jaxme.xs.XSAttribute;
import org.apache.ws.jaxme.xs.XSType;
import org.apache.ws.jaxme.xs.XSWildcard;
import org.apache.ws.jaxme.xs.xml.XsNamespaceList;
import org.apache.ws.jaxme.xs.xml.XsQName;
import org.apache.ws.jaxme.xs.xml.XsTWildcard;
import org.apache.ws.jaxme.xs.xml.XsTWildcard.ProcessContents;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
class DFDLJAXBAttributeSG extends JAXBAttributeSG implements AttributeSGChain {

    private final boolean isRequired, isWildcard;

    private final XsQName name;

    private final XsNamespaceList namespaceList;

    private final XsTWildcard.ProcessContents processContents;

    private XSAttribute xsAttribute;

    private XSWildcard xsWildcard;

    private final TypeSG typeSG;

    private PropertySG propertySG;

    protected DFDLJAXBAttributeSG(SchemaSG pSchema, XSAttribute pAttribute, Context pClassContext) throws SAXException {
        super(pSchema, pAttribute, pClassContext);
        xsAttribute = pAttribute;
        xsWildcard = null;
        isRequired = !pAttribute.isOptional();
        name = pAttribute.getName();
        XSType type = pAttribute.getType();
        if (type == null) {
            throw new IllegalStateException("The attribute type must not be null.");
        }
        if (type.isGlobal()) {
            typeSG = getFactory().getTypeSG(type);
            if (typeSG == null) {
                throw new IllegalStateException("Unknown global type: " + type.getName());
            }
        } else {
            typeSG = getFactory().getTypeSG(pAttribute.getType(), pClassContext, name);
        }
        isWildcard = false;
        namespaceList = null;
        processContents = null;
    }

    protected DFDLJAXBAttributeSG(SchemaSG pSchema, XSWildcard pWildcard, Context pClassContext) throws SAXException {
        super(pSchema, pWildcard, pClassContext);
        xsWildcard = pWildcard;
        xsAttribute = null;
        isRequired = false;
        name = null;
        typeSG = null;
        isWildcard = true;
        namespaceList = pWildcard.getNamespaceList();
        processContents = pWildcard.getProcessContents();
    }

    public Object newPropertySGChain(AttributeSG pController) {
        PropertySGChain result;
        if (xsWildcard != null) {
            result = new AnyAttributePropertySG(pController, xsWildcard);
            xsWildcard = null;
        } else if (xsAttribute != null) {
            result = new DFDLJAXBPropertySG(pController, xsAttribute);
        } else {
            throw new IllegalStateException("PropertySG is already created.");
        }
        return result;
    }

    public XSAttribute getXSAttribute(AttributeSG pController) {
        return xsAttribute;
    }

    public void init(AttributeSG pController) throws SAXException {
        PropertySGChain chain = (PropertySGChain) pController.newPropertySGChain();
        propertySG = new PropertySGImpl(chain);
        propertySG.init();
    }

    public PropertySG getPropertySG(AttributeSG pController) {
        return propertySG;
    }

    public TypeSG getTypeSG(AttributeSG pController) {
        return typeSG;
    }

    public SGFactory getFactory(AttributeSG pController) {
        return getFactory();
    }

    public SchemaSG getSchema(AttributeSG pController) {
        return getSchema();
    }

    public Locator getLocator(AttributeSG pController) {
        return getLocator();
    }

    public XsQName getName(AttributeSG pController) {
        return name;
    }

    public boolean isRequired(AttributeSG pAttrController) {
        return isRequired;
    }

    public void forAllValues(AttributeSG pController, JavaMethod pMethod, DirectAccessible pElement, SGlet pSGlet) throws SAXException {
        pController.getPropertySG().forAllValues(pMethod, pElement, pSGlet);
    }

    public void forAllNonNullValues(AttributeSG pController, JavaMethod pMethod, DirectAccessible pElement, SGlet pSGlet) throws SAXException {
        pController.getPropertySG().forAllNonNullValues(pMethod, pElement, pSGlet);
    }

    public boolean isWildcard(AttributeSG pController) {
        return isWildcard;
    }

    public XsNamespaceList getNamespaceList(AttributeSG pController) {
        return namespaceList;
    }

    public ProcessContents getProcessContents(AttributeSG pController) {
        return processContents;
    }
}
