package net.sf.jaer.util;

import ch.unizh.ini.jaer.projects.opticalflow.Chip2DMotion;
import java.util.*;
import java.lang.reflect.Modifier;
import java.util.logging.Logger;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 * Finds subclasses of a given class name in classes on the loaded classpath.
 * Classes are cached in a HashMap to reduce cost of subsequent lookups.
 * <p>
 * See http://www.javaworld.com/javaworld/javatips/jw-javatip113.html?page=2
 * @author tobi
 */
public class SubclassFinder {

    private static final Logger log = Logger.getLogger("SubclassFinder");

    /** Creates a new instance of SubclassFinder */
    private SubclassFinder() {
    }

    private static class FastClassFinder {

        static HashMap<String, Class> map = new HashMap<String, Class>();

        private static synchronized Class forName(String name) throws ClassNotFoundException, ExceptionInInitializerError {
            Class c = null;
            if ((c = map.get(name)) == null) {
                try {
                    c = Class.forName(name);
                    map.put(name, c);
                } catch (Exception e) {
                    log.warning("caught " + e + " when trying to get class named " + name);
                }
                return c;
            } else {
                return c;
            }
        }

        private static synchronized void clear() {
            map.clear();
        }
    }

    /** Updates a ProgressMonitor while finding subclasses
     * 
     * @param name class to find subclasses of
     * @return list of subclasses that are not abstract
     */
    public static ArrayList<String> findSubclassesOf(String name) {
        return findSubclassesOf(name, null);
    }

    /** Finds subclasses in SwingWorker */
    public static class SubclassFinderWorker extends SwingWorker<ArrayList<String>, Object> {

        Class clazz;

        public SubclassFinderWorker(Class clazz) {
            this.clazz = clazz;
        }

        /** Called by SwingWorker on execute()
         * 
         * @return the list of classes that are subclasses.
         * @throws Exception on any error
         */
        @Override
        protected ArrayList<String> doInBackground() throws Exception {
            setProgress(0);
            String superClassName = clazz.getName();
            ArrayList<String> classes = new ArrayList<String>(100);
            if (superClassName == null) {
                log.warning("tried to find subclasses of null class name, returning empty list");
                return classes;
            }
            try {
                publish("Building class list");
                Class superClass = FastClassFinder.forName(superClassName);
                List<String> allClasses = ListClasses.listClasses();
                int n = ".class".length();
                Class c = null;
                if (allClasses.isEmpty()) {
                    log.warning("List of subclasses of " + superClassName + " is empty, is there something wrong with your classpath. Do you have \"compile on save\" turned on? (This option can break the SubclassFinder).");
                }
                int i = 0;
                int nclasses = allClasses.size();
                publish("Scanning class list to find subclasses");
                int lastProgress = 0;
                for (String s : allClasses) {
                    i++;
                    try {
                        int p = (int) ((float) i / nclasses * 100);
                        if (p > lastProgress + 5) {
                            setProgress(p);
                            lastProgress = p;
                        }
                        s = s.substring(0, s.length() - n);
                        s = s.replace('/', '.').replace('\\', '.');
                        if (s.indexOf("$") != -1) {
                            continue;
                        }
                        c = FastClassFinder.forName(s);
                        if (c == superClass || c == null) {
                            continue;
                        }
                        if (Modifier.isAbstract(c.getModifiers())) {
                            continue;
                        }
                        if (superClass.isAssignableFrom(c)) {
                            classes.add(s);
                        }
                    } catch (Throwable t) {
                        log.warning("ERROR: " + t + " while seeing if " + superClass + " isAssignableFrom " + c);
                    }
                }
            } catch (Exception e) {
                log.warning("Exception " + e.toString() + " while finding subclasses of " + superClassName);
                e.printStackTrace();
            } finally {
                return classes;
            }
        }

        @Override
        protected void done() {
            setProgress(100);
        }

        @Override
        protected void process(List<Object> chunks) {
        }
    }

    /** Finds and returns list of fully-qualified name Strings of all subclases of a class.
     * @param superClassName the fully qualified name, e.g. net.sf.jaer.chip.AEChip
     * @param progressMonitor updated during search
     * @return list of fully qualified class names that are subclasses (and not the same as) the argument
     * @see #findSubclassesOf(java.lang.String) 
     */
    public static ArrayList<String> findSubclassesOf(String superClassName, final ProgressMonitor progressMonitor) {
        ArrayList<String> classes = new ArrayList<String>(100);
        if (superClassName == null) {
            log.warning("tried to find subclasses of null class name, returning empty list");
            return classes;
        }
        try {
            if (progressMonitor != null) {
                progressMonitor.setNote("Building class list");
            }
            Class superClass = FastClassFinder.forName(superClassName);
            List<String> allClasses = ListClasses.listClasses();
            int n = ".class".length();
            Class c = null;
            if (allClasses.isEmpty()) {
                log.warning("List of subclasses of " + superClassName + " is empty, is there something wrong with your classpath. Do you have \"compile on save\" turned on? (This option can break the SubclassFinder).");
            }
            int i = 0;
            if (progressMonitor != null) {
                progressMonitor.setNote("Scanning class list to find subclasses");
            }
            if (progressMonitor != null) {
                progressMonitor.setMaximum(allClasses.size());
            }
            for (String s : allClasses) {
                try {
                    if (progressMonitor != null) {
                        if (progressMonitor.isCanceled()) {
                            break;
                        }
                        progressMonitor.setProgress(i);
                        i++;
                    }
                    s = s.substring(0, s.length() - n);
                    s = s.replace('/', '.').replace('\\', '.');
                    if (s.indexOf("$") != -1) {
                        continue;
                    }
                    c = FastClassFinder.forName(s);
                    if (c == superClass || c == null) {
                        continue;
                    }
                    if (Modifier.isAbstract(c.getModifiers())) {
                        continue;
                    }
                    if (superClass.isAssignableFrom(c)) {
                        classes.add(s);
                    }
                } catch (Throwable t) {
                    log.warning("ERROR: " + t + " while seeing if " + superClass + " isAssignableFrom " + c);
                }
            }
        } catch (Exception e) {
            log.warning("Exception " + e.toString() + " while finding subclasses of " + superClassName);
            e.printStackTrace();
        } finally {
            return classes;
        }
    }

    public static void main(String[] args) {
        final String superclass = "net.sf.jaer.eventprocessing.EventFilter2D";
        System.out.println("Subclasses of " + superclass + " are:");
        ArrayList<String> classNames = findSubclassesOf(superclass);
        for (String s : classNames) {
            System.out.println(s);
        }
        System.exit(0);
    }
}
