package org.epoline.phoenix.dosman;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;
import org.epo.print.local.IntHolder;
import org.epo.utils.EPODate;
import org.epo.utils.ImageFormat;
import org.epo.utils.PXIQueryDocument;
import org.epo.utils.PageRange;
import org.epoline.backend.utils.Strings;
import org.epoline.phoenix.authentication.shared.UserAuthenticator;
import org.epoline.phoenix.authentication.shared.UserProfile;
import org.epoline.phoenix.authorization.LocalAuthorization;
import org.epoline.phoenix.backend.BckUow;
import org.epoline.phoenix.backend.DmsLinkException;
import org.epoline.phoenix.backend.TSException;
import org.epoline.phoenix.common.UtilService;
import org.epoline.phoenix.common.backend.BackendUtils;
import org.epoline.phoenix.common.shared.EnumPackageStatus;
import org.epoline.phoenix.common.shared.PhoenixDmsException;
import org.epoline.phoenix.common.shared.PhoenixException;
import org.epoline.phoenix.common.shared.PhoenixInvalidDataException;
import org.epoline.phoenix.common.shared.PhoenixObjectNotFoundException;
import org.epoline.phoenix.dms.DMSDocctl;
import org.epoline.phoenix.dms.DMSDocnote;
import org.epoline.phoenix.dms.DMSDocument;
import org.epoline.phoenix.dms.DMSDoscount;
import org.epoline.phoenix.dms.DMSDossier;
import org.epoline.phoenix.dms.DMSPackage;
import org.epoline.phoenix.dms.DMSParty;
import org.epoline.phoenix.dms.DMSPckhis;
import org.epoline.phoenix.dms.DMSProcedure;
import org.epoline.phoenix.dms.DMSProcgroup;
import org.epoline.phoenix.dms.DMSSoftbatch;
import org.epoline.phoenix.dms.DMSTeam;
import org.epoline.phoenix.dosman.shared.RedateProgressEvent;
import org.epoline.phoenix.dossiernotepad.shared.ItemDocument;
import org.epoline.phoenix.general.PrintServiceFacade;
import org.epoline.phoenix.printing.shared.PhoenixNoServerException;
import org.epoline.print.shared.PrintDocument;
import org.epoline.print.shared.PrintDocumentSet;
import org.epoline.print.shared.SoftCopyJob;

/**
 * Command object that incapsulates redating routine. 
 * Parameters for redating are passed via constructor.  
 * 
 * $Id: Redater.java,v 1.1.1.1 2005/04/27 21:23:45 rcruse Exp $
 */
class Redater implements Runnable {

    private static final String SOFT_COPY_JOB_DOCUMENT_SOURCE_COPY = "COPY";

    private static final String PROCEDURE_NAME_UNKNOWN = "UNKNOWN";

    private static final String PARTICIPANT_NAME_APPLICANT = "Applicant";

    private static final String SOFT_COPY_JOB_NAME = "P30WS00";

    private static final String PRINT_JOB_DOCUMENT_SOURCE_PHX = "PHX";

    private UserAuthenticator _user;

    private String _dossierKey;

    private List _documents;

    private Date _newDate;

    private DateFormat _dateFormat;

    private List _progressEvents = new ArrayList();

    private static Logger _logger = Logger.getLogger(Redater.class);

    Redater(UserAuthenticator user, String dossierKey, List documents, Date newDate, DateFormat dateFormat) {
        _user = user;
        _dossierKey = dossierKey;
        _documents = documents;
        _newDate = newDate;
        _dateFormat = dateFormat;
    }

    public void run() {
        try {
            redateDocuments(_user, _dossierKey, _documents, _newDate, _dateFormat);
        } catch (PhoenixException e) {
            throw new NestableRuntimeException("Error during redating. cause: " + e, e);
        }
    }

    /**
	 * @return the latest set of RedateProgressEvents. Cleans the list of current RedateProgressEvents 
	 * before returning. So that next time user asks for the status it woun't get previous events any more.
	 */
    synchronized List getProgress() {
        List progress = new ArrayList(_progressEvents);
        _progressEvents.clear();
        return progress;
    }

    private synchronized void addProgressEvent(RedateProgressEvent progressEvent) {
        _progressEvents.add(progressEvent);
    }

    private void redateDocuments(UserAuthenticator user, String dossierKey, List documents, Date newDate, DateFormat dateFormat) throws PhoenixException {
        RedateProgressEvent progressEvent = new RedateProgressEvent("", RedateProgressEvent.PROCESSING_STARTED);
        addProgressEvent(progressEvent);
        BckUow uow = new BckUow();
        try {
            DMSDossier dmsDossier = BackendUtils.getInstance().getDMSDossierWithAggregatesByKey(dossierKey, uow);
            List packagesToClose = new ArrayList();
            UserProfile userProfile = LocalAuthorization.getInstance().getUserProfile(user);
            String currentUser = userProfile.getUserId();
            DMSTeam dmsTeam = null;
            String mailboxId = dmsTeam == null ? null : dmsTeam.getOrgMbxID().trim();
            String dateString = dateFormat.format(newDate);
            String targetUser = null;
            for (Iterator iter = documents.iterator(); iter.hasNext(); ) {
                ItemDocument document = (ItemDocument) iter.next();
                String dockey = document.getKey();
                DMSDocument sourceDmsDocument = DMSDocument.getByKey(dockey);
                if (sourceDmsDocument == null) {
                    throw new PhoenixObjectNotFoundException("document with key: " + dockey);
                }
                DMSPackage sourceDmsPackage = null;
                SoftCopyJob softJob = null;
                try {
                    sourceDmsPackage = sourceDmsDocument.getOwnerPackage();
                    DMSDocctl dmsDoccontrol = UtilService.getDmsCache().getDMSDocctlByKey(sourceDmsDocument.getDOCDCTKEY());
                    String packageID = Strings.padSpaces(sourceDmsPackage.getPckPXI(), 17);
                    if (dmsTeam == null) {
                        DMSTeam tempDmsTeam = dmsDoccontrol.getOwnerTeam();
                        if (tempDmsTeam != null) {
                            mailboxId = tempDmsTeam.getOrgMbxID().trim();
                        }
                    }
                    String newPackageID = null;
                    DMSPackage newDmsPackage = null;
                    PXIQueryDocument docQuery = new PXIQueryDocument(packageID);
                    ImageFormat format = ImageFormat.getImageFormatFromTapeMode(docQuery.getTapeMode());
                    int urgency = dmsDoccontrol.getDctUrgency();
                    String messageText = "";
                    boolean createMessage = false;
                    int sublogic = dmsDoccontrol.getDctIndSublogic();
                    DMSProcgroup sourceDmsProcGroup = sourceDmsDocument.getOwnerParty().getOwnerProcgroup();
                    DMSProcedure dmsProcedure = sourceDmsProcGroup.getOwnerProcedure();
                    if (!dmsDoccontrol.getDCTPRCKEY().equals(sourceDmsProcGroup.getPRGPRCKEY())) {
                        sublogic = UtilService.getDmsCache().getSublogic(dmsProcedure);
                    }
                    newDmsPackage = createPackage(dmsDossier, 0, sourceDmsDocument.getDocPages(), EnumPackageStatus.SFTINDEXED.getCode(), SOFT_COPY_JOB_DOCUMENT_SOURCE_COPY, format, createMessage, messageText, dmsDoccontrol.getDctCode(), new EPODate(newDate), urgency, targetUser, mailboxId, sublogic, currentUser, uow);
                    newPackageID = Strings.padSpaces(newDmsPackage.getPckPXI(), 17);
                    int packageSequenceNumber = 1;
                    int offsetInPackage = 1;
                    DMSProcgroup dmsProcgroup = getOrCreateProceduralGroup(dmsProcedure, dmsDossier, uow);
                    DMSParty dmsParty = getOrCreateDefaultParty(dmsProcgroup, uow);
                    String[] notes = getDocumentNotes(sourceDmsDocument);
                    DMSDocument newDmsDocument = createDocument(newDmsPackage, dmsParty, dmsDoccontrol, sourceDmsDocument.getDocPages(), offsetInPackage, packageSequenceNumber, sourceDmsDocument.getDocCountryAbbr(), notes, uow);
                    softJob = new SoftCopyJob(currentUser, SOFT_COPY_JOB_NAME, newPackageID);
                    softJob.setPassword(userProfile.getPassword());
                    softJob.setSource(SOFT_COPY_JOB_DOCUMENT_SOURCE_COPY);
                    PrintDocumentSet docSet = new PrintDocumentSet();
                    PageRange pageRange = new PageRange();
                    pageRange.addPage(sourceDmsDocument.getDocPckOffset(), sourceDmsDocument.getDocPages());
                    PrintDocument printDoc = new PrintDocument(PRINT_JOB_DOCUMENT_SOURCE_PHX, packageID, pageRange);
                    docSet.addDocument(printDoc);
                    softJob.addDocumentSet(docSet);
                    PrintServiceFacade.getInstance().print(softJob);
                    sourceDmsDocument.setDocFlagDel(true);
                    sourceDmsDocument.updateWhenDirty(uow);
                    List dmsDocs = sourceDmsPackage.getDocumentChildren(uow);
                    boolean allClosed = true;
                    Iterator closeIter = dmsDocs.iterator();
                    while (closeIter.hasNext()) {
                        DMSDocument dmsDc = (DMSDocument) closeIter.next();
                        if (!dmsDc.getDocFlagDel()) {
                            allClosed = false;
                            break;
                        }
                    }
                    if (allClosed) {
                        BackendUtils.getInstance().closePackage(sourceDmsPackage, currentUser, uow);
                    }
                    uow.commit();
                    progressEvent = new RedateProgressEvent(document.getOwnerDoccontrol().getCode() + " new date:" + dateFormat.format(newDate), RedateProgressEvent.DOCUMENT_PROCESSED);
                    addProgressEvent(progressEvent);
                } catch (DmsLinkException x) {
                    addProgressEvent(new RedateProgressEvent(document.getOwnerDoccontrol().getCode() + " ", RedateProgressEvent.DOCUMENT_FAILED));
                    throw x;
                } catch (PhoenixNoServerException x) {
                    addProgressEvent(new RedateProgressEvent(document.getOwnerDoccontrol().getCode() + " - print manager failure.", RedateProgressEvent.DOCUMENT_FAILED));
                    throw x;
                }
            }
            progressEvent = new RedateProgressEvent("", RedateProgressEvent.PROCESSING_FINISHED);
            addProgressEvent(progressEvent);
        } catch (DmsLinkException x) {
            addProgressEvent(new RedateProgressEvent(" dms exception", RedateProgressEvent.PROCESSING_FAILED));
            rollback(uow);
            throw new PhoenixDmsException(x.toString());
        } catch (PhoenixException x) {
            addProgressEvent(new RedateProgressEvent(" service exception", RedateProgressEvent.PROCESSING_FAILED));
            rollback(uow);
            throw x;
        } catch (RuntimeException x) {
            addProgressEvent(new RedateProgressEvent(" runtime exception", RedateProgressEvent.PROCESSING_FAILED));
            rollback(uow);
            throw x;
        } catch (Exception x) {
            addProgressEvent(new RedateProgressEvent(" dms exception", RedateProgressEvent.PROCESSING_FAILED));
            rollback(uow);
            throw new PhoenixException(x.toString());
        }
    }

    private DMSProcgroup getOrCreateProceduralGroup(DMSProcedure procedure, DMSDossier dossier, BckUow uow) throws DmsLinkException, PhoenixException {
        int totalProcedures = 0;
        if (procedure != null) {
            String procedureKey = procedure.getPRCKEY();
            for (Iterator iter = dossier.getProcgroupChildren().iterator(); iter.hasNext(); ) {
                totalProcedures++;
                DMSProcgroup dmsProcgroup = (DMSProcgroup) iter.next();
                if (dmsProcgroup.getPRGPRCKEY().equals(procedureKey) && (dmsProcgroup.getPrgFlagOpen() || !procedure.getPrcFlagDuplicate())) {
                    return dmsProcgroup;
                }
            }
        } else {
            for (Iterator iter = dossier.getProcgroupChildren().iterator(); iter.hasNext(); ) {
                totalProcedures++;
                DMSProcgroup dmsProcgroup = (DMSProcgroup) iter.next();
                if (dmsProcgroup.getPrgFlagDefault()) {
                    return dmsProcgroup;
                }
                procedure = getUnknownProcedure();
                if (procedure == null) {
                    throw new org.epoline.phoenix.backend.DmsNotFoundException("Procedure named " + PROCEDURE_NAME_UNKNOWN);
                }
            }
        }
        DMSProcgroup result = new DMSProcgroup();
        result.setPrgFlagOpen(true);
        result.setPrgFlagDefault(totalProcedures == 0);
        result.setPrgPhxName(procedure.getPrcPhxName());
        result.setPrgSeqNum(totalProcedures);
        result.setPRGPRCKEY(procedure.getKey());
        dossier.addChild(result, uow);
        return result;
    }

    private DMSParty getOrCreateDefaultParty(DMSProcgroup dmsProcgroup, BckUow uow) throws DmsLinkException {
        for (Iterator iter = dmsProcgroup.getPartyChildren().iterator(); iter.hasNext(); ) {
            DMSParty dmsParty = (DMSParty) iter.next();
            if (dmsParty.getParSeqNum() == 0) {
                return dmsParty;
            }
        }
        DMSParty result = new DMSParty();
        result.setParFullName(PARTICIPANT_NAME_APPLICANT);
        result.setParSeqNum(0);
        dmsProcgroup.addChild(result, uow);
        return result;
    }

    /**
	 * Answer an array of three Strings: annotation, annotationAPublication,
	 * annotationBPublication; these are converted from DMSDocnotes belonging to
	 * the given DMSDocument. An absent DMSDocnote results in an empty string in
	 * the array.
	 */
    private String[] getDocumentNotes(DMSDocument dmsDocument) throws DmsLinkException, PhoenixException {
        String[] result = new String[3];
        for (int i = 0; i < result.length; i++) {
            result[i] = "";
        }
        if (dmsDocument.getDocnoteManager() == null) {
            return result;
        }
        Enumeration dmsNotes = dmsDocument.getDocnoteItems();
        while (dmsNotes.hasMoreElements()) {
            DMSDocnote note = (DMSDocnote) dmsNotes.nextElement();
            int index = note.getDntIndType();
            if (index < 0 || index >= 3) {
                new PhoenixInvalidDataException("Illegal document note indType encountered; ignored. Dossier: " + " package: " + dmsDocument.getOwnerPackage().getPckPXI() + " document: " + dmsDocument.getOwnerDocctl().getDctCode() + " offset  : " + dmsDocument.getDocPckOffset() + " document key: " + dmsDocument.getKey() + " note key: " + note.getKey()).printStackTrace();
            } else {
                result[index] = note.getDntText128().trim();
            }
        }
        return result;
    }

    private DMSDocument createDocument(DMSPackage dmsPackage, DMSParty party, DMSDocctl dmsDocctl, int pages, int pckOffset, int pckSeqNumber, String country, String[] annotationAndOtherNotes, BckUow uow) throws DmsLinkException {
        DMSDocument doc = new DMSDocument();
        doc.setDocPages(pages);
        doc.setDocIndPublic(dmsDocctl.getDctIndPublic());
        doc.setDocPckSeqNumber(pckSeqNumber);
        doc.setDocUser("");
        doc.setDocIndStatus(0);
        doc.setDocFlagDel(false);
        doc.setDocPckOffset(pckOffset);
        doc.setDocFlagIndex(dmsDocctl.getDctFlagFinalIdx());
        doc.setDocIndPUBA(dmsDocctl.getDctIndPub());
        doc.setDocIndPUBB(dmsDocctl.getDctIndPub());
        doc.setDocDatePUBA(" ");
        doc.setDocDatePUBB(" ");
        doc.setDocFlagModel(false);
        doc.setDocModelSeqNum(0);
        doc.setDocModelRoom("");
        doc.setDocModelBox("");
        doc.setDocCountryAbbr(country);
        doc.setDocFlagExtra1(false);
        doc.setDocFlagExtra2(false);
        doc.setDocFlagExtra3(false);
        doc.setDocFlagExtra4(false);
        doc.setDocFlagExtra5(false);
        doc.setDocFlagExtra6(false);
        doc.setDocFlagExtra7(false);
        doc.setDocFlagExtra8(false);
        doc.setDocFlagIndex(true);
        doc.setDocFrmIdCode("");
        doc.setDocFrmLang("");
        doc.setOwnerDocctl(dmsDocctl);
        doc.setOwnerParty(party);
        dmsPackage.addChild(doc, uow);
        for (int i = 0; i < 3; i++) {
            String annotationOrOtherNote = annotationAndOtherNotes[i];
            if (annotationOrOtherNote.length() > 0) {
                DMSDocnote note = new DMSDocnote();
                note.setDntIndType(i);
                note.setDntText128(annotationOrOtherNote);
                doc.addChild(note, uow);
            }
        }
        return doc;
    }

    private DMSPackage createPackage(DMSDossier aDossier, int logPackage, int pageCount, int status, String source, ImageFormat format, boolean createMessage, String message, String code, EPODate date, int urgency, String targetUser, String targetMailbox, int subLogic, String issueingUser, BckUow uow) throws DmsLinkException, PhoenixException {
        IntHolder totalPackages = new IntHolder();
        String id = reservePackageID(aDossier, format, totalPackages, uow);
        boolean flagMessage = createMessage || message != null && message.trim().length() > 0;
        int logNumber = logPackage != 0 ? logPackage : BackendUtils.convertString12_YZToInt(id.substring(12, 14));
        String packageStatusKey = UtilService.getDmsCache().getDMSPckstatByCode(status).getKey();
        DMSPackage newPackage = new DMSPackage();
        newPackage.setPckOriAppNumber(aDossier.getDosOriNumber());
        newPackage.setPckDateFormal(date.asSqlDate());
        newPackage.setPckAnnotation("");
        newPackage.setPckMsgText(message);
        newPackage.setPckMsgDocCode(code);
        newPackage.setPckPages(pageCount);
        newPackage.setPckUrgency(urgency);
        newPackage.setPckFlagTransl(false);
        newPackage.setPckFlagMsg(flagMessage);
        newPackage.setPckFlagDel(false);
        newPackage.setPckFlagError(false);
        newPackage.setPckBchSeqNumber(0);
        newPackage.setPckUserTarget(targetUser);
        newPackage.setPckSource(source);
        newPackage.setPckMbxTarget(targetMailbox);
        newPackage.setPckIndSublogic(subLogic);
        newPackage.setPCKPSTKEY(packageStatusKey);
        newPackage.setPckPXI(id);
        newPackage.setPckSeqNumber(totalPackages.value);
        newPackage.setPckLogNumber(logNumber);
        if (!source.equals("SCAN")) {
            DMSSoftbatch batch = getOrCreateSoftbatch(source, uow);
            newPackage.setPCKSFTKEY(batch.key());
        }
        DMSPckhis history = new DMSPckhis();
        history.setPhsTime(new Timestamp(System.currentTimeMillis()));
        history.setPhsOriStatus(0);
        history.setPhsNewStatus(status);
        history.setPhsUser(issueingUser);
        aDossier.addChild(newPackage, uow);
        newPackage.addChild(history, uow);
        return newPackage;
    }

    private String reservePackageID(DMSDossier aDossier, ImageFormat format, IntHolder totalPackages, BckUow uow) throws DmsLinkException {
        DMSDoscount count = null;
        int max = 0;
        for (Iterator iter = aDossier.getPackageChildren().iterator(); iter.hasNext(); ) {
            DMSPackage cur = (DMSPackage) iter.next();
            if (cur.getPckPXI() != null) {
                int seqNr = BackendUtils.convertString12_YZToInt(cur.getPckPXI().substring(12, 14));
                if (seqNr > max) max = seqNr;
            } else {
                throw new DmsLinkException("Dossier has invalid package: " + cur.toString());
            }
        }
        try {
            aDossier.retrieveDoscountChildren(true, uow);
            count = (DMSDoscount) aDossier.getDoscountChildren().get(0);
        } catch (java.util.NoSuchElementException e) {
            throw new DmsLinkException("Dossier doesn't have a DosCount object: " + aDossier.toString());
        }
        int seqNr = BackendUtils.convertString12_YZToInt(count.getDcnPckCounter());
        if (seqNr >= 36 * 36) {
            throw new DmsLinkException("Dossier doesn't have  packageIds left: " + aDossier.toString());
        }
        seqNr = Math.max(seqNr, max);
        count.setDcnPckCounter(BackendUtils.convertIntToString12_YZ(++seqNr));
        totalPackages.value = seqNr;
        count.update(uow);
        String tapeMode = format.getTapeMode();
        String result = count.getDcnTypeAsString() + count.getDcnCountryAsString() + count.getDcnPckStemAsString() + BackendUtils.convertIntToString12_YZ(seqNr) + tapeMode;
        return Strings.padSpaces(result, 17);
    }

    private DMSSoftbatch getOrCreateSoftbatch(String aSource, BckUow uow) throws DmsLinkException {
        EPODate now = new EPODate(0, 0, 0);
        java.sql.Timestamp sqlNow = new java.sql.Timestamp(now.asLong());
        DMSSoftbatch result = DMSSoftbatch.getByTimeSource(sqlNow, aSource, uow);
        if (result == null) {
            result = new DMSSoftbatch();
            result.setSftSource(aSource);
            result.setSftTime(sqlNow);
            result.add(uow);
        }
        return result;
    }

    private DMSProcedure getUnknownProcedure() throws PhoenixException {
        return UtilService.getDmsCache().getDMSProcedureByName(PROCEDURE_NAME_UNKNOWN);
    }

    private void rollback(BckUow uow) {
        try {
            uow.rollback();
        } catch (TSException x) {
            _logger.error("Error during rollback", x);
        }
    }
}
