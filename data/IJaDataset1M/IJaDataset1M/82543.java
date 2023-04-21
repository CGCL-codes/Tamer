package org.mitre.interactive.refimpl.gui.common;

import java.util.Comparator;
import org.apache.log4j.Logger;
import org.mitre.interactive.x02.PriorityType;
import org.mitre.interactive.x02.QuestionnaireDocument.Questionnaire;
import org.mitre.interactive.x02.TestActionRefDocument.TestActionRef;

/**
 *
 * @author mcasipe
 */
public class PriorityComparator implements Comparator {

    private static Logger logger = Logger.getLogger(PriorityComparator.class.getPackage().getName());

    public int compare(Object first, Object second) {
        Integer priorityOfFirst = getPriority(first);
        Integer priorityOfSecond = getPriority(second);
        int result = priorityOfFirst.compareTo(priorityOfSecond);
        if (result == 0) result = -1;
        return result;
    }

    private int getPriority(Object o) {
        PriorityType.Enum priority = PriorityType.LOW;
        if (o instanceof Questionnaire) {
            Questionnaire qn = (Questionnaire) o;
            priority = qn.getPriority();
        } else if (o instanceof TestActionRef) {
            TestActionRef tr = (TestActionRef) o;
            priority = tr.getPriority();
        } else {
            return -1;
        }
        if (priority == PriorityType.HIGH) return 0; else if (priority == PriorityType.MEDIUM) return 1;
        return 2;
    }
}
