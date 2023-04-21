package com.misyshealthcare.connect.base.clinicaldata.xsd;

/**
 *  TestBase bean class
 */
public class TestBase implements org.apache.axis2.databinding.ADBBean {

    /**
     * field for Category
     */
    protected java.lang.String localCategory;

    protected boolean localCategoryTracker = false;

    /**
     * field for Code
     */
    protected com.misyshealthcare.connect.base.clinicaldata.xsd.Code localCode;

    protected boolean localCodeTracker = false;

    /**
     * field for Codes
     * This was an Array!
     */
    protected com.misyshealthcare.connect.base.clinicaldata.xsd.Code[] localCodes;

    protected boolean localCodesTracker = false;

    /**
     * field for Name
     */
    protected java.lang.String localName;

    protected boolean localNameTracker = false;

    /**
     * field for Order
     */
    protected com.misyshealthcare.connect.base.clinicaldata.xsd.Order localOrder;

    protected boolean localOrderTracker = false;

    private static java.lang.String generatePrefix(java.lang.String namespace) {
        if (namespace.equals("http://clinicaldata.base.connect.misyshealthcare.com/xsd")) {
            return "ns4";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getCategory() {
        return localCategory;
    }

    /**
     * Auto generated setter method
     * @param param Category
     */
    public void setCategory(java.lang.String param) {
        if (param != null) {
            localCategoryTracker = true;
        } else {
            localCategoryTracker = true;
        }
        this.localCategory = param;
    }

    /**
     * Auto generated getter method
     * @return com.misyshealthcare.connect.base.clinicaldata.xsd.Code
     */
    public com.misyshealthcare.connect.base.clinicaldata.xsd.Code getCode() {
        return localCode;
    }

    /**
     * Auto generated setter method
     * @param param Code
     */
    public void setCode(com.misyshealthcare.connect.base.clinicaldata.xsd.Code param) {
        if (param != null) {
            localCodeTracker = true;
        } else {
            localCodeTracker = true;
        }
        this.localCode = param;
    }

    /**
     * Auto generated getter method
     * @return com.misyshealthcare.connect.base.clinicaldata.xsd.Code[]
     */
    public com.misyshealthcare.connect.base.clinicaldata.xsd.Code[] getCodes() {
        return localCodes;
    }

    /**
     * validate the array for Codes
     */
    protected void validateCodes(com.misyshealthcare.connect.base.clinicaldata.xsd.Code[] param) {
    }

    /**
     * Auto generated setter method
     * @param param Codes
     */
    public void setCodes(com.misyshealthcare.connect.base.clinicaldata.xsd.Code[] param) {
        validateCodes(param);
        if (param != null) {
            localCodesTracker = true;
        } else {
            localCodesTracker = true;
        }
        this.localCodes = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * @param param com.misyshealthcare.connect.base.clinicaldata.xsd.Code
     */
    public void addCodes(com.misyshealthcare.connect.base.clinicaldata.xsd.Code param) {
        if (localCodes == null) {
            localCodes = new com.misyshealthcare.connect.base.clinicaldata.xsd.Code[] {};
        }
        localCodesTracker = true;
        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localCodes);
        list.add(param);
        this.localCodes = (com.misyshealthcare.connect.base.clinicaldata.xsd.Code[]) list.toArray(new com.misyshealthcare.connect.base.clinicaldata.xsd.Code[list.size()]);
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getName() {
        return localName;
    }

    /**
     * Auto generated setter method
     * @param param Name
     */
    public void setName(java.lang.String param) {
        if (param != null) {
            localNameTracker = true;
        } else {
            localNameTracker = true;
        }
        this.localName = param;
    }

    /**
     * Auto generated getter method
     * @return com.misyshealthcare.connect.base.clinicaldata.xsd.Order
     */
    public com.misyshealthcare.connect.base.clinicaldata.xsd.Order getOrder() {
        return localOrder;
    }

    /**
     * Auto generated setter method
     * @param param Order
     */
    public void setOrder(com.misyshealthcare.connect.base.clinicaldata.xsd.Order param) {
        if (param != null) {
            localOrderTracker = true;
        } else {
            localOrderTracker = true;
        }
        this.localOrder = param;
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
                TestBase.this.serialize(parentQName, factory, xmlWriter);
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
        if (localCategoryTracker) {
            namespace = "http://clinicaldata.base.connect.misyshealthcare.com/xsd";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null) {
                    prefix = generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "category", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                } else {
                    xmlWriter.writeStartElement(namespace, "category");
                }
            } else {
                xmlWriter.writeStartElement("category");
            }
            if (localCategory == null) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localCategory);
            }
            xmlWriter.writeEndElement();
        }
        if (localCodeTracker) {
            if (localCode == null) {
                java.lang.String namespace2 = "http://clinicaldata.base.connect.misyshealthcare.com/xsd";
                if (!namespace2.equals("")) {
                    java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);
                    if (prefix2 == null) {
                        prefix2 = generatePrefix(namespace2);
                        xmlWriter.writeStartElement(prefix2, "code", namespace2);
                        xmlWriter.writeNamespace(prefix2, namespace2);
                        xmlWriter.setPrefix(prefix2, namespace2);
                    } else {
                        xmlWriter.writeStartElement(namespace2, "code");
                    }
                } else {
                    xmlWriter.writeStartElement("code");
                }
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();
            } else {
                localCode.serialize(new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "code"), factory, xmlWriter);
            }
        }
        if (localCodesTracker) {
            if (localCodes != null) {
                for (int i = 0; i < localCodes.length; i++) {
                    if (localCodes[i] != null) {
                        localCodes[i].serialize(new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "codes"), factory, xmlWriter);
                    } else {
                        java.lang.String namespace2 = "http://clinicaldata.base.connect.misyshealthcare.com/xsd";
                        if (!namespace2.equals("")) {
                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);
                            if (prefix2 == null) {
                                prefix2 = generatePrefix(namespace2);
                                xmlWriter.writeStartElement(prefix2, "codes", namespace2);
                                xmlWriter.writeNamespace(prefix2, namespace2);
                                xmlWriter.setPrefix(prefix2, namespace2);
                            } else {
                                xmlWriter.writeStartElement(namespace2, "codes");
                            }
                        } else {
                            xmlWriter.writeStartElement("codes");
                        }
                        writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                        xmlWriter.writeEndElement();
                    }
                }
            } else {
                java.lang.String namespace2 = "http://clinicaldata.base.connect.misyshealthcare.com/xsd";
                if (!namespace2.equals("")) {
                    java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);
                    if (prefix2 == null) {
                        prefix2 = generatePrefix(namespace2);
                        xmlWriter.writeStartElement(prefix2, "codes", namespace2);
                        xmlWriter.writeNamespace(prefix2, namespace2);
                        xmlWriter.setPrefix(prefix2, namespace2);
                    } else {
                        xmlWriter.writeStartElement(namespace2, "codes");
                    }
                } else {
                    xmlWriter.writeStartElement("codes");
                }
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();
            }
        }
        if (localNameTracker) {
            namespace = "http://clinicaldata.base.connect.misyshealthcare.com/xsd";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null) {
                    prefix = generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "name", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                } else {
                    xmlWriter.writeStartElement(namespace, "name");
                }
            } else {
                xmlWriter.writeStartElement("name");
            }
            if (localName == null) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localName);
            }
            xmlWriter.writeEndElement();
        }
        if (localOrderTracker) {
            if (localOrder == null) {
                java.lang.String namespace2 = "http://clinicaldata.base.connect.misyshealthcare.com/xsd";
                if (!namespace2.equals("")) {
                    java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);
                    if (prefix2 == null) {
                        prefix2 = generatePrefix(namespace2);
                        xmlWriter.writeStartElement(prefix2, "order", namespace2);
                        xmlWriter.writeNamespace(prefix2, namespace2);
                        xmlWriter.setPrefix(prefix2, namespace2);
                    } else {
                        xmlWriter.writeStartElement(namespace2, "order");
                    }
                } else {
                    xmlWriter.writeStartElement("order");
                }
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();
            } else {
                localOrder.serialize(new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "order"), factory, xmlWriter);
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
        if (localCategoryTracker) {
            elementList.add(new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "category"));
            elementList.add((localCategory == null) ? null : org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCategory));
        }
        if (localCodeTracker) {
            elementList.add(new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "code"));
            elementList.add((localCode == null) ? null : localCode);
        }
        if (localCodesTracker) {
            if (localCodes != null) {
                for (int i = 0; i < localCodes.length; i++) {
                    if (localCodes[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "codes"));
                        elementList.add(localCodes[i]);
                    } else {
                        elementList.add(new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "codes"));
                        elementList.add(null);
                    }
                }
            } else {
                elementList.add(new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "codes"));
                elementList.add(localCodes);
            }
        }
        if (localNameTracker) {
            elementList.add(new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "name"));
            elementList.add((localName == null) ? null : org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localName));
        }
        if (localOrderTracker) {
            elementList.add(new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "order"));
            elementList.add((localOrder == null) ? null : localOrder);
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
        public static TestBase parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            TestBase object = new TestBase();
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
                        if (!"TestBase".equals(type)) {
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (TestBase) com.misyshealthcare.connect.doc.ccd.ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }
                java.util.Vector handledAttributes = new java.util.Vector();
                reader.next();
                java.util.ArrayList list3 = new java.util.ArrayList();
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "category").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();
                        object.setCategory(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                    } else {
                        reader.getElementText();
                    }
                    reader.next();
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "code").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        object.setCode(null);
                        reader.next();
                        reader.next();
                    } else {
                        object.setCode(com.misyshealthcare.connect.base.clinicaldata.xsd.Code.Factory.parse(reader));
                        reader.next();
                    }
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "codes").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        list3.add(null);
                        reader.next();
                    } else {
                        list3.add(com.misyshealthcare.connect.base.clinicaldata.xsd.Code.Factory.parse(reader));
                    }
                    boolean loopDone3 = false;
                    while (!loopDone3) {
                        while (!reader.isEndElement()) reader.next();
                        reader.next();
                        while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                        if (reader.isEndElement()) {
                            loopDone3 = true;
                        } else {
                            if (new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "codes").equals(reader.getName())) {
                                nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                                if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                                    list3.add(null);
                                    reader.next();
                                } else {
                                    list3.add(com.misyshealthcare.connect.base.clinicaldata.xsd.Code.Factory.parse(reader));
                                }
                            } else {
                                loopDone3 = true;
                            }
                        }
                    }
                    object.setCodes((com.misyshealthcare.connect.base.clinicaldata.xsd.Code[]) org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(com.misyshealthcare.connect.base.clinicaldata.xsd.Code.class, list3));
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "name").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();
                        object.setName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                    } else {
                        reader.getElementText();
                    }
                    reader.next();
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "order").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        object.setOrder(null);
                        reader.next();
                        reader.next();
                    } else {
                        object.setOrder(com.misyshealthcare.connect.base.clinicaldata.xsd.Order.Factory.parse(reader));
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
