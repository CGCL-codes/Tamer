package org.idspace.aau.iwis.tmsync;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class TMSyncRequestWrapper extends HttpServletRequestWrapper {

    Map parameters;

    String method;

    @SuppressWarnings("unchecked")
    public TMSyncRequestWrapper(HttpServletRequest request) {
        super(request);
        this.method = request.getMethod();
        this.parameters = new HashMap(request.getParameterMap());
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public String getParameter(String name) {
        String returnValue = null;
        String[] paramArray = getParameterValues(name);
        if (paramArray != null && paramArray.length > 0) {
            returnValue = paramArray[0];
        }
        return returnValue;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map getParameterMap() {
        return Collections.unmodifiableMap(parameters);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration getParameterNames() {
        return Collections.enumeration(parameters.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] result = null;
        String[] temp = (String[]) parameters.get(name);
        if (temp != null) {
            result = new String[temp.length];
            System.arraycopy(temp, 0, result, 0, temp.length);
        }
        return result;
    }

    @Override
    public StringBuffer getRequestURL() {
        return super.getRequestURL().append("/init");
    }

    /**
     * Sets the a single value for the parameter.  Overwrites any current values.
     * @param name Name of the parameter to set
     * @param value Value of the parameter.
     */
    public void setParameter(String name, String value) {
        String[] oneParam = { value };
        setParameter(name, oneParam);
    }

    /**
     * Sets multiple values for a parameter.
     * Overwrites any current values.
     * @param name Name of the parameter to set
     * @param values String[] of values.
     */
    @SuppressWarnings("unchecked")
    public void setParameter(String name, String[] values) {
        parameters.put(name, values);
    }
}
