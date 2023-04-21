package net.narusas.daumaccess;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Scheduler extends Thread {

    Logger logger = Logger.getLogger("log");

    List<SchedulerJob> jobs = new LinkedList<SchedulerJob>();

    private int lastHour = -1;

    private int lastMin = -1;

    private String[] DATES = { "��", "��", "ȭ", "��", "��", "��", "��" };

    public Scheduler() {
        super("Scheduler thread");
    }

    public void addJob(String dir, String date, int hour, int min, int count, ScheduledJob run) {
        logger.log(Level.INFO, "�������� �߰��մϴ�. ����=" + date + ", �ð�=" + hour + " ��=" + min + " ���=" + dir);
        jobs.add(new SchedulerJob(dir, date, hour, min, count, run));
    }

    @Override
    public void run() {
        while (true) {
            logger.log(Level.INFO, "�������� üũ�մϴ�");
            execute(available());
            skip(1000 * 30);
        }
    }

    List<SchedulerJob> available() {
        Date d = new Date(System.currentTimeMillis());
        String date = DATES[d.getDay()];
        return available(date, d.getHours(), d.getMinutes());
    }

    List<SchedulerJob> available(String date, int hour, int min) {
        List<SchedulerJob> res = new LinkedList<SchedulerJob>();
        if (hour == lastHour && min == lastMin) {
            return res;
        }
        lastHour = hour;
        lastMin = min;
        for (SchedulerJob job : jobs) {
            boolean isAvailable = job.isAvailable(date, hour, min);
            System.out.println(isAvailable);
            if (isAvailable) {
                res.add(job);
            }
        }
        return res;
    }

    void execute(List<SchedulerJob> temp) {
        for (final SchedulerJob job : temp) {
            logger.log(Level.INFO, "�������� �����մϴ�. �ð�=" + job.hour + " ��=" + job.min);
            job.update();
            new Thread(job.run, "SchedulerJobThread").start();
        }
    }

    private void skip(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }
}

class SchedulerJob {

    int hour;

    int min;

    final ScheduledJob run;

    int count;

    public String dir;

    public String date;

    public SchedulerJob(String dir, String date, int hour, int min, int count, ScheduledJob run) {
        this.dir = dir;
        this.date = date;
        this.hour = hour;
        this.min = min;
        this.count = count;
        this.run = run;
    }

    public void update() {
        run.setCount(count);
        run.setPath(dir);
    }

    public boolean isAvailable(String date, int hour2, int min2) {
        return hour == hour2 && min == min2 && this.date.equals(date);
    }
}
