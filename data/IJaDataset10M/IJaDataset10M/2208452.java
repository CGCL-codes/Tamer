package se.infact.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import se.infact.domain.Language;
import se.infact.domain.User;
import se.infact.manager.InfactManager;
import se.infact.manager.UserManager;

@Controller
public class LanguageController extends ControllerUtil {

    private InfactManager infactManager;

    private UserManager userManager;

    @ModelAttribute("user")
    public User formBackingObject() {
        return userManager.getAuthenticatedUser();
    }

    @RequestMapping(value = "/language.html", method = RequestMethod.GET)
    public ModelAndView getLanguageView() {
        return getModelAndView();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute("user") User user, @RequestParam long languageId) throws IOException {
        Language language = infactManager.loadLanguage(languageId);
        user.setLanguage(language);
        userManager.saveUser(user);
        return getModelAndView();
    }

    private ModelAndView getModelAndView() {
        ModelAndView modelAndView = getModelAndView("language", "language");
        modelAndView.addObject("languages", infactManager.listLanguages());
        return modelAndView;
    }

    @Autowired
    public void setInfactManager(InfactManager infactManager) {
        this.infactManager = infactManager;
    }

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
