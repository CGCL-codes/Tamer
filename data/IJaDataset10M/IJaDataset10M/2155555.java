package com.modelmetrics.common.sforce.dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public abstract class AbstractSproxy implements Sproxy {

    private static final SimpleDateFormat FORCE_DATE = new SimpleDateFormat("yyyy-MM-dd");

    private static final SimpleDateFormat FORCE_DATETIME = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'-06:00'");

    protected Map<String, Object> map = new HashMap<String, Object>();

    protected Collection<String> nullFields = new TreeSet<String>();

    protected String type;

    protected String id;

    protected Sproxy parent;

    protected Map<String, Sproxy> children = new HashMap<String, Sproxy>();

    protected boolean dirty;

    public Map<String, Object> getValues() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public String getId() {
        return id;
    }

    public void setId(String salesforceId) {
        this.id = salesforceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String typeName) {
        this.type = typeName;
    }

    /**
	 * requires a little effort to be sure we return data in a format suitable
	 * for Force.com.
	 */
    public String getValue(String key) {
        key = key.toLowerCase();
        String ret = null;
        Object value = this.getValues().get(key);
        if (value instanceof java.sql.Timestamp) {
            ret = FORCE_DATETIME.format(value);
        } else if (value instanceof java.util.Date || value instanceof java.sql.Date) {
            Date dateValue = (Date) value;
            Calendar c = GregorianCalendar.getInstance();
            c.setTime(dateValue);
            if (c.get(Calendar.HOUR) == 0 && c.get(Calendar.MINUTE) == 0 && c.get(Calendar.SECOND) == 0 && c.get(Calendar.MILLISECOND) == 0) {
                ret = FORCE_DATE.format(value);
            } else {
                ret = FORCE_DATETIME.format(value);
            }
        } else if (value instanceof java.lang.Double) {
            ret = ((Double) value).toString();
        } else {
            if (value != null) {
                ret = value.toString();
            }
        }
        return ret;
    }

    public void removeValue(String key) {
        key = key.toLowerCase();
        this.getValues().remove(key);
    }

    public void removeNull(String key) {
        key = key.toLowerCase();
        this.getNullKeys().remove(key);
    }

    public void setNull(String key) {
        key = key.toLowerCase();
        if (this.map.containsKey(key)) {
            this.map.remove(key);
        }
        this.nullFields.add(key);
        this.setDirty(true);
    }

    public void setValue(String key, Object value) {
        key = key.toLowerCase();
        if (key.equalsIgnoreCase("id")) {
            this.setId(value.toString());
        } else {
            if (this.nullFields.contains(key)) {
                this.nullFields.remove(key);
            }
            if (this.getValueKeys().contains(key) && this.getValues().get(key) != null) {
                if (this.getValues().get(key).equals(value)) {
                    return;
                }
            }
            this.getValues().put(key, value);
        }
        this.setDirty(true);
    }

    public Collection<String> getNullKeys() {
        return this.nullFields;
    }

    public Collection<String> getValueKeys() {
        Collection<String> ret = new TreeSet<String>();
        ret.addAll(this.getValues().keySet());
        return ret;
    }

    public Collection<Sproxy> getChildren() {
        return children.values();
    }

    public Sproxy getChild(String key) {
        key = key.toLowerCase();
        return children.get(key);
    }

    public void putChild(String key, Sproxy sproxy) {
        key = key.toLowerCase();
        children.put(key, sproxy);
    }

    public Sproxy getParent() {
        return parent;
    }

    void setParent(Sproxy parent) {
        this.parent = parent;
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    public boolean hasChildren() {
        return this.children.size() > 0;
    }

    public boolean isDirty() {
        return dirty;
    }

    void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean hasNull(String key) {
        return this.getNullKeys().contains(key);
    }

    public boolean hasValue(String key) {
        return this.getValueKeys().contains(key) && this.getValues().get(key) != null;
    }
}
