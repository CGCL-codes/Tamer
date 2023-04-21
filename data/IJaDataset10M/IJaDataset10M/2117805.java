package cn.sduo.app.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import cn.sduo.app.service.admin.RegionService;

public abstract class BaseController {

    protected Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    protected RegionService regionService;

    public abstract String getTableId();

    public abstract ModelAndView update(HttpServletRequest request, HttpServletResponse response);

    public abstract ModelAndView view(HttpServletRequest request, HttpServletResponse response);

    public abstract ModelAndView delete(HttpServletRequest request, HttpServletResponse response, String id);

    public abstract ModelAndView list(HttpServletRequest request, HttpServletResponse response);

    @Autowired
    protected ResourceBundleMessageSource messageSource;
}
