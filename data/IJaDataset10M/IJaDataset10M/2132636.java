package com.struts2valestack.test;

import com.opensymphony.xwork2.ActionSupport;
import com.sun.net.httpserver.Authenticator.Success;

public class TestAction extends ActionSupport {

    @Override
    public String execute() throws Exception {
        System.out.println("do " + this.getClass().getName());
        return SUCCESS;
    }
}
