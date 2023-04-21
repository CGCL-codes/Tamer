package uk.org.primrose.jndi;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

public class PrimroseInitialContextFactory implements InitialContextFactory {

    /** 
     * Creates an Initial Context for beginning name resolution. 
     */
    static Context ctx;

    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
        if (ctx == null) {
            ctx = new PrimroseInitialContext();
        }
        return ctx;
    }
}
