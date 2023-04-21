package org.yawlfoundation.yawl.editor.reductionrules;

import org.yawlfoundation.yawl.editor.analyser.ResetWFNet;
import org.yawlfoundation.yawl.editor.analyser.RElement;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public abstract class ResetReductionRule {

    /**
    * Main method for calling reduction rule, apply a reduction rule recursively 
    * to a given net until it cannot be reduced further.
    * @net ResetWFNet to perform reduction
    * returns a reduced ResetWFNet or null if a given net cannot be reduced.
    */
    public ResetWFNet reduce(ResetWFNet net) {
        ResetWFNet reducedNet = reduceANet(net);
        if (reducedNet == null) {
            return null;
        } else {
            while (reducedNet != null) {
                ResetWFNet tempNet = reduceANet(reducedNet);
                if (tempNet == null) {
                    return reducedNet;
                } else {
                    reducedNet = tempNet;
                }
            }
        }
        return null;
    }

    ;

    /**
    * Inner method for a reduction rule.
    * Go through all elements in a ResetWFNet.
    * @net ResetWFNet to perform reduction
    * returns a reduced ResetWFNet or null if a given net cannot be reduced.
    */
    private ResetWFNet reduceANet(ResetWFNet net) {
        ResetWFNet reducedNet = null;
        Map netElements = net.getNetElements();
        Iterator netElesIter = netElements.values().iterator();
        while (netElesIter.hasNext()) {
            RElement nextElement = (RElement) netElesIter.next();
            reducedNet = reduceElement(net, nextElement);
            if (reducedNet != null) {
                return reducedNet;
            }
        }
        return reducedNet;
    }

    /**
    * Innermost method for a reduction rule.
    * @net ResetWFNet to perform reduction
    * @element one element for consideration.
    * returns a reduced ResetWFNet or null if a given net cannot be reduced.
    * Must be implemented by individual reduction rule
    */
    public abstract ResetWFNet reduceElement(ResetWFNet net, RElement element);
}
