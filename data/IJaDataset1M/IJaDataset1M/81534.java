package org.fluid.commons.ui.swt;

import java.util.Iterator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public abstract class MasterDetailComposite extends SashForm {

    private Viewer masterViewer;

    private Viewer detailViewer;

    public MasterDetailComposite(Composite parent, int style) {
        super(parent, style);
    }

    public void init() {
        masterViewer = createMasterViewer(this);
        detailViewer = createDetailViewer(this);
        masterViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                Object selected = ((IStructuredSelection) event.getSelection()).getFirstElement();
                boolean valid = canShowDetailsFor(selected);
                detailViewer.setInput(valid ? selected : null);
                setCompositeEnabled((Composite) detailViewer.getControl(), valid);
                masterSelected(selected);
            }
        });
        detailViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                detailSelected(((IStructuredSelection) event.getSelection()).iterator());
            }
        });
        setWeights(new int[] { 75, 25 });
    }

    protected static void setCompositeEnabled(Control control, boolean enabled) {
        control.setEnabled(enabled);
        if (control instanceof Composite) for (Control child : ((Composite) control).getChildren()) setCompositeEnabled(child, enabled);
    }

    /**
	 *  This method is invoqued when the detail view selection changed.
	 *  By default do nothing, subclasses may override.
	 */
    protected void detailSelected(Iterator<?> iterator) {
    }

    /**
	 *  This method is invoqued when the master view selection changed.
	 *  By default do nothing, subclasses may override.
	 */
    protected void masterSelected(Object selected) {
    }

    /** Create the master viewer controls */
    protected abstract Viewer createMasterViewer(Composite parent);

    /** Create the detail viewer controls */
    protected abstract Viewer createDetailViewer(Composite parent);

    public Viewer getMasterViewer() {
        return masterViewer;
    }

    public Viewer getDetailViewer() {
        return detailViewer;
    }

    public Object getMasterSelection() {
        return ((IStructuredSelection) masterViewer.getSelection()).getFirstElement();
    }

    public Iterator<?> getDetailSelection() {
        return ((IStructuredSelection) detailViewer.getSelection()).iterator();
    }

    protected boolean canShowDetailsFor(Object input) {
        return true;
    }
}
