package net.sf.rcpforms.form.UNSORTED;

import net.sf.rcpforms.form.IRCPFormEditorInput;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

/**
 * Der Typ GenericEditorInput wrappt irgend ein Model als {@link IRCPFormEditorInput},
 * so dass es als Rootmodel in einem Editor verwendet werden kann.
 * <p>
 * 
 * @author Copyright 2009 by The Swiss Post, PostFinance - all rights reserved
 */
public class GenericEditorInput<T> implements IRCPFormEditorInput {

    private T wrappedModel;

    public GenericEditorInput(final T model) {
        wrappedModel = model;
    }

    public Object[] getModels() {
        return new Object[] { wrappedModel };
    }

    public T getModel() {
        return wrappedModel;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof GenericEditorInput)) {
            return false;
        }
        final GenericEditorInput other = (GenericEditorInput) obj;
        return wrappedModel == null && other.wrappedModel == null || wrappedModel.equals(other.wrappedModel);
    }

    @Override
    public int hashCode() {
        return wrappedModel == null ? 0 : wrappedModel.hashCode();
    }

    public boolean exists() {
        return false;
    }

    public ImageDescriptor getImageDescriptor() {
        return null;
    }

    public String getName() {
        return "";
    }

    public IPersistableElement getPersistable() {
        return null;
    }

    public String getToolTipText() {
        return "";
    }

    public Object getAdapter(final Class adapter) {
        return null;
    }
}
