package Main;

import java.sql.*;
import Business.*;

/**
 *
 * @author borgar
 */
public class UserStory1 {

    /**
     * 
     * @param args
     * @throws java.sql.SQLException
     */
    public static void main(String args[]) throws SQLException {
        if (args.length != 1) {
            System.out.println("Parameter must be supplied! (SubmissionID)");
            return;
        }
        Submission.printMarkSheet(Integer.parseInt(args[0]));
    }
}
