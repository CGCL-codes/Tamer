package com.boshanam.user.ui.core.gwt.client.ds;

import java.util.Map;
import com.google.gwt.http.client.URL;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.OperationBinding;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.types.DSProtocol;

/**
 * Base implementation of REST data source to be extended by most other specific
 * data sources of SmartGwt.
 * 
 * Specifies JSON as underlying transport data format.
 * 
 * Uses URL encoding to submit request parameters in case of http PUT, as most
 * servers can not identify posted parameters of PUT request.
 * 
 * @author Sivakumar Y
 * @Email: shiva.forums@gmail.com
 * @Date Dec 4, 2011 7:58:43 PM
 * 
 */
public abstract class AbstractRestDataSource extends RestDataSource {

    public AbstractRestDataSource(String id) {
        setID(id);
        setClientOnly(false);
        OperationBinding fetch = new OperationBinding();
        fetch.setOperationType(DSOperationType.FETCH);
        DSRequest fetchProps = new DSRequest();
        fetchProps.setHttpMethod("GET");
        fetch.setRequestProperties(fetchProps);
        OperationBinding add = new OperationBinding();
        add.setOperationType(DSOperationType.ADD);
        add.setDataProtocol(DSProtocol.POSTPARAMS);
        OperationBinding update = new OperationBinding();
        update.setOperationType(DSOperationType.UPDATE);
        DSRequest updateProps = new DSRequest();
        updateProps.setHttpMethod("PUT");
        update.setRequestProperties(updateProps);
        OperationBinding remove = new OperationBinding();
        remove.setOperationType(DSOperationType.REMOVE);
        DSRequest removeProps = new DSRequest();
        removeProps.setHttpMethod("DELETE");
        remove.setRequestProperties(removeProps);
        setOperationBindings(fetch, add, update, remove);
        init();
    }

    @Override
    protected Object transformRequest(DSRequest request) {
        super.transformRequest(request);
        postProcessTransform(request);
        return "";
    }

    @SuppressWarnings("rawtypes")
    protected void postProcessTransform(DSRequest request) {
        StringBuilder url = new StringBuilder(getServiceRoot());
        Map dataMap = request.getAttributeAsMap("data");
        if (request.getOperationType() == DSOperationType.REMOVE) {
            url.append(getPrimaryKeyProperty()).append("/").append(dataMap.get(getPrimaryKeyProperty()));
        } else if (request.getOperationType() == DSOperationType.UPDATE) {
            appendParameters(url, request);
        }
        request.setActionURL(URL.encode(url.toString()));
    }

    @SuppressWarnings("rawtypes")
    protected void appendParameters(StringBuilder url, DSRequest request) {
        Map dataMap = request.getAttributeAsMap("data");
        Record oldValues = request.getOldValues();
        boolean paramsAppended = false;
        if (!dataMap.isEmpty()) {
            url.append("?");
        }
        for (Object keyObj : dataMap.keySet()) {
            String key = (String) keyObj;
            if (!dataMap.get(key).equals(oldValues.getAttribute(key)) || isPrimaryKey(key)) {
                url.append(key).append('=').append(dataMap.get(key)).append('&');
                paramsAppended = true;
            }
        }
        if (paramsAppended) {
            url.deleteCharAt(url.length() - 1);
        }
    }

    private boolean isPrimaryKey(String property) {
        return getPrimaryKeyProperty().equalsIgnoreCase(property);
    }

    protected String getPrimaryKeyProperty() {
        return "id";
    }

    protected abstract String getServiceRoot();

    protected abstract void init();
}
