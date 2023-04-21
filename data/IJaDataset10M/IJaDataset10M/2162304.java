package ar.com.fluxit.packageExplorerAddOns.listeners;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import ar.com.fluxit.packageExplorerAddOns.dialogs.PackageExplorerFinderDialog;
import ar.com.fluxit.packageExplorerAddOns.helpers.ProjectRevealer;

/**
 * Tree listener
 * 
 * @author Juan Barisich (<a href="mailto:juan.barisich@gmail.com">juan.barisich@gmail.com</a>)
 */
public class TreeListener implements IDoubleClickListener, ISelectionChangedListener {

    private final PackageExplorerFinderDialog dialog;

    public TreeListener(PackageExplorerFinderDialog dialog) {
        this.dialog = dialog;
    }

    public void doubleClick(DoubleClickEvent event) {
        final Object selected = ((TreeSelection) event.getSelection()).getFirstElement();
        if (isOpenable(selected)) {
            ProjectRevealer.getInstance().selectAndReveal(selected);
            dialog.close();
        }
    }

    private boolean isOpenable(final Object selected) {
        return selected instanceof IProject || selected instanceof IJavaProject;
    }

    @Override
    public void selectionChanged(SelectionChangedEvent paramSelectionChangedEvent) {
        final Object selected = ((TreeSelection) paramSelectionChangedEvent.getSelection()).getFirstElement();
        dialog.setOkButtonEnable(isOpenable(selected));
    }
}
