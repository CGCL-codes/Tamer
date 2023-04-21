package org.ru.mse10.cvis.web.bean.action;

import java.util.ArrayList;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import org.ru.mse10.cvis.constants.UserRole;
import org.ru.mse10.cvis.entity.cv.User;
import org.ru.mse10.cvis.service.MailService;
import org.ru.mse10.cvis.service.UserService;
import org.ru.mse10.cvis.util.CommonUtil;
import org.ru.mse10.cvis.web.bean.BaseFormBean;

@SessionScoped
@ManagedBean(name = "userAction")
public class UserAction extends BaseFormBean<User, UserService> {

    @EJB
    private UserService serviceUsers;

    @EJB
    private MailService mailService;

    private User user;

    private UserService service;

    private String init(User user) {
        setTargetEntity(user);
        return null;
    }

    public String createUserAction() {
        init(new User());
        setReadonly(false);
        return "userDetails";
    }

    public void save() {
        String password = CommonUtil.generatePassword();
        getTargetEntity().setPassword(CommonUtil.hashPassword(password));
        setTargetEntity(serviceUsers.save(getTargetEntity()));
        setReadonly(true);
        mailService.sendNewUserMail(password, getTargetEntity().getEmail());
    }

    public String open(User user) {
        init(user);
        setReadonly(true);
        return "userDetails";
    }

    public ArrayList<SelectItem> getRoleValues() {
        ArrayList<SelectItem> items = new ArrayList<SelectItem>(UserRole.values().length);
        for (UserRole g : UserRole.values()) {
            items.add(new SelectItem(g, g.getLabel()));
        }
        return items;
    }

    public String actionCancel() {
        if (user.getId() != null) {
            service.refresh(user);
            return init(user);
        }
        return "home";
    }
}
