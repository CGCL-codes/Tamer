package org.openremote.web.console.client.polling;

import java.util.HashSet;
import java.util.Iterator;
import org.openremote.web.console.client.Constants;
import org.openremote.web.console.client.utils.ClientDataBase;
import org.openremote.web.console.client.utils.ORListenerManager;
import org.openremote.web.console.client.utils.ORRoundRobin;
import org.openremote.web.console.client.window.LoginWindow;
import org.openremote.web.console.exception.ControllerExceptionMessage;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

/**
 * Polling Helper, this class will setup a polling thread to listen 
 * and notify screen component status changes.
 */
public class PollingHelper {

    private String pollingStatusIds;

    private String serverUrl;

    private boolean isPolling;

    private Timer pollingTimer;

    private static String sessionId;

    public PollingHelper(HashSet<Integer> ids) {
        readSessionId();
        serverUrl = ClientDataBase.appSetting.getCurrentServer();
        Iterator<Integer> id = ids.iterator();
        if (id.hasNext()) {
            pollingStatusIds = id.next().toString();
        }
        while (id.hasNext()) {
            pollingStatusIds = pollingStatusIds + "," + id.next();
        }
    }

    /**
    * Request current screen components status and start polling.
    */
    public void requestCurrentStatusAndStartPolling() {
        if (isPolling) {
            return;
        }
        isPolling = true;
        SimpleScriptTagProxy requestStatusProxy = new SimpleScriptTagProxy(serverUrl + "/rest/status/" + pollingStatusIds, new StatusResultReader());
        requestStatusProxy.load();
        final SimpleScriptTagProxy pollingStatusProxy = new SimpleScriptTagProxy(serverUrl + "/rest/polling/" + sessionId + "/" + pollingStatusIds, new StatusResultReader());
        pollingTimer = new Timer() {

            @Override
            public void run() {
                isPolling = true;
                pollingStatusProxy.load();
            }

            @Override
            public void cancel() {
                super.cancel();
                pollingStatusProxy.cancel();
            }
        };
        pollingTimer.run();
    }

    /**
    * Cancel current screen's polling.
    */
    public void cancelPolling() {
        isPolling = false;
        if (pollingTimer != null) {
            pollingTimer.cancel();
        }
        pollingTimer = null;
    }

    /**
    * Read session id from cookies.
    */
    private void readSessionId() {
        if (sessionId == null) {
            sessionId = Cookies.getCookie("JSESSIONID");
        }
    }

    /**
    * Read sensor status.
    * 
    * @param jsonObj the json obj
    */
    private void readStatus(JSONObject jsonObj) {
        JSONArray statusArray = jsonObj.get("status").isArray();
        if (statusArray != null) {
            for (int i = 0; i < statusArray.size(); i++) {
                String id = statusArray.get(i).isObject().get("id").isNumber().toString();
                JSONValue jsonValue = statusArray.get(i).isObject().get("content");
                ClientDataBase.statusMap.put(id, readStringValue(jsonValue));
                ORListenerManager.getInstance().notifyOREventListener(Constants.ListenerPollingStatusIdFormat + id, null);
            }
        } else {
            JSONObject statusObj = jsonObj.get("status").isObject();
            String id = statusObj.get("id").isNumber().toString();
            JSONValue jsonValue = statusObj.isObject().get("content");
            ClientDataBase.statusMap.put(id, readStringValue(jsonValue));
            ORListenerManager.getInstance().notifyOREventListener(Constants.ListenerPollingStatusIdFormat + id, null);
        }
    }

    private String readStringValue(JSONValue jsonValue) {
        String value = "";
        if (jsonValue.isString() != null) {
            value = jsonValue.isString().stringValue();
        } else if (jsonValue.isNumber() != null) {
            value = jsonValue.isNumber().toString();
        } else if (jsonValue.isBoolean() != null) {
            value = jsonValue.isBoolean().toString();
        } else if (jsonValue.isNull() != null) {
            value = jsonValue.isNull().toString();
        }
        return value;
    }

    private class StatusResultReader implements JsonResultReader {

        public void read(JSONObject jsonObj) {
            if (jsonObj.containsKey("status")) {
                readStatus(jsonObj);
                if (pollingTimer != null) {
                    pollingTimer.run();
                }
                return;
            } else if (jsonObj.containsKey("error")) {
                JSONObject errorObj = jsonObj.get("error").isObject();
                int errorCode = Integer.valueOf(errorObj.get("code").isNumber().toString());
                if (errorCode == ControllerExceptionMessage.GATEWAY_TIMEOUT) {
                    if (pollingTimer != null) {
                        pollingTimer.run();
                    }
                    return;
                } else if (errorCode == ControllerExceptionMessage.UNAUTHORIZED) {
                    cancelPolling();
                    new LoginWindow();
                } else if (errorCode == ControllerExceptionMessage.REFRESH_CONTROLLER) {
                    cancelPolling();
                    MessageBox.alert("ERROR", errorObj.get("message").isString().stringValue(), new Listener<MessageBoxEvent>() {

                        public void handleEvent(MessageBoxEvent be) {
                            Window.Location.reload();
                        }
                    });
                } else {
                    cancelPolling();
                    MessageBox.alert("ERROR", errorObj.get("message").isString().stringValue(), new Listener<MessageBoxEvent>() {

                        public void handleEvent(MessageBoxEvent be) {
                            ORRoundRobin.doSwitch();
                        }
                    });
                }
            }
        }
    }
}
