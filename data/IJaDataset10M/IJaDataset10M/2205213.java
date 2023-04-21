package org.wikiup.modules.php;

import java.util.Iterator;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import com.caucho.quercus.servlet.QuercusServlet;
import org.wikiup.core.bean.WikiupDynamicSingleton;
import org.wikiup.core.impl.iterable.BeanPropertyNames;
import org.wikiup.core.inf.Releasable;
import org.wikiup.core.inf.ext.Context;
import org.wikiup.core.util.ContextUtil;

public class PhpServletContainer extends WikiupDynamicSingleton<PhpServletContainer> implements Context<Object, Object>, Iterable<String>, Releasable {

    private static QuercusServlet phpServlet;

    private ServletConfig servletConfig;

    public void firstBuilt() {
    }

    public Object get(String name) {
        return ContextUtil.getBeanProperty(this, name);
    }

    public void set(String name, Object obj) {
        ContextUtil.setBeanProperty(this, name, obj);
    }

    public Iterator<String> iterator() {
        return new BeanPropertyNames(getClass(), true).iterator();
    }

    public QuercusServlet getQuercusServlet() throws ServletException {
        if (phpServlet == null) {
            phpServlet = new QuercusServlet();
            phpServlet.init(servletConfig);
        }
        return phpServlet;
    }

    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    public void release() {
        if (phpServlet != null) phpServlet.destroy();
        phpServlet = null;
    }
}
