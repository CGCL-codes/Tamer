package com.test.util;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.ehcache.Element;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class TestServletEhCache extends HttpServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private CacheUtil cacheUtil = null;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        cacheUtil.putObjectInCache("test", "dddddd");
        cacheUtil.getCache().flush();
        cacheUtil.getCache().get("");
        cacheUtil.getCache().getCacheConfiguration();
        cacheUtil.getCache().getAverageGetTime();
        cacheUtil.getCache().getCacheManager();
        cacheUtil.getCache().getKeys();
        cacheUtil.getCache().getName();
        cacheUtil.getCache().getSize();
        cacheUtil.getCache().isElementInMemory("");
        cacheUtil.getCache().put(new Element("", ""));
        cacheUtil.getCache().remove("");
        System.out.println(cacheUtil.getObjectFromCache("test"));
    }

    public void init(ServletConfig config) throws ServletException {
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        cacheUtil = (CacheUtil) wac.getBean("dhsCache");
    }
}
