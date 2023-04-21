package org.jcyclone.core.cfg;

import org.jcyclone.core.stage.IStage;
import org.jcyclone.core.stage.IStageManager;

/**
 * ConfigDataIF is used to pass configuration arguments to stages.
 * When a stage is initialized, a ConfigDataIF is passed to its
 * 'init()' method.
 *
 * @author Matt Welsh
 */
public interface IConfigData {

    /**
	 * The default value for a string key with no other specified value.
	 */
    public static final String SET = "set";

    /**
	 * Returns true if the given key is set in the configuration.
	 */
    boolean contains(String key);

    /**
	 * Get the string value corresponding to the given configuration key.
	 * This is the basic way for a stage to retrieve its initialization
	 * arguments. Returns null if not set.
	 */
    String getString(String key);

    /**
	 * Get the integer value corresponding to the given configuration key.
	 * This is the basic way for a stage to retrieve its initialization
	 * arguments. Returns -1 if not set or if the value is not an integer.
	 */
    int getInt(String key);

    /**
	 * Get the double value corresponding to the given configuration key.
	 * This is the basic way for a stage to retrieve its initialization
	 * arguments. Returns -1.0 if not set or if the value is not a double.
	 */
    double getDouble(String key);

    /**
	 * Get the boolean value corresponding to the given configuration key.
	 * This is the basic way for a stage to retrieve its initialization
	 * arguments. Returns false if not set.
	 */
    boolean getBoolean(String key);

    /**
	 * Get the object value corresponding to the given configuration key.
	 * This is the basic way for a stage to retrieve its initialization
	 * arguments. Returns null if not set.
	 */
    Object getObject(String key);

    /**
	 * Return a handle to the system manager.
	 * The system manager can (among other things) be used to access
	 * other stages in the system.
	 *
	 * @see org.jcyclone.core.stage.IStageManager
	 */
    IStageManager getManager();

    /**
	 * Return the IStage.
	 * The IStage can be used (among other things) to access the
	 * event queues for this stage.
	 *
	 * @see org.jcyclone.core.stage.IStage
	 */
    IStage getStage();
}
