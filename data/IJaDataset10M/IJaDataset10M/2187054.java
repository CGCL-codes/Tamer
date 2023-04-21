package org.atricore.idbus.kernel.main.mediation.camel.component.http;

import org.apache.camel.Message;
import org.apache.camel.component.http.DefaultHttpBinding;
import org.apache.camel.component.http.HttpMessage;
import org.apache.camel.spi.HeaderFilterStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This is actually a CAMEL HTTP Binding extension, it's not related with mediation HTTP bindings
 *
 * @author <a href="mailto:sgonzalez@atricore.org">Sebastian Gonzalez Oyuela</a>
 * @version $Id$
 */
public class IDBusHttpBinding extends DefaultHttpBinding {

    private static final Log logger = LogFactory.getLog(IDBusHttpBinding.class);

    public IDBusHttpBinding() {
        super();
    }

    public IDBusHttpBinding(HeaderFilterStrategy headerFilterStrategy) {
        super(headerFilterStrategy);
    }

    @Override
    public void readRequest(HttpServletRequest httpServletRequest, HttpMessage httpMessage) {
        logger.trace("Reading HTTP Servlet Request");
        super.readRequest(httpServletRequest, httpMessage);
        if (httpServletRequest.getCookies() != null) {
            for (Cookie cookie : httpServletRequest.getCookies()) {
                logger.debug("Setting IDBus Cookie header for " + cookie.getName() + "=" + cookie.getValue());
                httpMessage.getHeaders().put("org.atricore.idbus.http.Cookie." + cookie.getName(), cookie.getValue());
            }
        }
        httpMessage.getHeaders().put("org.atricore.idbus.http.RequestURL", httpServletRequest.getRequestURL().toString());
        httpMessage.getHeaders().put("org.atricore.idbus.http.QueryString", httpServletRequest.getQueryString());
        logger.debug("Publishing HTTP Session as Camel header org.atricore.idbus.http.HttpSession");
        httpMessage.getHeaders().put("org.atricore.idbus.http.HttpSession", httpServletRequest.getSession(true));
    }

    @Override
    public void doWriteResponse(Message message, HttpServletResponse httpServletResponse) throws IOException {
        logger.debug("Writing HTTP Servlet Response");
        for (String key : message.getHeaders().keySet()) {
            String value = message.getHeader(key, String.class);
            if (getHeaderFilterStrategy() != null && getHeaderFilterStrategy().applyFilterToCamelHeaders(key, value)) {
                if (key.startsWith("org.atricore.idbus.http.Set-Cookie.")) {
                    String cookieName = key.substring("org.atricore.idbus.http.Set-Cookie.".length());
                    if (!cookieName.equals("JSESSIONID")) {
                        logger.debug("Setting HTTP Cookie " + cookieName + "=" + value);
                        httpServletResponse.addHeader("Set-Cookie", cookieName + "=" + value);
                    }
                }
            }
        }
        logger.trace("Writting HTTP Servlet Response");
        super.doWriteResponse(message, httpServletResponse);
    }
}
