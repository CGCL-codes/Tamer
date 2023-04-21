package pl.pollub.superkino.commons.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SecurityController {

    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
    String login() {
        return "login";
    }

    @RequestMapping(value = "/logout.html", method = RequestMethod.GET)
    String logout() {
        return "logout";
    }
}
