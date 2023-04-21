package net.sourceforge.xconf.toolbox.spring.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractCommandController;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

/**
 * This controller uses commands like WebWork/Maverick-style prototypes that get populated with request parameters, 
 * executed to determine a view, and thrown away afterwards. The commands can be simple classes created via reflection
 * or more complex (non-singleton) beans retrieved from an application context. The method to be executed on the
 * command should be public, no-arg and should return an {@link ActionResult} instance.
 * 
 * @author Tom Czarniecki
 * 
 * @see Action
 * @see ActionResult
 */
public class ActionCommandController extends AbstractCommandController {

    /** 
     * Default method that will be invoked on any command object (ie. action) used by this controller.
     */
    public static final String ACTION_METHOD_NAME = "execute";

    private BinderRegistrar binderRegistrar;

    private MethodNameResolver commandBeanNameResolver;

    private MethodNameResolver commandMethodNameResolver = new StaticMethodNameResolver(ACTION_METHOD_NAME);

    private String commandMethodMissingViewName;

    public void setBinderRegistrar(BinderRegistrar registrar) {
        this.binderRegistrar = registrar;
    }

    public void setCommandBeanName(String beanName) {
        setCommandBeanNameResolver(new StaticMethodNameResolver(beanName));
    }

    public void setCommandBeanNameResolver(MethodNameResolver resolver) {
        this.commandBeanNameResolver = resolver;
    }

    public void setCommandMethodName(String methodName) {
        setCommandMethodNameResolver(new StaticMethodNameResolver(methodName));
    }

    public void setCommandMethodNameResolver(MethodNameResolver resolver) {
        this.commandMethodNameResolver = resolver;
    }

    public void setCommandMethodMissingViewName(String viewName) {
        this.commandMethodMissingViewName = viewName;
    }

    /**
     * Delegates to the configured {@link BinderRegistrar} if present.
     */
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        if (binderRegistrar != null) {
            binderRegistrar.initBinder(request, binder);
        }
    }

    /**
     * Delegates to the configured command bean name resolver (and retrieves the bean from the
     * current application context) if present, otherwise creates a command using the default
     * superclass implementation.
     * 
     * @see #setCommandBeanNameResolver(MethodNameResolver)
     */
    protected Object getCommand(HttpServletRequest request) throws Exception {
        if (commandBeanNameResolver != null) {
            String beanName = commandBeanNameResolver.getHandlerMethodName(request);
            return getApplicationContext().getBean(beanName);
        }
        return super.getCommand(request);
    }

    /**
     * Injects all aware setters, checks for binding and validation errors, uses the configured
     * command method name resolver to retrieve the name of the action method to invoke, then
     * invokes the action method and uses the returned ActionResult to create a ModelAndView.
     */
    protected ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        processAwareSetters(command, request, response, errors);
        if (errors.hasErrors() && (command instanceof ErrorsHandler)) {
            ActionResult result = ((ErrorsHandler) command).errorsResult();
            return createModelAndView(result, errors);
        }
        try {
            String methodName = commandMethodNameResolver.getHandlerMethodName(request);
            ActionResult result = invokeAction(command, methodName);
            return createModelAndView(result, errors);
        } catch (NoSuchRequestHandlingMethodException e) {
            return handleMethodMissing(request, response, e);
        }
    }

    protected void processAwareSetters(Object command, HttpServletRequest request, HttpServletResponse response, BindException errors) {
        if (command instanceof RequestAware) {
            ((RequestAware) command).setRequest(request);
        }
        if (command instanceof ResponseAware) {
            ((ResponseAware) command).setResponse(response);
        }
        if (command instanceof ErrorsAware) {
            ((ErrorsAware) command).setErrors(errors);
        }
    }

    protected ActionResult invokeAction(Object command, String methodName) throws Exception {
        try {
            Method method = command.getClass().getMethod(methodName, new Class[0]);
            if (!method.getReturnType().equals(ActionResult.class)) {
                throw new NoSuchRequestHandlingMethodException(methodName, command.getClass());
            }
            return (ActionResult) method.invoke(command, new Object[0]);
        } catch (NoSuchMethodException e) {
            throw new NoSuchRequestHandlingMethodException(methodName, command.getClass());
        } catch (InvocationTargetException e) {
            Throwable target = e.getTargetException();
            if (target instanceof Error) {
                throw (Error) target;
            }
            throw (Exception) target;
        }
    }

    /**
     * @see ActionResult#isAppendErrors()
     */
    protected ModelAndView createModelAndView(ActionResult result, BindException errors) {
        if (result == null) {
            return null;
        }
        Map model = new HashMap();
        model.putAll(result.getModel());
        if (result.isAppendErrors()) {
            model.putAll(errors.getModel());
        }
        return new ModelAndView(result.getViewName(), model);
    }

    /**
     * Handler invoked when unable to resolve an action method. If the {@link #setCommandMethodMissingViewName(String)}
     * has been provided, this method will return a ModelAndView to display the given view, otherwise this method
     * sends a 404 error message back to the browser.
     * 
     * @see HttpServletResponse#SC_NOT_FOUND
     */
    protected ModelAndView handleMethodMissing(HttpServletRequest request, HttpServletResponse response, NoSuchRequestHandlingMethodException error) throws Exception {
        logger.warn("Unable to find a method that returns an ActionResult", error);
        if (commandMethodMissingViewName != null) {
            return new ModelAndView(commandMethodMissingViewName);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }
}
