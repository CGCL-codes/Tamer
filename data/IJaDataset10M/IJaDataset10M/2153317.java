package org.oasis.wsrp.test.impl.message;

import javax.xml.transform.TransformerException;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * When an activated portlet URL has not specified the wsrp-interactionState
 * portlet URL parameter, the Consumer shall not supply any value in the
 * InteractionParams.interactionState field.
 *
 * @author Julie MacNaught (jmacna@us.ibm.com)
 * @author Martin Fanta (Martin.Fanta@cz.ibm.com)
 *
 */
public class RP1440 extends BaseRP {

    public RP1440(BaseMessageValidator impl) {
        super(impl);
    }

    private static final String PARAM_NAME_NO_INTERACTION_STATE = "wsrp_test_noInteractionState";

    protected void processPerformBlockingInteractionRequest(Document reqDoc) throws WSIException, TransformerException {
        Node iaParamsNode = NodeUtils.getNode(reqDoc, "interactionParams");
        if (iaParamsNode != null) {
            NodeList formParamNodes = NodeUtils.getNodes(iaParamsNode, "formParameters");
            int formParamCount = formParamNodes.getLength();
            Node formParamNode;
            String formParamName;
            for (int ii = 0; ii < formParamCount; ii++) {
                formParamNode = formParamNodes.item(ii);
                formParamName = NodeUtils.getAttributeValue(formParamNode, "name");
                if (PARAM_NAME_NO_INTERACTION_STATE.equals(formParamName)) {
                    Node iaStateNode = NodeUtils.getTextNode(iaParamsNode, "interactionState");
                    String iaState = NodeUtils.getSafeTextNodeValue(iaStateNode);
                    if ((iaState == null) || iaState.equals("")) {
                        pass();
                    } else {
                        fail("Interaction state not empty");
                    }
                    break;
                }
            }
        }
    }
}
