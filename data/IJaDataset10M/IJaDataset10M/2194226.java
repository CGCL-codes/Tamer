package com.iclotho.eshop.web.home.ctrl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.iclotho.eshop.web.HomeGenericAction;
import com.iclotho.eshop.web.base.ctrl.NewsForm;
import com.iclotho.eshop.web.base.vo.NewsVO;
import com.iclotho.eshop.web.home.HomeService;
import com.iclotho.foundation.pagination.PaginationContext;
import com.iclotho.foundation.pub.exception.AppException;

public class NewsMoreAction extends HomeGenericAction {

    Logger logger = Logger.getLogger(NewsMoreAction.class);

    public ActionForward workon(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        NewsForm newsForm = (NewsForm) actionForm;
        int currentPage = newsForm.getPage();
        if (currentPage == 0) currentPage = 1;
        try {
            NewsVO newsVO = null;
            PaginationContext paginationContext = new PaginationContext();
            paginationContext.setTotalRecords(HomeService.getAvailableNewsQuantityByCategory(newsForm.getCategoryId()));
            paginationContext.setCurrentPage(currentPage);
            paginationContext = HomeService.getNewsAvailableForPagination(newsForm.getCategoryId(), paginationContext);
            request.setAttribute("paginationContext", paginationContext);
            request.setAttribute("categoryId", newsForm.getCategoryId());
            return actionMapping.findForward("success");
        } catch (AppException appe) {
            return this.gotoError(appe.getCode(), appe.getMessage(), this.lang, request);
        }
    }
}
