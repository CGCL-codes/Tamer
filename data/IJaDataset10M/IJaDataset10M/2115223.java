package ca.ucalgary.cpsc.ebe.fitClipse.ui.testHierarchy.menu.actions;

import java.util.Iterator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import aaftt.RefactoringException;
import aaftt.Suite;
import aaftt.Test;
import ca.ucalgary.cpsc.ebe.fitClipse.refactoring.actions.AddActionOfAcceptanceTest;
import ca.ucalgary.cpsc.ebe.fitClipse.refactoring.core.AddActionInfo;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.editor.WikiEditor;

/**
 * The Class AddColumnAction.
 */
public class AddActionAction extends Action implements ISelectionChangedListener {

    /**
	 * The control.
	 */
    private TreeViewer control = null;

    /**
	 * Instantiates a new adds the column action.
	 * 
	 * @param control
	 *            the control
	 */
    public AddActionAction(TreeViewer control) {
        this.control = control;
        this.setText("Add Action");
        this.setEnabled(true);
    }

    public void run() {
        if (this.control.getSelection().isEmpty()) return;
        IStructuredSelection selection = (IStructuredSelection) this.control.getSelection();
        Test test = (Test) selection.getFirstElement();
        AddActionInfo addRemoveActionInfo = new AddActionInfo(test);
        try {
            new AddActionOfAcceptanceTest().startRefactoring(addRemoveActionInfo);
        } catch (RefactoringException e) {
            return;
        } catch (InterruptedException e) {
            return;
        }
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IEditorReference[] editorReference = page.getEditorReferences();
        for (IEditorReference er : editorReference) {
            Iterator<String> iterator = addRemoveActionInfo.getTestIterator();
            while (iterator.hasNext()) {
                String uniqueID = iterator.next();
                test = addRemoveActionInfo.getTests().get(uniqueID);
                try {
                    if (er.getEditorInput().getName().equals(test.getUniqueID())) {
                        WikiEditor wikiEditor;
                        try {
                            wikiEditor = (WikiEditor) er.getEditor(true);
                            wikiEditor.load(test);
                            wikiEditor.doSaveAs();
                        } catch (PartInitException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (PartInitException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void selectionChanged(SelectionChangedEvent event) {
        if (!this.control.getSelection().isEmpty() && !(((StructuredSelection) this.control.getSelection()).getFirstElement() instanceof Suite) && (((StructuredSelection) this.control.getSelection()).getFirstElement() instanceof Test)) {
            this.setEnabled(true);
        } else this.setEnabled(false);
    }
}
