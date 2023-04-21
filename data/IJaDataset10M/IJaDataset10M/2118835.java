package spellcast.game.event;

import java.io.*;
import spellcast.beings.*;

/**
 * The DisconnectionEvent is in response to a <code>DisconnectionRequestEvent</code>.
 *
 * @author Barrie Treloar
 */
public class DisconnectionEvent extends GameEvent implements Serializable {

    private String targetName;

    /**
     * Create a DisconnectionEvent where SPELLCAST is the originator,
     * the target is the wizard to be disconnected and everyone is the recipient.
     */
    public DisconnectionEvent(Wizard target) {
        super(WizardID.SPELLCAST, target.getID(), WizardID.EVERYONE);
        this.targetName = target.getName();
    }

    /**
     * Return the message formatted for the target of the event.
     *
     * @return the mesage for the intended Wizard.  Never null.
     */
    public String getMessageForTarget() {
        return "You have successfully disconnected from the Spellcast Server.";
    }

    /**
     * Return the message formatted for the everyone who was not the originator 
     * and also not the target.
     *
     * @return the mesage for the intended Wizard.  Never null.
     */
    public String getMessageForEveryoneElse() {
        return targetName + " has disconnected from the server.";
    }
}
