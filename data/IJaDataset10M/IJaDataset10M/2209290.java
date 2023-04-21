package org.groovyflow.spring.web.prettyURL;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import javax.servlet.http.HttpServletResponse;

public class PrettyURLResponse implements InvocationHandler {

    private HttpServletResponse response;

    private String path;

    private SafeRedirect safeRedirect;

    private PrettyURLResponse(HttpServletResponse response, String path, SafeRedirect safeRedirect) {
        this.response = response;
        this.path = path;
        this.safeRedirect = safeRedirect;
    }

    public static HttpServletResponse newInstance(HttpServletResponse target, String path, SafeRedirect safeRedirect) {
        Class targetClass = target.getClass();
        Class[] interfaces = new Class[] { javax.servlet.http.HttpServletResponse.class };
        return (HttpServletResponse) Proxy.newProxyInstance(targetClass.getClassLoader(), interfaces, new PrettyURLResponse(target, path, safeRedirect));
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (method.getName().equals("sendRedirect") || method.getName().equals("encodeRedirectURL")) {
                args[0] = (String) this.safeRedirect.makeSafeRedirect((String) args[0], path);
            }
            return method.invoke(response, args);
        } catch (InvocationTargetException ex) {
            throw ex.getTargetException();
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (IllegalAccessException ex) {
            throw ex;
        }
    }
}
