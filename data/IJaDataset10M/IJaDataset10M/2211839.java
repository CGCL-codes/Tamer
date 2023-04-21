package de.guhsoft.jinto.core.editor;

import java.util.LinkedList;
import java.util.List;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.ide.IGotoMarker;
import de.guhsoft.jinto.core.JIntoCorePlugin;
import de.guhsoft.jinto.core.model.ResourceBundleModel;
import de.guhsoft.jinto.core.model.ResourceRow;

/**
 * This class selects the rows which a problem marker contents. It is used when
 * a user clicks on a marker in the problems view(the gotoMarker method is
 * called).
 */
public class GoToResource implements IGotoMarker {

    private ResourceBundleModel fModel;

    private ISelectionProvider fSelectionProvider;

    /**
	 * The constructor
	 */
    public GoToResource(ResourceBundleModel model, ISelectionProvider selectionProvider) {
        this.fModel = model;
        this.fSelectionProvider = selectionProvider;
    }

    /**
	 * This method will be allocated when a user clicks on a marker in the
	 * problems view.
	 * 
	 * @see org.eclipse.ui.ide.IGotoMarker#gotoMarker(org.eclipse.core.resources.IMarker)
	 */
    public void gotoMarker(IMarker marker) {
        try {
            String[] keys = JIntoMarker.getRows(marker);
            if (keys != null && keys.length > 0) {
                List<ResourceRow> elements = new LinkedList<ResourceRow>();
                for (int i = 0; i < keys.length; i++) {
                    ResourceRow row = this.fModel.getRow(keys[i]);
                    if (row != null) {
                        elements.add(row);
                    }
                }
                this.fSelectionProvider.setSelection(new StructuredSelection(elements));
            }
        } catch (CoreException e) {
            String pluginID = JIntoCorePlugin.getID();
            String message = "Exception while getting the rows for the marker.";
            IStatus status = new Status(IStatus.ERROR, pluginID, IStatus.ERROR, message, new Throwable());
            JIntoCorePlugin.getDefault().getLog().log(status);
        }
    }
}
