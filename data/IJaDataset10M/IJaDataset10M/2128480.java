package tico.rules.actions;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.editor.actions.TEditorAbstractAction;
import tico.editor.actions.TProjectSaveAction;
import tico.rules.dialogs.TValidationDialog;

/**
 * Action which opens the project validation dialog.
 * 
 * @author Beatriz Mateo
 * @version 0.1 Mar 21, 2007
 */
public class TProjectValidationAction extends TEditorAbstractAction {

    /**
	 * Constructor for TProjectValidationAction.
	 * 
	 * @param editor The boards' editor
	 */
    public TProjectValidationAction(TEditor editor) {
        super(editor, TLanguage.getString("TProjectValidationAction.NAME"));
    }

    public void actionPerformed(ActionEvent e) {
        int choosenOption = JOptionPane.NO_OPTION;
        if (getEditor().isModified()) choosenOption = JOptionPane.showConfirmDialog(null, TLanguage.getString("TProjectValidationAction.ASK_SAVE") + "\n" + TLanguage.getString("TProjectValidationAction.ASK_SAVE_QUESTION"), TLanguage.getString("TProjectValidationAction.MODIFIED_PROJECT"), JOptionPane.YES_NO_CANCEL_OPTION);
        if (choosenOption == JOptionPane.CANCEL_OPTION) return;
        if (choosenOption == JOptionPane.YES_OPTION) new TProjectSaveAction(getEditor()).actionPerformed(e);
        new TValidationDialog(getEditor(), getEditor().getProject());
    }
}
