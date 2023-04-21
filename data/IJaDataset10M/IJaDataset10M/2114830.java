package net.dadajax.download;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import net.dadajax.model.LoginStorage;
import net.dadajax.model.SecureLoginStorage;
import org.apache.log4j.Logger;

/**
 * @author dadajax
 *
 */
public class RapidshareCookie implements Cookie {

    private static RapidshareCookie cookie;

    private String cookieString;

    private URL rapUrl;

    private LoginStorage loginStorage;

    private RapidshareCookie() {
        loginStorage = SecureLoginStorage.getInstance();
        loadCookie();
    }

    /**
	 * @return instance of RapidshareCookie.
	 */
    public static synchronized RapidshareCookie getInstance() {
        if (cookie == null) {
            cookie = new RapidshareCookie();
        }
        return cookie;
    }

    /**
	 * Load cookies from URL.
	 */
    private synchronized void loadCookie() {
        if (rapUrl == null) {
            return;
        }
        try {
            URLConnection urlCon = rapUrl.openConnection();
            urlCon.setDoInput(true);
            urlCon.setDoOutput(true);
            DataOutputStream output = new DataOutputStream(urlCon.getOutputStream());
            String login = loginStorage.getLogin(LoginStorage.SERVICE_RAPIDSHARE).getLogin();
            String password = loginStorage.getLogin(LoginStorage.SERVICE_RAPIDSHARE).getPassword();
            output.writeBytes("login=" + login + "&password=" + password);
            output.flush();
            output.close();
            String headerName = null;
            for (int i = 1; (headerName = urlCon.getHeaderFieldKey(i)) != null; i++) {
                if (headerName.equals("Set-Cookie")) {
                    cookieString = urlCon.getHeaderField(i);
                    Logger.getRootLogger().debug("cookie: " + cookieString);
                }
            }
        } catch (MalformedURLException e) {
            Logger.getRootLogger().error("bad url", e);
        } catch (IOException e) {
            Logger.getRootLogger().error("IO problem", e);
        }
    }

    @Override
    public synchronized String getCookie(String url) {
        if (rapUrl != null) {
            if (rapUrl.toString().equals(url)) {
                return cookieString;
            }
        }
        try {
            rapUrl = new URL(url);
            loadCookie();
        } catch (MalformedURLException e) {
            Logger.getRootLogger().error("cannot convert to url", e);
        }
        return cookieString;
    }
}
