package pku.edu.tutor.commands;

import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;

public class EditorCommandTarget extends CommandTarget {

    /**
	 * @param widget
	 * @param context
	 */
    public EditorCommandTarget(Widget widget, IEditorPart editor) {
        super(widget, editor);
    }

    public IEditorPart getEditor() {
        return (IEditorPart) getContext();
    }

    public void ensureVisible() {
        IEditorPart editor = getEditor();
        IWorkbenchPage page = editor.getEditorSite().getPage();
        page.activate(editor);
    }
}
