package org.matsim.utils.vis.otfivs.data;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.matsim.utils.vis.otfivs.caching.SceneGraph;
import org.matsim.utils.vis.otfivs.caching.SceneLayer;
import org.matsim.utils.vis.otfivs.interfaces.OTFDataReader;

public class OTFConnectionManager implements Cloneable {

    private final Logger log = Logger.getLogger(OTFConnectionManager.class);

    private boolean isValidated = false;

    public static class Entry {

        Class from, to;

        public Entry(Class from, Class to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            return "(" + from.toString() + "," + to.toString() + ") ";
        }
    }

    private final List<Entry> connections = new LinkedList<Entry>();

    @Override
    public OTFConnectionManager clone() {
        OTFConnectionManager clone = new OTFConnectionManager();
        Iterator<Entry> iter = connections.iterator();
        while (iter.hasNext()) {
            Entry entry = iter.next();
            clone.add(entry.from, entry.to);
        }
        return clone;
    }

    public void validate() {
        isValidated = true;
        for (Entry entry : connections) {
            if (OTFDataWriter.class.isAssignableFrom(entry.from)) {
                Collection<Class> readerClasses = this.getEntries(entry.from);
                int count = readerClasses.size();
                if (count != 1) {
                    if (count > 1) log.fatal("For Writer class" + entry.from.getCanonicalName() + " there is more than ONE reader class defined"); else log.fatal("For Writer class" + entry.from.getCanonicalName() + " there is NO reader class defined");
                    throw new RuntimeException();
                }
            }
        }
    }

    public void add(Entry entry) {
        remove(entry.from, entry.to);
        connections.add(entry);
    }

    public void add(Class from, Class to) {
        add(new Entry(from, to));
    }

    public void remove(Class from, Class to) {
        Iterator<Entry> iter = connections.iterator();
        while (iter.hasNext()) {
            Entry entry = iter.next();
            if (entry.from == from && entry.to == to) iter.remove();
        }
    }

    public void remove(Class from) {
        Iterator<Entry> iter = connections.iterator();
        while (iter.hasNext()) {
            Entry entry = iter.next();
            if (entry.from == from) iter.remove();
        }
    }

    public Collection<Class> getEntries(Class srcClass) {
        if (!isValidated) validate();
        List<Class> classList = new LinkedList<Class>();
        for (Entry entry : connections) {
            if (entry.from.equals(srcClass)) classList.add(entry.to);
        }
        return classList;
    }

    public Collection<OTFData.Receiver> getReceivers(Class srcClass, SceneGraph graph) {
        Collection<Class> classList = getEntries(srcClass);
        List<OTFData.Receiver> receiverList = new LinkedList<OTFData.Receiver>();
        for (Class entry : classList) {
            try {
                receiverList.add((OTFData.Receiver) (graph.newInstance(entry)));
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return receiverList;
    }

    private Class handleClassAdoption(Class entryClass, String fileFormat) {
        Class newReader = null;
        if (OTFDataReader.class.isAssignableFrom(entryClass)) {
            String ident = entryClass.getCanonicalName() + fileFormat;
            if (OTFDataReader.previousVersions.containsKey(ident)) {
                newReader = OTFDataReader.previousVersions.get(ident);
            }
        }
        return newReader;
    }

    public void adoptFileFormat(String fileFormat) {
        ListIterator<Entry> iter = connections.listIterator();
        while (iter.hasNext()) {
            Entry entry = iter.next();
            try {
                Object o = entry.to.newInstance();
                Object p = entry.from.newInstance();
            } catch (InstantiationException e) {
                log.warn("For Writer class" + entry.from.getCanonicalName() + "or " + entry.to.getCanonicalName() + " instance could not be generated");
            } catch (IllegalAccessException e) {
                log.warn("For Writer class" + entry.from.getCanonicalName() + "or " + entry.to.getCanonicalName() + " instance could not be accessed");
            }
            Class newReader = handleClassAdoption(entry.to, fileFormat);
            if (newReader != null) iter.set(new Entry(entry.from, newReader));
            newReader = handleClassAdoption(entry.from, fileFormat);
            if (newReader != null) iter.set(new Entry(newReader, entry.to));
        }
        System.out.println(OTFDataReader.previousVersions);
        System.out.println(connections);
    }

    public void fillLayerMap(Map<Class, SceneLayer> layers) {
        Iterator<Entry> iter = connections.iterator();
        while (iter.hasNext()) {
            Entry entry = iter.next();
            if (SceneLayer.class.isAssignableFrom(entry.to)) try {
                layers.put(entry.from, (SceneLayer) (entry.to.newInstance()));
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
