package coda.gui;

import coda.gui.utils.FileNameExtensionFilter;
import coda.io.WorkspaceIO;
import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author marc
 */
public class CoDaPackMenuBar extends JMenuBar {

    private JMenu menuFile;

    private final String ITEM_FILE = "File";

    private JMenuItem itemOpen;

    private final String ITEM_OPEN = "Open Workspace...";

    private JMenuItem itemSave;

    private final String ITEM_SAVE = "Save Workspace...";

    private JMenuItem itemSaveAs;

    private final String ITEM_SAVEAS = "Save Workspace As...";

    private JMenuItem itemCloseWS;

    private final String ITEM_CLOSEWS = "Close Workspace";

    private JMenuItem itemImport;

    private final String ITEM_IMPORT = "Import Data...";

    private JMenuItem itemExport;

    private final String ITEM_EXPORT = "Export Table...";

    private JMenuItem itemConfiguration;

    private final String ITEM_CONF = "Configuration";

    private JMenuItem itemQuit;

    private final String ITEM_QUIT = "Quit CoDaPack";

    private JMenu menuDataFrame;

    private final String ITEM_DATAFRAME = "DataFrames";

    private JMenuItem itemNewDF;

    private final String ITEM_NEWDF = "New DataFrame";

    private JMenuItem itemdelDataFrame;

    private final String ITEM_DEL_DATAFRAME = "Delete active DataFrame";

    private JMenu menuData;

    private final String ITEM_DATA = "Data";

    private JMenu menuTransforms;

    private final String ITEM_TRANS = "Transformations";

    private JMenuItem itemTransformALR;

    private final String ITEM_RAW_ALR = "ALR";

    private JMenuItem itemTransformCLR;

    private final String ITEM_RAW_CLR = "CLR";

    private JMenuItem itemTransformILR;

    private final String ITEM_RAW_ILR = "ILR";

    private JMenuItem itemCenter;

    private final String ITEM_CENTER = "Centering";

    private JMenuItem itemClosure;

    private final String ITEM_CLOSURE = "Subcomposition/Closure";

    private JMenuItem itemAmalgamation;

    private final String ITEM_AMALGAM = "Amalgamation";

    private JMenuItem itemPerturbate;

    private final String ITEM_PERTURBATE = "Perturbation";

    private JMenuItem itemPower;

    private final String ITEM_POWER = "Power transformation";

    private JMenuItem itemZeros;

    private final String ITEM_ZEROS = "Rounded zero replacement";

    private JMenuItem itemCategorizeVariables;

    private final String ITEM_CAT_VAR = "Numeric to categorical";

    private JMenuItem itemNumerizeVariables;

    private final String ITEM_NUM_VAR = "Categorical to Numeric";

    private JMenuItem itemAddVariables;

    private final String ITEM_ADD_VAR = "Add Numeric Variables";

    private JMenuItem itemDeleteVariables;

    private final String ITEM_DEL_VAR = "Delete variables";

    private JMenu menuStatistics;

    private final String ITEM_STATS = "Statistics";

    private JMenuItem itemCompStatsSummary;

    private final String ITEM_COMP_STATS_SUMMARY = "Compositional statistics summary";

    private JMenuItem itemClasStatsSummary;

    private final String ITEM_CLAS_STATS_SUMMARY = "Classical statistics summary";

    private JMenuItem itemNormalityTest;

    private final String ITEM_NORM_TEST = "Additive Logistic Normality Tests";

    private JMenuItem itemAtipicalityIndex;

    private final String ITEM_ATIP_INDEX = "Atipicality index";

    private JMenu menuGraphs;

    private final String ITEM_GRAPHS = "Graphs";

    private JMenuItem itemTernaryPlot;

    private final String ITEM_TERNARY_PLOT = "Ternary plot";

    private JMenuItem itemEmptyTernaryPlot;

    private final String ITEM_EMPTY_TERNARY_PLOT = "Ternary plot [Empty]";

    private JMenuItem itemBiPlot;

    private final String ITEM_BIPLOT = "CLR biplot";

    private JMenuItem itemDendrogramPlot;

    private final String ITEM_DENDROGRAM_PLOT = "Balance dendrogram";

    private JMenuItem itemALRPlot;

    private final String ITEM_ALR_PLOT = "ALR plot";

    private JMenuItem itemCLRPlot;

    private final String ITEM_CLR_PLOT = "CLR plot";

    private JMenuItem itemILRPlot;

    private final String ITEM_ILR_PLOT = "ILR plot";

    private JMenuItem principalComponentPlot;

    private final String ITEM_PC_PLOT = "Ternary Principal Components";

    private JMenuItem predictiveRegionPlot;

    private final String ITEM_PRED_REG_PLOT = "Predictive Region";

    private JMenuItem confidenceRegionPlot;

    private final String ITEM_CONF_REG_PLOT = "Center Confidence Region";

    private JMenu menuDistributions;

    private final String ITEM_DISTRIBUTIONS = "Distributions";

    private JMenu sampleGenerators;

    private final String SAMPLE_GENERATORS = "Random sample generators";

    private JMenuItem itemAdditiveLogisticNormal;

    private final String ITEM_ALN_DISTRIBUTION = "Additive Logistic Normal";

    private JFileChooser chooseFile;

    CoDaPackManager manager;

    ButtonGroup group = new ButtonGroup();

    public CoDaPackMenuBar(CoDaPackManager manager) {
        this.manager = manager;
        menuFile = new JMenu();
        itemOpen = new JMenuItem();
        itemSave = new JMenuItem();
        itemSaveAs = new JMenuItem();
        itemCloseWS = new JMenuItem();
        itemNewDF = new JMenuItem();
        itemImport = new JMenuItem();
        itemExport = new JMenuItem();
        itemdelDataFrame = new JMenuItem();
        itemConfiguration = new JMenuItem();
        itemQuit = new JMenuItem();
        menuDataFrame = new JMenu();
        menuData = new JMenu();
        menuTransforms = new JMenu();
        itemTransformALR = new JMenuItem();
        itemTransformCLR = new JMenuItem();
        itemTransformILR = new JMenuItem();
        itemCenter = new JMenuItem();
        itemClosure = new JMenuItem();
        itemAmalgamation = new JMenuItem();
        itemPerturbate = new JMenuItem();
        itemPower = new JMenuItem();
        itemZeros = new JMenuItem();
        itemCategorizeVariables = new JMenuItem();
        itemNumerizeVariables = new JMenuItem();
        itemAddVariables = new JMenuItem();
        itemDeleteVariables = new JMenuItem();
        menuStatistics = new JMenu();
        itemCompStatsSummary = new JMenuItem();
        itemClasStatsSummary = new JMenuItem();
        itemNormalityTest = new JMenuItem();
        itemAtipicalityIndex = new JMenuItem();
        menuGraphs = new JMenu();
        itemTernaryPlot = new JMenuItem();
        itemEmptyTernaryPlot = new JMenuItem();
        itemBiPlot = new JMenuItem();
        itemDendrogramPlot = new JMenuItem();
        itemALRPlot = new JMenuItem();
        itemCLRPlot = new JMenuItem();
        itemILRPlot = new JMenuItem();
        principalComponentPlot = new JMenuItem();
        predictiveRegionPlot = new JMenuItem();
        confidenceRegionPlot = new JMenuItem();
        menuDistributions = new JMenu();
        sampleGenerators = new JMenu();
        itemAdditiveLogisticNormal = new JMenuItem();
        menuFile.setText(ITEM_FILE);
        addJMenuItem(menuFile, itemOpen, ITEM_OPEN);
        addJMenuItem(menuFile, itemSave, ITEM_SAVE);
        add(menuFile);
        menuDataFrame.setText(ITEM_DATAFRAME);
        add(menuDataFrame);
        chooseFile = new JFileChooser();
    }

    private void addJMenuItem(JMenu menu, JMenuItem item, String title) {
        menu.add(item);
        item.setText(title);
        item.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemActionPerformed(evt);
            }
        });
    }

    public void addDataFrame(String name) {
        JCheckBoxMenuItem a = new JCheckBoxMenuItem();
        group.add(a);
        addJMenuItem(menuDataFrame, a, "- " + name);
        a.setSelected(true);
    }

    void itemActionPerformed(ActionEvent ev) {
        JMenuItem jMenuItem = (JMenuItem) ev.getSource();
        String title = jMenuItem.getText();
        if (title.equals(ITEM_OPEN)) {
            chooseFile.resetChoosableFileFilters();
            chooseFile.setFileFilter(new FileNameExtensionFilter("CoDaPack Workspace", "cdp"));
            if (chooseFile.showOpenDialog(manager.tablePanel) == JFileChooser.APPROVE_OPTION) {
                WorkspaceIO.openWorkspace(chooseFile.getSelectedFile().getAbsolutePath(), manager);
            }
        }
    }
}
