package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import java.util.Iterator;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

/**
 * Binary arc that constraints a numeric variable not to be a member of a set
 * 
 * @author Nick Coleman
 * @version $Revision $
 */
public class BinarySetNotMemberOfSetArc extends BinarySetArc {

    private SetNode<Number> set;

    public BinarySetNotMemberOfSetArc(SetNode<Number> set, NumNode expr) {
        super(set, expr);
        this.set = set;
    }

    public void propagate() throws PropagationFailureException {
        NumNode z = (NumNode) target;
        if (useDeltas) {
            Iterator<Number> removedIter = set.getRequiredDeltaSet().iterator();
            while (removedIter.hasNext()) z.removeValue((Number) removedIter.next());
        } else {
            NumberIterator targetIter = z.values();
            while (targetIter.hasNext()) {
                Number val = (Number) targetIter.next();
                if (set.isRequired(val)) z.removeValue(val);
            }
        }
    }

    public void propagate(Node src) throws PropagationFailureException {
        propagate();
    }
}
