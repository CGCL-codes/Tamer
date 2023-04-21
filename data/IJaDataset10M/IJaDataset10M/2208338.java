package org.openremote.controller.protocol.knx;

import org.apache.log4j.Logger;
import org.openremote.controller.utils.Strings;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents Data Link layer in KNX communication stack.
 *
 * @author <a href="mailto:juha@openremote.org">Juha Lindfors</a>
 */
class DataLink {

    private static final MessageCode DATA_REQUEST = new MessageCode(MessageCode.DATA_REQUEST_BYTE, "L_Data.req");

    private static final MessageCode DATA_INDICATE = new MessageCode(MessageCode.DATA_INDICATE_BYTE, "L_Data.ind");

    private static final MessageCode DATA_CONFIRM = new MessageCode(MessageCode.DATA_CONFIRM_BYTE, "L_Data.con");

    private static final MessageCode POLL_REQUEST = new MessageCode(MessageCode.POLL_REQUEST_BYTE, "L_Poll_Data.req");

    private static final MessageCode POLL_CONFIRM = new MessageCode(MessageCode.POLL_CONFIRM_BYTE, "L_Poll_Data.con");

    private static final MessageCode RAW_REQUEST = new MessageCode(MessageCode.RAW_REQUEST_BYTE, "L_Raw.req");

    private static final MessageCode RAW_INDICATE = new MessageCode(MessageCode.RAW_INDICATE_BYTE, "L_Raw.ind");

    private static final MessageCode RAW_CONFIRM = new MessageCode(MessageCode.RAW_CONFIRM_BYTE, "L_Raw.con");

    /**
   * KNX logger. Uses a common category for all KNX related logging.
   */
    private static final Logger log = Logger.getLogger(KNXCommandBuilder.KNX_LOG_CATEGORY);

    static String findServicePrimitiveByMessageCode(byte msgCode) {
        MessageCode mc = MessageCode.lookup(msgCode);
        if (mc == null) {
            log.warn("Looked up an unknown data link layer frame message code: " + Strings.byteToUnsignedHexString(msgCode));
            return "Unknown";
        } else {
            return mc.getPrimitiveName();
        }
    }

    static enum Service {

        DATA(DATA_REQUEST, DATA_INDICATE, DATA_CONFIRM), POLL(POLL_REQUEST, POLL_CONFIRM), RAW(RAW_REQUEST, RAW_INDICATE, RAW_CONFIRM);

        MessageCode requestCode;

        MessageCode indicateCode;

        MessageCode confirmCode;

        Service(MessageCode req, MessageCode ind, MessageCode con) {
            this(req, con);
            this.indicateCode = ind;
        }

        Service(MessageCode req, MessageCode con) {
            this.requestCode = req;
            this.confirmCode = con;
        }

        byte getRequestMessageCode() {
            return requestCode.getByteValue();
        }
    }

    public static class MessageCode {

        public static final int DATA_REQUEST_BYTE = 0x11;

        public static final int DATA_INDICATE_BYTE = 0x29;

        public static final int DATA_CONFIRM_BYTE = 0x2E;

        public static final int POLL_REQUEST_BYTE = 0x13;

        public static final int POLL_CONFIRM_BYTE = 0x25;

        public static final int RAW_REQUEST_BYTE = 0x10;

        public static final int RAW_INDICATE_BYTE = 0x2D;

        public static final int RAW_CONFIRM_BYTE = 0x2F;

        private static Map<Byte, MessageCode> lookup = new ConcurrentHashMap<Byte, MessageCode>(25);

        private static MessageCode lookup(byte b) {
            return lookup.get(b);
        }

        private int messageCode;

        private String primitive;

        private MessageCode(int messageCode, String primitive) {
            this.messageCode = messageCode;
            this.primitive = primitive;
            lookup.put((byte) messageCode, this);
        }

        private byte getByteValue() {
            return (byte) messageCode;
        }

        private String getPrimitiveName() {
            return primitive;
        }
    }
}
