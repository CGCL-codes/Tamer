package org.silicolife.gui.toolbar;

import javax.swing.JButton;
import javax.swing.JToolBar;
import org.silicolife.gui.constants.SLDatabaseConstants;
import org.silicolife.gui.img.Icons;

public class SLDatabaseToolBar {

    private static final long serialVersionUID = 1L;

    public static JToolBar createDatabaseToolBar(SLToolBarPanel toolBarPanel) {
        JToolBar toolBar = new JToolBar();
        toolBar.setName(SLDatabaseConstants.TOOLBAR_NAME);
        toolBarPanel.addButton(toolBar, createConnectButton());
        toolBarPanel.addButton(toolBar, createDisconnectButton());
        toolBarPanel.addButton(toolBar, createCommitButton());
        toolBarPanel.addButton(toolBar, createRollbackButton());
        return toolBar;
    }

    public static JButton createConnectButton() {
        JButton button = new SLToolBarButton(Icons.dbConnectIcon, SLDatabaseConstants.CONNECT_LABEL, SLDatabaseConstants.CONNECT_LABEL);
        button.setActionCommand(SLDatabaseConstants.CONNECT_ACTION_COMMAND);
        return button;
    }

    public static JButton createDisconnectButton() {
        JButton button = new SLToolBarButton(Icons.dbDisconnectIcon, SLDatabaseConstants.DISCONNECT_LABEL, SLDatabaseConstants.DISCONNECT_LABEL);
        button.setActionCommand(SLDatabaseConstants.DISCONNECT_ACTION_COMMAND);
        button.setEnabled(false);
        return button;
    }

    public static JButton createCommitButton() {
        JButton button = new SLToolBarButton(Icons.dbCommitIcon, SLDatabaseConstants.COMMIT_LABEL, SLDatabaseConstants.COMMIT_LABEL);
        button.setActionCommand(SLDatabaseConstants.COMMIT_ACTION_COMMAND);
        button.setEnabled(false);
        return button;
    }

    public static JButton createRollbackButton() {
        JButton button = new SLToolBarButton(Icons.dbRollbackIcon, SLDatabaseConstants.ROLLBACK_LABEL, SLDatabaseConstants.ROLLBACK_LABEL);
        button.setActionCommand(SLDatabaseConstants.ROLLBACK_ACTION_COMMAND);
        button.setEnabled(false);
        return button;
    }
}
