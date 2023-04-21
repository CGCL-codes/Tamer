package tico.editor.actions;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import tico.board.TBoard;
import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Action wich adds a new board for the editor project.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TBoardNewAction extends TEditorAbstractAction {

    /**
	 * Constructor for TBoardNewAction.
	 * 
	 * @param editor The boards' editor
	 */
    public TBoardNewAction(TEditor editor) {
        super(editor, TLanguage.getString("TBoardNewAction.NAME"), TResourceManager.getImageIcon("board-new-22.png"));
    }

    public void actionPerformed(ActionEvent e) {
        if (getEditor().getProject() == null) {
            JOptionPane.showMessageDialog(null, TLanguage.getString("TBoardNewAction.NO_PROJECT_ERROR"), TLanguage.getString("WARNING") + "!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        getEditor().getProject().addBoard(new TBoard());
    }
}
