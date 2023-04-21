package com.misyshealthcare.connect.base.clinicaldata.xsd;

/**
 *  Battery bean class
 */
public class Battery extends com.misyshealthcare.connect.base.clinicaldata.xsd.TestBase implements org.apache.axis2.databinding.ADBBean {

    /**
     * field for Test
     * This was an Array!
     */
    protected com.misyshealthcare.connect.base.clinicaldata.xsd.Test[] localTest;

    protected boolean localTestTracker = false;

    private static java.lang.String generatePrefix(java.lang.String namespace) {
        if (namespace.equals("http://clinicaldata.base.connect.misyshealthcare.com/xsd")) {
            return "ns4";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * Auto generated getter method
     * @return com.misyshealthcare.connect.base.clinicaldata.xsd.Test[]
     */
    public com.misyshealthcare.connect.base.clinicaldata.xsd.Test[] getTest() {
        return localTest;
    }

    /**
     * validate the array for Test
     */
    protected void validateTest(com.misyshealthcare.connect.base.clinicaldata.xsd.Test[] param) {
    }

    /**
     * Auto generated setter method
     * @param param Test
     */
    public void setTest(com.misyshealthcare.connect.base.clinicaldata.xsd.Test[] param) {
        validateTest(param);
        if (param != null) {
            localTestTracker = true;
        } else {
            localTestTracker = true;
        }
        this.localTest = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * @param param com.misyshealthcare.connect.base.clinicaldata.xsd.Test
     */
    public void addTest(com.misyshealthcare.connect.base.clinicaldata.xsd.Test param) {
        if (localTest == null) {
            localTest = new com.misyshealthcare.connect.base.clinicaldata.xsd.Test[] {};
        }
        localTestTracker = true;
        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localTest);
        list.add(param);
        this.localTest = (com.misyshealthcare.connect.base.clinicaldata.xsd.Test[]) list.toArray(new com.misyshealthcare.connect.base.clinicaldata.xsd.Test[list.size()]);
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
                Battery.this.serialize(parentQName, factory, xmlWriter);
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
        java.lang.String namespacePrefix = registerPrefix(xmlWriter, "http://clinicaldata.base.connect.misyshealthcare.com/xsd");
        if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":Battery", xmlWriter);
        } else {
            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "Battery", xmlWriter);
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
        if (localTestTracker) {
            if (localTest != null) {
                for (int i = 0; i < localTest.length; i++) {
                    if (localTest[i] != null) {
                        localTest[i].serialize(new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "test"), factory, xmlWriter);
                    } else {
                        java.lang.String namespace2 = "http://clinicaldata.base.connect.misyshealthcare.com/xsd";
                        if (!namespace2.equals("")) {
                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);
                            if (prefix2 == null) {
                                prefix2 = generatePrefix(namespace2);
                                xmlWriter.writeStartElement(prefix2, "test", namespace2);
                                xmlWriter.writeNamespace(prefix2, namespace2);
                                xmlWriter.setPrefix(prefix2, namespace2);
                            } else {
                                xmlWriter.writeStartElement(namespace2, "test");
                            }
                        } else {
                            xmlWriter.writeStartElement("test");
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
                        xmlWriter.writeStartElement(prefix2, "test", namespace2);
                        xmlWriter.writeNamespace(prefix2, namespace2);
                        xmlWriter.setPrefix(prefix2, namespace2);
                    } else {
                        xmlWriter.writeStartElement(namespace2, "test");
                    }
                } else {
                    xmlWriter.writeStartElement("test");
                }
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();
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
        attribList.add(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema-instance", "type"));
        attribList.add(new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "Battery"));
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
        if (localTestTracker) {
            if (localTest != null) {
                for (int i = 0; i < localTest.length; i++) {
                    if (localTest[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "test"));
                        elementList.add(localTest[i]);
                    } else {
                        elementList.add(new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "test"));
                        elementList.add(null);
                    }
                }
            } else {
                elementList.add(new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "test"));
                elementList.add(localTest);
            }
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
        public static Battery parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            Battery object = new Battery();
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
                        if (!"Battery".equals(type)) {
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (Battery) com.misyshealthcare.connect.doc.ccd.ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }
                java.util.Vector handledAttributes = new java.util.Vector();
                reader.next();
                java.util.ArrayList list3 = new java.util.ArrayList();
                java.util.ArrayList list6 = new java.util.ArrayList();
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
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "test").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        list6.add(null);
                        reader.next();
                    } else {
                        list6.add(com.misyshealthcare.connect.base.clinicaldata.xsd.Test.Factory.parse(reader));
                    }
                    boolean loopDone6 = false;
                    while (!loopDone6) {
                        while (!reader.isEndElement()) reader.next();
                        reader.next();
                        while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                        if (reader.isEndElement()) {
                            loopDone6 = true;
                        } else {
                            if (new javax.xml.namespace.QName("http://clinicaldata.base.connect.misyshealthcare.com/xsd", "test").equals(reader.getName())) {
                                nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                                if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                                    list6.add(null);
                                    reader.next();
                                } else {
                                    list6.add(com.misyshealthcare.connect.base.clinicaldata.xsd.Test.Factory.parse(reader));
                                }
                            } else {
                                loopDone6 = true;
                            }
                        }
                    }
                    object.setTest((com.misyshealthcare.connect.base.clinicaldata.xsd.Test[]) org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(com.misyshealthcare.connect.base.clinicaldata.xsd.Test.class, list6));
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
