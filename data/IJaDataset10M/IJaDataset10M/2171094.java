package com.faceye.components.portal.web.controller;

import java.io.Serializable;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.faceye.components.portal.dao.model.Portlet;
import com.faceye.components.portal.service.iface.IPortletService;
import com.faceye.core.util.helper.JSONUtil;
import com.faceye.core.web.controller.ExtTemplateAction;

public class PortletAction extends ExtTemplateAction {

    private IPortletService portletService = null;

    public IPortletService getPortletService() {
        return portletService;
    }

    public void setPortletService(IPortletService portletService) {
        this.portletService = portletService;
    }

    @Override
    protected void onInitEntity(HttpServletRequest request, Object o, ActionForm form) {
    }

    @Override
    protected void onInitForm(HttpServletRequest request, ActionForm form, Object o) {
    }

    protected String getPageJson(HttpServletRequest request) {
        String json = this.getPortletService().getPortletsJson(super.getHttp().getCurrentIndex(request), super.getHttp().getPageSize(request));
        return json;
    }

    /**
     *  根据portalColumn id 取得portlet
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward getPortletsByPortalColumn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Map params = this.getRequestParameterMap(request);
        String portalColumnId = params.get("portalColumnId").toString();
        String json = this.getPortletService().getPortletsByPortalColumnId(portalColumnId).json();
        this.jsonPrint(response, json);
        return null;
    }

    /**
     * 根据当前用户ID为用户进行portlet订阅
     */
    public ActionForward getPortletsByUserIdForSubscribe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Serializable userId = this.getHttp().getUserId(request);
        int startIndex = this.getHttp().getCurrentIndex(request);
        int pageSize = this.getHttp().getPageSize(request);
        Serializable portalId = this.getHttp().getParameter(request, "portalId");
        String json = this.getPortletService().getPagePortletsByUserIdForUserSubscribe(userId, portalId, startIndex, pageSize);
        this.jsonPrint(response, json);
        return null;
    }

    public ActionForward get(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String id = this.getHttp().getParameter(request, "id");
        if (StringUtils.isNotEmpty(id)) {
            Portlet portlet = (Portlet) this.getPortletService().getObject(Portlet.class, id);
            this.jsonPrint(response, JSONUtil.rowJson(portlet.json()));
        }
        return null;
    }
}
