package org.icehockeymanager.ihm.game.scheduler.events;

import java.io.*;
import java.util.*;
import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * SchedulerBreakerGroupEvent is a container for SchedulerBreakerEvents.
 * 
 * @author Bernhard von Gunten
 * @created October 2004
 */
public class SchedulerBreakerGroupEvent extends SchedulerEvent implements Serializable {

    /**
   * Comment for <code>serialVersionUID</code>
   */
    private static final long serialVersionUID = 3256437027829594423L;

    /**
   * Breakers
   */
    private Vector<SchedulerBreakerEvent> breakers = null;

    /**
   * SchedulerBreakerGroupEvent constructor
   * 
   * @param source
   *          Object
   * @param day
   *          Calendar
   * @param messageKey
   *          String
   */
    public SchedulerBreakerGroupEvent(Object source, Calendar day, String messageKey) {
        super(source, day, messageKey);
        this.breakers = new Vector<SchedulerBreakerEvent>();
    }

    /**
   * Plays the super.play() function and "finalizes" this function, so no real
   * BreakerEvent may have a play function.
   */
    public final void play() {
        super.play();
        SchedulerBreakerEvent[] myEvents = getAllBreakers();
        for (int i = 0; i < myEvents.length; i++) {
            myEvents[i].play();
        }
    }

    /**
   * Returns array of User who are interested in at least one of the
   * BreakerEvents.
   * 
   * @return User[]
   */
    public User[] getUsersInterested() {
        User[] allUsers = GameController.getInstance().getScenario().getUsers();
        Vector<User> interestedUsers = new Vector<User>();
        for (int i = 0; i < allUsers.length; i++) {
            if (getSchedulerEventBreakersByUser(allUsers[i]).length > 0) {
                interestedUsers.add(allUsers[i]);
            }
        }
        return interestedUsers.toArray(new User[interestedUsers.size()]);
    }

    /**
   * Returns all SchedulerBreakerEvents
   * 
   * @return SchedulerBreakerEvent[]
   */
    public SchedulerBreakerEvent[] getAllBreakers() {
        return breakers.toArray(new SchedulerBreakerEvent[breakers.size()]);
    }

    /**
   * Returns all SchedulerBreakerEvents in which a given user is interested in.
   * 
   * @param user
   *          User
   * @return SchedulerBreakerEvent[]
   */
    public SchedulerBreakerEvent[] getSchedulerEventBreakersByUser(User user) {
        Vector<SchedulerBreakerEvent> result = new Vector<SchedulerBreakerEvent>();
        for (int i = 0; i < breakers.size(); i++) {
            SchedulerBreakerEvent breaker = breakers.get(i);
            if (breaker.isUserInterested(user)) {
                result.add(breaker);
            }
        }
        return result.toArray(new SchedulerBreakerEvent[result.size()]);
    }

    /**
   * Add a SchedulerBreakerEvent to the collection.
   * 
   * @param breaker
   *          SchedulerBreakerEvent
   */
    public void addSchedulerEventBreaker(SchedulerBreakerEvent breaker) {
        breakers.add(breaker);
    }
}
