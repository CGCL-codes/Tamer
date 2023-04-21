package ie.ul.brendancleary.forager.views;

import ie.ul.brendancleary.forager.index.IndexElement;
import java.util.Vector;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

/**
 * Allows us to plug into eclipses DND framework, specifically it 
 * generates a set of assignment result markers that are serialised and 
 * passed to other views. 
 * 
 * @author Brendan.Cleary
 */
class AssignmentResultDragListener implements DragSourceListener {

    /**
	 * 
	 */
    private final AssignmentResultsView view;

    /**
	 * @param view
	 */
    AssignmentResultDragListener(AssignmentResultsView view) {
        this.view = view;
    }

    public void dragStart(DragSourceEvent event) {
        IStructuredSelection selection = (IStructuredSelection) this.view.getViewer().getSelection();
        Object[] selectedObjects = selection.toArray();
        if (selection != null) {
            for (int i = 0; i < selectedObjects.length; i++) {
                AssignmentResult assignmentResult = (AssignmentResult) ((Vector) selectedObjects[i]).get(AssignmentResultsView.ASSIGNMENT_RESULT_COLUMN);
                if (assignmentResult.getType() > IndexElement.COMPILATION_UNIT) {
                    event.doit = false;
                }
            }
        }
    }

    public void dragSetData(DragSourceEvent event) {
        IStructuredSelection selection = (IStructuredSelection) this.view.getViewer().getSelection();
        Object[] selectedObjects = selection.toArray();
        if (selection != null) {
            IMarker[] markers = new IMarker[selectedObjects.length];
            for (int i = 0; i < selectedObjects.length; i++) {
                AssignmentResult assignmentResult = (AssignmentResult) ((Vector) selectedObjects[i]).get(AssignmentResultsView.ASSIGNMENT_RESULT_COLUMN);
                try {
                    assignmentResult.getMarker().setAttribute("compileUnit", assignmentResult.getJavaElement().getElementName());
                    assignmentResult.getMarker().setAttribute("elementtype", new Integer(assignmentResult.getType()));
                    String sig = Signature.toString(((IMethod) assignmentResult.getJavaElement()).getSignature());
                    sig = sig.replaceAll(",", " ");
                    assignmentResult.getMarker().setAttribute("signature", sig);
                    assignmentResult.getMarker().setAttribute("path", assignmentResult.getJavaElement().getPath().toString());
                    markers[i] = assignmentResult.getMarker();
                } catch (CoreException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    System.out.println("here");
                }
            }
            event.data = markers;
        }
    }

    public void dragFinished(DragSourceEvent event) {
    }
}
