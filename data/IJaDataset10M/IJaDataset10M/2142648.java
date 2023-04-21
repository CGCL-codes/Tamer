package hci.gnomex.controller;

import hci.gnomex.billing.BillingPlugin;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.ExperimentCollaborator;
import hci.gnomex.model.PropertyType;
import hci.gnomex.model.FlowCellChannel;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Label;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.LabelingReactionSize;
import hci.gnomex.model.OligoBarcode;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.PriceSheet;
import hci.gnomex.model.PriceSheetPriceCategory;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Sample;
import hci.gnomex.model.Property;
import hci.gnomex.model.PropertyEntry;
import hci.gnomex.model.PropertyEntryValue;
import hci.gnomex.model.PropertyOption;
import hci.gnomex.model.SeqLibTreatment;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.Slide;
import hci.gnomex.model.SlideDesign;
import hci.gnomex.model.Step;
import hci.gnomex.model.TransferLog;
import hci.gnomex.model.TreatmentEntry;
import hci.gnomex.model.Visibility;
import hci.gnomex.model.WorkItem;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.FileDescriptorUploadParser;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.HybNumberComparator;
import hci.gnomex.utility.LabeledSampleNumberComparator;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.RequestEmailBodyFormatter;
import hci.gnomex.utility.RequestParser;
import hci.gnomex.utility.SampleNumberComparator;
import hci.gnomex.utility.SequenceLaneNumberComparator;
import hci.gnomex.utility.WorkItemHybParser;
import hci.gnomex.utility.RequestParser.HybInfo;
import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import java.io.File;
import java.io.Serializable;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class SaveRequest extends GNomExCommand implements Serializable {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveRequest.class);

    private String requestXMLString;

    private Document requestDoc;

    private RequestParser requestParser;

    private String filesToRemoveXMLString;

    private Document filesToRemoveDoc;

    private FileDescriptorUploadParser filesToRemoveParser;

    private BillingPeriod billingPeriod;

    private String launchAppURL;

    private String showRequestFormURLBase;

    private String appURL;

    private String serverName;

    private String originalRequestNumber;

    private Integer idProject;

    private Map labelMap = new HashMap();

    private Map idSampleMap = new HashMap();

    private TreeSet hybs = new TreeSet(new HybNumberComparator());

    private TreeSet samples = new TreeSet(new SampleNumberComparator());

    private TreeSet sequenceLanes = new TreeSet(new SequenceLaneNumberComparator());

    private TreeSet hybsAdded = new TreeSet(new HybNumberComparator());

    private TreeSet samplesAdded = new TreeSet(new SampleNumberComparator());

    private TreeSet labeledSamplesAdded = new TreeSet(new LabeledSampleComparator());

    private TreeSet sequenceLanesAdded = new TreeSet(new SequenceLaneNumberComparator());

    private TreeSet samplesDeleted = new TreeSet(new SampleNumberComparator());

    private TreeSet sequenceLanesDeleted = new TreeSet(new SequenceLaneNumberComparator());

    private TreeSet hybsDeleted = new TreeSet(new HybNumberComparator());

    private Map channel1SampleMap = new HashMap();

    private Map channel2SampleMap = new HashMap();

    private Integer idLabelingProtocolDefault;

    private Integer idHybProtocolDefault;

    private Integer idScanProtocolDefault;

    private Integer idFeatureExtractionProtocolDefault;

    private String invoicePrice;

    public void validate() {
    }

    public void loadCommand(HttpServletRequest request, HttpSession session) {
        if (request.getParameter("requestXMLString") != null && !request.getParameter("requestXMLString").equals("")) {
            requestXMLString = request.getParameter("requestXMLString");
            this.requestXMLString = this.requestXMLString.replaceAll("&", "&amp;");
        }
        if (request.getParameter("filesToRemoveXMLString") != null && !request.getParameter("filesToRemoveXMLString").equals("")) {
            filesToRemoveXMLString = "<FilesToRemove>" + request.getParameter("filesToRemoveXMLString") + "</FilesToRemove>";
            StringReader reader = new StringReader(filesToRemoveXMLString);
            try {
                SAXBuilder sax = new SAXBuilder();
                filesToRemoveDoc = sax.build(reader);
                filesToRemoveParser = new FileDescriptorUploadParser(filesToRemoveDoc);
            } catch (JDOMException je) {
                log.error("Cannot parse filesToRemoveXMLString", je);
                this.addInvalidField("FilesToRemoveXMLString", "Invalid filesToRemove xml");
            }
        }
        invoicePrice = "";
        if (request.getParameter("invoicePrice") != null && request.getParameter("invoicePrice").length() > 0) {
            invoicePrice = request.getParameter("invoicePrice");
        }
        StringReader reader = new StringReader(requestXMLString);
        try {
            SAXBuilder sax = new SAXBuilder();
            requestDoc = sax.build(reader);
            requestParser = new RequestParser(requestDoc, this.getSecAdvisor());
        } catch (JDOMException je) {
            log.error("Cannot parse requestXMLString", je);
            this.addInvalidField("RequestXMLString", "Invalid request xml");
        }
        if (request.getParameter("idProject") != null && !request.getParameter("idProject").equals("")) {
            idProject = new Integer(request.getParameter("idProject"));
        }
        try {
            launchAppURL = this.getLaunchAppURL(request);
            showRequestFormURLBase = this.getShowRequestFormURL(request);
            appURL = this.getAppURL(request);
        } catch (Exception e) {
            log.warn("Cannot get launch app URL in SaveRequest", e);
        }
        serverName = request.getServerName();
    }

    public Command execute() throws RollBackCommandException {
        Session sess = null;
        String billingAccountMessage = "";
        try {
            sess = HibernateSession.currentSession(this.getUsername());
            DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
            billingPeriod = dictionaryHelper.getCurrentBillingPeriod();
            if (billingPeriod == null && requestXMLString.contains("isExternal=\"N\"")) {
                throw new Exception("Cannot find current billing period to create billing items");
            }
            requestParser.parse(sess);
            PropertyDictionaryHelper propertyHelper = PropertyDictionaryHelper.getInstance(sess);
            if (propertyHelper.getProperty(PropertyDictionary.BST_LINKAGE_SUPPORTED) != null && propertyHelper.getProperty(PropertyDictionary.BST_LINKAGE_SUPPORTED).equals("Y")) {
                validateCCNumbers();
            }
            if (requestParser.isNewRequest()) {
                if (!this.getSecAdvisor().isGroupIAmMemberOrManagerOf(requestParser.getRequest().getIdLab())) {
                    this.addInvalidField("PermissionLab", "Insufficient permissions to submit the request for this lab.");
                }
            } else {
                if (!this.getSecAdvisor().canUpdate(requestParser.getRequest())) {
                    this.addInvalidField("PermissionAddRequest", "Insufficient permissions to edit the request.");
                }
            }
            if (this.isValid()) {
                List labels = sess.createQuery("SELECT label from Label label").list();
                for (Iterator i = labels.iterator(); i.hasNext(); ) {
                    Label l = (Label) i.next();
                    labelMap.put(l.getLabel(), l.getIdLabel());
                }
                saveRequest(requestParser.getRequest(), sess);
                if (filesToRemoveParser != null) {
                    for (Iterator i = filesToRemoveParser.parseFilesToRemove().iterator(); i.hasNext(); ) {
                        String fileName = (String) i.next();
                        String queryBuf = "SELECT tl from TransferLog tl where tl.idRequest = " + requestParser.getRequest().getIdRequest() + " AND tl.fileName like '%" + new File(fileName).getName() + "'";
                        List transferLogs = sess.createQuery(queryBuf).list();
                        if (transferLogs.size() == 1) {
                            TransferLog transferLog = (TransferLog) transferLogs.get(0);
                            sess.delete(transferLog);
                        }
                        boolean success = new File(fileName).delete();
                        if (!success) {
                            throw new Exception("Unable to delete file " + fileName);
                        }
                    }
                    sess.flush();
                }
                if (!requestParser.isNewRequest() && !requestParser.isAmendRequest()) {
                    for (Iterator i = requestParser.getRequest().getSamples().iterator(); i.hasNext(); ) {
                        Sample sample = (Sample) i.next();
                        boolean found = false;
                        for (Iterator i1 = requestParser.getSampleIds().iterator(); i1.hasNext(); ) {
                            String idSampleString = (String) i1.next();
                            if (idSampleString != null && !idSampleString.equals("") && !idSampleString.startsWith("Sample")) {
                                if (Integer.valueOf(idSampleString).equals(sample.getIdSample())) {
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if (!found) {
                            this.samplesDeleted.add(sample);
                        }
                    }
                }
                if (this.samplesDeleted.size() > 0) {
                    if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_WRITE_ANY_OBJECT)) {
                        throw new RollBackCommandException("Insufficient permission to delete samples.");
                    }
                }
                int sampleCount = 1;
                for (Iterator i = requestParser.getSampleIds().iterator(); i.hasNext(); ) {
                    String idSampleString = (String) i.next();
                    boolean isNewSample = requestParser.isNewRequest() || idSampleString == null || idSampleString.equals("") || idSampleString.startsWith("Sample");
                    Sample sample = (Sample) requestParser.getSampleMap().get(idSampleString);
                    saveSample(idSampleString, sample, sess, sampleCount, dictionaryHelper);
                    if (!requestParser.isExternalExperiment()) {
                        if ((requestParser.isNewRequest() || isNewSample || requestParser.isQCAmendRequest())) {
                            WorkItem workItem = new WorkItem();
                            workItem.setIdRequest(requestParser.getRequest().getIdRequest());
                            if (RequestCategory.isIlluminaRequestCategory(requestParser.getRequest().getCodeRequestCategory())) {
                                if (requestParser.isQCAmendRequest() && !isNewSample) {
                                    String codeStepNext = requestParser.getRequest().getCodeRequestCategory().equals(RequestCategory.SOLEXA_REQUEST_CATEGORY) ? Step.SEQ_PREP : Step.HISEQ_PREP;
                                    workItem.setCodeStepNext(codeStepNext);
                                    if (sample.getSeqPrepByCore() != null && sample.getSeqPrepByCore().equalsIgnoreCase("Y")) {
                                        sample.setQualBypassed("Y");
                                        sample.setQualDate(new java.sql.Date(System.currentTimeMillis()));
                                    }
                                } else {
                                    if (sample.getSeqPrepByCore() != null && sample.getSeqPrepByCore().equalsIgnoreCase("Y")) {
                                        String codeStepNext = requestParser.getRequest().getCodeRequestCategory().equals(RequestCategory.SOLEXA_REQUEST_CATEGORY) ? Step.SEQ_QC : Step.HISEQ_QC;
                                        workItem.setCodeStepNext(codeStepNext);
                                    } else {
                                        String codeStepNext = requestParser.getRequest().getCodeRequestCategory().equals(RequestCategory.SOLEXA_REQUEST_CATEGORY) ? Step.SEQ_PREP : Step.HISEQ_PREP;
                                        workItem.setCodeStepNext(codeStepNext);
                                        sample.setQualBypassed("Y");
                                        sample.setQualDate(new java.sql.Date(System.currentTimeMillis()));
                                    }
                                }
                            } else {
                                if (requestParser.isNewRequest() || isNewSample) {
                                    workItem.setCodeStepNext(Step.QUALITY_CONTROL_STEP);
                                }
                            }
                            if (workItem.getCodeStepNext() != null) {
                                workItem.setSample(sample);
                                workItem.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
                                sess.save(workItem);
                            }
                        }
                    }
                    sampleCount++;
                }
                requestParser.getRequest().setSamples(samples);
                if (!requestParser.isNewRequest() && !requestParser.isAmendRequest()) {
                    for (Iterator i = requestParser.getRequest().getHybridizations().iterator(); i.hasNext(); ) {
                        Hybridization hyb = (Hybridization) i.next();
                        boolean found = false;
                        for (Iterator i1 = requestParser.getHybInfos().iterator(); i1.hasNext(); ) {
                            HybInfo hybInfo = (HybInfo) i1.next();
                            if (hybInfo.getIdHybridization() != null && !hybInfo.getIdHybridization().equals("") && !hybInfo.getIdHybridization().startsWith("Hyb")) {
                                if (Integer.valueOf(hybInfo.getIdHybridization()).equals(hyb.getIdHybridization())) {
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if (!found) {
                            this.hybsDeleted.add(hyb);
                        }
                    }
                }
                if (this.hybsDeleted.size() > 0) {
                    if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_WRITE_ANY_OBJECT)) {
                        throw new RollBackCommandException("Insufficient permission to delete hybs.");
                    }
                }
                if (!requestParser.isNewRequest() && !requestParser.isAmendRequest()) {
                    for (Iterator i = requestParser.getRequest().getHybridizations().iterator(); i.hasNext(); ) {
                        Hybridization hyb = (Hybridization) i.next();
                        if (hyb.getIdLabeledSampleChannel1() != null) {
                            this.channel1SampleMap.put(hyb.getIdSampleChannel1(), hyb.getIdLabeledSampleChannel1());
                        }
                        if (hyb.getIdLabeledSampleChannel2() != null) {
                            this.channel2SampleMap.put(hyb.getIdSampleChannel2(), hyb.getIdLabeledSampleChannel2());
                        }
                    }
                }
                if (!requestParser.isNewRequest()) {
                    requestParser.getRequest().getHybridizations().size();
                }
                if (!requestParser.getHybInfos().isEmpty()) {
                    int hybCount = 1;
                    int newHybCount = 0;
                    for (Iterator i = requestParser.getHybInfos().iterator(); i.hasNext(); ) {
                        RequestParser.HybInfo hybInfo = (RequestParser.HybInfo) i.next();
                        boolean isNewHyb = requestParser.isNewRequest() || hybInfo.getIdHybridization() == null || hybInfo.getIdHybridization().startsWith("Hyb");
                        if (isNewHyb) {
                            newHybCount++;
                        }
                        saveHyb(hybInfo, sess, hybCount);
                        hybCount++;
                    }
                    if (requestParser.isNewRequest()) {
                        requestParser.getRequest().setHybridizations(hybs);
                    } else if (newHybCount > 0) {
                        requestParser.getRequest().getHybridizations().addAll(hybs);
                    }
                }
                StringBuffer buf = new StringBuffer();
                if (requestParser.getAmendState().equals(Constants.AMEND_QC_TO_MICROARRAY)) {
                    for (Iterator i = requestParser.getSampleIds().iterator(); i.hasNext(); ) {
                        String idSampleString = (String) i.next();
                        boolean isNewSample = requestParser.isNewRequest() || idSampleString == null || idSampleString.equals("") || idSampleString.startsWith("Sample");
                        Sample sample = (Sample) requestParser.getSampleMap().get(idSampleString);
                        if (!requestParser.isExternalExperiment() && !isNewSample) {
                            buf = new StringBuffer();
                            buf.append("SELECT  ls ");
                            buf.append(" from LabeledSample ls ");
                            buf.append(" WHERE  ls.idSample =  " + sample.getIdSample());
                            List labeledSamples = sess.createQuery(buf.toString()).list();
                            for (Iterator i1 = labeledSamples.iterator(); i1.hasNext(); ) {
                                LabeledSample ls = (LabeledSample) i1.next();
                                WorkItem wi = new WorkItem();
                                wi.setIdRequest(sample.getIdRequest());
                                wi.setCodeStepNext(Step.LABELING_STEP);
                                wi.setLabeledSample(ls);
                                wi.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
                                sess.save(wi);
                            }
                        }
                    }
                }
                HashMap sampleToLaneMap = new HashMap();
                HashMap existingLanesSaved = new HashMap();
                if (!requestParser.getSequenceLaneInfos().isEmpty()) {
                    for (Iterator i = requestParser.getSequenceLaneInfos().iterator(); i.hasNext(); ) {
                        RequestParser.SequenceLaneInfo laneInfo = (RequestParser.SequenceLaneInfo) i.next();
                        List lanes = (List) sampleToLaneMap.get(laneInfo.getIdSampleString());
                        if (lanes == null) {
                            lanes = new ArrayList();
                            sampleToLaneMap.put(laneInfo.getIdSampleString(), lanes);
                        }
                        lanes.add(laneInfo);
                    }
                    for (Iterator i = sampleToLaneMap.keySet().iterator(); i.hasNext(); ) {
                        String idSampleString = (String) i.next();
                        List lanes = (List) sampleToLaneMap.get(idSampleString);
                        int lastSampleSeqCount = 0;
                        for (Iterator i1 = lanes.iterator(); i1.hasNext(); ) {
                            RequestParser.SequenceLaneInfo laneInfo = (RequestParser.SequenceLaneInfo) i1.next();
                            boolean isNewLane = requestParser.isNewRequest() || laneInfo.getIdSequenceLane() == null || laneInfo.getIdSequenceLane().startsWith("SequenceLane");
                            if (!isNewLane) {
                                SequenceLane lane = (SequenceLane) sess.load(SequenceLane.class, new Integer(laneInfo.getIdSequenceLane()));
                                String[] tokens = lane.getNumber().split("_");
                                if (tokens.length == 2) {
                                    Integer lastSeqLaneNumber = Integer.valueOf(tokens[1]);
                                    if (lastSeqLaneNumber.intValue() > lastSampleSeqCount) {
                                        lastSampleSeqCount = lastSeqLaneNumber.intValue();
                                    }
                                }
                            }
                        }
                        for (Iterator i1 = lanes.iterator(); i1.hasNext(); ) {
                            RequestParser.SequenceLaneInfo laneInfo = (RequestParser.SequenceLaneInfo) i1.next();
                            boolean isNewLane = requestParser.isNewRequest() || laneInfo.getIdSequenceLane() == null || laneInfo.getIdSequenceLane().startsWith("SequenceLane");
                            SequenceLane lane = saveSequenceLane(laneInfo, sess, lastSampleSeqCount);
                            if (!isNewLane) {
                                existingLanesSaved.put(lane.getIdSequenceLane(), lane);
                            }
                            if ((!requestParser.isExternalExperiment() && !requestParser.isNewRequest() && isNewLane)) {
                                WorkItem workItem = new WorkItem();
                                workItem.setIdRequest(requestParser.getRequest().getIdRequest());
                                workItem.setSequenceLane(lane);
                                String codeStepNext = requestParser.getRequest().getCodeRequestCategory().equals(RequestCategory.SOLEXA_REQUEST_CATEGORY) ? Step.SEQ_CLUSTER_GEN : Step.HISEQ_CLUSTER_GEN;
                                workItem.setCodeStepNext(codeStepNext);
                                sess.save(workItem);
                            }
                            if (isNewLane) {
                                lastSampleSeqCount++;
                            }
                        }
                    }
                }
                if (!requestParser.isAmendRequest()) {
                    for (Iterator i = requestParser.getRequest().getSequenceLanes().iterator(); i.hasNext(); ) {
                        SequenceLane lane = (SequenceLane) i.next();
                        if (!existingLanesSaved.containsKey(lane.getIdSequenceLane())) {
                            boolean canDeleteLane = true;
                            if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_WRITE_ANY_OBJECT)) {
                                this.addInvalidField("deleteLanePermissionError1", "Insufficient permissions to delete sequence lane\n");
                                canDelete = false;
                            }
                            buf = new StringBuffer("SELECT x.idSequenceLane from AnalysisExperimentItem x where x.idSequenceLane = " + lane.getIdSequenceLane());
                            List analysis = sess.createQuery(buf.toString()).list();
                            if (analysis != null && analysis.size() > 0) {
                                canDelete = false;
                                this.addInvalidField("deleteLaneError1", "Cannot delete lane " + lane.getNumber() + " because it is associated with existing analysis in GNomEx.  Please sever link before attempting delete\n");
                            }
                            if (lane.getFlowCellChannel() != null) {
                                canDelete = false;
                                this.addInvalidField("deleteLaneError2", "Cannot delete lane " + lane.getNumber() + " because it is loaded on a flow cell.  Please delete flow cell channel before attempting delete\n");
                            }
                            if (lane.getFlowCellChannel() != null) {
                                buf = new StringBuffer("SELECT ch.idFlowCellChannel from WorkItem wi join wi.flowCellChannel ch where ch.idFlowCellChannel = " + lane.getIdFlowCellChannel());
                                List workItems = sess.createQuery(buf.toString()).list();
                                if (workItems != null && workItems.size() > 0) {
                                    canDelete = false;
                                    this.addInvalidField("deleteLaneError3", "Cannot delete lane " + lane.getNumber() + " because it is loaded on a flow cell that is on the seq run worklist.  Please delete flow cell channel and work item before attempting delete\n");
                                }
                            }
                            if (canDeleteLane) {
                                sequenceLanesDeleted.add(lane);
                                sess.delete(lane);
                            }
                        }
                    }
                }
                Set seqLibTreatments = new TreeSet();
                for (Iterator i = requestParser.getSeqLibTreatmentMap().keySet().iterator(); i.hasNext(); ) {
                    String key = (String) i.next();
                    Integer idSeqLibTreatment = Integer.parseInt(key);
                    SeqLibTreatment slt = dictionaryHelper.getSeqLibTreatment(idSeqLibTreatment);
                    seqLibTreatments.add(slt);
                }
                this.requestParser.getRequest().setSeqLibTreatments(seqLibTreatments);
                sess.save(requestParser.getRequest());
                sess.flush();
                for (Iterator i1 = requestParser.getRequest().getCollaborators().iterator(); i1.hasNext(); ) {
                    ExperimentCollaborator ec = (ExperimentCollaborator) i1.next();
                    if (!requestParser.getCollaboratorMap().containsKey(ec.getIdAppUser())) {
                        sess.delete(ec);
                    }
                }
                Set collaborators = new TreeSet();
                for (Iterator i = requestParser.getCollaboratorMap().keySet().iterator(); i.hasNext(); ) {
                    String key = (String) i.next();
                    Integer idAppUser = Integer.parseInt(key);
                    String canUploadData = (String) requestParser.getCollaboratorMap().get(key);
                    ExperimentCollaborator collaborator = (ExperimentCollaborator) sess.createQuery("SELECT ec from ExperimentCollaborator ec where idRequest = " + requestParser.getRequest().getIdRequest() + " and idAppUser = " + idAppUser).uniqueResult();
                    if (collaborator == null) {
                        collaborator = new ExperimentCollaborator();
                        collaborator.setIdAppUser(idAppUser);
                        collaborator.setIdRequest(requestParser.getRequest().getIdRequest());
                        collaborator.setCanUploadData(canUploadData);
                        sess.save(collaborator);
                    } else {
                        collaborator.setCanUploadData(canUploadData);
                    }
                }
                sess.flush();
                if (!requestParser.isNewRequest() && (requestParser.isAmendRequest() || !samplesAdded.isEmpty() || !labeledSamplesAdded.isEmpty() || !hybsAdded.isEmpty() || !sequenceLanesAdded.isEmpty() || !sequenceLanesDeleted.isEmpty())) {
                    originalRequestNumber = requestParser.getRequest().getNumber();
                    int revNumber = 1;
                    String[] tokens = requestParser.getRequest().getNumber().split("R");
                    if (tokens.length > 1) {
                        if (tokens[1] != null && !tokens[1].equals("")) {
                            Integer oldRevNumber = Integer.valueOf(tokens[1]);
                            revNumber = oldRevNumber.intValue() + 1;
                        }
                        originalRequestNumber = tokens[0] + "R";
                    }
                    requestParser.getRequest().setNumber(originalRequestNumber + revNumber);
                    sess.flush();
                }
                billingAccountMessage = "";
                if (!requestParser.isExternalExperiment()) {
                    sess.refresh(requestParser.getRequest());
                    if (requestParser.getAmendState().equals(Constants.AMEND_QC_TO_SEQ)) {
                        samplesAdded.addAll(requestParser.getRequest().getSamples());
                    }
                    createBillingItems(sess, requestParser.getRequest(), requestParser.getAmendState(), billingPeriod, dictionaryHelper, samplesAdded, labeledSamplesAdded, hybsAdded, sequenceLanesAdded);
                    sess.flush();
                    int reassignCount = 0;
                    int unassignedCount = 0;
                    if (!requestParser.isNewRequest() && requestParser.isReassignBillingAccount()) {
                        for (Iterator ib = requestParser.getRequest().getBillingItems().iterator(); ib.hasNext(); ) {
                            BillingItem bi = (BillingItem) ib.next();
                            if (bi.getCodeBillingStatus().equals(BillingStatus.PENDING) || bi.getCodeBillingStatus().equals(BillingStatus.COMPLETED)) {
                                bi.setIdBillingAccount(requestParser.getRequest().getIdBillingAccount());
                                reassignCount++;
                            } else {
                                unassignedCount++;
                            }
                        }
                        if (unassignedCount > 0) {
                            billingAccountMessage = "WARNING: The billing account could not be reassigned for " + unassignedCount + " approved billing items.  Please reassign in the Billing screen.";
                        }
                        if (billingAccountMessage.length() > 0) {
                            billingAccountMessage += "\n\n(The billing account has been reassigned for  " + reassignCount + " billing item(s).)";
                        } else {
                            billingAccountMessage = "The billing account has been reassigned for " + reassignCount + " billing item(s).";
                        }
                        if (reassignCount > 0) {
                            sess.flush();
                        }
                    }
                }
                reassignLabForTransferLog(sess);
                sess.flush();
                if (requestParser.isNewRequest()) {
                    this.createResultDirectories(requestParser.getRequest(), dictionaryHelper.getPropertyDictionary(PropertyDictionary.QC_DIRECTORY), dictionaryHelper.getMicroarrayDirectoryForWriting(serverName));
                }
                XMLOutputter out = new org.jdom.output.XMLOutputter();
                String emailErrorMessage = sendEmails(sess);
                this.xmlResult = "<SUCCESS idRequest=\"" + requestParser.getRequest().getIdRequest() + "\" requestNumber=\"" + requestParser.getRequest().getNumber() + "\" deleteSampleCount=\"" + this.samplesDeleted.size() + "\" deleteHybCount=\"" + this.hybsDeleted.size() + "\" deleteLaneCount=\"" + this.sequenceLanesDeleted.size() + "\" billingAccountMessage = \"" + billingAccountMessage + "\" emailErrorMessage = \"" + emailErrorMessage + "\"/>";
            }
            if (isValid()) {
                setResponsePage(this.SUCCESS_JSP);
            } else {
                setResponsePage(this.ERROR_JSP);
            }
        } catch (Exception e) {
            log.error("An exception has occurred while emailing in SaveRequest ", e);
            e.printStackTrace();
            throw new RollBackCommandException(e.toString());
        } finally {
            try {
                if (sess != null) {
                    HibernateSession.closeSession();
                }
            } catch (Exception e) {
            }
        }
        return this;
    }

    private String sendEmails(Session sess) {
        StringBuffer message = new StringBuffer();
        if (requestParser.isNewRequest() || requestParser.isAmendRequest()) {
            sess.refresh(requestParser.getRequest());
            if (requestParser.getRequest().getAppUser() != null && requestParser.getRequest().getAppUser().getEmail() != null && !requestParser.getRequest().getAppUser().getEmail().equals("")) {
                try {
                    sendConfirmationEmail(sess);
                } catch (Exception e) {
                    String msg = "Unable to send confirmation email notifying submitter that request " + requestParser.getRequest().getNumber() + " has been submitted.  " + e.toString();
                    log.error(msg);
                    message.append(msg + "\n");
                }
            } else {
                String msg = ("Unable to send confirmation email notifying submitter that request " + requestParser.getRequest().getNumber() + " has been submitted.  Request submitter or request submitter email is blank.");
                log.error(msg);
                message.append(msg + "\n");
            }
            if (this.invoicePrice.length() > 0) {
                Lab lab = requestParser.getRequest().getLab();
                String billedAccountName = requestParser.getRequest().getBillingAccountName();
                String contactEmail = lab.getContactEmail();
                String ccEmail = "";
                for (Iterator i1 = lab.getManagers().iterator(); i1.hasNext(); ) {
                    AppUser manager = (AppUser) i1.next();
                    if (manager.getIsActive() != null && manager.getIsActive().equalsIgnoreCase("Y")) {
                        if (manager.getEmail() != null) {
                            ccEmail = ccEmail + manager.getEmail() + ", ";
                        }
                    }
                }
                if ((contactEmail != null && contactEmail.length() > 0) || ccEmail.length() > 0) {
                    try {
                        sendInvoicePriceEmail(sess, contactEmail, ccEmail, billedAccountName);
                    } catch (Exception e) {
                        String msg = "Unable to send estimated charges notification for request " + requestParser.getRequest().getNumber() + "  " + e.toString();
                        log.error(msg);
                        message.append(msg + "\n");
                    }
                } else {
                    String msg = "Unable to send estimated charges notification for request " + requestParser.getRequest().getNumber() + " has been submitted.  Contact or lab manager(s) email is blank.";
                    log.error(msg);
                    message.append(msg + "\n");
                }
            }
        }
        return message.toString();
    }

    private void validateCCNumbers() {
        Session sessGuest = null;
        Connection con = null;
        boolean hasCCNumbers = false;
        List<String> ccNumberList = requestParser.getCcNumberList();
        StringBuffer buf = new StringBuffer("select ccNumber from BST.dbo.Sample WHERE ccNumber in (");
        Iterator<String> itStr = ccNumberList.iterator();
        boolean firstTime = true;
        while (itStr.hasNext()) {
            hasCCNumbers = true;
            String thisKey = itStr.next();
            if (!firstTime) buf.append(","); else firstTime = false;
            buf.append("'" + thisKey + "'");
        }
        buf.append(")");
        if (hasCCNumbers) {
            try {
                Statement stmt = null;
                ResultSet rs = null;
                sessGuest = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
                con = sessGuest.connection();
                stmt = con.createStatement();
                rs = stmt.executeQuery(buf.toString());
                List<String> ccNumbersRetreivedList = new ArrayList<String>();
                while (rs.next()) {
                    ccNumbersRetreivedList.add(rs.getString("ccNumber"));
                }
                rs.close();
                stmt.close();
                buf = new StringBuffer();
                itStr = ccNumberList.iterator();
                firstTime = true;
                while (itStr.hasNext()) {
                    String thisKey = itStr.next();
                    if (!ccNumbersRetreivedList.contains(thisKey)) {
                        if (!firstTime) buf.append(", "); else firstTime = false;
                        buf.append("'" + thisKey + "'");
                    }
                }
                if (buf.toString().length() > 0) {
                    this.addInvalidField("InvalidCCNumber", "The following CC Numbers do not exist in BST: " + buf.toString() + ".\n\nPlease correct on the Samples tab.");
                }
            } catch (Exception e) {
            } finally {
                try {
                    if (sessGuest != null) {
                        if (con != null) {
                            con.close();
                        }
                        this.getSecAdvisor().closeReadOnlyHibernateSession();
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    private void saveRequest(Request request, Session sess) throws Exception {
        sess.save(request);
        if (requestParser.isNewRequest()) {
            request.setNumber(request.getIdRequest().toString() + "R");
            sess.save(request);
        }
        originalRequestNumber = request.getNumber();
        sess.flush();
    }

    private void saveSample(String idSampleString, Sample sample, Session sess, int sampleCount, DictionaryHelper dh) throws Exception {
        sample.setIdRequest(requestParser.getRequest().getIdRequest());
        sess.save(sample);
        boolean isNewSample = requestParser.isNewRequest() || idSampleString == null || idSampleString.equals("") || idSampleString.startsWith("Sample");
        if (isNewSample) {
            sample.setNumber(requestParser.getRequest().getIdRequest().toString() + "X" + sampleCount);
            sess.save(sample);
        }
        if (!isNewSample) {
            for (Iterator i = sample.getPropertyEntries().iterator(); i.hasNext(); ) {
                PropertyEntry entry = (PropertyEntry) i.next();
                for (Iterator i1 = entry.getValues().iterator(); i1.hasNext(); ) {
                    PropertyEntryValue v = (PropertyEntryValue) i1.next();
                    sess.delete(v);
                }
                sess.flush();
                entry.setValues(null);
                sess.delete(entry);
            }
        }
        Map sampleAnnotations = (Map) requestParser.getSampleAnnotationMap().get(idSampleString);
        for (Iterator i = sampleAnnotations.keySet().iterator(); i.hasNext(); ) {
            Integer idProperty = (Integer) i.next();
            String value = (String) sampleAnnotations.get(idProperty);
            Property property = (Property) dh.getPropertyObject(idProperty);
            PropertyEntry entry = new PropertyEntry();
            entry.setIdSample(sample.getIdSample());
            if (property.getName().equals("Other")) {
                entry.setOtherLabel(requestParser.getOtherCharacteristicLabel());
            }
            entry.setIdProperty(idProperty);
            entry.setValue(value);
            sess.save(entry);
            sess.flush();
            if (value != null && !value.equals("") && property.getCodePropertyType().equals(PropertyType.URL)) {
                Set urlValues = new TreeSet();
                String[] valueTokens = value.split("\\|");
                for (int x = 0; x < valueTokens.length; x++) {
                    String v = valueTokens[x];
                    PropertyEntryValue urlValue = new PropertyEntryValue();
                    urlValue.setValue(v);
                    urlValue.setIdPropertyEntry(entry.getIdPropertyEntry());
                    sess.save(urlValue);
                }
            }
            sess.flush();
            if (value != null && !value.equals("") && property.getCodePropertyType().equals(PropertyType.MULTI_OPTION)) {
                Set options = new TreeSet();
                String[] valueTokens = value.split(",");
                for (int x = 0; x < valueTokens.length; x++) {
                    String v = valueTokens[x];
                    for (Iterator i1 = property.getOptions().iterator(); i1.hasNext(); ) {
                        PropertyOption option = (PropertyOption) i1.next();
                        if (v.equals(option.getIdPropertyOption().toString())) {
                            options.add(option);
                        }
                    }
                }
                entry.setOptions(options);
            }
        }
        if (!isNewSample) {
            for (Iterator i = sample.getTreatmentEntries().iterator(); i.hasNext(); ) {
                TreatmentEntry entry = (TreatmentEntry) i.next();
                sess.delete(entry);
            }
        }
        String treatment = (String) requestParser.getSampleTreatmentMap().get(idSampleString);
        if (requestParser.getShowTreatments() && treatment != null && !treatment.equals("")) {
            TreatmentEntry entry = new TreatmentEntry();
            entry.setIdSample(sample.getIdSample());
            entry.setTreatment(treatment);
            sess.save(entry);
        }
        if (sample.getIdOligoBarcode() != null) {
            sample.setBarcodeSequence(dh.getBarcodeSequence(sample.getIdOligoBarcode()));
        }
        sess.flush();
        idSampleMap.put(idSampleString, sample.getIdSample());
        samples.add(sample);
        if (isNewSample) {
            samplesAdded.add(sample);
        }
    }

    private void saveHyb(RequestParser.HybInfo hybInfo, Session sess, int hybCount) throws Exception {
        StringBuffer buf = new StringBuffer();
        buf.append("SELECT x.idLabelingProtocolDefault, x.idHybProtocolDefault, x.idScanProtocolDefault, x.idFeatureExtractionProtocolDefault ");
        buf.append(" FROM  RequestCategoryApplication x ");
        buf.append(" WHERE x.codeRequestCategory = '" + requestParser.getRequest().getCodeRequestCategory() + "'");
        buf.append(" AND   x.codeApplication = '" + requestParser.getRequest().getCodeApplication() + "'");
        List defaultProtocolIds = sess.createQuery(buf.toString()).list();
        if (defaultProtocolIds.size() > 0) {
            Object[] row = (Object[]) defaultProtocolIds.get(0);
            idLabelingProtocolDefault = (Integer) row[0];
            idHybProtocolDefault = (Integer) row[1];
            idScanProtocolDefault = (Integer) row[2];
            idFeatureExtractionProtocolDefault = (Integer) row[3];
        }
        Hybridization hyb = null;
        boolean isNewHyb = requestParser.isNewRequest() || hybInfo.getIdHybridization() == null || hybInfo.getIdHybridization().startsWith("Hyb");
        if (isNewHyb) {
            hyb = new Hybridization();
            hyb.setCreateDate(new Date(System.currentTimeMillis()));
            hyb.setIdHybProtocol(idHybProtocolDefault);
            hyb.setIdScanProtocol(idScanProtocolDefault);
            hyb.setIdFeatureExtractionProtocol(idFeatureExtractionProtocolDefault);
            isNewHyb = true;
        } else {
            hyb = (Hybridization) sess.load(Hybridization.class, new Integer(hybInfo.getIdHybridization()));
        }
        Integer idSampleChannel1Real = null;
        if (hybInfo.getIdSampleChannel1String() != null && !hybInfo.getIdSampleChannel1String().equals("")) {
            idSampleChannel1Real = (Integer) idSampleMap.get(hybInfo.getIdSampleChannel1String());
        }
        Integer idSampleChannel2Real = null;
        if (hybInfo.getIdSampleChannel2String() != null && !hybInfo.getIdSampleChannel2String().equals("")) {
            idSampleChannel2Real = (Integer) idSampleMap.get(hybInfo.getIdSampleChannel2String());
        }
        LabeledSample labeledSampleChannel1 = null;
        LabeledSample labeledSampleChannel2 = null;
        if (isNewHyb) {
            Integer idLabeledSampleChannel1 = (Integer) channel1SampleMap.get(idSampleChannel1Real);
            if (!channel1SampleMap.containsKey(idSampleChannel1Real)) {
                labeledSampleChannel1 = new LabeledSample();
                labeledSampleChannel1.setIdSample(idSampleChannel1Real);
                labeledSampleChannel1.setIdLabel((Integer) labelMap.get("Cy3"));
                labeledSampleChannel1.setIdRequest(requestParser.getRequest().getIdRequest());
                labeledSampleChannel1.setCodeLabelingReactionSize(LabelingReactionSize.STANDARD);
                labeledSampleChannel1.setNumberOfReactions(new Integer(1));
                labeledSampleChannel1.setIdLabelingProtocol(idLabelingProtocolDefault);
                sess.save(labeledSampleChannel1);
                idLabeledSampleChannel1 = labeledSampleChannel1.getIdLabeledSample();
                channel1SampleMap.put(idSampleChannel1Real, idLabeledSampleChannel1);
                labeledSamplesAdded.add(labeledSampleChannel1);
            }
            hyb.setIdLabeledSampleChannel1(idLabeledSampleChannel1);
            if (idSampleChannel2Real != null) {
                Integer idLabeledSampleChannel2 = (Integer) channel2SampleMap.get(idSampleChannel2Real);
                if (!channel2SampleMap.containsKey(idSampleChannel2Real)) {
                    labeledSampleChannel2 = new LabeledSample();
                    labeledSampleChannel2.setIdSample(idSampleChannel2Real);
                    labeledSampleChannel2.setIdLabel((Integer) labelMap.get("Cy5"));
                    labeledSampleChannel2.setIdRequest(requestParser.getRequest().getIdRequest());
                    labeledSampleChannel2.setCodeLabelingReactionSize(LabelingReactionSize.STANDARD);
                    labeledSampleChannel2.setNumberOfReactions(new Integer(1));
                    labeledSampleChannel2.setIdLabelingProtocol(idLabelingProtocolDefault);
                    sess.save(labeledSampleChannel2);
                    idLabeledSampleChannel2 = labeledSampleChannel2.getIdLabeledSample();
                    channel2SampleMap.put(idSampleChannel2Real, idLabeledSampleChannel2);
                    labeledSamplesAdded.add(labeledSampleChannel2);
                }
                hyb.setIdLabeledSampleChannel2(idLabeledSampleChannel2);
            }
        } else {
            boolean changedChannelSample = false;
            if ((hyb.getLabeledSampleChannel1() == null && idSampleChannel1Real != null) || (hyb.getLabeledSampleChannel1() != null && idSampleChannel1Real == null) || (hyb.getLabeledSampleChannel1() != null && idSampleChannel1Real != null && !hyb.getLabeledSampleChannel1().getIdSample().equals(idSampleChannel1Real))) {
                LabeledSample labeledSampleObsoleted = null;
                if (hyb.getIdLabeledSampleChannel1() != null) {
                    labeledSampleObsoleted = hyb.getLabeledSampleChannel1();
                }
                if (idSampleChannel1Real != null) {
                    Integer idLabeledSampleChannel1 = null;
                    if (channel1SampleMap.containsKey(idSampleChannel1Real)) {
                        idLabeledSampleChannel1 = (Integer) channel1SampleMap.get(idSampleChannel1Real);
                    } else {
                        labeledSampleChannel1 = new LabeledSample();
                        labeledSampleChannel1.setIdSample(idSampleChannel1Real);
                        labeledSampleChannel1.setIdLabel((Integer) labelMap.get("Cy3"));
                        labeledSampleChannel1.setIdRequest(requestParser.getRequest().getIdRequest());
                        labeledSampleChannel1.setCodeLabelingReactionSize(LabelingReactionSize.STANDARD);
                        labeledSampleChannel1.setNumberOfReactions(new Integer(1));
                        labeledSampleChannel1.setIdLabelingProtocol(idLabelingProtocolDefault);
                        sess.save(labeledSampleChannel1);
                        idLabeledSampleChannel1 = labeledSampleChannel1.getIdLabeledSample();
                        channel1SampleMap.put(idSampleChannel1Real, idLabeledSampleChannel1);
                    }
                    hyb.setIdLabeledSampleChannel1(idLabeledSampleChannel1);
                } else {
                    hyb.setIdLabeledSampleChannel1(null);
                }
                sess.flush();
                if (labeledSampleObsoleted != null) {
                    List referencingWorkItems = sess.createQuery("SELECT wi from WorkItem wi join wi.labeledSample as ls where ls.idLabeledSample = " + labeledSampleObsoleted.getIdLabeledSample()).list();
                    if (referencingWorkItems.size() > 0) {
                        for (Iterator i1 = referencingWorkItems.iterator(); i1.hasNext(); ) {
                            WorkItem wi = (WorkItem) i1.next();
                            if (labeledSampleChannel1 != null) {
                                wi.setLabeledSample(labeledSampleChannel1);
                            } else {
                                sess.delete(wi);
                            }
                        }
                    }
                    List referencingHybs = sess.createQuery("SELECT h from Hybridization h where h.idLabeledSampleChannel1 = " + labeledSampleObsoleted.getIdLabeledSample()).list();
                    if (referencingHybs.size() == 0) {
                        sess.delete(labeledSampleObsoleted);
                    }
                }
                changedChannelSample = true;
            }
            if ((hyb.getLabeledSampleChannel2() == null && idSampleChannel2Real != null) || (hyb.getLabeledSampleChannel2() != null && idSampleChannel2Real == null) || (hyb.getLabeledSampleChannel2() != null && idSampleChannel2Real != null && !hyb.getLabeledSampleChannel2().getIdSample().equals(idSampleChannel2Real))) {
                LabeledSample labeledSampleObsoleted = null;
                if (hyb.getIdLabeledSampleChannel1() != null) {
                    labeledSampleObsoleted = hyb.getLabeledSampleChannel2();
                }
                if (idSampleChannel2Real != null) {
                    Integer idLabeledSampleChannel2 = null;
                    if (channel2SampleMap.containsKey(idSampleChannel2Real)) {
                        idLabeledSampleChannel2 = (Integer) channel2SampleMap.get(idSampleChannel2Real);
                    } else {
                        labeledSampleChannel2 = new LabeledSample();
                        labeledSampleChannel2.setIdSample(idSampleChannel2Real);
                        labeledSampleChannel2.setIdLabel((Integer) labelMap.get("Cy5"));
                        labeledSampleChannel2.setIdRequest(requestParser.getRequest().getIdRequest());
                        labeledSampleChannel2.setCodeLabelingReactionSize(LabelingReactionSize.STANDARD);
                        labeledSampleChannel2.setNumberOfReactions(new Integer(1));
                        labeledSampleChannel2.setIdLabelingProtocol(idLabelingProtocolDefault);
                        sess.save(labeledSampleChannel2);
                        idLabeledSampleChannel2 = labeledSampleChannel2.getIdLabeledSample();
                        channel2SampleMap.put(idSampleChannel2Real, idLabeledSampleChannel2);
                    }
                    hyb.setIdLabeledSampleChannel2(idLabeledSampleChannel2);
                } else {
                    hyb.setIdLabeledSampleChannel2(null);
                }
                sess.flush();
                if (labeledSampleObsoleted != null) {
                    List referencingWorkItems = sess.createQuery("SELECT wi from WorkItem wi join wi.labeledSample as ls where ls.idLabeledSample = " + labeledSampleObsoleted.getIdLabeledSample()).list();
                    if (referencingWorkItems.size() > 0) {
                        for (Iterator i1 = referencingWorkItems.iterator(); i1.hasNext(); ) {
                            WorkItem wi = (WorkItem) i1.next();
                            if (labeledSampleChannel2 != null) {
                                wi.setLabeledSample(labeledSampleChannel2);
                            } else {
                                sess.delete(wi);
                            }
                        }
                    }
                    List referencingHybs = sess.createQuery("SELECT h from Hybridization h where h.idLabeledSampleChannel2 = " + labeledSampleObsoleted.getIdLabeledSample()).list();
                    if (referencingHybs.size() == 0) {
                        sess.delete(labeledSampleObsoleted);
                    }
                }
                changedChannelSample = true;
            }
            if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
                if (!changedChannelSample) {
                    if (hyb.getLabeledSampleChannel1() != null) {
                        if (hybInfo.getLabelingCompletedChannel1().equals("Y") && hyb.getLabeledSampleChannel1().getLabelingDate() == null) {
                            hyb.getLabeledSampleChannel1().setLabelingDate(new java.sql.Date(System.currentTimeMillis()));
                        }
                        hyb.getLabeledSampleChannel1().setLabelingBypassed(hybInfo.getLabelingBypassedChannel1());
                        hyb.getLabeledSampleChannel1().setLabelingFailed(hybInfo.getLabelingFailedChannel1());
                        hyb.getLabeledSampleChannel1().setIdLabelingProtocol(hybInfo.getIdLabelingProtocolChannel1());
                        hyb.getLabeledSampleChannel1().setLabelingYield(hybInfo.getLabelingYieldChannel1());
                        hyb.getLabeledSampleChannel1().setNumberOfReactions(hybInfo.getNumberOfReactionsChannel1());
                        hyb.getLabeledSampleChannel1().setCodeLabelingReactionSize(hybInfo.getCodeLabelingReactionSizeChannel1());
                    }
                    if (hyb.getLabeledSampleChannel2() != null) {
                        if (hybInfo.getLabelingCompletedChannel2().equals("Y") && hyb.getLabeledSampleChannel2().getLabelingDate() == null) {
                            hyb.getLabeledSampleChannel2().setLabelingDate(new java.sql.Date(System.currentTimeMillis()));
                        }
                        hyb.getLabeledSampleChannel2().setLabelingBypassed(hybInfo.getLabelingBypassedChannel2());
                        hyb.getLabeledSampleChannel2().setLabelingFailed(hybInfo.getLabelingFailedChannel2());
                        hyb.getLabeledSampleChannel2().setIdLabelingProtocol(hybInfo.getIdLabelingProtocolChannel2());
                        hyb.getLabeledSampleChannel2().setLabelingYield(hybInfo.getLabelingYieldChannel2());
                        hyb.getLabeledSampleChannel2().setNumberOfReactions(hybInfo.getNumberOfReactionsChannel2());
                        hyb.getLabeledSampleChannel2().setCodeLabelingReactionSize(hybInfo.getCodeLabelingReactionSizeChannel2());
                    }
                }
                hyb.setIdHybProtocol(hybInfo.getIdHybProtocol());
                hyb.setIdScanProtocol(hybInfo.getIdScanProtocol());
                hyb.setIdFeatureExtractionProtocol(hybInfo.getIdFeatureExtractionProtocol());
                if (hybInfo.getHybCompleted().equals("Y") && hyb.getHybDate() == null) {
                    hyb.setHybDate(new java.sql.Date(System.currentTimeMillis()));
                }
                hyb.setHybFailed(hybInfo.getHybFailed());
                hyb.setHybBypassed(hybInfo.getHybBypassed());
                if (hybInfo.getExtractionCompleted().equals("Y") && hyb.getExtractionDate() == null) {
                    hyb.setExtractionDate(new java.sql.Date(System.currentTimeMillis()));
                }
                hyb.setExtractionFailed(hybInfo.getExtractionFailed());
                hyb.setExtractionBypassed(hybInfo.getExtractionBypassed());
                Slide slide = hyb.getSlide();
                if (hybInfo.getSlideBarcode() != null) {
                    slide = WorkItemHybParser.getSlideForHyb(sess, hyb, hyb.getIdSlideDesign(), hybInfo.getSlideBarcode(), requestParser.getRequest().getIdRequest());
                    if (slide == null) {
                        slide = new Slide();
                        sess.save(slide);
                        if (hyb.getSlide() != null) {
                            WorkItemHybParser.deleteOrphanSlide(sess, hyb, requestParser.getRequest().getIdRequest());
                        }
                    }
                    hyb.setIdSlide(slide.getIdSlide());
                    slide.setIdSlideDesign(hyb.getIdSlideDesign());
                    slide.setBarcode(hybInfo.getSlideBarcode());
                    WorkItemHybParser.setArrayCoordinate(sess, hyb, slide, hybInfo.getArrayCoordinateName(), requestParser.getRequest().getIdRequest());
                }
            }
        }
        String codeSlideSource = hybInfo.getCodeSlideSource();
        hyb.setCodeSlideSource(codeSlideSource);
        if (hybInfo.getIdSlideDesign() != null) {
            hyb.setIdSlideDesign(hybInfo.getIdSlideDesign());
        } else {
            List slideDesigns = sess.createQuery("select sd from SlideDesign sd where sd.idSlideProduct = " + requestParser.getRequest().getIdSlideProduct()).list();
            if (slideDesigns.size() > 1) {
                throw new Exception("Cannot set slide design because multiple slide designs exist for slide product " + requestParser.getRequest().getIdSlideProduct());
            } else if (slideDesigns.size() == 0) {
                throw new Exception("Cannot set slide design because no slide designs exist for slide product " + requestParser.getRequest().getIdSlideProduct());
            }
            SlideDesign sd = (SlideDesign) slideDesigns.get(0);
            hyb.setIdSlideDesign(sd.getIdSlideDesign());
        }
        hyb.setNotes(hybInfo.getNotes());
        sess.save(hyb);
        if (isNewHyb) {
            hyb.setNumber(requestParser.getRequest().getIdRequest().toString() + "E" + hybCount);
            sess.save(hyb);
            sess.flush();
            sess.refresh(hyb);
            if (hyb.getLabeledSampleChannel1() != null) {
                sess.refresh(hyb.getLabeledSampleChannel1());
            }
            if (hyb.getLabeledSampleChannel2() != null) {
                sess.refresh(hyb.getLabeledSampleChannel2());
            }
            hybs.add(hyb);
            hybsAdded.add(hyb);
        }
        sess.flush();
    }

    private SequenceLane saveSequenceLane(RequestParser.SequenceLaneInfo sequenceLaneInfo, Session sess, int lastSampleSeqCount) throws Exception {
        SequenceLane sequenceLane = null;
        boolean isNewSequenceLane = requestParser.isNewRequest() || sequenceLaneInfo.getIdSequenceLane() == null || sequenceLaneInfo.getIdSequenceLane().startsWith("SequenceLane");
        if (isNewSequenceLane) {
            sequenceLane = new SequenceLane();
            sequenceLane.setIdRequest(requestParser.getRequest().getIdRequest());
            sequenceLane.setCreateDate(new Date(System.currentTimeMillis()));
            isNewSequenceLane = true;
        } else {
            sequenceLane = (SequenceLane) sess.load(SequenceLane.class, new Integer(sequenceLaneInfo.getIdSequenceLane()));
        }
        Integer idSampleReal = null;
        if (sequenceLaneInfo.getIdSampleString() != null && !sequenceLaneInfo.getIdSampleString().equals("") && !sequenceLaneInfo.getIdSampleString().equals("0")) {
            idSampleReal = (Integer) idSampleMap.get(sequenceLaneInfo.getIdSampleString());
        }
        sequenceLane.setIdSample(idSampleReal);
        sequenceLane.setIdSeqRunType(sequenceLaneInfo.getIdSeqRunType());
        sequenceLane.setIdNumberSequencingCycles(sequenceLaneInfo.getIdNumberSequencingCycles());
        sequenceLane.setIdGenomeBuildAlignTo(sequenceLaneInfo.getIdGenomeBuildAlignTo());
        sequenceLane.setAnalysisInstructions(sequenceLaneInfo.getAnalysisInstructions());
        sess.save(sequenceLane);
        if (isNewSequenceLane) {
            Sample theSample = (Sample) sess.get(Sample.class, sequenceLane.getIdSample());
            String flowCellNumber = theSample.getNumber().toString().replaceFirst("X", "F");
            sequenceLane.setNumber(flowCellNumber + "_" + (lastSampleSeqCount + 1));
            sess.save(sequenceLane);
            sess.flush();
            sequenceLanes.add(sequenceLane);
            sequenceLanesAdded.add(sequenceLane);
        }
        if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
            if (sequenceLane.getFlowCellChannel() != null) {
                FlowCellChannel channel = sequenceLane.getFlowCellChannel();
                channel.setClustersPerTile(sequenceLaneInfo.getClustersPerTile());
                channel.setNumberSequencingCyclesActual(sequenceLaneInfo.getNumberSequencingCyclesActual());
                channel.setFileName(sequenceLaneInfo.getFileName());
                if (sequenceLaneInfo.getSeqRunFirstCycleCompleted().equals("Y") && channel.getFirstCycleDate() == null) {
                    channel.setFirstCycleDate(new java.sql.Date(System.currentTimeMillis()));
                }
                channel.setFirstCycleFailed(sequenceLaneInfo.getSeqRunFirstCycleFailed());
                if (sequenceLaneInfo.getSeqRunLastCycleCompleted().equals("Y") && channel.getLastCycleDate() == null) {
                    channel.setLastCycleDate(new java.sql.Date(System.currentTimeMillis()));
                }
                channel.setLastCycleFailed(sequenceLaneInfo.getSeqRunLastCycleFailed());
                if (sequenceLaneInfo.getSeqRunPipelineCompleted().equals("Y") && channel.getPipelineDate() == null) {
                    channel.setPipelineDate(new java.sql.Date(System.currentTimeMillis()));
                }
                channel.setPipelineFailed(sequenceLaneInfo.getSeqRunPipelineFailed());
            }
        }
        sess.flush();
        sess.refresh(sequenceLane);
        return sequenceLane;
    }

    public static void createBillingItems(Session sess, Request request, String amendState, BillingPeriod billingPeriod, DictionaryHelper dh, Set<Sample> samples, Set<LabeledSample> labeledSamples, Set<Hybridization> hybs, Set<SequenceLane> lanes) throws Exception {
        List billingItems = new ArrayList<BillingItem>();
        PriceSheet priceSheet = null;
        List priceSheets = sess.createQuery("SELECT ps from PriceSheet as ps").list();
        for (Iterator i = priceSheets.iterator(); i.hasNext(); ) {
            PriceSheet ps = (PriceSheet) i.next();
            for (Iterator i1 = ps.getRequestCategories().iterator(); i1.hasNext(); ) {
                RequestCategory requestCategory = (RequestCategory) i1.next();
                if (requestCategory.getCodeRequestCategory().equals(request.getCodeRequestCategory())) {
                    priceSheet = ps;
                    break;
                }
            }
        }
        if (priceSheet != null) {
            for (Iterator i1 = priceSheet.getPriceCategories().iterator(); i1.hasNext(); ) {
                PriceSheetPriceCategory priceCategoryX = (PriceSheetPriceCategory) i1.next();
                PriceCategory priceCategory = priceCategoryX.getPriceCategory();
                if (priceCategory.getIsActive() != null && priceCategory.getIsActive().equals("N")) {
                    continue;
                }
                BillingPlugin plugin = null;
                if (priceCategory.getPluginClassName() != null) {
                    try {
                        plugin = (BillingPlugin) Class.forName(priceCategory.getPluginClassName()).newInstance();
                    } catch (Exception e) {
                        log.error("Unable to instantiate billing plugin " + priceCategory.getPluginClassName());
                    }
                }
                if (plugin != null) {
                    List billingItemsForCategory = plugin.constructBillingItems(sess, amendState, billingPeriod, priceCategory, request, samples, labeledSamples, hybs, lanes);
                    billingItems.addAll(billingItemsForCategory);
                }
            }
            for (Iterator i = billingItems.iterator(); i.hasNext(); ) {
                BillingItem bi = (BillingItem) i.next();
                sess.save(bi);
            }
        }
    }

    private void sendConfirmationEmail(Session sess) throws NamingException, MessagingException {
        DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
        StringBuffer introNote = new StringBuffer();
        String trackRequestURL = launchAppURL + "?requestNumber=" + requestParser.getRequest().getNumber() + "&launchWindow=" + Constants.WINDOW_TRACK_REQUESTS;
        if (requestParser.isExternalExperiment()) {
            if (requestParser.isNewRequest()) {
                introNote.append("Experiment " + requestParser.getRequest().getNumber() + " has been registered in the GNomEx repository.");
            } else {
                introNote.append("Additional services have been added to experiment " + originalRequestNumber + ".");
            }
            introNote.append("<br><br>To view the experiment details, click <a href=\"" + trackRequestURL + "\">" + Constants.APP_NAME + " - " + Constants.WINDOW_NAME_TRACK_REQUESTS + "</a>.");
        } else {
            if (requestParser.isNewRequest()) {
                introNote.append("Experiment request " + requestParser.getRequest().getNumber() + " has been submitted to the " + dictionaryHelper.getPropertyDictionary(PropertyDictionary.CORE_FACILITY_NAME) + ".  You will receive email notification when the experiment is complete.");
            } else {
                introNote.append("Request " + requestParser.getRequest().getNumber() + " to add services to existing experiment " + originalRequestNumber + " has been submitted to the " + dictionaryHelper.getPropertyDictionary(PropertyDictionary.CORE_FACILITY_NAME) + ".  You will receive email notification when the experiment is complete.");
            }
            introNote.append("<br><br>To track progress on the experiment request, click <a href=\"" + trackRequestURL + "\">" + Constants.APP_NAME + " - " + Constants.WINDOW_NAME_TRACK_REQUESTS + "</a>.");
        }
        RequestEmailBodyFormatter emailFormatter = new RequestEmailBodyFormatter(sess, this.getSecAdvisor(), appURL, dictionaryHelper, requestParser.getRequest(), requestParser.getAmendState(), samples, hybs, sequenceLanes, introNote.toString());
        String subject = dictionaryHelper.getRequestCategory(requestParser.getRequest().getCodeRequestCategory()) + (requestParser.isExternalExperiment() ? " Experiment " : " Experiment Request ") + requestParser.getRequest().getNumber() + (requestParser.isExternalExperiment() ? " registered" : " submitted");
        boolean send = false;
        if (dictionaryHelper.isProductionServer(serverName)) {
            send = true;
        } else {
            if (requestParser.getRequest().getAppUser().getEmail().equals(dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER))) {
                send = true;
                subject = "TEST - " + subject;
            }
        }
        if (send) {
            MailUtil.send(requestParser.getRequest().getAppUser().getEmail(), null, (requestParser.isExternalExperiment() ? dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_BUGS) : dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY)), subject, emailFormatter.format(), true);
        }
    }

    private void sendInvoicePriceEmail(Session sess, String contactEmail, String ccEmail, String billedAccountName) throws NamingException, MessagingException {
        DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
        String requestType = dictionaryHelper.getRequestCategory(requestParser.getRequest().getCodeRequestCategory());
        String requestNumber = requestParser.getRequest().getNumber();
        String requestCategoryMsg = "";
        if (RequestCategory.isMicroarrayRequestCategory(requestParser.getRequest().getCodeRequestCategory())) {
            requestCategoryMsg = "Estimated Microarray";
        }
        if (RequestCategory.isIlluminaRequestCategory(requestParser.getRequest().getCodeRequestCategory())) {
            requestCategoryMsg = "Estimated Illumina Sequencing";
        }
        if (requestCategoryMsg.length() == 0) {
            return;
        }
        requestCategoryMsg = requestCategoryMsg + " for request " + requestNumber;
        StringBuffer emailBody = new StringBuffer();
        String trackRequestURL = launchAppURL + "?requestNumber=" + requestNumber + "&launchWindow=" + Constants.WINDOW_TRACK_REQUESTS;
        if (requestParser.isNewRequest()) {
            emailBody.append("An experiment request has been submitted to the " + dictionaryHelper.getPropertyDictionary(PropertyDictionary.CORE_FACILITY_NAME) + ".");
        } else {
            emailBody.append("A request to add services to existing experiment (" + originalRequestNumber + ") has been submitted to the " + dictionaryHelper.getPropertyDictionary(PropertyDictionary.CORE_FACILITY_NAME) + ".");
        }
        emailBody.append("<br><br><table border='0' width = '400'><tr><td>Request Type:</td><td>" + requestType);
        emailBody.append("</td></tr><tr><td>Request #:</td><td>" + requestNumber);
        emailBody.append("</td></tr><tr><td>Total Estimated Charges:</td><td>" + this.invoicePrice);
        emailBody.append("</td></tr><tr><td>Billing Account Name:</td><td>" + billedAccountName);
        emailBody.append("</td></tr></table><br><br>To track progress on the experiment request, click <a href=\"" + trackRequestURL + "\">" + Constants.APP_NAME + " - " + Constants.WINDOW_NAME_TRACK_REQUESTS + "</a>.");
        String subject = "Estimated Microarray charges for request " + requestNumber;
        String senderEmail = requestParser.isExternalExperiment() ? dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_BUGS) : dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY);
        if (contactEmail == null || contactEmail.length() == 0) {
            contactEmail = ccEmail;
            ccEmail = null;
            if (contactEmail == null) {
                contactEmail = senderEmail;
            }
        } else if (ccEmail != null && ccEmail.length() == 0) {
            ccEmail = null;
        }
        String emailInfo = "";
        boolean send = false;
        if (dictionaryHelper.isProductionServer(serverName)) {
            send = true;
        } else {
            if (requestParser.getRequest().getAppUser().getEmail().equals(dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER))) {
                send = true;
                subject = "TEST - " + subject;
                emailInfo = "[If this were a production environment then this email would have been sent to: " + contactEmail + " cc: " + ccEmail + "<br><br>";
                ccEmail = null;
            }
        }
        if (send) {
            MailUtil.send(contactEmail, ccEmail, senderEmail, subject, emailInfo + emailBody.toString(), true);
        }
    }

    private void reassignLabForTransferLog(Session sess) {
        if (!requestParser.isNewRequest() && !requestParser.getOriginalIdLab().equals(requestParser.getRequest().getIdLab())) {
            String buf = "SELECT tl from TransferLog tl where idRequest = " + requestParser.getRequest().getIdRequest();
            List transferLogs = sess.createQuery(buf).list();
            for (Iterator i = transferLogs.iterator(); i.hasNext(); ) {
                TransferLog tl = (TransferLog) i.next();
                tl.setIdLab(requestParser.getRequest().getIdLab());
            }
        }
    }

    private void createResultDirectories(Request req, String qcDirectory, String microarrayDir) {
        String createYear = this.formatDate(req.getCreateDate(), this.DATE_OUTPUT_ALTIO).substring(0, 4);
        String rootDir = microarrayDir + "/" + createYear;
        boolean success = false;
        if (!new File(rootDir).exists()) {
            success = (new File(rootDir)).mkdir();
            if (!success) {
                log.error("Unable to create directory " + rootDir);
            }
        }
        String directoryName = rootDir + "/" + req.getNumber();
        success = (new File(directoryName)).mkdir();
        if (!success) {
            log.error("Unable to create directory " + directoryName);
        }
        String bioanalyzerDirName = directoryName + "/" + qcDirectory;
        success = (new File(bioanalyzerDirName)).mkdir();
        if (!success) {
            log.error("Unable to create directory " + bioanalyzerDirName);
        }
        if (req.getHybridizations() != null) {
            for (Iterator i = req.getHybridizations().iterator(); i.hasNext(); ) {
                Hybridization hyb = (Hybridization) i.next();
                String hybDirectoryName = directoryName + "/" + hyb.getNumber();
                success = (new File(hybDirectoryName)).mkdir();
                if (!success) {
                    log.error("Unable to create directory " + hybDirectoryName);
                }
            }
        }
    }

    public class LabeledSampleComparator implements Comparator, Serializable {

        public int compare(Object o1, Object o2) {
            LabeledSample ls1 = (LabeledSample) o1;
            LabeledSample ls2 = (LabeledSample) o2;
            return ls1.getIdLabeledSample().compareTo(ls2.getIdLabeledSample());
        }
    }
}
