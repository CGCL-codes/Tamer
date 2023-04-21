package com.fujitsu.arcon.njs.logger;

import java.util.*;

/**
 * @author Sven van den Berghe, fujitsu 
 *
 * @version $Id: LoggerManager.java,v 1.2 2004/06/30 13:45:33 svenvdb Exp $
 *
 **/
public class LoggerManager {

    public static class Group {

        public static final Group GNRL = new LoggerManager.Group('G');

        public static final Group AANS = new LoggerManager.Group('A');

        public static final Group RCPT = new LoggerManager.Group('R');

        public static final Group AUTH = new LoggerManager.Group('U');

        public static final Group TSII = new LoggerManager.Group('T');

        public static final Group EXTN = new LoggerManager.Group('E');

        Set factories;

        private Group(char n) {
            factories = new HashSet();
            name = n;
        }

        private char name;

        public char getName() {
            return name;
        }

        private LoggerLevel level;

        public void setLevel(LoggerLevel new_level) {
            if (new_level != level) {
                if (logger.CHAT) logger.chat("Changing to log level <" + new_level + "> for group <" + getName() + ">");
                Iterator i = factories.iterator();
                while (i.hasNext()) {
                    ((LoggerFactory) i.next()).setLevel(new_level);
                }
                level = new_level;
            }
        }

        public LoggerLevel getLevel() {
            return level;
        }
    }

    private static Handler to_file_handler;

    public static void setLoggingDir(java.io.File log_dir) {
        to_file_handler = new Handler(log_dir);
    }

    public static Handler getFileLoggingHandler() {
        return to_file_handler;
    }

    private static Map by_class_name = new HashMap();

    public static Logger logger;

    public static Logger get(String class_name) {
        return ((LoggerFactory) by_class_name.get(class_name)).get();
    }

    public static void add(String class_name, LoggerFactory logger_factory, LoggerManager.Group logger_group) {
        by_class_name.put(class_name, logger_factory);
        logger_group.factories.add(logger_factory);
    }

    public static void setLevel(LoggerLevel level) {
        Group.GNRL.setLevel(level);
        Group.AANS.setLevel(level);
        Group.RCPT.setLevel(level);
        Group.AUTH.setLevel(level);
        Group.TSII.setLevel(level);
        Group.EXTN.setLevel(level);
    }

    private static long transaction_id = 0;

    public static void newLogFiles() {
        transaction_id = System.currentTimeMillis();
        newLogFiles(Group.GNRL);
        newLogFiles(Group.AANS);
        newLogFiles(Group.RCPT);
        newLogFiles(Group.AUTH);
        newLogFiles(Group.TSII);
        newLogFiles(Group.EXTN);
        transaction_id = 0;
    }

    public static void newLogFiles(LoggerManager.Group logger_group) {
        if (logger.CHAT) logger.chat("Changing log file for group <" + logger_group.getName() + ">");
        boolean new_id = false;
        if (transaction_id == 0) {
            transaction_id = System.currentTimeMillis();
            new_id = true;
        }
        Iterator i = logger_group.factories.iterator();
        while (i.hasNext()) {
            ((LoggerFactory) i.next()).getHandler().newLogFile(transaction_id);
        }
        if (new_id) transaction_id = 0;
    }

    private static long nlfci = 99999999;

    public static long getLogFileChangeInterval() {
        return nlfci;
    }

    /**
	 * Set the change interval for those Handlers that are interested,
	 * not all have to be.
	 *
	 * In hours, takes abs value
	 *
	 **/
    public static void setLogFileChangeInterval(long l) {
        if (l < 0) {
            nlfci = -l;
        } else {
            nlfci = l;
        }
        if (nlfci == 0) nlfci = 1;
        nlfci = nlfci * 60 * 60 * 1000;
        to_file_handler.intervalChanged();
    }

    public static void setChangeLogFileOnTheHour() {
        nlfci = -1;
        to_file_handler.intervalChanged();
    }

    public static void setChangeLogFileOnTheDay() {
        nlfci = -2;
        to_file_handler.intervalChanged();
    }

    public static String getInfo() {
        String rtn = "";
        if (nlfci == -1) {
            rtn += "Change log files on the hour.\n";
        } else if (nlfci == -2) {
            rtn += "Change log files on the day.\n";
        } else {
            rtn += "Change log files every " + nlfci / (60 * 60 * 1000) + " hours.\n";
        }
        rtn += "The current log file is " + ((LoggerFactory) Group.GNRL.factories.iterator().next()).getHandler().getCurrentFileName() + "\n";
        rtn += "Group " + Group.GNRL.getName() + " is logging at level " + Group.GNRL.getLevel() + ".\n";
        rtn += "Group " + Group.AANS.getName() + " is logging at level " + Group.AANS.getLevel() + ".\n";
        rtn += "Group " + Group.RCPT.getName() + " is logging at level " + Group.RCPT.getLevel() + ".\n";
        rtn += "Group " + Group.AUTH.getName() + " is logging at level " + Group.AUTH.getLevel() + ".\n";
        rtn += "Group " + Group.TSII.getName() + " is logging at level " + Group.TSII.getLevel() + ".\n";
        rtn += "Group " + Group.EXTN.getName() + " is logging at level " + Group.EXTN.getLevel() + ".\n";
        return rtn;
    }
}
