package nur.aini.REST;

import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * @author Nur Aini Rakhmawati
 * @since 30 January 2011
 */
public class RestConnection {

    private final String host;

    private String message = null;

    public RestConnection(final String hostURL) {
        this.host = hostURL;
    }

    public boolean postData(String url, NameValuePair[] data) {
        boolean status = false;
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod();
        try {
            post.setURI(new URI(this.host + url, false));
            post.setRequestBody(data);
            if (client.executeMethod(post) != HttpStatus.SC_OK) {
                this.message = post.getStatusText();
            } else status = true;
        } catch (URIException e) {
            this.message = e.getMessage();
        } catch (NullPointerException e) {
            this.message = e.getMessage();
        } catch (HttpException e) {
            this.message = e.getMessage();
        } catch (IOException e) {
            this.message = e.getMessage();
        } finally {
            post.releaseConnection();
        }
        return status;
    }

    public boolean getData(String url) {
        boolean status = false;
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod(this.host + url);
        get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        try {
            if (client.executeMethod(get) != HttpStatus.SC_OK) {
                this.message = get.getStatusText();
            } else {
                status = true;
                StringBuffer responseBuffer = new StringBuffer();
                InputStreamReader stream = new InputStreamReader(get.getResponseBodyAsStream(), "UTF-8");
                int data = stream.read();
                while (data != -1) {
                    responseBuffer.append((char) data);
                    data = stream.read();
                }
                this.message = responseBuffer.toString();
                stream.close();
            }
        } catch (HttpException e) {
            this.message = e.getMessage();
        } catch (IOException e) {
            this.message = e.getMessage();
        } finally {
            get.releaseConnection();
        }
        return status;
    }

    public String getMessage() {
        return this.message;
    }

    public static void main(String[] args) {
        RestConnection restconn = new RestConnection("http://yyyy");
        if (restconn.getData("xxx")) System.out.println("hasil get Data" + restconn.getMessage()); else System.out.println("Error saat get Data" + restconn.getMessage());
        final NameValuePair[] nvp = { new NameValuePair("user", "iin"), new NameValuePair("password", "pass") };
        if (restconn.postData("xxx", nvp)) System.out.println("hasil post Data" + restconn.getMessage()); else System.out.println("Error saat post Data" + restconn.getMessage());
    }
}
