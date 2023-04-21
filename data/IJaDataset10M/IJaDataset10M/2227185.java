package org.colombbus.tangara.ide.controller.action;

import java.awt.event.ActionEvent;
import org.apache.commons.lang.Validate;
import org.colombbus.tangara.ide.view.codeeditor.CodeEditorPane;
import org.colombbus.tangara.ide.view.codeeditor.CodeEditorTabManager;

/**
 * Cut action for code editor
 *
 * @author Aurelien Bourdon <aurelien.bourdon@gmail.com>
 */
@SuppressWarnings("serial")
public class CutAction extends TangaraAction {

    private CodeEditorTabManager codeEditorTabManager;

    public CutAction(CodeEditorTabManager codeEditorTabManager) {
        Validate.notNull(codeEditorTabManager, "codeEditorTabManager argument is null");
        putValue(LARGE_ICON_KEY, RESOURCE_BUNDLE.getIcon("CutAction.largeIcon"));
        putValue(SHORT_DESCRIPTION, RESOURCE_BUNDLE.getString("CutAction.shortDescription"));
        this.codeEditorTabManager = codeEditorTabManager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CodeEditorPane currentCodeEditorPane = (CodeEditorPane) codeEditorTabManager.getSelectedComponent();
        currentCodeEditorPane.cut();
    }
}
