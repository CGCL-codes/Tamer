package Main;

import Business.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Iteration 3. User story 2.
 * Convenor creates a new assignment and configures the status of the assignment
 * (open for submission, closed for submission, open for marking, open for marking
 * and submission) as well as defines the due date.
 *
 * @author boogie~
 */
public class Iter3_UserStory2 {

    public static void main(String args[]) {
        System.out.println(args.length);
        if (args.length != 8) {
            System.err.print("Invalid parameters.");
            return;
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        java.util.Date dt1 = null;
        java.util.Date dt2 = null;
        java.util.Date dt3 = null;
        try {
            dt1 = df.parse(args[2]);
            dt2 = df.parse(args[3]);
            dt3 = df.parse(args[6]);
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        Assignment ass1 = new Assignment(-1, args[0], args[1], dt1, dt2, args[4], args[5], dt3, Integer.parseInt(args[7]));
        Assignment.addAssignment(ass1);
    }
}
