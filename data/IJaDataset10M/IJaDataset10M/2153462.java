package net.nexttext.behaviour;

import net.nexttext.*;
import net.nexttext.property.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class EB extends Behaviour {

    static final String REVISION = "$CVSHeader$";

    static Map defaultTextObjectProperties;

    static {
        defaultTextObjectProperties = new HashMap();
        Property dVelocity = new Vector3Property(new Vector3(0, 0, 0));
        defaultTextObjectProperties.put("Velocity", dVelocity);
    }

    /** This behaviour requires Velocity. */
    protected Map getDefaultTextObjectProperties() {
        return defaultTextObjectProperties;
    }

    /** This behaviour calculates, so it's ORDER_CALCULATOR. */
    public int getPreferredOrder() {
        return Behaviour.ORDER_CALCULATOR;
    }

    /** Constructors always require the book. */
    public EB(Book book) {
        init(book);
    }

    /** Do its thing to the text objects. */
    public void behave(TextObject to) {
    }
}
