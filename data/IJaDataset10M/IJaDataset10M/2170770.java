package org.kaintoch.gps.gpx.gui;

import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.kaintoch.gps.gpx.*;

/**
 * The tracks (data-collection) used by JGPXtool.
 * It holds
 * <ul>
 * <li>the list of tracks,</li>
 * <li>the longest track's length,</li>
 * <li>the longest track's duration.</li>
 * </ul>
 * @author stefan
 *
 */
public class GpxDataModel implements ListModel, IGpxMinMaxProvider {

    /** list of tracks */
    private DefaultListModel tracks = new DefaultListModel();

    /** list of way points */
    private DefaultListModel waypoints = new DefaultListModel();

    /** the longest track's distance */
    private double maxTrackDistance = 0.0;

    /** the longest track's duration */
    private double maxTrackDuration = 0.0;

    private GpxMinMaxProvider mmProv = null;

    public GpxDataModel() {
        super();
        init();
    }

    private void init() {
        mmProv = new GpxMinMaxProvider();
        maxTrackDistance = 0.0;
        maxTrackDuration = 0.0;
    }

    public void addGpxFile(GpxFile gpxFile) {
        int ii = 0;
        Iterator iiTrks = gpxFile.getTracksIterator();
        while (iiTrks.hasNext()) {
            GpxTrack trk = (GpxTrack) iiTrks.next();
            trk.setName(gpxFile.getName() + "." + ii);
            trk.setInfo(gpxFile.getInfo());
            addGpxTrack(trk);
            ++ii;
        }
        Iterator iiWpts = gpxFile.getWaypointsIterator();
        while (iiWpts.hasNext()) {
            GpxPoint wpt = (GpxPoint) iiWpts.next();
            addGpxWaypoint(wpt);
            ++ii;
        }
    }

    public void addGpxTrack(GpxTrack trk) {
        tracks.addElement(trk);
        trk.addListener(mmProv);
        mmProv.updateMinMax(trk);
        double dist = trk.distance();
        if (dist > maxTrackDistance) {
            maxTrackDistance = dist;
        }
        double dur = trk.duration();
        if (dur > maxTrackDuration) {
            maxTrackDuration = dur;
        }
    }

    public void addGpxWaypoint(GpxPoint wpt) {
        waypoints.addElement(wpt);
        wpt.addListener(mmProv);
        mmProv.updateMinMax(wpt);
    }

    public void delGpxTrack(int idx) {
        tracks.remove(idx);
    }

    public void delAllGpxTrack() {
        tracks.clear();
    }

    public GpxMinMaxPoint getMin() {
        return mmProv.getMin();
    }

    public GpxMinMaxPoint getMax() {
        return mmProv.getMax();
    }

    /**
	 * @return Returns the maxTrackDistance.
	 */
    public double getMaxTrackDistance() {
        return maxTrackDistance;
    }

    /**
	 * @return Returns the maxTrackDuration.
	 */
    public double getMaxTrackDuration() {
        return maxTrackDuration;
    }

    public void addListDataListener(ListDataListener arg0) {
        tracks.addListDataListener(arg0);
    }

    public Object getElementAt(int arg0) {
        return tracks.getElementAt(arg0);
    }

    public int getSize() {
        return tracks.getSize();
    }

    public void removeListDataListener(ListDataListener arg0) {
        tracks.removeListDataListener(arg0);
    }

    public void ensureCapacity(int arg0) {
        tracks.ensureCapacity(arg0);
    }

    public void addListener(IGpxMinMaxListener mmListn) {
        mmProv.addListener(mmListn);
    }

    public void announceChange() {
        mmProv.announceChange();
    }

    public void remListener(IGpxMinMaxListener mmListn) {
        mmProv.remListener(mmListn);
    }

    public int indexOf(Object obj) {
        return indexOf(obj, 0);
    }

    public int indexOf(Object obj, int start) {
        return tracks.indexOf(obj, start);
    }

    public void rebuildMinMax() {
        int ii;
        maxTrackDistance = 0.0;
        maxTrackDuration = 0.0;
        mmProv.rebuildMinMax();
        for (ii = 0; ii < tracks.getSize(); ++ii) {
            GpxTrack child = (GpxTrack) tracks.elementAt(ii);
            mmProv.updateMinMax(child);
            double dist = child.distance();
            if (dist > maxTrackDistance) {
                maxTrackDistance = dist;
            }
            double dur = child.duration();
            if (dur > maxTrackDuration) {
                maxTrackDuration = dur;
            }
        }
        ListDataListener[] listeners = tracks.getListDataListeners();
        for (ii = 0; ii < listeners.length; ++ii) {
            ListDataEvent event = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, listeners.length);
            listeners[ii].contentsChanged(event);
        }
    }
}
