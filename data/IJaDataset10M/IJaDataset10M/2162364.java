package it.webscience.kpeople.client.activiti;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

/**
 * Classe bas per iol client HTTP da usare per la comunicazione con il 
 * workflow engin Activiti.
 */
public class BaseClient {

    /**
	 * Definisce lo username da utilizzare per effettuare l'autenticazione HTTP.
	 */
    protected String httpAuthUserName = "";

    /**
	 * Definisce la password da utilizzare per effettuare l'autenticazione HTTP.
	 */
    protected String httpAuthPassword = "";

    /** logger. */
    private Logger logger;

    /** Costruttore. */
    public BaseClient(String pHttpAuthUserName) {
        this(pHttpAuthUserName, IActivitiConstants.ACTIVITI_FAKE_HTTPAUTH_PASSWORD);
    }

    /** Costruttore. */
    public BaseClient(String pHttpAuthUserName, String pHttpAuthPassword) {
        this.httpAuthUserName = pHttpAuthUserName;
        this.httpAuthPassword = pHttpAuthPassword;
        logger = Logger.getLogger(this.getClass().getName());
    }

    /**
	 * Costruisce un client HTTP ed effettua l'autenticazione HTTP.
	 * @return DefaultHttpClient
	 */
    protected final DefaultHttpClient createClientAndAuthorize() {
        DefaultHttpClient dhc = new DefaultHttpClient();
        AuthScope authScope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT);
        UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials(httpAuthUserName, httpAuthPassword);
        dhc.getCredentialsProvider().setCredentials(authScope, usernamePasswordCredentials);
        return dhc;
    }
}
