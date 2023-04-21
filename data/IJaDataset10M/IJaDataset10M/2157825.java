package net.gachet.vfshook;

import java.io.File;
import java.util.Vector;

/**
 * Specific implementation of a sensor monitoring file addition. 
 * If the monitored <code>File</code> object is a directory, this sensor alerts 
 * the registered {@link SensorListener} objects when a new file is added to the 
 * monitored directory or one of its subdirectory. If the monitored 
 * <code>File</code> object is a specific file, this sensor alerts the 
 * registered {@link SensorListener} objects if the file moves from an 
 * non-existing to an existing state.
 * @author <a href="mailto:alexandre@gachet.net>Alexandre Gachet</a>
 * @version $Revision: 1.6 $ ($Date: 2005/01/25 09:15:44 $)
 */
public class NewFileSensor extends AbstractSensor {

    private Vector snapshot;

    private Vector tmp;

    /**
     * Creates a new instance of <code>NewFileSensor</code>
     */
    public NewFileSensor() {
        this.snapshot = new Vector();
        this.tmp = new Vector();
    }

    /**
     * If the monitored <code>File</code> object is a directory, checks if
     * the directory (or one of its subdirectories) contains a new file
     * that was not there during the previous check.
     * If the monitored <code>File</code> object is a specific file, checks
     * if the file did not exist during the previous check and exists
     * now.
     * This method is regularly invoked by the appropriate {@link Monitor}
     * object.
     * @param f Monitored <code>File</code> object (either a file or a
     * directory)
     */
    public void check(File f) {
        tmp.clear();
        recursiveCheck(f);
        snapshot = (Vector) tmp.clone();
    }

    /**
     * Takes a snapshot of the state of the monitored <code>File</code>
     * (either a file or a directory). This sensor simply remembers the
     * existing <code>File</code> object(s). If a new <code>File</code>
     * appears before the next invocation of the {@link #check(File) check} 
     * method, the sensor will detect it by comparing this snapshot with the 
     * new list of existing <code>File</code> objects.
     * This method is first invoked by the appropriate {@link Monitor}
     * object when this Sensor is registered using the
     * {@link Monitor#addSensor(Sensor) addSensor} method.
     * @param f Monitored <code>File</code> object (either a file or a
     * directory)
     */
    public void snapshot(File f) {
        if (!f.exists()) return;
        snapshot.add(f);
        if (f.isFile()) return;
        File[] l = f.listFiles(filter);
        if (l == null) return;
        for (int i = 0; i < l.length; i++) {
            snapshot(l[i]);
        }
    }

    /**
     * If the monitored <code>File</code> object is a directory, checks
     * recursively its contents.
     * @param f Monitored <code>File</code> object (either a file or a
     * directory)
     */
    protected void recursiveCheck(File f) {
        if (!f.exists()) return;
        tmp.add(f);
        if (!snapshot.contains(f)) {
            SensorEvent event = new SensorEvent(this);
            event.put("File added: ", f);
            senseEvent(event);
        }
        if (f.isFile()) return;
        File[] l = f.listFiles(this.filter);
        if (l == null) return;
        for (int i = 0; i < l.length; i++) {
            recursiveCheck(l[i]);
        }
    }
}
