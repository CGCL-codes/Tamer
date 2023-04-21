package com.xiledsystems.AlternateJavaBridgelib.components.altbridge;

import android.os.Handler;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import com.xiledsystems.AlternateJavaBridgelib.components.Component;
import com.xiledsystems.AlternateJavaBridgelib.components.altbridge.collect.Lists;
import com.xiledsystems.AlternateJavaBridgelib.components.altbridge.util.AsyncCallbackPair;
import com.xiledsystems.AlternateJavaBridgelib.components.altbridge.util.AsynchUtil;
import com.xiledsystems.AlternateJavaBridgelib.components.altbridge.util.WebServiceUtil;
import com.xiledsystems.AlternateJavaBridgelib.components.events.EventDispatcher;
import com.xiledsystems.AlternateJavaBridgelib.components.util.JsonUtil;

public class TinyWebDB extends AndroidNonvisibleComponent implements Component {

    private static final String LOG_TAG = "TinyWebDB";

    private static final String STOREAVALUE_COMMAND = "storeavalue";

    private static final String TAG_PARAMETER = "tag";

    private static final String VALUE_PARAMETER = "value";

    private static final String GETVALUE_COMMAND = "getvalue";

    private String serviceURL;

    private Handler androidUIHandler;

    /**
   * Creates a new TinyWebDB component.
   *
   * @param container the Form that this component is contained in.
   */
    public TinyWebDB(ComponentContainer container) {
        super(container.$form());
        androidUIHandler = new Handler();
        serviceURL = "http://appinvtinywebdb.appspot.com/";
    }

    /**
   * Returns the URL of the web service database.
   */
    public String ServiceURL() {
        return serviceURL;
    }

    /**
   * Specifies the URL of the  Web service.
   * The default value is the demo service running on App Engine.
   */
    public void ServiceURL(String url) {
        serviceURL = url;
    }

    public void StoreValue(final String tag, final Object valueToStore) {
        final Runnable call = new Runnable() {

            public void run() {
                postStoreValue(tag, valueToStore);
            }
        };
        AsynchUtil.runAsynchronously(call);
    }

    private void postStoreValue(String tag, Object valueToStore) {
        AsyncCallbackPair<String> myCallback = new AsyncCallbackPair<String>() {

            public void onSuccess(String response) {
                androidUIHandler.post(new Runnable() {

                    public void run() {
                        ValueStored();
                    }
                });
            }

            public void onFailure(final String message) {
                androidUIHandler.post(new Runnable() {

                    public void run() {
                        WebServiceError(message);
                    }
                });
            }
        };
        try {
            WebServiceUtil.getInstance().postCommand(serviceURL, STOREAVALUE_COMMAND, Lists.<NameValuePair>newArrayList(new BasicNameValuePair(TAG_PARAMETER, tag), new BasicNameValuePair(VALUE_PARAMETER, JsonUtil.getJsonRepresentation(valueToStore))), myCallback);
        } catch (JSONException e) {
            throw new RuntimeException("Value failed to convert to JSON.");
        }
    }

    /**
   * Event indicating that a StoreValue server request has succeeded.
   */
    public void ValueStored() {
        EventDispatcher.dispatchEvent(this, "ValueStored");
    }

    /**
   * GetValue asks the Web service to get the value stored under the given tag.
   * It is up to the Web service what to return if there is no value stored
   * under the tag.  This component just accepts whatever is returned.
   *
   * @param tag The tag whose value is to be retrieved.
   */
    public void GetValue(final String tag) {
        final Runnable call = new Runnable() {

            public void run() {
                postGetValue(tag);
            }
        };
        AsynchUtil.runAsynchronously(call);
    }

    private void postGetValue(final String tag) {
        AsyncCallbackPair<JSONArray> myCallback = new AsyncCallbackPair<JSONArray>() {

            public void onSuccess(JSONArray result) {
                if (result == null) {
                    androidUIHandler.post(new Runnable() {

                        public void run() {
                            WebServiceError("The Web server did not respond to the get value request " + "for the tag " + tag + ".");
                        }
                    });
                    return;
                } else {
                    try {
                        final String tagFromWebDB = result.getString(1);
                        String value = result.getString(2);
                        final Object valueFromWebDB = (value.length() == 0) ? "" : JsonUtil.getObjectFromJson(value);
                        androidUIHandler.post(new Runnable() {

                            public void run() {
                                GotValue(tagFromWebDB, valueFromWebDB);
                            }
                        });
                    } catch (JSONException e) {
                        androidUIHandler.post(new Runnable() {

                            public void run() {
                                WebServiceError("The Web server returned a garbled value " + "for the tag " + tag + ".");
                            }
                        });
                        return;
                    }
                }
            }

            public void onFailure(final String message) {
                androidUIHandler.post(new Runnable() {

                    public void run() {
                        WebServiceError(message);
                    }
                });
                return;
            }
        };
        WebServiceUtil.getInstance().postCommandReturningArray(serviceURL, GETVALUE_COMMAND, Lists.<NameValuePair>newArrayList(new BasicNameValuePair(TAG_PARAMETER, tag)), myCallback);
        return;
    }

    /**
   * Indicates that a GetValue server request has succeeded.
   *
   * @param valueFromWebDB the value that was returned. Can be any type of value
   * (e.g. number, text, boolean or list).
   */
    public void GotValue(String tagFromWebDB, Object valueFromWebDB) {
        EventDispatcher.dispatchEvent(this, "GotValue", tagFromWebDB, valueFromWebDB);
    }

    /**
   * Indicates that the communication with the Web service signaled an error
   *
   * @param message the error message
   */
    public void WebServiceError(String message) {
        EventDispatcher.dispatchEvent(this, "WebServiceError", message);
    }
}
