package alice.c4jadex.bridge.actions;

import alice.cartago.CartagoNode;
import alice.cartago.CartagoService;

/**
 * <p>Internal action: <b><code>install_node</code></b>.</p>
 *
 * <p>Description: install a CARTAGO Node, eventually specifying a port. 
 * To be used without any predefined environment.</p>
 *
 * <p>Parameters:
 * <ul>
 * <li>+ port (number [optional]): network port.</li>
 * </ul></p>
 */
public class install_node extends cartago_action {

    /** install_node */
    public void body() {
        int port = CartagoNode.DEFAULT_PORT;
        try {
            port = (Integer) getParameter("port").getValue();
            CartagoService.installNode(port);
        } catch (Exception ex) {
            System.err.println("[install_node] port has to be numeric (int). " + "Impossible install node on port: " + port);
            fail();
        }
    }
}
