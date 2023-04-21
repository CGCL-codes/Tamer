package com.sks.web.action.house;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.springframework.stereotype.Controller;
import com.sks.bean.PageView;
import com.sks.bean.QueryResult;
import com.sks.bean.pojo.Metro;
import com.sks.bean.pojo.MetroStation;
import com.sks.service.house.MetroService;
import com.sks.service.house.MetroStationService;
import com.sks.utils.SiteUrl;
import com.sks.utils.StringUtil;
import com.sks.web.action.BasicAction;
import com.sks.web.action.privilege.Permission;
import com.sks.web.formbean.house.MetroStationForm;

@Controller("/cms/house/metroStation")
public class MetroStationAction extends BasicAction {

    @Resource(name = "metroServiceBean")
    private MetroService metroService;

    @Resource(name = "metroStationServiceBean")
    private MetroStationService service;

    @Permission(module = "info", privilege = "view")
    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        MetroStationForm formbean = (MetroStationForm) form;
        StringBuffer jpql = new StringBuffer("");
        List<Object> params = new ArrayList<Object>();
        if ("true".equals(formbean.getQuery())) {
            if (formbean.getName() != null && !"".equals(formbean.getName().trim())) {
                params.add("%" + formbean.getName() + "%");
                jpql.append(" o.name like ?" + params.size());
            }
        }
        PageView<MetroStation> pageView = new PageView<MetroStation>(12, formbean.getPage());
        LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
        orderby.put("stationId", "asc");
        QueryResult<MetroStation> qr = service.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), jpql.toString(), params.toArray(), orderby);
        pageView.setQueryResult(qr);
        request.setAttribute("pageView", pageView);
        this.setReadOnly(request, "list");
        return mapping.findForward(LIST);
    }

    @Permission(module = "info", privilege = "update")
    public ActionForward addUI(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.saveToken(request);
        getMetros(request);
        MetroStationForm formbean = (MetroStationForm) form;
        formbean.setMethod(ADD);
        return mapping.findForward(EDIT);
    }

    @Permission(module = "info", privilege = "view")
    public ActionForward editUI(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.saveToken(request);
        getMetros(request);
        MetroStationForm formbean = (MetroStationForm) form;
        MetroStation ms = service.find(formbean.getStationId());
        copyProperties(ms, formbean);
        request.setAttribute("isIntersect", formbean.getIsIntersect());
        formbean.setMethod(EDIT);
        this.setReadOnly(request, "editUI");
        return mapping.findForward(EDIT);
    }

    public void beforeadd(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages errors = new ActionMessages();
        MetroStationForm formbean = (MetroStationForm) form;
        if (StringUtil.isEmpty(formbean.getName())) {
            errors.add("name", new ActionMessage("errors.required", getMessage("info.metroStation.field.name")));
        }
        List params = new ArrayList();
        params.add(formbean.getName());
        StringBuffer jpql = new StringBuffer("o.name = ?" + params.size());
        if (EDIT.equals(formbean.getMethod())) {
            params.add(formbean.getStationId());
            jpql.append(" and o.stationId <> ?" + params.size());
        }
        QueryResult<MetroStation> m = service.getScrollData(-1, -1, jpql.toString(), params.toArray());
        if (null != m && m.getResultlist().size() > 0) {
            errors.add("name", new ActionMessage("errors.repeat", getMessage("info.metroStation.field.name")));
            getMetros(request);
            request.setAttribute("isIntersect", formbean.getIsIntersect());
            request.setAttribute(ACTION, formbean.getMethod());
        }
        saveErrors(request, errors);
    }

    @Permission(module = "info", privilege = "update")
    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        MetroStationForm formbean = (MetroStationForm) form;
        MetroStation ms = new MetroStation();
        copyProperties(formbean, ms);
        Metro m = metroService.find(formbean.getMetroId());
        ms.setMetroByMetroId(m);
        if (formbean.getIsIntersect()) {
            Metro m1 = metroService.find(formbean.getMetroId1());
            ms.setMetroByMetroId1(m1);
        }
        ms.setStationId(null);
        service.save(ms);
        request.setAttribute(MESSAGE, getMessage("display.add") + getMessage("info.metroStation") + getMessage("display.success"));
        request.setAttribute("urladdress", SiteUrl.readUrl("cms.house.metroStation"));
        return mapping.findForward(MESSAGE);
    }

    public void beforeedit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.beforeadd(mapping, form, request, response);
    }

    @Permission(module = "info", privilege = "update")
    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        MetroStationForm formbean = (MetroStationForm) form;
        MetroStation ms = service.find(formbean.getStationId());
        copyProperties(formbean, ms);
        Metro m = metroService.find(formbean.getMetroId());
        ms.setMetroByMetroId(m);
        if (formbean.getIsIntersect()) {
            Metro m1 = metroService.find(formbean.getMetroId1());
            ms.setMetroByMetroId1(m1);
        } else {
            ms.setMetroByMetroId1(null);
        }
        service.update(ms);
        request.setAttribute(MESSAGE, getMessage("display.edit") + getMessage("info.metroStation") + getMessage("display.success"));
        request.setAttribute("urladdress", SiteUrl.readUrl("cms.house.metroStation"));
        return mapping.findForward(MESSAGE);
    }

    @Permission(module = "info", privilege = "update")
    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        MetroStationForm formbean = (MetroStationForm) form;
        service.delete(formbean.getStationId());
        request.setAttribute(MESSAGE, getMessage("display.delete") + getMessage("info.metroStation") + getMessage("display.success"));
        request.setAttribute("urladdress", SiteUrl.readUrl("cms.house.metroStation"));
        return mapping.findForward(MESSAGE);
    }

    @Permission(module = "info", privilege = "view")
    public ActionForward queryUI(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward(QUERY);
    }

    private void getMetros(HttpServletRequest request) {
        QueryResult<Metro> metros = metroService.getScrollData(-1, -1);
        request.setAttribute("metros", metros.getResultlist());
    }
}
