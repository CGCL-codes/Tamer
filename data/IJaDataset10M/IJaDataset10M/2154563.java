package com.crowdsourcing.framework.servlet.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.crowdsourcing.framework.servlet.IllegalParameterException;

/**
 * <code>XSSFilter</code> protects the application from Cross Site Scripting (XSS) attacks. 
 * @web.filter
 *     display-name="XSS Filter"
 *     name="XSSFilter"
 *
 * @web.filter-mapping
 *     url-pattern="/*"
 */
public class XSSFilter extends BaseServletFilter {

    private static final Logger LOGGER = Logger.getLogger(XSSFilter.class);

    /**
     * Constructs a <code>XSSFilter</code>.
     */
    public XSSFilter() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public void doFilter(ServletRequest pRequest, ServletResponse pResponse, FilterChain pChain) throws IOException, ServletException {
        ServletRequest request = pRequest;
        if (pRequest instanceof HttpServletRequest) {
            request = new XSSHttpServletRequest((HttpServletRequest) pRequest);
        }
        super.doFilter(request, pResponse, pChain);
    }

    /**
     * <code>XSSHttpServletRequest</code> intercepts parameter access and throws an {@link 
     * IllegalParameterException} when a parameter value contains script snippets.
     * This protects the application from Cross Site Scripting (XSS) attacks.
     */
    private static class XSSHttpServletRequest extends HttpServletRequestWrapper {

        private final Set xssParams;

        public XSSHttpServletRequest(HttpServletRequest pRequest) {
            super(pRequest);
            Set invalidParams = null;
            for (Enumeration paramNames = pRequest.getParameterNames(); paramNames.hasMoreElements(); ) {
                String paramName = (String) paramNames.nextElement();
                String[] paramValues = super.getParameterValues(paramName);
                if (XSSChecker.testForXSS(paramName, paramValues)) {
                    if (invalidParams == null) {
                        invalidParams = new HashSet();
                    }
                    invalidParams.add(paramName);
                    if (LOGGER.isEnabledFor(Level.WARN)) {
                        LOGGER.warn("Parameter \"" + paramName + "\" may contain script snippets: " + paramValues[0]);
                    }
                }
            }
            this.xssParams = invalidParams;
        }

        /**
         * {@inheritDoc}
         */
        public String getParameter(String pName) {
            String value = super.getParameter(pName);
            if (xssParams != null && xssParams.contains(pName)) {
                throw new IllegalParameterException("Request parameter seems to contain script snippets: " + pName + '=' + value);
            }
            return value;
        }

        /**
         * {@inheritDoc}
         */
        public String[] getParameterValues(String pName) {
            String[] values = super.getParameterValues(pName);
            if (xssParams != null && xssParams.contains(pName)) {
                throw new IllegalParameterException("Request parameter seems to contain script snippets: " + pName + '=' + values[0]);
            }
            return values;
        }

        /**
         * {@inheritDoc}
         */
        public Map getParameterMap() {
            Map parameters = super.getParameterMap();
            if (xssParams != null) {
                parameters = new XSSMap(parameters);
            }
            return parameters;
        }

        /**
         * <code>XSSMap</code> description_here.
         */
        private class XSSMap implements Map {

            private final Map parameters;

            public XSSMap(Map pParameters) {
                parameters = pParameters;
            }

            public void clear() {
                parameters.clear();
            }

            public boolean containsKey(Object pKey) {
                return parameters.containsKey(pKey);
            }

            public boolean containsValue(Object pValue) {
                return parameters.containsValue(pValue);
            }

            public Set entrySet() {
                return parameters.entrySet();
            }

            public boolean equals(Object pObj) {
                return parameters.equals(pObj);
            }

            public Object get(Object pKey) {
                Object values = parameters.get(pKey);
                if (xssParams.contains(pKey)) {
                    throw new IllegalParameterException("Request parameter seems to contain script snippets: " + pKey + '=' + ((String[]) values)[0]);
                }
                return values;
            }

            public int hashCode() {
                return parameters.hashCode();
            }

            public boolean isEmpty() {
                return parameters.isEmpty();
            }

            public Set keySet() {
                return parameters.keySet();
            }

            public Object put(Object pKey, Object pValue) {
                return parameters.put(pKey, pValue);
            }

            public void putAll(Map pT) {
                parameters.putAll(pT);
            }

            public Object remove(Object pKey) {
                return parameters.remove(pKey);
            }

            public int size() {
                return parameters.size();
            }

            public String toString() {
                return parameters.toString();
            }

            public Collection values() {
                return parameters.values();
            }
        }
    }
}
