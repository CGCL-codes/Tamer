package uk.ac.bolton.archimate.templates.dialog;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import uk.ac.bolton.archimate.templates.model.ITemplate;
import uk.ac.bolton.archimate.templates.model.ITemplateGroup;

/**
 * Drag Drop Handler
 * 
 * @author Phillip Beauvoir
 */
public class TemplateManagerDialogDragDropHandler {

    private TreeViewer fTreeViewer;

    private int fDragOperations = DND.DROP_COPY;

    Transfer[] sourceTransferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer() };

    public TemplateManagerDialogDragDropHandler(TableViewer tableViewer, TreeViewer treeViewer) {
        fTreeViewer = treeViewer;
        registerDragSupport(tableViewer);
        registerDropSupport(treeViewer);
    }

    /**
     * Register drag support that starts from the Table
     */
    private void registerDragSupport(final StructuredViewer viewer) {
        viewer.addDragSupport(fDragOperations, sourceTransferTypes, new DragSourceListener() {

            public void dragFinished(DragSourceEvent event) {
                LocalSelectionTransfer.getTransfer().setSelection(null);
            }

            public void dragSetData(DragSourceEvent event) {
            }

            public void dragStart(DragSourceEvent event) {
                LocalSelectionTransfer.getTransfer().setSelection(viewer.getSelection());
                event.doit = true;
            }
        });
    }

    /**
     * Register drop support that ends on the Tree
     */
    private void registerDropSupport(final StructuredViewer viewer) {
        viewer.addDropSupport(fDragOperations, sourceTransferTypes, new DropTargetListener() {

            public void dragEnter(DropTargetEvent event) {
            }

            public void dragLeave(DropTargetEvent event) {
            }

            public void dragOperationChanged(DropTargetEvent event) {
                event.detail = getEventDetail(event);
            }

            public void dragOver(DropTargetEvent event) {
                event.detail = getEventDetail(event);
                event.feedback |= DND.FEEDBACK_SCROLL | DND.FEEDBACK_EXPAND;
            }

            public void drop(DropTargetEvent event) {
                doDropOperation(event);
            }

            public void dropAccept(DropTargetEvent event) {
            }

            private int getEventDetail(DropTargetEvent event) {
                return isValidSelection() && isValidDropTarget(event) ? DND.DROP_COPY : DND.DROP_NONE;
            }
        });
    }

    private void doDropOperation(DropTargetEvent event) {
        Object parent = getTargetParent(event);
        if (parent instanceof ITemplateGroup) {
            copy((ITemplateGroup) parent);
        }
    }

    private void copy(ITemplateGroup parent) {
        IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().getSelection();
        for (Object o : selection.toArray()) {
            if (o instanceof ITemplate) {
                if (!parent.getTemplates().contains(o)) {
                    parent.addTemplate((ITemplate) o);
                }
            }
        }
        fTreeViewer.refresh();
    }

    private boolean isValidSelection() {
        return true;
    }

    /**
     * Determine the target parent from the drop event
     */
    private Object getTargetParent(DropTargetEvent event) {
        if (event.item == null) {
            return null;
        }
        Object objectDroppedOn = event.item.getData();
        if (objectDroppedOn instanceof ITemplateGroup) {
            return objectDroppedOn;
        }
        return null;
    }

    /**
     * @return True if target is valid
     */
    private boolean isValidDropTarget(DropTargetEvent event) {
        Object parent = getTargetParent(event);
        if (parent instanceof ITemplateGroup) {
            ITemplateGroup targetGroup = (ITemplateGroup) parent;
            IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().getSelection();
            for (Object object : selection.toList()) {
                if (targetGroup.getTemplates().contains(object)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
