package de.herberlin.server.mail;

import java.net.Socket;
import de.herberlin.server.common.AbstractServer;
import de.herberlin.server.common.ConfigConstants;
import de.herberlin.server.common.Configuration;
import de.herberlin.server.common.event.ApplicationEvent;
import de.herberlin.server.common.event.ServerStartEvent;

/**
 * Mail server waiting for clients
 * @author herberlin
 *
 */
public class MailServer extends AbstractServer {

    protected int getPort() {
        return Configuration.getPrefs().getInt(ConfigConstants.MODE_MAIL + ConfigConstants.SETTING_PORT, 25);
    }

    protected void process(Socket client) {
        new MailThread(client);
    }

    protected ApplicationEvent getServerStartEvent() {
        return new ServerStartEvent("Mail Server", new String[] { "Time", "From", "To", "Conversation" });
    }
}
