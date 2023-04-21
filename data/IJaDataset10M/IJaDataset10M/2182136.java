package net.community.chest.web.servlet.framework;

import java.io.IOException;
import java.net.URI;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;

/**
 * <P>Copyright 2010 as per GPLv2</P>
 * 
 * Adds some set-ter(s) that are not available in the original interface
 * @author Lyor G.
 * @since Jun 15, 2010 10:02:01 AM
 */
public interface XServletRequest extends ServletRequest {

    Map<String, Object> getAttributesMap();

    void setContentLength(int value);

    void setContentType(String value);

    void setLocalAddr(String value);

    void setLocalName(String value);

    void setLocalPort(int value);

    void setRemotePort(int value);

    void setInputStream(ServletInputStream value) throws IOException;

    void setLocale(Locale value);

    void setProtocol(String value);

    void setRemoteAddr(String value);

    void setRemoteHost(String value);

    void setScheme(String value);

    void setServerName(String value);

    void setServerPort(int value);

    void setSecure(boolean value);

    void updateContents(URI uri);
}
