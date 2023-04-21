package com.iclotho.eshop.web.merchant.ctrl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.iclotho.eshop.AppContext;
import com.iclotho.eshop.util.AppConstants;
import com.iclotho.eshop.web.base.vo.OperatorVO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class MerchantBaseAction extends Action {

    protected final Logger log = LogManager.getLogger(this.getClass());

    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        if (AppContext.isSessionMock()) {
            session.setAttribute(AppConstants.LANGUAGE, AppConstants.LANG_ZH);
            session.setAttribute(AppConstants.SESSION_MERCHANT_OPERATOR, AppContext.getMockMerchantOperatorVO());
        }
        OperatorVO operatorVO = (OperatorVO) session.getAttribute(AppConstants.SESSION_MERCHANT_OPERATOR);
        if (operatorVO == null) {
            return this.gotoLoginPage();
        }
        return this.workon(actionMapping, actionForm, request, response);
    }

    public abstract ActionForward workon(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response);

    protected ActionForward gotoError(String code, String errMsg, String lang, HttpServletRequest request) {
        String msg = AppContext.getMessage(code, lang);
        if (msg == null) {
            msg = errMsg;
        }
        request.setAttribute(AppContext.COMMON_MSG, msg);
        return new ActionForward("/error.jsp");
    }

    public ActionForward gotoSuccess(String code, String lang, String returnPath, HttpServletRequest request) {
        String msg = AppContext.getMessage(code, lang);
        request.setAttribute(AppContext.COMMON_MSG, msg);
        request.setAttribute("returnPath", returnPath);
        return new ActionForward("/success.jsp");
    }

    public ActionForward gotoLoginPage() {
        return new ActionForward("/login.jsp");
    }
}
