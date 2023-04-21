package org.telscenter.sail.webapp.presentation.web.controllers.admin;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.sail.webapp.domain.User;
import net.sf.sail.webapp.service.UserService;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.telscenter.sail.webapp.domain.admin.StudentFields;
import org.telscenter.sail.webapp.domain.authentication.Gender;
import org.telscenter.sail.webapp.domain.impl.LookupParameters;

/**
 * @author patrick lawler
 *
 */
public class LookupStudentController extends SimpleFormController {

    private static final String VIEW = "admin/manageusers";

    private UserService userService;

    @Override
    protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("fields", StudentFields.values());
        return model;
    }

    /**
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object,
     *      org.springframework.validation.BindException)
     */
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) {
        LookupParameters params = (LookupParameters) command;
        Object term = new Object();
        if (params.getLookupField().equals("GENDER")) {
            term = Gender.valueOf(params.getLookupData().toUpperCase());
        } else if (params.getLookupCriteria().equals("like")) {
            term = "%" + params.getLookupData() + "%";
        } else {
            term = params.getLookupData();
        }
        List<User> users = this.userService.retrieveByField(params.getLookupField().toLowerCase(), params.getLookupCriteria(), term, "studentUserDetails");
        ModelAndView modelAndView = new ModelAndView(VIEW);
        List<String> usernames = new ArrayList<String>();
        for (User user : users) {
            usernames.add(user.getUserDetails().getUsername());
        }
        if (users.size() < 1) {
            modelAndView.addObject("message", "No users given search criteria found.");
        } else {
            modelAndView.addObject("students", usernames);
        }
        return modelAndView;
    }

    /**
	 * @param userService the userService to set
	 */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
