package projects.defaultProject.models.mobilityModels;

import sinalgo.configuration.Configuration;
import sinalgo.models.MobilityModel;
import sinalgo.nodes.Node;
import sinalgo.nodes.Position;
import sinalgo.runtime.Main;

/**
 * Implements a mobility model under which nodes are not moving at all. 
 */
public class NoMobility extends MobilityModel {

    private static boolean firstTime = true;

    public Position getNextPos(Node n) {
        return n.getPosition();
    }

    /**
	 * Constructor that prints a warning if interference is turned on 
	 */
    public NoMobility() {
        super(false);
        if (firstTime && Configuration.mobility && Configuration.showOptimizationHints) {
            Main.warning("At least some nodes use the '" + this.getClass().getSimpleName() + "' mobility model. " + "If you do not consider mobility at all in your project, you can " + "considerably improve performance by turning off mobility in the " + "XML configuration file.");
            firstTime = false;
        }
    }
}
