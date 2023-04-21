package com.cromoteca.meshcms.server.core;

import com.cromoteca.meshcms.client.toolbox.Path;
import com.cromoteca.meshcms.server.toolbox.InMemoryResponseWrapper;
import com.cromoteca.meshcms.server.toolbox.JSON;
import com.cromoteca.meshcms.server.toolbox.Locales;
import com.cromoteca.meshcms.server.toolbox.Web;
import com.cromoteca.meshcms.server.webview.Expiring;
import com.cromoteca.meshcms.server.webview.Scope;
import com.cromoteca.meshcms.server.webview.ServerPage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtils;

public class Context {

    /**
	 * Name of the request parameter that is used to specify some actions.
	 * Currently only {@link #ACTION_NO_DRAFT} is used as value. This parameter is
	 * read by custom JSP tags.
	 */
    public static final String ACTION_NAME = "meshcmsaction";

    public static final String ACTION_NO_DRAFT = "nodraft";

    public static final String ACTION_COMPARE = "compare";

    public static final String JSTL_FMT_LOCALE = "javax.servlet.jsp.jstl.fmt.locale.request";

    public static final Path MESHCMS_PATH = new Path("meshcms");

    private static final String EXPIRING_PREFIX = "expiring:";

    private static final ThreadLocal<Context> threadLocal = new ThreadLocal<Context>();

    private static ServletContext servletContext;

    private HttpServletRequest request;

    private InMemoryResponseWrapper response;

    private URL url;

    static void setWebSite(WebSite webSite) {
        setSingleton(Scope.REQUEST, webSite, WebSite.class);
    }

    public static Locale getLocale() {
        boolean mustSet = true;
        Locale locale = null;
        Object jstlLocale = getAttribute(Scope.REQUEST, JSTL_FMT_LOCALE, Object.class);
        if (jstlLocale instanceof Locale) {
            locale = (Locale) jstlLocale;
            mustSet = false;
        } else if (jstlLocale instanceof String) {
            locale = Locales.getLocale((String) jstlLocale);
        }
        if (locale == null) {
            locale = getRequest().getLocale();
            if (locale == null) {
                locale = Locale.getDefault();
            }
        }
        if (mustSet) {
            setLocale(locale);
        }
        return locale;
    }

    public static void setLocale(Locale locale) {
        setAttribute(Scope.REQUEST, JSTL_FMT_LOCALE, locale);
        getResponse().setLocale(locale);
    }

    public static <T> T loadFromJSON(Class<T> cls, File file) throws IOException {
        if (file.exists()) {
            Reader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                return JSON.getGson().fromJson(reader, cls);
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }
        return null;
    }

    public static void storeToJSON(Object o, File file) throws IOException {
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(file));
            JSON.getGson().toJson(o, writer);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static ResourceBundle getConstants() {
        return getBundle("Constants", false);
    }

    public static ResourceBundle getMessages() {
        return getBundle("Messages", false);
    }

    public static ResourceBundle getConstantsInUserLocale() {
        return getBundle("Constants", true);
    }

    public static ResourceBundle getMessagesInUserLocale() {
        return getBundle("Messages", true);
    }

    private static ResourceBundle getBundle(String name, boolean inUserLocale) {
        inUserLocale = inUserLocale && SessionUser.get() != null;
        String key = "b:" + name + inUserLocale;
        ResourceBundle bundle = Context.getAttribute(Scope.REQUEST, key, ResourceBundle.class);
        if (bundle == null) {
            Locale locale = inUserLocale ? Locales.getLocale(SessionUser.get().getLocale()) : getLocale();
            bundle = ResourceBundle.getBundle("com.cromoteca.meshcms.client.i18n." + name, locale);
            Context.setAttribute(Scope.REQUEST, key, bundle);
        }
        return bundle;
    }

    static void setRequestContext(HttpServletRequest request, InMemoryResponseWrapper response) {
        set(request, response);
    }

    private static Context getContext() {
        return threadLocal.get();
    }

    public static HttpServletRequest getRequest() {
        return getContext().request;
    }

    public static HttpServletResponse getResponse() {
        return getContext().response;
    }

    public static ServletContext getServletContext() {
        return servletContext;
    }

    public static String getContextPath() {
        return getRequest().getContextPath();
    }

    public static URL getURL() {
        return threadLocal.get().url;
    }

    public static void setServletContext(ServletContext sc) {
        if (servletContext == null) {
            servletContext = sc;
        }
    }

    public static boolean isActive() {
        return threadLocal.get() != null;
    }

    public static void set(HttpServletRequest request, InMemoryResponseWrapper response) {
        Context c = new Context();
        threadLocal.set(c);
        c.request = request;
        c.response = response;
        try {
            c.url = new URL(Web.getFullURL(request));
        } catch (MalformedURLException ex) {
            log(ex);
        }
        if (Server.get() == null) {
            Server.init();
        }
    }

    public static void unset() {
        threadLocal.remove();
    }

    public static boolean hasAttribute(Scope scope, String name) {
        return getAttribute(scope, name, Object.class) != null;
    }

    public static <T> boolean hasSingleton(Scope scope, Class<T> cls) {
        return getSingleton(scope, cls) != null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAttribute(Scope scope, String name, Class<T> cls) {
        T result = null;
        switch(scope) {
            case APPLICATION:
                result = (T) servletContext.getAttribute(name);
                break;
            case REQUEST:
                result = (T) getRequest().getAttribute(name);
                break;
            case SESSION:
                HttpSession session = getRequest().getSession(false);
                if (session != null) {
                    result = (T) session.getAttribute(name);
                }
                break;
        }
        return result;
    }

    public static void setAttribute(Scope scope, String name, Object value) {
        switch(scope) {
            case APPLICATION:
                servletContext.setAttribute(name, value);
                break;
            case REQUEST:
                getRequest().setAttribute(name, value);
                break;
            case SESSION:
                getRequest().getSession(true).setAttribute(name, value);
                break;
        }
    }

    public static void removeAttribute(Scope scope, String name) {
        switch(scope) {
            case APPLICATION:
                servletContext.removeAttribute(name);
                break;
            case REQUEST:
                getRequest().removeAttribute(name);
                break;
            case SESSION:
                HttpSession session = getRequest().getSession(false);
                if (session != null) {
                    session.removeAttribute(name);
                }
                break;
        }
    }

    public static <T> T getSingleton(Scope scope, Class<T> cls) {
        return getAttribute(scope, cls.getName(), cls);
    }

    public static void setSingleton(Scope scope, Object value, Class<?> cls) {
        if (value != null && !(cls.isAssignableFrom(value.getClass()))) {
            throw new IllegalArgumentException(value.getClass() + " does not extend/implement " + cls);
        }
        setAttribute(scope, cls.getName(), value);
    }

    public static void removeSingleton(Scope scope, Class cls) {
        removeAttribute(scope, cls.getName());
    }

    protected static <T extends Expiring> T getExpiringAttribute(String name, Class<T> cls, Scope scope) {
        name = EXPIRING_PREFIX + name;
        T t = getAttribute(scope, name, cls);
        if (t != null) {
            removeAttribute(scope, name);
            if (t.getExpirationTime() < System.currentTimeMillis()) {
                t = null;
            }
        }
        return t;
    }

    protected static void setExpiringAttribute(String name, Expiring value, Scope scope) {
        setAttribute(scope, EXPIRING_PREFIX + name, value);
    }

    protected static void removeExpiringAttribute(String name, Scope scope) {
        removeAttribute(scope, EXPIRING_PREFIX + name);
    }

    public static void fillModel(Object model) {
        HttpServletRequest request = getRequest();
        Enumeration names = request.getParameterNames();
        Map<String, String[]> map = new HashMap<String, String[]>();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            map.put(name, request.getParameterValues(name));
        }
        try {
            BeanUtils.populate(model, map);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void log(String message) {
        servletContext.log(message);
    }

    public static void log(String message, Throwable t) {
        servletContext.log(message, t);
    }

    public static void log(Throwable t) {
        servletContext.log(t.getMessage(), t);
    }

    public static void include(String file, ServerPage page, HttpServletResponse response) throws ServletException, IOException {
        include(file, page, response, "bean");
    }

    public static void include(String file, ServerPage page, HttpServletResponse response, String beanName) throws ServletException, IOException {
        HttpServletRequest request = getRequest();
        Object oldBean = request.getAttribute(beanName);
        try {
            String redirect = null;
            if (page != null) {
                Object bean = page.getBean();
                if (bean != null) {
                    Context.fillModel(bean);
                    request.setAttribute(beanName, bean);
                }
                redirect = page.process();
            }
            if (redirect == null) {
                request.getRequestDispatcher(file).include(request, response);
            } else {
                RequestContext.get().redirect(redirect, false, false);
            }
        } finally {
            if (oldBean == null) {
                request.removeAttribute(beanName);
            } else {
                request.setAttribute(beanName, oldBean);
            }
        }
    }
}
