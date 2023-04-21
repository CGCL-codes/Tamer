package net.dadajax.download;

/**
 * @author dadajax
 *
 */
public interface Cookie {

    /**
	 * Return a cookie as a string from given url web address.
	 * @return cookie as a string
	 */
    public String getCookie(String url);
}
