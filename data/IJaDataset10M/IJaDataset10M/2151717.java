package com.hk.frame.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.hk.frame.util.DataUtil;

/**
 * web运行的入口
 * 
 * @author akwei
 */
public class ActionFilter implements Filter {

    /**
	 * 忽略匹配的文件集合，通过web.xml进行配置，只需要配置扩展名称，例如：.jpg,.jsp，暂不支持通配符*
	 */
    private List<String> ingoreList = new ArrayList<String>();

    /**
	 * 程序运行器初始化
	 */
    private ActionExe actionExe;

    public void init(FilterConfig config) throws ServletException {
        String actionExeClass = config.getInitParameter("actionExeClass");
        if (DataUtil.isNotEmpty(actionExeClass)) {
            try {
                Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(actionExeClass);
                this.actionExe = (ActionExe) clazz.getConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            this.actionExe = new ActionExe();
        }
        String url_extension = config.getInitParameter("url-extension");
        String v = config.getInitParameter("debug");
        if (v != null && v.equals("true")) {
            actionExe.setDebug(true);
        }
        actionExe.setUrl_extension(url_extension);
        String endIngore = config.getInitParameter("endIngore");
        if (endIngore != null) {
            String[] t = endIngore.split(",");
            if (t != null) {
                for (String s : t) {
                    ingoreList.add(s);
                }
            }
        }
    }

    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) arg0;
        String uri = req.getRequestURI();
        if (uri.endsWith(".jsp")) {
            arg2.doFilter(arg0, arg1);
            return;
        }
        for (String s : ingoreList) {
            if (uri.endsWith(s)) {
                arg2.doFilter(arg0, arg1);
                return;
            }
        }
        req.setAttribute("appctx_path", req.getContextPath());
        this.actionExe.proccess(req, (HttpServletResponse) arg1);
    }

    @Override
    public void destroy() {
    }
}
