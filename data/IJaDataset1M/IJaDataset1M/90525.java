package example.chapter7;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * LogoutController.
 * 
 * @author Xuefeng
 * 
 * @spring.bean name="/logout.do"
 */
public class LogoutController extends AbstractController {

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getSession().removeAttribute("USERNAME");
        return new ModelAndView("redirect:login.do");
    }
}
