package ajpf.product;

import gov.nasa.jpf.JPF;
import java.util.logging.Logger;

/**
 * A model edge which has a probability.
 * @author louiseadennis
 *
 */
public class ProbabilisticModelEdge extends ModelEdge {

    protected static Logger log = JPF.getLogger("ajpf.product.ProbabilisticModelEdge");

    double probability = 0;

    public ProbabilisticModelEdge(ModelState s1, ModelState s2) {
        super(s1, s2);
    }

    public ProbabilisticModelEdge(ModelState s1, ModelState s2, double prob) {
        super(s1, s2);
        probability = prob;
    }

    public void setProb(double prob) {
        probability = prob;
        log.fine("setting prob");
    }

    public void annotate_edge(double prob) {
        setProb(prob);
    }

    public double getProbability() {
        return probability;
    }
}
