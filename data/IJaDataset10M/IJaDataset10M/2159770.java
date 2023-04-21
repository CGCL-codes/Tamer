package calibration;

import nxt.NXT;
import util.Console;
import java.io.IOException;
import java.sql.SQLException;

public class Translation {

    /**
     * @param args
     */
    public static void main(String[] args) {
        DBHelper db = null;
        try {
            db = new DBHelper();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Database connected");
        int distance;
        int seqn;
        double actual;
        while (true) {
            NXT.keepAlive();
            System.out.print("Input DISTANCE and SEQN: ");
            distance = Console.readInt();
            seqn = Console.readInt();
            try {
                Console.cutOffLine();
            } catch (IOException e) {
                System.out.println("Warning: too many inputs, INPUT AGAIN!");
                continue;
            }
            if (seqn < 0) {
                break;
            }
            System.out.println("Moving " + distance);
            NXT.travel(distance);
            System.out.print("Input ACTUAL distance: ");
            actual = Console.readDouble();
            try {
                Console.cutOffLine();
            } catch (IOException e) {
                System.out.println("Warning: wrong input, DO NOTHING!");
                continue;
            }
            try {
                db.writeTranslation(distance, seqn, actual);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        NXT.close();
        System.out.println("NXT disconnected");
        try {
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Dababase disconnected");
    }
}
