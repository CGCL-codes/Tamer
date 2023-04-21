package com.monad.homerun.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *  Binding is a base class representing an action rule
 *  bound to a context of execution. Subclasses include
 *  {@link com.monad.homerun.action.TimeBinding} (an action
 *  bound to a specific recurring time in a schedule), 
 *  {@link com.monad.homerun.action.EventBinding} (an
 *  action bound to the occurance of an event in an event plan),
 *  and {@link com.monad.homerun.action.DateBinding} (an
 *  action bound to a date in a Calendar),
 */
public class Binding implements Serializable {

    private static final long serialVersionUID = 5056521834569892827L;

    /**
	 * Run (execute) the action rule immediately
	 */
    public static final int RUN_MODE = 0;

    /**
     * Start (schedule for execution when conditions met) the action rule
     */
    public static final int START_MODE = 1;

    /**
     * Stop (de-schedule for execution) the action rule
     */
    public static final int STOP_MODE = 2;

    public static final String RUN_MODE_STR = "Run";

    public static final String START_MODE_STR = "Start";

    public static final String STOP_MODE_STR = "Stop";

    protected int mode = RUN_MODE;

    protected String actionName = null;

    protected String actionCategory = null;

    private String note = null;

    private Map<String, String> props = null;

    /**
     * Creates a binding with no attributes.
     */
    public Binding() {
    }

    /**
     * Creates a binding with specified mode, name and category.
     * 
     * @param mode
     *        the action mode (run, start, or stop)
     * @param actionName
     *        the name of the bond action
     * @param category
     *        the category of the action (which is its namespace)
     */
    public Binding(int mode, String actionName, String category) {
        this.mode = mode;
        this.actionName = actionName;
        this.actionCategory = category;
    }

    /**
     * Creates a binding cloned from passed binding.
     * 
     * @param b
     *        the binding to clone
     */
    public Binding(Binding b) {
        this.mode = b.mode;
        this.actionName = b.actionName;
        this.actionCategory = b.actionCategory;
        this.props = b.props;
    }

    /**
     * Returns the binding mode
     * 
     * @return mode - the binding mode (run, start or stop)
     */
    public int getMode() {
        return mode;
    }

    /**
     * Returns a readable string for the mode
     * 
     * @param mode the mode
     * @return mode String - a readable String
     */
    public static String getActionMode(int mode) {
        switch(mode) {
            case RUN_MODE:
                return RUN_MODE_STR;
            case START_MODE:
                return START_MODE_STR;
            case STOP_MODE:
                return STOP_MODE_STR;
            default:
                return "??";
        }
    }

    /**
     * Returns a mode for a readable string
     * 
     * @param mode the readable string
     * @return mode the mode
     */
    public static int modeFor(String mode) {
        if (mode.equalsIgnoreCase(RUN_MODE_STR)) {
            return RUN_MODE;
        } else if (mode.equalsIgnoreCase(START_MODE_STR)) {
            return START_MODE;
        } else if (mode.equalsIgnoreCase(STOP_MODE_STR)) {
            return STOP_MODE;
        }
        return -1;
    }

    /**
     * Returns all modes as readable strings
     * 
     * @return an array of readable mode names
     */
    public static String[] getModes() {
        return new String[] { RUN_MODE_STR, START_MODE_STR, STOP_MODE_STR };
    }

    /**
     * Returns the name of the bound action rule
     * 
     * @return action name
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * Returns the category of the bound action
     * 
     * @return action category
     */
    public String getActionCategory() {
        return actionCategory;
    }

    /**
     * Returns the name of the icon for the binding mode
     * 
     * @param mode the binding mode
     * @return the icon name
     */
    public static String getModeIconName(int mode) {
        switch(mode) {
            case RUN_MODE:
                return "run.gif";
            case START_MODE:
                return "start.gif";
            case STOP_MODE:
                return "stop.gif";
            default:
                return null;
        }
    }

    /**
     * Returns an optional note
     * 
     * @return an optional binding note
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets the value of the optional note
     * 
     * @param note
     *        the note string value
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Returns the value of the named property
     * 
     * @param name
     * 		  the name of the property
     */
    public String getProperty(String name) {
        return props == null ? null : props.get(name);
    }

    /**
     * Assigns a value to a property
     * 
     */
    public void setProperty(String name, String value) {
        if (props == null) {
            props = new HashMap<String, String>();
        }
        props.put(name, value);
    }

    public Map<String, String> getProperties() {
        return props;
    }

    public void setProperties(Map<String, String> props) {
        this.props = props;
    }
}
