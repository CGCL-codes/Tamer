package polychess.x.nspace;

import polychess.x.NSpacePath;
import polychess.x.NSpacePathComponent;

/**
 *
 * @author jlf
 */
public class PathUtils {

    /** Creates a new instance of PathUtils */
    public PathUtils() {
    }

    public static NSpacePath createMirroredPath(NSpacePath basePath) {
        NSpacePath path = new NSpacePath();
        NSpacePath oppositePath = new NSpacePath();
        NSpacePathComponent currentComp = null, previousComp = null, lastAddedOppositeComp = null;
        for (int i = 0; i < basePath.getSize(); i++) {
            previousComp = currentComp;
            currentComp = basePath.getComponentAt(i);
            if (previousComp != null) {
                int[] vector = getPreviousVector(currentComp, previousComp);
                NSpacePathComponent newComp = new NSpacePathComponent(lastAddedOppositeComp);
                newComp.addVectorInverse(vector);
                oppositePath.addComponent(newComp);
                lastAddedOppositeComp = newComp;
            } else {
                oppositePath.addComponent(currentComp);
                lastAddedOppositeComp = currentComp;
            }
        }
        oppositePath.reversePath();
        path.addPath(basePath);
        path.addPath(oppositePath);
        return path;
    }

    public static int[] getPreviousVector(NSpacePathComponent currentComponent, NSpacePathComponent previousComponent) {
        int[] rval = new int[currentComponent.getDimension()];
        for (int i = 0; i < rval.length; i++) {
            rval[i] = currentComponent.getMagnitude(i) - previousComponent.getMagnitude(i);
        }
        return rval;
    }
}
