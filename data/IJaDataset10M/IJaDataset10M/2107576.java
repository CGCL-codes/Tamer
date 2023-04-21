package com.google.api.adwords.v201109.cm;

public class UserListServiceSoapBindingStub extends org.apache.axis.client.Stub implements com.google.api.adwords.v201109.cm.UserListServiceInterface {

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
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "serviceSelector"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "Selector"), com.google.api.adwords.v201109.cm.Selector.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "UserListPage"));
        oper.setReturnClass(com.google.api.adwords.v201109.cm.UserListPage.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "rval"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "ApiExceptionFault"), "com.google.api.adwords.v201109.cm.ApiException", new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "ApiException"), true));
        _operations[0] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("mutate");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "operations"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "UserListOperation"), com.google.api.adwords.v201109.cm.UserListOperation[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "UserListReturnValue"));
        oper.setReturnClass(com.google.api.adwords.v201109.cm.UserListReturnValue.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "rval"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "ApiExceptionFault"), "com.google.api.adwords.v201109.cm.ApiException", new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "ApiException"), true));
        _operations[1] = oper;
    }

    public UserListServiceSoapBindingStub() throws org.apache.axis.AxisFault {
        this(null);
    }

    public UserListServiceSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public UserListServiceSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "AccessReason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.AccessReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "AccountUserListStatus");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.AccountUserListStatus.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "ApiError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.ApiError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "ApiException");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.ApiException.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "ApplicationException");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.ApplicationException.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "AuthenticationError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.AuthenticationError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "AuthenticationError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.AuthenticationErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "AuthorizationError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.AuthorizationError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "AuthorizationError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.AuthorizationErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "ClientTermsError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.ClientTermsError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "ClientTermsError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.ClientTermsErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "DatabaseError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.DatabaseError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "DatabaseError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.DatabaseErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "DateRange");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.DateRange.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "DistinctError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.DistinctError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "DistinctError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.DistinctErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "ExternalRemarketingUserList");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.ExternalRemarketingUserList.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "InternalApiError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.InternalApiError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "InternalApiError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.InternalApiErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "ListReturnValue");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.ListReturnValue.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "LogicalUserList");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.LogicalUserList.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "LogicalUserListOperand");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.LogicalUserListOperand.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "NotEmptyError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.NotEmptyError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "NotEmptyError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.NotEmptyErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "NotWhitelistedError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.NotWhitelistedError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "NotWhitelistedError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.NotWhitelistedErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "Operation");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.Operation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "OperationAccessDenied");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.OperationAccessDenied.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "OperationAccessDenied.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.OperationAccessDeniedReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "Operator");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.Operator.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "OrderBy");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.OrderBy.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "Page");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.Page.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "Paging");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.Paging.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "Predicate");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.Predicate.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "Predicate.Operator");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.PredicateOperator.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "QuotaCheckError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.QuotaCheckError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "QuotaCheckError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.QuotaCheckErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "RangeError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.RangeError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "RangeError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.RangeErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "RateExceededError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.RateExceededError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "RateExceededError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.RateExceededErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "ReadOnlyError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.ReadOnlyError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "ReadOnlyError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.ReadOnlyErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "RemarketingUserList");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.RemarketingUserList.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "RequestError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.RequestError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "RequestError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.RequestErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "RequiredError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.RequiredError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "RequiredError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.RequiredErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "Selector");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.Selector.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "SelectorError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.SelectorError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "SelectorError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.SelectorErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "SizeLimitError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.SizeLimitError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "SizeLimitError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.SizeLimitErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "SizeRange");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.SizeRange.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "SoapHeader");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.SoapHeader.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "SoapResponseHeader");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.SoapResponseHeader.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "SortOrder");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.SortOrder.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "UserInterest");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.UserInterest.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "UserList");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.UserList.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "UserList.Type");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.UserListType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "UserListConversionType");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.UserListConversionType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "UserListConversionType.Category");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.UserListConversionTypeCategory.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "UserListError");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.UserListError.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "UserListError.Reason");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.UserListErrorReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "UserListLogicalRule");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.UserListLogicalRule.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "UserListLogicalRule.Operator");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.UserListLogicalRuleOperator.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "UserListMembershipStatus");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.UserListMembershipStatus.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "UserListOperation");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.UserListOperation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "UserListPage");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.UserListPage.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "UserListReturnValue");
        cachedSerQNames.add(qName);
        cls = com.google.api.adwords.v201109.cm.UserListReturnValue.class;
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

    public com.google.api.adwords.v201109.cm.UserListPage get(com.google.api.adwords.v201109.cm.Selector serviceSelector) throws java.rmi.RemoteException, com.google.api.adwords.v201109.cm.ApiException {
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
        _call.setOperationName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "get"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { serviceSelector });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.google.api.adwords.v201109.cm.UserListPage) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.google.api.adwords.v201109.cm.UserListPage) org.apache.axis.utils.JavaUtils.convert(_resp, com.google.api.adwords.v201109.cm.UserListPage.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof com.google.api.adwords.v201109.cm.ApiException) {
                    throw (com.google.api.adwords.v201109.cm.ApiException) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public com.google.api.adwords.v201109.cm.UserListReturnValue mutate(com.google.api.adwords.v201109.cm.UserListOperation[] operations) throws java.rmi.RemoteException, com.google.api.adwords.v201109.cm.ApiException {
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
        _call.setOperationName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "mutate"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { operations });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.google.api.adwords.v201109.cm.UserListReturnValue) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.google.api.adwords.v201109.cm.UserListReturnValue) org.apache.axis.utils.JavaUtils.convert(_resp, com.google.api.adwords.v201109.cm.UserListReturnValue.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof com.google.api.adwords.v201109.cm.ApiException) {
                    throw (com.google.api.adwords.v201109.cm.ApiException) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }
}
