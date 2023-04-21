package org.tiberiumlabs.lailaps.downloads;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.tiberiumlabs.lailaps.Mediator;
import org.tiberiumlabs.lailaps.settings.BehaviourSettings;
import org.tiberiumlabs.util.NumberFormat;
import static org.tiberiumlabs.lailaps.actions.ActionFactory.ACTION_THREAD_NAME;

/**
 *
 * @author <a href="mailto:paranoid.tiberiumlabs@gmail.com">Paranoid</a>
 */
public class DownloadManager {

    private HashMap<String, Integer> containsMap;

    private ArrayList<DownloadItem> downloadItems;

    private NumberFormat numberFormat = NumberFormat.sharedInstance();

    public int addItem(DownloadItem item) {
        threadCheck();
        if (behaviourSettings.isIgnoreExisting() && containsMap.containsKey(item.getLink())) {
            return -1;
        }
        containsMap.put(item.getLink(), null);
        if (behaviourSettings.isAddToFront()) {
            downloadItems.add(0, item);
            return 0;
        } else {
            int s = downloadItems.size();
            downloadItems.add(item);
            return s;
        }
    }

    private void threadCheck() {
        if (!ACTION_THREAD_NAME.equals(Thread.currentThread().getName())) {
            throw new ConcurrentModificationException("Downloads list can be changed only from actions thread");
        }
    }

    public int getCount() {
        return downloadItems.size();
    }

    public Object getStatus(int row) {
        return null;
    }

    public Object getLink(int row) {
        return downloadItems.get(row).getLink();
    }

    public Object getFile(int row) {
        return downloadItems.get(row).getFile();
    }

    public Object getPath(int row) {
        return downloadItems.get(row).getWorkingPath();
    }

    public Object getSize(int row) {
        return numberFormat.formatSize(downloadItems.get(row).getSize());
    }

    public Object getCompleted(int row) {
        return null;
    }

    public Object getSpeed(int row) {
        return null;
    }

    public Object getProgress(int row) {
        return null;
    }

    public Object getPercents(int row) {
        return null;
    }

    public Object getTimeLeft(int row) {
        return null;
    }

    public Object getTimeSpent(int row) {
        return null;
    }

    public Object getSegments(int row) {
        return null;
    }

    public Object getSegmentsProgress(int row) {
        return null;
    }

    private final ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();

    public void addTableModelListener(TableModelListener l) {
        if (l != null) {
            synchronized (listeners) {
                listeners.add(l);
            }
        }
    }

    public void removeTableModelListener(TableModelListener l) {
        if (l != null) {
            synchronized (listeners) {
                listeners.remove(l);
            }
        }
    }

    public void fireTableModelEvent(TableModelEvent event) {
        synchronized (listeners) {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).tableChanged(event);
            }
        }
    }

    private BehaviourSettings behaviourSettings;

    private BehaviourSettings getBehaviourSettings() {
        if (behaviourSettings == null) {
            behaviourSettings = Mediator.getMediator().getSettings().getBehaviourSettings();
        }
        return behaviourSettings;
    }
}
