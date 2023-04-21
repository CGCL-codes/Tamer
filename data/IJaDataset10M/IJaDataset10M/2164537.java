package com.googlecode.webduff;

import javax.servlet.ServletException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import com.googlecode.webduff.authentication.WebdavAuthentication;
import com.googlecode.webduff.authentication.provider.WebdavAuthenticationProvider;
import com.googlecode.webduff.store.WebdavStoreFactory;

/**
 * Servlet which provides support for WebDAV level 2.
 * 
 * the original class is org.apache.catalina.servlets.WebdavServlet by Remy
 * Maucherat, which was heavily changed
 * 
 * @author Remy Maucherat
 * @param <T>
 */
public class WebDuffServlet extends WebDavServletBean {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(WebDuffServlet.class);

    private static final long serialVersionUID = 4615945957164027168L;

    private WebDuffConfiguration globalConfiguration;

    public void init() throws ServletException {
        try {
            globalConfiguration = new WebDuffConfiguration(getServletContext(), new XMLConfiguration(getServletContext().getRealPath(getInitParameter("WebDuffConfig"))));
        } catch (ConfigurationException e) {
            log.fatal("While reading the configuration", e);
        }
        WebdavStoreFactory webdavStoreFactory = globalConfiguration.<WebdavStoreFactory>getConfigurableComponent(globalConfiguration.subset("store"));
        WebdavAuthenticationProvider webdavAuthentication = globalConfiguration.<WebdavAuthenticationProvider>getConfigurableComponent(globalConfiguration.subset("authentication-provider"));
        WebdavAuthentication webdavAuthorization = globalConfiguration.<WebdavAuthentication>getConfigurableComponent(globalConfiguration.subset("authentication"));
        super.init(webdavStoreFactory, webdavAuthentication, webdavAuthorization, new DefaultMimeTyper(getServletContext()));
    }
}
