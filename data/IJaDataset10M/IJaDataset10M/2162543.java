package com.spring.workflow.login;

import com.spring.workflow.WorkflowConstants;
import com.spring.workflow.util.WorkflowHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.HashMap;

public class AbstractAuthenticate extends MultiActionController {

    private static final Pattern REQUEST_PARAMETER_SPLITTER = Pattern.compile("&");

    private static final Pattern KEY_VALUE_SPLITTER = Pattern.compile("=");

    /**
	 * This method will set the user object in the session. After the user has been logged in, the workflow will be
	 * redirected to the original page that was first requested (before the login pageflow took over). If there was no
	 * original page, then an empty ModelAndView will be returned.
	 *
	 * @param request
	 * @param user
	 * @return
	 * @throws ServletException
	 */
    public ModelAndView makeSessionUser(HttpServletRequest request, ISpringWorkflowUser user) throws ServletException {
        if (user != null) {
            request.getSession().setAttribute(WorkflowConstants.USER_INFO, user);
            if (request.getAttribute(WorkflowConstants.ORIGINAL_REQUEST) != null) {
                String[] requestParameters = REQUEST_PARAMETER_SPLITTER.split((String) request.getAttribute(WorkflowConstants.ORIGINAL_REQUEST));
                request.setAttribute(WorkflowConstants.ORIGINAL_REQUEST, null);
                WorkflowHttpServletRequest newRequest = new WorkflowHttpServletRequest(request);
                newRequest.clearParameters();
                for (int i = 0; i < requestParameters.length; i++) {
                    String keyValue = requestParameters[i];
                    String[] keyValues = KEY_VALUE_SPLITTER.split(keyValue);
                    if (keyValues.length > 1) {
                        if (keyValues[0].equals("__action__") && keyValues[1].equals("null")) {
                            continue;
                        }
                        newRequest.addParameter(keyValues[0], keyValues[1]);
                    }
                }
                return new ModelAndView("forward:", WorkflowConstants.ORIGINAL_REQUEST, newRequest);
            }
        } else {
            Map errors = new HashMap();
            errors.put(WorkflowConstants.MESSAGE_NAME, "error.credentials.not.allowed");
            return new ModelAndView("forward:", errors);
        }
        return new ModelAndView("");
    }

    /**
	 * This method will remove the user object from the session and thus ensuring the logging out of the user.
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 */
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        request.getSession().removeAttribute(WorkflowConstants.USER_INFO);
        request.getSession().invalidate();
        return new ModelAndView("");
    }

    /**
	 * This method will return the logged in user from the session.
	 *
	 * @param request
	 * @return
	 */
    public static IUser getUser(HttpServletRequest request) {
        return (IUser) request.getSession().getAttribute(WorkflowConstants.USER_INFO);
    }
}
