package com.pinae.timon;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.pinae.timon.filter.resource.Security;

/**
 * 对Servlet对象进行包装
 * 
 * @author 惠毓赓
 *
 */
public class TimonServlet {

    private HttpServlet servlet;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private String web_inf_folder;

    private Security security;

    /**
	 * 构造函数
	 */
    public TimonServlet() {
    }

    /**
	 * 构造函数
	 * 
	 * @param servlet 向Timon的HTTP请求的HttpServlet
	 * @param request  向TimonHTTP请求的HttpServletRequest
	 * @param response 向TimonHTTP请求的HttpServletResponse
	 * @param web_inf_folder WEB_INF文件夹的真实位置
	 * @param security 对调用Bean的方法保护安全配置
	 */
    public TimonServlet(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response, String web_inf_folder, Security security) {
        super();
        this.servlet = servlet;
        this.request = request;
        this.response = response;
        this.web_inf_folder = web_inf_folder;
        this.security = security;
    }

    /**
	 * 返回WEB_INF文件夹的真实位置
	 * 
	 * @return WEB_INF文件夹的真实位置
	 */
    public String getWebInfFolder() {
        return web_inf_folder;
    }

    /**
	 * 设置WEB_INF文件夹的真实位置
	 * 
	 * @param web_inf_folder WEB_INF文件夹的真实位置
	 */
    public void setWebInfFolder(String web_inf_folder) {
        this.web_inf_folder = web_inf_folder;
    }

    /**
	 * 返回向TimonHTTP请求的HttpServletRequest
	 * 
	 * @return HTTP请求的HttpServletRequest
	 */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
	 * 设置向TimonHTTP请求的HttpServletRequest
	 * 
	 * @param request HTTP请求的HttpServletRequest
	 */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
	 * 返回向TimonHTTP请求的HttpServletResponse
	 * 
	 * @return HTTP请求的HttpServletResponse
	 */
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
	 * 设置向TimonHTTP请求的HttpServletResponse
	 * 
	 * @param response HTTP请求的HttpServletResponse
	 */
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**
	 * 返回向TimonHTTP请求的HttpServlet
	 * 
	 * @return HTTP请求的HttpServlet
	 */
    public HttpServlet getServlet() {
        return servlet;
    }

    /**
	 * 设置向TimonHTTP请求的HttpServlet
	 * 
	 * @param servlet HTTP请求的HttpServlet
	 */
    public void setServlet(HttpServlet servlet) {
        this.servlet = servlet;
    }

    /**
	 * 返回对调用Bean的方法保护安全配置
	 * 
	 * @return 对调用Bean的方法保护安全配置
	 */
    public Security getSecurity() {
        return security;
    }

    /**
	 * 设置对调用Bean的方法保护安全配置
	 * 
	 * @param security 对调用Bean的方法保护安全配置
	 */
    public void setSecurity(Security security) {
        this.security = security;
    }
}
