package com.ma.j2mesync.google;

import com.ma.j2mesync.google.ui.GoogleSync;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.HttpsConnection;

/**
 *
 * @author ma
 */
public class NetworkUtil {

    private String connectString = "https://www.google.com/accounts/ClientLogin";

    private String username;

    private String password;

    HttpsConnection httpsConnection;

    HttpConnection httpConnection;

    String authToken;

    DataInputStream dataInputStream = null;

    OutputStream outputstream = null;

    int responseCode = 0;

    String allContacts;

    private int AUTH_OK_CODE = 200;

    public NetworkUtil(String connectString, String userName, String password) throws IOException, Exception {
        this.connectString = connectString;
        this.username = userName;
        this.password = password;
        if (GoogleSync.printSystemOut) {
            System.out.println("connecting: ");
        }
        httpsConnection = (HttpsConnection) Connector.open(connectString);
        httpsConnection.setRequestMethod("POST");
        httpsConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        String requestBody = "accountType=GOOGLE&Email=" + username + "&Passwd=" + password + "&service=cp&source=j2mesync-j2mesync-1.0";
        httpsConnection.setRequestProperty("Content-Length", Integer.toString(requestBody.length()));
        outputstream = httpsConnection.openOutputStream();
        outputstream.write(requestBody.getBytes());
        outputstream.close();
        dataInputStream = httpsConnection.openDataInputStream();
        responseCode = httpsConnection.getResponseCode();
        if (GoogleSync.printSystemOut) {
            System.out.println("response code: " + responseCode);
        }
        byte[] buf = new byte[dataInputStream.available()];
        dataInputStream.readFully(buf);
        String content = new String(buf);
        authToken = parseAuthToken(content);
        if (GoogleSync.printSystemOut) {
            System.out.println("response content: " + content);
            System.out.println("auth token: '" + authToken + "'");
        }
        httpsConnection.close();
        outputstream = null;
        dataInputStream = null;
        httpsConnection = null;
        if (responseCode != AUTH_OK_CODE) {
            throw new Exception(content);
        }
    }

    public String retrieveAllContacts() {
        String content = null;
        try {
            String url = "http://www.google.com/m8/feeds/contacts/" + username + "/base?max-results=20000";
            httpConnection = (HttpConnection) Connector.open(url);
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("Authorization", "GoogleLogin auth=" + authToken);
            outputstream = httpConnection.openOutputStream();
            outputstream.write("".getBytes());
            outputstream.close();
            dataInputStream = httpConnection.openDataInputStream();
            responseCode = httpConnection.getResponseCode();
            if (GoogleSync.printSystemOut) {
                System.out.println("response code: " + responseCode);
                System.out.println("dataInputStream.available(): " + dataInputStream.available());
            }
            int bufSize = 200000;
            byte[] buf = new byte[bufSize];
            int i = 0;
            while (i < bufSize) {
                try {
                    buf[i] = dataInputStream.readByte();
                } catch (Exception e) {
                    break;
                }
                i++;
            }
            content = new String(buf, 0, i);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return content;
    }

    public DataInputStream retrieveAllContactsInputStream() {
        try {
            String url = "http://www.google.com/m8/feeds/contacts/" + username + "/base?max-results=20000";
            httpConnection = (HttpConnection) Connector.open(url);
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("Authorization", "GoogleLogin auth=" + authToken);
            outputstream = httpConnection.openOutputStream();
            outputstream.write("".getBytes());
            outputstream.close();
            dataInputStream = httpConnection.openDataInputStream();
            responseCode = httpConnection.getResponseCode();
            if (GoogleSync.printSystemOut) {
                System.out.println("response code: " + responseCode);
            }
            return dataInputStream;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String parseAuthToken(String authResponse) {
        String authTag = "Auth=";
        int index = authResponse.indexOf(authTag);
        return authResponse.substring(index + authTag.length(), authResponse.length() - 1);
    }
}
