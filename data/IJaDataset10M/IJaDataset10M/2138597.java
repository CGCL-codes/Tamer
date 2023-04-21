package com.kwoksys.action.contacts;

import com.kwoksys.action.base.BaseAction;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.framework.system.AppPaths;
import com.kwoksys.framework.system.ObjectTypes;
import com.kwoksys.framework.system.RequestContext;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Action class for editing company.
 */
public class CompanyEdit2Action extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestContext requestContext = new RequestContext(request);
        AccessUser user = requestContext.getUser();
        Company company = new Company();
        CompanyForm actionForm = (CompanyForm) form;
        company.setId(actionForm.getCompanyId());
        company.setName(actionForm.getCompanyName().trim());
        company.setDescription(actionForm.getCompanyDescription().trim());
        company.setTags(actionForm.getCompanyTags().trim());
        company.setTypeIds(actionForm.getCompanyTypes());
        company.setModifierId(user.getId());
        Map<Integer, Attribute> customAttributes = AdminUtils.getCustomFieldMap(ObjectTypes.COMPANY);
        AdminUtils.populateCustomFieldValues(request, company, customAttributes);
        ContactService contactService = ServiceProvider.getContactService();
        ActionMessages errors = contactService.updateCompany(requestContext, company, customAttributes);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("error");
        } else {
            return redirect(AppPaths.CONTACTS_COMPANY_DETAIL + "?companyId=" + company.getId());
        }
    }
}
