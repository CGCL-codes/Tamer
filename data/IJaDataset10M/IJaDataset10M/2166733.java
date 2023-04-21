package org.hip.vif.groupadmin.views;

import org.hip.kernel.servlet.Context;
import org.hip.kernel.util.XMLRepresentation;
import org.hip.vif.groupadmin.Activator;
import org.hip.vif.servlets.AbstractVIFView;
import org.hip.vif.servlets.VIFContext;
import org.osgi.framework.Bundle;

/**
 * View to display the contributions selected for deletion for that
 * the user can confirm.
 * 
 * @author Benno Luthiger
 * Created on Aug 25, 2008
 */
public class ContributionDeleteConfirmationView extends AbstractVIFView {

    private static String XSL_FILE = "bodyConfirm.xsl";

    /**
	 * ContributionDeleteConfirmationView constructor.
	 * 
	 * @param inContext Context
	 * @param inConfirmationXML String
	 * @param inGroupID String
	 */
    public ContributionDeleteConfirmationView(Context inContext, String inConfirmationXML, String inGroupID) {
        super(inContext);
        setStylesheetParameter("bundleName", Activator.getBundleName());
        setStylesheetParameter("groupID", inGroupID);
        prepareTransformation(createXML(inConfirmationXML));
    }

    /**
	 * @see org.hip.kernel.servlet.impl.AbstractHtmlView#getXMLName()
	 */
    protected String getXMLName() {
        return XSL_FILE;
    }

    private XMLRepresentation createXML(String inConfirmationXML) {
        StringBuffer outXML = new StringBuffer(VIFContext.HEADER);
        outXML.append(VIFContext.ROOT_BEGIN);
        outXML.append(inConfirmationXML);
        outXML.append(VIFContext.ROOT_END);
        return new XMLRepresentation(new String(outXML));
    }

    @Override
    protected Bundle getBundle() {
        return Activator.getContext().getBundle();
    }
}
