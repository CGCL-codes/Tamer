package de.jaret.util.ui.timebars.swt;

import java.util.List;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * Jface StructuredViewer for the TimeBarViewer. Naming is odd due to historical reasons. This is not yet a complete
 * implementation.
 * 
 * @author Peter Kliem
 * @version $Id: TBViewer.java 800 2008-12-27 22:27:33Z kliem $
 */
public class TBViewer extends StructuredViewer {

    /** the underlying timebarviewer. */
    protected TimeBarViewer _tbv;

    /** this pointer. */
    protected TBViewer _this;

    /**
     * Constructor.
     * 
     * @param tbv timebarviewer that should be wrapped
     */
    public TBViewer(TimeBarViewer tbv) {
        _tbv = tbv;
        _this = this;
        super.hookControl(_tbv);
        _tbv.addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent e) {
                DoubleClickEvent event = new DoubleClickEvent(_this, getSelection());
                fireDoubleClick(event);
            }

            public void mouseDown(MouseEvent e) {
            }

            public void mouseUp(MouseEvent e) {
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Control getControl() {
        return _tbv;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getInput() {
        return _tbv.getModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ISelection getSelection() {
        return _tbv.getSelection();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refresh() {
        _tbv.redraw();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelection(ISelection selection, boolean reveal) {
        _tbv.setSelection(selection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Widget doFindInputItem(Object element) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Widget doFindItem(Object element) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doUpdateItem(Widget item, Object element, boolean fullMap) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List getSelectionFromWidget() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void internalRefresh(Object element) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reveal(Object element) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setSelectionToWidget(List l, boolean reveal) {
    }
}
