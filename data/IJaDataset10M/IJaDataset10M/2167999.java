package de.vsis.coordination.coordinator.ba.messagereceiver;

public class BusinessAgreementWithCompleteCoordinatorServiceStub extends org.apache.axis2.client.Stub {

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
        return java.lang.Long.toString(java.lang.System.currentTimeMillis()) + "_" + counter;
    }

    private void populateAxisService() throws org.apache.axis2.AxisFault {
        _service = new org.apache.axis2.description.AxisService("BusinessAgreementWithCompleteCoordinatorService" + getUniqueSuffix());
        addAnonymousOperations();
        org.apache.axis2.description.AxisOperation __operation;
        _operations = new org.apache.axis2.description.AxisOperation[10];
        __operation = new org.apache.axis2.description.OutOnlyAxisOperation();
        __operation.setName(new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "chooseOperation"));
        _service.addOperation(__operation);
        _operations[0] = __operation;
        __operation = new org.apache.axis2.description.OutOnlyAxisOperation();
        __operation.setName(new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "exitOperation"));
        _service.addOperation(__operation);
        _operations[1] = __operation;
        __operation = new org.apache.axis2.description.OutOnlyAxisOperation();
        __operation.setName(new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "cannotComplete"));
        _service.addOperation(__operation);
        _operations[2] = __operation;
        __operation = new org.apache.axis2.description.OutOnlyAxisOperation();
        __operation.setName(new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "completedOperation"));
        _service.addOperation(__operation);
        _operations[3] = __operation;
        __operation = new org.apache.axis2.description.OutOnlyAxisOperation();
        __operation.setName(new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "closedOperation"));
        _service.addOperation(__operation);
        _operations[4] = __operation;
        __operation = new org.apache.axis2.description.OutOnlyAxisOperation();
        __operation.setName(new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "failOperation"));
        _service.addOperation(__operation);
        _operations[5] = __operation;
        __operation = new org.apache.axis2.description.OutOnlyAxisOperation();
        __operation.setName(new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "compensatedOperation"));
        _service.addOperation(__operation);
        _operations[6] = __operation;
        __operation = new org.apache.axis2.description.OutOnlyAxisOperation();
        __operation.setName(new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "canceledOperation"));
        _service.addOperation(__operation);
        _operations[7] = __operation;
        __operation = new org.apache.axis2.description.OutOnlyAxisOperation();
        __operation.setName(new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "getStatusOperation"));
        _service.addOperation(__operation);
        _operations[8] = __operation;
        __operation = new org.apache.axis2.description.OutOnlyAxisOperation();
        __operation.setName(new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "statusOperation"));
        _service.addOperation(__operation);
        _operations[9] = __operation;
    }

    private void populateFaults() {
    }

    /**
	 * Constructor that takes in a configContext
	 */
    public BusinessAgreementWithCompleteCoordinatorServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext, java.lang.String targetEndpoint) throws org.apache.axis2.AxisFault {
        this(configurationContext, targetEndpoint, false);
    }

    /**
	 * Constructor that takes in a configContext and useseperate listner
	 */
    public BusinessAgreementWithCompleteCoordinatorServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext, java.lang.String targetEndpoint, boolean useSeparateListener) throws org.apache.axis2.AxisFault {
        populateAxisService();
        populateFaults();
        _serviceClient = new org.apache.axis2.client.ServiceClient(configurationContext, _service);
        _serviceClient.getOptions().setTo(new org.apache.axis2.addressing.EndpointReference(targetEndpoint));
        _serviceClient.getOptions().setUseSeparateListener(useSeparateListener);
    }

    /**
	 * Default Constructor
	 */
    public BusinessAgreementWithCompleteCoordinatorServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext) throws org.apache.axis2.AxisFault {
        this(configurationContext, "http://localhost:8080/axis2/services/BusinessAgreementWithCompleteCoordinatorService");
    }

    /**
	 * Default Constructor
	 */
    public BusinessAgreementWithCompleteCoordinatorServiceStub() throws org.apache.axis2.AxisFault {
        this("http://localhost:8080/axis2/services/BusinessAgreementWithCompleteCoordinatorService");
    }

    /**
	 * Constructor taking the target endpoint
	 */
    public BusinessAgreementWithCompleteCoordinatorServiceStub(java.lang.String targetEndpoint) throws org.apache.axis2.AxisFault {
        this(null, targetEndpoint);
    }

    /**
	 * Auto generated method signature
	 * 
	 */
    public void chooseOperation(de.vsis.coordination.stubs.wsba.extensions.Choose choose60) throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;
        org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[0].getName());
        _operationClient.getOptions().setAction("http://coordination.vsis.de/wsba/ChooseOperation");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
        addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        org.apache.axiom.soap.SOAPEnvelope env = null;
        _messageContext = new org.apache.axis2.context.MessageContext();
        env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), choose60, optimizeContent(new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "chooseOperation")), new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "chooseOperation"));
        _serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);
        _operationClient.execute(true);
        if (_messageContext.getTransportOut() != null) {
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
        return;
    }

    /**
	 * Auto generated method signature
	 * 
	 */
    public void exitOperation(de.vsis.coordination.stubs.wsba.Exit exit61) throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;
        org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[1].getName());
        _operationClient.getOptions().setAction("http://docs.oasis-open.org/ws-tx/wsba/2006/06/ExitOperation");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
        addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        org.apache.axiom.soap.SOAPEnvelope env = null;
        _messageContext = new org.apache.axis2.context.MessageContext();
        env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), exit61, optimizeContent(new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "exitOperation")), new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "exitOperation"));
        _serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);
        _operationClient.execute(true);
        if (_messageContext.getTransportOut() != null) {
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
        return;
    }

    /**
	 * Auto generated method signature
	 * 
	 */
    public void cannotComplete(de.vsis.coordination.stubs.wsba.CannotComplete cannotComplete62) throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;
        org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[2].getName());
        _operationClient.getOptions().setAction("http://docs.oasis-open.org/ws-tx/wsba/2006/06/CannotComplete");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
        addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        org.apache.axiom.soap.SOAPEnvelope env = null;
        _messageContext = new org.apache.axis2.context.MessageContext();
        env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), cannotComplete62, optimizeContent(new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "cannotComplete")), new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "cannotComplete"));
        _serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);
        _operationClient.execute(true);
        if (_messageContext.getTransportOut() != null) {
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
        return;
    }

    /**
	 * Auto generated method signature
	 * 
	 */
    public void completedOperation(de.vsis.coordination.stubs.wsba.Completed completed63) throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;
        org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[3].getName());
        _operationClient.getOptions().setAction("http://docs.oasis-open.org/ws-tx/wsba/2006/06/CompletedOperation");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
        addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        org.apache.axiom.soap.SOAPEnvelope env = null;
        _messageContext = new org.apache.axis2.context.MessageContext();
        env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), completed63, optimizeContent(new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "completedOperation")), new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "completedOperation"));
        _serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);
        _operationClient.execute(true);
        if (_messageContext.getTransportOut() != null) {
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
        return;
    }

    /**
	 * Auto generated method signature
	 * 
	 */
    public void closedOperation(de.vsis.coordination.stubs.wsba.Closed closed64) throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;
        org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[4].getName());
        _operationClient.getOptions().setAction("http://docs.oasis-open.org/ws-tx/wsba/2006/06/ClosedOperation");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
        addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        org.apache.axiom.soap.SOAPEnvelope env = null;
        _messageContext = new org.apache.axis2.context.MessageContext();
        env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), closed64, optimizeContent(new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "closedOperation")), new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "closedOperation"));
        _serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);
        _operationClient.execute(true);
        if (_messageContext.getTransportOut() != null) {
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
        return;
    }

    /**
	 * Auto generated method signature
	 * 
	 */
    public void failOperation(de.vsis.coordination.stubs.wsba.Fail fail65) throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;
        org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[5].getName());
        _operationClient.getOptions().setAction("http://docs.oasis-open.org/ws-tx/wsba/2006/06/FailOperation");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
        addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        org.apache.axiom.soap.SOAPEnvelope env = null;
        _messageContext = new org.apache.axis2.context.MessageContext();
        env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), fail65, optimizeContent(new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "failOperation")), new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "failOperation"));
        _serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);
        _operationClient.execute(true);
        if (_messageContext.getTransportOut() != null) {
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
        return;
    }

    /**
	 * Auto generated method signature
	 * 
	 */
    public void compensatedOperation(de.vsis.coordination.stubs.wsba.Compensated compensated66) throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;
        org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[6].getName());
        _operationClient.getOptions().setAction("http://docs.oasis-open.org/ws-tx/wsba/2006/06/CompensatedOperation");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
        addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        org.apache.axiom.soap.SOAPEnvelope env = null;
        _messageContext = new org.apache.axis2.context.MessageContext();
        env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), compensated66, optimizeContent(new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "compensatedOperation")), new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "compensatedOperation"));
        _serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);
        _operationClient.execute(true);
        if (_messageContext.getTransportOut() != null) {
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
        return;
    }

    /**
	 * Auto generated method signature
	 * 
	 */
    public void canceledOperation(de.vsis.coordination.stubs.wsba.Canceled canceled67) throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;
        org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[7].getName());
        _operationClient.getOptions().setAction("http://docs.oasis-open.org/ws-tx/wsba/2006/06/CanceledOperation");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
        addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        org.apache.axiom.soap.SOAPEnvelope env = null;
        _messageContext = new org.apache.axis2.context.MessageContext();
        env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), canceled67, optimizeContent(new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "canceledOperation")), new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "canceledOperation"));
        _serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);
        _operationClient.execute(true);
        if (_messageContext.getTransportOut() != null) {
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
        return;
    }

    /**
	 * Auto generated method signature
	 * 
	 */
    public void getStatusOperation(de.vsis.coordination.stubs.wsba.GetStatus getStatus68) throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;
        org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[8].getName());
        _operationClient.getOptions().setAction("http://docs.oasis-open.org/ws-tx/wsba/2006/06/GetStatusOperation");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
        addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        org.apache.axiom.soap.SOAPEnvelope env = null;
        _messageContext = new org.apache.axis2.context.MessageContext();
        env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), getStatus68, optimizeContent(new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "getStatusOperation")), new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "getStatusOperation"));
        _serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);
        _operationClient.execute(true);
        if (_messageContext.getTransportOut() != null) {
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
        return;
    }

    /**
	 * Auto generated method signature
	 * 
	 */
    public void statusOperation(de.vsis.coordination.stubs.wsba.Status status69) throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;
        org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[9].getName());
        _operationClient.getOptions().setAction("http://docs.oasis-open.org/ws-tx/wsba/2006/06/StatusOperation");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
        addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        org.apache.axiom.soap.SOAPEnvelope env = null;
        _messageContext = new org.apache.axis2.context.MessageContext();
        env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), status69, optimizeContent(new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "statusOperation")), new javax.xml.namespace.QName("http://docs.oasis-open.org/ws-tx/wsba/2006/06", "statusOperation"));
        _serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);
        _operationClient.execute(true);
        if (_messageContext.getTransportOut() != null) {
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
        return;
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

    private org.apache.axiom.om.OMElement toOM(de.vsis.coordination.stubs.wsba.extensions.Choose param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(de.vsis.coordination.stubs.wsba.extensions.Choose.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(de.vsis.coordination.stubs.wsba.Exit param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(de.vsis.coordination.stubs.wsba.Exit.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(de.vsis.coordination.stubs.wsba.CannotComplete param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(de.vsis.coordination.stubs.wsba.CannotComplete.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(de.vsis.coordination.stubs.wsba.Completed param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(de.vsis.coordination.stubs.wsba.Completed.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(de.vsis.coordination.stubs.wsba.Closed param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(de.vsis.coordination.stubs.wsba.Closed.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(de.vsis.coordination.stubs.wsba.Fail param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(de.vsis.coordination.stubs.wsba.Fail.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(de.vsis.coordination.stubs.wsba.Compensated param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(de.vsis.coordination.stubs.wsba.Compensated.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(de.vsis.coordination.stubs.wsba.Canceled param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(de.vsis.coordination.stubs.wsba.Canceled.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(de.vsis.coordination.stubs.wsba.GetStatus param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(de.vsis.coordination.stubs.wsba.GetStatus.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(de.vsis.coordination.stubs.wsba.Status param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(de.vsis.coordination.stubs.wsba.Status.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, de.vsis.coordination.stubs.wsba.extensions.Choose param, boolean optimizeContent, javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(de.vsis.coordination.stubs.wsba.extensions.Choose.MY_QNAME, factory));
            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, de.vsis.coordination.stubs.wsba.Exit param, boolean optimizeContent, javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(de.vsis.coordination.stubs.wsba.Exit.MY_QNAME, factory));
            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, de.vsis.coordination.stubs.wsba.CannotComplete param, boolean optimizeContent, javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(de.vsis.coordination.stubs.wsba.CannotComplete.MY_QNAME, factory));
            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, de.vsis.coordination.stubs.wsba.Completed param, boolean optimizeContent, javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(de.vsis.coordination.stubs.wsba.Completed.MY_QNAME, factory));
            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, de.vsis.coordination.stubs.wsba.Closed param, boolean optimizeContent, javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(de.vsis.coordination.stubs.wsba.Closed.MY_QNAME, factory));
            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, de.vsis.coordination.stubs.wsba.Fail param, boolean optimizeContent, javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(de.vsis.coordination.stubs.wsba.Fail.MY_QNAME, factory));
            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, de.vsis.coordination.stubs.wsba.Compensated param, boolean optimizeContent, javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(de.vsis.coordination.stubs.wsba.Compensated.MY_QNAME, factory));
            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, de.vsis.coordination.stubs.wsba.Canceled param, boolean optimizeContent, javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(de.vsis.coordination.stubs.wsba.Canceled.MY_QNAME, factory));
            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, de.vsis.coordination.stubs.wsba.GetStatus param, boolean optimizeContent, javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(de.vsis.coordination.stubs.wsba.GetStatus.MY_QNAME, factory));
            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, de.vsis.coordination.stubs.wsba.Status param, boolean optimizeContent, javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(de.vsis.coordination.stubs.wsba.Status.MY_QNAME, factory));
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
            if (de.vsis.coordination.stubs.wsba.extensions.Choose.class.equals(type)) {
                return de.vsis.coordination.stubs.wsba.extensions.Choose.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (de.vsis.coordination.stubs.wsba.Exit.class.equals(type)) {
                return de.vsis.coordination.stubs.wsba.Exit.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (de.vsis.coordination.stubs.wsba.CannotComplete.class.equals(type)) {
                return de.vsis.coordination.stubs.wsba.CannotComplete.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (de.vsis.coordination.stubs.wsba.Completed.class.equals(type)) {
                return de.vsis.coordination.stubs.wsba.Completed.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (de.vsis.coordination.stubs.wsba.Closed.class.equals(type)) {
                return de.vsis.coordination.stubs.wsba.Closed.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (de.vsis.coordination.stubs.wsba.Fail.class.equals(type)) {
                return de.vsis.coordination.stubs.wsba.Fail.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (de.vsis.coordination.stubs.wsba.Compensated.class.equals(type)) {
                return de.vsis.coordination.stubs.wsba.Compensated.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (de.vsis.coordination.stubs.wsba.Canceled.class.equals(type)) {
                return de.vsis.coordination.stubs.wsba.Canceled.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (de.vsis.coordination.stubs.wsba.GetStatus.class.equals(type)) {
                return de.vsis.coordination.stubs.wsba.GetStatus.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (de.vsis.coordination.stubs.wsba.Status.class.equals(type)) {
                return de.vsis.coordination.stubs.wsba.Status.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
        } catch (java.lang.Exception e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
        return null;
    }
}
