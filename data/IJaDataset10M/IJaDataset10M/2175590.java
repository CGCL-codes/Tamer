package org.tru42.signal.model;

public abstract class ModelChangeAdapter implements IModelChangeListener {

    @Override
    public void processorAdded(ISignalProcessor proc) {
        modelChanged(proc);
    }

    @Override
    public void processorRemoved(ISignalProcessor proc) {
        modelChanged(proc);
    }
}
