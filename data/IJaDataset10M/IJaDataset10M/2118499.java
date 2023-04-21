package org.dbe.toolkit.pa.sdl;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.rpc.encoding.Deserializer;
import javax.xml.rpc.encoding.DeserializerFactory;
import javax.xml.rpc.encoding.SerializerFactory;
import org.apache.axis.Constants;
import org.apache.axis.encoding.SerializationContext;
import org.apache.axis.encoding.TypeMapping;
import org.apache.axis.encoding.ser.ElementSerializer;
import org.apache.axis.utils.Messages;
import org.apache.log4j.Logger;
import org.dbe.studio.editor.sdl.Type;
import org.dbe.toolkit.pa.utils.SdlUtil;
import org.exolab.castor.xml.schema.ComplexType;
import org.exolab.castor.xml.schema.ElementDecl;
import org.w3c.dom.CDATASection;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Custom serializer for axis that handles complex types over rpc. The base class,
 * <code>ElementSerializer</code> wasn't handling checking to see if we were
 * encoding the types via multiRef.
 */
public class LiteralSerializer extends ElementSerializer {

    /** for deployment logging purposes */
    private static final Logger logger = Logger.getLogger(LiteralSerializer.class.getName());

    /** prefix used when setting the xsi:type on the serialized multiRef element.
     * The SdlLiteralDeserializer will skip over namespace declaration with this prefix
     * to avoid the namespace declaration getting propagated when it's not needed. */
    public static final String TYPE_PREFIX = "dbe-lit-ser-type";

    /** SDL def object that contains the Schemas needed to serialize the Document correctly */
    private SDLDefinition mDef;

    /** serializer factory used for just-in-time type mapping registration */
    private SerializerFactory mSerializerFactory = null;

    /** deserializer factory used for just-in-time type mapping registration */
    private DeserializerFactory mDeserializerFactory = null;

    /** prefix for type assignment unique */
    private int mPrefixTypeCount = 0;

    /**
     * Constructor
     * @param aDef
     * @param aPart
     */
    public LiteralSerializer(SDLDefinition aDef) {
        logger.debug("LiteralSerializer()");
        mDef = aDef;
    }

    /**
     * @see org.apache.axis.encoding.Serializer#serialize(javax.xml.namespace.QName, org.xml.sax.Attributes, java.lang.Object, org.apache.axis.encoding.SerializationContext)
     */
    public void serialize(QName aName, Attributes aAttributes, Object aValue, SerializationContext aContext) throws IOException {
        logger.debug("serialize() name=" + aName);
        if ("multiRef".equals(aName.getLocalPart())) {
            if (!(aValue instanceof Document || aValue instanceof DefaultElementHolder)) {
                logger.error("cantSerialize01");
                throw new IOException(Messages.getMessage("cantSerialize01"));
            }
            Element element = getElement(aValue);
            QName type = getType(aValue, element);
            Type sdlType = getSdlType(type);
            if (sdlType == null) {
                logger.error("Type is not defined in a schema that is in scope:" + type);
                throw new IOException("Type is not defined in a schema that is in scope:" + type);
            } else {
                Attributes attribs = null;
                if (sdlType instanceof org.dbe.studio.editor.sdl.ComplexType && isArray((org.dbe.studio.editor.sdl.ComplexType) sdlType)) {
                    logger.info("got an complex sdl type to serialize !!!");
                    aContext.getPrefixForURI(Constants.URI_SOAP11_ENC, TYPE_PREFIX + mPrefixTypeCount++);
                    attribs = aContext.setTypeAttribute(aAttributes, new QName(Constants.URI_SOAP11_ENC, "Array"));
                } else {
                    aContext.getPrefixForURI(type.getNamespaceURI(), TYPE_PREFIX + mPrefixTypeCount++);
                    attribs = aContext.setTypeAttribute(aAttributes, type);
                }
                NamedNodeMap attrMap = element.getAttributes();
                if (attrMap != null && attrMap.getLength() > 0) {
                    AttributesImpl attrs = new AttributesImpl(attribs);
                    for (int i = 0; i < attrMap.getLength(); ++i) {
                        Node node = attrMap.item(i);
                        if (SdlUtil.W3C_XMLNS.equals(node.getNamespaceURI()) && node.getNodeName().startsWith("xmlns:")) {
                            if (SdlUtil.isNullOrEmpty(attribs.getValue(node.getNamespaceURI(), node.getLocalName()))) attrs.addAttribute(node.getNamespaceURI(), node.getLocalName(), node.getNodeName(), "CDATA", node.getNodeValue());
                        }
                    }
                    attribs = attrs;
                }
                aContext.startElement(aName, attribs);
                if (sdlType instanceof org.dbe.studio.editor.sdl.ComplexType) {
                    org.dbe.studio.editor.sdl.ComplexType complexType = (org.dbe.studio.editor.sdl.ComplexType) sdlType;
                    NodeList children = element.getChildNodes();
                    for (int i = 0; i < children.getLength(); i++) {
                        Node child = children.item(i);
                        if (child instanceof Element) {
                            Element childElement = (Element) child;
                            if (hasChildElements(childElement)) {
                                serializeComplexSdlType(aContext, complexType, childElement);
                            } else {
                                String childType = childElement.getAttribute(org.dbe.sdl.util.Constants.XMI_TYPE);
                                if (childType != null) {
                                    boolean typeAdded = addXmiType(childElement, childType, aContext);
                                    aContext.writeDOMElement(childElement);
                                    if (typeAdded) {
                                        removeXmiType(childElement);
                                    }
                                } else {
                                    aContext.writeDOMElement(childElement);
                                }
                            }
                        } else if (child instanceof CDATASection) {
                            aContext.writeString("<![CDATA[");
                            aContext.writeString(((Text) child).getData());
                            aContext.writeString("]]>");
                        } else if (child instanceof Comment) {
                            aContext.writeString("<!--");
                            aContext.writeString(((CharacterData) child).getData());
                            aContext.writeString("-->");
                        } else if (child instanceof Text) {
                            aContext.writeSafeString(((Text) child).getData());
                        }
                    }
                } else {
                    aContext.writeSafeString(SdlUtil.getText(element));
                }
                aContext.endElement();
            }
        } else {
            super.serialize(aName, aAttributes, getElement(aValue), aContext);
        }
    }

    /**
     * Removes the xmi:type previously added in <code>addXmiType()</code>
     * @param aChildElement
     */
    private void removeXmiType(Element aChildElement) {
        aChildElement.removeAttributeNS(SdlUtil.W3C_XML_SCHEMA_INSTANCE, "type");
    }

    /**
     * Attempts to add an xmi:type attribute to the element. This will only be added
     * if we can find the proper namespace and prefix for the type's qname.
     * @param aChildElement
     * @param aElementDecl
     * @param aContext
     */
    private boolean addXmiType(Element aChildElement, String childType, SerializationContext aContext) {
        logger.debug("addXmiType() childType=" + childType);
        boolean typeAdded = false;
        if (aChildElement.getAttributeNodeNS(SdlUtil.W3C_XML_SCHEMA_INSTANCE, "type") == null) {
            aChildElement.setAttributeNS(SdlUtil.W3C_XML_SCHEMA_INSTANCE, "xmi:type", childType);
            typeAdded = true;
        }
        return typeAdded;
    }

    /**
     * Determines if the type we're serializing is an array.
     * @param aXMLType
     */
    private boolean isArray(org.dbe.studio.editor.sdl.ComplexType aComplexType) {
        boolean isArray = false;
        if (aComplexType instanceof ComplexType) {
            if (aComplexType.getCmpPart().size() > 1) isArray = true;
        }
        return isArray;
    }

    /**
     * Serializes the complex type to the context.
     * @param aContext - current serialization context
     * @param aParentComplexType - the parent of the type we're serializing
     * @param aElement - the instance of the complex type we need to serialize
     */
    protected void serializeComplexSdlType(SerializationContext aContext, org.dbe.studio.editor.sdl.ComplexType aParentComplexType, Element aElement) throws IOException {
        logger.debug("serializeComplexSdlType()");
        QName childQName = null;
        String elementName = null;
        childQName = getSdlType(null, aElement);
        elementName = childQName.getLocalPart();
        TypeMapping tm = aContext.getTypeMapping();
        tm.register(DefaultElementHolder.class, childQName, getSerializerFactory(), getDeserializerFactory());
        DefaultElementHolder childValue = new DefaultElementHolder(childQName, aElement);
        aContext.serialize(new QName(elementName), null, childValue);
    }

    /**
     * Determines if the type we're serializing is an array.
     * @param aXMLType
     */
    private boolean isArray(ComplexType aComplexType) {
        boolean isArray = false;
        if (aComplexType.getBaseType() instanceof ComplexType) {
            ComplexType baseType = (ComplexType) aComplexType.getBaseType();
            isArray = baseType.getName().equals("Array") && Constants.isSOAP_ENC(baseType.getSchema().getTargetNamespace());
        }
        return isArray;
    }

    /**
     * Gets the type QName from the element declaration.
     * @param aChildDeclaration
     */
    protected QName getTypeQName(ElementDecl aElementDecl) {
        String namespace = aElementDecl.getType().getSchema().getTargetNamespace();
        return new QName(namespace, aElementDecl.getType().getName());
    }

    /**
     * Returns the local name of this element or its node name if the local
     * name is null.
     * @param aElement
     */
    protected String getLocalName(Element aElement) {
        if (aElement.getLocalName() != null) {
            return aElement.getLocalName();
        }
        return aElement.getNodeName();
    }

    /**
     * Getter for the literal serializer factory with lazy instantiation.
     */
    protected SerializerFactory getSerializerFactory() {
        if (mSerializerFactory == null) {
            mSerializerFactory = new LiteralSerializerFactory(this);
        }
        return mSerializerFactory;
    }

    /**
     * Getter for the literal serializer factory with lazy instantiation.
     */
    protected DeserializerFactory getDeserializerFactory() {
        if (mDeserializerFactory == null) {
            mDeserializerFactory = new SdlNoOpDeserializerFactory();
        }
        return mDeserializerFactory;
    }

    private static class SdlNoOpDeserializerFactory implements DeserializerFactory {

        /**
         * @see javax.xml.rpc.encoding.DeserializerFactory#getDeserializerAs(java.lang.String)
         */
        public Deserializer getDeserializerAs(String mechanismType) {
            throw new UnsupportedOperationException();
        }

        /**
         * @see javax.xml.rpc.encoding.DeserializerFactory#getSupportedMechanismTypes()
         */
        public Iterator getSupportedMechanismTypes() {
            return Collections.EMPTY_LIST.iterator();
        }
    }

    /**
     * Returns true if the child passed in has child elements.
     * @param aChild
     */
    protected boolean hasChildElements(Element aChild) {
        NodeList nl = aChild.getChildNodes();
        for (int i = 0; nl.item(i) != null; i++) {
            if (nl.item(i) instanceof Element) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the Type for the QName passed in.
     * @param aType
     */
    protected Type getSdlType(QName aType) throws IOException {
        Type type = mDef.getType(aType);
        return type;
    }

    /**
     * Gets the element from the value passed in which must be either a <code>Document</code>
     * or an <code>ElementHolder</code>
     * @param aValue
     */
    protected Element getElement(Object aValue) {
        if (aValue instanceof Document) {
            return ((Document) aValue).getDocumentElement();
        } else {
            return ((DefaultElementHolder) aValue).getElement();
        }
    }

    /**
     * Extracts the type from the element.
     * @param aValue either an <code>Element</code> or an <code>AeElementHolder</code>
     * @param aElement
     */
    private QName getType(Object aValue, Element aElement) {
        if (aValue instanceof DefaultElementHolder) {
            return ((DefaultElementHolder) aValue).getType();
        }
        String typeValue = aElement.getAttributeNS(XMLHelper.W3C_XML_SCHEMA_INSTANCE, "type");
        String typeNamespace = XMLHelper.getNamespaceForPrefix(aElement, XMLHelper.extractPrefix(typeValue));
        String typeLocalPart = XMLHelper.extractLocalPart(typeValue);
        if (typeValue != null) {
            typeNamespace = aElement.getNamespaceURI();
            typeLocalPart = XMLHelper.extractLocalPart(aElement.getNodeName());
        }
        QName type = new QName(typeNamespace, typeLocalPart);
        return type;
    }

    /**
     * Extracts the type from the element.
     * @param aValue either an <code>Element</code> or an <code>SdlElementHolder</code>
     * @param aElement
     */
    private QName getSdlType(Object aValue, Element aElement) {
        logger.debug("getSdlType(Object, Element) ");
        if (aValue instanceof DefaultElementHolder) return ((DefaultElementHolder) aValue).getType();
        String typeValue = aElement.getAttribute(org.dbe.sdl.util.Constants.XMI_TYPE);
        String typeNamespace = SdlUtil.getNamespaceForPrefix(aElement, SdlUtil.extractPrefix(typeValue));
        String typeLocalPart = SdlUtil.extractLocalPart(typeValue);
        if (SdlUtil.isNullOrEmpty(typeValue)) {
            typeNamespace = aElement.getNamespaceURI();
            typeLocalPart = SdlUtil.extractLocalPart(aElement.getNodeName());
        }
        QName type = new QName(typeNamespace, typeLocalPart);
        logger.debug("in getSdlType() and return type " + type);
        return type;
    }
}
