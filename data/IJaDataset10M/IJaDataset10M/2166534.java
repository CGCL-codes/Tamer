package genj.geo;

import genj.gedcom.Entity;
import genj.gedcom.Gedcom;
import genj.gedcom.GedcomListener;
import genj.gedcom.Property;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;
import spin.Spin;
import com.vividsolutions.jts.geom.Coordinate;

class GeoModel implements GedcomListener {

    private static final Logger LOG = Logger.getLogger("genj.geo");

    /** state */
    private List<GeoModelListener> listeners = new CopyOnWriteArrayList<GeoModelListener>();

    private Gedcom gedcom;

    private Map<GeoLocation, GeoLocation> locations = new HashMap<GeoLocation, GeoLocation>();

    private Worker worker = new Worker();

    /**
   * Constructor
   */
    public GeoModel() {
        Thread t = new Thread(worker);
        t.setDaemon(true);
        t.start();
    }

    /**
   * Accessor - gedcom
   */
    public Gedcom getGedcom() {
        return gedcom;
    }

    /** 
   * re-resolve all locations
   */
    public void resolveAll() {
        if (gedcom != null) worker.queue(gedcom, locations.keySet(), true);
    }

    /**
   * Accessor - gedcom
   */
    public void setGedcom(Gedcom set) {
        if (gedcom == set) return;
        if (gedcom != null) {
            Collection removed = new ArrayList(locations.keySet());
            for (Iterator it = removed.iterator(); it.hasNext(); ) {
                GeoLocation loc = (GeoLocation) it.next();
                locations.remove(loc);
                fireLocationRemoved(loc);
            }
            gedcom.removeGedcomListener(this);
        }
        gedcom = set;
        if (gedcom != null) {
            for (Iterator it = GeoLocation.parseEntities(gedcom.getEntities()).iterator(); it.hasNext(); ) {
                GeoLocation loc = (GeoLocation) it.next();
                locations.put(loc, loc);
                fireLocationAdded(loc);
            }
            worker.queue(gedcom, locations.keySet(), false);
            gedcom.addGedcomListener(this);
        }
    }

    /**
   * Set a location's coordinates
   */
    public void setCoordinates(GeoLocation loc, Coordinate coord) {
        loc = (GeoLocation) locations.get(loc);
        if (loc != null) {
            loc.setCoordinate(coord);
            GeoService.getInstance().remember(gedcom, loc);
            fireLocationUpdated(loc);
        }
    }

    /**
   * Accessor - locations
   */
    public synchronized int getNumLocations() {
        return locations.size();
    }

    /**
   * Accessor - locations
   */
    public synchronized Collection getLocations() {
        return locations.keySet();
    }

    /**
   * Tell listeners about a found location
   */
    private void fireLocationAdded(GeoLocation location) {
        for (GeoModelListener listener : listeners) listener.locationAdded(location);
    }

    /**
   * Tell listeners about an updated location
   */
    private void fireLocationUpdated(GeoLocation location) {
        for (GeoModelListener listener : listeners) listener.locationUpdated(location);
    }

    /**
   * Tell listeners about a removed location
   */
    private void fireLocationRemoved(GeoLocation location) {
        for (GeoModelListener listener : listeners) listener.locationRemoved(location);
    }

    /**
   * Tell listeners about async resolving going on
   */
    private void fireAsyncResolveStart() {
        for (GeoModelListener listener : listeners) listener.asyncResolveStart();
    }

    /**
   * Tell listeners about async resolving ended
   */
    private void fireAsyncResolveEnd(int status, String msg) {
        for (GeoModelListener listener : listeners) listener.asyncResolveEnd(status, msg);
    }

    /**
   * add a listener
   */
    public void addGeoModelListener(GeoModelListener l) {
        listeners.add((GeoModelListener) Spin.over(l));
    }

    /**
   * remove a listener
   */
    public void removeGeoModelListener(GeoModelListener l) {
        listeners.remove((GeoModelListener) Spin.over(l));
    }

    public void gedcomEntityAdded(Gedcom gedcom, Entity entity) {
        Set added = GeoLocation.parseEntities(Collections.singletonList(entity));
        for (Iterator locs = added.iterator(); locs.hasNext(); ) {
            GeoLocation loc = (GeoLocation) locs.next();
            GeoLocation old = (GeoLocation) locations.get(loc);
            if (old != null) {
                old.add(loc);
                fireLocationUpdated(old);
            } else {
                locations.put(loc, loc);
                fireLocationAdded(loc);
            }
        }
        worker.queue(gedcom, added, true);
    }

    public void gedcomEntityDeleted(Gedcom gedcom, Entity entity) {
        List current = new ArrayList(locations.keySet());
        for (Iterator locs = current.iterator(); locs.hasNext(); ) {
            GeoLocation loc = (GeoLocation) locs.next();
            loc.removeEntity(entity);
            if (loc.getNumProperties() == 0) {
                locations.remove(loc);
                fireLocationRemoved(loc);
            } else {
                fireLocationUpdated(loc);
            }
        }
    }

    public void gedcomPropertyAdded(Gedcom gedcom, Property property, int pos, Property added) {
        gedcomPropertyChanged(gedcom, property);
    }

    public void gedcomPropertyChanged(Gedcom gedcom, Property property) {
        Entity entity = property.getEntity();
        gedcomEntityDeleted(gedcom, entity);
        gedcomEntityAdded(gedcom, entity);
    }

    public void gedcomPropertyDeleted(Gedcom gedcom, Property property, int pos, Property deleted) {
        gedcomPropertyChanged(gedcom, property);
    }

    /**
   * our asynchronous worker
   */
    private class Worker implements Runnable {

        private BlockingDeque<Job> jobs = new LinkedBlockingDeque<Job>();

        public void run() {
            while (true) try {
                Job job = jobs.takeFirst();
                fireAsyncResolveStart();
                GeoServiceException gse = null;
                try {
                    GeoService.getInstance().match(job.gedcom, job.locations, job.matchAll);
                } catch (GeoServiceException ex) {
                    LOG.log(Level.FINE, ex.getMessage(), ex);
                    gse = ex;
                }
                int misses = 0;
                for (GeoLocation location : job.locations) {
                    if (!location.isValid()) misses++;
                    GeoLocation old = (GeoLocation) locations.get(location);
                    if (old != null) fireLocationUpdated(location);
                }
                if (gse != null) fireAsyncResolveEnd(GeoModelListener.ERROR, gse.getMessage()); else fireAsyncResolveEnd(misses > 0 ? GeoModelListener.SOME_MATCHED : GeoModelListener.ALL_MATCHED, "");
            } catch (Throwable t) {
                LOG.log(Level.WARNING, "throwable in GeoModel.Worker", t);
            }
        }

        void queue(Gedcom gedcom, Collection<GeoLocation> todo, boolean matchAll) {
            jobs.push(new Job(gedcom, todo, matchAll));
        }

        private class Job {

            private Gedcom gedcom;

            private Collection<GeoLocation> locations;

            private boolean matchAll;

            private Job(Gedcom gedcom, Collection<GeoLocation> locations, boolean matchAll) {
                this.gedcom = gedcom;
                this.locations = new ArrayList<GeoLocation>(locations);
                this.matchAll = matchAll;
            }
        }
    }
}
