package org.yawlfoundation.yawl.editor.reductionrules;

import org.yawlfoundation.yawl.elements.*;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Reduction rule for YAWL net: FATY rule
 */
public class FATYrule extends YAWLReductionRule {

    /**
    * Innermost method for a reduction rule.
    * Implementation of the abstract method from superclass.
    * @net YNet to perform reduction
    * @element an  for consideration.
    * returns a reduced YNet or null if a given net cannot be reduced.
    */
    public YNet reduceElement(YNet net, YExternalNetElement nextElement) {
        YNet reducedNet = net;
        boolean isReducible = false;
        if (nextElement instanceof YTask) {
            YTask task = (YTask) nextElement;
            Set postSet = task.getPostsetElements();
            Set preSet = task.getPresetElements();
            if (preSet.size() > 1 && postSet.size() > 1 && task.getSplitType() == YTask._XOR && task.getJoinType() == YTask._XOR) {
                Map netElements = net.getNetElements();
                Iterator netElesIter = netElements.values().iterator();
                while (netElesIter.hasNext()) {
                    YExternalNetElement element = (YExternalNetElement) netElesIter.next();
                    if (element instanceof YTask) {
                        Set postSet2 = element.getPostsetElements();
                        Set preSet2 = element.getPresetElements();
                        YTask elementTask = (YTask) element;
                        if (postSet.equals(postSet2) && preSet.equals(preSet2) && task.getRemoveSet().isEmpty() && task.getSplitType() == elementTask.getSplitType() && task.getJoinType() == elementTask.getJoinType() && task.getCancelledBySet().isEmpty() && element.getCancelledBySet().isEmpty() && elementTask.getRemoveSet().isEmpty() && !element.equals(task)) {
                            isReducible = true;
                            reducedNet.removeNetElement(element);
                            task.addToYawlMappings(element);
                            task.addToYawlMappings(element.getYawlMappings());
                        }
                    }
                }
                if (isReducible) {
                    return reducedNet;
                }
            }
        }
        return null;
    }
}
