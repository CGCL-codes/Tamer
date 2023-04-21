package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Sent to fill the search panel of a players social window<br />
 * I.E.: In response to a <tt>CM_PLAYER_SEARCH</tt>
 */
public class SM_PLAYER_SEARCH extends AionServerPacket {

    private static final Logger log = Logger.getLogger(SM_PLAYER_SEARCH.class);

    private List<Player> players;

    private int region;

    private int status;

    /**
	* Constructs a new packet that will send these players
	* @param players List of players to show
	* @param region of search - should be passed as parameter
	* to prevent null in player.getActiveRegion()
	* 
	*/
    public SM_PLAYER_SEARCH(List<Player> players, int region) {
        this.players = new ArrayList<Player>(players);
        this.region = region;
    }

    /**
	* {@inheritDoc}
	*/
    @Override
    public void writeImpl(AionConnection con, ByteBuffer buf) {
        writeH(buf, players.size());
        for (Player player : players) {
            if (player.getActiveRegion() == null) {
                log.warn("CHECKPOINT: null active region for " + player.getObjectId() + "-" + player.getX() + "-" + player.getY() + "-" + player.getZ());
            }
            writeD(buf, player.getActiveRegion() == null ? region : player.getActiveRegion().getMapId());
            writeF(buf, player.getPosition().getX());
            writeF(buf, player.getPosition().getY());
            writeF(buf, player.getPosition().getZ());
            writeC(buf, player.getPlayerClass().getClassId());
            writeC(buf, player.getGender().getGenderId());
            writeC(buf, player.getLevel());
            if (player.isLookingForGroup()) status = 2; else if (player.isInGroup()) status = 3; else status = 0;
            writeC(buf, status);
            writeS(buf, player.getName());
            byte[] unknown = new byte[52 - (player.getName().length() * 2 + 2)];
            writeB(buf, unknown);
        }
    }
}
