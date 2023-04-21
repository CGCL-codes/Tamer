package cn.edu.bit.mm.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import cn.edu.bit.mm.SearchAction;

/**
 * 
 * @author maomao
 *  为什么非得用spring?????
 *  模拟struts2工作方式人工集成用spring装配调用controller
 */
public class Commander implements Filter {

    private ServletContext servletContext;

    public void destroy() {
        servletContext = null;
    }

    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;
        SearchAction servlet = (SearchAction) ctx.getBean("/searching");
        servlet.execute(request, response, servletContext);
    }

    public void init(FilterConfig arg0) throws ServletException {
        servletContext = arg0.getServletContext();
    }
}
