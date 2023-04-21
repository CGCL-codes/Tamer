package org.tolven.nuh.process;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathVariableResolver;
import org.apache.log4j.Logger;
import org.drools.StatefulSession;
import org.tolven.app.AppEvalAdaptor;
import org.tolven.app.el.TrimExpressionEvaluator;
import org.tolven.app.entity.MenuData;
import org.tolven.app.entity.MenuStructure;
import org.tolven.doc.DocumentLocal;
import org.tolven.doc.bean.TolvenMessage;
import org.tolven.doc.bean.TolvenMessageWithAttachments;
import org.tolven.doc.entity.DocBase;
import org.tolven.doc.entity.DocXML;
import org.tolven.doctype.DocumentType;
import org.tolven.el.ExpressionEvaluator;
import org.tolven.nuh.api.ProcessNUHLocal;
import org.tolven.security.key.DocumentSecretKey;
import org.tolven.trim.CE;
import org.tolven.trim.NullFlavor;
import org.tolven.trim.NullFlavorType;
import org.tolven.trim.ex.TrimFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Process CDA R2 messages. Unlike Trim-based messages which define binding requirements in the message itself, CDA messages
 * are bound to placeholders in a "pull mode", that is, from the outside. For example, the patient ID is assumed to exist in the
 * message and will be extracted by this module and then posted
 * @author John Churin
 *
 */
@Stateless
@Local(ProcessNUHLocal.class)
public class ProcessNUH extends AppEvalAdaptor {

    @EJB
    DocumentLocal documentBean;

    private static final String NUHns = "nuh";

    private ExpressionEvaluator cdaee;

    private Logger logger = Logger.getLogger(this.getClass());

    private XPath xpath = XPathFactory.newInstance().newXPath();

    private Variables variables = new Variables();

    private static final TrimFactory trimFactory = new TrimFactory();

    private final XPathExpression patientPath = path("/nuh/nuh_pat_demo_1/patient");

    private final XPathExpression inpMedPath = path("/nuh/nug_pat_prescriptions/nuh_pat_inp_med[hrn_n=$hrn]");

    private final XPathExpression inpMedDetPath = path("/nuh/nug_pat_prescribed_medications/nuh_pat_inp_med_det[hrn_n=$hrn]");

    private final XPathExpression medCertPath = path("/nuh/nuh_med_cert/nuh_pat_med_cert[hrn_n=$hrn]");

    private final XPathExpression xrayPath = path("/nuh/nuh_xray/nuh_pat_x005F_xray[hrn_n=$hrn and chk_in_id_c=$orderid]");

    private final XPathExpression patRecPath = path("pat_rec_n");

    private final XPathExpression hrnPath = path("hrn_n");

    private final XPathExpression acctPath = path("acct_n");

    private final XPathExpression seqPath = path("seq_n");

    private final XPathExpression setIdPath = path("set_id");

    private final XPathExpression checkinIdPath = path("chk_in_id_c");

    private final XPathExpression mcrPath = path("mcr_n");

    private final XPathExpression itemPath = path("item_c");

    private final XPathExpression diagCodePath = path("diag_c");

    private final XPathExpression diagNamePath = path("diag_m");

    private final XPathExpression dischDatePath = path("disch_d");

    private final XPathExpression examPath = path("exam_c");

    private final XPathExpression statusPath = path("status_c");

    private final XPathExpression priorityPath = path("priority_c");

    private final XPathExpression filmCountPath = path("no_films_n");

    private final XPathExpression radiologistPath = path("radiolg_c");

    private final XPathExpression radiologist2Path = path("radiolg2_c");

    private final XPathExpression radiographerPath = path("radiogr_c");

    private final XPathExpression patLocationPath = path("PatLocation");

    private final XPathExpression ludatePath = path("ludate_d");

    private final XPathExpression cancelDatePath = path("proc_cancel_d");

    static class Variables extends HashMap<QName, String> implements XPathVariableResolver {

        @Override
        public Object resolveVariable(QName arg0) {
            return get(arg0);
        }
    }

    protected Document document;

    protected StatefulSession workingMemory;

    protected ExpressionEvaluator ee;

    /**
	 * Main entry point for processing a message. Parse the document, in this case, as a DOM though in
	 * other cases, we use JAXB.
	 */
    @Override
    protected void loadWorkingMemory(StatefulSession workingMemory) throws Exception {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            xpath.setXPathVariableResolver(variables);
            InputStream bis = new ByteArrayInputStream(tm.getPayload());
            document = builder.parse(bis);
            this.workingMemory = workingMemory;
            bis.close();
            ee = getExpressionEvaluator();
            bindToPatients();
        } catch (Exception e) {
            throw new RuntimeException("Unable to process NUH document", e);
        }
    }

    /**
	 * Find all patients in the supplied document and process them one at a time.
	 * @param workingMemory
	 */
    protected void bindToPatients() {
        try {
            NodeList patientList = (NodeList) patientPath.evaluate(document, XPathConstants.NODESET);
            for (int i = 0; i < patientList.getLength(); i++) {
                MenuData mdPatient = bindToPatient(patientList.item(i));
                bindToEncounters(mdPatient);
                bindToXRays(mdPatient);
                bindToLabs(mdPatient);
                bindToMeds(mdPatient);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error binding to patients", e);
        }
    }

    /**
	 * Process one Patient's data
	 * @return Patient MenuData
	 */
    protected MenuData bindToPatient(Node patient) {
        try {
            MenuStructure msPatient = menuBean.findMenuStructure(getAccount(), ":patient");
            String hrn = hrnPath.evaluate(patient);
            variables.put(new QName("hrn"), hrn);
            System.out.println(patient.getNodeName() + " mrn: " + hrn);
            MenuData mdPatient = null;
            List<MenuData> mdPatients = menuBean.findMenuDataById(getAccount(), "hrn_n", hrn);
            if (mdPatients.size() > 0) {
                mdPatient = mdPatients.get(0);
            } else {
                mdPatient = new MenuData();
                mdPatient.setMenuStructure(msPatient.getAccountMenuStructure());
                mdPatient.setAccount(getAccount());
                mdPatient.addPlaceholderID("hrn_n", hrn, "nuh");
            }
            List<Node> documentNodes = new ArrayList<Node>();
            documentNodes.add(patient);
            createDocument(mdPatient);
            populateDocument(mdPatient, documentNodes);
            ee.pushContext();
            XPathMap xpathMap = new XPathMap(patient);
            ee.addVariable("nuh", xpathMap);
            menuBean.populateMenuData(ee, mdPatient);
            Date dob = (Date) mdPatient.getField("dob");
            if (dob != null && dob.after(getNow())) {
                Calendar dobc = new GregorianCalendar();
                dobc.setTime(dob);
                dobc.add(Calendar.YEAR, -100);
                mdPatient.setField("dob", dobc.getTime());
            }
            StringBuffer homeAddr2 = new StringBuffer();
            homeAddr2.append(mdPatient.getField("homeAddr2"));
            if (homeAddr2.length() > 0) {
                homeAddr2.append(" ");
            }
            homeAddr2.append(xpath.evaluate("blk_n", patient));
            if (homeAddr2.length() > 0) {
                homeAddr2.append(" ");
            }
            homeAddr2.append(xpath.evaluate("level_n", patient));
            if (homeAddr2.length() > 0) {
                homeAddr2.append(" ");
            }
            homeAddr2.append(xpath.evaluate("unit_n", patient));
            mdPatient.setField("homeAddr2", homeAddr2.toString());
            String vip = xpath.evaluate("vip_d", patient);
            if ("YES".equalsIgnoreCase(vip)) {
                CE vipCE = trimFactory.createCE();
                vipCE.setCodeSystem("2.16.840.1.113883.5.1075");
                vipCE.setCodeSystemName("HL7");
                vipCE.setCode("VIP");
                mdPatient.setField("vip", vipCE);
            } else {
                NullFlavor nf = trimFactory.createNullFlavor();
                nf.setType(NullFlavorType.NI);
                mdPatient.setField("vip", nf);
            }
            ee.popContext();
            persistMenuData(mdPatient);
            workingMemory.insert(mdPatient);
            return mdPatient;
        } catch (Exception e) {
            throw new RuntimeException("Unable to setup patient in account " + getAccount().getId(), e);
        }
    }

    /**
	 * Bind to encounters for one patient
	 * @param document
	 * @param workingMemory
	 */
    protected void bindToEncounters(MenuData mdPatient) {
        try {
            NodeList visitList = (NodeList) xpath.evaluate("/nuh/nuh_visit_info/nuh_pat_visit_info[hrn_n=$hrn]", document, XPathConstants.NODESET);
            for (int i = 0; i < visitList.getLength(); i++) {
                bindToEncounter(visitList.item(i), mdPatient);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error binding to encounters", e);
        }
    }

    /**
	 * Process one encounter for a patient
	 * @return Encounter MenuData
	 */
    protected MenuData bindToEncounter(Node visit, MenuData mdPatient) {
        try {
            MenuStructure msEncounter = menuBean.findMenuStructure(getAccount(), ":patient:encounter");
            String acct = acctPath.evaluate(visit);
            variables.put(new QName("acct"), acct);
            System.out.println(visit.getNodeName() + " acct: " + acct);
            String dischargeDate = dischDatePath.evaluate(visit);
            MenuData mdEncounter = null;
            List<MenuData> mdEncounters = menuBean.findMenuDataById(getAccount(), "acct_n", acct);
            if (mdEncounters.size() > 0) {
                mdEncounter = mdEncounters.get(0);
            } else {
                mdEncounter = new MenuData();
                mdEncounter.setMenuStructure(msEncounter.getAccountMenuStructure());
                mdEncounter.setAccount(getAccount());
                mdEncounter.setParent01(mdPatient);
                mdEncounter.addPlaceholderID("acct_n", acct, "nuh");
            }
            if (dischargeDate != null && dischargeDate.length() > 0) {
                mdEncounter.setActStatus("completed");
            } else {
                mdEncounter.setActStatus("active");
            }
            ee.pushContext();
            XPathMap xpathMap = new XPathMap(visit);
            ee.addVariable("nuh", xpathMap);
            menuBean.populateMenuData(ee, mdEncounter);
            ee.popContext();
            persistMenuData(mdEncounter);
            NodeList admittingList = (NodeList) xpath.evaluate("/nuh/nuh_visit_doc/nuh_pat_visit_doc[hrn_an_field=$hrn and acct_n=$acct and dr_type_c='A']", document, XPathConstants.NODESET);
            if (admittingList.getLength() > 0) {
                Node admitter = admittingList.item(0);
                String admitterName = xpath.evaluate("dr_m", admitter);
                mdEncounter.setField("otherAdmitter", admitterName);
            }
            NodeList attendingList = (NodeList) xpath.evaluate("/nuh/nuh_visit_doc/nuh_pat_visit_doc[hrn_an_field=$hrn and acct_n=$acct and dr_type_c='T']", document, XPathConstants.NODESET);
            if (attendingList.getLength() > 0) {
                Node attender = attendingList.item(0);
                String attenderName = xpath.evaluate("dr_m", attender);
                mdEncounter.setField("otherAttender", attenderName);
            }
            String loc = xpath.evaluate("loc_c", visit);
            String room = xpath.evaluate("room_n", visit);
            String bed = xpath.evaluate("bed_n", visit);
            mdEncounter.setField("otherLocation", loc + " " + room + " " + bed);
            NodeList visitDocList = (NodeList) xpath.evaluate("/nuh/nuh_visit_doc/nuh_pat_visit_doc[hrn_an_field=$hrn and acct_n=$acct]", document, XPathConstants.NODESET);
            NodeList visitDiagList = (NodeList) xpath.evaluate("/nuh/nuh_visit_diag/nuh_pat_visit_diag[hrn_n=$hrn and acct_n=$acct]", document, XPathConstants.NODESET);
            List<Node> documentNodes = new ArrayList<Node>(1 + visitDocList.getLength() + visitDiagList.getLength());
            documentNodes.add(visit);
            for (int x = 0; x < visitDocList.getLength(); x++) {
                documentNodes.add(visitDocList.item(x));
            }
            for (int x = 0; x < visitDiagList.getLength(); x++) {
                Node dx = visitDiagList.item(x);
                documentNodes.add(dx);
                loadDiagnosis(dx, mdPatient);
            }
            createDocument(mdEncounter);
            loadMedCert(mdEncounter, documentNodes);
            populateDocument(mdEncounter, documentNodes);
            workingMemory.insert(mdEncounter);
            return mdEncounter;
        } catch (Exception e) {
            throw new RuntimeException("Unable to setup encounter in account " + getAccount().getId(), e);
        }
    }

    protected void loadMedCert(MenuData mdEncounter, List<Node> documentNodes) throws Exception {
        NodeList medCerts = (NodeList) xpath.evaluate("/nuh/nuh_med_cert/nuh_pat_med_cert[hrn_n=$hrn and acct_n = $acct]", document, XPathConstants.NODESET);
        for (int x = 0; x < medCerts.getLength(); x++) {
            Node medCert = medCerts.item(x);
            documentNodes.add(medCert);
        }
    }

    /**
	 * Load one diagnosis
	 * @return
	 */
    protected MenuData loadDiagnosis(Node dx, MenuData mdPatient) throws Exception {
        MenuStructure msDiagnosis = menuBean.findMenuStructure(getAccount(), ":patient:diagnosis");
        MenuData mdDiagnosis = new MenuData();
        mdDiagnosis.setMenuStructure(msDiagnosis.getAccountMenuStructure());
        mdDiagnosis.setAccount(getAccount());
        mdDiagnosis.setParent01(mdPatient);
        mdDiagnosis.setField("code", diagCodePath.evaluate(dx));
        mdDiagnosis.setField("title", diagNamePath.evaluate(dx));
        mdDiagnosis.setField("effectiveTime", getFieldAsDate("ludate_d", dx));
        List<Node> documentNodes = new ArrayList<Node>(1);
        documentNodes.add(dx);
        persistMenuData(mdDiagnosis);
        createDocument(mdDiagnosis);
        populateDocument(mdDiagnosis, documentNodes);
        workingMemory.insert(mdDiagnosis);
        return mdDiagnosis;
    }

    /**
	 * Bind to XRays  for one patient. We start with the Order and build from there
	 * @param document
	 * @param workingMemory
	 */
    protected void bindToXRays(MenuData mdPatient) {
        try {
            NodeList xrayList = (NodeList) xpath.evaluate("/nuh/nuh_xray_order/nuh_pat_x005F_xray_order[hrn_n=$hrn]", document, XPathConstants.NODESET);
            for (int i = 0; i < xrayList.getLength(); i++) {
                bindToXRay(xrayList.item(i), mdPatient);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error binding to XRays", e);
        }
    }

    /**
	 * XRay data, starting with the order (request)
	 * @return Patient MenuData
	 */
    protected MenuData bindToXRay(Node xrayOrder, MenuData mdPatient) {
        try {
            ee.pushContext();
            MenuStructure msOrder = menuBean.findMenuStructure(getAccount(), ":patient:request");
            String checkinId = checkinIdPath.evaluate(xrayOrder);
            variables.put(new QName("orderid"), checkinId);
            MenuData mdXRayOrder = null;
            List<MenuData> mdXRayOrders = menuBean.findMenuDataById(getAccount(), "xrayorderid", checkinId);
            if (mdXRayOrders.size() == 1) {
                System.out.println("XRay found");
                mdXRayOrder = mdXRayOrders.get(0);
            } else {
                mdXRayOrder = new MenuData();
                mdXRayOrder.setMenuStructure(msOrder.getAccountMenuStructure());
                mdXRayOrder.setAccount(getAccount());
                mdXRayOrder.setParent01(mdPatient);
                mdXRayOrder.addPlaceholderID("xrayorderid", checkinId, "nuh");
            }
            createDocument(mdXRayOrder);
            List<Node> documentNodes = new ArrayList<Node>(1);
            documentNodes.add(xrayOrder);
            XPathMap xpathMap = new XPathMap(xrayOrder);
            ee.addVariable("nuh", xpathMap);
            persistMenuData(mdXRayOrder);
            menuBean.populateMenuData(ee, mdXRayOrder);
            addXRayOrderFields(xrayOrder, mdXRayOrder);
            createDocument(mdXRayOrder);
            loadXRayResults(mdXRayOrder, documentNodes);
            populateDocument(mdXRayOrder, documentNodes);
            workingMemory.insert(mdXRayOrder);
            ee.popContext();
            return mdXRayOrder;
        } catch (Exception e) {
            throw new RuntimeException("Unable to setup XRay in account " + getAccount().getId(), e);
        }
    }

    protected void loadXRayResults(MenuData mdXRayOrder, List<Node> documentNodes) throws Exception {
        MenuStructure msResult = menuBean.findMenuStructure(getAccount(), ":patient:result");
        NodeList xrayReports = (NodeList) xpath.evaluate("/nuh/nuh_xray_report/nuh_pat_x005F_xray_rep[hrn_n=$hrn and chk_in_id_c=$orderid]", document, XPathConstants.NODESET);
        for (int x = 0; x < xrayReports.getLength(); x++) {
            Node xrayReport = xrayReports.item(x);
            String resultId = checkinIdPath.evaluate(xrayReport);
            List<MenuData> mdXRayResults = menuBean.findMenuDataById(getAccount(), "xrayresultid", resultId);
            MenuData mdXRayResult = null;
            if (mdXRayResults.size() == 1) {
                System.out.println("XRay found");
                mdXRayResult = mdXRayResults.get(0);
            } else {
                mdXRayResult = new MenuData();
                mdXRayResult.setMenuStructure(msResult.getAccountMenuStructure());
                mdXRayResult.setAccount(getAccount());
                mdXRayResult.setField("patient", mdXRayOrder.getField("patient"));
                mdXRayResult.setField("request", mdXRayOrder);
                mdXRayResult.addPlaceholderID("xrayresultid", resultId, "checkinId");
            }
            String exam = examPath.evaluate(xrayReport);
            mdXRayResult.setField("title", exam);
            String status = statusPath.evaluate(xrayReport);
            mdXRayResult.setField("status", status);
            String priority = priorityPath.evaluate(xrayReport);
            String filmCount = filmCountPath.evaluate(xrayReport);
            String radiologist = radiologistPath.evaluate(xrayReport);
            String radiologist2 = radiologist2Path.evaluate(xrayReport);
            String radiographer = radiographerPath.evaluate(xrayReport);
            StringBuffer source = new StringBuffer(100);
            source.append(radiologist);
            if (radiologist2 != null && radiologist2.length() > 0) {
                source.append(" - ");
                source.append(radiologist2);
            }
            if (radiographer != null && radiographer.length() > 0) {
                source.append(" - ");
                source.append(radiographer);
            }
            mdXRayResult.setField("source", source.toString());
            String patLocation = patLocationPath.evaluate(xrayReport);
            mdXRayResult.setField("patientLocation", patLocation.toString());
            mdXRayResult.setField("effectiveTime", getFieldAsDate("ludate_d", xrayReport));
            String cancelDate = cancelDatePath.evaluate(xrayReport);
            mdXRayResult.setField("type", "Imaging X-ray");
            persistMenuData(mdXRayResult);
            workingMemory.insert(mdXRayResult);
            documentNodes.add(xrayReport);
            NodeList xrayList = (NodeList) xpath.evaluate("/nuh/nuh_xray/nuh_pat_x005F_xray[hrn_n=$hrn and chk_in_id_c=$orderid]", document, XPathConstants.NODESET);
            for (int y = 0; y < xrayList.getLength(); y++) {
                Node xray = xrayList.item(y);
                documentNodes.add(xray);
                mdXRayResult.setField("effectiveTime", getFieldAsDate("exam_d", xray));
            }
            NodeList xrayImageList = (NodeList) xpath.evaluate("/nuh/nuh_xray_images/nuh_pat_x005F_xray_rep_ddi[hrn_n=$hrn and chk_in_id_c=$orderid]", document, XPathConstants.NODESET);
            for (int y = 0; y < xrayImageList.getLength(); y++) {
                Node xrayImage = xrayImageList.item(y);
                documentNodes.add(xrayImage);
            }
            NodeList xrayRepRepList = (NodeList) xpath.evaluate("/nuh/nuh_xray_rep_rep/nuh_pat_x005F_xray_rep_rep[hrn_n=$hrn and chk_in_id_c=$orderid]", document, XPathConstants.NODESET);
            for (int y = 0; y < xrayRepRepList.getLength(); y++) {
                Node xrayRepRep = xrayRepRepList.item(y);
                documentNodes.add(xrayRepRep);
            }
            mdXRayResult.setDocumentId(mdXRayOrder.getDocumentId());
        }
    }

    /**
	 * Bind to Lab data for one patient. We start with orders and find related results
	 * @param document
	 * @param workingMemory
	 */
    protected void bindToLabs(MenuData mdPatient) {
        try {
            NodeList labOrderList = (NodeList) xpath.evaluate("/nuh/nuh_lab_req/nuh_pat_lab_req[hrn_n=$hrn]", document, XPathConstants.NODESET);
            for (int i = 0; i < labOrderList.getLength(); i++) {
                bindToLabOrder(labOrderList.item(i), mdPatient);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error binding to Labs", e);
        }
    }

    protected MenuData bindToLabOrder(Node labOrder, MenuData mdPatient) throws Exception {
        ee.pushContext();
        MenuStructure msOrder = menuBean.findMenuStructure(getAccount(), ":patient:request");
        String seqId = seqPath.evaluate(labOrder);
        variables.put(new QName("seq"), seqId);
        MenuData mdLabOrder = null;
        List<MenuData> mdLabOrders = menuBean.findMenuDataById(getAccount(), "laborderid", seqId);
        if (mdLabOrders.size() == 1) {
            System.out.println("Lab found");
            mdLabOrder = mdLabOrders.get(0);
        } else {
            mdLabOrder = new MenuData();
            mdLabOrder.setMenuStructure(msOrder.getAccountMenuStructure());
            mdLabOrder.setAccount(getAccount());
            mdLabOrder.setField("patient", mdPatient);
            mdLabOrder.addPlaceholderID("laborderid", seqId, "nuh");
        }
        List<Node> documentNodes = new ArrayList<Node>(1);
        documentNodes.add(labOrder);
        persistMenuData(mdLabOrder);
        createDocument(mdLabOrder);
        XPathMap xpathMap = new XPathMap(labOrder);
        ee.addVariable("nuh", xpathMap);
        menuBean.populateMenuData(ee, mdLabOrder);
        addLabOrderFields(labOrder, mdLabOrder);
        workingMemory.insert(mdLabOrder);
        loadLabResults(mdLabOrder, documentNodes);
        populateDocument(mdLabOrder, documentNodes);
        ee.popContext();
        return mdLabOrder;
    }

    protected void loadLabResults(MenuData mdLabOrder, List<Node> documentNodes) throws Exception {
        MenuStructure msResult = menuBean.findMenuStructure(getAccount(), ":patient:result");
        NodeList labResults = (NodeList) xpath.evaluate("/nuh/nuh_results/nuh_pat_lab_res[hrn_n=$hrn and seq_n=$seq]", document, XPathConstants.NODESET);
        for (int x = 0; x < labResults.getLength(); x++) {
            Node labResult = labResults.item(x);
            String resultId = seqPath.evaluate(labResult) + "-" + setIdPath.evaluate(labResult);
            List<MenuData> mdLabResults = menuBean.findMenuDataById(getAccount(), "labresultid", resultId);
            MenuData mdLabResult = null;
            if (mdLabResults.size() == 1) {
                System.out.println("Lab result found");
                mdLabResult = mdLabResults.get(0);
            } else {
                mdLabResult = new MenuData();
                mdLabResult.setMenuStructure(msResult.getAccountMenuStructure());
                mdLabResult.setAccount(getAccount());
                mdLabResult.setField("patient", mdLabOrder.getField("patient"));
                mdLabResult.setField("request", mdLabOrder);
                mdLabResult.addPlaceholderID("labresultid", resultId, "nuh");
            }
            addLabResultFields(labResult, mdLabResult, mdLabOrder);
            persistMenuData(mdLabResult);
            workingMemory.insert(mdLabResult);
            documentNodes.add(labResult);
            NodeList labNotesList = (NodeList) xpath.evaluate("/nuh/nuh_lab_notes/nuh_pat_lab_notes[hrn_n=$hrn and seq_n=$seq]", document, XPathConstants.NODESET);
            for (int y = 0; y < labNotesList.getLength(); y++) {
                Node labNote = labNotesList.item(y);
                documentNodes.add(labNote);
            }
            mdLabResult.setDocumentId(mdLabOrder.getDocumentId());
        }
    }

    /**
	 * Bind to Lab data for one patient. We start with orders and find related results
	 * @param document
	 * @param workingMemory
	 */
    protected void bindToMeds(MenuData mdPatient) {
        try {
            NodeList rxList = (NodeList) xpath.evaluate("/nuh/nug_pat_prescriptions/nuh_pat_inp_med[hrn_n=$hrn]", document, XPathConstants.NODESET);
            for (int i = 0; i < rxList.getLength(); i++) {
                bindToRX(rxList.item(i), mdPatient);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error binding to Prescriptions", e);
        }
    }

    protected MenuData bindToStaff(String drid) {
        MenuStructure msAssigned = menuBean.findMenuStructure(getAccount(), ":assigned");
        List<MenuData> mdStaff = menuBean.findMenuDataById(getAccount(), "drid", drid);
        MenuData mdAssigned = null;
        if (mdStaff.size() == 1) {
            System.out.println("author found");
            mdAssigned = mdStaff.get(0);
        } else {
            mdAssigned = new MenuData();
            mdAssigned.setMenuStructure(msAssigned.getAccountMenuStructure());
            mdAssigned.setAccount(getAccount());
            mdAssigned.addPlaceholderID("drid", drid, "nuh");
            persistMenuData(mdAssigned);
            mdAssigned.setField("firstName", drid);
            mdAssigned.setField("lastName", drid);
            createDocument(mdAssigned);
            workingMemory.insert(mdAssigned);
            List<Node> documentNodes = new ArrayList<Node>(1);
            populateDocument(mdAssigned, documentNodes);
        }
        return mdAssigned;
    }

    protected void bindToRX(Node rx, MenuData mdPatient) throws Exception {
        MenuStructure msRx = menuBean.findMenuStructure(getAccount(), ":patient:medication");
        String accountId = xpath.evaluate("acct_n", rx);
        variables.put(new QName("acct_n"), accountId);
        Date consult_d = getFieldAsDate("consult_d", rx);
        String hrn_n = xpath.evaluate("hrn_n", rx);
        String dr_m = xpath.evaluate("dr_m", rx);
        MenuData mdAuthor = bindToStaff(dr_m);
        String loc_m = xpath.evaluate("loc_m", rx);
        String status_d = xpath.evaluate("status_d", rx);
        String presc_desc = xpath.evaluate("presc_desc", rx);
        NodeList medList = (NodeList) xpath.evaluate("/nuh/nug_pat_prescribed_medications/nuh_pat_inp_med_det[hrn_n=$hrn and acct_n=$acct_n]", document, XPathConstants.NODESET);
        for (int y = 0; y < medList.getLength(); y++) {
            Node med = medList.item(y);
            List<Node> documentNodes = new ArrayList<Node>(1);
            documentNodes.add(rx);
            documentNodes.add(med);
            MenuData mdMed = null;
            String item_seq_n = xpath.evaluate("item_seq_n", med);
            String rxid = hrn_n + "-" + accountId + "-" + item_seq_n;
            List<MenuData> mdMeds = menuBean.findMenuDataById(getAccount(), "rxid", rxid);
            if (mdMeds.size() == 1) {
                System.out.println("med found");
                mdMed = mdMeds.get(0);
            } else {
                mdMed = new MenuData();
                mdMed.setMenuStructure(msRx.getAccountMenuStructure());
                mdMed.setAccount(getAccount());
                mdMed.setField("patient", mdPatient);
                mdMed.addPlaceholderID("rxid", accountId, "nuh");
            }
            String item_m = xpath.evaluate("item_m", med);
            String p_dose_fm_t = xpath.evaluate("p_dose_fm_t", med);
            String dose_t = xpath.evaluate("dose_t", med);
            String p_freq_desc = xpath.evaluate("p_freq_desc", med);
            String p_dur_desc = xpath.evaluate("p_dur_desc", med);
            String stren_t = xpath.evaluate("stren_t", med);
            String cancel_c = xpath.evaluate("cancel_c", med);
            StringBuffer title = new StringBuffer();
            title.append(item_m);
            title.append(" ");
            title.append(p_dose_fm_t);
            title.append(" ");
            title.append(dose_t);
            title.append(" ");
            title.append(p_freq_desc);
            title.append(" ");
            title.append(p_dur_desc);
            title.append(" ");
            title.append(stren_t);
            mdMed.setField("title", title.toString());
            mdMed.setField("source", presc_desc);
            mdMed.setField("status", status_d);
            if ("C".equalsIgnoreCase(cancel_c)) {
                Date cancelDate = getFieldAsDate("cancel_d", med);
                mdMed.setField("dateType", "Cancel");
                mdMed.setField("effectiveTimeLow", cancelDate);
            } else {
                mdMed.setField("dateType", "Order");
                mdMed.setField("effectiveTimeLow", consult_d);
            }
            mdMed.setField("author", mdAuthor);
            persistMenuData(mdMed);
            createDocument(mdMed);
            workingMemory.insert(mdMed);
            populateDocument(mdMed, documentNodes);
        }
    }

    protected XPathExpression path(String expression) {
        try {
            return xpath.compile(expression);
        } catch (XPathExpressionException e) {
            throw new RuntimeException("Invalid XPath Expression", e);
        }
    }

    protected void openDocument() {
    }

    @Override
    protected ExpressionEvaluator getExpressionEvaluator() {
        if (cdaee == null) {
            cdaee = new TrimExpressionEvaluator();
            cdaee.addVariable("now", getNow());
            cdaee.addVariable("doc", getDocument());
            cdaee.addVariable(TrimExpressionEvaluator.ACCOUNT, getAccount());
            cdaee.addVariable(DocumentType.DOCUMENT, getDocument());
        }
        return cdaee;
    }

    /**
	 * Translate the node to XML, and make it pretty
	 * @param node
	 * @return String containing XML
	 */
    protected String nodesToXML(List<Node> nodes) {
        try {
            javax.xml.transform.Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
            StringWriter writer = new StringWriter();
            writer.write("<nuh>\n");
            StreamResult result = new StreamResult(writer);
            for (Node node : nodes) {
                DOMSource source = new DOMSource(node);
                transformer.transform(source, result);
            }
            writer.write("</nuh>");
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error creating XML output", e);
        }
    }

    protected DocXML createDocument(MenuData placeholder) {
        DocXML docBase = documentBean.createXMLDocument("nuh", tm.getAuthorId(), tm.getAccountId());
        placeholder.setDocumentId(docBase.getId());
        return docBase;
    }

    /**
	 * Setup a document in the placeholder with the node supplied
	 * @param placeholder
	 * @param node
	 */
    protected void populateDocument(MenuData placeholder, List<Node> nodes) {
        DocBase docBase = documentBean.findDocument(placeholder.getDocumentId());
        String kbeKeyAlgorithm = propertyBean.getProperty(DocumentSecretKey.DOC_KBE_KEY_ALGORITHM_PROP);
        int kbeKeyLength = Integer.parseInt(propertyBean.getProperty(DocumentSecretKey.DOC_KBE_KEY_LENGTH));
        docBase.setAsEncryptedContent(nodesToXML(nodes).getBytes(), kbeKeyAlgorithm, kbeKeyLength);
    }

    /**
	 * Get an XML data as a Java Date
	 * @param expression
	 * @param node
	 * @return
	 * @throws Exception
	 */
    protected Date getFieldAsDate(String expression, Node node) throws Exception {
        String lexicalRepresentation = xpath.evaluate(expression, node);
        if (lexicalRepresentation == null || lexicalRepresentation.length() == 0) {
            return null;
        }
        XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(lexicalRepresentation);
        return cal.toGregorianCalendar().getTime();
    }

    /**
	 * Setup XRay order fields
	 * @param xrayOrder
	 * @param mdXRayOrder
	 * @throws Exception
	 */
    protected void addXRayOrderFields(Node xrayOrder, MenuData mdXRayOrder) throws Exception {
        mdXRayOrder.setField("source", xpath.evaluate("order_mcr_n", xrayOrder));
        mdXRayOrder.setField("effectiveTime", getFieldAsDate("ludate_d", xrayOrder));
        mdXRayOrder.setField("type", "Imaging X-ray");
    }

    /**
	 * Setup Lab order fields
	 * @param xrayOrder
	 * @param mdXRayOrder
	 * @throws Exception
	 */
    protected void addLabOrderFields(Node labOrder, MenuData mdLabOrder) throws Exception {
        mdLabOrder.setField("effectiveTime", getFieldAsDate("receive_d", labOrder));
        mdLabOrder.setField("title", xpath.evaluate("universal_desc_c", labOrder));
        mdLabOrder.setField("source", xpath.evaluate("dr_m", labOrder));
        mdLabOrder.setField("status", xpath.evaluate("result_stat_d", labOrder));
    }

    /**
	 * Setup Lab result fields
	 * @param labResult XML node
	 * @param mdLabResult MenuData
	 * @throws Exception
	 */
    protected void addLabResultFields(Node labResult, MenuData mdLabResult, MenuData mdLabOrder) throws Exception {
        mdLabResult.setField("effectiveTime", mdLabOrder.getField("effectiveTime"));
        mdLabResult.setField("title", xpath.evaluate("testName_c", labResult));
        StringBuffer interp = new StringBuffer();
        String value = xpath.evaluate("obsv_result", labResult);
        if (value != null) {
            if (value.matches("[\\d]+\\.[\\d]+")) {
                mdLabResult.setField("value", Double.parseDouble(value));
            } else {
                interp.append(value);
            }
        }
        if (interp.length() > 0) {
            interp.append(" ");
        }
        interp.append(xpath.evaluate("abnormal_flg", labResult));
        interp.append(" ");
        interp.append(xpath.evaluate("range_c", labResult));
        mdLabResult.setField("units", xpath.evaluate("unit_c", labResult));
        mdLabResult.setField("source", xpath.evaluate("dr_m", labResult));
        mdLabResult.setField("interp", interp.toString());
        mdLabResult.setField("status", xpath.evaluate("result_stat_d", labResult));
        mdLabResult.setField("type", "Lab");
    }

    @Override
    protected DocBase scanInboundDocument(DocBase doc) throws Exception {
        return doc;
    }

    @Override
    public void process(Object message, Date now) {
        try {
            if (message instanceof TolvenMessage || message instanceof TolvenMessageWithAttachments) {
                TolvenMessage tm = (TolvenMessage) message;
                if (NUHns.equals(tm.getXmlNS())) {
                    associateDocument(tm, now);
                    runRules();
                    logger.info("Processing NUH document " + getDocument().getId() + " for account: " + tm.getAccountId());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception in NUH processor", e);
        }
    }

    /**
	 * Internal Map implementation that allows XPath expressions to be embedded in EL in field definitions.
	 * @author John Churin
	 *
	 */
    private static class XPathMap implements Map<String, Object> {

        private XPath xpath = XPathFactory.newInstance().newXPath();

        private Node root;

        public XPathMap(Node root) {
            this.root = root;
        }

        @Override
        public Object get(Object key) {
            try {
                XPathExpression keyPath = xpath.compile((String) key);
                String result = keyPath.evaluate(root);
                if (result != null && result.endsWith("00:00") && ((String) key).endsWith("_d")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date date = sdf.parse(result);
                    return date;
                }
                return result;
            } catch (Exception e) {
                throw new RuntimeException("XPath error: " + key, e);
            }
        }

        @Override
        public void clear() {
        }

        @Override
        public boolean containsKey(Object key) {
            return false;
        }

        @Override
        public boolean containsValue(Object value) {
            return false;
        }

        @Override
        public Set<java.util.Map.Entry<String, Object>> entrySet() {
            return null;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Set<String> keySet() {
            return null;
        }

        @Override
        public Object put(String key, Object value) {
            return null;
        }

        @Override
        public void putAll(Map<? extends String, ? extends Object> m) {
        }

        @Override
        public Object remove(Object key) {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public Collection<Object> values() {
            return null;
        }
    }
}
