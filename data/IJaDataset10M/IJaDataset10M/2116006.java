package com.iclotho.eshop.web.merchant.ctrl;

import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.iclotho.eshop.AppContext;
import com.iclotho.eshop.util.AppConstants;
import com.iclotho.eshop.web.base.ctrl.ProductCategoryForm;
import com.iclotho.eshop.web.base.vo.MerchantVO;
import com.iclotho.eshop.web.base.vo.OperatorVO;
import com.iclotho.eshop.web.base.vo.ProductCategoryVO;
import com.iclotho.eshop.web.merchant.MerchantService;
import com.iclotho.foundation.pub.exception.AppException;

public class MerchantNewsPreAddAction extends MerchantBaseAction {

    Logger logger = Logger.getLogger(MerchantNewsPreAddAction.class);

    public ActionForward workon(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        logger.debug("begin to load newscategory info...");
        try {
            HttpSession session = request.getSession();
            OperatorVO operatorVO = (OperatorVO) session.getAttribute(AppConstants.SESSION_MERCHANT_OPERATOR);
            String merchantId = operatorVO.getMerchantId();
            List newsCategoryList = MerchantService.getNewsCategoryAvailable(merchantId);
            request.setAttribute("newsCategoryList", newsCategoryList);
            return actionMapping.findForward("success");
        } catch (AppException appe) {
            logger.error(appe.getMessage());
        } catch (Exception sqle) {
            logger.error(sqle.getMessage());
        }
        return actionMapping.findForward("success");
    }
}
