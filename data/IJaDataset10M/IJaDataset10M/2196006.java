package org.asteriskjava.fastagi.command;

/**
 * Enable/Disable TDD transmission/reception on a channel.<p>
 * Returns 1 if successful, or 0 if channel is not TDD-capable.
 * 
 * @author srt
 * @version $Id: TddModeCommand.java 938 2007-12-31 03:23:38Z srt $
 */
public class TddModeCommand extends AbstractAgiCommand {

    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 3258411746401268532L;

    /**
     * The mode to set.
     */
    private String mode;

    /**
     * Creates a new TDDModeCommand.
     * 
     * @param mode the mode to set, this can be one of "on", "off", "mate" or
     *            "tdd".
     */
    public TddModeCommand(String mode) {
        super();
        this.mode = mode;
    }

    /**
     * Returns the mode to set.
     * 
     * @return the mode to set.
     */
    public String getMode() {
        return mode;
    }

    /**
     * Sets the mode to set.
     * 
     * @param mode the mode to set, this can be one of "on", "off", "mate" or
     *            "tdd".
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String buildCommand() {
        return "TDD MODE " + escapeAndQuote(mode);
    }
}
