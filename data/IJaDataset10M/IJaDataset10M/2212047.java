package jrackattack.event;

import jonkoshare.util.VersionInformation;
import jrackattack.midi.PatternParameter;

/**
 * Event to notify about pattern parameter changes.
 * This event maps a value to a parameter(-address)
 * within a specific pattern.
 *
 * @since 0
 * @author methke01
 */
@VersionInformation(lastChanged = "$LastChangedDate: 2009-07-25 05:59:33 -0400 (Sat, 25 Jul 2009) $", authors = { "Alexander Methke" }, revision = "$LastChangedRevision: 11 $", lastEditor = "$LastChangedBy: onkobu $", id = "$Id")
public class PatternParameterEvent extends ParameterEvent {

    /** Creates a new instance of PatternParameterEvent */
    public PatternParameterEvent(Object source, int num) {
        super(source, num);
    }

    public PatternParameterEvent(Object source, PatternParameter sp) {
        super(source, sp.getSoundNumber());
        PatternParameter = sp;
    }

    public PatternParameterEvent(Object source, int num, byte[] ah_al, int value) {
        super(source, num, ah_al, value);
    }

    public PatternParameter getPatternParameter() {
        return PatternParameter;
    }

    /**
	 * @see #getValue
	 */
    public int getChange() {
        return getValue();
    }

    /**
	 * Pattern's number.
	 *
	 * @see #getNumber
	 */
    public int getPatternNumber() {
        return getNumber();
    }

    private PatternParameter PatternParameter;
}
