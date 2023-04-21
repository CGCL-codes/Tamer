package ru.newton.pokertrainer.web.controllers;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.newton.pokertrainer.entities.users.Role;
import ru.newton.pokertrainer.entities.users.User;
import ru.newton.pokertrainer.services.UserService;
import ru.newton.pokertrainer.utils.Utils;
import ru.newton.pokertrainer.utils.Utils.RunnableWithParam;
import ru.newton.pokertrainer.web.constants.ViewConstants;
import ru.newton.pokertrainer.web.forms.MainForm;
import ru.newton.pokertrainer.web.gwt.pokertrainermodule.shared.constants.RequestConstants;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author echo
 */
@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @RequestMapping("/{" + RequestConstants.FORM_URL + "}" + RequestConstants.FORM_POSTFIX)
    public ModelAndView main(@PathVariable(RequestConstants.FORM_URL) String form, final HttpServletRequest request) {
        MainForm mainForm = new MainForm();
        mainForm.setRequest(form);
        String userName = Utils.getCurrentUserName(request);
        mainForm.setUserName(userName);
        String userRoleIds = StringUtils.EMPTY;
        if (userName != null) {
            User user = this.userService.getUserByLogin(userName);
            if (user != null) {
                final Collection<Long> ids = new ArrayList<Long>();
                Utils.run(user.getRoles(), new RunnableWithParam<Role>() {

                    @Override
                    public void run() {
                        ids.add(this.getObject().getId());
                    }
                });
                userRoleIds = StringUtils.join(ids.iterator(), ",");
            }
        }
        mainForm.setUserRoleIds(userRoleIds);
        ModelAndView view = new ModelAndView(ViewConstants.MAIN_VIEW_NAME, ViewConstants.MAIN_FORM_BEAN_NAME, mainForm);
        return view;
    }
}
