package dupefinder;

import java.io.*;
import java.util.*;
import java.security.*;

public class Dupefinder {

    File startDir;

    LinkedList dirs = new LinkedList();

    int numCompleted;

    long time;

    public Dupefinder(String startDir) {
        this.startDir = new File(startDir);
    }

    public void start() {
        if (!startDir.isDirectory()) usage(startDir + " is not a directory");
        initDirs();
        time = System.currentTimeMillis();
        System.err.println("Starting: " + new Date(time).toGMTString());
        for (Iterator it = dirs.iterator(); it.hasNext(); ) {
            handleDir((File) it.next());
        }
        printResults();
    }

    private void initDirs() {
        handleSubdirs(startDir);
        System.err.println("Finished reading dirs. Total in tree: " + dirs.size());
    }

    private void printResults() {
        Set keys = map.keySet();
        for (Iterator it = keys.iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            LinkedList list = (LinkedList) map.get(key);
            print(key, list);
        }
    }

    private void print(String key, LinkedList list) {
        if (list.size() < 2) return;
        boolean first = true;
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            if (!first) {
                ((File) it.next()).delete();
                continue;
            }
            System.out.println(it.next());
            first = false;
        }
    }

    private void handleDir(File directory) {
        File[] files = directory.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.isFile() && pathname.canRead();
            }
        });
        if (files == null) return;
        for (int i = 0; i != files.length; ++i) {
            handleFile(files[i]);
        }
        if ((++numCompleted % 150) == 0) {
            System.err.println("Completed " + numCompleted + " of " + dirs.size());
            long timeSpent = System.currentTimeMillis() - time;
            float rate = timeSpent / numCompleted;
            long eta = (long) ((rate * dirs.size()) + time);
            System.err.println("ETA : " + new Date(eta).toGMTString());
        }
    }

    private void handleFile(File file) {
        add(getID(file), file);
    }

    private MessageDigest md = null;

    private String getID(File file) {
        FileInputStream is = null;
        try {
            md = md == null ? MessageDigest.getInstance("MD5") : md;
            md.reset();
            is = new FileInputStream(file);
            byte[] buf = new byte[512];
            int len = 0;
            while ((len = is.read(buf)) != -1) {
                md.update(buf, 0, len);
            }
        } catch (Throwable t) {
            usage(t.getMessage());
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return new String(md.digest());
    }

    private void handleSubdirs(File parent) {
        File[] files = parent.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.isDirectory() && pathname.canRead();
            }
        });
        for (int i = 0; i != files.length; ++i) {
            dirs.add(files[i]);
            handleSubdirs(files[i]);
        }
    }

    private HashMap map = new HashMap();

    private void add(String id, File fullName) {
        LinkedList entry = (LinkedList) map.get(id);
        if (entry == null) {
            entry = new LinkedList();
            map.put(id, entry);
        }
        entry.add(fullName);
    }

    private static void usage(String mes) {
        System.out.println(mes);
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            usage("usage: [jre] Duperfinder startDir");
        }
        Dupefinder f = new Dupefinder(args[0]);
        f.start();
    }
}
