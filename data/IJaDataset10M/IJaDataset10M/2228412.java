package net.infonode.docking.model;

import java.io.*;
import java.lang.ref.*;
import java.util.*;
import net.infonode.docking.*;
import net.infonode.docking.internal.*;
import net.infonode.docking.properties.*;
import net.infonode.properties.propertymap.*;
import net.infonode.util.*;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.16 $
 */
public abstract class WindowItem {

    public static final DockingWindowProperties emptyProperties = new DockingWindowProperties();

    public abstract WindowItem copy();

    private WindowItem parent;

    private WeakReference connectedWindow = new WeakReference(null);

    private ArrayList windows = new ArrayList();

    private DockingWindowProperties dockingWindowProperties;

    private DockingWindowProperties parentProperties = emptyProperties;

    private Direction lastMinimizedDirection;

    protected WindowItem() {
        dockingWindowProperties = new DockingWindowProperties(emptyProperties);
    }

    protected WindowItem(WindowItem windowItem) {
        dockingWindowProperties = new DockingWindowProperties(windowItem.getDockingWindowProperties().getMap().copy(true, true));
        dockingWindowProperties.getMap().replaceSuperMap(windowItem.getParentDockingWindowProperties().getMap(), emptyProperties.getMap());
        lastMinimizedDirection = windowItem.getLastMinimizedDirection();
    }

    public boolean isRestoreWindow() {
        return parent != null && parent.isRestoreWindow();
    }

    public void addWindow(WindowItem item) {
        addWindow(item, windows.size());
    }

    public void addWindow(WindowItem item, int index) {
        index = index == -1 ? windows.size() : index;
        if (item.parent == this) {
            int currentIndex = windows.indexOf(item);
            if (currentIndex != index) {
                windows.remove(currentIndex);
                windows.add(currentIndex < index ? index - 1 : index, item);
            }
        } else {
            item.setParent(this);
            windows.add(index, item);
        }
    }

    public void removeWindow(WindowItem item) {
        if (windows.remove(item)) item.parent = null;
    }

    public void removeWindowRefs(DockingWindow window) {
        if (connectedWindow.get() == window) connectedWindow = new WeakReference(null);
        for (int i = 0; i < getWindowCount(); i++) getWindow(i).removeWindowRefs(window);
    }

    public void replaceWith(WindowItem item) {
        if (item == this || parent == null) return;
        item.setParent(parent);
        int index = parent.windows.indexOf(this);
        parent.windows.set(index, item);
        parent = null;
    }

    public int getWindowIndex(WindowItem item) {
        return windows.indexOf(item);
    }

    private void setParent(WindowItem parent) {
        if (this.parent == parent) return;
        if (this.parent != null) this.parent.removeWindow(this);
        this.parent = parent;
    }

    public final int getWindowCount() {
        return windows.size();
    }

    public final WindowItem getWindow(int index) {
        return (WindowItem) windows.get(index);
    }

    public WindowItem getParent() {
        return parent;
    }

    public void setConnectedWindow(DockingWindow window) {
        connectedWindow = new WeakReference(window);
    }

    public DockingWindow getConnectedWindow() {
        return (DockingWindow) connectedWindow.get();
    }

    public RootWindowItem getRootItem() {
        return parent == null ? null : parent.getRootItem();
    }

    public DockingWindow getVisibleDockingWindow() {
        DockingWindow window = getConnectedWindow();
        if (window != null && window.getRootWindow() != null && !window.isMinimized() && !window.isUndocked()) return window;
        for (int i = 0; i < getWindowCount(); i++) {
            WindowItem item = getWindow(i);
            window = item.getVisibleDockingWindow();
            if (window != null) return window;
        }
        return null;
    }

    public void removeAll() {
        while (getWindowCount() > 0) removeWindow(getWindow(0));
    }

    public boolean cleanUp() {
        for (int i = getWindowCount() - 1; i >= 0; i--) {
            if (getWindow(i).cleanUp()) windows.remove(i);
        }
        return getWindowCount() == 0 && getConnectedWindow() == null;
    }

    public WindowItem getChildWindowContaining(WindowItem windowItem) {
        while (windowItem.getParent() != this) {
            windowItem = windowItem.getParent();
            if (windowItem == null) return null;
        }
        return windowItem;
    }

    public boolean hasAncestor(WindowItem ancestor) {
        return this == ancestor || (parent != null && parent.hasAncestor(ancestor));
    }

    public WindowItem getTopItem() {
        return parent == null ? this : parent.getTopItem();
    }

    public DockingWindowProperties getDockingWindowProperties() {
        if (dockingWindowProperties == null) {
            dockingWindowProperties = new DockingWindowProperties(emptyProperties);
            parentProperties = emptyProperties;
        }
        return dockingWindowProperties;
    }

    public DockingWindowProperties getParentDockingWindowProperties() {
        return parentProperties == null ? emptyProperties : parentProperties;
    }

    public void setParentDockingWindowProperties(DockingWindowProperties parentProperties) {
        dockingWindowProperties.getMap().replaceSuperMap(this.parentProperties.getMap(), parentProperties.getMap());
        this.parentProperties = parentProperties;
    }

    public Direction getLastMinimizedDirection() {
        return lastMinimizedDirection;
    }

    public void setLastMinimizedDirection(Direction lastMinimizedDirection) {
        this.lastMinimizedDirection = lastMinimizedDirection;
    }

    public void writeSettings(ObjectOutputStream out, WriteContext context) throws IOException {
        out.writeInt(getLastMinimizedDirection() == null ? -1 : getLastMinimizedDirection().getValue());
        if (context.getWritePropertiesEnabled()) {
            dockingWindowProperties.getMap().write(out, true);
            getPropertyObject().write(out, true);
        }
    }

    public void readSettings(ObjectInputStream in, ReadContext context) throws IOException {
        if (context.getVersion() > 1) {
            int dir = in.readInt();
            setLastMinimizedDirection(dir == -1 ? null : Direction.getDirections()[dir]);
        }
        if (context.isPropertyValuesAvailable()) {
            if (context.getReadPropertiesEnabled()) {
                dockingWindowProperties.getMap().read(in);
                getPropertyObject().read(in);
            } else {
                PropertyMapUtil.skipMap(in);
                PropertyMapUtil.skipMap(in);
            }
        }
    }

    public void write(ObjectOutputStream out, WriteContext context, ViewWriter viewWriter) throws IOException {
        out.writeInt(getWindowCount());
        for (int i = 0; i < getWindowCount(); i++) getWindow(i).write(out, context, viewWriter);
        DockingWindow window = getConnectedWindow();
        writeSettings(out, context);
        out.writeBoolean(window != null && !window.isMinimized() && !window.isUndocked() && window.getRootWindow() != null);
    }

    protected PropertyMap getPropertyObject() {
        return null;
    }
}
