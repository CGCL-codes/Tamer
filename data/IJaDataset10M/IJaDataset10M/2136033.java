package com.thyrsus.project.controller.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import com.thyrsus.project.form.user.UserForm;
import com.thyrsus.project.helper.LocaleHelper;
import com.thyrsus.project.helper.URLHelper;
import com.thyrsus.project.helper.UserHelper;
import com.thyrsus.project.object.UserContext;
import com.thyrsus.project.service.user.UserService;

@Controller
@SessionAttributes
public class EditUserController {

    static Logger LOGGER = Logger.getLogger(EditUserController.class);

    @Autowired
    UserService userService;

    /**
	 * This function fill the form and redirect to the edit page
	 * @param userId
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
    @RequestMapping("/user/edit")
    public ModelAndView doEdit(HttpServletRequest request, HttpServletResponse response) {
        if (!UserHelper.isConnected(request)) {
            return new ModelAndView(URLHelper.getURL(UserHelper.getLoginURL(request, response, "/user/edit"), request), "userForm", new UserForm());
        } else {
            ModelAndView mav = new ModelAndView(URLHelper.getURL("/user/edit", request));
            UserContext userContext = null;
            try {
                userContext = userService.getUserContextById(UserHelper.getUser(request).getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            UserForm userForm = new UserForm(userContext);
            mav.addObject("userForm", userForm);
            return mav;
        }
    }

    /**
	 * This function save the new value into the user object
	 * @param userForm
	 * @param result
	 * @param request
	 * @param response
	 * @return
	 */
    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    public ModelAndView doNew(@ModelAttribute("userForm") UserForm userForm, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        if (!UserHelper.isConnected(request)) {
            return new ModelAndView(URLHelper.getURL(UserHelper.getLoginURL(request, response, "/user/edit"), request), "userForm", new UserForm());
        } else {
            ModelAndView mav = new ModelAndView(URLHelper.getURL("/user/edit", request));
            UserContext userContext = null;
            try {
                userContext = userService.getUserContextById(UserHelper.getUser(request).getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            LocaleHelper.setLocale(userForm.getLocale(), request, response);
            userForm = new UserForm(userContext);
            mav.addObject("userForm", userForm);
            return mav;
        }
    }
}
