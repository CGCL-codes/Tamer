package org.openstreetmap.travelingsalesman.gps.data;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import javax.swing.DefaultListModel;
import org.openstreetmap.travelingsalesman.gps.BaseGpsProvider;
import org.openstreetmap.travelingsalesman.gps.GPXFileProvider;
import org.openstreetmap.travelingsalesman.gps.NMEAFileProvider;
import org.openstreetmap.travelingsalesman.painting.ColorHelper;

/**
 * Storage for several gps tracks.
 * TODO: distinct color and visualization details from model class.
 * TODO: hide some unsafe inherited methods.
 * @author <a href="mailto:oleg_chubaryov@mail.ru">Oleg Chubaryov</a>
 * */
public class GpsTracksStorage extends DefaultListModel {

    /**
     * Default serial UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Storage listeners.
     */
    private HashSet<ITracksChangeListener> myListeners = new HashSet<ITracksChangeListener>();

    /**
     * The colors to use for the different tracks.
     */
    private static final Color[] COLORS = { ColorHelper.html2color("#0404B4"), ColorHelper.html2color("#5F04B4"), ColorHelper.html2color("#8A0886"), ColorHelper.html2color("#045FB4"), ColorHelper.html2color("#04B4AE"), ColorHelper.html2color("#045FB4"), ColorHelper.html2color("#04B45F") };

    /**
     * The index into {@link #COLORS} that was last used.
     * Used by {@link #getNextDefaultColor()}
     */
    private static int lastUsedColor = -1;

    /**
     * The color to use for the next track when
     * cycling through the tracks.
     * @return a color from {@link #COLORS}
     */
    private static Color getNextDefaultColor() {
        return COLORS[++lastUsedColor % COLORS.length];
    }

    /**
     * Properties per Track.
     * The only used property at the moment is "color".
     */
    private HashMap<GpsTrack, Properties> props = new HashMap<GpsTrack, Properties>();

    /**
     * Default property values.
     */
    private Properties defaultProperties = new Properties();

    {
        defaultProperties.put("color", ColorHelper.html2color("#0404B4"));
        defaultProperties.put("visible", true);
    }

    /**
     * Adds given track to the tracks storage.
     * @param aTrack the track
     */
    public void add(final GpsTrack aTrack) {
        addElement(aTrack);
        Properties p = new Properties();
        p.put("color", getNextDefaultColor());
        props.put(aTrack, p);
        notifyAddTrack(aTrack);
    }

    /**
     * Remove given track from  storage.
     * @param aTrack the track
     */
    public void remove(final GpsTrack aTrack) {
        props.remove(aTrack);
        removeElement(aTrack);
        notifyRemoveTrack(aTrack);
    }

    /**
     * Get the track at the given index.
     * @see #getSize()
     * @param index the index of the track
     * @return the track
     */
    public GpsTrack getTrack(final int index) {
        return (GpsTrack) getElementAt(index);
    }

    /**
     * Return given property of given track.
     * If properties for given track if not founded,
     * return default property value.
     * <i>Warning: return is type unsafe, can return null</i>
     * @param gpsTrack the track.
     * @param property property name
     * @return Object associated with given property name, if property not founded return null.
     */
    public Object getProperty(final GpsTrack gpsTrack, final String property) {
        if (props.containsKey(gpsTrack) && props.get(gpsTrack).containsKey(property)) {
            return props.get(gpsTrack).get(property);
        }
        if (defaultProperties.containsKey(property)) {
            return defaultProperties.get(property);
        }
        return null;
    }

    /**
     * Notify our listeners about an added track.
     * @param track the added track
     */
    private void notifyAddTrack(final GpsTrack track) {
        for (ITracksChangeListener listener : myListeners) {
            listener.updateTrack(TrackEvent.ADD, track);
        }
    }

    /**
     * Notify our listeners about a removed track.
     * @param track the removed track
     */
    private void notifyRemoveTrack(final GpsTrack track) {
        for (ITracksChangeListener listener : myListeners) {
            listener.updateTrack(TrackEvent.REMOVE, track);
        }
    }

    /**
     * Load gps track from selected .gpx or .nmea file to storage.
     * @param selectedFile the GPX or NMEA track file.
     */
    public void addTrackFile(final File selectedFile) {
        BaseGpsProvider fileProvider = null;
        if (selectedFile.getName().toUpperCase().endsWith(".GPX")) {
            fileProvider = new GPXFileProvider(selectedFile);
        }
        if (selectedFile.getName().toUpperCase().endsWith(".NMEA")) {
            fileProvider = new NMEAFileProvider(selectedFile);
        }
        if (fileProvider != null) {
            GpsTrack gpsTrack = new GpsTrack();
            fileProvider.addGPSListener(gpsTrack);
            gpsTrack.setName(selectedFile.getName());
            add(gpsTrack);
        }
    }

    /**
     * Add a listener to be informed about added, removed and updated tracks.
     * @param listener the listener to add
     */
    public void addTracksChangeListener(final ITracksChangeListener listener) {
        this.myListeners.add(listener);
    }

    /**
     * Remove a listener of added, removed and updated tracks.
     * @param listener the listener to be removed
     */
    public void removeTracksChangeListener(final ITracksChangeListener listener) {
        this.myListeners.remove(listener);
    }
}
