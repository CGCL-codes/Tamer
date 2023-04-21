package com.misyshealthcare.connect.doc.ccd.xsd;

/**
 *  SubmissionSet bean class
 */
public class SubmissionSet implements org.apache.axis2.databinding.ADBBean {

    /**
     * field for Documents
     * This was an Array!
     */
    protected com.misyshealthcare.connect.doc.ccd.xsd.CCDDocument[] localDocuments;

    protected boolean localDocumentsTracker = false;

    /**
     * field for SubmissionSetMetaData
     */
    protected com.misyshealthcare.connect.doc.ccd.xsd.SubmissionSetMetaData localSubmissionSetMetaData;

    protected boolean localSubmissionSetMetaDataTracker = false;

    private static java.lang.String generatePrefix(java.lang.String namespace) {
        if (namespace.equals("http://ccd.doc.connect.misyshealthcare.com/xsd")) {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * Auto generated getter method
     * @return com.misyshealthcare.connect.doc.ccd.xsd.CCDDocument[]
     */
    public com.misyshealthcare.connect.doc.ccd.xsd.CCDDocument[] getDocuments() {
        return localDocuments;
    }

    /**
     * validate the array for Documents
     */
    protected void validateDocuments(com.misyshealthcare.connect.doc.ccd.xsd.CCDDocument[] param) {
    }

    /**
     * Auto generated setter method
     * @param param Documents
     */
    public void setDocuments(com.misyshealthcare.connect.doc.ccd.xsd.CCDDocument[] param) {
        validateDocuments(param);
        if (param != null) {
            localDocumentsTracker = true;
        } else {
            localDocumentsTracker = true;
        }
        this.localDocuments = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * @param param com.misyshealthcare.connect.doc.ccd.xsd.CCDDocument
     */
    public void addDocuments(com.misyshealthcare.connect.doc.ccd.xsd.CCDDocument param) {
        if (localDocuments == null) {
            localDocuments = new com.misyshealthcare.connect.doc.ccd.xsd.CCDDocument[] {};
        }
        localDocumentsTracker = true;
        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localDocuments);
        list.add(param);
        this.localDocuments = (com.misyshealthcare.connect.doc.ccd.xsd.CCDDocument[]) list.toArray(new com.misyshealthcare.connect.doc.ccd.xsd.CCDDocument[list.size()]);
    }

    /**
     * Auto generated getter method
     * @return com.misyshealthcare.connect.doc.ccd.xsd.SubmissionSetMetaData
     */
    public com.misyshealthcare.connect.doc.ccd.xsd.SubmissionSetMetaData getSubmissionSetMetaData() {
        return localSubmissionSetMetaData;
    }

    /**
     * Auto generated setter method
     * @param param SubmissionSetMetaData
     */
    public void setSubmissionSetMetaData(com.misyshealthcare.connect.doc.ccd.xsd.SubmissionSetMetaData param) {
        if (param != null) {
            localSubmissionSetMetaDataTracker = true;
        } else {
            localSubmissionSetMetaDataTracker = true;
        }
        this.localSubmissionSetMetaData = param;
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
                SubmissionSet.this.serialize(parentQName, factory, xmlWriter);
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
        if (localDocumentsTracker) {
            if (localDocuments != null) {
                for (int i = 0; i < localDocuments.length; i++) {
                    if (localDocuments[i] != null) {
                        localDocuments[i].serialize(new javax.xml.namespace.QName("http://ccd.doc.connect.misyshealthcare.com/xsd", "documents"), factory, xmlWriter);
                    } else {
                        java.lang.String namespace2 = "http://ccd.doc.connect.misyshealthcare.com/xsd";
                        if (!namespace2.equals("")) {
                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);
                            if (prefix2 == null) {
                                prefix2 = generatePrefix(namespace2);
                                xmlWriter.writeStartElement(prefix2, "documents", namespace2);
                                xmlWriter.writeNamespace(prefix2, namespace2);
                                xmlWriter.setPrefix(prefix2, namespace2);
                            } else {
                                xmlWriter.writeStartElement(namespace2, "documents");
                            }
                        } else {
                            xmlWriter.writeStartElement("documents");
                        }
                        writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                        xmlWriter.writeEndElement();
                    }
                }
            } else {
                java.lang.String namespace2 = "http://ccd.doc.connect.misyshealthcare.com/xsd";
                if (!namespace2.equals("")) {
                    java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);
                    if (prefix2 == null) {
                        prefix2 = generatePrefix(namespace2);
                        xmlWriter.writeStartElement(prefix2, "documents", namespace2);
                        xmlWriter.writeNamespace(prefix2, namespace2);
                        xmlWriter.setPrefix(prefix2, namespace2);
                    } else {
                        xmlWriter.writeStartElement(namespace2, "documents");
                    }
                } else {
                    xmlWriter.writeStartElement("documents");
                }
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();
            }
        }
        if (localSubmissionSetMetaDataTracker) {
            if (localSubmissionSetMetaData == null) {
                java.lang.String namespace2 = "http://ccd.doc.connect.misyshealthcare.com/xsd";
                if (!namespace2.equals("")) {
                    java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);
                    if (prefix2 == null) {
                        prefix2 = generatePrefix(namespace2);
                        xmlWriter.writeStartElement(prefix2, "submissionSetMetaData", namespace2);
                        xmlWriter.writeNamespace(prefix2, namespace2);
                        xmlWriter.setPrefix(prefix2, namespace2);
                    } else {
                        xmlWriter.writeStartElement(namespace2, "submissionSetMetaData");
                    }
                } else {
                    xmlWriter.writeStartElement("submissionSetMetaData");
                }
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();
            } else {
                localSubmissionSetMetaData.serialize(new javax.xml.namespace.QName("http://ccd.doc.connect.misyshealthcare.com/xsd", "submissionSetMetaData"), factory, xmlWriter);
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
        if (localDocumentsTracker) {
            if (localDocuments != null) {
                for (int i = 0; i < localDocuments.length; i++) {
                    if (localDocuments[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("http://ccd.doc.connect.misyshealthcare.com/xsd", "documents"));
                        elementList.add(localDocuments[i]);
                    } else {
                        elementList.add(new javax.xml.namespace.QName("http://ccd.doc.connect.misyshealthcare.com/xsd", "documents"));
                        elementList.add(null);
                    }
                }
            } else {
                elementList.add(new javax.xml.namespace.QName("http://ccd.doc.connect.misyshealthcare.com/xsd", "documents"));
                elementList.add(localDocuments);
            }
        }
        if (localSubmissionSetMetaDataTracker) {
            elementList.add(new javax.xml.namespace.QName("http://ccd.doc.connect.misyshealthcare.com/xsd", "submissionSetMetaData"));
            elementList.add((localSubmissionSetMetaData == null) ? null : localSubmissionSetMetaData);
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
        public static SubmissionSet parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            SubmissionSet object = new SubmissionSet();
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
                        if (!"SubmissionSet".equals(type)) {
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (SubmissionSet) com.misyshealthcare.connect.doc.ccd.ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }
                java.util.Vector handledAttributes = new java.util.Vector();
                reader.next();
                java.util.ArrayList list1 = new java.util.ArrayList();
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://ccd.doc.connect.misyshealthcare.com/xsd", "documents").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        list1.add(null);
                        reader.next();
                    } else {
                        list1.add(com.misyshealthcare.connect.doc.ccd.xsd.CCDDocument.Factory.parse(reader));
                    }
                    boolean loopDone1 = false;
                    while (!loopDone1) {
                        while (!reader.isEndElement()) reader.next();
                        reader.next();
                        while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                        if (reader.isEndElement()) {
                            loopDone1 = true;
                        } else {
                            if (new javax.xml.namespace.QName("http://ccd.doc.connect.misyshealthcare.com/xsd", "documents").equals(reader.getName())) {
                                nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                                if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                                    list1.add(null);
                                    reader.next();
                                } else {
                                    list1.add(com.misyshealthcare.connect.doc.ccd.xsd.CCDDocument.Factory.parse(reader));
                                }
                            } else {
                                loopDone1 = true;
                            }
                        }
                    }
                    object.setDocuments((com.misyshealthcare.connect.doc.ccd.xsd.CCDDocument[]) org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(com.misyshealthcare.connect.doc.ccd.xsd.CCDDocument.class, list1));
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://ccd.doc.connect.misyshealthcare.com/xsd", "submissionSetMetaData").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        object.setSubmissionSetMetaData(null);
                        reader.next();
                        reader.next();
                    } else {
                        object.setSubmissionSetMetaData(com.misyshealthcare.connect.doc.ccd.xsd.SubmissionSetMetaData.Factory.parse(reader));
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
