package org.exjello.mail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import static org.exjello.mail.ExchangeConstants.CONNECTION_TIMEOUT_PROPERTY;
import static org.exjello.mail.ExchangeConstants.DELETE_PROPERTY;
import static org.exjello.mail.ExchangeConstants.FROM_PROPERTY;
import static org.exjello.mail.ExchangeConstants.LIMIT_PROPERTY;
import static org.exjello.mail.ExchangeConstants.LOCAL_ADDRESS_PROPERTY;
import static org.exjello.mail.ExchangeConstants.MAILBOX_PROPERTY;
import static org.exjello.mail.ExchangeConstants.PORT_PROPERTY;
import static org.exjello.mail.ExchangeConstants.SSL_PROPERTY;
import static org.exjello.mail.ExchangeConstants.TIMEOUT_PROPERTY;
import static org.exjello.mail.ExchangeConstants.UNFILTERED_PROPERTY;

class Exchange2007Connection implements ExchangeConnection {

    private static final String DEBUG_PASSWORD_PROPERTY = "org.exjello.mail.debug.password";

    private static final int HTTP_PORT = 80;

    private static final int HTTPS_PORT = 443;

    private final Session session;

    private final String server;

    private final String mailbox;

    private final String username;

    private final String password;

    private final int timeout;

    private final int connectionTimeout;

    private final InetAddress localAddress;

    private final boolean unfiltered;

    private final String filterLastCheck;

    private final String filterFrom;

    private final String filterNotFrom;

    private final String filterTo;

    private final boolean delete;

    private final int limit;

    public static Exchange2007Connection createConnection(String protocol, Session session, String host, int port, String username, String password) throws Exception {
        String prefix = "mail." + protocol.toLowerCase() + ".";
        boolean debugPassword = Boolean.parseBoolean(session.getProperty(DEBUG_PASSWORD_PROPERTY));
        String pwd = (password == null) ? null : debugPassword ? password : "<password>";
        if (host == null || username == null || password == null) {
            if (session.getDebug()) {
                session.getDebugOut().println("Missing parameter; host=\"" + host + "\",username=\"" + username + "\",password=\"" + pwd + "\"");
            }
            throw new IllegalStateException("Host, username, and password must be specified.");
        }
        boolean unfiltered = Boolean.parseBoolean(session.getProperty(UNFILTERED_PROPERTY));
        String filterLastCheck = session.getProperty(ExchangeConstants.FILTER_LAST_CHECK);
        String filterFrom = session.getProperty(ExchangeConstants.FILTER_FROM_PROPERTY);
        String filterNotFrom = session.getProperty(ExchangeConstants.FILTER_NOT_FROM_PROPERTY);
        String filterTo = session.getProperty(ExchangeConstants.FILTER_TO_PROPERTY);
        boolean delete = Boolean.parseBoolean(session.getProperty(DELETE_PROPERTY));
        boolean secure = Boolean.parseBoolean(session.getProperty(prefix + SSL_PROPERTY));
        int limit = -1;
        String limitString = session.getProperty(LIMIT_PROPERTY);
        if (limitString != null) {
            try {
                limit = Integer.parseInt(limitString);
            } catch (NumberFormatException ex) {
                throw new NumberFormatException("Invalid limit specified: " + limitString);
            }
        }
        try {
            URL url = new URL(host);
            secure = "https".equalsIgnoreCase(url.getProtocol());
            host = url.getHost();
            int specifiedPort = url.getPort();
            if (specifiedPort != -1) port = specifiedPort;
        } catch (MalformedURLException ex) {
            if (session.getDebug()) {
                session.getDebugOut().println("Not parsing " + host + " as a URL; using explicit options for " + "secure, host, and port.");
            }
        }
        if (port == -1) {
            try {
                port = Integer.parseInt(session.getProperty(prefix + PORT_PROPERTY));
            } catch (Exception ignore) {
            }
            if (port == -1) port = secure ? HTTPS_PORT : HTTP_PORT;
        }
        String server = (secure ? "https://" : "http://") + host;
        if (secure ? (port != HTTPS_PORT) : (port != HTTP_PORT)) {
            server += ":" + port;
        }
        String mailbox = session.getProperty(MAILBOX_PROPERTY);
        if (mailbox == null) {
            mailbox = session.getProperty(prefix + FROM_PROPERTY);
            if (mailbox == null) {
                mailbox = InternetAddress.getLocalAddress(session).getAddress();
            }
        }
        int index = username.indexOf(':');
        if (index != -1) {
            mailbox = username.substring(index + 1);
            username = username.substring(0, index);
            String mailboxOptions = null;
            index = mailbox.indexOf('[');
            if (index != -1) {
                mailboxOptions = mailbox.substring(index + 1);
                mailboxOptions = mailboxOptions.substring(0, mailboxOptions.indexOf(']'));
                mailbox = mailbox.substring(0, index);
            }
            if (mailboxOptions != null) {
                Properties props = null;
                try {
                    props = parseOptions(mailboxOptions);
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Unable to parse mailbox options: " + ex.getMessage(), ex);
                }
                String value = props.getProperty("unfiltered");
                if (value != null) unfiltered = Boolean.parseBoolean(value);
                value = props.getProperty("filterLastCheck");
                if (value != null) filterLastCheck = value;
                value = props.getProperty("filterTo");
                if (value != null) filterTo = value;
                value = props.getProperty("filterFrom");
                if (value != null) filterFrom = value;
                value = props.getProperty("filterNotFrom");
                if (value != null) filterNotFrom = value;
                value = props.getProperty("delete");
                if (value != null) delete = Boolean.parseBoolean(value);
                value = props.getProperty("limit");
                if (value != null) {
                    try {
                        limit = Integer.parseInt(value);
                    } catch (NumberFormatException ex) {
                        throw new NumberFormatException("Invalid limit specified: " + value);
                    }
                }
            } else if (session.getDebug()) {
                session.getDebugOut().println("No mailbox options specified; " + "using explicit limit, unfiltered, and delete.");
            }
        } else if (session.getDebug()) {
            session.getDebugOut().println("No mailbox specified in username; " + "using explicit mailbox, limit, unfiltered, and delete.");
        }
        int timeout = -1;
        String timeoutString = session.getProperty(prefix + TIMEOUT_PROPERTY);
        if (timeoutString != null) {
            try {
                timeout = Integer.parseInt(timeoutString);
            } catch (NumberFormatException ex) {
                throw new NumberFormatException("Invalid timeout value: " + timeoutString);
            }
        }
        int connectionTimeout = -1;
        timeoutString = session.getProperty(prefix + CONNECTION_TIMEOUT_PROPERTY);
        if (timeoutString != null) {
            try {
                connectionTimeout = Integer.parseInt(timeoutString);
            } catch (NumberFormatException ex) {
                throw new NumberFormatException("Invalid connection timeout value: " + timeoutString);
            }
        }
        InetAddress localAddress = null;
        String localAddressString = session.getProperty(prefix + LOCAL_ADDRESS_PROPERTY);
        if (localAddressString != null) {
            try {
                localAddress = InetAddress.getByName(localAddressString);
            } catch (Exception ex) {
                throw new UnknownHostException("Invalid local address specified: " + localAddressString);
            }
        }
        if (mailbox == null) {
            throw new IllegalStateException("No mailbox specified.");
        }
        if (session.getDebug()) {
            PrintStream debugStream = session.getDebugOut();
            debugStream.println("Server:\t" + server);
            debugStream.println("Username:\t" + username);
            debugStream.println("Password:\t" + pwd);
            debugStream.println("Mailbox:\t" + mailbox);
            debugStream.print("Options:\t");
            debugStream.print((limit > 0) ? "Message Limit = " + limit : "Unlimited Messages");
            debugStream.print(unfiltered ? "; Unfiltered" : "; Filtered to Unread");
            debugStream.print(filterLastCheck == null || "".equals(filterLastCheck) ? "; NO filterLastCheck" : "; Filtered after " + filterLastCheck);
            debugStream.print(filterFrom == null || "".equals(filterFrom) ? "; NO filterFromDomain" : "; Filtered from " + filterFrom);
            debugStream.print(filterNotFrom == null || "".equals(filterNotFrom) ? "; NO filterNotFrom" : "; Filtered not from " + filterNotFrom);
            debugStream.print(filterTo == null || "".equals(filterTo) ? "; NO filterToEmail" : "; Filtered to " + filterTo);
            debugStream.println(delete ? "; Delete Messages on Delete" : "; Mark as Read on Delete");
            if (timeout > 0) {
                debugStream.println("Read timeout:\t" + timeout + " ms");
            }
            if (connectionTimeout > 0) {
                debugStream.println("Connection timeout:\t" + connectionTimeout + " ms");
            }
        }
        return new Exchange2007Connection(session, server, mailbox, username, password, timeout, connectionTimeout, localAddress, unfiltered, delete, limit, filterLastCheck, filterFrom, filterNotFrom, filterTo);
    }

    private Exchange2007Connection(Session session, String server, String mailbox, String username, String password, int timeout, int connectionTimeout, InetAddress localAddress, boolean unfiltered, boolean delete, int limit, String filterLastCheck, String filterFrom, String filterNotFrom, String filterTo) {
        this.session = session;
        this.server = server;
        this.mailbox = mailbox;
        this.username = username;
        this.password = password;
        this.timeout = timeout;
        this.connectionTimeout = connectionTimeout;
        this.localAddress = localAddress;
        this.unfiltered = unfiltered;
        this.delete = delete;
        this.limit = limit;
        this.filterLastCheck = filterLastCheck;
        this.filterFrom = filterFrom;
        this.filterNotFrom = filterNotFrom;
        this.filterTo = filterTo;
    }

    public void connect() throws Exception {
        throw new UnsupportedOperationException("connect not yet supported.");
    }

    public List<String> getMessages(String name) throws Exception {
        throw new UnsupportedOperationException("getMessages not yet supported.");
    }

    public void send(MimeMessage message) throws Exception {
        throw new UnsupportedOperationException("send not yet supported.");
    }

    public void delete(List<ExchangeMessage> messages) throws Exception {
        throw new UnsupportedOperationException("delete not yet supported.");
    }

    public InputStream getInputStream(ExchangeMessage message) throws Exception {
        throw new UnsupportedOperationException("getInputStream not yet supported.");
    }

    private static Properties parseOptions(String options) throws Exception {
        StringBuilder collector = new StringBuilder();
        String[] nvPairs = options.split("[,;]");
        for (String nvPair : nvPairs) collector.append(nvPair).append('\n');
        Properties properties = new Properties();
        properties.load(new ByteArrayInputStream(collector.toString().getBytes("ISO-8859-1")));
        return properties;
    }
}
