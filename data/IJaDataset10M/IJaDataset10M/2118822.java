package net.sourceforge.pmd.eclipse.views;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.pmd.eclipse.PMDPlugin;
import net.sourceforge.pmd.eclipse.model.FileRecord;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * Provides the ViolationOutlinePages with Content
 * 
 * @author SebastianRaffel  ( 08.05.2005 )
 */
public class ViolationOutlineContentProvider implements IStructuredContentProvider, IResourceChangeListener {

    protected static final Object[] NO_ELEMENTS = new Object[0];

    private ViolationOutlinePage outlinePage;

    private TableViewer tableViewer;

    private FileRecord resource;

    /**
	 * Constructor
	 * 
	 * @param page
	 */
    public ViolationOutlineContentProvider(ViolationOutlinePage page) {
        outlinePage = page;
        tableViewer = page.getTableViewer();
    }

    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof FileRecord) {
            return ((FileRecord) inputElement).findMarkers();
        }
        return NO_ELEMENTS;
    }

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        if (resource != null) {
            resource.getResource().getWorkspace().removeResourceChangeListener(this);
        }
        resource = (FileRecord) newInput;
        if (resource != null) {
            resource.getResource().getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
        }
        tableViewer = (TableViewer) viewer;
    }

    public void resourceChanged(IResourceChangeEvent event) {
        IMarkerDelta[] markerDeltas = event.findMarkerDeltas(PMDPlugin.PMD_MARKER, true);
        if ((!resource.getResource().exists()) || (resource == null) || (markerDeltas == null)) return;
        final List additions = new ArrayList();
        final List removals = new ArrayList();
        final List changes = new ArrayList();
        for (int i = 0; i < markerDeltas.length; i++) {
            if (!markerDeltas[i].getResource().equals(resource.getResource())) continue;
            IMarker marker = markerDeltas[i].getMarker();
            switch(markerDeltas[i].getKind()) {
                case IResourceDelta.ADDED:
                    additions.add(marker);
                    break;
                case IResourceDelta.REMOVED:
                    removals.add(marker);
                    break;
                case IResourceDelta.CHANGED:
                    changes.add(marker);
                    break;
            }
        }
        tableViewer.getControl().getDisplay().syncExec(new Runnable() {

            public void run() {
                updateViewer(additions, removals, changes);
            }
        });
    }

    /**
     * Applies found updates on the table, 
     * adapted from Philippe Herlin
	 * 
	 * @param additions
	 * @param removals
	 * @param changes
	 */
    protected void updateViewer(List additions, List removals, List changes) {
        if (removals.size() > 0) {
            tableViewer.cancelEditing();
            tableViewer.remove(removals.toArray());
        }
        if (additions.size() > 0) {
            tableViewer.add(additions.toArray());
        }
        if (changes.size() > 0) {
            tableViewer.update(changes.toArray(), null);
        }
        outlinePage.refresh();
    }
}
