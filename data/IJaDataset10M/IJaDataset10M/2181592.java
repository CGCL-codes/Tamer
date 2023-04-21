package com.rome.syncml.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 类VersionController.java的实现描述：TODO 类实现描述
 * 
 * @author rosebible 2011-8-23 上午09:32:03
 */
@Controller
public class PortalController {

    @RequestMapping(value = "/index")
    public String index(ModelMap model) throws Exception {
        return "portal/index";
    }

    @RequestMapping(value = "/home")
    public String home(ModelMap model) throws Exception {
        return "portal/home";
    }

    @RequestMapping(value = "/download")
    public String download(ModelMap model) throws Exception {
        return "portal/download";
    }

    @RequestMapping(value = "/faq")
    public String faq(ModelMap model) throws Exception {
        return "portal/faq";
    }

    @RequestMapping(value = "/about")
    public String about(ModelMap model) throws Exception {
        return "portal/about";
    }

    @RequestMapping(value = "/terms")
    public String terms(ModelMap model) throws Exception {
        return "portal/terms";
    }

    @RequestMapping(value = "/privacy")
    public String privacy(ModelMap model) throws Exception {
        return "portal/privacy";
    }
}
