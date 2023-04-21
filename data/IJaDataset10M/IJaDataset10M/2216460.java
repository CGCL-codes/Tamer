package org.elucida.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Elucida Software
 * @version 1.0
 * 
 */
public interface RequestHandler {

    /**
     * @param request
     * @return Result
     * @throws Exception
     */
    public Result execute();

    /**
     * @param httpSession
     */
    public void setHttpSession(HttpSession httpSession);

    public void setMapping(RequestMapping requestMapping);

    public void setRequest(HttpServletRequest request);
}
