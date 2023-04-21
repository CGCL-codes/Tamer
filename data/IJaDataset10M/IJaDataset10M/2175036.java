package de.tudresden.inf.rn.mobilis.server.xhunt.state;

import org.jivesoftware.smack.packet.IQ;
import de.tudresden.inf.rn.mobilis.server.xhunt.Game;
import de.tudresden.inf.rn.mobilis.server.xhunt.XHuntController;

/**Abstract class for the game states.
 * @author elmar, Daniel Esser
 *
 */
public abstract class GameState {

    protected XHuntController control;

    protected Game game;

    /**Handles the IQ packet in the corresponding game state. Has to be overwritten by all subclasses.
	 * @param iq Packet.
	 */
    public abstract void processPacket(IQ iq);
}
