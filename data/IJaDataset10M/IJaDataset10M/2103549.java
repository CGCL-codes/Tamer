package com.joe.test;

import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

public class GzipTest {

    /**
	 * @param args
	 * @throws IOException
	 * @throws HttpException
	 */
    public static void main(String[] args) throws HttpException, IOException {
        HttpClient http = new HttpClient();
        GetMethod get = new GetMethod("http://localhost:82/interlib/admin/javascript/ext-all.js");
        try {
            get.addRequestHeader("accept-encoding", "gzip,deflate");
            get.addRequestHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0; Alexa Toolbar; Maxthon 2.0)");
            int er = http.executeMethod(get);
            if (er == 200) {
                System.out.println(get.getResponseContentLength());
                String html = get.getResponseBodyAsString();
                System.out.println(html.getBytes().length + " bytes, " + (html.getBytes().length / 1024) + " kb.");
            }
        } finally {
            get.releaseConnection();
        }
    }
}
