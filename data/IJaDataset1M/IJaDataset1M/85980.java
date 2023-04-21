package org.identifylife.key.editor.gwt.shared.service.impl;

import org.identifylife.key.editor.gwt.shared.service.EditorService;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author dbarnier
 *
 */
public class JsonEditorService extends JsonService implements EditorService {

    @Override
    public void init(String keyId, AsyncCallback<String> callback) {
        String request = GWT.getModuleBaseURL() + "ws/editor/init/" + keyId + "/";
        Log.debug("init(): request: " + request);
        doPost(request, callback);
    }
}
