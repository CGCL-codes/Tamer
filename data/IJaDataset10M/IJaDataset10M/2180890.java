package com.hilaver.dzmis.exhibition.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.hilaver.dzmis.exhibition.hibernate.Color;

/**
 * Servlet implementation class for Servlet: ColorServlet
 *
 */
public class ColorServlet extends com.hilaver.dzmis.exhibition.servlet.AbstractBaseServlet implements javax.servlet.Servlet {

    static final long serialVersionUID = 1L;

    public ColorServlet() {
        this.fullName = Color.class.getName();
        this.simpleName = Color.class.getSimpleName();
    }
}
