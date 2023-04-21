package com.tcmcguire.tapform.http;

import java.net.*;
import java.io.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.MultipartPostMethod;

/**
 *
 * @author tmcguire
 *
 * 10/24/09
 * - This is a conglomeration of changes to make this code compile. It
 *   hasn't been tested yet.
 */
public class HttpPost {

    URL u;

    public HttpPost(String url) throws MalformedURLException {
        this.u = new URL(url);
    }

    public int post(String s) throws IOException {
        HttpClient client = new HttpClient();
        MultipartPostMethod method = new MultipartPostMethod(u.toString());
        File file = new File(s);
        method.addParameter(s, file);
        client.executeMethod(method);
        String response = method.getResponseBodyAsString();
        System.out.println(response);
        method.releaseConnection();
        return 0;
    }

    public static void main(String args[]) {
        try {
            HttpPost httpPost = new HttpPost("http://tcmcguire.dyndns.org:8080/TapformUpload/TapformUpload");
            httpPost.post("c:/work/james-2.3.2/temp/iphone/ProgressNote.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File sent");
    }
}
