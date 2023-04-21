package eu.future.earth.web.html;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * <p>
 * Title: Future Earth Web Library
 * </p>
 * <p>
 * Description: This class hold the Util method for working in html.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Future Earth
 * </p>
 * 
 * @author Marteijn Nouwens
 */
public class HtmlUtils {

    public static final String AND = "&amp;";

    public HtmlUtils() {
        super();
    }

    /**
	 * This method return's the context form the page context.
	 * 
	 * @param context
	 *            PageContext - The Page Context.
	 * @return String - The Context null is not a HttpServletRequest.
	 */
    public static String getContext(PageContext context) {
        if (context.getRequest() instanceof HttpServletRequest) {
            return getContext((HttpServletRequest) context.getRequest());
        }
        return null;
    }

    /**
	 * Get's the Context from a HttpServlet Request.
	 * 
	 * @param request
	 *            HttpServletRequest - The Request.
	 * @return String - The context;
	 */
    public static String getContext(HttpServletRequest request) {
        String result = request.getContextPath();
        if (result.endsWith("/")) {
            result = result.substring(0, result.length());
        }
        return result;
    }
}
