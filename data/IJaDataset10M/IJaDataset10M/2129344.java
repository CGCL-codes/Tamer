package org.nexopenframework.ide.eclipse.commons.popup.actions;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.actions.SelectionConverter;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.ui.IWorkingCopyManager;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p></p>
 * 
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class JavaTypeViewerAction extends JavaTypeAction implements IEditorActionDelegate {

    /**
	 * selected editor.
	 */
    private ITextEditor editor;

    /**
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
    public void run(IAction action) {
        IType type = null;
        if (this.editor != null) {
            if (this.editor instanceof JavaEditor) {
                try {
                    IJavaElement element = SelectionConverter.getElementAtOffset((JavaEditor) this.editor);
                    if (element != null) {
                        type = (IType) element.getAncestor(IJavaElement.TYPE);
                    }
                } catch (JavaModelException e) {
                }
            }
            if (type == null) {
                IWorkingCopyManager manager = JavaUI.getWorkingCopyManager();
                ICompilationUnit unit = manager.getWorkingCopy(this.editor.getEditorInput());
                type = unit.findPrimaryType();
            }
        }
        if (type != null) {
            runAction(action, type, new Shell());
        }
    }

    /**
	 * selected editor has changed.
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(IAction, ISelection)
	 */
    public void selectionChanged(IAction action, ISelection selection) {
    }

    /**
	 * @see org.eclipse.ui.IEditorActionDelegate#setActiveEditor(IAction,
	 *      IEditorPart)
	 */
    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
        if (targetEditor instanceof ITextEditor) {
            this.editor = (ITextEditor) targetEditor;
        }
    }
}
