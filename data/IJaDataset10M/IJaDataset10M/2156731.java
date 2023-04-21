package info.jtrac.mylyn.ui.editor;

import org.eclipse.mylyn.tasks.ui.editors.AbstractRepositoryTaskEditor;
import org.eclipse.ui.forms.editor.FormEditor;

public class JtracRepositoryTaskEditor extends AbstractRepositoryTaskEditor {

    public JtracRepositoryTaskEditor(FormEditor editor) {
        super(editor);
    }

    @Override
    protected void validateInput() {
    }
}
