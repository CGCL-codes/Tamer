package nakayo.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import java.util.Collection;
import nakayo.gameserver.model.siege.SiegeLocation;
import nakayo.gameserver.network.aion.AionConnection;
import nakayo.gameserver.network.aion.AionServerPacket;

/**
 * 
 * @author Sylar, Vial, Ritsu
 *
 */
public class SM_SIEGE_AETHERIC_FIELDS extends AionServerPacket {

    private Collection<SiegeLocation> locations;

    public SM_SIEGE_AETHERIC_FIELDS(Collection<SiegeLocation> locations) {
        this.locations = locations;
    }

    @Override
    public void writeImpl(AionConnection con, ByteBuffer buf) {
        writeH(buf, locations.size());
        for (SiegeLocation loc : locations) {
            writeD(buf, loc.getLocationId());
            if (loc.isVulnerable() && loc.isShieldActive()) writeC(buf, 1); else writeC(buf, 0);
        }
    }
}
