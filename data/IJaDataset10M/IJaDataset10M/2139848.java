package com.wgo.bpot.server.web.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.caucho.hessian.io.SerializerFactory;
import com.caucho.hessian.server.HessianServlet;
import com.caucho.hessian.server.HessianSkeleton;
import com.caucho.services.server.GenericService;
import com.caucho.services.server.Service;
import com.caucho.services.server.ServiceContext;
import com.wgo.bpot.common.transport.exception.RemoteServiceException;
import com.wgo.bpot.common.transport.servicefacade.hessian.EnumSerializerFactory;
import com.wgo.bpot.common.transport.servicefacade.hessian.MethodSerializerFactory;
import com.wgo.bpot.common.transport.servicefacade.hessian.TransientTypeSerializerFactory;
import com.wgo.bpot.common.transport.servicefacade.hessian.UrlSerializerFactory;
import com.wgo.bpot.wiring.WiredService;
import com.wgo.bpot.wiring.WiringEngine;

/**
 * This class is required because of just a couple of lines within HessianServlet, which are made
 * impossible to modify.
 * The code that needed to change is:
 *  getSerializerFactory()  // This was possible to change in isolation
 *  within the service() method:
 *  	HessianInput in = new HessianInput(request.getInputStream());
 *  	in.setSerializerFactory(getSerializerFactory());  // new line added
 *  // This required me to copy/paste pretty much the entire servlet because of 
 *  the new line (indicated above)
 *
 * @author petterei
 */
public class LightServlet extends HessianServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(LightServlet.class);

    private Class _homeAPI;

    private Object _homeImpl;

    private Class _objectAPI;

    private Object _objectImpl;

    private HessianSkeleton _homeSkeleton;

    private HessianSkeleton _objectSkeleton;

    private SerializerFactory _serializerFactory;

    @Override
    public String getServletInfo() {
        return "Hessian Servlet";
    }

    /**
	 * Sets the home api.
	 */
    @Override
    public void setHomeAPI(Class api) {
        _homeAPI = api;
    }

    /**
	 * Sets the home implementation
	 */
    @Override
    public void setHome(Object home) {
        _homeImpl = home;
    }

    /**
	 * Sets the object api.
	 */
    @Override
    public void setObjectAPI(Class api) {
        _objectAPI = api;
    }

    /**
	 * Sets the object implementation
	 */
    @Override
    public void setObject(Object object) {
        _objectImpl = object;
    }

    /**
	 * Sets the service class.
	 */
    @Override
    public void setService(Object service) {
        setHome(service);
    }

    /**
	 * Sets the api-class.
	 */
    @Override
    public void setAPIClass(Class api) {
        setHomeAPI(api);
    }

    /**
	 * Gets the api-class.
	 */
    @Override
    public Class getAPIClass() {
        return _homeAPI;
    }

    /**
	 * Sets the serializer send collection java type.
	 */
    @Override
    public void setSendCollectionType(boolean sendType) {
        getSerializerFactory().setSendCollectionType(sendType);
    }

    /**
	 * Initialize the service, including the service object.
	 */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            if (_homeImpl != null) {
            } else if (getInitParameter("home-class") != null) {
                String className = getInitParameter("home-class");
                Class homeClass = loadClass(className);
                _homeImpl = homeClass.newInstance();
                init(_homeImpl);
            } else if (getInitParameter("service-class") != null) {
                String className = getInitParameter("service-class");
                Class homeClass = loadClass(className);
                _homeImpl = homeClass.newInstance();
                init(_homeImpl);
            } else {
                if (getClass().equals(HessianServlet.class)) throw new ServletException("server must extend HessianServlet");
            }
            if (_homeAPI != null) {
            } else if (getInitParameter("home-api") != null) {
                String className = getInitParameter("home-api");
                _homeAPI = loadClass(className);
            } else if (getInitParameter("api-class") != null) {
                String className = getInitParameter("api-class");
                _homeAPI = loadClass(className);
            } else if (_homeImpl != null) {
                _homeAPI = findRemoteAPI(_homeImpl.getClass());
                if (_homeAPI == null) _homeAPI = _homeImpl.getClass();
            }
            if (_objectImpl != null) {
            } else if (getInitParameter("object-class") != null) {
                String className = getInitParameter("object-class");
                Class objectClass = loadClass(className);
                _objectImpl = objectClass.newInstance();
                init(_objectImpl);
            }
            if (_objectAPI != null) {
            } else if (getInitParameter("object-api") != null) {
                String className = getInitParameter("object-api");
                _objectAPI = loadClass(className);
            } else if (_objectImpl != null) _objectAPI = _objectImpl.getClass();
            _homeSkeleton = new HessianSkeleton(_homeImpl, _homeAPI);
            if (_objectAPI != null) _homeSkeleton.setObjectClass(_objectAPI);
            if (_objectImpl != null) {
                _objectSkeleton = new HessianSkeleton(_objectImpl, _objectAPI);
                _objectSkeleton.setHomeClass(_homeAPI);
            } else _objectSkeleton = _homeSkeleton;
        } catch (ServletException e) {
            throw e;
        } catch (Exception e) {
            throw new ServletException(e);
        }
        try {
            String wiringEngineClassName = getInitParameter("wiring-engine");
            Class wiringEngineClass = loadClass(wiringEngineClassName);
            Object wiringEngineObject = wiringEngineClass.newInstance();
            if (wiringEngineObject instanceof WiringEngine) {
                if (_homeImpl instanceof WiredService) {
                    WiringEngine wiringEngine = (WiringEngine) wiringEngineObject;
                    boolean transactionRunning = wiringEngine.getServiceAccessor().getPersistService().isTransactionActive();
                    if (!transactionRunning) {
                        wiringEngine.getServiceAccessor().getPersistService().startTransaction();
                    } else {
                        log.warn("A transaction was already running when service " + _homeImpl.getClass().getName() + " is about to be initialized");
                    }
                    ((WiredService) _homeImpl).init(wiringEngine);
                    if (!transactionRunning) {
                        wiringEngine.getServiceAccessor().getPersistService().commitTransaction();
                    }
                    log.info("Wired Service " + _homeImpl.getClass().getName() + " initialized.");
                } else {
                    log.warn("home-api was not a wired service");
                }
            } else {
                log.error("wiring-engine was not instance of WiringEngine");
            }
        } catch (Exception e) {
            throw new ServletException("Error initializing wired service with wiring engine", e);
        }
    }

    private Class findRemoteAPI(Class implClass) {
        if (implClass == null || implClass.equals(GenericService.class)) return null;
        Class[] interfaces = implClass.getInterfaces();
        if (interfaces.length == 1) return interfaces[0];
        return findRemoteAPI(implClass.getSuperclass());
    }

    private Class loadClass(String className) throws ClassNotFoundException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader != null) {
            return Class.forName(className, false, loader);
        }
        return Class.forName(className);
    }

    private void init(Object service) throws ServletException {
        if (service instanceof Service) ((Service) service).init(getServletConfig()); else if (service instanceof Servlet) ((Servlet) service).init(getServletConfig());
    }

    @Override
    public SerializerFactory getSerializerFactory() {
        if (_serializerFactory == null) {
            _serializerFactory = new SerializerFactory();
            _serializerFactory.addFactory(new EnumSerializerFactory());
            _serializerFactory.addFactory(new MethodSerializerFactory());
            _serializerFactory.addFactory(new TransientTypeSerializerFactory());
            _serializerFactory.addFactory(new UrlSerializerFactory());
        }
        return _serializerFactory;
    }

    /**
	 * Execute a request. The path-info of the request selects the bean. Once
	 * the bean's selected, it will be applied.
	 */
    @SuppressWarnings("deprecation")
    @Override
    public void service(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        if (!req.getMethod().equals("POST")) {
            res.setStatus(500, "Hessian Requires POST");
            PrintWriter out = res.getWriter();
            res.setContentType("text/html");
            out.println("<h1>Hessian Requires POST</h1>");
            return;
        }
        String serviceId = req.getPathInfo();
        String objectId = req.getParameter("id");
        if (objectId == null) objectId = req.getParameter("ejbid");
        ServiceContext.begin(req, serviceId, objectId);
        long time = System.currentTimeMillis();
        HessianInput in = null;
        try {
            OutputStream os = response.getOutputStream();
            in = new HessianInput(request.getInputStream());
            in.setSerializerFactory(getSerializerFactory());
            HessianOutput out = new HessianOutput();
            out.setSerializerFactory(getSerializerFactory());
            out.init(os);
            if (objectId != null) {
                _objectSkeleton.invoke(in, out);
            } else {
                _homeSkeleton.invoke(in, out);
            }
        } catch (InvocationTargetException e) {
            log.error(e.getTargetException());
            throw new RemoteServiceException(e.getTargetException());
        } catch (RuntimeException e) {
            log.error(e);
            throw e;
        } catch (ServletException e) {
            log.error(e);
            throw e;
        } catch (NoClassDefFoundError ncdfe) {
            ncdfe.printStackTrace();
        } catch (Throwable e) {
            log.error(e);
            throw new RemoteServiceException(e);
        } finally {
            ServiceContext.end();
            String methodName = (null == in) ? "EMPTY" : in.getMethod();
            log.debug(methodName + " finished from " + request.getRemoteAddr() + " in " + (System.currentTimeMillis() - time) + "ms");
        }
    }
}
