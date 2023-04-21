package tuwien.auto.calimero.datapoint;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.exception.KNXIllegalArgumentException;
import tuwien.auto.calimero.xml.Element;
import tuwien.auto.calimero.xml.KNXMLException;
import tuwien.auto.calimero.xml.XMLReader;
import tuwien.auto.calimero.xml.XMLWriter;

/**
 * A datapoint model storing datapoints with no defined order or hierarchy using a map
 * implementation.
 * <p>
 * 
 * @author B. Malinowsky
 */
public class DatapointMap implements DatapointModel {

    private static final String TAG_DATAPOINTS = "datapoints";

    private final Map points;

    /**
	 * Creates a new empty datapoint map.
	 * <p>
	 */
    public DatapointMap() {
        points = Collections.synchronizedMap(new HashMap(20));
    }

    /**
	 * Creates a new datapoint map and adds all <code>datapoints</code> to the map.
	 * <p>
	 * A datapoint to be added has to be unique according its main address, the attempt to
	 * add two datapoints using the same main address results in a
	 * KNXIllegalArgumentException.
	 * 
	 * @param datapoints collection with entries of type {@link Datapoint}
	 * @throws KNXIllegalArgumentException on duplicate datapoint
	 */
    public DatapointMap(Collection datapoints) {
        final Map m = new HashMap(Math.max(2 * datapoints.size(), 11));
        for (final Iterator i = datapoints.iterator(); i.hasNext(); ) {
            final Datapoint dp = (Datapoint) i.next();
            if (m.containsKey(dp.getMainAddress())) throw new KNXIllegalArgumentException("duplicate datapoint " + dp.getMainAddress());
            m.put(dp.getMainAddress(), dp);
        }
        points = Collections.synchronizedMap(m);
    }

    public void add(Datapoint dp) {
        synchronized (points) {
            if (points.containsKey(dp.getMainAddress())) throw new KNXIllegalArgumentException("duplicate datapoint " + dp.getMainAddress());
            points.put(dp.getMainAddress(), dp);
        }
    }

    public void remove(Datapoint dp) {
        points.remove(dp.getMainAddress());
    }

    public void removeAll() {
        points.clear();
    }

    public Datapoint get(GroupAddress main) {
        return (Datapoint) points.get(main);
    }

    /**
	 * Returns all datapoints currently contained in this map.
	 * <p>
	 * 
	 * @return unmodifiable collection with entries of type {@link Datapoint}
	 */
    public Collection getDatapoints() {
        return Collections.unmodifiableCollection(points.values());
    }

    public boolean contains(GroupAddress main) {
        return points.containsKey(main);
    }

    public boolean contains(Datapoint dp) {
        return points.containsKey(dp.getMainAddress());
    }

    public void load(XMLReader r) throws KNXMLException {
        if (r.getPosition() != XMLReader.START_TAG) r.read();
        final Element e = r.getCurrent();
        if (r.getPosition() != XMLReader.START_TAG || !e.getName().equals(TAG_DATAPOINTS)) throw new KNXMLException(TAG_DATAPOINTS + " element not found", e != null ? e.getName() : null, r.getLineNumber());
        synchronized (points) {
            while (r.read() == XMLReader.START_TAG) {
                final Datapoint dp = Datapoint.create(r);
                if (points.containsKey(dp.getMainAddress())) throw new KNXMLException(TAG_DATAPOINTS + " element contains " + "duplicate datapoint", dp.getMainAddress().toString(), r.getLineNumber());
                points.put(dp.getMainAddress(), dp);
            }
        }
    }

    public void save(XMLWriter w) throws KNXMLException {
        w.writeElement(TAG_DATAPOINTS, Collections.EMPTY_LIST, null);
        synchronized (points) {
            for (final Iterator i = points.values().iterator(); i.hasNext(); ) ((Datapoint) i.next()).save(w);
        }
        w.endElement();
    }
}
