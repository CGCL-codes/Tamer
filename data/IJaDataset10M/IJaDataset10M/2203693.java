package edu.uiuc.ncsa.myproxy.delegation.server.servlet;

import edu.uiuc.ncsa.myproxy.delegation.server.DSClient;
import edu.uiuc.ncsa.myproxy.delegation.server.DSException;
import edu.uiuc.ncsa.myproxy.delegation.server.DSTransaction;
import edu.uiuc.ncsa.security.delegation.server.request.AGRequest;
import edu.uiuc.ncsa.security.delegation.server.request.AGResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Map;
import static edu.uiuc.ncsa.security.oauth_1_0a.OAuthConstants.OAUTH_CALLBACK;
import static edu.uiuc.ncsa.security.util.pkcs.CertUtil.fromStringToCertReq;

/**
 * <p>Created by Jeff Gaynor<br>
 * on May 17, 2011 at  3:38:58 PM
 */
public class InitServlet extends MyProxyDelegationServlet {

    @Override
    protected void doIt(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Throwable {
        info("2.a. Starting a new cert request");
        DSClient client = getClient(httpServletRequest);
        checkClient(client);
        String cid = "for client=" + client.getIdentifier();
        info(cid);
        AGResponse agResponse = (AGResponse) getAGI().process(new AGRequest(httpServletRequest, client));
        Map<String, String> params = agResponse.getParameters();
        String certReq = params.get(CERT_REQUEST_KEY);
        if (isEmpty(certReq)) {
            throw new DSException("Error: No cert request");
        }
        String certLifetimeString = params.get(CERT_LIFETIME_KEY);
        long certLifetime = 0;
        if (!isEmpty(certLifetimeString)) {
            try {
                certLifetime = Long.parseLong(certLifetimeString) * 1000;
                if (certLifetime < 0) {
                    certLifetime = 0L;
                }
            } catch (NumberFormatException x) {
                info("No/bad cert lifetime found (" + certLifetimeString + "), using default. " + cid);
            }
        }
        String callback = params.get(OAUTH_CALLBACK);
        if (isEmpty(callback)) {
            throw new DSException("Error: No callback specified");
        }
        URI cb = URI.create(callback);
        if (cb.getScheme() == null || !cb.getScheme().equals("https")) {
            throw new DSException("Error: unsupported protocol in the callback. It must be https");
        }
        DSTransaction transaction = newTransaction();
        transaction.setAuthorizationGrant(agResponse.getGrant());
        debug("creating transaction for " + cid + ", trans id=" + transaction.getIdentifier());
        transaction.setClient(client);
        transaction.setAuthGrantValid(true);
        transaction.setAccessTokenValid(false);
        transaction.setCallback(URI.create(callback));
        transaction.setCertReq(fromStringToCertReq(certReq));
        transaction.setLifetime(certLifetime);
        getTransactionStore().save(transaction);
        debug("saved transaction for " + cid + ", trans id=" + transaction.getIdentifier());
        agResponse.write(httpServletResponse);
        info("2.b finished initial request for token =\"" + transaction.getIdentifier() + "\".");
    }
}
