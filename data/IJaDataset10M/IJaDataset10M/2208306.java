package net.sf.provisioner.IntrawayWSDL_pkg;

public class IntrawayWSDLBindingStub extends org.apache.axis.client.Stub implements net.sf.provisioner.IntrawayWSDL_pkg.IntrawayWSDLPortType {

    private java.util.Vector cachedSerClasses = new java.util.Vector();

    private java.util.Vector cachedSerQNames = new java.util.Vector();

    private java.util.Vector cachedSerFactories = new java.util.Vector();

    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc[] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[4];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1() {
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("Put");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "authKey"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ArrayOfInterfaceObjInput"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:IntrawayWSDL", "ArrayOfInterfaceObjInput"), net.sf.provisioner.IntrawayWSDL_pkg.InterfaceObjInput[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:IntrawayWSDL", "ArrayOfInterfaceObjOutput"));
        oper.setReturnClass(net.sf.provisioner.IntrawayWSDL_pkg.InterfaceObjOutput[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "ArrayOfInterfaceObjOutput"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[0] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetActivity");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "authKey"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "lastIdEntradaCaller"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idInterface"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idError"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "errorStr"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ArrayOfActivityObjOutput"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:IntrawayWSDL", "ArrayOfActivityObjOutput"), net.sf.provisioner.IntrawayWSDL_pkg.ActivityObjOutput[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[1] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetDocsisStatus");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "authKey"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idEmpresaCRM"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idServicio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idVenta"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idProducto"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "xmlEncoding"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idError"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "errorStr"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "DocsisStatusObjOutput"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:IntrawayWSDL", "DocsisStatusObjOutput"), net.sf.provisioner.IntrawayWSDL_pkg.DocsisStatusObjOutput.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[2] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("Maintenance");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "authKey"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ArrayOfMaintenanceObjInput"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:IntrawayWSDL", "ArrayOfMaintenanceObjInput"), net.sf.provisioner.IntrawayWSDL_pkg.MaintenanceObjInput[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:IntrawayWSDL", "ArrayOfMaintenanceObjOutput"));
        oper.setReturnClass(net.sf.provisioner.IntrawayWSDL_pkg.MaintenanceObjOutput[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "ArrayOfMaintenanceObjOutput"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[3] = oper;
    }

    public IntrawayWSDLBindingStub() throws org.apache.axis.AxisFault {
        this(null);
    }

    public IntrawayWSDLBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public IntrawayWSDLBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service) super.service).setTypeMappingVersion("1.2");
        java.lang.Class cls;
        javax.xml.namespace.QName qName;
        javax.xml.namespace.QName qName2;
        java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
        java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
        java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
        java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
        java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
        java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
        java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
        java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
        java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
        java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "ActivityObjOutput");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.ActivityObjOutput.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "ArrayOfActivityObjOutput");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.ActivityObjOutput[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "ActivityObjOutput");
        qName2 = null;
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "ArrayOfComandosObj");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.ComandosObj[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "comandosObj");
        qName2 = null;
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "ArrayOfFlowsObj");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.FlowsObj[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "flowsObj");
        qName2 = null;
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "ArrayOfInterfaceObjInput");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.InterfaceObjInput[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "InterfaceObjInput");
        qName2 = null;
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "ArrayOfInterfaceObjOutput");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.InterfaceObjOutput[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "InterfaceObjOutput");
        qName2 = null;
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "ArrayOfLeasesObj");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.LeasesObj[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "leasesObj");
        qName2 = null;
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "ArrayOfMaintenanceObjInput");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.MaintenanceObjInput[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "MaintenanceObjInput");
        qName2 = null;
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "ArrayOfMaintenanceObjOutput");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.MaintenanceObjOutput[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "MaintenanceObjOutput");
        qName2 = null;
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "ArrayOfMensajesObj");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.MensajesObj[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "mensajesObj");
        qName2 = null;
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "ArrayOfOpcionesObj");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.OpcionesObj[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "opcionesObj");
        qName2 = null;
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "ArrayOfPoolingObj");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.PoolingObj[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "poolingObj");
        qName2 = null;
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "comandosObj");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.ComandosObj.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "DocsisStatusObjOutput");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.DocsisStatusObjOutput.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "flowsObj");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.FlowsObj.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "InterfaceObjInput");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.InterfaceObjInput.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "InterfaceObjOutput");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.InterfaceObjOutput.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "leasesObj");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.LeasesObj.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "MaintenanceObjInput");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.MaintenanceObjInput.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "MaintenanceObjOutput");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.MaintenanceObjOutput.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "mensajesObj");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.MensajesObj.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "opcionesObj");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.OpcionesObj.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "poolingObj");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.PoolingObj.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:IntrawayWSDL", "spDescriptionObj");
        cachedSerQNames.add(qName);
        cls = net.sf.provisioner.IntrawayWSDL_pkg.SpDescriptionObj.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            synchronized (this) {
                if (firstCall()) {
                    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
                    _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName = (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class) cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class) cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        } else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory) cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory) cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        } catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public net.sf.provisioner.IntrawayWSDL_pkg.InterfaceObjOutput[] put(java.lang.String authKey, net.sf.provisioner.IntrawayWSDL_pkg.InterfaceObjInput[] arrayOfInterfaceObjInput) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:IntrawayWSDL#Put");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:IntrawayWSDL", "Put"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { authKey, arrayOfInterfaceObjInput });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (net.sf.provisioner.IntrawayWSDL_pkg.InterfaceObjOutput[]) _resp;
                } catch (java.lang.Exception _exception) {
                    return (net.sf.provisioner.IntrawayWSDL_pkg.InterfaceObjOutput[]) org.apache.axis.utils.JavaUtils.convert(_resp, net.sf.provisioner.IntrawayWSDL_pkg.InterfaceObjOutput[].class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public void getActivity(java.lang.String authKey, java.lang.String lastIdEntradaCaller, java.lang.String idInterface, javax.xml.rpc.holders.StringHolder idError, javax.xml.rpc.holders.StringHolder errorStr, net.sf.provisioner.IntrawayWSDL_pkg.holders.ArrayOfActivityObjOutputHolder arrayOfActivityObjOutput) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:IntrawayWSDL#GetActivity");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:IntrawayWSDL", "GetActivity"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { authKey, lastIdEntradaCaller, idInterface });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                java.util.Map _output;
                _output = _call.getOutputParams();
                try {
                    idError.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("", "idError"));
                } catch (java.lang.Exception _exception) {
                    idError.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "idError")), java.lang.String.class);
                }
                try {
                    errorStr.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("", "errorStr"));
                } catch (java.lang.Exception _exception) {
                    errorStr.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "errorStr")), java.lang.String.class);
                }
                try {
                    arrayOfActivityObjOutput.value = (net.sf.provisioner.IntrawayWSDL_pkg.ActivityObjOutput[]) _output.get(new javax.xml.namespace.QName("", "ArrayOfActivityObjOutput"));
                } catch (java.lang.Exception _exception) {
                    arrayOfActivityObjOutput.value = (net.sf.provisioner.IntrawayWSDL_pkg.ActivityObjOutput[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "ArrayOfActivityObjOutput")), net.sf.provisioner.IntrawayWSDL_pkg.ActivityObjOutput[].class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public void getDocsisStatus(java.lang.String authKey, java.lang.String idEmpresaCRM, java.lang.String idServicio, java.lang.String idVenta, java.lang.String idProducto, java.lang.String xmlEncoding, javax.xml.rpc.holders.StringHolder idError, javax.xml.rpc.holders.StringHolder errorStr, net.sf.provisioner.IntrawayWSDL_pkg.holders.DocsisStatusObjOutputHolder docsisStatusObjOutput) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:IntrawayWSDL#GetDocsisStatus");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:IntrawayWSDL", "GetDocsisStatus"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { authKey, idEmpresaCRM, idServicio, idVenta, idProducto, xmlEncoding });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                java.util.Map _output;
                _output = _call.getOutputParams();
                try {
                    idError.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("", "idError"));
                } catch (java.lang.Exception _exception) {
                    idError.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "idError")), java.lang.String.class);
                }
                try {
                    errorStr.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("", "errorStr"));
                } catch (java.lang.Exception _exception) {
                    errorStr.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "errorStr")), java.lang.String.class);
                }
                try {
                    docsisStatusObjOutput.value = (net.sf.provisioner.IntrawayWSDL_pkg.DocsisStatusObjOutput) _output.get(new javax.xml.namespace.QName("", "DocsisStatusObjOutput"));
                } catch (java.lang.Exception _exception) {
                    docsisStatusObjOutput.value = (net.sf.provisioner.IntrawayWSDL_pkg.DocsisStatusObjOutput) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "DocsisStatusObjOutput")), net.sf.provisioner.IntrawayWSDL_pkg.DocsisStatusObjOutput.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public net.sf.provisioner.IntrawayWSDL_pkg.MaintenanceObjOutput[] maintenance(java.lang.String authKey, net.sf.provisioner.IntrawayWSDL_pkg.MaintenanceObjInput[] arrayOfMaintenanceObjInput) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:IntrawayWSDL#Maintenance");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:IntrawayWSDL", "Maintenance"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { authKey, arrayOfMaintenanceObjInput });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (net.sf.provisioner.IntrawayWSDL_pkg.MaintenanceObjOutput[]) _resp;
                } catch (java.lang.Exception _exception) {
                    return (net.sf.provisioner.IntrawayWSDL_pkg.MaintenanceObjOutput[]) org.apache.axis.utils.JavaUtils.convert(_resp, net.sf.provisioner.IntrawayWSDL_pkg.MaintenanceObjOutput[].class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }
}
