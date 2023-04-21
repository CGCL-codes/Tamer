package com.jz.action;

import java.io.IOException;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import com.fivestars.interfaces.bbs.client.Client;
import com.fivestars.interfaces.bbs.util.XMLHelper;
import com.jz.config.UrlConfig;
import com.jz.model.User;
import com.jz.util.MD5;
import com.opensymphony.xwork2.ActionSupport;

public class PassportAction extends ActionSupport {

    public String loginAuth() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse res = ServletActionContext.getResponse();
        String username = (String) request.getParameter("Email");
        String passward = (String) request.getParameter("Passwd");
        passward = MD5.getMD5Encode(passward);
        Client e = new Client();
        String result = e.uc_user_login(username, passward);
        LinkedList<String> rs = XMLHelper.uc_unserialize(result);
        if (rs.size() > 0) {
            int $uid = Integer.parseInt(rs.get(0));
            String $username = rs.get(1);
            String $password = rs.get(2);
            String $email = rs.get(3);
            if ($uid > 0) {
                User user = new User($username, $password, $email);
                request.getSession().setAttribute("authUser", user);
                System.out.println("登录成功");
                System.out.println($username);
                System.out.println($password);
                System.out.println($email);
                String $ucsynlogin = e.uc_user_synlogin($uid);
                res.setContentType("text/html");
                res.setCharacterEncoding("utf-8");
                if ($ucsynlogin != null) {
                    res.getWriter().write("成功登陆论坛" + $ucsynlogin);
                } else {
                    res.getWriter().write("请先登陆并激活论坛账号");
                }
                String timeScript = "<input type='text' readonly='true' value='5' size='1' id='time' style= 'border:0px;background-color:#FFFFFF'>秒后跳转到论坛；如未成功<script language='javascript'>var t = 5;var time = document.getElementById('time');function fun(){t--;time.value = t;if(t<=0){location.href = '" + UrlConfig.webUrl + "';clearInterval(inter);}}var inter = setInterval('fun()',1000);</script>";
                res.getWriter().write(timeScript);
                res.getWriter().write("<a href='" + UrlConfig.webUrl + "'>请点此进入</a>");
                return null;
            } else if ($uid == -1) {
                System.out.println("用户不存在,或者被删除");
            } else if ($uid == -2) {
                System.out.println("密码错");
            } else {
                System.out.println("未定义");
            }
        } else {
            System.out.println("Login failed");
            System.out.println(result);
        }
        return null;
    }

    public String join() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String username = (String) request.getParameter("Username");
        String email = (String) request.getParameter("Email");
        String passward = (String) request.getParameter("Passwd");
        String passwdAgain = (String) request.getParameter("PasswdAgain");
        Client uc = new Client();
        String $returns = uc.uc_user_register(username, passward, email);
        int $uid = Integer.parseInt($returns);
        if ($uid <= 0) {
            if ($uid == -1) {
                System.out.print("用户名不合法");
            } else if ($uid == -2) {
                System.out.print("包含要允许注册的词语");
            } else if ($uid == -3) {
                System.out.print("用户名已经存在");
            } else if ($uid == -4) {
                System.out.print("Email 格式有误");
            } else if ($uid == -5) {
                System.out.print("Email 不允许注册");
            } else if ($uid == -6) {
                System.out.print("该 Email 已经被注册");
            } else {
                System.out.print("未定义");
            }
        } else {
            System.out.println("OK:" + $returns);
        }
        try {
            response.sendRedirect("../index.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String loginOut() throws IOException {
        Client uc = new Client();
        String $ucsynlogout = uc.uc_user_synlogout();
        System.out.println("退出成功" + $ucsynlogout);
        if ($ucsynlogout != null) {
            HttpServletRequest request = ServletActionContext.getRequest();
            HttpSession session = request.getSession();
            session.removeAttribute("authUser");
            HttpServletResponse res = ServletActionContext.getResponse();
            System.out.println($ucsynlogout);
            res.reset();
            res.setContentType("text/html");
            res.setCharacterEncoding("utf-8");
            res.getWriter().write("成功登出" + $ucsynlogout);
            String timeScript = "<input type='text' readonly='true' value='5' size='1' id='time'  style= 'border:0px;background-color:#FF0000'>秒后回到首页；如未成功<script language='javascript'>var t = 5;var time = document.getElementById('time');function fun(){t--;time.value = t;if(t<=0){parent.location.href = '" + UrlConfig.webUrl + "';clearInterval(inter);}}var inter = setInterval('fun()',1000);</script>";
            res.getWriter().write(timeScript);
            res.getWriter().write("<a href='" + UrlConfig.webUrl + "' target='_parent'>请点此进入</a>");
            return null;
        }
        return null;
    }
}
