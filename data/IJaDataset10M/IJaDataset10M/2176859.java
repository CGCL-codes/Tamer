package gov.irs.www.a2a.mef.mefstateservice_xsd;

/**
            *  SubmissionIdListType bean class
            */
public class SubmissionIdListType implements org.apache.axis2.databinding.ADBBean {

    private static java.lang.String generatePrefix(java.lang.String namespace) {
        if (namespace.equals("http://www.irs.gov/a2a/mef/MeFStateService.xsd")) {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
                        * field for Count
                        */
    protected org.apache.axis2.databinding.types.PositiveInteger localCount;

    /**
                           * Auto generated getter method
                           * @return org.apache.axis2.databinding.types.PositiveInteger
                           */
    public org.apache.axis2.databinding.types.PositiveInteger getCount() {
        return localCount;
    }

    /**
                               * Auto generated setter method
                               * @param param Count
                               */
    public void setCount(org.apache.axis2.databinding.types.PositiveInteger param) {
        this.localCount = param;
    }

    /**
                        * field for SubmissionId
                        * This was an Array!
                        */
    protected gov.irs.www.a2a.mef.mefstateservice_xsd.SubmissionIdType[] localSubmissionId;

    /**
                           * Auto generated getter method
                           * @return gov.irs.www.a2a.mef.mefstateservice_xsd.SubmissionIdType[]
                           */
    public gov.irs.www.a2a.mef.mefstateservice_xsd.SubmissionIdType[] getSubmissionId() {
        return localSubmissionId;
    }

    /**
                               * validate the array for SubmissionId
                               */
    protected void validateSubmissionId(gov.irs.www.a2a.mef.mefstateservice_xsd.SubmissionIdType[] param) {
        if ((param != null) && (param.length < 1)) {
            throw new java.lang.RuntimeException();
        }
    }

    /**
                              * Auto generated setter method
                              * @param param SubmissionId
                              */
    public void setSubmissionId(gov.irs.www.a2a.mef.mefstateservice_xsd.SubmissionIdType[] param) {
        validateSubmissionId(param);
        this.localSubmissionId = param;
    }

    /**
                             * Auto generated add method for the array for convenience
                             * @param param gov.irs.www.a2a.mef.mefstateservice_xsd.SubmissionIdType
                             */
    public void addSubmissionId(gov.irs.www.a2a.mef.mefstateservice_xsd.SubmissionIdType param) {
        if (localSubmissionId == null) {
            localSubmissionId = new gov.irs.www.a2a.mef.mefstateservice_xsd.SubmissionIdType[] {};
        }
        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localSubmissionId);
        list.add(param);
        this.localSubmissionId = (gov.irs.www.a2a.mef.mefstateservice_xsd.SubmissionIdType[]) list.toArray(new gov.irs.www.a2a.mef.mefstateservice_xsd.SubmissionIdType[list.size()]);
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
                SubmissionIdListType.this.serialize(parentQName, factory, xmlWriter);
            }
        };
        return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(parentQName, factory, dataSource);
    }

    public void serialize(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory, org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
        serialize(parentQName, factory, xmlWriter, false);
    }

    public void serialize(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory, org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter, boolean serializeType) throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
        java.lang.String prefix = null;
        java.lang.String namespace = null;
        prefix = parentQName.getPrefix();
        namespace = parentQName.getNamespaceURI();
        if ((namespace != null) && (namespace.trim().length() > 0)) {
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
        if (serializeType) {
            java.lang.String namespacePrefix = registerPrefix(xmlWriter, "http://www.irs.gov/a2a/mef/MeFStateService.xsd");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":SubmissionIdListType", xmlWriter);
            } else {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "SubmissionIdListType", xmlWriter);
            }
        }
        namespace = "http://www.irs.gov/a2a/mef/MeFStateService.xsd";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "Count", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            } else {
                xmlWriter.writeStartElement(namespace, "Count");
            }
        } else {
            xmlWriter.writeStartElement("Count");
        }
        if (localCount == null) {
            throw new org.apache.axis2.databinding.ADBException("Count cannot be null!!");
        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCount));
        }
        xmlWriter.writeEndElement();
        if (localSubmissionId != null) {
            for (int i = 0; i < localSubmissionId.length; i++) {
                if (localSubmissionId[i] != null) {
                    localSubmissionId[i].serialize(new javax.xml.namespace.QName("http://www.irs.gov/a2a/mef/MeFStateService.xsd", "SubmissionId"), factory, xmlWriter);
                } else {
                    throw new org.apache.axis2.databinding.ADBException("SubmissionId cannot be null!!");
                }
            }
        } else {
            throw new org.apache.axis2.databinding.ADBException("SubmissionId cannot be null!!");
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
        elementList.add(new javax.xml.namespace.QName("http://www.irs.gov/a2a/mef/MeFStateService.xsd", "Count"));
        if (localCount != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCount));
        } else {
            throw new org.apache.axis2.databinding.ADBException("Count cannot be null!!");
        }
        if (localSubmissionId != null) {
            for (int i = 0; i < localSubmissionId.length; i++) {
                if (localSubmissionId[i] != null) {
                    elementList.add(new javax.xml.namespace.QName("http://www.irs.gov/a2a/mef/MeFStateService.xsd", "SubmissionId"));
                    elementList.add(localSubmissionId[i]);
                } else {
                    throw new org.apache.axis2.databinding.ADBException("SubmissionId cannot be null !!");
                }
            }
        } else {
            throw new org.apache.axis2.databinding.ADBException("SubmissionId cannot be null!!");
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
        public static SubmissionIdListType parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            SubmissionIdListType object = new SubmissionIdListType();
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
                        if (!"SubmissionIdListType".equals(type)) {
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (SubmissionIdListType) gov.irs.www.a2a.mef.mefstateservice.ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }
                java.util.Vector handledAttributes = new java.util.Vector();
                reader.next();
                java.util.ArrayList list2 = new java.util.ArrayList();
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.irs.gov/a2a/mef/MeFStateService.xsd", "Count").equals(reader.getName())) {
                    java.lang.String content = reader.getElementText();
                    object.setCount(org.apache.axis2.databinding.utils.ConverterUtil.convertToPositiveInteger(content));
                    reader.next();
                } else {
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.irs.gov/a2a/mef/MeFStateService.xsd", "SubmissionId").equals(reader.getName())) {
                    list2.add(gov.irs.www.a2a.mef.mefstateservice_xsd.SubmissionIdType.Factory.parse(reader));
                    boolean loopDone2 = false;
                    while (!loopDone2) {
                        while (!reader.isEndElement()) reader.next();
                        reader.next();
                        while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                        if (reader.isEndElement()) {
                            loopDone2 = true;
                        } else {
                            if (new javax.xml.namespace.QName("http://www.irs.gov/a2a/mef/MeFStateService.xsd", "SubmissionId").equals(reader.getName())) {
                                list2.add(gov.irs.www.a2a.mef.mefstateservice_xsd.SubmissionIdType.Factory.parse(reader));
                            } else {
                                loopDone2 = true;
                            }
                        }
                    }
                    object.setSubmissionId((gov.irs.www.a2a.mef.mefstateservice_xsd.SubmissionIdType[]) org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(gov.irs.www.a2a.mef.mefstateservice_xsd.SubmissionIdType.class, list2));
                } else {
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement()) throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }
            return object;
        }
    }
}
