package com.germinus.portlet.scribe.action;

import com.germinus.xpression.cms.action.CMSPortletAction;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 *
 * User: Eduardo Canuria
 * Date: 21-jul-2008
 * Time: 10:38:02
 *
 */
public class ViewAction extends CMSPortletAction {

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        return mapping.findForward(SUCCESS);
    }
}
