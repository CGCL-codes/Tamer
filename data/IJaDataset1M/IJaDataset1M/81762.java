package edu.indiana.extreme.xbaya;

/**
            *  LaunchWorkflow bean class
            */
public class LaunchWorkflow implements org.apache.axis2.databinding.ADBBean {

    public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName("http://xbaya.extreme.indiana.edu", "launchWorkflow", "ns1");

    private static java.lang.String generatePrefix(java.lang.String namespace) {
        if (namespace.equals("http://xbaya.extreme.indiana.edu")) {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
                        * field for WorkflowAsString
                        */
    protected java.lang.String localWorkflowAsString;

    protected boolean localWorkflowAsStringTracker = false;

    /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
    public java.lang.String getWorkflowAsString() {
        return localWorkflowAsString;
    }

    /**
                               * Auto generated setter method
                               * @param param WorkflowAsString
                               */
    public void setWorkflowAsString(java.lang.String param) {
        if (param != null) {
            localWorkflowAsStringTracker = true;
        } else {
            localWorkflowAsStringTracker = true;
        }
        this.localWorkflowAsString = param;
    }

    /**
                        * field for Topic
                        */
    protected java.lang.String localTopic;

    protected boolean localTopicTracker = false;

    /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
    public java.lang.String getTopic() {
        return localTopic;
    }

    /**
                               * Auto generated setter method
                               * @param param Topic
                               */
    public void setTopic(java.lang.String param) {
        if (param != null) {
            localTopicTracker = true;
        } else {
            localTopicTracker = true;
        }
        this.localTopic = param;
    }

    /**
                        * field for Password
                        */
    protected java.lang.String localPassword;

    protected boolean localPasswordTracker = false;

    /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
    public java.lang.String getPassword() {
        return localPassword;
    }

    /**
                               * Auto generated setter method
                               * @param param Password
                               */
    public void setPassword(java.lang.String param) {
        if (param != null) {
            localPasswordTracker = true;
        } else {
            localPasswordTracker = true;
        }
        this.localPassword = param;
    }

    /**
                        * field for Username
                        */
    protected java.lang.String localUsername;

    protected boolean localUsernameTracker = false;

    /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
    public java.lang.String getUsername() {
        return localUsername;
    }

    /**
                               * Auto generated setter method
                               * @param param Username
                               */
    public void setUsername(java.lang.String param) {
        if (param != null) {
            localUsernameTracker = true;
        } else {
            localUsernameTracker = true;
        }
        this.localUsername = param;
    }

    /**
                        * field for Inputs
                        * This was an Array!
                        */
    protected edu.indiana.extreme.xbaya.NameValue[] localInputs;

    protected boolean localInputsTracker = false;

    /**
                           * Auto generated getter method
                           * @return edu.indiana.extreme.xbaya.NameValue[]
                           */
    public edu.indiana.extreme.xbaya.NameValue[] getInputs() {
        return localInputs;
    }

    /**
                               * validate the array for Inputs
                               */
    protected void validateInputs(edu.indiana.extreme.xbaya.NameValue[] param) {
    }

    /**
                              * Auto generated setter method
                              * @param param Inputs
                              */
    public void setInputs(edu.indiana.extreme.xbaya.NameValue[] param) {
        validateInputs(param);
        if (param != null) {
            localInputsTracker = true;
        } else {
            localInputsTracker = true;
        }
        this.localInputs = param;
    }

    /**
                             * Auto generated add method for the array for convenience
                             * @param param edu.indiana.extreme.xbaya.NameValue
                             */
    public void addInputs(edu.indiana.extreme.xbaya.NameValue param) {
        if (localInputs == null) {
            localInputs = new edu.indiana.extreme.xbaya.NameValue[] {};
        }
        localInputsTracker = true;
        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localInputs);
        list.add(param);
        this.localInputs = (edu.indiana.extreme.xbaya.NameValue[]) list.toArray(new edu.indiana.extreme.xbaya.NameValue[list.size()]);
    }

    /**
                        * field for Configurations
                        * This was an Array!
                        */
    protected edu.indiana.extreme.xbaya.NameValue[] localConfigurations;

    protected boolean localConfigurationsTracker = false;

    /**
                           * Auto generated getter method
                           * @return edu.indiana.extreme.xbaya.NameValue[]
                           */
    public edu.indiana.extreme.xbaya.NameValue[] getConfigurations() {
        return localConfigurations;
    }

    /**
                               * validate the array for Configurations
                               */
    protected void validateConfigurations(edu.indiana.extreme.xbaya.NameValue[] param) {
    }

    /**
                              * Auto generated setter method
                              * @param param Configurations
                              */
    public void setConfigurations(edu.indiana.extreme.xbaya.NameValue[] param) {
        validateConfigurations(param);
        if (param != null) {
            localConfigurationsTracker = true;
        } else {
            localConfigurationsTracker = true;
        }
        this.localConfigurations = param;
    }

    /**
                             * Auto generated add method for the array for convenience
                             * @param param edu.indiana.extreme.xbaya.NameValue
                             */
    public void addConfigurations(edu.indiana.extreme.xbaya.NameValue param) {
        if (localConfigurations == null) {
            localConfigurations = new edu.indiana.extreme.xbaya.NameValue[] {};
        }
        localConfigurationsTracker = true;
        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localConfigurations);
        list.add(param);
        this.localConfigurations = (edu.indiana.extreme.xbaya.NameValue[]) list.toArray(new edu.indiana.extreme.xbaya.NameValue[list.size()]);
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
                LaunchWorkflow.this.serialize(MY_QNAME, factory, xmlWriter);
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
            java.lang.String namespacePrefix = registerPrefix(xmlWriter, "http://xbaya.extreme.indiana.edu");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":launchWorkflow", xmlWriter);
            } else {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "launchWorkflow", xmlWriter);
            }
        }
        if (localWorkflowAsStringTracker) {
            namespace = "";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null) {
                    prefix = generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "workflowAsString", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                } else {
                    xmlWriter.writeStartElement(namespace, "workflowAsString");
                }
            } else {
                xmlWriter.writeStartElement("workflowAsString");
            }
            if (localWorkflowAsString == null) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localWorkflowAsString);
            }
            xmlWriter.writeEndElement();
        }
        if (localTopicTracker) {
            namespace = "";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null) {
                    prefix = generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "topic", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                } else {
                    xmlWriter.writeStartElement(namespace, "topic");
                }
            } else {
                xmlWriter.writeStartElement("topic");
            }
            if (localTopic == null) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localTopic);
            }
            xmlWriter.writeEndElement();
        }
        if (localPasswordTracker) {
            namespace = "";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null) {
                    prefix = generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "password", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                } else {
                    xmlWriter.writeStartElement(namespace, "password");
                }
            } else {
                xmlWriter.writeStartElement("password");
            }
            if (localPassword == null) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localPassword);
            }
            xmlWriter.writeEndElement();
        }
        if (localUsernameTracker) {
            namespace = "";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null) {
                    prefix = generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "username", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                } else {
                    xmlWriter.writeStartElement(namespace, "username");
                }
            } else {
                xmlWriter.writeStartElement("username");
            }
            if (localUsername == null) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localUsername);
            }
            xmlWriter.writeEndElement();
        }
        if (localInputsTracker) {
            if (localInputs != null) {
                for (int i = 0; i < localInputs.length; i++) {
                    if (localInputs[i] != null) {
                        localInputs[i].serialize(new javax.xml.namespace.QName("", "inputs"), factory, xmlWriter);
                    } else {
                        java.lang.String namespace2 = "";
                        if (!namespace2.equals("")) {
                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);
                            if (prefix2 == null) {
                                prefix2 = generatePrefix(namespace2);
                                xmlWriter.writeStartElement(prefix2, "inputs", namespace2);
                                xmlWriter.writeNamespace(prefix2, namespace2);
                                xmlWriter.setPrefix(prefix2, namespace2);
                            } else {
                                xmlWriter.writeStartElement(namespace2, "inputs");
                            }
                        } else {
                            xmlWriter.writeStartElement("inputs");
                        }
                        writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                        xmlWriter.writeEndElement();
                    }
                }
            } else {
                java.lang.String namespace2 = "";
                if (!namespace2.equals("")) {
                    java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);
                    if (prefix2 == null) {
                        prefix2 = generatePrefix(namespace2);
                        xmlWriter.writeStartElement(prefix2, "inputs", namespace2);
                        xmlWriter.writeNamespace(prefix2, namespace2);
                        xmlWriter.setPrefix(prefix2, namespace2);
                    } else {
                        xmlWriter.writeStartElement(namespace2, "inputs");
                    }
                } else {
                    xmlWriter.writeStartElement("inputs");
                }
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();
            }
        }
        if (localConfigurationsTracker) {
            if (localConfigurations != null) {
                for (int i = 0; i < localConfigurations.length; i++) {
                    if (localConfigurations[i] != null) {
                        localConfigurations[i].serialize(new javax.xml.namespace.QName("", "configurations"), factory, xmlWriter);
                    } else {
                        java.lang.String namespace2 = "";
                        if (!namespace2.equals("")) {
                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);
                            if (prefix2 == null) {
                                prefix2 = generatePrefix(namespace2);
                                xmlWriter.writeStartElement(prefix2, "configurations", namespace2);
                                xmlWriter.writeNamespace(prefix2, namespace2);
                                xmlWriter.setPrefix(prefix2, namespace2);
                            } else {
                                xmlWriter.writeStartElement(namespace2, "configurations");
                            }
                        } else {
                            xmlWriter.writeStartElement("configurations");
                        }
                        writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                        xmlWriter.writeEndElement();
                    }
                }
            } else {
                java.lang.String namespace2 = "";
                if (!namespace2.equals("")) {
                    java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);
                    if (prefix2 == null) {
                        prefix2 = generatePrefix(namespace2);
                        xmlWriter.writeStartElement(prefix2, "configurations", namespace2);
                        xmlWriter.writeNamespace(prefix2, namespace2);
                        xmlWriter.setPrefix(prefix2, namespace2);
                    } else {
                        xmlWriter.writeStartElement(namespace2, "configurations");
                    }
                } else {
                    xmlWriter.writeStartElement("configurations");
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
        if (localWorkflowAsStringTracker) {
            elementList.add(new javax.xml.namespace.QName("", "workflowAsString"));
            elementList.add(localWorkflowAsString == null ? null : org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWorkflowAsString));
        }
        if (localTopicTracker) {
            elementList.add(new javax.xml.namespace.QName("", "topic"));
            elementList.add(localTopic == null ? null : org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTopic));
        }
        if (localPasswordTracker) {
            elementList.add(new javax.xml.namespace.QName("", "password"));
            elementList.add(localPassword == null ? null : org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPassword));
        }
        if (localUsernameTracker) {
            elementList.add(new javax.xml.namespace.QName("", "username"));
            elementList.add(localUsername == null ? null : org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUsername));
        }
        if (localInputsTracker) {
            if (localInputs != null) {
                for (int i = 0; i < localInputs.length; i++) {
                    if (localInputs[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("", "inputs"));
                        elementList.add(localInputs[i]);
                    } else {
                        elementList.add(new javax.xml.namespace.QName("", "inputs"));
                        elementList.add(null);
                    }
                }
            } else {
                elementList.add(new javax.xml.namespace.QName("", "inputs"));
                elementList.add(localInputs);
            }
        }
        if (localConfigurationsTracker) {
            if (localConfigurations != null) {
                for (int i = 0; i < localConfigurations.length; i++) {
                    if (localConfigurations[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("", "configurations"));
                        elementList.add(localConfigurations[i]);
                    } else {
                        elementList.add(new javax.xml.namespace.QName("", "configurations"));
                        elementList.add(null);
                    }
                }
            } else {
                elementList.add(new javax.xml.namespace.QName("", "configurations"));
                elementList.add(localConfigurations);
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
        public static LaunchWorkflow parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            LaunchWorkflow object = new LaunchWorkflow();
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
                        if (!"launchWorkflow".equals(type)) {
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (LaunchWorkflow) edu.indiana.extreme.xbaya.ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }
                java.util.Vector handledAttributes = new java.util.Vector();
                reader.next();
                java.util.ArrayList list5 = new java.util.ArrayList();
                java.util.ArrayList list6 = new java.util.ArrayList();
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("", "workflowAsString").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();
                        object.setWorkflowAsString(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                    } else {
                        reader.getElementText();
                    }
                    reader.next();
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("", "topic").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();
                        object.setTopic(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                    } else {
                        reader.getElementText();
                    }
                    reader.next();
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("", "password").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();
                        object.setPassword(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                    } else {
                        reader.getElementText();
                    }
                    reader.next();
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("", "username").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();
                        object.setUsername(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                    } else {
                        reader.getElementText();
                    }
                    reader.next();
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("", "inputs").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        list5.add(null);
                        reader.next();
                    } else {
                        list5.add(edu.indiana.extreme.xbaya.NameValue.Factory.parse(reader));
                    }
                    boolean loopDone5 = false;
                    while (!loopDone5) {
                        while (!reader.isEndElement()) reader.next();
                        reader.next();
                        while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                        if (reader.isEndElement()) {
                            loopDone5 = true;
                        } else {
                            if (new javax.xml.namespace.QName("", "inputs").equals(reader.getName())) {
                                nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                                if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                                    list5.add(null);
                                    reader.next();
                                } else {
                                    list5.add(edu.indiana.extreme.xbaya.NameValue.Factory.parse(reader));
                                }
                            } else {
                                loopDone5 = true;
                            }
                        }
                    }
                    object.setInputs((edu.indiana.extreme.xbaya.NameValue[]) org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(edu.indiana.extreme.xbaya.NameValue.class, list5));
                } else {
                }
                while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                if (reader.isStartElement() && new javax.xml.namespace.QName("", "configurations").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        list6.add(null);
                        reader.next();
                    } else {
                        list6.add(edu.indiana.extreme.xbaya.NameValue.Factory.parse(reader));
                    }
                    boolean loopDone6 = false;
                    while (!loopDone6) {
                        while (!reader.isEndElement()) reader.next();
                        reader.next();
                        while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                        if (reader.isEndElement()) {
                            loopDone6 = true;
                        } else {
                            if (new javax.xml.namespace.QName("", "configurations").equals(reader.getName())) {
                                nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                                if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                                    list6.add(null);
                                    reader.next();
                                } else {
                                    list6.add(edu.indiana.extreme.xbaya.NameValue.Factory.parse(reader));
                                }
                            } else {
                                loopDone6 = true;
                            }
                        }
                    }
                    object.setConfigurations((edu.indiana.extreme.xbaya.NameValue[]) org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(edu.indiana.extreme.xbaya.NameValue.class, list6));
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
