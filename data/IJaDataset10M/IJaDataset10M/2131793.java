package com.tiger.aowim.contactus.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.framework.base.BaseController;

@Controller
@RequestMapping("/contactus")
public class ContactusController extends BaseController<Object, Long> {

    @Override
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response, Object entityBean) throws Exception {
        return new ModelAndView("/contactus/index");
    }
}
