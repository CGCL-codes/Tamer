package clubmixer.server.ui.options.utils;

import java.util.TimerTask;

/**
 *
 * @author Alexander Schindler
 */
public class TickTock extends TimerTask {

    private final ICountable label;

    /**
     * Constructs ...
     *
     *
     * @param lbl
     */
    public TickTock(ICountable lbl) {
        this.label = lbl;
    }

    /**
     * Method description
     *
     */
    public void run() {
        label.count();
    }
}
