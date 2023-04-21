package alice.c4jadex.bridge.actions;

import alice.c4jadex.bridge.*;
import alice.cartago.*;
import jadex.runtime.*;

/**
 *    quit_workspace
 * @author Michele Piunti
 */
public class quit_workspace extends cartago_action {

    /** Creates a new instance of quit_workspace */
    public void body() {
        try {
            AgentContext actx = getContext(getAgentName());
            actx.getContext().quitWorkspace();
        } catch (Exception ex) {
            System.err.println("quit_workspace failed");
            ex.printStackTrace();
        }
    }
}
