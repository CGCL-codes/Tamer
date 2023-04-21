package com.google.api.adwords.v200909.cm;

public class CampaignTargetServiceSoapBindingStub extends org.apache.axis.client.Stub implements com.google.api.adwords.v200909.cm.CampaignTargetServiceInterface {

    private java.util.Vector cachedSerClasses = new java.util.Vector();

    private java.util.Vector cachedSerQNames = new java.util.Vector();

    private java.util.Vector cachedSerFactories = new java.util.Vector();

    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc[] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[2];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1() {
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("get");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "selector"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "CampaignTargetSelector"), com.google.api.adwords.v200909.cm.CampaignTargetSelector.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "CampaignTargetPage"));
        oper.setReturnClass(com.google.api.adwords.v200909.cm.CampaignTargetPage.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "rval"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "ApiExceptionFault"), "com.google.api.adwords.v200909.cm.ApiException", new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "ApiException"), true));
        _operations[0] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("mutate");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "operations"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "CampaignTargetOperation"), com.google.api.adwords.v200909.cm.CampaignTargetOperation[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "CampaignTargetReturnValue"));
        oper.setReturnClass(com.google.api.adwords.v200909.cm.CampaignTargetReturnValue.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "rval"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "ApiExceptionFault"), "com.google.api.adwords.v200909.cm.ApiException", new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "ApiException"), true));
        _operations[1] = oper;
    }

    public CampaignTargetServiceSoapBindingStub() throws org.apache.axis.AxisFault {
        this(null);
    }

    public CampaignTargetServiceSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public CampaignTargetServiceSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "Address");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.Address.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "AdScheduleTarget");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.AdScheduleTarget.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "AdScheduleTargetList");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.AdScheduleTargetList.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "AgeTarget");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.AgeTarget.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "AgeTarget.Age");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.AgeTargetAge.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "ApiError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.ApiError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "ApiException");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.ApiException.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "ApplicationException");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.ApplicationException.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "AuthenticationError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.AuthenticationError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "AuthenticationError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.AuthenticationErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "AuthorizationError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.AuthorizationError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "AuthorizationError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.AuthorizationErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "BiddingError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.BiddingError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "BiddingError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.BiddingErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "CampaignError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.CampaignError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "CampaignError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.CampaignErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "CampaignTargetOperation");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.CampaignTargetOperation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "CampaignTargetPage");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.CampaignTargetPage.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "CampaignTargetReturnValue");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.CampaignTargetReturnValue.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "CampaignTargetSelector");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.CampaignTargetSelector.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "CityTarget");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.CityTarget.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "ClientTermsError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.ClientTermsError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "ClientTermsError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.ClientTermsErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "CountryTarget");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.CountryTarget.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "DatabaseError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.DatabaseError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "DatabaseError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.DatabaseErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "DayOfWeek");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.DayOfWeek.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "DemographicTarget");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.DemographicTarget.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "DemographicTargetList");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.DemographicTargetList.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "DistinctError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.DistinctError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "DistinctError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.DistinctErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "EntityNotFound");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.EntityNotFound.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "EntityNotFound.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.EntityNotFoundReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "GenderTarget");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.GenderTarget.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "GenderTarget.Gender");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.GenderTargetGender.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "GeoPoint");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.GeoPoint.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "GeoTarget");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.GeoTarget.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "GeoTargetList");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.GeoTargetList.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "InternalApiError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.InternalApiError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "InternalApiError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.InternalApiErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "LanguageTarget");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.LanguageTarget.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "LanguageTargetList");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.LanguageTargetList.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "ListReturnValue");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.ListReturnValue.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "MetroTarget");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.MetroTarget.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "MinuteOfHour");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.MinuteOfHour.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "NetworkCoverageType");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.NetworkCoverageType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "NetworkTarget");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.NetworkTarget.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "NetworkTargetList");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.NetworkTargetList.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "NotEmptyError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.NotEmptyError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "NotEmptyError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.NotEmptyErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "NotWhitelistedError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.NotWhitelistedError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "NotWhitelistedError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.NotWhitelistedErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "NullError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.NullError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "NullError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.NullErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "Operation");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.Operation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "OperationAccessDenied");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.OperationAccessDenied.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "OperationAccessDenied.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.OperationAccessDeniedReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "Operator");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.Operator.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "OperatorError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.OperatorError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "OperatorError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.OperatorErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "Page");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.Page.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "PlatformTarget");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.PlatformTarget.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "PlatformTargetList");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.PlatformTargetList.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "PlatformType");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.PlatformType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "PolygonTarget");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.PolygonTarget.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "ProvinceTarget");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.ProvinceTarget.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "ProximityTarget");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.ProximityTarget.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "ProximityTarget.DistanceUnits");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.ProximityTargetDistanceUnits.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "QuotaCheckError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.QuotaCheckError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "QuotaCheckError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.QuotaCheckErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "QuotaError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.QuotaError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "QuotaError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.QuotaErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "QuotaExceededError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.QuotaExceededError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "QuotaExceededError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.QuotaExceededErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "RangeError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.RangeError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "RangeError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.RangeErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "RegionCodeError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.RegionCodeError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "RegionCodeError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.RegionCodeErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "RequiredError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.RequiredError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "RequiredError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.RequiredErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "SizeLimitError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.SizeLimitError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "SizeLimitError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.SizeLimitErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "SoapHeader");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.SoapHeader.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "SoapResponseHeader");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.SoapResponseHeader.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "StringLengthError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.StringLengthError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "StringLengthError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.StringLengthErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "Target");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.Target.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "TargetError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.TargetError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "TargetError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.TargetErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "TargetList");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v200909.cm.TargetList.class;
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

    public com.google.api.adwords.v200909.cm.CampaignTargetPage get(com.google.api.adwords.v200909.cm.CampaignTargetSelector selector) throws java.rmi.RemoteException, com.google.api.adwords.v200909.cm.ApiException {
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
        _call.setOperationName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "get"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { selector });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.google.api.adwords.v200909.cm.CampaignTargetPage) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.google.api.adwords.v200909.cm.CampaignTargetPage) org.apache.axis.utils.JavaUtils.convert(_resp, com.google.api.adwords.v200909.cm.CampaignTargetPage.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof com.google.api.adwords.v200909.cm.ApiException) {
                    throw (com.google.api.adwords.v200909.cm.ApiException) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public com.google.api.adwords.v200909.cm.CampaignTargetReturnValue mutate(com.google.api.adwords.v200909.cm.CampaignTargetOperation[] operations) throws java.rmi.RemoteException, com.google.api.adwords.v200909.cm.ApiException {
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
        _call.setOperationName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "mutate"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { operations });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.google.api.adwords.v200909.cm.CampaignTargetReturnValue) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.google.api.adwords.v200909.cm.CampaignTargetReturnValue) org.apache.axis.utils.JavaUtils.convert(_resp, com.google.api.adwords.v200909.cm.CampaignTargetReturnValue.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof com.google.api.adwords.v200909.cm.ApiException) {
                    throw (com.google.api.adwords.v200909.cm.ApiException) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }
}
