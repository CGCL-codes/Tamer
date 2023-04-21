package com.xmog.web.annotation.processing;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import com.xmog.web.annotation.CustomBindingErrorHandler;
import com.xmog.web.annotation.Handles;
import com.xmog.web.annotation.UrlParameter;
import com.xmog.web.handler.UrlHandler;
import com.xmog.web.util.ClassDetectionException;
import com.xmog.web.util.ReflectionUtils;

/**
 * Given a package name, finds all classes in the package which contain Xmog Web
 * annotations. Provides, via {@link #getHandlerMap()}, a mapping of URLs to
 * methods for invocation.
 * 
 * @author <a href="mailto:maa@xmog.com">Mark Allen</a>
 */
@SuppressWarnings("unchecked")
public class AnnotationProcessor {

    private Map<String, UrlHandler> handlerMap = new HashMap<String, UrlHandler>();

    private static final Logger log = Logger.getLogger(AnnotationProcessor.class);

    public AnnotationProcessor(String basePackage) {
        Set<Class> classes = new HashSet<Class>();
        if (basePackage == null || "".equals(basePackage.trim())) {
            throw new AnnotationConfigurationException("You must define a basePackage to scan for annotations.");
        }
        try {
            classes = ReflectionUtils.findClassesIn(basePackage);
        } catch (ClassDetectionException e) {
            throw new AnnotationConfigurationException("Unable to process classes in package '" + basePackage + "'.", e);
        }
        if (classes.size() == 0) {
            log.warn("No classes were found in package '" + basePackage + "'.");
        }
        for (Class clazz : classes) {
            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(Handles.class)) {
                    addToHandlerMap(method);
                } else {
                    if (method.isAnnotationPresent(CustomBindingErrorHandler.class)) {
                        log.warn(ReflectionUtils.methodToString(method) + " is missing a @Handles annotation, ignoring.");
                    } else {
                        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                        for (int i = 0; i < parameterAnnotations.length; i++) {
                            for (int j = 0; j < parameterAnnotations[i].length; j++) {
                                if (parameterAnnotations[i][j].annotationType().equals(UrlParameter.class)) {
                                    log.warn("A parameter in " + ReflectionUtils.methodToString(method) + " has a @UrlParameter annotation, but the method " + "is missing a @Handles annotation, ignoring.");
                                }
                            }
                        }
                    }
                }
            }
        }
        if (handlerMap.size() == 0) {
            log.warn("No annotated classes were found in package '" + basePackage + "'.");
        } else if (log.isDebugEnabled()) {
            log.debug(toString());
        }
    }

    public Map<String, UrlHandler> getHandlerMap() {
        return Collections.unmodifiableMap(handlerMap);
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        int mappings = handlerMap.size();
        if (mappings == 0) {
            return "No mappings have been defined.";
        }
        int i = 0;
        StringBuffer buffer = new StringBuffer();
        buffer.append("Detected URL Mappings:\n");
        for (String url : handlerMap.keySet()) {
            buffer.append(url);
            buffer.append(" -> ");
            buffer.append(handlerMap.get(url));
            if (++i < mappings) {
                buffer.append("\n");
            }
        }
        return buffer.toString();
    }

    private void addToHandlerMap(Method method) {
        String value = method.getAnnotation(Handles.class).value();
        if (value == null) {
            warnAboutBadAnnotation("is null", method);
            return;
        }
        value = value.trim();
        if ("".equals(value)) {
            warnAboutBadAnnotation("is empty", method);
            return;
        }
        if (!value.startsWith("/")) {
            warnAboutBadAnnotation("does not start with /", method);
            return;
        }
        handlerMap.put(value, new UrlHandler(method));
    }

    private void warnAboutBadAnnotation(String message, Method method) {
        log.warn("The @" + Handles.class.getSimpleName() + " annotation value of " + ReflectionUtils.methodToString(method) + " " + message + ", ignoring.");
    }
}
