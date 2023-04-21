package org.cyberaide.calendar;

import java.util.TimerTask;
import java.util.Date;

/**
 * The class represents a task that will be executed at a given time on remote machine.
 *
 */
public class CalendarTask extends TimerTask {

    private CalendarCLI cli;

    private String machineid;

    private String jobid;

    public CalendarTask(CalendarCLI cli, String machineid, String jobid) {
        this.cli = cli;
        this.machineid = machineid;
        this.jobid = jobid;
    }

    @Override
    public void run() {
        try {
            System.out.println(new Date());
            cli.submit(machineid, jobid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
