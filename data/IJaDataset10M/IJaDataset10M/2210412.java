package jframe.common;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @描述:<p>WEB上下文工具类 </p>
 *
 * @作者: 叶平平(yepp)
 *
 * @时间: 2011-4-6 下午10:43:10
 */
public class Context {

    public static ServletContext servletContext;

    public static WebApplicationContext webApplicationContext;

    /**
	 * @描述:<p>得到request对象</p>
	 *
	 * @作者:  叶平平(yepp)
	 *
	 * @时间:  2011-4-6 下午11:00:51
	 *
	 * @修改历史: <p>修改时间、修改人、修改原因/说明</p>
	 *
	 * @return
	 */
    public static HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }

    /**
	 * @描述:<p>得到Session对象</p>
	 *
	 * @作者:  叶平平(yepp)
	 *
	 * @时间:  2011-4-6 下午11:01:39
	 *
	 * @修改历史: <p>修改时间、修改人、修改原因/说明</p>
	 *
	 * @return
	 */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
	 * @描述:<p>得到servletContent对象</p>
	 *
	 * @作者:  叶平平(yepp)
	 *
	 * @时间:  2011-4-6 下午11:02:35
	 *
	 * @修改历史: <p>修改时间、修改人、修改原因/说明</p>
	 *
	 * @return
	 */
    public static ServletContext getServletContext() {
        return servletContext;
    }

    /**
	 * @描述:<p>得到spring上下文</p>
	 *
	 * @作者:  叶平平(yepp)
	 *
	 * @时间:  2011-4-6 下午11:03:29
	 *
	 * @修改历史: <p>修改时间、修改人、修改原因/说明</p>
	 *
	 * @return
	 */
    public static WebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }

    /**
	 * @描述:<p>从spring配置文件中得到Bean的实例 </p>
	 *
	 * @作者:  叶平平(yepp)
	 *
	 * @时间:  2011-4-6 下午11:04:17
	 *
	 * @修改历史: <p>修改时间、修改人、修改原因/说明</p>
	 *
	 * @param beanName 名称
	 * @return Bean 实例
	 */
    public static Object getSpringBean(String beanName) {
        ServletContext servletContext = getServletContext();
        WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        return wac.getBean(beanName);
    }

    /**
	 * @描述:<p>删除查询条件</p>
	 *
	 * @作者:  叶平平(yepp)
	 *
	 * @时间:  2011-4-6 下午11:13:50
	 *
	 * @修改历史: <p>修改时间、修改人、修改原因/说明</p>
	 *
	 * @param actionName 访问action 名称
	 */
    public static void removeQueryParameter(String actionName) {
        ServletActionContext.getRequest().getSession().removeAttribute(actionName);
    }

    /**
	 * @描述:<p>判断当前用户是否对某个按钮具有操作权限</p>
	 *
	 * @作者:  叶平平(yepp)
	 *
	 * @时间:  2011-4-6 下午11:17:19
	 *
	 * @修改历史: <p>修改时间、修改人、修改原因/说明</p>
	 *
	 * @param operator
	 * @return
	 */
    public static Boolean hasPrivilege(String operator) {
        return false;
    }

    /**
	 * @描述:<p>获取上下文根路径</p>
	 *
	 * @作者:  叶平平(yepp)
	 *
	 * @时间:  2011-4-6 下午11:22:53
	 *
	 * @修改历史: <p>修改时间、修改人、修改原因/说明</p>
	 *
	 * @return
	 */
    public static String getContextPath() {
        return ServletActionContext.getRequest().getContextPath();
    }

    /**
	 * @描述:<p>获取上下文URL全路径</p>
	 *
	 * @作者:  叶平平(yepp)
	 *
	 * @时间:  2011-4-6 下午11:23:40
	 *
	 * @修改历史: <p>修改时间、修改人、修改原因/说明</p>
	 *
	 * @return
	 */
    public static String getContextAllPath() {
        return getRequest().getScheme() + "://" + getRequest().getServerName() + ":" + getRequest().getServerPort() + getRequest().getContextPath();
    }
}
