package com.agentfactory.logic.lang;

/**
 * Thrown whenever a logic statement is badly formed. These exceptions do not 
 * always result in the agent stopping. 
 *
 * @author Robert Ross, Universitat Bremen
 */
public class MalformedLogicException extends Exception {

    public MalformedLogicException(String inString) {
        super("Logic Problem: " + inString);
    }
}
