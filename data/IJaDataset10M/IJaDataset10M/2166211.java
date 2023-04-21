package com.iclotho.eshop.web.merchant.ctrl;

import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.iclotho.eshop.AppContext;
import com.iclotho.eshop.util.AppConstants;
import com.iclotho.eshop.web.base.ctrl.MerchantForm;
import com.iclotho.eshop.web.base.vo.MerchantVO;
import com.iclotho.eshop.web.base.vo.OperatorVO;
import com.iclotho.eshop.web.merchant.MerchantService;
import com.iclotho.foundation.pub.exception.AppException;
import com.iclotho.foundation.pub.util.BeanUtil;

public class MerchantUpdateAction extends MerchantBaseAction {

    Logger logger = Logger.getLogger(MerchantUpdateAction.class);

    public ActionForward workon(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        MerchantForm merchantForm = (MerchantForm) actionForm;
        HttpSession session = request.getSession();
        String lang = (String) session.getAttribute(AppContext.LANGUAGE);
        logger.debug("begin to load merchant");
        try {
            OperatorVO operatorVO = (OperatorVO) session.getAttribute(AppConstants.SESSION_MERCHANT_OPERATOR);
            String merchantId = AppContext.ICLOTHO_MERCHANT_ID;
            if (operatorVO != null) {
                merchantId = operatorVO.getMerchantId();
            }
            MerchantVO merchantVO = new MerchantVO();
            BeanUtil.copyProperties(merchantVO, merchantForm);
            MerchantService.updateMerchant(merchantVO);
            return actionMapping.findForward("success");
        } catch (AppException appe) {
            return this.gotoError(appe.getCode(), appe.getMessage(), lang, request);
        } catch (SQLException sqle) {
            return this.gotoError(AppContext.MSG_CODE_SQLE, sqle.getMessage(), lang, request);
        }
    }
}
