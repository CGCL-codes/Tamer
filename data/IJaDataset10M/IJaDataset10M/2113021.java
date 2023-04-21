package de.vsis.coordination.demo.participant3.stubs.fluglinie;

/**
 * BookFlightResponseType bean class
 */
@SuppressWarnings({ "unchecked", "unused" })
public class BookFlightResponseType implements org.apache.axis2.databinding.ADBBean {

    /**
	 * field for Done
	 */
    protected de.vsis.coordination.demo.participant3.stubs.fluglinie.Confirmation localDone;

    /**
	 * Auto generated getter method
	 * 
	 * @return 
	 *         de.vsis.coordination.demo.participant3.stubs.fluglinie.Confirmation
	 */
    public de.vsis.coordination.demo.participant3.stubs.fluglinie.Confirmation getDone() {
        return localDone;
    }

    /**
	 * Auto generated setter method
	 * 
	 * @param param
	 *            Done
	 */
    public void setDone(de.vsis.coordination.demo.participant3.stubs.fluglinie.Confirmation param) {
        this.localDone = param;
    }

    /**
	 * field for ParticipantId
	 */
    protected de.vsis.coordination.demo.participant3.stubs.fluglinie.IdType localParticipantId;

    /**
	 * Auto generated getter method
	 * 
	 * @return de.vsis.coordination.demo.participant3.stubs.fluglinie.IdType
	 */
    public de.vsis.coordination.demo.participant3.stubs.fluglinie.IdType getParticipantId() {
        return localParticipantId;
    }

    /**
	 * Auto generated setter method
	 * 
	 * @param param
	 *            ParticipantId
	 */
    public void setParticipantId(de.vsis.coordination.demo.participant3.stubs.fluglinie.IdType param) {
        this.localParticipantId = param;
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
            java.lang.String namespacePrefix = registerPrefix(xmlWriter, "http://coordination.vsis.de/Flugbuchung/Lufthansa");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":BookFlightResponseType", xmlWriter);
            } else {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "BookFlightResponseType", xmlWriter);
            }
        }
        if (localDone == null) {
            throw new org.apache.axis2.databinding.ADBException("Done cannot be null!!");
        }
        localDone.serialize(new javax.xml.namespace.QName("http://coordination.vsis.de/Flugbuchung/Lufthansa", "Done"), xmlWriter);
        if (localParticipantId == null) {
            throw new org.apache.axis2.databinding.ADBException("ParticipantId cannot be null!!");
        }
        localParticipantId.serialize(new javax.xml.namespace.QName("http://coordination.vsis.de/Flugbuchung/Lufthansa", "ParticipantId"), xmlWriter);
        xmlWriter.writeEndElement();
    }

    private static java.lang.String generatePrefix(java.lang.String namespace) {
        if (namespace.equals("http://coordination.vsis.de/Flugbuchung/Lufthansa")) {
            return "ns2";
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
        elementList.add(new javax.xml.namespace.QName("http://coordination.vsis.de/Flugbuchung/Lufthansa", "Done"));
        if (localDone == null) {
            throw new org.apache.axis2.databinding.ADBException("Done cannot be null!!");
        }
        elementList.add(localDone);
        elementList.add(new javax.xml.namespace.QName("http://coordination.vsis.de/Flugbuchung/Lufthansa", "ParticipantId"));
        if (localParticipantId == null) {
            throw new org.apache.axis2.databinding.ADBException("ParticipantId cannot be null!!");
        }
        elementList.add(localParticipantId);
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
        public static BookFlightResponseType parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            BookFlightResponseType object = new BookFlightResponseType();
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
                        if (!"BookFlightResponseType".equals(type)) {
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (BookFlightResponseType) de.vsis.coordination.demo.participant3.stubs.fluglinie.ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }
                java.util.Vector handledAttributes = new java.util.Vector();
                reader.next();
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://coordination.vsis.de/Flugbuchung/Lufthansa", "Done").equals(reader.getName())) {
                    object.setDone(de.vsis.coordination.demo.participant3.stubs.fluglinie.Confirmation.Factory.parse(reader));
                    reader.next();
                } else {
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://coordination.vsis.de/Flugbuchung/Lufthansa", "ParticipantId").equals(reader.getName())) {
                    object.setParticipantId(de.vsis.coordination.demo.participant3.stubs.fluglinie.IdType.Factory.parse(reader));
                    reader.next();
                } else {
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
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
