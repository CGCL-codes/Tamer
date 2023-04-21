package nakayo.gameserver.model.siege;

import nakayo.gameserver.model.gameobjects.player.Player;
import nakayo.gameserver.network.aion.serverpackets.SM_INFLUENCE_RATIO;
import nakayo.gameserver.services.SiegeService;
import nakayo.gameserver.utils.PacketSendUtility;
import nakayo.gameserver.world.Executor;
import nakayo.gameserver.world.World;

/**
 * Calculates fortresses as 10 points and artifacts as 1 point each.
 * Need to find retail calculation. (Upper forts worth more...)
 *
 * @author Sarynth
 */
public class Influence {

    private float elyos = 0;

    private float asmos = 0;

    private float balaur = 0;

    private Influence() {
        calculateInfluence();
    }

    public static final Influence getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Recalculates Influence and Broadcasts new values
     */
    public void recalculateInfluence() {
        calculateInfluence();
        broadcastInfluencePacket();
    }

    /**
     * calculate influence
     */
    private void calculateInfluence() {
        int total = 0;
        int asmos = 0;
        int elyos = 0;
        int balaur = 0;
        for (SiegeLocation sLoc : SiegeService.getInstance().getSiegeLocations().values()) {
            int bonus = 0;
            switch(sLoc.getSiegeType()) {
                case ARTIFACT:
                    bonus = 1;
                    break;
                case FORTRESS:
                    bonus = 10;
                    break;
                default:
                    break;
            }
            total += bonus;
            switch(sLoc.getRace()) {
                case BALAUR:
                    balaur += bonus;
                    break;
                case ASMODIANS:
                    asmos += bonus;
                    break;
                case ELYOS:
                    elyos += bonus;
                    break;
            }
        }
        this.balaur = (float) balaur / total;
        this.elyos = (float) elyos / total;
        this.asmos = (float) asmos / total;
    }

    /**
     * Broadcast packet with influence update to all players.
     * - Responsible for the message "The Divine Fortress is now vulnerable."
     */
    private void broadcastInfluencePacket() {
        final SM_INFLUENCE_RATIO pkt = new SM_INFLUENCE_RATIO();
        World.getInstance().doOnAllPlayers(new Executor<Player>() {

            @Override
            public boolean run(Player player) {
                PacketSendUtility.sendPacket(player, pkt);
                return true;
            }
        });
    }

    /**
     * @return elyos control
     */
    public float getElyos() {
        return this.elyos;
    }

    /**
     * @return asmos control
     */
    public float getAsmos() {
        return this.asmos;
    }

    /**
     * @return balaur control
     */
    public float getBalaur() {
        return this.balaur;
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final Influence instance = new Influence();
    }
}
