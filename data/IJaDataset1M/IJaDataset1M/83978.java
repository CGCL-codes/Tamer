package cz.softinel.kapr.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import cz.softinel.uaf.spring.web.controller.CommonDispatchController;

/**
 * @author Radek Pinc
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PersonController extends CommonDispatchController {

    public ModelAndView storeRegistration(HttpServletRequest request, HttpServletResponse response) {
        return createModelAndView(getSuccessView());
    }
}
