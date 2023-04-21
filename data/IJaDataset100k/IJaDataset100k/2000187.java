package cn.edu.thss.iise.beehivez.client.ui.miningevaluate;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import org.processmining.analysis.benchmark.metric.BehavioralAppropriatenessMetric;
import org.processmining.analysis.benchmark.metric.TokenFitnessMetric;
import org.processmining.analysis.conformance.AnalysisMethodExecutionThread;
import org.processmining.analysis.conformance.ConformanceAnalysisPlugin;
import org.processmining.analysis.conformance.ConformanceAnalysisSettings;
import org.processmining.analysis.conformance.ConformanceLogReplayResult;
import org.processmining.analysis.conformance.ConformanceMeasurer;
import org.processmining.analysis.conformance.DisplayState;
import org.processmining.analysis.conformance.FitnessAnalysisGUI;
import org.processmining.analysis.conformance.MaximumSearchDepthDiagnosis;
import org.processmining.analysis.conformance.StateSpaceExplorationMethod;
import org.processmining.analysis.conformance.StateSpaceExplorationResult;
import org.processmining.analysis.conformance.StructuralAnalysisMethod;
import org.processmining.analysis.conformance.StructuralAnalysisResult;
import org.processmining.exporting.petrinet.PnmlExport;
import org.processmining.framework.log.LogFile;
import org.processmining.framework.log.LogReader;
import org.processmining.framework.log.LogReaderFactory;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.models.petrinet.algorithms.PnmlWriter;
import org.processmining.framework.models.petrinet.algorithms.logReplay.AnalysisConfiguration;
import org.processmining.framework.models.petrinet.algorithms.logReplay.AnalysisMethodEnum;
import org.processmining.framework.models.petrinet.algorithms.logReplay.LogReplayAnalysisMethod;
import org.processmining.framework.models.petrinet.algorithms.logReplay.LogReplayAnalysisResult;
import org.processmining.framework.plugin.ProvidedObject;
import org.processmining.framework.ui.Message;
import org.processmining.framework.ui.MessagePanel;
import org.processmining.framework.ui.OpenLogSettings;
import org.processmining.framework.ui.Progress;
import org.processmining.framework.ui.slicker.logdialog.SlickerOpenLogSettings;
import org.processmining.importing.pnml.PnmlImport;
import org.processmining.mining.petrinetmining.PetriNetResult;
import cn.edu.thss.iise.beehivez.server.metric.tar.loggenerator.ExtensiveTarLPM;
import cn.edu.thss.iise.beehivez.util.loggenerator.AverageWeightLPM;
import cn.edu.thss.iise.beehivez.util.loggenerator.LogManager;
import cn.edu.thss.iise.beehivez.util.loggenerator.LogProduceMethod;

public class MiningEvaluateUI extends JSplitPane {

    private static final long serialVersionUID = 1L;

    private Float value1 = 0.0f;

    private Float value2 = 0.0f;

    private Float value3 = 0.0f;

    private Float value4 = 0.0f;

    private Float averageSim = 0.0f;

    private double lastCompleteness = -1;

    private int lastMultiple = -1;

    private String lastLogAlg = null;

    private JSlider jSlider = null;

    private JLabel jComLabel = null;

    private JSplitPane toppanel = null;

    private JPanel topleftpanel = null;

    private JTable jTable = null;

    private MyTableModel myTableModel = new MyTableModel();

    private JPanel toprightpanel = null;

    private JSplitPane bottompanel = null;

    private JPanel bottomleftpanel = null;

    private MyCurve tpane = null;

    private MyCurve simcurve = null;

    private MyCurve fabscurve = null;

    private JTabbedPane bottomrightpanel = null;

    private JLabel averageSimilarity = null;

    private JLabel meanDeviation = null;

    private JTextField averageSimilarityField = null;

    private JTextField meanDeviationField = null;

    private JLabel averageStrSimilarity = null;

    private JLabel meanStrDeviation = null;

    private JLabel averageBehStrSim = null;

    private JTextField averageBehStrSimField = null;

    private JTextField averageStrSimilarityField = null;

    private JTextField meanStrDeviationField = null;

    private JLabel jChooseLogLabel = null;

    private JLabel jChooseMiningAlgorithmLabel = null;

    private JLabel jChooseSimilarityAlgorithmLabel = null;

    private JLabel jChooseStrSimilarityAlgorithmLabel = null;

    private JComboBox miningAlgorithms = null;

    private JComboBox similarityAlgorithms = null;

    private JComboBox similarityStrAlgorithms = null;

    private JComboBox logAlgorithms = null;

    private JButton chooseModelSetButton = null;

    private JButton submitButton = null;

    private String modelpath = null;

    public Vector<String> logAlgorithmList = null;

    public Vector<String> miningAlgorithmList = null;

    public Vector<String> similarityAlgorithmList = null;

    public Vector<String> similarityStrAlgorithmList = null;

    public int selectedLogAlgorithm = 0;

    public int selectedMiningAlgorithm = 0;

    public int selectedSimilarityAlgorithm = 0;

    public int selectedStrSimilarityAlgorithm = 0;

    public Hashtable<String, PetriNet> map = new Hashtable<String, PetriNet>();

    public HashMap<String, String> logminemap = null;

    public HashMap<String, String> modellogmap = null;

    public HashMap<String, String> modelminemap = null;

    public HashMap<Float, String> simmodelmap = null;

    public HashMap<Float, LinkedList<String>> map1 = null;

    public HashMap<String, Integer> filenummap = null;

    private JScrollPane jScrollPane1 = null;

    private JProgressBar bar = null;

    private ArrayList<Float> similarityvaluevector = null;

    private ArrayList<Float> similaritystrvaluevector = null;

    private ArrayList<Float> totalSimVector = null;

    private ArrayList<String> model = null;

    private ArrayList<Float> f = null;

    private ArrayList<Float> aB = null;

    private ArrayList<Float> aS = null;

    private Object[][] data;

    private JLabel JTestLabel = null;

    private JTextField JSliderField = null;

    private JTextField multipleField = null;

    /**
	 * This is the default constructor
	 */
    public MiningEvaluateUI() {
        super();
        initialize();
        System.out.println("Initialized successfully!");
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        loadAlgorithmList();
        this.setSize(800, 500);
        this.setTopComponent(getToppanel());
        this.setBottomComponent(getBottompanel());
        this.setDividerLocation(300);
        this.setOrientation(JSplitPane.VERTICAL_SPLIT);
    }

    public void loadAlgorithmList() {
        logAlgorithmList = new Vector<String>();
        miningAlgorithmList = new Vector<String>();
        similarityAlgorithmList = new Vector<String>();
        similarityStrAlgorithmList = new Vector<String>();
        logAlgorithmList.add("cn.edu.thss.iise.beehivez.util.loggenerator.AverageWeightLPM");
        logAlgorithmList.add("cn.edu.thss.iise.beehivez.server.basicprocess.occurrencenet.ExtensiveTarLPM");
        miningAlgorithmList.add("cn.edu.thss.iise.beehivez.server.mining.AlphaMiner");
        miningAlgorithmList.add("cn.edu.thss.iise.beehivez.server.mining.AlphaPlusPlusMiner");
        miningAlgorithmList.add("cn.edu.thss.iise.beehivez.server.mining.AlphaSharpMiner");
        miningAlgorithmList.add("cn.edu.thss.iise.beehivez.server.mining.GeneticMiner");
        miningAlgorithmList.add("cn.edu.thss.iise.beehivez.server.mining.DupTGeneticMiner");
        miningAlgorithmList.add("cn.edu.thss.iise.beehivez.server.mining.HeuristicMiner");
        miningAlgorithmList.add("cn.edu.thss.iise.beehivez.server.mining.Region_Miner");
        similarityAlgorithmList.add("cn.edu.thss.iise.beehivez.server.metric.JaccardTARSimilarity");
        similarityAlgorithmList.add("cn.edu.thss.iise.beehivez.server.metric.ExtensiveTARSimilarity");
        similarityAlgorithmList.add("cn.edu.thss.iise.beehivez.server.metric.BPSSimilarity");
        similarityAlgorithmList.add("cn.edu.thss.iise.beehivez.server.metric.CausalFootprintSimilarity");
        similarityStrAlgorithmList.add("cn.edu.thss.iise.beehivez.server.metric.ContextBasedSimilarity");
        similarityStrAlgorithmList.add("cn.edu.thss.iise.beehivez.server.metric.JaccardStructureSimilarity");
        similarityStrAlgorithmList.add("cn.edu.thss.iise.beehivez.server.metric.CausalFootprintSimilarity");
        similarityStrAlgorithmList.add("cn.edu.thss.iise.beehivez.server.metric.DependencyGraphSimilarity");
    }

    private JSplitPane getToppanel() {
        if (toppanel == null) {
            toppanel = new JSplitPane();
            toppanel.setDividerLocation(300);
            toppanel.setLeftComponent(getLeftToppanel());
            toppanel.setRightComponent(getRightToppanel());
            toppanel.setDividerSize(10);
        }
        return toppanel;
    }

    private JSplitPane getBottompanel() {
        if (bottompanel == null) {
            bottompanel = new JSplitPane();
            bottompanel.setDividerLocation(300);
            bottompanel.setLeftComponent(getLeftBottompanel());
            bottompanel.setRightComponent(getRightBottompanel());
            bottompanel.setDividerSize(10);
        }
        return bottompanel;
    }

    private JPanel getLeftBottompanel() {
        if (bottomleftpanel == null) {
            averageSimilarity = new JLabel();
            averageSimilarity.setBounds(new Rectangle(10, 5, 170, 30));
            averageSimilarity.setFont(new Font("Dialog", Font.BOLD, 14));
            averageSimilarity.setText("Average BehSimilarity :");
            averageSimilarityField = new JTextField();
            averageSimilarityField.setBounds(new Rectangle(190, 5, 70, 30));
            meanDeviation = new JLabel();
            meanDeviation.setBounds(new Rectangle(10, 40, 170, 30));
            meanDeviation.setFont(new Font("Dialog", Font.BOLD, 14));
            meanDeviation.setText("Mean BehDeviation :");
            meanDeviationField = new JTextField();
            meanDeviationField.setBounds(new Rectangle(190, 40, 70, 30));
            averageStrSimilarity = new JLabel();
            averageStrSimilarity.setBounds(new Rectangle(10, 75, 170, 30));
            averageStrSimilarity.setFont(new Font("Dialog", Font.BOLD, 14));
            averageStrSimilarity.setText("Average StrSimilarity :");
            averageStrSimilarityField = new JTextField();
            averageStrSimilarityField.setBounds(new Rectangle(190, 75, 70, 30));
            meanStrDeviation = new JLabel();
            meanStrDeviation.setBounds(new Rectangle(10, 110, 170, 30));
            meanStrDeviation.setFont(new Font("Dialog", Font.BOLD, 14));
            meanStrDeviation.setText("Mean StrDeviation :");
            meanStrDeviationField = new JTextField();
            meanStrDeviationField.setBounds(new Rectangle(190, 110, 70, 30));
            averageBehStrSim = new JLabel();
            averageBehStrSim.setBounds(new Rectangle(10, 145, 170, 30));
            averageBehStrSim.setFont(new Font("Dialog", Font.BOLD, 14));
            averageBehStrSim.setText("Average BehStrSim :");
            averageBehStrSimField = new JTextField();
            averageBehStrSimField.setBounds(new Rectangle(190, 145, 70, 30));
            bottomleftpanel = new JPanel();
            bottomleftpanel.setLayout(null);
            bottomleftpanel.add(averageSimilarity, null);
            bottomleftpanel.add(averageSimilarityField, null);
            bottomleftpanel.add(meanDeviation, null);
            bottomleftpanel.add(meanDeviationField, null);
            bottomleftpanel.add(averageStrSimilarity, null);
            bottomleftpanel.add(averageStrSimilarityField, null);
            bottomleftpanel.add(meanStrDeviation, null);
            bottomleftpanel.add(meanStrDeviationField, null);
            bottomleftpanel.add(averageBehStrSim, null);
            bottomleftpanel.add(averageBehStrSimField, null);
        }
        return bottomleftpanel;
    }

    public JTabbedPane getRightBottompanel() {
        if (bottomrightpanel == null) {
            tpane = new MyCurve();
            simcurve = new MyCurve();
            fabscurve = new MyCurve();
            bottomrightpanel = new JTabbedPane();
            bottomrightpanel.addTab("Similarity", simcurve);
            bottomrightpanel.addTab("  f/aB/aS ", fabscurve);
            bottomrightpanel.addTab("  Summary ", tpane);
        }
        return bottomrightpanel;
    }

    private JPanel getLeftToppanel() {
        if (topleftpanel == null) {
            jScrollPane1 = new JScrollPane();
            jScrollPane1.setPreferredSize(new Dimension(270, 250));
            JTestLabel = new JLabel();
            JTestLabel.setBounds(new Rectangle(10, 10, 100, 30));
            JTestLabel.setFont(new Font("Dialog", Font.BOLD, 14));
            JTestLabel.setText("Process Model                 ");
            topleftpanel = new JPanel();
            topleftpanel.add(JTestLabel, null);
            topleftpanel.add(getChooseModelSetButton(), null);
            String[] columnNames = { "Select", "Folder Name", "File Number" };
            Object nullData[][] = new Object[0][3];
            myTableModel.setColumnNames(columnNames);
            myTableModel.setDatas(nullData);
            jTable = new JTable(myTableModel);
            jTable.getColumnModel().getColumn(0).setPreferredWidth(60);
            jTable.getColumnModel().getColumn(1).setPreferredWidth(90);
            jTable.getColumnModel().getColumn(2).setPreferredWidth(90);
            jTable.getTableHeader().setReorderingAllowed(false);
            jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jScrollPane1.setViewportView(jTable);
            topleftpanel.add(jScrollPane1, null);
        }
        return topleftpanel;
    }

    /**
	 * This method initializes chooseModelSetButton
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getChooseModelSetButton() {
        if (chooseModelSetButton == null) {
            chooseModelSetButton = new JButton("Directory");
            chooseModelSetButton.setBounds(new Rectangle(100, 10, 60, 30));
            chooseModelSetButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser("c://");
                    fileChooser.setDialogTitle("Open Model File");
                    fileChooser.setFileSelectionMode(fileChooser.DIRECTORIES_ONLY);
                    fileChooser.rescanCurrentDirectory();
                    int choose = fileChooser.showOpenDialog(null);
                    if (choose == JFileChooser.APPROVE_OPTION) {
                        modelpath = fileChooser.getSelectedFile().getAbsolutePath().trim();
                    } else return;
                    filenummap = new HashMap<String, Integer>();
                    File modelsFile = new File(modelpath);
                    File[] files = modelsFile.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        File[] temp = files[i].listFiles();
                        int num = temp.length;
                        filenummap.put(files[i].getName(), num);
                    }
                    data = new Object[files.length][3];
                    Set<String> en = filenummap.keySet();
                    Iterator<String> it = en.iterator();
                    int i = 0;
                    while (it.hasNext()) {
                        String filefoldername = it.next();
                        int filenumber = filenummap.get(filefoldername);
                        data[i][0] = new Boolean(false);
                        data[i][1] = filefoldername;
                        data[i][2] = new Integer(filenumber);
                        i++;
                    }
                    myTableModel.setDatas(data);
                    myTableModel.fireTableDataChanged();
                }
            });
        }
        return chooseModelSetButton;
    }

    /**
	 * This method initializes toppanel
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getRightToppanel() {
        if (toprightpanel == null) {
            jComLabel = new JLabel();
            jComLabel.setBounds(new Rectangle(18, 18, 150, 30));
            jComLabel.setFont(new Font("Dialog", Font.BOLD, 14));
            jComLabel.setText("Log Completeness :");
            jChooseLogLabel = new JLabel();
            jChooseLogLabel.setBounds(new Rectangle(18, 72, 200, 30));
            jChooseLogLabel.setFont(new Font("Dialog", Font.BOLD, 14));
            jChooseLogLabel.setText("Log Generator Algorithm :");
            jChooseMiningAlgorithmLabel = new JLabel();
            jChooseMiningAlgorithmLabel.setBounds(new Rectangle(18, 122, 200, 30));
            jChooseMiningAlgorithmLabel.setFont(new Font("Dialog", Font.BOLD, 14));
            jChooseMiningAlgorithmLabel.setText("Process Mining Algorithm :");
            jChooseSimilarityAlgorithmLabel = new JLabel();
            jChooseSimilarityAlgorithmLabel.setBounds(new Rectangle(18, 173, 210, 30));
            jChooseSimilarityAlgorithmLabel.setFont(new Font("Dialog", Font.BOLD, 14));
            jChooseSimilarityAlgorithmLabel.setText("BehSimilarity Algorithm :");
            jChooseStrSimilarityAlgorithmLabel = new JLabel();
            jChooseStrSimilarityAlgorithmLabel.setBounds(new Rectangle(18, 220, 210, 30));
            jChooseStrSimilarityAlgorithmLabel.setFont(new Font("Dialog", Font.BOLD, 14));
            jChooseStrSimilarityAlgorithmLabel.setText("StrSimilarity Algorithm :");
            jSlider = new JSlider(0, 100, 0);
            jSlider.setBounds(new Rectangle(239, 12, 228, 45));
            jSlider.setFont(new Font("Dialog", Font.PLAIN, 10));
            jSlider.setPaintTicks(true);
            jSlider.setMajorTickSpacing(10);
            jSlider.setMinorTickSpacing(1);
            jSlider.setPaintLabels(true);
            jSlider.setPaintTrack(true);
            jSlider.setSnapToTicks(true);
            jSlider.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    Integer hValue = ((JSlider) e.getSource()).getValue();
                    JSliderField.setText(hValue.toString() + "%");
                }
            });
            JSliderField = new JTextField();
            JSliderField.setBounds(new Rectangle(165, 18, 50, 30));
            multipleField = new JTextField();
            multipleField.setBounds(new Rectangle(165, 50, 50, 25));
            bar = new JProgressBar(0, 100);
            bar.setBounds(new Rectangle(20, 267, 265, 10));
            toprightpanel = new JPanel();
            toprightpanel.setLayout(null);
            toprightpanel.add(jChooseLogLabel, null);
            toprightpanel.add(jChooseMiningAlgorithmLabel, null);
            toprightpanel.add(jChooseSimilarityAlgorithmLabel, null);
            toprightpanel.add(jChooseStrSimilarityAlgorithmLabel, null);
            toprightpanel.add(getLogAlgorithms(), null);
            toprightpanel.add(getMiningAlgorithms(), null);
            toprightpanel.add(getSimilarityAlgorithms(), null);
            toprightpanel.add(getStrSimilarityAlgorithms(), null);
            toprightpanel.add(getSubmitButton(), null);
            toprightpanel.add(JSliderField);
            toprightpanel.add(multipleField);
            toprightpanel.add(bar);
            toprightpanel.add(jComLabel);
            toprightpanel.add(jSlider);
        }
        return toprightpanel;
    }

    private JComboBox getLogAlgorithms() {
        if (logAlgorithms == null) {
            String logName[] = { "AverageWeightLPM", "ExtensiveTarLPM" };
            logAlgorithms = new JComboBox(logName);
            logAlgorithms.setBounds(new Rectangle(246, 73, 220, 30));
            logAlgorithms.setEditable(false);
            logAlgorithms.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    selectedLogAlgorithm = ((JComboBox) (e.getSource())).getSelectedIndex();
                }
            });
        }
        return logAlgorithms;
    }

    /**
	 * This method initializes miningAlgorithms
	 * 
	 * @return javax.swing.JComboBox
	 */
    private JComboBox getMiningAlgorithms() {
        if (miningAlgorithms == null) {
            miningAlgorithms = new JComboBox();
            miningAlgorithms.setBounds(new Rectangle(246, 122, 220, 30));
            for (int i = 0; i < miningAlgorithmList.size(); i++) {
                try {
                    Class algorithmClass;
                    algorithmClass = Class.forName(miningAlgorithmList.get(i));
                    Object algorithmObj = null;
                    algorithmObj = algorithmClass.newInstance();
                    Method method = algorithmClass.getMethod("getName", null);
                    Object result = method.invoke(algorithmObj, null);
                    miningAlgorithms.addItem((String) result);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            miningAlgorithms.setEditable(false);
            miningAlgorithms.setSelectedItem(selectedMiningAlgorithm);
            miningAlgorithms.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    selectedMiningAlgorithm = ((JComboBox) (e.getSource())).getSelectedIndex();
                }
            });
        }
        return miningAlgorithms;
    }

    /**
	 * This method initializes similarityAlgorithms
	 * 
	 * @return javax.swing.JComboBox
	 */
    private JComboBox getSimilarityAlgorithms() {
        if (similarityAlgorithms == null) {
            similarityAlgorithms = new JComboBox();
            similarityAlgorithms.setBounds(new Rectangle(246, 172, 220, 30));
            for (int i = 0; i < similarityAlgorithmList.size(); i++) {
                try {
                    Class algorithmClass;
                    algorithmClass = Class.forName(similarityAlgorithmList.get(i));
                    Object algorithmObj = null;
                    algorithmObj = algorithmClass.newInstance();
                    Method method = algorithmClass.getMethod("getName", null);
                    Object result = method.invoke(algorithmObj, null);
                    similarityAlgorithms.addItem((String) result);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            similarityAlgorithms.setEditable(false);
            similarityAlgorithms.setSelectedItem(selectedSimilarityAlgorithm);
            similarityAlgorithms.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    selectedSimilarityAlgorithm = ((JComboBox) (e.getSource())).getSelectedIndex();
                }
            });
        }
        return similarityAlgorithms;
    }

    private JComboBox getStrSimilarityAlgorithms() {
        if (similarityStrAlgorithms == null) {
            similarityStrAlgorithms = new JComboBox();
            similarityStrAlgorithms.setBounds(new Rectangle(246, 220, 220, 30));
            for (int i = 0; i < similarityStrAlgorithmList.size(); i++) {
                try {
                    Class algorithmClass;
                    algorithmClass = Class.forName(similarityStrAlgorithmList.get(i));
                    Object algorithmObj = null;
                    algorithmObj = algorithmClass.newInstance();
                    Method method = algorithmClass.getMethod("getName", null);
                    Object result = method.invoke(algorithmObj, null);
                    similarityStrAlgorithms.addItem((String) result);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            similarityStrAlgorithms.setEditable(false);
            similarityStrAlgorithms.setSelectedItem(selectedStrSimilarityAlgorithm);
            similarityStrAlgorithms.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    selectedStrSimilarityAlgorithm = ((JComboBox) (e.getSource())).getSelectedIndex();
                }
            });
        }
        return similarityStrAlgorithms;
    }

    public void deleteAllFiles(File folder) {
        if (!folder.isDirectory()) return;
        File[] list = folder.listFiles();
        for (int i = 0; i < list.length; i++) {
            list[i].delete();
        }
    }

    /**
	 * This method initializes submitButton
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getSubmitButton() {
        if (submitButton == null) {
            submitButton = new JButton("Start");
            submitButton.setBounds(new Rectangle(366, 260, 100, 30));
            submitButton.setFont(new Font("Dialog", Font.BOLD, 14));
            submitButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    bar.setValue(0);
                    System.out.println(modelpath);
                    if (modelpath.equals("")) {
                        JOptionPane.showMessageDialog(null, "Incomplete information, please complete the information!");
                        return;
                    }
                    Vector<File> filesvec = new Vector<File>();
                    for (int i = 0; i < data.length; i++) {
                        if ((Boolean) data[i][0]) {
                            String str = (String) data[i][1];
                            File modelsFile = new File(modelpath + "/" + str);
                            File[] file1 = modelsFile.listFiles();
                            for (int j = 0; j < file1.length; j++) {
                                filesvec.add(file1[j]);
                            }
                        }
                    }
                    logminemap = new HashMap<String, String>();
                    modellogmap = new HashMap<String, String>();
                    modelminemap = new HashMap<String, String>();
                    map1 = new HashMap<Float, LinkedList<String>>();
                    FileInputStream in = null;
                    PnmlImport input = new PnmlImport();
                    map.clear();
                    String foldername = System.getProperty("user.dir", "") + "\\log";
                    File logfolder = new File(foldername);
                    if (!logfolder.exists()) {
                        logfolder.mkdirs();
                    } else if (logfolder.isFile()) {
                        logfolder.delete();
                        logfolder.mkdirs();
                    }
                    LogProduceMethod lpm;
                    String logType = "";
                    if (selectedLogAlgorithm == 0) {
                        lpm = new AverageWeightLPM();
                        logType = lpm.getLogType();
                        String cuLogAlg = "tar";
                        double completeness = jSlider.getValue() / 100.0;
                        System.out.println("completeness :" + completeness);
                        int multiple = Integer.parseInt(multipleField.getText());
                        System.out.println("multiple :" + multiple);
                        for (int i = 0; i < filesvec.size(); i++) {
                            File model = filesvec.get(i);
                            if (model.getAbsolutePath().endsWith(".pnml") || model.getAbsolutePath().endsWith(".xml")) {
                                try {
                                    in = new FileInputStream(model.getAbsolutePath());
                                    PetriNet pn = input.read(in);
                                    in.close();
                                    int index = model.getName().lastIndexOf(".");
                                    String logPath = logfolder.getPath() + "\\" + model.getName().substring(0, index) + ".mxml";
                                    map.put(logPath, pn);
                                    modellogmap.put(logPath, model.getAbsolutePath());
                                    String temp = logPath.replaceAll("log", "miningmodel");
                                    String minepath = temp.replaceAll("mxml", "pnml");
                                    modelminemap.put(model.getAbsolutePath(), minepath);
                                    logminemap.put(logPath, minepath);
                                    File logfile = new File(logPath);
                                    if (!logfile.exists() || completeness != lastCompleteness || !cuLogAlg.equals(lastLogAlg) || multiple != lastMultiple) {
                                        logfile.createNewFile();
                                        LogManager.generateLog(logPath, 100, lpm, pn, completeness, multiple);
                                        System.out.println("log generate");
                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                            bar.setValue((50 / filesvec.size()) * (i + 1));
                        }
                        lastLogAlg = cuLogAlg;
                        lastCompleteness = completeness;
                        lastMultiple = multiple;
                    }
                    if (selectedLogAlgorithm == 1) {
                        lpm = new ExtensiveTarLPM();
                        logType = lpm.getLogType();
                        String cuLogAlg = "etar";
                        for (int i = 0; i < filesvec.size(); i++) {
                            File model = filesvec.get(i);
                            if (model.getAbsolutePath().endsWith(".pnml") || model.getAbsolutePath().endsWith(".xml")) {
                                try {
                                    in = new FileInputStream(model.getAbsolutePath());
                                    PetriNet pn = input.read(in);
                                    in.close();
                                    int index = model.getName().lastIndexOf(".");
                                    String logPath = logfolder.getPath() + "\\" + model.getName().substring(0, index) + ".mxml";
                                    map.put(logPath, pn);
                                    modellogmap.put(logPath, model.getAbsolutePath());
                                    String temp = logPath.replaceAll("log", "miningmodel");
                                    String minepath = temp.replaceAll("mxml", "pnml");
                                    modelminemap.put(model.getAbsolutePath(), minepath);
                                    logminemap.put(logPath, minepath);
                                    File logfile = new File(logPath);
                                    if (!logfile.exists() || !cuLogAlg.equals(lastLogAlg)) {
                                        logfile.createNewFile();
                                        LogManager.generateLog(logPath, lpm, pn);
                                        System.out.println("log generate");
                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                            bar.setValue((50 / filesvec.size()) * (i + 1));
                        }
                        lastLogAlg = cuLogAlg;
                    }
                    System.out.println("logminemap :" + logminemap);
                    System.out.println("map :" + map);
                    new MiningThread().start();
                }
            });
        }
        return submitButton;
    }

    public class MiningThread extends Thread {

        public void run() {
            if (map == null || map.size() <= 0) {
                return;
            }
            bar.setValue(50);
            value1 = 0.0f;
            value2 = 0.0f;
            value3 = 0.0f;
            value4 = 0.0f;
            averageSim = 0.0f;
            similarityvaluevector = new ArrayList<Float>();
            similaritystrvaluevector = new ArrayList<Float>();
            model = new ArrayList<String>();
            f = new ArrayList<Float>();
            aB = new ArrayList<Float>();
            aS = new ArrayList<Float>();
            LogReader logReader = null;
            String miningAlgorithmName;
            Class miningAlgorithmClass;
            String similarityAlgorithmName;
            Class similarityAlgorithmClass;
            String similarityStrAlgorithmName;
            Class similarityStrAlgorithmClass;
            String miningmodelfolder = System.getProperty("user.dir", "") + "/miningmodel";
            File modelFolder = new File(miningmodelfolder);
            if (!modelFolder.exists()) modelFolder.mkdir();
            String resultFile = System.getProperty("user.dir", "") + "/result.xls";
            File resultfile = new File(resultFile);
            if (!resultfile.exists()) try {
                resultfile.createNewFile();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            FileWriter fw = null;
            BufferedWriter bw = null;
            try {
                fw = new FileWriter(resultFile);
                bw = new BufferedWriter(fw);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bw.write("FileName", 0, 8);
                bw.write("\t");
                bw.write("BehSimilarity", 0, 13);
                bw.write("\t");
                bw.write("StrSimilarity", 0, 13);
                bw.write("\t");
                bw.write("Time", 0, 4);
                bw.write("\t");
                bw.write("aS", 0, 2);
                bw.write("\t");
                bw.write("f", 0, 1);
                bw.write("\t");
                bw.write("aB", 0, 2);
                bw.newLine();
                bw.flush();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            try {
                Enumeration<String> en = map.keys();
                while (en.hasMoreElements()) {
                    String logpath = en.nextElement();
                    String modelpath = modellogmap.get(logpath);
                    String minepath = logminemap.get(logpath);
                    FileInputStream in1 = null;
                    PnmlImport input = null;
                    PetriNet pn = null;
                    LogFile logFile = LogFile.getInstance(logpath);
                    logReader = LogReaderFactory.createInstance(null, logFile);
                    miningAlgorithmName = miningAlgorithmList.get(selectedMiningAlgorithm);
                    miningAlgorithmClass = Class.forName(miningAlgorithmName);
                    Object miningAlgorithmObject = miningAlgorithmClass.newInstance();
                    Class ptype[] = new Class[1];
                    ptype[0] = Class.forName("org.processmining.framework.log.LogReader");
                    Method miningMethod = miningAlgorithmClass.getMethod("mine", ptype);
                    Object m_args[] = new Object[1];
                    m_args[0] = logReader;
                    Long startTime = System.nanoTime();
                    PetriNet miningModel = (PetriNet) miningMethod.invoke(miningAlgorithmObject, m_args);
                    Long estimatedTime = System.nanoTime() - startTime;
                    String outputFile = logpath;
                    outputFile = outputFile.replaceFirst("log", "miningmodel");
                    outputFile = outputFile.replaceFirst("mxml", "pnml");
                    if (miningModel != null) {
                        PnmlExport exportPlugin = new PnmlExport();
                        Object[] objects = new Object[] { miningModel };
                        ProvidedObject object = new ProvidedObject("temp", objects);
                        File file = new File(outputFile);
                        if (file.exists()) {
                            file.delete();
                        }
                        file.createNewFile();
                        FileOutputStream outputStream = new FileOutputStream(outputFile);
                        exportPlugin.export(object, outputStream);
                        outputStream.close();
                    } else {
                        System.err.println("No Petri net could be constructed.");
                    }
                    bar.setValue(75);
                    similarityAlgorithmName = similarityAlgorithmList.get(selectedSimilarityAlgorithm);
                    similarityAlgorithmClass = Class.forName(similarityAlgorithmName);
                    Object similarityAlgorithmObject = similarityAlgorithmClass.newInstance();
                    Class stype[] = new Class[2];
                    stype[0] = Class.forName("org.processmining.framework.models.petrinet.PetriNet");
                    stype[1] = Class.forName("org.processmining.framework.models.petrinet.PetriNet");
                    Method similarityMethod = similarityAlgorithmClass.getMethod("similarity", stype);
                    Object s_args[] = new Object[2];
                    s_args[0] = map.get(logpath);
                    s_args[1] = miningModel;
                    Object result = similarityMethod.invoke(similarityAlgorithmObject, s_args);
                    similarityStrAlgorithmName = similarityStrAlgorithmList.get(selectedStrSimilarityAlgorithm);
                    similarityStrAlgorithmClass = Class.forName(similarityStrAlgorithmName);
                    Object similarityStrAlgorithmObject = similarityStrAlgorithmClass.newInstance();
                    Class stype1[] = new Class[2];
                    stype1[0] = Class.forName("org.processmining.framework.models.petrinet.PetriNet");
                    stype1[1] = Class.forName("org.processmining.framework.models.petrinet.PetriNet");
                    Method similarityStrMethod = similarityStrAlgorithmClass.getMethod("similarity", stype1);
                    Object s_args1[] = new Object[2];
                    s_args1[0] = map.get(logpath);
                    s_args1[1] = miningModel;
                    Object result1 = similarityStrMethod.invoke(similarityStrAlgorithmObject, s_args1);
                    miningModel.clearGraph();
                    miningModel.delete();
                    (map.get(logpath)).clearGraph();
                    (map.get(logpath)).delete();
                    System.out.println(logpath + " the BehSimilarity is " + Float.valueOf(result.toString()));
                    System.out.println(logpath + " the StrSimilarity is " + Float.valueOf(result1.toString()));
                    String rs = result.toString();
                    String rs1 = result1.toString();
                    Float ress = 0f;
                    Float ress1 = 0f;
                    if (!rs.equals("NaN")) {
                        Float res = Float.parseFloat(rs);
                        ress = (float) (((int) (res * 100)) / 100.0);
                        similarityvaluevector.add(ress);
                        model.add("");
                    }
                    if (!rs1.equals("NaN")) {
                        Float res1 = Float.parseFloat(rs1);
                        ress1 = (float) (((int) (res1 * 100)) / 100.0);
                        similaritystrvaluevector.add(ress1);
                    }
                    int indexx = modelpath.lastIndexOf("\\");
                    String name = modelpath.substring(indexx + 1, modelpath.length());
                    bw.write(name, 0, name.length());
                    bw.write("\t");
                    bw.write(ress.toString(), 0, ress.toString().length());
                    bw.write("\t");
                    bw.write(ress1.toString(), 0, ress1.toString().length());
                    bw.write("\t");
                    bw.write(estimatedTime.toString(), 0, estimatedTime.toString().length());
                    bw.write("\t");
                    try {
                        in1 = new FileInputStream(minepath);
                        input = new PnmlImport();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        pn = input.read(in1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    StructuralAnalysisResult sar = new StructuralAnalysisResult(new AnalysisConfiguration(), pn, new StructuralAnalysisMethod(pn));
                    float aSr = sar.getStructuralAppropriatenessMeasure();
                    Float aSre = (float) (((int) (aSr * 100)) / 100.0);
                    aS.add(aSre);
                    try {
                        bw.write(aSre.toString(), 0, aSre.toString().length());
                        bw.write("\t");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    AnalysisConfiguration myOptions = createAnalysisConfiguration();
                    LogReplayAnalysisMethod logReplayAnalysis = new LogReplayAnalysisMethod(pn, logReader, new ConformanceMeasurer(), new Progress(0, 100));
                    int maxSearchDepth = MaximumSearchDepthDiagnosis.determineMaximumSearchDepth(pn);
                    logReplayAnalysis.setMaxDepth(maxSearchDepth);
                    ConformanceLogReplayResult clrr = (ConformanceLogReplayResult) logReplayAnalysis.analyse(myOptions);
                    float fr = clrr.getFitnessMeasure();
                    Float fre = (float) (((int) (fr * 100)) / 100.0);
                    f.add(fre);
                    try {
                        bw.write(fre.toString(), 0, fre.toString().length());
                        bw.write("\t");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    float ab = 0;
                    try {
                        ab = clrr.getBehavioralAppropriatenessMeasure();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    Float abb = (float) (((int) (ab * 100)) / 100.0);
                    aB.add(abb);
                    try {
                        bw.write(abb.toString(), 0, abb.toString().length());
                        bw.newLine();
                        bw.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("similaritystrvaluevector :" + similaritystrvaluevector);
            System.out.println("similarityvaluevector :" + similarityvaluevector);
            System.out.println("f :" + f);
            System.out.println("aB :" + aB);
            System.out.println("aS :" + aS);
            float s1 = 0;
            float s2 = 0;
            for (int i = 0; i < similarityvaluevector.size(); i++) {
                s1 += similarityvaluevector.get(i);
                s2 += similarityvaluevector.get(i) * similarityvaluevector.get(i);
            }
            float value11 = s1 / similarityvaluevector.size();
            float ex2 = s2 / similarityvaluevector.size();
            float dx = ex2 - value11 * value11;
            float value22 = (float) Math.sqrt(dx);
            value1 = (float) (((int) (value11 * 100)) / 100.0);
            value2 = (float) (((int) (value22 * 100)) / 100.0);
            averageSimilarityField.setText(value1.toString());
            meanDeviationField.setText(value2.toString());
            float s3 = 0;
            float s4 = 0;
            for (int i = 0; i < similaritystrvaluevector.size(); i++) {
                s3 += similaritystrvaluevector.get(i);
                s4 += similaritystrvaluevector.get(i) * similaritystrvaluevector.get(i);
            }
            float value33 = s3 / similaritystrvaluevector.size();
            float ex3 = s2 / similaritystrvaluevector.size();
            float dx1 = ex3 - value33 * value33;
            float value44 = (float) Math.sqrt(dx);
            value3 = (float) (((int) (value33 * 100)) / 100.0);
            value4 = (float) (((int) (value44 * 100)) / 100.0);
            averageStrSimilarityField.setText(value3.toString());
            meanStrDeviationField.setText(value4.toString());
            averageSim = (value1 + value3) / 2;
            averageSim = (float) (((int) (averageSim * 1000)) / 1000.0);
            averageBehStrSimField.setText(averageSim.toString());
            tpane.setHistogramTitle("", "Model");
            tpane.setSim(similarityvaluevector);
            tpane.setStrsim(similaritystrvaluevector);
            tpane.setaB(aB);
            tpane.setaS(aS);
            tpane.setF(f);
            tpane.repaint();
            simcurve.setHistogramTitle("Similarity", "Model");
            simcurve.setSim(similarityvaluevector);
            simcurve.setStrsim(similaritystrvaluevector);
            simcurve.repaint();
            fabscurve.setHistogramTitle("", "Model");
            fabscurve.setaB(aB);
            fabscurve.setaS(aS);
            fabscurve.setF(f);
            fabscurve.repaint();
            map.clear();
            bar.setValue(100);
        }
    }

    private AnalysisConfiguration createAnalysisConfiguration() {
        AnalysisConfiguration f_option = new AnalysisConfiguration();
        f_option.setName("f");
        f_option.setToolTip("Degree of fit based on missing and remaining tokens in the model during log replay");
        f_option.setDescription("The token-based <b>fitness</b> metric <i>f</i> relates the amount of missing tokens during log replay with the amount of consumed ones and " + "the amount of remaining tokens with the produced ones. If the log could be replayed correctly, that is, there were no tokens missing nor remaining, it evaluates to 1.");
        f_option.setNewAnalysisMethod(AnalysisMethodEnum.LOG_REPLAY);
        AnalysisConfiguration fitnessOptions = new AnalysisConfiguration();
        fitnessOptions.setName("Fitness");
        fitnessOptions.setToolTip("Fitness Analysis");
        fitnessOptions.setDescription("Fitness evaluates whether the observed process <i>complies with</i> the control flow specified by the process. " + "One way to investigate the fitness is to replay the log in the Petri net. The log replay is carried out in a non-blocking way, i.e., if there are tokens missing " + "to fire the transition in question they are created artificially and replay proceeds. While doing so, diagnostic data is collected and can be accessed afterwards.");
        fitnessOptions.addChildConfiguration(f_option);
        AnalysisConfiguration aB_option = new AnalysisConfiguration();
        aB_option.setName("saB");
        aB_option.setToolTip("Simple behavioral appropriateness based on the mean number of enabled transitions");
        aB_option.setDescription("The <b>simple behavioral appropriateness</b> metric <i>sa<sub>B</sub></i> is based on the mean number of enabled transitions during log replay " + "(the greater the value the less behavior is allowed by the process model and the more precisely the behavior observed in the log is captured). " + "Note that this metric should only be used as a comparative means for models without alternative duplicate tasks. " + "Note further that in order to determine the mean number of enabled tasks in the presence of invisible tasks requires to build the state space " + "from the current marking after each replay step. Since this may greatly decrease the performance of the computational process, you might want to swich this feature off.");
        aB_option.setNewAnalysisMethod(AnalysisMethodEnum.LOG_REPLAY);
        AnalysisConfiguration behAppropOptions = new AnalysisConfiguration();
        behAppropOptions.setName("Precision");
        behAppropOptions.setToolTip("Behavioral Appropriateness Analysis");
        behAppropOptions.setDescription("Precision, or Behavioral Appropriateness, evaluates <i>how precisely</i> the model describes the observed process.");
        behAppropOptions.addChildConfiguration(aB_option);
        AnalysisConfiguration analysisOptions = new AnalysisConfiguration();
        analysisOptions.addChildConfiguration(fitnessOptions);
        analysisOptions.addChildConfiguration(behAppropOptions);
        return analysisOptions;
    }
}
