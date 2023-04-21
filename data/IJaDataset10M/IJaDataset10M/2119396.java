package com.prem.share.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpRequester {

    HttpURLConnection conn = null;

    URL url = null;

    public HttpRequester(URL url, Map<String, String> headers) throws IOException {
        conn = getRequestConnection(url);
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                setHeader(header.getKey(), header.getValue());
            }
        }
    }

    public String getRequest() {
        StringBuilder data = new StringBuilder("");
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                if (line.trim().length() > 0) {
                    data.append(line);
                }
            }
            rd.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return data.toString();
    }

    public void setHeader(String name, String value) {
        conn.setRequestProperty(name, value);
    }

    protected HttpURLConnection getRequestConnection(URL requestUrl) throws IOException {
        if (!requestUrl.getProtocol().startsWith("http")) {
            throw new UnsupportedOperationException("Unsupported scheme:" + requestUrl.getProtocol());
        }
        HttpURLConnection huc = (HttpURLConnection) requestUrl.openConnection();
        huc.setUseCaches(false);
        huc.setDefaultUseCaches(false);
        huc.setInstanceFollowRedirects(false);
        return huc;
    }
}
