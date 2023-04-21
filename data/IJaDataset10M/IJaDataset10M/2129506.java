package org.dcm4chex.archive.dcm.gpwlscp;

import java.rmi.RemoteException;
import java.util.Date;
import javax.ejb.CreateException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import org.dcm4che.data.Command;
import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmElement;
import org.dcm4che.data.DcmObjectFactory;
import org.dcm4che.dict.Tags;
import org.dcm4che.dict.UIDs;
import org.dcm4che.net.ActiveAssociation;
import org.dcm4che.net.Association;
import org.dcm4che.net.AssociationFactory;
import org.dcm4che.net.DcmServiceException;
import org.dcm4che.net.Dimse;
import org.dcm4cheri.util.StringUtils;
import org.dcm4chex.archive.config.RetryIntervalls;
import org.dcm4chex.archive.dcm.AbstractScuService;
import org.dcm4chex.archive.ejb.interfaces.GPPPSManager;
import org.dcm4chex.archive.ejb.interfaces.GPPPSManagerHome;
import org.dcm4chex.archive.ejb.interfaces.GPWLManager;
import org.dcm4chex.archive.ejb.interfaces.GPWLManagerHome;
import org.dcm4chex.archive.mbean.JMSDelegate;
import org.dcm4chex.archive.util.EJBHomeFactory;
import org.dcm4chex.archive.util.HomeFactoryException;

/**
 * @author gunter.zeilinger@tiani.com
 * @version $Revision: 9943 $ $Date: 2009-02-23 10:59:02 -0500 (Mon, 23 Feb 2009) $
 * @since 12.04.2005
 * 
 */
public class PPSScuService extends AbstractScuService implements MessageListener, NotificationListener {

    private static final int PCID_GPPPS = 1;

    private static final String NONE = "NONE";

    private static final String[] EMPTY = {};

    private static final int[] SOP_IUID = { Tags.SOPInstanceUID };

    private static final int[] N_CREATE_SPS_ATTRS = { Tags.SpecificCharacterSet, Tags.PatientName, Tags.PatientID, Tags.PatientBirthDate, Tags.PatientSex, Tags.ActualHumanPerformersSeq, Tags.RefRequestSeq };

    private static final int[] N_CREATE_TYPE2_ATTRS = { Tags.RefRequestSeq, Tags.ActualHumanPerformersSeq, Tags.ActualHumanPerformersSeq, Tags.RequestedSubsequentWorkitemCodeSeq, Tags.NonDICOMOutputCodeSeq, Tags.OutputInformationSeq };

    private RetryIntervalls retryIntervalls = new RetryIntervalls();

    private String[] destAETs = EMPTY;

    private ObjectName gpwlScpServiceName;

    private String queueName;

    private int concurrency = 1;

    private String ppsuidSuffix = ".1";

    private boolean copyWorkitemCode = true;

    private boolean copyProcessingApplicationsCode = true;

    private boolean copyStationGeographicLocationCode = true;

    private boolean copyStationClassCode = true;

    private boolean copyStationNameCode = true;

    private JMSDelegate jmsDelegate = new JMSDelegate(this);

    public final ObjectName getJmsServiceName() {
        return jmsDelegate.getJmsServiceName();
    }

    public final void setJmsServiceName(ObjectName jmsServiceName) {
        jmsDelegate.setJmsServiceName(jmsServiceName);
    }

    public final boolean isCopyProcessingApplicationsCode() {
        return copyProcessingApplicationsCode;
    }

    public final void setCopyProcessingApplicationsCode(boolean copyProcessingApplicationsCode) {
        this.copyProcessingApplicationsCode = copyProcessingApplicationsCode;
    }

    public final boolean isCopyStationClassCode() {
        return copyStationClassCode;
    }

    public final void setCopyStationClassCode(boolean copyStationClassCode) {
        this.copyStationClassCode = copyStationClassCode;
    }

    public final boolean isCopyStationGeographicLocationCode() {
        return copyStationGeographicLocationCode;
    }

    public final void setCopyStationGeographicLocationCode(boolean copyStationGeographicLocationCode) {
        this.copyStationGeographicLocationCode = copyStationGeographicLocationCode;
    }

    public final boolean isCopyStationNameCode() {
        return copyStationNameCode;
    }

    public final void setCopyStationNameCode(boolean copyStationNameCode) {
        this.copyStationNameCode = copyStationNameCode;
    }

    public final boolean isCopyWorkitemCode() {
        return copyWorkitemCode;
    }

    public final void setCopyWorkitemCode(boolean copyWorkitemCode) {
        this.copyWorkitemCode = copyWorkitemCode;
    }

    public final String getPpsUidSuffix() {
        return ppsuidSuffix;
    }

    public final void setPpsUidSuffix(String ppsuidSuffix) {
        this.ppsuidSuffix = ppsuidSuffix;
    }

    public final int getConcurrency() {
        return concurrency;
    }

    private GPWLManager getGPWLManager() throws HomeFactoryException, RemoteException, CreateException {
        return ((GPWLManagerHome) EJBHomeFactory.getFactory().lookup(GPWLManagerHome.class, GPWLManagerHome.JNDI_NAME)).create();
    }

    private GPPPSManager getGPPPSManager() throws HomeFactoryException, RemoteException, CreateException {
        return ((GPPPSManagerHome) EJBHomeFactory.getFactory().lookup(GPPPSManagerHome.class, GPPPSManagerHome.JNDI_NAME)).create();
    }

    public final void setConcurrency(int concurrency) throws Exception {
        if (concurrency <= 0) throw new IllegalArgumentException("Concurrency: " + concurrency);
        if (this.concurrency != concurrency) {
            final boolean restart = getState() == STARTED;
            if (restart) stop();
            this.concurrency = concurrency;
            if (restart) start();
        }
    }

    public final String getDestAETs() {
        return destAETs.length > 0 ? StringUtils.toString(destAETs, '\\') : NONE;
    }

    public final void setDestAETs(String forwardAETs) {
        this.destAETs = NONE.equalsIgnoreCase(forwardAETs) ? EMPTY : StringUtils.split(forwardAETs, '\\');
    }

    public final ObjectName getGpwlScpServiceName() {
        return gpwlScpServiceName;
    }

    public final void setGpwlScpServiceName(ObjectName serviceName) {
        this.gpwlScpServiceName = serviceName;
    }

    public final String getQueueName() {
        return queueName;
    }

    public final void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getRetryIntervalls() {
        return retryIntervalls.toString();
    }

    public void setRetryIntervalls(String text) {
        retryIntervalls = new RetryIntervalls(text);
    }

    protected void startService() throws Exception {
        jmsDelegate.startListening(queueName, this, concurrency);
        server.addNotificationListener(gpwlScpServiceName, this, GPWLScpService.ON_SPS_ACTION_NOTIF_FILTER, null);
    }

    protected void stopService() throws Exception {
        server.removeNotificationListener(gpwlScpServiceName, this, GPWLScpService.ON_SPS_ACTION_NOTIF_FILTER, null);
        jmsDelegate.stopListening(queueName);
    }

    public void handleNotification(Notification notif, Object handback) {
        String spsuid = (String) notif.getUserData();
        Dataset pps = DcmObjectFactory.getInstance().newDataset();
        try {
            Dataset sps;
            GPWLManager gpwlmgr = getGPWLManager();
            sps = gpwlmgr.getWorklistItem(spsuid);
            String ppsiuid = spsuid + ppsuidSuffix;
            String status = sps.getString(Tags.GPSPSStatus);
            pps.putCS(Tags.GPPPSStatus, status);
            pps.putUI(Tags.SOPInstanceUID, ppsiuid);
            Date now = new Date();
            if ("IN PROGRESS".equals(status)) {
                try {
                    getGPPPSManager().getGPPPS(ppsiuid);
                    return;
                } catch (Exception e) {
                }
                pps.putSH(Tags.PPSID, "PPS" + ppsiuid.hashCode());
                pps.putDA(Tags.PPSStartDate, now);
                pps.putTM(Tags.PPSStartTime, now);
                pps.putDA(Tags.PPSEndDate);
                pps.putTM(Tags.PPSEndTime);
                for (int i = 0; i < N_CREATE_TYPE2_ATTRS.length; i++) {
                    pps.putXX(N_CREATE_TYPE2_ATTRS[i]);
                }
                pps.putAll(sps.subSet(N_CREATE_SPS_ATTRS));
                copyCode(copyWorkitemCode, sps.getItem(Tags.ScheduledWorkitemCodeSeq), pps.putSQ(Tags.PerformedWorkitemCodeSeq));
                copyCode(copyStationNameCode, sps.getItem(Tags.ScheduledStationNameCodeSeq), pps.putSQ(Tags.PerformedStationNameCodeSeq));
                copyCode(copyStationClassCode, sps.getItem(Tags.ScheduledStationClassCodeSeq), pps.putSQ(Tags.PerformedStationClassCodeSeq));
                copyCode(copyStationGeographicLocationCode, sps.getItem(Tags.ScheduledStationGeographicLocationCodeSeq), pps.putSQ(Tags.PerformedStationGeographicLocationCodeSeq));
                copyCode(copyProcessingApplicationsCode, sps.getItem(Tags.ScheduledProcessingApplicationsCodeSeq), pps.putSQ(Tags.PerformedProcessingApplicationsCodeSeq));
            } else if ("COMPLETED".equals(status) || "DISCONTINUED".equals(status)) {
                pps.putDA(Tags.PPSEndDate, now);
                pps.putTM(Tags.PPSEndTime, now);
                pps.putAll(gpwlmgr.getOutputInformation(spsuid));
            } else {
                return;
            }
        } catch (Exception e) {
            log.error("Failed to access GP-SPS[" + spsuid + "]", e);
            return;
        }
        for (int i = 0; i < destAETs.length; i++) {
            PPSOrder order = new PPSOrder(pps, destAETs[i]);
            try {
                log.info("Scheduling " + order);
                jmsDelegate.queue(queueName, order, Message.DEFAULT_PRIORITY, 0L);
            } catch (Exception e) {
                log.error("Failed to schedule " + order, e);
            }
        }
    }

    private void copyCode(boolean copy, Dataset code, DcmElement sq) {
        if (copy && code != null) {
            sq.addItem(code);
        }
    }

    public void onMessage(Message message) {
        ObjectMessage om = (ObjectMessage) message;
        try {
            PPSOrder order = (PPSOrder) om.getObject();
            log.info("Start processing " + order);
            try {
                sendPPS(order.isCreate(), order.getDataset(), order.getDestination());
                log.info("Finished processing " + order);
            } catch (Exception e) {
                order.setThrowable(e);
                final int failureCount = order.getFailureCount() + 1;
                order.setFailureCount(failureCount);
                final long delay = retryIntervalls.getIntervall(failureCount);
                if (delay == -1L) {
                    log.error("Give up to process " + order, e);
                    jmsDelegate.fail(queueName, order);
                } else {
                    log.warn("Failed to process " + order + ". Scheduling retry.", e);
                    jmsDelegate.queue(queueName, order, 0, System.currentTimeMillis() + delay);
                }
            }
        } catch (JMSException e) {
            log.error("jms error during processing message: " + message, e);
        } catch (Throwable e) {
            log.error("unexpected error during processing message: " + message, e);
        }
    }

    void sendPPS(boolean create, Dataset pps, String aet) throws Exception {
        ActiveAssociation aa = openAssociation(aet, UIDs.GeneralPurposePerformedProcedureStepSOPClass);
        try {
            Association a = aa.getAssociation();
            DcmObjectFactory dof = DcmObjectFactory.getInstance();
            Command cmdRq = dof.newCommand();
            final String iuid = pps.getString(Tags.SOPInstanceUID);
            if (create) {
                cmdRq.initNCreateRQ(a.nextMsgID(), UIDs.GeneralPurposePerformedProcedureStepSOPClass, iuid);
            } else {
                cmdRq.initNSetRQ(a.nextMsgID(), UIDs.GeneralPurposePerformedProcedureStepSOPClass, iuid);
            }
            Dimse dimseRq = AssociationFactory.getInstance().newDimse(PCID_GPPPS, cmdRq, pps.exclude(SOP_IUID));
            if (log.isDebugEnabled()) {
                log.debug("GP-PPS Attributes:");
                log.debug(pps);
            }
            final Dimse dimseRsp = aa.invoke(dimseRq).get();
            final Command cmdRsp = dimseRsp.getCommand();
            final int status = cmdRsp.getStatus();
            switch(status) {
                case 0x0000:
                    break;
                case 0x0116:
                    log.warn("Received Warning Status 116H (=Attribute Value Out of Range) from remote AE " + aet);
                    break;
                default:
                    throw new DcmServiceException(status, cmdRsp.getString(Tags.ErrorComment));
            }
        } finally {
            try {
                aa.release(true);
            } catch (Exception e) {
                log.warn("Failed to release " + aa.getAssociation());
            }
        }
    }
}
