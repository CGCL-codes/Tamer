package com.kwoksys.action.software;

import com.kwoksys.action.base.BaseAction;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dto.SoftwareLicense;
import com.kwoksys.framework.connection.database.QueryBits;
import com.kwoksys.framework.util.HtmlUtils;
import com.kwoksys.framework.util.HttpUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Action class serving page for Ajax call.
 */
public class AjaxGetLicenseBySoftwareAction extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer softwareId = HttpUtils.getParameter(request, "softwareId", 0);
        List licenseOptions = new ArrayList();
        if (softwareId != 0) {
            QueryBits query = new QueryBits();
            query.setOrderByColumn(HardwareQueries.getOrderByColumn(SoftwareLicense.LICENSE_KEY));
            HardwareService hardwareService = ServiceProvider.getHardwareService();
            for (SoftwareLicense license : hardwareService.getAvailableLicenses(query, softwareId)) {
                licenseOptions.add(new LabelValueBean(HtmlUtils.encode(license.getKey()) + (license.getNote().isEmpty() ? "" : " (" + SoftwareUtils.formatLicenseKey(license.getNote()) + ")"), String.valueOf(license.getId())));
            }
        }
        request.setAttribute("licenseOptions", licenseOptions);
        return mapping.findForward("success");
    }
}
