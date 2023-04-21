package ru.caffeineim.protocols.icq.examples;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.exceptions.ConvertStringException;
import ru.caffeineim.protocols.icq.integration.OscarInterface;
import ru.caffeineim.protocols.icq.integration.events.LoginErrorEvent;
import ru.caffeineim.protocols.icq.integration.events.StatusEvent;
import ru.caffeineim.protocols.icq.integration.listeners.OurStatusListener;

/**
 * <p>Created by 05.06.2008
 *   @author Samolisov Pavel
 */
public class MultiSendMessageTest implements OurStatusListener {

    private static Log log = LogFactory.getLog(MultiSendMessageTest.class);

    private static final String SERVER = "login.icq.com";

    private static final int PORT = 5190;

    private static final String TEST_MESSAGE = "Я - тучка тучка тучка, я вовсе не медведь!";

    private OscarConnection con;

    private String receiver;

    public MultiSendMessageTest(String login, String password, String receiver) {
        this.receiver = receiver;
        con = new OscarConnection(SERVER, PORT, login, password);
        con.addOurStatusListener(this);
        con.connect();
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Use : MultiSendMessageTest MY_UIN MY_PASSWORD RECEIVER_UIN");
        } else {
            new MultiSendMessageTest(args[0], args[1], args[2]);
        }
    }

    public void onAuthorizationFailed(LoginErrorEvent e) {
        con.close();
        log.error("Authorization failed: " + e.getErrorMessage());
        System.exit(1);
    }

    public void onLogin() {
        try {
            for (int i = 1; i <= 15; i++) {
                OscarInterface.sendBasicMessage(con, receiver, TEST_MESSAGE + " " + i);
                Thread.sleep(100);
            }
        } catch (ConvertStringException ex) {
            log.error(ex.getMessage(), ex);
        } catch (InterruptedException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public void onLogout(Exception e) {
        con.close();
        log.error("Logout ", e);
        System.exit(1);
    }

    public void onStatusResponse(StatusEvent e) {
    }
}
