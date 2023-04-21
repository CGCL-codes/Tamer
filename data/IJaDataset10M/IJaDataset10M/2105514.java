package gov.nasa.gsfc.visbard.model;

import gov.nasa.gsfc.visbard.model.ArrowManager;
import gov.nasa.gsfc.visbard.model.ArrowManagerListener;
import gov.nasa.gsfc.visbard.model.CategoryPool;
import gov.nasa.gsfc.visbard.model.CategoryPoolListener;
import gov.nasa.gsfc.visbard.repository.category.Category;
import java.util.HashMap;
import java.util.Vector;
import java.util.Iterator;

class DefaultPropertyContainer implements PropertyContainer, CategoryPoolListener, ArrowManagerListener, SettingsHolder {

    private static final Float A_WIDTH_DEFAULT = new Float(1f);

    private static final Float A_SCALE_DEFAULT = new Float(30f);

    private static final Float DEC_PERCENT_DEFAULT = new Float(1f);

    private static final Integer NUM_GLYPHS_DEFAULT = new Integer(100);

    private static final Integer M_STYLE_DEFAULT = new Integer(Glyph.STYLE_ROMBUS);

    private static final Float M_SCALE_DEFAULT = new Float(1f);

    private static final Boolean STATIC_COLOR_DEFAULT = new Boolean(false);

    private static final Boolean PLOT_ORBIT_DEFAULT = new Boolean(false);

    private static final Float ORBIT_WIDTH_DEFAULT = new Float(1f);

    private static final Float ORBIT_DEC_DEFAULT = new Float(0.4f);

    private Vector fListeners = new Vector();

    private HashMap fProperties = new HashMap();

    private HashMap fSettings = new HashMap();

    private boolean fAllowNulls = true;

    private Object fPropertyLock = new Object();

    static org.apache.log4j.Category sLogger = org.apache.log4j.Category.getInstance(DefaultPropertyContainer.class.getName());

    /**
     * Initialize the property container. If allowNulls is false,
     * setting null values for non category properties is not allowed.
     */
    public DefaultPropertyContainer(boolean allowNulls) {
        VisbardMain.getCategoryPool().addListener(this);
        VisbardMain.getArrowManager().addListener(this);
        fAllowNulls = allowNulls;
        if (!fAllowNulls) {
            this.addGlyphDefaults();
            int numarrows = VisbardMain.getArrowManager().getNumArrows();
            for (int i = 0; i < numarrows; i++) {
                this.addArrowDefaults(i);
            }
        }
    }

    public void setPropertyValue(Property prop, Object val) {
        synchronized (fPropertyLock) {
            if (val == null) {
                fProperties.remove(prop);
                fSettings.remove(prop.toString());
            } else {
                fProperties.put(prop, val);
                fSettings.put(prop.toString(), val.toString());
            }
            this.firePropertyChangedEvent(prop);
        }
    }

    public void setPropertyToEmpty(Property prop) {
        synchronized (fPropertyLock) {
            fProperties.put(prop, null);
            fSettings.put(prop.toString(), null);
            this.firePropertyChangedEvent(prop);
        }
    }

    /**
     * Return the value for a particular visual property
     */
    public Object getPropertyValue(Property prop) {
        synchronized (fPropertyLock) {
            return fProperties.get(prop);
        }
    }

    public static Object stringToVal(Property prop, String val) {
        if (val == null) return null;
        if (prop.getValueType().equals(Property.VAL_TYPE_CATEGORY)) {
            Category cat = VisbardMain.getCategoryPool().getCategory(val);
            return cat;
        } else if (prop.getValueType().equals(Property.VAL_TYPE_FLOAT)) {
            return new Float(Float.parseFloat(val));
        } else if (prop.getValueType().equals(Property.VAL_TYPE_INTEGER)) {
            return new Integer(Integer.parseInt(val));
        } else if (prop.getValueType().equals(Property.VAL_TYPE_BOOLEAN)) {
            return new Boolean(Boolean.valueOf(val).booleanValue());
        }
        sLogger.error("Unknown property value type!");
        return null;
    }

    /**
     * Indicates that a category has been added to the system.
     */
    public void categoryAdded(CategoryPool source, Category cat) {
        Iterator keys = fSettings.keySet().iterator();
        while (keys.hasNext()) {
            String string_prop = (String) keys.next();
            String string_val = (String) fSettings.get(string_prop);
            Property prop = new Property(string_prop);
            Object val = stringToVal(prop, string_val);
            if ((val != null) && (prop.getValueType().equals(Property.VAL_TYPE_CATEGORY))) {
                Category catval = (Category) val;
                if (catval.equals(cat)) {
                    fProperties.put(prop, catval);
                    this.firePropertyChangedEvent(prop);
                }
            }
        }
    }

    /**
     * Indicates that a category has been removed from the system.
     */
    public void categoryRemoved(CategoryPool source, Category cat) {
        Property[] props = new Property[fProperties.keySet().size()];
        fProperties.keySet().toArray(props);
        for (int i = 0; i < props.length; i++) {
            Property key = props[i];
            Object val = fProperties.get(key);
            if ((val != null) && val.equals(cat)) {
                fProperties.remove(key);
                this.firePropertyChangedEvent(key);
            }
        }
    }

    private void setDefault(Property prop, Object deflt) {
        String string_val = (String) fSettings.get(prop.toString());
        Object val = stringToVal(prop, string_val);
        if (val == null) val = deflt;
        this.setPropertyValue(prop, val);
    }

    private void addGlyphDefaults() {
        this.setDefault(Property.sMStyle, M_STYLE_DEFAULT);
        this.setDefault(Property.sMScale, M_SCALE_DEFAULT);
        this.setDefault(Property.sDecPercent, DEC_PERCENT_DEFAULT);
        this.setDefault(Property.sNumGlyphs, NUM_GLYPHS_DEFAULT);
        this.setDefault(Property.sStaticColor, STATIC_COLOR_DEFAULT);
        this.setDefault(Property.sPlotOrbit, PLOT_ORBIT_DEFAULT);
        this.setDefault(Property.sOrbitThickness, ORBIT_WIDTH_DEFAULT);
        this.setDefault(Property.sVertexDec, ORBIT_DEC_DEFAULT);
    }

    private void addArrowDefaults(int idx) {
        Property prop;
        prop = new Property(Property.A_SCALE, idx);
        this.setDefault(prop, A_SCALE_DEFAULT);
        prop = new Property(Property.A_WIDTH, idx);
        this.setDefault(prop, A_WIDTH_DEFAULT);
    }

    /**
     * Indicates that an arrow has been added. (to the end of the arrow list)
     */
    public void arrowAdded(ArrowManager source) {
        this.addArrowDefaults(source.getNumArrows() - 1);
    }

    /**
     * Indicates that an arrow at index idx has been removed. Bump
     * the index of properties down one.
     */
    public void arrowRemoved(ArrowManager source, int idx) {
        for (int j = idx; j <= source.getNumArrows(); j++) {
            Property[] props = new Property[fProperties.keySet().size()];
            fProperties.keySet().toArray(props);
            for (int i = 0; i < props.length; i++) {
                if (props[i].arrowIdx() == j) fProperties.remove(props[i]);
            }
            for (int i = 0; i < props.length; i++) {
                if (props[i].arrowIdx() == j + 1) {
                    Property newProp = new Property(props[i].type(), j);
                    fProperties.put(newProp, fProperties.get(props[i]));
                }
            }
            String[] str_props = new String[fSettings.keySet().size()];
            fSettings.keySet().toArray(str_props);
            for (int i = 0; i < props.length; i++) {
                Property prop = new Property(str_props[i]);
                if (prop.arrowIdx() == j) fSettings.remove(str_props[i]);
            }
            for (int i = 0; i < str_props.length; i++) {
                Property prop = new Property(str_props[i]);
                if (prop.arrowIdx() == j + 1) {
                    Property newProp = new Property(prop.type(), j);
                    fSettings.put(newProp.toString(), fSettings.get(str_props[i]));
                }
            }
        }
    }

    /**
     * Unregisters and cleans up.
     */
    public void dispose() {
        VisbardMain.getCategoryPool().removeListener(this);
        VisbardMain.getArrowManager().removeListener(this);
    }

    /**
     * Returns all the currently existing properties.
     */
    public Property[] getAllProperties() {
        Property arr[] = new Property[fProperties.keySet().size()];
        fProperties.keySet().toArray(arr);
        return arr;
    }

    /**
     * Returns true if this container allows nulls.
     */
    public boolean allowsNulls() {
        return fAllowNulls;
    }

    /**
     * Returns a hashmap of settings (strings) which represent the current
     * object state, and are keyed by string names.
     */
    public HashMap getSettings() {
        HashMap setts = new HashMap(fSettings);
        setts.remove(Property.sTime.toString());
        setts.remove(Property.sLocation.toString());
        return setts;
    }

    /**
     * Changes the settings of this object to match the settings in the
     * specified hashmap. HashMap may be empty, in which case no settings are
     * delivered
     */
    public void setSettings(HashMap settings) {
        int numarrows = VisbardMain.getArrowManager().getNumArrows();
        Iterator keys = settings.keySet().iterator();
        while (keys.hasNext()) {
            String string_prop = (String) keys.next();
            String string_val = (String) settings.get(string_prop);
            Property prop = new Property(string_prop);
            Object val = stringToVal(prop, string_val);
            if (prop.arrowIdx() < numarrows) this.setPropertyValue(prop, val);
            fSettings.put(string_prop, string_val);
        }
    }

    /**
     * Returns true if this container has the property.
     */
    public boolean hasProperty(Property prop) {
        return fProperties.containsKey(prop);
    }

    /**
     * Send a general event to all listeners.
     */
    protected void firePropertyChangedEvent(Property prop) {
        Vector myVec = fListeners;
        int length = myVec.size();
        for (int i = 0; i < length; i++) {
            PropertyContainerListener listener = (PropertyContainerListener) myVec.get(i);
            listener.propertyChanged(this, prop);
        }
    }

    /**
     * Registers a listener.
     */
    public synchronized void addListener(PropertyContainerListener listener) {
        Vector myVec = (Vector) fListeners.clone();
        if (!myVec.contains(listener)) {
            myVec.add(listener);
            fListeners = myVec;
        }
    }

    /**
     * Unregisters a listener.
     */
    public synchronized void removeListener(PropertyContainerListener listener) {
        Vector myVec = (Vector) fListeners.clone();
        myVec.remove(listener);
        fListeners = myVec;
    }

    /**
     * Returns a string which identifies this object uniquely. Any string is
     * valid as long as it does not contain the underscore character.
     */
    public String getHolderID() {
        return "PropertyContainer";
    }
}
