package de.lot.action;

import de.lot.model.StudentLearningOutcome;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

/**
 * String representing list of Student Learning Outcomes
 *
 * @author Stefan Kohler <kohler.stefan@gmail.com>
 */
@Name("studentLearningOutcomeList")
public class StudentLearningOutcomeList extends EntityQuery<StudentLearningOutcome> {

    @Override
    public String getEjbql() {
        return "select studentlearningoutcome from StudentLearningOutcome studentLearningOutcome";
    }
}
