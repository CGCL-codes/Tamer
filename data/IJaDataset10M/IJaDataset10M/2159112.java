package com.khotyn.heresy.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.khotyn.heresy.bean.HeresyErrorMessage;
import com.khotyn.heresy.bean.PagedBean;
import com.khotyn.heresy.exception.IllegalUrlParamException;
import com.khotyn.heresy.service.FriendListService;

/**
 * 好友列表控制器
 * 
 * @author 黄挺
 * 
 */
@Controller
@RequestMapping("/friendList.html")
public class FriendListController {

    @Autowired
    FriendListService friendListService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView onLoad(HttpSession session, HttpServletRequest request, @RequestParam(value = "page", required = false) String pageStr) {
        Integer userID = (Integer) session.getAttribute("userID");
        try {
            validate(pageStr, userID);
        } catch (IllegalUrlParamException e) {
            return e.getErrorModel();
        }
        Integer page = Integer.parseInt(pageStr);
        PagedBean friends = friendListService.doService(userID, page);
        return new ModelAndView("friendList", "friends", friends);
    }

    private void validate(String pageStr, Integer userID) throws IllegalUrlParamException {
        HeresyErrorMessage errorMessage = null;
        if (userID == null) {
            errorMessage = new HeresyErrorMessage("您尚未登录，请先登录", "未登录", "login.html");
        } else if (!NumberUtils.isDigits(pageStr)) {
            errorMessage = new HeresyErrorMessage("非法的参数", "操作失败", "/Dragonfly/");
        }
        if (errorMessage != null) {
            throw new IllegalUrlParamException(errorMessage);
        }
    }
}
