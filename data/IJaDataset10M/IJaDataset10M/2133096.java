package net.sf.depcon.core.ui;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;

public abstract class AbstractSelectionProvider implements ISelectionProvider {

    @SuppressWarnings("unchecked")
    private List listeners = new ArrayList();

    @SuppressWarnings("unchecked")
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        listeners.add(listener);
    }

    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        listeners.remove(listener);
    }

    public void setSelection(ISelection selection) {
    }
}
