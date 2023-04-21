package tr.com.srdc.www.isurf.cppdesigner;

/**
            *  SaveCPFRDesignToDatabase bean class
            */
public class SaveCPFRDesignToDatabase implements org.apache.axis2.databinding.ADBBean {

    public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName("http://www.srdc.com.tr/isurf/cppdesigner/", "saveCPFRDesignToDatabase", "ns2");

    private static java.lang.String generatePrefix(java.lang.String namespace) {
        if (namespace.equals("http://www.srdc.com.tr/isurf/cppdesigner/")) {
            return "ns2";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
                        * field for FileName
                        */
    protected java.lang.String localFileName;

    /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
    public java.lang.String getFileName() {
        return localFileName;
    }

    /**
                               * Auto generated setter method
                               * @param param FileName
                               */
    public void setFileName(java.lang.String param) {
        this.localFileName = param;
    }

    /**
                        * field for FileContent
                        */
    protected java.lang.String localFileContent;

    /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
    public java.lang.String getFileContent() {
        return localFileContent;
    }

    /**
                               * Auto generated setter method
                               * @param param FileContent
                               */
    public void setFileContent(java.lang.String param) {
        this.localFileContent = param;
    }

    /**
                        * field for SellerCompanyUsername
                        */
    protected java.lang.String localSellerCompanyUsername;

    /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
    public java.lang.String getSellerCompanyUsername() {
        return localSellerCompanyUsername;
    }

    /**
                               * Auto generated setter method
                               * @param param SellerCompanyUsername
                               */
    public void setSellerCompanyUsername(java.lang.String param) {
        this.localSellerCompanyUsername = param;
    }

    /**
                        * field for SellerUsername
                        */
    protected java.lang.String localSellerUsername;

    /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
    public java.lang.String getSellerUsername() {
        return localSellerUsername;
    }

    /**
                               * Auto generated setter method
                               * @param param SellerUsername
                               */
    public void setSellerUsername(java.lang.String param) {
        this.localSellerUsername = param;
    }

    /**
                        * field for BuyerCompanyUsername
                        */
    protected java.lang.String localBuyerCompanyUsername;

    /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
    public java.lang.String getBuyerCompanyUsername() {
        return localBuyerCompanyUsername;
    }

    /**
                               * Auto generated setter method
                               * @param param BuyerCompanyUsername
                               */
    public void setBuyerCompanyUsername(java.lang.String param) {
        this.localBuyerCompanyUsername = param;
    }

    /**
                        * field for BuyerUsername
                        */
    protected java.lang.String localBuyerUsername;

    /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
    public java.lang.String getBuyerUsername() {
        return localBuyerUsername;
    }

    /**
                               * Auto generated setter method
                               * @param param BuyerUsername
                               */
    public void setBuyerUsername(java.lang.String param) {
        this.localBuyerUsername = param;
    }

    /**
                        * field for SellerMessagingStandard
                        */
    protected java.lang.String localSellerMessagingStandard;

    /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
    public java.lang.String getSellerMessagingStandard() {
        return localSellerMessagingStandard;
    }

    /**
                               * Auto generated setter method
                               * @param param SellerMessagingStandard
                               */
    public void setSellerMessagingStandard(java.lang.String param) {
        this.localSellerMessagingStandard = param;
    }

    /**
                        * field for BuyerMessagingStandard
                        */
    protected java.lang.String localBuyerMessagingStandard;

    /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
    public java.lang.String getBuyerMessagingStandard() {
        return localBuyerMessagingStandard;
    }

    /**
                               * Auto generated setter method
                               * @param param BuyerMessagingStandard
                               */
    public void setBuyerMessagingStandard(java.lang.String param) {
        this.localBuyerMessagingStandard = param;
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
        org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, MY_QNAME) {

            public void serialize(org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
                SaveCPFRDesignToDatabase.this.serialize(MY_QNAME, factory, xmlWriter);
            }
        };
        return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(MY_QNAME, factory, dataSource);
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
            java.lang.String namespacePrefix = registerPrefix(xmlWriter, "http://www.srdc.com.tr/isurf/cppdesigner/");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":saveCPFRDesignToDatabase", xmlWriter);
            } else {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "saveCPFRDesignToDatabase", xmlWriter);
            }
        }
        namespace = "";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "fileName", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            } else {
                xmlWriter.writeStartElement(namespace, "fileName");
            }
        } else {
            xmlWriter.writeStartElement("fileName");
        }
        if (localFileName == null) {
            throw new org.apache.axis2.databinding.ADBException("fileName cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localFileName);
        }
        xmlWriter.writeEndElement();
        namespace = "";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "fileContent", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            } else {
                xmlWriter.writeStartElement(namespace, "fileContent");
            }
        } else {
            xmlWriter.writeStartElement("fileContent");
        }
        if (localFileContent == null) {
            throw new org.apache.axis2.databinding.ADBException("fileContent cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localFileContent);
        }
        xmlWriter.writeEndElement();
        namespace = "";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "sellerCompanyUsername", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            } else {
                xmlWriter.writeStartElement(namespace, "sellerCompanyUsername");
            }
        } else {
            xmlWriter.writeStartElement("sellerCompanyUsername");
        }
        if (localSellerCompanyUsername == null) {
            throw new org.apache.axis2.databinding.ADBException("sellerCompanyUsername cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localSellerCompanyUsername);
        }
        xmlWriter.writeEndElement();
        namespace = "";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "sellerUsername", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            } else {
                xmlWriter.writeStartElement(namespace, "sellerUsername");
            }
        } else {
            xmlWriter.writeStartElement("sellerUsername");
        }
        if (localSellerUsername == null) {
            throw new org.apache.axis2.databinding.ADBException("sellerUsername cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localSellerUsername);
        }
        xmlWriter.writeEndElement();
        namespace = "";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "buyerCompanyUsername", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            } else {
                xmlWriter.writeStartElement(namespace, "buyerCompanyUsername");
            }
        } else {
            xmlWriter.writeStartElement("buyerCompanyUsername");
        }
        if (localBuyerCompanyUsername == null) {
            throw new org.apache.axis2.databinding.ADBException("buyerCompanyUsername cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localBuyerCompanyUsername);
        }
        xmlWriter.writeEndElement();
        namespace = "";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "buyerUsername", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            } else {
                xmlWriter.writeStartElement(namespace, "buyerUsername");
            }
        } else {
            xmlWriter.writeStartElement("buyerUsername");
        }
        if (localBuyerUsername == null) {
            throw new org.apache.axis2.databinding.ADBException("buyerUsername cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localBuyerUsername);
        }
        xmlWriter.writeEndElement();
        namespace = "";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "sellerMessagingStandard", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            } else {
                xmlWriter.writeStartElement(namespace, "sellerMessagingStandard");
            }
        } else {
            xmlWriter.writeStartElement("sellerMessagingStandard");
        }
        if (localSellerMessagingStandard == null) {
            throw new org.apache.axis2.databinding.ADBException("sellerMessagingStandard cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localSellerMessagingStandard);
        }
        xmlWriter.writeEndElement();
        namespace = "";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "buyerMessagingStandard", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            } else {
                xmlWriter.writeStartElement(namespace, "buyerMessagingStandard");
            }
        } else {
            xmlWriter.writeStartElement("buyerMessagingStandard");
        }
        if (localBuyerMessagingStandard == null) {
            throw new org.apache.axis2.databinding.ADBException("buyerMessagingStandard cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localBuyerMessagingStandard);
        }
        xmlWriter.writeEndElement();
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
        elementList.add(new javax.xml.namespace.QName("", "fileName"));
        if (localFileName != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFileName));
        } else {
            throw new org.apache.axis2.databinding.ADBException("fileName cannot be null!!");
        }
        elementList.add(new javax.xml.namespace.QName("", "fileContent"));
        if (localFileContent != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFileContent));
        } else {
            throw new org.apache.axis2.databinding.ADBException("fileContent cannot be null!!");
        }
        elementList.add(new javax.xml.namespace.QName("", "sellerCompanyUsername"));
        if (localSellerCompanyUsername != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSellerCompanyUsername));
        } else {
            throw new org.apache.axis2.databinding.ADBException("sellerCompanyUsername cannot be null!!");
        }
        elementList.add(new javax.xml.namespace.QName("", "sellerUsername"));
        if (localSellerUsername != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSellerUsername));
        } else {
            throw new org.apache.axis2.databinding.ADBException("sellerUsername cannot be null!!");
        }
        elementList.add(new javax.xml.namespace.QName("", "buyerCompanyUsername"));
        if (localBuyerCompanyUsername != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localBuyerCompanyUsername));
        } else {
            throw new org.apache.axis2.databinding.ADBException("buyerCompanyUsername cannot be null!!");
        }
        elementList.add(new javax.xml.namespace.QName("", "buyerUsername"));
        if (localBuyerUsername != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localBuyerUsername));
        } else {
            throw new org.apache.axis2.databinding.ADBException("buyerUsername cannot be null!!");
        }
        elementList.add(new javax.xml.namespace.QName("", "sellerMessagingStandard"));
        if (localSellerMessagingStandard != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSellerMessagingStandard));
        } else {
            throw new org.apache.axis2.databinding.ADBException("sellerMessagingStandard cannot be null!!");
        }
        elementList.add(new javax.xml.namespace.QName("", "buyerMessagingStandard"));
        if (localBuyerMessagingStandard != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localBuyerMessagingStandard));
        } else {
            throw new org.apache.axis2.databinding.ADBException("buyerMessagingStandard cannot be null!!");
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
        public static SaveCPFRDesignToDatabase parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            SaveCPFRDesignToDatabase object = new SaveCPFRDesignToDatabase();
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
                        if (!"saveCPFRDesignToDatabase".equals(type)) {
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (SaveCPFRDesignToDatabase) tr.com.srdc.www.isurf.cppdesigner.anytype.ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }
                java.util.Vector handledAttributes = new java.util.Vector();
                reader.next();
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("", "fileName").equals(reader.getName())) {
                    java.lang.String content = reader.getElementText();
                    object.setFileName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                    reader.next();
                } else {
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("", "fileContent").equals(reader.getName())) {
                    java.lang.String content = reader.getElementText();
                    object.setFileContent(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                    reader.next();
                } else {
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("", "sellerCompanyUsername").equals(reader.getName())) {
                    java.lang.String content = reader.getElementText();
                    object.setSellerCompanyUsername(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                    reader.next();
                } else {
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("", "sellerUsername").equals(reader.getName())) {
                    java.lang.String content = reader.getElementText();
                    object.setSellerUsername(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                    reader.next();
                } else {
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("", "buyerCompanyUsername").equals(reader.getName())) {
                    java.lang.String content = reader.getElementText();
                    object.setBuyerCompanyUsername(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                    reader.next();
                } else {
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("", "buyerUsername").equals(reader.getName())) {
                    java.lang.String content = reader.getElementText();
                    object.setBuyerUsername(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                    reader.next();
                } else {
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("", "sellerMessagingStandard").equals(reader.getName())) {
                    java.lang.String content = reader.getElementText();
                    object.setSellerMessagingStandard(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                    reader.next();
                } else {
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("", "buyerMessagingStandard").equals(reader.getName())) {
                    java.lang.String content = reader.getElementText();
                    object.setBuyerMessagingStandard(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                    reader.next();
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
