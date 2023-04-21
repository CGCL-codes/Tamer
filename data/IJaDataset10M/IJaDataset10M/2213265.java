package com.vangent.hieos.services.xca.gateway.transactions;

import com.vangent.hieos.xutil.atna.ATNAAuditEvent;
import com.vangent.hieos.xutil.atna.ATNAAuditEventHelper;
import com.vangent.hieos.xutil.atna.ATNAAuditEventRetrieveDocumentSet;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.xconfig.XConfigActor;
import com.vangent.hieos.xutil.atna.XATNALogger;
import org.apache.axis2.context.MessageContext;
import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;

/**
 *
 * @author Bernie Thuman
 */
public class XCARGRetrieveDocumentSet extends XCARetrieveDocumentSet {

    private static final Logger logger = Logger.getLogger(XCARGRetrieveDocumentSet.class);

    /**
     * 
     * @param log_message
     * @param messageContext
     */
    public XCARGRetrieveDocumentSet(XLogMessage log_message, MessageContext messageContext) {
        super(log_message, messageContext);
    }

    /**
     * Make sure that the xdsb namespace is in order.
     *
     * @param request  The root of the XML request.
     */
    @Override
    protected void validateRequest(OMElement request) {
        super.validateRequest(request);
        try {
            XATNALogger xATNALogger = new XATNALogger();
            if (xATNALogger.isPerformAudit()) {
                ATNAAuditEventRetrieveDocumentSet auditEvent = ATNAAuditEventHelper.getATNAAuditEventRetrieveDocumentSet(request);
                auditEvent.setActorType(ATNAAuditEvent.ActorType.RESPONDING_GATEWAY);
                auditEvent.setTransaction(ATNAAuditEvent.IHETransaction.ITI39);
                auditEvent.setAuditEventType(ATNAAuditEvent.AuditEventType.EXPORT);
                xATNALogger.audit(auditEvent);
            }
        } catch (Exception ex) {
        }
    }

    /**
     *
     * @param docRequest
     * @param homeCommunityId
     * @param gatewayConfig
     * @throws XdsInternalException
     */
    protected void processRemoteCommunityRequest(OMElement docRequest, String homeCommunityId, XConfigActor gatewayConfig) throws XdsInternalException {
    }
}
