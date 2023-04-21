package org.beanopen.f.engine;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.InitializingBean;

/**
 * 
 * @author Bean
 *
 */
public interface Execution extends InitializingBean {

    /**
	 * 执行过滤器主方法
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @param chain {@link ExecutionChain}
	 * @throws IOException
	 * @throws ServletException
	 */
    public void doFilter(HttpServletRequest request, HttpServletResponse response, ExecutionChain chain) throws IOException, ServletException;

    /**
	 * @param request {@link HttpServletRequest}
	 * @return 是否过滤当前请求
	 */
    public boolean isPattern(HttpServletRequest request);
}
