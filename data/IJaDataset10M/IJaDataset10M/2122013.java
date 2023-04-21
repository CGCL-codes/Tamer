package nz.ac.massey.xmldad.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

/**
 * Sample client for posting requests to a web server.
 * @author Nathan C Jones
 * @version 0.1
 */
public class XmlDadClient {

    public String sendMessage(String message, String url) throws Exception {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(url);
        post.setRequestEntity(new StringRequestEntity(message, "application/xml", "UTF-8"));
        client.executeMethod(post);
        BufferedReader reader = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream()));
        StringBuffer text = new StringBuffer(1000);
        String line = reader.readLine();
        while (line != null) {
            text.append(line + "\n");
            line = reader.readLine();
        }
        return text.toString();
    }
}
