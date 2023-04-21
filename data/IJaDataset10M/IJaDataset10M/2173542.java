package org.oasis.wsrp.test.impl.message;

import javax.xml.transform.TransformerException;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * A high function Consumer's value for secureDefaultTemplate includes 
 * the parameters wsrp-navigationalState, wsrp-interactionState, 
 * wsrp-windowState and wsrp-mode whenever the secureRenderTemplate 
 * or secureBlockingActionTemplate are not also supplied.
 *
 * @author Julie MacNaught (jmacna@us.ibm.com)
 * @author Martin Fanta (Martin.Fanta@cz.ibm.com)
 *
 */
public class RP1910 extends WSRPMessageAssertionProcess {

    public RP1910(BaseMessageValidator impl) {
        super(impl);
    }

    public AssertionResult validate(TestAssertion testAssertion, EntryContext entryContext) throws WSIException {
        if (validator.isOneWayResponse(entryContext)) {
            na();
        } else {
            try {
                Document reqDoc = entryContext.getRequestDocument();
                Node runtimeContextNode = NodeUtils.getNode(reqDoc, "runtimeContext");
                if (runtimeContextNode != null) {
                    Node templatesNode = NodeUtils.getNode(runtimeContextNode, "templates");
                    if (templatesNode != null) {
                        if ((NodeUtils.getTextNode(templatesNode, "secureRenderTemplate") == null) || (NodeUtils.getTextNode(templatesNode, "secureBlockingActionTemplate") == null)) {
                            Node secureDefaultTemplateNode = NodeUtils.getTextNode(templatesNode, "secureDefaultTemplate");
                            if (secureDefaultTemplateNode != null) {
                                String secureDefaultTemplateValue = secureDefaultTemplateNode.getNodeValue();
                                if ((secureDefaultTemplateValue.indexOf("wsrp-navigationalState") == -1) || (secureDefaultTemplateValue.indexOf("wsrp-interactionState") == -1) || (secureDefaultTemplateValue.indexOf("wsrp-windowState") == -1) || (secureDefaultTemplateValue.indexOf("wsrp-mode") == -1)) {
                                    fail("SecureDefaultTemplate of this high function Consumer did not include one of the wsrp-navigationalState, wsrp-interactionState, wsrp-windowState, wsrp-mode parameters required when either secureRenderTemplate or secureBlockingActionTemplate is missing");
                                } else {
                                    pass();
                                }
                            } else {
                                fail("SecureDefaultTemplate missing while either of secureRenderTemplate and secureBlockingActionTemplate also missing for this high function Consumer.");
                            }
                        } else {
                            na();
                        }
                    } else {
                        na();
                    }
                } else {
                    na();
                }
            } catch (TransformerException te) {
                te.printStackTrace();
                fail(te.getMessage());
            }
        }
        return createAssertionResult(testAssertion, result, failureDetailMessage);
    }
}
