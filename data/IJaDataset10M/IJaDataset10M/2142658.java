package ar.com.omnipresence.game.client;

import java.io.Serializable;

/**
 *
 * @author Martín Straus
 */
public class TickerTO implements Serializable {

    private Long id;

    private long interval;

    private long currentTick;

    private boolean running;

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public long getCurrentTick() {
        return currentTick;
    }

    public void setCurrentTick(long currentTick) {
        this.currentTick = currentTick;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }
}
