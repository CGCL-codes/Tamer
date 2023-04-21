package com.kwoksys.action.issueplugin;

import com.kwoksys.action.base.BaseAction;
import com.kwoksys.action.common.template.HeaderGenericTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.AttributeSearch;
import com.kwoksys.framework.connection.database.QueryBits;
import com.kwoksys.framework.system.Localizer;
import com.kwoksys.framework.system.RequestContext;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Issue legend details
 */
public class IssueLegendDetailAction extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestContext requestContext = new RequestContext(request);
        AttributeSearch attributeSearch = new AttributeSearch();
        attributeSearch.put("attributeIdEquals", request.getParameter("attributeId"));
        attributeSearch.put("attrFieldEnabled", true);
        attributeSearch.put("isEditable", true);
        QueryBits query = new QueryBits(attributeSearch);
        query.setOrderByColumn("attribute_field_name");
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setTemplatePath(mapping, SUCCESS);
        AdminService adminService = ServiceProvider.getAdminService();
        request.setAttribute("attributeFieldList", adminService.getEditAttributeFields(query));
        HeaderGenericTemplate header = new HeaderGenericTemplate();
        standardTemplate.addTemplate(header);
        header.setTitleText(Localizer.getText(requestContext, "issuePlugin.issueAdd.legendTitle"));
        header.setHeaderText(Localizer.getText(requestContext, "issuePlugin.issueAdd.legendTitle"));
        return standardTemplate.findForward(SUCCESS);
    }
}
