package org.dcm4chex.cdw.mbean;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import javax.jms.JMSException;
import javax.management.Attribute;
import org.dcm4che.data.Command;
import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmElement;
import org.dcm4che.data.DcmObjectFactory;
import org.dcm4che.data.FileFormat;
import org.dcm4che.data.FileMetaInfo;
import org.dcm4che.dict.Status;
import org.dcm4che.dict.Tags;
import org.dcm4che.dict.UIDs;
import org.dcm4che.net.ActiveAssociation;
import org.dcm4che.net.Association;
import org.dcm4che.net.DcmService;
import org.dcm4che.net.DcmServiceBase;
import org.dcm4che.net.DcmServiceException;
import org.dcm4che.net.Dimse;
import org.dcm4chex.cdw.common.ExecutionStatus;
import org.dcm4chex.cdw.common.ExecutionStatusInfo;
import org.dcm4chex.cdw.common.FailureReason;
import org.dcm4chex.cdw.common.Flag;
import org.dcm4chex.cdw.common.JMSDelegate;
import org.dcm4chex.cdw.common.MediaCreationException;
import org.dcm4chex.cdw.common.MediaCreationRequest;
import org.dcm4chex.cdw.common.Priority;

/**
 * @author gunter.zeilinter@tiani.com
 * @version $Revision: 9805 $ $Date: 2009-02-16 07:47:16 -0500 (Mon, 16 Feb 2009) $
 * @since 22.06.2004
 *
 */
public class MediaCreationMgtScpService extends AbstractScpService {

    private static final String LOOKUP_MEDIA_WRITER_NAME = "lookupMediaWriterName";

    private static final int INITIATE = 1;

    private static final int CANCEL = 2;

    private static final String[] CUIDS = { UIDs.MediaCreationManagementSOPClass };

    private boolean keepSpoolFiles = false;

    private String defaultMediaApplicationProfile = "STD-GEN-CD";

    private String defaultRequestPriority = Priority.LOW;

    private String defaultFilesetID;

    private String mediaComposerQueueName;

    private DecimalFormat defaultFilesetIDFormat;

    private int nextDefaultFilesetIDSeqno = 1;

    private int maxNumberOfCopies = 10;

    private boolean defaultLabelUsingInformationExtractedFromInstances = true;

    private String defaultLabelText = "";

    private String defaultLabelStyleSelection = "";

    private boolean defaultAllowMediaSplitting = false;

    private boolean defaultAllowLossyCompression = false;

    private String defaultIncludeNonDICOMObjects = "NO";

    private boolean defaultIncludeDisplayApplication = false;

    private boolean defaultPreserveInstances = false;

    private boolean allowCancelAlreadyCreating = true;

    private boolean tolerateDuplicateRefSOPInstances = false;

    private final DcmService service = new DcmServiceBase() {

        protected Dataset doNCreate(ActiveAssociation assoc, Dimse rq, Command rspCmd) throws IOException, DcmServiceException {
            return MediaCreationMgtScpService.this.doNCreate(assoc, rq, rspCmd);
        }

        protected Dataset doNAction(ActiveAssociation assoc, Dimse rq, Command rspCmd) throws IOException, DcmServiceException {
            return MediaCreationMgtScpService.this.doNAction(assoc, rq, rspCmd);
        }

        protected Dataset doNGet(ActiveAssociation assoc, Dimse rq, Command rspCmd) throws IOException, DcmServiceException {
            return MediaCreationMgtScpService.this.doNGet(assoc, rq, rspCmd);
        }
    };

    private int maxReadRequestRetries;

    private long readRequestRetryInterval;

    public final boolean isKeepSpoolFiles() {
        return keepSpoolFiles;
    }

    public final boolean isTolerateDuplicateRefSOPInstances() {
        return tolerateDuplicateRefSOPInstances;
    }

    public final void setTolerateDuplicateRefSOPInstances(boolean allowDuplicateRefSOPInstances) {
        this.tolerateDuplicateRefSOPInstances = allowDuplicateRefSOPInstances;
    }

    public final void setKeepSpoolFiles(boolean keepSpoolFiles) {
        this.keepSpoolFiles = keepSpoolFiles;
    }

    public final String getDefaultMediaApplicationProfile() {
        return defaultMediaApplicationProfile;
    }

    public final void setDefaultMediaApplicationProfile(String profile) {
        this.defaultMediaApplicationProfile = profile;
    }

    public final String getDefaultFilesetID() {
        return defaultFilesetID;
    }

    public final void setDefaultFilesetID(String defaultFilesetID) {
        if (defaultFilesetID.indexOf('0') != -1 || defaultFilesetID.indexOf('#') != -1) {
            defaultFilesetIDFormat = new DecimalFormat(defaultFilesetID);
        }
        this.defaultFilesetID = defaultFilesetID;
    }

    public final int getNextDefaultFilesetIDSeqno() {
        return nextDefaultFilesetIDSeqno;
    }

    public final void setNextDefaultFilesetIDSeqno(int seqno) {
        this.nextDefaultFilesetIDSeqno = seqno;
    }

    public final boolean isAllowCancelAlreadyCreating() {
        return allowCancelAlreadyCreating;
    }

    public final void setAllowCancelAlreadyCreating(boolean allowCancelAlreadyCreating) {
        this.allowCancelAlreadyCreating = allowCancelAlreadyCreating;
    }

    public final boolean isDefaultAllowLossyCompression() {
        return defaultAllowLossyCompression;
    }

    public final void setDefaultAllowLossyCompression(boolean defaultAllowLossyCompression) {
        this.defaultAllowLossyCompression = defaultAllowLossyCompression;
    }

    public final boolean isDefaultAllowMediaSplitting() {
        return defaultAllowMediaSplitting;
    }

    public final void setDefaultAllowMediaSplitting(boolean defaultAllowMediaSplitting) {
        this.defaultAllowMediaSplitting = defaultAllowMediaSplitting;
    }

    public final boolean isDefaultIncludeDisplayApplication() {
        return defaultIncludeDisplayApplication;
    }

    public final void setDefaultIncludeDisplayApplication(boolean defaultIncludeDisplayApplication) {
        this.defaultIncludeDisplayApplication = defaultIncludeDisplayApplication;
    }

    public final String getDefaultIncludeNonDICOMObjects() {
        return defaultIncludeNonDICOMObjects;
    }

    public final void setDefaultIncludeNonDICOMObjects(String defaultIncludeNonDICOMObjects) {
        this.defaultIncludeNonDICOMObjects = defaultIncludeNonDICOMObjects;
    }

    public final String getDefaultLabelStyleSelection() {
        return defaultLabelStyleSelection;
    }

    public final void setDefaultLabelStyleSelection(String defaultLabelStyleSelection) {
        this.defaultLabelStyleSelection = defaultLabelStyleSelection;
    }

    public final String getDefaultLabelText() {
        return defaultLabelText;
    }

    public final void setDefaultLabelText(String defaultLabelText) {
        this.defaultLabelText = defaultLabelText;
    }

    public final boolean isDefaultLabelUsingInformationExtractedFromInstances() {
        return defaultLabelUsingInformationExtractedFromInstances;
    }

    public final void setDefaultLabelUsingInformationExtractedFromInstances(boolean defaultLabelUsingInformationExtractedFromInstances) {
        this.defaultLabelUsingInformationExtractedFromInstances = defaultLabelUsingInformationExtractedFromInstances;
    }

    public final boolean isDefaultPreserveInstances() {
        return defaultPreserveInstances;
    }

    public final void setDefaultPreserveInstances(boolean defaultPreserveInstances) {
        this.defaultPreserveInstances = defaultPreserveInstances;
    }

    public final String getDefaultRequestPriority() {
        return defaultRequestPriority;
    }

    public final void setDefaultRequestPriority(String priority) {
        if (!Priority.isValid(priority)) throw new IllegalArgumentException("priority:" + priority);
        this.defaultRequestPriority = priority;
    }

    public final int getMaxNumberOfCopies() {
        return maxNumberOfCopies;
    }

    public final void setMaxNumberOfCopies(int maxNumberOfCopies) {
        if (maxNumberOfCopies < 1) throw new IllegalArgumentException("maxNumberOfCopies:" + maxNumberOfCopies);
        this.maxNumberOfCopies = maxNumberOfCopies;
    }

    public final int getMaxReadRequestRetries() {
        return maxReadRequestRetries;
    }

    public final void setMaxReadRequestRetries(int retries) {
        if (retries < 0) throw new IllegalArgumentException("retries: " + retries);
        this.maxReadRequestRetries = retries;
    }

    public final long getReadRequestRetryInterval() {
        return readRequestRetryInterval;
    }

    public final void setReadRequestRetryInterval(long interval) {
        if (interval < 0) throw new IllegalArgumentException("interval: " + interval);
        this.readRequestRetryInterval = interval;
    }

    protected void bindDcmServices() {
        bindDcmServices(CUIDS, service);
    }

    protected void unbindDcmServices() {
        unbindDcmServices(CUIDS);
    }

    protected void updatePresContexts() {
        putPresContexts(CUIDS, valuesToStringArray(tsuidMap));
    }

    protected void removePresContexts() {
        putPresContexts(CUIDS, null);
    }

    public final String getMediaComposerQueueName() {
        return mediaComposerQueueName;
    }

    public final void setMediaComposerQueueName(String mediaComposerQueueName) {
        this.mediaComposerQueueName = mediaComposerQueueName;
    }

    private Dataset doNCreate(ActiveAssociation assoc, Dimse rq, Command rspCmd) throws IOException, DcmServiceException {
        Command rqCmd = rq.getCommand();
        Dataset info = rq.getDataset();
        if (log.isDebugEnabled()) {
            DcmElement refSOPs = info.get(Tags.RefSOPSeq);
            String prompt = refSOPs == null ? "N-Create Information:\n" : "N-Create Information:\n(0008,1199) SQ #-1 *" + refSOPs.countItems() + " // Referenced SOP Sequence\n";
            logDataset(prompt, info.subSet(new int[] { Tags.RefSOPSeq }, true, true));
        }
        checkCreateAttributes(info, rspCmd);
        String iuid = rspCmd.getAffectedSOPInstanceUID();
        File f = spoolDir.getMediaCreationRequestFile(iuid);
        if (f.exists()) throw new DcmServiceException(Status.DuplicateSOPInstance);
        String cuid = rqCmd.getAffectedSOPClassUID();
        String tsuid = rq.getTransferSyntaxUID();
        DcmObjectFactory dof = DcmObjectFactory.getInstance();
        FileMetaInfo fmi = dof.newFileMetaInfo(cuid, iuid, tsuid);
        info.setFileMetaInfo(fmi);
        info.putUI(Tags.SOPInstanceUID, iuid);
        info.putUI(Tags.SOPClassUID, cuid);
        info.putCS(Tags.ExecutionStatus, ExecutionStatus.IDLE);
        info.putCS(Tags.ExecutionStatusInfo, ExecutionStatusInfo.NORMAL);
        try {
            info.writeFile(f, null);
        } catch (IOException e) {
            throw new DcmServiceException(Status.ProcessingFailure);
        }
        return info;
    }

    private void logDataset(String prefix, Dataset ds) {
        try {
            StringWriter w = new StringWriter();
            w.write(prefix);
            ds.dumpDataset(w, null);
            log.debug(w.toString());
        } catch (Exception e) {
            log.warn("Failed to dump dataset", e);
        }
    }

    private void checkCreateAttributes(Dataset ds, Command rspCmd) throws DcmServiceException {
        checkFlag(ds, Tags.LabelUsingInformationExtractedFromInstances, defaultLabelUsingInformationExtractedFromInstances, rspCmd);
        checkFlag(ds, Tags.AllowMediaSplitting, defaultAllowMediaSplitting, rspCmd);
        checkFlag(ds, Tags.AllowLossyCompression, defaultAllowLossyCompression, rspCmd);
        checkFlag(ds, Tags.IncludeDisplayApplication, defaultIncludeDisplayApplication, rspCmd);
        checkFlag(ds, Tags.PreserveCompositeInstancesAfterMediaCreation, defaultPreserveInstances, rspCmd);
        if (!ds.containsValue(Tags.LabelText)) ds.putUT(Tags.LabelText, defaultLabelText);
        if (!ds.containsValue(Tags.LabelText)) ds.putCS(Tags.LabelStyleSelection, defaultLabelStyleSelection);
        if (!ds.containsValue(Tags.IncludeNonDICOMObjects)) ds.putCS(Tags.IncludeNonDICOMObjects, defaultIncludeNonDICOMObjects);
        DcmElement refSOPs = ds.get(Tags.RefSOPSeq);
        if (refSOPs == null || refSOPs.countItems() == 0) throw new DcmServiceException(Status.MissingAttribute, "Missing or empty Referenced SOP Sequence");
        for (int i = 0, n = refSOPs.countItems(); i < n; ++i) {
            Dataset item = refSOPs.getItem(i);
            if (!item.containsValue(Tags.RefSOPInstanceUID)) throw new DcmServiceException(Status.MissingAttribute, "Missing Referenced SOP Instance UID");
            if (!item.containsValue(Tags.RefSOPClassUID)) throw new DcmServiceException(Status.MissingAttribute, "Missing Referenced SOP Class UID");
            if (!item.containsValue(Tags.RequestedMediaApplicationProfile)) item.putLO(Tags.RequestedMediaApplicationProfile, defaultMediaApplicationProfile);
        }
    }

    private void checkFlag(Dataset ds, int tag, boolean defval, Command rspCmd) {
        String s = ds.getString(tag);
        if (!Flag.isValid(s)) {
            rspCmd.putUS(Tags.Status, Status.AttributeValueOutOfRange);
            rspCmd.putLO(Tags.ErrorComment, "" + ds.get(tag));
            s = null;
        }
        if (s == null) ds.putCS(tag, Flag.valueOf(defval));
    }

    private void checkRequest(Dataset attrs) throws MediaCreationException {
        ArrayList missingSOPs = new ArrayList();
        HashSet profiles = new HashSet();
        LinkedHashMap checkForDuplicate = new LinkedHashMap();
        DcmElement refSOPs = attrs.get(Tags.RefSOPSeq);
        for (int i = 0, n = refSOPs.countItems(); i < n; ++i) {
            Dataset item = refSOPs.getItem(i);
            String cuid = item.getString(Tags.RefSOPClassUID);
            String iuid = item.getString(Tags.RefSOPInstanceUID);
            if (checkForDuplicate.put(iuid, item) != null) {
                log.warn("Duplicate referenced SOP Instance: " + iuid);
            }
            String profile = item.getString(Tags.RequestedMediaApplicationProfile);
            File f = spoolDir.getInstanceFile(iuid);
            if (!f.exists()) {
                log.warn("No Instance: " + iuid);
                DcmObjectFactory dof = DcmObjectFactory.getInstance();
                Dataset sop = dof.newDataset();
                sop.putUI(Tags.RefSOPClassUID, cuid);
                sop.putUI(Tags.RefSOPInstanceUID, iuid);
                sop.putUS(Tags.FailureReason, FailureReason.NoSuchObjectInstance);
                missingSOPs.add(sop);
            }
        }
        if (!missingSOPs.isEmpty()) throw new MediaCreationException(ExecutionStatusInfo.NO_INSTANCE, missingSOPs);
        if (checkForDuplicate.size() < refSOPs.countItems()) {
            if (!tolerateDuplicateRefSOPInstances) throw new MediaCreationException(ExecutionStatusInfo.DUPL_REF_INST);
            DcmElement corrRefSOPs = attrs.putSQ(Tags.RefSOPSeq);
            for (Iterator it = checkForDuplicate.values().iterator(); it.hasNext(); ) corrRefSOPs.addItem((Dataset) it.next());
        }
    }

    private Dataset doNAction(ActiveAssociation assoc, Dimse rq, Command rspCmd) throws IOException, DcmServiceException {
        Command rqCmd = rq.getCommand();
        Dataset actionInfo = rq.getDataset();
        if (log.isDebugEnabled()) logDataset("N-Action Information:\n", actionInfo);
        String iuid = rqCmd.getAffectedSOPInstanceUID();
        File f = spoolDir.getMediaCreationRequestFile(iuid);
        if (!f.exists()) throw new DcmServiceException(Status.NoSuchObjectInstance);
        Association a = assoc.getAssociation();
        MediaCreationRequest mcrq = new MediaCreationRequest(f, a.getCallingAET());
        Dataset attrs;
        try {
            attrs = mcrq.readAttributes(log);
        } catch (IOException e) {
            throw new DcmServiceException(Status.ProcessingFailure);
        }
        final String status = attrs.getString(Tags.ExecutionStatus, ExecutionStatus.IDLE);
        final int actionID = rqCmd.getInt(Tags.ActionTypeID, -1);
        switch(actionID) {
            case INITIATE:
                if (!ExecutionStatus.IDLE.equals(status)) throw new DcmServiceException(Status.DuplicateInitiateMediaCreation);
                int numberOfCopies = actionInfo != null ? actionInfo.getInt(Tags.NumberOfCopies, 1) : 1;
                if (numberOfCopies < 1 || numberOfCopies > maxNumberOfCopies) {
                    throw new DcmServiceException(Status.InvalidArgumentValue, "" + actionInfo.get(Tags.NumberOfCopies));
                }
                String priority = actionInfo != null ? actionInfo.getString(Tags.RequestPriority, defaultRequestPriority) : defaultRequestPriority;
                if (!Priority.isValid(priority)) {
                    throw new DcmServiceException(Status.InvalidArgumentValue, "" + actionInfo.get(Tags.RequestPriority));
                }
                if (spoolDir.isFilesetHighWater()) {
                    throw new DcmServiceException(Status.ResourceLimitation);
                }
                try {
                    checkRequest(attrs);
                    mcrq.setMediaWriterName(lookupMediaWriterName(a.getCalledAET()));
                    mcrq.setPriority(priority);
                    mcrq.setRemainingCopies(numberOfCopies);
                    mcrq.setFilesetID(ensureFileSetID(attrs.getString(Tags.StorageMediaFileSetID)));
                    mcrq.setVolsetID(attrs.getString(Tags.StorageMediaFileSetID));
                    attrs.putIS(Tags.NumberOfCopies, numberOfCopies);
                    attrs.putCS(Tags.RequestPriority, priority);
                    attrs.putCS(Tags.ExecutionStatus, ExecutionStatus.PENDING);
                    attrs.putCS(Tags.ExecutionStatusInfo, ExecutionStatusInfo.QUEUED_BUILD);
                    try {
                        mcrq.writeAttributes(attrs, log);
                    } catch (IOException e) {
                        throw new DcmServiceException(Status.ProcessingFailure, e);
                    }
                    try {
                        JMSDelegate.queue(mediaComposerQueueName, "Schedule Composing media for " + rq, log, mcrq, 0L);
                    } catch (JMSException e) {
                        throw new MediaCreationException(ExecutionStatusInfo.PROC_FAILURE, e);
                    }
                } catch (MediaCreationException e) {
                    attrs.putCS(Tags.ExecutionStatus, ExecutionStatus.FAILURE);
                    attrs.putCS(Tags.ExecutionStatusInfo, e.getStatusInfo());
                    List failedSOPs = e.getFailedSOPInstances();
                    if (failedSOPs != null) {
                        DcmElement sq = attrs.putSQ(Tags.FailedSOPSeq);
                        for (int i = 0, n = failedSOPs.size(); i < n; ++i) sq.addItem((Dataset) failedSOPs.get(i));
                    }
                    try {
                        mcrq.writeAttributes(attrs, log);
                    } catch (IOException ioe) {
                    }
                    if (!keepSpoolFiles) deleteRefInstances(attrs);
                    throw new DcmServiceException(Status.ProcessingFailure, e);
                }
                break;
            case CANCEL:
                if (ExecutionStatus.DONE.equals(status) || ExecutionStatus.FAILURE.equals(status)) throw new DcmServiceException(Status.MediaCreationRequestAlreadyCompleted);
                if (!allowCancelAlreadyCreating && ExecutionStatus.CREATING.equals(status)) throw new DcmServiceException(Status.MediaCreationRequestAlreadyInProgress);
                if (!f.delete()) throw new DcmServiceException(Status.CancellationDeniedForUnspecifiedReason);
                deleteRefInstances(attrs);
                break;
            default:
                throw new DcmServiceException(Status.NoSuchActionType, "actionID:" + actionID);
        }
        return null;
    }

    private String ensureFileSetID(String value) {
        if (value != null) {
            return value;
        }
        if (defaultFilesetIDFormat == null) {
            return defaultFilesetID;
        }
        synchronized (defaultFilesetIDFormat) {
            try {
                server.setAttribute(serviceName, new Attribute("NextDefaultFilesetIDSeqno", new Integer(nextDefaultFilesetIDSeqno + 1)));
            } catch (Exception e) {
                log.warn("Failed to store incremented NextFilesetSeqno - " + "will be reset by next reboot! ", e);
                ++nextDefaultFilesetIDSeqno;
            }
            return defaultFilesetIDFormat.format(nextDefaultFilesetIDSeqno - 1);
        }
    }

    private void deleteRefInstances(final Dataset attrs) {
        new Thread(new Runnable() {

            public void run() {
                spoolDir.deleteRefInstances(attrs);
            }
        });
    }

    private String lookupMediaWriterName(String aet) throws DcmServiceException {
        try {
            return (String) server.invoke(dcmServerName, LOOKUP_MEDIA_WRITER_NAME, new Object[] { aet }, new String[] { String.class.getName() });
        } catch (Exception e) {
            throw new DcmServiceException(Status.ProcessingFailure, e);
        }
    }

    private Dataset doNGet(ActiveAssociation assoc, Dimse rq, Command rspCmd) throws IOException, DcmServiceException {
        Command rqCmd = rq.getCommand();
        Dataset ds = rq.getDataset();
        String iuid = rqCmd.getAffectedSOPInstanceUID();
        File f = spoolDir.getMediaCreationRequestFile(iuid);
        if (!f.exists()) throw new DcmServiceException(Status.NoSuchObjectInstance);
        DcmObjectFactory dof = DcmObjectFactory.getInstance();
        Dataset mcrq = dof.newDataset();
        log.info("M-READ " + f);
        IOException error = null;
        for (int retry = 0; retry <= maxReadRequestRetries; ++retry) {
            try {
                mcrq.readFile(f, FileFormat.DICOM_FILE, -1);
                final int[] filter = rqCmd.getTags(Tags.AttributeIdentifierList);
                return filter != null ? mcrq.subSet(filter) : mcrq;
            } catch (IOException e) {
                error = e;
                log.warn("M-READ " + f + " failed");
                try {
                    Thread.sleep(readRequestRetryInterval);
                } catch (InterruptedException e1) {
                    log.warn("Wait Request Retry Interval was interrupted:", e);
                }
            }
        }
        log.error("Give up attemps to read " + f, error);
        throw new DcmServiceException(Status.ProcessingFailure, error);
    }
}
