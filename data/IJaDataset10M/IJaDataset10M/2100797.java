package org.dcm4chex.archive.dcm.modify;

import java.util.HashMap;
import java.util.Map;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import org.dcm4che.data.Command;
import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmObjectFactory;
import org.dcm4che.dict.Tags;
import org.dcm4che.dict.UIDs;
import org.dcm4che.net.ActiveAssociation;
import org.dcm4che.net.Association;
import org.dcm4che.net.AssociationFactory;
import org.dcm4che.net.DcmServiceException;
import org.dcm4che.net.Dimse;
import org.dcm4che.util.UIDGenerator;
import org.dcm4chex.archive.common.PrivateTags;
import org.dcm4chex.archive.config.DicomPriority;
import org.dcm4chex.archive.config.ForwardingRules;
import org.dcm4chex.archive.config.RetryIntervalls;
import org.dcm4chex.archive.dcm.AbstractScuService;
import org.dcm4chex.archive.mbean.JMSDelegate;

/**
 * @author Gunter Zeilinger <gunterze@gmail.com>
 * @version $Revision$ $Date$
 * @since Aug 11, 2009
 */
public class AttributesModificationScuService extends AbstractScuService implements MessageListener, NotificationListener {

    private static final int PCID = 1;

    private JMSDelegate jmsDelegate = new JMSDelegate(this);

    private String queueName;

    private ObjectName attributesModificationScpServiceName;

    private int concurrency = 1;

    private RetryIntervalls retryIntervalls = new RetryIntervalls();

    private ForwardingRules forwardingRules = new ForwardingRules("");

    private int priority;

    private int noRetryErrorCode;

    public final String getPriority() {
        return DicomPriority.toString(priority);
    }

    public final void setPriority(String priority) {
        this.priority = DicomPriority.toCode(priority);
    }

    public final String getForwardingRules() {
        return forwardingRules.toString();
    }

    public final void setForwardingRules(String forwardingRules) {
        this.forwardingRules = new ForwardingRules(forwardingRules);
    }

    public final String getRetryIntervalls() {
        return retryIntervalls.toString();
    }

    public final void setRetryIntervalls(String text) {
        retryIntervalls = new RetryIntervalls(text);
    }

    public final int getNoRetryErrorCode() {
        return noRetryErrorCode;
    }

    public final void setNoRetryErrorCodeAsString(String noRetryErrorCode) {
        this.noRetryErrorCode = noRetryErrorCode.endsWith("H") ? Integer.parseInt(noRetryErrorCode.substring(0, noRetryErrorCode.length() - 1), 16) : Integer.parseInt(noRetryErrorCode);
    }

    public final String getNoRetryErrorCodeAsString() {
        return String.format("%04XH", noRetryErrorCode);
    }

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

    public final ObjectName getAttributesModificationScpServiceName() {
        return attributesModificationScpServiceName;
    }

    public final void setAttributesModificationScpServiceName(ObjectName serviceName) {
        this.attributesModificationScpServiceName = serviceName;
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

    protected void startService() throws Exception {
        jmsDelegate.startListening(queueName, this, concurrency);
        server.addNotificationListener(attributesModificationScpServiceName, this, AttributesModificationScpService.NOTIF_FILTER, null);
    }

    protected void stopService() throws Exception {
        server.removeNotificationListener(attributesModificationScpServiceName, this, AttributesModificationScpService.NOTIF_FILTER, null);
        jmsDelegate.stopListening(queueName);
    }

    public void handleNotification(Notification notif, Object handback) {
        Dataset modifiedAttrs = (Dataset) notif.getUserData();
        modifiedAttrs.setPrivateCreatorID(PrivateTags.CreatorID);
        Map<String, String[]> param = new HashMap<String, String[]>();
        param.put("calling", new String[] { modifiedAttrs.getString(PrivateTags.CallingAET) });
        modifiedAttrs.setPrivateCreatorID(null);
        String[] destAETs = forwardingRules.getForwardDestinationsFor(param);
        for (int i = 0; i < destAETs.length; i++) {
            AttributesModificationOrder order = new AttributesModificationOrder(modifiedAttrs, ForwardingRules.toAET(destAETs[i]));
            try {
                log.info("Scheduling " + order);
                jmsDelegate.queue(queueName, order, Message.DEFAULT_PRIORITY, 0L);
            } catch (Exception e) {
                log.error("Failed to schedule " + order, e);
            }
        }
    }

    public void onMessage(Message message) {
        ObjectMessage om = (ObjectMessage) message;
        try {
            AttributesModificationOrder order = (AttributesModificationOrder) om.getObject();
            log.info("Start processing " + order);
            try {
                sendAttributesModification(order.getDataset(), order.getDestination());
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

    private void sendAttributesModification(Dataset dataset, String aet) throws Exception {
        ActiveAssociation aa = openAssociation(aet, UIDs.Dcm4cheAttributesModificationNotificationSOPClass);
        Association a = aa.getAssociation();
        try {
            DcmObjectFactory dof = DcmObjectFactory.getInstance();
            Command cmdRq = dof.newCommand();
            cmdRq.initCStoreRQ(a.nextMsgID(), UIDs.Dcm4cheAttributesModificationNotificationSOPClass, UIDGenerator.getInstance().createUID(), priority);
            Dimse dimseRq = AssociationFactory.getInstance().newDimse(PCID, cmdRq, dataset);
            final Dimse dimseRsp = aa.invoke(dimseRq).get();
            final Command cmdRsp = dimseRsp.getCommand();
            final int status = cmdRsp.getStatus();
            if (status != 0) {
                log.warn("Received Error Status " + Integer.toHexString(status) + "H, Error Comment: " + cmdRsp.getString(Tags.ErrorComment));
                if (status != noRetryErrorCode) throw new DcmServiceException(status, cmdRsp.getString(Tags.ErrorComment));
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
