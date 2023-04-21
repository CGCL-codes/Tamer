package org.icehockeymanager.ihm.game.randomimpacts.log;

import java.util.*;
import org.icehockeymanager.ihm.game.eventlog.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.randomimpacts.*;

/**
 * RandomImpactsPlayerLog is a log containing the random impact object.
 * 
 * @author Bernhard von Gunten
 * @created October, 2004
 */
public class RandomImpactsPlayerLog extends EventLogEntry {

    /**
   * Comment for <code>serialVersionUID</code>
   */
    private static final long serialVersionUID = 3257003259171715129L;

    /**
   * MESSAGE_KEY
   */
    public static final String MESSAGE_KEY = "events.playerInjured";

    /**
   * Random impact
   */
    private RandomImpactPlayer randomImpactPlayer = null;

    /**
   * RandomImpactsPlayerLog constructor
   * 
   * @param source
   * @param day
   * @param player
   * @param randomImpactPlayer
   */
    public RandomImpactsPlayerLog(Object source, Calendar day, Player player, RandomImpactPlayer randomImpactPlayer) {
        super(source, day, player);
        this.randomImpactPlayer = randomImpactPlayer;
    }

    /**
   * Returns the RandomImpactPlayer object.
   * 
   * @return randomImpactPlayer
   */
    public RandomImpactPlayer getRandomImpact() {
        return randomImpactPlayer;
    }
}
