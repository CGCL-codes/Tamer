package org.gaea.ui.graphic;

import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.gaea.ui.language.Messages;
import org.gaea.ui.utilities.ToolBarUtilities;

/**
 * Creates the main ToolBar (left one).
 * 
 * @author mtremblay
 */
public class MainToolBar extends JToolBar {

    /**
	 * Auto Generated
	 */
    private static final long serialVersionUID = -4501538792319545284L;

    public static final String IMAGES_GENERAL_PATH = "/org/gaea/ui/graphic/images/toolbar/toolbarButtonGraphics/general/";

    public static final String IMAGES_NAVIGATION_PATH = "/org/gaea/ui/graphic/images/toolbar/toolbarButtonGraphics/navigation/";

    public static final String IMAGES_EDIT_PATH = "/org/gaea/ui/graphic/images/toolbar/toolbarButtonGraphics/table/";

    public static final String IMAGES_DEVELOPMENT_PATH = "/org/gaea/ui/graphic/images/toolbar/toolbarButtonGraphics/development/";

    public static final String IMAGES_EXTENSION = ".gif";

    public static final String IMAGES_NEW_SMALL = IMAGES_GENERAL_PATH + "New16" + IMAGES_EXTENSION;

    public static final String IMAGES_OPEN_SMALL = IMAGES_GENERAL_PATH + "Open16" + IMAGES_EXTENSION;

    public static final String IMAGES_SAVE_SMALL = IMAGES_GENERAL_PATH + "Save16" + IMAGES_EXTENSION;

    public static final String IMAGES_SAVE_AS_SMALL = IMAGES_GENERAL_PATH + "SaveAs16" + IMAGES_EXTENSION;

    public static final String IMAGES_START_TRANSAC_SMALL = IMAGES_GENERAL_PATH + "History16" + IMAGES_EXTENSION;

    public static final String IMAGES_COMMIT_TRANSAC_SMALL = IMAGES_GENERAL_PATH + "Import16" + IMAGES_EXTENSION;

    public static final String IMAGES_ROLLBACK_TRANSAC_SMALL = IMAGES_GENERAL_PATH + "Undo16" + IMAGES_EXTENSION;

    public static final String IMAGES_REFRESH_SMALL = IMAGES_GENERAL_PATH + "Refresh16" + IMAGES_EXTENSION;

    public static final String IMAGES_ABOUT_SMALL = IMAGES_GENERAL_PATH + "About16" + IMAGES_EXTENSION;

    public static final String IMAGES_HELP_SMALL = IMAGES_GENERAL_PATH + "Help16" + IMAGES_EXTENSION;

    public static final String IMAGES_OPTIONS_SMALL = IMAGES_GENERAL_PATH + "Preferences16" + IMAGES_EXTENSION;

    public static final String IMAGES_NEW = IMAGES_GENERAL_PATH + "New24" + IMAGES_EXTENSION;

    public static final String IMAGES_OPEN = IMAGES_GENERAL_PATH + "Open24" + IMAGES_EXTENSION;

    public static final String IMAGES_SAVE = IMAGES_GENERAL_PATH + "Save24" + IMAGES_EXTENSION;

    public static final String IMAGES_SAVE_AS = IMAGES_GENERAL_PATH + "SaveAs24" + IMAGES_EXTENSION;

    public static final String IMAGES_START_TRANSAC = IMAGES_GENERAL_PATH + "History24" + IMAGES_EXTENSION;

    public static final String IMAGES_COMMIT_TRANSAC = IMAGES_GENERAL_PATH + "Import24" + IMAGES_EXTENSION;

    public static final String IMAGES_ROLLBACK_TRANSAC = IMAGES_GENERAL_PATH + "Undo24" + IMAGES_EXTENSION;

    public static final String IMAGES_REFRESH = IMAGES_GENERAL_PATH + "Refresh24" + IMAGES_EXTENSION;

    public static final String IMAGES_DATA_PERSPECTIVE_SMALL = IMAGES_DEVELOPMENT_PATH + "J2EEServer16" + IMAGES_EXTENSION;

    public static final String IMAGES_STRUCTURE_PERSPECTIVE_SMALL = IMAGES_DEVELOPMENT_PATH + "Server16" + IMAGES_EXTENSION;

    private JButton _commitTransacButton;

    private JButton _rollbackTransacButton;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("org.gaea.ui.lookandfeel.GaeaLookAndFeel");
        } catch (InstantiationException e) {
        } catch (ClassNotFoundException e) {
        } catch (UnsupportedLookAndFeelException e) {
        } catch (IllegalAccessException e) {
        }
        JFrame frm = new JFrame();
        frm.add(new MainToolBar());
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setSize(500, 500);
        frm.setVisible(true);
    }

    /**
	 * Constructor
	 */
    public MainToolBar() {
        super();
        setFloatable(false);
        setRollover(true);
        createToolBar();
    }

    /**
	 * Add button to the ToolBar
	 */
    private void createToolBar() {
        this.add(Box.createHorizontalStrut(10));
        ActionListener eventActionListener = new GeneralWindowActionListener();
        JButton refreshButton = ToolBarUtilities.makeButton(GeneralWindowActionListener.REFRESH_ACTION, Messages.getString("MainToolBar.RefreshFromDB"), Messages.getString("Common.Refresh"), IMAGES_REFRESH, eventActionListener);
        this.add(refreshButton);
        addSeparator();
        _commitTransacButton = ToolBarUtilities.makeButton(GeneralWindowActionListener.COMMIT_TRANSAC_ACTION, Messages.getString("Common.CommitTransaction"), "Commit", IMAGES_COMMIT_TRANSAC, eventActionListener);
        this.add(_commitTransacButton);
        _rollbackTransacButton = ToolBarUtilities.makeButton(GeneralWindowActionListener.ROLLBACK_TRANSAC_ACTION, Messages.getString("MainToolBar.RollbackTransaction"), "Rollback", IMAGES_ROLLBACK_TRANSAC, eventActionListener);
        this.add(_rollbackTransacButton);
    }

    /**
	 * Hides or shows the data perspective menus.
	 * 
	 * @param enabled
	 *            True if the components should be enabled, false if they should
	 *            be disabled
	 */
    public void showDataPerspectiveOptions(boolean enabled) {
        _commitTransacButton.setEnabled(enabled);
        _rollbackTransacButton.setEnabled(enabled);
    }
}
