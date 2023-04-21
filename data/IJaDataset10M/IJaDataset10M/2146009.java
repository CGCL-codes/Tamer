package nakayo.gameserver.network.aion.clientpackets;

import nakayo.gameserver.model.AbyssRankingResult;
import nakayo.gameserver.model.Race;
import nakayo.gameserver.network.aion.AionClientPacket;
import nakayo.gameserver.network.aion.serverpackets.SM_ABYSS_RANKING_PLAYERS;
import nakayo.gameserver.services.AbyssRankingService;
import nakayo.gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;
import java.util.ArrayList;

/**
 * In this packets aion client is asking for player abyss rankings
 * 
 * @author zdead
 */
public class CM_ABYSS_RANKING_PLAYERS extends AionClientPacket {

    private Race queriedRace;

    private int raceId;

    private static final Logger log = Logger.getLogger(CM_ABYSS_RANKING_PLAYERS.class);

    public CM_ABYSS_RANKING_PLAYERS(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        raceId = readC();
        switch(raceId) {
            case 0:
                queriedRace = Race.ELYOS;
                break;
            case 1:
                queriedRace = Race.ASMODIANS;
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        if (queriedRace != null) {
            ArrayList<AbyssRankingResult> results = AbyssRankingService.getInstance().getInviduals(queriedRace);
            PacketSendUtility.sendPacket(getConnection().getActivePlayer(), new SM_ABYSS_RANKING_PLAYERS(results, queriedRace, getConnection().getActivePlayer()));
        } else {
            log.warn("Received invalid raceId: " + raceId);
        }
    }
}
