package prest.core;

import static prest.core.util.ValidationUtil.validateNonNullParameter;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import prest.core.Resource.ActionContainer;
import prest.core.annotations.Action;
import prest.core.filters.ActionInvokeFilter;
import prest.core.filters.Filter;
import prest.core.io.Environment;
import prest.core.io.Request;
import prest.core.io.Response;
import prest.core.logger.Logger;
import prest.core.logger.PrestLogger;
import prest.core.reflection.ControllerExplorer;
import prest.core.reflection.PrestReflectionUtils;

/**
 * <p>
 * pREST application
 *
 * <ul>
 * <li>manages the controllers</li>
 * <li>processes the requests (executes the action filter chain)</li>
 * </ul>
 * </p>
 *
 * <p>
 * Usage: create sublclass of the {@link Application}, implement the
 * <code>initialize</code> method, in which you will typicaly mount your
 * <em>controllers</em>.
 *
 * <pre>
 * public class MyApplication extends prest.core.Application {
 * 	public void initialize() throws ApplicationException {
 * 		mount(&quot;/my_controller&quot;, new MyController());
 * 	}
 * }
 * </pre>
 *
 * </p>
 *
 * @author Peter Rybar
 * @author Daniel Buchta
 *
 */
public abstract class Application {

    private static Application instance;

    private static final String DELIMITER = "/";

    /**
	 * The instance getter
	 *
	 * @return the instance (<code>null</code> if the instance hasn't been
	 *         initialized)
	 */
    public static synchronized Application getInstance() {
        return instance;
    }

    /**
	 * Logger getter
	 *
	 * @return the application logger
	 */
    public static Logger logger() {
        return getInstance().getLogger();
    }

    /**
	 * The application path getter
	 *
	 * @return the application path
	 */
    protected static String getApplicationPath() {
        FilterConfig config = Application.getInstance().getFilterConfig();
        return config.getServletContext().getContextPath();
    }

    /**
	 * The instance setter - this method should be called only once
	 *
	 * @param applicationClassName
	 *            name of pREST application class used to created its instance
	 * @throws IllegalStateException
	 *             if the application has been already initialized
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
    @SuppressWarnings("unchecked")
    static synchronized Application setInstance(String applicationClassName) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        if (instance != null) {
            throw new IllegalStateException("Application was already created");
        }
        Class<? extends Application> applicationClass = (Class<? extends Application>) Class.forName(applicationClassName);
        Application.instance = applicationClass.newInstance();
        return instance;
    }

    private Integer maxMultipartContentLength = null;

    private FilterConfig filterConfig;

    private final ControllersMap controllers = new ControllersMap();

    private final ControllersFiller controllersFiller = new ControllersFiller();

    private Logger logger;

    /** Default constructor */
    protected Application() {
    }

    /**
	 * The maxMultipartContentLength getter
	 *
	 * @return the maxMultipartContentLength
	 */
    public Integer getMaxMultipartContentLength() {
        return this.maxMultipartContentLength;
    }

    /**
	 * Mount controllers in this method, set another application parameters.
	 *
	 * @throws ApplicationException
	 */
    public abstract void initialize() throws ApplicationException;

    /**
	 * Mounts the controller instance to the specified location
	 *
	 * @param location
	 *            controller's location, not <code>null</code>
	 * @param controller
	 *            controller to mount, not <code>null</code>
	 * @throws ApplicationException
	 *             if some error occurs while mounting controller
	 * @throws IllegalArgumentException
	 *             if location or controller is <code>null</code>
	 */
    public void mount(String location, Controller controller) throws ApplicationException {
        validateNonNullParameter(location, "location");
        validateNonNullParameter(controller, "controller");
        if (location.endsWith(DELIMITER)) {
            location = location.substring(0, location.length() - 1);
        }
        getLogger().info("Mounting controller '%s' on path '%s'", controller.getClass().getSimpleName(), location);
        getControllersFiller().addController(location, controller);
    }

    /**
	 * Returns a unmodifiable set of mounted locations
	 *
	 * @return a unmodifiable set of mounted locations
	 */
    public Collection<String> mounted() {
        return getModel().getControllersLocations();
    }

    /**
	 * The maxMultipartContentLength setter
	 *
	 * @param maxContentLength
	 *            the maxMultipartContentLength to set
	 */
    public void setMaxMultipartContentLength(Integer maxContentLength) {
        this.maxMultipartContentLength = maxContentLength;
    }

    /**
	 * Umount the controller on the specified location
	 *
	 * @param location
	 *            controller's location, not <code>null</code>
	 * @throws IllegalArgumentException
	 *             if location is <code>null</code> or if the specified location
	 *             doesn't exist
	 */
    public void umount(String location) {
        validateNonNullParameter(location, "location");
        getModel().removeController(location);
    }

    /**
	 * The filterConfig getter
	 *
	 * @return the filterConfig
	 */
    protected FilterConfig getFilterConfig() {
        return this.filterConfig;
    }

    /**
	 * The appLogger getter
	 *
	 * @return the appLogger
	 */
    protected Logger getLogger() {
        return this.logger;
    }

    /**
	 * The logger setter
	 *
	 * @param logger
	 *            the logger to set
	 */
    protected void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
	 * Execute the request
	 *
	 * @param request
	 *            the request object
	 * @param response
	 *            the response object
	 *
	 * @throws NoControllerException
	 *             if the request URL specifies no controller
	 * @throws NoActionException
	 *             if the request URL specifies no controller's action
	 */
    void execute(Request request, Response response) throws NoControllerException, NoActionException {
        String servletPath = request.getRequestURI().substring(request.getContextPath().length());
        PathElements elements = parseRequestUri(servletPath, request.getCharacterEncoding());
        String controllerPath = elements.getController();
        String actionName = elements.getUrlParameters().getSize() > 0 ? elements.getUrlParameters().get(0) : "";
        String httpMethod = request.getHttpMethod();
        getLogger().info("'%s' %s %s %s %s", controllerPath, httpMethod, elements.getUrlParameters(), request.getRequestParameters(), request.getFileRequestParameters());
        ControllerContainer cc = getModel().getController(controllerPath);
        Resource resource = cc.getResource(actionName);
        Resource.ActionContainer ac = resource.getAction(httpMethod);
        if (resource.getName().equals(actionName) && !"".equals(actionName)) {
            assert elements.getUrlParameters().getSize() > 0;
            elements.getUrlParameters().remove(0);
        }
        request.setUrlParameters(elements.getUrlParameters());
        ServletContext servletContext = getFilterConfig().getServletContext();
        String contextPath = request.getContextPath();
        Environment environment = new Environment(request, response, servletContext, this, contextPath, cc.getController(), controllerPath, ac.getMethod(), resource.getName());
        ac.getFilterChain().execute(environment);
    }

    /**
	 * Returns the model of the application - collection of controllerse
	 *
	 * @return the model of the application
	 */
    ControllersMap getModel() {
        return this.controllers;
    }

    /**
	 * The filterConfig setter
	 *
	 * @param filterConfig
	 *            the filterConfig to set, not <code>null</code>
	 */
    void setFilterConfig(FilterConfig filterConfig) {
        validateNonNullParameter(filterConfig, "filterConfig");
        this.filterConfig = filterConfig;
        synchronized (this) {
            if (getLogger() == null) {
                setLogger(new PrestLogger(filterConfig.getServletContext()));
            }
        }
    }

    /**
	 * The actionMapHandler getter
	 *
	 * @return the actionMapHandler
	 */
    private ControllersFiller getControllersFiller() {
        return this.controllersFiller;
    }

    /**
	 * Retrieves the controller's location and url parameters from the specified
	 * servlet path.
	 *
	 * @param servletPath
	 *            the servlet path, can't be <code>null</code>
	 * @param encoding
	 *            the request character encoding
	 * @return the controller's location and url parameters, never returns
	 *         <code>null</code>
	 * @throws NoControllerException
	 *             if the specified servlet path doesn't contains any
	 *             controller's location
	 */
    private PathElements parseRequestUri(String servletPath, String encoding) throws NoControllerException {
        validateNonNullParameter(servletPath, "servletPath");
        StringBuilder path = new StringBuilder(servletPath);
        List<String> uriParameters = new LinkedList<String>();
        if (servletPath.endsWith(DELIMITER)) {
            uriParameters.add("");
            path.deleteCharAt(path.length() - 1);
        }
        Collection<String> locations = getModel().getControllersLocations();
        while (path.length() > 0 && !locations.contains(path.toString())) {
            int lastDelimiter = path.lastIndexOf(DELIMITER);
            String urlParameter = path.substring(lastDelimiter + 1);
            try {
                urlParameter = URLDecoder.decode(urlParameter, encoding);
            } catch (UnsupportedEncodingException e) {
                Application.logger().error("URL prameter decoding", e, e.getMessage());
            }
            uriParameters.add(0, urlParameter);
            path.delete(lastDelimiter, path.length());
        }
        String controller = path.toString();
        if (!locations.contains(controller)) {
            getLogger().trace("No controller found on '%s'", servletPath);
            throw new NoControllerException(servletPath);
        }
        return new PathElements(controller, uriParameters);
    }

    /**
	 * Responsible for adding new controller to application model
	 *
	 * @author Daniel Buchta
	 * @author Peter Rybar
	 *
	 */
    private class ControllersFiller {

        /**
		 * Adds the controller into the controllers map on the specified
		 * location
		 *
		 * @param location
		 *            location, where controller will be added, not
		 *            <code>null</code>
		 * @param controller
		 *            the controller to add, not <code>null</code>
		 */
        public void addController(String location, Controller controller) throws ApplicationException {
            assert location != null && controller != null;
            ControllerExplorer ce = getControllerExplorer(controller.getClass());
            ControllerContainer cc = getModel().addController(location, controller);
            for (Method method : ce.getActions()) {
                Action actionAnnotation = method.getAnnotation(Action.class);
                assert actionAnnotation != null;
                List<String> parameterNames = PrestReflectionUtils.getParameterNames(method);
                for (String httpMethod : actionAnnotation.httpMethod()) {
                    String format = "Exposing action '%s' for HTTP method '%s'";
                    getLogger().debug(format, method, httpMethod);
                    String resourceName = actionAnnotation.name();
                    Resource resource = cc.addOrGetResource(resourceName);
                    addAction(resource, method, ce.getAllAnnotations(method.getName()), parameterNames, httpMethod);
                }
            }
        }

        /**
		 * Creates the action for the specified resource
		 *
		 * @param resource
		 *            resource to add the action for
		 * @param method
		 *            java method
		 * @param parameterNames
		 *            action parameters
		 * @param httpMethod
		 *            HTTP method handled by this action
		 * @throws ApplicationException
		 */
        private void addAction(Resource resource, Method method, List<Annotation> annotations, List<String> parameterNames, String httpMethod) throws ApplicationException {
            ActionContainer ac = resource.addAction(httpMethod, method, parameterNames);
            ac.addAnnotationFilter(annotations);
            Filter filter = new ActionInvokeFilter(parameterNames);
            ac.addFilter(filter);
        }

        /**
		 * Created the instance of {@link ControllerExplorer} assiociated with
		 * the specifed controller class
		 *
		 * @param controllerClass
		 *            class of controller used to create controller explorer
		 * @return instance of {@link ControllerExplorer} assiociated with the
		 *         specifed controller class
		 * @throws ApplicationException
		 *             if the specified controller class is not valid
		 */
        private ControllerExplorer getControllerExplorer(Class<? extends Controller> controllerClass) throws ApplicationException {
            assert controllerClass != null;
            ControllerExplorer ce;
            try {
                ce = ControllerExplorer.getInstance(controllerClass);
            } catch (InvalidControllerException e) {
                throw new ApplicationException(e);
            }
            return ce;
        }
    }
}
