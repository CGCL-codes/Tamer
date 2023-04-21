package com.google.code.japa.repository;

import java.beans.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import org.springframework.beans.*;
import org.springframework.core.*;
import org.springframework.util.*;
import com.google.code.japa.annotation.*;

/**
 * Base class for parsers of annotations specifying repository queries.
 * 
 * @author Leandro Aparecido
 * @since 1.0
 * @see com.google.code.japa.repository.query.RepositoryQueryParser
 * @see com.google.code.japa.repository.ret.RepositoryReturnParser
 * @see org.springframework.core.ParameterNameDiscoverer
 */
public class RepositoryAnnotationParser {

    /**
	 * Discoverer of method parameter names.
	 */
    private ParameterNameDiscoverer parameterNameDiscoverer;

    /**
	 * Creates a parser using the {@link org.springframework.core.LocalVariableTableParameterNameDiscoverer}
	 * to discover method parameter names.
	 */
    public RepositoryAnnotationParser() {
        parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    }

    /**
	 * Parse the query name from the value and name attributes of the annotation.
	 * Makes sure only one of the two values is defined.
	 * 
	 * @param value Value attribute of the annotation. 
	 * @param name Name attribute of the annotation.
	 * @return Name of the query.
	 * @see com.google.code.japa.annotation.RepositoryQuery#value()
	 * @see com.google.code.japa.annotation.RepositoryQuery#name()
	 */
    protected String parseQueryName(String value, String name) {
        String queryName;
        if (StringUtils.hasText(value) && StringUtils.hasText(name)) {
            throw new IllegalArgumentException("Only one attribute can be defined for RepositoryQuery/Return annotation: value or name");
        }
        if (StringUtils.hasText(value)) {
            queryName = value;
        } else if (StringUtils.hasText(name)) {
            queryName = name;
        } else {
            queryName = null;
        }
        return queryName;
    }

    /**
	 * Parse the query parameters using the {@link #parameterNameDiscoverer}.
	 * 
	 * @param method Method to parse the parameters.
	 * @param arguments Arguments passed to the method invocation.
	 * @return Map containing the associations name => value.
	 */
    protected Map<String, QueryParameter> parseQueryParameters(Method method, Object[] arguments) {
        Map<String, QueryParameter> parameters = new HashMap<String, QueryParameter>();
        String[] names = parameterNameDiscoverer.getParameterNames(method);
        if (names == null) {
            if (method.getParameterTypes().length > 0) {
                throw new UnsupportedOperationException("Could not discover parameter names of method [" + method + "]. To do this, you have to compile your classes with debug information or configure argument names on RepositoryQuery annotations");
            }
        } else {
            for (int i = 0; i < arguments.length; i++) {
                parameters.put(names[i], parseQueryParameter(names[i], arguments[i], method.getParameterAnnotations()[i]));
            }
        }
        return parameters;
    }

    /**
	 * Parse query parameters investigating the javabean-style properties of the passed method and
	 * arguments.
	 * 
	 * @param method Method to parse the parameters.
	 * @param arguments Arguments passed to the method invocation.
	 * @param index Index of the argument to investigate the javabean properties, all the other
	 * arguments will be parsed using {@link #parseQueryParameters(Method, Object[])}.
	 * @return Map containing the associations name => value.
	 */
    protected Map<String, QueryParameter> parseQueryParametersByExample(Method method, Object[] arguments, int index) {
        Map<String, QueryParameter> parameters = new HashMap<String, QueryParameter>();
        Object exampleBean = arguments[index];
        if (exampleBean == null) {
            throw new IllegalArgumentException("Cannot use null example in query by example of method [" + method + "] argument [" + index + "]");
        }
        parameters.putAll(parseQueryParameters(method, arguments));
        parameters.putAll(parseQueryParametersFromExampleBean(exampleBean));
        return parameters;
    }

    /**
	 * Parse query parameters investigating the javabean-style properties of the passed object.
	 * 
	 * @param exampleBean Object to investigate.
	 * @return Map containing the associations name => parameter.
	 */
    protected Map<String, QueryParameter> parseQueryParametersFromExampleBean(Object exampleBean) {
        Map<String, QueryParameter> parameters = new HashMap<String, QueryParameter>();
        BeanWrapper beanWrapper = new BeanWrapperImpl(exampleBean);
        for (PropertyDescriptor propertyDescriptor : beanWrapper.getPropertyDescriptors()) {
            String propertyName = propertyDescriptor.getName();
            parameters.put(propertyName, new QueryParameter(propertyName, beanWrapper.getPropertyValue(propertyName)));
        }
        return parameters;
    }

    /**
	 * Parse parameters for the query.
	 * 
	 * @param name
	 * @param value
	 * @param parameterAnnotations
	 * @return
	 */
    protected QueryParameter parseQueryParameter(String name, Object value, Annotation[] parameterAnnotations) {
        RepositoryQueryParam annotation = null;
        for (Annotation parameterAnnotation : parameterAnnotations) {
            if (RepositoryQueryParam.class == parameterAnnotation.annotationType()) {
                annotation = (RepositoryQueryParam) parameterAnnotation;
            }
        }
        if (annotation == null) {
            return new QueryParameter(name, value);
        }
        if (annotation.forLike() && (StringUtils.hasText(annotation.prefix()) || StringUtils.hasText(annotation.suffix()))) {
            throw new IllegalArgumentException("Repository query parameter cannot be forLike and define prefix or suffix at the same time");
        }
        if (annotation.forLike()) {
            return new QueryParameter(name, value, true);
        }
        return new QueryParameter(name, value, annotation.prefix(), annotation.suffix());
    }

    public ParameterNameDiscoverer getParameterNameDiscoverer() {
        return parameterNameDiscoverer;
    }

    public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }
}
