package org.genwork.util.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import org.genwork.util.exception.Errors;
import org.genwork.util.exception.GenericException;

public class Connection {

    /** D�lai de connection maximal, utilis� si non pr�cis� dans la requ�te */
    private static final int DEFAULT_CONNECTION_TIMEOUT = 10;

    /** User Agent envoy� chaque requ�te, contient LisaWeb et sa version */
    private static final String USER_AGENT = "Genwork";

    /** Default encoding */
    private static final String DEFAULT_ENCODING = System.getProperty("file.encoding");

    private static final int MAX_SIZE = 1 * 1024 * 1024;

    static {
        System.setProperty("http.keepAlive", "false");
    }

    /**
	 * Ex�cute une requ�te HTTP en m�thode GET sur l'URL sp�cifi�e. 
	 * 
	 * @param url URL de la requ�te
	 * @return Une instance de HTTPResult
	 * @throws GenericException 
	 */
    public static HTTPResult get(String url) throws GenericException {
        return get(url, DEFAULT_CONNECTION_TIMEOUT);
    }

    /**
	 * Ex�cute une requ�te HTTP en m�thode GET sur l'URL sp�cifi�e. 
	 * 
	 * @param url URL de la requ�te
	 * @param timeout D�lai maximum de connection en secondes
	 * @return Une instance de HTTPResult
	 * @throws GenericException 
	 */
    public static HTTPResult get(String url, int timeout) throws GenericException {
        HttpURLConnection huc = null;
        try {
            huc = (HttpURLConnection) new URL(url).openConnection();
            huc.setReadTimeout(timeout * 1000);
            huc.setAllowUserInteraction(false);
            huc.setUseCaches(false);
            huc.setRequestProperty("User-Agent", USER_AGENT);
            huc.setRequestMethod("GET");
            huc.setDoInput(true);
            huc.connect();
            return getHTTPResult(huc);
        } catch (Exception e) {
            throw new GenericException(e, Errors.IO_HTTP_DOWNLOAD, url, "?");
        } finally {
            if (huc != null) huc.disconnect();
        }
    }

    ;

    private static HTTPResult getHTTPResult(HttpURLConnection huc) throws IOException {
        if (huc.getResponseCode() != HttpURLConnection.HTTP_OK) {
            return new HTTPResult(null, null, huc.getResponseCode());
        }
        InputStream is = huc.getInputStream();
        int contentLength = huc.getContentLength();
        byte[] content = null;
        if (contentLength != -1) {
            content = new byte[contentLength];
            int read = 0;
            while (read < huc.getContentLength()) {
                read += is.read(content, read, contentLength - read);
            }
        } else {
            byte[] temp = new byte[MAX_SIZE];
            int read = 0;
            while (is.available() != 0) {
                read += is.read(temp, read, MAX_SIZE - read);
            }
            content = new byte[read];
            System.arraycopy(temp, 0, content, 0, read);
        }
        is.close();
        HTTPResult result = new HTTPResult(content, huc.getContentType(), huc.getResponseCode());
        result.setContentDisposition(huc.getHeaderField("Content-Disposition"));
        return result;
    }

    public static HTTPResult post(String url, int timeout, String paramName, String paramValue) throws GenericException {
        HashMap<String, String> params = new HashMap<String, String>(1);
        params.put(paramName, paramValue);
        return post(url, DEFAULT_CONNECTION_TIMEOUT, params);
    }

    public static HTTPResult post(String url, String paramName, String paramValue) throws GenericException {
        return post(url, DEFAULT_CONNECTION_TIMEOUT, paramName, paramValue);
    }

    public static HTTPResult post(String url, HashMap<String, String> params) throws GenericException {
        return post(url, DEFAULT_CONNECTION_TIMEOUT, params);
    }

    public static HTTPResult post(String url, int timeout, HashMap<String, String> params) throws GenericException {
        HttpURLConnection huc = null;
        try {
            huc = (HttpURLConnection) new URL(url).openConnection();
            huc.setReadTimeout(timeout * 1000);
            huc.setAllowUserInteraction(false);
            huc.setUseCaches(false);
            huc.setRequestProperty("User-Agent", USER_AGENT);
            huc.setDoOutput(true);
            huc.setDoInput(true);
            huc.setRequestMethod("POST");
            String body = createBodyRequest(params);
            huc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            huc.setRequestProperty("Content-Length", String.valueOf(body.length()));
            PrintWriter pw = new PrintWriter(huc.getOutputStream());
            pw.write(body);
            pw.close();
            huc.connect();
            return getHTTPResult(huc);
        } catch (Exception e) {
            throw new GenericException(e, Errors.IO_HTTP_DOWNLOAD, url, "?");
        } finally {
            if (huc != null) huc.disconnect();
        }
    }

    private static String createBodyRequest(HashMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (String paramName : params.keySet()) {
            sb.append(paramName);
            sb.append("=");
            sb.append(URLEncode(params.get(paramName)));
            sb.append("&");
        }
        return sb.toString();
    }

    public static String URLEncode(String str) {
        try {
            return URLEncoder.encode(str, DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String URLDecode(String str) {
        try {
            return URLDecoder.decode(str, DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
