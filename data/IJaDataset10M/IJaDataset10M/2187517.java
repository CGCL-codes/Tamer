package com.j2biz.blogunity.web.actions.unsecure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.j2biz.blogunity.IConstants;
import com.j2biz.blogunity.exception.BlogunityException;
import com.j2biz.blogunity.web.ActionResultFactory;
import com.j2biz.blogunity.web.IActionResult;
import com.j2biz.blogunity.web.actions.AbstractAction;

/**
 * @author michelson
 * @version $$
 * @since 0.1
 * 
 *  
 */
public class LogoutAction extends AbstractAction {

    private static final IActionResult LOGOUT_SUCCESS_FORWARD = ActionResultFactory.buildForward("/jsp/unsecure/logout.jsp");

    public IActionResult execute(HttpServletRequest request, HttpServletResponse response) throws BlogunityException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(IConstants.Session.USER);
        }
        return LOGOUT_SUCCESS_FORWARD;
    }
}
