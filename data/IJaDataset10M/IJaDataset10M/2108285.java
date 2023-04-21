package net.sf.traser.databinding.basic;

/**
*  PropertyValuesReport bean class
*/
public class PropertyValuesReport implements org.apache.axis2.databinding.ADBBean {

    public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName("http://files.traser.emi.sztaki.hu/schemas/transitional/2008/10/base.xsd", "propertyValuesReport", "ns1");

    private static java.lang.String generatePrefix(java.lang.String namespace) {
        if (namespace.equals("http://files.traser.emi.sztaki.hu/schemas/transitional/2008/10/base.xsd")) {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
    * field for Item
    */
    protected net.sf.traser.databinding.basic.Item localItem;

    /**
    * Auto generated getter method
    * @return net.sf.traser.databinding.basic.Item
    */
    public net.sf.traser.databinding.basic.Item getItem() {
        return localItem;
    }

    /**
       * Auto generated setter method
       * @param param Item
       */
    public void setItem(net.sf.traser.databinding.basic.Item param) {
        this.localItem = param;
    }

    /**
    * field for Property
    * This was an Array!
    */
    protected net.sf.traser.databinding.basic.PropertyValueResponse[] localProperty;

    protected boolean localPropertyTracker = false;

    /**
    * Auto generated getter method
    * @return net.sf.traser.databinding.basic.PropertyValueResponse[]
    */
    public net.sf.traser.databinding.basic.PropertyValueResponse[] getProperty() {
        return localProperty;
    }

    /**
     * validate the array for Property
     */
    protected void validateProperty(net.sf.traser.databinding.basic.PropertyValueResponse[] param) {
    }

    /**
     * Auto generated setter method
     * @param param Property
     */
    public void setProperty(net.sf.traser.databinding.basic.PropertyValueResponse[] param) {
        validateProperty(param);
        if (param != null) {
            localPropertyTracker = true;
        } else {
            localPropertyTracker = false;
        }
        this.localProperty = param;
    }

    /**
    * Auto generated add method for the array for convenience
    * @param param net.sf.traser.databinding.basic.PropertyValueResponse
    */
    public void addProperty(net.sf.traser.databinding.basic.PropertyValueResponse param) {
        if (localProperty == null) {
            localProperty = new net.sf.traser.databinding.basic.PropertyValueResponse[] {};
        }
        localPropertyTracker = true;
        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localProperty);
        list.add(param);
        this.localProperty = (net.sf.traser.databinding.basic.PropertyValueResponse[]) list.toArray(new net.sf.traser.databinding.basic.PropertyValueResponse[list.size()]);
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
                PropertyValuesReport.this.serialize(MY_QNAME, factory, xmlWriter);
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
            java.lang.String namespacePrefix = registerPrefix(xmlWriter, "http://files.traser.emi.sztaki.hu/schemas/transitional/2008/10/base.xsd");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":propertyValuesReport", xmlWriter);
            } else {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "propertyValuesReport", xmlWriter);
            }
        }
        if (localItem == null) {
            throw new org.apache.axis2.databinding.ADBException("item cannot be null!!");
        }
        localItem.serialize(new javax.xml.namespace.QName("http://files.traser.emi.sztaki.hu/schemas/transitional/2008/10/base.xsd", "item"), factory, xmlWriter);
        if (localPropertyTracker) {
            if (localProperty != null) {
                for (int i = 0; i < localProperty.length; i++) {
                    if (localProperty[i] != null) {
                        localProperty[i].serialize(new javax.xml.namespace.QName("http://files.traser.emi.sztaki.hu/schemas/transitional/2008/10/base.xsd", "property"), factory, xmlWriter);
                    } else {
                    }
                }
            } else {
                throw new org.apache.axis2.databinding.ADBException("property cannot be null!!");
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
        elementList.add(new javax.xml.namespace.QName("http://files.traser.emi.sztaki.hu/schemas/transitional/2008/10/base.xsd", "item"));
        if (localItem == null) {
            throw new org.apache.axis2.databinding.ADBException("item cannot be null!!");
        }
        elementList.add(localItem);
        if (localPropertyTracker) {
            if (localProperty != null) {
                for (int i = 0; i < localProperty.length; i++) {
                    if (localProperty[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("http://files.traser.emi.sztaki.hu/schemas/transitional/2008/10/base.xsd", "property"));
                        elementList.add(localProperty[i]);
                    } else {
                    }
                }
            } else {
                throw new org.apache.axis2.databinding.ADBException("property cannot be null!!");
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
        public static PropertyValuesReport parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            PropertyValuesReport object = new PropertyValuesReport();
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
                        if (!"propertyValuesReport".equals(type)) {
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (PropertyValuesReport) net.sf.traser.databinding.basic.ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }
                java.util.Vector handledAttributes = new java.util.Vector();
                reader.next();
                java.util.ArrayList list2 = new java.util.ArrayList();
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://files.traser.emi.sztaki.hu/schemas/transitional/2008/10/base.xsd", "item").equals(reader.getName())) {
                    object.setItem(net.sf.traser.databinding.basic.Item.Factory.parse(reader));
                    reader.next();
                } else {
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("http://files.traser.emi.sztaki.hu/schemas/transitional/2008/10/base.xsd", "property").equals(reader.getName())) {
                    list2.add(net.sf.traser.databinding.basic.PropertyValueResponse.Factory.parse(reader));
                    boolean loopDone2 = false;
                    while (!loopDone2) {
                        while (!reader.isEndElement()) reader.next();
                        reader.next();
                        while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                        if (reader.isEndElement()) {
                            loopDone2 = true;
                        } else {
                            if (new javax.xml.namespace.QName("http://files.traser.emi.sztaki.hu/schemas/transitional/2008/10/base.xsd", "property").equals(reader.getName())) {
                                list2.add(net.sf.traser.databinding.basic.PropertyValueResponse.Factory.parse(reader));
                            } else {
                                loopDone2 = true;
                            }
                        }
                    }
                    object.setProperty((net.sf.traser.databinding.basic.PropertyValueResponse[]) org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(net.sf.traser.databinding.basic.PropertyValueResponse.class, list2));
                } else {
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
