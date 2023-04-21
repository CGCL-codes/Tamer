package org.columba.core.scripting.extensions;

import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
  @author Celso Pinto (cpinto@yimports.com)
 */
public class ExtensionPointManager {

    private Map extensionPoints = null;

    private static ExtensionPointManager self = null;

    private ExtensionPointManager() {
        extensionPoints = new TreeMap();
        initDefaultExtensionPoints();
    }

    private void initDefaultExtensionPoints() {
        addExtensionPoint(new MenuExtensionPoint());
        addExtensionPoint(new ToolbarExtensionPoint());
    }

    public static ExtensionPointManager getInstance() {
        if (self == null) self = new ExtensionPointManager();
        return self;
    }

    public void addExtensionPoint(AbstractExtensionPoint point) {
        extensionPoints.put(point.getId(), point);
    }

    public void removeExtensionPoint(String id) {
        extensionPoints.remove(id);
    }

    public AbstractExtensionPoint getExtensionPoint(String id) {
        return (AbstractExtensionPoint) extensionPoints.get(id);
    }

    public Enumeration enumExtensionPoints() {
        return (new Vector(extensionPoints.values())).elements();
    }
}
