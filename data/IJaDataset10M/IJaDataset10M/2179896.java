package org.asteriskjava.manager.event;

import org.asteriskjava.util.AstState;

/**
 * A MasqueradeEvent is triggered when Asterisk masquerades a channel.<p>
 * Available since Asterisk 1.6<p>
 * It is implemented in <code>main/channel.c</code>
 *
 * @author srt
 * @version $Id: MasqueradeEvent.java 1112 2008-08-16 14:03:18Z srt $
 * @since 1.0.0
 */
public class MasqueradeEvent extends ManagerEvent {

    static final long serialVersionUID = 1L;

    private String clone;

    private String cloneStateDesc;

    private String original;

    private String originalStateDesc;

    public MasqueradeEvent(Object source) {
        super(source);
    }

    /**
     * Returns the name of the clone channel.
     *
     * @return the name of the clone channel.
     */
    public String getClone() {
        return clone;
    }

    public void setClone(String clone) {
        this.clone = clone;
    }

    /**
     * Returns the state of the clone channel.
     *
     * @return the state of the clone channel.
     * @see org.asteriskjava.util.AstState
     */
    public Integer getCloneState() {
        return AstState.str2state(cloneStateDesc);
    }

    /**
     * Returns the state of the clone channel as a descriptive text.
     *
     * @return the state of the clone channel as a descriptive text.
     */
    public String getCloneStateDesc() {
        return cloneStateDesc;
    }

    public void setCloneState(String cloneState) {
        this.cloneStateDesc = cloneState;
    }

    /**
     * Returns the name of the original channel.
     *
     * @return the name of the original channel.
     */
    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    /**
     * Returns the state of the original channel.
     *
     * @return the state of the original channel.
     * @see org.asteriskjava.util.AstState
     */
    public Integer getOriginalState() {
        return AstState.str2state(originalStateDesc);
    }

    /**
     * Returns the state of the original channel as a descriptive text.
     *
     * @return the state of the original channel as a descriptive text.
     */
    public String getOriginalStateDesc() {
        return originalStateDesc;
    }

    public void setOriginalState(String originalState) {
        this.originalStateDesc = originalState;
    }
}
