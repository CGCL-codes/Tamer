package org.dcm4chex.cdw.mbean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.StringTokenizer;
import javax.jms.JMSException;
import javax.management.Attribute;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmElement;
import org.dcm4che.data.DcmObjectFactory;
import org.dcm4che.dict.Tags;
import org.dcm4che.dict.UIDs;
import org.dcm4che.util.UIDGenerator;
import org.dcm4chex.cdw.common.ExecutionStatus;
import org.dcm4chex.cdw.common.ExecutionStatusInfo;
import org.dcm4chex.cdw.common.Flag;
import org.dcm4chex.cdw.common.JMSDelegate;
import org.dcm4chex.cdw.common.MediaCreationException;
import org.dcm4chex.cdw.common.MediaCreationRequest;
import org.dcm4chex.cdw.common.Priority;
import org.dcm4chex.cdw.common.SpoolDirDelegate;
import org.jboss.system.ServiceMBeanSupport;

/**
 * @author Gunter Zeilinger <gunterze@gmail.com>
 * @version $Revision$ $Date$
 * @since Feb 13, 2009
 */
public class MediaCreationRequestEmulatorService extends ServiceMBeanSupport implements NotificationListener {

    private String mediaComposerQueueName;

    private String mediaApplicationProfile = "STD-GEN-CD";

    private String requestPriority = Priority.LOW;

    private String filesetID;

    private DecimalFormat filesetIDFormat;

    private int nextFilesetIDSeqno = 1;

    private int numberOfCopies = 1;

    private boolean labelUsingInformationExtractedFromInstances = true;

    private String labelText = "";

    private String labelStyleSelection = "";

    private boolean allowMediaSplitting = false;

    private boolean allowLossyCompression = false;

    private String includeNonDICOMObjects = "NO";

    private boolean includeDisplayApplication = false;

    private boolean preserveInstances = false;

    private long pollInterval = SpoolDirService.MS_PER_MINUTE;

    private Integer schedulerID;

    private String timerID;

    private final SpoolDirDelegate spoolDir = new SpoolDirDelegate(this);

    private final SchedulerDelegate scheduler = new SchedulerDelegate(this);

    private String[] sourceAETs;

    private long[] delays;

    private UIDGenerator uidGenerator = UIDGenerator.getInstance();

    public String getSourceAETitles() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < sourceAETs.length; i++) {
            sb.append(sourceAETs[i]);
            if (delays[i] != SpoolDirService.MS_PER_MINUTE) {
                sb.append(':').append(SpoolDirService.timeAsString(delays[i]));
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    public void setSourceAETitles(String s) {
        StringTokenizer stk = new StringTokenizer(s, "\r\n");
        sourceAETs = new String[stk.countTokens()];
        delays = new long[sourceAETs.length];
        int endAET;
        for (int i = 0; i < sourceAETs.length; i++) {
            sourceAETs[i] = stk.nextToken().trim();
            delays[i] = SpoolDirService.MS_PER_MINUTE;
            endAET = sourceAETs[i].indexOf(':');
            if (endAET >= 0) {
                delays[i] = SpoolDirService.timeFromString(sourceAETs[i].substring(endAET + 1));
                sourceAETs[i] = sourceAETs[i].substring(0, endAET);
            }
        }
    }

    public boolean containsSourceAETitle(String aet) {
        if (sourceAETs == null) {
            return false;
        }
        for (String sourceAET : sourceAETs) {
            if (sourceAET.equals(aet)) {
                return true;
            }
        }
        return false;
    }

    public ObjectName getSpoolDirName() {
        return spoolDir.getSpoolDirName();
    }

    public void setSpoolDirName(ObjectName spoolDirName) {
        spoolDir.setSpoolDirName(spoolDirName);
    }

    public ObjectName getSchedulerServiceName() {
        return scheduler.getSchedulerServiceName();
    }

    public void setSchedulerServiceName(ObjectName schedulerServiceName) {
        scheduler.setSchedulerServiceName(schedulerServiceName);
    }

    public final String getMediaComposerQueueName() {
        return mediaComposerQueueName;
    }

    public final void setMediaComposerQueueName(String mediaComposerQueueName) {
        this.mediaComposerQueueName = mediaComposerQueueName;
    }

    public final String getMediaApplicationProfile() {
        return mediaApplicationProfile;
    }

    public final void setMediaApplicationProfile(String profile) {
        this.mediaApplicationProfile = profile;
    }

    public final String getFilesetID() {
        return filesetID;
    }

    public final void setFilesetID(String filesetID) {
        if (filesetID.indexOf('0') != -1 || filesetID.indexOf('#') != -1) {
            filesetIDFormat = new DecimalFormat(filesetID);
        }
        this.filesetID = filesetID;
    }

    public final int getNextFilesetIDSeqno() {
        return nextFilesetIDSeqno;
    }

    public final void setNextFilesetIDSeqno(int seqno) {
        this.nextFilesetIDSeqno = seqno;
    }

    private String nextFileSetID() {
        if (filesetIDFormat == null) {
            return filesetID;
        }
        synchronized (filesetIDFormat) {
            try {
                server.setAttribute(serviceName, new Attribute("NextFilesetIDSeqno", new Integer(nextFilesetIDSeqno + 1)));
            } catch (Exception e) {
                log.warn("Failed to store incremented NextFilesetSeqno - " + "will be reset by next reboot! ", e);
                ++nextFilesetIDSeqno;
            }
            return filesetIDFormat.format(nextFilesetIDSeqno - 1);
        }
    }

    public final boolean isAllowLossyCompression() {
        return allowLossyCompression;
    }

    public final void setAllowLossyCompression(boolean allowLossyCompression) {
        this.allowLossyCompression = allowLossyCompression;
    }

    public final boolean isAllowMediaSplitting() {
        return allowMediaSplitting;
    }

    public final void setAllowMediaSplitting(boolean allowMediaSplitting) {
        this.allowMediaSplitting = allowMediaSplitting;
    }

    public final boolean isIncludeDisplayApplication() {
        return includeDisplayApplication;
    }

    public final void setIncludeDisplayApplication(boolean includeDisplayApplication) {
        this.includeDisplayApplication = includeDisplayApplication;
    }

    public final String getIncludeNonDICOMObjects() {
        return includeNonDICOMObjects;
    }

    public final void setIncludeNonDICOMObjects(String includeNonDICOMObjects) {
        this.includeNonDICOMObjects = includeNonDICOMObjects;
    }

    public final String getLabelStyleSelection() {
        return labelStyleSelection;
    }

    public final void setLabelStyleSelection(String labelStyleSelection) {
        this.labelStyleSelection = labelStyleSelection;
    }

    public final String getLabelText() {
        return labelText;
    }

    public final void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    public final boolean isLabelUsingInformationExtractedFromInstances() {
        return labelUsingInformationExtractedFromInstances;
    }

    public final void setLabelUsingInformationExtractedFromInstances(boolean labelUsingInformationExtractedFromInstances) {
        this.labelUsingInformationExtractedFromInstances = labelUsingInformationExtractedFromInstances;
    }

    public final boolean isPreserveInstances() {
        return preserveInstances;
    }

    public final void setPreserveInstances(boolean preserveInstances) {
        this.preserveInstances = preserveInstances;
    }

    public final String getRequestPriority() {
        return requestPriority;
    }

    public final void setRequestPriority(String priority) {
        if (!Priority.isValid(priority)) throw new IllegalArgumentException("priority:" + priority);
        this.requestPriority = priority;
    }

    public final int getNumberOfCopies() {
        return numberOfCopies;
    }

    public final void setNumberOfCopies(int numberOfCopies) {
        if (numberOfCopies < 1) throw new IllegalArgumentException("numberOfCopies:" + numberOfCopies);
        this.numberOfCopies = numberOfCopies;
    }

    public final String getPollInterval() {
        return SpoolDirService.timeAsString(pollInterval);
    }

    public void setPollInterval(String interval) throws Exception {
        this.pollInterval = SpoolDirService.timeFromString(interval);
        if (getState() == STARTED) {
            scheduler.stopScheduler(timerID, schedulerID, this);
            schedulerID = scheduler.startScheduler("PurgeSpoolDir", pollInterval, this);
        }
    }

    public void setTimerID(String timerID) {
        this.timerID = timerID;
    }

    public String getTimerID() {
        return timerID;
    }

    protected void startService() throws Exception {
        schedulerID = scheduler.startScheduler(timerID, pollInterval, this);
    }

    protected void stopService() throws Exception {
        scheduler.stopScheduler(timerID, schedulerID, this);
    }

    public void handleNotification(Notification notif, Object handback) {
        poll();
    }

    public int poll() {
        if (sourceAETs == null) {
            return 0;
        }
        log.info("Check for received objects from AEs for which Media " + "Creation requests are emulated.");
        int count = 0;
        long now = System.currentTimeMillis();
        for (int i = 0; i < sourceAETs.length; i++) {
            File[] files = spoolDir.getEmulateRequestFiles(sourceAETs[i], now - delays[i]);
            for (File file : files) {
                try {
                    createRequest(file, sourceAETs[i]);
                    count++;
                } catch (Exception e) {
                    log.error("Failed to emulate Media Creation Request for " + file, e);
                }
            }
        }
        return count;
    }

    private void createRequest(File sopListFile, String aet) throws Exception {
        String iuid = uidGenerator.createUID();
        File f = spoolDir.getMediaCreationRequestFile(iuid);
        MediaCreationRequest mcrq = new MediaCreationRequest(f, aet);
        mcrq.setPriority(requestPriority);
        mcrq.setRemainingCopies(numberOfCopies);
        String fileID = nextFileSetID();
        mcrq.setFilesetID(fileID);
        mcrq.setVolsetID(fileID);
        DcmObjectFactory dof = DcmObjectFactory.getInstance();
        Dataset attrs = dof.newDataset();
        attrs.setFileMetaInfo(dof.newFileMetaInfo(iuid, UIDs.MediaCreationManagementSOPClass, UIDs.ExplicitVRLittleEndian));
        attrs.putUI(Tags.SOPClassUID, UIDs.MediaCreationManagementSOPClass);
        attrs.putUI(Tags.SOPInstanceUID, iuid);
        attrs.putIS(Tags.NumberOfCopies, numberOfCopies);
        attrs.putCS(Tags.RequestPriority, requestPriority);
        attrs.putCS(Tags.ExecutionStatus, ExecutionStatus.PENDING);
        attrs.putCS(Tags.ExecutionStatusInfo, ExecutionStatusInfo.QUEUED_BUILD);
        attrs.putCS(Tags.LabelUsingInformationExtractedFromInstances, Flag.valueOf(labelUsingInformationExtractedFromInstances));
        attrs.putCS(Tags.AllowMediaSplitting, Flag.valueOf(allowMediaSplitting));
        attrs.putCS(Tags.AllowLossyCompression, Flag.valueOf(allowLossyCompression));
        attrs.putCS(Tags.IncludeDisplayApplication, Flag.valueOf(includeDisplayApplication));
        attrs.putCS(Tags.PreserveCompositeInstancesAfterMediaCreation, Flag.valueOf(preserveInstances));
        attrs.putUT(Tags.LabelText, labelText);
        attrs.putCS(Tags.LabelStyleSelection, labelStyleSelection);
        attrs.putCS(Tags.IncludeNonDICOMObjects, includeNonDICOMObjects);
        log.info("M-READ " + sopListFile);
        BufferedReader reader = new BufferedReader(new FileReader(sopListFile));
        try {
            HashSet<String> iuids = new HashSet<String>();
            mcrq.setMediaWriterName(reader.readLine());
            DcmElement refSOPs = attrs.putSQ(Tags.RefSOPSeq);
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() == 0) continue;
                int cuidEnd = line.indexOf('\t');
                iuid = line.substring(cuidEnd + 1);
                if (!iuids.add(iuid)) continue;
                Dataset item = refSOPs.addNewItem();
                item.putUI(Tags.RefSOPClassUID, line.substring(0, cuidEnd));
                item.putUI(Tags.RefSOPInstanceUID, iuid);
                item.putLO(Tags.RequestedMediaApplicationProfile, mediaApplicationProfile);
            }
        } finally {
            reader.close();
        }
        attrs.writeFile(f, null);
        JMSDelegate.queue(mediaComposerQueueName, "Schedule Composing media for " + sopListFile, log, mcrq, 0L);
        if (sopListFile.delete()) {
            log.info("M-DELETE " + sopListFile);
        } else {
            log.warn("Failed: M-DELETE " + sopListFile);
        }
    }
}
