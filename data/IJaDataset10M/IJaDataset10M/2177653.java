package org.javathena.login.parse;

import java.net.*;
import org.javathena.core.data.IParse;
import org.javathena.core.data.Socket_data;
import org.javathena.core.utiles.Functions;
import org.javathena.login.Login;
import org.javathena.login.UserManagement;

/**
 * 
 * @author Francois
 */
public class FromChar implements IParse {

    public static final int END_OF_CONNECTION = -257;

    public static final int ACCOUNT_AUTHENTIFY = 0x2712;

    public static final int NUMBER_OF_USER = 0x2714;

    public static final int EMAIL_CREATION = 0x2715;

    public static final int REQUEST_ACCOUNT_DATA = 0x2716;

    public static final int PING_REQUEST = 0x2719;

    public static final int CHANGE_AN_EMAIL = 0x2722;

    public static final int STATUS_CHANGE = 0x2724;

    public static final int BAN_REQUEST = 0x2725;

    public static final int CHANGE_SEX = 0x2727;

    public static final int ACCOUNT_REG2 = 0x2728;

    public static final int UNBAN = 0x272a;

    public static final int ACCOUNT_TO_ONLINE = 0x272b;

    public static final int ACCOUNT_TO_OFFLINE = 0x272c;

    public static final int ONLINE_ACCOUNT_LIST = 0x272d;

    public static final int REQUEST_ACCOUNT_REG2 = 0x272e;

    public static final int WAN_UPDATE = 0x2736;

    public static final int REQUEST_ALL_OFFLINE = 0x2737;

    /** Creates a new instance of Parse_fromchar */
    public FromChar() {
    }

    public int parse(Socket_data session, byte packet[]) {
        int commande = packet[0] | (packet[1] * 256);
        System.out.printf("FromChar %x\n", commande);
        switch(commande) {
            case -257:
                Functions.showWarning("Disconnection of the char server: %s", session.getName());
                session.setEof(1);
                break;
            case ACCOUNT_AUTHENTIFY:
                UserManagement.charServerToAuthentify(session, packet);
                break;
            case NUMBER_OF_USER:
                UserManagement.numberOfUser(session, packet);
                break;
            case EMAIL_CREATION:
                UserManagement.emailCreation(session);
                break;
            case REQUEST_ACCOUNT_DATA:
                UserManagement.requestAccountData(session, packet);
                break;
            case PING_REQUEST:
                session.func_send(new byte[] { 0x18, 0x27 });
                break;
            case CHANGE_AN_EMAIL:
                UserManagement.toChangeAnEmail(session);
                break;
            case STATUS_CHANGE:
                UserManagement.statusChange(session, packet);
                break;
            case BAN_REQUEST:
                UserManagement.banResquest(session);
                break;
            case CHANGE_SEX:
                UserManagement.changeSex(session);
                break;
            case ACCOUNT_REG2:
                System.out.println("ACCOUNT_REG2");
                UserManagement.receiveAccountReg2(session, packet);
                break;
            case UNBAN:
                UserManagement.unban(session);
                break;
            case ACCOUNT_TO_ONLINE:
                UserManagement.account_idToOnline(session, packet);
                break;
            case ONLINE_ACCOUNT_LIST:
                UserManagement.receiveAllOnlinAccounts(session, packet);
                break;
            case ACCOUNT_TO_OFFLINE:
                UserManagement.account_idToOffline(session, packet);
                break;
            case REQUEST_ACCOUNT_REG2:
                UserManagement.requestAccountReg2(session, packet);
                break;
            case WAN_UPDATE:
                Functions.showWarning("Not implemented yet");
                break;
            case REQUEST_ALL_OFFLINE:
                Functions.showWarning("Not implemented yet");
                break;
            default:
                if (commande < 0) System.exit(0);
                Login.logUnknownPackets(session);
        }
        return 0;
    }
}
