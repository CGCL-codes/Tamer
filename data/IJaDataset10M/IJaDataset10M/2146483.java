package ppsim.server.utils;

import java.util.*;
import ppsim.server.core.*;

/**
 * Schedules repainting of process image.
 * 
 * @author danielg
 */
public class ViewRefresher {

    Timer timer;

    public ViewRefresher(float seconds) {
        timer = new Timer();
        timer.schedule(new RepaintTask(), (int) (seconds * 1000), (int) (seconds * 1000));
    }

    class RepaintTask extends TimerTask {

        public void run() {
            ServerConstants.procImage.repaint();
        }
    }

    /**
	 * @return Returns the timer.
	 */
    public Timer getTimer() {
        return timer;
    }

    /**
	 * Stops this view-refresher.
	 */
    public void stop() {
        timer.cancel();
    }
}
