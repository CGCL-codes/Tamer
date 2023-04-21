package bt747.j2se_view.model;

import gps.log.GPSRecord;
import gps.log.out.CommonOut;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import javax.swing.AbstractListModel;
import net.sf.bt747.j2se.app.filefilters.KnownFileFilter;
import net.sf.bt747.j2se.app.utils.GPSRecordTimeComparator;
import org.jdesktop.beans.AbstractBean;
import bt747.j2se_view.J2SEAppModel;
import bt747.model.Controller;
import bt747.model.ModelEvent;
import bt747.sys.Generic;
import bt747.sys.interfaces.BT747Path;

/**
 * @author Mario
 * 
 */
@SuppressWarnings("serial")
public class PositionData extends AbstractBean {

    public static final String WAYPOINTSELECTED = "selectedwaypoint";

    public static final String WPDISPLAYCHANGE = "wpdisplaychange";

    J2SEAppModel m;

    private List<List<GPSRecord>> trks = new Vector<List<GPSRecord>>();

    private final Vector<MapWaypoint> wayPoints = new Vector<MapWaypoint>();

    private final Vector<MapWaypoint> userWayPoints = new Vector<MapWaypoint>();

    /**
     * 
     */
    public PositionData() {
    }

    public PositionData(final J2SEAppModel m) {
        this.m = m;
    }

    public final List<List<GPSRecord>> getTracks() {
        return trks;
    }

    public final void setTracks(final List<List<GPSRecord>> trks) {
        this.trks = trks;
        fireTrackPointListChange();
    }

    public final GPSRecord[] getWayPoints() {
        final GPSRecord[] r = new GPSRecord[wayPoints.size()];
        int index = 0;
        for (final MapWaypoint w : wayPoints) {
            r[index++] = w.getGpsRecord();
        }
        return r;
    }

    public final List<MapWaypoint> getBT747Waypoints() {
        return wayPoints;
    }

    public final List<MapWaypoint> getBT747UserWaypoints() {
        return userWayPoints;
    }

    public final void setWayPoints(final GPSRecord[] waypoints) {
        wayPoints.removeAllElements();
        if (waypoints != null) {
            for (final GPSRecord wp : waypoints) {
                wayPoints.add(new MapWaypoint(wp));
            }
        }
        fireWaypointListUpdate();
    }

    public final GPSRecord[] getUserWayPointsGPSRecords() {
        final GPSRecord[] r = new GPSRecord[userWayPoints.size()];
        int index = 0;
        for (final MapWaypoint w : userWayPoints) {
            r[index++] = w.getGpsRecord();
        }
        return r;
    }

    public final List<MapWaypoint> getUserWayPoints() {
        return userWayPoints;
    }

    public void userWaypointsUpdated() {
        fireUserWaypointUpdate();
    }

    public void dataUpdated() {
        fireTrackPointListChange();
        fireWaypointListUpdate();
        fireUserWaypointUpdate();
    }

    private void fireWaypointListUpdate() {
        postEvent(new ModelEvent(J2SEAppModel.UPDATE_WAYPOINT_LIST, null));
    }

    /**
     * 
     */
    private void fireUserWaypointUpdate() {
        userWpListModel.fireContentsChanged(userWpListModel, 0, userWpListModel.getSize() - 1);
        postEvent(new ModelEvent(J2SEAppModel.UPDATE_USERWAYPOINT_LIST, null));
    }

    private final void fireTrackPointListChange() {
        postEvent(new ModelEvent(J2SEAppModel.UPDATE_TRACKPOINT_LIST, trks));
    }

    private final void postEvent(final ModelEvent e) {
        if (m != null) {
            m.postEvent(e);
        }
    }

    private UserWayPointListModel userWpListModel = new UserWayPointListModel();

    ;

    public UserWayPointListModel getWaypointListModel() {
        return userWpListModel;
    }

    public GPSRecord[] getSortedWaypointGPSRecords() {
        GPSRecord[] rcrds;
        rcrds = new GPSRecord[userWayPoints.size()];
        int i = 0;
        for (final MapWaypoint w : userWayPoints) {
            GPSRecord r = w.getGpsRecord();
            if (r != null) {
                rcrds[i++] = r;
            } else {
                r = GPSRecord.getLogFormatRecord(0);
                bt747.sys.Generic.debug("Null GPS Record found");
            }
        }
        java.util.Arrays.sort(rcrds, new GPSRecordTimeComparator());
        return rcrds;
    }

    private void fireLogFileUpdate() {
        postEvent(new ModelEvent(ModelEvent.UPDATE_LOG_FILE_LIST, null));
    }

    public final void addFiles(final File[] files) {
        if (files != null) {
            final FileFilter filter = new KnownFileFilter();
            for (int i = 0; i < files.length; i++) {
                try {
                    final File f = files[i];
                    if (f.exists()) {
                        final String path = files[i].getCanonicalPath();
                        if (filter.accept(f)) {
                            Controller.addLogFile(new BT747Path(path));
                        } else {
                            userWpListModel.add(path);
                        }
                    } else {
                        System.err.println("File not found: " + f.getCanonicalPath());
                    }
                } catch (final IOException e) {
                    Generic.debug("Issue with adding a file", e);
                }
            }
            fireLogFileUpdate();
        }
    }

    private void fireWpDisplayChange() {
        firePropertyChange(WPDISPLAYCHANGE, null, Boolean.TRUE);
    }

    @SuppressWarnings("serial")
    public class UserWayPointListModel extends AbstractListModel implements PropertyChangeListener {

        private java.util.Hashtable<String, FileWaypoint> imageTable = new Hashtable<String, FileWaypoint>();

        public void add(final String path) {
            synchronized (imageTable) {
                if (!imageTable.contains(path)) {
                    final ImageData id = new ImageData();
                    if (id.setFilePath(new BT747Path(path))) {
                        add(id);
                    } else {
                        final FileWaypoint fw = new FileWaypoint();
                        fw.setFilePath(new BT747Path(path));
                        add(fw);
                    }
                }
            }
        }

        public void add(final FileWaypoint id) {
            synchronized (imageTable) {
                imageTable.put(id.getFilePath().getPath(), id);
                add(new MapWaypoint(id));
            }
        }

        public void add(final MapWaypoint wp) {
            synchronized (userWayPoints) {
                userWayPoints.add(wp);
                final int row = userWayPoints.size() - 1;
                wp.addPropertyChangeListener(this);
                fireIntervalAdded(this, row, row);
            }
        }

        public void remove(Object[] elements) {
            int count = imageTable.size() - 1;
            for (Object element : elements) {
                if (element instanceof ImageData) {
                    final ImageData new_name = (ImageData) element;
                    imageTable.remove(new_name.getFilePath());
                }
                userWayPoints.remove(element);
            }
            userWpListModel.fireIntervalRemoved(this, 0, count);
        }

        public void clear() {
            int org;
            org = userWayPoints.size();
            userWayPoints.removeAllElements();
            imageTable.clear();
            userWpListModel.fireIntervalRemoved(userWpListModel, 0, org - 1);
        }

        public Object getElementAt(final int index) {
            if (index >= 0 && index < userWayPoints.size()) {
                return userWayPoints.get(index);
            }
            return null;
        }

        public int getSize() {
            return userWayPoints.size();
        }

        @Override
        protected void fireContentsChanged(final Object source, final int index0, final int index1) {
            super.fireContentsChanged(source, index0, index1);
        }

        @Override
        protected void fireIntervalAdded(final Object source, final int index0, final int index1) {
            super.fireIntervalAdded(source, index0, index1);
        }

        public void fireChange(final int idx) {
            super.fireContentsChanged(this, idx, idx);
        }

        @Override
        protected void fireIntervalRemoved(final Object source, final int index0, final int index1) {
            super.fireIntervalRemoved(source, index0, index1);
        }

        public void propertyChange(final PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(MapWaypoint.PROPERTY_SELECTED)) {
                fireWpDisplayChange();
                try {
                    if ((Boolean) evt.getNewValue()) {
                        fireWpSelected((MapWaypoint) evt.getSource());
                    }
                } catch (final Exception e) {
                }
            } else if (evt.getPropertyName().equals(MapWaypoint.PROPERTY_SHOWTAG)) {
                fireWpDisplayChange();
            }
        }
    }

    private void fireWpSelected(final MapWaypoint w) {
        firePropertyChange(WAYPOINTSELECTED, null, w);
    }

    public static final Object getData(final MapWaypoint wpt, final int type) {
        final BT747Waypoint w = wpt.getBT747Waypoint();
        Object result;
        result = GPSRecordUtils.getValue(w.getGpsRecord(), type);
        if (result == null) {
            if (w instanceof FileWaypoint) {
                final FileWaypoint fw = (FileWaypoint) w;
                switch(type) {
                    case DataTypes.FILE_DATE:
                        return CommonOut.getDateStr(fw.getUtc());
                    case DataTypes.FILE_TIME:
                        return CommonOut.getTimeStr(fw.getUtc());
                    case DataTypes.FILE_DATETIME:
                        return CommonOut.getDateTimeStr(fw.getUtc());
                    case DataTypes.FILE_PATH:
                        return fw.getFilePath();
                }
            }
            if (w instanceof ImageData) {
                final ImageData img = (ImageData) w;
                switch(type) {
                    case DataTypes.IMAGE_WIDTH:
                        return Integer.valueOf(img.getWidth());
                    case DataTypes.IMAGE_HEIGHT:
                        return Integer.valueOf(img.getHeight());
                    case DataTypes.GEOMETRY:
                        if (img.getWidth() != 0) {
                            return img.getWidth() + "x" + img.getHeight();
                        }
                        break;
                }
            }
        }
        return result;
    }
}
