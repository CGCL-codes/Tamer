package com.kwoksys.action.hardware;

import com.kwoksys.action.base.BaseAction;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.system.dto.linking.SoftwareContactLink;
import com.kwoksys.biz.system.dto.linking.HardwareContactLink;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.framework.system.AppPaths;
import com.kwoksys.framework.util.HttpUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action class for removing a Hardware Contact mapping.
 */
public class HardwareContactRemoveAction extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HardwareForm actionForm = (HardwareForm) form;
        HardwareService hardwareService = ServiceProvider.getHardwareService();
        Hardware hardware = hardwareService.getHardware(actionForm.getHardwareId());
        HardwareContactLink hardwareContactLink = new HardwareContactLink();
        hardwareContactLink.setHardwareId(hardware.getId());
        hardwareContactLink.setContactId(Integer.valueOf(actionForm.getContactId()));
        SystemService systemService = ServiceProvider.getSystemService();
        ActionMessages errors = systemService.deleteObjectMapping(hardwareContactLink.createObjectMap());
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("error");
        } else {
            return redirect(AppPaths.HARDWARE_CONTACTS + "?hardwareId=" + hardware.getId());
        }
    }
}
