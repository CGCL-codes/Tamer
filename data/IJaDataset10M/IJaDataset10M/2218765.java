package sg.com.fastwire.mediation.plugins.huaweiMTOSI.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import sg.com.fastwire.mediation.plugins.huaweiMTOSI.common.utils.CommonLogger;
import sg.com.fastwire.mediation.plugins.huaweiMTOSI.common.utils.U2000ProcessException;

public class HttpSoapClient {

    private URL url = null;

    private HttpURLConnection httpConn = null;

    private byte[] bData = null;

    public HttpSoapClient(URL url, byte[] bData) {
        this.url = url;
        this.bData = bData;
    }

    public String postXml() throws U2000ProcessException {
        connect(bData);
        writeXml();
        return response();
    }

    private void connect(byte[] bData) throws U2000ProcessException {
        System.out.println("Connecting to: " + url.toString());
        String SOAPAction = "";
        URLConnection connection = null;
        try {
            connection = url.openConnection();
            httpConn = (HttpURLConnection) connection;
            httpConn.setRequestProperty("Content-Length", String.valueOf(bData.length));
            httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            httpConn.setRequestProperty("SOAPAction", SOAPAction);
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
        } catch (IOException ioExp) {
            CommonLogger.error(this, ioExp.getMessage());
            throw new U2000ProcessException(ioExp.getMessage());
        }
    }

    private String response() throws U2000ProcessException {
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(httpConn.getInputStream());
        } catch (IOException ioExp) {
            isr = new InputStreamReader(httpConn.getErrorStream());
        }
        BufferedReader in = new BufferedReader(isr);
        String output = "";
        String inputLine;
        try {
            while ((inputLine = in.readLine()) != null) {
                output += inputLine;
                if (in.ready()) {
                    output += "\n";
                }
            }
        } catch (IOException ioExp) {
            CommonLogger.error(this, ioExp.getMessage());
            throw new U2000ProcessException(ioExp.getMessage());
        }
        try {
            in.close();
        } catch (IOException ioExp) {
            throw new U2000ProcessException(ioExp.getMessage());
        }
        return output;
    }

    private void writeXml() throws U2000ProcessException {
        OutputStream out = null;
        ;
        try {
            out = httpConn.getOutputStream();
            out.write(bData);
            out.close();
        } catch (IOException ioExp) {
            CommonLogger.error(this, ioExp.getMessage());
            throw new U2000ProcessException(ioExp.getMessage());
        }
    }
}
