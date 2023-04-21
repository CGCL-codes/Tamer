package org.identifylife.key.editor.gwt.shared.service.impl;

import org.identifylife.key.editor.gwt.shared.service.DatasetService;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author dbarnier
 *
 */
public class JsonDatasetService extends JsonService implements DatasetService {

    @Override
    public void getAll(AsyncCallback<String> callback) {
        String request = GWT.getModuleBaseURL() + "ws/dataset/all/";
        Log.debug("getById(): request: " + request);
        doGet(request, callback);
    }

    @Override
    public void getByType(String type, AsyncCallback<String> callback) {
        String request = GWT.getModuleBaseURL() + "ws/dataset/type/" + type + "/";
        Log.debug("getByType(): request: " + request);
        doGet(request, callback);
    }

    @Override
    public void getById(String datasetId, AsyncCallback<String> callback) {
        String request = GWT.getModuleBaseURL() + "ws/dataset/" + datasetId + "/";
        Log.debug("getById(): request: " + request);
        doGet(request, callback);
    }
}
