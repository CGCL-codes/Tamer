package com.ivis.xprocess.ui.diagram.model;

import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.ui.services.IDisposable;
import com.ivis.xprocess.ui.UIPlugin;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;

public abstract class ModelProvider implements IDisposable {

    private Object myInput;

    private Collection<ModelChangeListener> myListeners = new ArrayList<ModelChangeListener>(1);

    private DiagModel myModel;

    public void setInput(Object input, boolean forceRebuild) {
        boolean rebuild = (myInput != null) ? (!myInput.equals(input)) : (input != null);
        myInput = input;
        if (rebuild || forceRebuild) {
            rebuildModel();
            fireModelChanged();
        }
    }

    public void dispose() {
    }

    public DiagModel getModel() {
        return myModel;
    }

    public Object getInput() {
        return myInput;
    }

    public void addModelChangeListener(ModelChangeListener listener) {
        assert !myListeners.contains(listener) : "Listener already exists: " + listener;
        myListeners.add(listener);
    }

    public void removeModelChangeListener(ModelChangeListener listener) {
        assert myListeners.contains(listener) : "Listener doesn't exists: " + listener;
        myListeners.remove(listener);
    }

    public void rebuildModel() {
        if (myInput == null) {
            myModel = DiagModel.EMPTY;
        } else {
            myModel = buildModel(myInput, myModel);
        }
    }

    public String getRootUuid() {
        DiagNode root = getModel().getRoot();
        if (root != null) {
            IElementWrapper wrapper = root.getElementWrapper();
            if ((wrapper != null) && !wrapper.isDeleted() && !wrapper.isGhost()) {
                return wrapper.getUuid();
            }
        }
        return null;
    }

    protected void fireModelChanged() {
        for (ModelChangeListener listener : myListeners) {
            try {
                listener.modelChanged(myInput);
            } catch (Exception e) {
                UIPlugin.log(e);
            }
        }
    }

    protected abstract DiagModel buildModel(Object input, DiagModel currentModel);

    public interface ModelChangeListener {

        void modelChanged(Object input);
    }
}
