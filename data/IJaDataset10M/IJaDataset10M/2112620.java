package es.ulpgc.dis.heuristicide.rcp.ui.execution.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import es.ulpgc.dis.heuriskein.model.solver.Execution;
import es.ulpgc.dis.heuriskein.model.solver.ExecutionRelease;
import es.ulpgc.dis.heuristicide.rcp.actions.Images;
import es.ulpgc.dis.heuristicide.rcp.ui.commons.EditorInput;
import es.ulpgc.dis.heuristicide.rcp.ui.execution.ExecutionDebugEditor;
import es.ulpgc.dis.heuristicide.rcp.ui.execution.ExecutionReleaseEditor;
import es.ulpgc.dis.heuristicide.rcp.ui.navigation.NavigationView;

public class StartAction extends Action implements IEditorActionDelegate, ISelectionListener {

    private Execution execution;

    private IWorkbenchWindow window;

    public StartAction(String str, IWorkbenchWindow window) {
        super(str);
        this.window = window;
        setToolTipText("Starts the execution");
        setImageDescriptor(Images.createImageDescriptorFor("icons/start.gif"));
        window.getSelectionService().addSelectionListener(this);
        setEnabled(false);
    }

    public void run() {
        execution.start();
    }

    public void setActiveEditor(IAction arg0, IEditorPart part) {
        if (part instanceof ExecutionReleaseEditor) {
            execution = ((ExecutionReleaseEditor) part).getExecution();
        } else {
            execution = ((ExecutionDebugEditor) part).getExecution();
        }
        check(execution);
    }

    private void check(Execution execution2) {
        setEnabled(true);
    }

    public void run(IAction arg0) {
    }

    public void selectionChanged(IAction arg0, ISelection arg1) {
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection arg1) {
        if (part instanceof NavigationView) {
            Object ob = ((IStructuredSelection) arg1).getFirstElement();
            if (ob instanceof Execution) {
                this.execution = (Execution) ob;
                check(execution);
            } else {
                setEnabled(false);
            }
        }
    }
}
