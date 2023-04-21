package fr.cnes.sitools.proxy;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.representation.Representation;
import org.restlet.routing.Redirector;

/**
 * Proxy redirection if set in ProxySettings.
 * 
 * @author jp.boignard (AKKA Technologies)
 */
public final class RedirectorProxy extends Redirector {

    /**
   * Constructor
   * @param context restlet context
   * @param targetPattern target pattern
   * @param mode mode
   */
    public RedirectorProxy(Context context, String targetPattern, int mode) {
        super(context, targetPattern, mode);
    }

    /**
   * Constructor
   * @param context restlet context
   * @param targetTemplate targetTemplate
   */
    public RedirectorProxy(Context context, String targetTemplate) {
        super(context, targetTemplate);
    }

    @Override
    public void handle(Request request, Response response) {
        if ((ProxySettings.getProxyAuthentication() != null) && request.getProxyChallengeResponse() == null) {
            request.setProxyChallengeResponse(ProxySettings.getProxyAuthentication());
        }
        try {
            super.handle(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Optionally rewrites the response entity returned in the
   * {@link #MODE_SERVER_INBOUND} and {@link #MODE_SERVER_OUTBOUND} modes. By
   * default, it just returns the initial entity without any modification.
   * 
   * @param initialEntity
   *            The initial entity returned.
   * @return The rewritten entity.
   */
    protected Representation rewrite(Representation initialEntity) {
        return initialEntity;
    }
}
