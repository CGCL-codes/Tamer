package com.sy.easycms.struts;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionServlet;

/**
 * 自定义了ActionServlet的子类,用于设定Action的统一编码
 * @author Luke Sun
 * @version 1.0
 */
public class BaseAction extends ActionServlet {

    protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("utf-8");
        super.process(request, response);
    }
}
