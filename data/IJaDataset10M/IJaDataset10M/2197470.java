package com.bbn.vessel.core.runtime.message;

import com.bbn.vessel.core.arguments.Arguments;
import com.bbn.vessel.core.runtime.command.Command;
import com.bbn.vessel.core.runtime.event.Event;

/**
 * A base class for {@link Event}s and {@link Command}s. These are units of
 * information that flow to and from the game engine.
 */
public interface Message {

    /**
     * String tag for argument that describes what kind of message this is (e.g.
     * Event, Command, etc.)
     */
    String KIND = "kind";

    /**
     * String tag for argument that describes what type of message this is (e.g.
     * DoOpen, OnEnter, etc.)
     */
    String TYPE = "type";

    /** String tag for argument that provides a timestamp for this message. */
    String TIME = "time";

    /** @return the required kind (e.g. "event") */
    String getKind();

    /** @return the required type (e.g. "OnThrow") */
    String getType();

    /** @return the optional timestamp; same as getLong("time") */
    long getTime();

    /** @return all name=value pairs */
    Arguments getArguments();

    /**
     * @param name
     *            The name of the desired argument.
     * @return same as getArguments().get(name)
     */
    Object get(String name);

    /**
     * @param name
     *            The name of the desired string argument.
     * @return same as getArguments().getString(name)
     */
    String getString(String name);

    /**
     * @param name
     *            The name of the desired boolean argument.
     * @return same as getArguments().getBoolean(name)
     */
    boolean getBoolean(String name);

    /**
     * @param name
     *            The name of the desired integer argument.
     * @return same as getArguments().getInt(name)
     */
    int getInt(String name);

    /**
     * @param name
     *            The name of the desired float argument.
     * @return same as getArguments().getFloat(name)
     */
    float getFloat(String name);

    /**
     * @param name
     *            The name of the desired long argument.
     * @return same as getArguments().getLong(name)
     */
    long getLong(String name);

    /**
     * @param name
     *            The name of the desired double argument.
     * @return same as getArguments().getDouble(name)
     */
    double getDouble(String name);

    /**
     * @param o
     *            a message
     * @return true if every name=value argument in this message is in the
     *         specified message
     */
    boolean matches(Object o);

    /** @return a comma-separated string */
    String toCSV();

    /**
     * Set arguments based on an array of strings representing the columns of a
     * CSV string.
     * 
     * @param columns
     *            The values to be made into arguments.
     * 
     * @return the last column read
     */
    int fromCSV(String... columns);
}
