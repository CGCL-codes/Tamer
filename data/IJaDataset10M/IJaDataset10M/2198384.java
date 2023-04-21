package org.qtitools.assessr.providers;

import java.util.Iterator;
import java.util.List;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.SelectQuery;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.qtitools.assessr.cayenne.Assessment;
import org.qtitools.assessr.cayenne.Tutor;

public class AssessmentSharedTutorProvider extends SortableDataProvider implements IDataProvider {

    private static final long serialVersionUID = 1L;

    Assessment assessment;

    SelectQuery sq;

    public AssessmentSharedTutorProvider(Assessment assess) {
        assessment = assess;
        Expression qualifier = ExpressionFactory.matchExp("groups.assessments", assessment);
        sq = new SelectQuery(Tutor.class, qualifier);
        setSort("name", true);
    }

    public Iterator iterator(int offset, int count) {
        DataContext ctx = DataContext.getThreadDataContext();
        sq.setPageSize(count);
        SortParam sp = getSort();
        sq.clearOrderings();
        sq.addOrdering(sp.getProperty(), sp.isAscending());
        List res = ctx.performQuery(sq);
        return res.listIterator(offset);
    }

    public IModel model(Object arg0) {
        return new CompoundPropertyModel((Tutor) arg0);
    }

    public int size() {
        DataContext ctx = DataContext.getThreadDataContext();
        sq.setPageSize(100);
        return ctx.performQuery(sq).size();
    }
}
