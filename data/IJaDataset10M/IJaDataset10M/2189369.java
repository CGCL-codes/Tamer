package it.ansf.liferay.portlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class CasRestUtil {

    private static final Logger LOG = Logger.getLogger(CasRestUtil.class.getName());

    public static void sendUrl(String url) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet client = new HttpGet(url);
        try {
            HttpResponse response = httpclient.execute(client);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getTicket(final String server, final String username, final String password, final String service) throws ClientProtocolException, IOException {
        notNull(server, "server must not be null");
        notNull(username, "username must not be null");
        notNull(password, "password must not be null");
        notNull(service, "service must not be null");
        return getServiceTicket(server, getTicketGrantingTicket(server, username, password), service);
    }

    private static String getServiceTicket(final String server, final String ticketGrantingTicket, final String service) throws ClientProtocolException, IOException {
        if (ticketGrantingTicket == null) return null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost client = new HttpPost(server + "/" + ticketGrantingTicket);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("service", service));
        client.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpclient.execute(client);
        switch(response.getStatusLine().getStatusCode()) {
            case 200:
                return _getResponseBody(response.getEntity()).toString();
            default:
                LOG.warning("Invalid response code (" + response.getStatusLine().getStatusCode() + ") from CAS server!");
                LOG.info("Response (1k): " + response.toString().substring(0, Math.min(1024, response.toString().length())));
                break;
        }
        return null;
    }

    static String getTicketGrantingTicket(final String server, final String username, final String password) throws ClientProtocolException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(server);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("username", username));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse responseHttp = client.execute(post);
        String response = _getResponseBody(responseHttp.getEntity());
        switch(responseHttp.getStatusLine().getStatusCode()) {
            case 201:
                {
                    final Matcher matcher = Pattern.compile(".*action=\".*/(.*?)\".*").matcher(response);
                    if (matcher.matches()) return matcher.group(1);
                    LOG.warning("Successful ticket granting request, but no ticket found!");
                    LOG.info("Response (1k): " + response.substring(0, Math.min(1024, response.length())));
                    break;
                }
            default:
                LOG.warning("Invalid response code (" + responseHttp.getStatusLine().getStatusCode() + ") from CAS server!");
                LOG.info("Response (1k): " + response.substring(0, Math.min(1024, response.length())));
                break;
        }
        return null;
    }

    private static void notNull(final Object object, final String message) {
        if (object == null) throw new IllegalArgumentException(message);
    }

    public static String _getResponseBody(final HttpEntity entity) throws IOException, ParseException {
        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }
        InputStream instream = entity.getContent();
        if (instream == null) {
            return "";
        }
        if (entity.getContentLength() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
        }
        String charset = getContentCharSet(entity);
        if (charset == null) {
            charset = HTTP.DEFAULT_CONTENT_CHARSET;
        }
        Reader reader = new InputStreamReader(instream, charset);
        StringBuilder buffer = new StringBuilder();
        try {
            char[] tmp = new char[1024];
            int l;
            while ((l = reader.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
            }
        } finally {
            reader.close();
        }
        return buffer.toString();
    }

    public static String getContentCharSet(final HttpEntity entity) throws ParseException {
        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }
        String charset = null;
        if (entity.getContentType() != null) {
            HeaderElement values[] = entity.getContentType().getElements();
            if (values.length > 0) {
                NameValuePair param = values[0].getParameterByName("charset");
                if (param != null) {
                    charset = param.getValue();
                }
            }
        }
        return charset;
    }
}
