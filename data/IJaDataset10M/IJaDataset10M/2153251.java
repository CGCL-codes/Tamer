package com.juliashine.web.spring.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juliashine@gmail.com 2011-5-23
 *
 */
@Controller
@RequestMapping("/demo.do")
public class DemoController {

    @RequestMapping
    public ModelAndView demo() {
        System.out.println("hello, this is a demo.");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("demo");
        mav.addObject("name", "zzy");
        return mav;
    }
}
