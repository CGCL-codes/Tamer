package packets;

import elements.Consts.Priorities;
import network.ClientController;

/**
 *
 * @author Joel Garboden
 */
public class NextTurnCmd extends Command {

    private boolean turnToggle;

    /**
   *
   * @param turnToggle
   */
    public NextTurnCmd(boolean turnToggle) {
        this.turnToggle = turnToggle;
        this.priority = Priorities.TURN_UPDATE;
    }

    @Override
    public boolean execCommand(ClientController client) {
        if (client.isGameStarted()) client.toggleTurn(turnToggle); else client.firstTurn();
        return true;
    }

    @Override
    public String toString() {
        return "turnToggle: " + turnToggle;
    }
}
