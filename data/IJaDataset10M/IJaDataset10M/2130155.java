package org.schnelln.gui.dialogs;

import static org.schnelln.core.config.I18N.i18n;
import java.awt.event.WindowListener;
import javax.swing.JTabbedPane;
import org.schnelln.gui.MainGui;
import org.schnelln.gui.dialogs.components.ConnectionPanel;
import org.schnelln.gui.dialogs.components.ServerSettingsPanel;
import org.schnelln.gui.helper.listeners.dialogs.ADialogListener;
import pagelayout.Column;
import pagelayout.GridRows;

/**
 * The Class NewGameDialog.<br>
 * This Dialog is used for joining a new, already created game. <br>
 * 
 * @author Lukas Tischler [tischler.lukas_AT_gmail.com]
 */
public class NewGameDialog extends AbstractDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7262969856607870807L;

    private ServerSettingsPanel settingsPanel;

    private ConnectionPanel connectionPanel;

    private JTabbedPane tabbedPane;

    /**
	 * Instantiates a new new game dialog.
	 * 
	 * @param gui
	 *            the gui
	 */
    public NewGameDialog(MainGui gui) {
        super(gui, "newGameDialog.title");
        this.init();
        this.pack();
        this.setSize(new java.awt.Dimension(500, 300));
        this.setLocation(calculateLocation());
    }

    /**
	 * TODO: give Listener for this panel a reference to the tabbed pane (maybe
	 * make it accessible via a getter?) to enable/disable it also disable the
	 * tab for settings if we choose another item in the combo-box of game
	 * names!
	 */
    private void init() {
        ADialogListener dialogListener = (ADialogListener) this.beanManager.getActionListener("newGameDialogListener");
        this.addWindowListener((WindowListener) dialogListener);
        this.settingsPanel = new ServerSettingsPanel(null, false);
        this.connectionPanel = new ConnectionPanel(dialogListener, true);
        GridRows rows = new GridRows();
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab(i18n("createNewGameDialog.connection"), this.connectionPanel);
        tabbedPane.addTab(i18n("createNewGameDialog.settings"), this.settingsPanel);
        tabbedPane.setEnabledAt(1, false);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        rows.newRow().add(new Column(tabbedPane)).span(3);
        addDefaultButtonsToGridRow(rows, dialogListener);
        rows.createCellGrid().createLayout(this);
    }

    /**
	 * Gets the settings panel.
	 * 
	 * @return the settings panel
	 */
    public ServerSettingsPanel getSettingsPanel() {
        return settingsPanel;
    }

    /**
	 * Gets the tabbed pane.
	 * 
	 * @return the tabbed pane
	 */
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    /**
	 * Gets the connection panel.
	 * 
	 * @return the connection panel
	 */
    public ConnectionPanel getConnectionPanel() {
        return connectionPanel;
    }
}
