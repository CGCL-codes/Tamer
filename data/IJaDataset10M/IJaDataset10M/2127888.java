package com.dyuproject.web.rest.consumer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.mortbay.util.ajax.JSON;
import org.mortbay.util.ajax.JSON.ReaderSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dyuproject.json.StandardPojoConvertor;
import com.dyuproject.json.StandardPojoConvertor.StandardSetter;
import com.dyuproject.web.rest.RequestContext;
import com.dyuproject.web.rest.ValidationException;

/**
 * Consumes json content and converts it to pojos.
 * 
 * @author David Yu
 * @created Jan 18, 2009
 */
@SuppressWarnings("unchecked")
public final class SimpleJSONConsumer extends AbstractConsumer {

    public static final String DEFAULT_DISPATCHER_NAME = "json";

    public static final String CACHE_KEY = SimpleJSONConsumer.class + ".cache";

    private static String __defaultContentType = "text/json";

    private static final Logger log = LoggerFactory.getLogger(SimpleJSONConsumer.class);

    public static void setDefaultContentType(String defaultContentType) {
        if (defaultContentType != null && defaultContentType.length() != 0) __defaultContentType = defaultContentType;
    }

    private static final JSON __json = new JSON() {

        public Object convertTo(Class c, Map map) {
            return map;
        }
    };

    private ValidatingPojoConvertor _pojoConvertor;

    public SimpleJSONConsumer() {
    }

    protected String getDefaultContentType() {
        return __defaultContentType;
    }

    protected void init() {
        Map<String, ValidatingPojoConvertor> cache = (Map<String, ValidatingPojoConvertor>) getWebContext().getAttribute(CACHE_KEY);
        if (cache == null) {
            cache = new HashMap<String, ValidatingPojoConvertor>();
            getWebContext().setAttribute(CACHE_KEY, cache);
        }
        ValidatingPojoConvertor vpc = cache.get(_pojoClass.getName());
        if (vpc == null) {
            _pojoConvertor = new ValidatingPojoConvertor(_pojoClass, _fieldParams);
            cache.put(_pojoClass.getName(), _pojoConvertor);
        } else if (vpc._fieldParams.hashCode() == _fieldParams.hashCode()) _pojoConvertor = vpc; else _pojoConvertor = new ValidatingPojoConvertor(_pojoClass, _fieldParams);
    }

    public final boolean merge(Object pojo, RequestContext rc) throws IOException, ValidationException {
        Map<?, ?> props = (Map<?, ?>) __json.parse(new ReaderSource(rc.getRequest().getReader()));
        return props != null && !props.isEmpty() && _pojoConvertor.setProps(pojo, props) != 0;
    }

    public final Object consume(RequestContext rc) throws IOException, ValidationException {
        Map<?, ?> props = (Map<?, ?>) __json.parse(new ReaderSource(rc.getRequest().getReader()));
        return props == null || props.isEmpty() ? null : _pojoConvertor.fromJSON(props);
    }

    public static final class ValidatingPojoConvertor extends StandardPojoConvertor {

        private final Map<?, ?> _fieldParams;

        public ValidatingPojoConvertor(Class<?> pojoClass, Map<?, ?> fieldParams) {
            super(pojoClass);
            _fieldParams = fieldParams;
        }

        protected String getFieldParam(String name) {
            return (String) _fieldParams.get(name);
        }

        protected void addSetter(String field, Method m) {
            boolean included = !"false".equalsIgnoreCase(getFieldParam(field + ".included"));
            if (!included) {
                log.info(field + " excluded");
                return;
            }
            boolean required = !"false".equalsIgnoreCase(getFieldParam(field + ".required"));
            String errorMsg = getFieldParam(field + ".error_msg");
            String validator = getFieldParam(field + ".validator");
            FieldValidator fv = null;
            if (validator != null) {
                try {
                    fv = (FieldValidator) newObjectInstance(validator);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (errorMsg == null || errorMsg.length() == 0) errorMsg = getDefaultErrorMsg(field);
            _setters.put(field, new ValidatingSetter(field, m, required, fv, errorMsg));
        }

        public int setProps(Object obj, Map props) {
            int count = 0;
            Collection<ValidatingSetter> vSetters = (Collection<ValidatingSetter>) _setters.values();
            for (ValidatingSetter vs : vSetters) {
                Object value = props.get(vs.getPropertyName());
                if (value == null) {
                    if (vs.isRequired()) throw new ValidationException(vs.getErrorMsg(), vs.getPropertyName(), obj);
                    continue;
                }
                try {
                    vs.invoke(obj, value);
                    count++;
                } catch (ValidationException e) {
                    throw e;
                } catch (IllegalArgumentException e) {
                    throw new ValidationException(vs.getErrorMsg(), vs.getPropertyName(), obj);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new ValidationException(vs.getErrorMsg(), vs.getPropertyName(), obj);
                }
            }
            return count;
        }
    }

    public static final class ValidatingSetter extends StandardSetter {

        protected final boolean _required;

        protected final FieldValidator _validator;

        protected final String _errorMsg;

        public ValidatingSetter(String propertyName, Method method, boolean required, FieldValidator validator, String errorMsg) {
            super(propertyName, method);
            _required = required;
            _validator = validator;
            _errorMsg = errorMsg;
        }

        public boolean isRequired() {
            return _required;
        }

        public FieldValidator getValidator() {
            return _validator;
        }

        public String getErrorMsg() {
            return _errorMsg;
        }

        public void invokeObject(Object obj, Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
            if (_validator == null) super.invokeObject(obj, value); else {
                String errorMsg = _validator.validate(value);
                if (errorMsg == null) super.invokeObject(obj, value); else throw new ValidationException(errorMsg, getPropertyName(), obj);
            }
        }
    }
}
