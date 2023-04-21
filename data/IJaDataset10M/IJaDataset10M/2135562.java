package org.qtitools.assessr.cayenne;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
import org.qtitools.assessr.cayenne.auto._Group;

public class Group extends _Group {

    private static final long serialVersionUID = 1L;

    public static Group getMatchingGroup(Assessment assessment, Tutor tutor) {
        DataContext context = DataContext.getThreadDataContext();
        final Expression qualifier = Expression.fromString("assessments = $assessment and tutors = $tutor");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("assessment", assessment);
        params.put("tutor", tutor);
        SelectQuery select = new SelectQuery(Group.class, qualifier.expWithParameters(params));
        List grps = context.performQuery(select);
        if (grps.size() == 0) return null;
        if (!(grps.size() == 1)) throw new RuntimeException("Duplicate groups!");
        return (Group) grps.get(0);
    }
}
