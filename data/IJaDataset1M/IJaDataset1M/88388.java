package org.dicom4j.toolkit.dimse.service.basicworklist.scu;

import java.util.HashMap;
import java.util.Map;
import org.dicom4j.network.dimse.messages.CFindResponseMessage;
import org.dicom4j.network.dimse.messages.support.AbstractDimseMessage;
import org.dicom4j.toolkit.dimse.service.AbstractServiceSCU;
import org.dicom4j.toolkit.network.devices.LightRemoteDevice;
import org.dicom4j.toolkit.query.DefaultQueryRunnerListener;
import org.dicom4j.toolkit.query.QueryRunner;
import org.dolmen.core.lang.thread.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Send Dicom Worklist queries.
 * <p>usage: 
 * <ul>
 * <li>use "setOrderFiller" to indicate how to connect to the WorklistSCP </li>
 * <li>creates a {@link ModalityWorklistQuery} and set the Query Keys Matching and Query Keys Return </li>
 * <li>call "sendQuery"</li>
 * </ul>   
 * 
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 */
public class ModalityWorklistSCU extends AbstractServiceSCU {

    /**
	 * the logger
	 */
    private static final Logger logger = LoggerFactory.getLogger(ModalityWorklistSCU.class);

    /**
	 * used to allow this class to send many queries at the same time
	 */
    private Map<Integer, QuerySession> fQueriesSession = new HashMap<Integer, QuerySession>();

    /**
	 * the Order Filler (ie: Worklist SCP)
	 */
    private LightRemoteDevice fOrderFiller;

    public ModalityWorklistSCU() {
    }

    /**
	 * send the supplied queries to SCP
	 * @param aQuery the Query
	 * @param aHandler the Handler which is called each time we receive a response from the SCP 
	 * @throws Exception if the supplied query is already in progress (you can send the same query is the previous was not success or canceled)
	 */
    public synchronized void sendQuery(ModalityWorklistAssociateQuery aAssociateQuery, ModalityWorklistQuery aQuery, QueryModalityWorklistHandler aHandler) throws Exception {
        logger.info("sendQuery");
        Integer lQueryID = new Integer(aQuery.getQueryID());
        logger.debug("sendQuery, QueryID: " + aQuery.getQueryID());
        if (fQueriesSession.containsKey(lQueryID)) {
            throw new Exception("The supplied Query is already in progress");
        } else {
            QuerySession lSess = new QuerySession(aQuery, aHandler);
            fQueriesSession.put(lQueryID, lSess);
            QuerySender lSender = new QuerySender(lSess);
            ThreadUtils.startRunnable(lSender);
        }
    }

    /**
	 * the thread used to send a query
	 *
	 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte 
	 *
	 */
    private class QuerySender extends DefaultQueryRunnerListener implements Runnable {

        private QuerySession fQuerySession;

        public QuerySender(QuerySession aSession) {
            fQuerySession = aSession;
        }

        public void run() {
        }

        public void handleMessageReceived(QueryRunner aRunner, byte aPresentationContextID, AbstractDimseMessage aMessage) throws Exception {
            if (aMessage instanceof CFindResponseMessage) {
                CFindResponseMessage lresp = (CFindResponseMessage) aMessage;
                fQuerySession.getHandler().handleQueryResult(lresp.getStatus(), lresp.getDataSet());
                if (lresp.isSuccess() || lresp.isCanceled()) {
                    logger.debug("messageReceived, message ID: " + lresp.getMessageID());
                    fQueriesSession.remove(lresp.getMessageID());
                }
            } else {
                String lErr = "unexpected Response, A-Abort sent";
                logger.error(lErr);
                aRunner.sendAbort();
                fireException(new Exception(lErr));
            }
        }

        @Override
        public void handleException(QueryRunner aRunner, Throwable aCause) {
            fireException(aCause);
        }

        private void fireException(Throwable aCause) {
            if (fQuerySession.getHandler() != null) {
                fQuerySession.getHandler().handleException(aCause);
            } else {
                logger.warn("No Handler was set");
                logger.error(aCause.getMessage());
            }
        }
    }
}
