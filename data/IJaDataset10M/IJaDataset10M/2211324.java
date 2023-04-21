package idv.nightpig.lab02.web.action;

import idv.nightpig.lab02.domain.User;
import idv.nightpig.lab02.service.Lab02Service;
import java.util.Collection;
import java.util.Date;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Component
@Scope("prototype")
public class UserAction extends ActionSupport {

    private static final long serialVersionUID = 1L;

    @Autowired
    private Lab02Service lab02Service;

    private User user;

    private Collection<User> users;

    @Action(value = "user-destroy", results = { @Result(name = "success", type = "redirectAction", location = "user") })
    public String destroy() {
        if (user != null && user.getId() != null) {
            lab02Service.removeUser(user);
        }
        return "success";
    }

    @Action(value = "user-edit", results = { @Result(name = "success", location = "/WEB-INF/content/user.jsp") })
    public String edit() {
        if (user != null && user.getId() != null) {
            user = lab02Service.findUserById(user.getId());
        }
        return "success";
    }

    public User getUser() {
        return user;
    }

    public Collection<User> getUsers() {
        return users;
    }

    @Action("user")
    public String index() {
        users = lab02Service.findAllUser();
        return "success";
    }

    @Action(value = "user-save", results = { @Result(name = "success", type = "redirectAction", location = "user"), @Result(name = "input", type = "redirectAction", location = "user") })
    @Validations(requiredStrings = { @RequiredStringValidator(message = "required", fieldName = "user.account", type = ValidatorType.SIMPLE, trim = true), @RequiredStringValidator(message = "required", fieldName = "user.email", type = ValidatorType.SIMPLE, trim = true), @RequiredStringValidator(message = "required", fieldName = "user.password", type = ValidatorType.SIMPLE, trim = true) })
    public String save() {
        if (user != null && user.getId() != null) {
            User u = lab02Service.findUserById(user.getId());
            u.setAccount(user.getAccount());
            u.setEmail(user.getEmail());
            u.setPassword(user.getPassword());
            u.setUpdateTime(new Date());
            lab02Service.persistUser(u);
        } else {
            user.setCreateTime(new Date());
            user.setUpdateTime(user.getCreateTime());
            lab02Service.persistUser(user);
        }
        return "success";
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }
}
