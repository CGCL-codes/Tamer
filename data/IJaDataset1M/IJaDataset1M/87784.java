package edu.psu.citeseerx.exec.respmod.handler;

/**
 * Thrown when the response modifier does not conform to the response
 * modifier development policy. Currently, the policy is to subclass
 * the response modifier base class.
 * 
 * @author Levent Bolelli
 *
 */
public class InvalidResponseModifierException extends Exception {

    static final long serialVersionUID = 7871215085972144821L;

    final Class responseModifier;

    public InvalidResponseModifierException(Class c) {
        responseModifier = c;
    }

    public String getMessage() {
        return "Class " + responseModifier.toString() + " does not extend" + " base response modifier class\n";
    }
}
