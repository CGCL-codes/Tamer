package org.ufacekit.ui.viewers.databinding.internal;

import org.eclipse.core.databinding.observable.Realm;
import org.ufacekit.ui.viewers.IViewer;
import org.ufacekit.ui.viewers.databinding.IViewerObservableValue;

/**
 * Observes single selection of a <code>Viewer</code>.
 *
 * @since 1.2
 */
public class ViewerSingleSelectionObservableValue extends SelectionProviderSingleSelectionObservableValue implements IViewerObservableValue {

    @SuppressWarnings("unchecked")
    private IViewer viewer;

    /**
	 * @param realm
	 * @param viewer
	 */
    @SuppressWarnings("unchecked")
    public ViewerSingleSelectionObservableValue(Realm realm, IViewer viewer) {
        super(realm, viewer);
        this.viewer = viewer;
    }

    @SuppressWarnings("unchecked")
    public IViewer getViewer() {
        return viewer;
    }
}
