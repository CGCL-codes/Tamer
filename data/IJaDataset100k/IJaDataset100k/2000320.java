package loginserver.network.aion;

import java.nio.ByteBuffer;
import loginserver.network.aion.AionConnection.State;
import loginserver.network.aion.clientpackets.CM_AUTH_GG;
import loginserver.network.aion.clientpackets.CM_LOGIN;
import loginserver.network.aion.clientpackets.CM_PLAY;
import loginserver.network.aion.clientpackets.CM_SERVER_LIST;
import loginserver.network.aion.clientpackets.CM_UPDATE_SESSION;
import org.apache.log4j.Logger;

public class AionPacketHandler {

    /**
	 * logger for this class
	 */
    private static final Logger log = Logger.getLogger(AionPacketHandler.class);

    /**
	 * Reads one packet from given ByteBuffer
	 * 
	 * @param data
	 * @param client
	 * @return AionClientPacket object from binary data
	 */
    public static AionClientPacket handle(ByteBuffer data, AionConnection client) {
        AionClientPacket msg = null;
        State state = client.getState();
        int id = data.get() & 0xff;
        switch(state) {
            case CONNECTED:
                {
                    switch(id) {
                        case 0x07:
                            msg = new CM_AUTH_GG(data, client);
                            break;
                        case 0x08:
                            msg = new CM_UPDATE_SESSION(data, client);
                            break;
                        default:
                            unknownPacket(state, id);
                    }
                    break;
                }
            case AUTHED_GG:
                {
                    switch(id) {
                        case 0x0B:
                            msg = new CM_LOGIN(data, client);
                            break;
                        default:
                            unknownPacket(state, id);
                    }
                    break;
                }
            case AUTHED_LOGIN:
                {
                    switch(id) {
                        case 0x05:
                            msg = new CM_SERVER_LIST(data, client);
                            break;
                        case 0x02:
                            msg = new CM_PLAY(data, client);
                            break;
                        default:
                            unknownPacket(state, id);
                    }
                    break;
                }
        }
        return msg;
    }

    /**
	 * Logs unknown packet.
	 * 
	 * @param state
	 * @param id
	 */
    private static void unknownPacket(State state, int id) {
        log.warn(String.format("Se ha recibido un paquete desconocido del cliente de Aion: 0x%02X Estado=%s", id, state.toString()));
    }
}
