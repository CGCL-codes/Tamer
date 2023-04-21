package pl.freetimetool.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(final Locale locale, final Model model) {
        LoginController.logger.info("Welcome home! the client locale is " + locale.toString());
        final Date date = new Date();
        final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
        final String formattedDate = dateFormat.format(date);
        model.addAttribute("serverTime", formattedDate);
        return "greetingPage";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false, defaultValue = "false") boolean error) {
        ModelAndView mav = new ModelAndView("login/login");
        mav.addObject("isError", error);
        return mav;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/j_spring_security_logout");
        return mav;
    }
}
