package mx.kolobok.noteeditor.ui;

import mx.kolobok.noteeditor.NoteManager;
import mx.kolobok.noteeditor.ui.keymap.actions.*;
import mx.kolobok.noteeditor.ui.list.NoteList;
import mx.kolobok.noteeditor.ui.list.NoteListModel;
import org.jetbrains.annotations.NotNull;
import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * User:  Nikita Belenkiy
 * Date: 19.05.11
 * Time: 3:42
 */
public class MainForm extends JPanel implements WindowListener {

    @NotNull
    private JTextField filter = new JTextField();

    @NotNull
    private NoteList noteList;

    @NotNull
    private Editor editor = new Editor();

    private JScrollPane noteListScrollPane;

    private JScrollPane editorScrollPane;

    public MainForm(NoteManager noteManager) {
        setLayout(new GridBagLayout());
        NoteListModel noteListModel = new NoteListModel(noteManager);
        noteList = new NoteList(editor, noteListModel);
        initActions(noteListModel);
        noteListScrollPane = new JScrollPane(noteList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        editorScrollPane = new JScrollPane(editor, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        noteList.getModel().addListDataListener(new MyListDataListener(editor));
        layoutComponents();
    }

    private void initActions(NoteListModel noteListModel) {
        UIUtils.addAll(this, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap uiActionMap = getActionMap();
        uiActionMap.put(Actions.NEW_NOTE_ACTION, new NewNoteAction(noteListModel));
        uiActionMap.put(Actions.NEW_NOTE_FROM_CLIPBOARD_ACTION, new NewNoteFromClipboardAction(noteListModel));
        uiActionMap.put(Actions.DELETE_NOTE_ACTION, new DeleteNoteAction(noteList, noteListModel));
        uiActionMap.put(Actions.RENAME_NOTE_ACTION, new RenameNoteAction(noteList));
        uiActionMap.setParent(noteList.getActionMap());
    }

    private void layoutComponents() {
        add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, noteListScrollPane, editorScrollPane), new GridBagConstraints(0, 2, 3, 1, 1, 0.2, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        editor.setNote(null);
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    private class MyListDataListener implements ListDataListener {

        private final Editor editor;

        public MyListDataListener(Editor editor) {
            this.editor = editor;
        }

        public void intervalAdded(ListDataEvent e) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    noteList.setSelectedIndex(noteList.getModel().getSize() - 1);
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            JScrollBar verticalScrollBar = noteListScrollPane.getVerticalScrollBar();
                            verticalScrollBar.setValue(verticalScrollBar.getMaximum());
                            editor.requestFocus();
                        }
                    });
                }
            });
        }

        public void intervalRemoved(ListDataEvent e) {
            editor.setNote(null);
        }

        public void contentsChanged(ListDataEvent e) {
            noteList.setSelectedIndex(e.getIndex0());
        }
    }
}
