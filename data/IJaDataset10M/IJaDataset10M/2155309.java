package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import controller.exceptions.NonexistentEntityException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import entity.Adminpage;
import entity.AdminpageLogic;

public class HalamanAdminLogic {

    HttpServletRequest request;

    public HalamanAdminLogic(HttpServletRequest request) {
        this.request = request;
    }

    public String loggedIn() {
        HttpSession session = request.getSession();
        Adminpage user = new Adminpage();
        AdminpageLogic ap = new AdminpageLogic();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        user.setUsername(username);
        session.setAttribute("tempLog", user);
        if (username.equals("") || password.equals("")) {
            session.setAttribute("error", "masih ada field kosong");
            return "admin-login.jsp";
        } else {
            boolean hasilCheck = ap.checkUser(username);
            if (hasilCheck) {
                user = null;
                user = ap.getUser(username);
                if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                    session.setAttribute("uname", user);
                    session.removeAttribute("tempLog");
                    return "admin-dashboard.jsp";
                } else {
                    session.setAttribute("error", "username/password salah");
                    return "admin-login.jsp";
                }
            } else {
                session.setAttribute("error", "username tidak ada");
                return "admin-login.jsp";
            }
        }
    }

    public String logout() {
        HttpSession session = request.getSession();
        if (session.getAttribute("uname") != null) {
            session.removeAttribute("uname");
            return "admin-login.jsp";
        } else {
            session.setAttribute("error", "sudah logout sebelumnya");
            return "admin-login.jsp";
        }
    }
}
