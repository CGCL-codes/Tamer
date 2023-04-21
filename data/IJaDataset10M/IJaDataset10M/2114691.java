package com.android.ide.eclipse.adt.internal.editors.layout;

import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolder;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IFileListener;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IResourceEventListener;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager.IResourceListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Monitor for file changes that could trigger a layout redraw, or a UI update
 */
public final class LayoutReloadMonitor {

    private static final LayoutReloadMonitor sThis = new LayoutReloadMonitor();

    /**
     * Map of listeners by IProject.
     */
    private final Map<IProject, List<ILayoutReloadListener>> mListenerMap = new HashMap<IProject, List<ILayoutReloadListener>>();

    public static final class ChangeFlags {

        public boolean code = false;

        public boolean resources = false;

        public boolean rClass = false;

        public boolean localeList = false;

        boolean isAllTrue() {
            return code && resources && rClass && localeList;
        }
    }

    /**
     * List of projects having received a resource change.
     */
    private final Map<IProject, ChangeFlags> mProjectFlags = new HashMap<IProject, ChangeFlags>();

    /**
     * Classes which implement this interface provide a method to respond to resource changes
     * triggering a layout redraw
     */
    public interface ILayoutReloadListener {

        /**
         * Sent when the layout needs to be redrawn
         * @param flags a {@link ChangeFlags} object indicating what type of resource changed.
         */
        void reloadLayout(ChangeFlags flags);
    }

    /**
     * Returns the single instance of {@link LayoutReloadMonitor}.
     */
    public static LayoutReloadMonitor getMonitor() {
        return sThis;
    }

    private LayoutReloadMonitor() {
        ResourceManager.getInstance().addListener(mResourceListener);
        GlobalProjectMonitor monitor = GlobalProjectMonitor.getMonitor();
        monitor.addFileListener(mFileListener, IResourceDelta.ADDED | IResourceDelta.CHANGED | IResourceDelta.REMOVED);
        monitor.addResourceEventListener(mResourceEventListener);
    }

    /**
     * Adds a listener for a given {@link IProject}.
     * @param project
     * @param listener
     */
    public void addListener(IProject project, ILayoutReloadListener listener) {
        synchronized (mListenerMap) {
            List<ILayoutReloadListener> list = mListenerMap.get(project);
            if (list == null) {
                list = new ArrayList<ILayoutReloadListener>();
                mListenerMap.put(project, list);
            }
            list.add(listener);
        }
    }

    /**
     * Removes a listener for a given {@link IProject}.
     */
    public void removeListener(IProject project, ILayoutReloadListener listener) {
        synchronized (mListenerMap) {
            List<ILayoutReloadListener> list = mListenerMap.get(project);
            if (list != null) {
                list.remove(listener);
            }
        }
    }

    /**
     * Removes a listener, no matter which {@link IProject} it was associated with.
     */
    public void removeListener(ILayoutReloadListener listener) {
        synchronized (mListenerMap) {
            for (List<ILayoutReloadListener> list : mListenerMap.values()) {
                Iterator<ILayoutReloadListener> it = list.iterator();
                while (it.hasNext()) {
                    ILayoutReloadListener i = it.next();
                    if (i == listener) {
                        it.remove();
                    }
                }
            }
        }
    }

    /**
     * Implementation of the {@link IFileListener} as an internal class so that the methods
     * do not appear in the public API of {@link LayoutReloadMonitor}.
     */
    private IFileListener mFileListener = new IFileListener() {

        public void fileChanged(IFile file, IMarkerDelta[] markerDeltas, int kind) {
            IProject project = file.getProject();
            ChangeFlags changeFlags = mProjectFlags.get(project);
            if (changeFlags != null && changeFlags.isAllTrue()) {
                return;
            }
            if (AndroidConstants.EXT_CLASS.equals(file.getFileExtension())) {
                if (file.getName().matches("R[\\$\\.](.*)")) {
                    if (changeFlags == null) {
                        changeFlags = new ChangeFlags();
                        mProjectFlags.put(project, changeFlags);
                    }
                    changeFlags.rClass = true;
                } else {
                    if (changeFlags == null) {
                        changeFlags = new ChangeFlags();
                        mProjectFlags.put(project, changeFlags);
                    }
                    changeFlags.code = true;
                }
            }
        }
    };

    /**
     * Implementation of the {@link IResourceEventListener} as an internal class so that the methods
     * do not appear in the public API of {@link LayoutReloadMonitor}.
     */
    private IResourceEventListener mResourceEventListener = new IResourceEventListener() {

        public void resourceChangeEventStart() {
        }

        public void resourceChangeEventEnd() {
            synchronized (mListenerMap) {
                for (Entry<IProject, ChangeFlags> project : mProjectFlags.entrySet()) {
                    List<ILayoutReloadListener> listeners = mListenerMap.get(project.getKey());
                    ChangeFlags flags = project.getValue();
                    if (listeners != null) {
                        for (ILayoutReloadListener listener : listeners) {
                            listener.reloadLayout(flags);
                        }
                    }
                }
            }
            mProjectFlags.clear();
        }
    };

    /**
     * Implementation of the {@link IResourceListener} as an internal class so that the methods
     * do not appear in the public API of {@link LayoutReloadMonitor}.
     */
    private IResourceListener mResourceListener = new IResourceListener() {

        public void folderChanged(IProject project, ResourceFolder folder, int eventType) {
            ChangeFlags changeFlags = mProjectFlags.get(project);
            if (changeFlags != null && changeFlags.isAllTrue()) {
                return;
            }
            if (changeFlags == null) {
                changeFlags = new ChangeFlags();
                mProjectFlags.put(project, changeFlags);
            }
            changeFlags.localeList = true;
        }

        public void fileChanged(IProject project, ResourceFile file, int eventType) {
            ChangeFlags changeFlags = mProjectFlags.get(project);
            if (changeFlags != null && changeFlags.isAllTrue()) {
                return;
            }
            ResourceType[] resTypes = file.getResourceTypes();
            if (resTypes.length > 0) {
                if (resTypes.length > 1 || resTypes[0] != ResourceType.LAYOUT) {
                    if (changeFlags == null) {
                        changeFlags = new ChangeFlags();
                        mProjectFlags.put(project, changeFlags);
                    }
                    changeFlags.resources = true;
                }
            }
        }
    };
}
