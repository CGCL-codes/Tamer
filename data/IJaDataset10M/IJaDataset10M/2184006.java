package pl.lodz.p.cm.ctp.npvrd;

import java.io.*;
import pl.lodz.p.cm.ctp.dao.*;
import pl.lodz.p.cm.ctp.dao.model.*;
import it.sauronsoftware.cron4j.Scheduler;
import java.text.SimpleDateFormat;
import java.util.*;
import com.thoughtworks.xstream.*;

public class Npvrd {

    static class ChannelListenerThread {

        public Thread thread;

        public ChannelListener channelListener;

        public ChannelListenerThread(Thread thread, ChannelListener channelListener) {
            this.thread = thread;
            this.channelListener = channelListener;
        }
    }

    static Configuration config;

    static ArrayList<ChannelListenerThread> channelListeners;

    static Thread cleanerThread;

    static Scheduler cleanerScheduler;

    public static boolean isAnyRecorderAlive() {
        for (ChannelListenerThread threadListener : channelListeners) {
            if (threadListener.thread.isAlive()) return true;
        }
        return false;
    }

    public static void wakeUpAllRecorders() {
        for (ChannelListenerThread threadListener : channelListeners) {
            threadListener.thread.interrupt();
        }
    }

    public static void setRunModesRecorders(ChannelListener.RunMode newMode) {
        for (ChannelListenerThread threadListener : channelListeners) {
            threadListener.channelListener.setRunMode(newMode);
        }
    }

    /**
	 * @param args Arguments passed to the program in the command line.
	 */
    public static void main(String[] args) {
        String configFile = "config.xml";
        channelListeners = new ArrayList<ChannelListenerThread>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-c")) {
                configFile = args[++i];
            }
        }
        try {
            XStream xs = new XStream();
            FileInputStream fis = new FileInputStream(configFile);
            xs.alias("config", Configuration.class);
            xs.aliasField("database", Configuration.class, "database");
            config = (Configuration) xs.fromXML(fis);
        } catch (FileNotFoundException e) {
            error("Configuration file not found! This is a critical problem, shuting down. Message: " + e.getMessage());
            System.exit(1);
        }
        DAOFactory dbase = DAOFactory.getInstance(config.database);
        TvChannelDAO tvChannelDAO = dbase.getTvChannelDAO();
        try {
            List<TvChannel> tvChannelList = tvChannelDAO.list();
            log("Got " + tvChannelList.size() + " TV channels. Creating listeners for channels.");
            for (TvChannel tvChannel : tvChannelList) {
                ChannelListener NewChannel = new ChannelListener(tvChannel);
                Thread RecordingThread = new Thread(NewChannel);
                channelListeners.add(new ChannelListenerThread(RecordingThread, NewChannel));
                RecordingThread.start();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }
        } catch (DAOException e1) {
            error("Database error: " + e1.getMessage());
            error("This is a critical error. Terminating.");
            System.exit(1);
        }
        cleanerScheduler = new Scheduler();
        cleanerThread = new Thread(new Cleaner());
        cleanerScheduler.schedule(Npvrd.config.cleanerSchedule, cleanerThread);
        cleanerScheduler.start();
        Thread runtimeHookThread = new Thread() {

            public void run() {
                shutdownHook();
            }
        };
        Runtime.getRuntime().addShutdownHook(runtimeHookThread);
        try {
            while (isAnyRecorderAlive()) {
                Thread.sleep(1000L * 60L * 10L);
            }
        } catch (Throwable t) {
            error("Exception " + t.getMessage());
        }
    }

    public static void shutdownHook() {
        log("Shutting down at user request.");
        setRunModesRecorders(ChannelListener.RunMode.STOP);
        wakeUpAllRecorders();
        cleanerScheduler.stop();
    }

    static void error(String msg) {
        System.err.println(getTimeStamp() + " " + msg);
    }

    static void log(String msg) {
        System.out.println(getTimeStamp() + " " + msg);
    }

    private static String getTimeStamp() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f.format(new Date());
    }
}
