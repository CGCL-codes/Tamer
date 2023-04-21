package com.demo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class Display1Controller extends SimpleFormController {

    public Display1Controller() {
        setCommandClass(DisplayViewBean.class);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        DisplayViewBean displayViewBean = new DisplayViewBean();
        String first = request.getParameter("first");
        String second = request.getParameter("second");
        String third = request.getParameter("third");
        displayViewBean.setFirst(first);
        displayViewBean.setSecond(second);
        displayViewBean.setThird(third);
        System.out.println("in form backing of display1");
        return displayViewBean;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        System.out.println("on submit of retrive pdf ");
        DisplayViewBean displayViewBean = (DisplayViewBean) command;
        ModelAndView targetLocation = new ModelAndView(getFormView(), getCommandName(), displayViewBean);
        targetLocation = new ModelAndView(new RedirectView("/demo/display/display1"));
        return targetLocation;
    }

    protected void doSubmitAction(Object command) throws Exception {
        System.out.println("fjds::::::44");
        super.doSubmitAction(command);
    }

    @Override
    protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        System.out.println("fjds::::::77");
        return super.processFormSubmission(request, response, command, errors);
    }
}
