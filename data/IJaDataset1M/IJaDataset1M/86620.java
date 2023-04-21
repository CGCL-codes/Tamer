package at.ac.ait.enviro.dsobscache.store.container;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author BonitzA
 */
public class TimeSeriesContainer implements Serializable, Iterable<SlotContainer> {

    private Long timeSeriesID;

    private Map<String, SearchableProptery> properties;

    private Set<SlotContainer> slots;

    public TimeSeriesContainer() {
        properties = new TreeMap<String, SearchableProptery>();
        slots = new HashSet<SlotContainer>();
    }

    public TimeSeriesContainer(final Map<String, SearchableProptery> properties, final Set<SlotContainer> slots) {
        this.properties = properties;
        this.slots = slots;
    }

    public Long getTimeSeriesID() {
        return timeSeriesID;
    }

    protected void setTimeSeriesID(Long timeSeriesID) {
        this.timeSeriesID = timeSeriesID;
    }

    public Map<String, SearchableProptery> getProperties() {
        return properties;
    }

    public Map<String, Object> getNormaizedProperties() {
        final Map<String, Object> tsProperties = new TreeMap<String, Object>();
        for (Map.Entry<String, SearchableProptery> entry : properties.entrySet()) {
            tsProperties.put(entry.getKey(), entry.getValue().getValue());
        }
        return tsProperties;
    }

    protected void setProperties(final Map<String, SearchableProptery> properties) {
        this.properties = properties;
    }

    public Object addTSProperty(final String key, final Serializable value) {
        return properties.put(key, new SearchableProptery(value, String.valueOf(value)));
    }

    public Object addTSProperty(final String key, final SearchableProptery value) {
        return properties.put(key, value);
    }

    public Object getTSProperty(final String key) {
        return properties.get(key);
    }

    public Set<SlotContainer> getSlots() {
        return slots;
    }

    public void setSlots(Set<SlotContainer> slots) {
        this.slots = slots;
    }

    public boolean removeSlot(SlotContainer slot) {
        return slots.remove(slot);
    }

    public void removeSlot(Date timeStamp) {
        for (final SlotContainer sc : getSlots()) {
            if (sc.getTimeStamp().equals(timeStamp)) {
                removeSlot(sc);
            }
        }
    }

    public SlotContainer getSlot(Date timeStamp) {
        SlotContainer container = null;
        for (final SlotContainer sc : getSlots()) {
            if (sc.getTimeStamp().equals(timeStamp)) {
                container = sc;
                break;
            }
        }
        return container;
    }

    public boolean addSlot(SlotContainer e) {
        return slots.add(e);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TS '" + timeSeriesID + "' ");
        sb.append("{SLOTS='").append(slots).append("', ");
        sb.append("PROPERTIES='").append(properties).append("'}");
        return sb.toString();
    }

    @Override
    public Iterator<SlotContainer> iterator() {
        return this.slots.iterator();
    }
}
