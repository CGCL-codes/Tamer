package org.freelords.server.phase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.freelords.armies.Army;
import org.freelords.armies.EntityStat;
import org.freelords.client.remote.ClientEntitiesHandler;
import org.freelords.client.remote.ClientPlayerHandler;
import org.freelords.entity.EntityDisappearance;
import org.freelords.game.Game;
import org.freelords.game.TurnPhase;
import org.freelords.player.Player;
import org.freelords.server.Server;
import org.freelords.server.ServerUtils;
import org.freelords.util.Rand;

/** Phase where player's units desert.
  * 
  * <p>
  * When the player does not have enough gold to pay upkeep for his troops, his units
  * desert. This is put into a phase separate from the income and upkeep phase
  * because the code, and especially the phase output is largely different.
  * </p>
  * 
  * <p>
  * Some note: Units in cities do not need upkeep, but if they desert, their
  * upkeep is effectively added to the player's treasury. This is intentional.
  * It means that units in cities can desert, but do not do so excessively.
  * </p>
  *
  * @author Ulf Lorenz
  */
public class DesertionPhase implements GamePhase {

    /** The results object */
    private DesertionOutput output;

    /** Constructor for the Phase; sets up the random number generator. */
    public DesertionPhase() {
    }

    /**
	* {@inheritDoc}
	*/
    public boolean executePhase(Server server) {
        return executePhase(server.getGame());
    }

    /**
	 * {@inheritDoc}
	*/
    public boolean executePhase(Game game) {
        output = new DesertionOutput();
        for (Player p : game.getAllPlayers().getAll()) {
            while (p.getGold() < 0) {
                Collection<Army> owned = game.getOwners().get(p.getId()).getOwnedArmies();
                int index = Rand.GEN.nextInt(owned.size());
                Iterator<Army> it = owned.iterator();
                while (index > 0) {
                    it.next();
                    index--;
                }
                Army army = it.next();
                p.adjustGold(army.getTemplate().getBaseStats().getModifiers().get(EntityStat.UPKEEP));
                game.remove(army);
                output.addArmy(army);
            }
        }
        return true;
    }

    /** This is the desertion phase */
    public TurnPhase getPhase() {
        return TurnPhase.DESERTION;
    }

    /** This phase does not require player input. */
    public boolean requiresPlayerInput() {
        return false;
    }

    /** Returns the phase output object */
    public PhaseOutput getPhaseOutput() {
        return output;
    }

    /** The internal Phase output class. */
    private static class DesertionOutput implements PhaseOutput {

        /** List of deserted armies */
        private List<Army> deserters = new ArrayList<Army>();

        /** Adds another army to the deserters list */
        public void addArmy(Army army) {
            deserters.add(army);
        }

        /** Execution of desertions on the client */
        public void execute(Server server) {
            for (Army a : deserters) {
                server.getBroadcaster(ClientEntitiesHandler.class).destroy(Collections.singleton(a), EntityDisappearance.DESERTED);
                server.getClient(a.getPlayer(), ClientPlayerHandler.class).goldAdjust(a.getTemplate().getBaseStats().getModifiers().get(EntityStat.UPKEEP));
                ServerUtils.loseTest(server, a.getPlayer());
            }
        }
    }
}
