package de.robowars.ai;

import de.robowars.comm.transport.RobotEnumeration;

/**
 * �berschrift:   RoboWars
 * Beschreibung:  creates an AIPlayer
 * Copyright:     Copyright (c) 2001
 * Organisation:
 * @author		  ivonne
 * @version 1.0
 */
public abstract class AIFactory {

    private static final String[] levels = { "SIMPLE", "ADVANCED" };

    /**
	 * Creates an AIPlayer with the given difficulty; 
	 * default: AdvancedAIPlayer
	 * @param level level of the AILevel, 
	 * @param name  name of the AIPlayer
	 * @return the created AIPlayer
	 */
    public static AIPlayer createAIPlayer(String level, String name, RobotEnumeration type) {
        AIPlayer ai = null;
        if (level == levels[0]) return (AIPlayer) new SimpleAIPlayer(name, type); else return (AIPlayer) new AdvancedAIPlayer(name, type);
    }

    /**
	 * returns all possible levels of the AI
	 * @return String[] 
	 */
    public static String[] getPossibleLevels() {
        return levels;
    }
}
