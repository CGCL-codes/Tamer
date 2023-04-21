package org.jdesktop.mtgame;

/**
 * This condition is used for tracking AWT events
 * 
 * @author Doug Twilleager
 */
public class AwtEventCondition extends ProcessorArmingCondition {

    /**
     * The default constructor
     */
    public AwtEventCondition(ProcessorComponent pc) {
        super(pc);
    }
}
