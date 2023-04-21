package jgrit.gui.export;

import static jgrit.Constants.ADVANCED_GUI;
import static jgrit.Constants.ANNOUNCEMENT;
import static jgrit.Constants.MAIN_GUI_PROPERTY;
import static jgrit.Constants.SHOW_ANNOUNCEMENT;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import jgrit.JGritProperties;
import jgrit.gui.AreaOptionsPanel;
import jgrit.gui.FileOptionsPanel;
import jgrit.gui.GraphicsOptionsPanel;
import jgrit.gui.MapOptionsPanel;
import jgrit.gui.MetaOptionsPanel;
import jgrit.gui.PaletteOptionsPanel;
import jgrit.options.OptionCollection;
import com.jgoodies.forms.builder.ButtonBarBuilder2;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Advanced GUI for jgrit
 * @author Derek Olejnik
 *
 */
public class GBAExportAdvanced extends ExportDialog {

    private static final long serialVersionUID = 5202839368716131858L;

    /***************************************************************************
	 * 								 VARIABLES
	 **************************************************************************/
    private JPanel mainPanel;

    private JButton basicButton;

    private JTextPane announcementPane;

    /**
	 * @throws HeadlessException
	 */
    public GBAExportAdvanced() throws HeadlessException {
        super();
        initGUI();
    }

    /**
	 * Creates an instance of this window associated with the given parent
	 * @param taskParent - parent Task-style window
	 */
    public GBAExportAdvanced(GBAExportTaskStyle otherGUI) {
        super(otherGUI);
        initGUI();
    }

    /**
	 * Creates an advanced dialog based upon an already created option collection
	 * @param options
	 * @throws HeadlessException
	 */
    public GBAExportAdvanced(OptionCollection options, Frame owner, String title, boolean modal) throws HeadlessException {
        super(options, owner, title, modal);
        SETTINGS = options;
        initGUI();
    }

    /**
	 * Initializes the GUI
	 */
    private void initGUI() {
        try {
            setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            this.setTitle("GBA Export - Advanced (DON'T PANIC)");
            initLayout();
            if (SHOW_ANNOUNCEMENT) {
                initAnnouncement();
            }
            initPanels();
            initButtons();
            pack();
            if (otherGUI == null) {
                otherGUI = new GBAExportTaskStyle(this);
            }
            setLocationRelativeTo(getOwner());
        } catch (Exception e) {
            System.err.println("ERROR INITIALIZING ADVANCED GUI");
            e.printStackTrace();
        }
    }

    /**
	 * Initializes the announcement
	 */
    private void initAnnouncement() throws BadLocationException {
        announcementPane = new JTextPane();
        Document textBackend = new DefaultStyledDocument();
        textBackend.insertString(0, ANNOUNCEMENT, announcementPane.getStyle("default"));
        announcementPane.setDocument(textBackend);
        announcementPane.setEditable(false);
        mainPanel.add(announcementPane, new CellConstraints("1,1,7,1,fill,fill"));
    }

    /**
	 * Initializes the layout of the window
	 */
    private void initLayout() {
        mainPanel = new JPanel();
        FormLayout mainPanelLayout = new FormLayout("$dmargin,p,$rgap,p,$rgap,max(p;$ugap):grow,$dmargin", "pref,$rgap,max(p;$ugap),$rgap,max(p;$ugap),$rgap,max(p;$ugap),$rgap,max(p;$ugap),$rgap,max(p;$ugap),$dmargin");
        mainPanel.setLayout(mainPanelLayout);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    /**
	 * Initializes all the option panels
	 */
    private void initPanels() {
        JPanel graphicsOptionsPanel, paletteOptionsPanel, mapOptionsPanel;
        JPanel metaOptionsPanel, fileOptionsPanel, areaOptionsPanel;
        graphicsOptionsPanel = new GraphicsOptionsPanel(SETTINGS);
        paletteOptionsPanel = new PaletteOptionsPanel(SETTINGS);
        mapOptionsPanel = new MapOptionsPanel(SETTINGS);
        metaOptionsPanel = new MetaOptionsPanel(SETTINGS);
        fileOptionsPanel = new FileOptionsPanel(SETTINGS);
        areaOptionsPanel = new AreaOptionsPanel(SETTINGS);
        graphicsOptionsPanel.setBorder(BorderFactory.createTitledBorder("Graphics"));
        paletteOptionsPanel.setBorder(BorderFactory.createTitledBorder("Palette"));
        mapOptionsPanel.setBorder(BorderFactory.createTitledBorder("Map"));
        metaOptionsPanel.setBorder(BorderFactory.createTitledBorder("Meta-Tile"));
        fileOptionsPanel.setBorder(BorderFactory.createTitledBorder("File"));
        areaOptionsPanel.setBorder(BorderFactory.createTitledBorder("Area"));
        mainPanel.add(graphicsOptionsPanel, new CellConstraints("2,3,1,1,fill,fill"));
        mainPanel.add(paletteOptionsPanel, new CellConstraints("4,3,1,1,fill,fill"));
        mainPanel.add(mapOptionsPanel, new CellConstraints("6,3,1,1,fill,fill"));
        mainPanel.add(metaOptionsPanel, new CellConstraints("6,5,1,1,fill,fill"));
        mainPanel.add(fileOptionsPanel, new CellConstraints("2,5,3,7,fill,fill"));
        mainPanel.add(areaOptionsPanel, new CellConstraints("6,7,1,1,fill,fill"));
    }

    /**
	 * Sets up the Basic, ok, and cancel buttons
	 */
    protected void initButtons() {
        super.initButtons();
        basicButton = new JButton("Basic...");
        basicButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ExportDialog.inst = otherGUI;
                otherGUI.setVisible(true);
                GBAExportAdvanced.this.setVisible(false);
            }
        });
        ButtonBarBuilder2 builder = new ButtonBarBuilder2();
        builder.addGrowing(new JButton[] { okButton, cancelButton });
        mainPanel.add(builder.getPanel(), new CellConstraints("6, 11, 1, 1"));
        mainPanel.add(basicButton, new CellConstraints("6,9,1,1,fill,fill"));
    }

    /**
	 * Any time this window is opened anew, we need to set the default options
	 * for the currently selected task
	 */
    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            JGritProperties.getInstance().setProperty(MAIN_GUI_PROPERTY, ADVANCED_GUI);
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                GBAExportAdvanced.super.setVisible(visible);
            }
        });
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                GBAExportAdvanced inst = new GBAExportAdvanced();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }
}
