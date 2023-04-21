package org.jcvi.platetools.swing.db;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.skin.ModerateSkin;
import org.jcvi.glk.Plate;
import org.jcvi.glk.session.SybaseServer;
import org.jcvi.platetools.HibernatePlateDataResolver;
import org.jcvi.platetools.PlateDataResolver;
import org.jcvi.platetools.swing.PlateListModel;
import org.jcvi.platetools.swing.PlateSelectionSource;
import org.jcvi.platetools.swing.main.PlateToolFrame;
import org.jcvi.platetools.swing.util.CancelAction;

/**
 * 
 *
 * @author jsitz@jcvi.org
 */
public class DatabasePlateSelectDialog extends DatabaseSelectionDialog implements PlateSelectionSource, DatabaseSessionSource {

    /** The Serial Version UID */
    private static final long serialVersionUID = 6777585161774092688L;

    public static void main(String[] args) {
        SubstanceLookAndFeel.setSkin(new ModerateSkin());
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new DatabasePlateSelectDialog(new PlateToolFrame()).setVisible(true);
            }
        });
    }

    /** The {@link PlateToolFrame} which created this dialog. */
    private final PlateToolFrame toolFrame;

    private final PlateListModel plateListModel;

    private final Action selectAction;

    private final DatabaseListAction listAction;

    private final JButton listButton;

    private final JButton cancelButton;

    private final JButton selectButton;

    private final JComboBox serverSelect;

    private final JTextField dbName;

    private final JTextField dbUsername;

    private final JPasswordField dbPassword;

    private final JTable plateTable;

    /**
     * Creates a new <code>DatabasePlateSelectDialog</code>.
     * 
     * @param toolFrame The {@link PlateToolFrame} which created the dialog.
     */
    public DatabasePlateSelectDialog(PlateToolFrame toolFrame) {
        super("Select Plate", toolFrame);
        this.toolFrame = toolFrame;
        this.selectAction = this.getSelectAction();
        this.plateListModel = new PlateListModel();
        this.listAction = new DatabaseListAction(this.plateListModel, this);
        this.plateTable = new JTable(this.plateListModel);
        this.serverSelect = new JComboBox(SybaseServer.values());
        this.dbName = new JTextField(12);
        this.dbUsername = new JTextField(20);
        this.dbPassword = new JPasswordField(20);
        this.listButton = new JButton(this.listAction);
        this.selectButton = new JButton(this.selectAction);
        this.cancelButton = new JButton(new CancelAction(this));
        this.init();
    }

    public PlateToolFrame getParentFrame() {
        return this.toolFrame;
    }

    protected Action getSelectAction() {
        return new AbstractAction("Select") {

            /** The Serial Version UID. */
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                DatabasePlateSelectDialog.this.dispose();
            }
        };
    }

    /**
     * Initialize the contents of this dialog.
     */
    private void init() {
        this.setLayout(new MigLayout());
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.add(new JLabel("Server"), "align right");
        this.add(this.serverSelect, "wrap, pushx 100");
        this.add(new JLabel("Database"), "align right");
        this.add(this.dbName, "wrap, pushx 100");
        this.add(new JLabel("Username"), "align right");
        this.add(this.dbUsername, "wrap, pushx 100");
        this.add(new JLabel("Password"), "align right");
        this.add(this.dbPassword, "pushx 100");
        this.add(this.listButton, "wrap");
        this.initTable();
        this.add(new JScrollPane(this.plateTable), "growx, spanx, pushx, wrap");
        final JPanel buttonPanel = new JPanel(new MigLayout());
        buttonPanel.add(this.cancelButton);
        buttonPanel.add(this.selectButton);
        this.add(buttonPanel, "spanx, growx, pushx");
        this.pack();
        this.setLocationRelativeTo(this.getParent());
    }

    /**
     * Initialize the plate selection table.
     */
    private void initTable() {
        this.plateTable.setAutoCreateRowSorter(true);
        this.plateTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.plateTable.setColumnSelectionAllowed(false);
        this.plateTable.setRowSelectionAllowed(true);
    }

    @Override
    public Plate getPlate() {
        final int selectedIndex = this.plateTable.getRowSorter().convertRowIndexToModel(this.plateTable.getSelectedRow());
        Logger.getLogger("plateTool").info("Selected Plate #" + selectedIndex);
        return this.plateListModel.get(selectedIndex);
    }

    @Override
    public PlateDataResolver getResolver() {
        return new HibernatePlateDataResolver(this.getSession());
    }

    @Override
    protected String getDatabaseServerName() {
        return this.serverSelect.getSelectedItem().toString();
    }

    @Override
    protected String getDatabaseCatalogName() {
        return this.dbName.getText();
    }

    @Override
    protected String getDatabaseUsername() {
        return this.dbUsername.getText();
    }

    @Override
    protected String getDatabasePassword() {
        return String.valueOf(this.dbPassword.getPassword());
    }
}
