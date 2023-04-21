package org.easyrec.controller;

import org.easyrec.model.web.Operator;
import org.easyrec.model.web.RemoteTenant;
import org.easyrec.service.web.ViewInitializationService;
import org.easyrec.store.dao.web.RemoteTenantDAO;
import org.easyrec.utils.Security;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This controller contains ItemType specific views to manage the itemTypes for a tenant.
 * User: dmann
 * Date: 16.05.11
 * Time: 14:37
 */
public class TypeManagerController extends MultiActionController {

    private ViewInitializationService viewInitializationService;

    private RemoteTenantDAO remoteTenantDAO;

    public void setViewInitializationService(ViewInitializationService viewInitializationService) {
        this.viewInitializationService = viewInitializationService;
    }

    public void setRemoteTenantDAO(RemoteTenantDAO remoteTenantDAO) {
        this.remoteTenantDAO = remoteTenantDAO;
    }

    public ModelAndView typemanager(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        ModelAndView mav = new ModelAndView("page");
        mav.addObject("page", "easyrec/typemanager");
        mav.addObject("menubar", "itemtypes");
        mav.addObject("selectedMenu", "myEasyrec");
        Operator signedInOperator = Security.signedInOperator(request);
        RemoteTenant remoteTenant = viewInitializationService.initializeView(request, mav);
        if (signedInOperator != null) {
            mav.addObject("apiKey", signedInOperator.getApiKey());
            mav.addObject("tenants", remoteTenantDAO.getTenantsFromOperator(remoteTenant.getOperatorId()));
        }
        return mav;
    }
}
