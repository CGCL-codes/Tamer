package net.entelijan.cobean.bind.impl;

import java.util.List;
import net.entelijan.cobean.bind.IModelChangeListener;

interface IModelWrapper {

    public <T> T wrapModel(T model, List<IModelChangeListener> modelChangeListeners);
}
