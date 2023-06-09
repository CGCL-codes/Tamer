package org.brandao.brutos.web.scope;

import javax.servlet.ServletRequest;
import org.brandao.brutos.BrutosException;
import org.brandao.brutos.MvcRequest;
import org.brandao.brutos.web.ContextLoaderListener;
import org.brandao.brutos.web.http.BrutosRequest;
import org.brandao.brutos.scope.Scope;
import org.brandao.brutos.web.ContextLoader;
import org.brandao.brutos.web.AbstractWebApplicationContext;
import org.brandao.brutos.web.WebApplicationContext;
import org.brandao.brutos.web.WebMvcRequest;

/**
 *
 * @author Afonso Brandao
 */
public class ParamScope implements Scope {

    public ParamScope() {
    }

    private ServletRequest getServletRequest() {
        WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        MvcRequest request = context.getMvcRequest();
        if (!(request instanceof WebMvcRequest)) throw new BrutosException("invalid web request: " + request.getClass());
        return ((WebMvcRequest) request).getServletRequest();
    }

    public void put(String name, Object value) {
        BrutosRequest request = (BrutosRequest) getServletRequest();
        request.setObject(name, value);
    }

    public Object get(String name) {
        BrutosRequest request = (BrutosRequest) getServletRequest();
        return request.getObject(name);
    }

    public Object getCollection(String name) {
        BrutosRequest request = (BrutosRequest) getServletRequest();
        return request.getObjects(name);
    }

    public void remove(String name) {
    }
}
