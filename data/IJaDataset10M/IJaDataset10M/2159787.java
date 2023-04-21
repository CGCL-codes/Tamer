package pl.lodz.p.cm.ctp.epgd;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import com.thoughtworks.xstream.*;
import it.sauronsoftware.cron4j.Scheduler;

public class Epgd {

    static Configuration config;

    static volatile Scheduler scheduler;

    public static void main(String[] args) {
        String configFile = "config.xml";
        scheduler = new Scheduler();
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
            xs.alias("xmlTvGrabber", XmlTvGrabberConfig.class);
            xs.alias("xmlTvGrabbers", ArrayList.class);
            config = (Configuration) xs.fromXML(fis);
        } catch (FileNotFoundException e) {
            error("Configuration file not found! This is a critical problem, shuting down.");
            System.exit(1);
        }
        Thread runtimeHookThread = new Thread() {

            public void run() {
                shutdownHook();
            }
        };
        Runtime.getRuntime().addShutdownHook(runtimeHookThread);
        log("Seting up the scheduler");
        log("Creating XMLTV grabbers");
        for (XmlTvGrabberConfig grabber : config.xmlTvGrabbers) {
            String schedule = grabber.schedule;
            scheduler.schedule(schedule, new ProgramUpdater(grabber));
        }
        log("Creating a cleaner");
        String schedule = config.cleaner.schedule;
        scheduler.schedule(schedule, new Cleaner(config.cleaner));
        scheduler.start();
        try {
            while (true) {
                Thread.sleep(1000L * 60L * 10L);
            }
        } catch (Throwable t) {
            error("Exception " + t.getMessage());
        }
    }

    private static void shutdownHook() {
        log("Shutting down at user request.");
        scheduler.stop();
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
