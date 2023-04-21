package ws;

import basededatos.Productossp;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import logicacatalogo.ComunicacionProductos;

public class Catalogo {

    public Catalogo() {
    }

    public Hashtable getProductos() {
        final Context context;
        try {
            context = getInitialContext();
            ComunicacionProductos comunicacionProductos = (ComunicacionProductos) context.lookup("ComunicacionProductos/remote");
            return comunicacionProductos.getProductos();
        } catch (NamingException e) {
            return null;
        }
    }

    private static Context getInitialContext() throws NamingException {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        env.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:-Org.jnp.interfaces");
        env.put(Context.PROVIDER_URL, "jnp://localhost:1099");
        return new InitialContext(env);
    }
}
