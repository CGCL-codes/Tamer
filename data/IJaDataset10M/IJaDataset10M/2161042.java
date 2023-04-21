package projects.sample2.models.distributionModels;

import projects.defaultProject.models.distributionModels.Random;
import sinalgo.configuration.Configuration;
import sinalgo.nodes.Position;
import sinalgo.tools.Tools;

/**
 * Places the Nodes randomly on the field using the Random-Distribution-Model but 
 * generating a new position if the simulation is using a map (useMap == true)
 * and there is a value greater than 0 in the map.
 */
public class LakeAvoidRandomDistribution extends Random {

    @Override
    public Position getNextPosition() {
        Position pos = super.getNextPosition();
        if (Configuration.useMap) {
            while (!Tools.getBackgroundMap().isWhite(pos)) {
                pos = super.getNextPosition();
            }
        }
        return pos;
    }
}
