package fi.hip.gb.gridlib.idff12.workunit;

import javax.xml.namespace.QName;
import org.sourceid.idff12.protocol.idff12.StatusQNames;
import org.sourceid.protocol.liberty.idff12.xml.ProxyDelegationResponseDocument;
import org.sourceid.protocol.liberty.idff12.xml.ProxyDelegationResponseType;
import org.sourceid.idff12.workflow.IDFFWorkflowFailure;
import org.sourceid.workflow.WorkUnit;
import org.sourceid.workflow.WorkUnitContext;
import org.sourceid.workflow.WorkflowFailure;

/**
 * Processes the Proxy Delegation Response at a GridSP.
 * @author Henri Mikkonen
 * @version $Id: ProcessPDResponse.java,v 1.2 2006/05/22 12:16:02 mikkonen Exp $
 */
public class ProcessPDResponse implements WorkUnit {

    /**
     * Processes the Proxy Delegation Response at a GridSP.
     * @param ctx The <code>WorkUnitContext</code>.
     * @param pdResponseDoc The <code>ProxyDelegationResponseDocument</code>.
     * @param failure The <code>WorkflowFailure</code>.
     */
    public void execute(WorkUnitContext ctx, ProxyDelegationResponseDocument pdResponseDoc, WorkflowFailure failure) throws IDFFWorkflowFailure {
        if (failure == null) {
            ProxyDelegationResponseType response = pdResponseDoc.getProxyDelegationResponse();
            QName statusValue = response.getStatus().getStatusCode().getValue();
            if (!statusValue.equals(StatusQNames.SAML_STATUS_SUCCESS)) {
                throw new IDFFWorkflowFailure("Proxy Delegation Response not " + "successful.  Status: " + statusValue);
            }
        }
    }
}
