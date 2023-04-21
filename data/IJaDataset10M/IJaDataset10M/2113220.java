package ru.caffeineim.protocols.icq.examples;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.exceptions.ConvertStringException;
import ru.caffeineim.protocols.icq.integration.OscarInterface;
import ru.caffeineim.protocols.icq.integration.events.LoginErrorEvent;
import ru.caffeineim.protocols.icq.integration.events.StatusEvent;
import ru.caffeineim.protocols.icq.integration.listeners.OurStatusListener;
import ru.caffeineim.protocols.icq.setting.enumerations.XStatusModeEnum;

/**
 * <p>Created by 22.03.2008
 *   @author Samolisov Pavel
 */
public class SampleSendMessageTest implements OurStatusListener {

    private static Log log = LogFactory.getLog(SampleSendMessageTest.class);

    private static final String SERVER = "login.icq.com";

    private static final int PORT = 5190;

    private static final String BASIC_MESSAGE = "Channel1 message - Привет! Я - базовое сообщение";

    private static final String EXTENDS_MESSAGE = "Channel2 message\r\nПривет! Я - расширенное сообщение";

    private OscarConnection con;

    private String receiver;

    public SampleSendMessageTest(String login, String password, String receiver) {
        this.receiver = receiver;
        con = new OscarConnection(SERVER, PORT, login, password);
        con.addOurStatusListener(this);
        con.connect();
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Use : SampleSendMessageTest MY_UIN MY_PASSWORD RECEIVER_UIN");
        } else {
            new SampleSendMessageTest(args[0], args[1], args[2]);
        }
    }

    public void onAuthorizationFailed(LoginErrorEvent e) {
        con.close();
        log.error("Authorization failed: " + e.getErrorMessage());
        System.exit(1);
    }

    public void onLogin() {
        try {
            OscarInterface.changeXStatus(con, new XStatusModeEnum(XStatusModeEnum.THINKING));
            OscarInterface.sendBasicMessage(con, receiver, BASIC_MESSAGE);
            OscarInterface.sendExtendedMessage(con, receiver, EXTENDS_MESSAGE);
        } catch (ConvertStringException ex) {
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
