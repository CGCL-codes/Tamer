package com.pallas.unicore.utility;

import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;

/**
 *  This class represents the different logging levels
 *
 *@author     Kirsten Foerster
 *@version    $Id: LoggingLevels.java,v 1.1 2004/05/25 14:58:53 rmenday Exp $
 */
public class LoggingLevels {

    private Vector levels = null;

    private Hashtable levelsTable = null;

    public LoggingLevels() {
        this.init();
    }

    /**
	 *  add a Level object
	 *
	 *@param  level  - a new created Level object
	 */
    public void add(Level level) {
        levels.addElement(level);
        this.levelsTable.put(level.toString(), level);
    }

    public Level get(String object) {
        return (Level) this.levelsTable.get(object);
    }

    public Vector getLevels() {
        return levels;
    }

    /**
	 * initialize the vector levels and the hashtable levelsTable conisting of
	 * all levels and their belonging strings
	 */
    private void init() {
        this.initLevels();
        this.initLevelsTable(levels);
    }

    /**
	 *  initialize the vector levels
	 */
    private void initLevels() {
        levels = new Vector();
        levels.addElement(Level.ALL);
        levels.addElement(Level.FINEST);
        levels.addElement(Level.FINER);
        levels.addElement(Level.FINE);
        levels.addElement(Level.CONFIG);
        levels.addElement(Level.INFO);
        levels.addElement(Level.WARNING);
        levels.addElement(Level.SEVERE);
        levels.addElement(Level.OFF);
    }

    /**
	 *  initialize the hashtable levelsTable conisting of all levels and their
	 *  belonging strings
	 *
	 *@param  levels  - a vector with Level-objects
	 */
    private void initLevelsTable(Vector levels) {
        this.levelsTable = new Hashtable();
        for (int i = 0; i < levels.size(); i++) {
            this.levelsTable.put(((Level) levels.elementAt(i)).toString(), (Level) levels.elementAt(i));
        }
    }
}
