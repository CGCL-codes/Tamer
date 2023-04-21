package es.caib.bpm.connection;

import java.io.IOException;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.login.LoginException;

public class ConnectionManager {

    private ConnectionManager() throws IOException, NamingException {
        this.initComponents();
    }

    public static synchronized ConnectionManager getInstance() throws IOException, NamingException {
        if (INSTANCE == null) {
            INSTANCE = new ConnectionManager();
        }
        return INSTANCE;
    }

    public Context getContext() {
        return this.context;
    }

    private void initComponents() throws IOException, NamingException {
        Properties properties = null;
        properties = new Properties();
        this.context = new InitialContext();
    }

    /** El contexto */
    private Context context = null;

    private static ConnectionManager INSTANCE = null;
}
