package de.haumacher.timecollect.report;

import java.sql.Connection;
import java.sql.SQLException;
import de.haumacher.timecollect.common.db.DBException;
import de.haumacher.timecollect.common.report.Query;
import de.haumacher.timecollect.common.report.ReportBuilder;
import de.haumacher.timecollect.common.report.ReportError;

public class ReportGenerator {

    private final Context context;

    private final ReportBuilder builder;

    public ReportGenerator(Context context, ReportBuilder builder) {
        this.context = context;
        this.builder = builder;
    }

    public void run(String queryDefinition) {
        try {
            process(context, queryDefinition, builder);
        } catch (ReportError ex) {
            context.handleError("Report failed", "Invalid query: " + ex.getMessage(), null);
        } catch (SQLException ex) {
            context.handleError("Query failed", "Database reported failure: " + ex.getMessage(), null);
        } catch (DBException ex) {
            context.handleError(ex);
        }
    }

    public static void process(Context context, String queryDefinition, ReportBuilder builder) throws SQLException, DBException, ReportError {
        Query query = new Query(queryDefinition);
        Connection connection = context.borrowReadConnection();
        try {
            query.process(connection, builder);
        } finally {
            context.releaseConnection(connection);
        }
    }
}
