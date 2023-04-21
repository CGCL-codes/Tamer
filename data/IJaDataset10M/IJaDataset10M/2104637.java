package com.cibertec.project.ps.test;

import com.opensymphony.xwork2.ActionSupport;

public class ActionTest extends ActionSupport {

    @Override
    public String execute() throws Exception {
        System.out.println("Hola Mundo ...");
        return SUCCESS;
    }
}
