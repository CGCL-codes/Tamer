package com.zhongkai.web.control.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.zhongkai.model.config.User;
import com.zhongkai.service.config.ButtonService;
import com.zhongkai.service.config.MenuService;
import com.zhongkai.service.config.UserService;

@Controller
@RequestMapping(value = "/system/ajax/user.do")
public class UserAjaxControl {

    private Logger log = Logger.getLogger(this.getClass());

    private UserService userService;

    @Resource
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(params = "method=findUserAccount")
    public String findUserAccount(HttpServletResponse response, String userAccount) throws IOException {
        try {
            List<User> userList = userService.findByHql("from User where userAccount=?", new Object[] { userAccount });
            PrintWriter out = response.getWriter();
            if (userAccount == null || "".equals(userAccount)) {
                out.write("<font color=\"red\">账号/用户名不能为空!</font>");
                return null;
            }
            if (userList == null || userList.size() == 0 || userList.isEmpty()) {
                out.write("<font color=\"green\">该账号/用户名可用!</font>");
                return null;
            } else {
                out.write("<font color=\"red\">该账号/用户名已存在!</font>");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }
    }

    @RequestMapping(params = "method=findIdentifier")
    public String findIdentifier(HttpServletResponse response, String userIdentifier) throws IOException {
        try {
            List<User> userList = userService.findByHql("from User where userIdentifier=?", new Object[] { userIdentifier });
            PrintWriter out = response.getWriter();
            if (userIdentifier == null || "".equals(userIdentifier)) {
                out.write("<span style='color:red'>编号不能为空!</span>");
                return null;
            }
            Pattern pattern = Pattern.compile("^\\d{1,11}$");
            if (!pattern.matcher(userIdentifier).matches()) {
                out.write("<span style='color:red'>编号过长或非法!</span>");
                return null;
            }
            if (userList == null || userList.size() == 0 || userList.isEmpty()) {
                out.write("<span style='color:green'>该编号可用!</span>");
                return null;
            } else {
                out.write("<span style='color:red'>该编号已存在!</span>");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }
    }
}
