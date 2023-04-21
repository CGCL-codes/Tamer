package org.openi.web.controller;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Uddhab Pant <br>
 * @version $Revision: 1.3 $ $Date: 2006/04/12 00:39:12 $ <br>
 *
 * Controller for user guide page.
 *
 */
public class UserGuideController extends AbstractController {

    private static Logger logger = Logger.getLogger(UserGuideController.class);

    /**
     * Process the request and return a ModelAndView object which the DispatcherServlet will render.
     *
     * @param httpServletRequest HttpServletRequest
     * @param httpServletResponse HttpServletResponse
     * @return ModelAndView
     * @throws Exception
     */
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String view = "userGuideView";
        try {
            return new ModelAndView(view);
        } catch (Exception e) {
            logger.error("Exception:", e);
            throw e;
        }
    }
}
