package overlaysim.protocol;

import overlaysim.message.Message;

public class Protocol {

    public static final int PROTOCOL_OVERLAY_CHORD = 1;

    public static final int PROTOCOL_OVERLAY_CODHT = 2;

    public static final int PROTOCOL_OVERLAY_CODHTSIMPLE = 3;

    public static final int PROTOCOL_OVERLAY_DHTLAYER = 4;

    public static final int PROTOCOL_OVERLAY_PASTRY = 5;

    public static final int PROTOCOL_OVERLAY_CHORD64 = 6;

    public static final int PROTOCOL_OVERLAY_BTSIM = 7;

    public static final int PROTOCOL_OVERLAY_CHORDCANCHING = 8;

    public static final int PROTOCOL_END = 100;

    public static int getId(String protocol_name) {
        if (protocol_name.equals("CHORD")) {
            return PROTOCOL_OVERLAY_CHORD;
        } else if (protocol_name.equals("CODHT")) {
            return PROTOCOL_OVERLAY_CODHT;
        } else if (protocol_name.equals("CODHTSIMPLE")) {
            return PROTOCOL_OVERLAY_CODHTSIMPLE;
        } else if (protocol_name.equals("DHTLAYER")) {
            return PROTOCOL_OVERLAY_DHTLAYER;
        } else if (protocol_name.equals("PASTRY")) {
            return PROTOCOL_OVERLAY_PASTRY;
        } else if (protocol_name.equals("CHORD64")) {
            return PROTOCOL_OVERLAY_CHORD64;
        } else if (protocol_name.equals("BTSIM")) {
            return PROTOCOL_OVERLAY_BTSIM;
        } else if (protocol_name.equals("CHORDCANCHING")) {
            return PROTOCOL_OVERLAY_CHORDCANCHING;
        } else {
            return PROTOCOL_END;
        }
    }
}
