package at.vartmp.jschnellen.gui.dialogs;

import static at.vartmp.jschnellen.core.config.I18N.i18n;
import javax.swing.JTabbedPane;
import at.vartmp.jschnellen.core.util.logger.SchnellnLogFactory;
import at.vartmp.jschnellen.core.util.logger.SchnellnLogger;
import at.vartmp.jschnellen.gui.MainGui;
import at.vartmp.jschnellen.gui.dialogs.components.ClientSettingsPanel;
import at.vartmp.jschnellen.gui.dialogs.components.LoggerPanel;
import at.vartmp.jschnellen.gui.helper.listeners.dialogs.ADialogListener;
import pagelayout.Column;
import pagelayout.GridRows;

/**
 * @author Lukas Tischler [tischler.lukas_AT_gmail.com]
 * 
 */
public class SettingsDialog extends AbstractDialog {

    private static final long serialVersionUID = -2252287245172180989L;

    /** The logger of this class and subclasses. */
    private static SchnellnLogger logger = SchnellnLogFactory.getLog(SettingsDialog.class);

    private ClientSettingsPanel settingsPanel;

    private LoggerPanel loggerPanel;

    /**
	 * Instantiates a new new game dialog. This constructor is for subclasses.
	 * 
	 * @param gui
	 *            a reference to the main gui
	 */
    public SettingsDialog(MainGui gui) {
        super(gui, i18n("settingsDialog.title"));
        this.init();
        this.pack();
        this.setSize(new java.awt.Dimension(500, 300));
    }

    /**
	 * Initializes the SettingsManager and creates the Layout.
	 */
    private void init() {
        logger.debug("Initializing SettingsDialog...");
        ADialogListener listener = (ADialogListener) this.beanManager.getActionListener("settingsDialogListener");
        this.addWindowListener(listener);
        this.settingsPanel = new ClientSettingsPanel(listener);
        this.loggerPanel = new LoggerPanel(listener);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add(i18n("createNewGameDialog.settings"), settingsPanel);
        tabbedPane.add(i18n("createNewGameDialog.expert"), loggerPanel);
        GridRows rows = new GridRows();
        rows.newRow().add(new Column(tabbedPane)).span(3);
        this.addDefaultButtonsToGridRow(rows, listener);
        rows.createCellGrid().createLayout(this);
    }

    /**
	 * Gets the settings panel.
	 * 
	 * @return the settings panel
	 */
    public ClientSettingsPanel getSettingsPanel() {
        return this.settingsPanel;
    }

    /**
	 * Gets the logger panel.
	 * 
	 * @return the logger panel
	 */
    public LoggerPanel getLoggerPanel() {
        return loggerPanel;
    }
}
