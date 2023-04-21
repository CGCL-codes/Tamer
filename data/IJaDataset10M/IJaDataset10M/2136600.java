package net.sf.webphotos.locator;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

public class BasicEJBLocator {

    protected InitialContext cachedContext = null;

    public static String EJB_URL_PREFIXES = "org.jboss.naming";

    public static String EJB_CONTEXT_FACTORY = "org.jnp.interfaces.NamingContextFactory";

    public static String EJB_SERVER = "127.0.0.1:1099";

    private static Logger logger = Logger.getLogger(BasicEJBLocator.class);

    public void initEJBContext() {
        try {
            cachedContext = (InitialContext) getRemoteEJBsInitialContext();
        } catch (NamingException e) {
            logger.error("Erro fatal com context de EJBs", e);
        }
    }

    public InitialContext getEJBContext() {
        if (cachedContext == null) {
            initEJBContext();
        }
        return cachedContext;
    }

    public Context getRemoteEJBsInitialContext() throws NamingException {
        Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(Context.INITIAL_CONTEXT_FACTORY, EJB_CONTEXT_FACTORY);
        props.put(Context.PROVIDER_URL, EJB_SERVER);
        props.put(Context.URL_PKG_PREFIXES, EJB_URL_PREFIXES);
        return new InitialContext(props);
    }
}
