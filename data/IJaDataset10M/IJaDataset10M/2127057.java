package com.kn.servlet;

import javax.servlet.http.HttpServlet;
import org.springframework.web.context.WebApplicationContext;
import com.kn.spring.BeanUtils;

public class BaseServlet extends HttpServlet {

    public Object getBean(String beanName) {
        WebApplicationContext wa = BeanUtils.getWebApplicationContext(this);
        return wa.getBean(beanName);
    }
}
