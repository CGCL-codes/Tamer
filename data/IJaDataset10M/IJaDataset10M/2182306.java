package net.teqlo.search;

import java.io.Serializable;
import net.teqlo.search.generic.Graph;

public class SpEdge extends Graph.SimpleEdge implements Serializable {

    private static final long serialVersionUID = -1637825750731869111L;

    protected SpEdgeGenerator spEdgeGenerator = null;

    public SpEdge(Object from, Object to, int value, SpEdgeGenerator spEdgeGenerator) {
        super(from, to, value);
        this.spEdgeGenerator = spEdgeGenerator;
    }

    /**
	 * Returns the serial of the standard producer that generated this edge
	 * @return Short serial of standard producer activity
	 */
    public Short getSpSerial() {
        return this.spEdgeGenerator.getSpSerial();
    }

    public String toString() {
        return this.spEdgeGenerator.al.getActivityFqn();
    }
}
