package org.eclipse.core.internal.preferences;

import java.io.*;
import java.util.*;
import org.eclipse.core.internal.runtime.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.preferences.*;
import org.eclipse.osgi.util.NLS;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * Represents a node in the Eclipse preference node hierarchy. This class
 * is used as a default implementation/super class for those nodes which
 * belong to scopes which are contributed by the Platform.
 * 
 * Implementation notes:
 * 
 *  - For thread safety, we always synchronize on the node object when writing
 * the children or properties fields.  Must ensure we don't synchronize when calling
 * client code such as listeners.
 * 
 * @since 3.0
 */
public class EclipsePreferences implements IEclipsePreferences, IScope {

    public static final String DEFAULT_PREFERENCES_DIRNAME = ".settings";

    public static final String PREFS_FILE_EXTENSION = "prefs";

    protected static final IEclipsePreferences[] EMPTY_NODE_ARRAY = new IEclipsePreferences[0];

    protected static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static final String FALSE = "false";

    private static final String TRUE = "true";

    protected static final String VERSION_KEY = "eclipse.preferences.version";

    protected static final String VERSION_VALUE = "1";

    protected static final String PATH_SEPARATOR = String.valueOf(IPath.SEPARATOR);

    protected static final String DOUBLE_SLASH = "//";

    protected static final String EMPTY_STRING = "";

    private String cachedPath;

    protected Map children;

    protected boolean dirty = false;

    protected boolean loading = false;

    protected final String name;

    protected final EclipsePreferences parent;

    protected HashMapOfString properties;

    protected boolean removed = false;

    private ListenerList nodeChangeListeners;

    private ListenerList preferenceChangeListeners;

    public EclipsePreferences() {
        this(null, null);
    }

    protected EclipsePreferences(EclipsePreferences parent, String name) {
        super();
        this.parent = parent;
        this.name = name;
    }

    public String absolutePath() {
        if (cachedPath == null) {
            if (parent == null) cachedPath = PATH_SEPARATOR; else {
                String parentPath = parent.absolutePath();
                if (parentPath.length() == 1) cachedPath = parentPath + name(); else cachedPath = parentPath + PATH_SEPARATOR + name();
            }
        }
        return cachedPath;
    }

    public void accept(IPreferenceNodeVisitor visitor) throws BackingStoreException {
        if (!visitor.visit(this)) return;
        IEclipsePreferences[] toVisit = getChildren(true);
        for (int i = 0; i < toVisit.length; i++) toVisit[i].accept(visitor);
    }

    protected synchronized IEclipsePreferences addChild(String childName, IEclipsePreferences child) {
        if (children == null) children = Collections.synchronizedMap(new HashMap());
        children.put(childName, child == null ? (Object) childName : child);
        return child;
    }

    public void addNodeChangeListener(INodeChangeListener listener) {
        checkRemoved();
        if (nodeChangeListeners == null) nodeChangeListeners = new ListenerList();
        nodeChangeListeners.add(listener);
        if (InternalPlatform.DEBUG_PREFERENCE_GENERAL) Policy.debug("Added preference node change listener: " + listener + " to: " + absolutePath());
    }

    public void addPreferenceChangeListener(IPreferenceChangeListener listener) {
        checkRemoved();
        if (preferenceChangeListeners == null) preferenceChangeListeners = new ListenerList();
        preferenceChangeListeners.add(listener);
        if (InternalPlatform.DEBUG_PREFERENCE_GENERAL) Policy.debug("Added preference property change listener: " + listener + " to: " + absolutePath());
    }

    private IEclipsePreferences calculateRoot() {
        IEclipsePreferences result = this;
        while (result.parent() != null) result = (IEclipsePreferences) result.parent();
        return result;
    }

    protected void checkRemoved() {
        if (removed) {
            throw new IllegalStateException(NLS.bind(Messages.preferences_removedNode, name));
        }
    }

    public String[] childrenNames() {
        checkRemoved();
        return internalChildNames();
    }

    protected String[] internalChildNames() {
        Map temp = children;
        if (temp == null || temp.size() == 0) return EMPTY_STRING_ARRAY;
        return (String[]) temp.keySet().toArray(EMPTY_STRING_ARRAY);
    }

    public void clear() {
        checkRemoved();
        HashMapOfString temp = properties;
        if (temp == null) return;
        String[] keys = temp.keys();
        for (int i = 0; i < keys.length; i++) remove(keys[i]);
        synchronized (this) {
            properties = null;
        }
        makeDirty();
    }

    protected String[] computeChildren(IPath root) {
        if (root == null) return EMPTY_STRING_ARRAY;
        IPath dir = root.append(DEFAULT_PREFERENCES_DIRNAME);
        final ArrayList result = new ArrayList();
        final String extension = '.' + PREFS_FILE_EXTENSION;
        File file = dir.toFile();
        File[] totalFiles = file.listFiles();
        if (totalFiles != null) {
            for (int i = 0; i < totalFiles.length; i++) {
                if (totalFiles[i].isFile()) {
                    String filename = totalFiles[i].getName();
                    if (filename.endsWith(extension)) {
                        String shortName = filename.substring(0, filename.length() - extension.length());
                        result.add(shortName);
                    }
                }
            }
        }
        return (String[]) result.toArray(EMPTY_STRING_ARRAY);
    }

    protected IPath computeLocation(IPath root, String qualifier) {
        return root == null ? null : root.append(DEFAULT_PREFERENCES_DIRNAME).append(qualifier).addFileExtension(PREFS_FILE_EXTENSION);
    }

    protected static void convertFromProperties(EclipsePreferences node, Properties table, boolean notify) {
        String version = table.getProperty(VERSION_KEY);
        if (version == null || !VERSION_VALUE.equals(version)) {
        }
        table.remove(VERSION_KEY);
        for (Iterator i = table.keySet().iterator(); i.hasNext(); ) {
            String fullKey = (String) i.next();
            String value = table.getProperty(fullKey);
            if (value != null) {
                String[] splitPath = decodePath(fullKey);
                String path = splitPath[0];
                path = makeRelative(path);
                String key = splitPath[1];
                if (InternalPlatform.DEBUG_PREFERENCE_SET) Policy.debug("Setting preference: " + path + '/' + key + '=' + value);
                EclipsePreferences childNode = (EclipsePreferences) node.internalNode(path, false, null);
                String oldValue = childNode.internalPut(key, value);
                if (notify && !value.equals(oldValue)) node.firePreferenceEvent(key, oldValue, value);
            }
        }
        PreferencesService.getDefault().shareStrings();
    }

    protected Properties convertToProperties(Properties result, String prefix) throws BackingStoreException {
        HashMapOfString temp = properties;
        boolean addSeparator = prefix.length() != 0;
        if (temp != null) {
            synchronized (temp) {
                String[] keys = temp.keys();
                for (int i = 0; i < keys.length; i++) {
                    String value = temp.get(keys[i]);
                    if (value != null) result.put(encodePath(prefix, keys[i]), value);
                }
            }
        }
        IEclipsePreferences[] childNodes = getChildren(true);
        for (int i = 0; i < childNodes.length; i++) {
            EclipsePreferences child = (EclipsePreferences) childNodes[i];
            String fullPath = addSeparator ? prefix + PATH_SEPARATOR + child.name() : child.name();
            child.convertToProperties(result, fullPath);
        }
        PreferencesService.getDefault().shareStrings();
        return result;
    }

    public IEclipsePreferences create(IEclipsePreferences nodeParent, String nodeName) {
        return create((EclipsePreferences) nodeParent, nodeName, null);
    }

    protected boolean isLoading() {
        return loading;
    }

    protected void setLoading(boolean isLoading) {
        loading = isLoading;
    }

    public IEclipsePreferences create(EclipsePreferences nodeParent, String nodeName, Plugin context) {
        EclipsePreferences result = internalCreate(nodeParent, nodeName, context);
        nodeParent.addChild(nodeName, result);
        IEclipsePreferences loadLevel = result.getLoadLevel();
        if (loadLevel == null) return result;
        if (result != loadLevel) return result;
        if (isAlreadyLoaded(result) || result.isLoading()) return result;
        try {
            result.setLoading(true);
            result.loadLegacy();
            result.load();
            result.loaded();
            result.flush();
        } catch (BackingStoreException e) {
            IPath location = result.getLocation();
            String message = NLS.bind(Messages.preferences_loadException, location == null ? EMPTY_STRING : location.toString());
            IStatus status = new Status(IStatus.ERROR, Platform.PI_RUNTIME, IStatus.ERROR, message, e);
            InternalPlatform.getDefault().log(status);
        } finally {
            result.setLoading(false);
        }
        return result;
    }

    public void flush() throws BackingStoreException {
        checkRemoved();
        IEclipsePreferences loadLevel = getLoadLevel();
        if (loadLevel == null) {
            String[] childrenNames = childrenNames();
            for (int i = 0; i < childrenNames.length; i++) node(childrenNames[i]).flush();
            return;
        }
        if (this != loadLevel) {
            loadLevel.flush();
            return;
        }
        if (!dirty) return;
        dirty = false;
        try {
            save();
        } catch (BackingStoreException e) {
            dirty = true;
            throw e;
        }
    }

    public String get(String key, String defaultValue) {
        String value = internalGet(key);
        return value == null ? defaultValue : value;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = internalGet(key);
        return value == null ? defaultValue : TRUE.equalsIgnoreCase(value);
    }

    public byte[] getByteArray(String key, byte[] defaultValue) {
        String value = internalGet(key);
        return value == null ? defaultValue : Base64.decode(value.getBytes());
    }

    protected synchronized boolean childExists(String childName) {
        if (children == null) return false;
        return children.get(childName) != null;
    }

    /**
	 * Thread safe way to obtain a child for a given key. Returns the child
	 * that matches the given key, or null if there is no matching child.
	 */
    protected IEclipsePreferences getChild(String key, Plugin context, boolean create) {
        synchronized (this) {
            if (children == null) return null;
            Object value = children.get(key);
            if (value == null) return null;
            if (value instanceof IEclipsePreferences) return (IEclipsePreferences) value;
            if (!create) return null;
        }
        return addChild(key, create(this, key, context));
    }

    /**
	 * Thread safe way to obtain all children of this node. Never returns null.
	 */
    protected IEclipsePreferences[] getChildren(boolean create) {
        ArrayList result = new ArrayList();
        String[] names = internalChildNames();
        for (int i = 0; i < names.length; i++) {
            IEclipsePreferences child = getChild(names[i], null, create);
            if (child != null) result.add(child);
        }
        return (IEclipsePreferences[]) result.toArray(EMPTY_NODE_ARRAY);
    }

    public double getDouble(String key, double defaultValue) {
        String value = internalGet(key);
        double result = defaultValue;
        if (value != null) try {
            result = Double.parseDouble(value);
        } catch (NumberFormatException e) {
        }
        return result;
    }

    public float getFloat(String key, float defaultValue) {
        String value = internalGet(key);
        float result = defaultValue;
        if (value != null) try {
            result = Float.parseFloat(value);
        } catch (NumberFormatException e) {
        }
        return result;
    }

    public int getInt(String key, int defaultValue) {
        String value = internalGet(key);
        int result = defaultValue;
        if (value != null) try {
            result = Integer.parseInt(value);
        } catch (NumberFormatException e) {
        }
        return result;
    }

    protected IEclipsePreferences getLoadLevel() {
        return null;
    }

    protected IPath getLocation() {
        return null;
    }

    public long getLong(String key, long defaultValue) {
        String value = internalGet(key);
        long result = defaultValue;
        if (value != null) try {
            result = Long.parseLong(value);
        } catch (NumberFormatException e) {
        }
        return result;
    }

    protected EclipsePreferences internalCreate(EclipsePreferences nodeParent, String nodeName, Plugin context) {
        return new EclipsePreferences(nodeParent, nodeName);
    }

    /**
	 * Returns the existing value at the given key, or null if
	 * no such value exists.
	 */
    protected String internalGet(String key) {
        if (key == null) throw new NullPointerException();
        checkRemoved();
        HashMapOfString temp = properties;
        if (temp == null) {
            if (InternalPlatform.DEBUG_PREFERENCE_GET) Policy.debug("Getting preference value: " + absolutePath() + '/' + key + "->null");
            return null;
        }
        String result = temp.get(key);
        if (InternalPlatform.DEBUG_PREFERENCE_GET) Policy.debug("Getting preference value: " + absolutePath() + '/' + key + "->" + result);
        return result;
    }

    /**
	 * Implements the node(String) method, and optionally notifies listeners.
	 */
    protected IEclipsePreferences internalNode(String path, boolean notify, Plugin context) {
        checkRemoved();
        if (path.length() == 0) return this;
        if (path.charAt(0) == IPath.SEPARATOR) return (IEclipsePreferences) calculateRoot().node(path.substring(1));
        int index = path.indexOf(IPath.SEPARATOR);
        String key = index == -1 ? path : path.substring(0, index);
        boolean added = false;
        IEclipsePreferences child = getChild(key, context, true);
        if (child == null) {
            child = create(this, key, context);
            added = true;
        }
        if (added && notify) fireNodeEvent(new NodeChangeEvent(this, child), true);
        return (IEclipsePreferences) child.node(index == -1 ? EMPTY_STRING : path.substring(index + 1));
    }

    /**
	 * Stores the given (key,value) pair, performing lazy initialization of the
	 * properties field if necessary. Returns the old value for the given key,
	 * or null if no value existed.
	 */
    protected synchronized String internalPut(String key, String newValue) {
        checkRemoved();
        if (properties == null) properties = new HashMapOfString();
        String oldValue = properties.get(key);
        if (InternalPlatform.DEBUG_PREFERENCE_SET) Policy.debug("Setting preference: " + absolutePath() + '/' + key + '=' + newValue);
        properties.put(key, newValue);
        return oldValue;
    }

    private void internalRemove(String key, String oldValue) {
        boolean wasRemoved = false;
        synchronized (this) {
            if (properties == null) return;
            wasRemoved = properties.removeKey(key) != null;
            if (properties.size() == 0) properties = null;
            if (wasRemoved) makeDirty();
        }
        if (wasRemoved) firePreferenceEvent(key, oldValue, null);
    }

    protected boolean isAlreadyLoaded(IEclipsePreferences node) {
        return true;
    }

    public String[] keys() {
        checkRemoved();
        HashMapOfString temp = properties;
        if (temp == null || temp.size() == 0) return EMPTY_STRING_ARRAY;
        return temp.keys();
    }

    protected void load() throws BackingStoreException {
        load(getLocation());
    }

    protected static Properties loadProperties(IPath location) throws BackingStoreException {
        if (InternalPlatform.DEBUG_PREFERENCE_GENERAL) Policy.debug("Loading preferences from file: " + location);
        InputStream input = null;
        Properties result = new Properties();
        try {
            input = new BufferedInputStream(new FileInputStream(location.toFile()));
            result.load(input);
        } catch (FileNotFoundException e) {
            if (InternalPlatform.DEBUG_PREFERENCE_GENERAL) Policy.debug("Preference file does not exist: " + location);
            return result;
        } catch (IOException e) {
            String message = NLS.bind(Messages.preferences_loadException, location);
            log(new Status(IStatus.INFO, Platform.PI_RUNTIME, IStatus.INFO, message, e));
            throw new BackingStoreException(message);
        } finally {
            if (input != null) try {
                input.close();
            } catch (IOException e) {
            }
        }
        return result;
    }

    protected void load(IPath location) throws BackingStoreException {
        if (location == null) {
            if (InternalPlatform.DEBUG_PREFERENCE_GENERAL) Policy.debug("Unable to determine location of preference file for node: " + absolutePath());
            return;
        }
        Properties fromDisk = loadProperties(location);
        convertFromProperties(this, fromDisk, false);
    }

    protected void loaded() {
    }

    protected void loadLegacy() {
    }

    public static void log(IStatus status) {
        InternalPlatform.getDefault().log(status);
    }

    protected void makeDirty() {
        EclipsePreferences node = this;
        while (node != null && !node.removed) {
            node.dirty = true;
            node = (EclipsePreferences) node.parent();
        }
    }

    public String name() {
        return name;
    }

    public Preferences node(String pathName) {
        return internalNode(pathName, true, null);
    }

    protected void fireNodeEvent(final NodeChangeEvent event, final boolean added) {
        if (nodeChangeListeners == null) return;
        Object[] listeners = nodeChangeListeners.getListeners();
        for (int i = 0; i < listeners.length; i++) {
            final INodeChangeListener listener = (INodeChangeListener) listeners[i];
            ISafeRunnable job = new ISafeRunnable() {

                public void handleException(Throwable exception) {
                }

                public void run() throws Exception {
                    if (added) listener.added(event); else listener.removed(event);
                }
            };
            Platform.run(job);
        }
    }

    public boolean nodeExists(String path) throws BackingStoreException {
        if (path.length() == 0) return !removed;
        checkRemoved();
        if (path.charAt(0) == IPath.SEPARATOR) return calculateRoot().nodeExists(path.substring(1));
        int index = path.indexOf(IPath.SEPARATOR);
        boolean noSlash = index == -1;
        if (noSlash) return childExists(path);
        String childName = path.substring(0, index);
        if (!childExists(childName)) return false;
        IEclipsePreferences child = getChild(childName, null, true);
        if (child == null) return false;
        return child.nodeExists(path.substring(index + 1));
    }

    public Preferences parent() {
        checkRemoved();
        return parent;
    }

    protected void firePreferenceEvent(String key, Object oldValue, Object newValue) {
        if (preferenceChangeListeners == null) return;
        Object[] listeners = preferenceChangeListeners.getListeners();
        final PreferenceChangeEvent event = new PreferenceChangeEvent(this, key, oldValue, newValue);
        for (int i = 0; i < listeners.length; i++) {
            final IPreferenceChangeListener listener = (IPreferenceChangeListener) listeners[i];
            ISafeRunnable job = new ISafeRunnable() {

                public void handleException(Throwable exception) {
                }

                public void run() throws Exception {
                    listener.preferenceChange(event);
                }
            };
            Platform.run(job);
        }
    }

    public void put(String key, String newValue) {
        if (key == null || newValue == null) throw new NullPointerException();
        String oldValue = internalPut(key, newValue);
        if (!newValue.equals(oldValue)) {
            makeDirty();
            firePreferenceEvent(key, oldValue, newValue);
        }
    }

    public void putBoolean(String key, boolean value) {
        if (key == null) throw new NullPointerException();
        String newValue = value ? TRUE : FALSE;
        String oldValue = internalPut(key, newValue);
        if (!newValue.equals(oldValue)) {
            makeDirty();
            firePreferenceEvent(key, oldValue, newValue);
        }
    }

    public void putByteArray(String key, byte[] value) {
        if (key == null || value == null) throw new NullPointerException();
        String newValue = new String(Base64.encode(value));
        String oldValue = internalPut(key, newValue);
        if (!newValue.equals(oldValue)) {
            makeDirty();
            firePreferenceEvent(key, oldValue, newValue);
        }
    }

    public void putDouble(String key, double value) {
        if (key == null) throw new NullPointerException();
        String newValue = Double.toString(value);
        String oldValue = internalPut(key, newValue);
        if (!newValue.equals(oldValue)) {
            makeDirty();
            firePreferenceEvent(key, oldValue, newValue);
        }
    }

    public void putFloat(String key, float value) {
        if (key == null) throw new NullPointerException();
        String newValue = Float.toString(value);
        String oldValue = internalPut(key, newValue);
        if (!newValue.equals(oldValue)) {
            makeDirty();
            firePreferenceEvent(key, oldValue, newValue);
        }
    }

    public void putInt(String key, int value) {
        if (key == null) throw new NullPointerException();
        String newValue = Integer.toString(value);
        String oldValue = internalPut(key, newValue);
        if (!newValue.equals(oldValue)) {
            makeDirty();
            firePreferenceEvent(key, oldValue, newValue);
        }
    }

    public void putLong(String key, long value) {
        if (key == null) throw new NullPointerException();
        String newValue = Long.toString(value);
        String oldValue = internalPut(key, newValue);
        if (!newValue.equals(oldValue)) {
            makeDirty();
            firePreferenceEvent(key, oldValue, newValue);
        }
    }

    public void remove(String key) {
        String oldValue = internalGet(key);
        if (oldValue != null) internalRemove(key, oldValue);
    }

    public void removeNode() throws BackingStoreException {
        checkRemoved();
        String[] keys = keys();
        for (int i = 0; i < keys.length; i++) remove(keys[i]);
        if (parent != null && !(parent instanceof RootPreferences)) {
            removed = true;
            parent.removeNode(this);
        }
        IEclipsePreferences[] childNodes = getChildren(false);
        for (int i = 0; i < childNodes.length; i++) try {
            childNodes[i].removeNode();
        } catch (IllegalStateException e) {
        }
    }

    protected void removeNode(IEclipsePreferences child) {
        boolean wasRemoved = false;
        synchronized (this) {
            if (children != null) {
                wasRemoved = children.remove(child.name()) != null;
                if (wasRemoved) makeDirty();
                if (children.isEmpty()) children = null;
            }
        }
        if (wasRemoved) fireNodeEvent(new NodeChangeEvent(this, child), false);
    }

    public void removeNodeChangeListener(INodeChangeListener listener) {
        checkRemoved();
        if (nodeChangeListeners == null) return;
        nodeChangeListeners.remove(listener);
        if (nodeChangeListeners.size() == 0) nodeChangeListeners = null;
        if (InternalPlatform.DEBUG_PREFERENCE_GENERAL) Policy.debug("Removed preference node change listener: " + listener + " from: " + absolutePath());
    }

    public void removePreferenceChangeListener(IPreferenceChangeListener listener) {
        checkRemoved();
        if (preferenceChangeListeners == null) return;
        preferenceChangeListeners.remove(listener);
        if (preferenceChangeListeners.size() == 0) preferenceChangeListeners = null;
        if (InternalPlatform.DEBUG_PREFERENCE_GENERAL) Policy.debug("Removed preference property change listener: " + listener + " from: " + absolutePath());
    }

    protected void save() throws BackingStoreException {
        save(getLocation());
    }

    protected void save(IPath location) throws BackingStoreException {
        if (location == null) {
            if (InternalPlatform.DEBUG_PREFERENCE_GENERAL) Policy.debug("Unable to determine location of preference file for node: " + absolutePath());
            return;
        }
        if (InternalPlatform.DEBUG_PREFERENCE_GENERAL) Policy.debug("Saving preferences to file: " + location);
        Properties table = convertToProperties(new Properties(), EMPTY_STRING);
        if (table.isEmpty()) {
            if (location.toFile().exists() && !location.toFile().delete()) {
                String message = NLS.bind(Messages.preferences_failedDelete, location);
                log(new Status(IStatus.WARNING, Platform.PI_RUNTIME, IStatus.WARNING, message, null));
            }
            return;
        }
        table.put(VERSION_KEY, VERSION_VALUE);
        OutputStream output = null;
        FileOutputStream fos = null;
        try {
            File parentFile = location.toFile().getParentFile();
            if (parentFile == null) return;
            parentFile.mkdirs();
            fos = new FileOutputStream(location.toOSString(), false);
            output = new BufferedOutputStream(fos);
            table.store(output, null);
            output.flush();
            fos.getFD().sync();
        } catch (IOException e) {
            String message = NLS.bind(Messages.preferences_saveException, location);
            log(new Status(IStatus.ERROR, Platform.PI_RUNTIME, IStatus.ERROR, message, e));
            throw new BackingStoreException(message);
        } finally {
            if (output != null) try {
                output.close();
            } catch (IOException e) {
            }
        }
    }

    /**
	 * Traverses the preference hierarchy rooted at this node, and adds
	 * all preference key and value strings to the provided pool.  If an added
	 * string was already in the pool, all references will be replaced with the
	 * canonical copy of the string.
	 * 
	 * @param pool The pool to share strings in
	 */
    public void shareStrings(StringPool pool) {
        synchronized (this) {
            HashMapOfString temp = properties;
            if (temp != null) temp.shareStrings(pool);
        }
        IEclipsePreferences[] myChildren = getChildren(false);
        for (int i = 0; i < myChildren.length; i++) if (myChildren[i] instanceof EclipsePreferences) ((EclipsePreferences) myChildren[i]).shareStrings(pool);
    }

    public static String encodePath(String path, String key) {
        String result;
        int pathLength = path == null ? 0 : path.length();
        if (key.indexOf(IPath.SEPARATOR) == -1) {
            if (pathLength == 0) result = key; else result = path + IPath.SEPARATOR + key;
        } else {
            if (pathLength == 0) result = DOUBLE_SLASH + key; else result = path + DOUBLE_SLASH + key;
        }
        return result;
    }

    public static String getSegment(String path, int segment) {
        int start = path.indexOf(IPath.SEPARATOR) == 0 ? 1 : 0;
        int end = path.indexOf(IPath.SEPARATOR, start);
        if (end == path.length() - 1) end = -1;
        for (int i = 0; i < segment; i++) {
            if (end == -1) return null;
            start = end + 1;
            end = path.indexOf(IPath.SEPARATOR, start);
        }
        if (end == -1) end = path.length();
        return path.substring(start, end);
    }

    public static int getSegmentCount(String path) {
        StringTokenizer tokenizer = new StringTokenizer(path, String.valueOf(IPath.SEPARATOR));
        return tokenizer.countTokens();
    }

    public static String makeRelative(String path) {
        String result = path;
        if (path == null) return EMPTY_STRING;
        if (path.length() > 0 && path.charAt(0) == IPath.SEPARATOR) result = path.length() == 0 ? EMPTY_STRING : path.substring(1);
        return result;
    }

    public static String[] decodePath(String fullPath) {
        String key = null;
        String path = null;
        int index = fullPath.indexOf(DOUBLE_SLASH);
        if (index == -1) {
            int lastIndex = fullPath.lastIndexOf(IPath.SEPARATOR);
            if (lastIndex == -1) {
                key = fullPath;
            } else {
                path = fullPath.substring(0, lastIndex);
                key = fullPath.substring(lastIndex + 1);
            }
        } else {
            path = fullPath.substring(0, index);
            key = fullPath.substring(index + 2);
        }
        if (path != null) if (path.length() == 0) path = null; else if (path.charAt(0) == IPath.SEPARATOR) path = path.substring(1);
        return new String[] { path, key };
    }

    public void sync() throws BackingStoreException {
        checkRemoved();
        IEclipsePreferences node = getLoadLevel();
        if (node == null) {
            if (InternalPlatform.DEBUG_PREFERENCE_GENERAL) Policy.debug("Preference node is not a load root: " + absolutePath());
            return;
        }
        if (node instanceof EclipsePreferences) {
            ((EclipsePreferences) node).load();
            node.flush();
        }
    }

    public String toDeepDebugString() {
        final StringBuffer buffer = new StringBuffer();
        IPreferenceNodeVisitor visitor = new IPreferenceNodeVisitor() {

            public boolean visit(IEclipsePreferences node) throws BackingStoreException {
                buffer.append(node);
                buffer.append('\n');
                String[] keys = node.keys();
                for (int i = 0; i < keys.length; i++) {
                    buffer.append(node.absolutePath());
                    buffer.append(PATH_SEPARATOR);
                    buffer.append(keys[i]);
                    buffer.append('=');
                    buffer.append(node.get(keys[i], "*default*"));
                    buffer.append('\n');
                }
                return true;
            }
        };
        try {
            accept(visitor);
        } catch (BackingStoreException e) {
            System.out.println("Exception while calling #toDeepDebugString()");
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public String toString() {
        return absolutePath();
    }
}
