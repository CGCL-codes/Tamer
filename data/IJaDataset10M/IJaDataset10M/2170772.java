package org.apache.ws.jaxme.generator.sg.impl;

import org.apache.ws.jaxme.generator.sg.Context;
import org.apache.ws.jaxme.generator.sg.GroupSG;
import org.apache.ws.jaxme.generator.sg.ObjectSG;
import org.apache.ws.jaxme.generator.sg.ParticleSG;
import org.apache.ws.jaxme.generator.sg.ParticleSGChain;
import org.apache.ws.jaxme.generator.sg.PropertySG;
import org.apache.ws.jaxme.generator.sg.PropertySGChain;
import org.apache.ws.jaxme.generator.sg.SGFactory;
import org.apache.ws.jaxme.generator.sg.SGlet;
import org.apache.ws.jaxme.generator.sg.TypeSG;
import org.apache.ws.jaxme.js.DirectAccessible;
import org.apache.ws.jaxme.js.JavaMethod;
import org.apache.ws.jaxme.xs.XSAny;
import org.apache.ws.jaxme.xs.XSElement;
import org.apache.ws.jaxme.xs.XSParticle;
import org.apache.ws.jaxme.xs.impl.XSUtil;
import org.apache.ws.jaxme.xs.jaxb.JAXBElement;
import org.apache.ws.jaxme.xs.jaxb.JAXBProperty;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
public class DFDLJAXBParticleSG extends JAXBParticleSG {

    private final int minOccurs, maxOccurs;

    private final XSParticle.Type type;

    private PropertySG propertySG;

    private final GroupSG groupSG;

    private final ObjectSG objectSG;

    private final Locator locator;

    private XSElement element;

    private XSAny wildcard;

    /** <p>Creates a new instance of JAXBParticleSG.java.</p>
     */
    public DFDLJAXBParticleSG(GroupSG pGroup, XSParticle pParticle, Context pClassContext) throws SAXException {
        super(pGroup, pParticle, pClassContext);
        minOccurs = pParticle.getMinOccurs();
        maxOccurs = pParticle.getMaxOccurs();
        type = pParticle.getType();
        SGFactory factory = pGroup.getFactory();
        if (pParticle.isGroup()) {
            groupSG = factory.getGroupSG(pParticle.getGroup(), pClassContext);
            objectSG = null;
        } else if (pParticle.isElement()) {
            element = pParticle.getElement();
            if (element.isGlobal()) {
                objectSG = factory.getObjectSG(element);
            } else {
                objectSG = factory.getObjectSG(element, pClassContext);
            }
            groupSG = null;
        } else if (pParticle.isWildcard()) {
            objectSG = factory.getObjectSG(pParticle.getWildcard(), pClassContext);
            groupSG = null;
            wildcard = pParticle.getWildcard();
        } else {
            throw new IllegalStateException("Particle is neither group, nor element, or wildcard.");
        }
        locator = pParticle.getLocator();
    }

    public Object newPropertySGChain(ParticleSG pController) throws SAXException {
        PropertySGChain result;
        if (element != null) {
            result = new DFDLJAXBPropertySG(objectSG, element);
        } else if (wildcard != null) {
            result = new AnyElementPropertySG(objectSG, wildcard);
            wildcard = null;
        } else {
            throw new IllegalStateException("A new PropertySGChain cannot be obtained.");
        }
        if (maxOccurs > 1 || maxOccurs == -1) {
            PropertySG pSG = new PropertySGImpl(result);
            if ("indexed".equals(pSG.getCollectionType())) {
                result = new ArrayPropertySG(result, objectSG, minOccurs, maxOccurs);
            } else {
                result = new DFDLMultiplePropertySG(result, objectSG, minOccurs, maxOccurs);
            }
        }
        return result;
    }

    public void init(ParticleSG pController) throws SAXException {
        if (element != null || wildcard != null) {
            PropertySGChain chain = (PropertySGChain) pController.newPropertySGChain();
            propertySG = new PropertySGImpl(chain);
            propertySG.init();
        }
    }

    public Locator getLocator(ParticleSG pController) {
        return locator;
    }

    public int getMinOccurs(ParticleSG pController) {
        return minOccurs;
    }

    public int getMaxOccurs(ParticleSG pController) {
        return maxOccurs;
    }

    public boolean isMultiple(ParticleSG pController) {
        return maxOccurs == -1 || maxOccurs > 1;
    }

    public boolean isGroup(ParticleSG pController) {
        return type.equals(XSParticle.GROUP);
    }

    public boolean isElement(ParticleSG pController) {
        return type.equals(XSParticle.ELEMENT);
    }

    public XSElement getElement(ParticleSG pController) {
        return element;
    }

    public boolean isWildcard(ParticleSG pController) {
        return type.equals(XSParticle.WILDCARD);
    }

    public PropertySG getPropertySG(ParticleSG pController) throws SAXException {
        if (propertySG == null) {
            if (element != null || wildcard != null) {
                PropertySGChain chain = (PropertySGChain) pController.newPropertySGChain();
                propertySG = new PropertySGImpl(chain);
                propertySG.init();
            } else {
                throw new IllegalStateException("This particle has no PropertySG.");
            }
        }
        return propertySG;
    }

    public ObjectSG getObjectSG(ParticleSG pController) {
        if (objectSG == null) {
            throw new IllegalStateException("This particle is neither an element nor a wildcard.");
        }
        return objectSG;
    }

    public GroupSG getGroupSG(ParticleSG pController) {
        if (groupSG == null) {
            throw new IllegalStateException("This particle is no group.");
        }
        return groupSG;
    }

    public void forAllNonNullValues(ParticleSG pController, JavaMethod pMethod, DirectAccessible pElement, SGlet pSGlet) throws SAXException {
        if (pController.isElement()) {
            PropertySG pSG = pController.getPropertySG();
            boolean hasIsSetMethod = pSG.hasIsSetMethod();
            if (hasIsSetMethod) {
                pMethod.addIf(pSG.getXMLIsSetMethodName(), "()");
            }
            TypeSG typeSG = pController.getObjectSG().getTypeSG();
            Object v = pController.getPropertySG().getValue(pElement);
            if (typeSG.isComplex()) {
                pSGlet.generate(pMethod, v);
            } else {
                typeSG.getSimpleTypeSG().forAllValues(pMethod, v, pSGlet);
            }
            if (hasIsSetMethod) {
                pMethod.addEndIf();
            }
        } else {
            throw new IllegalStateException("Wildcards and subgroups are not yet supported.");
        }
    }
}
