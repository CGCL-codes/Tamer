package missionevolver.versant;

import herschel.ccm.api.CommandSet;
import herschel.ccm.api.CoreFactory;
import herschel.ccm.api.CoreFactoryManager;
import herschel.ccm.api.CusDefinition;
import herschel.ccm.api.Mission;
import herschel.ccm.api.MissionConfiguration;
import herschel.ccm.api.MissionManager;
import herschel.ccm.api.param.Parameter;
import herschel.ccm.api.param.ParameterSet;
import herschel.ccm.api.phs.EquatorialCoordinates;
import herschel.ccm.api.phs.Estimates;
import herschel.ccm.api.phs.FixedTimeConstraint;
import herschel.ccm.api.phs.ObservationConstraint;
import herschel.ccm.api.phs.ObservationProgramme;
import herschel.ccm.api.phs.ObservationProgrammeList;
import herschel.ccm.api.phs.ObservationRequest;
import herschel.ccm.api.phs.Proposal;
import herschel.ccm.api.phs.TargetFixedSingle;
import herschel.ccm.api.phs.TimeInterval;
import missionevolver.data.DataList;
import missionevolver.data.HistoryRevision;
import missionevolver.data.InfoList;
import missionevolver.db.DBFacade;
import missionevolver.util.PropertyHandler;
import herschel.store.api.ObjectStore;
import herschel.store.api.StoreException;
import herschel.store.api.StoreFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class VersantFacade implements DBFacade {

    public static Hashtable OR_STATUS_STRINGS;

    public static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    static {
        OR_STATUS_STRINGS = new Hashtable();
        OR_STATUS_STRINGS.put(ObservationRequest.OR_ACCEPTED, "Accepted");
        OR_STATUS_STRINGS.put(ObservationRequest.OR_EXECUTED, "Executed");
        OR_STATUS_STRINGS.put(ObservationRequest.OR_FAILED, "Failed");
        OR_STATUS_STRINGS.put(ObservationRequest.OR_NEW, "New");
        OR_STATUS_STRINGS.put(ObservationRequest.OR_REJECTED, "Rejected");
        OR_STATUS_STRINGS.put(ObservationRequest.OR_RELEASED, "Released");
        OR_STATUS_STRINGS.put(ObservationRequest.OR_SCHEDULED, "Scheduled");
        OR_STATUS_STRINGS.put(ObservationRequest.OR_SUBMITTED, "Submitted");
        OR_STATUS_STRINGS.put(ObservationRequest.OR_SUCCESSFUL, "Successful");
    }

    private static Hashtable PROP_STATUS_STRINGS;

    static {
        PROP_STATUS_STRINGS = new Hashtable();
        PROP_STATUS_STRINGS.put(Proposal.PROPOSAL_ACCEPTED, "Accepted");
        PROP_STATUS_STRINGS.put(Proposal.PROPOSAL_REJECTED, "Rejected");
        PROP_STATUS_STRINGS.put(Proposal.PROPOSAL_SUBMITTED, "Submitted");
        PROP_STATUS_STRINGS.put(Proposal.TECHNICALLY_IMPROVEMENTS_POSSIBLE, "Technically improvements possible");
        PROP_STATUS_STRINGS.put(Proposal.TECHNICALLY_NOT_EVALUATED, "Technically not evaluated");
        PROP_STATUS_STRINGS.put(Proposal.TECHNICALLY_NOT_FEASIBLE, "Technically not feasible");
        PROP_STATUS_STRINGS.put(Proposal.TECHNICALLY_OKAY, "Technically ok");
    }

    public static boolean hasDbAccess() {
        boolean hasDbAccess = false;
        ObjectStore store = null;
        try {
            StoreFactory factory = StoreFactory.create();
            store = factory.createStore("hcss.phs.database");
            hasDbAccess = true;
        } catch (Throwable t) {
            System.out.println(t.getLocalizedMessage());
        } finally {
            if (store != null) {
                store.exit();
            }
        }
        return hasDbAccess;
    }

    public byte[] getProposal(Hashtable ht, StringBuffer pfile, Logger logger, String program, String proposal, InfoList il) throws Exception {
        byte[] pdfContent = null;
        ObjectStore store = null;
        try {
            List<String> programmes = new Vector<String>();
            programmes.add(program);
            StoreFactory factory = StoreFactory.create();
            store = factory.createStore("hcss.phs.database");
            CoreFactory coreFactory = CoreFactoryManager.getInstance();
            MissionManager manager = coreFactory.createMissionManager();
            Mission mission = manager.getInstance();
            for (int i = 0; i < programmes.size(); i++) {
                store.begin();
                String programeName = programmes.get(i);
                ObservationProgramme programme = mission.getObservationProgrammeList().getObservationProgramme(programeName);
                if (programme == null) {
                    throw new RuntimeException("Unknown programme:" + programeName);
                }
                logger.info("Updating requests in the following programme: " + programeName);
                Iterator<Proposal> proposals = programme.proposalIterator();
                while (proposals.hasNext()) {
                    Proposal p = proposals.next();
                    if (p.getProposalId() != null && p.getProposalId().equals(proposal)) {
                        retrieveProposal(p, il, null);
                        logger.info("Processing proposal: " + p.getProposalId());
                        Iterator<ObservationRequest> ors = p.observationRequestIterator();
                        while (ors.hasNext()) {
                            ObservationRequest or = ors.next();
                            Object[] request = null;
                            try {
                                request = retrieveObservationRequest(or);
                            } catch (Exception e) {
                                request = generateExceptionInfo(e, or);
                            }
                            ht.put(or.getTitle(), request);
                        }
                        try {
                            pdfContent = unpackFirst(p.getScienceJustificationFile());
                            if (pdfContent != null && pdfContent.length > 0) {
                            }
                        } catch (Exception e) {
                            logger.info("Proposal without pdf");
                        }
                    } else {
                    }
                }
            }
        } catch (StoreException ex) {
            logger.warning("StoreException:" + ex.getMessage());
        } catch (Exception ex) {
            logger.warning("Exception:" + ex.getMessage());
            logger.warning(getStackTrace(ex));
            logger.warning(getStackTrace(ex));
        } finally {
            logger.warning("Closing the store");
            if (store != null) {
                store.exit();
            }
        }
        return pdfContent;
    }

    public void getProposals(Hashtable ht, Logger logger, String program, Hashtable hr) throws Exception {
        ObjectStore store = null;
        try {
            List<String> programmes = new Vector<String>();
            programmes.add(program);
            StoreFactory factory = StoreFactory.create();
            store = factory.createStore("hcss.phs.database");
            CoreFactory coreFactory = CoreFactoryManager.getInstance();
            MissionManager manager = coreFactory.createMissionManager();
            Mission mission = manager.getInstance();
            store.begin();
            for (int i = 0; i < programmes.size(); i++) {
                String programeName = programmes.get(i);
                ObservationProgramme programme = mission.getObservationProgrammeList().getObservationProgramme(programeName);
                if (programme == null) {
                    throw new RuntimeException("Unknown programme:" + programeName);
                }
                logger.info("Programme: " + programeName);
                Iterator<Proposal> proposals = programme.proposalIterator();
                while (proposals.hasNext()) {
                    Proposal p = proposals.next();
                    InfoList il = new InfoList();
                    Vector<HistoryRevision> hrev = new Vector<HistoryRevision>();
                    retrieveProposal(p, il, hrev);
                    String id = p.getProposalId();
                    if (id == null) id = "Filtered";
                    ht.put(id, il);
                    hr.put(id, hrev);
                }
            }
        } catch (StoreException ex) {
            logger.warning("StoreException:" + getStackTrace(ex));
        } catch (Exception ex) {
            logger.warning("Exception:" + getStackTrace(ex));
        } finally {
            logger.warning("Closing the store");
            if (store != null) {
                store.exit();
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }

    /**
	     * Restores all data stored in current Blob object.
	     *
	     * @param dir Target directory under which data are restored.
	     */
    public byte[] unpackFirst(byte[] data) {
        boolean trimFilePath = true;
        byte[] unpacked = null;
        if (data != null) {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ZipInputStream zin = new ZipInputStream(in);
            try {
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null) {
                    String file = ze.getName();
                    if (trimFilePath) {
                        File newFile = new File(file);
                        file = newFile.getName();
                    }
                    ByteArrayOutputStream buffOS = new ByteArrayOutputStream();
                    int fileRead = -1;
                    byte[] BUFFER = new byte[4096];
                    while ((fileRead = zin.read(BUFFER)) != -1) {
                        buffOS.write(BUFFER, 0, fileRead);
                    }
                    unpacked = buffOS.toByteArray();
                }
                zin.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return unpacked;
    }

    public void getPrograms(Hashtable ht, Logger logger) throws Exception {
        ObjectStore store = null;
        try {
            StoreFactory factory = StoreFactory.create();
            store = factory.createStore("hcss.phs.database");
            CoreFactory coreFactory = CoreFactoryManager.getInstance();
            MissionManager manager = coreFactory.createMissionManager();
            Mission mission = manager.getInstance();
            store.begin();
            ObservationProgrammeList progList = mission.getObservationProgrammeList();
            for (Iterator iterator = progList.getObservationProgrammeIterator(); iterator.hasNext(); ) {
                ObservationProgramme programme = (ObservationProgramme) iterator.next();
                InfoList il = new InfoList();
                il.addInfo("Name", programme.getName());
                il.addInfo("Close", (programme.isProgrammeClosed() ? "true" : "false"));
                ht.put(programme.getName(), il);
            }
            store.abort();
        } catch (StoreException ex) {
            logger.warning("StoreException:" + getStackTrace(ex));
        } catch (Exception ex) {
            logger.warning("Exception:" + getStackTrace(ex));
        } finally {
            logger.warning("Closing the store");
            if (store != null) {
                store.exit();
            }
        }
    }

    public void getMissionConfiguration(Hashtable ht, Logger logger) throws Exception {
        ObjectStore store = null;
        try {
            StoreFactory factory = StoreFactory.create();
            store = factory.createStore("hcss.phs.database");
            CoreFactory coreFactory = CoreFactoryManager.getInstance();
            MissionManager manager = coreFactory.createMissionManager();
            Mission mission = manager.getInstance();
            store.begin();
            for (Iterator iterator = mission.getConfigurationNames(); iterator.hasNext(); ) {
                String missionConfigName = (String) iterator.next();
                InfoList il = new InfoList();
                MissionConfiguration missionConfig = mission.getMissionConfiguration(missionConfigName);
                il.addInfo("MIB associated", missionConfig.getMibVersionLabel());
                ht.put(missionConfigName, il);
            }
            store.abort();
        } catch (StoreException ex) {
            logger.warning("StoreException:" + getStackTrace(ex));
        } catch (Exception ex) {
            logger.warning("Exception:" + getStackTrace(ex));
        } finally {
            logger.warning("Closing the store");
            if (store != null) {
                store.exit();
            }
        }
    }

    private Object[] retrieveObservationRequest(ObservationRequest or) throws Exception {
        long id = or.getId();
        String obMode = or.getObservingMode().getName();
        InfoList il = new InfoList();
        il.addInfo("Id", or.getId() + "");
        il.addInfo("Instrument", or.getInstrument().name());
        il.addInfo("Subinstrument", or.getSubInstrument().name());
        il.addInfo("Target", or.getTarget().getName());
        il.addInfo("Target type", or.getTarget().getType());
        il.addInfo("Observing mode", obMode);
        if (or.getTarget().getType().equals(or.getTarget().FIXED_SINGLE)) {
            TargetFixedSingle tg = (TargetFixedSingle) or.getTarget();
            EquatorialCoordinates ec = (EquatorialCoordinates) tg.getPosition();
            il.addInfo("Proper motion lat", ec.getProperMotion().getLatPm() + "");
            il.addInfo("Proper motion lon", ec.getProperMotion().getLonPm() + "");
        }
        il.addInfo("Observing Mode Instrument", or.getObservingMode().getCommandSet().getInstrumentModel().getInstrumentName());
        il.addInfo("Mission Configuration", or.getObservingMode().getCommandSet().getInstrumentModel().getMissionConfigurationName());
        il.addInfo("Version", or.getVersionNumber() + "");
        il.addInfo("Chopper avoidance angles ", avoidanceAnglesToString(or.getChopperAvoidanceAngles()));
        il.addInfo("Map avoidance angles ", avoidanceAnglesToString(or.getMapAvoidanceAngles()));
        Estimates es = or.getEstimates();
        if (es != null) {
            il.addInfo("Estimation class", es.getClass().getCanonicalName());
            il.addInfo("Estimation time", Long.toString(es.getTimeEstimate()));
            il.addInfo("Estimation overhead", Long.toString(es.getOverhead()));
            il.addInfo("Estimation overhead", es.getVersion());
        } else {
            il.addInfo("Estimation", "not available");
        }
        CommandSet cmdSet = or.getObservingMode().getCommandSet();
        CusDefinition cdef = cmdSet.getCusDefinition(obMode);
        ParameterSet cusPSet = cdef.getParameterSet();
        il.addInfo("Schedulable", or.isSchedulable() ? "Yes" : "No");
        il.addInfo("Status", (String) OR_STATUS_STRINGS.get(or.getStatus()));
        InfoList psetList = new InfoList();
        for (Iterator iter = or.getParameterSet().iterator(); iter.hasNext(); ) {
            Parameter element = (Parameter) iter.next();
            String prefix = "[+]";
            String subfix = "";
            if (cusPSet.lookup(element.getName()) != null) {
                prefix = "<html>[*]<b>";
                subfix = "</b>";
            }
            psetList.addInfo(prefix + element.getName() + subfix, element.value().toString());
        }
        StringBuffer absent = new StringBuffer();
        for (Iterator iter = cusPSet.iterator(); iter.hasNext(); ) {
            Parameter element = (Parameter) iter.next();
            if (or.getParameterSet().lookup(element.getName()) == null) {
                absent.append((absent.length() > 0 ? ", " : "") + element.getName());
            }
        }
        if (absent.length() > 0) {
            psetList.addInfo("MISSING FROM CUS", absent.toString());
            il.addInfo("Compliant with cus definition", "<html><font style=\"color: red; \">NO</font>");
        } else {
            il.addInfo("Compliant with cus definition", "<html><font style=\"color: green; \">YES</font>");
        }
        Vector constraintList = new Vector();
        Iterator it = or.getConstraints();
        while (it.hasNext()) {
            InfoList constraintInfo = new InfoList();
            ObservationConstraint element = (ObservationConstraint) it.next();
            constraintInfo.addInfo("Name", element.getName());
            constraintInfo.addInfo("Type", element.getType().name());
            int nobs = 1;
            for (Iterator iter = element.getObservationRequests(); iter.hasNext(); ) {
                ObservationRequest constObs = (ObservationRequest) iter.next();
                constraintInfo.addInfo("Observation " + nobs, constObs.getTitle() + " (v " + constObs.getVersionNumber() + ")");
                nobs++;
            }
            constraintList.add(constraintInfo);
        }
        InfoList fixedTimeList = null;
        if (or.getFixedTimeConstraint() != null) {
            FixedTimeConstraint ftc = or.getFixedTimeConstraint();
            fixedTimeList = new InfoList();
            TimeInterval[] tms = ftc.getTimeIntervals();
            for (int j = 0; j < tms.length; j++) {
                TimeInterval timeInterval = tms[j];
                fixedTimeList.addInfo("Time interval " + j, "From " + timeInterval.getStartTime().toString() + " to " + timeInterval.getEndTime().toString());
            }
        }
        Object[] prevVersion = null;
        String showPreviousVersionProp = PropertyHandler.getInstance().getProperty("show.previousVersion");
        boolean showPreviousVersion = showPreviousVersionProp != null && showPreviousVersionProp.equalsIgnoreCase("true");
        if (showPreviousVersion) {
            int versionNumber = or.getVersionNumber();
            if (versionNumber > 1) {
                ObservationRequest nr = or.getVersion(versionNumber - 1);
                try {
                    prevVersion = retrieveObservationRequest(nr);
                } catch (Exception e) {
                    prevVersion = generateExceptionInfo(e, or);
                }
            }
        }
        return new Object[] { il, psetList, constraintList, fixedTimeList, prevVersion };
    }

    private void retrieveProposal(Proposal p, InfoList il, Vector<HistoryRevision> hr) {
        il.clear();
        il.addInfo("Title", p.getTitle());
        il.addInfo("Version", p.getVersionNumber() + "");
        if (hr != null) {
            for (int i = p.getVersionNumber(); i > 0; i--) {
                Proposal pold = p.getVersion(i);
                HistoryRevision rev = new HistoryRevision(i, DATE_FORMATTER.format(pold.getVersionHistory().getDate()), pold.getVersionHistory().getAuthor(), pold.getVersionHistory().getComment());
                hr.add(rev);
            }
        }
        il.addInfo("Proposal Status", (String) PROP_STATUS_STRINGS.get(p.getProposalStatus()));
        il.addInfo("Hotac comment", p.getHotacComment());
        NumberFormat nf = NumberFormat.getInstance();
    }

    private String avoidanceAnglesToString(double[] angles) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < angles.length; i += 2) {
            double from = angles[i];
            double to = angles[i + 1];
            sb.append("[" + from + "," + to + "]");
        }
        return sb.toString();
    }

    private String constraintToString(ObservationConstraint oc) {
        StringBuffer sb = new StringBuffer();
        sb.append("[" + oc.getType().name() + "]");
        sb.append(oc.getName() + ": ");
        Iterator<ObservationRequest> ors = oc.getObservationRequests();
        while (ors.hasNext()) {
            ObservationRequest observationRequest = (ObservationRequest) ors.next();
            sb.append(observationRequest.getTitle() + " (v " + observationRequest.getVersionNumber() + ")");
        }
        return sb.toString();
    }

    private Object[] generateExceptionInfo(Exception e, ObservationRequest or) {
        InfoList il = new InfoList();
        il.addInfo("01_Title", or.getTitle());
        il.addInfo("03_Instrument", or.getInstrument().name());
        il.addInfo("04_Subinstrument", or.getSubInstrument().name());
        il.addInfo("06_Stack", getStackTrace(e));
        try {
            il.addInfo("02_Observing mode", or.getObservingMode().getName());
            il.addInfo("05_Mission Configuration", or.getObservingMode().getCommandSet().getInstrumentModel().getMissionConfigurationName());
        } catch (Exception ex) {
            il.addInfo("02_Observing mode", "UNKNOW");
        }
        return new Object[] { new Exception(e), il };
    }

    public int countObservations(String programmeName, String proposalName, String observationName) {
        int numObs = 0;
        ObjectStore store = null;
        try {
            StoreFactory factory = StoreFactory.create();
            store = factory.createStore("hcss.phs.database");
            CoreFactory coreFactory = CoreFactoryManager.getInstance();
            MissionManager manager = coreFactory.createMissionManager();
            Mission mission = manager.getInstance();
            store.begin();
            ObservationProgramme programme = mission.getObservationProgrammeList().getObservationProgramme(programmeName);
            if (programme == null) {
                throw new RuntimeException("Unknown programme:" + programmeName);
            }
            Iterator<Proposal> proposals = programme.proposalIterator();
            while (proposals.hasNext()) {
                Proposal p = proposals.next();
                if (proposalName == null || p.getProposalId().equals(proposalName)) {
                    Iterator<ObservationRequest> ors = p.observationRequestIterator();
                    while (ors.hasNext()) {
                        ObservationRequest or = ors.next();
                        if (observationName == null || or.getTitle().equals(observationName)) {
                            numObs++;
                        }
                    }
                }
            }
            store.abort();
        } catch (Exception ex) {
            System.out.println("Exception:" + getStackTrace(ex));
        } finally {
            if (store != null) {
                store.exit();
            }
        }
        return numObs;
    }
}
