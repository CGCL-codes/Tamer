package org.hip.vif.admin.admin.tasks;

import org.hip.kernel.bom.OrderObject;
import org.hip.kernel.bom.impl.OrderObjectImpl;
import org.hip.kernel.bom.impl.OutlinedQueryResult;
import org.hip.kernel.exc.VException;
import org.hip.kernel.servlet.HtmlView;
import org.hip.kernel.servlet.impl.AbstractHtmlPage;
import org.hip.kernel.servlet.impl.CssLink;
import org.hip.kernel.servlet.impl.DefaultHtmlPage;
import org.hip.vif.admin.admin.Activator;
import org.hip.vif.admin.admin.Constants;
import org.hip.vif.admin.admin.views.SendMailView;
import org.hip.vif.bom.GroupHome;
import org.hip.vif.bom.RoleHome;
import org.hip.vif.menu.AppMenu;
import org.hip.vif.registry.IPluggableTask;
import org.hip.vif.servlets.AbstractVIFAdminTask;
import org.hip.vif.servlets.VIFContext;
import org.hip.vif.util.EditorWidget;

/**
 * Shows the form to enter mail subject and text for a mail to the members or participants of selected groups.
 *
 * @author Luthiger
 * Created: 24.07.2009
 */
public class ShowSendMailTask extends AbstractVIFAdminTask implements IPluggableTask {

    protected static final String EDITOR_ID = "textileEdit";

    @Override
    protected String needsPermission() {
        return Constants.PERMISSION_SEND_MAIL;
    }

    @Override
    protected void runChecked() throws VException {
        VIFContext lContext = (VIFContext) getContext();
        DefaultHtmlPage lPage = new DefaultHtmlPage();
        GroupHome lGroupHome = getGroupHome();
        try {
            Long lActorID = lContext.getActor().getActorID();
            boolean isAdmin = isAdmin(lActorID);
            OutlinedQueryResult lResult = lGroupHome.selectForAdministration(lActorID, createOrder());
            String lWebappURL = lContext.getWebappURL();
            HtmlView lView = new SendMailView(lContext, lResult.getQueryResult(), lResult.getCount(), isAdmin, EditorWidget.getSetupScript(EDITOR_ID, lWebappURL, lContext.getServletPath()), EDITOR_ID);
            lPage.add(lView);
            preparePageEditor(lPage, lWebappURL);
            prepareVIFPage(lPage, AppMenu.renderOnLoad(true, Activator.getBundleName(), Constants.TASK_SET_ID_ADMIN));
            lContext.setView(lPage);
        } catch (Exception exc) {
            throw createContactAdminException(exc, lContext.getLocale());
        }
    }

    protected void preparePageEditor(AbstractHtmlPage inPage, String inWebappURL) {
        inPage.setScriptLinks(EditorWidget.getScriptLinks(inWebappURL));
        for (CssLink lCss : EditorWidget.getCssLinks(inWebappURL)) {
            inPage.getCssLinks().addCssLink(lCss);
        }
    }

    protected boolean isAdmin(Long inActorID) throws Exception {
        int lRole = getMemberCacheHome().getMember(inActorID.toString()).getBestRole();
        if (lRole == RoleHome.ROLE_SU || lRole == RoleHome.ROLE_ADMIN) {
            return true;
        }
        return false;
    }

    protected OrderObject createOrder() throws VException {
        OrderObject outOrder = new OrderObjectImpl();
        outOrder.setValue(GroupHome.KEY_NAME, 0);
        return outOrder;
    }
}
