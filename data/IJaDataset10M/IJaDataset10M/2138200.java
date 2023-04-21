package com.liferay.portlet.journal.action;

import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portlet.journal.DuplicateStructureIdException;
import com.liferay.portlet.journal.NoSuchStructureException;
import com.liferay.portlet.journal.StructureIdException;
import com.liferay.portlet.journal.service.JournalStructureServiceUtil;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="CopyStructureAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class CopyStructureAction extends PortletAction {

    public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        try {
            copyStructure(actionRequest);
            sendRedirect(actionRequest, actionResponse);
        } catch (Exception e) {
            if (e instanceof NoSuchStructureException || e instanceof PrincipalException) {
                SessionErrors.add(actionRequest, e.getClass().getName());
                setForward(actionRequest, "portlet.journal.error");
            } else if (e instanceof DuplicateStructureIdException || e instanceof StructureIdException) {
                SessionErrors.add(actionRequest, e.getClass().getName());
            } else {
                throw e;
            }
        }
    }

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse) throws Exception {
        return mapping.findForward(getForward(renderRequest, "portlet.journal.copy_structure"));
    }

    protected void copyStructure(ActionRequest actionRequest) throws Exception {
        long groupId = ParamUtil.getLong(actionRequest, "groupId");
        String oldStructureId = ParamUtil.getString(actionRequest, "oldStructureId");
        String newStructureId = ParamUtil.getString(actionRequest, "newStructureId");
        boolean autoStructureId = ParamUtil.getBoolean(actionRequest, "autoStructureId");
        JournalStructureServiceUtil.copyStructure(groupId, oldStructureId, newStructureId, autoStructureId);
    }
}
