package projects.sample5;

import java.awt.Color;
import projects.sample5.nodes.nodeImplementations.FNode;
import sinalgo.nodes.Node;
import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.tools.Tools;

/**
 * This class holds customized global state and methods for the framework. 
 * The only mandatory method to overwrite is 
 * <code>hasTerminated</code>
 * <br>
 * Optional methods to override are
 * <ul>
 * <li><code>customPaint</code></li>
 * <li><code>handleEmptyEventQueue</code></li>
 * <li><code>onExit</code></li>
 * <li><code>preRun</code></li>
 * <li><code>preRound</code></li>
 * <li><code>postRound</code></li>
 * <li><code>checkProjectRequirements</code></li>
 * </ul>
 * @see sinalgo.runtime.AbstractCustomGlobal for more details.
 * <br>
 * In addition, this class also provides the possibility to extend the framework with
 * custom methods that can be called either through the menu or via a button that is
 * added to the GUI. 
 */
public class CustomGlobal extends AbstractCustomGlobal {

    public boolean hasTerminated() {
        return false;
    }

    @GlobalMethod(menuText = "Clear Routing Tables")
    public void clearRoutingTalbes() {
        for (Node n : Tools.getNodeList()) {
            FNode fn = (FNode) n;
            fn.clearRoutingTable();
        }
    }

    @GlobalMethod(menuText = "Reset Node Color")
    public void resetNodeColor() {
        for (Node n : Tools.getNodeList()) {
            n.setColor(Color.BLACK);
        }
    }
}
