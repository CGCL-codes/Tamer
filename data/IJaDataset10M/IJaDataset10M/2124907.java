package game.gui;

import game.neurons.Neuron;
import game.trainers.Trainer;
import game.configuration.NetworkConfiguration;
import game.gui.GMDHtree;
import game.utils.UnitLoader;
import game.stopping.GLaEarlyStopping;
import game.stopping.ValMinEarlyStopping;
import game.neurons.Unit;
import game.configuration.NetworkGAMEConfiguration;
import game.configuration.GlobalConfig;
import game.data.GlobalData;
import java.awt.event.ItemListener;
import java.util.Vector;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.Insets;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JDialog;
import java.awt.event.ActionListener;

public class ConfigGAMENetwork extends JDialog implements ItemListener, ActionListener {

    private UnitLoader u;

    private GLaEarlyStopping estop = new GLaEarlyStopping();

    private ValMinEarlyStopping vstop = new ValMinEarlyStopping();

    private int groups;

    private Vector panels;

    private Vector panelNames;

    private Vector processed;

    private JPanel panel1 = new JPanel();

    private BorderLayout borderLayout1 = new BorderLayout();

    JButton button1 = new JButton();

    JPanel panel2 = new JPanel();

    private JTabbedPane tabpanel = new JTabbedPane();

    private GMDHtree sef;

    private GridLayout gridLayout1 = new GridLayout();

    private JTextField[] iPop;

    private JTextField[] pop;

    private JTextField[] epoch;

    JTextField[] bpipop;

    private ButtonGroup rgroup;

    private JRadioButton brms;

    private JRadioButton brmsp;

    private JRadioButton brmspn;

    private JButton ok;

    private static String[] LIST = { "Complexity", "Unit types", "Training methods", "Evolution", "Data", "Connections", "Others" };

    private static String[] TYPE = { "Linear", "Polynomial", "Perceptron", "Other" };

    private static int TYPES = 4;

    private DefaultMutableTreeNode[] tops;

    private DefaultMutableTreeNode[] types;

    private static int LISTS = 7;

    private JCheckBox[] allowed;

    private JCheckBox[] gallowed;

    private JCheckBox[] trainerAllowed;

    private JLabel label1 = new JLabel();

    private JLabel label2 = new JLabel();

    private static int GMDH_LAYERS = NetworkConfiguration.MAX_LAYERS;

    JPanel list = new JPanel();

    GridLayout gridLayout2 = new GridLayout();

    private JPanel panel6 = new JPanel();

    private ButtonGroup checkboxGroup1 = new ButtonGroup();

    private JPanel panel7 = new JPanel();

    private JRadioButton checkbox1 = new JRadioButton();

    private JRadioButton checkbox2 = new JRadioButton();

    private GridLayout gridLayout3 = new GridLayout();

    private JPanel panel8 = new JPanel();

    private GridLayout gridLayout4 = new GridLayout();

    private JRadioButton checkbox3 = new JRadioButton();

    private JRadioButton checkbox4 = new JRadioButton();

    private JRadioButton checkbox5 = new JRadioButton();

    private JRadioButton checkbox6 = new JRadioButton();

    private JRadioButton checkbox7 = new JRadioButton();

    private ButtonGroup checkboxGroup2 = new ButtonGroup();

    private JRadioButton checkbox8 = new JRadioButton();

    private JCheckBox employLast = new JCheckBox();

    private JCheckBox genomeDistance = new JCheckBox();

    private JCheckBox correlationDistance = new JCheckBox();

    private JSlider randomChildren = new JSlider();

    private JCheckBox bootstrap = new JCheckBox();

    private JSlider maxdrop;

    private JSlider muta;

    private JSlider distMatters;

    private JSlider iS;

    private JSlider isS;

    private JSlider popu;

    private JSlider epoc;

    private JTree tree;

    private GridLayout gridLayout5 = new GridLayout();

    private JPanel panel9 = new JPanel();

    private JRadioButton checkbox9 = new JRadioButton();

    private JCheckBox crowding = new JCheckBox();

    private JCheckBox stop = new JCheckBox();

    private JCheckBox tit = new JCheckBox();

    private ButtonGroup checkboxGroup3 = new ButtonGroup();

    private JCheckBox normalize;

    private boolean[] typeAllowed;

    private int activeConfig;

    /**
     * show the config window
     *
     * @param boss
     * @param title
     * @param modal
     * @param group number of data records
     */
    public ConfigGAMENetwork(GMDHtree boss, String title, boolean modal, int group) {
        super(GMDHtree.frame, title, modal);
        sef = boss;
        groups = group;
        u = UnitLoader.getInstance();
        panels = new Vector();
        panelNames = new Vector();
        processed = new Vector();
        allowed = new JCheckBox[u.getNeuronsNumber()];
        gallowed = new JCheckBox[TYPES];
        trainerAllowed = new JCheckBox[u.getTrainersNumber()];
        typeAllowed = new boolean[TYPES];
        pop = new JTextField[10];
        iPop = new JTextField[10];
        epoch = new JTextField[10];
        try {
            jbInit();
            this.getContentPane().add(panel1);
            pack();
            this.setLocation(50, 50);
            setResizable(false);
            doLayout();
            setVisible(true);
            toFront();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * graphics stuffs
     * @throws Exception
     * @throws Exception
     * @throws Exception
     */
    void jbInit() throws Exception {
        borderLayout1.setHgap(5);
        borderLayout1.setVgap(5);
        panel1.setLayout(borderLayout1);
        panels.add(createUnitNumbersPanel());
        panelNames.add(LIST[0]);
        panels.add(createUnitTypesPanel());
        panelNames.add(LIST[1]);
        panels.add(createTrainingMethodsPanel());
        panelNames.add(LIST[2]);
        panels.add(createNichingPanel());
        panelNames.add(LIST[3]);
        panels.add(createDataPanel());
        panelNames.add(LIST[4]);
        panels.add(createConnectionsPanel());
        panelNames.add(LIST[5]);
        panels.add(createOtherPanel());
        panelNames.add(LIST[6]);
        ok = new JButton();
        ok.setText("OK");
        ok.addActionListener(this);
        tops = new DefaultMutableTreeNode[LISTS];
        types = new DefaultMutableTreeNode[TYPES];
        for (int i = 0; i < LISTS; i++) {
            tops[i] = new DefaultMutableTreeNode(LIST[i]);
        }
        for (int i = 0; i < TYPES; i++) {
            types[i] = new DefaultMutableTreeNode(TYPE[i]);
            tops[1].add(types[i]);
        }
        Neuron nen;
        for (int j = 0; j < TYPES; j++) {
            for (int i = 0; i < u.getNeuronsNumber(); i++) {
                nen = (Neuron) u.getNeuronClass(i).newInstance();
                if (nen.getType().indexOf(TYPE[j]) >= 0) {
                    processed.add(u.getNeuronName(i));
                }
            }
        }
        for (int i = 0; i < u.getTrainersNumber(); i++) {
            if (u.getTrainerConfig(i) != null) {
                tops[2].add(new DefaultMutableTreeNode(u.getTrainerName(i)));
            }
        }
        DefaultMutableTreeNode topps = new DefaultMutableTreeNode("GAME configuration");
        for (int i = 0; i < LISTS; i++) {
            topps.add(tops[i]);
        }
        tree = new JTree(topps) {

            public Insets getInsets() {
                return new Insets(5, 5, 5, 5);
            }
        };
        tree.setEditable(false);
        JScrollPane conf = new JScrollPane(tree);
        MouseListener ml = new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                int selRow = tree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                if (selRow != -1) {
                    if (e.getClickCount() > 0) {
                        treeNodeSelected(selPath.getLastPathComponent().toString());
                    }
                }
            }
        };
        tree.addMouseListener(ml);
        for (int i = 0; i < u.getNeuronsNumber(); i++) {
            nen = (Neuron) u.getNeuronClass(i).newInstance();
            allowed[i] = new JCheckBox(u.getNeuronName(i) + ", " + nen.getType() + ", " + nen.getTrainedBy(), GlobalConfig.getInstance().getGac().neuronTypeAllowed(i));
        }
        panel1.add(conf, BorderLayout.WEST);
        for (int i = 0; i < panels.size(); i++) {
            tabpanel.add((String) panelNames.elementAt(i), (JPanel) panels.elementAt(i));
        }
        panel1.add(tabpanel, BorderLayout.CENTER);
        panel1.add(ok, BorderLayout.SOUTH);
    }

    private JPanel createNichingPanel() {
        JPanel pan23 = new JPanel();
        pan23.setLayout(new BoxLayout(pan23, BoxLayout.Y_AXIS));
        maxdrop = new JSlider(JSlider.HORIZONTAL, 0, 1000, (int) (GlobalConfig.getInstance().getGac().getMaximalDiversityDrop() * 1000));
        muta = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) (GlobalConfig.getInstance().getGac().getMutationRate() * 100));
        distMatters = new JSlider(JSlider.HORIZONTAL, 0, 500, (int) GlobalConfig.getInstance().getGac().getDistanceMatters());
        JPanel pp = new JPanel();
        pp.setLayout(new BoxLayout(pp, BoxLayout.X_AXIS));
        pp.add(maxdrop);
        ChangeListener sl = new SliderListener(pp, "Minimum diversity allowed - the fraction of initial diversity to stop GA", 1000);
        maxdrop.addChangeListener(sl);
        sl.stateChanged(new ChangeEvent(maxdrop));
        JPanel rpp = new JPanel();
        rpp.setLayout(new BoxLayout(rpp, BoxLayout.X_AXIS));
        distMatters.setPaintLabels(false);
        rpp.add(distMatters);
        ChangeListener s2 = new SliderListener(rpp, "Specify how important the distance of individuals should be:", 1);
        distMatters.addChangeListener(s2);
        s2.stateChanged(new ChangeEvent(distMatters));
        JPanel pppb = new JPanel();
        pppb.setLayout(new FlowLayout());
        genomeDistance = new JCheckBox("Distance of units: take into account the difference of inputs", GlobalConfig.getInstance().getGac().isGenomeDistance());
        pppb.add(genomeDistance);
        pan23.add(pppb);
        JPanel pppc = new JPanel();
        pppc.setLayout(new FlowLayout());
        correlationDistance = new JCheckBox("Distance: take the correlation of errors on training game.data vectors", GlobalConfig.getInstance().getGac().isCorrelationDistance());
        pppc.add(correlationDistance);
        pan23.add(pppc);
        JPanel pppd = new JPanel();
        pppd.setLayout(new BoxLayout(pppd, BoxLayout.X_AXIS));
        randomChildren = new JSlider(JSlider.HORIZONTAL, 0, 100, GlobalConfig.getInstance().getGac().getRandomChildren());
        pppd.add(randomChildren);
        ChangeListener s3 = new SliderListener(pppd, "Offsprings of units [(0=type and trainer derived from parents)...(100=random)]:", 1);
        randomChildren.addChangeListener(s3);
        s3.stateChanged(new ChangeEvent(randomChildren));
        JPanel pppa = new JPanel();
        pppa.setLayout(new FlowLayout());
        employLast = new JCheckBox("Generate one input from previous layer for each individual in the initial population", GlobalConfig.getInstance().getGac().isEmployPrevious());
        pppa.add(employLast);
        pan23.add(pppa);
        JPanel ppa = new JPanel();
        ppa.setLayout(new BoxLayout(ppa, BoxLayout.X_AXIS));
        ppa.add(muta);
        ChangeListener s4 = new SliderListener(ppa, "Mutation rate (evolution of units) (1 = 100%)", 100);
        muta.addChangeListener(s4);
        s4.stateChanged(new ChangeEvent(muta));
        JPanel nnn = new JPanel();
        nnn.setLayout(new FlowLayout());
        crowding = new JCheckBox("Deterministic crowding enabled (if disabled simple GA is used)", GlobalConfig.getInstance().getGac().isCrowdingEmployed());
        nnn.add(crowding);
        pan23.add(nnn);
        pan23.add(pp);
        pan23.add(pppd);
        pan23.add(rpp);
        pan23.add(ppa);
        return pan23;
    }

    private JPanel createDataPanel() {
        JPanel pan23 = new JPanel();
        pan23.setLayout(new BoxLayout(pan23, BoxLayout.Y_AXIS));
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        iS = new JSlider(JSlider.HORIZONTAL, 1, 9999, GlobalConfig.getInstance().getGac().getLearnPercent());
        p.add(iS);
        ChangeListener s3 = new SliderListener(p, "Maximal number of training vectors used for learning:", 1);
        iS.addChangeListener(s3);
        s3.stateChanged(new ChangeEvent(iS));
        JPanel pp = new JPanel();
        pp.setLayout(new BoxLayout(pp, BoxLayout.X_AXIS));
        isS = new JSlider(JSlider.HORIZONTAL, 0, 100, 100 - GlobalConfig.getInstance().getGac().getVectorsInTestingSet());
        pp.add(isS);
        ChangeListener s4 = new SliderListener(pp, "The size of the Learning and Validation set: ", 1);
        isS.addChangeListener(s4);
        s4.stateChanged(new ChangeEvent(isS));
        pan23.add(pp);
        pan23.add(p);
        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout());
        tit = new JCheckBox("Validate on the training game.data set, too", GlobalConfig.getInstance().getGac().isTestOnBothTrainingAndTestingData());
        p2.add(tit);
        pan23.add(p2);
        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout());
        bootstrap = new JCheckBox(" Use bootstrap sampling (othewise training vectors are selected without replacement) ", GlobalConfig.getInstance().getGac().isBootstrap());
        p1.add(bootstrap);
        pan23.add(p1);
        return pan23;
    }

    private JPanel createOtherPanel() {
        JPanel pan23 = new JPanel();
        pan23.setLayout(new BoxLayout(pan23, BoxLayout.Y_AXIS));
        JPanel x = new JPanel();
        x.setLayout(new BoxLayout(x, BoxLayout.Y_AXIS));
        x.setBorder(new TitledBorder("The Regularization of GAME units"));
        brms = new JRadioButton("CR = Error on the training and validation set(RMS)");
        x.add(brms);
        brmsp = new JRadioButton("CR = RMS + Penalization of Complexity(p)");
        x.add(brmsp);
        brmspn = new JRadioButton("CR = RMS * [1 + p * Noise in the game.data set(noise)]");
        x.add(brmspn);
        pan23.add(x);
        rgroup = new ButtonGroup();
        rgroup.add(brms);
        rgroup.add(brmsp);
        rgroup.add(brmspn);
        if (GlobalConfig.getInstance().getGac().getRegularization() == NetworkGAMEConfiguration.RMS) brms.setSelected(true);
        if (GlobalConfig.getInstance().getGac().getRegularization() == NetworkGAMEConfiguration.RMS_PENALTY) brmsp.setSelected(true);
        if (GlobalConfig.getInstance().getGac().getRegularization() == NetworkGAMEConfiguration.RMS_PENALTY_NOISE) brmspn.setSelected(true);
        JPanel nnn = new JPanel(new FlowLayout());
        stop = new JCheckBox("Build layers while error decreases", GlobalConfig.getInstance().getGac().isBuildWhileDec());
        nnn.add(stop);
        pan23.add(nnn);
        normalize = new JCheckBox("Normalize input and output game.data into interval <0,1>", GlobalConfig.getInstance().getGc().isNormalization());
        JPanel njn = new JPanel();
        njn.setLayout(new FlowLayout());
        njn.add(normalize);
        pan23.add(njn);
        if (estop != null) {
            JPanel pp = new JPanel();
            pp.setBorder(new TitledBorder("Some units use stopping criteria to prevent overtraining"));
            pp.setLayout(new BoxLayout(pp, BoxLayout.Y_AXIS));
            pp.add(estop.configPanel());
            pp.add(vstop.configPanel());
            pan23.add(pp);
        }
        return pan23;
    }

    /**
     * createConnectionsPanel
     *
     * @return panel
     */
    private JPanel createConnectionsPanel() {
        checkboxGroup1.add(checkbox1);
        checkbox1.setText("Number of inputs to unit NOT LIMITED");
        checkboxGroup1.add(checkbox2);
        if (GlobalConfig.getInstance().getGac().isJustTwo()) {
            checkbox1.setSelected(true);
        } else {
            checkbox2.setSelected(true);
        }
        checkbox2.setText("Max. number of inputs set to layer index(growing complexity)");
        panel7.setLayout(gridLayout3);
        gridLayout3.setRows(2);
        gridLayout3.setColumns(1);
        panel8.setLayout(gridLayout4);
        gridLayout4.setRows(5);
        gridLayout4.setColumns(1);
        checkboxGroup2.add(checkbox3);
        checkbox3.setText("Extra input units from the INPUT layer only");
        checkboxGroup2.add(checkbox4);
        checkbox4.setText("Extra input units from the PREVIOUS layer only");
        checkboxGroup2.add(checkbox5);
        checkbox5.setText("Extra input units mostly from the PREVIOUS layer");
        checkboxGroup2.add(checkbox6);
        checkbox6.setText("Randomly generated extra input units");
        checkboxGroup2.add(checkbox7);
        checkbox7.setText("Extra input units mostly from the INPUT layer");
        switch(GlobalConfig.getInstance().getGac().getParents()) {
            case NetworkConfiguration.YOUNG:
                checkbox4.setSelected(true);
                break;
            case NetworkConfiguration.YOUNGER:
                checkbox5.setSelected(true);
                break;
            case NetworkConfiguration.MIDDLE:
                checkbox6.setSelected(true);
                break;
            case NetworkConfiguration.OLDER:
                checkbox7.setSelected(true);
                break;
            case NetworkConfiguration.OLD:
                checkbox3.setSelected(true);
                break;
        }
        checkbox8.setText("Keep units with higher error than best unit from previous layer");
        checkboxGroup3.add(checkbox8);
        gridLayout5.setColumns(1);
        gridLayout5.setRows(2);
        panel9.setLayout(gridLayout5);
        checkboxGroup3.add(checkbox9);
        checkbox9.setText("Delete units worse than the best unit of the previous layer");
        if (GlobalConfig.getInstance().getGac().isDeleteWorse()) {
            checkbox8.setSelected(true);
        } else {
            checkbox9.setSelected(true);
        }
        panel9.setBorder(new EmptyBorder(10, 5, 0, 0));
        panel8.setBorder(new TitledBorder("Select from where extra inputs should be taken"));
        panel7.setBorder(new EmptyBorder(0, 5, 10, 0));
        panel6.setLayout(new BoxLayout(panel6, BoxLayout.Y_AXIS));
        panel6.add(panel7);
        panel7.add(checkbox1, null);
        panel7.add(checkbox2, null);
        panel6.add(panel8);
        panel8.add(checkbox3, null);
        panel8.add(checkbox7, null);
        panel8.add(checkbox6, null);
        panel8.add(checkbox5, null);
        panel8.add(checkbox4, null);
        panel6.add(panel9);
        panel9.add(checkbox8, null);
        panel9.add(checkbox9, null);
        return panel6;
    }

    /**
     * createUnitNumbersPanel
     *
     * @return panel
     */
    private JPanel createUnitNumbersPanel() {
        JPanel lab = new JPanel();
        lab.setLayout(new BoxLayout(lab, BoxLayout.Y_AXIS));
        JPanel lb = new JPanel();
        lb.setBorder(new TitledBorder("Set for all layers"));
        lb.setLayout(new BoxLayout(lb, BoxLayout.X_AXIS));
        popu = new JSlider(JSlider.HORIZONTAL, 1, 1000, GlobalConfig.getInstance().getGac().getLayerInitialNeuronsNumber(0));
        lb.add(createPanel(popu, 1, "Population: "));
        epoc = new JSlider(JSlider.HORIZONTAL, 0, 500, GlobalConfig.getInstance().getGac().getLayerEpochs(0));
        lb.add(createPanel(epoc, 1, "Epochs: "));
        JPanel panel3 = new JPanel();
        label1.setText("Populat. size");
        label2.setText("Max.surv.units");
        panel3.setLayout(gridLayout1);
        gridLayout1.setRows(11);
        gridLayout1.setColumns(3);
        JPanel pan3 = new JPanel();
        pan3.setLayout(new BoxLayout(pan3, BoxLayout.X_AXIS));
        panel3.add(new JLabel(""));
        panel3.add(label1, null);
        panel3.add(label2, null);
        panel3.add(new JLabel("Epochs"), null);
        pan3.add(panel3);
        for (int i = 0; i < 10; i++) {
            iPop[i] = new JTextField(Integer.toString(GlobalConfig.getInstance().getGac().getLayerInitialNeuronsNumber(i)), 3);
            pop[i] = new JTextField(Integer.toString(GlobalConfig.getInstance().getGac().getLayerNeuronsNumber(i)), 3);
            epoch[i] = new JTextField(Integer.toString(GlobalConfig.getInstance().getGac().getLayerEpochs(i)), 3);
            if (i == 9) {
                panel3.add(new JLabel("Layer 10-" + Integer.toString(GMDH_LAYERS)));
            } else {
                panel3.add(new JLabel("Layer " + Integer.toString(i + 1)));
            }
            panel3.add(iPop[i], null);
            panel3.add(pop[i], null);
            panel3.add(epoch[i], null);
        }
        panel3.setBorder(new EmptyBorder(10, 10, 10, 10));
        lab.add(panel3);
        lab.add(lb);
        return lab;
    }

    private JPanel createTrainingMethodsPanel() {
        JPanel panel5 = new JPanel();
        panel5.setLayout(new BoxLayout(panel5, BoxLayout.Y_AXIS));
        panel5.setBorder(new TitledBorder("Select which types of trainig methods are allowed"));
        Trainer nen = new Trainer();
        for (int i = 0; i < u.getTrainersNumber(); i++) {
            try {
                nen = (Trainer) u.getTrainerClass(i).newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            trainerAllowed[i] = new JCheckBox(nen.getMethodName() + " [" + u.getTrainerName(i) + "] ", GlobalConfig.getInstance().getGac().neuronTrainerAllowed(i));
            panel5.add(trainerAllowed[i]);
        }
        return panel5;
    }

    /**
     * createUnitsAllowedPanel
     *
     * @param unitType
     * @param indexType
     * @return panel
     */
    private JPanel createUnitTypesPanel(String unitType, int indexType) {
        JPanel panel5 = new JPanel();
        Neuron nen;
        panel5.setLayout(new BoxLayout(panel5, BoxLayout.Y_AXIS));
        if (unitType.compareTo("Other") == 0) {
            for (int i = 0; i < u.getNeuronsNumber(); i++) {
                if (!processed.contains(u.getNeuronName(i))) {
                    if (u.getNeuronConfig(i) != null) {
                        types[indexType].add(new DefaultMutableTreeNode(u.getNeuronName(i)));
                    }
                    panel5.add(allowed[i]);
                }
            }
        } else {
            for (int i = 0; i < u.getNeuronsNumber(); i++) {
                try {
                    nen = (Neuron) u.getNeuronClass(i).newInstance();
                    if (nen.getType().indexOf(unitType) >= 0) {
                        panel5.add(allowed[i]);
                        if (u.getNeuronConfig(i) != null) {
                            types[indexType].add(new DefaultMutableTreeNode(u.getNeuronName(i)));
                        }
                    }
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        panel5.setBorder(new TitledBorder("Select which units are allowed:"));
        return panel5;
    }

    /**
     * createUnitsAllowedPanel
     *
     * @return panel
     */
    private JPanel createUnitTypesPanel() {
        JPanel panel5 = new JPanel();
        panel5.setBorder(new TitledBorder("Enable/Disable all units with transfer function of following type:"));
        panel5.setLayout(new BoxLayout(panel5, BoxLayout.Y_AXIS));
        for (int i = 0; i < TYPES; i++) {
            gallowed[i] = new JCheckBox(TYPE[i], typeAllowed[i]);
            gallowed[i].addItemListener(this);
            panel5.add(gallowed[i]);
        }
        return panel5;
    }

    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            cancel();
        }
        super.processWindowEvent(e);
    }

    void cancel() {
        sef.continues = true;
        dispose();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ok) {
            this.setVisible(false);
            for (int i = 0; i < u.getNeuronsNumber(); i++) {
                if (u.getNeuronConfig(i) != null) {
                    ((MyConfig) u.getNeuronConfig(i)).setValues();
                }
            }
            for (int i = 0; i < u.getNeuronsNumber(); i++) {
                if (allowed[i] != null) GlobalConfig.getInstance().getGac().setTypeAllowed(i, allowed[i].isSelected());
            }
            for (int i = 0; i < u.getTrainersNumber(); i++) {
                if (trainerAllowed[i] != null) GlobalConfig.getInstance().getGac().setTrainerAllowed(i, trainerAllowed[i].isSelected());
            }
            estop.setValues();
            vstop.setValues();
            GlobalConfig.getInstance().getGac().setDeleteWorse(checkbox8.isSelected());
            GlobalConfig.getInstance().getGac().setJustTwo(checkbox1.isSelected());
            GlobalConfig.getInstance().getGac().setEmployPrevious(employLast.isSelected());
            GlobalConfig.getInstance().getGac().setLearnPercent(iS.getValue());
            GlobalConfig.getInstance().getGac().setVectorsInTestingSet(100 - isS.getValue());
            GlobalConfig.getInstance().getGac().setCrowdingEmployed(crowding.isSelected());
            GlobalConfig.getInstance().getGac().setBuildWhileDec(stop.isSelected());
            GlobalConfig.getInstance().getGac().setGenomeDistance(genomeDistance.isSelected());
            GlobalConfig.getInstance().getGac().setCorrelationDistance(correlationDistance.isSelected());
            GlobalConfig.getInstance().getGac().setRandomChildren(randomChildren.getValue());
            GlobalConfig.getInstance().getGac().setBootstrap(bootstrap.isSelected());
            GlobalConfig.getInstance().getGac().setTestOnBothTrainingAndTestingData(tit.isSelected());
            if (checkbox4.isSelected()) {
                GlobalConfig.getInstance().getGac().setParents(NetworkConfiguration.YOUNG);
            }
            if (checkbox5.isSelected()) {
                GlobalConfig.getInstance().getGac().setParents(NetworkConfiguration.YOUNGER);
            }
            if (checkbox6.isSelected()) {
                GlobalConfig.getInstance().getGac().setParents(NetworkConfiguration.MIDDLE);
            }
            if (checkbox7.isSelected()) {
                GlobalConfig.getInstance().getGac().setParents(NetworkConfiguration.OLDER);
            }
            if (checkbox3.isSelected()) {
                GlobalConfig.getInstance().getGac().setParents(NetworkConfiguration.OLD);
            }
            GlobalConfig.getInstance().getGac().setMaximalDiversityDrop((double) maxdrop.getValue() / 1000.0);
            GlobalConfig.getInstance().getGac().setMutationRate((double) muta.getValue() / 100.0);
            GlobalConfig.getInstance().getGac().setDistanceMatters(distMatters.getValue());
            GlobalConfig.getInstance().getGc().setNormalization(normalize.isSelected());
            if (brms.isSelected()) GlobalConfig.getInstance().getGac().setRegularization(NetworkGAMEConfiguration.RMS);
            if (brmsp.isSelected()) GlobalConfig.getInstance().getGac().setRegularization(NetworkGAMEConfiguration.RMS_PENALTY);
            if (brmspn.isSelected()) GlobalConfig.getInstance().getGac().setRegularization(NetworkGAMEConfiguration.RMS_PENALTY_NOISE);
            try {
                for (int i = 0; i < GMDH_LAYERS; i++) {
                    int ii = i;
                    if (i > 9) {
                        ii = 9;
                    }
                    GlobalConfig.getInstance().getGac().setLayerNeuronsNumber(i, Integer.valueOf(pop[ii].getText()));
                    GlobalConfig.getInstance().getGac().setLayerInitialNeuronsNumber(i, Integer.valueOf(iPop[ii].getText()));
                    GlobalConfig.getInstance().getGac().setLayerEpochs(i, Integer.valueOf(epoch[ii].getText()));
                    if (GlobalConfig.getInstance().getGac().getLayerNeuronsNumber(i) > GlobalConfig.getInstance().getGac().getLayerInitialNeuronsNumber(i)) {
                        GlobalConfig.getInstance().getGac().setLayerNeuronsNumber(i, GlobalConfig.getInstance().getGac().getLayerInitialNeuronsNumber(i));
                    }
                }
            } catch (java.lang.NumberFormatException bad) {
            }
            cancel();
            sef.continues = true;
        }
    }

    void treeNodeSelected(String sel) {
        for (int i = 0; i < u.getNeuronsNumber(); i++) {
            if (u.getNeuronConfig(i) != null) {
                if (panelNames.contains(u.getNeuronName(i))) {
                    ((MyConfig) u.getNeuronConfig(i)).setValues();
                    int ind = panelNames.indexOf(u.getNeuronName(i));
                    panels.remove(ind);
                    panelNames.remove(ind);
                    tabpanel.remove(tabpanel.indexOfTab(u.getNeuronName(i)));
                }
                if (sel.compareTo(u.getNeuronName(i)) == 0) {
                    panelNames.add(u.getNeuronName(i));
                    panels.add(((MyConfig) u.getNeuronConfig(i)).showPanel());
                    tabpanel.add(u.getNeuronName(i), ((MyConfig) u.getNeuronConfig(i)).showPanel());
                }
            }
        }
        for (int i = 0; i < u.getTrainersNumber(); i++) {
            if (u.getTrainerConfig(i) != null) {
                if (panelNames.contains(u.getTrainerName(i))) {
                    int ind = panelNames.indexOf(u.getTrainerName(i));
                    panels.remove(ind);
                    panelNames.remove(ind);
                    tabpanel.remove(tabpanel.indexOfTab(u.getTrainerName(i)));
                }
                if (sel.compareTo(u.getTrainerName(i)) == 0) {
                    panelNames.add(u.getTrainerName(i));
                    panels.add(((MyConfig) u.getTrainerConfig(i)).showPanel());
                    tabpanel.add(u.getTrainerName(i), ((MyConfig) u.getTrainerConfig(i)).showPanel());
                }
            }
        }
        for (int j = 0; j < TYPES; j++) {
            if (panelNames.contains(TYPE[j])) {
                int ind = panelNames.indexOf(TYPE[j]);
                panels.remove(ind);
                panelNames.remove(ind);
                tabpanel.remove(tabpanel.indexOfTab(TYPE[j]));
            }
            if (sel.compareTo(TYPE[j]) == 0) {
                panelNames.add(TYPE[j]);
                JPanel pp = createUnitTypesPanel(TYPE[j], j);
                panels.add(pp);
                tabpanel.add(TYPE[j], pp);
            }
        }
        tabpanel.setSelectedIndex(panelNames.indexOf(sel));
        pack();
    }

    public void itemStateChanged(ItemEvent e) {
        for (int i = 0; i < TYPES; i++) if (e.getSource() == gallowed[i]) {
            if (gallowed[i].isSelected()) markAllUnits(TYPE[i], true); else markAllUnits(TYPE[i], false);
        }
    }

    /**
     * markAllUnits - enables/disables all units with the specified transfer function
     *
     * @param type String transfer function
     * @param enable      boolean enable/disable
     */
    private void markAllUnits(String type, boolean enable) {
        for (int i = 0; i < u.getNeuronsNumber(); i++) {
            try {
                if (((Unit) u.getNeuronClass(i).newInstance()).getType().indexOf(type) >= 0) {
                    GlobalConfig.getInstance().getGac().setTypeAllowed(i, enable);
                    if (allowed[i] != null) allowed[i].setSelected(enable);
                } else if (type.compareTo("Other") == 0) {
                    if (!processed.contains(u.getNeuronName(i))) {
                        GlobalConfig.getInstance().getGac().setTypeAllowed(i, enable);
                        if (allowed[i] != null) allowed[i].setSelected(enable);
                    }
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    JPanel createPanel(JSlider slider, int multiplication, String text) {
        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
        p1.add(slider);
        ChangeListener s1 = new SliderListener(p1, text, multiplication);
        slider.addChangeListener(s1);
        s1.stateChanged(new ChangeEvent(slider));
        return p1;
    }

    class SliderListener implements ChangeListener {

        JPanel tf;

        String ss;

        int mul;

        public SliderListener(JPanel f, String s, int multiply) {
            tf = f;
            ss = s;
            mul = multiply;
        }

        public void stateChanged(ChangeEvent e) {
            JSlider s1 = (JSlider) e.getSource();
            if (s1.equals(isS)) {
                String d = ss + "  " + s1.getValue() + "% /" + Integer.toString(100 - s1.getValue()) + "% ";
                if (GlobalData.getInstance().getInstNumber() > 0) d += "(learn: " + Integer.toString(GlobalData.getInstance().getInstNumber() * (s1.getValue()) / 100) + "/valid: " + Integer.toString(GlobalData.getInstance().getInstNumber() * (100 - s1.getValue()) / 100) + ")";
                tf.setBorder(new TitledBorder(d));
                return;
            }
            if (s1.equals(iS)) {
                String d = ss + "  " + s1.getValue() + " ";
                if (s1.getValue() == 9999) d = ss + " unlimited";
                tf.setBorder(new TitledBorder(d));
                return;
            }
            if (s1.equals(popu) || s1.equals(epoc)) {
                if (epoc != null && popu != null) {
                    for (int i = 0; i < GMDH_LAYERS; i++) {
                        int ii = i;
                        if (i > 9) {
                            ii = 9;
                        }
                        int p = popu.getValue();
                        int ep = epoc.getValue();
                        if (iPop[ii] != null) iPop[ii].setText(Integer.toString(p));
                        if (epoch[ii] != null) epoch[ii].setText(Integer.toString(ep));
                        GlobalConfig.getInstance().getGac().setLayerInitialNeuronsNumber(i, p);
                        GlobalConfig.getInstance().getGac().setLayerEpochs(i, ep);
                    }
                }
            }
            tf.setBorder(new TitledBorder(ss + "  " + Double.toString((double) s1.getValue() / mul)));
        }
    }
}
