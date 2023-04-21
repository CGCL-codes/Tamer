package cing.client;

import java.util.Set;
import cing.client.i18n.iCingConstants;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;

public class FormHandlerMain implements FormHandler {

    public iCing icing;

    public String action;

    public String exitCode;

    public String result;

    public JSONObject jso;

    iCingConstants c = iCing.c;

    public FormHandlerMain(iCing icing) {
        super();
        if (icing == null) {
            GenClient.showCodeBug("In FormHandlerMain constructor. Found null for icing.");
        }
        setiCing(icing);
    }

    public void onSubmit(FormSubmitEvent event) {
        GenClient.showDebug("Starting submit which will be dealt with from FormHandlerMain and sub class.");
        if (icing == null) {
            GenClient.showCodeBug("In FormHandlerMain.onSubmit Found null for icing.");
            event.setCancelled(true);
            return;
        }
    }

    public void onSubmitComplete(FormSubmitCompleteEvent event) {
        GenClient.showDebug("Now in FormHandlerMain.onSubmitComplete");
        String response = event.getResults();
        if (response == null) {
            GenClient.showError("Failed to get any response from server.");
            return;
        }
        int endIndex = Math.min(response.length(), Settings.MAX_RESPONSE_REPORTED_FOR_DEBUGGING);
        String responseTruncate = response.substring(0, endIndex).replace(GenClient.eol, "");
        GenClient.showDebug("responseTruncate is: [" + responseTruncate + "]");
        String jsonResult = removePreTags(response);
        JSONValue jsv;
        try {
            jsv = JSONParser.parse(jsonResult);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "Failed to parse json result from server for json: [" + jsonResult + "]";
            GenClient.showError(msg);
            return;
        }
        jso = jsv.isObject();
        Set<String> keySet = jso.keySet();
        Object[] keys = keySet.toArray();
        action = null;
        exitCode = null;
        result = null;
        for (int i = 0; i < keys.length; i++) {
            String key = (String) keys[i];
            JSONValue v = jso.get(key);
            JSONString valueObj = v.isString();
            if (valueObj == null) {
                GenClient.showDebug("Skipping key [" + key + "] with non-String value [" + v.toString() + "]");
                continue;
            }
            String value = valueObj.stringValue();
            endIndex = Math.min(value.length(), Settings.MAX_RESPONSE_REPORTED_FOR_DEBUGGING);
            String valueTruncate = value.substring(0, endIndex);
            GenClient.showDebug("Received key, valueTruncate: [" + key + "], [" + valueTruncate + "]");
            if (key.equals(Settings.FORM_PARM_ACTION)) {
                action = value;
            } else if (key.equals(Settings.RESPONSE_EXIT_CODE)) {
                exitCode = value;
            } else if (key.equals(Settings.RESPONSE_RESULT)) {
                result = value;
            }
        }
        if (action == null) {
            action = Settings.RESPONSE_ACTION_DEFAULT;
            GenClient.showDebug("Missing action; set to default: [" + action + "]");
            return;
        }
        if (exitCode == null) {
            exitCode = Settings.RESPONSE_EXIT_CODE_DEFAULT;
            GenClient.showDebug("Missing exitCode; set to default: [" + exitCode + "]");
        }
        if (!Settings.RESPONSE_EXIT_CODE_ALIST.contains(exitCode)) {
            GenClient.showError("Found invalid exit code: [" + exitCode + "] set to error [" + Settings.RESPONSE_EXIT_CODE_ERROR + "]");
            exitCode = Settings.RESPONSE_EXIT_CODE_ERROR;
        }
        if (result == null) {
            result = Settings.RESPONSE_RESULT_DEFAULT;
            GenClient.showDebug("Missing result; set to default: [" + result + "]");
            return;
        }
        if (exitCode.equals(Settings.RESPONSE_EXIT_CODE_ERROR)) {
            String msg = c.ERROR() + "\n\n" + "Action: " + action + "\n" + "exitCode: " + exitCode + "\n" + "result: " + result;
            Window.alert(msg);
        }
        GenClient.showDebug("Exiting FormHandlerMain.onSubmitComplete");
    }

    public void setiCing(iCing icing) {
        if (icing == null) {
            GenClient.showError("in FormHandlerMain.setiCing found icing: null");
        } else {
        }
        this.icing = icing;
    }

    public static String removePreTags(String response) {
        if (response == null) {
            return null;
        }
        if (!response.startsWith("<pre")) {
            return response;
        }
        int idxClosingAngularBracket = response.indexOf('>');
        return response.substring(idxClosingAngularBracket + 1, response.length() - "</pre>".length());
    }
}
