package net.sourceforge.xconf.toolbox;

import java.util.Date;

/**
 * Fake implementation of a clock to allow the provision of preset
 * dates and times to objects that require access to the current
 * date and time.
 * 
 * @author Tom Czarniecki
 */
public class FakeClock implements Clock {

    private long currentMillis;

    private Date currentDate;

    public long getCurrentTimeMillis() {
        return currentMillis;
    }

    public void setCurrentMillis(long currentMillis) {
        this.currentMillis = currentMillis;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }
}
