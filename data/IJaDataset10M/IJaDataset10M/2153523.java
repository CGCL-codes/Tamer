package ai.commanderAI;

import java.util.HashMap;
import scenario.MatchScenario;
import scenario.spawnStation.SpawnStation;
import ai.AIController;
import ai.pilotAI.ChaseController;
import ai.pilotAI.PilotAIController;
import com.jme.math.Vector3f;
import logic.Player;
import logic.Team;
import logic.nodes.TeamModelNode;
import logic.ships.hunter.Hunter;
import logic.ships.mothership.MotherShip;
import logic.ships.Ship;
import main.InitGame;

/**
 * Replaces an human Commander for a team. CommanderController sets suitable targets for all players of 
 * the team.
 * 
 * @author Wasserleiche
 */
public class CommanderController extends AIController {

    private static final long serialVersionUID = 1L;

    /** The maximum time a pilot can have the same target without damaging it enough. */
    private final float MAX_TARGET_TIME = 10f;

    /** The maximum distance of pilots to their targets. */
    private final float MAX_TARGET_DISTANCE = 500f;

    /** The minimal distance of enemy {@link Hunter} to a friendly {@link SpawnStation}, so the 
	 * {@link SpawnStation} will be defended. */
    private final float MIN_SPAWN_ENEMY_DIST = 100f;

    private Team team, enemyTeam;

    /** The maximum distance of all pilots to the own {@link MotherShip}. */
    private float MAX_MOTHERSHIP_DISTANCE;

    /** Stores for each possible target the number of pilots chasing this target. */
    private HashMap<TeamModelNode, Integer> targetCount;

    private MatchScenario scenario;

    /**
	 * Constructs a new CommanderController.
	 * @param team The team of this Commander.
	 * @param enemyTeam The enemy team of the given team.
	 */
    public CommanderController(Team team, Team enemyTeam) {
        super(team.getCommander(), 5f);
        this.team = team;
        this.enemyTeam = enemyTeam;
        scenario = InitGame.getMatchState().getGame().getScenario();
        MAX_MOTHERSHIP_DISTANCE = team.getMotherShipSpawn().distance(enemyTeam.getMotherShipSpawn());
        targetCount = new HashMap<TeamModelNode, Integer>();
        for (Player player : team.getAllPlayers()) {
            targetCount.put(player.getCurrentTarget(), new Integer(1));
        }
        for (Player player : enemyTeam.getAllPlayers()) {
            if (targetCount.get(player) == null && player.getHunter() != null) targetCount.put(player.getHunter(), new Integer(0));
        }
        if (targetCount.get(enemyTeam.getMotherShip()) == null) targetCount.put(enemyTeam.getMotherShip(), new Integer(0));
        for (SpawnStation spawnStation : scenario.getSpawns()) {
            targetCount.put(spawnStation, new Integer(0));
        }
    }

    @Override
    public void delayedUpdate(float time) {
        for (Player pilot : team.getBots()) {
            if (!pilot.isPilot() || !pilot.isAlive()) continue;
            if (defendMothership()) {
                Ship target = getNearestShip(pilot.getTeam().getMotherShipSpawn());
                setTargetOf(pilot, target);
                continue;
            }
            float motherShipDist = pilot.getHunter().getLocalTranslation().distance(enemyTeam.getMotherShipSpawn());
            if (motherShipDist > MAX_MOTHERSHIP_DISTANCE) {
                setTargetOf(pilot, enemyTeam.getMotherShip());
                continue;
            }
            ChaseController controller = pilot.getChaseController();
            if (badTarget(pilot) || (controller.isCapturing() && capturedSpawnStation(pilot))) setNewTarget(pilot);
        }
    }

    /**
	 * Sets a new suitable target for the given {@link Player}. A target is either the nearest enemy {@link Hunter} or 
	 * an enemy {@link Hunter} that is attacking a {@link SpawnStation} (the nearer of both possibilities will be chosen).
	 * @param pilot The {@link Player} that shall get a new target.
	 */
    private void setNewTarget(Player pilot) {
        boolean cont = false;
        for (SpawnStation station : team.getSpawnStations()) {
            if (defendSpawnStation(station)) {
                Ship target = getNearestShip(station.getWorldTranslation());
                setTargetOf(pilot, target);
                cont = true;
                break;
            }
        }
        if (cont) return;
        Vector3f pilotPos = pilot.getHunter().getWorldTranslation();
        Ship nearShip = getNearestShip(pilotPos);
        float shipDist = nearShip.getWorldTranslation().distance(pilotPos);
        SpawnStation nearSpawnStation = getNearestSpawnStation(pilotPos);
        float spawnStationDist = nearSpawnStation.getWorldTranslation().distance(pilotPos);
        int shipCount = getTargetCountOf(nearShip);
        int spawnStationCount = getTargetCountOf(nearSpawnStation);
        if (shipDist <= spawnStationDist || shipCount <= spawnStationCount) setTargetOf(pilot, nearShip); else setTargetOf(pilot, nearSpawnStation);
    }

    /**
	 * Finds the nearest enemy {@link Ship} to the given position. It can either be a {@link Hunter} or 
	 * the enemy {@link MotherShip}.
	 * @param loc The position from where the nearest target has to be computed.
	 * @return The nearest enemy {@link Ship} to the given position.
	 */
    private Ship getNearestShip(Vector3f loc) {
        float minDist = Float.POSITIVE_INFINITY;
        Ship target = null;
        for (Player enemyPilot : enemyTeam.getAllPlayers()) {
            if (!enemyPilot.isAlive() || !enemyPilot.isPilot()) continue;
            float dist = enemyPilot.getHunter().getLocalTranslation().distance(loc);
            if (dist < minDist) {
                minDist = dist;
                target = enemyPilot.getHunter();
            }
        }
        if (enemyTeam.getMotherShipSpawn().distance(loc) < minDist) target = enemyTeam.getMotherShip();
        return target;
    }

    /**
	 * Sets the given target to the given {@link Player}. The Target-Count of the given target will be increased 
	 * while the Target-Count of the current target of the {@link Player} will be decreased.
	 * @param pilot The {@link Player} that shall get a new target.
	 * @param target The target to be set.
	 */
    private void setTargetOf(Player pilot, TeamModelNode target) {
        increaseTargetCount(target);
        decreaseTargetCount(pilot.getCurrentTarget());
        pilot.setCurrentTarget(target);
    }

    /**
	 * Finds the nearest {@link SpawnStation} to the given location, that does not belong to this team.
	 * @param loc The position from where to search.
	 * @return The nearest {@link SpawnStation}.
	 */
    private SpawnStation getNearestSpawnStation(Vector3f loc) {
        float minDist = Float.POSITIVE_INFINITY;
        SpawnStation target = null;
        for (SpawnStation station : scenario.getSpawns()) {
            if (station.getTeam() == team) continue;
            float dist = station.getWorldTranslation().distance(loc);
            if (dist < minDist) {
                minDist = dist;
                target = station;
            }
        }
        return target;
    }

    /**
	 * Checks if the given {@link Player} has a target that should be replaced. A target should be 
	 * replaced if more than half of all other Team-Mates have the same target, or the target has been 
	 * chased too long without doing much damage to it or the distance to the target is too big.
	 * @param pilot The {@link Player} whose target has to be checked.
	 * @return true, if the {@link Player} has a target that should be replaced. false, else.
	 */
    private boolean badTarget(Player pilot) {
        TeamModelNode target = pilot.getCurrentTarget();
        int count = getTargetCountOf(target);
        if (count > team.getPlayerNumber() / 2) return true;
        Vector3f targetPos = target.getLocalTranslation();
        float targetDist = pilot.getHunter().getLocalTranslation().distance(targetPos);
        boolean targetTimeTooLong = ((PilotAIController) pilot.getChaseController()).getTargetTime() > MAX_TARGET_TIME;
        float targetShieldRate = target.getCurrentShields() / target.getMaxShields();
        float targetHPRate = target.getCurrentHP() / target.getMaxHP();
        boolean targetHasNoDamage = targetShieldRate > 0.7f && targetHPRate > 0.9f;
        return ((targetTimeTooLong && targetHasNoDamage) || targetDist > MAX_TARGET_DISTANCE);
    }

    /**
	 * Returns the Target-Count of the given {@link TeamModelNode}. If the given target has no 
	 * Target-Count yet, it will be initialized with Zero.
	 * @param target The target whose Target-Count shall be returned.
	 * @return The number of chasing Team-Mates for this target.
	 */
    private Integer getTargetCountOf(TeamModelNode target) {
        if (targetCount.get(target) == null) {
            targetCount.put(target, new Integer(0));
            return 0;
        }
        return targetCount.get(target);
    }

    /**
	 * Increases the Target-Count of the given {@link TeamModelNode} by One.
	 * @param target The target to increase the Target-Count.
	 */
    private void increaseTargetCount(TeamModelNode target) {
        int currentTargetCount = getTargetCountOf(target);
        targetCount.put(target, new Integer(currentTargetCount + 1));
    }

    /**
	 * Decreases the Target-Count of the given {@link TeamModelNode} by One.
	 * @param target The target to decrease the Target-Count.
	 */
    private void decreaseTargetCount(TeamModelNode target) {
        int currentTargetCount = getTargetCountOf(target);
        if (currentTargetCount > 0) targetCount.put(target, new Integer(currentTargetCount - 1));
    }

    /**
	 * Checks if the given {@link SpawnStation} should be defended. This is the case, if the 
	 * {@link SpawnStation} has less than half of his shields or if there is a {@link Ship} in the 
	 * near surrounding.
	 * @param station The {@link SpawnStation} that has to be checked.
	 * @return true, if the given {@link SpawnStation} should be defendet. false, else.
	 */
    private boolean defendSpawnStation(SpawnStation station) {
        if (station.getCurrentShields() <= station.getMaxShields() / 2) return true;
        Ship ship = getNearestShip(station.getWorldTranslation());
        float dist = ship.getWorldTranslation().distance(station.getWorldTranslation());
        return dist < MIN_SPAWN_ENEMY_DIST;
    }

    /**
	 * Checks if the given {@link Player} has just captured a {@link SpawnStation}. That is, if the 
	 * {@link Player}'s target is {@link SpawnStation} and if the {@link SpawnStation} belongs to the 
	 * {@link Team}.
	 * @param pilot The {@link Player} that shall be checked.
	 * @return true, if the given {@link Player} did just capture a {@link SpawnStation}. false, else.
	 */
    private boolean capturedSpawnStation(Player pilot) {
        TeamModelNode target = pilot.getCurrentTarget();
        return (target instanceof SpawnStation && target.getTeam() == team);
    }

    /**
	 * Checks, if the {@link MotherShip} of the {@link Team} has to be defended. That is, if the 
	 * shields of the {@link MotherShip} are dropped to 50%.
	 * @return true, if the {@link MotherShip} has to be defended. false, else.
	 */
    private boolean defendMothership() {
        return team.getMotherShip().getCurrentShields() <= team.getMotherShip().getMaxShields() / 2;
    }
}
