package com.misyshealthcare.connect.base.demographicdata.xsd;

/**
 *  PhoneNumber bean class
 */
public class PhoneNumber implements org.apache.axis2.databinding.ADBBean {

    /**
     * field for AreaCode
     */
    protected java.lang.String localAreaCode;

    protected boolean localAreaCodeTracker = false;

    /**
     * field for CountryCode
     */
    protected java.lang.String localCountryCode;

    protected boolean localCountryCodeTracker = false;

    /**
     * field for Empty
     */
    protected boolean localEmpty;

    protected boolean localEmptyTracker = false;

    /**
     * field for Extension
     */
    protected java.lang.String localExtension;

    protected boolean localExtensionTracker = false;

    /**
     * field for Note
     */
    protected java.lang.String localNote;

    protected boolean localNoteTracker = false;

    /**
     * field for Number
     */
    protected java.lang.String localNumber;

    protected boolean localNumberTracker = false;

    /**
     * field for Type
     */
    protected com.misyshealthcare.connect.base.xsd.PhoneType localType;

    protected boolean localTypeTracker = false;

    private static java.lang.String generatePrefix(java.lang.String namespace) {
        if (namespace.equals("http://demographicdata.base.connect.misyshealthcare.com/xsd")) {
            return "ns3";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getAreaCode() {
        return localAreaCode;
    }

    /**
     * Auto generated setter method
     * @param param AreaCode
     */
    public void setAreaCode(java.lang.String param) {
        if (param != null) {
            localAreaCodeTracker = true;
        } else {
            localAreaCodeTracker = true;
        }
        this.localAreaCode = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getCountryCode() {
        return localCountryCode;
    }

    /**
     * Auto generated setter method
     * @param param CountryCode
     */
    public void setCountryCode(java.lang.String param) {
        if (param != null) {
            localCountryCodeTracker = true;
        } else {
            localCountryCodeTracker = true;
        }
        this.localCountryCode = param;
    }

    /**
     * Auto generated getter method
     * @return boolean
     */
    public boolean getEmpty() {
        return localEmpty;
    }

    /**
     * Auto generated setter method
     * @param param Empty
     */
    public void setEmpty(boolean param) {
        if (false) {
            localEmptyTracker = false;
        } else {
            localEmptyTracker = true;
        }
        this.localEmpty = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getExtension() {
        return localExtension;
    }

    /**
     * Auto generated setter method
     * @param param Extension
     */
    public void setExtension(java.lang.String param) {
        if (param != null) {
            localExtensionTracker = true;
        } else {
            localExtensionTracker = true;
        }
        this.localExtension = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getNote() {
        return localNote;
    }

    /**
     * Auto generated setter method
     * @param param Note
     */
    public void setNote(java.lang.String param) {
        if (param != null) {
            localNoteTracker = true;
        } else {
            localNoteTracker = true;
        }
        this.localNote = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getNumber() {
        return localNumber;
    }

    /**
     * Auto generated setter method
     * @param param Number
     */
    public void setNumber(java.lang.String param) {
        if (param != null) {
            localNumberTracker = true;
        } else {
            localNumberTracker = true;
        }
        this.localNumber = param;
    }

    /**
     * Auto generated getter method
     * @return com.misyshealthcare.connect.base.xsd.PhoneType
     */
    public com.misyshealthcare.connect.base.xsd.PhoneType getType() {
        return localType;
    }

    /**
     * Auto generated setter method
     * @param param Type
     */
    public void setType(com.misyshealthcare.connect.base.xsd.PhoneType param) {
        if (param != null) {
            localTypeTracker = true;
        } else {
            localTypeTracker = true;
        }
        this.localType = param;
    }

    /**
     * isReaderMTOMAware
     * @return true if the reader supports MTOM
     */
    public static boolean isReaderMTOMAware(javax.xml.stream.XMLStreamReader reader) {
        boolean isReaderMTOMAware = false;
        try {
            isReaderMTOMAware = java.lang.Boolean.TRUE.equals(reader.getProperty(org.apache.axiom.om.OMConstants.IS_DATA_HANDLERS_AWARE));
        } catch (java.lang.IllegalArgumentException e) {
            isReaderMTOMAware = false;
        }
        return isReaderMTOMAware;
    }

    /**
     *
     * @param parentQName
     * @param factory
     * @return org.apache.axiom.om.OMElement
     */
    public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException {
        org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, parentQName) {

            public void serialize(org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
                PhoneNumber.this.serialize(parentQName, factory, xmlWriter);
            }
        };
        return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(parentQName, factory, dataSource);
    }

    public void serialize(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory, org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
        java.lang.String prefix = null;
        java.lang.String namespace = null;
        prefix = parentQName.getPrefix();
        namespace = parentQName.getNamespaceURI();
        if (namespace != null) {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, parentQName.getLocalPart());
            } else {
                if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }
                xmlWriter.writeStartElement(prefix, parentQName.getLocalPart(), namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        } else {
            xmlWriter.writeStartElement(parentQName.getLocalPart());
        }
        if (localAreaCodeTracker) {
            namespace = "http://demographicdata.base.connect.misyshealthcare.com/xsd";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null) {
                    prefix = generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "areaCode", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                } else {
                    xmlWriter.writeStartElement(namespace, "areaCode");
                }
            } else {
                xmlWriter.writeStartElement("areaCode");
            }
            if (localAreaCode == null) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localAreaCode);
            }
            xmlWriter.writeEndElement();
        }
        if (localCountryCodeTracker) {
            namespace = "http://demographicdata.base.connect.misyshealthcare.com/xsd";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null) {
                    prefix = generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "countryCode", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                } else {
                    xmlWriter.writeStartElement(namespace, "countryCode");
                }
            } else {
                xmlWriter.writeStartElement("countryCode");
            }
            if (localCountryCode == null) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localCountryCode);
            }
            xmlWriter.writeEndElement();
        }
        if (localEmptyTracker) {
            namespace = "http://demographicdata.base.connect.misyshealthcare.com/xsd";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null) {
                    prefix = generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "empty", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                } else {
                    xmlWriter.writeStartElement(namespace, "empty");
                }
            } else {
                xmlWriter.writeStartElement("empty");
            }
            if (false) {
                throw new org.apache.axis2.databinding.ADBException("empty cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEmpty));
            }
            xmlWriter.writeEndElement();
        }
        if (localExtensionTracker) {
            namespace = "http://demographicdata.base.connect.misyshealthcare.com/xsd";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null) {
                    prefix = generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "extension", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                } else {
                    xmlWriter.writeStartElement(namespace, "extension");
                }
            } else {
                xmlWriter.writeStartElement("extension");
            }
            if (localExtension == null) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localExtension);
            }
            xmlWriter.writeEndElement();
        }
        if (localNoteTracker) {
            namespace = "http://demographicdata.base.connect.misyshealthcare.com/xsd";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null) {
                    prefix = generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "note", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                } else {
                    xmlWriter.writeStartElement(namespace, "note");
                }
            } else {
                xmlWriter.writeStartElement("note");
            }
            if (localNote == null) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localNote);
            }
            xmlWriter.writeEndElement();
        }
        if (localNumberTracker) {
            namespace = "http://demographicdata.base.connect.misyshealthcare.com/xsd";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null) {
                    prefix = generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "number", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                } else {
                    xmlWriter.writeStartElement(namespace, "number");
                }
            } else {
                xmlWriter.writeStartElement("number");
            }
            if (localNumber == null) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localNumber);
            }
            xmlWriter.writeEndElement();
        }
        if (localTypeTracker) {
            if (localType == null) {
                java.lang.String namespace2 = "http://demographicdata.base.connect.misyshealthcare.com/xsd";
                if (!namespace2.equals("")) {
                    java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);
                    if (prefix2 == null) {
                        prefix2 = generatePrefix(namespace2);
                        xmlWriter.writeStartElement(prefix2, "type", namespace2);
                        xmlWriter.writeNamespace(prefix2, namespace2);
                        xmlWriter.setPrefix(prefix2, namespace2);
                    } else {
                        xmlWriter.writeStartElement(namespace2, "type");
                    }
                } else {
                    xmlWriter.writeStartElement("type");
                }
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();
            } else {
                localType.serialize(new javax.xml.namespace.QName("http://demographicdata.base.connect.misyshealthcare.com/xsd", "type"), factory, xmlWriter);
            }
        }
        xmlWriter.writeEndElement();
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
     *  method to handle Qnames
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
        if (localAreaCodeTracker) {
            elementList.add(new javax.xml.namespace.QName("http://demographicdata.base.connect.misyshealthcare.com/xsd", "areaCode"));
            elementList.add((localAreaCode == null) ? null : org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAreaCode));
        }
        if (localCountryCodeTracker) {
            elementList.add(new javax.xml.namespace.QName("http://demographicdata.base.connect.misyshealthcare.com/xsd", "countryCode"));
            elementList.add((localCountryCode == null) ? null : org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCountryCode));
        }
        if (localEmptyTracker) {
            elementList.add(new javax.xml.namespace.QName("http://demographicdata.base.connect.misyshealthcare.com/xsd", "empty"));
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEmpty));
        }
        if (localExtensionTracker) {
            elementList.add(new javax.xml.namespace.QName("http://demographicdata.base.connect.misyshealthcare.com/xsd", "extension"));
            elementList.add((localExtension == null) ? null : org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localExtension));
        }
        if (localNoteTracker) {
            elementList.add(new javax.xml.namespace.QName("http://demographicdata.base.connect.misyshealthcare.com/xsd", "note"));
            elementList.add((localNote == null) ? null : org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNote));
        }
        if (localNumberTracker) {
            elementList.add(new javax.xml.namespace.QName("http://demographicdata.base.connect.misyshealthcare.com/xsd", "number"));
            elementList.add((localNumber == null) ? null : org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNumber));
        }
        if (localTypeTracker) {
            elementList.add(new javax.xml.namespace.QName("http://demographicdata.base.connect.misyshealthcare.com/xsd", "type"));
            elementList.add((localType == null) ? null : localType);
        }
        return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
    }

    /**
     *  Factory class that keeps the parse method
     */
    public static class Factory {

        /**
         * static method to create the object
         * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
         *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
         * Postcondition: If this object is an element, the reader is positioned at its end element
         *                If this object is a complex type, the reader is positioned at the end element of its outer element
         */
        public static PhoneNumber parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            PhoneNumber object = new PhoneNumber();
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
                        nsPrefix = (nsPrefix == null) ? "" : nsPrefix;
                        java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);
                        if (!"PhoneNumber".equals(type)) {
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (PhoneNumber) com.misyshealthcare.connect.doc.ccd.ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }
                java.util.Vector handledAttributes = new java.util.Vector();
                reader.next();
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://demographicdata.base.connect.misyshealthcare.com/xsd", "areaCode").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();
                        object.setAreaCode(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                    } else {
                        reader.getElementText();
                    }
                    reader.next();
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://demographicdata.base.connect.misyshealthcare.com/xsd", "countryCode").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();
                        object.setCountryCode(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                    } else {
                        reader.getElementText();
                    }
                    reader.next();
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://demographicdata.base.connect.misyshealthcare.com/xsd", "empty").equals(reader.getName())) {
                    java.lang.String content = reader.getElementText();
                    object.setEmpty(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                    reader.next();
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://demographicdata.base.connect.misyshealthcare.com/xsd", "extension").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();
                        object.setExtension(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                    } else {
                        reader.getElementText();
                    }
                    reader.next();
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://demographicdata.base.connect.misyshealthcare.com/xsd", "note").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();
                        object.setNote(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                    } else {
                        reader.getElementText();
                    }
                    reader.next();
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://demographicdata.base.connect.misyshealthcare.com/xsd", "number").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();
                        object.setNumber(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                    } else {
                        reader.getElementText();
                    }
                    reader.next();
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://demographicdata.base.connect.misyshealthcare.com/xsd", "type").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        object.setType(null);
                        reader.next();
                        reader.next();
                    } else {
                        object.setType(com.misyshealthcare.connect.base.xsd.PhoneType.Factory.parse(reader));
                        reader.next();
                    }
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement()) {
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                }
            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }
            return object;
        }
    }
}
