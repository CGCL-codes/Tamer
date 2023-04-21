package org.skyfree.ghyll.ui.part;

import java.util.ArrayList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

public abstract class SelectionEnableViewPart extends ViewPart implements ISelectionProvider {

    @Override
    public void init(IViewSite site) throws PartInitException {
        super.init(site);
        this.getSite().setSelectionProvider(this);
    }

    private ISelection selectionSource = null;

    /**
	 * The list of selection listeners.
	 */
    protected ArrayList<ISelectionChangedListener> selectionListeners = new ArrayList<ISelectionChangedListener>(1);

    @Override
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        selectionListeners.add(listener);
    }

    @Override
    public ISelection getSelection() {
        return this.selectionSource;
    }

    @Override
    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        selectionListeners.remove(listener);
    }

    @Override
    public void setSelection(ISelection selection) {
        this.selectionSource = selection;
    }

    public void fireSelectionEvent(SelectionChangedEvent event) {
        Object listeners[] = selectionListeners.toArray();
        for (int i = 0; i < selectionListeners.size(); i++) ((ISelectionChangedListener) listeners[i]).selectionChanged(event);
    }
}
