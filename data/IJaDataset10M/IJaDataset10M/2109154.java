package de.juwimm.cms.plugins.server;

import javax.servlet.http.Cookie;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id: Response.java 8 2009-02-15 08:54:54Z skulawik $
 */
public interface Response {

    public void addCookie(Cookie cookie);
}
