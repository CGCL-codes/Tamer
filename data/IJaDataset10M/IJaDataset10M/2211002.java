package ch.iserver.ace.application.action;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import ch.iserver.ace.application.LocaleMessageSource;
import ch.iserver.ace.application.PersistentContentPane;
import ch.iserver.ace.application.editor.Editor;

public class ToggleFullScreenEditingAction extends AbstractAction {

    private PersistentContentPane persistentContentPane;

    private boolean fullScreenEditingMode = false;

    private LocaleMessageSource messageSource;

    public ToggleFullScreenEditingAction(LocaleMessageSource messageSource, PersistentContentPane persistentContentPane, Editor editor) {
        super(messageSource.getMessage("mViewEditorFullScreen"), messageSource.getIcon("iViewEditorFullScreen"));
        putValue(SHORT_DESCRIPTION, messageSource.getMessage("mViewEditorFullScreenTT"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('F', InputEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        this.messageSource = messageSource;
        this.persistentContentPane = persistentContentPane;
        editor.getEditorComponent().addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    toggleEditingMode();
                }
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        toggleEditingMode();
    }

    public void toggleEditingMode() {
        if (fullScreenEditingMode) {
            putValue(SMALL_ICON, messageSource.getIcon("iViewEditorFullScreen"));
            putValue(SHORT_DESCRIPTION, messageSource.getMessage("mViewEditorFullScreenTT"));
            putValue(NAME, messageSource.getMessage("mViewEditorFullScreenTT"));
            persistentContentPane.switchFullScreenEditing();
            fullScreenEditingMode = false;
        } else {
            putValue(SMALL_ICON, messageSource.getIcon("iViewEditorNormalScreen"));
            putValue(SHORT_DESCRIPTION, messageSource.getMessage("mViewEditorNormalScreenTT"));
            putValue(NAME, messageSource.getMessage("mViewEditorNormalScreenTT"));
            persistentContentPane.switchFullScreenEditing();
            fullScreenEditingMode = true;
        }
    }
}
