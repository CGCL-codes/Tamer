package common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Donghui
 */
public class TodayDate {

    private static TodayDate local;

    private Date today;

    private String tep;

    public static TodayDate getInstance() {
        if (local == null) {
            local = new TodayDate();
        }
        return local;
    }

    public String run() {
        today = new Date();
        SimpleDateFormat kiper = new SimpleDateFormat("yyyy-MM-dd");
        return tep = kiper.format(today);
    }
}
