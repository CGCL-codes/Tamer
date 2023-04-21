package serfsoftherealm.game;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import serfsoftherealm.data.Faction;
import serfsoftherealm.data.Time;
import serfsoftherealm.data.World;
import serfsoftherealm.game.actions.PlayerAction;
import serfsoftherealm.game.worldtasks.TerminateTask;
import serfsoftherealm.game.worldtasks.WorldExecutorThread;
import serfsoftherealm.game.worldtasks.WorldMonitor;
import serfsoftherealm.game.worldtasks.WorldTask;
import serfsoftherealm.staticdata.StaticWorldData;

public abstract class Scenario {

    protected final StaticWorldData worldData;

    protected final WorldMonitor worldMon;

    protected final LinkedBlockingQueue<WorldTask> worldTasks;

    /** Remembered version used to detect world changes. */
    private int lastWorldVersion = -1;

    /** Remembered game time to detect end of turn being carried out. */
    private Time lastTurn = null;

    protected String thisPlayer;

    protected HashMap<String, Integer> playersToFactions = new HashMap<String, Integer>();

    protected Scenario(StaticWorldData worldData, Random rng) {
        this.worldData = worldData;
        worldMon = new WorldMonitor();
        worldTasks = new LinkedBlockingQueue<WorldTask>();
        World world = createTheWorld();
        WorldExecutorThread worldExecThread = new WorldExecutorThread(worldTasks, world, worldData, worldMon, rng);
        worldExecThread.start();
    }

    public World getWorld() {
        return worldMon.getWorld();
    }

    public Faction getFaction() {
        World w = getWorld();
        return w.getFactions().get(playersToFactions.get(thisPlayer));
    }

    public abstract void postAction(PlayerAction action);

    protected WorldMonitor getWorldMonitor() {
        return worldMon;
    }

    protected LinkedBlockingQueue<WorldTask> getWorldTasks() {
        return worldTasks;
    }

    public void terminateWorldExecutorThread() {
        worldTasks.add(new TerminateTask());
    }

    public void finalize() {
        terminateWorldExecutorThread();
    }

    public boolean checkForWorldChange() {
        int version = worldMon.getVersion();
        if (version == lastWorldVersion) {
            return false;
        }
        lastWorldVersion = version;
        detectEndOfTurn();
        return true;
    }

    private void detectEndOfTurn() {
        Time turn = getWorld().getTime();
        if (turn.equals(lastTurn)) {
            return;
        }
        lastTurn = turn;
        newTurn();
    }

    protected abstract void newTurn();

    public StaticWorldData getStaticWorldData() {
        return worldData;
    }

    /**
	 * This can't be done in constructor since a world usually needs the corresponding
	 * static world data to be created. E.g. we get a situation where derived
	 * scenario calling Scenario(World, WorldData) can't create the world data
	 * before the World.
	 * @return
	 */
    protected abstract World createTheWorld();
}
