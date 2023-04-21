package com.bbs.util;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Render {

    public static String START = "/WEB-INF/views/";

    public static String END = ".jsp";

    /**
	 * 定向到name.jsp页面
	 * @param name
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
    public static void rend(String name, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(START + name + END).forward(request, response);
    }
}
