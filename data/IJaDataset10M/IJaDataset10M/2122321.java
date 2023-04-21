package net.sylvek.taiyaki;

import net.sylvek.taiyaki.events.EventListener;
import java.util.Calendar;

/**
 * Clock event listener.
 * 
 * @author Sylvain Maucourt (smaucourt@gmail.com)
 * 
 */
public class ClockListener extends EventListener {

    public static String SPACE = ".";

    @Override
    public void run() {
        Calendar now = Calendar.getInstance();
        perform("date:" + now.get(Calendar.MONTH) + SPACE + now.get(Calendar.DAY_OF_MONTH) + SPACE + now.get(Calendar.YEAR));
        perform("time:" + now.get(Calendar.HOUR_OF_DAY) + SPACE + now.get(Calendar.MINUTE));
        perform("day_week:" + now.get(Calendar.DAY_OF_WEEK));
        perform("day_month:" + now.get(Calendar.DAY_OF_MONTH));
        perform("day_year:" + now.get(Calendar.DAY_OF_YEAR));
        perform("minute:" + now.get(Calendar.MINUTE));
        perform("hour:" + now.get(Calendar.HOUR_OF_DAY));
        perform("day_week_time:" + now.get(Calendar.DAY_OF_WEEK) + SPACE + now.get(Calendar.HOUR_OF_DAY) + SPACE + now.get(Calendar.MINUTE));
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " performed");
        }
    }
}
