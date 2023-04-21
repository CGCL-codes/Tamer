package br.com.danielnegri.nfe.wsdl.nfecancelamento2;

public class NfeCancelamento2Stub extends org.apache.axis2.client.Stub {

    protected org.apache.axis2.description.AxisOperation[] _operations;

    private java.util.HashMap faultExceptionNameMap = new java.util.HashMap();

    private java.util.HashMap faultExceptionClassNameMap = new java.util.HashMap();

    private java.util.HashMap faultMessageMap = new java.util.HashMap();

    private static int counter = 0;

    private static synchronized java.lang.String getUniqueSuffix() {
        if (counter > 99999) {
            counter = 0;
        }
        counter = counter + 1;
        return java.lang.Long.toString(System.currentTimeMillis()) + "_" + counter;
    }

    private void populateAxisService() throws org.apache.axis2.AxisFault {
        _service = new org.apache.axis2.description.AxisService("NfeCancelamento2" + getUniqueSuffix());
        addAnonymousOperations();
        org.apache.axis2.description.AxisOperation __operation;
        _operations = new org.apache.axis2.description.AxisOperation[1];
        __operation = new org.apache.axis2.description.OutInAxisOperation();
        __operation.setName(new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2", "nfeCancelamentoNF2"));
        _service.addOperation(__operation);
        _operations[0] = __operation;
    }

    private void populateFaults() {
    }

    /**
	 * Constructor that takes in a configContext
	 */
    public NfeCancelamento2Stub(org.apache.axis2.context.ConfigurationContext configurationContext, java.lang.String targetEndpoint) throws org.apache.axis2.AxisFault {
        this(configurationContext, targetEndpoint, false);
    }

    /**
	 * Constructor that takes in a configContext and useseperate listner
	 */
    public NfeCancelamento2Stub(org.apache.axis2.context.ConfigurationContext configurationContext, java.lang.String targetEndpoint, boolean useSeparateListener) throws org.apache.axis2.AxisFault {
        populateAxisService();
        populateFaults();
        _serviceClient = new org.apache.axis2.client.ServiceClient(configurationContext, _service);
        _serviceClient.getOptions().setTo(new org.apache.axis2.addressing.EndpointReference(targetEndpoint));
        _serviceClient.getOptions().setUseSeparateListener(useSeparateListener);
        _serviceClient.getOptions().setSoapVersionURI(org.apache.axiom.soap.SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
    }

    /**
	 * Default Constructor
	 */
    public NfeCancelamento2Stub(org.apache.axis2.context.ConfigurationContext configurationContext) throws org.apache.axis2.AxisFault {
        this(configurationContext, "https://homologacao.nfe.fazenda.sp.gov.br/nfeweb/services/NfeCancelamento2.asmx");
    }

    /**
	 * Default Constructor
	 */
    public NfeCancelamento2Stub() throws org.apache.axis2.AxisFault {
        this("https://homologacao.nfe.fazenda.sp.gov.br/nfeweb/services/NfeCancelamento2.asmx");
    }

    /**
	 * Constructor taking the target endpoint
	 */
    public NfeCancelamento2Stub(java.lang.String targetEndpoint) throws org.apache.axis2.AxisFault {
        this(null, targetEndpoint);
    }

    /**
	 * Auto generated method signature Cancelamento de NF-e
	 * 
	 * @see br.inf.portalfiscal.www.nfe.wsdl.nfecancelamento2.NfeCancelamento2#nfeCancelamentoNF2
	 * @param nfeDadosMsg0
	 * 
	 * @param nfeCabecMsg1
	 */
    public br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeCancelamentoNF2Result nfeCancelamentoNF2(br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeDadosMsg nfeDadosMsg0, br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeCabecMsgE nfeCabecMsg1) throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;
        try {
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[0].getName());
            _operationClient.getOptions().setAction("http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2/nfeCancelamentoNF2");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
            addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
            _messageContext = new org.apache.axis2.context.MessageContext();
            org.apache.axiom.soap.SOAPEnvelope env = null;
            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), nfeDadosMsg0, optimizeContent(new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2", "nfeCancelamentoNF2")));
            env.build();
            if (nfeCabecMsg1 != null) {
                org.apache.axiom.om.OMElement omElementnfeCabecMsg1 = toOM(nfeCabecMsg1, optimizeContent(new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2", "nfeCancelamentoNF2")));
                addHeader(omElementnfeCabecMsg1, env);
            }
            _serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);
            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
            java.lang.Object object = fromOM(_returnEnv.getBody().getFirstElement(), br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeCancelamentoNF2Result.class, getEnvelopeNamespaces(_returnEnv));
            return (br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeCancelamentoNF2Result) object;
        } catch (org.apache.axis2.AxisFault f) {
            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt != null) {
                if (faultExceptionNameMap.containsKey(faultElt.getQName())) {
                    try {
                        java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
                        java.lang.String messageClassName = (java.lang.String) faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt, messageClass, null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage", new java.lang.Class[] { messageClass });
                        m.invoke(ex, new java.lang.Object[] { messageObject });
                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    } catch (java.lang.ClassCastException e) {
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        throw f;
                    } catch (java.lang.NoSuchMethodException e) {
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        throw f;
                    } catch (java.lang.IllegalAccessException e) {
                        throw f;
                    } catch (java.lang.InstantiationException e) {
                        throw f;
                    }
                } else {
                    throw f;
                }
            } else {
                throw f;
            }
        } finally {
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
    }

    /**
	 * Auto generated method signature for Asynchronous Invocations Cancelamento
	 * de NF-e
	 * 
	 * @see br.inf.portalfiscal.www.nfe.wsdl.nfecancelamento2.NfeCancelamento2#startnfeCancelamentoNF2
	 * @param nfeDadosMsg0
	 * 
	 * @param nfeCabecMsg1
	 */
    public void startnfeCancelamentoNF2(br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeDadosMsg nfeDadosMsg0, br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeCabecMsgE nfeCabecMsg1, final br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2CallbackHandler callback) throws java.rmi.RemoteException {
        org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[0].getName());
        _operationClient.getOptions().setAction("http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2/nfeCancelamentoNF2");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
        addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        org.apache.axiom.soap.SOAPEnvelope env = null;
        final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();
        env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), nfeDadosMsg0, optimizeContent(new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2", "nfeCancelamentoNF2")));
        if (nfeCabecMsg1 != null) {
            org.apache.axiom.om.OMElement omElementnfeCabecMsg1 = toOM(nfeCabecMsg1, optimizeContent(new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2", "nfeCancelamentoNF2")));
            addHeader(omElementnfeCabecMsg1, env);
        }
        _serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);
        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {

            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                try {
                    org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                    java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(), br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeCancelamentoNF2Result.class, getEnvelopeNamespaces(resultEnv));
                    callback.receiveResultnfeCancelamentoNF2((br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeCancelamentoNF2Result) object);
                } catch (org.apache.axis2.AxisFault e) {
                    callback.receiveErrornfeCancelamentoNF2(e);
                }
            }

            public void onError(java.lang.Exception error) {
                if (error instanceof org.apache.axis2.AxisFault) {
                    org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
                    org.apache.axiom.om.OMElement faultElt = f.getDetail();
                    if (faultElt != null) {
                        if (faultExceptionNameMap.containsKey(faultElt.getQName())) {
                            try {
                                java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap.get(faultElt.getQName());
                                java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                                java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
                                java.lang.String messageClassName = (java.lang.String) faultMessageMap.get(faultElt.getQName());
                                java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                                java.lang.Object messageObject = fromOM(faultElt, messageClass, null);
                                java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage", new java.lang.Class[] { messageClass });
                                m.invoke(ex, new java.lang.Object[] { messageObject });
                                callback.receiveErrornfeCancelamentoNF2(new java.rmi.RemoteException(ex.getMessage(), ex));
                            } catch (java.lang.ClassCastException e) {
                                callback.receiveErrornfeCancelamentoNF2(f);
                            } catch (java.lang.ClassNotFoundException e) {
                                callback.receiveErrornfeCancelamentoNF2(f);
                            } catch (java.lang.NoSuchMethodException e) {
                                callback.receiveErrornfeCancelamentoNF2(f);
                            } catch (java.lang.reflect.InvocationTargetException e) {
                                callback.receiveErrornfeCancelamentoNF2(f);
                            } catch (java.lang.IllegalAccessException e) {
                                callback.receiveErrornfeCancelamentoNF2(f);
                            } catch (java.lang.InstantiationException e) {
                                callback.receiveErrornfeCancelamentoNF2(f);
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrornfeCancelamentoNF2(f);
                            }
                        } else {
                            callback.receiveErrornfeCancelamentoNF2(f);
                        }
                    } else {
                        callback.receiveErrornfeCancelamentoNF2(f);
                    }
                } else {
                    callback.receiveErrornfeCancelamentoNF2(error);
                }
            }

            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                onError(fault);
            }

            public void onComplete() {
                try {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                } catch (org.apache.axis2.AxisFault axisFault) {
                    callback.receiveErrornfeCancelamentoNF2(axisFault);
                }
            }
        });
        org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if (_operations[0].getMessageReceiver() == null && _operationClient.getOptions().isUseSeparateListener()) {
            _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
            _operations[0].setMessageReceiver(_callbackReceiver);
        }
        _operationClient.execute(false);
    }

    /**
	 * A utility method that copies the namepaces from the SOAPEnvelope
	 */
    private java.util.Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env) {
        java.util.Map returnMap = new java.util.HashMap();
        java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext()) {
            org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
            returnMap.put(ns.getPrefix(), ns.getNamespaceURI());
        }
        return returnMap;
    }

    private javax.xml.namespace.QName[] opNameArray = null;

    private boolean optimizeContent(javax.xml.namespace.QName opName) {
        if (opNameArray == null) {
            return false;
        }
        for (int i = 0; i < opNameArray.length; i++) {
            if (opName.equals(opNameArray[i])) {
                return true;
            }
        }
        return false;
    }

    public static class NfeDadosMsg implements org.apache.axis2.databinding.ADBBean {

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2", "nfeDadosMsg", "");

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if (namespace.equals("http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2")) {
                return "";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
		 * field for ExtraElement
		 */
        protected org.apache.axiom.om.OMElement localExtraElement;

        /**
		 * Auto generated getter method
		 * 
		 * @return org.apache.axiom.om.OMElement
		 */
        public org.apache.axiom.om.OMElement getExtraElement() {
            return localExtraElement;
        }

        /**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            ExtraElement
		 */
        public void setExtraElement(org.apache.axiom.om.OMElement param) {
            this.localExtraElement = param;
        }

        /**
		 * isReaderMTOMAware
		 * 
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
                    NfeDadosMsg.this.serialize(MY_QNAME, factory, xmlWriter);
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
                java.lang.String namespacePrefix = registerPrefix(xmlWriter, "http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":nfeDadosMsg", xmlWriter);
                } else {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "nfeDadosMsg", xmlWriter);
                }
            }
            if (localExtraElement != null) {
                localExtraElement.serialize(xmlWriter);
            } else {
                throw new org.apache.axis2.databinding.ADBException("extraElement cannot be null!!");
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
            if (localExtraElement != null) {
                elementList.add(org.apache.axis2.databinding.utils.Constants.OM_ELEMENT_KEY);
                elementList.add(localExtraElement);
            } else {
                throw new org.apache.axis2.databinding.ADBException("extraElement cannot be null!!");
            }
            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
        }

        /**
		 * Factory class that keeps the parse method
		 */
        public static class Factory {

            /**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
            public static NfeDadosMsg parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
                NfeDadosMsg object = new NfeDadosMsg();
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
                            if (!"nfeDadosMsg".equals(type)) {
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (NfeDadosMsg) ExtensionMapper.getTypeObject(nsUri, type, reader);
                            }
                        }
                    }
                    java.util.Vector handledAttributes = new java.util.Vector();
                    reader.next();
                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                    if (reader.isStartElement()) {
                        javax.xml.namespace.QName startQname1 = reader.getName();
                        org.apache.axis2.databinding.utils.NamedStaxOMBuilder builder1 = new org.apache.axis2.databinding.utils.NamedStaxOMBuilder(new org.apache.axis2.util.StreamWrapper(reader), startQname1);
                        object.setExtraElement(builder1.getOMElement());
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

    public static class NfeCancelamentoNF2Result implements org.apache.axis2.databinding.ADBBean {

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2", "nfeCancelamentoNF2Result", "");

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if (namespace.equals("http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2")) {
                return "";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
		 * field for ExtraElement
		 */
        protected org.apache.axiom.om.OMElement localExtraElement;

        /**
		 * Auto generated getter method
		 * 
		 * @return org.apache.axiom.om.OMElement
		 */
        public org.apache.axiom.om.OMElement getExtraElement() {
            return localExtraElement;
        }

        /**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            ExtraElement
		 */
        public void setExtraElement(org.apache.axiom.om.OMElement param) {
            this.localExtraElement = param;
        }

        /**
		 * isReaderMTOMAware
		 * 
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
                    NfeCancelamentoNF2Result.this.serialize(MY_QNAME, factory, xmlWriter);
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
                java.lang.String namespacePrefix = registerPrefix(xmlWriter, "http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":nfeCancelamentoNF2Result", xmlWriter);
                } else {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "nfeCancelamentoNF2Result", xmlWriter);
                }
            }
            if (localExtraElement != null) {
                localExtraElement.serialize(xmlWriter);
            } else {
                throw new org.apache.axis2.databinding.ADBException("extraElement cannot be null!!");
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
            if (localExtraElement != null) {
                elementList.add(org.apache.axis2.databinding.utils.Constants.OM_ELEMENT_KEY);
                elementList.add(localExtraElement);
            } else {
                throw new org.apache.axis2.databinding.ADBException("extraElement cannot be null!!");
            }
            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
        }

        /**
		 * Factory class that keeps the parse method
		 */
        public static class Factory {

            /**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
            public static NfeCancelamentoNF2Result parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
                NfeCancelamentoNF2Result object = new NfeCancelamentoNF2Result();
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
                            if (!"nfeCancelamentoNF2Result".equals(type)) {
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (NfeCancelamentoNF2Result) ExtensionMapper.getTypeObject(nsUri, type, reader);
                            }
                        }
                    }
                    java.util.Vector handledAttributes = new java.util.Vector();
                    reader.next();
                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                    if (reader.isStartElement()) {
                        javax.xml.namespace.QName startQname1 = reader.getName();
                        org.apache.axis2.databinding.utils.NamedStaxOMBuilder builder1 = new org.apache.axis2.databinding.utils.NamedStaxOMBuilder(new org.apache.axis2.util.StreamWrapper(reader), startQname1);
                        object.setExtraElement(builder1.getOMElement());
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

    public static class ExtensionMapper {

        public static java.lang.Object getTypeObject(java.lang.String namespaceURI, java.lang.String typeName, javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            if ("http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2".equals(namespaceURI) && "nfeCabecMsg".equals(typeName)) {
                return NfeCabecMsg.Factory.parse(reader);
            }
            throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
        }
    }

    public static class NfeCabecMsg implements org.apache.axis2.databinding.ADBBean {

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if (namespace.equals("http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2")) {
                return "";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
		 * field for CUF
		 */
        protected java.lang.String localCUF;

        protected boolean localCUFTracker = false;

        /**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
        public java.lang.String getCUF() {
            return localCUF;
        }

        /**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            CUF
		 */
        public void setCUF(java.lang.String param) {
            if (param != null) {
                localCUFTracker = true;
            } else {
                localCUFTracker = false;
            }
            this.localCUF = param;
        }

        /**
		 * field for VersaoDados
		 */
        protected java.lang.String localVersaoDados;

        protected boolean localVersaoDadosTracker = false;

        /**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
        public java.lang.String getVersaoDados() {
            return localVersaoDados;
        }

        /**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            VersaoDados
		 */
        public void setVersaoDados(java.lang.String param) {
            if (param != null) {
                localVersaoDadosTracker = true;
            } else {
                localVersaoDadosTracker = false;
            }
            this.localVersaoDados = param;
        }

        /**
		 * field for ExtraAttributes This was an Attribute! This was an Array!
		 */
        protected org.apache.axiom.om.OMAttribute[] localExtraAttributes;

        /**
		 * Auto generated getter method
		 * 
		 * @return org.apache.axiom.om.OMAttribute[]
		 */
        public org.apache.axiom.om.OMAttribute[] getExtraAttributes() {
            return localExtraAttributes;
        }

        /**
		 * validate the array for ExtraAttributes
		 */
        protected void validateExtraAttributes(org.apache.axiom.om.OMAttribute[] param) {
            if ((param != null) && (param.length > 1)) {
                throw new java.lang.RuntimeException();
            }
            if ((param != null) && (param.length < 1)) {
                throw new java.lang.RuntimeException();
            }
        }

        /**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            ExtraAttributes
		 */
        public void setExtraAttributes(org.apache.axiom.om.OMAttribute[] param) {
            validateExtraAttributes(param);
            this.localExtraAttributes = param;
        }

        /**
		 * Auto generated add method for the array for convenience
		 * 
		 * @param param
		 *            org.apache.axiom.om.OMAttribute
		 */
        public void addExtraAttributes(org.apache.axiom.om.OMAttribute param) {
            if (localExtraAttributes == null) {
                localExtraAttributes = new org.apache.axiom.om.OMAttribute[] {};
            }
            java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localExtraAttributes);
            list.add(param);
            this.localExtraAttributes = (org.apache.axiom.om.OMAttribute[]) list.toArray(new org.apache.axiom.om.OMAttribute[list.size()]);
        }

        /**
		 * isReaderMTOMAware
		 * 
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
                    NfeCabecMsg.this.serialize(parentQName, factory, xmlWriter);
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
                java.lang.String namespacePrefix = registerPrefix(xmlWriter, "http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":nfeCabecMsg", xmlWriter);
                } else {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "nfeCabecMsg", xmlWriter);
                }
            }
            if (localExtraAttributes != null) {
                for (int i = 0; i < localExtraAttributes.length; i++) {
                    writeAttribute(localExtraAttributes[i].getNamespace().getName(), localExtraAttributes[i].getLocalName(), localExtraAttributes[i].getAttributeValue(), xmlWriter);
                }
            }
            if (localCUFTracker) {
                namespace = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2";
                if (!namespace.equals("")) {
                    prefix = xmlWriter.getPrefix(namespace);
                    if (prefix == null) {
                        prefix = generatePrefix(namespace);
                        xmlWriter.writeStartElement(prefix, "cUF", namespace);
                        xmlWriter.writeNamespace(prefix, namespace);
                        xmlWriter.setPrefix(prefix, namespace);
                    } else {
                        xmlWriter.writeStartElement(namespace, "cUF");
                    }
                } else {
                    xmlWriter.writeStartElement("cUF");
                }
                if (localCUF == null) {
                    throw new org.apache.axis2.databinding.ADBException("cUF cannot be null!!");
                } else {
                    xmlWriter.writeCharacters(localCUF);
                }
                xmlWriter.writeEndElement();
            }
            if (localVersaoDadosTracker) {
                namespace = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2";
                if (!namespace.equals("")) {
                    prefix = xmlWriter.getPrefix(namespace);
                    if (prefix == null) {
                        prefix = generatePrefix(namespace);
                        xmlWriter.writeStartElement(prefix, "versaoDados", namespace);
                        xmlWriter.writeNamespace(prefix, namespace);
                        xmlWriter.setPrefix(prefix, namespace);
                    } else {
                        xmlWriter.writeStartElement(namespace, "versaoDados");
                    }
                } else {
                    xmlWriter.writeStartElement("versaoDados");
                }
                if (localVersaoDados == null) {
                    throw new org.apache.axis2.databinding.ADBException("versaoDados cannot be null!!");
                } else {
                    xmlWriter.writeCharacters(localVersaoDados);
                }
                xmlWriter.writeEndElement();
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
            if (localCUFTracker) {
                elementList.add(new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2", "cUF"));
                if (localCUF != null) {
                    elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCUF));
                } else {
                    throw new org.apache.axis2.databinding.ADBException("cUF cannot be null!!");
                }
            }
            if (localVersaoDadosTracker) {
                elementList.add(new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2", "versaoDados"));
                if (localVersaoDados != null) {
                    elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localVersaoDados));
                } else {
                    throw new org.apache.axis2.databinding.ADBException("versaoDados cannot be null!!");
                }
            }
            for (int i = 0; i < localExtraAttributes.length; i++) {
                attribList.add(org.apache.axis2.databinding.utils.Constants.OM_ATTRIBUTE_KEY);
                attribList.add(localExtraAttributes[i]);
            }
            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
        }

        /**
		 * Factory class that keeps the parse method
		 */
        public static class Factory {

            /**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
            public static NfeCabecMsg parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
                NfeCabecMsg object = new NfeCabecMsg();
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
                            if (!"nfeCabecMsg".equals(type)) {
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (NfeCabecMsg) ExtensionMapper.getTypeObject(nsUri, type, reader);
                            }
                        }
                    }
                    java.util.Vector handledAttributes = new java.util.Vector();
                    for (int i = 0; i < reader.getAttributeCount(); i++) {
                        if (!handledAttributes.contains(reader.getAttributeLocalName(i))) {
                            org.apache.axiom.om.impl.llom.OMAttributeImpl attr = new org.apache.axiom.om.impl.llom.OMAttributeImpl(reader.getAttributeLocalName(i), new org.apache.axiom.om.impl.dom.NamespaceImpl(reader.getAttributeNamespace(i), reader.getAttributePrefix(i)), reader.getAttributeValue(i), org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                            object.addExtraAttributes(attr);
                        }
                    }
                    reader.next();
                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2", "cUF").equals(reader.getName())) {
                        java.lang.String content = reader.getElementText();
                        object.setCUF(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                        reader.next();
                    } else {
                    }
                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2", "versaoDados").equals(reader.getName())) {
                        java.lang.String content = reader.getElementText();
                        object.setVersaoDados(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                        reader.next();
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

    public static class NfeCabecMsgE implements org.apache.axis2.databinding.ADBBean {

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2", "nfeCabecMsg", "");

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if (namespace.equals("http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2")) {
                return "";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
		 * field for NfeCabecMsg
		 */
        protected NfeCabecMsg localNfeCabecMsg;

        /**
		 * Auto generated getter method
		 * 
		 * @return NfeCabecMsg
		 */
        public NfeCabecMsg getNfeCabecMsg() {
            return localNfeCabecMsg;
        }

        /**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            NfeCabecMsg
		 */
        public void setNfeCabecMsg(NfeCabecMsg param) {
            this.localNfeCabecMsg = param;
        }

        /**
		 * isReaderMTOMAware
		 * 
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
                    NfeCabecMsgE.this.serialize(MY_QNAME, factory, xmlWriter);
                }
            };
            return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(MY_QNAME, factory, dataSource);
        }

        public void serialize(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory, org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
            serialize(parentQName, factory, xmlWriter, false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory, org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter, boolean serializeType) throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
            if (localNfeCabecMsg == null) {
                throw new org.apache.axis2.databinding.ADBException("Property cannot be null!");
            }
            localNfeCabecMsg.serialize(MY_QNAME, factory, xmlWriter);
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
            return localNfeCabecMsg.getPullParser(MY_QNAME);
        }

        /**
		 * Factory class that keeps the parse method
		 */
        public static class Factory {

            /**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
            public static NfeCabecMsgE parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
                NfeCabecMsgE object = new NfeCabecMsgE();
                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix = "";
                java.lang.String namespaceuri = "";
                try {
                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                    java.util.Vector handledAttributes = new java.util.Vector();
                    while (!reader.isEndElement()) {
                        if (reader.isStartElement()) {
                            if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2", "nfeCabecMsg").equals(reader.getName())) {
                                object.setNfeCabecMsg(NfeCabecMsg.Factory.parse(reader));
                            } else {
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                            }
                        } else {
                            reader.next();
                        }
                    }
                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }
                return object;
            }
        }
    }

    private org.apache.axiom.om.OMElement toOM(br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeDadosMsg param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeDadosMsg.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeCancelamentoNF2Result param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeCancelamentoNF2Result.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeCabecMsgE param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeCabecMsgE.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeDadosMsg param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeDadosMsg.MY_QNAME, factory));
            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    /**
	 * get the default envelope
	 */
    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory) {
        return factory.getDefaultEnvelope();
    }

    private java.lang.Object fromOM(org.apache.axiom.om.OMElement param, java.lang.Class type, java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault {
        try {
            if (br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeDadosMsg.class.equals(type)) {
                return br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeDadosMsg.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeCancelamentoNF2Result.class.equals(type)) {
                return br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeCancelamentoNF2Result.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeCabecMsgE.class.equals(type)) {
                return br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeCabecMsgE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeCabecMsgE.class.equals(type)) {
                return br.com.danielnegri.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub.NfeCabecMsgE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
        } catch (java.lang.Exception e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
        return null;
    }
}
