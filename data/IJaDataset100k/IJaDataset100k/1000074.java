package net.sf.eclipse.portlet.ui.internal.sse.contentassist;

import java.util.List;
import java.util.Locale;
import java.util.Vector;
import net.sf.eclipse.portlet.core.Constants;
import net.sf.eclipse.portlet.core.model.IPortletArtifact;
import net.sf.eclipse.portlet.core.model.descriptor.ICustomWindowState;
import net.sf.eclipse.portlet.ui.image.ImageConstants;
import net.sf.eclipse.portlet.ui.image.ImageType;
import net.sf.eclipse.portlet.ui.image.PortletImageRegistry;
import net.sf.eclipse.portlet.ui.internal.Messages;
import net.sf.eclipse.portlet.ui.internal.sse.SSEUtil;
import org.eclipse.jface.text.IDocument;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;

/**
 * Implement autosuggestion for supported window states. Compute suggestion using defined window states and build-in window states.
 * 
 * @author fwjwiegerinck
 * @since 0.2
 */
public class SupportedWindowStatesContentAssistProcessor extends AbstractGenericXmlContentAssistProcessor<ICustomWindowState> {

    /**
	 * internal implementation used to mix custom with buildin portlet modes
	 */
    static class BuildInWindowState implements ICustomWindowState {

        private String name;

        private String description;

        public BuildInWindowState(String name, String description) {
            super();
            this.name = name;
            this.description = description;
        }

        public void addDescription(Locale locale, String description) {
        }

        public String getDescription(Locale locale) {
            return this.description;
        }

        public String getId() {
            return null;
        }

        public String getName() {
            return this.name;
        }

        public void removeDescription(Locale locale) {
        }

        public void setId(String id) {
        }

        public void setName(String name) throws IllegalArgumentException {
        }
    }

    /**
	 * Initialize based upon window-state element
	 */
    public SupportedWindowStatesContentAssistProcessor() {
        super(Constants.XML_DESCRIPTOR_ELEMENT_PORTLET_SUPPORTS_WINDOWSTATE);
    }

    /**
	 * @see net.sf.eclipse.portlet.ui.internal.sse.contentassist.AbstractGenericXmlContentAssistProcessor#computeElementsProposal(org.eclipse.jface.text.IDocument, java.lang.String)
	 */
    @Override
    protected List<ICustomWindowState> computeElementsProposal(IDocument document) {
        IPortletArtifact artifact = SSEUtil.getPortletArtifact(document);
        List<ICustomWindowState> returnValue = new Vector<ICustomWindowState>();
        returnValue.add(new BuildInWindowState("NORMAL", "Build-in window state"));
        returnValue.add(new BuildInWindowState("MAXIMIZED", "Build-in window state"));
        returnValue.add(new BuildInWindowState("MINIMIZED", "Build-in window state"));
        if (artifact != null) {
            returnValue.addAll(artifact.getPortletApplication().getCustomWindowStates());
        }
        return returnValue;
    }

    /**
	 * @see net.sf.eclipse.portlet.ui.internal.sse.contentassist.AbstractGenericXmlContentAssistProcessor#getAdditionalInformation(java.lang.Object)
	 */
    @Override
    protected String getAdditionalInformation(ICustomWindowState element) {
        String description = element.getDescription(null);
        if (description == null) description = "";
        String message;
        if (element instanceof BuildInWindowState) {
            message = Messages.sse_contentassist_supportedwindowstate_buildin_additionalInformation;
        } else {
            message = Messages.sse_contentassist_supportedwindowstate_custom_additionalInformation;
        }
        return NLS.bind(message, element.getName(), description);
    }

    /**
	 * @see net.sf.eclipse.portlet.ui.internal.sse.contentassist.AbstractGenericXmlContentAssistProcessor#getDisplayString(java.lang.Object)
	 */
    @Override
    protected String getDisplayString(ICustomWindowState element) {
        String message;
        if (element instanceof BuildInWindowState) {
            message = Messages.sse_contentassist_supportedwindowstate_buildin_displayString;
        } else {
            message = Messages.sse_contentassist_supportedwindowstate_custom_displayString;
        }
        return NLS.bind(message, element.getName());
    }

    /**
	 * @see net.sf.eclipse.portlet.ui.internal.sse.contentassist.AbstractGenericXmlContentAssistProcessor#getImage(java.lang.Object)
	 */
    @Override
    protected Image getImage(ICustomWindowState element) {
        return PortletImageRegistry.getImage(ImageType.ICON, ImageConstants.PORTLET_CUSTOMWINDOWSTATE);
    }

    /**
	 * @see net.sf.eclipse.portlet.ui.internal.sse.contentassist.AbstractGenericXmlContentAssistProcessor#getReplacement(java.lang.Object)
	 */
    @Override
    protected String getReplacement(ICustomWindowState element) {
        return element.getName();
    }
}
