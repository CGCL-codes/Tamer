package sdloader.javaee.impl;

import java.util.Enumeration;
import java.util.Map;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import sdloader.util.CollectionsUtil;
import sdloader.util.IteratorEnumeration;

/**
 * FilterConfig実装クラス
 * 
 * @author c9katayama
 */
public class FilterConfigImpl implements FilterConfig {

    private Map<String, String> initParameter = CollectionsUtil.newHashMap();

    private String filterName;

    private ServletContext servletContext;

    public String getFilterName() {
        return filterName;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public String getInitParameter(String key) {
        return (String) initParameter.get(key);
    }

    public Enumeration<String> getInitParameterNames() {
        return new IteratorEnumeration<String>(initParameter.keySet().iterator());
    }

    public void addInitParameter(String key, String value) {
        initParameter.put(key, value);
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }
}
