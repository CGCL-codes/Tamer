package gov.esporing.ost;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;

public class Test2 extends JFrame {

    public boolean run(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("d.M.yyyy");
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
            System.out.println(testDate);
        } catch (ParseException e) {
            return false;
        }
        if (!sdf.format(testDate).equals(date)) {
            return false;
        }
        return true;
    }

    public static void main(String args[]) {
        Test2 a = new Test2();
        System.out.println(a.run("01.02.2001"));
    }
}
