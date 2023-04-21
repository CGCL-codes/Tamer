package org.yawlfoundation.yawl.editor.reductionrules;

import org.yawlfoundation.yawl.elements.YExternalNetElement;
import org.yawlfoundation.yawl.elements.YFlow;
import org.yawlfoundation.yawl.elements.YNet;
import org.yawlfoundation.yawl.elements.YTask;
import java.util.Iterator;
import java.util.Set;

/**
 * Reduction rule for YAWL net with OR-joins: FOR rule
 */
public class FORrule extends YAWLReductionRule {

    /**
    * Innermost method for a reduction rule.
    * Implementation of the abstract method from superclass.
    * @net YNet to perform reduction
    * @element an  for consideration.
    * returns a reduced YNet or null if a given net cannot be reduced.
    */
    public YNet reduceElement(YNet net, YExternalNetElement nextElement) {
        YNet reducedNet = net;
        if (nextElement instanceof YTask) {
            YTask task = (YTask) nextElement;
            if (task.getJoinType() == YTask._OR && task.getRemoveSet().isEmpty() && task.getCancelledBySet().isEmpty()) {
                Set preSet = task.getPresetElements();
                Set preSetTasks = YNet.getPreset(preSet);
                if (preSetTasks.size() == 1) {
                    YTask t = (YTask) preSetTasks.toArray()[0];
                    Set postSetOft = t.getPostsetElements();
                    if (t.getRemoveSet().isEmpty() && t.getCancelledBySet().isEmpty() && postSetOft.equals(preSet) && checkEqualConditions(preSet)) {
                        Set postFlowElements = task.getPostsetElements();
                        Iterator postFlowIter = postFlowElements.iterator();
                        while (postFlowIter.hasNext()) {
                            YExternalNetElement next = (YExternalNetElement) postFlowIter.next();
                            YFlow postflow = new YFlow(t, next);
                            t.addPostset(postflow);
                        }
                        t.setSplitType(task.getSplitType());
                        reducedNet.removeNetElement(task);
                        t.addToYawlMappings(task);
                        t.addToYawlMappings(task.getYawlMappings());
                        Iterator conditionsIter = preSet.iterator();
                        while (conditionsIter.hasNext()) {
                            YExternalNetElement next = (YExternalNetElement) conditionsIter.next();
                            reducedNet.removeNetElement(next);
                            t.addToYawlMappings(next);
                            t.addToYawlMappings(next.getYawlMappings());
                        }
                        return reducedNet;
                    }
                }
            }
        }
        return null;
    }
}
