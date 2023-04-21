package de.rentoudu.chat.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@link JsonResponse} should or can be used together with Gson.
 * It interacts like an DTO.
 * 
 * @author Florian Sauter
 */
public class JsonResponse {

    private int length;

    private boolean success;

    private String message;

    private List<Object> data;

    private Map<String, Object> properties;

    private JsonResponse() {
        this.data = new ArrayList<Object>();
        this.properties = new HashMap<String, Object>();
        this.length = 0;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Object> getData() {
        return data;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public void setData(List<Object> data) {
        this.data = data;
        this.length = this.data.size();
    }

    public void addProperty(String key, Object value) {
        this.properties.put(key, value);
    }

    public void addData(Object data) {
        this.data.add(data);
        this.length = this.data.size();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static JsonResponse fromUnsuccessfulResult() {
        JsonResponse response = new JsonResponse();
        response.setSuccess(false);
        return response;
    }
}
