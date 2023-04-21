package myriadempires.core.utils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

/**
 *
 * @author Richard
 */
public class Logger {

    private static HashMap<Class, ArrayList<String>> buffers = new HashMap<Class, ArrayList<String>>();

    private static boolean debug = false;

    private static ArrayList<String> ignore = new ArrayList<String>(Arrays.asList(new String[] {}));

    private static ArrayList<String> always = new ArrayList<String>(Arrays.asList(new String[] { "myriadempires.pantheon.ModuleMulticaster" }));

    private static final PrintStream log = System.out;

    private static boolean wait = false;

    static {
        ArrayList<Class> temp = (ArrayList<Class>) ignore.clone();
        temp.retainAll(always);
        if (temp.size() > 0) {
            debug = false;
            always.clear();
            ignore.clear();
            log.format("----ERR:Logger----%n" + "Logger cannot initialise: %d elements are present in both `ignore` and `always` lists%n", temp.size());
            for (Class c : temp) {
                log.format("\t - \"%s\"%n", c);
            }
        }
    }

    public static synchronized boolean supress(Class orig) {
        return supress(orig, false);
    }

    public static synchronized boolean supress(Class orig, boolean flushOld) {
        boolean ret = true;
        if (buffers.containsKey(orig)) {
            if (flushOld) {
                flush(orig);
                ret = false;
            } else {
                return false;
            }
        }
        buffers.put(orig, new ArrayList<String>());
        return ret;
    }

    public static synchronized boolean flush(Class orig) {
        if (!buffers.containsKey(orig)) {
            return false;
        }
        wait = true;
        log.format("%s:%n", orig);
        for (String s : buffers.get(orig)) {
            log.format("\t%s%n", s);
        }
        buffers.remove(orig);
        wait = false;
        synchronized (log) {
            log.notifyAll();
        }
        return true;
    }

    public static synchronized void output(Class orig, String msg) {
        if (ignore.contains(orig.getName()) || !(always.contains(orig.getName()) || debug)) {
            return;
        }
        if (buffers.containsKey(orig)) {
            buffers.get(orig).add(msg);
        } else {
            synchronized (log) {
                try {
                    while (wait) {
                        log.wait();
                    }
                } catch (InterruptedException ex) {
                }
            }
            log.format("%s:%n\t%s%n", orig, msg);
        }
    }
}
