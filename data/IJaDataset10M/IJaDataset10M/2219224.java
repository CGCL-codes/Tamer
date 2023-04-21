package jmax.fts;

import java.util.*;
import jmax.mda.*;

/**
 * Represent a property value in the new property system.  It is a
 * MaxDataTreeNode also, so it can be browsed in the patcher tree.
 * This class provide basic empty or abstract methods for all types,
 * that are then implemented by subclasses for each type. The user
 * code use only this class, and it is not aware of subclasses; the
 * goal is to allows property access and modification without casts
 * and without object allocation.  For maximum performance, user code
 * should get the property object from the FtsObject and cache it to
 * access and modify the value.
 * Listeners are handled at the property level; each property support
 * only one listener, to reduce memory overhead. This may change if needed.
 *
 * The set*Property methods are called by the jmax code, and provoke a change in 
 * the server; they also invoke the property listener(s).
 * The serverSet*Property methods are indirectly called by the fts server, and
 * they fire the listeners, but do not send the value to the server.
 * In order to know if the change is originated locally  or by the server (to avoid
 * for example infinite client-server loops), the listener have a flag argument saying
 * if the change is originated by the server.
 */
public abstract class FtsProperty implements MaxDataTreeNode {

    public interface Listener {

        public void propertyChanged(FtsProperty property, boolean server);
    }

    public static final String OBJECT_ERROR = "error";

    public static final String OBJECT_ERROR_DESCRIPTION = "errdesc";

    public static final String OBJECT_FONT = "font";

    public static final String OBJECT_FONT_SIZE = "fs";

    public static final String OBJECT_FONT_STYLE = "fontStyle";

    public static final String OBJECT_WINDOW_FONT = "wfont";

    public static final String OBJECT_WINDOW_FONT_SIZE = "wfs";

    public static final String OBJECT_WINDOW_FONT_STYLE = "wfontStyle";

    public static final String OBJECT_LAYER = "layer";

    public static final String OBJECT_VALUE = "value";

    public static final String OBJECT_X = "x";

    public static final String OBJECT_Y = "y";

    public static final String OBJECT_WIDTH = "w";

    public static final String OBJECT_HEIGHT = "h";

    public static final String WINDOW_X = "wx";

    public static final String WINDOW_Y = "wy";

    public static final String WINDOW_WIDTH = "ww";

    public static final String WINDOW_HEIGHT = "wh";

    public static final String OBJECT_INS = "ins";

    public static final String OBJECT_OUTS = "outs";

    public static final String OBJECT_IMAGE = "image";

    public static final String OBJECT_COMMENT = "comment";

    public static final String OBJECT_COLOR = "color";

    public static final String OBJECT_HAS_DATA_OBJECT = "hasDataObject";

    public static final String OBJECT_DATA_OBJECT = "data";

    public static final String OBJECT_MESSAGE = "message";

    static FtsProperty newPropertyForInt(String name, FtsObject ftsObject) {
        return new FtsIntProperty(name, ftsObject);
    }

    static FtsProperty newPropertyForFloat(String name, FtsObject ftsObject) {
        return new FtsFloatProperty(name, ftsObject);
    }

    static FtsProperty newPropertyForString(String name, FtsObject ftsObject) {
        return new FtsStringProperty(name, ftsObject);
    }

    static FtsProperty newPropertyForObject(String name, FtsObject ftsObject) {
        return new FtsObjectProperty(name, ftsObject);
    }

    protected String name;

    protected FtsObject ftsObject;

    private LinkedList<Listener> listeners = null;

    private boolean isSet = false;

    FtsProperty(String name, FtsObject ftsObject) {
        this.name = name;
        this.ftsObject = ftsObject;
    }

    public void addPropertyListener(Listener listener) {
        if (listeners == null) listeners = new LinkedList<Listener>();
        listeners.add(listener);
    }

    public void removePropertyListener(Listener listener) {
        if (listeners != null) listeners.remove(listener);
    }

    protected void firePropertyChanged(boolean server) {
        if (listeners != null) for (Listener listener : listeners) listener.propertyChanged(this, server);
    }

    public String getName() {
        return name;
    }

    public FtsObject getFtsObject() {
        return ftsObject;
    }

    protected void setSet() {
        isSet = true;
    }

    public boolean isSet() {
        return isSet;
    }

    public boolean isString() {
        return false;
    }

    public void setStringValue(String stringValue, boolean dirty) {
        throw new FtsPropertyError("Setting string value on " + this);
    }

    public void serverSetStringValue(String stringValue) {
        throw new FtsPropertyError("Setting string value on " + this);
    }

    public String getStringValue() {
        throw new FtsPropertyError("Getting string value on " + this);
    }

    public boolean isInt() {
        return false;
    }

    public void setIntValue(int intValue, boolean dirty) {
        throw new FtsPropertyError("Setting int value on " + this);
    }

    public void serverSetIntValue(int intValue) {
        throw new FtsPropertyError("Setting int value on " + this);
    }

    public int getIntValue() {
        throw new FtsPropertyError("Getting int value on " + this);
    }

    public void setBooleanValue(boolean booleanValue, boolean dirty) {
        throw new FtsPropertyError("Setting boolean (int) value on " + this);
    }

    public void serverSetBooleanValue(boolean booleanValue) {
        throw new FtsPropertyError("Setting boolean (int) value on " + this);
    }

    public boolean getBooleanValue() {
        throw new FtsPropertyError("Getting boolean (int) value on " + this);
    }

    public boolean isFloat() {
        return false;
    }

    public void setFloatValue(float floatValue, boolean dirty) {
        throw new FtsPropertyError("Setting float value on " + this);
    }

    public void serverSetFloatValue(float floatValue) {
        throw new FtsPropertyError("Setting float value on " + this);
    }

    public float getFloatValue() {
        throw new FtsPropertyError("Getting float value on " + this);
    }

    public boolean isObject() {
        return false;
    }

    public void setObjectValue(Object objectValue, boolean dirty) {
        throw new FtsPropertyError("Setting object value on " + this);
    }

    public void serverSetObjectValue(Object objectValue) {
        throw new FtsPropertyError("Setting object value on " + this);
    }

    public Object getObjectValue() {
        throw new FtsPropertyError("Getting object value on " + this);
    }

    abstract String treeLabel();

    /*****************************************************************************/
    public String getNodeName() {
        return treeLabel();
    }

    public String getIconURI() {
        return "jmax://core/images/property.gif";
    }

    public int getChildCount() {
        return 0;
    }

    public MaxDataTreeNode getChild(int index) {
        return null;
    }

    public int getIndexOfChild(MaxDataTreeNode child) {
        return -1;
    }

    public boolean isLeaf() {
        return true;
    }

    public MaxDataTreeNode getNodeParent() {
        return ftsObject;
    }

    public void addDataTreeNodeListener(MaxDataTreeNodeListener listener) {
    }

    public void removeDataTreeNodeListener(MaxDataTreeNodeListener listener) {
    }

    public void lazyLoad() {
    }
}
