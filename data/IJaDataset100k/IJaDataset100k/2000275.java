package com.android.hierarchyviewer.ui.action;

import com.android.hierarchyviewer.ui.Workspace;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

public class SaveSceneAction extends BackgroundAction {

    public static final String ACTION_NAME = "saveScene";

    private Workspace mWorkspace;

    public SaveSceneAction(Workspace workspace) {
        mWorkspace = workspace;
        putValue(NAME, "Save as PNG...");
        putValue(SHORT_DESCRIPTION, "Save");
        putValue(LONG_DESCRIPTION, "Save as PNG...");
        putValue(MNEMONIC_KEY, KeyEvent.VK_S);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }

    public void actionPerformed(ActionEvent e) {
        executeBackgroundTask(mWorkspace.saveSceneAsImage());
    }
}
