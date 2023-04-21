package org.wijiscommons.ssaf.integration.disclosurecontrol.cdcl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.wijiscommons.cdcl.gatepoint.CDCLGatepoint;
import org.wijiscommons.cdcl.gatepoint.CDCLGatepointFactory;
import org.wijiscommons.cdcl.gatepoint.DisclosureProblemInputMessage;
import org.wijiscommons.cdcl.gatepoint.DisclosureProblemOutputMessage;
import org.wijiscommons.cdcl.gatepoint.GatepointException;
import org.wijiscommons.cdcl.gatepoint.GatepointRuntimeException;
import org.wijiscommons.cdcl.gatepoint.CDCLGatepoint.OperationModeEnum;
import org.wijiscommons.ssaf.integration.disclosurecontrol.SSAFDisclosureControlGateway;

/**
 * This implementation of the {@link SSAFDisclosureControlGateway}, communicates with 
 * a CDCL Gatepoint to provide disclosure control services for a SSAF instance.
 *
 * @author Pattabi Doraiswamy (http://pattabidoraiswamy.com)
 * @since Jan 29, 2009
 */
public class SSAFCDCLGatewayImpl implements SSAFDisclosureControlGateway {

    private static final Log LOG = LogFactory.getLog(SSAFCDCLGatewayImpl.class);

    private static final CDCLGatepoint CDCL_GATEPOINT = CDCLGatepointFactory.getGatepointInstance();

    /**
     * {@inheritDoc}
     */
    public Document performDisclosureControl(Document ssafDocument) {
        Node samlAssertionResponseNode = SSAFDocumentUtil.getSAMLAssertionForRecipientUser(ssafDocument);
        Node messagePayload = SSAFDocumentUtil.getMessagePayload(ssafDocument);
        DisclosureProblemOutputMessage disclosureProblemOutputMessage = invokeCDCLGatepoint(messagePayload, samlAssertionResponseNode);
        updatePayload(ssafDocument, messagePayload, disclosureProblemOutputMessage);
        updateProvenanceInfo(ssafDocument, disclosureProblemOutputMessage);
        return ssafDocument;
    }

    private void updatePayload(Document ssafDocument, Node originalMessagePayload, DisclosureProblemOutputMessage disclosureProblemOutputMessage) {
        Document ssafProcessedDocument = (Document) disclosureProblemOutputMessage.getResultDocument().getDocument();
        Node processedMessagePayload = ssafProcessedDocument.getDocumentElement();
        processedMessagePayload = ssafDocument.importNode(processedMessagePayload, true);
        ssafDocument.getDocumentElement().replaceChild(processedMessagePayload, originalMessagePayload);
    }

    private void updateProvenanceInfo(Document ssafDocument, DisclosureProblemOutputMessage disclosureProblemOutputMessage) {
        String gatepointURI = disclosureProblemOutputMessage.getOutputMessageMetaData().getProvenance().getGatePointURI();
        String gatepointProcessTimestamp = disclosureProblemOutputMessage.getOutputMessageMetaData().getProvenance().getGatepointProcessTimeStamp().toGMTString();
        Node gatepointProvenanceNode = CDCLResponseAdapter.createDisclosureControlProvenanceNode(ssafDocument, disclosureProblemOutputMessage);
        SSAFDocumentUtil.addNewProcessHistoryItemNode(ssafDocument, gatepointProvenanceNode, gatepointURI, gatepointProcessTimestamp);
    }

    private DisclosureProblemOutputMessage invokeCDCLGatepoint(Node messagePayload, Node samlAssertionResponseNode) {
        DisclosureProblemOutputMessage disclosureProblemOutputMessage = null;
        DisclosureProblemInputMessage disclosureProblemInputMessage = CDCLComponentBuilder.createDisclosureProblemInputMessage(samlAssertionResponseNode, messagePayload);
        Map<String, DisclosureProblemInputMessage> inputMessages = new HashMap<String, DisclosureProblemInputMessage>(1);
        String messageID = UUID.randomUUID().toString();
        inputMessages.put(messageID, disclosureProblemInputMessage);
        Map<String, DisclosureProblemOutputMessage> disclosureProblemOutputMessages = null;
        try {
            disclosureProblemOutputMessages = CDCL_GATEPOINT.processMessages(inputMessages, OperationModeEnum.NORMAL_MODE);
        } catch (GatepointRuntimeException gatepointRuntimeException) {
            LOG.error(gatepointRuntimeException);
        } catch (GatepointException gatepointException) {
            LOG.error(gatepointException);
        }
        disclosureProblemOutputMessage = disclosureProblemOutputMessages.get(messageID);
        return disclosureProblemOutputMessage;
    }
}
