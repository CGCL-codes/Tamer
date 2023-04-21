package progranet.omg.core.types;

/**
 *  Parameter bean class
 */
public class Parameter implements org.apache.axis2.databinding.ADBBean {

    /**
     * field for Type
     */
    protected progranet.omg.core.types.Type localType;

    protected boolean localTypeTracker = false;

    /**
     * field for Id
     */
    protected java.lang.String localId;

    protected boolean localIdTracker = false;

    /**
     * field for Name
     */
    protected java.lang.String localName;

    protected boolean localNameTracker = false;

    /**
     * field for QualifiedName
     */
    protected java.lang.String localQualifiedName;

    protected boolean localQualifiedNameTracker = false;

    /**
     * field for MetaClass
     */
    protected progranet.omg.core.types.Type localMetaClass;

    protected boolean localMetaClassTracker = false;

    /**
     * field for Collection
     */
    protected boolean localCollection;

    protected boolean localCollectionTracker = false;

    /**
     * field for Lower
     */
    protected int localLower;

    protected boolean localLowerTracker = false;

    /**
     * field for Upper
     */
    protected org.apache.axiom.om.OMElement localUpper;

    protected boolean localUpperTracker = false;

    private static java.lang.String generatePrefix(java.lang.String namespace) {
        if (namespace.equals("http://types.core.omg.progranet")) {
            return "ns3";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * Auto generated getter method
     * @return progranet.omg.core.types.Type
     */
    public progranet.omg.core.types.Type getType() {
        return localType;
    }

    /**
     * Auto generated setter method
     * @param param Type
     */
    public void setType(progranet.omg.core.types.Type param) {
        if (param != null) {
            localTypeTracker = true;
        } else {
            localTypeTracker = true;
        }
        this.localType = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getId() {
        return localId;
    }

    /**
     * Auto generated setter method
     * @param param Id
     */
    public void setId(java.lang.String param) {
        if (param != null) {
            localIdTracker = true;
        } else {
            localIdTracker = true;
        }
        this.localId = param;
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
     * @return java.lang.String
     */
    public java.lang.String getQualifiedName() {
        return localQualifiedName;
    }

    /**
     * Auto generated setter method
     * @param param QualifiedName
     */
    public void setQualifiedName(java.lang.String param) {
        if (param != null) {
            localQualifiedNameTracker = true;
        } else {
            localQualifiedNameTracker = true;
        }
        this.localQualifiedName = param;
    }

    /**
     * Auto generated getter method
     * @return progranet.omg.core.types.Type
     */
    public progranet.omg.core.types.Type getMetaClass() {
        return localMetaClass;
    }

    /**
     * Auto generated setter method
     * @param param MetaClass
     */
    public void setMetaClass(progranet.omg.core.types.Type param) {
        if (param != null) {
            localMetaClassTracker = true;
        } else {
            localMetaClassTracker = true;
        }
        this.localMetaClass = param;
    }

    /**
     * Auto generated getter method
     * @return boolean
     */
    public boolean getCollection() {
        return localCollection;
    }

    /**
     * Auto generated setter method
     * @param param Collection
     */
    public void setCollection(boolean param) {
        if (false) {
            localCollectionTracker = false;
        } else {
            localCollectionTracker = true;
        }
        this.localCollection = param;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getLower() {
        return localLower;
    }

    /**
     * Auto generated setter method
     * @param param Lower
     */
    public void setLower(int param) {
        if (param == java.lang.Integer.MIN_VALUE) {
            localLowerTracker = true;
        } else {
            localLowerTracker = true;
        }
        this.localLower = param;
    }

    /**
     * Auto generated getter method
     * @return org.apache.axiom.om.OMElement
     */
    public org.apache.axiom.om.OMElement getUpper() {
        return localUpper;
    }

    /**
     * Auto generated setter method
     * @param param Upper
     */
    public void setUpper(org.apache.axiom.om.OMElement param) {
        if (param != null) {
            localUpperTracker = true;
        } else {
            localUpperTracker = true;
        }
        this.localUpper = param;
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
                Parameter.this.serialize(parentQName, factory, xmlWriter);
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
        if (localTypeTracker) {
            if (localType == null) {
                java.lang.String namespace2 = "http://types.core.omg.progranet";
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
                localType.serialize(new javax.xml.namespace.QName("http://types.core.omg.progranet", "type"), factory, xmlWriter);
            }
        }
        if (localIdTracker) {
            namespace = "http://types.core.omg.progranet";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null) {
                    prefix = generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "id", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                } else {
                    xmlWriter.writeStartElement(namespace, "id");
                }
            } else {
                xmlWriter.writeStartElement("id");
            }
            if (localId == null) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localId);
            }
            xmlWriter.writeEndElement();
        }
        if (localNameTracker) {
            namespace = "http://types.core.omg.progranet";
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
        if (localQualifiedNameTracker) {
            namespace = "http://types.core.omg.progranet";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null) {
                    prefix = generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "qualifiedName", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                } else {
                    xmlWriter.writeStartElement(namespace, "qualifiedName");
                }
            } else {
                xmlWriter.writeStartElement("qualifiedName");
            }
            if (localQualifiedName == null) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localQualifiedName);
            }
            xmlWriter.writeEndElement();
        }
        if (localMetaClassTracker) {
            if (localMetaClass == null) {
                java.lang.String namespace2 = "http://types.core.omg.progranet";
                if (!namespace2.equals("")) {
                    java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);
                    if (prefix2 == null) {
                        prefix2 = generatePrefix(namespace2);
                        xmlWriter.writeStartElement(prefix2, "metaClass", namespace2);
                        xmlWriter.writeNamespace(prefix2, namespace2);
                        xmlWriter.setPrefix(prefix2, namespace2);
                    } else {
                        xmlWriter.writeStartElement(namespace2, "metaClass");
                    }
                } else {
                    xmlWriter.writeStartElement("metaClass");
                }
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();
            } else {
                localMetaClass.serialize(new javax.xml.namespace.QName("http://types.core.omg.progranet", "metaClass"), factory, xmlWriter);
            }
        }
        if (localCollectionTracker) {
            namespace = "http://types.core.omg.progranet";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null) {
                    prefix = generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "collection", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                } else {
                    xmlWriter.writeStartElement(namespace, "collection");
                }
            } else {
                xmlWriter.writeStartElement("collection");
            }
            if (false) {
                throw new org.apache.axis2.databinding.ADBException("collection cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCollection));
            }
            xmlWriter.writeEndElement();
        }
        if (localLowerTracker) {
            namespace = "http://types.core.omg.progranet";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null) {
                    prefix = generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "lower", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                } else {
                    xmlWriter.writeStartElement(namespace, "lower");
                }
            } else {
                xmlWriter.writeStartElement("lower");
            }
            if (localLower == java.lang.Integer.MIN_VALUE) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLower));
            }
            xmlWriter.writeEndElement();
        }
        if (localUpperTracker) {
            if (localUpper != null) {
                java.lang.String namespace2 = "http://types.core.omg.progranet";
                if (!namespace2.equals("")) {
                    java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);
                    if (prefix2 == null) {
                        prefix2 = generatePrefix(namespace2);
                        xmlWriter.writeStartElement(prefix2, "upper", namespace2);
                        xmlWriter.writeNamespace(prefix2, namespace2);
                        xmlWriter.setPrefix(prefix2, namespace2);
                    } else {
                        xmlWriter.writeStartElement(namespace2, "upper");
                    }
                } else {
                    xmlWriter.writeStartElement("upper");
                }
                localUpper.serialize(xmlWriter);
                xmlWriter.writeEndElement();
            } else {
                java.lang.String namespace2 = "http://types.core.omg.progranet";
                if (!namespace2.equals("")) {
                    java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);
                    if (prefix2 == null) {
                        prefix2 = generatePrefix(namespace2);
                        xmlWriter.writeStartElement(prefix2, "upper", namespace2);
                        xmlWriter.writeNamespace(prefix2, namespace2);
                        xmlWriter.setPrefix(prefix2, namespace2);
                    } else {
                        xmlWriter.writeStartElement(namespace2, "upper");
                    }
                } else {
                    xmlWriter.writeStartElement("upper");
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
        if (localTypeTracker) {
            elementList.add(new javax.xml.namespace.QName("http://types.core.omg.progranet", "type"));
            elementList.add((localType == null) ? null : localType);
        }
        if (localIdTracker) {
            elementList.add(new javax.xml.namespace.QName("http://types.core.omg.progranet", "id"));
            elementList.add((localId == null) ? null : org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localId));
        }
        if (localNameTracker) {
            elementList.add(new javax.xml.namespace.QName("http://types.core.omg.progranet", "name"));
            elementList.add((localName == null) ? null : org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localName));
        }
        if (localQualifiedNameTracker) {
            elementList.add(new javax.xml.namespace.QName("http://types.core.omg.progranet", "qualifiedName"));
            elementList.add((localQualifiedName == null) ? null : org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localQualifiedName));
        }
        if (localMetaClassTracker) {
            elementList.add(new javax.xml.namespace.QName("http://types.core.omg.progranet", "metaClass"));
            elementList.add((localMetaClass == null) ? null : localMetaClass);
        }
        if (localCollectionTracker) {
            elementList.add(new javax.xml.namespace.QName("http://types.core.omg.progranet", "collection"));
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCollection));
        }
        if (localLowerTracker) {
            elementList.add(new javax.xml.namespace.QName("http://types.core.omg.progranet", "lower"));
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLower));
        }
        if (localUpperTracker) {
            elementList.add(new javax.xml.namespace.QName("http://types.core.omg.progranet", "upper"));
            elementList.add((localUpper == null) ? null : localUpper);
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
        public static Parameter parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            Parameter object = new Parameter();
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
                        if (!"Parameter".equals(type)) {
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (Parameter) progranet.ganesa.metamodel.ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }
                java.util.Vector handledAttributes = new java.util.Vector();
                reader.next();
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://types.core.omg.progranet", "type").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        object.setType(null);
                        reader.next();
                        reader.next();
                    } else {
                        object.setType(progranet.omg.core.types.Type.Factory.parse(reader));
                        reader.next();
                    }
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://types.core.omg.progranet", "id").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();
                        object.setId(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                    } else {
                        reader.getElementText();
                    }
                    reader.next();
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://types.core.omg.progranet", "name").equals(reader.getName())) {
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
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://types.core.omg.progranet", "qualifiedName").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();
                        object.setQualifiedName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                    } else {
                        reader.getElementText();
                    }
                    reader.next();
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://types.core.omg.progranet", "metaClass").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        object.setMetaClass(null);
                        reader.next();
                        reader.next();
                    } else {
                        object.setMetaClass(progranet.omg.core.types.Type.Factory.parse(reader));
                        reader.next();
                    }
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://types.core.omg.progranet", "collection").equals(reader.getName())) {
                    java.lang.String content = reader.getElementText();
                    object.setCollection(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                    reader.next();
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://types.core.omg.progranet", "lower").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();
                        object.setLower(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                    } else {
                        object.setLower(java.lang.Integer.MIN_VALUE);
                        reader.getElementText();
                    }
                    reader.next();
                } else {
                    object.setLower(java.lang.Integer.MIN_VALUE);
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://types.core.omg.progranet", "upper").equals(reader.getName())) {
                    boolean loopDone8 = false;
                    javax.xml.namespace.QName startQname8 = new javax.xml.namespace.QName("http://types.core.omg.progranet", "upper");
                    while (!loopDone8) {
                        if (reader.isStartElement() && startQname8.equals(reader.getName())) {
                            loopDone8 = true;
                        } else {
                            reader.next();
                        }
                    }
                    org.apache.axis2.databinding.utils.NamedStaxOMBuilder builder8 = new org.apache.axis2.databinding.utils.NamedStaxOMBuilder(new org.apache.axis2.util.StreamWrapper(reader), startQname8);
                    object.setUpper(builder8.getOMElement().getFirstElement());
                    reader.next();
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
