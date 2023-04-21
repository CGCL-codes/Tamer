package org.dcm4chex.archive.dcm.movescu;

import java.io.IOException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.management.ObjectName;
import org.dcm4che.data.Command;
import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmObjectFactory;
import org.dcm4che.dict.Status;
import org.dcm4che.dict.Tags;
import org.dcm4che.dict.UIDs;
import org.dcm4che.net.ActiveAssociation;
import org.dcm4che.net.AssociationFactory;
import org.dcm4che.net.DcmServiceException;
import org.dcm4che.net.Dimse;
import org.dcm4chex.archive.config.RetryIntervalls;
import org.dcm4chex.archive.dcm.AbstractScuService;
import org.dcm4chex.archive.mbean.JMSDelegate;

/**
 * @author gunter.zeilinger@tiani.com
 * @version $Revision: 13708 $ $Date: 2010-07-07 04:16:48 -0400 (Wed, 07 Jul 2010) $
 * @since 17.12.2003
 */
public class MoveScuService extends AbstractScuService implements MessageListener {

    private static final int PCID_MOVE = 1;

    private static final String DEF_CALLED_AET = "QR_SCP";

    private String calledAET = DEF_CALLED_AET;

    private RetryIntervalls.Map retryIntervalls;

    private int concurrency = 1;

    private String queueName;

    private boolean forceCalledAET;

    private JMSDelegate jmsDelegate = new JMSDelegate(this);

    public final ObjectName getJmsServiceName() {
        return jmsDelegate.getJmsServiceName();
    }

    public final void setJmsServiceName(ObjectName jmsServiceName) {
        jmsDelegate.setJmsServiceName(jmsServiceName);
    }

    public final String getQueueName() {
        return queueName;
    }

    public final void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public final String getCalledAET() {
        return calledAET;
    }

    public final void setCalledAET(String retrieveAET) {
        this.calledAET = retrieveAET;
    }

    public final int getConcurrency() {
        return concurrency;
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

    public String getRetryIntervalls() {
        return retryIntervalls != null ? retryIntervalls.toString() : "NEVER\n";
    }

    public void setRetryIntervalls(String text) {
        retryIntervalls = new RetryIntervalls.Map(text);
    }

    public boolean isForceCalledAET() {
        return forceCalledAET;
    }

    public void setForceCalledAET(boolean forceCalledAET) {
        this.forceCalledAET = forceCalledAET;
    }

    public void scheduleMove(String retrieveAET, String destAET, int priority, String pid, String studyIUID, String seriesIUID, String[] sopIUIDs, long scheduledTime) {
        MoveOrder moveOrder = new MoveOrder(retrieveAET, destAET, priority, pid, studyIUID, seriesIUID, sopIUIDs);
        moveOrder.processOrderProperties();
        scheduleMoveOrder(moveOrder, scheduledTime);
    }

    public void scheduleMove(String retrieveAET, String destAET, int priority, String pid, String[] studyIUIDs, String[] seriesIUIDs, String[] sopIUIDs, long scheduledTime) {
        MoveOrder moveOrder = new MoveOrder(retrieveAET, destAET, priority, pid, studyIUIDs, seriesIUIDs, sopIUIDs);
        moveOrder.processOrderProperties();
        scheduleMoveOrder(moveOrder, scheduledTime);
    }

    public void scheduleMoveOrder(MoveOrder order, long scheduledTime) {
        try {
            log.info("Schedule order: " + order);
            jmsDelegate.queue(queueName, order, JMSDelegate.toJMSPriority(order.getPriority()), scheduledTime);
        } catch (Exception e) {
            log.error("Failed to schedule order: " + order);
        }
    }

    protected void startService() throws Exception {
        jmsDelegate.startListening(queueName, this, concurrency);
    }

    protected void stopService() throws Exception {
        jmsDelegate.stopListening(queueName);
    }

    public void onMessage(Message message) {
        ObjectMessage om = (ObjectMessage) message;
        try {
            MoveOrder order = (MoveOrder) om.getObject();
            log.info("Start processing " + order);
            try {
                process(order);
                log.info("Finished processing " + order);
            } catch (Exception e) {
                order.setThrowable(e);
                final int failureCount = order.getFailureCount() + 1;
                order.setFailureCount(failureCount);
                final long delay = retryIntervalls.getIntervall(order.getMoveDestination(), failureCount);
                if (delay == -1L) {
                    log.error("Give up to process " + order, e);
                    jmsDelegate.fail(queueName, order);
                } else {
                    log.warn("Failed to process " + order + ". Scheduling retry.", e);
                    scheduleMoveOrder(order, System.currentTimeMillis() + delay);
                }
            }
        } catch (Throwable e) {
            log.error("unexpected error during processing message: " + message, e);
        }
    }

    protected void process(MoveOrder order) throws Exception {
        String aet = order.getRetrieveAET();
        if (forceCalledAET || aet == null) {
            aet = calledAET;
        }
        ActiveAssociation aa = openAssociation(aet, UIDs.PatientRootQueryRetrieveInformationModelMOVE);
        try {
            invokeDimse(aa, order);
        } finally {
            try {
                aa.release(true);
                Thread.sleep(10);
            } catch (Exception e) {
                log.warn("Failed to release association " + aa.getAssociation(), e);
            }
        }
    }

    private void invokeDimse(ActiveAssociation aa, MoveOrder order) throws InterruptedException, IOException, DcmServiceException {
        AssociationFactory af = AssociationFactory.getInstance();
        DcmObjectFactory dof = DcmObjectFactory.getInstance();
        Command cmd = dof.newCommand();
        cmd.initCMoveRQ(aa.getAssociation().nextMsgID(), UIDs.PatientRootQueryRetrieveInformationModelMOVE, order.getPriority(), order.getMoveDestination());
        Dataset ds = dof.newDataset();
        ds.putCS(Tags.QueryRetrieveLevel, order.getQueryRetrieveLevel());
        putLO(ds, Tags.PatientID, order.getPatientId());
        putUI(ds, Tags.StudyInstanceUID, order.getStudyIuids());
        putUI(ds, Tags.SeriesInstanceUID, order.getSeriesIuids());
        putUI(ds, Tags.SOPInstanceUID, order.getSopIuids());
        log.debug("Move Identifier:\n");
        log.debug(ds);
        Dimse dimseRsp = aa.invoke(af.newDimse(PCID_MOVE, cmd, ds)).get();
        Command cmdRsp = dimseRsp.getCommand();
        int status = cmdRsp.getStatus();
        if (status != 0) {
            if (status == Status.SubOpsOneOrMoreFailures && order.getSopIuids() != null) {
                Dataset moveRspData = dimseRsp.getDataset();
                if (moveRspData != null) {
                    String[] failedUIDs = ds.getStrings(Tags.FailedSOPInstanceUIDList);
                    if (failedUIDs != null && failedUIDs.length != 0) {
                        order.setSopIuids(failedUIDs);
                    }
                }
            }
            throw new DcmServiceException(status, cmdRsp.getString(Tags.ErrorComment));
        }
    }

    private static void putLO(Dataset ds, int tag, String s) {
        if (s != null) ds.putLO(tag, s);
    }

    private static void putUI(Dataset ds, int tag, String[] uids) {
        if (uids != null) ds.putUI(tag, uids);
    }
}
