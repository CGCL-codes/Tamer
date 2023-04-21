package gui;

import starting.runner.GARunner;
import java.util.concurrent.locks.Lock;

/**
 * Created by IntelliJ IDEA.
 * User: HREN_VAM
 * Date: 06.04.2008
 * Time: 0:01:03
 * To change this template use File | Settings | File Templates.
 */
public class FrameCreator implements Runnable {

    private GARunner run;

    private Lock lock;

    public FrameCreator(GARunner run, Lock lock) {
        this.run = run;
        this.lock = lock;
    }

    public void run() {
        new MainFrame("!!!", run);
        lock.unlock();
    }
}
