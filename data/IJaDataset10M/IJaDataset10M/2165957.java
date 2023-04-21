package com.sforce.soap._2006._04.metadata;

public class MetadataBindingStub extends org.apache.axis.client.Stub implements com.sforce.soap._2006._04.metadata.MetadataPortType {

    private java.util.Vector cachedSerClasses = new java.util.Vector();

    private java.util.Vector cachedSerQNames = new java.util.Vector();

    private java.util.Vector cachedSerFactories = new java.util.Vector();

    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc[] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[10];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1() {
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("checkDeployStatus");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "asyncProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ID"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "DeployResult"));
        oper.setReturnClass(com.sforce.soap._2006._04.metadata.DeployResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("checkRetrieveStatus");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "asyncProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ID"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "RetrieveResult"));
        oper.setReturnClass(com.sforce.soap._2006._04.metadata.RetrieveResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("checkStatus");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "asyncProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ID"), java.lang.String[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "AsyncResult"));
        oper.setReturnClass(com.sforce.soap._2006._04.metadata.AsyncResult[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("create");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "metadata"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Metadata"), com.sforce.soap._2006._04.metadata.Metadata[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "AsyncResult"));
        oper.setReturnClass(com.sforce.soap._2006._04.metadata.AsyncResult[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("delete");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "metadata"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Metadata"), com.sforce.soap._2006._04.metadata.Metadata[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "AsyncResult"));
        oper.setReturnClass(com.sforce.soap._2006._04.metadata.AsyncResult[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("deploy");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ZipFile"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "DeployOptions"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "DeployOptions"), com.sforce.soap._2006._04.metadata.DeployOptions.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "AsyncResult"));
        oper.setReturnClass(com.sforce.soap._2006._04.metadata.AsyncResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("describeMetadata");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "asOfVersion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "DescribeMetadataResult"));
        oper.setReturnClass(com.sforce.soap._2006._04.metadata.DescribeMetadataResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("listMetadata");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "queries"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ListMetadataQuery"), com.sforce.soap._2006._04.metadata.ListMetadataQuery[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "FileProperties"));
        oper.setReturnClass(com.sforce.soap._2006._04.metadata.FileProperties[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[7] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("retrieve");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "retrieveRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "RetrieveRequest"), com.sforce.soap._2006._04.metadata.RetrieveRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "AsyncResult"));
        oper.setReturnClass(com.sforce.soap._2006._04.metadata.AsyncResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[8] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("update");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "UpdateMetadata"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "UpdateMetadata"), com.sforce.soap._2006._04.metadata.UpdateMetadata[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "AsyncResult"));
        oper.setReturnClass(com.sforce.soap._2006._04.metadata.AsyncResult[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[9] = oper;
    }

    public MetadataBindingStub() throws org.apache.axis.AxisFault {
        this(null);
    }

    public MetadataBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public MetadataBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
        addBindings0();
        addBindings1();
        addBindings2();
    }

    private void addBindings0() {
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
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", ">CallOptions");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CallOptions.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", ">DebuggingHeader");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.DebuggingHeader.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", ">DebuggingInfo");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.DebuggingInfo.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", ">SessionHeader");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.SessionHeader.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ActionEmailRecipientTypes");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ActionEmailRecipientTypes.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ActionTaskAssignedToTypes");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ActionTaskAssignedToTypes.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ApexClass");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ApexClass.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ApexCodeUnitStatus");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ApexCodeUnitStatus.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ApexComponent");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ApexComponent.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ApexPage");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ApexPage.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ApexTrigger");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ApexTrigger.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "APIAccessLevel");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.APIAccessLevel.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Article");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.Article.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "AsyncRequestState");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.AsyncRequestState.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "AsyncResult");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.AsyncResult.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Attachment");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.Attachment.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CaseType");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CaseType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CdnStatus");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CdnStatus.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ChartBackgroundDirection");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ChartBackgroundDirection.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ChartLegendPosition");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ChartLegendPosition.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ChartPosition");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ChartPosition.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ChartRangeType");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ChartRangeType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ChartType");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ChartType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ChartUnits");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ChartUnits.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CodeCoverageResult");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CodeCoverageResult.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CodeCoverageWarning");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CodeCoverageWarning.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CodeLocation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CodeLocation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CurrencyIsoCode");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CurrencyIsoCode.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CustomApplication");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CustomApplication.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CustomApplicationTranslation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CustomApplicationTranslation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CustomDataType");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CustomDataType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CustomDataTypeComponent");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CustomDataTypeComponent.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CustomDataTypeComponentTranslation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CustomDataTypeComponentTranslation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CustomDataTypeTranslation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CustomDataTypeTranslation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CustomField");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CustomField.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CustomFieldDatatypes");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CustomFieldDatatypes.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CustomFieldTranslation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CustomFieldTranslation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CustomLabel");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CustomLabel.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CustomLabels");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CustomLabels.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CustomLabelTranslation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CustomLabelTranslation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CustomObject");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CustomObject.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CustomObjectTranslation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CustomObjectTranslation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CustomPageWebLink");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CustomPageWebLink.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CustomPageWebLinkTranslation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CustomPageWebLinkTranslation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CustomSettingsType");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CustomSettingsType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CustomSite");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CustomSite.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CustomTab");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CustomTab.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CustomTabTranslation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.CustomTabTranslation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Dashboard");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.Dashboard.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "DashboardComponent");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.DashboardComponent.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "DashboardComponentFilter");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.DashboardComponentFilter.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "DashboardComponentSection");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.DashboardComponentSection.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "DashboardComponentSize");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.DashboardComponentSize.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "DashboardComponentType");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.DashboardComponentType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "DashboardFolder");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.DashboardFolder.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "DeploymentStatus");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.DeploymentStatus.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "DeployMessage");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.DeployMessage.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "DeployOptions");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.DeployOptions.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "DeployResult");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.DeployResult.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "DescribeMetadataObject");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.DescribeMetadataObject.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "DescribeMetadataResult");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.DescribeMetadataResult.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Document");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.Document.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "DocumentFolder");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.DocumentFolder.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "EmailFolder");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.EmailFolder.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "EmailTemplate");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.EmailTemplate.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "EmailTemplateStyle");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.EmailTemplateStyle.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "EmailTemplateType");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.EmailTemplateType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Encoding");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.Encoding.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "EncryptedFieldMaskChar");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.EncryptedFieldMaskChar.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "EncryptedFieldMaskType");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.EncryptedFieldMaskType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "FieldType");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.FieldType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "FieldUpdateOperation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.FieldUpdateOperation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "FileProperties");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.FileProperties.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "FilterItem");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.FilterItem.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "FilterOperation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.FilterOperation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "FilterScope");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.FilterScope.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Folder");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.Folder.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "FolderAccessTypes");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.FolderAccessTypes.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Gender");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.Gender.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "HomePageComponent");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.HomePageComponent.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "HomePageLayout");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.HomePageLayout.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ID");
        cachedSerQNames.add(qName);
        cls = java.lang.String.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Language");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.Language.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Layout");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.Layout.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "LayoutColumn");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.LayoutColumn.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "LayoutHeader");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.LayoutHeader.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "LayoutItem");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.LayoutItem.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "LayoutSection");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.LayoutSection.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "LayoutSectionStyle");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.LayoutSectionStyle.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "LayoutSectionTranslation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.LayoutSectionTranslation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "LayoutTranslation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.LayoutTranslation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Letterhead");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.Letterhead.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "LetterheadHeaderFooter");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.LetterheadHeaderFooter.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "LetterheadHorizontalAlignment");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.LetterheadHorizontalAlignment.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "LetterheadLine");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.LetterheadLine.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "LetterheadVerticalAlignment");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.LetterheadVerticalAlignment.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ListMetadataQuery");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ListMetadataQuery.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ListView");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ListView.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ListViewFilter");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ListViewFilter.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "LogCategory");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.LogCategory.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
    }

    private void addBindings1() {
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
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "LogCategoryLevel");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.LogCategoryLevel.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "LogInfo");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.LogInfo.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "LogType");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.LogType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "LookupValueType");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.LookupValueType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ManageableState");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ManageableState.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Metadata");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.Metadata.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "MetadataWithContent");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.MetadataWithContent.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "MiniLayout");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.MiniLayout.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ObjectNameCaseValue");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ObjectNameCaseValue.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ObjectRelationship");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ObjectRelationship.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Package");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata._package.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "PackageTypeMembers");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.PackageTypeMembers.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "PageComponentType");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.PageComponentType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "PageComponentWidth");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.PageComponentWidth.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Picklist");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.Picklist.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "PicklistValue");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.PicklistValue.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "PicklistValueTranslation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.PicklistValueTranslation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Portal");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.Portal.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "PortalRoles");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.PortalRoles.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "PortalType");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.PortalType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Possessive");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.Possessive.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Profile");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.Profile.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ProfileApexClassAccess");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ProfileApexClassAccess.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ProfileApexPageAccess");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ProfileApexPageAccess.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ProfileApplicationVisibility");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ProfileApplicationVisibility.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ProfileFieldLevelSecurity");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ProfileFieldLevelSecurity.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ProfileLayoutAssignment");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ProfileLayoutAssignment.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ProfileObjectPermissions");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ProfileObjectPermissions.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ProfileRecordTypeVisibility");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ProfileRecordTypeVisibility.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ProfileTabVisibility");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ProfileTabVisibility.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "PublicFolderAccess");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.PublicFolderAccess.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "RecordType");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.RecordType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "RecordTypePicklistValue");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.RecordTypePicklistValue.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "RecordTypeTranslation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.RecordTypeTranslation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "RelatedListItem");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.RelatedListItem.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Report");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.Report.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ReportAggregate");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ReportAggregate.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ReportChart");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ReportChart.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ReportChartSize");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ReportChartSize.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ReportColorRange");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ReportColorRange.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ReportColumn");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ReportColumn.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ReportFilter");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ReportFilter.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ReportFilterItem");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ReportFilterItem.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ReportFolder");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ReportFolder.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ReportFormat");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ReportFormat.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ReportGrouping");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ReportGrouping.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ReportLayoutSection");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ReportLayoutSection.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ReportParam");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ReportParam.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ReportSummaryType");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ReportSummaryType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ReportTimeFrameFilter");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ReportTimeFrameFilter.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ReportType");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ReportType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ReportTypeCategory");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ReportTypeCategory.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ReportTypeColumn");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ReportTypeColumn.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ReportTypeColumnTranslation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ReportTypeColumnTranslation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ReportTypeSectionTranslation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ReportTypeSectionTranslation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ReportTypeTranslation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ReportTypeTranslation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "RetrieveMessage");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.RetrieveMessage.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "RetrieveRequest");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.RetrieveRequest.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "RetrieveResult");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.RetrieveResult.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "RunTestFailure");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.RunTestFailure.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "RunTestsResult");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.RunTestsResult.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "RunTestSuccess");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.RunTestSuccess.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Scontrol");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.Scontrol.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "SControlContentSource");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.SControlContentSource.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ScontrolTranslation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ScontrolTranslation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "SearchLayouts");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.SearchLayouts.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "SharingModel");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.SharingModel.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "SharingReason");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.SharingReason.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "SharingReasonTranslation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.SharingReasonTranslation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "SharingRecalculation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.SharingRecalculation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "SortOrder");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.SortOrder.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "StartsWith");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.StartsWith.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "StaticResource");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.StaticResource.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "StaticResourceCacheControl");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.StaticResourceCacheControl.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "StatusCode");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.StatusCode.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "SummaryOperations");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.SummaryOperations.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "TabVisibility");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.TabVisibility.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Translations");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.Translations.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "TreatBlanksAs");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.TreatBlanksAs.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "UiBehavior");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.UiBehavior.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "UpdateMetadata");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.UpdateMetadata.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "UserDateGranularity");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.UserDateGranularity.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "UserDateInterval");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.UserDateInterval.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ValidationRule");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ValidationRule.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "ValidationRuleTranslation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.ValidationRuleTranslation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "WebLink");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.WebLink.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "WebLinkAvailability");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.WebLinkAvailability.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "WebLinkDisplayType");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.WebLinkDisplayType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "WebLinkPosition");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.WebLinkPosition.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "WebLinkTranslation");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.WebLinkTranslation.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "WebLinkType");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.WebLinkType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "WebLinkWindowType");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.WebLinkWindowType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Workflow");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.Workflow.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "WorkflowAction");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.WorkflowAction.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "WorkflowActionReference");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.WorkflowActionReference.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "WorkflowActionType");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.WorkflowActionType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "WorkflowAlert");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.WorkflowAlert.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "WorkflowEmailRecipient");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.WorkflowEmailRecipient.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "WorkflowFieldUpdate");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.WorkflowFieldUpdate.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "WorkflowOutboundMessage");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.WorkflowOutboundMessage.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
    }

    private void addBindings2() {
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
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "WorkflowRule");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.WorkflowRule.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "WorkflowTask");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.WorkflowTask.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "WorkflowTriggerTypes");
        cachedSerQNames.add(qName);
        cls = com.sforce.soap._2006._04.metadata.WorkflowTriggerTypes.class;
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

    public com.sforce.soap._2006._04.metadata.DeployResult checkDeployStatus(java.lang.String asyncProcessId) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "checkDeployStatus"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { asyncProcessId });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.sforce.soap._2006._04.metadata.DeployResult) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.sforce.soap._2006._04.metadata.DeployResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.sforce.soap._2006._04.metadata.DeployResult.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.sforce.soap._2006._04.metadata.RetrieveResult checkRetrieveStatus(java.lang.String asyncProcessId) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "checkRetrieveStatus"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { asyncProcessId });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.sforce.soap._2006._04.metadata.RetrieveResult) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.sforce.soap._2006._04.metadata.RetrieveResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.sforce.soap._2006._04.metadata.RetrieveResult.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.sforce.soap._2006._04.metadata.AsyncResult[] checkStatus(java.lang.String[] asyncProcessId) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "checkStatus"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { asyncProcessId });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.sforce.soap._2006._04.metadata.AsyncResult[]) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.sforce.soap._2006._04.metadata.AsyncResult[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.sforce.soap._2006._04.metadata.AsyncResult[].class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.sforce.soap._2006._04.metadata.AsyncResult[] create(com.sforce.soap._2006._04.metadata.Metadata[] metadata) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "create"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { metadata });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.sforce.soap._2006._04.metadata.AsyncResult[]) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.sforce.soap._2006._04.metadata.AsyncResult[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.sforce.soap._2006._04.metadata.AsyncResult[].class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.sforce.soap._2006._04.metadata.AsyncResult[] delete(com.sforce.soap._2006._04.metadata.Metadata[] metadata) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "delete"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { metadata });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.sforce.soap._2006._04.metadata.AsyncResult[]) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.sforce.soap._2006._04.metadata.AsyncResult[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.sforce.soap._2006._04.metadata.AsyncResult[].class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.sforce.soap._2006._04.metadata.AsyncResult deploy(byte[] zipFile, com.sforce.soap._2006._04.metadata.DeployOptions deployOptions) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "deploy"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { zipFile, deployOptions });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.sforce.soap._2006._04.metadata.AsyncResult) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.sforce.soap._2006._04.metadata.AsyncResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.sforce.soap._2006._04.metadata.AsyncResult.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.sforce.soap._2006._04.metadata.DescribeMetadataResult describeMetadata(double asOfVersion) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "describeMetadata"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { new java.lang.Double(asOfVersion) });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.sforce.soap._2006._04.metadata.DescribeMetadataResult) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.sforce.soap._2006._04.metadata.DescribeMetadataResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.sforce.soap._2006._04.metadata.DescribeMetadataResult.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.sforce.soap._2006._04.metadata.FileProperties[] listMetadata(com.sforce.soap._2006._04.metadata.ListMetadataQuery[] queries) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "listMetadata"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { queries });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.sforce.soap._2006._04.metadata.FileProperties[]) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.sforce.soap._2006._04.metadata.FileProperties[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.sforce.soap._2006._04.metadata.FileProperties[].class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.sforce.soap._2006._04.metadata.AsyncResult retrieve(com.sforce.soap._2006._04.metadata.RetrieveRequest retrieveRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "retrieve"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { retrieveRequest });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.sforce.soap._2006._04.metadata.AsyncResult) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.sforce.soap._2006._04.metadata.AsyncResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.sforce.soap._2006._04.metadata.AsyncResult.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.sforce.soap._2006._04.metadata.AsyncResult[] update(com.sforce.soap._2006._04.metadata.UpdateMetadata[] updateMetadata) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "update"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { updateMetadata });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.sforce.soap._2006._04.metadata.AsyncResult[]) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.sforce.soap._2006._04.metadata.AsyncResult[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.sforce.soap._2006._04.metadata.AsyncResult[].class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }
}
