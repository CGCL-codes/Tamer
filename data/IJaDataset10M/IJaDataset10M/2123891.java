package org.lightframework.mvc;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletContext;
import org.lightframework.mvc.HTTP.Request;
import org.lightframework.mvc.HTTP.Response;
import org.lightframework.mvc.Result.ErrorResult;
import org.lightframework.mvc.internal.reflect.ReflectMethod;
import org.lightframework.mvc.internal.reflect.ReflectType;
import org.lightframework.mvc.routing.Routes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * represents a mvc application
 * 
 * @author fenghm (live.fenghm@gmail.com)
 * 
 * @since 1.0.0
 */
public class Application implements ApplicationContext {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private static final String DEFAULT_APPLICATION_NAME = "default";

    /** {@link ThreadLocal} to store context {@link Application} object */
    private static final ThreadLocal<Application> threadLocal = new ThreadLocal<Application>();

    /** store all the {@link Application}s managed by current mvc framwork  */
    private static final Map<Object, Application> applications = new ConcurrentHashMap<Object, Application>();

    /** default instance of {@link Application} */
    private static final Application defaultApplication = new Application();

    private String name = DEFAULT_APPLICATION_NAME;

    private String encoding = "UTF-8";

    private Module module = null;

    private LinkedList<Module> modules = new LinkedList<Module>();

    private Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();

    private ApplicationHandler handler = null;

    private Routes routes = new Routes();

    private Plugins plugins = new Plugins();

    private ServletContext servletContext = null;

    private Object externalContext = new Object();

    /**
	 * @return current {@link Application}
	 */
    public static Application current() {
        Application application = threadLocal.get();
        if (null == application) {
            return defaultApplication;
        }
        return application;
    }

    protected static Application currentOf(Object context) {
        return applications.get(context);
    }

    protected Application() {
    }

    protected Application(Object context, Module root) {
        this.externalContext = context;
        this.module = root;
    }

    protected Application(ServletContext context, Module root) {
        this.externalContext = context;
        this.servletContext = context;
        this.module = root;
    }

    public String name() {
        return name;
    }

    public String encoding() {
        return encoding;
    }

    public Plugins plugins() {
        return plugins;
    }

    public Routes routes() {
        return routes;
    }

    /**@return External Applicatoin Context Object,such as {@link javax.servlet.ServletContext} */
    public Object getExternalContext() {
        return externalContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public Module getRootModule() {
        return module;
    }

    LinkedList<Module> getChildModules() {
        return modules;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public Object removeAttribute(String name) {
        return attributes.remove(name);
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    protected final void start() {
        findController();
        if (null != handler) {
            handler.start();
            module.getPlugins().addAll(plugins);
        }
    }

    protected final void end() {
        if (null != handler) {
            handler.end();
        }
    }

    protected final boolean error(Request request, Response response, ErrorResult error) {
        if (null != handler) {
            return handler.error(request, response, error);
        }
        return false;
    }

    protected final void findController() {
        Class<?> controllerClass = module.findClass(module.getPackagee() + ".application");
        if (null != controllerClass) {
            this.handler = new ApplicationHandler();
            ReflectType controllerType = ReflectType.get(controllerClass);
            handler.instance = controllerType.newInstance();
            handler.start = controllerType.findMethod("start", ApplicationContext.class);
            handler.end = controllerType.findMethod("end", ApplicationContext.class);
            handler.error1 = controllerType.findMethod("error", ApplicationContext.class, Throwable.class);
            handler.error2 = controllerType.findMethod("error", ApplicationContext.class, Request.class, Response.class, Throwable.class);
            handler.error3 = controllerType.findMethod("error", ApplicationContext.class, Request.class, Response.class, ErrorResult.class);
        }
    }

    protected static void setCurrent(Application application) {
        threadLocal.set(application);
        if (null != application) {
            applications.put(application.getExternalContext(), application);
        }
    }

    protected static void remove(Object context) {
        Application application = applications.get(context);
        if (null != application) {
            log.info("[mvc] -> remove application '{}' of context : {}", application.name(), context);
            applications.remove(context);
        } else {
            log.info("[mvc] -> application not found of context : {}", context);
        }
    }

    protected static void removeCurrent() {
        Application application = threadLocal.get();
        if (null != application) {
            remove(application.getExternalContext());
        }
    }

    private class ApplicationHandler {

        Object instance;

        ReflectMethod start;

        ReflectMethod end;

        ReflectMethod error1;

        ReflectMethod error2;

        ReflectMethod error3;

        void start() {
            if (null != start) {
                log.info("[mvc] -> invoke application's start(..) method");
                start.invoke(instance, Application.this);
            }
        }

        void end() {
            if (null != end) {
                log.info("[mvc] -> invoke application's end(..) method");
                end.invoke(instance, Application.this);
            }
        }

        boolean error(Request request, Response response, ErrorResult error) {
            if (null != error3) {
                error3.invoke(instance, new Object[] { Application.this, request, response, error });
                return true;
            }
            if (null != error2) {
                error2.invoke(instance, new Object[] { Application.this, request, response, error.getError() });
                return true;
            }
            if (null != error1 && null != error.getError()) {
                error1.invoke(instance, new Object[] { Application.this, request, response, error.getError() });
                return true;
            }
            return false;
        }
    }
}
