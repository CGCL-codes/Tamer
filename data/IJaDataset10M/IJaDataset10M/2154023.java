package mecanismosdecomunicacion;

import comunicacion.Comunicacion;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ConexionEventos {

    public ConexionEventos() {
    }

    public Hashtable getEventos() {
        try {
            final Context context = getInitialContext();
            Comunicacion comunicacion = (Comunicacion) context.lookup("Comunicacion/remote");
            return comunicacion.getActividades();
        } catch (Exception ex) {
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
