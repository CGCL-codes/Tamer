package com.redhipps.hips.client.model;

import java.io.Serializable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.redhipps.hips.client.io.RequestBuilderFactory;
import com.redhipps.hips.client.io.RequestBuilderFactory.Type;

@Deprecated
public class Context implements Serializable {

    private PythonDatastoreKey loginKey;

    private Institution institution;

    private Schedule schedule;

    private Doctor doctor;

    private ScheduleSolution solution;

    Context() {
    }

    public void pushInstitution(Institution institution) {
        if (loginKey == null) {
            throw new IllegalStateException();
        }
        this.institution = institution;
    }

    public void pushDoctor(Doctor doctor) {
        if (institution == null) {
            throw new IllegalStateException();
        }
        this.doctor = doctor;
    }

    public void pushSchedule(Schedule schedule) {
        if (institution == null) {
            throw new IllegalStateException();
        }
        this.schedule = schedule;
    }

    public void pushScheduleSolution(ScheduleSolution solution) {
        if (schedule == null) {
            throw new IllegalStateException();
        }
        this.solution = solution;
    }

    public void popInstitution() {
        institution = null;
    }

    public void popDoctor() {
        doctor = null;
    }

    public void popSchedule() {
        schedule = null;
    }

    public void popScheduleSolution() {
        solution = null;
    }

    public PythonDatastoreKey loginKey() {
        return loginKey;
    }

    public Institution institution() {
        return institution;
    }

    public Schedule schedule() {
        return schedule;
    }

    public Doctor doctor() {
        return doctor;
    }

    public ScheduleSolution scheduleSolution() {
        return solution;
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("{");
        if (loginKey != null) {
            b.append("\"login\": \"");
            b.append(loginKey);
            b.append("\", ");
        }
        if (institution != null) {
            b.append("\"institution\": \"");
            b.append("\", ");
        }
        if (schedule != null) {
            b.append("\"schedule\": \"");
            b.append("\", ");
        }
        if (doctor != null) {
            b.append("\"doctor\": \"");
            b.append("\", ");
        }
        b.append("}");
        return b.toString();
    }

    public static Context create(PythonDatastoreKey loginKey) {
        return create(loginKey, null);
    }

    public static Context create(PythonDatastoreKey loginKey, Institution institution) {
        Context c = new Context();
        c.loginKey = loginKey;
        c.institution = institution;
        return c;
    }

    private static Context ROOT_CONTEXT;

    /**
   * Explicitly invokes initialization of root context.
   * <p>
   * TODO It seems wasteful to include an extra RPC here.
   * 
   * @param requestBuilderFactory Factory to build rpc request.
   * @param callback Callback to invoke upon RPC completion. This callback does not need
   *     to implement any additional logic to handle the root context response.
   * @throws RequestException
   */
    public static void initialize(RequestBuilderFactory requestBuilderFactory, final RequestCallback callback) {
        RequestBuilder builder = requestBuilderFactory.createBuilder(Type.CONTEXT_ROOT);
        try {
            builder.sendRequest(null, new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    GWT.log("Error in context load.", exception);
                    ROOT_CONTEXT = create(new PythonDatastoreKey("error"));
                    callback.onError(request, exception);
                }

                public void onResponseReceived(Request request, Response response) {
                    GWT.log("Received context string: " + response.getText(), null);
                    JSONValue value = JSONParser.parse(response.getText());
                    JSONObject dict = value.isObject();
                    ROOT_CONTEXT = create(new PythonDatastoreKey(dict.get("login").isString().stringValue()));
                    callback.onResponseReceived(request, response);
                }
            });
        } catch (RequestException e) {
            callback.onError(null, e);
        }
    }

    public static Context rootContext() {
        return ROOT_CONTEXT;
    }
}
