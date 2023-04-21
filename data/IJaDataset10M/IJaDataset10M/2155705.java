package com.clickatell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.googlecode.sms4j.SmsClient;
import com.googlecode.sms4j.SmsException;

public class ClickatellSmsClient implements SmsClient {

    private static final String CLICKATELL_GATEWAY_URL = "https://api.clickatell.com/http/";

    private final String apiId;

    private final String username;

    private final String password;

    public ClickatellSmsClient(String apiId, String username, String password) {
        this.apiId = apiId;
        this.username = username;
        this.password = password;
    }

    public String send(String srcName, String srcNumber, String destNumber, String text) throws IOException, SmsException {
        final String result = sendImpl(((srcName != null) ? srcName : srcNumber), destNumber, text);
        if (result == null) {
            throw new SmsException("Invalid result");
        } else if (result.startsWith("ID: ")) {
            return result.substring(4);
        } else {
            throw new SmsException(result);
        }
    }

    private String sendImpl(String from, String destNumber, String text) throws IOException {
        final QueryStringBuilder query = new QueryStringBuilder();
        query.append("user", username);
        query.append("password", password);
        query.append("api_id", apiId);
        query.append("to", destNumber);
        if (from != null) {
            query.append("from", from);
        }
        query.append("text", text);
        final URL url = new URL(CLICKATELL_GATEWAY_URL + "sendmsg" + query.toString());
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        try {
            return br.readLine();
        } finally {
            br.close();
        }
    }
}
