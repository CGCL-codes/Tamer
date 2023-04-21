package com.tensegrity.palowebviewer.modules.engine.client;

import java.util.ArrayList;
import java.util.List;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XHelper;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.util.client.Arrays;
import com.tensegrity.palowebviewer.modules.util.client.Logger;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.ITask;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.TaskQueue;

public class PaloServerModelListenerCollection {

    private final List listenerList = new ArrayList();

    private final TaskQueue taskQueue = TaskQueue.getInstance();

    public void addListener(IPaloServerModelListener listener) {
        if (listener == null) throw new IllegalArgumentException("Listener can not be null.");
        listenerList.add(listener);
    }

    public void removeListener(IPaloServerModelListener listener) {
        listenerList.remove(listener);
    }

    public void fireChildArrayChanged(XObject[] path, XObject[] oldChildren, int type) {
        if (path == null) throw new IllegalArgumentException("Path can not be null");
        Logger.debug("fireChildrenArrayChanged(" + Arrays.toString(path) + ", " + Arrays.toString(oldChildren) + ", " + XHelper.typeToString(type) + ")");
        Object[] listeners = listenerList.toArray();
        for (int i = 0; i < listeners.length; i++) {
            IPaloServerModelListener listener = (IPaloServerModelListener) listeners[i];
            listener.onChildArrayChanged(path, oldChildren, type);
        }
    }

    public void fireModelChanged() {
        Object[] listeners = listenerList.toArray();
        for (int i = 0; i < listeners.length; i++) {
            IPaloServerModelListener listener = (IPaloServerModelListener) listeners[i];
            listener.modelChanged();
        }
    }

    public void fireError(Throwable cause) {
        Object[] listeners = listenerList.toArray();
        for (int i = 0; i < listeners.length; i++) {
            IPaloServerModelListener listener = (IPaloServerModelListener) listeners[i];
            listener.onError(cause);
        }
    }

    public void fireUpdateComplete() {
        Object[] listeners = listenerList.toArray();
        for (int i = 0; i < listeners.length; i++) {
            IPaloServerModelListener listener = (IPaloServerModelListener) listeners[i];
            taskQueue.add(new OnUpdateCompleteTask(listener));
        }
    }

    public void fireFavoriteViewsLoaded() {
        Object[] listeners = listenerList.toArray();
        for (int i = 0; i < listeners.length; i++) {
            IPaloServerModelListener listener = (IPaloServerModelListener) listeners[i];
            taskQueue.add(new OnFavoriteViewsLoadedTask(listener));
        }
    }

    public void fireDefaultViewLoaded(XCube cube) {
        Object[] listeners = listenerList.toArray();
        for (int i = 0; i < listeners.length; i++) {
            IPaloServerModelListener listener = (IPaloServerModelListener) listeners[i];
            listener.defaultViewLoaded(cube);
        }
    }

    public void fireObjectRenamed(XObject object) {
        Object[] listeners = listenerList.toArray();
        for (int i = 0; i < listeners.length; i++) {
            IPaloServerModelListener listener = (IPaloServerModelListener) listeners[i];
            listener.objectRenamed(object);
        }
    }

    public void fireObjectInvalide(XPath path) {
        Object[] listeners = listenerList.toArray();
        for (int i = 0; i < listeners.length; i++) {
            IPaloServerModelListener listener = (IPaloServerModelListener) listeners[i];
            listener.objectInvalide(path);
        }
    }
}

final class OnUpdateCompleteTask implements ITask {

    private final IPaloServerModelListener listener;

    public OnUpdateCompleteTask(IPaloServerModelListener listener) {
        this.listener = listener;
    }

    public void execute() {
        listener.onUpdateComplete();
    }

    public String getName() {
        return "OnUpdateCompleteTask";
    }
}

final class OnFavoriteViewsLoadedTask implements ITask {

    private final IPaloServerModelListener listener;

    public OnFavoriteViewsLoadedTask(IPaloServerModelListener listener) {
        this.listener = listener;
    }

    public void execute() {
        listener.onFavoriteViewsLoaded();
    }

    public String getName() {
        return "OnFavoriteViewsLoadedTask";
    }
}
