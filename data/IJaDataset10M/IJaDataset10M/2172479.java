package cnaf.sidoc.ide.modules.ui.internal.editor.contentassist;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.search.core.util.DOMUtils;
import org.w3c.dom.Node;
import cnaf.sidoc.ide.core.SIDocXMLConstants;
import cnaf.sidoc.ide.ui.editor.ImageResource;
import cnaf.sidoc.ide.ui.editor.contentassist.SIDocNodeContentAssistAdditionalProposalInfoProvider;

public class ModulesContentAssistAdditionalProposalInfoProvider extends SIDocNodeContentAssistAdditionalProposalInfoProvider implements SIDocXMLConstants {

    public Image getImage(Node node) {
        return ImageResource.getImage(ImageResource.IMG_MODULE_OBJ);
    }

    public String doGetTextInfo(IDOMElement element) {
        IFile file = DOMUtils.getFile(element);
        StringBuilder buf = new StringBuilder();
        buf.append("<b>Collection: </b>");
        buf.append(file.getProject().getName());
        buf.append("<br><b>Module:</b> ");
        buf.append(element.getAttribute(URI_ATTR));
        buf.append("<br><b>Label:</b> ");
        buf.append(element.getAttribute(LABEL_ATTR));
        buf.append("<br><b>Public ID:</b> ");
        buf.append(element.getAttribute(PUBLIC_ID_ATTR));
        buf.append("<br><b>Function group:</b> ");
        buf.append(element.getAttribute(FUNCTION_GROUP_ATTR));
        buf.append("<br><b>File:</b> ");
        String baseLocation = element.getModel().getBaseLocation();
        buf.append(baseLocation);
        return buf.toString();
    }
}
