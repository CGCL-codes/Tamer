package cn.myapps.webservice.client;

public class DepartmentServiceSoapBindingStub extends org.apache.axis.client.Stub implements cn.myapps.webservice.client.DepartmentService {

    private java.util.Vector<Object> cachedSerClasses = new java.util.Vector<Object>();

    private java.util.Vector<Object> cachedSerQNames = new java.util.Vector<Object>();

    private java.util.Vector<Object> cachedSerFactories = new java.util.Vector<Object>();

    private java.util.Vector<Object> cachedDeserFactories = new java.util.Vector<Object>();

    static org.apache.axis.description.OperationDesc[] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[5];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1() {
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("createDepartment");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dep"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:model.webservice.myapps.cn", "SimpleDepartment"), cn.myapps.webservice.model.SimpleDepartment.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:model.webservice.myapps.cn", "SimpleDepartment"));
        oper.setReturnClass(cn.myapps.webservice.model.SimpleDepartment.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "createDepartmentReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("http://client.webservice.myapps.cn", "fault"), "cn.myapps.webservice.fault.DepartmentServiceFault", new javax.xml.namespace.QName("urn:fault.webservice.myapps.cn", "DepartmentServiceFault"), true));
        _operations[0] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("updateDepartment");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dep"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:model.webservice.myapps.cn", "SimpleDepartment"), cn.myapps.webservice.model.SimpleDepartment.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("http://client.webservice.myapps.cn", "fault"), "cn.myapps.webservice.fault.DepartmentServiceFault", new javax.xml.namespace.QName("urn:fault.webservice.myapps.cn", "DepartmentServiceFault"), true));
        _operations[1] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getDepartment");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "pk"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:model.webservice.myapps.cn", "SimpleDepartment"));
        oper.setReturnClass(cn.myapps.webservice.model.SimpleDepartment.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getDepartmentReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("http://client.webservice.myapps.cn", "fault"), "cn.myapps.webservice.fault.DepartmentServiceFault", new javax.xml.namespace.QName("urn:fault.webservice.myapps.cn", "DepartmentServiceFault"), true));
        _operations[2] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("deleteDepartment");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "pk"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("http://client.webservice.myapps.cn", "fault"), "cn.myapps.webservice.fault.DepartmentServiceFault", new javax.xml.namespace.QName("urn:fault.webservice.myapps.cn", "DepartmentServiceFault"), true));
        _operations[3] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("upateSuperior");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dep"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:model.webservice.myapps.cn", "SimpleDepartment"), cn.myapps.webservice.model.SimpleDepartment.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "superDep"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:model.webservice.myapps.cn", "SimpleDepartment"), cn.myapps.webservice.model.SimpleDepartment.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("http://client.webservice.myapps.cn", "fault"), "cn.myapps.webservice.fault.DepartmentServiceFault", new javax.xml.namespace.QName("urn:fault.webservice.myapps.cn", "DepartmentServiceFault"), true));
        _operations[4] = oper;
    }

    public DepartmentServiceSoapBindingStub() throws org.apache.axis.AxisFault {
        this(null);
    }

    public DepartmentServiceSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public DepartmentServiceSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service) super.service).setTypeMappingVersion("1.2");
        javax.xml.namespace.QName qName;
        java.lang.Class<?> cls;
        java.lang.Class<?> beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
        java.lang.Class<?> beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
        qName = new javax.xml.namespace.QName("urn:fault.webservice.myapps.cn", "DepartmentServiceFault");
        cachedSerQNames.add(qName);
        cls = cn.myapps.webservice.fault.DepartmentServiceFault.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:model.webservice.myapps.cn", "SimpleDepartment");
        cachedSerQNames.add(qName);
        cls = cn.myapps.webservice.model.SimpleDepartment.class;
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
            java.util.Enumeration<Object> keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            synchronized (this) {
                if (firstCall()) {
                    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
                    _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class<?> cls = (java.lang.Class<?>) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName = (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class<?>) {
                            java.lang.Class<?> sf = (java.lang.Class<?>) cachedSerFactories.get(i);
                            java.lang.Class<?> df = (java.lang.Class<?>) cachedDeserFactories.get(i);
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

    public cn.myapps.webservice.model.SimpleDepartment createDepartment(cn.myapps.webservice.model.SimpleDepartment dep) throws java.rmi.RemoteException, cn.myapps.webservice.fault.DepartmentServiceFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://client.webservice.myapps.cn", "createDepartment"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { dep });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (cn.myapps.webservice.model.SimpleDepartment) _resp;
                } catch (java.lang.Exception _exception) {
                    return (cn.myapps.webservice.model.SimpleDepartment) org.apache.axis.utils.JavaUtils.convert(_resp, cn.myapps.webservice.model.SimpleDepartment.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof cn.myapps.webservice.fault.DepartmentServiceFault) {
                    throw (cn.myapps.webservice.fault.DepartmentServiceFault) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public void updateDepartment(cn.myapps.webservice.model.SimpleDepartment dep) throws java.rmi.RemoteException, cn.myapps.webservice.fault.DepartmentServiceFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://client.webservice.myapps.cn", "updateDepartment"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { dep });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            }
            extractAttachments(_call);
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof cn.myapps.webservice.fault.DepartmentServiceFault) {
                    throw (cn.myapps.webservice.fault.DepartmentServiceFault) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public cn.myapps.webservice.model.SimpleDepartment getDepartment(java.lang.String pk) throws java.rmi.RemoteException, cn.myapps.webservice.fault.DepartmentServiceFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://client.webservice.myapps.cn", "getDepartment"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { pk });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (cn.myapps.webservice.model.SimpleDepartment) _resp;
                } catch (java.lang.Exception _exception) {
                    return (cn.myapps.webservice.model.SimpleDepartment) org.apache.axis.utils.JavaUtils.convert(_resp, cn.myapps.webservice.model.SimpleDepartment.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof cn.myapps.webservice.fault.DepartmentServiceFault) {
                    throw (cn.myapps.webservice.fault.DepartmentServiceFault) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public void deleteDepartment(java.lang.String pk) throws java.rmi.RemoteException, cn.myapps.webservice.fault.DepartmentServiceFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://client.webservice.myapps.cn", "deleteDepartment"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { pk });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            }
            extractAttachments(_call);
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof cn.myapps.webservice.fault.DepartmentServiceFault) {
                    throw (cn.myapps.webservice.fault.DepartmentServiceFault) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public void upateSuperior(cn.myapps.webservice.model.SimpleDepartment dep, cn.myapps.webservice.model.SimpleDepartment superDep) throws java.rmi.RemoteException, cn.myapps.webservice.fault.DepartmentServiceFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://client.webservice.myapps.cn", "upateSuperior"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { dep, superDep });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            }
            extractAttachments(_call);
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof cn.myapps.webservice.fault.DepartmentServiceFault) {
                    throw (cn.myapps.webservice.fault.DepartmentServiceFault) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }
}
