package org.brainypdm.modules.web.servlet;

import java.lang.reflect.InvocationTargetException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.context.Context;
import org.brainypdm.constants.ErrorCodes;
import org.brainypdm.exceptions.BaseException;
import org.brainypdm.modules.commons.log.BrainyLogger;
import org.brainypdm.modules.web.WebConstant;
import org.brainypdm.modules.web.classdef.ActionDef;
import org.brainypdm.modules.web.classdef.ActionRet;
import org.brainypdm.modules.web.classdef.Actions;
import org.brainypdm.modules.web.classdef.WebUser;
import org.brainypdm.modules.web.exceptions.WebModuleException;
import org.brainypdm.modules.web.utility.DateUtility;

/**
 * @author <a href="mailto:thomas@brainypdm.org">Thomas Buffagni</a>
 */
public class ServletBase {

    public static final BrainyLogger logger = new BrainyLogger(ServletBase.class);

    /**
	 * the name of action parameter
	 */
    private static final String ACTION_PARAMETER = "execute";

    /**
	 * cache of action
	 */
    private static Actions actions;

    /**
	 * verify if session is valid
	 * @param request
	 * @return
	 */
    public static boolean isSessionValid(HttpServletRequest request) {
        WebUser user = (WebUser) request.getSession().getAttribute(WebConstant.USER_SESSION_PROPERTY);
        if (user == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
	 * execute action
	 * @param request the request
	 * @param response the response
	 * @param ctx context
	 * @return
	 * @throws Exception
	 */
    public static String executeAction(HttpServletRequest request, HttpServletResponse response, Context ctx) throws IllegalAccessException, InvocationTargetException, BaseException {
        String actionName = request.getParameter(ACTION_PARAMETER);
        if (actionName == null) {
            WebModuleException ex = new WebModuleException(ErrorCodes.CODE_1704, ACTION_PARAMETER);
            logger.error(ex.toString());
            throw ex;
        } else {
            logger.debug("ActionName: " + actionName);
            ActionDef action = getAction().getActionExecute(actionName);
            ActionRet ret = (ActionRet) action.getMethodRef().invoke(action.getInstance(), request, response, ctx, action);
            if (ret == null) {
                throw new WebModuleException(ErrorCodes.CODE_1705, actionName);
            } else {
                if (ret.isVelocityTemplate()) {
                    commonContextSetting(request, response, ctx);
                    return ret.getValue();
                }
                if (ret.isAjaxTemplate()) {
                    return ret.getValue();
                } else {
                    throw new WebModuleException(ErrorCodes.CODE_1706);
                }
            }
        }
    }

    public static Actions getAction() throws BaseException {
        if (actions == null) {
            actions = Actions.getActionfromDefinition();
        }
        return actions;
    }

    /**
	 * the common context setting.
	 * @param request
	 * @param response
	 * @param ctx
	 */
    private static void commonContextSetting(HttpServletRequest request, HttpServletResponse response, Context ctx) throws BaseException {
        ctx.put("toDay", DateUtility.toDay());
        ctx.put("user", request.getSession().getAttribute(WebConstant.USER_SESSION_PROPERTY));
    }
}
