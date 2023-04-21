package org.icehockeymanager.ihm.game.scheduler;

import java.util.*;
import org.icehockeymanager.ihm.game.scheduler.events.*;

/**
 * A simple abstract Scheduler eventListener. This listener is provided by the
 * GameThread.
 * 
 * <p>
 * The Listener fires events for every of the 3 different SchedulerEvents.
 * (Internal, Breaker and Special). All SchedulerBreakerEvents are fired as a
 * dailiy group (SchedulerBreakerGroupEvent).
 * 
 * @author Bernhard von Gunten & Arik Dasen
 * @created December 2001
 */
public abstract interface SchedulerListener extends EventListener {

    /**
   * Fire SchedulerSpecialEvnet.
   * 
   * @param e
   *          SchedulerSpecialEvent
   */
    public abstract void schedulerSpecialEvent(SchedulerSpecialEvent e);

    /**
   * Fire SchedulerInternalEvent.
   * 
   * @param e
   *          SchedulerInternalEvent
   */
    public abstract void schedulerInternalEvent(SchedulerInternalEvent e);

    /**
   * Fire SchedulerBreakerGroupEvent.
   * 
   * @param e
   *          SchedulerBreakerGroupEvent
   */
    public abstract void schedulerBreakerGroupEvent(SchedulerBreakerGroupEvent e);
}
