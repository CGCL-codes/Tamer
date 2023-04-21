package org.eledge.components.questions;

import static org.eledge.Eledge.dbcommit;
import static org.eledge.Eledge.dataContext;
import java.util.List;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.eledge.domain.Assignment;
import org.eledge.domain.AssignmentQuestion;
import org.eledge.domain.AssignmentQuestionAnswer;
import org.eledge.pages.EditAssignmentQuestions;

/**
 * @author robertz
 * 
 */
public class EditQuestionHelper {

    public static void delete(AssignmentQuestion question, IRequestCycle cycle) {
        Assignment a = question.getAssignment();
        dataContext().deleteObject(question);
        saveAndReactivate(a, cycle);
    }

    public static void doEditQPageRedirect(Assignment a, IRequestCycle cycle) {
        EditAssignmentQuestions page = (EditAssignmentQuestions) cycle.getPage("EditAssignmentQuestions");
        page.setAssignment(a);
        throw new PageRedirectException(page);
    }

    public static void saveAndReactivate(Assignment a, IRequestCycle cycle) {
        dbcommit(cycle);
        doEditQPageRedirect(a, cycle);
    }

    public static void reorderAnswers(List<AssignmentQuestionAnswer> answers) {
        int i = 0;
        for (AssignmentQuestionAnswer ans : answers) {
            ans.setOrder(new Integer(i++));
        }
    }

    public static void checkPoints(AssignmentQuestion question) {
        int total = 0;
        for (AssignmentQuestionAnswer ans : question.getAnswers()) {
            if (ans.getPointValue().intValue() > 0) {
                total += ans.getPointValue().intValue();
            }
        }
        if (total == 0) {
            total = 1;
        }
        if (question.getPointValue().intValue() != total) {
            question.setPointValue(new Integer(total));
        }
    }
}
