package org.activebpel.rt.bpel.impl.activity.support;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.activebpel.rt.AeException;
import org.activebpel.rt.IAeConstants;
import org.activebpel.rt.IAePolicyConstants;
import org.activebpel.rt.bpel.AeBusinessProcessException;
import org.activebpel.rt.bpel.AeMessages;
import org.activebpel.rt.bpel.IAeEndpointReference;
import org.activebpel.rt.bpel.IAeFault;
import org.activebpel.rt.bpel.IAeProcessInfoEvent;
import org.activebpel.rt.bpel.def.activity.AeActivityInvokeDef;
import org.activebpel.rt.bpel.impl.AeBpelState;
import org.activebpel.rt.bpel.impl.AePartnerLink;
import org.activebpel.rt.bpel.impl.AeProcessInfoEvent;
import org.activebpel.rt.bpel.impl.AeSuspendReason;
import org.activebpel.rt.bpel.impl.activity.AeActivityInvokeImpl;
import org.activebpel.rt.util.AeUtil;
import org.activebpel.rt.util.AeXPathUtil;
import org.activebpel.rt.util.AeXmlUtil;
import org.activebpel.rt.xml.AeXMLParserBase;
import org.activebpel.wsio.IAeWebServiceResponse;
import org.activebpel.wsio.receive.AeMessageContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Encapsulates all of the retry code for the invoke activity
 */
public class AeInvokeRetryPolicy {

    /** Count of times that alarms have fired to retry this activity. */
    private int mRetries;

    /** Ref to the invoke activity */
    private AeActivityInvokeImpl mInvoke;

    /**
    * Ctor accepts the invoke
    * @param aInvoke
    */
    public AeInvokeRetryPolicy(AeActivityInvokeImpl aInvoke) {
        mInvoke = aInvoke;
    }

    /**
    * Getter for the invoke
    */
    protected AeActivityInvokeImpl getInvoke() {
        return mInvoke;
    }

    /**
    * Getter for the def
    */
    protected AeActivityInvokeDef getDef() {
        return (AeActivityInvokeDef) getInvoke().getDefinition();
    }

    /**
    * Tests the policies associated with this endpoint to see if the invoke
    * should be rescheduled for a later attempt. Ideally this could be handled
    * by an invoke handler itself, but for internal policy handling it is
    * simpler to do here since all the neccesary info is at hand.
    * @param aFault The fault which occured diuring the invoke.
    * @return true if the invoke has been rescheduled and the fault should be ignored.
    */
    public boolean reschedule(IAeFault aFault) throws AeBusinessProcessException {
        AePartnerLink partnerLink = getInvoke().findPartnerLink(getDef().getPartnerLink());
        IAeEndpointReference epr = partnerLink.getPartnerReference();
        if (epr != null) {
            List policyElements = epr.findPolicyElements(partnerLink.getProcess().getProcessPlan(), getDef().getProducerPortType(), getDef().getProducerOperation(), IAePolicyConstants.RETRY_POLICY_TAG);
            for (Iterator iter = policyElements.iterator(); iter.hasNext(); ) {
                if (rescheduleFromRetryAssertion(aFault, (Element) iter.next())) return true;
            }
        }
        return false;
    }

    /**
    * Returns true if based on the passed retry assertion and fault the current invoke
    * has rescheduled itself.
    * @param aFault the fault returned from invoke.
    * @param aRetryAssertionElem The assertion element to test
    * @return true if the invoke has been rescheduled.
    * @throws AeBusinessProcessException
    */
    protected boolean rescheduleFromRetryAssertion(IAeFault aFault, Element aRetryAssertionElem) throws AeBusinessProcessException {
        if (isFaultEligibleForRetry(aFault, aRetryAssertionElem)) {
            boolean retry = false;
            int intervalValue = 0;
            String service = aRetryAssertionElem.getAttribute(IAePolicyConstants.PROCESS_SERVICE_NAME_ATTR);
            if (AeUtil.notNullOrEmpty(service)) {
                intervalValue = callRetryCheckService(service, aFault);
                if (intervalValue < 0) retry = false; else retry = true;
            } else {
                String attempts = aRetryAssertionElem.getAttribute(IAePolicyConstants.RETRY_ATTEMPTS_ATTR);
                if (AeUtil.notNullOrEmpty(attempts) && (getRetries() + 1) < Integer.parseInt(attempts)) {
                    retry = true;
                    String interval = aRetryAssertionElem.getAttribute(IAePolicyConstants.RETRY_INTERVAL_ATTR);
                    if (AeUtil.notNullOrEmpty(interval)) intervalValue = Integer.parseInt(interval);
                }
            }
            if (retry) {
                Date deadline = new Date(System.currentTimeMillis() + (intervalValue * 1000));
                getInvoke().setQueued(true);
                AeProcessInfoEvent infoEvent = new AeProcessInfoEvent(getInvoke().getProcess().getProcessId(), getInvoke().getLocationPath(), IAeProcessInfoEvent.GENERIC_INFO_EVENT, null, AeMessages.format("AeInvokeRetryPolicy.RetryingInvoke", deadline.toString()));
                getInvoke().getProcess().getEngine().fireInfoEvent(infoEvent);
                getInvoke().getProcess().queueAlarm(getInvoke(), deadline);
                return true;
            } else {
                String onFailure = aRetryAssertionElem.getAttribute(IAePolicyConstants.RETRY_ON_FAILURE_ATTR);
                if (IAePolicyConstants.RETRY_SUSPEND_ON_FAILURE.equals(onFailure)) {
                    getInvoke().setFault(aFault);
                    getInvoke().setState(AeBpelState.FAULTING);
                    AeSuspendReason reason = new AeSuspendReason(AeSuspendReason.SUSPEND_CODE_INVOKE_RETRY, getInvoke().getLocationPath(), null);
                    getInvoke().getProcess().suspend(reason);
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * Checks the current fault against the list of faults for inclusion or exclusion
    * to determine if the current fault is eligible for retry.
    * @param aFault
    * @param aAssertionEl
    */
    protected boolean isFaultEligibleForRetry(IAeFault aFault, Element aAssertionEl) {
        String faultList = aAssertionEl.getAttribute(IAePolicyConstants.FAULT_LIST_ATTR);
        if (AeUtil.notNullOrEmpty(faultList)) {
            String[] qnameList = faultList.split("\\s");
            for (int i = 0; i < qnameList.length; ++i) {
                if (isFaultNameMatch(aFault.getFaultName(), qnameList[i])) return true;
            }
            return false;
        }
        String faultExclusionList = aAssertionEl.getAttribute(IAePolicyConstants.FAULT_EXCLUSION_LIST_ATTR);
        if (AeUtil.notNullOrEmpty(faultExclusionList)) {
            String[] qnameList = faultExclusionList.split("\\s");
            for (int i = 0; i < qnameList.length; ++i) {
                if (isFaultNameMatch(aFault.getFaultName(), qnameList[i])) return false;
            }
            return true;
        }
        return true;
    }

    /**
    * Tests if the passed qname and string qname match.  The string qname can contain wildcards
    * in the form of an asterisk.  The string qname is in the format <CODE>{namespace}localpart</CODE>.
    * @param aFaultName the fault name to test.
    * @param aString qname string in form that can be passed to valurOf in QName. The format is <CODE>{namespace}localpart</CODE>.
    * @return boolean true if the fault names match.
    */
    private boolean isFaultNameMatch(QName aFaultName, String aString) {
        try {
            QName testName = QName.valueOf(aString);
            if (IAePolicyConstants.QNAME_WILDCARD.equals(testName.getNamespaceURI()) || AeUtil.compareObjects(aFaultName.getNamespaceURI(), testName.getNamespaceURI())) {
                if (IAePolicyConstants.QNAME_WILDCARD.equals(testName.getLocalPart()) || AeUtil.compareObjects(aFaultName.getLocalPart(), testName.getLocalPart())) return true;
            }
        } catch (Exception ex) {
            AeException.logError(ex, ex.getLocalizedMessage());
        }
        return false;
    }

    /**
    * Calls the passed service to decide whether and when the invoke should be retried.
    * @param aService the service to invoke.
    * @param aFault the fault which is being checked for retry.
    * @return int which will be the number of seconds before retrying, -1 means no retry,
    */
    protected int callRetryCheckService(String aService, IAeFault aFault) {
        int interval = -1;
        try {
            AeMessageContext context = new AeMessageContext();
            context.setServiceName(aService);
            Document[] array = new Document[1];
            array[0] = createRetryCheckInput(aFault);
            IAeWebServiceResponse resp = getInvoke().getProcess().getEngine().queueReceiveData(context, array);
            if (!resp.isFaultResponse()) {
                Element elem = ((Document) resp.getMessageData().getMessageData().get(IAePolicyConstants.RETRY_CHECK_OUTPUT_PART)).getDocumentElement();
                String retry = AeXmlUtil.getText(AeXmlUtil.findSubElement(elem, IAePolicyConstants.RETRY_TAG));
                if ("yes".equals(retry)) {
                    String intervalStr = AeXmlUtil.getText(AeXmlUtil.findSubElement(elem, IAePolicyConstants.INTERVAL_TAG));
                    if (AeUtil.notNullOrEmpty(intervalStr)) interval = Integer.parseInt(intervalStr);
                    setAlternateEndpoint(elem);
                }
            } else {
                AeException.logWarning(AeMessages.format("AeInvokeRetryPolicy.ERROR_RETRY_SERVICE_FAILURE", aService));
            }
        } catch (Exception e) {
            AeException.logError(e, AeMessages.format("AeInvokeRetryPolicy.ERROR_RETRY_SERVICE_FAILURE", aService));
        }
        return interval;
    }

    /**
    * @return the document to be passed to the retry check service operation.
    */
    protected Document createRetryCheckInput(IAeFault aFault) throws AeException {
        AeXMLParserBase parser = new AeXMLParserBase();
        Document doc = parser.createDocument();
        Element root = doc.createElementNS(IAePolicyConstants.ABPEL_RETRY_CHECK_NS, IAePolicyConstants.RETRY_CHECK_INPUT_TAG);
        doc.appendChild(root);
        root.setAttributeNS(IAeConstants.W3C_XMLNS, "xmlns", IAePolicyConstants.ABPEL_RETRY_CHECK_NS);
        AeXmlUtil.addElementNS(root, IAePolicyConstants.ABPEL_RETRY_CHECK_NS, "faultName", aFault.getFaultName().toString());
        AeXmlUtil.addElementNS(root, IAePolicyConstants.ABPEL_RETRY_CHECK_NS, "processId", "" + getInvoke().getProcess().getProcessId());
        AeXmlUtil.addElementNS(root, IAePolicyConstants.ABPEL_RETRY_CHECK_NS, "processName", getInvoke().getProcess().getName().toString());
        AeXmlUtil.addElementNS(root, IAePolicyConstants.ABPEL_RETRY_CHECK_NS, "invokePath", getInvoke().getLocationPath());
        AeXmlUtil.addElementNS(root, IAePolicyConstants.ABPEL_RETRY_CHECK_NS, "attempts", "" + (getRetries() + 1));
        AeXmlUtil.addElementNS(root, IAePolicyConstants.ABPEL_RETRY_CHECK_NS, "partnerLinkName", getDef().getPartnerLink());
        Element pl = AeXmlUtil.addElementNS(root, IAePolicyConstants.ABPEL_RETRY_CHECK_NS, "partnerLink", null);
        Document epr = getInvoke().findPartnerLink(getDef().getPartnerLink()).getPartnerReference().toDocument();
        pl.appendChild(doc.importNode(epr.getDocumentElement(), true));
        return doc;
    }

    /**
    * If the retry service responds with an alternate endpoint, update the partner endpoint
    * on the invoke to use the alternate for the attempt.  
    * 
    * @param aServiceResponse
    * @throws AeException
    */
    protected void setAlternateEndpoint(Element aServiceResponse) throws AeException {
        Map wsaNs = new HashMap();
        wsaNs.put("wsa03", IAeConstants.WSA_NAMESPACE_URI);
        wsaNs.put("wsa0403", IAeConstants.WSA_NAMESPACE_URI_2004_03);
        wsaNs.put("wsa0408", IAeConstants.WSA_NAMESPACE_URI_2004_08);
        wsaNs.put("wsa05", IAeConstants.WSA_NAMESPACE_URI_2005_08);
        String eprXPath = "//wsa03:EndpointReference | //wsa0403:EndpointReference | //wsa0408:EndpointReference | //wsa05:EndpointReference";
        List eprList = AeXPathUtil.selectNodes(aServiceResponse, eprXPath, wsaNs);
        if (eprList.size() > 0) {
            Object eprElem = eprList.get(0);
            if (eprElem instanceof Element) {
                AePartnerLink partnerLink = getInvoke().findPartnerLink(getDef().getPartnerLink());
                partnerLink.getPartnerReference().updateReferenceData((Element) eprElem);
            }
        }
    }

    /**
    * @return Returns the retries.
    */
    public int getRetries() {
        return mRetries;
    }

    /**
    * @param aRetries The retries to set.
    */
    public void setRetries(int aRetries) {
        mRetries = aRetries;
    }
}
