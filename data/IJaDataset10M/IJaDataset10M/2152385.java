package edu.ucla.mbi.curator.actions.stats;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.ucla.mbi.curator.actions.database.Connect;
import edu.ucla.mbi.curator.actions.user.GetUserFromServlet;
import edu.ucla.mbi.curator.webutils.session.SessionManager;
import edu.ucla.mbi.curator.Constants;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: Apr 19, 2006
 * Time: 8:55:39 AM
 */
public class UpdatePersonalStatistics extends Action {

    private Log log = LogFactory.getLog(UpdatePersonalStatistics.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionManager sessionManager = SessionManager.getSessionManager(request);
        int curatorId = sessionManager.getCuratorId();
        if (curatorId == -1) new GetUserFromServlet().execute(mapping, form, request, response);
        curatorId = sessionManager.getCuratorId();
        if (curatorId == -1) return mapping.findForward("undefinedUser");
        new Connect().execute(mapping, form, request, response);
        Connection dbConnection = (Connection) request.getAttribute("dbConnection");
        Statement statement = dbConnection.createStatement();
        String qry = "select char_length(file), num_interactions from miffiles " + "where curator_id = " + curatorId + " " + "and curation_status != " + Constants.PRE_SUBMISSION_STATUS;
        ResultSet submittedFileSizes = null;
        try {
            submittedFileSizes = statement.executeQuery(qry);
        } catch (SQLException sql) {
            log.error("sql error: " + qry);
        }
        assert submittedFileSizes != null;
        int numSubmittedFiles = 0;
        int numInteractions = 0;
        long totalBytes = 0;
        while (submittedFileSizes.next()) {
            numSubmittedFiles++;
            totalBytes += Integer.valueOf(submittedFileSizes.getString("char_length"));
            numInteractions += Integer.valueOf(submittedFileSizes.getString("num_interactions"));
        }
        submittedFileSizes.close();
        statement.close();
        qry = "select statistic_id from curator_statistics where curator_id = " + curatorId;
        statement = dbConnection.createStatement();
        ResultSet stat_id = null;
        try {
            stat_id = statement.executeQuery(qry);
        } catch (SQLException sql) {
            log.error("sql error: " + qry);
        }
        assert stat_id != null;
        stat_id.next();
        int statisticId = Integer.valueOf(stat_id.getString("statistic_id"));
        stat_id.close();
        statement.close();
        qry = "update statistics " + "set num_submissions = " + numSubmittedFiles + ", " + "num_bytes = " + totalBytes + ", " + "num_interactions = " + numInteractions + " " + "where statistic_id = " + statisticId;
        statement = dbConnection.createStatement();
        try {
            statement.execute(qry);
        } catch (SQLException sql) {
            log.error("sql error: " + qry);
        }
        return mapping.findForward("success");
    }
}
