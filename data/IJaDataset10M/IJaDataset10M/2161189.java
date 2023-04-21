package pl.org.minions.stigma.server;

import java.util.LinkedList;
import java.util.List;
import pl.org.minions.stigma.databases.actor.ArchetypeDB;
import pl.org.minions.stigma.databases.actor.ProficiencyDB;
import pl.org.minions.stigma.databases.actor.server.ArchetypeDBSync;
import pl.org.minions.stigma.databases.actor.server.ProficiencyDBSync;
import pl.org.minions.stigma.databases.item.ItemFactory;
import pl.org.minions.stigma.databases.item.ItemTypeDB;
import pl.org.minions.stigma.databases.item.ModifierDB;
import pl.org.minions.stigma.databases.item.server.ItemTypeDBSync;
import pl.org.minions.stigma.databases.item.server.ModifierDBSync;
import pl.org.minions.stigma.databases.map.server.MapDBSync;
import pl.org.minions.stigma.databases.map.server.StaticItemsDB;
import pl.org.minions.stigma.databases.map.server.StaticNpcsDB;
import pl.org.minions.stigma.databases.server.PlayerLoadDB;
import pl.org.minions.stigma.databases.server.PlayerSaveDB;
import pl.org.minions.stigma.databases.sql.SqlAsyncDB;
import pl.org.minions.stigma.game.world.ExtendedWorld;
import pl.org.minions.stigma.game.world.server.ExtendedWorldImpl;
import pl.org.minions.stigma.network.GlobalConnector;
import pl.org.minions.stigma.network.server.Authenticator;
import pl.org.minions.stigma.network.server.Listener;
import pl.org.minions.stigma.network.server.LoggedPlayersCache;
import pl.org.minions.stigma.network.server.ServerGlobalConnector;
import pl.org.minions.stigma.server.managers.ChatManager;
import pl.org.minions.stigma.server.managers.WorldManager;
import pl.org.minions.utils.logger.Log;

/**
 * Singleton representing game server instance.
 */
public final class Server {

    private final class ShuttingThread implements Runnable {

        private boolean graceful;

        public ShuttingThread(boolean graceful) {
            this.graceful = graceful;
        }

        @Override
        public void run() {
            GlobalConnector.globalInstance().stop();
            authenticator.stop();
            listener.closeSocket();
            chatManager.stop();
            chatManager = null;
            worldManager.stop();
            worldManager = null;
            playerLoadDB.freeResources();
            playerSaveDB.freeResources();
            SqlAsyncDB.shutGlobalInstance(graceful);
            logonDB.stop();
            Log.logger.info("Server stopped");
            synchronized (state) {
                setState(State.STOPPED);
            }
        }
    }

    private final class StartingThread implements Runnable {

        @Override
        public void run() {
            ServerConfig conf = ServerConfig.globalInstance();
            ExtendedWorld world = new ExtendedWorldImpl(new MapDBSync(conf.getResourceUri()));
            StaticNpcsDB layerDB = new StaticNpcsDB(conf.getResourceUri());
            StaticItemsDB itemsDB = new StaticItemsDB(conf.getResourceUri());
            ArchetypeDB archetypeDB = new ArchetypeDBSync(conf.getResourceUri());
            ItemTypeDB itemTypeDB = new ItemTypeDBSync(conf.getResourceUri());
            ModifierDB modifierDB = new ModifierDBSync(conf.getResourceUri());
            ProficiencyDB proficiencyDB = new ProficiencyDBSync(conf.getResourceUri());
            ItemFactory.initialize(itemTypeDB, modifierDB);
            chatManager = new ChatManager();
            worldManager = new WorldManager(world, chatManager, layerDB, itemsDB, archetypeDB, itemTypeDB, modifierDB, proficiencyDB);
            SqlAsyncDB.initGlobalInstance(conf.getSqlUrl(), conf.getSqlLogin(), conf.getSqlPassword(), conf.getSqlDriver());
            playerLoadDB = new PlayerLoadDB(archetypeDB);
            playerSaveDB = new PlayerSaveDB(archetypeDB);
            logonDB = new LoggedPlayersCache(playerSaveDB);
            authenticator = new Authenticator(worldManager, chatManager, logonDB, playerLoadDB);
            listener = new Listener(authenticator);
            ServerGlobalConnector.initGlobalInstance(listener);
            Log.logger.info("Server started");
            synchronized (state) {
                setState(State.WORKING);
            }
            if (!playerLoadDB.isOk()) {
                Log.logger.error("Couldn't start server - bad database");
                stop(false);
                return;
            }
        }
    }

    /**
     * Enum representing current server state.
     */
    public static enum State {

        BOOTING, WORKING, SHUTTING, STOPPED
    }

    private static Server instance = new Server();

    private List<ServerObserver> observers = new LinkedList<ServerObserver>();

    private WorldManager worldManager;

    private ChatManager chatManager;

    private Listener listener;

    private Authenticator authenticator;

    private LoggedPlayersCache logonDB;

    private PlayerLoadDB playerLoadDB;

    private PlayerSaveDB playerSaveDB;

    private State state = State.STOPPED;

    private Server() {
    }

    /**
     * Returns global instance of server.
     * @return global instance of server
     */
    public static Server globalInstance() {
        return instance;
    }

    /**
     * Adds observer.
     * @param observer
     *            observer to add
     */
    public void addObserver(ServerObserver observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Returns current server state.
     * @return current server state.
     */
    public State getState() {
        synchronized (state) {
            return state;
        }
    }

    /**
     * Returns {@code true} when server should be booting.
     * @return {@code true} when server should be booting.
     */
    public boolean isBooting() {
        synchronized (state) {
            return state == State.BOOTING;
        }
    }

    /**
     * Returns {@code true} when server should be shutting
     * down.
     * @return {@code true} when server should be shutting
     *         down.
     */
    public boolean isShuttingDown() {
        synchronized (state) {
            return state == State.SHUTTING;
        }
    }

    /**
     * Returns {@code true} when server should be stopped.
     * @return {@code true} when server should be stopped.
     */
    public boolean isStopped() {
        synchronized (state) {
            return state == State.STOPPED;
        }
    }

    /**
     * Returns {@code true} when server should be working.
     * @return {@code true} when server should be working.
     */
    public boolean isWorking() {
        synchronized (state) {
            return state == State.WORKING;
        }
    }

    /**
     * Removes observer.
     * @param observer
     *            to remove
     */
    public void removeObserver(ServerObserver observer) {
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    private void setState(State state) {
        this.state = state;
        synchronized (observers) {
            for (ServerObserver o : observers) o.stateChanged();
        }
    }

    /**
     * Initializes most static classes, starts
     * {@link Listener}, {@link GlobalConnector} and all
     * databases access classes. After this server should be
     * fully functional.
     */
    public void start() {
        synchronized (state) {
            if (state != State.STOPPED) {
                Log.logger.warn("Tried to start not stopped server");
                return;
            }
            setState(State.BOOTING);
        }
        Thread t = new Thread(new StartingThread(), "startingThread");
        t.start();
    }

    /**
     * Stops whole server. Closes network connection (hard
     * way), disconnects from databases, stops threads etc.
     * Should leave clean Java VM.
     * @param graceful
     *            when {@code true} server will stop
     *            "gracefully", saving all scheduled for
     *            saving actors, when {@code false} server
     *            will be just "killed"
     */
    public void stop(boolean graceful) {
        synchronized (state) {
            if (state != State.WORKING) {
                Log.logger.warn("Tried to stop not running server");
                return;
            }
            setState(State.SHUTTING);
        }
        Thread t = new Thread(new ShuttingThread(graceful), "shuttingThread");
        t.start();
    }
}
