package clubmixer.client.utils;

import java.util.TimerTask;

/**
 *
 * @author Alexander Schindler
 */
public class TickTock extends TimerTask {

    private ICountable lbl;

    public TickTock(ICountable lbl) {
        this.lbl = lbl;
    }

    public void run() {
        lbl.count();
    }
}
