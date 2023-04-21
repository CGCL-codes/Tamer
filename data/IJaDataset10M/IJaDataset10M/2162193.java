package cn.common.servlet;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.common.resource.DictBuilder;
import cn.common.resource.ResourceBuilder;

/**
 * 这个servlet用来加载所有应用需要初始化的信息
 * @author Administrator
 *
 */
public class InitServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ServletConfig cnf = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.cnf = config;
        initResource(cnf.getServletContext());
    }

    /**
	 * 析构方法
	 */
    public void destroy() {
        super.destroy();
        ResourceBuilder.clear();
        this.getServletContext().removeAttribute(ResourceBuilder.RESOURCE_KEY);
        DictBuilder.clear();
        this.getServletContext().removeAttribute(DictBuilder.DICT_KEY);
        System.out.println("destroy the init servlet..............");
    }

    /**
	 * 当客户端发送重新初始化的请求的时候， 用来重新初始化系统中的全局的用户信息
	 *
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    /**
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
	 * 初始化资源类的信息
	 * 
	 * @throws ServletException if an error occurs
	 */
    public void initResource(ServletContext app) {
        try {
            System.out.println("start the init servlet..............");
            ResourceBuilder instance = ResourceBuilder.getInstance();
            app.setAttribute(ResourceBuilder.RESOURCE_KEY, instance);
            ResourceBuilder.refresh(app);
            ResourceBuilder.stat();
            System.out.println("init the ResourceBuilder is finished........");
            DictBuilder dict = DictBuilder.getInstance();
            app.setAttribute(DictBuilder.DICT_KEY, dict);
            DictBuilder.refresh(app);
            DictBuilder.stat();
            System.out.println("init the DictBuilder is finished........");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
