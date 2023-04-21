package jade.tools.sniffer;

import java.awt.event.ActionEvent;

/**
   Invokes the agent Sniffer to delete itself, closing the Gui and unregistering.
 * @see jade.tools.sniffer.FixedAction
 */
public class ExitAction extends FixedAction {

    private Sniffer mySniffer;

    public ExitAction(ActionProcessor actpro, Sniffer mySniffer) {
        super("ExitActionIcon", "Exit", actpro);
        this.mySniffer = mySniffer;
    }

    public void doAction() {
        mySniffer.doDelete();
    }
}
