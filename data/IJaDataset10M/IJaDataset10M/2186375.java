package com.dropbox.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.eclipse.core.runtime.Path;
import org.json.simple.JSONValue;

/**
 * The DropboxClient is the core of the Java API, and is designed to be both
 * instructive and easy to use. If you find yourself needing to do more than
 * what's provided, use the code here as a guide to implement your own
 * alternatives.
 * 
 * Before you can work with DropboxClient you'll need to configure an
 * Authenticator with a working OAuth access token. Typically this is done like
 * this:
 * 
 * <pre>
 * Map config = Authenticator.loadConfig(&quot;config/testing.json&quot;);
 * Authenticator auth = new Authenticator(config);
 * String url = auth
 * 		.retrieveRequestToken(&quot;http://mysite.com/theyaredone?blah=blah&quot;);
 * // bounce them to the URL
 * auth.retrieveAccessToken(&quot;&quot;);
 * String access_key = auth.getTokenKey();
 * String access_secret = auth.getTokenSecret();
 * // store those so that you can put them in config and not have to do the above
 * // again
 * </pre>
 * 
 * Once you've done the above, or configured a config Map with
 * "access_token_key" and "access_token_secret" parameters, then you can create
 * a DropboxClient object to work with:
 * 
 * <pre>
 * DropboxClient client = new DropboxClient(config, auth);
 * </pre>
 * 
 * And that should be it. For every operation you need to catch the
 * DropboxException, but that's the only additional complexity.
 */
public class DropboxClient extends RESTUtility {

    public Authenticator auth;

    public String api_host = null;

    public String content_host = null;

    public int port = 80;

    /**
	 * This is set by Dropbox to indicate what version of the API you are using.
	 */
    public static final int API_VERSION = 0;

    /**
	 * Config needs to have settings for "server", "content_server", and "port"
	 * (which should be a Long), and auth should be ready to work with an access
	 * key.
	 */
    @SuppressWarnings("unchecked")
    public DropboxClient(Map config, Authenticator auth) {
        assert auth != null : "You must provide a valid authenticator.";
        assert auth.getTokenKey() != null : "You must provide an authenticator that has been authorized.";
        this.auth = auth;
        api_host = (String) config.get("server");
        content_host = (String) config.get("content_server");
        port = Integer.parseInt((String) config.get("port"));
        assert api_host != null : "You must configure the server.";
        assert content_host != null : "You must configure the content_server.";
    }

    /**
	 * The account/info API call to Dropbox for getting info about an account
	 * attached to the access token.
	 */
    @SuppressWarnings("unchecked")
    public Map accountInfo(boolean status_in_response, String callback) throws DropboxException {
        Object[] params = { "status_in_response", "" + status_in_response, "callback", callback };
        return (Map) request("GET", defaultProtocol, api_host, port, "/account/info", API_VERSION, params, auth);
    }

    /**
	 * Copy a file from one path to another, with root being either "sandbox" or
	 * "dropbox".
	 */
    @SuppressWarnings("unchecked")
    public Map fileCopy(String root, String from_path, String to_path, String callback) throws DropboxException {
        Object[] params = { "root", root, "from_path", from_path, "to_path", to_path, "callback", callback };
        return (Map) request("POST", defaultProtocol, api_host, port, "/fileops/copy", API_VERSION, params, auth);
    }

    /** Create a folder at the given path. */
    @SuppressWarnings("unchecked")
    public Map fileCreateFolder(String root, String path, String callback) throws DropboxException {
        Object[] params = { "root", root, "path", path, "callback", callback };
        return (Map) request("POST", defaultProtocol, api_host, port, "/fileops/create_folder", API_VERSION, params, auth);
    }

    /** Delete a file. */
    @SuppressWarnings("unchecked")
    public Map fileDelete(String root, String path, String callback) throws DropboxException {
        Object[] params = { "root", root, "path", path, "callback", callback };
        return (Map) request("POST", defaultProtocol, api_host, port, "/fileops/delete", API_VERSION, params, auth);
    }

    /** Move a file. */
    @SuppressWarnings("unchecked")
    public Map fileMove(String root, String from_path, String to_path, String callback) throws DropboxException {
        Object[] params = { "root", root, "from_path", from_path, "to_path", to_path, "callback", callback };
        return (Map) request("POST", defaultProtocol, api_host, port, "/fileops/move", API_VERSION, params, auth);
    }

    /**
	 * Get a file from the content server, returning the raw Apache HTTP
	 * Components response object so you can stream it or work with it how you
	 * need. You *must* call .getEntity().consumeContent() on the returned
	 * HttpResponse object or you might leak connections.
	 */
    public HttpResponse getFile(String root, String from_path) throws DropboxException {
        return getFileWithVersion(root, from_path, null);
    }

    /**
	 * Get a file from the content server, returning the raw Apache HTTP
	 * Components response object so you can stream it or work with it how you
	 * need. You *must* call .getEntity().consumeContent() on the returned
	 * HttpResponse object or you might leak connections.
	 */
    public HttpResponse getFileWithVersion(String root, String from_path, String etag) throws DropboxException {
        String path = "/files/" + root + from_path;
        try {
            String target = buildFullURL(secureProtocol, content_host, port, buildURL(path, API_VERSION, null));
            HttpClient client = getClient(target);
            HttpGet req = new HttpGet(target);
            if (etag != null) {
                req.addHeader("If-None-Match", etag);
            }
            auth.sign(req);
            return client.execute(req);
        } catch (Exception e) {
            throw new DropboxException(e);
        }
    }

    /**
	 * Does not actually talk to Dropbox, but instead crafts a properly
	 * formatted URL that you can put into your UI which will link a user to
	 * that file in their own account. They will need to login to their Dropbox
	 * account on the website to access the file.
	 */
    public String links(String root, String path) throws DropboxException {
        String url_path = "/links/" + root + path;
        return buildFullURL(defaultProtocol, api_host, port, buildURL(url_path, API_VERSION, null));
    }

    /**
	 * Get metadata about directories and files, such as file listings and such.
	 */
    @SuppressWarnings("unchecked")
    public Map metadata(String root, String path, int file_limit, String hash, boolean list, boolean status_in_response, String callback) throws DropboxException {
        Object[] params = { "file_limit", "" + file_limit, "hash", hash, "list", "" + list, "status_in_response", "" + status_in_response, "callback", callback };
        String url_path = "/files/" + root + path;
        return (Map) request("GET", defaultProtocol, api_host, port, url_path, API_VERSION, params, auth);
    }

    @SuppressWarnings("unchecked")
    public Map eventMetadata(String root, Map target_events) throws DropboxException {
        String jsonText = JSONValue.toJSONString(target_events);
        Object[] params = { "root", root, "target_events", jsonText };
        return (Map) request("POST", defaultProtocol, api_host, port, "/event_metadata", API_VERSION, params, auth);
    }

    /**
	 * You *must* call .getEntity().consumeContent() on the returned
	 * HttpResponse object or you might leak connections.
	 */
    public HttpResponse eventContent(String root, int user_id, int namespace_id, int journal_id) throws DropboxException {
        String path = "/event_content";
        Object[] params = { "root", root, "target_event", "" + user_id + ":" + namespace_id + ":" + journal_id };
        try {
            String target = buildFullURL(defaultProtocol, content_host, port, buildURL(path, API_VERSION, params));
            HttpClient client = getClient(target);
            HttpGet req = new HttpGet(target);
            auth.sign(req);
            return client.execute(req);
        } catch (Exception e) {
            throw new DropboxException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public boolean eventContentAvailable(Map md) {
        return md.get("error") == null && ((Boolean) md.get("latest")).booleanValue() == true && ((Integer) md.get("size")).intValue() != -1 && ((Boolean) md.get("is_dir")).booleanValue() == false;
    }

    /**
	 * Put a file in the user's Dropbox.
	 */
    @SuppressWarnings("unchecked")
    public HttpResponse putFile(String root, String to_path, File file_obj) throws DropboxException {
        String path = "/files/" + root + to_path;
        try {
            Path targetPath = new Path(path);
            String target = buildFullURL(secureProtocol, content_host, port, buildURL(targetPath.removeLastSegments(1).addTrailingSeparator().toString(), API_VERSION, null));
            HttpClient client = getClient(target);
            HttpPost req = new HttpPost(target);
            List nvps = new ArrayList();
            nvps.add(new BasicNameValuePair("file", targetPath.lastSegment()));
            req.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            auth.sign(req);
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            FileBody bin = new FileBody(file_obj, targetPath.lastSegment(), "application/octet-stream", null);
            entity.addPart("file", bin);
            req.setEntity(entity);
            HttpResponse resp = client.execute(req);
            resp.getEntity().consumeContent();
            return resp;
        } catch (Exception e) {
            throw new DropboxException(e);
        }
    }
}
