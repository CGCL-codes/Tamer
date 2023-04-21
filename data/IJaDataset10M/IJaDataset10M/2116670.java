package skribler.editor.actions;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.ImageIcon;
import skribler.editor.Editor;

@SuppressWarnings("serial")
public class SaveSourceAction extends EditorAction implements Action {

    private static final ImageIcon SAVE_SOURCE_ICON = new ImageIcon(SaveSourceAction.class.getResource("/skribler/resources/source.gif"));

    public SaveSourceAction(Editor editor) {
        super(editor, "Save Source", SAVE_SOURCE_ICON);
    }

    public void actionPerformed(ActionEvent ae) {
        System.out.println("Save Source");
        final Editor editor = this.editor;
        if (editor == null) {
            System.out.println("  No editor set.");
            return;
        }
        editor.saveSource();
    }
}
