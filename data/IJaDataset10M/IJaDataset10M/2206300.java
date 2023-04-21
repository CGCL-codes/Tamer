package net.gombology.synOscP5.system;

import net.gombology.synOscP5.SynMessage;

/**
 * 
 * <p>
 * Extract from the SynOSCopy namespace specification, by Fabian Ehrentraud:
 * </p><p>
 * <cite>
 * /SYN/IDx/SENDCONTR,T<br/>
 * Turns on/off sending out OSC data to external devices from a synth. When set to 'F', a 
 * hardware synth can still be played by both incomming OSC messages and it's own keyboard but 
 * it doesn't send out the messages.
 * </cite>
 * </p>
 * 
 * @author alvaro
 */
public class SendControl extends SynMessage {

    private Boolean action;

    /**
     * Constructor.
     * 
     * @param parent
     */
    public SendControl(SynMessage parent, boolean action) {
        this.setParent(parent);
        this.action = action;
        makeMessage();
    }

    @Override
    public String getPattern() {
        return "/SENDCONTR";
    }

    @Override
    protected void makeMessage() {
        Object[] args = { this.action };
        setArguments(args);
        setAddress(makePattern());
    }
}
