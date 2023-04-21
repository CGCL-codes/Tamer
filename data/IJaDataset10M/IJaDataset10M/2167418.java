package com.ivis.xprocess.ui.processdesigner.diagram;

import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.core.runtime.IStatus;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.ui.UIPlugin;
import com.ivis.xprocess.ui.processdesigner.diagram.model.Model;
import com.ivis.xprocess.ui.processdesigner.diagram.model.ModelBuilder;
import com.ivis.xprocess.ui.processdesigner.util.Util;

public class ModelProvider {

    private String myRootUuid;

    private Collection<RootChangeListener> myListeners = new ArrayList<RootChangeListener>(1);

    private Model myModel;

    public void setRootElementUuid(String uuid) {
        if (Util.isDeleted(uuid)) {
            throw new IllegalArgumentException("element deleted: " + uuid);
        }
        myRootUuid = uuid;
        rebuildModel();
        fireRootChanged();
    }

    public void rebuildModel() {
        Xelement rootXElement = Util.getElement(myRootUuid);
        if (rootXElement != null) {
            myModel = ModelBuilder.build(rootXElement);
        }
    }

    public Model getModel() {
        return myModel;
    }

    public String getRootElementUuid() {
        return myRootUuid;
    }

    public void save() {
    }

    public void addRootChangeListener(RootChangeListener listener) {
        if (myListeners.contains(listener)) {
            throw new IllegalArgumentException("listener already exists: " + listener);
        } else {
            myListeners.add(listener);
        }
    }

    public void removeRootChangeListener(RootChangeListener listener) {
        if (!myListeners.contains(listener)) {
            throw new IllegalArgumentException("listener doesn't exist: " + listener);
        } else {
            myListeners.remove(listener);
        }
    }

    private void fireRootChanged() {
        for (RootChangeListener listener : myListeners) {
            try {
                listener.rootChanged(myRootUuid);
            } catch (Exception e) {
                UIPlugin.log("", IStatus.ERROR, e);
            }
        }
    }

    public interface RootChangeListener {

        void rootChanged(String newRootUuid);
    }
}
