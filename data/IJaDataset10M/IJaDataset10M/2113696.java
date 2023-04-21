package com.jy.bookshop.web.controller.users;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import com.jy.bookshop.beans.UserInfo;
import com.jy.bookshop.service.UserInfoService;
import com.jy.common.util.WebUtils;

public class ViewMyUserInfoController implements Controller {

    private Logger log = Logger.getLogger(this.getClass());

    private UserInfoService userInfoService;

    public UserInfoService getUserInfoService() {
        return userInfoService;
    }

    public void setUserInfoService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("���� view my userInfo Page...");
        UserInfo userInfo = WebUtils.getSessionObject(request);
        userInfo = userInfoService.getUserInfoByName(userInfo.getName());
        WebUtils.setSessionObject(request, userInfo);
        return new ModelAndView("userinfo");
    }
}
