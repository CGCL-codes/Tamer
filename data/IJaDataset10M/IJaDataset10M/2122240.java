package edu.uiuc.ncsa.myproxy.delegation.server.servlet;

import edu.uiuc.ncsa.myproxy.delegation.server.*;
import edu.uiuc.ncsa.security.core.cache.Cleanup;
import edu.uiuc.ncsa.security.core.cache.ValidTimestampPolicy;
import edu.uiuc.ncsa.security.core.exceptions.ConfigurationException;
import edu.uiuc.ncsa.security.core.exceptions.GeneralException;
import edu.uiuc.ncsa.security.delegation.server.issuers.AccessTokenIssuer;
import edu.uiuc.ncsa.security.delegation.server.issuers.AuthorizationGrantIssuer;
import edu.uiuc.ncsa.security.delegation.storage.Client;
import edu.uiuc.ncsa.security.delegation.storage.TransactionStore;
import edu.uiuc.ncsa.security.delegation.storage.impl.BasicTransaction;
import edu.uiuc.ncsa.security.delegation.token.AuthorizationGrant;
import edu.uiuc.ncsa.security.servlet.AbstractServlet;
import edu.uiuc.ncsa.security.servlet.mail.MailUtil;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import static edu.uiuc.ncsa.myproxy.delegation.server.DSConfiguration.*;

/**
 * <p>Created by Jeff Gaynor<br>
 * on May 17, 2011 at  3:46:53 PM
 */
public class MyProxyDelegationServlet extends AbstractServlet {

    static Cleanup<String, BasicTransaction> transactionCleanup;

    public static final String CERT_REQUEST_KEY = "certreq";

    public static final String CERT_LIFETIME_KEY = "certlifetime";

    /**
     * The key for the token.
     */
    public static final String TOKEN_KEY = "oauth_token";

    public static final String CONSUMER_KEY = "oauth_consumer_key";

    @Override
    protected void doIt(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Throwable {
    }

    public static void loadProperties(DSConfiguration dsConfiguration, Properties properties) {
        String debug = properties.getProperty(ENABLE_DEBUG);
        dsConfiguration.setDebugOn(false);
        if (debug != null && 0 < debug.length()) {
            debug = debug.toLowerCase();
            try {
                dsConfiguration.setDebugOn(Boolean.parseBoolean(debug));
            } catch (Throwable t) {
            }
        }
        dsConfiguration.setDatabaseHost(properties.getProperty(DATABASE_HOST, "localhost"));
        dsConfiguration.setApproverPassword(properties.getProperty(APPROVER_PASSWORD_KEY));
        dsConfiguration.setApproverUsername(properties.getProperty(APPROVER_USERNAME));
        dsConfiguration.setClientPassword(properties.getProperty(CLIENT_PASSWORD_KEY));
        dsConfiguration.setClientUsername(properties.getProperty(CLIENT_USERNAME));
        dsConfiguration.setServicePassword(properties.getProperty(SERVICE_PASSWORD_KEY));
        dsConfiguration.setServiceUsername(properties.getProperty(SERVICE_USERNAME));
        if (properties.getProperty(SERVICE_ADDRESS) != null) {
            dsConfiguration.setServiceAddress(properties.getProperty(SERVICE_ADDRESS));
        }
        if (properties.getProperty(MYPROXY_SERVICE_ADDRESS) != null) {
            dsConfiguration.setMyProxyServer(properties.getProperty(MYPROXY_SERVICE_ADDRESS));
        }
        if (properties.getProperty(MYPROXY_SERVICE_PORT) != null) {
            try {
                int x = Integer.parseInt(properties.getProperty(MYPROXY_SERVICE_PORT));
                dsConfiguration.setMyProxyPort(x);
            } catch (Throwable t) {
                System.out.println("Warning: could not set MyProxy service port to \"" + properties.getProperty(MYPROXY_SERVICE_PORT) + "\"");
            }
        }
    }

    @Override
    public void loadEnvironment() throws IOException {
        if (portalEnvironment == null) {
            info("XUPServlet: setting runtime environment");
            InputStream is = null;
            portalEnvironment = new ServiceEnvironment();
            String configFile = getServletContext().getInitParameter(PROPERTY_FILE_DOMAIN);
            if (configFile != null && configFile.length() != 0) {
                info("setting configuration from " + configFile);
                is = new FileInputStream(configFile);
            } else {
                is = getServletContext().getResourceAsStream(CONFIG_FILE_PATH + CONFIG_FILE_NAME);
            }
            if (is == null) {
                throw new ConfigurationException("Error:No configuration found.");
            }
            DSConfiguration dsConfiguration = new DSConfiguration();
            Properties properties = new Properties();
            properties.load(is);
            is.close();
            loadProperties(dsConfiguration, properties);
            setDebugOn(dsConfiguration.isDebugOn());
            portalEnvironment.setConfiguration(dsConfiguration);
            info("Setting up mail utils");
            portalEnvironment.setMailUtil(new MailUtil(properties));
            portalEnvironment.getMailUtil().getMailEnvironment().setDebugOn(isDebugOn());
        }
    }

    protected AuthorizationGrantIssuer getAGI() throws IOException {
        return getServiceEnvironment().getAuthorizationGrantIssuer();
    }

    protected AccessTokenIssuer getATI() throws IOException {
        return getServiceEnvironment().getAccessTokenIssuer();
    }

    public static void setPortalEnvironment(ServiceEnvironment portalEnvironment) {
        MyProxyDelegationServlet.portalEnvironment = portalEnvironment;
    }

    static ServiceEnvironment portalEnvironment;

    public static ServiceEnvironment getServiceEnvironment() {
        if (portalEnvironment == null) {
            portalEnvironment = new ServiceEnvironment();
        }
        return portalEnvironment;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        RefFactory.getFactory();
        try {
            loadEnvironment();
        } catch (IOException e) {
            throw new ServletException("Error: could not load environment", e);
        }
        if (transactionCleanup == null) {
            transactionCleanup = new Cleanup<String, BasicTransaction>();
            transactionCleanup.setStopThread(false);
            transactionCleanup.setMap(getTransactionStore());
            transactionCleanup.addRetentionPolicy(new ValidTimestampPolicy());
            transactionCleanup.start();
            info("Starting transaction store cleanup thread");
        }
    }

    protected void shutdownCleanup(Cleanup c) {
        if (c != null && !c.isStopThread()) {
            c.setStopThread(true);
            c.interrupt();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        shutdownCleanup(transactionCleanup);
    }

    TransactionStore getTransactionStore() {
        return getServiceEnvironment().getTransactionStore();
    }

    public DSClient getClient(HttpServletRequest req) throws ServletException {
        String id = req.getParameter(CONSUMER_KEY);
        return getClient(id);
    }

    public DSClient getClient(String identifier) throws ServletException {
        if (identifier == null) {
            throw new ServletException("Error, no id found for client");
        }
        DSClient c = getServiceEnvironment().getClientStore().get(identifier);
        if (c == null) {
            throw new GeneralException("Error: There is no client associated with id \"" + identifier + "\"");
        }
        return c;
    }

    public DSTransaction newTransaction() {
        return getServiceEnvironment().getTransactionStore().create();
    }

    protected DSTransaction getTransaction(AuthorizationGrant grant) {
        return (DSTransaction) getTransactionStore().get(grant);
    }

    /**
     * A utility to get the client from the authorization grant. This looks up the transaction
     *
     * @param authorizationGrant
     * @return
     */
    protected DSClient getClient(AuthorizationGrant authorizationGrant) {
        DSTransaction transaction = getTransaction(authorizationGrant);
        return transaction.getClient();
    }

    /**
     * Checks if the client is approved. This should be done before each leg of the process
     *
     * @param client
     */
    public void checkClient(Client client) {
        if (!getServiceEnvironment().getClientApprovalStore().isApproved(client.getIdentifier())) {
            String ww = "client with identifier \"" + client.getIdentifier() + "\" has not been approved. Request rejected. Please contact your administrator.";
            warn(ww);
            throw new DSException("Error: " + ww);
        }
    }

    protected boolean isEmpty(String x) {
        return x == null || x.length() == 0;
    }
}
