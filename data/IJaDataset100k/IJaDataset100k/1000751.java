package org.oasis.wsrp.test.impl.message;

import javax.xml.transform.TransformerException;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * The Producer can use wildcards when specifying the value 
 * for MarkupTypes.mimeType.
 *
 * @author Julie MacNaught (jmacna@us.ibm.com)
 * @author Martin Fanta (Martin.Fanta@cz.ibm.com)
 *
 */
public class RP0130 extends WSRPMessageAssertionProcess {

    public RP0130(BaseMessageValidator impl) {
        super(impl);
    }

    public AssertionResult validate(TestAssertion testAssertion, EntryContext entryContext) throws WSIException {
        if (validator.isOneWayResponse(entryContext)) {
            na();
        } else {
            try {
                Document respDoc = entryContext.getResponseDocument();
                NodeList markupTypesNodes = NodeUtils.getNodes(respDoc, "markupTypes");
                int nodeCount = markupTypesNodes.getLength();
                int wildCardCount = 0;
                if (nodeCount > 0) {
                    String mimeType;
                    for (int ii = 0; ii < nodeCount; ii++) {
                        mimeType = NodeUtils.getTextNode(markupTypesNodes.item(ii), "mimeType").getNodeValue();
                        if (mimeType.indexOf("*") != -1) {
                            wildCardCount++;
                        }
                    }
                    if (wildCardCount > 0) {
                        warn("The Producer used wildcard in " + wildCardCount + " MimeTypes");
                    } else {
                        pass();
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
