package com.fourspaces.scratch.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.fourspaces.scratch.mapping.MethodArgument.Source;
import com.fourspaces.scratch.mapping.annotation.DefaultValue;
import com.fourspaces.scratch.mapping.annotation.HttpRequestAsParam;
import com.fourspaces.scratch.mapping.annotation.HttpResponseAsParam;
import com.fourspaces.scratch.mapping.annotation.HttpSessionAsParam;
import com.fourspaces.scratch.mapping.annotation.Include;
import com.fourspaces.scratch.mapping.annotation.Layout;
import com.fourspaces.scratch.mapping.annotation.Layouts;
import com.fourspaces.scratch.mapping.annotation.Parameter;
import com.fourspaces.scratch.mapping.annotation.PathParam;
import com.fourspaces.scratch.mapping.annotation.ReqParam;
import com.fourspaces.scratch.mapping.annotation.ServletContextAsParam;
import com.fourspaces.scratch.mapping.annotation.ServletParam;
import com.fourspaces.scratch.mapping.annotation.SessionParam;
import com.fourspaces.scratch.result.Dispatch;
import com.fourspaces.scratch.result.Forward;
import com.fourspaces.scratch.result.HTML;
import com.fourspaces.scratch.result.LayoutResult;
import com.fourspaces.scratch.result.Result;
import com.fourspaces.scratch.security.SecurityHandler;
import com.fourspaces.scratch.security.SecurityHandlerFactory;
import com.fourspaces.scratch.security.annotation.Security;
import com.fourspaces.scratch.util.ParameterUtils;
import com.fourspaces.scratch.util.RequestUtils;
import com.fourspaces.scratch.validation.ValidationHandler;
import com.fourspaces.scratch.validation.ValidatorFactory;
import com.fourspaces.scratch.validation.annotation.Validate;
import com.fourspaces.scratch.validation.annotation.Validation;
import com.fourspaces.scratch.validation.annotation.ValidationErrorPath;

/**
 * <p>
 * ControllerMapping handles all aspects of invoking a Method to handle a
 * request. The ControllerMapping is retrieved from a Registry.  This class looks up a method's
 * Layout, Validation, and Security annotations to properly handle each request.
 * <p>
 * This class must be subclasses to include an implementation of
 * "getInstance()". The instance can be a singleton, a Spring-managed bean,
 * etc...
 * <p>
 * There is a ControllerMapping for each Controller+Method combination. The
 * ControllerMapping then can handle all aspects of injecting the method's
 * parameters.
 * 
 * @author mbreese
 * 
 */
public abstract class ControllerMapping {

    protected final Class<?> clazz;

    protected final Method method;

    protected final MethodArgument[] arguments;

    protected final Log log = LogFactory.getLog(ControllerMapping.class);

    private final Map<String, LayoutMapping> layouts;

    private final SecurityHandler[] securityHandlers;

    private String securityFailPath = null;

    private String securityFailLayout = null;

    private Map<String, String> securityFailParameters = null;

    private Map<String, String> securityFailIncludes = null;

    private final Validate[] validations;

    private final ValidationErrorPath validationErrorPath;

    private final HttpMethod httpMethod;

    private final String parameterFilterName;

    private final String parameterFilterValue;

    /**
	 * Build a ControllerMapping based upon a class and method.
	 * 
	 * @param clazz
	 * @param method
	 */
    public ControllerMapping(Class<?> clazz, Method method) {
        this.clazz = clazz;
        this.method = method;
        this.arguments = loadArguments();
        this.layouts = loadLayouts();
        this.securityHandlers = loadSecurityHandlers();
        this.validations = loadValidations();
        this.validationErrorPath = loadValidationErrorPath();
        this.httpMethod = loadHttpMethod();
        String[] param = loadParameterFilter();
        this.parameterFilterName = param[0];
        this.parameterFilterValue = param[1];
    }

    protected HttpMethod loadHttpMethod() {
        HttpMethod httpMethod = HttpMethod.ALL;
        com.fourspaces.scratch.mapping.annotation.Method methAnnotation = method.getAnnotation(com.fourspaces.scratch.mapping.annotation.Method.class);
        if (methAnnotation != null) {
            httpMethod = methAnnotation.value();
        }
        return httpMethod;
    }

    /**
	 * 
	 * @return String[] 0 = name, 1 = value
	 */
    protected String[] loadParameterFilter() {
        String[] param = new String[2];
        param[0] = null;
        param[1] = null;
        ReqParam reqParam = method.getAnnotation(ReqParam.class);
        if (reqParam != null) {
            String nameval = reqParam.value();
            param = nameval.split("=");
            log.debug("ReqParam: " + nameval + " [" + param[0] + "=" + param[1] + "]");
        }
        return param;
    }

    protected Validate[] loadValidations() {
        Validation v = method.getAnnotation(Validation.class);
        if (v != null) {
            for (Validate val : v.value()) {
                String s = "";
                for (String f : val.field()) {
                    if (!s.equals("")) {
                        s += ", ";
                    }
                    s += f;
                }
                log.debug("Adding validation: " + val.handler() + " for " + s);
            }
            return v.value();
        }
        return null;
    }

    protected ValidationErrorPath loadValidationErrorPath() {
        ValidationErrorPath vep = method.getAnnotation(ValidationErrorPath.class);
        if (vep != null) {
            return vep;
        }
        return null;
    }

    /**
	 * Loads security handler settings from the Class and the Method.
	 * There can be any number of Security Handlers for a Class or Method.  If a Security Handler returns false, then
	 * the request is automatically forwarded to the the configured path or layout.
	 * 
	 * @return
	 */
    @SuppressWarnings("unchecked")
    protected SecurityHandler[] loadSecurityHandlers() {
        List<SecurityHandler> secHandlerList = new ArrayList<SecurityHandler>();
        Security secAnnController = (Security) clazz.getAnnotation(Security.class);
        Security secAnnAction = method.getAnnotation(Security.class);
        if (secAnnController != null) {
            Class<? extends SecurityHandler>[] classes = secAnnController.handlers();
            for (Class<? extends SecurityHandler> clazz : classes) {
                secHandlerList.add(SecurityHandlerFactory.getSecurityHandler(clazz));
            }
            securityFailPath = secAnnController.failPath();
            securityFailLayout = secAnnController.failLayout();
            if (securityFailLayout != null && !securityFailLayout.equals("")) {
                securityFailIncludes = new HashMap<String, String>();
                securityFailParameters = new HashMap<String, String>();
                if (secAnnController.failIncludes().length > 0) {
                    for (Include inc : secAnnController.failIncludes()) {
                        securityFailIncludes.put(inc.name(), inc.include());
                    }
                }
                if (secAnnController.failParameters().length > 0) {
                    for (Parameter inc : secAnnController.failParameters()) {
                        securityFailParameters.put(inc.name(), inc.value());
                    }
                }
            }
        }
        if (secAnnAction != null) {
            Class<? extends SecurityHandler>[] classes = secAnnAction.handlers();
            for (Class<? extends SecurityHandler> clazz : classes) {
                secHandlerList.add(SecurityHandlerFactory.getSecurityHandler(clazz));
            }
            securityFailPath = secAnnAction.failPath();
            securityFailLayout = secAnnAction.failLayout();
            if (securityFailLayout != null && !securityFailLayout.equals("")) {
                securityFailIncludes = new HashMap<String, String>();
                securityFailParameters = new HashMap<String, String>();
                if (secAnnAction.failIncludes().length > 0) {
                    for (Include inc : secAnnAction.failIncludes()) {
                        securityFailIncludes.put(inc.name(), inc.include());
                    }
                }
                if (secAnnAction.failParameters().length > 0) {
                    for (Parameter inc : secAnnAction.failParameters()) {
                        securityFailParameters.put(inc.name(), inc.value());
                    }
                }
            }
        }
        return secHandlerList.toArray(new SecurityHandler[0]);
    }

    @SuppressWarnings("unchecked")
    protected Map<String, LayoutMapping> loadLayouts() {
        Map<String, LayoutMapping> layouts = new HashMap<String, LayoutMapping>();
        Layouts classLayouts = (Layouts) clazz.getAnnotation(Layouts.class);
        if (classLayouts != null && classLayouts.value() != null) {
            for (Layout layout : classLayouts.value()) {
                layouts.put(layout.name(), new LayoutMapping(layout));
                log.debug(layouts.get(layout.name()));
            }
        }
        Layouts actionLayouts = method.getAnnotation(Layouts.class);
        if (actionLayouts != null && actionLayouts.value() != null) {
            for (Layout layout : actionLayouts.value()) {
                layouts.put(layout.name(), new LayoutMapping(layout));
                log.debug(layouts.get(layout.name()));
            }
        }
        return layouts;
    }

    protected MethodArgument[] loadArguments() {
        Class<?>[] types = method.getParameterTypes();
        Annotation[][] annotations = method.getParameterAnnotations();
        MethodArgument[] arguments = new MethodArgument[types.length];
        log.debug("Looking for annotated method arguments");
        for (int i = 0; i < types.length; i++) {
            Class<?> type = types[i];
            Source source = null;
            String value = null;
            String defValue = null;
            int group = -1;
            log.debug("Checking argument #" + i + " of type: " + type);
            for (Annotation a : annotations[i]) {
                if (a.annotationType().equals(SessionParam.class)) {
                    source = Source.SESSION;
                    value = ((SessionParam) a).value();
                } else if (a.annotationType().equals(PathParam.class)) {
                    source = Source.PATH;
                    group = ((PathParam) a).value();
                } else if (a.annotationType().equals(ReqParam.class)) {
                    source = Source.REQUEST;
                    value = ((ReqParam) a).value();
                } else if (a.annotationType().equals(ServletParam.class)) {
                    source = Source.SERVLET;
                    value = ((ServletParam) a).value();
                } else if (a.annotationType().equals(HttpRequestAsParam.class)) {
                    source = Source.HTTPREQUEST;
                } else if (a.annotationType().equals(HttpResponseAsParam.class)) {
                    source = Source.HTTPRESPONSE;
                } else if (a.annotationType().equals(HttpSessionAsParam.class)) {
                    source = Source.HTTPSESSION;
                } else if (a.annotationType().equals(ServletContextAsParam.class)) {
                    source = Source.SERVLETCONTEXT;
                } else if (a.annotationType().equals(DefaultValue.class)) {
                    defValue = ((DefaultValue) a).value();
                }
            }
            if (source != null) {
                log.debug("Value source: " + source);
                if (source == Source.PATH && group > -1) {
                    arguments[i] = new MethodArgument(type, source, group, defValue);
                    log.debug("group: " + group);
                } else if (source == Source.REQUEST || source == Source.SESSION) {
                    arguments[i] = new MethodArgument(type, source, value, defValue);
                    log.debug("key: " + value);
                } else if (source == Source.HTTPREQUEST || source == Source.HTTPSESSION || source == Source.HTTPRESPONSE || source == Source.SERVLETCONTEXT) {
                    arguments[i] = new MethodArgument(type, source);
                } else {
                    log.error("Error mapping method argument #" + i + " of type: " + type);
                }
            } else {
                log.error("Missing annotation for argument #" + i + " of type: " + type);
            }
        }
        return arguments;
    }

    /**
	 * Invokes a method, using the request and matched regex groups to find the
	 * parameter values used to call the method.
	 * <p>
	 * Retrieves the Object instance used to call the method from getInstance();
	 * 
	 * @param request
	 * @param matchedPathGroups
	 * @return
	 */
    public Result invoke(HttpServletRequest request, HttpServletResponse response, String[] matchedPathGroups) {
        boolean securityCheck = true;
        boolean validationCheck = true;
        if (securityHandlers != null) {
            for (SecurityHandler securityHandler : securityHandlers) {
                if (!securityHandler.check(request)) {
                    securityCheck = false;
                    log.debug("Security check failed: " + securityHandler.getClass().getName() + " on " + matchedPathGroups[0]);
                    RequestUtils.addError(request, securityHandler.getErrorMessage());
                }
            }
        }
        if (securityCheck && validations != null) {
            for (Validate val : validations) {
                ValidationHandler validationHandler = ValidatorFactory.getValidator(val.handler());
                if (!validationHandler.validate(request, val.field())) {
                    validationCheck = false;
                    RequestUtils.addError(request, validationHandler.errorMessage(request, val.field(), val.errorMessage()));
                    log.debug("Validation failed: " + validationHandler.errorMessage(request, val.field(), val.errorMessage()));
                }
            }
        }
        if (securityCheck && validationCheck) {
            Result result = null;
            Object obj = getInstance();
            Object[] args = buildMethodArguments(request, response, matchedPathGroups);
            try {
                result = (Result) method.invoke(obj, args);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return result;
        } else if (!securityCheck) {
            log.debug("Processing security check failing");
            Result r = null;
            if (securityFailPath != null && !securityFailPath.equals("")) {
                log.debug("Fail path: " + securityFailPath);
                r = new Forward(securityFailPath);
            } else if (securityFailLayout != null && !securityFailLayout.equals("")) {
                log.debug("Fail layout: " + securityFailLayout);
                r = new LayoutResult(securityFailLayout);
                log.debug("Adding includes...");
                for (String k : securityFailIncludes.keySet()) {
                    log.debug(k + " => " + securityFailIncludes.get(k));
                    ((LayoutResult) r).addInclude(k, securityFailIncludes.get(k));
                }
                log.debug("Adding parameters...");
                for (String k : securityFailParameters.keySet()) {
                    log.debug(k + " => " + securityFailParameters.get(k));
                    ((LayoutResult) r).addParameter(k, securityFailParameters.get(k));
                }
                return r;
            } else {
                log.debug("Unknown Error path/layout - " + matchedPathGroups[0]);
                r = new HTML("<html><head><title>Security Error!</title></head><body><h1>Security error</h1>There was a security error with your request: " + RequestUtils.getErrorsAsString(request) + " on " + matchedPathGroups[0] + "</body></html>");
            }
            r.markError();
            return r;
        } else if (!validationCheck) {
            if (validationErrorPath != null) {
                return new Dispatch(validationErrorPath.value()).markError();
            } else {
                String s = "<html><head></head><body>There has been a validation error!<br/><br/><ul>";
                for (String e : RequestUtils.getErrors(request)) {
                    s += "<li>" + e + "</li>";
                }
                s += "</ul></body></html>";
                return new HTML(s).markError();
            }
        } else {
            return null;
        }
    }

    /**
	 * Builds an array of argument values to pass to the method. This handles
	 * converting String values in the Request and Path elements to Integers,
	 * Longs, etc...
	 * 
	 * @param request
	 * @param matchedPathGroups
	 * @return
	 */
    protected Object[] buildMethodArguments(HttpServletRequest request, HttpServletResponse response, String[] matchedPathGroups) {
        Object[] argValues = new Object[arguments.length];
        log.debug("building method arguments");
        for (int i = 0; i < arguments.length; i++) {
            MethodArgument marg = arguments[i];
            String value = null;
            log.debug("[" + i + "] " + marg.getType() + " " + marg.getType() + " " + marg.getName() + "/" + marg.getRegexGroup());
            if (marg.getSource() == Source.SESSION) {
                Object obj = request.getSession().getAttribute(marg.getName());
                argValues[i] = obj;
            } else if (marg.getSource() == Source.HTTPSESSION) {
                Object obj = request.getSession();
                argValues[i] = obj;
            } else if (marg.getSource() == Source.HTTPREQUEST) {
                Object obj = request;
                argValues[i] = obj;
            } else if (marg.getSource() == Source.HTTPRESPONSE) {
                Object obj = response;
                argValues[i] = obj;
            } else if (marg.getSource() == Source.SERVLETCONTEXT) {
                Object obj = request.getSession().getServletContext();
                argValues[i] = obj;
            } else {
                if (marg.getSource() == MethodArgument.Source.PATH) {
                    if (marg.getRegexGroup() < matchedPathGroups.length) {
                        value = matchedPathGroups[marg.getRegexGroup()];
                    } else {
                        log.warn("Argument #" + (i + 1) + " in " + method.getName() + " is looking for a missing reg ex group: " + marg.getRegexGroup());
                    }
                } else if (marg.getSource() == MethodArgument.Source.REQUEST) {
                    value = request.getParameter(marg.getName());
                }
                log.debug("value=" + value);
                if (value == null) {
                    value = marg.getDefaultValue();
                    log.debug("value==null ! using default: " + value);
                }
                if (marg.getType().equals(String.class)) {
                    argValues[i] = value;
                } else if (marg.getType().equals(int.class) || marg.getType().equals(Integer.class)) {
                    argValues[i] = ParameterUtils.paramToInteger(value);
                } else if (marg.getType().equals(long.class) || marg.getType().equals(Long.class)) {
                    argValues[i] = ParameterUtils.paramToLong(value);
                } else if (marg.getType().equals(float.class) || marg.getType().equals(Float.class)) {
                    argValues[i] = ParameterUtils.paramToFloat(value);
                } else if (marg.getType().equals(double.class) || marg.getType().equals(Double.class)) {
                    argValues[i] = ParameterUtils.paramToDouble(value);
                } else if (marg.getType().equals(boolean.class) || marg.getType().equals(Boolean.class)) {
                    argValues[i] = ParameterUtils.paramToBoolean(value);
                } else {
                    log.warn("Unmatched conversion for MethodArgument: " + method.getName() + " arg:" + (i + 1) + " requested type:" + marg.getType().getName());
                    argValues[i] = value;
                }
            }
        }
        return argValues;
    }

    /**
	 * Retrieve or Create an instance of the class to service the request.
	 * @return An Instance of the configured class to call the method from.
	 */
    protected abstract Object getInstance();

    public Class<?> getMappedClass() {
        return clazz;
    }

    public Method getMappedMethod() {
        return method;
    }

    /**
	 * Returns the Layouts that have been configured for this action.
	 * 
	 * @return Map of layouts (name => LayoutMapping)
	 */
    public Map<String, LayoutMapping> getLayouts() {
        return layouts;
    }

    public Validate[] getValidations() {
        return validations;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public boolean matchParameterFilter(HttpServletRequest req) {
        if (parameterFilterName == null) {
            return true;
        } else if (parameterFilterValue == null) {
            return (req.getParameter(parameterFilterName) != null);
        } else {
            return req.getParameter(parameterFilterName).equals(parameterFilterValue);
        }
    }
}
