package com.google.code.facebookwebapp.controller.fbml;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.google.code.facebookwebapp.util.FacebookConstants;

/**
 * @author Cesar Arevalo
 * @since 0.1
 */
@Controller("fbmlWelcomeController")
@RequestMapping("/canvas/fbml/welcome")
public class WelcomeController {

    @RequestMapping(method = RequestMethod.POST)
    public String hanlder(Model model) throws Exception {
        model.addAttribute(FacebookConstants.MODEL_WELCOME_SELECTED, true);
        return "canvas.fbml.welcome";
    }
}
