package org.soybeanMilk.web.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.soybeanMilk.web.WebObjectSource;

/**
 * {@linkplain WebObjectSource WEB对象源}工厂，{@linkplain DispatchServlet}使用它来为请求创建WEB对象源。
 * @author earthAngry@gmail.com
 * @date 2010-12-9
 */
public interface WebObjectSourceFactory {

    /**
	 * 为请求创建WEB对象源
	 * @param request 当前{@linkplain HttpServletRequest 请求}对象
	 * @param response 当前{@linkplain HttpServletResponse 回应}对象
	 * @param application 当前{@linkplain ServletContext Servlet语境}对象
	 * @return
	 */
    WebObjectSource create(HttpServletRequest request, HttpServletResponse response, ServletContext application);
}
