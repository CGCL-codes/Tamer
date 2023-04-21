package com.csc.sfm.client.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "welcomeBean", eager = true)
@SessionScoped
public class WelcomeBean {

    public WelcomeBean() {
        System.out.println("WelcomeBean instantiated");
    }

    public String getMessage() {
        return "I'm alive!";
    }
}
