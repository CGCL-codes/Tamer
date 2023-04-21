package common.packet_gen;

import port.util;
import utility.Log;
import common.Connection;

public class packet_authentication_req {

    private static final int PACKET_AUTHENTICATION_REQ = 0;

    public authentication_type type;

    public String message;

    public static int dsend_packet_authentication_req(Connection pc, authentication_type type, String message) {
        packet_authentication_req real_packet = new packet_authentication_req();
        real_packet.type = type;
        return real_packet.send_packet_authentication_req(pc);
    }

    public int send_packet_authentication_req(Connection pc) {
        if (!pc.used) {
            util.freelog(Log.LOG_ERROR, "WARNING: trying to send data to the closed connection %s", pc.conn_description());
            return -1;
        }
        assert (pc.phs.variant != null);
        ensure_valid_variant_packet_authentication_req(pc);
        switch(pc.phs.variant[PACKET_AUTHENTICATION_REQ]) {
            case 100:
                return send_packet_authentication_req_100(pc);
            default:
                util.die("unknown variant");
                return -1;
        }
    }

    void ensure_valid_variant_packet_authentication_req(Connection pc) {
        int variant = -1;
        if (pc.phs.variant[PACKET_AUTHENTICATION_REQ] != -1) {
            return;
        }
        if (false) {
        } else if (true) {
            variant = 100;
        } else {
            util.die("unknown variant");
        }
        pc.phs.variant[PACKET_AUTHENTICATION_REQ] = variant;
    }

    int send_packet_authentication_req_100(Connection pc) {
        return 0;
    }
}
