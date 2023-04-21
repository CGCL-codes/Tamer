package com.air.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import com.air.common.controller.IBaseController;

public interface IDemoController extends IBaseController {

    public ModelAndView toDemoView(HttpServletRequest request, HttpServletResponse response);

    public ModelAndView uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
