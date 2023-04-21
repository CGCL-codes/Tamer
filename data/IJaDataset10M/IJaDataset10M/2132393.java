package nakayo.gameserver.network.aion;

import nakayo.gameserver.configs.network.NetworkConfig;
import nakayo.gameserver.network.aion.AionConnection.State;
import nakayo.gameserver.utils.Util;
import org.apache.log4j.Logger;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author -Nemesiss-
 * @author Luno
 * @author drsaluml
 */
public class AionPacketHandler {

    /**
     * logger for this class
     */
    private static final Logger log = Logger.getLogger(AionPacketHandler.class);

    private Map<State, Map<Integer, AionClientPacket>> packetsPrototypes = new HashMap<State, Map<Integer, AionClientPacket>>();

    /**
     * Reads one packet from given ByteBuffer
     *
     * @param data
     * @param client
     * @return AionClientPacket object from binary data
     */
    public AionClientPacket handle(ByteBuffer data, AionConnection client) {
        State state = client.getState();
        short id = data.getShort();
        data.position(data.position() + 3);
        return getPacket(state, id, data, client);
    }

    public void addPacketPrototype(AionClientPacket packetPrototype, State... states) {
        for (State state : states) {
            Map<Integer, AionClientPacket> pm = packetsPrototypes.get(state);
            if (pm == null) {
                pm = new HashMap<Integer, AionClientPacket>();
                packetsPrototypes.put(state, pm);
            }
            pm.put(packetPrototype.getOpcode(), packetPrototype);
        }
    }

    private AionClientPacket getPacket(State state, int id, ByteBuffer buf, AionConnection con) {
        AionClientPacket prototype = null;
        Map<Integer, AionClientPacket> pm = packetsPrototypes.get(state);
        if (pm != null) {
            prototype = pm.get(id);
        }
        if (prototype == null) {
            unknownPacket(state, id, buf);
            return null;
        }
        AionClientPacket res = prototype.clonePacket();
        res.setBuffer(buf);
        res.setConnection(con);
        return res;
    }

    /**
     * Logs unknown packet.
     *
     * @param state
     * @param id
     * @param data
     */
    private void unknownPacket(State state, int id, ByteBuffer data) {
        if (NetworkConfig.DISPLAY_UNKNOWNPACKETS) {
            log.warn(String.format("Unknown packet recived from Aion client: 0x%02X, state=%s %n%s", id, state.toString(), Util.toHex(data)));
        }
    }
}
