package ws.system;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

public class AdminThread implements Runnable {

    private DataStore store = null;

    private Calendar autoLoadNextRun = Calendar.getInstance();

    private Calendar reportNextRun = null;

    private Date lowSpaceEmailLastRun = new Date(0);

    public AdminThread() {
        System.out.println("Admin Thread: Created");
        store = DataStore.getInstance();
    }

    public void run() {
        System.out.println("Admin Thread: Started");
        try {
            while (true) {
                try {
                    nukeOldFiles();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String scheduleOptions = store.getProperty("guide.source.schedule");
                String[] schOptsArray = scheduleOptions.split(":");
                if (schOptsArray.length == 3 && "1".equals(schOptsArray[0])) {
                    int hour = -1;
                    int min = -1;
                    try {
                        hour = Integer.parseInt(schOptsArray[1]);
                        min = Integer.parseInt(schOptsArray[2]);
                    } catch (Exception e) {
                    }
                    if (autoLoadNextRun.get(Calendar.HOUR_OF_DAY) != hour || autoLoadNextRun.get(Calendar.MINUTE) != min) {
                        autoLoadNextRun.set(Calendar.HOUR_OF_DAY, hour);
                        autoLoadNextRun.set(Calendar.MINUTE, min);
                        autoLoadNextRun.set(Calendar.MILLISECOND, 0);
                        autoLoadNextRun.set(Calendar.SECOND, 0);
                        autoLoadNextRun.add(Calendar.DATE, -2);
                        while (autoLoadNextRun.before(Calendar.getInstance())) {
                            autoLoadNextRun.add(Calendar.DATE, 1);
                        }
                        System.out.println("Auto EPG data load next run at : " + autoLoadNextRun.getTime().toString());
                    }
                    if (autoLoadNextRun.before(Calendar.getInstance())) {
                        System.out.println("Auto Loading EPG Data");
                        while (autoLoadNextRun.before(Calendar.getInstance())) {
                            autoLoadNextRun.add(Calendar.DATE, 1);
                        }
                        System.out.println("Auto EPG data load next run at : " + autoLoadNextRun.getTime().toString());
                        SchGuideLoaderTread sch = new SchGuideLoaderTread();
                        Thread loader = new Thread(Thread.currentThread().getThreadGroup(), sch, sch.getClass().getName());
                        loader.start();
                    }
                }
                String sendWeeklyReport = store.getProperty("email.send.weeklyreport");
                if ("1".equals(sendWeeklyReport) == false) {
                    reportNextRun = null;
                } else {
                    if (reportNextRun == null || reportNextRun.get(Calendar.HOUR_OF_DAY) != 23 || reportNextRun.get(Calendar.MINUTE) != 59) {
                        reportNextRun = Calendar.getInstance();
                        reportNextRun.set(Calendar.HOUR_OF_DAY, 23);
                        reportNextRun.set(Calendar.MINUTE, 59);
                        reportNextRun.set(Calendar.MILLISECOND, 0);
                        reportNextRun.set(Calendar.SECOND, 0);
                        reportNextRun.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                        reportNextRun.add(Calendar.DATE, -14);
                        System.out.println("Weekly report next run at : " + reportNextRun.getTime().toString());
                    }
                    if (reportNextRun.before(Calendar.getInstance())) {
                        System.out.println("Sending Weekly Report");
                        while (reportNextRun.before(Calendar.getInstance())) {
                            reportNextRun.add(Calendar.DATE, 7);
                        }
                        System.out.println("Weekly report next run at : " + reportNextRun.getTime().toString());
                        String notificationBody = buildReportBody(7);
                        System.out.println(notificationBody);
                        EmailSender sender = new EmailSender();
                        sender.setSubject("TV Scheduler Pro Weekly Report");
                        sender.setBody(notificationBody);
                        try {
                            Thread mailThread = new Thread(Thread.currentThread().getThreadGroup(), sender, sender.getClass().getName());
                            mailThread.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                Thread.sleep(60000);
            }
        } catch (Exception e) {
            System.out.println("The main Admin Thread has crashed!");
            e.printStackTrace();
            System.out.println("This is really bad!!!!!");
            store.adminStatus = -1;
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            PrintWriter err = new PrintWriter(ba);
            e.printStackTrace(err);
            err.flush();
            store.adminThreadErrorStack = ba.toString();
        }
        System.out.println("Admin Thread: Exited");
    }

    private String buildReportBody(int prevDays) {
        String data = "TV Scheduler Pro Weekly  Report.\n\n";
        try {
            Calendar now = Calendar.getInstance();
            Calendar till = Calendar.getInstance();
            till.add(Calendar.DATE, prevDays * -1);
            Vector<ScheduleItem> items = new Vector<ScheduleItem>();
            File archiveDir = new File(new DllWrapper().getAllUserPath() + "archive");
            File[] itemFiles = archiveDir.listFiles();
            if (itemFiles != null && itemFiles.length > 0) {
                for (int x = 0; x < itemFiles.length; x++) {
                    if (itemFiles[x].isDirectory() == false && itemFiles[x].getName().startsWith("Schedule-")) {
                        try {
                            FileInputStream in = new FileInputStream(itemFiles[x]);
                            ObjectInputStream oin = new ObjectInputStream(in);
                            ScheduleItem item = (ScheduleItem) oin.readObject();
                            oin.close();
                            in.close();
                            if (item.getStart().getTime() < now.getTime().getTime() && item.getStart().getTime() > till.getTime().getTime()) {
                                items.add(item);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            String[] keys = store.getScheduleKeys();
            for (int x = 0; x < keys.length; x++) {
                ScheduleItem item = store.getScheduleItem(keys[x]);
                if (item.getStart().getTime() < now.getTime().getTime() && item.getStart().getTime() > till.getTime().getTime()) {
                    items.add(item);
                }
            }
            ScheduleItem[] sch_items = (ScheduleItem[]) items.toArray(new ScheduleItem[0]);
            Arrays.sort(sch_items);
            HashMap<String, Integer> warnignCount = new HashMap<String, Integer>();
            SimpleDateFormat df = new SimpleDateFormat("EE MMM d hh:mm a");
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMinimumIntegerDigits(3);
            data += "Schedule Activity List:\n";
            for (int x = 0; x < sch_items.length; x++) {
                data += df.format(sch_items[x].getStart()) + " (" + nf.format(sch_items[x].getDuration()) + ") : " + sch_items[x].getName() + " : " + sch_items[x].getStatus() + " : warnings " + sch_items[x].getWarnings().size() + "\n";
                for (int y = 0; y < sch_items[x].getWarnings().size(); y++) {
                    String waring = sch_items[x].getWarnings().get(y);
                    if (warnignCount.containsKey(waring)) {
                        Integer count = warnignCount.get(waring);
                        warnignCount.put(waring, new Integer(count.intValue() + 1));
                    } else {
                        warnignCount.put(waring, new Integer(1));
                    }
                }
            }
            data += "\n";
            data += "Warning Summary:\n";
            String[] warnkeys = (String[]) warnignCount.keySet().toArray(new String[0]);
            for (int x = 0; x < warnkeys.length; x++) {
                Integer count = warnignCount.get(warnkeys[x]);
                data += warnkeys[x] + " (" + count.intValue() + ")";
            }
            data += "\n\n";
            data += "Capture Path Details:\n";
            String[] paths = store.getCapturePaths();
            for (int x = 0; x < paths.length; x++) {
                File capPath = new File(paths[x]);
                if (capPath.exists() == false) {
                    data += "Path not found : " + capPath.getAbsolutePath() + "\n";
                } else {
                    DllWrapper wrapper = new DllWrapper();
                    long freeSpace = wrapper.getFreeSpace(capPath.getAbsolutePath());
                    freeSpace /= (1024 * 1024);
                    data += capPath.getAbsolutePath() + "\n";
                    data += "   Free Space            : " + nf.format(freeSpace) + " MB\n";
                    long[] statsData = new long[5];
                    for (int y = 0; y < statsData.length; y++) statsData[y] = 0;
                    fileStats(capPath, statsData);
                    nf.setMinimumIntegerDigits(0);
                    data += "   Media Data on Disk    : " + nf.format(statsData[0] / (1024 * 1024)) + " MB\n";
                    data += "   Number of Media Files : " + statsData[1] + "\n";
                    data += "   Number of Directories : " + statsData[2] + "\n";
                }
            }
            data += "\n";
            data += "Next Weekly Report : " + reportNextRun.getTime().toString();
            data += "\n";
        } catch (Exception e) {
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            PrintWriter err = new PrintWriter(ba);
            e.printStackTrace(err);
            err.flush();
            return "Error creating report data!\n\n" + ba.toString();
        }
        return data;
    }

    private void fileStats(File path, long[] statsData) {
        if (path == null || path.exists() == false || path.isDirectory() == false) return;
        statsData[2] += 1;
        File[] list = path.listFiles();
        if (list == null) return;
        for (int x = 0; x < list.length; x++) {
            if (list[x].exists() && list[x].isDirectory()) fileStats(list[x], statsData);
            if (list[x].getName().endsWith(".mpg") || list[x].getName().endsWith(".ts") || list[x].getName().endsWith(".dvr-ms")) {
                long size = list[x].length();
                statsData[0] = statsData[0] + size;
                statsData[1] += 1;
            }
        }
    }

    private void nukeOldFiles() {
        try {
            boolean updated = false;
            HashMap<String, KeepForDetails> adList = store.getAutoDelList();
            String[] key = (String[]) adList.keySet().toArray(new String[0]);
            Arrays.sort(key);
            Calendar limit = Calendar.getInstance();
            Calendar itemDate = Calendar.getInstance();
            for (int x = 0; x < key.length; x++) {
                KeepForDetails item = (KeepForDetails) adList.get(key[x]);
                File delFile = new File(item.getFileName());
                limit.setTime(new Date());
                limit.add(Calendar.DATE, (-1 * item.getKeepFor()));
                itemDate.setTime(item.getCreated());
                if (delFile.exists() == false) {
                    store.autoDelLogAdd("AutoDelete : " + item.getCreated().toString() + " - " + item.getFileName() + " : No longer exists, removing");
                    System.out.println("AutoDelete : " + item.getCreated().toString() + " - " + item.getFileName() + " : No longer exists, removing");
                    adList.remove(key[x]);
                    updated = true;
                } else if (itemDate.before(limit)) {
                    store.autoDelLogAdd("AutoDelete : " + item.getCreated().toString() + " - " + item.getFileName() + " : To old (" + item.getKeepFor() + ") DELETED");
                    System.out.println("AutoDelete : " + item.getCreated().toString() + " - " + item.getFileName() + " : To old (" + item.getKeepFor() + ") DELETED");
                    delFile.delete();
                    adList.remove(key[x]);
                    updated = true;
                }
            }
            String minSpaceWarning = "";
            NumberFormat nf = NumberFormat.getInstance();
            String[] paths = store.getCapturePaths();
            DllWrapper wrapper = new DllWrapper();
            int minSpaceSoft = 1200;
            try {
                minSpaceSoft = Integer.parseInt(store.getProperty("capture.minspacesoft"));
            } catch (Exception e) {
            }
            boolean deletetofreespace = "1".equals(store.getProperty("capture.deletetofreespace"));
            for (int x = 0; x < paths.length; x++) {
                File capPath = new File(paths[x]);
                long freeSpace = wrapper.getFreeSpace(capPath.getCanonicalPath()) / (1024 * 1024);
                if (deletetofreespace && (freeSpace < minSpaceSoft)) {
                    System.out.println("Delete To Free Space : checking auto-delete items");
                    HashMap<String, KeepForDetails> deleteList = store.getAutoDelList();
                    String[] deletekeys = (String[]) deleteList.keySet().toArray(new String[0]);
                    Arrays.sort(deletekeys);
                    for (int index = 0; ((index < deletekeys.length) && (freeSpace < minSpaceSoft)); index++) {
                        KeepForDetails autoDelItem = (KeepForDetails) deleteList.get(deletekeys[index]);
                        File autoDelFile = new File(autoDelItem.getFileName());
                        System.out.println("Delete To Free Space : " + deletekeys[index] + " " + autoDelFile.getAbsolutePath());
                        if (autoDelFile.exists() == false) {
                            store.autoDelLogAdd("AutoDelete : " + autoDelItem.getCreated().toString() + " - " + autoDelItem.getFileName() + " : No longer exists, removing");
                            System.out.println("AutoDelete : " + autoDelItem.getCreated().toString() + " - " + autoDelItem.getFileName() + " : No longer exists, removing");
                            deleteList.remove(deletekeys[x]);
                            updated = true;
                        } else if (autoDelFile.getAbsolutePath().startsWith(capPath.getAbsolutePath())) {
                            store.autoDelLogAdd("AutoDelete : " + autoDelItem.getCreated().toString() + " - " + autoDelItem.getFileName() + " : Removed to free space DELETED");
                            System.out.println("AutoDelete : " + autoDelItem.getCreated().toString() + " - " + autoDelItem.getFileName() + " : Deleted to free space DELETED");
                            autoDelFile.delete();
                            deleteList.remove(deletekeys[x]);
                            updated = true;
                            freeSpace = wrapper.getFreeSpace(capPath.getCanonicalPath()) / (1024 * 1024);
                        }
                    }
                }
                freeSpace = wrapper.getFreeSpace(capPath.getCanonicalPath()) / (1024 * 1024);
                if (freeSpace < minSpaceSoft) {
                    minSpaceWarning += " - " + capPath.getAbsolutePath() + " (" + nf.format(freeSpace) + " MB)\r\n";
                    System.out.println(" - " + capPath.getAbsolutePath() + " (" + nf.format(freeSpace) + " MB)");
                }
            }
            boolean sendLowSpaceWarning = "1".equals(store.getProperty("email.send.freespacelow"));
            if (sendLowSpaceWarning) {
                if (minSpaceWarning.length() > 0) {
                    long timeSinceLast = new Date().getTime() - lowSpaceEmailLastRun.getTime();
                    if (timeSinceLast > (1000 * 60 * 60 * 24)) {
                        System.out.println("Sending Low Space Warning Email");
                        EmailSender sender = new EmailSender();
                        sender.setSubject("TV Scheduler Pro: Low Space Warning");
                        sender.setBody(minSpaceWarning);
                        Thread mailThread = new Thread(Thread.currentThread().getThreadGroup(), sender, sender.getClass().getName());
                        mailThread.start();
                        lowSpaceEmailLastRun = new Date();
                    }
                } else {
                    lowSpaceEmailLastRun = new Date(0);
                }
            } else {
                lowSpaceEmailLastRun = new Date(0);
            }
            if (updated) store.saveAutoDelList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
