package projectatlast.test;

import projectatlast.data.Registry;
import projectatlast.graph.*;
import projectatlast.group.Group;
import projectatlast.group.GroupField;
import projectatlast.query.*;
import projectatlast.student.AuthController;
import projectatlast.student.Student;
import java.io.IOException;
import java.util.Date;
import javax.servlet.http.*;

public class SampleGraphsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("bla");
        Date now = new Date();
        Date tomorrow = new Date(now.getTime() + 86400000);
        Student student = AuthController.getCurrentStudent();
        Query query = new Query();
        Date from = new Date();
        from.setMonth(9);
        Date to = new Date();
        to.setMonth(1);
        to.setYear(8000);
        DateFilter d = new DateFilter(from, to);
        query.addOption(new StudentFilter(student));
        query.addOption(d);
        query.addGroup(new Group(GroupField.COURSE));
        XYGraph graph1 = new XYGraph("newqueryXY", student, query, GraphType.COLUMN, ParseField.DURATION, Parser.SUM);
        query = new Query();
        query.addOption(new StudentFilter(student));
        query.addGroup(new Group(GroupField.COURSE));
        query.addGroup(new Group(GroupField.TYPE));
        StackedGraph graph2 = new StackedGraph("Stacked", student, query, GraphType.COLUMN, ParseField.DURATION, Parser.SUM);
        query = new Query();
        query.addOption(new StudentFilter(student));
        query.addGroup(new Group(GroupField.HOUR_OF_DAY));
        query.addGroup(new Group(GroupField.COURSE));
        StackedGraph graph3 = new StackedGraph("Stacked Inversed", student, query, GraphType.COLUMN, ParseField.DURATION, Parser.SUM);
        query = new Query();
        query.addOption(new StudentFilter(student));
        query.addGroup(new Group(GroupField.ACTIVITY));
        ScatterGraph graph4 = new ScatterGraph("SCATTER", student, query, GraphType.SCATTER, ParseField.MOOD_COMPREHENSION, Parser.SUM, ParseField.MOOD_INTEREST, Parser.SUM);
        Registry.graphFinder().put(graph1, graph2, graph3, graph4);
    }
}
