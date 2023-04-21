package org.apache.derbyDemo.scores.proc;

import java.sql.*;
import org.apache.derbyDemo.scores.util.*;

/**
 * <p>
 * Procedures used by the Scores application.
 * </p>
 *
 */
public class Procedures {

    private static int _scoringCount = 0;

    /**
     * <p>
     * Score a test run and update TestTaking with the score.
     * </p>
     */
    public static void ScoreTestTaking(int takingID) throws SQLException {
        Connection conn = Functions.getDefaultConnection();
        Logger log = Logger.getLogger();
        boolean loggingEnabled = log.isLoggingEnabled();
        try {
            if (_scoringCount > 0) {
                log.enableLogging(false);
            }
            log.log("Trigger has just fired and started " + "the ScoreTestTaking procedure.\n");
            PreparedStatement ps = Utils.prepare(conn, "select \n" + " sum( weighQuestion( q.difficulty ) ),\n" + " sum( scoreAnswer( q.difficulty, q.numberOfChoices," + " q.correctChoice, qt.actualChoice ) )\n" + "from Question q, QuestionTaking qt\n" + "where q.questionID = qt.questionID\n" + "and qt.takingID = ?\n");
            ps.setInt(1, takingID);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int column = 1;
            double allCorrect = rs.getDouble(column++);
            double actual = rs.getDouble(column++);
            double score = Utils.finishScore(allCorrect, actual);
            Utils.close(rs);
            Utils.close(ps);
            int param = 1;
            ps = Utils.prepare(conn, "update TestTaking set score = ? where takingID = ?\n");
            ps.setDouble(param++, score);
            ps.setInt(param++, takingID);
            ps.executeUpdate();
            Utils.close(ps);
        } finally {
            log.enableLogging(loggingEnabled);
        }
        _scoringCount++;
    }
}
