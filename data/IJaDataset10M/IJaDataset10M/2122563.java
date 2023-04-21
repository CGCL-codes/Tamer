package de.cologneintelligence.fitgoodies.mail;

import java.util.Properties;

/**
 * Singleton class that holds information on how to access mails.
 * Can be set via HTML using the {@link SetupFixture}.
 *
 * @author jwierum
 * @version $Id: SetupHelper.java 46 2011-09-04 14:59:16Z jochen_wierum $
 */
public final class SetupHelper {

    private static SetupHelper instance;

    private String user;

    private String pass;

    private String proto;

    private String host;

    private String inbox;

    private boolean ssl;

    private int port;

    private SetupHelper() {
    }

    /**
	 * Returns the singleton instance.
	 * @return instance of <code>SetupHelper</code>
	 */
    public static SetupHelper instance() {
        if (instance == null) {
            instance = new SetupHelper();
        }
        return instance;
    }

    /**
	 * Resets the internal state of the singleton.
	 */
    public static void reset() {
        instance = null;
    }

    private void setProperty(final Properties prop, final String name, final String value) {
        if (value != null) {
            prop.setProperty(name, value);
        }
    }

    /**
	 * Generates a properties object which can be used by
	 * {@link de.cologneintelligence.fitgoodies.mail.providers.JavaMailMessageProvider}.
	 * Default values are not set.
	 *
	 * @return properties object
	 */
    public Properties generateProperties() {
        Properties result = new Properties();
        if (proto == null) {
            throw new RuntimeException("no protocol selected");
        }
        String protocol = proto.toLowerCase();
        setProperty(result, "mail.store.protocol", protocol);
        setProperty(result, "mail." + protocol + ".host", host);
        setProperty(result, "mail.username", user);
        setProperty(result, "mail.password", pass);
        if (port != 0) {
            setProperty(result, "mail." + protocol + ".port", Integer.toString(port));
        }
        if (ssl) {
            setProperty(result, "mail." + protocol + ".ssl", "true");
        }
        if (protocol.equals("pop3")) {
            setProperty(result, "mail.inbox", "INBOX");
        } else {
            if (inbox == null) {
                throw new RuntimeException("no inbox selected");
            }
            setProperty(result, "mail.inbox", inbox);
        }
        return result;
    }

    /**
	 * Sets the protocol to use.
	 * @param protocol protocol to use
	 */
    public void setProtocol(final String protocol) {
        proto = protocol;
    }

    /**
	 * Sets the user name to use.
	 * @param username user name to use
	 */
    public void setUsername(final String username) {
        user = username;
    }

    /**
	 * Sets the password to use.
	 * @param password the password to use
	 */
    public void setPassword(final String password) {
        pass = password;
    }

    /**
	 * Sets the hostname to use.
	 * @param hostname hostname to use
	 */
    public void setHost(final String hostname) {
        host = hostname;
    }

    /**
	 * Sets the inbox to use.
	 * @param inboxname inbox to use
	 */
    public void setInbox(final String inboxname) {
        inbox = inboxname;
    }

    /**
	 * Sets whether SSL will be used.
	 * @param enable sets the SSL state
	 */
    public void setSSL(final boolean enable) {
        ssl = enable;
    }

    /**
	 * Sets the port that will be used.
	 * @param port port to use
	 */
    public void setPort(final int port) {
        this.port = port;
    }
}
