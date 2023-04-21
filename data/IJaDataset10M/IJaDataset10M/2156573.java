package com.kwoksys.action.hardware;

import com.kwoksys.action.base.BaseAction;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.system.dto.linking.HardwareIssueLink;
import com.kwoksys.framework.system.AppPaths;
import com.kwoksys.framework.system.RequestContext;
import com.kwoksys.framework.util.HttpUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action class for adding a hardware component.
 */
public class HardwareIssueAdd2Action extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestContext requestContext = new RequestContext(request);
        AccessUser user = requestContext.getUser();
        Integer hardwareId = HttpUtils.getParameter(request, "hardwareId");
        Integer issueId = HttpUtils.getParameter(request, "issueId");
        HardwareService hardwareService = ServiceProvider.getHardwareService();
        hardwareService.getHardware(hardwareId);
        HardwareIssueLink issueMap = new HardwareIssueLink();
        issueMap.setHardwareId(hardwareId);
        issueMap.setIssueId(issueId);
        issueMap.setCreatorId(user.getId());
        ActionMessages errors = hardwareService.addHardwareIssue(issueMap);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("error");
        } else {
            return redirect(AppPaths.HARDWARE_ISSUE + "?hardwareId=" + hardwareId);
        }
    }
}
