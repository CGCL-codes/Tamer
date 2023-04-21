package com.patientis.business.external;

import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.patientis.business.controllers.DefaultCustomController;
import com.patientis.client.common.PromptsController;
import com.patientis.client.security.login.LoginController;
import com.patientis.client.security.login.LoginModel;
import com.patientis.client.service.clinical.ClinicalService;
import com.patientis.client.service.system.SystemService;
import com.spirit.ehr.ws.state.State;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.io.FileUtils;
import com.patientis.data.common.ISParameter;
import com.patientis.ejb.common.IChainStore;
import com.patientis.framework.api.services.ClinicalServer;
import com.patientis.framework.api.services.PatientServer;
import com.patientis.framework.api.services.ReferenceServer;
import com.patientis.framework.controls.forms.ISFrame;
import com.patientis.framework.itext.PDFText;
import com.patientis.framework.itext.PDFUtility;
import com.patientis.framework.locale.CalendarUtility;
import com.patientis.framework.locale.SystemUtil;
import com.patientis.framework.logging.Log;
import com.patientis.framework.scripting.ServiceUtility;
import com.patientis.framework.utility.FileSystemUtil;
import com.patientis.framework.utility.ProcessUtil;
import com.patientis.framework.utility.Software;
import com.patientis.framework.utility.XsltUtil;
import com.patientis.model.clinical.FormModel;
import com.patientis.model.clinical.FormTypeModel;
import com.patientis.model.common.Converter;
import com.patientis.model.common.DateTimeModel;
import com.patientis.model.common.DisplayModel;
import com.patientis.model.common.ServiceCall;
import com.patientis.model.patient.PatientModel;
import com.patientis.model.reference.ContentTypeReference;
import com.spirit.ehr.ws.client.WsClientAdt;
import com.spirit.ehr.ws.client.WsClientLogin;
import com.spirit.ehr.ws.client.WsClientPdq;
import com.spirit.ehr.ws.client.WsClientXdsCons;
import com.spirit.ehr.ws.client.WsClientXdsSrc;
import com.spirit.ehr.ws.client.generated.DocumentClientDto;
import com.spirit.ehr.ws.client.generated.EhrAdtInfoRsp;
import com.spirit.ehr.ws.client.generated.EhrAdtPidReq;
import com.spirit.ehr.ws.client.generated.EhrException_Exception;
import com.spirit.ehr.ws.client.generated.EhrPIDClientDto;
import com.spirit.ehr.ws.client.generated.EhrPatIdRsp;
import com.spirit.ehr.ws.client.generated.EhrPatientClientDto;
import com.spirit.ehr.ws.client.generated.EhrPatientReq;
import com.spirit.ehr.ws.client.generated.EhrPatientRsp;
import com.spirit.ehr.ws.client.generated.EhrWsEmptyReq;
import com.spirit.ehr.ws.client.generated.EhrXdsIRetrReq;
import com.spirit.ehr.ws.client.generated.EhrXdsIRetrRsp;
import com.spirit.ehr.ws.client.generated.EhrXdsQReq;
import com.spirit.ehr.ws.client.generated.EhrXdsQRsp;
import com.spirit.ehr.ws.client.generated.EhrXdsRetrReq;
import com.spirit.ehr.ws.client.generated.EhrXdsRetrRsp;
import com.spirit.ehr.ws.client.generated.FolderClientDto;
import com.spirit.ehr.ws.client.generated.IheClassificationClientDto;
import com.spirit.ehr.ws.client.generated.OrganisationalRolesClientDto;
import com.spirit.ehr.ws.client.generated.PatientContentClientDto;
import com.spirit.ehr.ws.client.generated.SourceSubmissionClientDto;
import com.spirit.ehr.ws.client.generated.SpiritOrganisationClientDto;
import com.spirit.ehr.ws.client.generated.SpiritUserClientDto;
import com.spirit.ehr.ws.client.generated.SpiritUserRequest;
import com.spirit.ehr.ws.client.generated.SubmissionSetClientDto;
import com.spirit.ehr.ws.client.generated.XdsIClientDto;
import com.spirit.ehr.ws.client.generated.XdsIFrameClientDto;
import com.spirit.ehr.ws.client.generated.XdsIInstanceClientDto;
import com.spirit.ehr.ws.client.generated.XdsISeriesClientDto;
import com.spirit.ehr.ws.client.generated.XdsIStudyClientDto;
import com.spirit.ehr.ws.client.generated.XdsSrcSubmitReq;
import com.spirit.ehr.ws.client.generated.XdsSrcSubmitRsp;
import com.spirit.ehr.ws.client.generated.XdsSrcUploadReq;
import com.spirit.ehr.ws.client.generated.XdsSrcUploadRsp;

/**
 * @author gcaulton
 *
 */
public class CiscoMdes extends DefaultCustomController {

    public static String DEFAULT_ENDPOINT_URL = "http://office.tiani-spirit.com:28181";

    public static String MDES_USERNAME = "Physician1";

    public static String MDES_PASSWORD = "Physician1";

    public final int DOC_HANDLING_TYPE__XDSI = 2;

    private State state = null;

    public static final long ReferringDoctor = 4954286L;

    public static final long StandardSOAPPatientChiefComplaint = 40000282L;

    public static final long StandardProblemHistory = 40037040L;

    public static final long StandardReferralInvestigationResults = 40037041L;

    public static final long StandardReferralReason = 40037042L;

    public static final long StandardReferralUrgentIndicator = 40037044L;

    public static final long StandardReferralFollowupExpectation = 40037043L;

    private Map<String, Boolean> loaded = new Hashtable<String, Boolean>();

    /**
	 * 
	 */
    public CiscoMdes() throws Exception {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("pos.cisco.properties"));
            for (Object o : properties.keySet()) {
                String key = (String) o;
                if (Converter.isTrimmedSameIgnoreCase("DEFAULT_ENDPOINT_URL", key)) {
                    DEFAULT_ENDPOINT_URL = Converter.convertDisplayString(properties.getProperty(key));
                } else if (Converter.isTrimmedSameIgnoreCase("MDES_USERNAME", key)) {
                    MDES_USERNAME = Converter.convertDisplayString(properties.getProperty(key));
                } else if (Converter.isTrimmedSameIgnoreCase("MDES_PASSWORD", key)) {
                    MDES_PASSWORD = Converter.convertDisplayString(properties.getProperty(key));
                }
            }
        } catch (Exception ex) {
            Log.exception(ex);
        }
        Software.setClientRunning(true);
        LoginController.authenticateUser(new LoginModel("ADMIN", "admin"));
        System.out.println("cisco login " + DEFAULT_ENDPOINT_URL + " username " + MDES_USERNAME + "");
        state = new State(DEFAULT_ENDPOINT_URL);
        login(MDES_USERNAME, MDES_PASSWORD);
    }

    /**
	 * 
	 * @param args
	 */
    public static void main(String[] args) throws Exception {
        Software.setClientRunning(true);
        LoginController.authenticateUser(new LoginModel("ADMIN", "admin"));
        CiscoMdes cisco = new CiscoMdes();
        while (true) {
            List<Long> formIds = cisco.getReferralFormIds();
            for (Long formId : formIds) {
                try {
                    cisco.process(formId);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            Thread.sleep(1000);
            List<Long> visitIds = cisco.getTodaysVisitIds();
            for (Long visitId : visitIds) {
                try {
                    cisco.inbound(visitId);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            Thread.sleep(1000);
        }
    }

    /**
	 * 
	 * @param formid
	 * @throws Exception
	 */
    public void inbound(long visitId) throws Exception {
        PatientModel patient = PatientServer.getPatientForVisitId(visitId);
        if (patient.isNotNew()) {
            List<DocumentClientDto> documents = getPatientDocuments(patient);
            for (DocumentClientDto document : documents) {
                byte[] content = getDocumentFile(document);
                DateTimeModel created = new DateTimeModel(CalendarUtility.parse(document.getCreationTime(), "yyyyMMddHHmmss").getTimeInMillis());
                created = created.getAddHours(-4);
                if (created.after(DateTimeModel.getNow().getAddHours(-1))) {
                    String sql = "select form_id from forms where record_uuid = '" + document.getCreationTime() + "'";
                    List<Long> ids = ReferenceServer.sqlQuery(sql, ISParameter.createList());
                    if (ids.size() == 0) {
                        if (!loaded.containsKey(document.getCreationTime())) {
                            loaded.put(document.getCreationTime(), Boolean.TRUE);
                            System.out.println(patient.getLastName() + " " + created + " ");
                            File tempFile = SystemUtil.getTemporaryFile("xml");
                            FileSystemUtil.createBinaryFile(tempFile, content);
                            if (Converter.isTrimmedSameIgnoreCase(document.getMimeType(), "image/jpeg")) {
                                importImageDocument(tempFile, created, document, patient);
                            } else if (Converter.isTrimmedSameIgnoreCase(document.getMimeType(), "text/xml")) {
                                importCDADocument(tempFile, created, document, patient);
                            } else {
                                System.out.println("upload " + document.getMimeType());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
	 * 
	 * @param xmlFile
	 * @param created
	 * @param document
	 */
    public void importCDADocument(File xmlFile, DateTimeModel created, DocumentClientDto document, PatientModel patient) {
        try {
            File xsl = new File("C:\\temp\\test_files\\tiani_cda.xsl");
            File htmlFile = SystemUtil.getTemporaryFile("htm");
            XsltUtil.createTextFile(xmlFile, xsl, htmlFile);
            String textContents = FileSystemUtil.getTextContents(htmlFile);
            int startPos = textContents.indexOf("<body>");
            textContents = "<html>" + textContents.substring(startPos);
            textContents = textContents.replace("<left>", "");
            textContents = textContents.replace("</left>", "");
            FileSystemUtil.createFile(htmlFile.getAbsolutePath(), textContents);
            FileUtils.copyFile(htmlFile, new File("C:\\temp\\test_files\\cda.html"));
            long fileId = SystemService.storeFile(htmlFile.getAbsolutePath(), ContentTypeReference.TEXTHTML.getRefId());
            FormModel form = ClinicalServer.prepareNewForm(patient.getId(), patient.getVisitId(), 40036996L);
            form.setRecordUuid(document.getCreationTime());
            form.setFileId(fileId);
            System.out.println("uploading CDA " + patient.getId() + " " + patient.getLastName() + " visit id " + patient.getVisitId());
            long formId = ClinicalServer.store(patient.getId(), patient.getVisitId(), form);
            System.out.println("form id " + formId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
	 * 
	 * @param xmlFile
	 * @param created
	 * @param document
	 */
    public void importImageDocument(File imageFile, DateTimeModel created, DocumentClientDto document, PatientModel patient) {
        try {
            long fileId = SystemService.storeFile(imageFile.getAbsolutePath(), ContentTypeReference.IMAGEJPG.getRefId());
            FormModel form = ClinicalServer.prepareNewForm(patient.getId(), patient.getVisitId(), 40037053L);
            form.setFileId(fileId);
            form.setRecordUuid(document.getCreationTime());
            System.out.println("uploading image file " + patient.getId() + " " + patient.getLastName() + " visit id " + patient.getVisitId());
            long formId = ClinicalServer.store(patient.getId(), patient.getVisitId(), form);
            System.out.println("form id " + formId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
	 * 
	 * @param patient
	 * @param referralForm
	 * @throws Exception
	 */
    public List<DocumentClientDto> getPatientDocuments(PatientModel patient) throws Exception {
        List<DocumentClientDto> docs = new ArrayList<DocumentClientDto>();
        System.out.println("pulling documents for " + patient.getPatientName() + " " + patient.getMedicalRecordNumber());
        try {
            EhrPatientClientDto ehrPatient = getAddPatient(patient);
            PatientContentClientDto pc = getPatientContent(ehrPatient.getPid());
            docs.addAll(pc.getDocuments());
            List<FolderClientDto> fml = pc.getFolders();
            for (int a = 0; a < fml.size(); a++) {
                List<DocumentClientDto> folderDocs = fml.get(a).getDocuments();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return docs;
    }

    /**
	 * 
	 * @param formid
	 * @throws Exception
	 */
    public void process(long formId) throws Exception {
        FormModel form = ClinicalServer.getForm(formId);
        PatientModel patient = PatientServer.getPatientForVisitId(form.getVisitId());
        if (patient.isNotNew()) {
            referPatient(patient, form);
        } else if (form.getId() > 0L) {
            PatientModel p = PatientServer.getPatient(form.getPatientId());
            referPatient(p, form);
        }
    }

    /**
	 * 
	 * @return
	 */
    public List<Long> getTodaysVisitIds() {
        List<Long> visitIds = new ArrayList<Long>();
        try {
            String sql = "select v.visit_id from visits v where v.admit_dt > current_date and" + " v.admit_dt < current_date +1 and v.active_ind = 1 and not v.visit_status_ref_id = 1053502";
            List list = ReferenceServer.sqlQuery(sql, ISParameter.createList());
            for (Object o : list) {
                long visitId = Converter.convertLong(o);
                if (visitId > 0L) {
                    visitIds.add(visitId);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return visitIds;
    }

    /**
	 * 
	 * @return
	 */
    public List<Long> getReferralFormIds() {
        List<Long> formIds = new ArrayList<Long>();
        try {
            String sql = "select form_id from forms where form_type_ref_id = 40037046" + " and insert_dt > current_date-1 and form_status_ref_id = 1061498 and active_ind =1" + " and system_sequence = 0";
            List list = ReferenceServer.sqlQuery(sql, ISParameter.createList());
            for (Object o : list) {
                long formId = Converter.convertLong(o);
                if (formId > 0L) {
                    formIds.add(formId);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return formIds;
    }

    /**
	 * @see com.patientis.business.controllers.DefaultCustomController#serverProcessPostSave(com.patientis.model.clinical.FormModel, com.patientis.model.clinical.FormTypeModel, com.patientis.ejb.common.IChainStore, com.patientis.model.common.ServiceCall)
	 */
    @Override
    public void serverProcessPostSave(FormModel form, FormTypeModel formType, IChainStore chain, ServiceCall call) throws Exception {
        System.out.println("cisco mdes");
        PatientModel patient = PatientServer.getPatientForVisitId(form.getVisitId());
        if (patient.isNotNew()) {
            referPatient(patient, form);
        } else if (form.getId() > 0L) {
            PatientModel p = PatientServer.getPatient(form.getPatientId());
            referPatient(p, form);
        }
    }

    /**
	 * 
	 * @param patient
	 * @param referralForm
	 * @throws Exception
	 */
    public void referXMLPatient(PatientModel patient, FormModel referralForm) throws Exception {
        System.out.println("getting referral file");
        try {
            EhrPatientClientDto ehrPatient = getAddPatient(patient);
            String filename = "C:\\temp\\test_files\\tiani_referral.xml";
            String ccr = FileSystemUtil.getTextContents(new File(filename));
            ccr = Converter.convertTemplate("patient", patient, ccr);
            File tempFile = SystemUtil.getTemporaryFile();
            FileSystemUtil.createFile(tempFile.getAbsolutePath(), ccr);
            submitReferral(FileSystemUtil.getBinaryContents(tempFile), filename, "text/xml", ehrPatient);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("updated form id " + referralForm.getId() + " " + patient.getPatientName() + " " + patient.getMedicalRecordNumber());
    }

    /**
	 * 
	 * @param patient
	 * @param referralForm
	 * @throws Exception
	 */
    public void referPatient(PatientModel patient, FormModel referralForm) throws Exception {
        System.out.println("getting referral file");
        try {
            EhrPatientClientDto ehrPatient = getAddPatient(patient);
            File templatePDFFile = new File("C:\\temp\\test_files\\tiani_referral.pdf");
            File pdfFile = SystemUtil.getTemporaryFile("pdf");
            List<PDFText> content = getReferralContents(patient, referralForm);
            int documentwidth = (int) PageSize.A4.getWidth();
            int documentheight = (int) PageSize.A4.getHeight();
            PDFUtility.appendPdfFile(templatePDFFile, pdfFile, content, documentwidth, documentheight);
            submitReferral(FileSystemUtil.getBinaryContents(pdfFile), "referral.pdf", "application/pdf", ehrPatient);
            referralForm.setSystemSequence(1L);
            ClinicalService.store(referralForm);
        } catch (Exception ex) {
            ex.printStackTrace();
            referralForm.setSystemSequence(-1L);
            ClinicalService.store(referralForm);
            Log.exception(ex);
        }
        System.out.println("updated form id " + referralForm.getId());
        referXMLPatient(patient, referralForm);
    }

    /**
	 * 
	 * @return
	 */
    public List<PDFText> getReferralContents(PatientModel patient, FormModel referralForm) {
        List<PDFText> contents = new ArrayList<PDFText>();
        int documentheight = (int) PageSize.A4.getHeight();
        List<String> lines = new ArrayList<String>();
        lines.add(patient.getFirstName() + " " + patient.getLastName() + ", " + patient.getGenderRef().getDisplay() + " " + patient.getAge() + ".");
        lines.add("100 Avenue Road, Walpole, MA 02032");
        lines.add("");
        for (long recordItemRefId : new Long[] { StandardSOAPPatientChiefComplaint, StandardSOAPPatientChiefComplaint, StandardProblemHistory, StandardReferralInvestigationResults, StandardReferralReason }) {
            List<String> textLines = Converter.convertStringLines(referralForm.getStringValueForRecordItem(recordItemRefId), false);
            for (String text : textLines) {
                lines.add(text);
            }
            lines.add("");
        }
        int height = 300;
        for (String s : lines) {
            PDFText text = new PDFText(s, 75, documentheight - height, 12, BaseFont.TIMES_ROMAN);
            contents.add(text);
            height += 12;
        }
        return contents;
    }

    /**
	 * 
	 * @param patient
	 * @throws Exception
	 */
    public EhrPatientClientDto getAddPatient(PatientModel patient) throws Exception {
        EhrPIDClientDto pidDto = new EhrPIDClientDto();
        pidDto.setPatientID(patient.getMedicalRecordNumber());
        List<EhrPatientClientDto> resultPats = patientSearch(null, null, pidDto);
        EhrPatientClientDto patAfterADT = null;
        if (resultPats.size() == 0) {
            System.out.println("adding patient " + patient.getLastName() + ", " + patient.getFirstName() + " " + patient.getMedicalRecordNumber());
            List<EhrPatientClientDto> insPat = addPatient(patient);
            patAfterADT = insPat.get(0);
        } else {
            System.out.println("found patient " + patient.getLastName() + ", " + patient.getFirstName() + " " + patient.getMedicalRecordNumber());
            patAfterADT = resultPats.get(0);
        }
        return patAfterADT;
    }

    /**
	 * 
	 * @param usrName
	 * @param pwd
	 * @throws Exception
	 */
    public void login(String usrName, String pwd) throws Exception {
        WsClientLogin cLogin = new WsClientLogin(state);
        SpiritUserClientDto usr = new SpiritUserClientDto();
        usr.getUid().add(usrName);
        usr.getUserPassword().add(pwd);
        SpiritUserRequest loginRq = new SpiritUserRequest();
        loginRq.setUser(usr);
        cLogin.usrLogin(loginRq);
        usr = state.getUsr();
        OrganisationalRolesClientDto orgRole0 = usr.getOrganisationalRoles().get(0);
        SpiritOrganisationClientDto loginOrg = new SpiritOrganisationClientDto();
        loginOrg.setOrganisationDN(orgRole0.getOrganisationDN());
        loginOrg.setOrganisationName(orgRole0.getOrganisationName());
        usr.setLoginOrganisation(loginOrg);
        String loginRole = orgRole0.getModuleRoles().get(0).getRoles().get(0);
        usr.setLoginRole(loginRole);
        loginRq = new SpiritUserRequest();
        loginRq.setUser(usr);
        cLogin.loginOrganisationRole(loginRq);
    }

    /**
	 * 
	 * @param famName
	 * @param givenName
	 * @param pid
	 * @return
	 * @throws MalformedURLException
	 * @throws EhrException_Exception
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
    public List<EhrPatientClientDto> patientSearch(String famName, String givenName, EhrPIDClientDto pid) throws MalformedURLException, EhrException_Exception, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        EhrPatientClientDto patFilter = new EhrPatientClientDto();
        if (famName != null) patFilter.setFamilyName(famName);
        if (givenName != null) patFilter.setGivenName(givenName);
        if (pid != null) {
            patFilter.getPid().add(pid);
        }
        EhrPatientReq pdqRq = new EhrPatientReq();
        pdqRq.setRequestData(patFilter);
        WsClientPdq cPdq = new WsClientPdq(state);
        EhrPatientRsp rsp = cPdq.queryForPatients(pdqRq);
        return rsp.getResponseData();
    }

    /**
	 * 
	 * @param patient
	 * @return
	 * @throws DatatypeConfigurationException
	 * @throws EhrException_Exception
	 * @throws MalformedURLException
	 */
    public List<EhrPatientClientDto> addPatient(PatientModel patient) throws DatatypeConfigurationException, EhrException_Exception, MalformedURLException {
        EhrPatientClientDto patToInsert = new EhrPatientClientDto();
        patToInsert.setFamilyName(patient.getLastName());
        patToInsert.setGivenName(patient.getFirstName());
        XMLGregorianCalendar bd = DatatypeFactory.newInstance().newXMLGregorianCalendar(patient.getBirthDt());
        patToInsert.setBirthdate(bd);
        patToInsert.setAccountNumber(patient.getMedicalRecordNumber());
        WsClientAdt cAdt = new WsClientAdt(state);
        EhrAdtInfoRsp infoRsp = cAdt.getAdtInfo(new EhrWsEmptyReq());
        String locWorkDom = infoRsp.getLocWorkDom().getCode();
        EhrAdtPidReq pidRq = new EhrAdtPidReq();
        pidRq.getPidReqDoms().add(locWorkDom);
        EhrPatIdRsp pidRsp = cAdt.createPID(pidRq);
        pidRsp.getResponseData().get(0).setPatientID(patient.getMedicalRecordNumber());
        List<EhrPIDClientDto> creaPids = pidRsp.getResponseData();
        Iterator<EhrPIDClientDto> it = creaPids.iterator();
        while (it.hasNext()) {
            EhrPIDClientDto creaPid = it.next();
            if (creaPid.getPatientID() == null || creaPid.getPatientID().trim().length() == 0) {
                creaPid.setPatientID(patient.getMedicalRecordNumber());
            }
        }
        patToInsert.getPid().addAll(creaPids);
        EhrPatientReq insertRq = new EhrPatientReq();
        insertRq.setRequestData(patToInsert);
        EhrPatientRsp insertRsp = cAdt.insertPatient(insertRq);
        return insertRsp.getResponseData();
    }

    /**
	 * 
	 * @param absoulteFileName
	 * @param patFromInsert
	 * @return
	 * @throws java.lang.Exception
	 */
    public EhrPatientClientDto submitReferral(byte[] docBytes, String filename, String mimeType, EhrPatientClientDto patFromInsert) throws java.lang.Exception {
        System.out.println("submitting referral");
        WsClientXdsSrc c = new WsClientXdsSrc(state);
        XdsSrcUploadReq uplRq = new XdsSrcUploadReq();
        uplRq.setDocument(docBytes);
        XdsSrcUploadRsp uplRsp = c.uploadFile(uplRq);
        String fileID = uplRsp.getFileID();
        DocumentClientDto doc = createDocument(fileID, filename, mimeType);
        SourceSubmissionClientDto subm = new SourceSubmissionClientDto();
        SubmissionSetClientDto subSet = new SubmissionSetClientDto();
        subm.setSubmissionSet(subSet);
        subSet.getAuthorPerson().add("subm.authorPerson");
        subSet.setDescription("subm.description");
        IheClassificationClientDto contentTypeCode = new IheClassificationClientDto();
        contentTypeCode.setNodeRepresentation("Communication");
        contentTypeCode.setValue("Communication");
        contentTypeCode.getSchema().add("Connect-a-thon contentTypeCodes");
        subSet.setContentTypeCode(contentTypeCode);
        FolderClientDto folder = new FolderClientDto();
        subm.setFolder(folder);
        folder.setDescription("folderDesc");
        folder.setName("folderName");
        folder.setCreateFolder(true);
        IheClassificationClientDto folderCode1 = new IheClassificationClientDto();
        folder.getCodeList().add(folderCode1);
        if (doc.getMimeType().equals("application/pdf")) {
            folderCode1.setNodeRepresentation("34140-4");
            folderCode1.setValue("Referral Notes");
            folderCode1.getSchema().add("LOINC");
        } else if (doc.getMimeType().equals("image/jpeg")) {
            folderCode1.setNodeRepresentation("34140-4");
            folderCode1.setValue("Referral Notes");
            folderCode1.getSchema().add("LOINC");
        } else if (doc.getMimeType().equals("text/xml")) {
            folderCode1.setNodeRepresentation("51900-9");
            folderCode1.setValue("Continuity of Care");
            folderCode1.getSchema().add("LOINC");
        } else {
            throw new Exception("invalid mime type " + doc.getMimeType());
        }
        subm.getDocuments().add(doc);
        XdsSrcSubmitReq subRq = new XdsSrcSubmitReq();
        subRq.setPatient(patFromInsert);
        subRq.setSrcSubmission(subm);
        XdsSrcSubmitRsp xdsSubRsp = c.submit(subRq);
        return xdsSubRsp.getResponseData();
    }

    /**
	 * 
	 * @param fileID
	 * @return
	 */
    public DocumentClientDto createDocument(String fileID, String filename, String mimeType) throws Exception {
        DocumentClientDto doc = new DocumentClientDto();
        doc.setLoaderUri(fileID);
        doc.setParentDocumentId(null);
        doc.setParentDocumentRelationShip(null);
        ArrayList<String> authorPerson = new ArrayList<String>();
        authorPerson.add("doc.authorPerson");
        doc.getAuthorPerson().add("doc.authorPerson");
        doc.getLegalAuthenticator().add("doc.authorPerson");
        doc.setLanguageCode("DE-TI");
        doc.setDescription("doc.Description");
        doc.setName(filename);
        doc.setMimeType(mimeType);
        if (doc.getMimeType().equals("application/pdf")) {
            IheClassificationClientDto formatCode = new IheClassificationClientDto();
            formatCode.setNodeRepresentation("PDF/IHE 1.x");
            formatCode.setValue("PDF/IHE 1.x");
            formatCode.getSchema().add("Connect-a-thon formatCodes");
            doc.setFormatCode(formatCode);
        } else if (doc.getMimeType().equals("image/jpeg")) {
            IheClassificationClientDto formatCode = new IheClassificationClientDto();
            formatCode.setNodeRepresentation("Generic Image");
            formatCode.setValue("Generic Image");
            formatCode.getSchema().add("Connect-a-thon formatCodes");
            doc.setFormatCode(formatCode);
        } else if (doc.getMimeType().equals("text/xml")) {
            IheClassificationClientDto formatCode = new IheClassificationClientDto();
            formatCode.setNodeRepresentation("CCR V1.0");
            formatCode.setValue("CCR V1.0");
            formatCode.getSchema().add("Connect-a-thon formatCodes");
            doc.setFormatCode(formatCode);
        } else {
            throw new Exception("invalid mime type " + doc.getMimeType());
        }
        IheClassificationClientDto classCode = new IheClassificationClientDto();
        classCode.setNodeRepresentation("Consent");
        classCode.setValue("Consent");
        classCode.getSchema().add("Connect-a-thon classCodes");
        doc.setClassCode(classCode);
        IheClassificationClientDto typeCode = new IheClassificationClientDto();
        typeCode.setNodeRepresentation("34140-4");
        typeCode.setValue("Referral Notes");
        typeCode.getSchema().add("LOINC");
        doc.setTypeCode(typeCode);
        IheClassificationClientDto confidentialityCode = new IheClassificationClientDto();
        confidentialityCode.setNodeRepresentation("C");
        confidentialityCode.setValue("Celebrity");
        confidentialityCode.getSchema().add("Connect-a-thon confidentialityCodes");
        doc.setConfidentialityCode(confidentialityCode);
        IheClassificationClientDto healthcareFacilityCode = new IheClassificationClientDto();
        healthcareFacilityCode.setNodeRepresentation("C");
        healthcareFacilityCode.setValue("Celebrity");
        healthcareFacilityCode.getSchema().add("Connect-a-thon confidentialityCodes");
        doc.setHealthcareFacilityCode(healthcareFacilityCode);
        IheClassificationClientDto practiceSettingCode = new IheClassificationClientDto();
        practiceSettingCode.setNodeRepresentation("Endocrinology");
        practiceSettingCode.setValue("Endocrinology");
        practiceSettingCode.getSchema().add("Connect-a-thon practiceSettingCodes");
        doc.setPracticeSettingCode(practiceSettingCode);
        return doc;
    }

    /**
	 * 
	 * @param pids
	 * @return
	 * @throws MalformedURLException
	 * @throws EhrException_Exception
	 */
    public PatientContentClientDto getPatientContent(List<EhrPIDClientDto> pids) throws MalformedURLException, EhrException_Exception {
        EhrXdsQReq xdsqRq = new EhrXdsQReq();
        xdsqRq.getRequestData().addAll(pids);
        WsClientXdsCons cXdsCons = new WsClientXdsCons(state);
        EhrXdsQRsp xdsqRsp = cXdsCons.queryForDocuments(xdsqRq);
        return xdsqRsp.getResponseData();
    }

    /**
	 * 
	 * @param path_to_file
	 * @param doc
	 * @return
	 * @throws FileNotFoundException
	 * @throws EhrException_Exception
	 * @throws IOException
	 * @throws Exception
	 */
    public byte[] getDocumentFile(DocumentClientDto doc) throws FileNotFoundException, EhrException_Exception, IOException, Exception {
        if (doc.getDocHandlingType() == DOC_HANDLING_TYPE__XDSI) {
            List<File> files = getXDSIDocument(doc);
            return FileSystemUtil.getBinaryContents(files.get(0));
        } else {
            EhrXdsRetrReq xdsrRq = new EhrXdsRetrReq();
            xdsrRq.setRequestData(doc);
            WsClientXdsCons cXdsCons = new WsClientXdsCons(state);
            EhrXdsRetrRsp xdsrRsp = cXdsCons.retrieveDocument(xdsrRq);
            return xdsrRsp.getDocument();
        }
    }

    public ArrayList<File> getXDSIDocument(DocumentClientDto doc) throws java.lang.Exception, EhrException_Exception, IOException {
        System.out.println("SpiritEhrWSClient.main ->  start to retrieve XDSI doc :" + doc.getUUID());
        if (doc.getDocHandlingType() != DOC_HANDLING_TYPE__XDSI) {
            throw new java.lang.Exception("DOC_IS_NO_KOS");
        }
        ArrayList<File> wadoFiles = new ArrayList<File>();
        EhrXdsRetrReq xdsrRq = new EhrXdsRetrReq();
        xdsrRq.setRequestData(doc);
        WsClientXdsCons cXdsCons = new WsClientXdsCons(state);
        EhrXdsIRetrRsp kosRsp = cXdsCons.retrieveXdsIData(xdsrRq);
        XdsIClientDto kosDto = kosRsp.getResponseData();
        List<XdsIStudyClientDto> stys = kosDto.getStudies();
        for (Iterator<XdsIStudyClientDto> itSty = stys.iterator(); itSty.hasNext(); ) {
            XdsIStudyClientDto sty = itSty.next();
            List<XdsISeriesClientDto> series = sty.getSeries();
            for (Iterator<XdsISeriesClientDto> itSer = series.iterator(); itSer.hasNext(); ) {
                XdsISeriesClientDto serie = itSer.next();
                List<XdsIInstanceClientDto> instances = serie.getInstances();
                for (Iterator<XdsIInstanceClientDto> itInst = instances.iterator(); itInst.hasNext(); ) {
                    XdsIInstanceClientDto inst = itInst.next();
                    List<XdsIFrameClientDto> frames = inst.getFrames();
                    int frameCnt = 0;
                    for (Iterator<XdsIFrameClientDto> itFrames = frames.iterator(); itFrames.hasNext(); ) {
                        XdsIFrameClientDto frame = itFrames.next();
                        EhrXdsIRetrReq iRq = new EhrXdsIRetrReq();
                        iRq.setRequestData(frame);
                        frame.setColumns(256);
                        frame.setRows(256);
                        EhrXdsIRetrRsp iRsp = cXdsCons.retrieveWadoImg(iRq);
                        File tempFile = SystemUtil.getTemporaryFile();
                        FileSystemUtil.createBinaryFile(tempFile, iRsp.getWadoImage());
                        wadoFiles.add(tempFile);
                        frameCnt++;
                    }
                }
            }
        }
        return wadoFiles;
    }
}
