package magictool.task;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
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
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import magictool.ExpFile;
import magictool.FileComboBox;
import magictool.GeneCreator;
import magictool.GeneCreatorFrame;
import magictool.Project;
import magictool.VerticalLayout;
import magictool.dissim.Dissimilarity;

/**
 * TaskBuilderFrame is a frame which allows a user to build an executable task to be added
 * to the task manager for later execution. The frame calls the TaskFactory to build the task
 * and currently all types of dissimilarity and cluster files can be created as tasks.
 */
public class TaskBuilderFrame extends JInternalFrame implements GeneCreator, KeyListener {

    private JPanel jPanel1 = new JPanel();

    private VerticalLayout verticalLayout1 = new VerticalLayout();

    private JLabel jLabel1 = new JLabel();

    private JComboBox executeBox = new JComboBox();

    private BorderLayout borderLayout1 = new BorderLayout();

    private Border border1;

    private JPanel jPanel2 = new JPanel();

    private TitledBorder titledBorder1;

    private CardLayout cardLayout1 = new CardLayout();

    private JPanel disPanel = new JPanel();

    private VerticalLayout verticalLayout2 = new VerticalLayout();

    private JPanel jPanel4 = new JPanel();

    private JPanel expChooserPanel = new JPanel();

    private JLabel jLabel2 = new JLabel();

    private JLabel jLabel3 = new JLabel();

    private BorderLayout borderLayout2 = new BorderLayout();

    private BorderLayout borderLayout3 = new BorderLayout();

    private ButtonGroup disbuttonGroup = new ButtonGroup();

    private JPanel jPanel6 = new JPanel();

    private JButton addTaskButton = new JButton();

    private JButton cancelButton = new JButton();

    private JPanel clustPanel = new JPanel();

    private VerticalLayout verticalLayout3 = new VerticalLayout();

    private JPanel jPanel8 = new JPanel();

    private JPanel jPanel9 = new JPanel();

    private JPanel jPanel10 = new JPanel();

    private JLabel jLabel4 = new JLabel();

    private JLabel linkageLabel = new JLabel();

    private JTextField minClusterField = new JTextField();

    private JPanel qtMinPanel = new JPanel();

    private JPanel paramPanel = new JPanel();

    private JPanel threshPane = new JPanel();

    private JLabel thresshLabel = new JLabel();

    private JPanel thresPane = new JPanel();

    private JPanel minClusterPanel = new JPanel();

    private JComboBox hierStyle = new JComboBox();

    private JTextField maxClusterField = new JTextField();

    private JPanel qtPanel = new JPanel();

    private FlowLayout flowLayout9 = new FlowLayout();

    private FlowLayout flowLayout7 = new FlowLayout();

    private JButton selectGeneButton = new JButton();

    private FlowLayout flowLayout4 = new FlowLayout();

    private FlowLayout flowLayout8 = new FlowLayout();

    private FlowLayout flowLayout2 = new FlowLayout();

    private VerticalLayout verticalLayout11 = new VerticalLayout();

    private JPanel numPane = new JPanel();

    private CardLayout cardParam = new CardLayout();

    private JButton createButton = new JButton();

    private VerticalLayout verticalLayout5 = new VerticalLayout();

    private VerticalLayout verticalLayout4 = new VerticalLayout();

    private JPanel jPanel12 = new JPanel();

    private JTextField threshField = new JTextField();

    private JLabel minClusterLabel = new JLabel();

    private JPanel superPanel = new JPanel();

    private JPanel hierPanel = new JPanel();

    private JPanel kmeansPanel = new JPanel();

    private JCheckBox geneCheckBox = new JCheckBox();

    private JLabel maxClusterLabel = new JLabel();

    private Border border2;

    private TitledBorder titledBorder2;

    private VerticalLayout verticalLayout6 = new VerticalLayout();

    private JPanel jPanel14 = new JPanel();

    private JLabel jLabel8 = new JLabel();

    private JTextField clusterField = new JTextField();

    private BorderLayout borderLayout5 = new BorderLayout();

    private BorderLayout borderLayout6 = new BorderLayout();

    private JTextField disField = new JTextField();

    private JPanel jPanel15 = new JPanel();

    private JPanel corrPanel = new JPanel();

    private JPanel lpPanel = new JPanel();

    private JRadioButton jackButton = new JRadioButton();

    private JPanel jackPanel = new JPanel();

    private FlowLayout flowLayout5 = new FlowLayout();

    private FlowLayout flowLayout3 = new FlowLayout();

    private FlowLayout flowLayout1 = new FlowLayout();

    private JLabel pLabel = new JLabel();

    private JRadioButton lpButton = new JRadioButton();

    private JTextField pField = new JTextField();

    private JRadioButton corrButton = new JRadioButton();

    private VerticalLayout verticalLayout7 = new VerticalLayout();

    private TitledBorder titledBorder3;

    private FlowLayout flowLayout11 = new FlowLayout();

    private JTextField superThreshField = new JTextField();

    private JLabel jLabel7 = new JLabel();

    private JPanel jPanel11 = new JPanel();

    private JComboBox clusterCombo = new JComboBox();

    private JPanel fileChooserPanel = new JPanel();

    private JLabel disLabel = new JLabel();

    private JPanel disChooserPanel = new JPanel();

    private BorderLayout borderLayout4 = new BorderLayout();

    private CardLayout fileCard = new CardLayout();

    private JPanel jPanel3 = new JPanel();

    private VerticalLayout verticalLayout8 = new VerticalLayout();

    private FlowLayout flowLayout6 = new FlowLayout();

    private JLabel jLabel5 = new JLabel();

    private JPanel knumPanel = new JPanel();

    private JTextField kClusterField = new JTextField();

    private FlowLayout flowLayout10 = new FlowLayout();

    private JTextField maxCycleField = new JTextField();

    private JPanel kmaxPanel = new JPanel();

    private JLabel maxCycleLabel = new JLabel();

    private GridLayout gridLayout1 = new GridLayout();

    private JPanel jPanel5 = new JPanel();

    private JCheckBox calculateBox = new JCheckBox();

    /**parent frame*/
    protected Frame parentFrame;

    /**task manager to add built tasks to*/
    protected TaskManager manager;

    /**gene creator frame to create a gene for supervised qt clustering*/
    protected GeneCreatorFrame gcf;

    /**whether or not a created cluster file uses data from a dissimilarity file for creation or an expression file*/
    protected boolean useDisFile = true;

    /**selected gene for supervised qt clustering*/
    protected String selectedGene = null;

    /**values of data for a created gene for supervised qt clustering*/
    protected double createdValues[];

    /**whether or not values have been set for a created gene for supervised qt clustering*/
    protected boolean valuesSet;

    /**project associated with the tasks to be created*/
    protected Project project = null;

    private FileComboBox disComboBox;

    private FileComboBox expBox;

    /**
   * Constructs a taskbuilderframe to build executable tasks to be executed later
   * @param project project associated with tasks
   * @param manager task manager which manages the tasks
   * @param parentFrame parent frame
   */
    public TaskBuilderFrame(Project project, TaskManager manager, Frame parentFrame) {
        this.project = project;
        this.manager = manager;
        this.parentFrame = parentFrame;
        disComboBox = new FileComboBox(manager.getTaskProject(), Project.DIS, false);
        expBox = new FileComboBox(manager.getTaskProject(), Project.EXP, false);
        try {
            jbInit();
            this.addKeyListenerRecursively(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        border1 = BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.white, Color.white, new Color(148, 145, 140), new Color(103, 101, 98)), BorderFactory.createEmptyBorder(3, 3, 3, 3));
        titledBorder1 = new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153), 2), "Task Setup");
        border2 = new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(142, 142, 142));
        titledBorder2 = new TitledBorder(border2, "Parameters");
        titledBorder3 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(142, 142, 142)), "Dissimilarity Method");
        this.getContentPane().setLayout(verticalLayout1);
        jLabel1.setBackground(new Color(204, 204, 204));
        jLabel1.setBorder(border1);
        jLabel1.setText("Select Task");
        jPanel1.setLayout(borderLayout1);
        jPanel1.setBorder(BorderFactory.createEtchedBorder());
        jPanel2.setBackground(new Color(204, 204, 204));
        jPanel2.setBorder(titledBorder1);
        jPanel2.setLayout(cardLayout1);
        disPanel.setLayout(verticalLayout2);
        jLabel2.setBackground(new Color(204, 204, 204));
        jLabel2.setBorder(border1);
        jLabel2.setText("Dissimilarity File");
        jLabel3.setBackground(new Color(204, 204, 204));
        jLabel3.setBorder(border1);
        jLabel3.setText("Expression File");
        expChooserPanel.setLayout(borderLayout2);
        jPanel4.setLayout(borderLayout3);
        addTaskButton.setText("Add Task");
        addTaskButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addTaskButton_actionPerformed(e);
            }
        });
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cancelButton_actionPerformed(e);
            }
        });
        clustPanel.setLayout(verticalLayout3);
        jLabel4.setBackground(new Color(204, 204, 204));
        jLabel4.setBorder(border1);
        jLabel4.setText("Cluster Method");
        linkageLabel.setText("Linkage Style");
        linkageLabel.setBackground(new Color(204, 204, 204));
        minClusterField.setText("5");
        minClusterField.setPreferredSize(new Dimension(50, 21));
        qtMinPanel.setBackground(new Color(204, 204, 204));
        qtMinPanel.setLayout(verticalLayout5);
        paramPanel.setLayout(cardParam);
        paramPanel.setBackground(new Color(204, 204, 204));
        paramPanel.setBorder(titledBorder2);
        threshPane.setLayout(verticalLayout4);
        threshPane.setBackground(new Color(204, 204, 204));
        thresshLabel.setText("Threshold =");
        thresPane.setLayout(flowLayout7);
        thresPane.setBackground(new Color(204, 204, 204));
        minClusterPanel.setBackground(new Color(204, 204, 204));
        hierStyle.setBackground(new Color(255, 255, 255));
        hierStyle.setOpaque(false);
        maxClusterField.setPreferredSize(new Dimension(50, 21));
        maxClusterField.setText("172");
        qtPanel.setLayout(flowLayout4);
        qtPanel.setBackground(new Color(204, 204, 204));
        flowLayout9.setAlignment(FlowLayout.LEFT);
        flowLayout9.setHgap(15);
        flowLayout9.setVgap(0);
        flowLayout7.setAlignment(FlowLayout.RIGHT);
        selectGeneButton.setText("Select Gene");
        selectGeneButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                selectGeneButton_actionPerformed(e);
            }
        });
        flowLayout4.setAlignment(FlowLayout.LEFT);
        flowLayout8.setAlignment(FlowLayout.LEFT);
        flowLayout2.setAlignment(FlowLayout.RIGHT);
        numPane.setLayout(flowLayout2);
        numPane.setBackground(new Color(204, 204, 204));
        createButton.setEnabled(false);
        createButton.setText("Create Gene");
        createButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                createButton_actionPerformed(e);
            }
        });
        jPanel12.setBackground(new Color(204, 204, 204));
        jPanel12.setLayout(verticalLayout11);
        threshField.setPreferredSize(new Dimension(50, 21));
        threshField.setText(".9");
        minClusterLabel.setText("Min Cluster Size =");
        superPanel.setBackground(new Color(204, 204, 204));
        superPanel.setPreferredSize(new Dimension(462, 40));
        superPanel.setLayout(flowLayout9);
        hierPanel.setLayout(flowLayout8);
        hierPanel.setBackground(new Color(204, 204, 204));
        hierPanel.setPreferredSize(new Dimension(462, 45));
        kmeansPanel.setLayout(gridLayout1);
        kmeansPanel.setBackground(new Color(204, 204, 204));
        geneCheckBox.setBackground(new Color(204, 204, 204));
        geneCheckBox.setSelected(true);
        geneCheckBox.setText("Use Existing Gene");
        geneCheckBox.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                geneCheckBox_itemStateChanged(e);
            }
        });
        maxClusterLabel.setText("Max Num Clusters =");
        jPanel6.setBackground(new Color(204, 204, 204));
        this.setClosable(true);
        this.getContentPane().setBackground(new Color(204, 204, 204));
        jPanel10.setBackground(new Color(204, 204, 204));
        jPanel10.setLayout(verticalLayout6);
        clustPanel.setBackground(new Color(204, 204, 204));
        clustPanel.setMaximumSize(new Dimension(494, 241));
        clustPanel.setMinimumSize(new Dimension(494, 241));
        jPanel8.setBackground(new Color(204, 204, 204));
        jPanel8.setLayout(borderLayout6);
        jPanel9.setBackground(new Color(204, 204, 204));
        executeBox.setBackground(new Color(204, 204, 204));
        executeBox.addItem("Create a Dissimilarity File");
        executeBox.addItem("Create a Cluster File");
        executeBox.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                executeBox_itemStateChanged(e);
            }
        });
        disPanel.setBackground(new Color(204, 204, 204));
        expBox.setBackground(new Color(204, 204, 204));
        expBox.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                expBox_itemStateChanged(e);
            }
        });
        jPanel14.setBackground(new Color(204, 204, 204));
        jPanel14.setLayout(borderLayout5);
        jLabel8.setBorder(border1);
        jLabel8.setText("Cluster File");
        jPanel15.setBackground(new Color(204, 204, 204));
        jPanel15.setBorder(titledBorder3);
        jPanel15.setLayout(verticalLayout7);
        corrPanel.setLayout(flowLayout1);
        corrPanel.setBackground(new Color(204, 204, 204));
        lpPanel.setLayout(flowLayout3);
        lpPanel.setBackground(new Color(204, 204, 204));
        jackButton.setBackground(new Color(204, 204, 204));
        jackButton.setText("1 - (jackknife correlation)");
        jackButton.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                jackButton_itemStateChanged(e);
            }
        });
        jackPanel.setLayout(flowLayout5);
        jackPanel.setBackground(new Color(204, 204, 204));
        flowLayout5.setAlignment(FlowLayout.LEFT);
        flowLayout3.setAlignment(FlowLayout.LEFT);
        flowLayout1.setAlignment(FlowLayout.LEFT);
        pLabel.setBackground(new Color(204, 204, 204));
        pLabel.setText("p=");
        lpButton.setBackground(new Color(204, 204, 204));
        lpButton.setText("l^p");
        lpButton.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                lpButton_itemStateChanged(e);
            }
        });
        pField.setEnabled(false);
        pField.setPreferredSize(new Dimension(35, 21));
        pField.setText("2");
        corrButton.setBackground(new Color(204, 204, 204));
        corrButton.setSelected(true);
        corrButton.setText("1 - correlation");
        corrButton.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                corrButton_itemStateChanged(e);
            }
        });
        flowLayout11.setAlignment(FlowLayout.LEFT);
        superThreshField.setText(".9            ");
        jLabel7.setBackground(new Color(204, 204, 204));
        jLabel7.setText("Threshold");
        jPanel11.setBackground(new Color(204, 204, 204));
        jPanel11.setLayout(flowLayout11);
        clusterCombo.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                clusterCombo_itemStateChanged(e);
            }
        });
        clusterCombo.setBackground(new Color(204, 204, 204));
        fileChooserPanel.setBackground(new Color(204, 204, 204));
        fileChooserPanel.setLayout(fileCard);
        disLabel.setBorder(border1);
        disLabel.setText("Dissimilarity File");
        disChooserPanel.setBackground(new Color(204, 204, 204));
        disChooserPanel.setLayout(borderLayout4);
        disComboBox.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                disComboBox_itemStateChanged(e);
            }
        });
        jPanel3.setLayout(verticalLayout8);
        flowLayout6.setAlignment(FlowLayout.LEFT);
        jLabel5.setBackground(new Color(204, 204, 204));
        jLabel5.setText("K (# of Clusters):");
        knumPanel.setBackground(new Color(204, 204, 204));
        knumPanel.setLayout(flowLayout6);
        kClusterField.setText("10");
        kClusterField.setColumns(5);
        kClusterField.setPreferredSize(new Dimension(50, 21));
        flowLayout10.setAlignment(FlowLayout.LEFT);
        maxCycleField.setText("20");
        maxCycleField.setColumns(5);
        kmaxPanel.setBackground(new Color(204, 204, 204));
        kmaxPanel.setLayout(flowLayout10);
        maxCycleLabel.setBackground(new Color(204, 204, 204));
        maxCycleLabel.setText("Max Cycles:");
        jPanel3.setBackground(new Color(204, 204, 204));
        gridLayout1.setColumns(2);
        jPanel5.setBackground(new Color(204, 204, 204));
        calculateBox.setBackground(new Color(204, 204, 204));
        calculateBox.setText("Constantly Calculate Seeds");
        this.getContentPane().add(jPanel1, null);
        jPanel1.add(jLabel1, BorderLayout.WEST);
        jPanel1.add(executeBox, BorderLayout.CENTER);
        this.getContentPane().add(jPanel2, null);
        jPanel2.add(disPanel, "disPanel");
        disPanel.add(jPanel15, null);
        lpPanel.add(lpButton, null);
        lpPanel.add(pLabel, null);
        lpPanel.add(pField, null);
        jPanel15.add(jackPanel, null);
        jackPanel.add(jackButton, null);
        jPanel15.add(corrPanel, null);
        corrPanel.add(corrButton, null);
        jPanel15.add(lpPanel, null);
        disPanel.add(expChooserPanel, null);
        expChooserPanel.add(jLabel3, BorderLayout.WEST);
        expChooserPanel.add(expBox, BorderLayout.CENTER);
        disPanel.add(jPanel4, null);
        jPanel4.add(jLabel2, BorderLayout.WEST);
        jPanel4.add(disField, BorderLayout.CENTER);
        this.getContentPane().add(jPanel6, null);
        jPanel6.add(addTaskButton, null);
        jPanel6.add(cancelButton, null);
        jPanel2.add(clustPanel, "clustPanel");
        clustPanel.add(jPanel8, null);
        jPanel8.add(jLabel4, BorderLayout.WEST);
        clustPanel.add(jPanel9, null);
        jPanel9.add(paramPanel, null);
        thresPane.add(thresshLabel, null);
        thresPane.add(threshField, null);
        threshPane.add(numPane, null);
        threshPane.add(thresPane, null);
        numPane.add(maxClusterLabel, null);
        numPane.add(maxClusterField, null);
        qtPanel.add(qtMinPanel, null);
        qtMinPanel.add(minClusterPanel, null);
        qtPanel.add(threshPane, null);
        minClusterPanel.add(minClusterLabel, null);
        minClusterPanel.add(minClusterField, null);
        paramPanel.add(superPanel, "superPanel");
        paramPanel.add(qtPanel, "qtPanel");
        jPanel11.add(jLabel7, null);
        jPanel11.add(superThreshField, null);
        superPanel.add(jPanel11, null);
        superPanel.add(geneCheckBox, null);
        superPanel.add(jPanel12, null);
        jPanel12.add(selectGeneButton, null);
        jPanel12.add(createButton, null);
        paramPanel.add(kmeansPanel, "kmeansPanel");
        kmeansPanel.add(jPanel3, null);
        kmaxPanel.add(maxCycleLabel, null);
        kmaxPanel.add(maxCycleField, null);
        jPanel3.add(kmaxPanel, null);
        jPanel3.add(knumPanel, null);
        knumPanel.add(jLabel5, null);
        knumPanel.add(kClusterField, null);
        kmeansPanel.add(jPanel5, null);
        jPanel5.add(calculateBox, null);
        paramPanel.add(hierPanel, "hierPanel");
        hierPanel.add(linkageLabel, null);
        hierPanel.add(hierStyle, null);
        clustPanel.add(jPanel10, null);
        jPanel10.add(disChooserPanel, null);
        disChooserPanel.add(disLabel, BorderLayout.WEST);
        jPanel10.add(jPanel14, null);
        jPanel14.add(jLabel8, BorderLayout.WEST);
        jPanel14.add(clusterField, BorderLayout.CENTER);
        hierStyle.addItem(new ComboItem("Single Linkage", true));
        hierStyle.addItem(new ComboItem("Complete Linkage", false));
        hierStyle.addItem(new ComboItem("Average Linkage", false));
        hierStyle.setRenderer(new ComboRenderer());
        hierStyle.addActionListener(new ComboListener(hierStyle));
        cardParam.show(paramPanel, "hierPanel");
        disbuttonGroup.add(jackButton);
        disbuttonGroup.add(corrButton);
        disbuttonGroup.add(lpButton);
        jPanel8.add(clusterCombo, BorderLayout.CENTER);
        clusterCombo.addItem("Hierarchical Clustering");
        clusterCombo.addItem("QT Clustering");
        clusterCombo.addItem("KMeans Clustering");
        clusterCombo.addItem("Supervised Clustering");
        disChooserPanel.add(disComboBox, BorderLayout.CENTER);
        rootPane.setDefaultButton(addTaskButton);
        pField.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                try {
                    Document doc = e.getDocument();
                    String docText = doc.getText(0, doc.getLength());
                    String name = expBox.getFileName();
                    name = name.substring(0, name.lastIndexOf("."));
                    disField.setText(name + "lp" + docText + ".dis");
                } catch (Exception e1) {
                }
            }

            public void insertUpdate(DocumentEvent e) {
                try {
                    Document doc = e.getDocument();
                    String docText = doc.getText(0, doc.getLength());
                    String name = expBox.getFileName();
                    name = name.substring(0, name.lastIndexOf("."));
                    disField.setText(name + "lp" + docText + ".dis");
                } catch (Exception e1) {
                }
            }

            public void removeUpdate(DocumentEvent e) {
                try {
                    Document doc = e.getDocument();
                    String docText = doc.getText(0, doc.getLength());
                    String name = expBox.getFileName();
                    name = name.substring(0, name.lastIndexOf("."));
                    disField.setText(name + "lp" + docText + ".dis");
                } catch (Exception e1) {
                }
            }
        });
    }

    /**
   * sets the values for a created gene for supervised qt clustering
   * @param vals values for a created gene for supervised qt clustering
   */
    public void setGeneValues(double vals[]) {
        createdValues = new double[vals.length];
        for (int i = 0; i < vals.length; i++) {
            createdValues[i] = vals[i];
        }
        valuesSet = true;
    }

    private void selectGeneButton_actionPerformed(ActionEvent e) {
        String ex = disComboBox.getSelectedItem().toString();
        ex = ex.substring(0, ex.lastIndexOf(File.separator));
        ex = project.getPath() + ex + File.separator + ex + ".exp";
        ExpFile exp = new ExpFile(new File((ex)));
        String select = (String) JOptionPane.showInputDialog(this, "Please Select A Gene", "Select One", JOptionPane.QUESTION_MESSAGE, null, exp.getGeneVector(), (selectedGene == null ? exp.getGeneName(0) : selectedGene));
        if (select != null) selectedGene = select;
    }

    private void createButton_actionPerformed(ActionEvent e) {
        String ex = disComboBox.getSelectedItem().toString();
        ex = ex.substring(0, ex.lastIndexOf(File.separator));
        ex = project.getPath() + ex + File.separator + ex + ".exp";
        gcf = new GeneCreatorFrame(new ExpFile(new File(ex)), parentFrame);
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
            createButton.setEnabled(true);
            selectGeneButton.setEnabled(false);
        }
    }

    private void executeBox_itemStateChanged(ItemEvent e) {
        if (executeBox.getSelectedIndex() == 0) {
            cardLayout1.show(jPanel2, "disPanel");
            if (expBox.getSelectedIndex() == 0) addTaskButton.setEnabled(false); else addTaskButton.setEnabled(true);
        } else if (executeBox.getSelectedIndex() == 1) {
            cardLayout1.show(jPanel2, "clustPanel");
        }
    }

    private void clusterCombo_itemStateChanged(ItemEvent e) {
        if (clusterCombo.getSelectedItem().toString().equals("Hierarchical Clustering")) {
            cardParam.show(paramPanel, "hierPanel");
            setOutFileField();
            if (disComboBox.getFileType() != Project.DIS) {
                disComboBox.setFileType(Project.DIS);
                disComboBox.reload();
                disLabel.setText("Dissimilarity File");
            }
            if (disComboBox.getSelectedIndex() != 0) addTaskButton.setEnabled(true); else addTaskButton.setEnabled(false);
        } else if (clusterCombo.getSelectedItem().toString().equals("QT Clustering")) {
            cardParam.show(paramPanel, "qtPanel");
            setOutFileField();
            if (disComboBox.getFileType() != Project.DIS) {
                disComboBox.setFileType(Project.DIS);
                disComboBox.reload();
                disLabel.setText("Dissimilarity File");
            }
            if (disComboBox.getSelectedIndex() != 0) addTaskButton.setEnabled(true); else addTaskButton.setEnabled(false);
        } else if (clusterCombo.getSelectedItem().toString().equals("KMeans Clustering")) {
            cardParam.show(paramPanel, "kmeansPanel");
            setOutFileField();
            if (disComboBox.getFileType() == Project.EXP) {
                disComboBox.setFileType(Project.EXP);
                disComboBox.reload();
                disLabel.setText("Expression File");
            }
            if (disComboBox.getSelectedIndex() != 0) addTaskButton.setEnabled(true); else addTaskButton.setEnabled(false);
        } else if (clusterCombo.getSelectedItem().toString().equals("Supervised Clustering")) {
            cardParam.show(paramPanel, "superPanel");
            setOutFileField();
            if (disComboBox.getFileType() != Project.DIS) {
                disComboBox.setFileType(Project.DIS);
                disComboBox.reload();
                disLabel.setText("Dissimilarity File");
            }
            if (disComboBox.getSelectedIndex() != 0) {
                addTaskButton.setEnabled(true);
                selectGeneButton.setEnabled(true);
                geneCheckBox.setEnabled(true);
                if (geneCheckBox.isSelected()) selectGeneButton.setEnabled(true); else {
                    selectGeneButton.setEnabled(false);
                    createButton.setEnabled(true);
                }
            } else {
                addTaskButton.setEnabled(false);
                selectGeneButton.setEnabled(false);
                geneCheckBox.setEnabled(false);
            }
        }
    }

    private void expBox_itemStateChanged(ItemEvent e) {
        if (executeBox.getSelectedIndex() == 0) {
            if (expBox.getSelectedIndex() != 0) addTaskButton.setEnabled(true); else addTaskButton.setEnabled(false);
        }
        setOutFileField();
    }

    /**
   * updates the name of the new file to default name
   */
    public void setOutFileField() {
        if (executeBox.getSelectedIndex() == 0) {
            if (expBox.getSelectedIndex() == 0) disField.setText(""); else {
                String name = expBox.getFileName();
                if (name != null) {
                    String type = "c";
                    if (jackButton.isSelected()) type = "j"; else if (lpButton.isSelected()) type = "lp" + pField.getText();
                    name = name.substring(0, name.lastIndexOf(".")) + type + ".dis";
                    disField.setText(name);
                } else disField.setText(name);
            }
        } else if (executeBox.getSelectedIndex() == 1) {
            if (disComboBox.getSelectedIndex() == 0) clusterField.setText(""); else {
                String name = disComboBox.getFileName();
                if (name != null) {
                    name = name.substring(0, name.lastIndexOf(".")) + clusterCombo.getSelectedItem().toString().substring(0, 1).toLowerCase() + ".clust";
                    clusterField.setText(name);
                } else clusterField.setText(name);
            }
        }
    }

    private void addTaskButton_actionPerformed(ActionEvent e) {
        try {
            Thread thread = new Thread() {

                public void run() {
                    Task t = null;
                    boolean canDispose = true;
                    if (executeBox.getSelectedIndex() == 0) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        String outfile = project.getPath() + expBox.getSimpleName() + File.separator + disField.getText().trim();
                        if (!outfile.endsWith(".dis")) outfile += ".dis";
                        Object modifiers[] = new Object[1];
                        modifiers[0] = "";
                        if (outfileIsValid(outfile, disField.getText().trim())) {
                            int method = Dissimilarity.COR;
                            if (lpButton.isSelected()) {
                                method = Dissimilarity.LP;
                                modifiers[0] = pField.getText().trim();
                            } else if (jackButton.isSelected()) {
                                method = Dissimilarity.JACK;
                            }
                            t = TaskFactory.createTask(Project.DIS, method, expBox.getFilePath(), outfile, project, modifiers, getDesktopPane());
                        }
                        setCursor(Cursor.getDefaultCursor());
                    } else if (executeBox.getSelectedIndex() == 1) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        String out = disComboBox.getFilePath();
                        out = out.substring(0, out.lastIndexOf(File.separator) + 1);
                        if (out != null) out += clusterField.getText().trim();
                        if (!out.endsWith(".clust")) out += ".clust";
                        if (outfileIsValid(out, clusterField.getText().trim())) {
                            int m = TaskFactory.HIERARCHICAL;
                            Object[] modifiers = new Object[0];
                            if (clusterCombo.getSelectedItem().toString().equals("Hierarchical Clustering")) {
                                m = TaskFactory.HIERARCHICAL;
                                modifiers = new Object[1];
                                modifiers[0] = new Integer(hierStyle.getSelectedIndex());
                            } else if (clusterCombo.getSelectedItem().toString().equals("QT Clustering")) {
                                m = TaskFactory.QTCLUST;
                                modifiers = new Object[3];
                                modifiers[0] = new Float(Float.parseFloat(threshField.getText()));
                                modifiers[1] = new Integer(Integer.parseInt(maxClusterField.getText()));
                                modifiers[2] = new Integer(Integer.parseInt(minClusterField.getText()));
                            } else if (clusterCombo.getSelectedItem().toString().equals("KMeans Clustering")) {
                                m = TaskFactory.KMEANS;
                                modifiers = new Object[3];
                                modifiers[0] = new Integer(Integer.parseInt(maxCycleField.getText().trim()));
                                modifiers[1] = new Integer(Integer.parseInt(kClusterField.getText().trim()));
                                modifiers[2] = new Boolean(calculateBox.isSelected());
                            } else if (clusterCombo.getSelectedItem().toString().equals("Supervised Clustering")) {
                                m = TaskFactory.SUPERVISED;
                                if (geneCheckBox.isSelected()) {
                                    if (selectedGene != null) {
                                        modifiers = new Object[3];
                                        modifiers[0] = new Boolean(true);
                                        modifiers[1] = new Float(Float.parseFloat(superThreshField.getText()));
                                        modifiers[2] = selectedGene;
                                    } else {
                                        JOptionPane.showMessageDialog(parentFrame, "Error! You Must Select A Gene");
                                        canDispose = false;
                                    }
                                } else {
                                    if (valuesSet) {
                                        modifiers = new Object[createdValues.length + 2];
                                        modifiers[0] = new Boolean(false);
                                        modifiers[1] = new Float(Float.parseFloat(superThreshField.getText()));
                                        for (int i = 0; i < createdValues.length; i++) {
                                            modifiers[i + 2] = new Double(createdValues[i]);
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(parentFrame, "Error! You Must Create A Gene");
                                        canDispose = false;
                                    }
                                }
                            }
                            if (canDispose) {
                                t = TaskFactory.createTask(Project.CLUST, m, disComboBox.getFilePath(), out, project, modifiers, getDesktopPane());
                            }
                        }
                    }
                    if (t != null && canDispose) {
                        manager.addTask(t);
                        dispose();
                        setCursor(Cursor.getDefaultCursor());
                    }
                }
            };
            thread.start();
        } catch (NumberFormatException e1) {
            JOptionPane.showMessageDialog(this, "Error! One Or More TextFields Contains Improper Number");
        }
    }

    private boolean outfileIsValid(String outfile, String name) {
        boolean goodFile;
        outfile.trim();
        if (name.indexOf(File.separator) != -1) {
            JOptionPane.showMessageDialog(this, "Error! Invalid Filename");
            return false;
        }
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
        } else if (manager.fileExists(outFile.getParentFile().getName() + File.separator + outFile.getName())) {
            JOptionPane.showMessageDialog(this, "Error! This Filename Has Already Been Created In The Task Manager.\nPlease Select Another Name");
            return false;
        }
        return true;
    }

    private void cancelButton_actionPerformed(ActionEvent e) {
        this.dispose();
    }

    private void disComboBox_itemStateChanged(ItemEvent e) {
        String name = disComboBox.getFileName();
        if (disComboBox.getSelectedIndex() != 0) {
            geneCheckBox.setEnabled(true);
            addTaskButton.setEnabled(true);
            if (geneCheckBox.isSelected()) selectGeneButton.setEnabled(true); else {
                selectGeneButton.setEnabled(false);
                createButton.setEnabled(true);
            }
        } else {
            selectGeneButton.setEnabled(false);
            createButton.setEnabled(false);
            geneCheckBox.setEnabled(false);
            addTaskButton.setEnabled(false);
        }
        setOutFileField();
        valuesSet = false;
        selectedGene = null;
    }

    private void lpButton_itemStateChanged(ItemEvent e) {
        if (lpButton.isSelected()) {
            setOutFileField();
            pField.setEnabled(true);
        } else pField.setEnabled(false);
    }

    private void corrButton_itemStateChanged(ItemEvent e) {
        if (corrButton.isSelected()) setOutFileField();
    }

    private void jackButton_itemStateChanged(ItemEvent e) {
        if (jackButton.isSelected()) setOutFileField();
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
}
