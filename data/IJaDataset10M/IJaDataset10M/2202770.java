package com.guanghua.brick.html.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 *
 * 更改所生成类型注释的模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
public class EncodingFilter implements Filter {

    private String encoding = null;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.encoding = filterConfig.getInitParameter("encoding");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request.getCharacterEncoding() == null && this.encoding != null) {
            request.setCharacterEncoding(this.encoding);
        }
        String url = ((HttpServletRequest) request).getRequestURL().toString();
        if (url.endsWith(".jpg") || url.endsWith(".gif") || url.endsWith(".png") || url.endsWith(".cur") || url.endsWith(".ico") || url.endsWith(".bmp")) {
            ((HttpServletResponse) response).setHeader("Cache-Control", "max-age=604800");
        } else if (url.endsWith(".css") || url.endsWith(".js")) {
            ((HttpServletResponse) response).setHeader("Cache-Control", "max-age=86400");
        } else {
            ((HttpServletResponse) response).setHeader("Cache-Control", "no-cache");
        }
        chain.doFilter(request, response);
    }

    public void destroy() {
        this.encoding = null;
    }
}
