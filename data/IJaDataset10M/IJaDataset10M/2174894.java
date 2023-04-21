package uk.co.marcoratto.easter.test;

import java.util.Calendar;
import uk.co.marcoratto.easter.*;

public class TestEaster {

    public static void main(String[] args) throws Exception {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int j = -5, i = currentYear - 5; j < 5; i++, j++) {
            System.out.println(Easter.find(i));
        }
    }
}
