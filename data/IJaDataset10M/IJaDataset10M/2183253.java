package com.jme.util;

/**
 * Just a simple flag holder for runtime stripping of various jME logging and
 * debugging features.
 * 
 * @author Joshua Slack
 */
public class Debug {

    public static final boolean debug = (!"FALSE".equalsIgnoreCase(System.getProperty("jme.debug")));

    public static final boolean stats = (System.getProperty("jme.stats") != null);

    public static final boolean infoLogging = (System.getProperty("jme.info") != null) ? (!"FALSE".equalsIgnoreCase(System.getProperty("jme.info"))) : true;

    public static boolean updateGraphs = false;

    public static final boolean trackDirectMemory = (System.getProperty("jme.trackDirect") != null);
}
