package cx.ath.contribs.klex.forester.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import cx.ath.contribs.klex.forester.ForesterPlugin;
import cx.ath.contribs.klex.forester.editors.ElementEditor;
import cx.ath.contribs.klex.forester.editors.ElementEditorInput;
import cx.ath.contribs.klex.forester.editors.ElementTypeEditor;
import cx.ath.contribs.klex.forester.editors.ElementTypeEditorInput;

public class ElementAttributeOpenAction extends Action {

    IEditorInput input;

    public ElementAttributeOpenAction(IEditorInput input) {
        super();
        this.input = input;
    }

    public void run() {
        if (input instanceof ElementEditorInput) {
            IWorkbenchPage page = ForesterPlugin.getInstance().getWorkbench().getActiveWorkbenchWindow().getActivePage();
            IEditorPart edt = page.findEditor(input);
            if (edt == null) {
                try {
                    page.openEditor(input, ElementEditor.ID);
                } catch (PartInitException e) {
                    e.printStackTrace();
                }
            } else {
            }
        }
        if (input instanceof ElementTypeEditorInput) {
            IWorkbenchPage page = ForesterPlugin.getInstance().getWorkbench().getActiveWorkbenchWindow().getActivePage();
            IEditorPart edt = page.findEditor(input);
            if (edt == null) {
                try {
                    page.openEditor(input, ElementTypeEditor.ID);
                } catch (PartInitException e) {
                    e.printStackTrace();
                }
            } else {
            }
        }
    }
}
