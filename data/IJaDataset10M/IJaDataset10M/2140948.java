package vidis.ui.model.graph.layouts.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.vecmath.Point3d;
import org.apache.log4j.Logger;
import vidis.data.sim.SimNode;
import vidis.ui.model.graph.layouts.AGraphLayout;
import vidis.ui.model.graph.layouts.IGraphLayout;

/**
 * very fast and easy to calculate layout
 * 
 * it places the nodes somewhere :-)
 * 
 * @author Dominik
 *
 */
public class GraphRandomLayout extends AGraphLayout {

    private final double helper = 10;

    private final double xMin_min = -helper;

    private final double xMin_max = -helper;

    private final double yMin_min = 0;

    private final double yMin_max = 0;

    private final double zMin_min = -helper;

    private final double zMin_max = -helper;

    private final double xMax_min = helper;

    private final double xMax_max = helper;

    private final double yMax_min = 0;

    private final double yMax_max = 0;

    private final double zMax_min = helper;

    private final double zMax_max = helper;

    private double xMax, xMin, yMax, yMin, zMax, zMin;

    private static Logger logger = Logger.getLogger(GraphRandomLayout.class);

    private GraphRandomLayout() {
        setNodeDensity(0.4);
    }

    private static IGraphLayout instance = null;

    public static IGraphLayout getInstance() {
        if (instance == null) instance = new GraphRandomLayout();
        return instance;
    }

    public void apply(Collection<SimNode> nodes) throws Exception {
        logger.debug("generate positions in {[" + xMin + ".." + xMax + "],[" + yMin + ".." + yMax + "],[" + zMin + ".." + zMax + "]}");
        oldNodes.clear();
        List<SimNode> nodesList = new ArrayList<SimNode>(nodes);
        for (int i = 0; i < nodesList.size(); i++) {
            positionNode(nodesList.get(i));
        }
        GraphCenterLayout.getInstance().apply(nodesList);
    }

    public void relayout(Collection<SimNode> nodes) throws Exception {
        List<SimNode> nodesList = new ArrayList<SimNode>(nodes);
        for (int i = 0; i < nodesList.size(); i++) {
            SimNode s = nodesList.get(i);
            if (oldNodes.contains(s)) {
            } else {
                positionNode(s);
            }
        }
    }

    private void positionNode(SimNode node) {
        Point3d random = new Point3d();
        random.x = Math.random() * xMax + xMin;
        random.y = Math.random() * yMax + yMin;
        random.z = Math.random() * zMax + zMin;
        setPosition(node, random);
        oldNodes.add(node);
    }

    public void setNodeDensity(double density) {
        density = Math.max(0.0, density);
        density = Math.min(1.0, density);
        xMin = density * (xMin_max - xMin_min) + xMin_min;
        xMax = density * (xMax_max - xMax_min) + xMax_min;
        yMin = density * (yMin_max - yMin_min) + yMin_min;
        yMax = density * (yMax_max - yMax_min) + yMax_min;
        zMin = density * (zMin_max - zMin_min) + zMin_min;
        zMax = density * (zMax_max - zMax_min) + zMax_min;
    }
}
