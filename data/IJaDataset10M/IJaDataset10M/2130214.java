package com.csp.controls;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home/*")
public class HomeControl {

    @RequestMapping(value = "/")
    public String goIndex() {
        return "index";
    }

    @RequestMapping(value = "/index2.do")
    public String goIndex2() {
        return "index2";
    }
}
