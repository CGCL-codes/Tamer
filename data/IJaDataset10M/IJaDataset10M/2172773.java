package dialogs;

import globals.AntiAliasedPanel;
import globals.ConstantInterface;
import globals.ConstantRoutines;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import pagelayout.CellGrid;
import pagelayout.GridRows;
import pagelayout.PanelCell;
import pagelayout.Row;
import plot.PlotChart;

/**
 * Provides the export plot dialog.
 * 
 * @author Luca Petraglio
 * @author Michael Mattes
 */
public class ExportPlotDialog implements ActionListener, ConstantInterface {

    private static ExportPlotDialog uniqueInstance;

    private final int W = 313;

    private final int H = 298;

    private final JFrame frame;

    private JTextField fileNameT;

    private JToggleButton browse;

    private JToggleButton export;

    private JTextField widthT;

    private JTextField heightT;

    private JLabel fileStatusL;

    private static JButton exportButton;

    private static JButton cancel;

    private JComboBox type;

    private static File f;

    private static int status = 1;

    /**
	 * A private Constructor prevents any other class from instantiating.
	 */
    private ExportPlotDialog() {
        frame = new JFrame("Export Plot");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        Point screenCenter = ConstantRoutines.getScreenCenter();
        frame.setLocation((int) (screenCenter.getX() - W / 2), (int) (screenCenter.getY() - H / 2));
        final ImageIcon icon = new ImageIcon(this.getClass().getResource("/img/crocus-icon.gif"));
        frame.setIconImage(icon.getImage());
        frame.getRootPane().registerKeyboardAction(this, "cancel", ESC, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    /**
	 *  Get the instance of class.
	 * 
	 *  @return uniqueInstance instance of class
	 */
    public static ExportPlotDialog getObject() {
        if (uniqueInstance == null) {
            uniqueInstance = new ExportPlotDialog();
        }
        return uniqueInstance;
    }

    /**
	 * Build the frame using PageLayout as layout and show the frame.
	 */
    public void build(final JToggleButton export) {
        this.export = export;
        final Container container = frame.getContentPane();
        final GridRows mainRows = new GridRows();
        mainRows.newRow().add(headerFrame());
        mainRows.newRow().add(fileName());
        mainRows.newRow().add(properties());
        mainRows.newRow().add(createButtons());
        final CellGrid cellgrid = mainRows.createCellGrid();
        cellgrid.createLayout(container);
        frame.pack();
        frame.setSize(frame.getPreferredSize());
        frame.setVisible(true);
        frame.getRootPane().setDefaultButton(exportButton);
    }

    /**
	 * Provides a JPanel with a JLabel and an icon.
	 * 
	 * @return PanelCell
	 */
    public PanelCell headerFrame() {
        final JPanel panel = new AntiAliasedPanel();
        panel.setBackground(new Color(255, 255, 255));
        Border border = BorderFactory.createLineBorder(new Color(137, 137, 137));
        border = BorderFactory.createTitledBorder(border, "title");
        final Icon icon = new ImageIcon(this.getClass().getResource(SMALL_ICONS_PATH + "gimp.png"));
        final JLabel img = new JLabel("");
        img.setIcon(icon);
        final JLabel labelHeader = new JLabel("<html><b><font color=\"#736AFF\" size=4 >Export Plot");
        final GridRows rows = new GridRows();
        rows.newRow().add(Row.RIGHT, Row.NO_ALIGNMENT, labelHeader, img);
        final CellGrid grid = rows.createCellGrid();
        return new PanelCell(panel, grid);
    }

    /**
	 * Provides a JPanel with border, JLabel, JTextField and JButton.
	 * 
	 * @return PanelCell
	 */
    private PanelCell fileName() {
        final JPanel panel = new AntiAliasedPanel();
        final Border border = BorderFactory.createTitledBorder("File");
        panel.setBorder(border);
        final JLabel fileNameL = new JLabel("<html>File Name: ");
        fileNameT = ConstantRoutines.createTextField("", 13);
        final Icon brosweIcon = new ImageIcon(this.getClass().getResource(SMALL_ICONS_PATH + "browse.png"));
        browse = new JToggleButton("<html>Browse", brosweIcon);
        browse.setActionCommand("browse");
        browse.addActionListener(this);
        fileStatusL = new JLabel("                        ");
        final GridRows rows = new GridRows();
        rows.newRow().add(fileNameL);
        rows.newRow().add(fileNameT).add(browse);
        rows.newRow().add(fileStatusL);
        final CellGrid grid = rows.createCellGrid();
        return new PanelCell(panel, grid);
    }

    /**
	 * Provides a JPanel with border, 3 JLabel, 2 JCheckBox and 1 JtextField
	 * 
	 * @return PanelCell
	 */
    private PanelCell properties() {
        final JPanel panel = new AntiAliasedPanel();
        final Border border = BorderFactory.createTitledBorder("Properties");
        panel.setBorder(border);
        final JLabel widthL = new JLabel("<html>Width: ");
        final JLabel heightL = new JLabel("<html>Height: ");
        widthT = ConstantRoutines.createTextField("640", 3);
        heightT = ConstantRoutines.createTextField("480", 3);
        final JLabel typeL = new JLabel("<html>Image type:");
        type = new JComboBox();
        final String jpg = "jpg";
        final String png = "png";
        type.addItem(png);
        type.addItem(jpg);
        final GridRows rows = new GridRows();
        rows.newRow().add(widthL).add(widthT).add(typeL).add(type);
        rows.newRow().add(heightL).add(heightT);
        final CellGrid grid = rows.createCellGrid();
        return new PanelCell(panel, grid);
    }

    /**
	 * Provides a JPanel with 2 buttons.
	 * 
	 * @return PanelCell
	 */
    public PanelCell createButtons() {
        final JPanel panel = new AntiAliasedPanel();
        final Icon cancelIcon = new ImageIcon(this.getClass().getResource(SMALL_ICONS_PATH + "process-stop.png"));
        cancel = new JButton("<html>Cancel", cancelIcon);
        cancel.setActionCommand("cancel");
        cancel.addActionListener(this);
        final Icon okIcon = new ImageIcon(this.getClass().getResource(SMALL_ICONS_PATH + "go-down.png"));
        exportButton = new JButton("<html>Export", okIcon);
        exportButton.setActionCommand("finish");
        exportButton.addActionListener(this);
        exportButton.setEnabled(false);
        final JLabel fake = new JLabel("                    ");
        final GridRows rows = new GridRows();
        rows.newRow().add(Row.LEFT, Row.NO_ALIGNMENT, cancel).add(fake).add(exportButton);
        final CellGrid grid = rows.createCellGrid();
        return new PanelCell(panel, grid);
    }

    /**
	 * Main method for class's test
	 * 
	 * @param args
	 */
    public static void main(final String args[]) {
        final ExportPlotDialog dialog = ExportPlotDialog.getObject();
        dialog.build(null);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if ("browse".equals(e.getActionCommand())) {
            final FileChooser fileChooser = FileChooser.getInstance();
            status = fileChooser.save(browse, "File to Export the Plot");
            if (status == 0) {
                f = fileChooser.getF();
                fileNameT.setText(f.getName());
                exportButton.setEnabled(true);
            }
        } else if ("finish".equals(e.getActionCommand())) {
            final PlotChart chart = PlotChart.getObject();
            if (type.getSelectedItem().toString().equalsIgnoreCase("jpg")) {
                chart.saveImage(f.getAbsolutePath(), true, new Dimension(Integer.parseInt(widthT.getText()), Integer.parseInt(heightT.getText())));
            } else {
                chart.saveImage(f.getAbsolutePath(), false, new Dimension(Integer.parseInt(widthT.getText()), Integer.parseInt(heightT.getText())));
            }
            export.setSelected(false);
            frame.dispose();
            uniqueInstance = null;
        } else if ("cancel".equals(e.getActionCommand())) {
            export.setSelected(false);
            frame.dispose();
            uniqueInstance = null;
        }
    }
}
