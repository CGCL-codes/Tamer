package com.ynhenc.topis.mobile.web;

import org.springframework.stereotype.*;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.DispatcherServlet;

@Controller
public class C9000_TwitterController extends ControllerCommon {

    @RequestMapping("/twitter.top")
    public String showTwitter(ModelMap model) {
        String forwardPage = "/jsp/view/v7000_twitterView.jsp";
        return forward(forwardPage);
    }
}
