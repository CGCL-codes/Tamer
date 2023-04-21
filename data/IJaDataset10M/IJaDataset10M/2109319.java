package name.nirav.refactoringman.actions;

import name.nirav.refactoringman.refactorings.StaticCallForwardingRefactoring;
import name.nirav.refactoringman.wizards.StaticCallForwardWizard;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.internal.ui.actions.SelectionConverter;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * @author Nirav Thaker
 *
 */
@SuppressWarnings("restriction")
public class StaticCallForwardingAction implements IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow fWindow;

    protected JavaEditor editor;

    public StaticCallForwardingAction() {
    }

    public void run(IAction action) {
        ITypeRoot editorInput = SelectionConverter.getInput(editor);
        if (editorInput instanceof ICompilationUnit) {
            ISelection selection = editor.getSelectionProvider().getSelection();
            String origMeth = null;
            if (selection instanceof ITextSelection) {
                ITextSelection sel = (ITextSelection) selection;
                origMeth = sel.getText();
            }
            ICompilationUnit unit = (ICompilationUnit) editorInput;
            runInternal(origMeth, unit);
        }
    }

    private void runInternal(String origMeth, ICompilationUnit unit) {
        try {
            StaticCallForwardingRefactoring refactoring = new StaticCallForwardingRefactoring();
            refactoring.setOriginalMethod(origMeth);
            refactoring.setCompilationUnit(unit);
            RefactoringWizard wizard = new StaticCallForwardWizard(refactoring, "Static Call Forward Wizard");
            RefactoringWizardOpenOperation operation = new RefactoringWizardOpenOperation(wizard);
            operation.run(fWindow.getShell(), "Static Call Forward");
        } catch (InterruptedException exception) {
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
        this.fWindow = window;
        this.fWindow.getSelectionService().addSelectionListener(new ISelectionListener() {

            @Override
            public void selectionChanged(IWorkbenchPart part, ISelection selection) {
                if (part instanceof JavaEditor) {
                    editor = (JavaEditor) part;
                }
            }
        });
    }
}
