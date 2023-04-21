package com.google.api.ads.dfp.v201203;

public class LabelServiceSoapBindingStub extends org.apache.axis.client.Stub implements com.google.api.ads.dfp.v201203.LabelServiceInterface {

    private java.util.Vector cachedSerClasses = new java.util.Vector();

    private java.util.Vector cachedSerQNames = new java.util.Vector();

    private java.util.Vector cachedSerFactories = new java.util.Vector();

    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc[] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[7];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1() {
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("createLabel");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "label"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "Label"), com.google.api.ads.dfp.v201203.Label.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "Label"));
        oper.setReturnClass(com.google.api.ads.dfp.v201203.Label.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "rval"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ApiExceptionFault"), "com.google.api.ads.dfp.v201203.ApiException", new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ApiException"), true));
        _operations[0] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("createLabels");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "labels"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "Label"), com.google.api.ads.dfp.v201203.Label[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "Label"));
        oper.setReturnClass(com.google.api.ads.dfp.v201203.Label[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "rval"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ApiExceptionFault"), "com.google.api.ads.dfp.v201203.ApiException", new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ApiException"), true));
        _operations[1] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getLabel");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "labelId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), java.lang.Long.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "Label"));
        oper.setReturnClass(com.google.api.ads.dfp.v201203.Label.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "rval"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ApiExceptionFault"), "com.google.api.ads.dfp.v201203.ApiException", new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ApiException"), true));
        _operations[2] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getLabelsByStatement");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "filterStatement"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "Statement"), com.google.api.ads.dfp.v201203.Statement.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "LabelPage"));
        oper.setReturnClass(com.google.api.ads.dfp.v201203.LabelPage.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "rval"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ApiExceptionFault"), "com.google.api.ads.dfp.v201203.ApiException", new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ApiException"), true));
        _operations[3] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("performLabelAction");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "labelAction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "LabelAction"), com.google.api.ads.dfp.v201203.LabelAction.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "filterStatement"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "Statement"), com.google.api.ads.dfp.v201203.Statement.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "UpdateResult"));
        oper.setReturnClass(com.google.api.ads.dfp.v201203.UpdateResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "rval"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ApiExceptionFault"), "com.google.api.ads.dfp.v201203.ApiException", new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ApiException"), true));
        _operations[4] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("updateLabel");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "label"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "Label"), com.google.api.ads.dfp.v201203.Label.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "Label"));
        oper.setReturnClass(com.google.api.ads.dfp.v201203.Label.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "rval"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ApiExceptionFault"), "com.google.api.ads.dfp.v201203.ApiException", new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ApiException"), true));
        _operations[5] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("updateLabels");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "labels"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "Label"), com.google.api.ads.dfp.v201203.Label[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "Label"));
        oper.setReturnClass(com.google.api.ads.dfp.v201203.Label[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "rval"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ApiExceptionFault"), "com.google.api.ads.dfp.v201203.ApiException", new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ApiException"), true));
        _operations[6] = oper;
    }

    public LabelServiceSoapBindingStub() throws org.apache.axis.AxisFault {
        this(null);
    }

    public LabelServiceSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public LabelServiceSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ActivateLabels");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.ActivateLabels.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ApiError");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.ApiError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ApiException");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.ApiException.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ApiVersionError");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.ApiVersionError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ApiVersionError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.ApiVersionErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ApplicationException");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.ApplicationException.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "Authentication");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.Authentication.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "AuthenticationError");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.AuthenticationError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "AuthenticationError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.AuthenticationErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "BooleanValue");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.BooleanValue.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ClientLogin");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.ClientLogin.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "CommonError");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.CommonError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "CommonError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.CommonErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "Date");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.Date.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "DateTime");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.DateTime.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "DateTimeValue");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.DateTimeValue.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "DeactivateLabels");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.DeactivateLabels.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "InternalApiError");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.InternalApiError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "InternalApiError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.InternalApiErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "Label");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.Label.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "LabelAction");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.LabelAction.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "LabelError");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.LabelError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "LabelError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.LabelErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "LabelPage");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.LabelPage.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "LabelType");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.LabelType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "NotNullError");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.NotNullError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "NotNullError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.NotNullErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "NullError");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.NullError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "NullError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.NullErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "NumberValue");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.NumberValue.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "OAuth");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.OAuth.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ParseError");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.ParseError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ParseError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.ParseErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "PermissionError");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.PermissionError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "PermissionError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.PermissionErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "PublisherQueryLanguageContextError");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.PublisherQueryLanguageContextError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "PublisherQueryLanguageContextError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.PublisherQueryLanguageContextErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "PublisherQueryLanguageSyntaxError");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.PublisherQueryLanguageSyntaxError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "PublisherQueryLanguageSyntaxError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.PublisherQueryLanguageSyntaxErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "QuotaError");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.QuotaError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "QuotaError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.QuotaErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "RequiredError");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.RequiredError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "RequiredError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.RequiredErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ServerError");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.ServerError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "ServerError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.ServerErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "SoapRequestHeader");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.SoapRequestHeader.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "SoapResponseHeader");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.SoapResponseHeader.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "Statement");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.Statement.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "StatementError");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.StatementError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "StatementError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.StatementErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "String_ValueMapEntry");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.String_ValueMapEntry.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "StringLengthError");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.StringLengthError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "StringLengthError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.StringLengthErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "TextValue");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.TextValue.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "TypeError");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.TypeError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "UniqueError");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.UniqueError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "UpdateResult");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.UpdateResult.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "Value");
        cachedSerQNames.add(qName);
        cls = com.google.api.ads.dfp.v201203.Value.class;
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

    public com.google.api.ads.dfp.v201203.Label createLabel(com.google.api.ads.dfp.v201203.Label label) throws java.rmi.RemoteException, com.google.api.ads.dfp.v201203.ApiException {
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
        _call.setOperationName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "createLabel"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { label });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.google.api.ads.dfp.v201203.Label) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.google.api.ads.dfp.v201203.Label) org.apache.axis.utils.JavaUtils.convert(_resp, com.google.api.ads.dfp.v201203.Label.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof com.google.api.ads.dfp.v201203.ApiException) {
                    throw (com.google.api.ads.dfp.v201203.ApiException) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public com.google.api.ads.dfp.v201203.Label[] createLabels(com.google.api.ads.dfp.v201203.Label[] labels) throws java.rmi.RemoteException, com.google.api.ads.dfp.v201203.ApiException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "createLabels"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { labels });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.google.api.ads.dfp.v201203.Label[]) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.google.api.ads.dfp.v201203.Label[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.google.api.ads.dfp.v201203.Label[].class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof com.google.api.ads.dfp.v201203.ApiException) {
                    throw (com.google.api.ads.dfp.v201203.ApiException) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public com.google.api.ads.dfp.v201203.Label getLabel(java.lang.Long labelId) throws java.rmi.RemoteException, com.google.api.ads.dfp.v201203.ApiException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "getLabel"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { labelId });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.google.api.ads.dfp.v201203.Label) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.google.api.ads.dfp.v201203.Label) org.apache.axis.utils.JavaUtils.convert(_resp, com.google.api.ads.dfp.v201203.Label.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof com.google.api.ads.dfp.v201203.ApiException) {
                    throw (com.google.api.ads.dfp.v201203.ApiException) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public com.google.api.ads.dfp.v201203.LabelPage getLabelsByStatement(com.google.api.ads.dfp.v201203.Statement filterStatement) throws java.rmi.RemoteException, com.google.api.ads.dfp.v201203.ApiException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "getLabelsByStatement"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { filterStatement });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.google.api.ads.dfp.v201203.LabelPage) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.google.api.ads.dfp.v201203.LabelPage) org.apache.axis.utils.JavaUtils.convert(_resp, com.google.api.ads.dfp.v201203.LabelPage.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof com.google.api.ads.dfp.v201203.ApiException) {
                    throw (com.google.api.ads.dfp.v201203.ApiException) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public com.google.api.ads.dfp.v201203.UpdateResult performLabelAction(com.google.api.ads.dfp.v201203.LabelAction labelAction, com.google.api.ads.dfp.v201203.Statement filterStatement) throws java.rmi.RemoteException, com.google.api.ads.dfp.v201203.ApiException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "performLabelAction"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { labelAction, filterStatement });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.google.api.ads.dfp.v201203.UpdateResult) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.google.api.ads.dfp.v201203.UpdateResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.google.api.ads.dfp.v201203.UpdateResult.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof com.google.api.ads.dfp.v201203.ApiException) {
                    throw (com.google.api.ads.dfp.v201203.ApiException) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public com.google.api.ads.dfp.v201203.Label updateLabel(com.google.api.ads.dfp.v201203.Label label) throws java.rmi.RemoteException, com.google.api.ads.dfp.v201203.ApiException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "updateLabel"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { label });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.google.api.ads.dfp.v201203.Label) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.google.api.ads.dfp.v201203.Label) org.apache.axis.utils.JavaUtils.convert(_resp, com.google.api.ads.dfp.v201203.Label.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof com.google.api.ads.dfp.v201203.ApiException) {
                    throw (com.google.api.ads.dfp.v201203.ApiException) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public com.google.api.ads.dfp.v201203.Label[] updateLabels(com.google.api.ads.dfp.v201203.Label[] labels) throws java.rmi.RemoteException, com.google.api.ads.dfp.v201203.ApiException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "updateLabels"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { labels });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.google.api.ads.dfp.v201203.Label[]) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.google.api.ads.dfp.v201203.Label[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.google.api.ads.dfp.v201203.Label[].class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof com.google.api.ads.dfp.v201203.ApiException) {
                    throw (com.google.api.ads.dfp.v201203.ApiException) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }
}
