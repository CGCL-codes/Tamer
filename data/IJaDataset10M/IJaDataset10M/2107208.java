package org.apache.ws.jaxme.xs.jaxb.impl;

import org.apache.ws.jaxme.xs.XSAttribute;
import org.apache.ws.jaxme.xs.XSElement;
import org.apache.ws.jaxme.xs.XSEnumeration;
import org.apache.ws.jaxme.xs.XSGroup;
import org.apache.ws.jaxme.xs.XSObject;
import org.apache.ws.jaxme.xs.XSSchema;
import org.apache.ws.jaxme.xs.XSSimpleType;
import org.apache.ws.jaxme.xs.XSType;
import org.apache.ws.jaxme.xs.impl.XSObjectFactoryImpl;
import org.apache.ws.jaxme.xs.parser.XSContext;
import org.apache.ws.jaxme.xs.xml.XsEChoice;
import org.apache.ws.jaxme.xs.xml.XsEEnumeration;
import org.apache.ws.jaxme.xs.xml.XsEList;
import org.apache.ws.jaxme.xs.xml.XsERestriction;
import org.apache.ws.jaxme.xs.xml.XsESchema;
import org.apache.ws.jaxme.xs.xml.XsESequence;
import org.apache.ws.jaxme.xs.xml.XsETopLevelSimpleType;
import org.apache.ws.jaxme.xs.xml.XsEUnion;
import org.apache.ws.jaxme.xs.xml.XsTAll;
import org.apache.ws.jaxme.xs.xml.XsTAttribute;
import org.apache.ws.jaxme.xs.xml.XsTComplexType;
import org.apache.ws.jaxme.xs.xml.XsTElement;
import org.apache.ws.jaxme.xs.xml.XsTGroupRef;
import org.apache.ws.jaxme.xs.xml.XsTLocalComplexType;
import org.apache.ws.jaxme.xs.xml.XsTLocalSimpleType;
import org.apache.ws.jaxme.xs.xml.XsTNamedGroup;
import org.apache.ws.jaxme.xs.xml.XsTSimpleRestrictionType;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
public class JAXBObjectFactoryImpl extends XSObjectFactoryImpl {

    public XSSimpleType newXSAtomicType(XSType pResultType, XSType pRestrictedType, XsERestriction pRestriction) throws SAXException {
        return new JAXBSimpleTypeImpl(pResultType, super.newXSAtomicType(pResultType, pRestrictedType, pRestriction));
    }

    public XSSimpleType newXSAtomicType(XSType pResultType, XSType pRestrictedType, XsTSimpleRestrictionType pRestriction) throws SAXException {
        return new JAXBSimpleTypeImpl(pResultType, super.newXSAtomicType(pResultType, pRestrictedType, pRestriction));
    }

    public XSAttribute newXSAttribute(XSObject pParent, XsTAttribute pAttribute) throws SAXException {
        return new JAXBAttributeImpl(pParent, pAttribute);
    }

    public XSEnumeration newXSEnumeration(XSObject pParent, XsEEnumeration pEnumeration) throws SAXException {
        return new JAXBEnumerationImpl(pParent, pEnumeration);
    }

    public XSSchema newXSSchema(XSContext pContext, XsESchema pSchema) throws SAXException {
        return new JAXBSchemaImpl(pContext, pSchema);
    }

    public XSGroup newXSGroup(XSObject pParent, XsTAll pAll) throws SAXException {
        return new JAXBGroupImpl(pParent, pAll);
    }

    public XSGroup newXSGroup(XSObject pParent, XsEChoice pChoice) throws SAXException {
        return new JAXBGroupImpl(pParent, pChoice);
    }

    public XSGroup newXSGroup(XSObject pParent, XsESequence pSequence) throws SAXException {
        return new JAXBGroupImpl(pParent, pSequence);
    }

    public XSGroup newXSGroup(XSObject pParent, XsTGroupRef pGroupRef) throws SAXException {
        return new JAXBGroupImpl(pParent, pGroupRef);
    }

    public XSGroup newXSGroup(XSObject pParent, XsTNamedGroup pNamedGroup) throws SAXException {
        return new JAXBGroupImpl(pParent, pNamedGroup);
    }

    public XSSimpleType newXSListType(XSType pResultType, XsEList pList) throws SAXException {
        return new JAXBSimpleTypeImpl(pResultType, super.newXSListType(pResultType, pList));
    }

    public XSSimpleType newXSListType(XSType pResultType, XSType pRestrictedType, XsERestriction pRestriction) throws SAXException {
        return new JAXBSimpleTypeImpl(pResultType, super.newXSListType(pResultType, pRestrictedType, pRestriction));
    }

    public XSSimpleType newXSListType(XSType pResultType, XSType pRestrictedType, XsTSimpleRestrictionType pRestriction) throws SAXException {
        return new JAXBSimpleTypeImpl(pResultType, super.newXSListType(pResultType, pRestrictedType, pRestriction));
    }

    public XSType newXSType(XSObject pParent, XsETopLevelSimpleType pType) throws SAXException {
        return new JAXBTypeImpl(pParent, pType);
    }

    public XSType newXSType(XSObject pParent, XsTComplexType pType) throws SAXException {
        return new JAXBTypeImpl(pParent, pType);
    }

    public XSType newXSType(XSObject pParent, XsTLocalComplexType pType) throws SAXException {
        return new JAXBTypeImpl(pParent, pType);
    }

    public XSType newXSType(XSObject pParent, XsTLocalSimpleType pType) throws SAXException {
        return new JAXBTypeImpl(pParent, pType);
    }

    public XSType newXSType(XSObject pParent, XsTSimpleRestrictionType pType) throws SAXException {
        return new JAXBTypeImpl(pParent, pType);
    }

    public XSElement newXSElement(XSObject pParent, XsTElement pElement) throws SAXException {
        return new JAXBElementImpl(pParent, pElement);
    }

    public XSSimpleType newXSUnionType(XSType pResultType, XsEUnion pUnion) throws SAXException {
        return new JAXBSimpleTypeImpl(pResultType, super.newXSUnionType(pResultType, pUnion));
    }

    public XSSimpleType newXSUnionType(XSType pResultType, XSType pRestrictedType, XsERestriction pRestriction) throws SAXException {
        return new JAXBSimpleTypeImpl(pResultType, super.newXSUnionType(pResultType, pRestrictedType, pRestriction));
    }

    public XSSimpleType newXSUnionType(XSType pResultType, XSType pRestrictedType, XsTSimpleRestrictionType pRestriction) throws SAXException {
        return new JAXBSimpleTypeImpl(pResultType, super.newXSUnionType(pResultType, pRestrictedType, pRestriction));
    }
}
