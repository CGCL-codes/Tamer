package org.jnp.interfaces.java;

import java.util.Hashtable;
import javax.naming.*;
import javax.naming.spi.*;
import org.jnp.interfaces.NamingContext;
import org.jnp.interfaces.Naming;

/**
 *   Implementation of "java:" namespace factory. The context is associated
 *   with the thread, so the root context must be set before this is used in a thread
 *      
 *   @author Rickard Oberg
 *   @version $Revision: 57199 $
 */
public class javaURLContextFactory implements ObjectFactory {

    private static ThreadLocal server = new ThreadLocal();

    public static void setRoot(Naming srv) {
        server.set(srv);
    }

    public static Naming getRoot() {
        return (Naming) server.get();
    }

    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment) throws Exception {
        if (obj == null) return new NamingContext(environment, name, (Naming) server.get()); else if (obj instanceof String) {
            String url = (String) obj;
            Context ctx = new NamingContext(environment, name, (Naming) server.get());
            Name n = ctx.getNameParser(name).parse(url.substring(url.indexOf(":") + 1));
            if (n.size() >= 3) {
                if (n.get(0).toString().equals("") && n.get(1).toString().equals("")) {
                    ctx.addToEnvironment(Context.PROVIDER_URL, n.get(2));
                }
            }
            return ctx;
        } else {
            return null;
        }
    }
}
