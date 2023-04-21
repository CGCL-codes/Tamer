package com.rolfje.speedforge.soapclient;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import com.gforgegroup.Project;
import com.gforgegroup.Tracker;
import com.gforgegroup.TrackerItem;

/**
 * Extends the GForgeDAO to implement very simplistic "caching", preventing
 * repetitive calls to GForge when fetching identical items. See
 * {@link GForgeDAO#getInstance()} to see wether this class is actually used or
 * not.
 * 
 * @author rolf
 * 
 */
public class SimpleCachedGForgeDAO extends GForgeDAO {

    private Map<Integer, Project> simpleProjectCache = new HashMap<Integer, Project>();

    private Map<Integer, Tracker> simpleTrackerCache = new HashMap<Integer, Tracker>();

    private Map<Integer, TrackerItem> simpleTrackerItemCache = new HashMap<Integer, TrackerItem>();

    private Map<String, URL> simpleLinkCache = new HashMap<String, URL>();

    public Project getProject(int projectId) {
        if (simpleProjectCache.containsKey(Integer.valueOf(projectId))) {
            return simpleProjectCache.get(Integer.valueOf(projectId));
        }
        Project p = super.getProject(projectId);
        simpleProjectCache.put(Integer.valueOf(projectId), p);
        return p;
    }

    public Tracker getTracker(int trackerId) {
        if (simpleTrackerCache.containsKey(Integer.valueOf(trackerId))) {
            return simpleTrackerCache.get(Integer.valueOf(trackerId));
        }
        Tracker t = super.getTracker(trackerId);
        simpleTrackerCache.put(Integer.valueOf(trackerId), t);
        return t;
    }

    public TrackerItem getTrackerItem(int trackerItemId) {
        if (simpleTrackerItemCache.containsKey(Integer.valueOf(trackerItemId))) {
            return simpleTrackerItemCache.get(Integer.valueOf(trackerItemId));
        }
        TrackerItem i = super.getTrackerItem(trackerItemId);
        simpleTrackerItemCache.put(Integer.valueOf(trackerItemId), i);
        return i;
    }

    @Override
    public URL createLink(TrackerItem trackerItem) {
        int trackerItemId = trackerItem.getTracker_item_id();
        int trackerId = trackerItem.getTracker_id();
        String key = "TrackerItem-" + trackerId + "-" + trackerItemId;
        if (simpleLinkCache.containsKey(key)) {
            return simpleLinkCache.get(key);
        }
        URL trackerItemURL = super.createLink(trackerItem);
        simpleLinkCache.put(key, trackerItemURL);
        return trackerItemURL;
    }
}
