package com.google.api.adwords.v201003.cm;

public class GeoLocationServiceSoapBindingStub extends org.apache.axis.client.Stub implements com.google.api.adwords.v201003.cm.GeoLocationServiceInterface {

    private java.util.Vector cachedSerClasses = new java.util.Vector();

    private java.util.Vector cachedSerQNames = new java.util.Vector();

    private java.util.Vector cachedSerFactories = new java.util.Vector();

    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc[] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[1];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1() {
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("get");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "selector"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "GeoLocationSelector"), com.google.api.adwords.v201003.cm.GeoLocationSelector.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "GeoLocation"));
        oper.setReturnClass(com.google.api.adwords.v201003.cm.GeoLocation[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "rval"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "ApiExceptionFault"), "com.google.api.adwords.v201003.cm.ApiException", new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "ApiException"), true));
        _operations[0] = oper;
    }

    public GeoLocationServiceSoapBindingStub() throws org.apache.axis.AxisFault {
        this(null);
    }

    public GeoLocationServiceSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public GeoLocationServiceSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "Address");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.Address.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "ApiError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.ApiError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "ApiException");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.ApiException.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "ApplicationException");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.ApplicationException.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "AuthenticationError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.AuthenticationError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "AuthenticationError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.AuthenticationErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "AuthorizationError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.AuthorizationError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "AuthorizationError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.AuthorizationErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "ClientTermsError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.ClientTermsError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "ClientTermsError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.ClientTermsErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "GeoLocation");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.GeoLocation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "GeoLocationError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.GeoLocationError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "GeoLocationError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.GeoLocationErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "GeoLocationSelector");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.GeoLocationSelector.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "GeoPoint");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.GeoPoint.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "InternalApiError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.InternalApiError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "InternalApiError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.InternalApiErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "InvalidGeoLocation");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.InvalidGeoLocation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "NotEmptyError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.NotEmptyError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "NotEmptyError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.NotEmptyErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "NotWhitelistedError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.NotWhitelistedError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "NotWhitelistedError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.NotWhitelistedErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "NullError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.NullError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "NullError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.NullErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "QuotaCheckError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.QuotaCheckError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "QuotaCheckError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.QuotaCheckErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "RangeError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.RangeError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "RangeError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.RangeErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "RateExceededError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.RateExceededError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "RateExceededError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.RateExceededErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "RegionCodeError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.RegionCodeError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "RegionCodeError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.RegionCodeErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "RequestError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.RequestError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "RequestError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.RequestErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "RequiredError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.RequiredError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "RequiredError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.RequiredErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "SizeLimitError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.SizeLimitError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "SizeLimitError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.SizeLimitErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "SoapHeader");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.SoapHeader.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "SoapResponseHeader");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.SoapResponseHeader.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "StringLengthError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.StringLengthError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "StringLengthError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201003.cm.StringLengthErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
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
                    _call.setEncodingStyle(null);
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

    public com.google.api.adwords.v201003.cm.GeoLocation[] get(com.google.api.adwords.v201003.cm.GeoLocationSelector selector) throws java.rmi.RemoteException, com.google.api.adwords.v201003.cm.ApiException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "get"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { selector });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.google.api.adwords.v201003.cm.GeoLocation[]) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.google.api.adwords.v201003.cm.GeoLocation[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.google.api.adwords.v201003.cm.GeoLocation[].class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof com.google.api.adwords.v201003.cm.ApiException) {
                    throw (com.google.api.adwords.v201003.cm.ApiException) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }
}
