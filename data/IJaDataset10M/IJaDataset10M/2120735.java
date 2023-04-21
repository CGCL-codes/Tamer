package tr.com.srdc.isurf.xmlbuilders;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.ServiceFactory;
import org.apache.axis.client.Call;
import org.hibernate.criterion.Expression;
import org.openbravo.dal.core.OBContext;
import org.openbravo.dal.service.OBCriteria;
import org.openbravo.dal.service.OBDal;
import org.openbravo.model.ad.access.User;
import org.openbravo.model.common.businesspartner.BusinessPartner;
import org.w3c.dom.Document;
import tr.com.srdc.isurf.ISURFException;
import tr.com.srdc.isurf.ISURFExceptionNotification;
import tr.com.srdc.isurf.ISURFExceptionVariance;
import tr.com.srdc.isurf.ISURFWebService;
import tr.com.srdc.isurf.gs1.ucc.ean.ActivityTypeCodeListType;
import tr.com.srdc.isurf.gs1.ucc.ean.BusinessScope;
import tr.com.srdc.isurf.gs1.ucc.ean.CollaborationPriorityCodeListType;
import tr.com.srdc.isurf.gs1.ucc.ean.CollaborativeTradeItemType;
import tr.com.srdc.isurf.gs1.ucc.ean.CommandType;
import tr.com.srdc.isurf.gs1.ucc.ean.ContactInformation;
import tr.com.srdc.isurf.gs1.ucc.ean.DataSourceCodeListType;
import tr.com.srdc.isurf.gs1.ucc.ean.DocumentCommandHeaderType;
import tr.com.srdc.isurf.gs1.ucc.ean.DocumentCommandListType;
import tr.com.srdc.isurf.gs1.ucc.ean.DocumentCommandOperandType;
import tr.com.srdc.isurf.gs1.ucc.ean.DocumentCommandType;
import tr.com.srdc.isurf.gs1.ucc.ean.DocumentIdentification;
import tr.com.srdc.isurf.gs1.ucc.ean.DocumentStatusListType;
import tr.com.srdc.isurf.gs1.ucc.ean.EntityIdentificationType;
import tr.com.srdc.isurf.gs1.ucc.ean.ExceptionNotificationType;
import tr.com.srdc.isurf.gs1.ucc.ean.ExceptionStatusCodeListType;
import tr.com.srdc.isurf.gs1.ucc.ean.ExceptionType;
import tr.com.srdc.isurf.gs1.ucc.ean.ForecastAccuracyExceptionType;
import tr.com.srdc.isurf.gs1.ucc.ean.ForecastComparisonExceptionType;
import tr.com.srdc.isurf.gs1.ucc.ean.ForecastPurposeCodeListType;
import tr.com.srdc.isurf.gs1.ucc.ean.ForecastTypeCodeListType;
import tr.com.srdc.isurf.gs1.ucc.ean.MeasurementValueType;
import tr.com.srdc.isurf.gs1.ucc.ean.MessageType;
import tr.com.srdc.isurf.gs1.ucc.ean.MetricExceptionType;
import tr.com.srdc.isurf.gs1.ucc.ean.MetricTypeCodeListType;
import tr.com.srdc.isurf.gs1.ucc.ean.MultiMeasurementValueType;
import tr.com.srdc.isurf.gs1.ucc.ean.ObjectFactory;
import tr.com.srdc.isurf.gs1.ucc.ean.OperationalExceptionType;
import tr.com.srdc.isurf.gs1.ucc.ean.Partner;
import tr.com.srdc.isurf.gs1.ucc.ean.PartnerIdentification;
import tr.com.srdc.isurf.gs1.ucc.ean.PartyIdentificationType;
import tr.com.srdc.isurf.gs1.ucc.ean.ResolutionCodeListType;
import tr.com.srdc.isurf.gs1.ucc.ean.Scope;
import tr.com.srdc.isurf.gs1.ucc.ean.StandardBusinessDocument;
import tr.com.srdc.isurf.gs1.ucc.ean.StandardBusinessDocumentHeader;
import tr.com.srdc.isurf.gs1.ucc.ean.TimePeriodType;
import tr.com.srdc.isurf.gs1.ucc.ean.TradeItemIdentificationType;
import tr.com.srdc.isurf.gs1.ucc.ean.TransactionType;
import tr.com.srdc.isurf.gs1.ucc.ean.VersionType;

public class ExceptionNotificationMessageBuilder implements MessageBuilder {

    private static int BUYER = 1;

    private static int SELLER = 2;

    private static int dataSource;

    private String exceptionnotificationId;

    private ISURFExceptionNotification exceptionnotification;

    public ObjectFactory oFactory = null;

    public StandardBusinessDocumentHeader MBusinessDocumentHeader;

    public StandardBusinessDocument MBusinessDocument;

    public ExceptionNotificationMessageBuilder(String exceptionnotificationId) {
        super();
        this.exceptionnotificationId = exceptionnotificationId;
    }

    @Override
    public int buildMessage() {
        final OBCriteria<ISURFExceptionNotification> enCriteria = OBDal.getInstance().createCriteria(ISURFExceptionNotification.class);
        enCriteria.add(Expression.eq(ISURFExceptionNotification.PROPERTY_ID, exceptionnotificationId));
        final List<ISURFExceptionNotification> flist = enCriteria.list();
        exceptionnotification = flist.get(0);
        oFactory = new ObjectFactory();
        MBusinessDocumentHeader = oFactory.createStandardBusinessDocumentHeader();
        MBusinessDocument = oFactory.createStandardBusinessDocument();
        if (exceptionnotification.getRoleInTransaction().equals("BUYER")) dataSource = BUYER; else dataSource = SELLER;
        int errorCode = 0;
        errorCode = constructHeader();
        if (errorCode == 0) {
            errorCode = constructMessage();
        }
        if (errorCode == 0) {
            JAXBContext jaxbContext = null;
            Marshaller marshaller = null;
            try {
                jaxbContext = JAXBContext.newInstance("tr.com.srdc.isurf.gs1.ucc.ean");
                marshaller = jaxbContext.createMarshaller();
            } catch (JAXBException e) {
                e.printStackTrace();
            }
            File out = null;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document stNode = null;
            try {
                stNode = dbf.newDocumentBuilder().newDocument();
            } catch (ParserConfigurationException e1) {
                e1.printStackTrace();
            }
            try {
                out = new File("OutputEN.xml");
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(oFactory.createStandardBusinessDocument(MBusinessDocument), new FileOutputStream(out));
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(oFactory.createStandardBusinessDocument(MBusinessDocument), stNode);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (JAXBException e) {
                e.printStackTrace();
            }
            return sendMessage(stNode);
        }
        return errorCode;
    }

    @Override
    public int sendMessage(Document stNode) {
        System.setProperty("javax.xml.soap.MessageFactory", "weblogic.webservice.core.soap.MessageFactoryImpl");
        OBCriteria<ISURFWebService> cri = OBDal.getInstance().createCriteria(ISURFWebService.class);
        cri.add(Expression.eq(ISURFWebService.PROPERTY_BUSINESSPARTNER, exceptionnotification.getBusinessPartner()));
        if (cri.list().size() == 0) return ErrorTypes.WEBSERVICE_DEF_NOT_FOUND;
        ISURFWebService webService = cri.list().get(0);
        ServiceFactory factory;
        try {
            factory = ServiceFactory.newInstance();
            String targetNamespace = webService.getNamespace();
            QName serviceName = new QName(targetNamespace, webService.getServiceName());
            QName portName = new QName(targetNamespace, webService.getPortName());
            String operationName = null;
            if (webService.getExceptionNotificationOperation() != null) operationName = webService.getExceptionNotificationOperation().getName(); else return ErrorTypes.WSDL_OPERATION_NOTFOUND;
            URL wsdlLocation = new URL(webService.getWSDLLocation());
            Service service = factory.createService(wsdlLocation, serviceName);
            Call call = (Call) service.createCall(portName, operationName);
            if (webService.getEndPointLocation() != null) call.setTargetEndpointAddress(webService.getEndPointLocation());
            call.setProperty(Call.ENCODINGSTYLE_URI_PROPERTY, targetNamespace);
            QName qn = new QName("http://ws.isurf.srdc.com.tr", "Result");
            call.registerTypeMapping(Result.class, qn, new org.apache.axis.encoding.ser.BeanSerializerFactory(Result.class, qn), new org.apache.axis.encoding.ser.BeanDeserializerFactory(Result.class, qn));
            call.setReturnType(qn);
            return ((Result) call.invoke(new Object[] { stNode })).getResult();
        } catch (ServiceException e) {
            e.printStackTrace();
            return ErrorTypes.NETWORK_ERROR;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ErrorTypes.NETWORK_ERROR;
        } catch (RemoteException e) {
            e.printStackTrace();
            return ErrorTypes.NETWORK_ERROR;
        }
    }

    @Override
    public int constructHeader() {
        Partner sender = oFactory.createPartner();
        Partner receiver = oFactory.createPartner();
        PartnerIdentification senderI = oFactory.createPartnerIdentification();
        PartnerIdentification receiverI = oFactory.createPartnerIdentification();
        senderI.setAuthority("EAN:UCC");
        receiverI.setAuthority("EAN:UCC");
        ContactInformation senderCI = oFactory.createContactInformation();
        ContactInformation receiverCI = oFactory.createContactInformation();
        User OBuser = OBContext.getOBContext().getUser();
        BusinessPartner bPartner = exceptionnotification.getBusinessPartner();
        if (exceptionnotification.getRoleInTransaction().equals("BUYER")) dataSource = BUYER; else dataSource = SELLER;
        if (dataSource == SELLER) {
            senderCI.setContactTypeIdentifier("Seller");
            receiverCI.setContactTypeIdentifier("Buyer");
        } else {
            senderCI.setContactTypeIdentifier("Buyer");
            receiverCI.setContactTypeIdentifier("Seller");
        }
        senderCI.setContact(OBuser.getName());
        senderCI.setEmailAddress(OBuser.getEmail());
        senderCI.setFaxNumber(OBuser.getFax());
        senderCI.setTelephoneNumber(OBuser.getPhone());
        receiverCI.setContact(bPartner.getName());
        receiverCI.setEmailAddress(bPartner.getADUserList().get(0).getEmail());
        receiverCI.setFaxNumber(bPartner.getBusinessPartnerLocationList().get(0).getFax());
        receiverCI.setTelephoneNumber(bPartner.getBusinessPartnerLocationList().get(0).getPhone());
        sender.getContactInformation().add(senderCI);
        sender.setIdentifier(senderI);
        receiver.getContactInformation().add(receiverCI);
        receiver.setIdentifier(receiverI);
        BusinessScope bscope = oFactory.createBusinessScope();
        Scope scope = oFactory.createScope();
        scope.setInstanceIdentifier(exceptionnotification.getBusinessSession().getSessionIDNumber().toString());
        scope.setType("session");
        bscope.getScope().add(scope);
        DocumentIdentification docIdentification = oFactory.createDocumentIdentification();
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        DatatypeFactory dtf = null;
        try {
            dtf = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        docIdentification.setCreationDateAndTime(dtf.newXMLGregorianCalendar(cal));
        docIdentification.setInstanceIdentifier(exceptionnotificationId);
        docIdentification.setStandard("EAN.UCC");
        docIdentification.setType("ExceptionNotification");
        docIdentification.setTypeVersion("2.0.2");
        MBusinessDocumentHeader.setDocumentIdentification(docIdentification);
        MBusinessDocumentHeader.setBusinessScope(bscope);
        MBusinessDocumentHeader.setHeaderVersion("2.2");
        MBusinessDocumentHeader.getSender().add(sender);
        MBusinessDocumentHeader.getReceiver().add(receiver);
        return 0;
    }

    @Override
    public int constructMessage() {
        MessageType MMessageType = oFactory.createMessageType();
        TransactionType MTransactionType = oFactory.createTransactionType();
        DocumentCommandHeaderType MDocumentCommandHeaderType = oFactory.createDocumentCommandHeaderType();
        CommandType MCommandType = oFactory.createCommandType();
        DocumentCommandType MDocumentCommandType = oFactory.createDocumentCommandType();
        DocumentCommandOperandType MDocumentCommandOperandType = oFactory.createDocumentCommandOperandType();
        ExceptionNotificationType MExceptionNotificationType = oFactory.createExceptionNotificationType();
        GregorianCalendar cal = new GregorianCalendar();
        EntityIdentificationType eit = oFactory.createEntityIdentificationType();
        eit.setUniqueCreatorIdentification("MSG-EC-" + exceptionnotificationId.substring(0, 5));
        if (exceptionnotification.getOrganization().getOrganizationInformationList().size() == 0) {
            return ErrorTypes.ORGINFO_NOTFOUND;
        } else if (exceptionnotification.getOrganization().getOrganizationInformationList().get(0).getGlobalLocationNo() == null || exceptionnotification.getOrganization().getOrganizationInformationList().get(0).getGlobalLocationNo().equals("")) {
            return ErrorTypes.ORGINFO_GLN_NOTFOUND;
        } else {
            PartyIdentificationType contentOwner = oFactory.createPartyIdentificationType();
            contentOwner.setGln(exceptionnotification.getOrganization().getOrganizationInformationList().get(0).getGlobalLocationNo());
            eit.setContentOwner(contentOwner);
        }
        MMessageType.setEntityIdentification(eit);
        MTransactionType.setEntityIdentification(eit);
        MDocumentCommandHeaderType.setEntityIdentification(eit);
        MDocumentCommandHeaderType.setType(DocumentCommandListType.ADD);
        MExceptionNotificationType.setDataSourceCode(DataSourceCodeListType.fromValue(exceptionnotification.getRoleInTransaction()));
        PartyIdentificationType pitBuyer = new PartyIdentificationType();
        PartyIdentificationType pitSeller = new PartyIdentificationType();
        String BPartnerGLN = "";
        if (exceptionnotification.getBusinessPartner().getBusinessPartnerLocationList().size() == 0) {
            return ErrorTypes.BPARTNERLOC_NOTFOUND;
        } else {
            int i;
            for (i = 0; i < exceptionnotification.getBusinessPartner().getBusinessPartnerLocationList().size(); i++) {
                if (exceptionnotification.getBusinessPartner().getBusinessPartnerLocationList().get(i).getGlobalLocationNo() != null && !exceptionnotification.getBusinessPartner().getBusinessPartnerLocationList().get(i).getGlobalLocationNo().equals("")) {
                    BPartnerGLN = exceptionnotification.getBusinessPartner().getBusinessPartnerLocationList().get(i).getGlobalLocationNo();
                    break;
                }
            }
            if (i == exceptionnotification.getBusinessPartner().getBusinessPartnerLocationList().size()) {
                return ErrorTypes.BPARTNERLOC_GLN_NOTFOUND;
            }
        }
        if (dataSource == BUYER) {
            pitBuyer.setGln(exceptionnotification.getOrganization().getOrganizationInformationList().get(0).getGlobalLocationNo());
            pitSeller.setGln(BPartnerGLN);
        } else {
            pitSeller.setGln(exceptionnotification.getOrganization().getOrganizationInformationList().get(0).getGlobalLocationNo());
            pitBuyer.setGln(BPartnerGLN);
        }
        MExceptionNotificationType.setBuyer(pitBuyer);
        MExceptionNotificationType.setSeller(pitSeller);
        VersionType vt = oFactory.createVersionType();
        vt.setVersionIdentification("2.0.2");
        MExceptionNotificationType.setContentVersion(vt);
        cal.setTime(new Date());
        DatatypeFactory dtf = null;
        try {
            dtf = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        MExceptionNotificationType.setCreationDateTime(dtf.newXMLGregorianCalendar(cal));
        MExceptionNotificationType.setDocumentStatus(DocumentStatusListType.ORIGINAL);
        MExceptionNotificationType.setDocumentStructureVersion(vt);
        TimePeriodType period = oFactory.createTimePeriodType();
        cal.setTime(exceptionnotification.getPeriodbegindate());
        period.setBeginDate(dtf.newXMLGregorianCalendar(cal));
        cal.setTime(exceptionnotification.getPeriodenddate());
        period.setEndDate(dtf.newXMLGregorianCalendar(cal));
        MExceptionNotificationType.setPeriod(period);
        int ie = 0;
        for (ie = 0; ie < exceptionnotification.getISURFExceptionList().size(); ie++) {
            ISURFException exception = exceptionnotification.getISURFExceptionList().get(ie);
            ExceptionType exceptionType = oFactory.createExceptionType();
            CollaborativeTradeItemType cTradeItemType = oFactory.createCollaborativeTradeItemType();
            PartyIdentificationType pitCSeller = oFactory.createPartyIdentificationType();
            PartyIdentificationType pitCBuyer = oFactory.createPartyIdentificationType();
            if (dataSource == 1) {
                pitCBuyer.setGln(exceptionnotification.getOrganization().getOrganizationInformationList().get(0).getGlobalLocationNo());
                pitCSeller.setGln(BPartnerGLN);
            } else {
                pitCBuyer.setGln(BPartnerGLN);
                pitCSeller.setGln(exceptionnotification.getOrganization().getOrganizationInformationList().get(0).getGlobalLocationNo());
            }
            cTradeItemType.setBuyerLocation(pitCBuyer);
            cTradeItemType.setSellerLocation(pitCSeller);
            TradeItemIdentificationType tiit = oFactory.createTradeItemIdentificationType();
            if (exception.getProduct().getUPCEAN() == null || exception.getProduct().getUPCEAN().equals("")) return ErrorTypes.PRODUCT_GTIN_NOTFOUND;
            tiit.setGtin(exception.getProduct().getUPCEAN());
            cTradeItemType.setProduct(tiit);
            exceptionType.setCollaborativeTradeItem(cTradeItemType);
            TimePeriodType exCriterionPeriod = oFactory.createTimePeriodType();
            cal.setTime(exception.getPeriodbegindate());
            exCriterionPeriod.setBeginDate(dtf.newXMLGregorianCalendar(cal));
            cal.setTime(exception.getPeriodenddate());
            exCriterionPeriod.setEndDate(dtf.newXMLGregorianCalendar(cal));
            exceptionType.setEffectiveDate(exCriterionPeriod);
            exceptionType.setExceptionStatus(ExceptionStatusCodeListType.fromValue(exception.getExceptionStatus()));
            exceptionType.setResolutionCode(ResolutionCodeListType.fromValue(exception.getResolutionCode()));
            exceptionType.setCollaborationPriority(CollaborationPriorityCodeListType.fromValue(exception.getCollaborationPriority()));
            exceptionType.setExceptionComment(exception.getExceptionComment());
            exceptionType.setComparedValue(exception.getComparedValue().floatValue());
            exceptionType.setSourceValue(exception.getSourceValue().floatValue());
            if (exception.getChoice().equals("FAE")) {
                if (exception.getDataSource() != null && exception.getForecastPurpose() != null && exception.getForecastType() != null && exception.getForecastGenerationTime() != null) {
                    ForecastAccuracyExceptionType faet = oFactory.createForecastAccuracyExceptionType();
                    faet.setDataSource(DataSourceCodeListType.fromValue(exception.getDataSource()));
                    faet.setForecastPurpose(ForecastPurposeCodeListType.fromValue(exception.getForecastPurpose()));
                    faet.setForecastType(ForecastTypeCodeListType.fromValue(exception.getForecastType()));
                    cal.setTime(exception.getForecastGenerationTime());
                    faet.setForecastGenerationDateTime(dtf.newXMLGregorianCalendar(cal));
                    exceptionType.setForecastAccuracyException(faet);
                } else return ErrorTypes.EXCEPTION_CHOICE_NOTFILLED;
            } else if (exception.getChoice().equals("FCE")) {
                if (exception.getDataSource() != null && exception.getComparisonDataSource() != null && exception.getForecastPurpose() != null && exception.getForecastType() != null && exception.getForecastGenerationTime() != null && exception.getComparisonForecastGenerationTime() != null) {
                    ForecastComparisonExceptionType fcet = oFactory.createForecastComparisonExceptionType();
                    fcet.setDataSource(DataSourceCodeListType.fromValue(exception.getDataSource()));
                    fcet.setComparisonDataSource(DataSourceCodeListType.fromValue(exception.getComparisonDataSource()));
                    fcet.setForecastPurpose(ForecastPurposeCodeListType.fromValue(exception.getForecastPurpose()));
                    fcet.setForecastType(ForecastTypeCodeListType.fromValue(exception.getForecastType()));
                    cal.setTime(exception.getForecastGenerationTime());
                    fcet.setForecastGenerationDateTime(dtf.newXMLGregorianCalendar(cal));
                    cal.setTime(exception.getComparisonForecastGenerationTime());
                    fcet.setComparisonForecastGenerationDateTime(dtf.newXMLGregorianCalendar(cal));
                    exceptionType.setForecastComparisonException(fcet);
                } else return ErrorTypes.EXCEPTION_CHOICE_NOTFILLED;
            } else if (exception.getChoice().equals("ME")) {
                if (exception.getMetricType() != null) {
                    MetricExceptionType met = oFactory.createMetricExceptionType();
                    met.setMetricType(MetricTypeCodeListType.fromValue(exception.getMetricType()));
                    exceptionType.setMetricException(met);
                } else return ErrorTypes.EXCEPTION_CHOICE_NOTFILLED;
            } else {
                if (exception.getActivityType() != null) {
                    OperationalExceptionType oet = oFactory.createOperationalExceptionType();
                    oet.setActivityType(ActivityTypeCodeListType.fromValue(exception.getActivityType()));
                    exceptionType.setOperationalException(oet);
                } else return ErrorTypes.EXCEPTION_CHOICE_NOTFILLED;
            }
            MultiMeasurementValueType mmvt = oFactory.createMultiMeasurementValueType();
            int t;
            for (t = 0; t < exception.getISURFExceptionVarianceList().size(); t++) {
                ISURFExceptionVariance exVariance = exception.getISURFExceptionVarianceList().get(t);
                MeasurementValueType mvt = oFactory.createMeasurementValueType();
                mvt.setUnitOfMeasure(exVariance.getUOM().getEDICode());
                mvt.setValue(exVariance.getValue());
                mmvt.getMeasurementValue().add(mvt);
            }
            if (t == 0) return ErrorTypes.CRITERION_THRESHOLD_NOTFOUND;
            exceptionType.setVariance(mmvt);
            MExceptionNotificationType.getException().add(exceptionType);
        }
        if (ie == 0) {
            return ErrorTypes.CRITERION_NOTFOUND;
        }
        MDocumentCommandOperandType.getDocument().add(oFactory.createExceptionNotification(MExceptionNotificationType));
        MDocumentCommandType.setDocumentCommandHeader(MDocumentCommandHeaderType);
        MDocumentCommandType.setDocumentCommandOperand(MDocumentCommandOperandType);
        MCommandType.setCommand(oFactory.createDocumentCommand(MDocumentCommandType));
        MTransactionType.getCommand().add(MCommandType);
        MMessageType.getAny().add(oFactory.createTransaction(MTransactionType));
        MBusinessDocument.setStandardBusinessDocumentHeader(MBusinessDocumentHeader);
        MBusinessDocument.setAny(oFactory.createMessage(MMessageType));
        return 0;
    }
}
