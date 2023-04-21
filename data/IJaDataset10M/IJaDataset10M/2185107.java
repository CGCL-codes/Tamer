package org.identifylife.key.editor.gwt.shared.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author dbarnier
 *
 */
public interface DatasetService {

    void getAll(AsyncCallback<String> callback);

    void getByType(String type, AsyncCallback<String> callback);

    void getById(String datasetId, AsyncCallback<String> callback);
}
