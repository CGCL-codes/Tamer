package net.webassembletool.http;

import java.io.Serializable;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;

/**
 * Serializable implementation of {@link CookieStore}
 * 
 * @author Nicolas Richeton
 */
public class SerializableBasicCookieStore extends BasicCookieStore implements Serializable {

    /**
	 * Serial Id
	 */
    private static final long serialVersionUID = 5884817839252416275L;

    /** 
	 * @see org.apache.http.impl.client.BasicCookieStore#addCookie(org.apache.http.cookie.Cookie)
	 */
    @Override
    public synchronized void addCookie(Cookie cookie) {
        super.addCookie(new SerializableBasicClientCookie2(cookie));
    }
}
