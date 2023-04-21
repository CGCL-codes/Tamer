package indiji.io;

import indiji.struct.Times;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Vector;

/**
 * Some basic logging functionality.
 * @author Pascal Lehwark
 *
 */
public class Log {

    private static Integer maxid = 0;

    private static final HashMap<Integer, String> sv_names = new HashMap<Integer, String>();

    private static final HashMap<Integer, String> sv_descs = new HashMap<Integer, String>();

    private static final HashMap<Integer, Object> sv_values = new HashMap<Integer, Object>();

    private static final TreeSet<Integer> sv_blocking = new TreeSet<Integer>();

    private static final Vector<LogListener> listener = new Vector<LogListener>();

    /**
	 * Log a message (prints to stdio).
	 * @param msg
	 */
    public static synchronized void log(Object msg) {
        String s = Times.render(new Date().getTime(), Times.sec, Times.year, false);
        System.out.println("[" + s + "] " + msg);
    }

    /**
	 * Log an error message (prints to stderr).
	 * @param msg
	 */
    public static synchronized void err(String msg) {
        System.err.println(new Date() + ": " + msg);
    }

    /**
	 * Add a LogListener.
	 * @param l The Listener
	 */
    public static synchronized void addListener(LogListener l) {
        if (!listener.contains(l)) listener.add(l);
    }

    /**
	 * Remove a LogListener
	 * @param l The Listener
	 */
    public static void removeListener(LogListener l) {
        if (listener.contains(l)) listener.remove(l);
    }

    /**
	 * Get a handle for new status variable.
	 * @return The new handle
	 */
    public static synchronized Integer getNewStatsVar() {
        Integer id = maxid++;
        sv_names.put(id, "StatsVar " + id);
        for (LogListener l : listener) l.statsVarAdded(id);
        return id;
    }

    /**
	 * Get a new status variable handle with the given name.
	 * @param name
	 * @return
	 */
    public static synchronized Integer getNewStatsVar(String name) {
        Integer id = maxid++;
        sv_names.put(id, name);
        for (LogListener l : listener) l.statsVarAdded(id);
        return id;
    }

    /**
	 * Get a new status variable handle with the given name and blocking status.
	 * @param name
	 * @param blocking
	 * @return
	 */
    public static synchronized Integer getNewStatsVar(String name, Boolean blocking) {
        Integer id = maxid++;
        sv_names.put(id, name);
        if (blocking) sv_blocking.add(id);
        for (LogListener l : listener) l.statsVarAdded(id);
        if (blocking && sv_blocking.size() == 1) for (LogListener l : listener) l.statsBlockingChanged(true);
        return id;
    }

    /**
	 * Get a new status variable handle with the given name and description
	 * @param name
	 * @param desc
	 * @return
	 */
    public static synchronized Integer getNewStatsVar(String name, String desc) {
        Integer id = maxid++;
        sv_names.put(id, name);
        sv_descs.put(id, desc);
        for (LogListener l : listener) l.statsVarAdded(id);
        return id;
    }

    /**
	 * Remove the status variable for the given handle. 
	 * @param id
	 */
    public static synchronized void removeStatsVar(Integer id) {
        sv_names.remove(id);
        sv_descs.remove(id);
        sv_values.remove(id);
        boolean bc = sv_blocking.size() == 1 && sv_blocking.contains(id);
        sv_blocking.remove(id);
        for (LogListener l : listener) l.statsVarRemoved(id);
        if (bc) for (LogListener l : listener) l.statsBlockingChanged(false);
    }

    /**
	 * Set the name for the given status variable handle.
	 * @param id
	 * @param name
	 */
    public static synchronized void setStatsVarName(Integer id, String name) {
        sv_names.put(id, name);
        for (LogListener l : listener) l.statsVarChanged(id);
    }

    /**
	 * Set the description for the given status variable handle.
	 * @param id
	 * @param desc
	 */
    public static synchronized void setStatsVarDesc(Integer id, String desc) {
        sv_descs.put(id, desc);
        for (LogListener l : listener) l.statsVarChanged(id);
    }

    /**
	 * Set the value for the given status variable handle.
	 * @param id
	 * @param value
	 */
    public static synchronized void setStatsVarValue(Integer id, Object value) {
        sv_values.put(id, value);
        for (LogListener l : listener) l.statsVarChanged(id);
    }

    /**
	 * Set, whether the given status variable is blocking.
	 * @param id
	 * @param b
	 */
    public static synchronized void setStatsVarBlocking(Integer id, Boolean b) {
        if (b) sv_blocking.add(id); else sv_blocking.remove(id);
        for (LogListener l : listener) l.statsVarChanged(id);
    }

    /**
	 * Get the name for the given status variable handle.
	 * @param id
	 * @return
	 */
    public static synchronized String getStatsVarName(Integer id) {
        return sv_names.get(id);
    }

    /**
	 * Get the description for the given status variable handle.
	 * @param id
	 * @return
	 */
    public static synchronized String getStatsVarDesc(Integer id) {
        return sv_descs.get(id);
    }

    /**
	 * Get the value for the given status variable handle.
	 * @param id
	 * @return
	 */
    public static synchronized Object getStatsVarValue(Integer id) {
        return sv_values.get(id);
    }

    /**
	 * Return, whether the given status variable is blocking
	 * @param id
	 * @return
	 */
    public static synchronized Boolean isStatsVarBlocking(Integer id) {
        return sv_blocking.contains(id);
    }

    /**
	 * Get all status variable handles.
	 * @return
	 */
    public static synchronized Vector<Integer> getStatsVarIDs() {
        Vector<Integer> result = new Vector<Integer>();
        result.addAll(sv_names.keySet());
        Collections.sort(result, new Comparator<Integer>() {

            @Override
            public int compare(Integer a, Integer b) {
                if (isStatsVarBlocking(a) && !isStatsVarBlocking(b)) return -1;
                if (isStatsVarBlocking(b) && !isStatsVarBlocking(a)) return 1;
                return sv_names.get(a).compareTo(sv_names.get(b));
            }
        });
        return result;
    }

    /**
	 * Get the string representation for the given status variable handle.
	 * @param id
	 * @return
	 */
    public static synchronized String getStatsVarAsString(Integer id) {
        String result = "";
        String desc = sv_descs.get(id);
        desc = (desc == null ? "" : "(" + desc + ")");
        Object value = sv_values.get(id);
        String val = null;
        if (value != null) {
            if (value instanceof Float) {
                val = ((Float) value) * 100 + "";
                int idx = val.indexOf(".");
                if (idx != -1 && val.length() - idx > 2) val = val.substring(0, idx + 2);
                val += " %";
            } else val = value.toString();
        }
        result += (sv_names.get(id) + ":\t" + val + " " + desc);
        return result;
    }

    /**
	 * Returns, whether at least one of the status variables is blocking.
	 * @return
	 */
    public static synchronized boolean isBlocking() {
        return sv_blocking.size() > 0;
    }
}
