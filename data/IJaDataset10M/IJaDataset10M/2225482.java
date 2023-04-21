package Main;

import java.io.*;
import java.sql.*;
import java.util.*;
import Business.*;
import DAL.*;

/**
 *
 * @author borgar
 */
public class Main2 {

    public static void main(String args[]) throws SQLException {
        DALSubmission dalSub = new DALSubmission("//localhost:1527/UniLearn");
        List<Submission> arSubmissions;
        try {
            PrintWriter out = new PrintWriter(new FileWriter("C:\\eminate\\src\\submissions.txt"));
            if (args.length != 1) {
                arSubmissions = dalSub.getSubmissionsByUser(1);
            } else {
                arSubmissions = dalSub.getSubmissionsByUser(Integer.parseInt(args[0]));
            }
            for (int i = 0; i < arSubmissions.size(); i++) {
                Submission sub = (Submission) arSubmissions.get(i);
                out.println(sub.getID() + "-" + sub.getUserID() + "-" + sub.getComment() + "-" + sub.getFeedback());
                for (int j = 0; j < sub.getFiles().size(); j++) out.println("-" + sub.getFiles().get(j).getFileSource());
            }
            out.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
