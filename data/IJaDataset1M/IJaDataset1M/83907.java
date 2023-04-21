package net.sourceforge.pharos.plugin;

import net.sourceforge.pharos.security.CSRFTokenGenerator;
import javax.servlet.http.HttpServletRequest;

/**
 * @author kaushikr
 *
 */
public final class YakshaCSRFTokenGenerator implements CSRFTokenGenerator {

    /**
	 * @see net.sourceforge.pharos.security.CSRFTokenGenerator#getNextToken(javax.servlet.http.HttpServletRequest)
	 */
    public String getNextToken(HttpServletRequest request) {
        return "";
    }
}
