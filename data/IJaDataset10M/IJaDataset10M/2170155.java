package controllers;

import org.appleframework.web.ActionSupper;

/**
 *
 * @author zhujg
 */
public class UsersController extends ActionSupper {

    public static void list() {
        render("index.html");
    }

    public static void login() {
        System.out.println("host:" + request.host);
        System.out.println("username:" + params.get("username"));
        System.out.println("password:" + params.get("password"));
        render("index.html");
    }
}
