package api.server.jUDDI.api_v2_uddi;

public class PublishSoapStub extends org.apache.axis.client.Stub implements api.server.jUDDI.api_v2_uddi.Publish {

    private java.util.Vector cachedSerClasses = new java.util.Vector();

    private java.util.Vector cachedSerQNames = new java.util.Vector();

    private java.util.Vector cachedSerFactories = new java.util.Vector();

    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc[] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[16];
        _initOperationDesc1();
        _initOperationDesc2();
    }

    private static void _initOperationDesc1() {
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("add_publisherAssertions");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "add_publisherAssertions"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uddi-org:api_v2", "add_publisherAssertions"), api.server.jUDDI.api_v2_uddi.Add_publisherAssertions.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"));
        oper.setReturnClass(api.server.jUDDI.api_v2_uddi.DispositionReport.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), "api.server.jUDDI.api_v2_uddi.DispositionReport", new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), true));
        _operations[0] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("delete_binding");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "delete_binding"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uddi-org:api_v2", "delete_binding"), api.server.jUDDI.api_v2_uddi.Delete_binding.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"));
        oper.setReturnClass(api.server.jUDDI.api_v2_uddi.DispositionReport.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), "api.server.jUDDI.api_v2_uddi.DispositionReport", new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), true));
        _operations[1] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("delete_business");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "delete_business"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uddi-org:api_v2", "delete_business"), api.server.jUDDI.api_v2_uddi.Delete_business.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"));
        oper.setReturnClass(api.server.jUDDI.api_v2_uddi.DispositionReport.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), "api.server.jUDDI.api_v2_uddi.DispositionReport", new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), true));
        _operations[2] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("delete_publisherAssertions");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "delete_publisherAssertions"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uddi-org:api_v2", "delete_publisherAssertions"), api.server.jUDDI.api_v2_uddi.Delete_publisherAssertions.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"));
        oper.setReturnClass(api.server.jUDDI.api_v2_uddi.DispositionReport.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), "api.server.jUDDI.api_v2_uddi.DispositionReport", new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), true));
        _operations[3] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("delete_service");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "delete_service"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uddi-org:api_v2", "delete_service"), api.server.jUDDI.api_v2_uddi.Delete_service.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"));
        oper.setReturnClass(api.server.jUDDI.api_v2_uddi.DispositionReport.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), "api.server.jUDDI.api_v2_uddi.DispositionReport", new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), true));
        _operations[4] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("delete_tModel");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "delete_tModel"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uddi-org:api_v2", "delete_tModel"), api.server.jUDDI.api_v2_uddi.Delete_tModel.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"));
        oper.setReturnClass(api.server.jUDDI.api_v2_uddi.DispositionReport.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), "api.server.jUDDI.api_v2_uddi.DispositionReport", new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), true));
        _operations[5] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("discard_authToken");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "discard_authToken"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uddi-org:api_v2", "discard_authToken"), api.server.jUDDI.api_v2_uddi.Discard_authToken.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"));
        oper.setReturnClass(api.server.jUDDI.api_v2_uddi.DispositionReport.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), "api.server.jUDDI.api_v2_uddi.DispositionReport", new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), true));
        _operations[6] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("get_assertionStatusReport");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "get_assertionStatusReport"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uddi-org:api_v2", "get_assertionStatusReport"), api.server.jUDDI.api_v2_uddi.Get_assertionStatusReport.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "assertionStatusReport"));
        oper.setReturnClass(api.server.jUDDI.api_v2_uddi.AssertionStatusReport.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "assertionStatusReport"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), "api.server.jUDDI.api_v2_uddi.DispositionReport", new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), true));
        _operations[7] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("get_authToken");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "get_authToken"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uddi-org:api_v2", "get_authToken"), api.server.jUDDI.api_v2_uddi.Get_authToken.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "authToken"));
        oper.setReturnClass(api.server.jUDDI.api_v2_uddi.AuthToken.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "authToken"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), "api.server.jUDDI.api_v2_uddi.DispositionReport", new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), true));
        _operations[8] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("get_publisherAssertions");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "get_publisherAssertions"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uddi-org:api_v2", "get_publisherAssertions"), api.server.jUDDI.api_v2_uddi.Get_publisherAssertions.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "publisherAssertions"));
        oper.setReturnClass(api.server.jUDDI.api_v2_uddi.PublisherAssertions.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "publisherAssertions"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), "api.server.jUDDI.api_v2_uddi.DispositionReport", new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), true));
        _operations[9] = oper;
    }

    private static void _initOperationDesc2() {
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("get_registeredInfo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "get_registeredInfo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uddi-org:api_v2", "get_registeredInfo"), api.server.jUDDI.api_v2_uddi.Get_registeredInfo.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "registeredInfo"));
        oper.setReturnClass(api.server.jUDDI.api_v2_uddi.RegisteredInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "registeredInfo"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), "api.server.jUDDI.api_v2_uddi.DispositionReport", new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), true));
        _operations[10] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("save_binding");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "save_binding"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uddi-org:api_v2", "save_binding"), api.server.jUDDI.api_v2_uddi.Save_binding.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "bindingDetail"));
        oper.setReturnClass(api.server.jUDDI.api_v2_uddi.BindingDetail.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "bindingDetail"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), "api.server.jUDDI.api_v2_uddi.DispositionReport", new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), true));
        _operations[11] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("save_business");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "save_business"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uddi-org:api_v2", "save_business"), api.server.jUDDI.api_v2_uddi.Save_business.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "businessDetail"));
        oper.setReturnClass(api.server.jUDDI.api_v2_uddi.BusinessDetail.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "businessDetail"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), "api.server.jUDDI.api_v2_uddi.DispositionReport", new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), true));
        _operations[12] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("save_service");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "save_service"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uddi-org:api_v2", "save_service"), api.server.jUDDI.api_v2_uddi.Save_service.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "serviceDetail"));
        oper.setReturnClass(api.server.jUDDI.api_v2_uddi.ServiceDetail.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "serviceDetail"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), "api.server.jUDDI.api_v2_uddi.DispositionReport", new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), true));
        _operations[13] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("save_tModel");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "save_tModel"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uddi-org:api_v2", "save_tModel"), api.server.jUDDI.api_v2_uddi.Save_tModel.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "tModelDetail"));
        oper.setReturnClass(api.server.jUDDI.api_v2_uddi.TModelDetail.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "tModelDetail"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), "api.server.jUDDI.api_v2_uddi.DispositionReport", new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), true));
        _operations[14] = oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("set_publisherAssertions");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "set_publisherAssertions"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uddi-org:api_v2", "set_publisherAssertions"), api.server.jUDDI.api_v2_uddi.Set_publisherAssertions.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "publisherAssertions"));
        oper.setReturnClass(api.server.jUDDI.api_v2_uddi.PublisherAssertions.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "publisherAssertions"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), "api.server.jUDDI.api_v2_uddi.DispositionReport", new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport"), true));
        _operations[15] = oper;
    }

    public PublishSoapStub() throws org.apache.axis.AxisFault {
        this(null);
    }

    public PublishSoapStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public PublishSoapStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "accessPoint");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.AccessPoint.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "add_publisherAssertions");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Add_publisherAssertions.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "address");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Address.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "addressLine");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.AddressLine.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "assertionStatusItem");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.AssertionStatusItem.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "assertionStatusReport");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.AssertionStatusReport.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "authToken");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.AuthToken.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "bindingDetail");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.BindingDetail.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "bindingKey");
        cachedSerQNames.add(qName);
        cls = java.lang.String.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "bindingTemplate");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.BindingTemplate.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "bindingTemplates");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.BindingTemplates.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "businessDetail");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.BusinessDetail.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "businessEntity");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.BusinessEntity.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "businessInfo");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.BusinessInfo.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "businessInfos");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.BusinessInfos.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "businessKey");
        cachedSerQNames.add(qName);
        cls = java.lang.String.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "businessService");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.BusinessService.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "businessServices");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.BusinessServices.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "categoryBag");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.CategoryBag.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "contact");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Contact.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "contacts");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Contacts.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "delete_binding");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Delete_binding.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "delete_business");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Delete_business.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "delete_publisherAssertions");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Delete_publisherAssertions.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "delete_service");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Delete_service.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "delete_tModel");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Delete_tModel.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "description");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Description.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "discard_authToken");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Discard_authToken.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "discoveryURL");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.DiscoveryURL.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "discoveryURLs");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.DiscoveryURLs.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "dispositionReport");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.DispositionReport.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "email");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Email.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "errInfo");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.ErrInfo.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "get_assertionStatusReport");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Get_assertionStatusReport.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "get_authToken");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Get_authToken.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "get_publisherAssertions");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Get_publisherAssertions.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "get_registeredInfo");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Get_registeredInfo.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "hostingRedirector");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.HostingRedirector.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "identifierBag");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.IdentifierBag.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "instanceDetails");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.InstanceDetails.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "keyedReference");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.KeyedReference.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "keysOwned");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.KeysOwned.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "keyType");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.KeyType.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "name");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Name.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "overviewDoc");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.OverviewDoc.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "phone");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Phone.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "publisherAssertion");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.PublisherAssertion.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "publisherAssertions");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.PublisherAssertions.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "registeredInfo");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.RegisteredInfo.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "result");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Result.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "save_binding");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Save_binding.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "save_business");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Save_business.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "save_service");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Save_service.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "save_tModel");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Save_tModel.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "serviceDetail");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.ServiceDetail.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "serviceInfo");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.ServiceInfo.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "serviceInfos");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.ServiceInfos.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "serviceKey");
        cachedSerQNames.add(qName);
        cls = java.lang.String.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "set_publisherAssertions");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Set_publisherAssertions.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "tModel");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.TModel.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "tModelDetail");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.TModelDetail.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "tModelInfo");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.TModelInfo.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "tModelInfos");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.TModelInfos.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "tModelInstanceDetails");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.TModelInstanceDetails.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "tModelInstanceInfo");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.TModelInstanceInfo.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "tModelKey");
        cachedSerQNames.add(qName);
        cls = java.lang.String.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "truncated");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.Truncated.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);
        qName = new javax.xml.namespace.QName("urn:uddi-org:api_v2", "URLType");
        cachedSerQNames.add(qName);
        cls = api.server.jUDDI.api_v2_uddi.URLType.class;
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

    public api.server.jUDDI.api_v2_uddi.DispositionReport add_publisherAssertions(api.server.jUDDI.api_v2_uddi.Add_publisherAssertions body) throws java.rmi.RemoteException, api.server.jUDDI.api_v2_uddi.DispositionReport {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("add_publisherAssertions");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "add_publisherAssertions"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { body });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (api.server.jUDDI.api_v2_uddi.DispositionReport) _resp;
                } catch (java.lang.Exception _exception) {
                    return (api.server.jUDDI.api_v2_uddi.DispositionReport) org.apache.axis.utils.JavaUtils.convert(_resp, api.server.jUDDI.api_v2_uddi.DispositionReport.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof api.server.jUDDI.api_v2_uddi.DispositionReport) {
                    throw (api.server.jUDDI.api_v2_uddi.DispositionReport) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public api.server.jUDDI.api_v2_uddi.DispositionReport delete_binding(api.server.jUDDI.api_v2_uddi.Delete_binding body) throws java.rmi.RemoteException, api.server.jUDDI.api_v2_uddi.DispositionReport {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("delete_binding");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "delete_binding"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { body });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (api.server.jUDDI.api_v2_uddi.DispositionReport) _resp;
                } catch (java.lang.Exception _exception) {
                    return (api.server.jUDDI.api_v2_uddi.DispositionReport) org.apache.axis.utils.JavaUtils.convert(_resp, api.server.jUDDI.api_v2_uddi.DispositionReport.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof api.server.jUDDI.api_v2_uddi.DispositionReport) {
                    throw (api.server.jUDDI.api_v2_uddi.DispositionReport) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public api.server.jUDDI.api_v2_uddi.DispositionReport delete_business(api.server.jUDDI.api_v2_uddi.Delete_business body) throws java.rmi.RemoteException, api.server.jUDDI.api_v2_uddi.DispositionReport {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("delete_business");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "delete_business"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { body });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (api.server.jUDDI.api_v2_uddi.DispositionReport) _resp;
                } catch (java.lang.Exception _exception) {
                    return (api.server.jUDDI.api_v2_uddi.DispositionReport) org.apache.axis.utils.JavaUtils.convert(_resp, api.server.jUDDI.api_v2_uddi.DispositionReport.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof api.server.jUDDI.api_v2_uddi.DispositionReport) {
                    throw (api.server.jUDDI.api_v2_uddi.DispositionReport) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public api.server.jUDDI.api_v2_uddi.DispositionReport delete_publisherAssertions(api.server.jUDDI.api_v2_uddi.Delete_publisherAssertions body) throws java.rmi.RemoteException, api.server.jUDDI.api_v2_uddi.DispositionReport {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("delete_publisherAssertions");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "delete_publisherAssertions"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { body });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (api.server.jUDDI.api_v2_uddi.DispositionReport) _resp;
                } catch (java.lang.Exception _exception) {
                    return (api.server.jUDDI.api_v2_uddi.DispositionReport) org.apache.axis.utils.JavaUtils.convert(_resp, api.server.jUDDI.api_v2_uddi.DispositionReport.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof api.server.jUDDI.api_v2_uddi.DispositionReport) {
                    throw (api.server.jUDDI.api_v2_uddi.DispositionReport) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public api.server.jUDDI.api_v2_uddi.DispositionReport delete_service(api.server.jUDDI.api_v2_uddi.Delete_service body) throws java.rmi.RemoteException, api.server.jUDDI.api_v2_uddi.DispositionReport {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("delete_service");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "delete_service"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { body });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (api.server.jUDDI.api_v2_uddi.DispositionReport) _resp;
                } catch (java.lang.Exception _exception) {
                    return (api.server.jUDDI.api_v2_uddi.DispositionReport) org.apache.axis.utils.JavaUtils.convert(_resp, api.server.jUDDI.api_v2_uddi.DispositionReport.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof api.server.jUDDI.api_v2_uddi.DispositionReport) {
                    throw (api.server.jUDDI.api_v2_uddi.DispositionReport) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public api.server.jUDDI.api_v2_uddi.DispositionReport delete_tModel(api.server.jUDDI.api_v2_uddi.Delete_tModel body) throws java.rmi.RemoteException, api.server.jUDDI.api_v2_uddi.DispositionReport {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("delete_tModel");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "delete_tModel"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { body });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (api.server.jUDDI.api_v2_uddi.DispositionReport) _resp;
                } catch (java.lang.Exception _exception) {
                    return (api.server.jUDDI.api_v2_uddi.DispositionReport) org.apache.axis.utils.JavaUtils.convert(_resp, api.server.jUDDI.api_v2_uddi.DispositionReport.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof api.server.jUDDI.api_v2_uddi.DispositionReport) {
                    throw (api.server.jUDDI.api_v2_uddi.DispositionReport) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public api.server.jUDDI.api_v2_uddi.DispositionReport discard_authToken(api.server.jUDDI.api_v2_uddi.Discard_authToken body) throws java.rmi.RemoteException, api.server.jUDDI.api_v2_uddi.DispositionReport {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("discard_authToken");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "discard_authToken"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { body });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (api.server.jUDDI.api_v2_uddi.DispositionReport) _resp;
                } catch (java.lang.Exception _exception) {
                    return (api.server.jUDDI.api_v2_uddi.DispositionReport) org.apache.axis.utils.JavaUtils.convert(_resp, api.server.jUDDI.api_v2_uddi.DispositionReport.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof api.server.jUDDI.api_v2_uddi.DispositionReport) {
                    throw (api.server.jUDDI.api_v2_uddi.DispositionReport) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public api.server.jUDDI.api_v2_uddi.AssertionStatusReport get_assertionStatusReport(api.server.jUDDI.api_v2_uddi.Get_assertionStatusReport body) throws java.rmi.RemoteException, api.server.jUDDI.api_v2_uddi.DispositionReport {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("get_assertionStatusReport");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "get_assertionStatusReport"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { body });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (api.server.jUDDI.api_v2_uddi.AssertionStatusReport) _resp;
                } catch (java.lang.Exception _exception) {
                    return (api.server.jUDDI.api_v2_uddi.AssertionStatusReport) org.apache.axis.utils.JavaUtils.convert(_resp, api.server.jUDDI.api_v2_uddi.AssertionStatusReport.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof api.server.jUDDI.api_v2_uddi.DispositionReport) {
                    throw (api.server.jUDDI.api_v2_uddi.DispositionReport) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public api.server.jUDDI.api_v2_uddi.AuthToken get_authToken(api.server.jUDDI.api_v2_uddi.Get_authToken body) throws java.rmi.RemoteException, api.server.jUDDI.api_v2_uddi.DispositionReport {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("get_authToken");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "get_authToken"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { body });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (api.server.jUDDI.api_v2_uddi.AuthToken) _resp;
                } catch (java.lang.Exception _exception) {
                    return (api.server.jUDDI.api_v2_uddi.AuthToken) org.apache.axis.utils.JavaUtils.convert(_resp, api.server.jUDDI.api_v2_uddi.AuthToken.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof api.server.jUDDI.api_v2_uddi.DispositionReport) {
                    throw (api.server.jUDDI.api_v2_uddi.DispositionReport) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public api.server.jUDDI.api_v2_uddi.PublisherAssertions get_publisherAssertions(api.server.jUDDI.api_v2_uddi.Get_publisherAssertions body) throws java.rmi.RemoteException, api.server.jUDDI.api_v2_uddi.DispositionReport {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("get_publisherAssertions");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "get_publisherAssertions"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { body });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (api.server.jUDDI.api_v2_uddi.PublisherAssertions) _resp;
                } catch (java.lang.Exception _exception) {
                    return (api.server.jUDDI.api_v2_uddi.PublisherAssertions) org.apache.axis.utils.JavaUtils.convert(_resp, api.server.jUDDI.api_v2_uddi.PublisherAssertions.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof api.server.jUDDI.api_v2_uddi.DispositionReport) {
                    throw (api.server.jUDDI.api_v2_uddi.DispositionReport) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public api.server.jUDDI.api_v2_uddi.RegisteredInfo get_registeredInfo(api.server.jUDDI.api_v2_uddi.Get_registeredInfo body) throws java.rmi.RemoteException, api.server.jUDDI.api_v2_uddi.DispositionReport {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("get_registeredInfo");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "get_registeredInfo"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { body });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (api.server.jUDDI.api_v2_uddi.RegisteredInfo) _resp;
                } catch (java.lang.Exception _exception) {
                    return (api.server.jUDDI.api_v2_uddi.RegisteredInfo) org.apache.axis.utils.JavaUtils.convert(_resp, api.server.jUDDI.api_v2_uddi.RegisteredInfo.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof api.server.jUDDI.api_v2_uddi.DispositionReport) {
                    throw (api.server.jUDDI.api_v2_uddi.DispositionReport) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public api.server.jUDDI.api_v2_uddi.BindingDetail save_binding(api.server.jUDDI.api_v2_uddi.Save_binding body) throws java.rmi.RemoteException, api.server.jUDDI.api_v2_uddi.DispositionReport {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("save_binding");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "save_binding"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { body });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (api.server.jUDDI.api_v2_uddi.BindingDetail) _resp;
                } catch (java.lang.Exception _exception) {
                    return (api.server.jUDDI.api_v2_uddi.BindingDetail) org.apache.axis.utils.JavaUtils.convert(_resp, api.server.jUDDI.api_v2_uddi.BindingDetail.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof api.server.jUDDI.api_v2_uddi.DispositionReport) {
                    throw (api.server.jUDDI.api_v2_uddi.DispositionReport) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public api.server.jUDDI.api_v2_uddi.BusinessDetail save_business(api.server.jUDDI.api_v2_uddi.Save_business body) throws java.rmi.RemoteException, api.server.jUDDI.api_v2_uddi.DispositionReport {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("save_business");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "save_business"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { body });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (api.server.jUDDI.api_v2_uddi.BusinessDetail) _resp;
                } catch (java.lang.Exception _exception) {
                    return (api.server.jUDDI.api_v2_uddi.BusinessDetail) org.apache.axis.utils.JavaUtils.convert(_resp, api.server.jUDDI.api_v2_uddi.BusinessDetail.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof api.server.jUDDI.api_v2_uddi.DispositionReport) {
                    throw (api.server.jUDDI.api_v2_uddi.DispositionReport) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public api.server.jUDDI.api_v2_uddi.ServiceDetail save_service(api.server.jUDDI.api_v2_uddi.Save_service body) throws java.rmi.RemoteException, api.server.jUDDI.api_v2_uddi.DispositionReport {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("save_service");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "save_service"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { body });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (api.server.jUDDI.api_v2_uddi.ServiceDetail) _resp;
                } catch (java.lang.Exception _exception) {
                    return (api.server.jUDDI.api_v2_uddi.ServiceDetail) org.apache.axis.utils.JavaUtils.convert(_resp, api.server.jUDDI.api_v2_uddi.ServiceDetail.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof api.server.jUDDI.api_v2_uddi.DispositionReport) {
                    throw (api.server.jUDDI.api_v2_uddi.DispositionReport) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public api.server.jUDDI.api_v2_uddi.TModelDetail save_tModel(api.server.jUDDI.api_v2_uddi.Save_tModel body) throws java.rmi.RemoteException, api.server.jUDDI.api_v2_uddi.DispositionReport {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("save_tModel");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "save_tModel"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { body });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (api.server.jUDDI.api_v2_uddi.TModelDetail) _resp;
                } catch (java.lang.Exception _exception) {
                    return (api.server.jUDDI.api_v2_uddi.TModelDetail) org.apache.axis.utils.JavaUtils.convert(_resp, api.server.jUDDI.api_v2_uddi.TModelDetail.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof api.server.jUDDI.api_v2_uddi.DispositionReport) {
                    throw (api.server.jUDDI.api_v2_uddi.DispositionReport) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public api.server.jUDDI.api_v2_uddi.PublisherAssertions set_publisherAssertions(api.server.jUDDI.api_v2_uddi.Set_publisherAssertions body) throws java.rmi.RemoteException, api.server.jUDDI.api_v2_uddi.DispositionReport {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[15]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("set_publisherAssertions");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "set_publisherAssertions"));
        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[] { body });
            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (api.server.jUDDI.api_v2_uddi.PublisherAssertions) _resp;
                } catch (java.lang.Exception _exception) {
                    return (api.server.jUDDI.api_v2_uddi.PublisherAssertions) org.apache.axis.utils.JavaUtils.convert(_resp, api.server.jUDDI.api_v2_uddi.PublisherAssertions.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof api.server.jUDDI.api_v2_uddi.DispositionReport) {
                    throw (api.server.jUDDI.api_v2_uddi.DispositionReport) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }
}
