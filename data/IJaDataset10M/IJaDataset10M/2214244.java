package mx.kolobok.noteeditor.ui;

import mx.kolobok.noteeditor.Note;
import mx.kolobok.noteeditor.ui.keymap.actions.RedoAction;
import mx.kolobok.noteeditor.ui.keymap.actions.UndoAction;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import javax.swing.text.Keymap;
import java.awt.event.KeyEvent;

/**
 * User:  Nikita Belenkiy
 * Date: 24.05.11
 * Time: 19:37
 */
public class Editor extends JEditorPane {

    private Note note;

    private MyPopupMenu cutCopyPastePopupMenu = new MyPopupMenu(this);

    public Editor() {
        Keymap keymap = getKeymap();
        keymap.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK, true), new UndoAction());
        keymap.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_MASK, true), new RedoAction());
        setEditable(false);
        setComponentPopupMenu(cutCopyPastePopupMenu);
    }

    public void setNote(@Nullable Note newNote) {
        if (note != null) {
            note.setText(getText());
            getDocument().removeUndoableEditListener(note.getUndoManager());
        }
        if (newNote == null) {
            setEditable(false);
            setText("");
            setComponentPopupMenu(null);
        } else {
            setText(newNote.getText());
            getDocument().addUndoableEditListener(newNote.getUndoManager());
            setEditable(true);
            setComponentPopupMenu(cutCopyPastePopupMenu);
        }
        this.note = newNote;
    }

    public Note getNote() {
        return note;
    }
}
