package com.trapezium.attractor;

import com.trapezium.vrml.node.Node;
import com.trapezium.space.SpaceStructure;
import com.trapezium.chisel.*;

/** Attractor polygon reduction chisel which works with two level LOD
 *  nodes.  The low resolution IFS is used as an attractor to reduce the
 *  high resolution IFS, then the result is used to replace the high
 *  resolution IFS.
 *
 *  @author          Johannes N. Johannsen
 *  @version         1.0, 8 Oct 1998
 *
 *  @since           1.0
 */
public class LODAttractor extends AttractorPolygonReduction {

    Node currentLOD;

    /** Class constructor */
    public LODAttractor() {
        super("Reduce high res LOD");
    }

    /** get attractor from child node 1 of LOD */
    public SpaceStructure generateAttractor() {
        if (currentLOD != null) {
            Node c1 = currentLOD.getChildNode(1);
            if (c1 != null) {
                Node coord = c1.getNodeValue("coord");
                if (coord != null) {
                    return (spaceStructureLoader.getSpaceStructure(coord));
                }
            }
        }
        return (null);
    }

    /** optimization is OK if the node is in a two level LOD */
    public boolean optimizationOK(SpaceStructure nSpace, Node n) {
        if (super.optimizationOK(nSpace, n)) {
            currentLOD = n.getParent("LOD");
            if (currentLOD != null) {
                return (currentLOD.getChildNode(0) == n);
            }
        }
        return (false);
    }
}
