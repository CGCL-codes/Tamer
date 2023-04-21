package org.jazzteam.admiralbattles.logging;

/**
 * 
 * 
 * @author ADLeR
 * @version $Rev: $
 */
public class LoggingEvent {

    public static int TEXT_EVENT = 0;

    public static int SHIP_ADDING_EVENT = 1;

    public static int SHOTS_EVENT = 2;

    protected int type;

    public int getType() {
        return type;
    }
}
