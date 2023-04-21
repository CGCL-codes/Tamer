package com.georgeandabe.ignant;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class DataResource {

    public static final Long LONG_ELEMENT = new Long(-1);

    public static final Long WILDCARD_ELEMENT = new Long(-100);

    private Object pathElement = null;

    private Vector<DataResource> subResources = new Vector<DataResource>();

    /**
     * @param pathElement A single path element string (e.g. "foopath"), LONG_ELEMENT, or WILDCARD_ELEMENT
     */
    public DataResource(Object pathElement) {
        if (!(pathElement instanceof String) && pathElement != LONG_ELEMENT && pathElement != WILDCARD_ELEMENT) {
            throw new IllegalArgumentException("Bad path element: " + pathElement);
        }
        this.pathElement = pathElement;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response, String[] pathElements) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response, String[] pathElements) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return;
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response, String[] pathElements) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return;
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response, String[] pathElements) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return;
    }

    public void doHead(HttpServletRequest request, HttpServletResponse response, String[] pathElements) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return;
    }

    public Object getPathElement() {
        return pathElement;
    }

    public boolean matches(String pathElement) {
        if (this.pathElement == WILDCARD_ELEMENT) {
            return true;
        } else if (this.pathElement instanceof String) {
            return this.pathElement.equals(pathElement);
        } else if (this.pathElement == LONG_ELEMENT) {
            try {
                Long.parseLong(pathElement);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        throw new IllegalStateException("Path element must be String, LONG_ELEMENT, or WILDCARD_ELEMENT: " + this.pathElement);
    }

    public void prependSubResource(DataResource resource) {
        subResources.add(0, resource);
    }

    public void addSubResource(DataResource subResource) {
        subResources.add(subResource);
    }

    public DataResource getSubResource(String path) {
        DataResource[] subs = getSubResources();
        for (int i = 0; i < subs.length; i++) {
            if (subs[i].matches(path)) {
                return subs[i];
            }
        }
        return null;
    }

    public void removeSubResource(DataResource resource) {
        subResources.remove(resource);
    }

    public DataResource[] getSubResources() {
        return (DataResource[]) subResources.toArray(new DataResource[0]);
    }

    public static long parseLong(String input) {
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Bad long parser input: " + input);
        }
    }

    public void sendStringResponse(String message, String mimeType, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(mimeType);
        byte[] result = message.getBytes();
        response.setContentLength(result.length);
        response.getOutputStream().write(result);
    }

    public static void transfer(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[2048];
        int read = -1;
        while ((read = input.read(buffer)) > 0) {
            output.write(buffer, 0, read);
        }
    }

    public void toDocumentation(StringBuffer result, String parentPath, int depth) {
        String pathElementDisplayName = getPathElementDisplayName(pathElement);
        String path = parentPath + pathElementDisplayName + "/";
        result.append(path + "\n");
        String description = getAPIDescription();
        if (description != null) {
            result.append(description + "\n");
        }
        result.append("\n");
        DataResource[] subs = getSubResources();
        for (int i = 0; i < subs.length; i++) {
            subs[i].toDocumentation(result, path, depth + 1);
        }
    }

    public String getPathElementDisplayName(Object pathElement) {
        if (LONG_ELEMENT.equals(pathElement)) {
            return "bigint";
        } else if (WILDCARD_ELEMENT.equals(pathElement)) {
            return "*";
        }
        return pathElement.toString();
    }

    public String getAPIDescription() {
        return null;
    }

    public void sendJSONResponse(JSONObject json, HttpServletResponse response) throws IOException {
        try {
            sendStringResponse(json.toString(2), "application/json", response);
        } catch (JSONException e) {
            throw new IllegalArgumentException("Could not print JSON: " + e);
        }
    }
}
