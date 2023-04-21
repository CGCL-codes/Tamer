package fix;

import quickfix.*;

/**
 * Created by IntelliJ IDEA.
 * User: laurencekirk
 * Date: Oct 3, 2010
 * Time: 3:01:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class FixEngine implements quickfix.Application {

    private MessageHandler messageHandler;

    public void onCreate(SessionID sessionID) {
        System.out.println("******* Session created" + sessionID);
        messageHandler.onCreate(sessionID);
    }

    public void onLogon(SessionID sessionID) {
        System.out.println("******* Logon " + sessionID);
        messageHandler.onLogon(sessionID);
    }

    public void onLogout(SessionID sessionID) {
        System.out.println("******* Logoout " + sessionID);
        messageHandler.onLogout(sessionID);
    }

    public void toAdmin(Message message, SessionID sessionID) {
        System.out.println("******* GeneralMessage " + message + "" + sessionID);
        messageHandler.toAdmin(message, sessionID);
    }

    public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        System.out.println("******* GeneralMessage from Admin " + message + "" + sessionID);
        messageHandler.fromAdmin(message, sessionID);
    }

    public void toApp(Message message, SessionID sessionID) throws DoNotSend {
        System.out.println("******* GeneralMessage to App " + message + "" + sessionID);
        messageHandler.toApp(message, sessionID);
    }

    public void fromApp(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        System.out.println("******* GeneralMessage from App " + message + "" + sessionID);
        messageHandler.fromApp(message, sessionID);
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }
}
