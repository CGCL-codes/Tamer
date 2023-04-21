package com.freshbooks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;

public class PDFGrabber {

    public static byte[] getPDF(long invoiceId, String clientViewUrlStr) throws IOException, MalformedURLException {
        HttpClient client = new HttpClient();
        client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        URL clientViewUrl = new URL(clientViewUrlStr);
        PostMethod method = new PostMethod(clientViewUrlStr);
        try {
            client.executeMethod(method);
        } finally {
            method.releaseConnection();
        }
        String pdfUrl = "https://" + clientViewUrl.getHost() + "/getPDF.php";
        method = new PostMethod(pdfUrl);
        method.addParameter("invoiceid[]", String.valueOf(invoiceId));
        method.addParameter("disposition", "attachment");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            client.executeMethod(method);
            InputStream in = method.getResponseBodyAsStream();
            byte[] buffer = new byte[1024];
            while (in.read(buffer) != -1) {
                baos.write(buffer);
            }
        } finally {
            method.releaseConnection();
        }
        byte[] outBytes = baos.toByteArray();
        baos.close();
        baos = null;
        return outBytes;
    }
}
