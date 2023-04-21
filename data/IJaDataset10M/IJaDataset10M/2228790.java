package net.sourceforge.omov.app.gui.scan;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;
import net.sourceforge.jpotpourri.jpotface.IPtEscapeDisposeReceiver;
import net.sourceforge.jpotpourri.jpotface.PtEscapeDisposer;
import net.sourceforge.jpotpourri.jpotface.button.PtContextMenuButton;
import net.sourceforge.jpotpourri.jpotface.table.IPtTableBodyContextListener;
import net.sourceforge.jpotpourri.jpotface.table.PtTableBodyContext;
import net.sourceforge.jpotpourri.jpotface.util.PtGuiUtil;
import net.sourceforge.omov.app.gui.comp.FolderChooseButton;
import net.sourceforge.omov.app.gui.comp.IFolderChooseListener;
import net.sourceforge.omov.app.gui.comp.OmovContextMenuButton;
import net.sourceforge.omov.app.util.AppImageFactory;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.Icon16x16;
import net.sourceforge.omov.core.bo.CheckedMovie;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.guicore.GuiKeyAdapter;
import net.sourceforge.omov.guicore.OmovGuiUtil;
import net.sourceforge.omov.logic.prefs.PreferencesDao;
import net.sourceforge.omov.logic.tools.scan.ScanHint;
import net.sourceforge.omov.logic.tools.scan.ScannedMovie;
import net.sourceforge.omov.qtjApi.QtjFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class ScanDialog extends JDialog implements IPtTableBodyContextListener, IPtEscapeDisposeReceiver, IFolderChooseListener {

    private static final Log LOG = LogFactory.getLog(ScanDialog.class);

    private static final long serialVersionUID = 8730290488508038854L;

    private static final String CMD_CONTEXT_FETCH_METADATA = "CMD_FETCH_METADATA";

    private static final String CMD_CONTEXT_REMOVE_METADATA = "CMD_REMOVE_METADATA";

    private static final String CMD_PLAY_QUICKVIEW = "CMD_PLAY_QUICKVIEW";

    private static final String DEFAULT_SCAN_ROOT_PATH_TEXT = "Select Scan Root";

    private static final int MARGIN_TOP = 12;

    private static final int MARGIN_LEFT = 12;

    private static final int MARGIN_BOTTOM = 12;

    private static final int MARGIN_RIGHT = 12;

    private final ScanDialogController controller = new ScanDialogController(this);

    private final FolderChooseButton btnSetScanFolder = new FolderChooseButton(this);

    private String scanRootFolder;

    private final JTextField txtScanFolder = new JTextField(DEFAULT_SCAN_ROOT_PATH_TEXT, 22);

    private final JProgressBar progressBar = new JProgressBar();

    private final JButton btnDoScan = new JButton("Scan");

    private final JCheckBox inpFetchMetadata = new JCheckBox("Fetch Metadata");

    private final JButton btnDoImportMovies = new JButton("Import");

    private final ScannedMovieTableModel tblScannedMovieModel = new ScannedMovieTableModel();

    private final ScannedMovieTable tblScannedMovie = new ScannedMovieTable(this.tblScannedMovieModel);

    private final ScanHintTableModel tblHintsModel = new ScanHintTableModel();

    private final ScanHintTable tblHints = new ScanHintTable(this.tblHintsModel);

    private JSplitPane tableSplitter;

    private PtContextMenuButton btnAdvancedOptions;

    private JMenuItem itemPrepareFolder;

    private JMenuItem itemSelectAll;

    private JMenuItem itemSelectNone;

    public ScanDialog(JFrame owner) {
        super(owner, "Scan Repository", false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(final WindowEvent event) {
                controller.doClose();
            }
        });
        PtEscapeDisposer.enableEscape(this.getRootPane(), this);
        PtEscapeDisposer.enableEscape(this.tblScannedMovie, this);
        final List<JMenuItem> itemsSingle = new ArrayList<JMenuItem>();
        PtTableBodyContext.newJMenuItem(itemsSingle, "Fetch Metadata", CMD_CONTEXT_FETCH_METADATA, AppImageFactory.getInstance().getIcon(Icon16x16.FETCH_METADATA));
        PtTableBodyContext.newJMenuItem(itemsSingle, "Remove Metadata", CMD_CONTEXT_REMOVE_METADATA);
        if (QtjFactory.isQtjAvailable()) {
            PtTableBodyContext.newJMenuItem(itemsSingle, "QuickView", CMD_PLAY_QUICKVIEW, AppImageFactory.getInstance().getIcon(Icon16x16.QUICKVIEW));
        }
        new PtTableBodyContext(this.tblScannedMovie, itemsSingle, null, this);
        this.getRootPane().setDefaultButton(this.btnDoScan);
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(true);
        OmovGuiUtil.lockOriginalSizeAsMinimum(this);
        PtGuiUtil.setCenterLocation(this);
    }

    void updateScannedMovie(ScannedMovie confirmedScannedMovie) {
        this.tblScannedMovieModel.updateMovieByFolderPath(confirmedScannedMovie);
    }

    @Override
    public JFrame getOwner() {
        return (JFrame) super.getOwner();
    }

    private JPanel initComponents() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.getColorWindowBackground());
        panel.add(this.panelNorth(), BorderLayout.NORTH);
        panel.add(this.panelCenter(), BorderLayout.CENTER);
        panel.add(this.panelSouth(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel panelNorth() {
        final Image img = AppImageFactory.getInstance().getBigScanImage();
        final JPanel panelCenter = new JPanel(new BorderLayout()) {

            private static final long serialVersionUID = 0L;

            @Override
            public void paint(Graphics g) {
                g.drawImage(img, 0, 0, this);
                super.paint(g);
            }
        };
        panelCenter.setOpaque(false);
        {
            final JPanel panelCenterNorth = new JPanel();
            panelCenterNorth.setLayout(new BoxLayout(panelCenterNorth, BoxLayout.Y_AXIS));
            panelCenterNorth.setOpaque(false);
            final JLabel lblHeader = new JLabel("Scan Repository");
            lblHeader.setFont(Constants.getFontHeader1());
            lblHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            panelCenterNorth.add(lblHeader);
            final JLabel lblMain1 = new JLabel("Just select a root folder to scan for movies.");
            panelCenterNorth.add(lblMain1);
            final JLabel lblMain2 = new JLabel("Afterwards, you can import all selected entries at once.");
            panelCenterNorth.add(lblMain2);
            List<JMenuItem> advancedOptions = new ArrayList<JMenuItem>(1);
            this.itemPrepareFolder = PtGuiUtil.newMenuItem("Prepare Folder", ScanDialogController.CMD_OPTIONS_PREPARE_FOLDER, advancedOptions);
            this.itemPrepareFolder.setToolTipText("Create necessary folders for scan root");
            this.itemSelectAll = PtGuiUtil.newMenuItem("Select All", ScanDialogController.CMD_SELECT_ALL, advancedOptions);
            this.itemSelectNone = PtGuiUtil.newMenuItem("Unselect All", ScanDialogController.CMD_SELECT_NONE, advancedOptions);
            this.itemPrepareFolder.setEnabled(false);
            this.itemSelectAll.setEnabled(false);
            this.itemSelectNone.setEnabled(false);
            this.btnAdvancedOptions = new OmovContextMenuButton(advancedOptions, this.controller);
            this.btnAdvancedOptions.setOpaque(false);
            final JPanel panelCenterSouth = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelCenterSouth.setOpaque(false);
            final JLabel lblAdvancedOptions = new JLabel("Advanced Options");
            lblAdvancedOptions.setFont(Constants.getFontSmall());
            panelCenterSouth.add(lblAdvancedOptions);
            panelCenterSouth.add(this.btnAdvancedOptions);
            panelCenter.add(panelCenterNorth, BorderLayout.NORTH);
            panelCenter.add(panelCenterSouth, BorderLayout.SOUTH);
        }
        final JPanel panelEast = new JPanel();
        {
            final GridBagLayout layout = new GridBagLayout();
            final GridBagConstraints c = new GridBagConstraints();
            layout.setConstraints(panelEast, c);
            panelEast.setLayout(layout);
            panelEast.setOpaque(false);
            this.btnSetScanFolder.setInitialPath(PreferencesDao.getInstance().getRecentScanPath());
            this.btnSetScanFolder.addFolderChooseListener(this);
            this.txtScanFolder.setHorizontalAlignment(JTextField.RIGHT);
            this.txtScanFolder.setEditable(false);
            this.txtScanFolder.setForeground(Constants.getColorDarkGray());
            this.txtScanFolder.setBackground(Constants.getColorWindowBackground());
            this.txtScanFolder.setBorder(BorderFactory.createEmptyBorder());
            c.fill = GridBagConstraints.NONE;
            c.anchor = GridBagConstraints.LAST_LINE_END;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(0, 0, 0, 16);
            panelEast.add(this.btnSetScanFolder, c);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.LAST_LINE_START;
            c.gridx = 0;
            c.gridy = 1;
            c.insets = new Insets(0, 0, 0, 0);
            panelEast.add(this.txtScanFolder, c);
        }
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(MARGIN_TOP, MARGIN_LEFT, 0, MARGIN_RIGHT));
        panel.add(panelCenter, BorderLayout.CENTER);
        panel.add(panelEast, BorderLayout.EAST);
        return panel;
    }

    private JPanel panelCenter() {
        final JPanel panel = new JPanel(new BorderLayout());
        this.tblScannedMovieModel.setColumnModel(this.tblScannedMovie.getColumnModel());
        this.tblScannedMovie.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        this.tblScannedMovie.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent event) {
                final int row = tblScannedMovie.getSelectedRow();
                if (row > -1 && event.getClickCount() >= 2) {
                    final int columnIndex = tblScannedMovie.columnAtPoint(event.getPoint());
                    final TableColumn column = tblScannedMovie.getColumnModel().getColumn(columnIndex);
                    if (column.getHeaderValue().equals(ScannedMovieTableModel.TABLE_COLUMN_VALUE_MOVIE_CHECKED)) {
                        LOG.debug("Ignoring doubleclick on table because it seems as checkbox was clicked (selected column=" + tblScannedMovie.getSelectedColumn() + ").");
                    } else {
                        final ScannedMovie selectedMovie = tblScannedMovieModel.getMovieAt(tblScannedMovie.getSelectedRow());
                        controller.doEditScannedMovie(selectedMovie);
                    }
                }
            }
        });
        this.tblScannedMovie.getColumnModel().getColumn(0).setMaxWidth(20);
        this.tblScannedMovie.getColumnModel().getColumn(0).setMinWidth(20);
        if (QtjFactory.isQtjAvailable()) {
            this.tblScannedMovie.addKeyListener(new GuiKeyAdapter() {

                @Override
                public void keyReleasedAction(final KeyEvent event) {
                    final int code = event.getKeyCode();
                    LOG.debug("scan table got key event with code " + code + " (" + event.getKeyChar() + ").");
                    if (code == KeyEvent.VK_SPACE && QtjFactory.isQtjAvailable()) {
                        LOG.debug("key event: space");
                        final List<CheckedMovie> selectedMovies = tblScannedMovie.getSelectedMovies();
                        if (selectedMovies.size() == 1) {
                            controller.doPlayQuickView(selectedMovies.get(0));
                        } else {
                            LOG.debug("Can not quickview because selected movies != 1, but: " + selectedMovies.size());
                            Toolkit.getDefaultToolkit().beep();
                        }
                    }
                }
            });
        }
        this.tblHints.getColumnModel().getColumn(0).setMinWidth(70);
        this.tblHints.getColumnModel().getColumn(0).setMaxWidth(70);
        this.tblHints.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        final JScrollPane paneHints = OmovGuiUtil.wrapScroll(this.tblHints, 300, 20);
        paneHints.setMinimumSize(new Dimension(0, 0));
        this.tableSplitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, OmovGuiUtil.wrapScroll(this.tblScannedMovie, 400, 140), paneHints);
        this.tableSplitter.setOneTouchExpandable(true);
        this.tableSplitter.setBorder(null);
        this.tableSplitter.setBackground(Constants.getColorWindowBackground());
        this.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent event) {
                tableSplitter.setDividerLocation(700);
            }
        });
        panel.add(this.tableSplitter, BorderLayout.CENTER);
        return panel;
    }

    private JPanel panelSouth() {
        this.btnDoScan.setOpaque(false);
        this.inpFetchMetadata.setOpaque(false);
        this.btnDoImportMovies.setOpaque(false);
        this.progressBar.setOpaque(false);
        this.btnDoScan.setEnabled(false);
        this.inpFetchMetadata.setEnabled(false);
        this.btnDoImportMovies.setEnabled(false);
        this.btnDoScan.setActionCommand(ScanDialogController.CMD_SCAN);
        this.btnDoImportMovies.setActionCommand(ScanDialogController.CMD_IMPORT_MOVIES);
        this.btnDoScan.addActionListener(this.controller);
        this.btnDoImportMovies.addActionListener(this.controller);
        this.progressBar.setPreferredSize(new Dimension(400, (int) this.progressBar.getPreferredSize().getHeight()));
        this.progressBar.setIndeterminate(false);
        this.progressBar.setString("");
        this.progressBar.setStringPainted(true);
        final JPanel panel = new JPanel(new BorderLayout(20, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(10, MARGIN_LEFT, MARGIN_BOTTOM, MARGIN_RIGHT));
        panel.setOpaque(false);
        final JPanel panelWest = new JPanel();
        panelWest.setOpaque(false);
        panelWest.add(this.btnDoScan);
        panelWest.add(this.inpFetchMetadata);
        panel.add(panelWest, BorderLayout.WEST);
        panel.add(this.progressBar, BorderLayout.CENTER);
        final JPanel panelEast = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelEast.setOpaque(false);
        panelEast.add(this.btnDoImportMovies);
        panel.add(panelEast, BorderLayout.EAST);
        return panel;
    }

    /**
     * invoked by controller if user hits scan button
     */
    void doScanStart() {
        assert (this.scanRootFolder != null);
        final File scanRoot = new File(this.scanRootFolder);
        this.enableUi(false);
        this.getRootPane().setDefaultButton(this.btnDoImportMovies);
        this.controller.doScan(scanRoot, this.inpFetchMetadata.isSelected());
    }

    /**
     * used by scan-start/stop to en/disable whole user interface
     */
    private void enableUi(final boolean enabled) {
        this.btnAdvancedOptions.setEnabled(enabled);
        this.btnSetScanFolder.setEnabled(enabled);
        this.inpFetchMetadata.setEnabled(enabled);
        this.btnDoScan.setEnabled(enabled);
        this.btnDoImportMovies.setEnabled(enabled);
    }

    void doScanCompleted(List<ScannedMovie> scannedMovies, List<ScanHint> hints) {
        this.enableUi(true);
        this.progressBar.setString("Finished");
        this.tblScannedMovieModel.setData(scannedMovies);
        this.tblHintsModel.setData(hints);
        if (scannedMovies.size() == 0) {
            this.tableSplitter.setDividerLocation(0.4);
            this.itemSelectAll.setEnabled(false);
            this.itemSelectNone.setEnabled(false);
            PtGuiUtil.info(this, "Scan Finished", "There could be not any movie found to be imported.");
        } else {
            this.itemSelectAll.setEnabled(true);
            this.itemSelectNone.setEnabled(true);
        }
    }

    void doSelect(boolean selectAll) {
        final int n = this.tblScannedMovieModel.getRowCount();
        for (int i = 0; i < n; i++) {
            final ScannedMovie movie = this.tblScannedMovieModel.getMovieAt(i);
            movie.setChecked(selectAll);
        }
        this.tblScannedMovie.repaint();
    }

    JProgressBar getProgressBar() {
        return this.progressBar;
    }

    List<Movie> getCheckedMovies() {
        return this.tblScannedMovieModel.getCheckedMovies();
    }

    public void contextMenuClicked(JMenuItem item, int tableRowSelected) {
        final String cmd = item.getActionCommand();
        final ScannedMovie movie = this.tblScannedMovieModel.getMovieAt(tableRowSelected);
        if (cmd.equals(CMD_CONTEXT_FETCH_METADATA)) {
            this.controller.doFetchMetaData(movie);
        } else if (cmd.equals(CMD_CONTEXT_REMOVE_METADATA)) {
            this.controller.doRemoveMetaData(movie);
        } else if (cmd.equals(CMD_PLAY_QUICKVIEW)) {
            this.controller.doPlayQuickView(movie);
        } else {
            throw new IllegalArgumentException("unhandled action command '" + cmd + "'!");
        }
    }

    public void contextMenuClickedMultiple(JMenuItem item, int[] tableRowsSelected) {
        throw new UnsupportedOperationException();
    }

    public void doEscape() {
        this.controller.doClose();
    }

    public void notifyFolderCleared() {
        this.scanRootFolder = null;
        this.txtScanFolder.setText(DEFAULT_SCAN_ROOT_PATH_TEXT);
        this.itemPrepareFolder.setEnabled(false);
        this.btnDoScan.setEnabled(false);
        this.inpFetchMetadata.setEnabled(false);
    }

    public void notifyFolderSelected(File folder) {
        PreferencesDao.getInstance().setRecentScanPath(folder.getParent());
        this.scanRootFolder = folder.getAbsolutePath();
        this.txtScanFolder.setText(this.scanRootFolder);
        this.itemPrepareFolder.setEnabled(true);
        this.btnDoScan.setEnabled(true);
        this.inpFetchMetadata.setEnabled(true);
    }

    String getScanRootFolder() {
        return this.scanRootFolder;
    }

    public static void main(String[] args) {
        new ScanDialog(new JFrame()).setVisible(true);
    }
}
