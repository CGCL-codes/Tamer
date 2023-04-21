package org.mayo.requestmapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Enumeration;

/**
 * This class wraps request parameters provided by the mapping system into the
 * default request.
 * @author Chris Corbyn <chris@w3style.co.uk>
 */
@SuppressWarnings("deprecation")
public class MutableRequestWrapper extends HttpServletRequestWrapper implements MutableRequest {

    /** An immutable Map of parameters */
    private Map<String, String[]> internalParameterMap;

    /**
   * Constructs a new wrapper from the given request.
   * @param HttpServletRequest request
   */
    @SuppressWarnings("unchecked")
    public MutableRequestWrapper(HttpServletRequest request) {
        super(request);
        internalParameterMap = super.getParameterMap();
    }

    /**
   * Set a single value for a parameter.
   * @param String param
   * @param String value
   */
    public void setParameter(String param, String value) {
        String[] values = { value };
        setParameterValues(param, values);
    }

    /**
   * Set a collection of values for a parameter.
   * @param String param
   * @param String[] values
   */
    public void setParameterValues(String param, String[] values) {
        internalParameterMap.put(param, values);
    }

    /**
   * Returns an Enumeration over the names of the parameters in this request.
   * @return Enumeration<String>
   */
    @Override
    public Enumeration<String> getParameterNames() {
        final Iterator<String> it = internalParameterMap.keySet().iterator();
        return new Enumeration<String>() {

            public boolean hasMoreElements() {
                return it.hasNext();
            }

            public String nextElement() {
                return it.next();
            }
        };
    }

    /**
   * Get the collection of values for the given parameter.
   * This methods returns null if no such parameter exists.
   * @param String param
   * @return String[]
   */
    @Override
    public String[] getParameterValues(String param) {
        return internalParameterMap.get(param);
    }

    /**
   * Returns the value of the given request parameter.
   * If no such parameter exists this method returns null.
   * @param String param
   * @return String
   */
    @Override
    public String getParameter(String param) {
        String value = null;
        String[] values = getParameterValues(param);
        if (values != null) {
            value = values[0];
        }
        return value;
    }

    /**
   * Returns an immutable Map containing all parameters in this request.
   * @return Map<String,String[]>
   */
    @Override
    public Map<String, String[]> getParameterMap() {
        return new HashMap<String, String[]>(internalParameterMap);
    }
}
