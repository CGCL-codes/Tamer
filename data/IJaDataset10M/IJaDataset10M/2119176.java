package com.kwoksys.action.hardware;

import com.kwoksys.action.base.BaseAction;
import com.kwoksys.action.common.template.*;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.system.dto.Link;
import com.kwoksys.framework.system.Access;
import com.kwoksys.framework.system.AppPaths;
import com.kwoksys.framework.system.RequestContext;
import com.kwoksys.framework.util.HttpUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action class for deleting hardware.
 */
public class HardwareDeleteAction extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestContext requestContext = new RequestContext(request);
        AccessUser user = requestContext.getUser();
        Integer hardwareId = HttpUtils.getParameter(request, "hardwareId");
        HardwareService hardwareService = ServiceProvider.getHardwareService();
        Hardware hardware = hardwareService.getHardware(hardwareId);
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setTemplatePath(mapping, SUCCESS);
        ObjectDeleteTemplate delete = new ObjectDeleteTemplate();
        standardTemplate.addTemplate(delete);
        delete.setFormAction(AppPaths.ROOT + AppPaths.HARDWARE_DELETE_2 + "?hardwareId=" + hardwareId);
        delete.setFormCancelAction(AppPaths.HARDWARE_DETAIL + "?hardwareId=" + hardwareId);
        delete.setConfirmationMsgKey("itMgmt.hardwareDelete.confirm");
        delete.setSubmitButtonKey("itMgmt.hardwareDelete.submitButton");
        HardwareSpecTemplate tmpl = new HardwareSpecTemplate(hardware);
        standardTemplate.addTemplate(tmpl);
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] { hardware.getName() });
        if (Access.hasPermission(user, AppPaths.HARDWARE_LIST)) {
            Link link = new Link();
            link.setPath(AppPaths.ROOT + AppPaths.HARDWARE_LIST);
            link.setTitleKey("itMgmt.cmd.hardwareList");
            header.addHeaderCmds(link);
        }
        return standardTemplate.findForward(STANDARD_TEMPLATE);
    }
}
