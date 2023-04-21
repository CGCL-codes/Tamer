package com.android.hierarchyviewer.ui.action;

import com.android.hierarchyviewer.ui.Workspace;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

public class RequestLayoutAction extends BackgroundAction {

    public static final String ACTION_NAME = "requestLayout";

    private Workspace mWorkspace;

    public RequestLayoutAction(Workspace workspace) {
        putValue(NAME, "Request Layout");
        putValue(SHORT_DESCRIPTION, "Request Layout");
        putValue(LONG_DESCRIPTION, "Request Layout");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        this.mWorkspace = workspace;
    }

    public void actionPerformed(ActionEvent e) {
        executeBackgroundTask(mWorkspace.requestLayout());
    }
}
