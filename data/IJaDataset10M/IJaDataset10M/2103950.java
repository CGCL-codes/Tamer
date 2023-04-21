package org.eclipse.jface.databinding.viewers;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.internal.databinding.internal.viewers.SelectionProviderSingleSelectionObservableValue;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Display;

/**
 * @since 1.1
 * 
 */
public class ViewersObservables {

    /**
	 * @param selectionProvider
	 * @return the observable value tracking the (single) selection of the given
	 *         selection provider
	 */
    public static IObservableValue observeSingleSelection(ISelectionProvider selectionProvider) {
        return new SelectionProviderSingleSelectionObservableValue(SWTObservables.getRealm(Display.getDefault()), selectionProvider);
    }
}
