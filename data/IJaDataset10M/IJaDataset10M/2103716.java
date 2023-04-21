package normal.engine.split;

import normal.engine.ShareableObject;
import normal.engine.triangulation.NTriangulation;

public interface NSignature extends ShareableObject {

    long getOrder();

    NTriangulation triangulate();
}
