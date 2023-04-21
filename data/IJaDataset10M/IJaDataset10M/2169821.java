package be.kuleuven.cs.samgi;

public class SAmgIStub extends org.apache.axis2.client.Stub {

    protected org.apache.axis2.description.AxisOperation[] _operations;

    private java.util.HashMap faultExceptionNameMap = new java.util.HashMap();

    private java.util.HashMap faultExceptionClassNameMap = new java.util.HashMap();

    private java.util.HashMap faultMessageMap = new java.util.HashMap();

    private static int counter = 0;

    private static synchronized String getUniqueSuffix() {
        if (counter > 99999) {
            counter = 0;
        }
        counter = counter + 1;
        return Long.toString(System.currentTimeMillis()) + "_" + counter;
    }

    private void populateAxisService() throws org.apache.axis2.AxisFault {
        _service = new org.apache.axis2.description.AxisService("SAmgI" + getUniqueSuffix());
        addAnonymousOperations();
        org.apache.axis2.description.AxisOperation __operation;
        _operations = new org.apache.axis2.description.AxisOperation[4];
        __operation = new org.apache.axis2.description.OutInAxisOperation();
        __operation.setName(new javax.xml.namespace.QName("http://cs.kuleuven.be/SAmgI/", "GetOutput"));
        _service.addOperation(__operation);
        _operations[0] = __operation;
        __operation = new org.apache.axis2.description.OutInAxisOperation();
        __operation.setName(new javax.xml.namespace.QName("http://cs.kuleuven.be/SAmgI/", "GetGenerators"));
        _service.addOperation(__operation);
        _operations[1] = __operation;
        __operation = new org.apache.axis2.description.OutInAxisOperation();
        __operation.setName(new javax.xml.namespace.QName("http://cs.kuleuven.be/SAmgI/", "GetMetadata"));
        _service.addOperation(__operation);
        _operations[2] = __operation;
        __operation = new org.apache.axis2.description.OutInAxisOperation();
        __operation.setName(new javax.xml.namespace.QName("http://cs.kuleuven.be/SAmgI/", "GetInput"));
        _service.addOperation(__operation);
        _operations[3] = __operation;
    }

    private void populateFaults() {
    }

    /**
      *Constructor that takes in a configContext
      */
    public SAmgIStub(org.apache.axis2.context.ConfigurationContext configurationContext, java.lang.String targetEndpoint) throws org.apache.axis2.AxisFault {
        this(configurationContext, targetEndpoint, false);
    }

    /**
     * Constructor that takes in a configContext  and useseperate listner
     */
    public SAmgIStub(org.apache.axis2.context.ConfigurationContext configurationContext, java.lang.String targetEndpoint, boolean useSeparateListener) throws org.apache.axis2.AxisFault {
        populateAxisService();
        populateFaults();
        _serviceClient = new org.apache.axis2.client.ServiceClient(configurationContext, _service);
        configurationContext = _serviceClient.getServiceContext().getConfigurationContext();
        _serviceClient.getOptions().setTo(new org.apache.axis2.addressing.EndpointReference(targetEndpoint));
        _serviceClient.getOptions().setUseSeparateListener(useSeparateListener);
    }

    /**
     * Default Constructor
     */
    public SAmgIStub(org.apache.axis2.context.ConfigurationContext configurationContext) throws org.apache.axis2.AxisFault {
        this(configurationContext, "http://cs.kuleuven.be/");
    }

    /**
     * Default Constructor
     */
    public SAmgIStub() throws org.apache.axis2.AxisFault {
        this("http://cs.kuleuven.be/");
    }

    /**
     * Constructor taking the target endpoint
     */
    public SAmgIStub(java.lang.String targetEndpoint) throws org.apache.axis2.AxisFault {
        this(null, targetEndpoint);
    }

    /**
                     * Auto generated method signature
                     * 
                     * @see be.kuleuven.cs.samgi.SAmgI#GetOutput
                     * @param entrypoint59
                    
                     */
    public be.kuleuven.cs.samgi.ParameterList GetOutput(be.kuleuven.cs.samgi.EntrypointE entrypoint59) throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;
        try {
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[0].getName());
            _operationClient.getOptions().setAction("http://cs.kuleuven.be/SAmgI/GetOutput");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
            addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
            _messageContext = new org.apache.axis2.context.MessageContext();
            org.apache.axiom.soap.SOAPEnvelope env = null;
            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), entrypoint59, optimizeContent(new javax.xml.namespace.QName("http://cs.kuleuven.be/SAmgI/", "GetOutput")));
            _serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);
            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
            java.lang.Object object = fromOM(_returnEnv.getBody().getFirstElement(), be.kuleuven.cs.samgi.ParameterList.class, getEnvelopeNamespaces(_returnEnv));
            return (be.kuleuven.cs.samgi.ParameterList) object;
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
                * Auto generated method signature for Asynchronous Invocations
                * 
                * @see be.kuleuven.cs.samgi.SAmgI#startGetOutput
                    * @param entrypoint59
                
                */
    public void startGetOutput(be.kuleuven.cs.samgi.EntrypointE entrypoint59, final be.kuleuven.cs.samgi.SAmgICallbackHandler callback) throws java.rmi.RemoteException {
        org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[0].getName());
        _operationClient.getOptions().setAction("http://cs.kuleuven.be/SAmgI/GetOutput");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
        addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        org.apache.axiom.soap.SOAPEnvelope env = null;
        final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();
        env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), entrypoint59, optimizeContent(new javax.xml.namespace.QName("http://cs.kuleuven.be/SAmgI/", "GetOutput")));
        _serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);
        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {

            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                try {
                    org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                    java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(), be.kuleuven.cs.samgi.ParameterList.class, getEnvelopeNamespaces(resultEnv));
                    callback.receiveResultGetOutput((be.kuleuven.cs.samgi.ParameterList) object);
                } catch (org.apache.axis2.AxisFault e) {
                    callback.receiveErrorGetOutput(e);
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
                                callback.receiveErrorGetOutput(new java.rmi.RemoteException(ex.getMessage(), ex));
                            } catch (java.lang.ClassCastException e) {
                                callback.receiveErrorGetOutput(f);
                            } catch (java.lang.ClassNotFoundException e) {
                                callback.receiveErrorGetOutput(f);
                            } catch (java.lang.NoSuchMethodException e) {
                                callback.receiveErrorGetOutput(f);
                            } catch (java.lang.reflect.InvocationTargetException e) {
                                callback.receiveErrorGetOutput(f);
                            } catch (java.lang.IllegalAccessException e) {
                                callback.receiveErrorGetOutput(f);
                            } catch (java.lang.InstantiationException e) {
                                callback.receiveErrorGetOutput(f);
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorGetOutput(f);
                            }
                        } else {
                            callback.receiveErrorGetOutput(f);
                        }
                    } else {
                        callback.receiveErrorGetOutput(f);
                    }
                } else {
                    callback.receiveErrorGetOutput(error);
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
                    callback.receiveErrorGetOutput(axisFault);
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
                     * Auto generated method signature
                     * 
                     * @see be.kuleuven.cs.samgi.SAmgI#GetGenerators
                     */
    public be.kuleuven.cs.samgi.GetGeneratorsResponse GetGenerators() throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;
        try {
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[1].getName());
            _operationClient.getOptions().setAction("http://cs.kuleuven.be/SAmgI/GetGenerators");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
            addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
            _messageContext = new org.apache.axis2.context.MessageContext();
            org.apache.axiom.soap.SOAPEnvelope env = null;
            org.apache.axiom.soap.SOAPFactory factory = getFactory(_operationClient.getOptions().getSoapVersionURI());
            env = factory.getDefaultEnvelope();
            _serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);
            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
            java.lang.Object object = fromOM(_returnEnv.getBody().getFirstElement(), be.kuleuven.cs.samgi.GetGeneratorsResponse.class, getEnvelopeNamespaces(_returnEnv));
            return (be.kuleuven.cs.samgi.GetGeneratorsResponse) object;
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
                * Auto generated method signature for Asynchronous Invocations
                * 
                * @see be.kuleuven.cs.samgi.SAmgI#startGetGenerators
                */
    public void startGetGenerators(final be.kuleuven.cs.samgi.SAmgICallbackHandler callback) throws java.rmi.RemoteException {
        org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[1].getName());
        _operationClient.getOptions().setAction("http://cs.kuleuven.be/SAmgI/GetGenerators");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
        addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        org.apache.axiom.soap.SOAPEnvelope env = null;
        final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();
        org.apache.axiom.soap.SOAPFactory factory = getFactory(_operationClient.getOptions().getSoapVersionURI());
        env = factory.getDefaultEnvelope();
        _serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);
        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {

            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                try {
                    org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                    java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(), be.kuleuven.cs.samgi.GetGeneratorsResponse.class, getEnvelopeNamespaces(resultEnv));
                    callback.receiveResultGetGenerators((be.kuleuven.cs.samgi.GetGeneratorsResponse) object);
                } catch (org.apache.axis2.AxisFault e) {
                    callback.receiveErrorGetGenerators(e);
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
                                callback.receiveErrorGetGenerators(new java.rmi.RemoteException(ex.getMessage(), ex));
                            } catch (java.lang.ClassCastException e) {
                                callback.receiveErrorGetGenerators(f);
                            } catch (java.lang.ClassNotFoundException e) {
                                callback.receiveErrorGetGenerators(f);
                            } catch (java.lang.NoSuchMethodException e) {
                                callback.receiveErrorGetGenerators(f);
                            } catch (java.lang.reflect.InvocationTargetException e) {
                                callback.receiveErrorGetGenerators(f);
                            } catch (java.lang.IllegalAccessException e) {
                                callback.receiveErrorGetGenerators(f);
                            } catch (java.lang.InstantiationException e) {
                                callback.receiveErrorGetGenerators(f);
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorGetGenerators(f);
                            }
                        } else {
                            callback.receiveErrorGetGenerators(f);
                        }
                    } else {
                        callback.receiveErrorGetGenerators(f);
                    }
                } else {
                    callback.receiveErrorGetGenerators(error);
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
                    callback.receiveErrorGetGenerators(axisFault);
                }
            }
        });
        org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if (_operations[1].getMessageReceiver() == null && _operationClient.getOptions().isUseSeparateListener()) {
            _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
            _operations[1].setMessageReceiver(_callbackReceiver);
        }
        _operationClient.execute(false);
    }

    /**
                     * Auto generated method signature
                     * 
                     * @see be.kuleuven.cs.samgi.SAmgI#GetMetadata
                     * @param getMetadata63
                    
                     */
    public be.kuleuven.cs.samgi.GetMetadataResponse GetMetadata(be.kuleuven.cs.samgi.GetMetadata getMetadata63) throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;
        try {
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[2].getName());
            _operationClient.getOptions().setAction("http://cs.kuleuven.be/SAmgI/GetMetadata");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
            addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
            _messageContext = new org.apache.axis2.context.MessageContext();
            org.apache.axiom.soap.SOAPEnvelope env = null;
            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), getMetadata63, optimizeContent(new javax.xml.namespace.QName("http://cs.kuleuven.be/SAmgI/", "GetMetadata")));
            _serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);
            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
            java.lang.Object object = fromOM(_returnEnv.getBody().getFirstElement(), be.kuleuven.cs.samgi.GetMetadataResponse.class, getEnvelopeNamespaces(_returnEnv));
            return (be.kuleuven.cs.samgi.GetMetadataResponse) object;
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
                * Auto generated method signature for Asynchronous Invocations
                * 
                * @see be.kuleuven.cs.samgi.SAmgI#startGetMetadata
                    * @param getMetadata63
                
                */
    public void startGetMetadata(be.kuleuven.cs.samgi.GetMetadata getMetadata63, final be.kuleuven.cs.samgi.SAmgICallbackHandler callback) throws java.rmi.RemoteException {
        org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[2].getName());
        _operationClient.getOptions().setAction("http://cs.kuleuven.be/SAmgI/GetMetadata");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
        addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        org.apache.axiom.soap.SOAPEnvelope env = null;
        final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();
        env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), getMetadata63, optimizeContent(new javax.xml.namespace.QName("http://cs.kuleuven.be/SAmgI/", "GetMetadata")));
        _serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);
        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {

            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                try {
                    org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                    java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(), be.kuleuven.cs.samgi.GetMetadataResponse.class, getEnvelopeNamespaces(resultEnv));
                    callback.receiveResultGetMetadata((be.kuleuven.cs.samgi.GetMetadataResponse) object);
                } catch (org.apache.axis2.AxisFault e) {
                    callback.receiveErrorGetMetadata(e);
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
                                callback.receiveErrorGetMetadata(new java.rmi.RemoteException(ex.getMessage(), ex));
                            } catch (java.lang.ClassCastException e) {
                                callback.receiveErrorGetMetadata(f);
                            } catch (java.lang.ClassNotFoundException e) {
                                callback.receiveErrorGetMetadata(f);
                            } catch (java.lang.NoSuchMethodException e) {
                                callback.receiveErrorGetMetadata(f);
                            } catch (java.lang.reflect.InvocationTargetException e) {
                                callback.receiveErrorGetMetadata(f);
                            } catch (java.lang.IllegalAccessException e) {
                                callback.receiveErrorGetMetadata(f);
                            } catch (java.lang.InstantiationException e) {
                                callback.receiveErrorGetMetadata(f);
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorGetMetadata(f);
                            }
                        } else {
                            callback.receiveErrorGetMetadata(f);
                        }
                    } else {
                        callback.receiveErrorGetMetadata(f);
                    }
                } else {
                    callback.receiveErrorGetMetadata(error);
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
                    callback.receiveErrorGetMetadata(axisFault);
                }
            }
        });
        org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if (_operations[2].getMessageReceiver() == null && _operationClient.getOptions().isUseSeparateListener()) {
            _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
            _operations[2].setMessageReceiver(_callbackReceiver);
        }
        _operationClient.execute(false);
    }

    /**
                     * Auto generated method signature
                     * 
                     * @see be.kuleuven.cs.samgi.SAmgI#GetInput
                     * @param entrypoint65
                    
                     */
    public be.kuleuven.cs.samgi.ParameterList GetInput(be.kuleuven.cs.samgi.EntrypointE entrypoint65) throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;
        try {
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[3].getName());
            _operationClient.getOptions().setAction("http://cs.kuleuven.be/SAmgI/GetInput");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
            addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
            _messageContext = new org.apache.axis2.context.MessageContext();
            org.apache.axiom.soap.SOAPEnvelope env = null;
            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), entrypoint65, optimizeContent(new javax.xml.namespace.QName("http://cs.kuleuven.be/SAmgI/", "GetInput")));
            _serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);
            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
            java.lang.Object object = fromOM(_returnEnv.getBody().getFirstElement(), be.kuleuven.cs.samgi.ParameterList.class, getEnvelopeNamespaces(_returnEnv));
            return (be.kuleuven.cs.samgi.ParameterList) object;
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
                * Auto generated method signature for Asynchronous Invocations
                * 
                * @see be.kuleuven.cs.samgi.SAmgI#startGetInput
                    * @param entrypoint65
                
                */
    public void startGetInput(be.kuleuven.cs.samgi.EntrypointE entrypoint65, final be.kuleuven.cs.samgi.SAmgICallbackHandler callback) throws java.rmi.RemoteException {
        org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[3].getName());
        _operationClient.getOptions().setAction("http://cs.kuleuven.be/SAmgI/GetInput");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
        addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        org.apache.axiom.soap.SOAPEnvelope env = null;
        final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();
        env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), entrypoint65, optimizeContent(new javax.xml.namespace.QName("http://cs.kuleuven.be/SAmgI/", "GetInput")));
        _serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);
        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {

            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                try {
                    org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                    java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(), be.kuleuven.cs.samgi.ParameterList.class, getEnvelopeNamespaces(resultEnv));
                    callback.receiveResultGetInput((be.kuleuven.cs.samgi.ParameterList) object);
                } catch (org.apache.axis2.AxisFault e) {
                    callback.receiveErrorGetInput(e);
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
                                callback.receiveErrorGetInput(new java.rmi.RemoteException(ex.getMessage(), ex));
                            } catch (java.lang.ClassCastException e) {
                                callback.receiveErrorGetInput(f);
                            } catch (java.lang.ClassNotFoundException e) {
                                callback.receiveErrorGetInput(f);
                            } catch (java.lang.NoSuchMethodException e) {
                                callback.receiveErrorGetInput(f);
                            } catch (java.lang.reflect.InvocationTargetException e) {
                                callback.receiveErrorGetInput(f);
                            } catch (java.lang.IllegalAccessException e) {
                                callback.receiveErrorGetInput(f);
                            } catch (java.lang.InstantiationException e) {
                                callback.receiveErrorGetInput(f);
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorGetInput(f);
                            }
                        } else {
                            callback.receiveErrorGetInput(f);
                        }
                    } else {
                        callback.receiveErrorGetInput(f);
                    }
                } else {
                    callback.receiveErrorGetInput(error);
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
                    callback.receiveErrorGetInput(axisFault);
                }
            }
        });
        org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if (_operations[3].getMessageReceiver() == null && _operationClient.getOptions().isUseSeparateListener()) {
            _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
            _operations[3].setMessageReceiver(_callbackReceiver);
        }
        _operationClient.execute(false);
    }

    /**
        *  A utility method that copies the namepaces from the SOAPEnvelope
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

    private org.apache.axiom.om.OMElement toOM(be.kuleuven.cs.samgi.EntrypointE param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(be.kuleuven.cs.samgi.EntrypointE.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(be.kuleuven.cs.samgi.ParameterList param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(be.kuleuven.cs.samgi.ParameterList.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(be.kuleuven.cs.samgi.GetGeneratorsResponse param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(be.kuleuven.cs.samgi.GetGeneratorsResponse.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(be.kuleuven.cs.samgi.GetMetadata param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(be.kuleuven.cs.samgi.GetMetadata.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(be.kuleuven.cs.samgi.GetMetadataResponse param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(be.kuleuven.cs.samgi.GetMetadataResponse.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, be.kuleuven.cs.samgi.EntrypointE param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(be.kuleuven.cs.samgi.EntrypointE.MY_QNAME, factory));
            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, be.kuleuven.cs.samgi.GetMetadata param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(be.kuleuven.cs.samgi.GetMetadata.MY_QNAME, factory));
            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    /**
        *  get the default envelope
        */
    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory) {
        return factory.getDefaultEnvelope();
    }

    private java.lang.Object fromOM(org.apache.axiom.om.OMElement param, java.lang.Class type, java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault {
        try {
            if (be.kuleuven.cs.samgi.EntrypointE.class.equals(type)) {
                return be.kuleuven.cs.samgi.EntrypointE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (be.kuleuven.cs.samgi.ParameterList.class.equals(type)) {
                return be.kuleuven.cs.samgi.ParameterList.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (be.kuleuven.cs.samgi.GetGeneratorsResponse.class.equals(type)) {
                return be.kuleuven.cs.samgi.GetGeneratorsResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (be.kuleuven.cs.samgi.GetMetadata.class.equals(type)) {
                return be.kuleuven.cs.samgi.GetMetadata.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (be.kuleuven.cs.samgi.GetMetadataResponse.class.equals(type)) {
                return be.kuleuven.cs.samgi.GetMetadataResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (be.kuleuven.cs.samgi.EntrypointE.class.equals(type)) {
                return be.kuleuven.cs.samgi.EntrypointE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (be.kuleuven.cs.samgi.ParameterList.class.equals(type)) {
                return be.kuleuven.cs.samgi.ParameterList.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
        } catch (java.lang.Exception e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
        return null;
    }
}
