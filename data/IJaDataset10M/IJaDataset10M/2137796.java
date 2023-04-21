package com.google.health.examples.appengine.gdata;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.health.examples.appengine.Profile;
import com.google.health.examples.appengine.oauth.AuthenticationException;
import com.google.health.examples.appengine.oauth.OAuthService;

public class HealthClient {

    private GoogleService service;

    private OAuthService oauthAuthenticator;

    private Profile profile;

    /**
   * Matches the profile name and id in the Atom results from the Health profile
   * feed.
   */
    static final Pattern PROFILE_PATTERN = Pattern.compile("<title type='text'>([^<]*)</title><content type='text'>([\\w\\.]*)</content>");

    static final String ATOM_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\">";

    static final String ATOM_FOOTER = "</entry>";

    /** Params: title, content */
    static final String NOTICE = "<title type=\"text\">%s</title><content type=\"text\">%s</content>";

    static final String CCR_HEADER = "<ContinuityOfCareRecord xmlns=\"urn:astm-org:CCR\">";

    static final String CCR_FOOTER = "</ContinuityOfCareRecord>";

    public HealthClient(GoogleService service) {
        this.service = service;
    }

    public HealthClient(String serviceName) {
        if (GoogleService.H9.getServiceName().equals(serviceName)) {
            this.service = GoogleService.H9;
        } else if (GoogleService.HEALTH.getServiceName().equals(serviceName)) {
            this.service = GoogleService.HEALTH;
        } else {
            throw new IllegalArgumentException("Invalid service name. Expecting 'weaver' or 'health'.");
        }
    }

    public OAuthService getOAuthAuthenticator() {
        return oauthAuthenticator;
    }

    public void setOAuthAuthenticator(OAuthService oauthAuthenticator) {
        this.oauthAuthenticator = oauthAuthenticator;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Result> retrieveResults() throws AuthenticationException, IOException {
        String url = service.getBaseURL() + "/profile/default/-/labtest?digest=true";
        String data = getData(url);
        CCRResultsHandler ccrHandler = new CCRResultsHandler();
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            xr.setContentHandler(ccrHandler);
            xr.parse(new InputSource(new ByteArrayInputStream(data.getBytes())));
        } catch (ParserConfigurationException e) {
            return null;
        } catch (SAXException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return ccrHandler.getResults();
    }

    public Result createResult(Result result) throws AuthenticationException, IOException {
        String ccr = CCR_HEADER + "<Body><Results>" + result.toCCR() + "</Results></Body>" + CCR_FOOTER;
        String notice = String.format(NOTICE, "Health App Engine Example App data posted", "The Health App Egine Example App posted the following data to your profile:");
        String atom = ATOM_HEADER + notice + ccr + ATOM_FOOTER;
        String url = service.getBaseURL() + "/register/default";
        postData(url, atom);
        return result;
    }

    private String getData(String requestUrl) throws AuthenticationException, IOException {
        URL url = new URL(requestUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        String header;
        try {
            header = oauthAuthenticator.getHttpAuthorizationHeader(url.toString(), "GET", profile.getOAuthToken(), profile.getOAuthTokenSecret());
        } catch (OAuthException e) {
            throw new AuthenticationException(e);
        }
        conn.setRequestProperty("Authorization", header);
        if (conn.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            throw new AuthenticationException();
        }
        InputStreamReader reader = new InputStreamReader(conn.getInputStream());
        char[] buffer = new char[1024];
        int bytesRead = 0;
        StringBuilder data = new StringBuilder();
        while ((bytesRead = reader.read(buffer)) != -1) {
            data.append(buffer, 0, bytesRead);
        }
        reader.close();
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException(conn.getResponseCode() + " " + conn.getResponseMessage() + "\n" + data);
        }
        return data.toString();
    }

    private String postData(String requestUrl, String atom) throws AuthenticationException, IOException {
        URL url = new URL(requestUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        String header;
        try {
            header = oauthAuthenticator.getHttpAuthorizationHeader(url.toString(), "POST", profile.getOAuthToken(), profile.getOAuthTokenSecret());
        } catch (OAuthException e) {
            throw new AuthenticationException(e);
        }
        conn.setRequestProperty("Authorization", header);
        conn.setRequestProperty("Content-Type", "application/atom+xml");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        writer.write(atom);
        writer.close();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            throw new AuthenticationException();
        }
        InputStreamReader reader = new InputStreamReader(conn.getInputStream());
        char[] buffer = new char[1024];
        int bytesRead = 0;
        StringBuilder data = new StringBuilder();
        while ((bytesRead = reader.read(buffer)) != -1) {
            data.append(buffer, 0, bytesRead);
        }
        reader.close();
        if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
            throw new IOException(conn.getResponseCode() + " " + conn.getResponseMessage() + "\n" + data);
        }
        return data.toString();
    }
}
