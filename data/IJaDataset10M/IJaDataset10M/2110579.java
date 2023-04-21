package tr.com.srdc.isurf.xmlbuilders;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
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
import tr.com.srdc.isurf.ISURFInventoryActivityLineItem;
import tr.com.srdc.isurf.ISURFInventoryActivityOrStatus;
import tr.com.srdc.isurf.ISURFInventoryActivityQuantitySpecfication;
import tr.com.srdc.isurf.ISURFInventoryActivityTransactionalItem;
import tr.com.srdc.isurf.ISURFInventoryActivityTransactionalItemWeight;
import tr.com.srdc.isurf.ISURFInventoryDeliverReference;
import tr.com.srdc.isurf.ISURFInventoryItemLocationInfo;
import tr.com.srdc.isurf.ISURFInventoryStatusLineItem;
import tr.com.srdc.isurf.ISURFInventoryStatusQuantitySpecification;
import tr.com.srdc.isurf.ISURFInventoryStatusTransactionalItem;
import tr.com.srdc.isurf.ISURFInventoryStatusTransactionalItemWeight;
import tr.com.srdc.isurf.ISURFWebService;
import tr.com.srdc.isurf.gs1.ucc.ean.BusinessScope;
import tr.com.srdc.isurf.gs1.ucc.ean.CommandType;
import tr.com.srdc.isurf.gs1.ucc.ean.ContactInformation;
import tr.com.srdc.isurf.gs1.ucc.ean.DeliverReferenceCodeListType;
import tr.com.srdc.isurf.gs1.ucc.ean.DeliverReferenceType;
import tr.com.srdc.isurf.gs1.ucc.ean.DocumentCommandHeaderType;
import tr.com.srdc.isurf.gs1.ucc.ean.DocumentCommandListType;
import tr.com.srdc.isurf.gs1.ucc.ean.DocumentCommandOperandType;
import tr.com.srdc.isurf.gs1.ucc.ean.DocumentCommandType;
import tr.com.srdc.isurf.gs1.ucc.ean.DocumentIdentification;
import tr.com.srdc.isurf.gs1.ucc.ean.DocumentStatusListType;
import tr.com.srdc.isurf.gs1.ucc.ean.EntityIdentificationType;
import tr.com.srdc.isurf.gs1.ucc.ean.ISO31661CodeType;
import tr.com.srdc.isurf.gs1.ucc.ean.InventoryActivityLineItemType;
import tr.com.srdc.isurf.gs1.ucc.ean.InventoryActivityListType;
import tr.com.srdc.isurf.gs1.ucc.ean.InventoryActivityOrInventoryStatusType;
import tr.com.srdc.isurf.gs1.ucc.ean.InventoryActivityQuantitySpecificationType;
import tr.com.srdc.isurf.gs1.ucc.ean.InventoryDocumentTypeListType;
import tr.com.srdc.isurf.gs1.ucc.ean.InventoryItemLocationInformationType;
import tr.com.srdc.isurf.gs1.ucc.ean.InventoryMovementTypeListType;
import tr.com.srdc.isurf.gs1.ucc.ean.InventoryStatusLineItemType;
import tr.com.srdc.isurf.gs1.ucc.ean.InventoryStatusListType;
import tr.com.srdc.isurf.gs1.ucc.ean.InventoryStatusQuantitySpecificationType;
import tr.com.srdc.isurf.gs1.ucc.ean.MeasurementTypeCodeListType;
import tr.com.srdc.isurf.gs1.ucc.ean.MeasurementUnitCodeType;
import tr.com.srdc.isurf.gs1.ucc.ean.MeasurementValueType;
import tr.com.srdc.isurf.gs1.ucc.ean.MessageType;
import tr.com.srdc.isurf.gs1.ucc.ean.ObjectFactory;
import tr.com.srdc.isurf.gs1.ucc.ean.Partner;
import tr.com.srdc.isurf.gs1.ucc.ean.PartnerIdentification;
import tr.com.srdc.isurf.gs1.ucc.ean.PartyIdentificationType;
import tr.com.srdc.isurf.gs1.ucc.ean.QuantityType;
import tr.com.srdc.isurf.gs1.ucc.ean.Scope;
import tr.com.srdc.isurf.gs1.ucc.ean.StandardBusinessDocument;
import tr.com.srdc.isurf.gs1.ucc.ean.StandardBusinessDocumentHeader;
import tr.com.srdc.isurf.gs1.ucc.ean.StructureTypeListType;
import tr.com.srdc.isurf.gs1.ucc.ean.TradeItemIdentificationType;
import tr.com.srdc.isurf.gs1.ucc.ean.TransactionType;
import tr.com.srdc.isurf.gs1.ucc.ean.TransactionalItemDataType;
import tr.com.srdc.isurf.gs1.ucc.ean.UnitMeasurementType;
import tr.com.srdc.isurf.gs1.ucc.ean.VersionType;

public class InventoryActivityOrStatusMessageBuilder implements MessageBuilder {

    private String invActOrStatId;

    private ISURFInventoryActivityOrStatus invActOrStat;

    public ObjectFactory oFactory = null;

    public StandardBusinessDocumentHeader MBusinessDocumentHeader;

    public StandardBusinessDocument MBusinessDocument;

    public InventoryActivityOrStatusMessageBuilder(String invActOrStatId) {
        super();
        this.invActOrStatId = invActOrStatId;
    }

    @Override
    public int buildMessage() {
        final OBCriteria<ISURFInventoryActivityOrStatus> reCriteria = OBDal.getInstance().createCriteria(ISURFInventoryActivityOrStatus.class);
        reCriteria.add(Expression.eq(ISURFInventoryActivityOrStatus.PROPERTY_ID, invActOrStatId));
        final List<ISURFInventoryActivityOrStatus> flist = reCriteria.list();
        invActOrStat = flist.get(0);
        oFactory = new ObjectFactory();
        MBusinessDocumentHeader = oFactory.createStandardBusinessDocumentHeader();
        MBusinessDocument = oFactory.createStandardBusinessDocument();
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
                out = new File("OutputIA.xml");
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
        BusinessPartner bPartner = invActOrStat.getBusinessPartner();
        senderCI.setContactTypeIdentifier("Seller");
        receiverCI.setContactTypeIdentifier("Buyer");
        senderCI.setContact(OBuser.getFirstName() + " " + OBuser.getLastName());
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
        scope.setInstanceIdentifier(invActOrStat.getBusinessSession().getSessionIDNumber().toString());
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
        docIdentification.setInstanceIdentifier(invActOrStatId);
        docIdentification.setStandard("EAN.UCC");
        docIdentification.setType("InventoryActivityOrStatus");
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
        InventoryActivityOrInventoryStatusType MInventoryActOrStatType = oFactory.createInventoryActivityOrInventoryStatusType();
        GregorianCalendar cal = new GregorianCalendar();
        EntityIdentificationType eit = oFactory.createEntityIdentificationType();
        eit.setUniqueCreatorIdentification("MSG-IAOIS-" + invActOrStatId.substring(0, 5));
        if (invActOrStat.getOrganization().getOrganizationInformationList().size() == 0) {
            return ErrorTypes.ORGINFO_NOTFOUND;
        } else if (invActOrStat.getOrganization().getOrganizationInformationList().get(0).getGlobalLocationNo() == null || invActOrStat.getOrganization().getOrganizationInformationList().get(0).getGlobalLocationNo().equals("")) {
            return ErrorTypes.ORGINFO_GLN_NOTFOUND;
        } else {
            PartyIdentificationType contentOwner = oFactory.createPartyIdentificationType();
            contentOwner.setGln(invActOrStat.getOrganization().getOrganizationInformationList().get(0).getGlobalLocationNo());
            eit.setContentOwner(contentOwner);
        }
        MMessageType.setEntityIdentification(eit);
        MTransactionType.setEntityIdentification(eit);
        MDocumentCommandHeaderType.setEntityIdentification(eit);
        MDocumentCommandHeaderType.setType(DocumentCommandListType.ADD);
        VersionType vt = oFactory.createVersionType();
        vt.setVersionIdentification("2.0.2");
        MInventoryActOrStatType.setContentVersion(vt);
        cal.setTime(new Date());
        DatatypeFactory dtf = null;
        try {
            dtf = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        MInventoryActOrStatType.setCreationDateTime(dtf.newXMLGregorianCalendar(cal));
        MInventoryActOrStatType.setDocumentStatus(DocumentStatusListType.ORIGINAL);
        MInventoryActOrStatType.setDocumentStructureVersion(vt);
        cal.setTime(invActOrStat.getBeginDate());
        MInventoryActOrStatType.setBeginDateTime(dtf.newXMLGregorianCalendar(cal));
        MInventoryActOrStatType.setInventoryDocumentType(InventoryDocumentTypeListType.fromValue(invActOrStat.getDocumentType()));
        MInventoryActOrStatType.setStructureType(StructureTypeListType.fromValue(invActOrStat.getStructureType()));
        cal.setTime(invActOrStat.getEndingDate());
        MInventoryActOrStatType.setEndDateTime(dtf.newXMLGregorianCalendar(cal));
        PartyIdentificationType pitfrom = oFactory.createPartyIdentificationType();
        pitfrom.setGln(invActOrStat.getOrganization().getOrganizationInformationList().get(0).getGlobalLocationNo());
        MInventoryActOrStatType.setInventoryReportingParty(pitfrom);
        String BPartnerGLN = "";
        if (invActOrStat.getBusinessPartner().getBusinessPartnerLocationList().size() == 0) {
            return ErrorTypes.BPARTNERLOC_NOTFOUND;
        } else {
            int i;
            for (i = 0; i < invActOrStat.getBusinessPartner().getBusinessPartnerLocationList().size(); i++) {
                if (invActOrStat.getBusinessPartner().getBusinessPartnerLocationList().get(i).getGlobalLocationNo() != null && !invActOrStat.getBusinessPartner().getBusinessPartnerLocationList().get(i).getGlobalLocationNo().equals("")) {
                    BPartnerGLN = invActOrStat.getBusinessPartner().getBusinessPartnerLocationList().get(i).getGlobalLocationNo();
                    break;
                }
            }
            if (i == invActOrStat.getBusinessPartner().getBusinessPartnerLocationList().size()) {
                return ErrorTypes.BPARTNERLOC_GLN_NOTFOUND;
            }
        }
        PartyIdentificationType pitto = oFactory.createPartyIdentificationType();
        pitto.setGln(BPartnerGLN);
        MInventoryActOrStatType.setInventoryReportToParty(pitto);
        int iili;
        for (iili = 0; iili < invActOrStat.getISURFInventoryItemLocationInfoList().size(); iili++) {
            ISURFInventoryItemLocationInfo isurf_locinfo = invActOrStat.getISURFInventoryItemLocationInfoList().get(iili);
            InventoryItemLocationInformationType locinfoType = oFactory.createInventoryItemLocationInformationType();
            TradeItemIdentificationType tiit = oFactory.createTradeItemIdentificationType();
            if (isurf_locinfo.getProduct().getUPCEAN() == null || isurf_locinfo.getProduct().getUPCEAN().equals("")) return ErrorTypes.PRODUCT_GTIN_NOTFOUND;
            tiit.setGtin(isurf_locinfo.getProduct().getUPCEAN());
            locinfoType.setTradeItemIdentification(tiit);
            if (isurf_locinfo.getWarehouse().getGlobalLocationNo() == null || isurf_locinfo.getWarehouse().getGlobalLocationNo().equals("")) {
                return ErrorTypes.WAREHOUSE_GLN_NOTFOUND;
            }
            PartyIdentificationType pitProduct = oFactory.createPartyIdentificationType();
            pitProduct.setGln(isurf_locinfo.getWarehouse().getGlobalLocationNo());
            locinfoType.setInventoryLocation(pitProduct);
            if (isurf_locinfo.getActivityOrStatus().equals("A")) {
                int ali;
                for (ali = 0; ali < isurf_locinfo.getISURFInventoryActivityLineItemList().size(); ali++) {
                    ISURFInventoryActivityLineItem isurf_iali = isurf_locinfo.getISURFInventoryActivityLineItemList().get(ali);
                    InventoryActivityLineItemType ialiType = oFactory.createInventoryActivityLineItemType();
                    cal.setTime(isurf_iali.getBeginDate());
                    ialiType.setInventoryBeginDateTime(dtf.newXMLGregorianCalendar(cal));
                    cal.setTime(isurf_iali.getEndingDate());
                    ialiType.setInventoryEndDateTime(dtf.newXMLGregorianCalendar(cal));
                    ialiType.setNumber(new BigDecimal(ali + 1).toBigInteger());
                    PartyIdentificationType pitsub = oFactory.createPartyIdentificationType();
                    pitsub.setGln(isurf_locinfo.getWarehouse().getGlobalLocationNo());
                    ialiType.setInventorySublocation(pitsub);
                    int iaqs;
                    for (iaqs = 0; iaqs < isurf_iali.getISURFInventoryActivityQuantitySpecficationList().size(); iaqs++) {
                        ISURFInventoryActivityQuantitySpecfication isurf_iaqs = isurf_iali.getISURFInventoryActivityQuantitySpecficationList().get(iaqs);
                        InventoryActivityQuantitySpecificationType iaqsType = oFactory.createInventoryActivityQuantitySpecificationType();
                        iaqsType.setInventoryActivityType(InventoryActivityListType.fromValue(isurf_iaqs.getActivityType()));
                        QuantityType qt = oFactory.createQuantityType();
                        MeasurementUnitCodeType muct = oFactory.createMeasurementUnitCodeType();
                        muct.setMeasurementUnitCodeValue(isurf_iaqs.getQuantityUOM().getEDICode());
                        qt.setUnitOfMeasure(muct);
                        qt.setValue(isurf_iaqs.getQuantity().floatValue());
                        iaqsType.setQuantityOfUnits(qt);
                        iaqsType.setInventoryMovementIndicator(InventoryMovementTypeListType.fromValue(isurf_iaqs.getMovementType()));
                        int dr;
                        for (dr = 0; dr < isurf_iaqs.getISURFInventoryDeliverReferenceList().size(); dr++) {
                            ISURFInventoryDeliverReference isurf_dr = isurf_iaqs.getISURFInventoryDeliverReferenceList().get(dr);
                            DeliverReferenceType drt = oFactory.createDeliverReferenceType();
                            drt.setDeliverReferenceIdentification(isurf_dr.getReference());
                            drt.setDeliverReferenceType(DeliverReferenceCodeListType.fromValue(isurf_dr.getReferenceType()));
                            iaqsType.getDeliverReferenceNumber().add(drt);
                        }
                        int tid;
                        for (tid = 0; tid < isurf_iaqs.getISURFInventoryActivityTransactionalItemList().size(); tid++) {
                            ISURFInventoryActivityTransactionalItem isurf_iati = isurf_iaqs.getISURFInventoryActivityTransactionalItemList().get(tid);
                            TransactionalItemDataType tidType = oFactory.createTransactionalItemDataType();
                            cal.setTime(isurf_iati.getAvailableForSale());
                            tidType.setAvailableForSaleDate(dtf.newXMLGregorianCalendar(cal));
                            cal.setTime(isurf_iati.getBestBefore());
                            tidType.setBestBeforeDate(dtf.newXMLGregorianCalendar(cal));
                            ISO31661CodeType countryCode = oFactory.createISO31661CodeType();
                            countryCode.setCountryISOCode(isurf_iati.getCountryOfOrigin().getISOCountryCode());
                            tidType.setCountryOfOrigin(countryCode);
                            cal.setTime(isurf_iati.getExpiration());
                            tidType.setItemExpirationDate(dtf.newXMLGregorianCalendar(cal));
                            cal.setTime(isurf_iati.getPackaged());
                            tidType.setPackagingDate(dtf.newXMLGregorianCalendar(cal));
                            cal.setTime(isurf_iati.getProduction());
                            tidType.setProductionDate(dtf.newXMLGregorianCalendar(cal));
                            cal.setTime(isurf_iati.getSellBy());
                            tidType.setSellByDate(dtf.newXMLGregorianCalendar(cal));
                            tidType.setShelfLife("" + isurf_iati.getShelfLife());
                            QuantityType qt2 = oFactory.createQuantityType();
                            qt2.setValue(isurf_iati.getQuality().floatValue());
                            MeasurementUnitCodeType muct2 = oFactory.createMeasurementUnitCodeType();
                            muct2.setMeasurementUnitCodeValue(isurf_iati.getQualityUOM().getEDICode());
                            qt2.setUnitOfMeasure(muct2);
                            tidType.setProductQualityIndication(qt2);
                            int iw;
                            for (iw = 0; iw < isurf_iati.getISURFInventoryActivityTransactionalItemWeightList().size(); iw++) {
                                ISURFInventoryActivityTransactionalItemWeight isurf_iatiw = isurf_iati.getISURFInventoryActivityTransactionalItemWeightList().get(iw);
                                UnitMeasurementType umt = oFactory.createUnitMeasurementType();
                                umt.setMeasurementType(MeasurementTypeCodeListType.fromValue(isurf_iatiw.getMeasurementType()));
                                MeasurementValueType mvt = oFactory.createMeasurementValueType();
                                mvt.setUnitOfMeasure(isurf_iatiw.getUom().getEDICode());
                                mvt.setValue(isurf_iatiw.getWeight());
                                umt.setMeasurementValue(mvt);
                                tidType.getTransactionalItemWeight().add(umt);
                            }
                            iaqsType.getTransactionalItemData().add(tidType);
                        }
                        ialiType.getInventoryActivityQuantitySpecification().add(iaqsType);
                    }
                    if (iaqs == 0) return ErrorTypes.INVENTORY_ACTIVITYQUANTITIYSPEC_NOTFOUND;
                    locinfoType.getInventoryActivityLineItem().add(ialiType);
                }
                if (ali == 0) return ErrorTypes.INVENTORY_ACTIVITYLINEITEM_NOTFOUND;
            } else {
                int sli;
                for (sli = 0; sli < isurf_locinfo.getISURFInventoryStatusLineItemList().size(); sli++) {
                    ISURFInventoryStatusLineItem isurf_isli = isurf_locinfo.getISURFInventoryStatusLineItemList().get(sli);
                    InventoryStatusLineItemType isliType = oFactory.createInventoryStatusLineItemType();
                    cal.setTime(isurf_isli.getInventoryDate());
                    isliType.setInventoryDateTime(dtf.newXMLGregorianCalendar(cal));
                    isliType.setNumber(new BigDecimal(sli + 1).toBigInteger());
                    PartyIdentificationType pitsub = oFactory.createPartyIdentificationType();
                    pitsub.setGln(isurf_locinfo.getWarehouse().getGlobalLocationNo());
                    isliType.setInventorySublocation(pitsub);
                    int isqs;
                    for (isqs = 0; isqs < isurf_isli.getISURFInventoryStatusQuantitySpecificationList().size(); isqs++) {
                        ISURFInventoryStatusQuantitySpecification isurf_isqs = isurf_isli.getISURFInventoryStatusQuantitySpecificationList().get(isqs);
                        InventoryStatusQuantitySpecificationType isqsType = oFactory.createInventoryStatusQuantitySpecificationType();
                        isqsType.setInventoryStatusType(InventoryStatusListType.fromValue(isurf_isqs.getStatusType()));
                        QuantityType qt = oFactory.createQuantityType();
                        qt.setValue(isurf_isqs.getQuantity().floatValue());
                        MeasurementUnitCodeType muct = oFactory.createMeasurementUnitCodeType();
                        muct.setMeasurementUnitCodeValue(isurf_isqs.getQuantityUOM().getEDICode());
                        qt.setUnitOfMeasure(muct);
                        isqsType.setQuantityOfUnits(qt);
                        int tid;
                        for (tid = 0; tid < isurf_isqs.getISURFInventoryStatusTransactionalItemList().size(); tid++) {
                            ISURFInventoryStatusTransactionalItem isurf_isti = isurf_isqs.getISURFInventoryStatusTransactionalItemList().get(tid);
                            TransactionalItemDataType tidType = oFactory.createTransactionalItemDataType();
                            if (isurf_isti.getAvailableForSale() != null) {
                                cal.setTime(isurf_isti.getAvailableForSale());
                                tidType.setAvailableForSaleDate(dtf.newXMLGregorianCalendar(cal));
                            }
                            if (isurf_isti.getBestBefore() != null) {
                                cal.setTime(isurf_isti.getBestBefore());
                                tidType.setBestBeforeDate(dtf.newXMLGregorianCalendar(cal));
                            }
                            if (isurf_isti.getCountryOfOrigin() != null) {
                                ISO31661CodeType countryCode = oFactory.createISO31661CodeType();
                                countryCode.setCountryISOCode(isurf_isti.getCountryOfOrigin().getISOCountryCode());
                                tidType.setCountryOfOrigin(countryCode);
                            }
                            if (isurf_isti.getExpiration() != null) {
                                cal.setTime(isurf_isti.getExpiration());
                                tidType.setItemExpirationDate(dtf.newXMLGregorianCalendar(cal));
                            }
                            if (isurf_isti.getPackaged() != null) {
                                cal.setTime(isurf_isti.getPackaged());
                                tidType.setPackagingDate(dtf.newXMLGregorianCalendar(cal));
                            }
                            if (isurf_isti.getProduction() != null) {
                                cal.setTime(isurf_isti.getProduction());
                                tidType.setProductionDate(dtf.newXMLGregorianCalendar(cal));
                            }
                            if (isurf_isti.getSellBy() != null) {
                                cal.setTime(isurf_isti.getSellBy());
                                tidType.setSellByDate(dtf.newXMLGregorianCalendar(cal));
                            }
                            if (isurf_isti.getShelfLife() != null) {
                                tidType.setShelfLife("" + isurf_isti.getShelfLife());
                            }
                            if (isurf_isti.getQuality() != null && isurf_isti.getQualityUOM() != null) {
                                QuantityType qt2 = oFactory.createQuantityType();
                                qt2.setValue(isurf_isti.getQuality().floatValue());
                                MeasurementUnitCodeType muct2 = oFactory.createMeasurementUnitCodeType();
                                muct2.setMeasurementUnitCodeValue(isurf_isti.getQualityUOM().getEDICode());
                                qt2.setUnitOfMeasure(muct2);
                                tidType.setProductQualityIndication(qt2);
                            }
                            int iw;
                            for (iw = 0; iw < isurf_isti.getISURFInventoryStatusTransactionalItemWeightList().size(); iw++) {
                                ISURFInventoryStatusTransactionalItemWeight isurf_iatiw = isurf_isti.getISURFInventoryStatusTransactionalItemWeightList().get(iw);
                                UnitMeasurementType umt = oFactory.createUnitMeasurementType();
                                umt.setMeasurementType(MeasurementTypeCodeListType.fromValue(isurf_iatiw.getMeasurementType()));
                                MeasurementValueType mvt = oFactory.createMeasurementValueType();
                                mvt.setUnitOfMeasure(isurf_iatiw.getUom().getEDICode());
                                mvt.setValue(isurf_iatiw.getWeight());
                                umt.setMeasurementValue(mvt);
                                tidType.getTransactionalItemWeight().add(umt);
                            }
                            isqsType.getTransactionalItemData().add(tidType);
                        }
                        isliType.getInventoryStatusQuantitySpecification().add(isqsType);
                    }
                    if (isqs == 0) return ErrorTypes.INVENTORY_STATUSQUANTITYSPEC_NOTFOUND;
                    locinfoType.getInventoryStatusLineItem().add(isliType);
                }
                if (sli == 0) return ErrorTypes.INVENTORY_STATUSLINEITEM_NOTFOUND;
            }
            MInventoryActOrStatType.getInventoryItemLocationInformation().add(locinfoType);
        }
        if (iili == 0) {
            return ErrorTypes.INVENTORY_ITEMLOCINFO_NOTFOUND;
        }
        MDocumentCommandOperandType.getDocument().add(oFactory.createInventoryActivityOrInventoryStatus(MInventoryActOrStatType));
        MDocumentCommandType.setDocumentCommandHeader(MDocumentCommandHeaderType);
        MDocumentCommandType.setDocumentCommandOperand(MDocumentCommandOperandType);
        MCommandType.setCommand(oFactory.createDocumentCommand(MDocumentCommandType));
        MTransactionType.getCommand().add(MCommandType);
        MMessageType.getAny().add(oFactory.createTransaction(MTransactionType));
        MBusinessDocument.setStandardBusinessDocumentHeader(MBusinessDocumentHeader);
        MBusinessDocument.setAny(oFactory.createMessage(MMessageType));
        return 0;
    }

    @Override
    public int sendMessage(Document stNode) {
        System.setProperty("javax.xml.soap.MessageFactory", "weblogic.webservice.core.soap.MessageFactoryImpl");
        OBCriteria<ISURFWebService> cri = OBDal.getInstance().createCriteria(ISURFWebService.class);
        cri.add(Expression.eq(ISURFWebService.PROPERTY_BUSINESSPARTNER, invActOrStat.getBusinessPartner()));
        if (cri.list().size() == 0) return ErrorTypes.WEBSERVICE_DEF_NOT_FOUND;
        ISURFWebService webService = cri.list().get(0);
        ServiceFactory factory;
        try {
            factory = ServiceFactory.newInstance();
            String targetNamespace = webService.getNamespace();
            QName serviceName = new QName(targetNamespace, webService.getServiceName());
            QName portName = new QName(targetNamespace, webService.getPortName());
            String operationName = null;
            if (webService.getInventoryActivityOrInventoryStatusOperation() != null) operationName = webService.getInventoryActivityOrInventoryStatusOperation().getName(); else return ErrorTypes.WSDL_OPERATION_NOTFOUND;
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
