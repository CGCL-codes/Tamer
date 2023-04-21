package org.myjerry.web.taglibs.gae;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class UserTag extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException, IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (user != null) {
            JspWriter out = getJspContext().getOut();
            out.write(user.getNickname());
        }
    }
}
