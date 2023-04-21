package org.openmim.msn;

import org.openmim.icq.util.*;
import org.openmim.icq.util.joe.*;
import org.openmim.mn.MessagingNetwork;
import org.openmim.mn.MessagingNetworkException;
import java.util.*;

/**
  Utility class for conversion of integer status values between
  MessagingNetwork ones and NATIVE protocol ones.
*/
public final class StatusUtil {

    public static final int NATIVE_STATUS_OFFLINE = 0;

    public static final int NATIVE_STATUS_ONLINE = 1;

    public static final int NATIVE_STATUS_BUSY = 2;

    public static final int NATIVE_STATUS_IDLE = 3;

    public static final int NATIVE_STATUS_BRB = 4;

    public static final int NATIVE_STATUS_AWAY = 5;

    public static final int NATIVE_STATUS_PHONE = 6;

    public static final int NATIVE_STATUS_LUNCH = 7;

    public static final int NATIVE_STATUS_HIDDEN = 8;

    public static void EXPECT_IS_NATIVE_STATUS(int status) throws MessagingNetworkException {
        switch(status) {
            case NATIVE_STATUS_OFFLINE:
            case NATIVE_STATUS_ONLINE:
            case NATIVE_STATUS_BUSY:
            case NATIVE_STATUS_IDLE:
            case NATIVE_STATUS_BRB:
            case NATIVE_STATUS_AWAY:
            case NATIVE_STATUS_PHONE:
            case NATIVE_STATUS_LUNCH:
            case NATIVE_STATUS_HIDDEN:
                return;
        }
        MLang.EXPECT_FALSE(HexUtil.toHexString0x(status) + " is not valid NATIVE status.", MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_MIM_BUG);
    }

    public static int translateStatusMimToNative(int status) throws MessagingNetworkException {
        MLang.EXPECT_IS_MIM_STATUS(status, "status");
        int translatedStatus = -1;
        switch(status) {
            case MessagingNetwork.STATUS_ONLINE:
                translatedStatus = NATIVE_STATUS_ONLINE;
                break;
            case MessagingNetwork.STATUS_BUSY:
                translatedStatus = NATIVE_STATUS_BUSY;
                break;
            case MessagingNetwork.STATUS_OFFLINE:
                translatedStatus = NATIVE_STATUS_OFFLINE;
                break;
            default:
                Lang.ASSERT_FALSE("invalid mim status: " + HexUtil.toHexString0x(status));
                break;
        }
        return translatedStatus;
    }

    public static String translateStatusMimToString(int status) {
        String translatedStatus;
        switch(status) {
            case MessagingNetwork.STATUS_ONLINE:
                translatedStatus = "MIM_STATUS_ONLINE";
                break;
            case MessagingNetwork.STATUS_BUSY:
                translatedStatus = "MIM_STATUS_BUSY";
                break;
            case MessagingNetwork.STATUS_OFFLINE:
                translatedStatus = "MIM_STATUS_OFFLINE";
                break;
            default:
                translatedStatus = "MIM_STATUS_INVALID (value==" + HexUtil.toHexString0x(status) + ")";
                break;
        }
        return translatedStatus;
    }

    public static int translateStatusNativeToMim_self(int status) {
        int translatedStatus;
        switch(status) {
            case NATIVE_STATUS_BUSY:
            case NATIVE_STATUS_PHONE:
            case NATIVE_STATUS_LUNCH:
                translatedStatus = MessagingNetwork.STATUS_BUSY;
                break;
            case NATIVE_STATUS_OFFLINE:
                translatedStatus = MessagingNetwork.STATUS_OFFLINE;
                break;
            default:
                translatedStatus = MessagingNetwork.STATUS_ONLINE;
                break;
        }
        return translatedStatus;
    }

    public static int translateStatusNativeToMim_cl_entry(int status) {
        int translatedStatus;
        switch(status) {
            case NATIVE_STATUS_BRB:
            case NATIVE_STATUS_AWAY:
            case NATIVE_STATUS_PHONE:
            case NATIVE_STATUS_LUNCH:
            case NATIVE_STATUS_IDLE:
                translatedStatus = MessagingNetwork.STATUS_BUSY;
                break;
            case NATIVE_STATUS_OFFLINE:
                translatedStatus = MessagingNetwork.STATUS_OFFLINE;
                break;
            default:
                translatedStatus = MessagingNetwork.STATUS_ONLINE;
                break;
        }
        return translatedStatus;
    }

    public static String translateStatusNativeToString(int status) {
        String translatedStatus;
        switch(status) {
            case NATIVE_STATUS_OFFLINE:
                translatedStatus = "NATIVE_STATUS_OFFLINE";
                break;
            case NATIVE_STATUS_ONLINE:
                translatedStatus = "NATIVE_STATUS_ONLINE";
                break;
            case NATIVE_STATUS_BUSY:
                translatedStatus = "NATIVE_STATUS_BUSY";
                break;
            case NATIVE_STATUS_IDLE:
                translatedStatus = "NATIVE_STATUS_IDLE";
                break;
            case NATIVE_STATUS_BRB:
                translatedStatus = "NATIVE_STATUS_BRB";
                break;
            case NATIVE_STATUS_AWAY:
                translatedStatus = "NATIVE_STATUS_AWAY";
                break;
            case NATIVE_STATUS_PHONE:
                translatedStatus = "NATIVE_STATUS_PHONE";
                break;
            case NATIVE_STATUS_LUNCH:
                translatedStatus = "NATIVE_STATUS_LUNCH";
                break;
            case NATIVE_STATUS_HIDDEN:
                translatedStatus = "NATIVE_STATUS_HIDDEN";
                break;
            default:
                translatedStatus = "NATIVE_STATUS_UNKNOWN (value==" + HexUtil.toHexString0x(status) + ")";
                break;
        }
        return translatedStatus;
    }

    public static String nativeStatusAsMnemonicString(int status) {
        String translatedStatus;
        switch(status) {
            case NATIVE_STATUS_OFFLINE:
                translatedStatus = "FLN";
                break;
            case NATIVE_STATUS_ONLINE:
                translatedStatus = "NLN";
                break;
            case NATIVE_STATUS_BUSY:
                translatedStatus = "BSY";
                break;
            case NATIVE_STATUS_IDLE:
                translatedStatus = "IDL";
                break;
            case NATIVE_STATUS_BRB:
                translatedStatus = "BRB";
                break;
            case NATIVE_STATUS_AWAY:
                translatedStatus = "AWY";
                break;
            case NATIVE_STATUS_PHONE:
                translatedStatus = "PHN";
                break;
            case NATIVE_STATUS_LUNCH:
                translatedStatus = "LUN";
                break;
            case NATIVE_STATUS_HIDDEN:
                translatedStatus = "HDN";
                break;
            default:
                throw new AssertException("NATIVE_STATUS_UNKNOWN has unknown mnemonic (status==" + HexUtil.toHexString0x(status) + ")");
        }
        return translatedStatus;
    }

    private static final Hashtable m = new Hashtable(7);

    static {
        m.put("FLN", new Integer(NATIVE_STATUS_OFFLINE));
        m.put("NLN", new Integer(NATIVE_STATUS_ONLINE));
        m.put("BSY", new Integer(NATIVE_STATUS_BUSY));
        m.put("IDL", new Integer(NATIVE_STATUS_IDLE));
        m.put("BRB", new Integer(NATIVE_STATUS_BRB));
        m.put("AWY", new Integer(NATIVE_STATUS_AWAY));
        m.put("PHN", new Integer(NATIVE_STATUS_PHONE));
        m.put("LUN", new Integer(NATIVE_STATUS_LUNCH));
        m.put("HDN", new Integer(NATIVE_STATUS_HIDDEN));
    }

    public static int mnemonicStringAsNativeStatus(String statusMnemonic) throws MExpectException {
        if ((statusMnemonic) == null) Lang.ASSERT_NOT_NULL(statusMnemonic, "statusMnemonic");
        Integer i = (Integer) m.get(statusMnemonic);
        if (i == null) throw new MExpectException("unknown status mnemonic: " + StringUtil.toPrintableString(statusMnemonic), MExpectException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MExpectException.ENDUSER_LOGGED_OFF_DUE_TO_PROTOCOL_ERROR);
        return i.intValue();
    }
}
