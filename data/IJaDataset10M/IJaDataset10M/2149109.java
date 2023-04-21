package com.germinus.portlet.repository_admin.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.germinus.xpression.cms.categories.CategoriesDefinition;
import com.germinus.xpression.cms.categories.CategoryConfig;
import com.germinus.xpression.cms.categories.CategoryManager;
import com.germinus.xpression.cms.util.ManagerRegistry;
import com.liferay.portal.struts.PortletAction;

public class CreateTestCategoriesAction extends PortletAction {

    public void processAction(ActionMapping mapping, ActionForm form, PortletConfig config, ActionRequest req, ActionResponse res) throws Exception {
        CategoriesDefinition categoriesDefinition = CategoryConfig.getDefinitions();
        CategoryManager categoryManager = ManagerRegistry.getCategoryManager();
        categoryManager.addCategoriesFromDefinitions(categoriesDefinition);
    }

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        return mapping.findForward("success");
    }
}
