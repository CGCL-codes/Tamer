package br.com.dimension.imap2rest;

import java.security.Security;
import java.util.Hashtable;
import java.util.Properties;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import org.mortbay.log.Logger;
import org.restlet.Guard;
import br.com.dimension.imap2rest.holders.ClientSession;

public class Imap2RestManager {

    static Imap2RestManager mg = null;

    Guard guard;

    Hashtable session;

    private Imap2RestManager() {
        session = new Hashtable();
    }

    public static Imap2RestManager getInstance() {
        if (mg == null) mg = new Imap2RestManager();
        return mg;
    }

    public void addSession(ClientSession s) {
        session.put(s.getHostname() + s.getUsername() + s.getPassword(), s);
    }

    public ClientSession findSession(String host, String user, String pass) {
        ClientSession cs = (ClientSession) session.get(host + user + pass);
        if (cs != null) {
            System.out.println("Found session for user " + cs.getUsername());
        }
        return cs;
    }

    public void setGuard(Guard g) {
        guard = g;
    }

    public Guard getGuard() {
        return guard;
    }

    public Session newSession() {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        Properties props = new Properties();
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.socketFactory.port", "993");
        props.setProperty("mail.imap.socketFactory.fallback", "false");
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
        Session session = Session.getInstance(props);
        return session;
    }

    public Store getStore(Session session) throws NoSuchProviderException {
        Store store = session.getStore("imap");
        return store;
    }
}
