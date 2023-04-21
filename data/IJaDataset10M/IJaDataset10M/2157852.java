package playground.scnadine.MapMatching.mapMatching;

import java.util.Vector;
import org.postgis.Point;

/**
For internal use by TreeNode and its subclasses. It holds the parameters
and the result, as far as it is known, of TreeNode::nClosest().
*/
public class QueryNStruct {

    public Point pt;

    public double max;

    public int n;

    public int s;

    public Vector closest;
}
