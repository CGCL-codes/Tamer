package com.chungco.rest;

import java.util.Set;
import com.chungco.rest.exception.RestCommandException;

public abstract class AbstractRestService<R extends IRestResult> implements IRestCommand<R> {

    private final ParameterMap mMap;

    public AbstractRestService() {
        mMap = new ParameterMap();
    }

    public R execute() throws InterruptedException, RestCommandException {
        preExecute();
        final R r = doExecute();
        postExecute(r);
        return r;
    }

    protected abstract R doExecute() throws InterruptedException, RestCommandException;

    protected void preExecute() {
    }

    protected void postExecute(R pR) {
    }

    /**
     * Not an API accessible method. Only subclasses may (ab)use this method.
     * For public use, implementations of this class should wrap a public
     * accessor around this method.
     * 
     * @param pKey
     *            The key key
     * @param pVal
     *            The value. If null, equivalent to removing the param
     * @return The key/value value.
     */
    protected void setParam(final String pKey, final String pVal) {
        if (pVal == null) {
            mMap.removeParam(pKey);
        } else {
            mMap.setParam(pKey, pVal);
        }
    }

    protected String getParam(final String pKey) {
        return mMap.getParam(pKey);
    }

    /**
     * Implementation is responsible for returning the end point URL. This
     * includes any key, or tag, or unique identifier Example:
     * 
     * @return The fully constructed end URL for this {@link IRestCommand}
     *         implementation
     */
    protected abstract String getEndpointURL();

    /**
     * Loads the parameter map. Not a merge.
     * 
     * @param pRestService
     * @param sharedMap
     */
    protected void setParameterMap(final ParameterMap sharedMap) {
        mMap.clear();
        final Set<String> keySet = sharedMap.keys();
        for (final String key : keySet) {
            final String value = sharedMap.getParam(key);
            mMap.setParam(key, value);
        }
    }

    protected String makeXML(String pXmlString) {
        for (final String key : mMap.keys()) {
            pXmlString = pXmlString.replace(RestUtils.en(key), mMap.getParam(key));
        }
        return pXmlString;
    }
}
