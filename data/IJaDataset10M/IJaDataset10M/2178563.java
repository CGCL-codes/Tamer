package clubmixerlibraryeditor.handler;

import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author Alexander Schindler
 */
public class DirTreeEventHandler {

    private Vector listeners;

    public DirTreeEventHandler() {
    }

    public synchronized void addDirTreeEventListener(DirTreeEventListener l) {
        if (listeners == null) {
            listeners = new Vector();
        }
        listeners.addElement(l);
    }

    public void fireDirTreeEventChanged(DirTreeEvent event) {
        if ((listeners != null) && !listeners.isEmpty()) {
            Vector targets;
            synchronized (this) {
                targets = (Vector) listeners.clone();
            }
            Enumeration e = targets.elements();
            while (e.hasMoreElements()) {
                DirTreeEventListener l = (DirTreeEventListener) e.nextElement();
                l.dirTreeSelectionChanged(event);
            }
        }
    }

    public synchronized void removeDirTreeEventListener(DirTreeEventListener l) {
        if (listeners == null) {
            listeners = new Vector();
        }
        listeners.removeElement(l);
    }
}
