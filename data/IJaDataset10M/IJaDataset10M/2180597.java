package com.blogspot.ostas.app.action;

import com.blogspot.ostas.app.model.User;
import com.blogspot.ostas.app.service.UserDao;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.apache.log4j.Logger;

@UrlBinding("/edit.htm")
public class UserEditActionBean extends BaseActionBean {

    private static Logger logger = Logger.getLogger(UserEditActionBean.class);

    @SpringBean
    private UserDao userDaoJpa;

    private User user;

    public final User getUser() {
        return user;
    }

    private int userId;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @DefaultHandler
    public Resolution view() {
        user = userDaoJpa.getUserById(userId);
        logger.debug("User for showing in edit form : " + user);
        return new ForwardResolution("/WEB-INF/jsp/edit.jsp");
    }

    public Resolution save() {
        logger.debug("User for edit : " + user);
        userDaoJpa.updateUser(user);
        return new RedirectResolution("/list.htm");
    }
}
