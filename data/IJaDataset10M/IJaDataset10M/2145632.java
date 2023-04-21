package com.dyuproject.web.rest.service;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import org.codehaus.jra.HttpResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dyuproject.util.Delim;
import com.dyuproject.web.rest.ConsumerInterceptor;
import com.dyuproject.web.rest.Interceptor;
import com.dyuproject.web.rest.RequestContext;
import com.dyuproject.web.rest.ValidatingConsumer;
import com.dyuproject.web.rest.WebContext;
import com.dyuproject.web.rest.annotation.Consume;

/**
 * The application context using REST services and resources
 * 
 * @author David Yu
 * @created Dec 4, 2008
 */
public final class RESTServiceContext extends WebContext {

    private static final Logger log = LoggerFactory.getLogger(RESTServiceContext.class);

    private final PathHandler _pathHandler = new PathHandler();

    private final List<Service> _services = new ArrayList<Service>();

    private final List<Resource> _resources = new ArrayList<Resource>();

    private final Map<String, Interceptor> _interceptors = new HashMap<String, Interceptor>();

    public void addService(Service service) {
        if (isInitialized()) throw new IllegalStateException("already initialized");
        _services.add(service);
    }

    public void setServices(Service[] services) {
        if (isInitialized()) throw new IllegalStateException("already initialized");
        for (Service s : services) _services.add(s);
    }

    public void setServices(List<Service> services) {
        if (isInitialized()) throw new IllegalStateException("already initialized");
        for (Service s : services) _services.add(s);
    }

    public void addResource(String path, Resource resource) {
        if (isInitialized()) throw new IllegalStateException("already initialized");
        mapResource(path, resource);
    }

    public void setResources(Map<String, Resource> resources) {
        if (isInitialized()) throw new IllegalStateException("already initialized");
        for (Map.Entry<String, Resource> entry : resources.entrySet()) mapResource(entry.getKey(), entry.getValue());
    }

    public void addInterceptor(String path, Interceptor interceptor) {
        if (isInitialized()) throw new IllegalStateException("already initialized");
        _interceptors.put(path, interceptor);
    }

    public void setInterceptors(Map<String, Interceptor> interceptors) {
        if (isInitialized()) throw new IllegalStateException("already initialized");
        _interceptors.putAll(interceptors);
    }

    protected void init() {
        for (Service s : _services) initService(s);
        for (Resource r : _resources) initResource(r);
        for (Iterator<Map.Entry<String, Interceptor>> iter = _interceptors.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<String, Interceptor> entry = iter.next();
            if (!_pathHandler.map(entry.getKey(), entry.getValue())) iter.remove();
        }
        _pathHandler.init(this);
        log.info(_services.size() + " services initialized.");
        log.info(_resources.size() + " resources initialized.");
        log.info(_interceptors.size() + " configured interceptors initialized.");
    }

    protected void destroy() {
        for (Service s : _services) s.destroy(this);
        for (Resource r : _resources) r.destroy(this);
        _pathHandler.destroy(this);
        log.info(_services.size() + " services destroyed.");
        log.info(_resources.size() + " resources destroyed.");
        log.info(_interceptors.size() + " configured interceptors destroyed.");
        _services.clear();
        _resources.clear();
        _interceptors.clear();
    }

    private PathHandler mapResource(String path, Resource resource) {
        PathHandler ph = _pathHandler.map(path, resource);
        if (ph != null) _resources.add(resource);
        return ph;
    }

    private void initResource(Resource resource) {
        resource.init(this);
    }

    private void initService(Service service) {
        for (Method m : service.getClass().getMethods()) {
            if (Modifier.isStatic(m.getModifiers()) || m.getDeclaringClass() == Object.class) continue;
            String location = null;
            String httpMethod = null;
            Annotation[] annotations = m.getDeclaredAnnotations();
            Consume c = null;
            if (annotations != null) {
                for (Annotation a : annotations) {
                    if (a instanceof HttpResource) {
                        if (location != null) throw new IllegalStateException("multiple declared HttpResource annotations");
                        location = ((HttpResource) a).location();
                        continue;
                    }
                    if (a instanceof Consume) {
                        if (c != null) throw new IllegalStateException("multiple declared Consume annotations");
                        c = (Consume) a;
                        continue;
                    }
                    String method = AnnotatedMethodResource.getHttpMethod(a.annotationType());
                    if (httpMethod != null) {
                        if (method != null) throw new IllegalStateException("multiple declared Http method annotations");
                    } else httpMethod = method;
                }
            }
            if (location == null) continue;
            if (httpMethod == null) {
                log.warn(location + " not mapped.  Http method annotation is required");
                continue;
            }
            int len = m.getParameterTypes().length;
            if (len == 0 || (len == 1 && RequestContext.class.isAssignableFrom(m.getParameterTypes()[0]))) {
                PathHandler ph = mapResource(location, new AnnotatedMethodResource(service, m, httpMethod));
                if (c != null && ph != null) mapConsumer(location, httpMethod, ph, c);
                continue;
            }
            log.warn(location + " not mapped.  THe annotated method's only argument must be RequestContext or can also have no args.");
        }
        service.init(this);
    }

    protected void mapConsumer(String location, String httpMethod, PathHandler ph, Consume c) {
        Class<?> pojoClass = c.pojoClass();
        Class<?>[] consumers = c.consumers();
        if (pojoClass == null) {
            log.warn("consumer @ " + location + " excluded. Param pojoClass is required.");
            return;
        }
        if (consumers == null || consumers.length == 0) {
            log.warn("consumer @ " + location + " excluded. Param consumers is required.");
            return;
        }
        ConsumerInterceptor ci = new ConsumerInterceptor();
        Properties fieldParams = new Properties();
        loadPropertiesFromClass(fieldParams, pojoClass);
        addParamsToProperties(fieldParams, c.fieldParams());
        for (int i = 0; i < consumers.length; i++) {
            try {
                ValidatingConsumer vc = (ValidatingConsumer) consumers[i].newInstance();
                vc.preConfigure(httpMethod, c.contentType(), pojoClass, fieldParams);
                ci.addConsumer(vc);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        ph.addMappedInterceptor(ci, 0);
    }

    @SuppressWarnings("unchecked")
    public void loadPropertiesFromClass(Properties props, Class<?> clazz) {
        Map<Class<?>, Properties> cache = (Map<Class<?>, Properties>) getAttribute(CONSUMER_PROPERTIES_CACHE);
        if (cache == null) {
            cache = new HashMap<Class<?>, Properties>(7);
            setAttribute(CONSUMER_PROPERTIES_CACHE, cache);
        }
        Properties defaultProps = cache.get(clazz);
        if (defaultProps == null) {
            defaultProps = new Properties();
            cache.put(clazz, defaultProps);
            String resource = clazz.getName().replace('.', '/') + ".properties";
            URL url = getResource(resource);
            if (url != null) {
                try {
                    defaultProps.load(url.openStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (defaultProps.size() != 0) props.putAll(defaultProps);
    }

    static void addParamsToProperties(Properties props, String params) {
        if (params == null || params.length() == 0) return;
        String[] tokens = Delim.AMPER.split(params);
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            int idx = token.indexOf('=');
            if (idx == -1) {
                if (token.length() > 0) props.put(token, "");
            } else if (idx != 0) props.setProperty(token.substring(0, idx), token.substring(idx + 1));
        }
    }

    protected void preConfigure(ServletConfig config) throws Exception {
        String servicesParam = config.getInitParameter("services");
        if (servicesParam != null) {
            StringTokenizer tokenizer = new StringTokenizer(servicesParam, ",;");
            while (tokenizer.hasMoreTokens()) addService((Service) newObjectInstance(tokenizer.nextToken().trim()));
        }
        String resourcesParam = config.getInitParameter("resources");
        if (resourcesParam != null) {
            StringTokenizer tokenizer = new StringTokenizer(resourcesParam, ",;");
            while (tokenizer.hasMoreTokens()) {
                String next = tokenizer.nextToken();
                int idx = next.indexOf('@');
                if (idx == -1) {
                    log.warn("invalid resource mapping: " + next);
                    continue;
                }
                String resourceClass = next.substring(0, idx).trim();
                String path = next.substring(idx + 1).trim();
                addResource(path, (Resource) newObjectInstance(resourceClass));
            }
        }
        String interceptorsParam = config.getInitParameter("interceptors");
        if (interceptorsParam != null) {
            StringTokenizer tokenizer = new StringTokenizer(interceptorsParam, ",;");
            while (tokenizer.hasMoreTokens()) {
                String next = tokenizer.nextToken();
                int idx = next.indexOf('@');
                if (idx == -1) {
                    log.warn("invalid interceptor mapping: " + next);
                    continue;
                }
                String interceptorClass = next.substring(0, idx).trim();
                String path = next.substring(idx + 1).trim();
                addInterceptor(path, (Interceptor) newObjectInstance(interceptorClass));
            }
        }
    }

    protected void handleRoot(RequestContext requestContext) throws ServletException, IOException {
        _pathHandler.resourceHandle(requestContext);
    }

    protected void handlePath(RequestContext requestContext) throws ServletException, IOException {
        _pathHandler.handle(requestContext.getPathInfo(), requestContext);
    }
}
