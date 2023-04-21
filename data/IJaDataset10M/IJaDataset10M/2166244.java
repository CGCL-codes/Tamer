/**
 * MerchantInterfaceStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.amazon.merchants.transport.soapclient;

public class MerchantInterfaceStub extends org.apache.axis.client.Stub implements com.amazon.merchants.transport.soapclient.MerchantInterface {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[12];
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getDocument");
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "merchant"), new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "Merchant"), com.amazon.merchants.transport.soapclient.Merchant.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "documentIdentifier"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "doc"), new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "octetstream"), org.apache.axis.attachments.OctetStream.class, org.apache.axis.description.ParameterDesc.OUT, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
		oper.setStyle(org.apache.axis.enum.Style.DOCUMENT);
        oper.setReturnQName(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "string_Response"));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "idoox-java-mapping.org.idoox.wasp.SoapFaultException"),
                      "com.amazon.merchants.transport.soapclient.MerchantInterface_getDocument_orgIdooxWaspSoapFaultException_Fault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getDocumentInterfaceConformance");
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "merchant"), new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "Merchant"), com.amazon.merchants.transport.soapclient.Merchant.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "documentIdentifier"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "doc"), new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "octetstream"), org.apache.axis.attachments.OctetStream.class, org.apache.axis.description.ParameterDesc.OUT, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
		oper.setStyle(org.apache.axis.enum.Style.DOCUMENT);
        oper.setReturnQName(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "string_Response"));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "idoox-java-mapping.org.idoox.wasp.SoapFaultException"),
                      "com.amazon.merchants.transport.soapclient.MerchantInterface_getDocumentInterfaceConformance_orgIdooxWaspSoapFaultException_Fault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("postDocumentDownloadAck");
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "merchant"), new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "Merchant"), com.amazon.merchants.transport.soapclient.Merchant.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "documentIdentifierArray"), new javax.xml.namespace.QName("http://systinet.com/wsdl/java/lang/", "ArrayOfstring"), com.amazon.merchants.transport.soapclient.ArrayOfstring.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "ArrayOfDocumentDownloadAckStatus"));
        oper.setReturnClass(com.amazon.merchants.transport.soapclient.ArrayOfDocumentDownloadAckStatus.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "ArrayOfDocumentDownloadAckStatus_Response"));
        oper.setStyle(org.apache.axis.enum.Style.DOCUMENT);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "idoox-java-mapping.org.idoox.wasp.SoapFaultException"),
                      "com.amazon.merchants.transport.soapclient.MerchantInterface_postDocumentDownloadAck_orgIdooxWaspSoapFaultException_Fault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("postDocument");
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "merchant"), new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "Merchant"), com.amazon.merchants.transport.soapclient.Merchant.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "messageType"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "doc"), new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "octetstream"), org.apache.axis.attachments.OctetStream.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "DocumentSubmissionResponse"));
        oper.setReturnClass(com.amazon.merchants.transport.soapclient.DocumentSubmissionResponse.class);
		oper.setStyle(org.apache.axis.enum.Style.DOCUMENT);
        oper.setReturnQName(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "DocumentSubmissionResponse_Response"));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "idoox-java-mapping.org.idoox.wasp.SoapFaultException"),
                      "com.amazon.merchants.transport.soapclient.MerchantInterface_postDocument_orgIdooxWaspSoapFaultException_Fault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("postDocumentInterfaceConformance");
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "merchant"), new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "Merchant"), com.amazon.merchants.transport.soapclient.Merchant.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "messageType"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "doc"), new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "octetstream"), org.apache.axis.attachments.OctetStream.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "DocumentSubmissionResponse"));
        oper.setReturnClass(com.amazon.merchants.transport.soapclient.DocumentSubmissionResponse.class);
		oper.setStyle(org.apache.axis.enum.Style.DOCUMENT);
        oper.setReturnQName(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "DocumentSubmissionResponse_Response"));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "idoox-java-mapping.org.idoox.wasp.SoapFaultException"),
                      "com.amazon.merchants.transport.soapclient.MerchantInterface_postDocumentInterfaceConformance_orgIdooxWaspSoapFaultException_Fault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getLastNDocumentInfo");
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "merchant"), new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "Merchant"), com.amazon.merchants.transport.soapclient.Merchant.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "messageType"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "howMany"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "ArrayOfMerchantDocumentInfo"));
        oper.setReturnClass(com.amazon.merchants.transport.soapclient.ArrayOfMerchantDocumentInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "ArrayOfMerchantDocumentInfo_Response"));
        oper.setStyle(org.apache.axis.enum.Style.DOCUMENT);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "idoox-java-mapping.org.idoox.wasp.SoapFaultException"),
                      "com.amazon.merchants.transport.soapclient.MerchantInterface_getLastNDocumentInfo_orgIdooxWaspSoapFaultException_Fault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getLastNPendingDocumentInfo");
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "merchant"), new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "Merchant"), com.amazon.merchants.transport.soapclient.Merchant.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "messageType"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "howMany"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "ArrayOfMerchantDocumentInfo"));
        oper.setReturnClass(com.amazon.merchants.transport.soapclient.ArrayOfMerchantDocumentInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "ArrayOfMerchantDocumentInfo_Response"));
        oper.setStyle(org.apache.axis.enum.Style.DOCUMENT);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "idoox-java-mapping.org.idoox.wasp.SoapFaultException"),
                      "com.amazon.merchants.transport.soapclient.MerchantInterface_getLastNPendingDocumentInfo_orgIdooxWaspSoapFaultException_Fault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getDocumentProcessingStatus");
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "merchant"), new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "Merchant"), com.amazon.merchants.transport.soapclient.Merchant.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "documentTransactionIdentifier"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "DocumentProcessingInfo"));
        oper.setReturnClass(com.amazon.merchants.transport.soapclient.DocumentProcessingInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "DocumentProcessingInfo_Response"));
        oper.setStyle(org.apache.axis.enum.Style.DOCUMENT);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "idoox-java-mapping.org.idoox.wasp.SoapFaultException"),
                      "com.amazon.merchants.transport.soapclient.MerchantInterface_getDocumentProcessingStatus_orgIdooxWaspSoapFaultException_Fault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAllPendingDocumentInfo");
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "merchant"), new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "Merchant"), com.amazon.merchants.transport.soapclient.Merchant.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "messageType"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "ArrayOfMerchantDocumentInfo"));
        oper.setReturnClass(com.amazon.merchants.transport.soapclient.ArrayOfMerchantDocumentInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "ArrayOfMerchantDocumentInfo_Response"));
        oper.setStyle(org.apache.axis.enum.Style.DOCUMENT);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "idoox-java-mapping.org.idoox.wasp.SoapFaultException"),
                      "com.amazon.merchants.transport.soapclient.MerchantInterface_getAllPendingDocumentInfo_orgIdooxWaspSoapFaultException_Fault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getLastNReportInfo");
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "merchant"), new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "Merchant"), com.amazon.merchants.transport.soapclient.Merchant.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "messageType"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "numberOfReports"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "ArrayOfReportInfo"));
        oper.setReturnClass(com.amazon.merchants.transport.soapclient.ArrayOfReportInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "ArrayOfReportInfo_Response"));
        oper.setStyle(org.apache.axis.enum.Style.DOCUMENT);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "idoox-java-mapping.org.idoox.wasp.SoapFaultException"),
                      "com.amazon.merchants.transport.soapclient.MerchantInterface_getLastNReportInfo_orgIdooxWaspSoapFaultException_Fault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[9] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getLastNDocumentProcessingStatuses");
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "merchant"), new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "Merchant"), com.amazon.merchants.transport.soapclient.Merchant.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "numberOfStatuses"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "uploadType"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "ArrayOfSummaryInfo"));
        oper.setReturnClass(com.amazon.merchants.transport.soapclient.ArrayOfSummaryInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "ArrayOfSummaryInfo_Response"));
        oper.setStyle(org.apache.axis.enum.Style.DOCUMENT);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "idoox-java-mapping.org.idoox.wasp.SoapFaultException"),
                      "com.amazon.merchants.transport.soapclient.MerchantInterface_getLastNDocumentProcessingStatuses_orgIdooxWaspSoapFaultException_Fault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getMerchantInfoFromCustomerID");
        oper.addParameter(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "customerID"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "MerchantInfo"));
        oper.setReturnClass(com.amazon.merchants.transport.soapclient.MerchantInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "MerchantInfo_Response"));
        oper.setStyle(org.apache.axis.enum.Style.DOCUMENT);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "idoox-java-mapping.org.idoox.wasp.SoapFaultException"),
                      "com.amazon.merchants.transport.soapclient.MerchantInterface_getMerchantInfoFromCustomerID_orgIdooxWaspSoapFaultException_Fault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[11] = oper;

    }

    public MerchantInterfaceStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public MerchantInterfaceStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public MerchantInterfaceStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class mimesf = org.apache.axis.encoding.ser.JAFDataHandlerSerializerFactory.class;
            java.lang.Class mimedf = org.apache.axis.encoding.ser.JAFDataHandlerDeserializerFactory.class;

            qName = new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "DataHandler");
            cachedSerQNames.add(qName);
            cls = javax.activation.DataHandler.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(mimesf);
            cachedDeserFactories.add(mimedf);

            qName = new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "DocumentDownloadAckStatus");
            cachedSerQNames.add(qName);
            cls = com.amazon.merchants.transport.soapclient.DocumentDownloadAckStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "MerchantDocumentInfo");
            cachedSerQNames.add(qName);
            cls = com.amazon.merchants.transport.soapclient.MerchantDocumentInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://systinet.com/wsdl/java/lang/", "ArrayOfstring");
            cachedSerQNames.add(qName);
            cls = com.amazon.merchants.transport.soapclient.ArrayOfstring.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "Merchant");
            cachedSerQNames.add(qName);
            cls = com.amazon.merchants.transport.soapclient.Merchant.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "ReportInfo");
            cachedSerQNames.add(qName);
            cls = com.amazon.merchants.transport.soapclient.ReportInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "ArrayOfReportInfo");
            cachedSerQNames.add(qName);
            cls = com.amazon.merchants.transport.soapclient.ArrayOfReportInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "ArrayOfMerchantDocumentInfo");
            cachedSerQNames.add(qName);
            cls = com.amazon.merchants.transport.soapclient.ArrayOfMerchantDocumentInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "ArrayOfSummaryInfo");
            cachedSerQNames.add(qName);
            cls = com.amazon.merchants.transport.soapclient.ArrayOfSummaryInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "DocumentSubmissionResponse");
            cachedSerQNames.add(qName);
            cls = com.amazon.merchants.transport.soapclient.DocumentSubmissionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "DocumentProcessingInfo");
            cachedSerQNames.add(qName);
            cls = com.amazon.merchants.transport.soapclient.DocumentProcessingInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "ArrayOfDocumentDownloadAckStatus");
            cachedSerQNames.add(qName);
            cls = com.amazon.merchants.transport.soapclient.ArrayOfDocumentDownloadAckStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "SummaryInfo");
            cachedSerQNames.add(qName);
            cls = com.amazon.merchants.transport.soapclient.SummaryInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.amazon.com/merchants/merchant-interface/", "MerchantInfo");
            cachedSerQNames.add(qName);
            cls = com.amazon.merchants.transport.soapclient.MerchantInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    private org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call =
                    (org.apache.axis.client.Call) super.service.createCall();
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
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                        java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                        _call.registerTypeMapping(cls, qName, sf, df, false);
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", t);
        }
    }

    public java.lang.String getDocument(com.amazon.merchants.transport.soapclient.Merchant merchant, java.lang.String documentIdentifier, org.apache.axis.holders.OctetStreamHolder doc) throws java.rmi.RemoteException, com.amazon.merchants.transport.soapclient.MerchantInterface_getDocument_orgIdooxWaspSoapFaultException_Fault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.amazon.com/merchants/merchant-interface/MerchantInterface#getDocument#KEx3YXNwY1NlcnZlci9BbXpJU0EvTWVyY2hhbnQ7TGphdmEvbGFuZy9TdHJpbmc7TG9yZy9pZG9veC93YXNwL3R5cGVzL1Jlc3BvbnNlTWVzc2FnZUF0dGFjaG1lbnQ7KUxqYXZhL2xhbmcvU3RyaW5nOw==");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getDocument"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {merchant, documentIdentifier});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                doc.value = (org.apache.axis.attachments.OctetStream) _output.get(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "doc"));
            } catch (java.lang.Exception _exception) {
                doc.value = (org.apache.axis.attachments.OctetStream) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "doc")), byte[].class);
            }
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
    }

    public java.lang.String getDocumentInterfaceConformance(com.amazon.merchants.transport.soapclient.Merchant merchant, java.lang.String documentIdentifier, org.apache.axis.holders.OctetStreamHolder doc) throws java.rmi.RemoteException, com.amazon.merchants.transport.soapclient.MerchantInterface_getDocumentInterfaceConformance_orgIdooxWaspSoapFaultException_Fault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.amazon.com/merchants/merchant-interface/MerchantInterface#getDocumentInterfaceConformance#KEx3YXNwY1NlcnZlci9BbXpJU0EvTWVyY2hhbnQ7TGphdmEvbGFuZy9TdHJpbmc7TG9yZy9pZG9veC93YXNwL3R5cGVzL1Jlc3BvbnNlTWVzc2FnZUF0dGFjaG1lbnQ7KUxqYXZhL2xhbmcvU3RyaW5nOw==");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getDocumentInterfaceConformance"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {merchant, documentIdentifier});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                doc.value = (org.apache.axis.attachments.OctetStream) _output.get(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "doc"));
            } catch (java.lang.Exception _exception) {
                doc.value = (org.apache.axis.attachments.OctetStream) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("http://systinet.com/xsd/SchemaTypes/", "doc")), byte[].class);
            }
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
    }

    public com.amazon.merchants.transport.soapclient.ArrayOfDocumentDownloadAckStatus postDocumentDownloadAck(com.amazon.merchants.transport.soapclient.Merchant merchant, com.amazon.merchants.transport.soapclient.ArrayOfstring documentIdentifierArray) throws java.rmi.RemoteException, com.amazon.merchants.transport.soapclient.MerchantInterface_postDocumentDownloadAck_orgIdooxWaspSoapFaultException_Fault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.amazon.com/merchants/merchant-interface/MerchantInterface#postDocumentDownloadAck#KEx3YXNwY1NlcnZlci9BbXpJU0EvTWVyY2hhbnQ7W0xqYXZhL2xhbmcvU3RyaW5nOylbTHdhc3BjU2VydmVyL0FteklTQS9Eb2N1bWVudERvd25sb2FkQWNrU3RhdHVzOw==");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "postDocumentDownloadAck"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {merchant, documentIdentifierArray});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.amazon.merchants.transport.soapclient.ArrayOfDocumentDownloadAckStatus) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.amazon.merchants.transport.soapclient.ArrayOfDocumentDownloadAckStatus) org.apache.axis.utils.JavaUtils.convert(_resp, com.amazon.merchants.transport.soapclient.ArrayOfDocumentDownloadAckStatus.class);
            }
        }
    }

    public com.amazon.merchants.transport.soapclient.DocumentSubmissionResponse postDocument(com.amazon.merchants.transport.soapclient.Merchant merchant, java.lang.String messageType, org.apache.axis.attachments.OctetStream doc) throws java.rmi.RemoteException, com.amazon.merchants.transport.soapclient.MerchantInterface_postDocument_orgIdooxWaspSoapFaultException_Fault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.amazon.com/merchants/merchant-interface/MerchantInterface#postDocument#KEx3YXNwY1NlcnZlci9BbXpJU0EvTWVyY2hhbnQ7TGphdmEvbGFuZy9TdHJpbmc7TG9yZy9pZG9veC93YXNwL3R5cGVzL1JlcXVlc3RNZXNzYWdlQXR0YWNobWVudDspTHdhc3BjU2VydmVyL0FteklTQS9Eb2N1bWVudFN1Ym1pc3Npb25SZXNwb25zZTs=");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "postDocument"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {merchant, messageType, doc});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.amazon.merchants.transport.soapclient.DocumentSubmissionResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.amazon.merchants.transport.soapclient.DocumentSubmissionResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.amazon.merchants.transport.soapclient.DocumentSubmissionResponse.class);
            }
        }
    }

    public com.amazon.merchants.transport.soapclient.DocumentSubmissionResponse postDocumentInterfaceConformance(com.amazon.merchants.transport.soapclient.Merchant merchant, java.lang.String messageType, org.apache.axis.attachments.OctetStream doc) throws java.rmi.RemoteException, com.amazon.merchants.transport.soapclient.MerchantInterface_postDocumentInterfaceConformance_orgIdooxWaspSoapFaultException_Fault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.amazon.com/merchants/merchant-interface/MerchantInterface#postDocumentInterfaceConformance#KEx3YXNwY1NlcnZlci9BbXpJU0EvTWVyY2hhbnQ7TGphdmEvbGFuZy9TdHJpbmc7TG9yZy9pZG9veC93YXNwL3R5cGVzL1JlcXVlc3RNZXNzYWdlQXR0YWNobWVudDspTHdhc3BjU2VydmVyL0FteklTQS9Eb2N1bWVudFN1Ym1pc3Npb25SZXNwb25zZTs=");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "postDocumentInterfaceConformance"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {merchant, messageType, doc});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.amazon.merchants.transport.soapclient.DocumentSubmissionResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.amazon.merchants.transport.soapclient.DocumentSubmissionResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.amazon.merchants.transport.soapclient.DocumentSubmissionResponse.class);
            }
        }
    }

    public com.amazon.merchants.transport.soapclient.ArrayOfMerchantDocumentInfo getLastNDocumentInfo(com.amazon.merchants.transport.soapclient.Merchant merchant, java.lang.String messageType, int howMany) throws java.rmi.RemoteException, com.amazon.merchants.transport.soapclient.MerchantInterface_getLastNDocumentInfo_orgIdooxWaspSoapFaultException_Fault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.amazon.com/merchants/merchant-interface/MerchantInterface#getLastNDocumentInfo#KEx3YXNwY1NlcnZlci9BbXpJU0EvTWVyY2hhbnQ7TGphdmEvbGFuZy9TdHJpbmc7SSlbTHdhc3BjU2VydmVyL0FteklTQS9NZXJjaGFudERvY3VtZW50SW5mbzs=");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getLastNDocumentInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {merchant, messageType, new java.lang.Integer(howMany)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.amazon.merchants.transport.soapclient.ArrayOfMerchantDocumentInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.amazon.merchants.transport.soapclient.ArrayOfMerchantDocumentInfo) org.apache.axis.utils.JavaUtils.convert(_resp, com.amazon.merchants.transport.soapclient.ArrayOfMerchantDocumentInfo.class);
            }
        }
    }

    public com.amazon.merchants.transport.soapclient.ArrayOfMerchantDocumentInfo getLastNPendingDocumentInfo(com.amazon.merchants.transport.soapclient.Merchant merchant, java.lang.String messageType, int howMany) throws java.rmi.RemoteException, com.amazon.merchants.transport.soapclient.MerchantInterface_getLastNPendingDocumentInfo_orgIdooxWaspSoapFaultException_Fault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.amazon.com/merchants/merchant-interface/MerchantInterface#getLastNPendingDocumentInfo#KEx3YXNwY1NlcnZlci9BbXpJU0EvTWVyY2hhbnQ7TGphdmEvbGFuZy9TdHJpbmc7SSlbTHdhc3BjU2VydmVyL0FteklTQS9NZXJjaGFudERvY3VtZW50SW5mbzs=");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getLastNPendingDocumentInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {merchant, messageType, new java.lang.Integer(howMany)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.amazon.merchants.transport.soapclient.ArrayOfMerchantDocumentInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.amazon.merchants.transport.soapclient.ArrayOfMerchantDocumentInfo) org.apache.axis.utils.JavaUtils.convert(_resp, com.amazon.merchants.transport.soapclient.ArrayOfMerchantDocumentInfo.class);
            }
        }
    }

    public com.amazon.merchants.transport.soapclient.DocumentProcessingInfo getDocumentProcessingStatus(com.amazon.merchants.transport.soapclient.Merchant merchant, long documentTransactionIdentifier) throws java.rmi.RemoteException, com.amazon.merchants.transport.soapclient.MerchantInterface_getDocumentProcessingStatus_orgIdooxWaspSoapFaultException_Fault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.amazon.com/merchants/merchant-interface/MerchantInterface#getDocumentProcessingStatus#KEx3YXNwY1NlcnZlci9BbXpJU0EvTWVyY2hhbnQ7SilMd2FzcGNTZXJ2ZXIvQW16SVNBL0RvY3VtZW50UHJvY2Vzc2luZ0luZm87");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getDocumentProcessingStatus"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {merchant, new java.lang.Long(documentTransactionIdentifier)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.amazon.merchants.transport.soapclient.DocumentProcessingInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.amazon.merchants.transport.soapclient.DocumentProcessingInfo) org.apache.axis.utils.JavaUtils.convert(_resp, com.amazon.merchants.transport.soapclient.DocumentProcessingInfo.class);
            }
        }
    }

    public com.amazon.merchants.transport.soapclient.ArrayOfMerchantDocumentInfo getAllPendingDocumentInfo(com.amazon.merchants.transport.soapclient.Merchant merchant, java.lang.String messageType) throws java.rmi.RemoteException, com.amazon.merchants.transport.soapclient.MerchantInterface_getAllPendingDocumentInfo_orgIdooxWaspSoapFaultException_Fault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.amazon.com/merchants/merchant-interface/MerchantInterface#getAllPendingDocumentInfo#KEx3YXNwY1NlcnZlci9BbXpJU0EvTWVyY2hhbnQ7TGphdmEvbGFuZy9TdHJpbmc7KVtMd2FzcGNTZXJ2ZXIvQW16SVNBL01lcmNoYW50RG9jdW1lbnRJbmZvOw==");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getAllPendingDocumentInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {merchant, messageType});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.amazon.merchants.transport.soapclient.ArrayOfMerchantDocumentInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.amazon.merchants.transport.soapclient.ArrayOfMerchantDocumentInfo) org.apache.axis.utils.JavaUtils.convert(_resp, com.amazon.merchants.transport.soapclient.ArrayOfMerchantDocumentInfo.class);
            }
        }
    }

    public com.amazon.merchants.transport.soapclient.ArrayOfReportInfo getLastNReportInfo(com.amazon.merchants.transport.soapclient.Merchant merchant, java.lang.String messageType, int numberOfReports) throws java.rmi.RemoteException, com.amazon.merchants.transport.soapclient.MerchantInterface_getLastNReportInfo_orgIdooxWaspSoapFaultException_Fault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.amazon.com/merchants/merchant-interface/MerchantInterface#getLastNReportInfo#KEx3YXNwY1NlcnZlci9BbXpJU0EvTWVyY2hhbnQ7TGphdmEvbGFuZy9TdHJpbmc7SSlbTHdhc3BjU2VydmVyL0FteklTQS9SZXBvcnRJbmZvOw==");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getLastNReportInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {merchant, messageType, new java.lang.Integer(numberOfReports)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.amazon.merchants.transport.soapclient.ArrayOfReportInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.amazon.merchants.transport.soapclient.ArrayOfReportInfo) org.apache.axis.utils.JavaUtils.convert(_resp, com.amazon.merchants.transport.soapclient.ArrayOfReportInfo.class);
            }
        }
    }

    public com.amazon.merchants.transport.soapclient.ArrayOfSummaryInfo getLastNDocumentProcessingStatuses(com.amazon.merchants.transport.soapclient.Merchant merchant, int numberOfStatuses, java.lang.String uploadType) throws java.rmi.RemoteException, com.amazon.merchants.transport.soapclient.MerchantInterface_getLastNDocumentProcessingStatuses_orgIdooxWaspSoapFaultException_Fault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.amazon.com/merchants/merchant-interface/MerchantInterface#getLastNDocumentProcessingStatuses#KEx3YXNwY1NlcnZlci9BbXpJU0EvTWVyY2hhbnQ7SUxqYXZhL2xhbmcvU3RyaW5nOylbTHdhc3BjU2VydmVyL0FteklTQS9TdW1tYXJ5SW5mbzs=");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getLastNDocumentProcessingStatuses"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {merchant, new java.lang.Integer(numberOfStatuses), uploadType});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.amazon.merchants.transport.soapclient.ArrayOfSummaryInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.amazon.merchants.transport.soapclient.ArrayOfSummaryInfo) org.apache.axis.utils.JavaUtils.convert(_resp, com.amazon.merchants.transport.soapclient.ArrayOfSummaryInfo.class);
            }
        }
    }

    public com.amazon.merchants.transport.soapclient.MerchantInfo getMerchantInfoFromCustomerID(java.lang.String customerID) throws java.rmi.RemoteException, com.amazon.merchants.transport.soapclient.MerchantInterface_getMerchantInfoFromCustomerID_orgIdooxWaspSoapFaultException_Fault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.amazon.com/merchants/merchant-interface/MerchantInterface#getMerchantInfoFromCustomerID#KExqYXZhL2xhbmcvU3RyaW5nOylMd2FzcGNTZXJ2ZXIvQW16SVNBL01lcmNoYW50SW5mbzs=");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getMerchantInfoFromCustomerID"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {customerID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.amazon.merchants.transport.soapclient.MerchantInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.amazon.merchants.transport.soapclient.MerchantInfo) org.apache.axis.utils.JavaUtils.convert(_resp, com.amazon.merchants.transport.soapclient.MerchantInfo.class);
            }
        }
    }

}
