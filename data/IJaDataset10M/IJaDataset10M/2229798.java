package br.com.wepa.webapps.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebControl {

    public static final String SESSION_SCOPE = SecurityConstants.SESSION_SCOPE;

    public static final String REQUEST_SCOPE = SecurityConstants.REQUEST_SCOPE;

    public static final String APPLICATION_SCOPE = SecurityConstants.APPLICATION_SCOPE;

    private final ThreadLocal<HttpServletRequest> threadRequest = new ThreadLocal<HttpServletRequest>();

    private final ThreadLocal<HttpServletResponse> threadResponse = new ThreadLocal<HttpServletResponse>();

    public HttpServletRequest getCurrentRequest() {
        HttpServletRequest request = (HttpServletRequest) threadRequest.get();
        testNull(request, "The local request could not be null, setCurrents(request,response) before");
        return request;
    }

    protected void setCurrentRequest(HttpServletRequest request) {
        threadRequest.set(request);
    }

    public HttpServletResponse getCurrentResponse() {
        HttpServletResponse response = threadResponse.get();
        testNull(response, "The local response thread could not be null, setCurrents(request,response) before");
        return response;
    }

    protected void setCurrentResponse(HttpServletResponse response) {
        threadResponse.set(response);
    }

    public void setCurrents(HttpServletRequest request, HttpServletResponse response) {
        setCurrentRequest(request);
        setCurrentResponse(response);
    }

    private void testNull(Object obj, String msg) {
        if (obj == null) throw new NullPointerException(msg);
    }

    /**
	 * Sets the attribute in the context specified
	 * 
	 * @param attributeName
	 * @param value
	 * @param request
	 * @param scope
	 */
    public void setInScope(String attributeName, Object value, String scope) {
        setInScope(attributeName, value, getCurrentRequest(), scope);
    }

    /**
	 * Gets the attribute in the context specified, null otherwise
	 * 
	 * @param attributeName
	 * @param request
	 * @param scope
	 * @return
	 */
    public Object getInScope(String attributeName, String scope) {
        return getInScope(attributeName, getCurrentRequest(), scope);
    }

    /**
	 * Sets the attribute in the context specified
	 * 
	 * @param attributeName
	 * @param value
	 * @param request
	 * @param scope
	 */
    public static void setInScope(String attributeName, Object value, HttpServletRequest request, String scope) {
        if (scope.equalsIgnoreCase(REQUEST_SCOPE)) {
            request.setAttribute(attributeName, value);
        } else if (scope.equalsIgnoreCase(SESSION_SCOPE)) {
            request.getSession().setAttribute(attributeName, value);
        } else if (scope.equalsIgnoreCase(APPLICATION_SCOPE)) {
            request.getSession().getServletContext().setAttribute(attributeName, value);
        }
    }

    /**
	 * Gets the attribute in the context specified, null otherwise
	 * @param attributeName
	 * @param request
	 * @param scope
	 * @return
	 */
    public static Object getInScope(String attributeName, HttpServletRequest request, String scope) {
        if (scope.equalsIgnoreCase(REQUEST_SCOPE)) {
            return request.getAttribute(attributeName);
        } else if (scope.equalsIgnoreCase(SESSION_SCOPE)) {
            return request.getSession().getAttribute(attributeName);
        } else if (scope.equalsIgnoreCase(APPLICATION_SCOPE)) {
            return request.getSession().getServletContext().getAttribute(attributeName);
        }
        return null;
    }
}
