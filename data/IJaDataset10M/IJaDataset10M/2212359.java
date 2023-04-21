package com.kwoksys.action.kb;

import com.kwoksys.action.base.BaseAction;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.kb.KbService;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.system.AppPaths;
import com.kwoksys.framework.system.RequestContext;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action class for updating KB category.
 */
public class CategoryEdit2Action extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestContext requestContext = new RequestContext(request);
        CategoryForm actionForm = (CategoryForm) form;
        Integer categoryId = actionForm.getCategoryId();
        KbService kbService = ServiceProvider.getKbService();
        Category category = kbService.getCategory(categoryId);
        category.setName(actionForm.getCategoryName().trim());
        category.setModifierId(requestContext.getUser().getId());
        ActionMessages errors = kbService.updateCategory(category);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("error");
        } else {
            return redirect(AppPaths.KB_CATEGORY_LIST);
        }
    }
}
