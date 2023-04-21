package com.jlz.actions.register;

import org.apache.struts2.convention.annotation.Standard;
import org.springframework.beans.factory.annotation.Autowired;
import com.jlz.beans.user.LinkBean;
import com.jlz.beans.user.UserBean;
import com.jlz.managers.user.Links;
import com.jlz.managers.user.Users;
import com.julewa.Client;

@Standard(rest = "usercode")
public class ActiveAction {

    @Autowired
    Links links;

    @Autowired
    Users users;

    @Autowired
    Client client;

    String usercode;

    public String execute() {
        if (usercode == null) return "global_home";
        LinkBean link = links.decode(usercode);
        if (link == null) return "global_home";
        link.setState(Links.State.USING.getValue());
        links.update(link);
        UserBean user = users.get(link.getUserId());
        client.setUser(user);
        return "active";
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }
}
