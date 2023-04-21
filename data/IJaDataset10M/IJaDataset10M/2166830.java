package cnaf.sidoc.ide.docflows.ui.internal.editor.contentassist;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.w3c.dom.Node;
import cnaf.sidoc.ide.core.SIDocXMLConstants;
import cnaf.sidoc.ide.ui.editor.ImageResource;
import cnaf.sidoc.ide.ui.editor.contentassist.SIDocNodeContentAssistAdditionalProposalInfoProvider;

public class ConditionContentAssistAdditionalProposalInfoProvider extends SIDocNodeContentAssistAdditionalProposalInfoProvider implements SIDocXMLConstants {

    public Image getImage(Node node) {
        return ImageResource.getImage(ImageResource.IMG_CONDITION_OBJ);
    }

    public String doGetTextInfo(IDOMElement element) {
        StringBuilder buf = new StringBuilder();
        buf.append("<b>Condition URI :</b> ");
        buf.append(element.getAttribute(URI_ATTR));
        buf.append("<br><b>Label: </b>");
        buf.append(element.getAttribute(LABEL_ATTR));
        buf.append("<br><b>Class: </b>");
        buf.append(element.getAttribute(CLASS_ATTR));
        buf.append("<br><b>File: </b> ");
        String baseLocation = element.getModel().getBaseLocation();
        buf.append(baseLocation);
        return buf.toString();
    }
}
