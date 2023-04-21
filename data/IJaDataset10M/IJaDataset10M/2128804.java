package aionjp.network.gameserver;

import java.nio.ByteBuffer;
import java.util.logging.Logger;
import aionjp.network.gameserver.GsConnection.State;
import aionjp.network.gameserver.clientpackets.GsAuth;

/**
 * @author -Nemesiss-
 */
public class GsPacketHandler {

    private static final Logger log = Logger.getLogger(GsPacketHandler.class.getName());

    public static GsClientPacket handle(ByteBuffer data, GsConnection client) {
        GsClientPacket msg = null;
        State state = client.getState();
        int id = data.get() & 0xff;
        switch(state) {
            case CONNECTED:
                {
                    switch(id) {
                        case 0x00:
                            msg = new GsAuth(data, client);
                            break;
                        default:
                            unkownPacket(state, id);
                    }
                    break;
                }
            case AUTHED:
                {
                    switch(id) {
                        case 0x02:
                            break;
                        case 0x03:
                            break;
                        default:
                            unkownPacket(state, id);
                    }
                    break;
                }
        }
        return msg;
    }

    private static final void unkownPacket(State state, int id) {
        log.info("Unkown packet recived from Game Server: " + id + " state=" + state);
    }
}
