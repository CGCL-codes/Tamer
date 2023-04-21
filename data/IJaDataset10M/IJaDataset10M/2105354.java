package org.finneyfill.ajax;

import java.util.ArrayList;
import java.util.List;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class JSONStoreCredentials extends ActionSupport {

    private String username;

    private String password;

    private List usernames = new ArrayList();

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        System.out.println("setData called with data: " + data);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List getUsernames() {
        return usernames;
    }

    public String execute() {
        usernames.add("user1" + data);
        usernames.add("user2 says jQuery sent me **" + data + "**");
        return Action.SUCCESS;
    }
}
