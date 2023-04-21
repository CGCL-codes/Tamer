package org.eiichiro.monophony;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.eiichiro.monophony.annotation.Application;
import org.eiichiro.monophony.annotation.Body;
import org.eiichiro.monophony.annotation.Header;
import org.eiichiro.monophony.annotation.Query;
import org.eiichiro.monophony.annotation.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code GenericRequest} is a generic implementation of {@code Request}.
 * If the request type corresponding to the MIME media type specified in 
 * "Content-Type" HTTP request header is not specified by {@code Settings}, 
 * Monophony uses this implementation.
 * 
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class GenericRequest implements Request {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /** Web context. */
    protected final WebContext context;

    /**
	 * Constructs a new {@code GenericRequest} with the specified Web context.
	 * 
	 * @param context Web context.
	 */
    public GenericRequest(WebContext context) {
        this.context = context;
    }

    /**
	 * Constructs Web endpoint parameter from the specified source list or type.
	 * This method is invoked for every Web endpoint method parameter by request 
	 * receiver.
	 * This method processes according to the following steps: 
	 * <ol>
	 * <li>If the specified type is an array type, returns the {@code GenericParameter}
	 * with <code>null</code>. An array type is not supported in this class.</li>
	 * <li>If the specified type is built-in type (See {@link Types#isBuiltinType(Type)}), 
	 * returns the built-in instance.</li>
	 * <li>Constructs the value as the specified type from the specified sources 
	 * and names. See {@link AbstractParameter}. 
	 * If the Web endpoint parameter is declared without any source annotation 
	 * except built-in type, this method always returns <code>null</code>.
	 * </li>
	 * <li>If the construction result is not <code>null</code>, returns the 
	 * result to the client.</li>
	 * <li>If the result is <code>null</code> of primitive type, returns the 
	 * default value of each primitive type to the client.</li>
	 * <li>If the result is <code>null</code> of supported collection type (See 
	 * {@link Types#isSupportedCollection(Type)}), returns the empty collection 
	 * of the specified type to the client.</li>
	 * <li>Otherwise, returns {@code GenericParameter} with <code>null</code>.</li>
	 * </ol>
	 * 
	 * @param type The type of Web endpoint parameter.
	 * @param sources The source list from which Web endpoint parameter is 
	 * constructed.
	 * @see AbstractParameter
	 * @return Web endpoint method parameter.
	 */
    @Override
    public Parameter get(Type type, List<Annotation> sources) {
        if (Types.isArray(type)) {
            logger.warn("Array type is not supported in [" + getClass() + "]");
            return new GenericParameter(null);
        } else if (Types.isBuiltinType(type)) {
            return new BuiltinParameter(context, type);
        }
        for (Annotation source : sources) {
            Parameter parameter = null;
            if (source instanceof Query) {
                Query query = (Query) source;
                parameter = new QueryParameter(context, type, query.value());
            } else if (source instanceof Body) {
                Body body = (Body) source;
                parameter = newBodyParameter(context, type, body.value());
            } else if (source instanceof Header) {
                Header header = (Header) source;
                parameter = new HeaderParameter(context, type, header.value());
            } else if (source instanceof org.eiichiro.monophony.annotation.Cookie) {
                org.eiichiro.monophony.annotation.Cookie cookie = (org.eiichiro.monophony.annotation.Cookie) source;
                parameter = new CookieParameter(context, type, cookie.value());
            } else if (source instanceof Session) {
                Session session = (Session) source;
                parameter = new SessionParameter(context, type, session.value());
            } else if (source instanceof Application) {
                Application application = (Application) source;
                parameter = new ApplicationParameter(context, type, application.value());
            } else {
                logger.warn("Unknown source [" + source + "]");
            }
            if (parameter != null && parameter.getValue() != null) {
                return parameter;
            }
        }
        if (Types.isPrimitive(type)) {
            logger.debug("Cannot construct [" + type + "] primitive; Returns the default value");
            return new PrimitiveParameter(type);
        } else if (Types.isCollection(type)) {
            if (Types.isSupportedCollection(type)) {
                logger.debug("Cannot construct [" + type + "] collection; Returns the empty colleciton");
                return new GenericParameter(Types.getEmptyCollection(type));
            } else {
                logger.warn("Collection type " + type + " is not supported in [" + getClass() + "]");
                return new GenericParameter(null);
            }
        }
        StringBuilder builder = new StringBuilder();
        for (Annotation source : sources) {
            builder.append(source + " ");
        }
        logger.debug("Cannot construct Web endpoint method parameter [" + builder + type + "]");
        return new GenericParameter(null);
    }

    /**
	 * Returns a new {@code BodyParameter} constructs the parameter value from 
	 * HTTP request body.
	 * This method is overridable. To construct the parameter value from the 
	 * content other than 'application/x-www-form-urlencoded' type, such as 
	 * 'application/xml', 'application/json', override this method and return 
	 * the body parameter implementation corresponding to the content type in 
	 * subclass.
	 * 
	 * @param context Web content.
	 * @param type Web endpoint method parameter type.
	 * @param name The parameter name. It may be <code>null</code>.
	 * @return The {@code Parameter} instance to construct the value from the 
	 * HTTP request body.
	 */
    protected Parameter newBodyParameter(WebContext context, Type type, String name) {
        return new BodyParameter(context, type, name);
    }

    /**
	 * {@code GenericParameter} abstracts generic value.
	 * 
	 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
	 */
    protected static final class GenericParameter implements Parameter {

        private final Object value;

        /**
		 * Constructs a new {@code GenericParameter} with the specified value.
		 * 
		 * @param value The parameter value.
		 */
        protected GenericParameter(Object value) {
            this.value = value;
        }

        /** Always returns the specified value. */
        @Override
        public Object getValue() {
            return value;
        }
    }

    /**
	 * {@code BuiltinParameter} abstracts Web built-in type parameter. 
	 * See {@link Types#isBuiltinType(Type)}.
	 * 
	 * @see Types
	 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
	 */
    protected static final class BuiltinParameter implements Parameter {

        private final Object value;

        /**
		 * Constructs a new {@code BuiltinParameter} from the specified Web 
		 * context and parameter type.
		 * 
		 * @param context The Web context.
		 * @param type The parameter type.
		 */
        public BuiltinParameter(WebContext context, Type type) {
            Class<?> rawType = Types.getRawType(type);
            if (rawType.equals(WebContext.class)) {
                value = context;
            } else if (rawType.equals(HttpServletRequest.class)) {
                value = context.getRequest();
            } else if (rawType.equals(HttpServletResponse.class)) {
                value = context.getResponse();
            } else if (rawType.equals(HttpSession.class)) {
                value = context.getSession();
            } else {
                value = context.getServletContext();
            }
        }

        /**
		 * Returns the value of built-in type.
		 * 
		 * @return The value of built-in type.
		 */
        @Override
        public Object getValue() {
            return value;
        }
    }

    /**
	 * {@code PrimitiveParameter} abstracts the default value of the specified 
	 * primitive type.
	 * 
	 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
	 */
    protected static final class PrimitiveParameter implements Parameter {

        private final Object value;

        /**
		 * Constructs a new {@code PrimitiveParameter} for the specified type.
		 * The specified type must represent each of primitive type. See 
		 * {@link Types#isPrimitive(Type)}.
		 * 
		 * @param type The primitive type.
		 */
        public PrimitiveParameter(Type type) {
            Class<?> rawType = Types.getRawType(type);
            if (rawType.equals(Boolean.TYPE)) {
                value = (boolean) false;
            } else if (rawType.equals(Character.TYPE)) {
                value = (char) 0;
            } else if (rawType.equals(Byte.TYPE)) {
                value = (byte) 0;
            } else if (rawType.equals(Double.TYPE)) {
                value = (double) 0.0;
            } else if (rawType.equals(Float.TYPE)) {
                value = (float) 0.0;
            } else if (rawType.equals(Integer.TYPE)) {
                value = (int) 0;
            } else {
                value = (short) 0;
            }
        }

        /**
		 * Returns the default value of the specified primitive type.
		 */
        @Override
        public Object getValue() {
            return value;
        }
    }

    /**
	 * {@code QueryParameter} abstracts Web endpoint method parameter in query 
	 * string.
	 * 
	 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
	 */
    protected static class QueryParameter extends AbstractParameter {

        /**
		 * Constructs a new {@code QueryParameter} from the specified Web 
		 * context, parameter type and parameter name.
		 * 
		 * @param context The Web context.
		 * @param type The parameter type.
		 * @param name The parameter name.
		 */
        protected QueryParameter(WebContext context, Type type, String name) {
            super(context, type, name);
        }

        /**
		 * Returns the value corresponding to the specified name from 
		 * {@code HttpServletRequest} query string.
		 * 
		 * @return The value corresponding to the specified name from the HTTP 
		 * request's query string.
		 */
        @Override
        protected Object getValue(String name) {
            HttpServletRequest request = context.getRequest();
            return request.getParameter(name);
        }

        /**
		 * Returns the value corresponding to the specified name as 
		 * {@code Collection} view from {@code HttpServletRequest}. The specified 
		 * name is treated as the key prefix (The specified name + '[').
		 * 
		 * @return The value corresponding to the specified name as 
		 * {@code Collection} view.
		 */
        @SuppressWarnings("unchecked")
        @Override
        protected Collection<Object> getValues(String name) {
            HttpServletRequest request = context.getRequest();
            Map<String, Object> map = new TreeMap<String, Object>();
            for (Object object : Collections.list(request.getParameterNames())) {
                String key = (String) object;
                if (key.startsWith(name + "[")) {
                    map.put(key, request.getParameter(key));
                }
            }
            return (map.isEmpty()) ? null : map.values();
        }

        /**
		 * Returns a new {@code QueryParameter} constructed with the specified 
		 * Web context and parameter type.
		 * 
		 * @return A new {@code QueryParameter} constructed with the specified 
		 * Web context and parameter type.
		 */
        @Override
        protected Parameter newParameter(WebContext context, Type type, String name) {
            return new QueryParameter(context, type, name);
        }
    }

    /**
	 * {@code BodyParameter} abstracts Web endpoint method parameter in posted 
	 * form.
	 * 
	 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
	 */
    protected static class BodyParameter extends AbstractParameter {

        /**
		 * Constructs a new {@code BodyParameter} from the specified Web 
		 * context, parameter type and parameter name.
		 * 
		 * @param context The Web context.
		 * @param type The parameter type.
		 * @param name The parameter name.
		 */
        protected BodyParameter(WebContext context, Type type, String name) {
            super(context, type, name);
        }

        /**
		 * Returns the value corresponding to the specified name from 
		 * {@code HttpServletRequest} posted form.
		 * 
		 * @return The value corresponding to the specified name from the HTTP 
		 * request's posted form.
		 */
        @Override
        protected Object getValue(String name) {
            HttpServletRequest request = context.getRequest();
            return request.getParameter(name);
        }

        /**
		 * Returns the value corresponding to the specified name as 
		 * {@code Collection} view from {@code HttpServletRequest}. The specified 
		 * name is treated as the key prefix (The specified name + '[').
		 * 
		 * @return The value corresponding to the specified name as 
		 * {@code Collection} view.
		 */
        @SuppressWarnings("unchecked")
        @Override
        protected Collection<Object> getValues(String name) {
            HttpServletRequest request = context.getRequest();
            Map<String, Object> map = new TreeMap<String, Object>();
            for (Object object : Collections.list(request.getParameterNames())) {
                String key = (String) object;
                if (key.startsWith(name + "[")) {
                    map.put(key, request.getParameter(key));
                }
            }
            return (map.isEmpty()) ? null : map.values();
        }

        /**
		 * Returns a new {@code BodyParameter} constructed with the specified 
		 * Web context and parameter type.
		 * 
		 * @return A new {@code BodyParameter} constructed with the specified 
		 * Web context and parameter type.
		 */
        @Override
        protected Parameter newParameter(WebContext context, Type type, String name) {
            return new BodyParameter(context, type, name);
        }
    }

    /**
	 * {@code HeaderParameter} abstracts Web endpoint method parameter in HTTP 
	 * request header.
	 * 
	 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
	 */
    protected static class HeaderParameter extends AbstractParameter {

        /**
		 * Constructs a new {@code HeaderParameter} from the specified Web 
		 * context, parameter type and parameter name.
		 * 
		 * @param context The Web context.
		 * @param type The parameter type.
		 * @param name The parameter name.
		 */
        protected HeaderParameter(WebContext context, Type type, String name) {
            super(context, type, name);
        }

        /**
		 * Returns the value corresponding to the specified name from 
		 * {@code HttpServletRequest} header.
		 * 
		 * @return The value corresponding to the specified name from the HTTP 
		 * request header.
		 */
        @Override
        protected Object getValue(String name) {
            HttpServletRequest request = context.getRequest();
            return request.getHeader(name);
        }

        /**
		 * Returns the value corresponding to the specified name as 
		 * {@code Collection} view from {@code HttpServletRequest} header. The 
		 * specified name is treated as the key prefix (The specified name + '[').
		 * 
		 * @return The value corresponding to the specified name as 
		 * {@code Collection} view.
		 */
        @SuppressWarnings("unchecked")
        @Override
        protected Collection<Object> getValues(String name) {
            HttpServletRequest request = context.getRequest();
            Map<String, Object> map = new TreeMap<String, Object>();
            for (Object object : Collections.list(request.getHeaderNames())) {
                String key = (String) object;
                if (key.startsWith(name + "[")) {
                    map.put(key, request.getHeader(key));
                }
            }
            return (map.isEmpty()) ? null : map.values();
        }

        /**
		 * Returns a new {@code HeaderParameter} constructed with the specified 
		 * Web context and parameter type.
		 * 
		 * @return A new {@code HeaderParameter} constructed with the specified 
		 * Web context and parameter type.
		 */
        @Override
        protected Parameter newParameter(WebContext context, Type type, String name) {
            return new HeaderParameter(context, type, name);
        }
    }

    /**
	 * {@code CookieParameter} abstracts Web endpoint method parameter in cookie.
	 * 
	 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
	 */
    protected static class CookieParameter extends AbstractParameter {

        /**
		 * Constructs a new {@code HeaderParameter} from the specified Web 
		 * context, parameter type and parameter name.
		 * 
		 * @param context The Web context.
		 * @param type The parameter type.
		 * @param name The parameter name.
		 */
        protected CookieParameter(WebContext context, Type type, String name) {
            super(context, type, name);
        }

        /**
		 * Returns the value corresponding to the specified name from cookie in 
		 * {@code HttpServletRequest}.
		 * 
		 * @return The value corresponding to the specified name from cookie.
		 */
        @Override
        protected Object getValue(String name) {
            HttpServletRequest request = context.getRequest();
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(name)) {
                        return cookie.getValue();
                    }
                }
            }
            return null;
        }

        /**
		 * Returns the value corresponding to the specified name as 
		 * {@code Collection} view from cookie in {@code HttpServletRequest}. The 
		 * specified name is treated as the key prefix (The specified name + '[').
		 * 
		 * @return The value corresponding to the specified name as 
		 * {@code Collection} view.
		 */
        @Override
        protected Collection<Object> getValues(String name) {
            HttpServletRequest request = context.getRequest();
            Map<String, Object> map = new TreeMap<String, Object>();
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    String key = cookie.getName();
                    if (key.startsWith(name + "[")) {
                        map.put(key, cookie.getValue());
                    }
                }
            }
            return (map.isEmpty()) ? null : map.values();
        }

        /**
		 * Returns a new {@code CookieParameter} constructed with the specified 
		 * Web context and parameter type.
		 * 
		 * @return A new {@code CookieParameter} constructed with the specified 
		 * Web context and parameter type.
		 */
        @Override
        protected Parameter newParameter(WebContext context, Type type, String name) {
            return new CookieParameter(context, type, name);
        }
    }

    /**
	 * {@code SessionParameter} abstracts Web endpoint method parameter in HTTP 
	 * session.
	 * 
	 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
	 */
    protected static class SessionParameter extends AbstractParameter {

        /**
		 * Constructs a new {@code HeaderParameter} from the specified Web 
		 * context, parameter type and parameter name.
		 * 
		 * @param context The Web context.
		 * @param type The parameter type.
		 * @param name The parameter name.
		 */
        protected SessionParameter(WebContext context, Type type, String name) {
            super(context, type, name);
        }

        /**
		 * Returns the value corresponding to the specified name from 
		 * {@code HttpSession}.
		 * 
		 * @return The value corresponding to the specified name from HTTP 
		 * session.
		 */
        @Override
        protected Object getValue(String name) {
            HttpSession session = context.getSession();
            return session.getAttribute(name);
        }

        /**
		 * Returns the value corresponding to the specified name as 
		 * {@code Collection} view from {@code HttpSession}. If the value is 
		 * found on HTTP session and the value is instance of collection, this 
		 * method returns it directly. Or the specified name is treated as the 
		 * key prefix (The specified name + '[').
		 * 
		 * @return The value corresponding to the specified name as 
		 * {@code Collection} view.
		 */
        @SuppressWarnings("unchecked")
        @Override
        protected Collection<Object> getValues(String name) {
            HttpSession session = context.getSession();
            Object attribute = session.getAttribute(name);
            if (attribute instanceof Collection<?>) {
                return (Collection<Object>) attribute;
            }
            Map<String, Object> map = new TreeMap<String, Object>();
            for (Object object : Collections.list(session.getAttributeNames())) {
                String key = (String) object;
                if (key.startsWith(name + "[")) {
                    map.put(key, session.getAttribute(key));
                }
            }
            return (map.isEmpty()) ? null : map.values();
        }

        /**
		 * Returns a new {@code SessionParameter} constructed with the specified 
		 * Web context and parameter type.
		 * 
		 * @return A new {@code SessionParameter} constructed with the specified 
		 * Web context and parameter type.
		 */
        @Override
        protected Parameter newParameter(WebContext context, Type type, String name) {
            return new SessionParameter(context, type, name);
        }
    }

    /**
	 * {@code ApplicationParameter} abstracts Web endpoint method parameter in 
	 * Web application ({@code ServletContext}).
	 * 
	 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
	 */
    protected static class ApplicationParameter extends AbstractParameter {

        /**
		 * Constructs a new {@code ApplicationParameter} from the specified Web 
		 * context, parameter type and parameter name.
		 * 
		 * @param context The Web context.
		 * @param type The parameter type.
		 * @param name The parameter name.
		 */
        protected ApplicationParameter(WebContext context, Type type, String name) {
            super(context, type, name);
        }

        /**
		 * Returns the value corresponding to the specified name from Web 
		 * application ({@code ServletContext}).
		 * 
		 * @return The value corresponding to the specified name from Web 
		 * application.
		 */
        @Override
        protected Object getValue(String name) {
            ServletContext servletContext = context.getServletContext();
            return servletContext.getAttribute(name);
        }

        /**
		 * Returns the value corresponding to the specified name as 
		 * {@code Collection} view from {@code ServletContext}. If the value is 
		 * found on Servlet context and the value is instance of collection, this 
		 * method returns it directly. Or the specified name is treated as the 
		 * key prefix (The specified name + '[').
		 * @return The value corresponding to the specified name as 
		 * {@code Collection} view.
		 */
        @SuppressWarnings("unchecked")
        @Override
        protected Collection<Object> getValues(String name) {
            ServletContext servletContext = context.getServletContext();
            Object attribute = servletContext.getAttribute(name);
            if (attribute instanceof Collection<?>) {
                return (Collection<Object>) attribute;
            }
            Map<String, Object> map = new TreeMap<String, Object>();
            for (Object object : Collections.list(servletContext.getAttributeNames())) {
                String key = (String) object;
                if (key.startsWith(name + "[")) {
                    map.put(key, servletContext.getAttribute(key));
                }
            }
            return (map.isEmpty()) ? null : map.values();
        }

        /**
		 * Returns a new {@code ApplicationParameter} constructed with the 
		 * specified Web context and parameter type.
		 * 
		 * @return A new {@code ApplicationParameter} constructed with the 
		 * specified Web context and parameter type.
		 */
        @Override
        protected Parameter newParameter(WebContext context, Type type, String name) {
            return new ApplicationParameter(context, type, name);
        }
    }
}
