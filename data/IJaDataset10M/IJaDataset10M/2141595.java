package com.iv.flash.servlet;

import com.iv.flash.util.*;
import java.io.*;
import java.util.Date;
import java.util.Vector;

/**
 * Generator servlet statistic
 */
public class StatManager {

    static class StatDaemon extends Thread {

        public StatDaemon() {
        }

        public void run() {
            for (; ; ) {
                Date date = new Date(System.currentTimeMillis());
                int day = date.getDay();
                try {
                    Thread.sleep(1000 * 60);
                } catch (InterruptedException e) {
                    continue;
                }
                long now = System.currentTimeMillis();
                today.setEndTime(now);
                save();
                date = new Date(now);
                if (day != date.getDay()) {
                    synchronized (StatManager.class) {
                        today = new StatUnit(now);
                        statistic.addElement(today);
                    }
                }
            }
        }
    }

    private static String statFileName;

    private static StatUnit sinceStartup;

    private static StatUnit today;

    private static Vector statistic;

    private static StatDaemon statDaemon;

    private static void save() {
        try {
            FileOutputStream out = new FileOutputStream(statFileName);
            ObjectOutputStream p = new ObjectOutputStream(out);
            p.writeObject(statistic);
            p.flush();
            out.close();
        } catch (IOException e) {
            Log.logRB(e);
        }
    }

    private static synchronized void load() {
        try {
            FileInputStream in = new FileInputStream(statFileName);
            ObjectInputStream p = new ObjectInputStream(in);
            statistic = (Vector) p.readObject();
            p.close();
            in.close();
        } catch (FileNotFoundException e) {
            statistic = new Vector();
        } catch (Exception e) {
            Log.logRB(e);
            statistic = new Vector();
        }
        today = null;
        Date date = new Date(System.currentTimeMillis());
        int tday = date.getDay();
        for (int i = 0; i < statistic.size(); i++) {
            StatUnit unit = (StatUnit) statistic.elementAt(i);
            date = new Date(unit.getStartTime());
            if (tday == date.getDay()) {
                today = unit;
                break;
            }
        }
        if (today == null) {
            today = new StatUnit(System.currentTimeMillis());
            statistic.addElement(today);
        }
    }

    public static void init() {
        sinceStartup = new StatUnit(System.currentTimeMillis());
        statFileName = PropertyManager.getProperty("com.iv.flash.statFileName", "logs/stat");
        if (statFileName != null) {
            File statFile = Util.getSysFile(statFileName);
            statFileName = statFile.getAbsolutePath();
        }
        load();
        statDaemon = new StatDaemon();
        statDaemon.start();
    }

    public static StatUnit getSinceStartup() {
        return sinceStartup;
    }

    public static Vector getStatistic() {
        return statistic;
    }

    /**
     * Add statistic for the given request
     */
    public static void addRequest(String fileName, int size, long processTime, long totalTime) {
        sinceStartup.addRequest(fileName, size, processTime, totalTime);
        today.addRequest(fileName, size, processTime, totalTime);
    }
}
