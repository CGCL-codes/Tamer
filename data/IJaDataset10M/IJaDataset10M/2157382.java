package net.gombology.synOscP5.synth;

import net.gombology.synOscP5.SynMessage;

/**
 * Sets parameter 1 of the voice
 * or
 * sets parameter 1 of the synth.
 * 
 * <p>
 * Extract from the SynOSCopy namespace specification, by Fabian Ehrentraud:
 * </p><p>
 * <cite>
 * /SYN/IDx/Vx/P1,Fi<br/>
 * Sets parameter 1 of the voice. This could e.g. be the note aftertouch. When the first argument 
 * is 'T', the second argument is relative.<br/>
 * Parameter 2 is method /SYN/IDx/Vx/P2, then comes P3 etc.
 * </cite>
 * </p>

 * @author Alvaro Ortiz (http://alvaro.kulturserver-berlin.de)
 */
public class Param extends SynthMessage {

    private int id;

    private Double value = 0.0;

    /**
     * Constructs a Param object with absolute value. 
     * 
     * @param parent the message this method belongs to
     * @param id parameter id
     * @param value parameter value
     * @throws IllegalArgumentException
     */
    public Param(SynMessage parent, int id, double value) {
        this.setParent(parent);
        if (id < 0 || id > getMax()) throw new IllegalArgumentException();
        this.id = id;
        this.value = value;
        makeMessage();
    }

    /**
     * Sets the message typetag and address pattern.
     */
    @Override
    protected void makeMessage() {
        clear();
        Object[] args = { this.relative, percent2value(this.value) };
        setArguments(args);
        setAddress(makePattern());
    }

    @Override
    public String getPattern() {
        return String.format("/P%s", this.id);
    }
}
