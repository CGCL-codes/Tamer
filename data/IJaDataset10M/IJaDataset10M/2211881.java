package edu.nus.iss.ejava.team4.controllers;

import com.opensymphony.xwork2.ActionSupport;

public class HomeAction extends ActionSupport {

    public String showHome() {
        System.out.println("====>Inside Home Action");
        return SUCCESS;
    }
}
