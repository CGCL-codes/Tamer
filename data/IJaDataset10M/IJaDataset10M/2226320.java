package biz.xsoftware.examples.timer;

import java.util.EventListener;

/**
 * Listener interface to notify client of an event starting.
 * 
 * @author Dean Hiller
 */
public interface ScheduleListener extends EventListener {

    /**
	 * @param title
	 */
    void eventStarted(String title);
}
