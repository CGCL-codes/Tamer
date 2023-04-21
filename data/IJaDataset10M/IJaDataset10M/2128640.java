package org.colombbus.tangara.ide.controller.action;

import java.awt.event.ActionEvent;
import org.apache.commons.lang.Validate;
import org.colombbus.tangara.ide.model.codeeditor.CodeEditor;
import org.colombbus.tangara.ide.model.codeeditor.CodeEditorManager;
import org.colombbus.tangara.ide.model.codeeditor.DefaultCodeEditor;

/**
 * Create a new code editor action.
 *
 * @author Aurelien Bourdon <aurelien.bourdon@gmail.com>
 */
@SuppressWarnings("serial")
public class NewCodeEditorAction extends TangaraAction {

    private CodeEditorManager codeEditorManager;

    public NewCodeEditorAction(CodeEditorManager codeEditorManager) {
        Validate.notNull(codeEditorManager, "codeEditorManager argument is null");
        this.codeEditorManager = codeEditorManager;
        putValue(LARGE_ICON_KEY, RESOURCE_BUNDLE.getIcon("NewCodeEditorAction.largeIcon"));
        putValue(SHORT_DESCRIPTION, RESOURCE_BUNDLE.getString("NewCodeEditorAction.shortDescription"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CodeEditor codeEditor = new DefaultCodeEditor();
        codeEditor.setName(RESOURCE_BUNDLE.getString("NewCodeEditorAction.newCodeEditorName"));
        codeEditorManager.insertCodeEditor(codeEditor);
        if (codeEditor != codeEditorManager.getCurrentCodeEditor()) codeEditorManager.setCurrentCodeEditor(codeEditor);
    }
}
