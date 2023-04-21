package com.abso.mp3tunes.locker.core.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import com.abso.mp3tunes.locker.core.AudioScrobblerServiceException;
import com.abso.mp3tunes.locker.core.data.AudioScrobblerHandshakeData;
import com.abso.mp3tunes.locker.core.data.Track;

/**
 * The AudioScrobbler submission service.
 */
public class AudioScrobblerService {

    /** HTTP connection time-out (in milliseconds). */
    public static final int TIMEOUT = 10000;

    /**
	 * Negotiates with the Audioscrobbler server to establish authentication and
	 * connection details for the session
	 * 
	 * @param username
	 *            the Last.fm username.
	 * @param password
	 *            the Last.fm password.
	 * @return the handshake data.
	 * @throws AudioScrobblerServiceException
	 *             if an error occurred performing the handshake.
	 */
    public AudioScrobblerHandshakeData handshake(String username, String password) throws AudioScrobblerServiceException {
        Map params = new HashMap();
        params.put("hs", "true");
        params.put("p", "1.2");
        params.put("c", "tst");
        params.put("v", "1.0");
        params.put("u", username);
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        params.put("t", timestamp);
        String md5pass = DigestUtils.md5Hex(password);
        String auth = DigestUtils.md5Hex(md5pass + timestamp);
        params.put("a", auth);
        String[] lines = StringUtils.split(executeGet(params), "\n");
        if (lines.length == 0) {
            throw new AudioScrobblerServiceException("Empty response");
        } else {
            String code = lines[0];
            if (code.equals("OK")) {
                if (lines.length < 4) {
                    throw new AudioScrobblerServiceException("Expecting OK response code followed by 3 further lines, but got " + (lines.length - 1) + " lines");
                } else {
                    AudioScrobblerHandshakeData data = new AudioScrobblerHandshakeData();
                    data.setSessionId(lines[1]);
                    data.setNowPlayingURL(lines[2]);
                    data.setSubmissionURL(lines[3]);
                    return data;
                }
            } else if (code.equals("BANNED")) {
                throw new AudioScrobblerServiceException("Banned client server: please update the client application");
            } else if (code.equals("BADAUTH")) {
                throw new AudioScrobblerServiceException("Authentication failed");
            } else if (code.equals("BADTIME")) {
                throw new AudioScrobblerServiceException("The timestamp provided was not close enough to the current time: please correct the system clock");
            } else if (code.startsWith("FAILED")) {
                throw new AudioScrobblerServiceException(StringUtils.substringAfter(code, "FAILED"));
            } else {
                throw new AudioScrobblerServiceException("Unrecognized code");
            }
        }
    }

    /**
	 * Submits a track.
	 * 
	 * @param handshakeData
	 *            the handshake data.
	 * @param track
	 *            the track being submitted.
	 * 
	 * @throws AudioScrobblerServiceException
	 *             if an error occurred sending the track.
	 */
    public void submit(AudioScrobblerHandshakeData handshakeData, Track track) throws AudioScrobblerServiceException {
        Map params = new HashMap();
        params.put("s", handshakeData.getSessionId());
        params.put("a[0]", track.getArtistName());
        params.put("t[0]", track.getTitle());
        long timestamp = (long) System.currentTimeMillis() / 1000;
        params.put("i[0]", String.valueOf(timestamp));
        params.put("o[0]", "P");
        params.put("r[0]", "L");
        int length = (int) track.getLength() / 1000;
        params.put("l[0]", String.valueOf(length));
        params.put("b[0]", track.getAlbumTitle());
        if (track.getNumber() > 0) {
            params.put("n[0]", String.valueOf(track.getNumber()));
        } else {
            params.put("n[0]", "");
        }
        params.put("m[0]", "");
        post(handshakeData.getSubmissionURL(), params);
    }

    /**
	 * Executes a REST method performing a GET request.
	 * 
	 * @param params
	 *            the set of parameters being appended to the secure request.
	 * @return the resulting string.
	 * @throws AudioScrobblerServiceException
	 *             if an error occurred executing the REST method.
	 */
    private String executeGet(Map params) throws AudioScrobblerServiceException {
        StringBuffer url = new StringBuffer("http://post.audioscrobbler.com/?");
        for (Iterator i = params.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry param = (Map.Entry) i.next();
            try {
                url.append(ServiceUtils.encodeURLParameter(ObjectUtils.toString(param.getKey())));
            } catch (UnsupportedEncodingException e) {
                throw new AudioScrobblerServiceException("Unable to encode parameter: " + param.getKey(), e);
            }
            url.append('=');
            try {
                url.append(ServiceUtils.encodeURLParameter(ObjectUtils.toString(param.getValue())));
            } catch (UnsupportedEncodingException e) {
                throw new AudioScrobblerServiceException("Unable to encode parameter: " + param.getValue(), e);
            }
            if (i.hasNext()) {
                url.append('&');
            }
        }
        SimpleHttpConnectionManager connMgr = new SimpleHttpConnectionManager();
        connMgr.getParams().setConnectionTimeout(TIMEOUT);
        connMgr.getParams().setSoTimeout(10000);
        connMgr.getParams().setParameter(HttpMethodParams.USER_AGENT, "MP3tunes Eclipse plug-in (http://abso.freehostia.com)");
        HttpMethodBase httpMethod = new GetMethod(url.toString());
        httpMethod.addRequestHeader("Host", "post.audioscrobbler.com");
        HttpClient httpClient = new HttpClient(connMgr);
        try {
            httpClient.executeMethod(httpMethod);
            return httpMethod.getResponseBodyAsString();
        } catch (HttpException e) {
            throw new AudioScrobblerServiceException("Unable to perform request: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new AudioScrobblerServiceException("Unable to perform request: " + e.getMessage(), e);
        } finally {
            httpMethod.releaseConnection();
        }
    }

    /**
	 * Performs a POST method.
	 * 
	 * @param url
	 *            the target URL.
	 * @param params
	 *            the set of parameters being appended to the secure request.
	 * @throws AudioScrobblerServiceException
	 *             if an error occurred executing the REST method.
	 */
    private void post(String url, Map params) throws AudioScrobblerServiceException {
        SimpleHttpConnectionManager connMgr = new SimpleHttpConnectionManager();
        connMgr.getParams().setConnectionTimeout(TIMEOUT);
        connMgr.getParams().setSoTimeout(TIMEOUT);
        connMgr.getParams().setParameter(HttpMethodParams.USER_AGENT, "MP3tunes Eclipse plug-in (http://abso.freehostia.com)");
        PostMethod postMethod = new PostMethod(url.toString());
        try {
            StringBuffer request = new StringBuffer();
            for (Iterator i = params.entrySet().iterator(); i.hasNext(); ) {
                Map.Entry param = (Map.Entry) i.next();
                String key = ObjectUtils.toString(param.getKey());
                String value = ObjectUtils.toString(param.getValue());
                request.append(key);
                request.append('=');
                request.append(ServiceUtils.encodeURLParameter(value));
                if (i.hasNext()) {
                    request.append("&");
                }
            }
            postMethod.setRequestEntity(new StringRequestEntity(request.toString(), "application/x-www-form-urlencoded", "UTF-8"));
            HttpClient httpClient = new HttpClient(connMgr);
            URL hostURL = new URL(url);
            postMethod.addRequestHeader("Host", hostURL.getHost());
            httpClient.executeMethod(postMethod);
        } catch (HttpException e) {
            throw new AudioScrobblerServiceException("Unable to perform request: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new AudioScrobblerServiceException("Unable to perform request: " + e.getMessage(), e);
        } finally {
            postMethod.releaseConnection();
        }
    }
}
