package at.syncme.framework;

/**
 * the game super-class. it always has exactly one current game-state, that has
 * to be maintained. a game should only talk to an algorithm, to forward the
 * event, and not directly to the network
 * 
 * @author Daniel Rudigier
 */
public abstract class Game implements Runnable, EventHandler {

    protected String name;

    protected GameState gs;

    protected Algorithm algo;

    protected EventHandler eventHandler;

    protected boolean running = true;

    private long gameStart = System.currentTimeMillis();

    private boolean connected;

    private String[] connectionUrls;

    /**
     * constructor
     * 
     * @param gs
     *            the empty specific game state
     * @param algo
     *            the specific algorithm
     */
    public Game(final GameState gs, final Algorithm algo) {
        this.gs = gs;
        this.gs.setGvt(1);
        this.algo = algo;
        this.algo.setGame(this);
    }

    /**
     * @param name
     *            to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @return gs
     */
    public GameState getGs() {
        return gs;
    }

    /**
     * @return algo
     */
    public Algorithm getAlgo() {
        return algo;
    }

    /**
     * start the game
     */
    public void start() {
        new Thread(this).start();
    }

    /**
     * override
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        this.running = true;
        while (this.running) {
            this.gs.updateGame();
            this.gs.setGvt(System.currentTimeMillis() - this.gameStart);
        }
        this.running = false;
        end();
    }

    /**
     * incoming game event, that have to be processed by the game. this is the
     * event sink. locally created events that need to be transferred (player moves) 
     * should not directly call this method, rather let the algorithm take care about
     * the synchronization beforehand.
     * 
     * @param e
     */
    public abstract void gameEvent(final Event e);

    /**
     * a setup event, to be advised by a network aspect. these events are
     * communication events between display and network. for example, a connect event
     * might include all data to connect to a game
     * 
     * @param e
     */
    public abstract void setupEvent(Event e);

    /**
     * game ended, release resources
     */
    protected abstract void end();

    /**
     * @param connected to set
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     * @param gs to set
     */
    public void setGs(GameState gs) {
        this.gs = gs;
    }

    /**
     * @return connected
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * @return running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * @return connectionUrls
     */
    public String[] getConnectionUrls() {
        return connectionUrls;
    }

    /**
     * @param connectionUrls to set
     */
    public void setConnectionUrls(String[] connectionUrls) {
        this.connectionUrls = connectionUrls;
    }
}
