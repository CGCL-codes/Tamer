package org.nakedobjects.nos.client.cli;

/**
 * Representation of something within the core framework, such as an object, field or action. The user needs
 * to interact with each of these in a common way.
 */
public interface Agent {

    String debug();

    void list(View view, String[] layout);

    /**
     * Name of the agent.
     */
    String getName();

    /**
     * Finds a related agent by following the specified path
     */
    Agent findAgent(String path);

    /**
     * The prompt to display to the user to indicate what this agent is doing.
     */
    String getPrompt();

    /**
     * Determines if the user should be prompted for direct entry, rather than a command. Used for entering
     * values.
     */
    boolean isValueEntry();

    boolean isReplaceable();
}
