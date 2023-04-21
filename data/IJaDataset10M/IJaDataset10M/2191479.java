package com.demo.service;

public interface LoginService {

    public Boolean doStuLogin(String username, String password);

    public Boolean doTeaLogin(String teaName, String teaPassword);

    public Boolean doAdmLogin(String adminName, String adminPassword);
}
