package org.hip.vif.member.tasks;

import org.hip.kernel.exc.VException;
import org.hip.kernel.servlet.HtmlView;
import org.hip.kernel.servlet.impl.DefaultHtmlPage;
import org.hip.vif.member.Activator;
import org.hip.vif.member.Constants;
import org.hip.vif.member.views.MemberView;
import org.hip.vif.registry.IPluggableTask;
import org.hip.vif.servlets.AbstractVIFAdminTask;
import org.hip.vif.servlets.VIFContext;

/**
 * Task to create a new member entry
 *
 * @author Luthiger
 * Created: 02.12.2007
 */
public class MemberNewTask extends AbstractVIFAdminTask implements IPluggableTask {

    @Override
    protected String needsPermission() {
        return "searchMembers";
    }

    /**
	 * Creates the form to enter new member data
	 */
    public void runChecked() throws VException {
        VIFContext lContext = (VIFContext) getContext();
        try {
            DefaultHtmlPage lPage = new DefaultHtmlPage();
            String lFromPage = lContext.getParameterValue(VIFContext.KEY_FROM_PAGE);
            lFromPage = (lFromPage == null) ? "" : lFromPage;
            HtmlView lView = new MemberView(lContext, "", "", "", "", "", "", "", "", "", "0", lFromPage, true);
            prepareVIFPage(lPage, Activator.getPartletHelper().renderOnLoad(Constants.TASK_SET_ID_BACK));
            lPage.add(lView);
            lContext.setView(lPage);
        } catch (Exception exc) {
            throw createContactAdminException(exc, lContext.getLocale());
        }
    }
}
