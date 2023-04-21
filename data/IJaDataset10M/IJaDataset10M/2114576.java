package org.homeunix.thecave.buddi.view.dialogs.schedule;

import org.homeunix.thecave.buddi.model.ScheduledTransaction;
import ca.digitalcave.moss.swing.MossPanel;

public class WeekdayCard extends MossPanel implements ScheduleCard {

    public static final long serialVersionUID = 0;

    public int getScheduleDay() {
        return 0;
    }

    public int getScheduleWeek() {
        return 0;
    }

    public int getScheduleMonth() {
        return 0;
    }

    public void loadSchedule(ScheduledTransaction s) {
    }
}
