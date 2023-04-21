package adlez.application.main;

public class GameTimer {

    protected long startTime;

    protected long lastTick;

    protected static GameTimer instance;

    private GameTimer() {
        this.startTime = this.lastTick = System.currentTimeMillis();
    }

    public static GameTimer getInstance() {
        if (GameTimer.instance == null) {
            GameTimer.instance = new GameTimer();
        }
        return GameTimer.instance;
    }

    public void nextTick() {
        this.lastTick = System.currentTimeMillis();
    }

    public long getTimeSinceLastTick() {
        return (System.currentTimeMillis() - this.lastTick);
    }

    public long getTimeSinceLastTick(boolean save) {
        if (save && ((System.currentTimeMillis() - this.lastTick) == 0)) {
            return 1;
        }
        return (System.currentTimeMillis() - this.lastTick);
    }
}
