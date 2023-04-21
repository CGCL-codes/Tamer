package magictool.cluster;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.RandomAccessFile;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import magictool.ExpFile;
import magictool.FileComboBox;
import magictool.GeneCreator;
import magictool.GeneCreatorFrame;
import magictool.Project;
import magictool.VerticalLayout;

/**
 * ClusterFrame is a frame which allows the user to create a cluster file. ClusterFrame
 * provides all the options for a user and then picks the correct cluster and starts the
 * clustering process. It implements the GeneCreator interface as it is possible to create
 * a gene for a SupervisedQTCluster.
 */
public class ClusterFrame extends JInternalFrame implements GeneCreator, KeyListener {

    private VerticalLayout verticalLayout1 = new VerticalLayout();

    private JButton cluscancelButton = new JButton("Cancel");

    private JPanel confirmPanel = new JPanel();

    private JButton clusokButton = new JButton("OK");

    private JLabel clusterStatusLabel = new JLabel();

    private JPanel titlePanel = new JPanel();

    private ButtonGroup clustButtons = new ButtonGroup();

    private TitledBorder titledBorder1;

    private VerticalLayout verticalLayout6 = new VerticalLayout();

    private Border border1;

    private Border border2;

    private Border border3;

    private JPanel paramPanel = new JPanel();

    private JPanel jPanel5 = new JPanel();

    private CardLayout cardParam = new CardLayout();

    private FlowLayout flowLayout3 = new FlowLayout();

    private JPanel hierPanel = new JPanel();

    private JComboBox hierStyle = new JComboBox();

    private JLabel linkageLabel = new JLabel();

    private JTextField minClusterField = new JTextField();

    private JTextField maxClusterField = new JTextField();

    private JTextField threshField = new JTextField();

    private FlowLayout flowLayout7 = new FlowLayout();

    private JPanel minClusterPanel = new JPanel();

    private JPanel qtMinPanel = new JPanel();

    private FlowLayout flowLayout4 = new FlowLayout();

    private VerticalLayout verticalLayout5 = new VerticalLayout();

    private VerticalLayout verticalLayout4 = new VerticalLayout();

    private FlowLayout flowLayout1 = new FlowLayout();

    private JPanel threshPane = new JPanel();

    private JPanel numPane = new JPanel();

    private JPanel qtPanel = new JPanel();

    private JLabel minClusterLabel = new JLabel();

    private JLabel maxClusterLabel = new JLabel();

    private JLabel thresshLabel = new JLabel();

    private JPanel thresPane = new JPanel();

    private JPanel superPanel = new JPanel();

    private JPanel kmeansPanel = new JPanel();

    private Border border4;

    private TitledBorder titledBorder2;

    private TitledBorder titledBorder3;

    private JPanel expinfoPanel = new JPanel();

    private JPanel outputPanel = new JPanel();

    private JPanel numPanel = new JPanel();

    private JPanel methodInfoPanel = new JPanel();

    private JLabel numIs = new JLabel();

    private FlowLayout flowLayout8 = new FlowLayout();

    private FlowLayout flowLayout6 = new FlowLayout();

    private JLabel expInfo = new JLabel();

    private FlowLayout flowLayout2 = new FlowLayout();

    private JPanel expInfoPanel = new JPanel();

    private JLabel clusterLabel = new JLabel();

    private CardLayout cardFile = new CardLayout();

    private VerticalLayout verticalLayout8 = new VerticalLayout();

    private JPanel disinfoPanel = new JPanel();

    private VerticalLayout verticalLayout7 = new VerticalLayout();

    private JPanel inputPanel = new JPanel();

    private JPanel filepanel = new JPanel();

    private VerticalLayout verticalLayout3 = new VerticalLayout();

    private VerticalLayout verticalLayout2 = new VerticalLayout();

    private JLabel numGeneExp = new JLabel();

    private JPanel jPanel3 = new JPanel();

    private JLabel methodInfo = new JLabel();

    private JLabel methodInfoLabel = new JLabel();

    private JLabel numLabel = new JLabel();

    private BorderLayout borderLayout5 = new BorderLayout();

    private JTextField outnameField = new JTextField();

    private BorderLayout borderLayout2 = new BorderLayout();

    private JLabel expInfoLabel = new JLabel();

    private JPanel outnamePanel = new JPanel();

    private JPanel jPanel4 = new JPanel();

    private JComboBox clusterCombo = new JComboBox();

    private JLabel methodLabel = new JLabel();

    private BorderLayout borderLayout6 = new BorderLayout();

    private VerticalLayout verticalLayout9 = new VerticalLayout();

    private JLabel disFileLabel = new JLabel();

    private JPanel namePanel = new JPanel();

    private FileComboBox disComboBox;

    private BorderLayout borderLayout1 = new BorderLayout();

    private JLabel expFileLabel = new JLabel();

    private JPanel jPanel2 = new JPanel();

    private FileComboBox expComboBox;

    private JPanel jPanel1 = new JPanel();

    private BorderLayout borderLayout4 = new BorderLayout();

    private BorderLayout borderLayout3 = new BorderLayout();

    private FlowLayout flowLayout9 = new FlowLayout();

    private JButton createButton = new JButton();

    private VerticalLayout verticalLayout11 = new VerticalLayout();

    private JButton selectGeneButton = new JButton();

    private JPanel jPanel6 = new JPanel();

    private JCheckBox geneCheckBox = new JCheckBox();

    private FlowLayout flowLayout11 = new FlowLayout();

    private JTextField superThreshField = new JTextField();

    private JLabel jLabel7 = new JLabel();

    private JPanel jPanel7 = new JPanel();

    private JPanel jPanel8 = new JPanel();

    private VerticalLayout verticalLayout12 = new VerticalLayout();

    private FlowLayout flowLayout10 = new FlowLayout();

    private JTextField maxCycleField = new JTextField();

    private JPanel kmaxPanel = new JPanel();

    private JLabel maxCycleLabel = new JLabel();

    private FlowLayout flowLayout5 = new FlowLayout();

    private JLabel jLabel5 = new JLabel();

    private JPanel knumPanel = new JPanel();

    private JTextField kClusterField = new JTextField();

    private GridLayout gridLayout1 = new GridLayout();

    private JPanel jPanel9 = new JPanel();

    private JCheckBox calculateBox = new JCheckBox();

    /**parent frame*/
    protected Frame parentFrame;

    /**project associated with new cluster file*/
    protected Project project;

    /**whether or not cluster file uses a dissimilarity file for creation*/
    protected boolean useDisFile = true;

    /**cluster to be made*/
    protected AbstractCluster cluster = null;

    /**values for created gene*/
    protected double createdValues[];

    /**whether or not the created values have been set*/
    protected boolean valuesSet = false;

    /**expression file*/
    protected ExpFile ex = null;

    /**selected gene for SupervisedQTCluster*/
    protected String selectedGene = null;

    /**frame to create a gene*/
    protected GeneCreatorFrame gcf;

    /**
     * Constructs the frame for the user to create a cluster file
     * @param project project associated with the cluster file
     * @param parentFrame parent frame
     */
    public ClusterFrame(Project project, Frame parentFrame) {
        this.project = project;
        this.parentFrame = parentFrame;
        expComboBox = new FileComboBox(project, project.EXP);
        disComboBox = new FileComboBox(project, project.DIS);
        try {
            jbInit();
            addKeyListenerRecursively(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        titledBorder1 = new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153), 2), "Select Clustering Method");
        border1 = BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.white, Color.white, new Color(148, 145, 140), new Color(103, 101, 98)), BorderFactory.createEmptyBorder(3, 3, 3, 3));
        border2 = BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.white, Color.white, new Color(148, 145, 140), new Color(103, 101, 98)), BorderFactory.createEmptyBorder(3, 3, 3, 3));
        border3 = BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.white, Color.white, new Color(148, 145, 140), new Color(103, 101, 98)), BorderFactory.createEmptyBorder(3, 3, 3, 3));
        border4 = BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.white, Color.white, new Color(148, 145, 140), new Color(103, 101, 98)), BorderFactory.createEmptyBorder(3, 3, 3, 3));
        titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "Parameters");
        titledBorder3 = new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153), 2), "Select Files");
        this.setClosable(true);
        this.getContentPane().setLayout(verticalLayout1);
        this.setOpaque(true);
        this.setBackground(new Color(204, 204, 204));
        cluscancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cluscancelButton_actionPerformed(e);
            }
        });
        clusokButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                clusokButton_actionPerformed(e);
            }
        });
        rootPane.setDefaultButton(clusokButton);
        clusterStatusLabel.setBackground(Color.lightGray);
        clusterStatusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        clusterStatusLabel.setMaximumSize(new Dimension(45, 21));
        clusterStatusLabel.setMinimumSize(new Dimension(4, 4));
        clusterStatusLabel.setOpaque(true);
        clusterStatusLabel.setPreferredSize(new Dimension(45, 21));
        clusterStatusLabel.setToolTipText("");
        clusterStatusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titlePanel.setBackground(new Color(204, 204, 204));
        titlePanel.setBorder(titledBorder1);
        titlePanel.setLayout(verticalLayout6);
        paramPanel.setLayout(cardParam);
        flowLayout3.setAlignment(FlowLayout.LEFT);
        hierPanel.setLayout(flowLayout3);
        hierPanel.setBackground(new Color(204, 204, 204));
        hierPanel.setPreferredSize(new Dimension(462, 45));
        hierStyle.setBackground(new Color(255, 255, 255));
        hierStyle.setOpaque(false);
        linkageLabel.setBackground(new Color(204, 204, 204));
        linkageLabel.setText("Linkage Style");
        minClusterField.setPreferredSize(new Dimension(50, 21));
        minClusterField.setText("5");
        maxClusterField.setPreferredSize(new Dimension(50, 21));
        maxClusterField.setText("172");
        threshField.setPreferredSize(new Dimension(50, 21));
        threshField.setText(".9");
        flowLayout7.setAlignment(FlowLayout.RIGHT);
        qtMinPanel.setLayout(verticalLayout5);
        flowLayout4.setAlignment(FlowLayout.LEFT);
        flowLayout1.setAlignment(FlowLayout.RIGHT);
        threshPane.setLayout(verticalLayout4);
        numPane.setLayout(flowLayout1);
        qtPanel.setLayout(flowLayout4);
        minClusterLabel.setText("Min Cluster Size =");
        maxClusterLabel.setText("Max Num Clusters =");
        thresshLabel.setText("Threshold =");
        thresPane.setLayout(flowLayout7);
        superPanel.setBackground(new Color(204, 204, 204));
        superPanel.setPreferredSize(new Dimension(462, 40));
        superPanel.setLayout(flowLayout9);
        kmeansPanel.setLayout(gridLayout1);
        paramPanel.setBackground(new Color(204, 204, 204));
        paramPanel.setBorder(titledBorder2);
        expinfoPanel.setBackground(new Color(204, 204, 204));
        expinfoPanel.setPreferredSize(new Dimension(462, 80));
        expinfoPanel.setLayout(verticalLayout7);
        outputPanel.setLayout(verticalLayout3);
        outputPanel.setBackground(new Color(204, 204, 204));
        outputPanel.setBorder(BorderFactory.createEtchedBorder());
        numPanel.setLayout(flowLayout2);
        methodInfoPanel.setLayout(flowLayout6);
        flowLayout8.setAlignment(FlowLayout.LEFT);
        flowLayout6.setAlignment(FlowLayout.LEFT);
        flowLayout2.setAlignment(FlowLayout.LEFT);
        flowLayout2.setVgap(0);
        expInfoPanel.setLayout(flowLayout8);
        clusterLabel.setBackground(new Color(204, 204, 204));
        clusterLabel.setBorder(border3);
        clusterLabel.setText("Cluster File");
        disinfoPanel.setLayout(verticalLayout2);
        inputPanel.setLayout(cardFile);
        inputPanel.setBorder(BorderFactory.createEtchedBorder());
        filepanel.setBackground(new Color(204, 204, 204));
        filepanel.setBorder(titledBorder3);
        filepanel.setLayout(verticalLayout8);
        numGeneExp.setBackground(new Color(204, 204, 204));
        numGeneExp.setFont(new java.awt.Font("Dialog", 1, 12));
        numGeneExp.setText(" Number of Genes:");
        jPanel3.setLayout(borderLayout5);
        jPanel3.setBackground(new Color(204, 204, 204));
        jPanel3.setPreferredSize(new Dimension(448, 27));
        methodInfoLabel.setBackground(new Color(204, 204, 204));
        methodInfoLabel.setFont(new java.awt.Font("Dialog", 1, 12));
        methodInfoLabel.setText("Dissimilarity Method:");
        numLabel.setBackground(new Color(204, 204, 204));
        numLabel.setFont(new java.awt.Font("Dialog", 1, 12));
        numLabel.setText("Number of Genes:");
        outnameField.setPreferredSize(new Dimension(370, 21));
        expInfoLabel.setBackground(new Color(204, 204, 204));
        expInfoLabel.setFont(new java.awt.Font("Dialog", 1, 12));
        expInfoLabel.setText("Original .exp File:");
        outnamePanel.setLayout(borderLayout2);
        threshPane.setBackground(new Color(204, 204, 204));
        jPanel5.setBackground(new Color(204, 204, 204));
        jPanel5.setBorder(BorderFactory.createEtchedBorder());
        jPanel5.setLayout(verticalLayout9);
        qtPanel.setBackground(new Color(204, 204, 204));
        thresPane.setBackground(new Color(204, 204, 204));
        numPane.setBackground(new Color(204, 204, 204));
        minClusterPanel.setBackground(new Color(204, 204, 204));
        qtMinPanel.setBackground(new Color(204, 204, 204));
        methodInfoPanel.setBackground(new Color(204, 204, 204));
        expInfoPanel.setBackground(new Color(204, 204, 204));
        numPanel.setBackground(new Color(204, 204, 204));
        disinfoPanel.setBackground(new Color(204, 204, 204));
        outnamePanel.setBackground(new Color(204, 204, 204));
        confirmPanel.setBackground(new Color(204, 204, 204));
        clusterCombo.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                clusterCombo_itemStateChanged(e);
            }
        });
        methodLabel.setBackground(new Color(204, 204, 204));
        methodLabel.setBorder(border4);
        methodLabel.setText("Cluster Method");
        jPanel4.setBackground(new Color(204, 204, 204));
        jPanel4.setLayout(borderLayout6);
        kmeansPanel.setBackground(new Color(204, 204, 204));
        disFileLabel.setBackground(new Color(204, 204, 204));
        disFileLabel.setBorder(border1);
        disFileLabel.setText("Dissimilarity File");
        namePanel.setLayout(borderLayout1);
        expFileLabel.setBackground(new Color(204, 204, 204));
        expFileLabel.setBorder(border3);
        expFileLabel.setMaximumSize(new Dimension(101, 21));
        expFileLabel.setMinimumSize(new Dimension(101, 21));
        expFileLabel.setPreferredSize(new Dimension(101, 21));
        expFileLabel.setText("Expression File");
        jPanel2.setPreferredSize(new Dimension(448, 27));
        jPanel2.setLayout(borderLayout3);
        jPanel1.setPreferredSize(new Dimension(448, 27));
        jPanel1.setLayout(borderLayout4);
        disComboBox.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                disComboBox_itemStateChanged(e);
            }
        });
        expComboBox.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                expComboBox_itemStateChanged(e);
            }
        });
        expComboBox.setBackground(new Color(204, 204, 204));
        disComboBox.setBackground(new Color(204, 204, 204));
        clusterCombo.setBackground(new Color(204, 204, 204));
        flowLayout9.setAlignment(FlowLayout.LEFT);
        flowLayout9.setHgap(15);
        flowLayout9.setVgap(0);
        createButton.setEnabled(false);
        createButton.setText("Create Gene");
        createButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                createButton_actionPerformed(e);
            }
        });
        selectGeneButton.setEnabled(false);
        selectGeneButton.setText("Select Gene");
        selectGeneButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                selectGeneButton_actionPerformed(e);
            }
        });
        jPanel6.setBackground(new Color(204, 204, 204));
        jPanel6.setLayout(verticalLayout11);
        geneCheckBox.setBackground(new Color(204, 204, 204));
        geneCheckBox.setEnabled(false);
        geneCheckBox.setSelected(true);
        geneCheckBox.setText("Use Existing Gene");
        geneCheckBox.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                geneCheckBox_itemStateChanged(e);
            }
        });
        flowLayout11.setAlignment(FlowLayout.LEFT);
        superThreshField.setText(".9            ");
        jLabel7.setBackground(new Color(204, 204, 204));
        jLabel7.setText("Threshold");
        jPanel7.setBackground(new Color(204, 204, 204));
        jPanel7.setLayout(flowLayout11);
        clusokButton.setEnabled(false);
        jPanel8.setBackground(new Color(204, 204, 204));
        jPanel8.setLayout(verticalLayout12);
        flowLayout10.setAlignment(FlowLayout.LEFT);
        maxCycleField.setText("20");
        maxCycleField.setColumns(5);
        kmaxPanel.setBackground(new Color(204, 204, 204));
        kmaxPanel.setLayout(flowLayout10);
        maxCycleLabel.setBackground(new Color(204, 204, 204));
        maxCycleLabel.setText("Max Cycles:");
        flowLayout5.setAlignment(FlowLayout.LEFT);
        jLabel5.setBackground(new Color(204, 204, 204));
        jLabel5.setText("K (# of Clusters):");
        knumPanel.setBackground(new Color(204, 204, 204));
        knumPanel.setLayout(flowLayout5);
        kClusterField.setText("10");
        kClusterField.setColumns(5);
        kClusterField.setPreferredSize(new Dimension(50, 21));
        gridLayout1.setColumns(2);
        jPanel9.setBackground(new Color(204, 204, 204));
        calculateBox.setBackground(new Color(204, 204, 204));
        calculateBox.setText("Constantly Calculate Seeds");
        disinfoPanel.add(namePanel, null);
        jPanel5.add(jPanel4, null);
        jPanel4.add(methodLabel, BorderLayout.WEST);
        jPanel4.add(clusterCombo, BorderLayout.CENTER);
        titlePanel.add(jPanel5, null);
        titlePanel.add(paramPanel, null);
        this.getContentPane().add(titlePanel, null);
        this.getContentPane().add(filepanel, null);
        this.getContentPane().add(confirmPanel, null);
        this.getContentPane().add(clusterStatusLabel, null);
        confirmPanel.add(clusokButton, null);
        confirmPanel.add(cluscancelButton, null);
        hierPanel.add(linkageLabel, null);
        hierPanel.add(hierStyle, null);
        paramPanel.add(qtPanel, "qtPanel");
        minClusterPanel.add(minClusterLabel, null);
        minClusterPanel.add(minClusterField, null);
        qtPanel.add(threshPane, null);
        qtPanel.add(qtMinPanel, null);
        qtMinPanel.add(minClusterPanel, null);
        numPane.add(maxClusterLabel, null);
        numPane.add(maxClusterField, null);
        threshPane.add(thresPane, null);
        threshPane.add(numPane, null);
        thresPane.add(thresshLabel, null);
        thresPane.add(threshField, null);
        paramPanel.add(superPanel, "superPanel");
        jPanel6.add(selectGeneButton, null);
        jPanel6.add(createButton, null);
        jPanel7.add(jLabel7, null);
        jPanel7.add(superThreshField, null);
        superPanel.add(jPanel7, null);
        superPanel.add(geneCheckBox, null);
        superPanel.add(jPanel6, null);
        paramPanel.add(kmeansPanel, "kmeansPanel");
        kmeansPanel.add(jPanel8, null);
        kmaxPanel.add(maxCycleLabel, null);
        kmaxPanel.add(maxCycleField, null);
        kmeansPanel.add(jPanel9, null);
        jPanel9.add(calculateBox, null);
        knumPanel.add(jLabel5, null);
        knumPanel.add(kClusterField, null);
        jPanel8.add(knumPanel, null);
        jPanel8.add(kmaxPanel, null);
        paramPanel.add(hierPanel, "hierPanel");
        hierStyle.addItem(new ComboItem("Single Linkage", true));
        hierStyle.addItem(new ComboItem("Complete Linkage", false));
        hierStyle.addItem(new ComboItem("Average Linkage", false));
        hierStyle.setRenderer(new ComboRenderer());
        hierStyle.addActionListener(new ComboListener(hierStyle));
        outputPanel.add(outnamePanel, null);
        numPanel.add(numLabel, null);
        numPanel.add(numIs, null);
        methodInfoPanel.add(methodInfoLabel, null);
        methodInfoPanel.add(methodInfo, null);
        expInfoPanel.add(expInfoLabel, null);
        expInfoPanel.add(expInfo, null);
        disinfoPanel.add(expInfoPanel, null);
        disinfoPanel.add(methodInfoPanel, null);
        disinfoPanel.add(numPanel, null);
        inputPanel.add(disinfoPanel, "disinfoPanel");
        filepanel.add(inputPanel, null);
        filepanel.add(outputPanel, null);
        outnamePanel.add(outnameField, BorderLayout.CENTER);
        outnamePanel.add(clusterLabel, BorderLayout.WEST);
        inputPanel.add(expinfoPanel, "expinfoPanel");
        expinfoPanel.add(jPanel2, null);
        jPanel2.add(jPanel1, BorderLayout.CENTER);
        expinfoPanel.add(jPanel3, null);
        jPanel3.add(numGeneExp, BorderLayout.WEST);
        clusterCombo.addItem("Hierarchical Clustering");
        clusterCombo.addItem("QT Clustering");
        clusterCombo.addItem("KMeans Clustering");
        clusterCombo.addItem("Supervised Clustering");
        namePanel.add(disComboBox, BorderLayout.CENTER);
        namePanel.add(disFileLabel, BorderLayout.WEST);
        jPanel1.add(expComboBox, BorderLayout.CENTER);
        jPanel1.add(expFileLabel, BorderLayout.WEST);
        cardParam.show(paramPanel, "hierPanel");
        cardFile.show(inputPanel, "disinfoPanel");
    }

    private void cluscancelButton_actionPerformed(ActionEvent e) {
        this.dispose();
    }

    private void clusokButton_actionPerformed(ActionEvent e) {
        try {
            String out;
            boolean canDispose = true;
            if (useDisFile) out = disComboBox.getFilePath(); else out = expComboBox.getFilePath();
            out = out.substring(0, out.lastIndexOf(File.separator) + 1);
            if (out != null) out += outnameField.getText().trim();
            if (!out.endsWith(".clust")) out += ".clust";
            if (outfileIsValid(out)) {
                if (clusterCombo.getSelectedItem().toString().equals("Hierarchical Clustering")) {
                    cluster = new HiClust(disComboBox.getFilePath(), out, this.getDesktopPane());
                    ((HiClust) cluster).setStyle(hierStyle.getSelectedIndex());
                    ((HiClust) cluster).setProject(project);
                } else if (clusterCombo.getSelectedItem().toString().equals("QT Clustering")) {
                    cluster = new QTClust(disComboBox.getFilePath(), out, Float.parseFloat(threshField.getText()), Integer.parseInt(maxClusterField.getText()), Integer.parseInt(minClusterField.getText()), this.getDesktopPane());
                    ((QTClust) cluster).setProject(project);
                } else if (clusterCombo.getSelectedItem().toString().equals("KMeans Clustering")) {
                    cluster = new KMeansClust(expComboBox.getFilePath(), out, this.getDesktopPane());
                    ((KMeansClust) cluster).setMaxCycle(Integer.parseInt(maxCycleField.getText().trim()));
                    ((KMeansClust) cluster).setNumberOfClusters(Integer.parseInt(kClusterField.getText().trim()));
                    ((KMeansClust) cluster).setConstantCalculation(calculateBox.isSelected());
                    ((KMeansClust) cluster).setProject(project);
                } else if (clusterCombo.getSelectedItem().toString().equals("Supervised Clustering")) {
                    if (geneCheckBox.isSelected()) {
                        if (selectedGene != null) {
                            cluster = new SupervisedQTClust(disComboBox.getFilePath(), out, Float.parseFloat(superThreshField.getText()), selectedGene, this.getDesktopPane());
                        } else {
                            JOptionPane.showMessageDialog(this, "Error! You Must Select A Gene");
                            canDispose = false;
                        }
                    } else {
                        if (valuesSet) {
                            cluster = new SupervisedQTClust(disComboBox.getFilePath(), out, Float.parseFloat(superThreshField.getText()), createdValues, this.getDesktopPane());
                        } else {
                            JOptionPane.showMessageDialog(this, "Error! You Must Create A Gene");
                            canDispose = false;
                        }
                    }
                    if (canDispose) cluster.setProject(project);
                }
                if (cluster != null) {
                    cluster.start();
                }
                if (canDispose) {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            dispose();
                        }
                    });
                }
            }
        } catch (NumberFormatException e1) {
            JOptionPane.showMessageDialog(this, "Error! One Or More TextFields Contains Improper Number");
        }
    }

    /**
     * returns whether or not the dissimilarity file is valid
     * @param disfile path of the dissimilarity file
     * @return whether or not the dissimilarity file is valid
     */
    private boolean disfileIsValid(String disfile) {
        disfile.trim();
        File disFile = new File(disfile);
        if (disFile.isDirectory()) {
            JOptionPane.showMessageDialog(this, "The dissimilarity file path is a directory.  Please add a file name.", "Directory Found", JOptionPane.OK_OPTION);
            return false;
        } else if (!disFile.exists()) {
            JOptionPane.showMessageDialog(this, "The file " + disFile.getPath() + " does not exist.  Please select an existing file.", "File Does Not Exist", JOptionPane.OK_OPTION);
            return false;
        }
        return true;
    }

    /**
     * returns whether or not the expression file is valid
     * @param expfile path of the expression file
     * @return whether or not the expression file is valid
     */
    private boolean expfileIsValid(String expfile) {
        expfile.trim();
        File expFile = new File(expfile);
        if (expFile.isDirectory()) {
            JOptionPane.showMessageDialog(this, "The expression file path is a directory.  Please add a file name.", "Directory Found", JOptionPane.OK_OPTION);
            return false;
        } else if (!expFile.exists()) {
            JOptionPane.showMessageDialog(this, "The file " + expFile.getPath() + " does not exist.  Please select an existing file.", "File Does Not Exist", JOptionPane.OK_OPTION);
            return false;
        }
        return true;
    }

    private boolean outfileIsValid(String outfile) {
        outfile.trim();
        File outFile = new File(outfile);
        if (outFile.isDirectory()) {
            JOptionPane.showMessageDialog(this, "The output file path is a directory.  Please add a file name.", "Directory Found", JOptionPane.OK_OPTION);
            return false;
        } else if (outFile.exists()) {
            int result = JOptionPane.showConfirmDialog(this, "The file " + outFile.getPath() + " already exists.  Overwrite this file?", "Overwrite File?", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                outFile.delete();
                return true;
            } else return false;
        }
        return true;
    }

    /**
   * sets the values for the created gene
   * @param vals array of values for the created gene
   */
    public void setGeneValues(double vals[]) {
        createdValues = new double[vals.length];
        for (int i = 0; i < vals.length; i++) {
            createdValues[i] = vals[i];
        }
        valuesSet = true;
    }

    private void setInfo(int type) {
        if (type == 1) {
            try {
                ExpFile exp = new ExpFile(new File(expComboBox.getFilePath()));
                System.out.println("ExpFile:" + expComboBox.getFilePath());
                numGeneExp.setText("Number of Genes: " + exp.numGenes());
            } catch (Exception e2) {
                numGeneExp.setText("Number of Genes: ----");
            }
        } else {
            try {
                RandomAccessFile stream = new RandomAccessFile(disComboBox.getFilePath(), "r");
                numIs.setText(Integer.toString(stream.readInt()));
                String exp = "";
                if (stream.readBoolean() == true) exp = stream.readUTF();
                expInfo.setText(exp.substring(exp.lastIndexOf(File.separator) + 1));
                int method = stream.readInt();
                if (method == 0) methodInfo.setText("1 - Correlation"); else if (method == 1) methodInfo.setText("l^p p=" + stream.readUTF()); else if (method == 2) methodInfo.setText("Jackknife correlation");
            } catch (Exception e2) {
                expInfo.setText("---");
                methodInfo.setText("---");
                numIs.setText("---");
            }
        }
    }

    private void clusterCombo_itemStateChanged(ItemEvent e) {
        if (clusterCombo.getSelectedItem().toString().equals("Hierarchical Clustering")) {
            cardParam.show(paramPanel, "hierPanel");
            cardFile.show(inputPanel, "disinfoPanel");
            useDisFile = true;
            if (disComboBox.getSelectedIndex() != 0) clusokButton.setEnabled(true); else clusokButton.setEnabled(false);
        } else if (clusterCombo.getSelectedItem().toString().equals("QT Clustering")) {
            cardParam.show(paramPanel, "qtPanel");
            cardFile.show(inputPanel, "disinfoPanel");
            useDisFile = true;
            if (disComboBox.getSelectedIndex() != 0) clusokButton.setEnabled(true); else clusokButton.setEnabled(false);
        } else if (clusterCombo.getSelectedItem().toString().equals("KMeans Clustering")) {
            cardParam.show(paramPanel, "kmeansPanel");
            cardFile.show(inputPanel, "expinfoPanel");
            useDisFile = false;
            if (expComboBox.getSelectedIndex() != 0) clusokButton.setEnabled(true); else clusokButton.setEnabled(false);
        } else if (clusterCombo.getSelectedItem().toString().equals("Supervised Clustering")) {
            cardParam.show(paramPanel, "superPanel");
            cardFile.show(inputPanel, "disinfoPanel");
            useDisFile = true;
            if (disComboBox.getSelectedIndex() != 0) {
                clusokButton.setEnabled(true);
                if (geneCheckBox.isSelected()) selectGeneButton.setEnabled(true); else selectGeneButton.setEnabled(false);
            } else {
                clusokButton.setEnabled(false);
            }
        }
        setOutFileField();
    }

    private void disComboBox_itemStateChanged(ItemEvent e) {
        setInfo(0);
        if (disComboBox.getSelectedIndex() != 0) {
            ex = new ExpFile(new File(getExpFile()));
            if (ex.isValid() && ex != null) {
                geneCheckBox.setEnabled(true);
                if (geneCheckBox.isSelected()) {
                    selectGeneButton.setEnabled(true);
                    createButton.setEnabled(false);
                } else {
                    selectGeneButton.setEnabled(false);
                    createButton.setEnabled(true);
                }
            } else {
                selectGeneButton.setEnabled(false);
                createButton.setEnabled(false);
            }
            if (useDisFile) clusokButton.setEnabled(true);
        } else {
            selectGeneButton.setEnabled(false);
            geneCheckBox.setEnabled(false);
            createButton.setEnabled(false);
            if (useDisFile) clusokButton.setEnabled(false);
        }
        valuesSet = false;
        selectedGene = null;
        setOutFileField();
    }

    private void expComboBox_itemStateChanged(ItemEvent e) {
        setInfo(1);
        if (expComboBox.getSelectedIndex() != 0) {
            if (!useDisFile) clusokButton.setEnabled(true);
        } else {
            if (!useDisFile) {
                clusokButton.setEnabled(false);
            }
        }
        setOutFileField();
    }

    /**
   * creates the default cluster filename and places it in the correct field
   */
    protected void setOutFileField() {
        if (useDisFile) {
            if (disComboBox.getSelectedIndex() == 0) outnameField.setText(""); else {
                String name = disComboBox.getFileName();
                if (name != null) {
                    name = name.substring(0, name.lastIndexOf(".")) + clusterCombo.getSelectedItem().toString().substring(0, 1).toLowerCase() + ".clust";
                    outnameField.setText(name);
                } else outnameField.setText(name);
            }
        } else {
            if (expComboBox.getSelectedIndex() == 0) outnameField.setText(""); else {
                String name = expComboBox.getFileName();
                if (name != null) {
                    name = name.substring(0, name.lastIndexOf(".")) + clusterCombo.getSelectedItem().toString().substring(0, 1).toLowerCase() + ".clust";
                    outnameField.setText(name);
                } else outnameField.setText(name);
            }
        }
    }

    private void createButton_actionPerformed(ActionEvent e) {
        gcf = new GeneCreatorFrame(new ExpFile(new File(getExpFile())), parentFrame);
        if (valuesSet) gcf.setValues(createdValues);
        gcf.setParent(this);
        int w, w1;
        gcf.setSize((((w = this.getDesktopPane().getWidth()) < (w1 = gcf.getMaximumSize().width)) ? w : w1), gcf.getMaximumSize().height);
        this.getDesktopPane().add(gcf);
        gcf.pack();
        gcf.repaint();
        gcf.show();
        gcf.setSize(gcf.getWidth() + 1, gcf.getHeight());
    }

    private void geneCheckBox_itemStateChanged(ItemEvent e) {
        if (geneCheckBox.isSelected()) {
            selectGeneButton.setEnabled(true);
            createButton.setEnabled(false);
        } else {
            selectGeneButton.setEnabled(false);
            createButton.setEnabled(true);
        }
    }

    private void selectGeneButton_actionPerformed(ActionEvent e) {
        ExpFile exp = new ExpFile(new File((getExpFile())));
        String select = (String) JOptionPane.showInputDialog(this, "Please Select A Gene", "Select One", JOptionPane.QUESTION_MESSAGE, null, exp.getGeneVector(), (selectedGene == null ? exp.getGeneName(0) : selectedGene));
        if (select != null) selectedGene = select;
    }

    private String getExpFile() {
        return project.getPath() + expInfo.getText().substring(0, expInfo.getText().lastIndexOf(".")) + File.separator + expInfo.getText();
    }

    private class ComboRenderer extends JLabel implements ListCellRenderer {

        public ComboRenderer() {
            setOpaque(true);
            setBorder(new EmptyBorder(1, 1, 1, 1));
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            if (!((ComboItem) value).isEnabled()) {
                setBackground(list.getBackground());
                setForeground(UIManager.getColor("Label.disabledForeground"));
            }
            setFont(list.getFont());
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    private class ComboListener implements ActionListener {

        JComboBox combo;

        Object currentItem;

        ComboListener(JComboBox combo) {
            this.combo = combo;
            combo.setSelectedIndex(0);
            currentItem = combo.getSelectedItem();
        }

        public void actionPerformed(ActionEvent e) {
            Object tempItem = combo.getSelectedItem();
            if (!((ComboItem) tempItem).isEnabled()) {
                combo.setSelectedItem(currentItem);
            } else {
                currentItem = tempItem;
            }
        }
    }

    private class ComboItem {

        Object obj;

        boolean isEnable;

        ComboItem(Object obj, boolean isEnable) {
            this.obj = obj;
            this.isEnable = isEnable;
        }

        ComboItem(Object obj) {
            this(obj, true);
        }

        public boolean isEnabled() {
            return isEnable;
        }

        public void setEnabled(boolean isEnable) {
            this.isEnable = isEnable;
        }

        public String toString() {
            return obj.toString();
        }
    }

    private void addKeyListenerRecursively(Component c) {
        c.removeKeyListener(this);
        c.addKeyListener(this);
        if (c instanceof Container) {
            Container cont = (Container) c;
            Component[] children = cont.getComponents();
            for (int i = 0; i < children.length; i++) {
                addKeyListenerRecursively(children[i]);
            }
        }
    }

    /**
     * Closes the frame when user press control + 'w'
     * @param e key event
     */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_MASK).getKeyCode() && e.isControlDown()) {
            this.dispose();
        }
    }

    /**
     * Not implemented in this frame
     * @param e key event
     */
    public void keyReleased(KeyEvent e) {
    }

    /**
     * Not implemented in this frame
     * @param e key event
     */
    public void keyTyped(KeyEvent e) {
    }
}
