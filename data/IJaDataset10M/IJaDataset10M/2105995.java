package org.aspencloud.simple9.client;

import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.aspencloud.simple9.server.enums.ContentType;
import org.aspencloud.simple9.server.enums.Header;
import org.aspencloud.simple9.server.enums.RequestType;
import org.aspencloud.simple9.util.StringUtils;

public class ClientThread extends Thread {

    private Client client;

    private RequestType type;

    private String path;

    private Map<String, String> headers;

    private Map<String, String> parameters;

    private ICallback callback;

    private ClientResponse response;

    public ClientThread(Client client, RequestType type, String path, Map<String, String> parameters) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        this.client = client;
        this.type = type;
        this.path = path;
        this.parameters = parameters;
        headers = new TreeMap<String, String>();
        headers.put(Header.ACCEPT.label(), ContentType.JS.getRequestProperty());
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    private void doDelete() {
        if (parameters == null) {
            parameters = new HashMap<String, String>(1);
        }
        parameters.put("_method", "DELETE");
        doPost();
    }

    private void setHeaders(URLConnection connection) {
        for (Entry<String, String> header : headers.entrySet()) {
            connection.setRequestProperty(header.getKey(), header.getValue());
        }
    }

    private void doGet() {
        try {
            URL url = getURL();
            URLConnection conn = url.openConnection();
            setHeaders(conn);
            response = new ClientResponse(conn);
        } catch (Exception e) {
            response = new ClientResponse(e);
        } finally {
            if (callback != null) {
                callback.run(response);
            }
        }
    }

    private void doPost() {
        try {
            URL url = getURL();
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            setHeaders(conn);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(StringUtils.attrsEncode(parameters));
            wr.flush();
            wr.close();
            response = new ClientResponse(conn);
        } catch (Exception e) {
            response = new ClientResponse(e);
        } finally {
            if (callback != null) {
                callback.run(response);
            }
        }
    }

    private void doPut() {
        if (parameters == null) {
            parameters = new HashMap<String, String>(1);
        }
        parameters.put("_method", "PUT");
        doPost();
    }

    public ClientResponse getResponse() {
        return response;
    }

    private URL getURL() throws MalformedURLException {
        if (RequestType.GET == type && hasParameters()) {
            String query = StringUtils.attrsEncode(parameters);
            return new URL(client.protocol, client.host, client.port, path + "?" + query);
        }
        return new URL(client.protocol, client.host, client.port, path);
    }

    private boolean hasParameters() {
        return parameters != null && !parameters.isEmpty();
    }

    @Override
    public void run() {
        switch(type) {
            case DELETE:
                doDelete();
                break;
            case GET:
                doGet();
                break;
            case POST:
                doPost();
                break;
            case PUT:
                doPut();
                break;
        }
    }

    public void setCallback(ICallback callback) {
        this.callback = callback;
    }
}
