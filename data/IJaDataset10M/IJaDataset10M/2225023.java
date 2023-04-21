package ch.gmtech.lab.datasource.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ch.gmtech.lab.ApplicationLogger;
import ch.gmtech.lab.ApplicationLoggerInterface;
import ch.gmtech.lab.domain.Course;
import ch.gmtech.lab.domain.OrderedCollection;
import ch.gmtech.lab.domain.OrderedCollectionImpl;
import ch.gmtech.lab.gui.UserMessage;

public class ResultSetToCourses {

    private final ApplicationLoggerInterface _logger;

    public ResultSetToCourses() {
        _logger = ApplicationLogger.getOnDemandInstance();
    }

    public OrderedCollection convert(ResultSet aResult) {
        if (aResult == null) return null;
        List<Course> result = new ArrayList<Course>();
        while (nextOf(aResult)) {
            result.add(new Course(NameColumn.valueFrom(aResult), CreditsColumn.valueFrom(aResult)));
        }
        return new OrderedCollectionImpl(result);
    }

    private boolean nextOf(ResultSet result) {
        try {
            return result.next();
        } catch (SQLException e) {
            logger().error(new UserMessage("Errore leggendo dal database"), e);
            return false;
        }
    }

    private ApplicationLoggerInterface logger() {
        return _logger;
    }
}
