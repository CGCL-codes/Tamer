package hu.openig.scripting;

import hu.openig.model.BattleInfo;
import hu.openig.model.Building;
import hu.openig.model.Fleet;
import hu.openig.model.GameScripting;
import hu.openig.model.GroundwarWorld;
import hu.openig.model.InventoryItem;
import hu.openig.model.Objective;
import hu.openig.model.Planet;
import hu.openig.model.Player;
import hu.openig.model.ResearchType;
import hu.openig.model.SpacewarScriptResult;
import hu.openig.model.SpacewarWorld;
import hu.openig.model.VideoMessage;
import hu.openig.utils.U;
import hu.openig.utils.XElement;
import java.util.Collections;
import java.util.List;

/**
 * An empty placeholder scripting for skirmish games.
 * @author akarnokd, 2012.01.12.
 */
public class EmptyScripting implements GameScripting {

    /** The main player. */
    protected Player player;

    /** Resume after win? */
    boolean resumeAfterWin;

    @Override
    public List<VideoMessage> getSendMessages() {
        return U.newArrayList();
    }

    @Override
    public List<VideoMessage> getReceiveMessages() {
        return U.newArrayList();
    }

    @Override
    public List<Objective> currentObjectives() {
        return Collections.emptyList();
    }

    @Override
    public void init(Player player, XElement in) {
        this.player = player;
    }

    @Override
    public void load(XElement in) {
        resumeAfterWin = in.getBoolean("resumeAfterWin", false);
    }

    @Override
    public void save(XElement out) {
        out.set("resumeAfterWin", resumeAfterWin);
    }

    @Override
    public void done() {
    }

    @Override
    public void onResearched(Player player, ResearchType rt) {
    }

    @Override
    public void onProduced(Player player, ResearchType rt) {
    }

    @Override
    public void onDestroyed(Fleet winner, Fleet loser) {
    }

    @Override
    public void onColonized(Planet planet) {
    }

    @Override
    public void onConquered(Planet planet, Player previousOwner) {
    }

    @Override
    public void onPlayerBeaten(Player player) {
    }

    @Override
    public void onDiscovered(Player player, Planet planet) {
    }

    @Override
    public void onDiscovered(Player player, Player other) {
    }

    @Override
    public void onDiscovered(Player player, Fleet fleet) {
    }

    @Override
    public void onLostSight(Player player, Fleet fleet) {
    }

    @Override
    public void onFleetAt(Fleet fleet, double x, double y) {
    }

    @Override
    public void onFleetAt(Fleet fleet, Fleet other) {
    }

    @Override
    public void onFleetAt(Fleet fleet, Planet planet) {
    }

    @Override
    public void onStance(Player first, Player second) {
    }

    @Override
    public void onAllyAgainst(Player first, Player second, Player commonEnemy) {
    }

    @Override
    public void onBattleComplete(Player player, BattleInfo battle) {
    }

    @Override
    public void onTime() {
        if (!resumeAfterWin) {
            int remaining = 0;
            for (Player p : player.world.players.values()) {
                if (p != player) {
                    remaining += p.statistics.planetsOwned;
                }
            }
            if (remaining == 0) {
                resumeAfterWin = true;
                player.world.env.pause();
                player.world.env.winGame();
            }
        }
    }

    @Override
    public void onBuildingComplete(Planet planet, Building building) {
    }

    @Override
    public void onRepairComplete(Planet planet, Building building) {
    }

    @Override
    public void onUpgrading(Planet planet, Building building, int newLevel) {
    }

    @Override
    public void onInventoryAdd(Planet planet, InventoryItem item) {
    }

    @Override
    public void onInventoryRemove(Planet planet, InventoryItem item) {
    }

    @Override
    public void onLost(Planet planet) {
    }

    @Override
    public void onLost(Fleet fleet) {
    }

    @Override
    public void onVideoComplete(String video) {
    }

    @Override
    public void onSoundComplete(String audio) {
    }

    @Override
    public void onPlanetInfected(Planet planet) {
    }

    @Override
    public void onPlanetCured(Planet planet) {
    }

    @Override
    public void onMessageSeen(String id) {
    }

    @Override
    public void onNewGame() {
    }

    @Override
    public void onLevelChanged() {
    }

    @Override
    public void onSpacewarFinish(SpacewarWorld war) {
    }

    @Override
    public void onSpacewarStart(SpacewarWorld war) {
    }

    @Override
    public SpacewarScriptResult onSpacewarStep(SpacewarWorld war) {
        return null;
    }

    @Override
    public void onGroundwarFinish(GroundwarWorld war) {
    }

    @Override
    public void onGroundwarStart(GroundwarWorld war) {
    }

    @Override
    public void onGroundwarStep(GroundwarWorld war) {
    }

    @Override
    public boolean mayControlFleet(Fleet f) {
        return true;
    }

    @Override
    public boolean mayAutoSave() {
        return true;
    }

    @Override
    public void onAutobattleFinish(BattleInfo battle) {
    }

    @Override
    public void onAutobattleStart(BattleInfo battle) {
    }

    @Override
    public void onTalkCompleted() {
    }

    @Override
    public void debug() {
    }

    @Override
    public boolean mayPlayerAttack(Player player) {
        return false;
    }

    @Override
    public void onDeploySatellite(Planet target, Player player, ResearchType satellite) {
    }

    @Override
    public boolean fleetBlink(Fleet f) {
        return false;
    }

    @Override
    public void onFleetsMoved() {
    }
}
