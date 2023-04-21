package net.sf.ikms.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Spring 3.0.x MVC REST demo
 * 
 * @author <b>oxidy</b>, Copyright &#169; 2003-2011
 * @version 0.1,2011-3-28
 */
@Controller
@RequestMapping("/demo")
public class HelloWorldController {

    @RequestMapping("/hello")
    public ModelAndView helloWorld() {
        String message = "Hello World, Welcome to Spring 3.0.x MVC REST!";
        return new ModelAndView("hello", "message", message);
    }
}
