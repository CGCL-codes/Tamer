package com.sns2Life.common.presentation.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionFilter implements Filter {

    private FilterConfig filterConfig;

    /**
     * ִ��filter.
     *
     * @param request HTTP����
     * @param response HTTP��Ӧ
     * @param chain filter��
     *
     * @throws IOException ����filter��ʱ���������������
     * @throws ServletException ����filter��ʱ�����һ�����
     */
    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest && response instanceof HttpServletResponse) || (request.getAttribute(getClass().getName()) != null)) {
            chain.doFilter(request, response);
            return;
        }
        request.setAttribute(getClass().getName(), Boolean.TRUE);
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        doFilter(req, res, chain);
    }

    /**
     * ȡ��filter��������Ϣ��
     *
     * @return <code>FilterConfig</code>����
     */
    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    /**
     * ȡ��servlet��������������Ϣ��
     *
     * @return <code>ServletContext</code>����
     */
    public ServletContext getServletContext() {
        return getFilterConfig().getServletContext();
    }

    /**
     * ��ʼ��filter��
     *
     * @throws ServletException ����ʼ��ʧ��
     */
    public void init() throws ServletException {
    }

    /**
	 * ��ʼ������
	 * 
	 * @param paramName
	 * @param defaultValue
	 * @return String
	 */
    private String getInitParameter(String paramName, String defaultValue) {
        String value = trimToNull(filterConfig.getInitParameter(paramName));
        if (value == null) {
            value = trimToNull(filterConfig.getServletContext().getInitParameter(paramName));
        }
        return (value == null) ? defaultValue : value;
    }

    /**
	 * ȡ���ַ��е����ҿո��null
	 * 
	 * @param str
	 * @return String
	 */
    private String trimToNull(String str) {
        if (str != null) {
            str = str.trim();
            if (str.length() == 0) {
                str = null;
            }
        }
        return str;
    }

    /**
     * ���filter.
     */
    public void destroy() {
        filterConfig = null;
    }

    /**
     * ��ʼ��filter��
     *
     * @param filterConfig filter��������Ϣ
     *
     * @throws ServletException ����ʼ��ʧ��
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        init();
    }

    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        BiddingHttpContext context = null;
        try {
            ServletContext servletContext = ((HttpServletRequest) request).getSession().getServletContext();
            context = new BiddingHttpContext(request, response, servletContext);
            chain.doFilter(context.getRequest(), context.getResponse());
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            try {
                if (context != null) {
                    context.commit();
                }
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }
    }
}
