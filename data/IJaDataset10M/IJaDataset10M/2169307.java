package de.vsis.coordination.demo.participant3.stubs.wsba;

/**
 * StatusType bean class
 */
@SuppressWarnings({ "unchecked", "unused" })
public class StatusType implements org.apache.axis2.databinding.ADBBean {

    /**
	 * field for State
	 */
    protected de.vsis.coordination.demo.participant3.stubs.wsba.StateType localState;

    /**
	 * Auto generated getter method
	 * 
	 * @return de.vsis.coordination.demo.participant3.stubs.wsba.StateType
	 */
    public de.vsis.coordination.demo.participant3.stubs.wsba.StateType getState() {
        return localState;
    }

    /**
	 * Auto generated setter method
	 * 
	 * @param param
	 *            State
	 */
    public void setState(de.vsis.coordination.demo.participant3.stubs.wsba.StateType param) {
        this.localState = param;
    }

    /**
	 * field for ExtraElement This was an Array!
	 */
    protected org.apache.axiom.om.OMElement[] localExtraElement;

    protected boolean localExtraElementTracker = false;

    public boolean isExtraElementSpecified() {
        return localExtraElementTracker;
    }

    /**
	 * Auto generated getter method
	 * 
	 * @return org.apache.axiom.om.OMElement[]
	 */
    public org.apache.axiom.om.OMElement[] getExtraElement() {
        return localExtraElement;
    }

    /**
	 * validate the array for ExtraElement
	 */
    protected void validateExtraElement(org.apache.axiom.om.OMElement[] param) {
    }

    /**
	 * Auto generated setter method
	 * 
	 * @param param
	 *            ExtraElement
	 */
    public void setExtraElement(org.apache.axiom.om.OMElement[] param) {
        validateExtraElement(param);
        localExtraElementTracker = param != null;
        this.localExtraElement = param;
    }

    /**
	 * Auto generated add method for the array for convenience
	 * 
	 * @param param
	 *            org.apache.axiom.om.OMElement
	 */
    public void addExtraElement(org.apache.axiom.om.OMElement param) {
        if (localExtraElement == null) {
            localExtraElement = new org.apache.axiom.om.OMElement[] {};
        }
        localExtraElementTracker = true;
        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localExtraElement);
        list.add(param);
        this.localExtraElement = (org.apache.axiom.om.OMElement[]) list.toArray(new org.apache.axiom.om.OMElement[list.size()]);
    }

    /**
	 * field for ExtraAttributes This was an Attribute! This was an Array!
	 */
    protected org.apache.axiom.om.OMAttribute[] localExtraAttributes;

    /**
	 * Auto generated getter method
	 * 
	 * @return org.apache.axiom.om.OMAttribute[]
	 */
    public org.apache.axiom.om.OMAttribute[] getExtraAttributes() {
        return localExtraAttributes;
    }

    /**
	 * validate the array for ExtraAttributes
	 */
    protected void validateExtraAttributes(org.apache.axiom.om.OMAttribute[] param) {
        if ((param != null) && (param.length > 1)) {
            throw new java.lang.RuntimeException();
        }
        if ((param != null) && (param.length < 1)) {
            throw new java.lang.RuntimeException();
        }
    }

    /**
	 * Auto generated setter method
	 * 
	 * @param param
	 *            ExtraAttributes
	 */
    public void setExtraAttributes(org.apache.axiom.om.OMAttribute[] param) {
        validateExtraAttributes(param);
        this.localExtraAttributes = param;
    }

    /**
	 * Auto generated add method for the array for convenience
	 * 
	 * @param param
	 *            org.apache.axiom.om.OMAttribute
	 */
    public void addExtraAttributes(org.apache.axiom.om.OMAttribute param) {
        if (localExtraAttributes == null) {
            localExtraAttributes = new org.apache.axiom.om.OMAttribute[] {};
        }
        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localExtraAttributes);
        list.add(param);
        this.localExtraAttributes = (org.apache.axiom.om.OMAttribute[]) list.toArray(new org.apache.axiom.om.OMAttribute[list.size()]);
    }

    /**
	 * 
	 * @param parentQName
	 * @param factory
	 * @return org.apache.axiom.om.OMElement
	 */
    public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException {
        org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, parentQName);
        return factory.createOMElement(dataSource, parentQName);
    }

    public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
        serialize(parentQName, xmlWriter, false);
    }

    public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter, boolean serializeType) throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
        java.lang.String prefix = null;
        java.lang.String namespace = null;
        prefix = parentQName.getPrefix();
        namespace = parentQName.getNamespaceURI();
        writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);
        if (serializeType) {
            java.lang.String namespacePrefix = registerPrefix(xmlWriter, "http://docs.oasis-open.org/ws-tx/wsba/2006/06");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":StatusType", xmlWriter);
            } else {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "StatusType", xmlWriter);
            }
        }
        if (localExtraAttributes != null) {
            for (int i = 0; i < localExtraAttributes.length; i++) {
                writeAttribute(localExtraAttributes[i].getNamespace().getName(), localExtraAttributes[i].getLocalName(), localExtraAttributes[i].getAttributeValue(), xmlWriter);
            }
        }
        if (localState == null) {
            throw new org.apache.axis2.databinding.ADBException("State cannot be null!!");
        }
        localState.serialize(new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "State"), xmlWriter);
        if (localExtraElementTracker) {
            if (localExtraElement != null) {
                for (int i = 0; i < localExtraElement.length; i++) {
                    if (localExtraElement[i] != null) {
                        localExtraElement[i].serialize(xmlWriter);
                    } else {
                    }
                }
            } else {
                throw new org.apache.axis2.databinding.ADBException("extraElement cannot be null!!");
            }
        }
        xmlWriter.writeEndElement();
    }

    private static java.lang.String generatePrefix(java.lang.String namespace) {
        if (namespace.equals("http://docs.oasis-open.org/ws-tx/wsba/2006/06")) {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
	 * Utility method to write an element start tag.
	 */
    private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
        java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
        if (writerPrefix != null) {
            xmlWriter.writeStartElement(namespace, localPart);
        } else {
            if (namespace.length() == 0) {
                prefix = "";
            } else if (prefix == null) {
                prefix = generatePrefix(namespace);
            }
            xmlWriter.writeStartElement(prefix, localPart, namespace);
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
    }

    /**
	 * Util method to write an attribute with the ns prefix
	 */
    private void writeAttribute(java.lang.String prefix, java.lang.String namespace, java.lang.String attName, java.lang.String attValue, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
        if (xmlWriter.getPrefix(namespace) == null) {
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
        xmlWriter.writeAttribute(namespace, attName, attValue);
    }

    /**
	 * Util method to write an attribute without the ns prefix
	 */
    private void writeAttribute(java.lang.String namespace, java.lang.String attName, java.lang.String attValue, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
        if (namespace.equals("")) {
            xmlWriter.writeAttribute(attName, attValue);
        } else {
            registerPrefix(xmlWriter, namespace);
            xmlWriter.writeAttribute(namespace, attName, attValue);
        }
    }

    /**
	 * Util method to write an attribute without the ns prefix
	 */
    private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName, javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
        java.lang.String attributeNamespace = qname.getNamespaceURI();
        java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
        if (attributePrefix == null) {
            attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
        }
        java.lang.String attributeValue;
        if (attributePrefix.trim().length() > 0) {
            attributeValue = attributePrefix + ":" + qname.getLocalPart();
        } else {
            attributeValue = qname.getLocalPart();
        }
        if (namespace.equals("")) {
            xmlWriter.writeAttribute(attName, attributeValue);
        } else {
            registerPrefix(xmlWriter, namespace);
            xmlWriter.writeAttribute(namespace, attName, attributeValue);
        }
    }

    /**
	 * method to handle Qnames
	 */
    private void writeQName(javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
        java.lang.String namespaceURI = qname.getNamespaceURI();
        if (namespaceURI != null) {
            java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
            if (prefix == null) {
                prefix = generatePrefix(namespaceURI);
                xmlWriter.writeNamespace(prefix, namespaceURI);
                xmlWriter.setPrefix(prefix, namespaceURI);
            }
            if (prefix.trim().length() > 0) {
                xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
        }
    }

    private void writeQNames(javax.xml.namespace.QName[] qnames, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
        if (qnames != null) {
            java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
            java.lang.String namespaceURI = null;
            java.lang.String prefix = null;
            for (int i = 0; i < qnames.length; i++) {
                if (i > 0) {
                    stringToWrite.append(" ");
                }
                namespaceURI = qnames[i].getNamespaceURI();
                if (namespaceURI != null) {
                    prefix = xmlWriter.getPrefix(namespaceURI);
                    if ((prefix == null) || (prefix.length() == 0)) {
                        prefix = generatePrefix(namespaceURI);
                        xmlWriter.writeNamespace(prefix, namespaceURI);
                        xmlWriter.setPrefix(prefix, namespaceURI);
                    }
                    if (prefix.trim().length() > 0) {
                        stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                } else {
                    stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                }
            }
            xmlWriter.writeCharacters(stringToWrite.toString());
        }
    }

    /**
	 * Register a namespace prefix
	 */
    private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
        java.lang.String prefix = xmlWriter.getPrefix(namespace);
        if (prefix == null) {
            prefix = generatePrefix(namespace);
            while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
            }
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
        return prefix;
    }

    /**
	 * databinding method to get an XML representation of this object
	 * 
	 */
    public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException {
        java.util.ArrayList elementList = new java.util.ArrayList();
        java.util.ArrayList attribList = new java.util.ArrayList();
        elementList.add(new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "State"));
        if (localState == null) {
            throw new org.apache.axis2.databinding.ADBException("State cannot be null!!");
        }
        elementList.add(localState);
        if (localExtraElementTracker) {
            if (localExtraElement != null) {
                for (int i = 0; i < localExtraElement.length; i++) {
                    if (localExtraElement[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("", "extraElement"));
                        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localExtraElement[i]));
                    } else {
                    }
                }
            } else {
                throw new org.apache.axis2.databinding.ADBException("extraElement cannot be null!!");
            }
        }
        for (int i = 0; i < localExtraAttributes.length; i++) {
            attribList.add(org.apache.axis2.databinding.utils.Constants.OM_ATTRIBUTE_KEY);
            attribList.add(localExtraAttributes[i]);
        }
        return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
    }

    /**
	 * Factory class that keeps the parse method
	 */
    public static class Factory {

        /**
		 * static method to create the object Precondition: If this object is an
		 * element, the current or next start element starts this object and any
		 * intervening reader events are ignorable If this object is not an
		 * element, it is a complex type and the reader is at the event just
		 * after the outer start element Postcondition: If this object is an
		 * element, the reader is positioned at its end element If this object
		 * is a complex type, the reader is positioned at the end element of its
		 * outer element
		 */
        public static StatusType parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            StatusType object = new StatusType();
            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix = "";
            java.lang.String namespaceuri = "";
            try {
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                    java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type");
                    if (fullTypeName != null) {
                        java.lang.String nsPrefix = null;
                        if (fullTypeName.indexOf(":") > -1) {
                            nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
                        }
                        nsPrefix = nsPrefix == null ? "" : nsPrefix;
                        java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);
                        if (!"StatusType".equals(type)) {
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (StatusType) de.vsis.coordination.demo.participant3.stubs.fluglinie.ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }
                java.util.Vector handledAttributes = new java.util.Vector();
                for (int i = 0; i < reader.getAttributeCount(); i++) {
                    if (!handledAttributes.contains(reader.getAttributeLocalName(i))) {
                        org.apache.axiom.om.OMFactory factory = org.apache.axiom.om.OMAbstractFactory.getOMFactory();
                        org.apache.axiom.om.OMAttribute attr = factory.createOMAttribute(reader.getAttributeLocalName(i), factory.createOMNamespace(reader.getAttributeNamespace(i), reader.getAttributePrefix(i)), reader.getAttributeValue(i));
                        object.addExtraAttributes(attr);
                    }
                }
                reader.next();
                java.util.ArrayList list2 = new java.util.ArrayList();
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "State").equals(reader.getName())) {
                    object.setState(de.vsis.coordination.demo.participant3.stubs.wsba.StateType.Factory.parse(reader));
                    reader.next();
                } else {
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement()) {
                    boolean loopDone2 = false;
                    while (!loopDone2) {
                        event = reader.getEventType();
                        if (javax.xml.stream.XMLStreamConstants.START_ELEMENT == event) {
                            org.apache.axis2.databinding.utils.NamedStaxOMBuilder builder2 = new org.apache.axis2.databinding.utils.NamedStaxOMBuilder(new org.apache.axis2.util.StreamWrapper(reader), reader.getName());
                            list2.add(builder2.getOMElement());
                            reader.next();
                            if (reader.isEndElement()) {
                                loopDone2 = true;
                            }
                        } else if (javax.xml.stream.XMLStreamConstants.END_ELEMENT == event) {
                            loopDone2 = true;
                        } else {
                            reader.next();
                        }
                    }
                    object.setExtraElement((org.apache.axiom.om.OMElement[]) org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(org.apache.axiom.om.OMElement.class, list2));
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement()) throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }
            return object;
        }
    }
}
