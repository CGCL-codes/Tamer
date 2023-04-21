package be.vds.jtbtaskplanner.client.core.event;

public class LogBookEvent {

    public static final String DIVE_SAVE = "dive.save";

    public static final String DIVE_RELOAD = "dive.reload";

    public static final String DIVE_UPDATE = "dive.update";

    public static final String DIVE_DELETED = "dive.delete";

    public static final String DIVE_MODIFIED = "dive.modified";

    public static final String DIVE_ADDED = "dive.added";

    public static final String SAVE_DIVES = "dives.save";

    public static final String DIVES_DELETED = "dives.delete";

    public static final String CURRENT_DIVE_CHANGED = "current.dive.changed";

    public static final String LOGBOOK_LOADED = "logbook.loaded";

    public static final String LOGBOOK_META_SAVED = "logbook.metadata.saved";

    public static final String LOGBOOK_CLOSED = "logbook.closed";

    public static final String LOGBOOK_DELETED = "logbook.deleted";

    public static final String MATERIAL_SAVED = "material.save";

    public static final String MATERIAL_DELETED = "material.deleted";

    public static final String MATERIAL_MERGED = "material.merged";

    private Object newValue;

    private Object oldValue;

    private String type;

    private DiveModification diveModification;

    public LogBookEvent(String type, Object oldValue, Object newValue) {
        this(type, oldValue, newValue, null);
    }

    public LogBookEvent(String type, Object oldValue, Object newValue, DiveModification diveModification) {
        this.type = type;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.diveModification = diveModification;
    }

    public Object getNewValue() {
        return newValue;
    }

    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public void setOldValue(Object oldValue) {
        this.oldValue = oldValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DiveModification getDiveModification() {
        return diveModification;
    }
}
