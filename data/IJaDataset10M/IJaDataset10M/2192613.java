package com.rpc.core.utils.ui.editors;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * A simple IEditorInput wrapper for a given object.
 * 
 * @author ted stockwell
 */
public class SimpleEditorInput implements IEditorInput {

    protected String _name;

    protected Object _object;

    public SimpleEditorInput(String name, Object object) {
        _name = name;
        _object = object;
    }

    public SimpleEditorInput(Object object) {
        _name = "";
        _object = object;
    }

    public SimpleEditorInput(String name) {
        this(name, null);
    }

    public SimpleEditorInput() {
        this("");
    }

    public boolean exists() {
        return true;
    }

    public ImageDescriptor getImageDescriptor() {
        return null;
    }

    public String getName() {
        return _name;
    }

    public IPersistableElement getPersistable() {
        return null;
    }

    public String getToolTipText() {
        return _name;
    }

    @SuppressWarnings("unchecked")
    public Object getAdapter(Class adapterClass) {
        Object adapter = null;
        if (_object == null) {
            return null;
        }
        if (_object instanceof IAdaptable) {
            adapter = ((IAdaptable) _object).getAdapter(adapterClass);
        }
        if (adapter == null) {
            adapter = Platform.getAdapterManager().getAdapter(_object, adapterClass);
        }
        if (adapter == null) {
            if (adapterClass.isAssignableFrom(_object.getClass())) {
                adapter = _object;
            }
        }
        return adapter;
    }
}
