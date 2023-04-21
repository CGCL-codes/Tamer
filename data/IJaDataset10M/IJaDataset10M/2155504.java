package ru.caffeineim.protocols.icq.setting.enumerations;

/**
 * <p>Created by
 *   @author Fabrice Michellonet
 */
public class MetaTypeEnum {

    public static final int CLIENT_REQUEST_OFFLINE_MESSAGES = 0x003C;

    public static final int CLIENT_ACK_OFFLINE_MESSAGES = 0x003E;

    public static final int CLIENT_ADVANCED_META = 0x07D0;

    public static final int SERVER_OFFLINE_MESSAGE = 0x0041;

    public static final int SERVER_END_OF_OFFLINE_MESSAGES = 0x0042;

    public static final int SERVER_ADVANCED_META = 0x07DA;

    private int type;

    public MetaTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String toString() {
        String ret = "";
        switch(type) {
            case CLIENT_REQUEST_OFFLINE_MESSAGES:
                ret = "Client request offline messages";
                break;
            case CLIENT_ACK_OFFLINE_MESSAGES:
                ret = "Client ack offline messages";
                break;
            case CLIENT_ADVANCED_META:
                ret = "Client advanced meta";
                break;
            case SERVER_OFFLINE_MESSAGE:
                ret = "Server offline message";
                break;
            case SERVER_END_OF_OFFLINE_MESSAGES:
                ret = "Server end of offline messages";
                break;
            case SERVER_ADVANCED_META:
                ret = "Server advanced meta";
                break;
        }
        return ret;
    }
}
