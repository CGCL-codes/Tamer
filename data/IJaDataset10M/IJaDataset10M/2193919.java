package org.deft.views;

import net.sf.vex.dom.Element;
import net.sf.vex.editor.VexEditor;
import org.deft.popups.PropertiesComposite;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

public class PropertiesView extends ViewPart implements ISelectionListener, IPartListener {

    public static final String ID = "org.deft.PropertiesView";

    private PropertiesComposite comp;

    private Composite parent;

    private IWorkbenchPart currentEditor;

    @Override
    public void createPartControl(Composite parent) {
        this.parent = parent;
        getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
        getSite().getPage().addPartListener(this);
    }

    @Override
    public void dispose() {
        disposeComposite();
        getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
        getSite().getPage().removePartListener(this);
        super.dispose();
    }

    @Override
    public void setFocus() {
        if (comp != null) comp.setFocus();
    }

    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if (part instanceof VexEditor) {
            if (selection instanceof StructuredSelection) {
                StructuredSelection ssel = (StructuredSelection) selection;
                Object obj = ssel.getFirstElement();
                if (obj instanceof Element) {
                    this.currentEditor = part;
                    Element e = (Element) obj;
                    refreshView(e);
                }
            }
        }
    }

    /**
	 * Decides what composite to show.
	 * @param e
	 */
    private void refreshView(Element e) {
        PropertiesComposite newComp = new PropertiesComposite(parent, SWT.NONE, e);
        if (comp != null) {
            comp.dispose();
        }
        comp = newComp;
        parent.layout();
        String description = comp.getDescription();
        if (description != null) {
            this.setContentDescription(description);
            parent.setVisible(true);
        } else {
            this.setContentDescription("Fragment could not be found.");
            parent.setVisible(false);
        }
    }

    private void disposeComposite() {
        if (comp != null) {
            comp.dispose();
            comp = null;
        }
    }

    public void partActivated(IWorkbenchPart part) {
    }

    public void partBroughtToTop(IWorkbenchPart part) {
    }

    public void partClosed(IWorkbenchPart part) {
        if (currentEditor != null && currentEditor.equals(part)) {
            disposeComposite();
        }
    }

    public void partDeactivated(IWorkbenchPart part) {
    }

    public void partOpened(IWorkbenchPart part) {
    }
}
