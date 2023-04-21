package org.eclipse.gef.emf;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

/**
 * An adapter implementation that delegates notification handling to an edit
 * part. This class helps to create implementations of edit parts based on an
 * EMF model.
 */
public class EditPartDelegatingAdapter extends AdapterImpl {

    protected final NotifiableEditPart editPart;

    public EditPartDelegatingAdapter(NotifiableEditPart editPart) {
        super();
        this.editPart = editPart;
    }

    public boolean isAdapterForType(Object type) {
        return type.equals(editPart.getModel().getClass());
    }

    @Override
    public void notifyChanged(Notification msg) {
        editPart.notifyChanged(msg);
    }

    public void activate() {
        ((Notifier) editPart.getModel()).eAdapters().add(this);
    }

    public void deactivate() {
        ((Notifier) editPart.getModel()).eAdapters().remove(this);
    }
}
