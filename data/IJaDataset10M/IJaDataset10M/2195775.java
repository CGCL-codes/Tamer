package ncg.statistics;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import geovista.common.data.DataSetBroadcaster;
import geovista.common.data.DataSetForApps;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.common.event.SubspaceEvent;
import geovista.common.event.SubspaceListener;
import geovista.common.ui.VariablePicker;
import geovista.readers.shapefile.ShapeFileDataReader;

/**
 * @author pfoley
 * 
 */
@SuppressWarnings("serial")
public class DiscriminantAnalysisGUI extends JPanel implements ActionListener, DataSetListener, SubspaceListener {

    private transient DataSetForApps dataSet = null;

    private transient boolean newDataSetFired = false;

    private transient JButton goButton = null;

    private transient JButton resetButton = null;

    private transient JComboBox categoryCombo = null;

    private transient VariablePicker indVarPicker = null;

    private transient JTextArea outputInfo = null;

    private transient JCheckBox doPCA = null;

    private transient JComboBox numPCAVars = null;

    private transient JCheckBox standardize = null;

    private transient JCheckBox doGWDA = null;

    private transient JCheckBox useCrossValidation = null;

    private transient JComboBox crossValidationMethod = null;

    private transient JComboBox minNumNearestNeighboursCV = null;

    private transient JComboBox maxNumNearestNeighboursCV = null;

    private transient JComboBox stepSizeNumNearestNeighboursCV = null;

    private transient JComboBox kernelFunctionType = null;

    private transient JComboBox numNearestNeighbours = null;

    private transient int[] indVarIndices = new int[0];

    private transient int categoryIndex = -1;

    private transient int numClassifications = 0;

    private transient Map<Integer, Integer> categoryIndexMap = null;

    private transient Map<Integer, Integer> indVarIndexMap = null;

    protected static final Logger logger = Logger.getLogger(DiscriminantAnalysisGUI.class.getPackage().getName());

    public DiscriminantAnalysisGUI() {
        super(new BorderLayout());
        goButton = new JButton("Classify");
        resetButton = new JButton("Reset");
        categoryCombo = new JComboBox();
        doPCA = new JCheckBox("Use Principal Components Analysis");
        numPCAVars = new JComboBox();
        standardize = new JCheckBox("Standarize Independent Variables");
        doGWDA = new JCheckBox("Use Geographical Weighting");
        useCrossValidation = new JCheckBox("Use Cross Validation");
        crossValidationMethod = new JComboBox();
        minNumNearestNeighboursCV = new JComboBox();
        maxNumNearestNeighboursCV = new JComboBox();
        stepSizeNumNearestNeighboursCV = new JComboBox();
        numNearestNeighbours = new JComboBox();
        kernelFunctionType = new JComboBox();
        indVarPicker = new VariablePicker(DataSetForApps.TYPE_DOUBLE);
        outputInfo = new JTextArea();
        outputInfo.setEditable(false);
        outputInfo.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputInfo.setLayout(new BoxLayout(outputInfo, BoxLayout.Y_AXIS));
        indVarPicker.setPreferredSize(new Dimension(180, 400));
        indVarPicker.setBorder(BorderFactory.createTitledBorder("Independent Variables"));
        doPCA.setSelected(false);
        numPCAVars.setEnabled(false);
        standardize.setSelected(false);
        doGWDA.setSelected(false);
        crossValidationMethod.addItem("Cross Validation Likelihood");
        crossValidationMethod.addItem("Cross Validation Score");
        kernelFunctionType.addItem("Bisquare Kernel");
        kernelFunctionType.addItem("Moving Window");
        useCrossValidation.setSelected(false);
        useCrossValidation.setEnabled(false);
        numNearestNeighbours.setEnabled(false);
        crossValidationMethod.setEnabled(false);
        minNumNearestNeighboursCV.setEnabled(false);
        maxNumNearestNeighboursCV.setEnabled(false);
        stepSizeNumNearestNeighboursCV.setEnabled(false);
        kernelFunctionType.setEnabled(false);
        goButton.addActionListener(this);
        resetButton.addActionListener(this);
        categoryCombo.addActionListener(this);
        indVarPicker.addSubspaceListener(this);
        doPCA.addActionListener(this);
        doGWDA.addActionListener(this);
        useCrossValidation.addActionListener(this);
        minNumNearestNeighboursCV.addActionListener(this);
        maxNumNearestNeighboursCV.addActionListener(this);
        JPanel classArea = new JPanel(new GridBagLayout());
        classArea.setBorder(BorderFactory.createTitledBorder("Classification"));
        classArea.add(new JLabel("Category : "), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        classArea.add(categoryCombo, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        classArea.add(goButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.LAST_LINE_START, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        classArea.add(resetButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.LAST_LINE_END, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        JPanel pcaArea = new JPanel(new GridBagLayout());
        pcaArea.setBorder(BorderFactory.createTitledBorder("Principal Components Analysis"));
        pcaArea.add(doPCA, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        pcaArea.add(new JLabel("Number of Principal Components"), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        pcaArea.add(numPCAVars, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        JPanel stdArea = new JPanel();
        stdArea.setBorder(BorderFactory.createTitledBorder("Standardization"));
        stdArea.add(standardize, BorderLayout.WEST);
        JPanel gwdaArea = new JPanel(new GridBagLayout());
        gwdaArea.setBorder(BorderFactory.createTitledBorder("Geographical Weighting"));
        gwdaArea.add(doGWDA, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));
        gwdaArea.add(new JLabel("Kernel Function"), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        gwdaArea.add(kernelFunctionType, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        gwdaArea.add(new JLabel("Number of Neighbours"), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        gwdaArea.add(numNearestNeighbours, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        gwdaArea.add(useCrossValidation, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));
        gwdaArea.add(new JLabel("CV Method"), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        gwdaArea.add(crossValidationMethod, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        gwdaArea.add(new JLabel("Min Number of Neighbours"), new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        gwdaArea.add(minNumNearestNeighboursCV, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        gwdaArea.add(new JLabel("Max Number of Neighbours"), new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        gwdaArea.add(maxNumNearestNeighboursCV, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        gwdaArea.add(new JLabel("Neighbour Step Size"), new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        gwdaArea.add(stepSizeNumNearestNeighboursCV, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        JPanel menuArea = new JPanel(new GridBagLayout());
        menuArea.add(classArea, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));
        menuArea.add(pcaArea, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));
        menuArea.add(stdArea, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));
        menuArea.add(gwdaArea, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.LAST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));
        JScrollPane outputInfoSPane = new JScrollPane(outputInfo);
        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Menu", menuArea);
        tabs.add("Output", outputInfoSPane);
        this.add(indVarPicker, BorderLayout.LINE_START);
        this.add(tabs, BorderLayout.CENTER);
    }

    private class ClassifierThread extends SwingWorker<DataSetForApps, Void> {

        private DiscriminantAnalysis daTask = null;

        private DataSetForApps newDataSet = null;

        public ClassifierThread() {
            super();
        }

        @Override
        public DataSetForApps doInBackground() {
            DataSetForApps newDataSetForApps = null;
            try {
                classify();
                getDiagnostics();
                newDataSetForApps = getNewDataSet();
            } catch (final DiscriminantAnalysisGUIException e) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        final String message = "unable to classify : " + e.getMessage();
                        logger.warning(message);
                        JOptionPane.showMessageDialog(DiscriminantAnalysisGUI.this, message, "WARNING", JOptionPane.WARNING_MESSAGE);
                    }
                });
            } catch (final Exception e) {
                logger.severe("unhandled exception during classification : " + e.getMessage());
            }
            return newDataSetForApps;
        }

        @Override
        public void done() {
            try {
                newDataSet = get();
                if (newDataSet != null) {
                    fireDataSetChanged(newDataSet);
                    newDataSetFired = true;
                    Runtime.getRuntime().gc();
                }
            } catch (InterruptedException e) {
                logger.severe(e.getMessage());
            } catch (ExecutionException e) {
                logger.severe(e.getMessage());
            } catch (Exception e) {
                logger.severe("unhandled exception encountered during classification : " + e.getMessage());
            }
        }

        private void classify() throws DiscriminantAnalysisGUIException {
            if (categoryIndex < 0 || indVarIndices.length == 0) {
                String message = ((categoryIndex < 0) ? "\nCategory Index is not set" : "");
                message += ((indVarIndices.length == 0) ? "\nIndependent Variables are not set" : "");
                throw new DiscriminantAnalysisGUIException(message);
            }
            double[][] data = new double[indVarIndices.length][0];
            for (int i = 0; i < indVarIndices.length; i++) {
                Object x = dataSet.getColumnValues(indVarIndices[i]);
                if (x instanceof double[]) {
                    data[i] = (double[]) x;
                } else {
                    logger.severe("type of column " + dataSet.getColumnName(indVarIndices[i]) + " is not double[] as expected");
                    throw new DiscriminantAnalysisGUIException();
                }
            }
            int[] categories = null;
            Object x = dataSet.getColumnValues(categoryIndex);
            if (x instanceof int[]) {
                categories = (int[]) x;
            } else {
                logger.severe("type of column " + dataSet.getColumnName(categoryIndex) + " is not int[] as expected");
                throw new DiscriminantAnalysisGUIException();
            }
            try {
                String categoryName = dataSet.getColumnName(categoryIndex);
                outputInfo.append("\nClassification " + Integer.toString(++numClassifications));
                outputInfo.append("\n\nClassification Category : " + categoryName);
                outputInfo.append("\nIndependent Variables (" + Integer.toString(indVarIndices.length) + ") : \n");
                for (int i = 0; i < indVarIndices.length; i++) {
                    outputInfo.append(dataSet.getColumnName(indVarIndices[i]) + "\n");
                }
                if (doPCA.isSelected() == true) {
                    int numPCs = (numPCAVars.getSelectedIndex() + 1);
                    PCA pcaTask = new PCA();
                    pcaTask.setObservations(data, false, true);
                    pcaTask.transform();
                    data = pcaTask.getPrincipalComponents(numPCs);
                    outputInfo.append("\nClassification uses the first " + numPCs + " Prinicpal Components\n");
                }
                if (doGWDA.isSelected() == true) {
                    daTask = new GWDiscriminantAnalysis();
                    outputInfo.append("\nClassification uses Geographically Weighted Discriminant Analysis\n");
                    ((GWDiscriminantAnalysis) daTask).setKernelFunctionType(kernelFunctionType.getSelectedIndex());
                    outputInfo.append("\nKernel Function type is [" + NCGStatUtils.kernelFunctionTypeToString(kernelFunctionType.getSelectedIndex()) + "]\n");
                    ((GWDiscriminantAnalysis) daTask).setUseCrossValidation(useCrossValidation.isSelected());
                    if (useCrossValidation.isSelected() == true) {
                        ((GWDiscriminantAnalysis) daTask).setCrossValidationMethod(crossValidationMethod.getSelectedIndex());
                        outputInfo.append("\nSelecting optimum number of nearest neighbours using cross validation\n");
                        outputInfo.append("Cross Validation Method is [" + NCGStatUtils.crossValidationMethodToString(crossValidationMethod.getSelectedIndex()) + "]");
                        int minNumNNCV = ((Integer) minNumNearestNeighboursCV.getSelectedItem()).intValue();
                        int maxNumNNCV = ((Integer) maxNumNearestNeighboursCV.getSelectedItem()).intValue();
                        int numNNStepSizeCV = 0;
                        if (stepSizeNumNearestNeighboursCV.getSelectedIndex() > -1) {
                            numNNStepSizeCV = ((Integer) stepSizeNumNearestNeighboursCV.getSelectedItem()).intValue();
                        }
                        ((GWDiscriminantAnalysis) daTask).setMinNumNearestNeighboursCV(minNumNNCV);
                        ((GWDiscriminantAnalysis) daTask).setMaxNumNearestNeighboursCV(maxNumNNCV);
                        ((GWDiscriminantAnalysis) daTask).setNumNearestNeighboursStepSizeCV(numNNStepSizeCV);
                    } else {
                        int numNN = (numNearestNeighbours.getSelectedIndex() + 1);
                        ((GWDiscriminantAnalysis) daTask).setNumNearestNeighbours(numNN);
                        outputInfo.append("\nSetting number of nearest neighbours to [" + numNN + "]\n");
                    }
                    Point2D[] centroids = NCGStatUtils.computeCentroids(dataSet);
                    ((GWDiscriminantAnalysis) daTask).setDistanceMatrix(centroids);
                } else {
                    daTask = new DiscriminantAnalysis();
                }
                daTask.setPredictorVariables(data, false, standardize.isSelected());
                outputInfo.append("\nIndependent variables are " + ((standardize.isSelected() == true) ? "" : "NOT") + " standardized prior to classification\n");
                daTask.setClassification(categories);
                daTask.setPriorProbabilities();
                daTask.classify();
            } catch (DiscriminantAnalysisException e) {
                outputInfo.append("ERROR: " + e.getMessage());
                throw new DiscriminantAnalysisGUIException(e.getMessage(), e.getCause());
            } catch (PCAException e) {
                outputInfo.append("ERROR:" + e.getMessage());
                throw new DiscriminantAnalysisGUIException(e.getMessage(), e.getCause());
            }
        }

        private void getDiagnostics() throws DiscriminantAnalysisGUIException {
            try {
                int[][] confMatrix = daTask.confusionMatrix();
                int[] classFreq = daTask.getClassFrequencies();
                int[] uniqueClasses = daTask.getUniqueClasses();
                double classAccuracy = daTask.getClassificationAccuracy();
                double randomClassAccuracy = daTask.getRandomClassificationAccuracy();
                int numClasses = uniqueClasses.length;
                if (useCrossValidation.isSelected() == true) {
                    outputInfo.append("\nOptimum number of nearest neighbours from cross validaton is " + ((GWDiscriminantAnalysis) daTask).getNumNearestNeighbours() + "\n");
                }
                String confMatrixStr = "\n\nConfusion Matrix\n\n";
                confMatrixStr += String.format("%8s", " ");
                for (int i = 0; i < numClasses; i++) {
                    confMatrixStr += " | " + String.format("%-8s", "Class " + String.valueOf(uniqueClasses[i]));
                }
                confMatrixStr += " | " + String.format("%-8s", "Total") + "\n";
                for (int i = 0; i < numClasses; i++) {
                    confMatrixStr += String.format("%-8s", "Class " + String.valueOf(uniqueClasses[i]));
                    for (int j = 0; j < numClasses; j++) {
                        confMatrixStr += " | " + String.format("%8d", confMatrix[i][j]);
                    }
                    confMatrixStr += " | " + String.format("%8d", classFreq[i]) + "\n";
                }
                outputInfo.append(confMatrixStr);
                outputInfo.append("\n\nClassification Accuracy                     : " + String.format("%5.2f", classAccuracy * 100.0) + " %");
                outputInfo.append("\nClassification Error Rate                   : " + String.format("%5.2f", (1.0 - classAccuracy) * 100.0) + " %");
                outputInfo.append("\nClassification Accuracy (Random Assignment) : " + String.format("%5.2f", randomClassAccuracy * 100.0) + " %");
            } catch (DiscriminantAnalysisException e) {
                throw new DiscriminantAnalysisGUIException(e.getMessage(), e.getCause());
            }
        }

        private DataSetForApps getNewDataSet() throws DiscriminantAnalysisException {
            int numClasses = daTask.getUniqueClasses().length;
            int numAttributes = dataSet.getColumnCount();
            Object[] newData = null;
            String[] newFieldNames = null;
            int newDataSetSize = -1;
            newDataSetSize = (numAttributes + 1 + (numClasses * 2));
            newData = new Object[newDataSetSize];
            newFieldNames = new String[newDataSetSize];
            for (int i = 0; i < numAttributes; i++) {
                newData[i] = dataSet.getColumnValues(i);
                newFieldNames[i] = dataSet.getColumnName(i);
            }
            newFieldNames[numAttributes] = "Classified";
            newData[numAttributes] = daTask.getClassified();
            for (int i = 0; i < numClasses; i++) {
                newFieldNames[i + (numAttributes + 1)] = "MhDist2_" + String.valueOf(i);
                newData[i + (numAttributes + 1)] = daTask.getMahalanobisDistance2(i);
            }
            for (int i = 0; i < numClasses; i++) {
                newFieldNames[i + (numAttributes + numClasses + 1)] = "PostProb_" + String.valueOf(i);
                newData[i + (numAttributes + numClasses + 1)] = daTask.getPosteriorProbabilities(i);
            }
            DataSetForApps newDataSetForApps = new DataSetForApps(newFieldNames, newData, dataSet.getShapeData());
            return newDataSetForApps;
        }
    }

    /**
	 * main method is used exclusively for testing and debugging purposes only
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        String logFile = DiscriminantAnalysisGUI.class.getSimpleName() + ".log";
        logger.setLevel(Level.ALL);
        logger.info(System.getProperty("java.version"));
        FileHandler logHandler = null;
        try {
            logHandler = new FileHandler(logFile);
            logHandler.setFormatter(new SimpleFormatter());
            logHandler.setLevel(Level.ALL);
            logger.addHandler(logHandler);
        } catch (IOException e) {
            System.out.println("Unable to create log file : " + e.getMessage());
            e.printStackTrace();
        }
        JFrame app = new JFrame();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final DiscriminantAnalysisGUI daGui = new DiscriminantAnalysisGUI();
        app.add(daGui);
        app.pack();
        app.setVisible(true);
        (new SwingWorker<DataSetForApps, Void>() {

            DataSetForApps data = null;

            @Override
            public DataSetForApps doInBackground() {
                URL testFileName = daGui.getClass().getResource("resources/iris_grid_petallength.shp");
                ShapeFileDataReader shpRead = new ShapeFileDataReader();
                shpRead.setFileName(testFileName.getFile());
                Object[] testDataArray = shpRead.getDataSet();
                DataSetForApps dataTest = null;
                if (testDataArray != null) {
                    dataTest = new DataSetForApps(testDataArray);
                    logger.info("test data loaded from " + testFileName.getFile());
                } else {
                    logger.severe("unable to read test data from file " + testFileName.getFile());
                }
                return dataTest;
            }

            @Override
            public void done() {
                try {
                    data = get();
                    DataSetBroadcaster dataCaster = new DataSetBroadcaster();
                    dataCaster.addDataSetListener(daGui);
                    dataCaster.setAndFireDataSet(data);
                } catch (ExecutionException e) {
                    logger.severe("unable to broadcast test data : " + e.getMessage());
                } catch (InterruptedException e) {
                    logger.severe("unable to broadcast test data : " + e.getMessage());
                }
            }
        }).execute();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == categoryCombo) {
            setCategoryIndex(categoryCombo.getSelectedIndex());
            if (categoryIndex > -1) {
                (new SwingWorker<Integer, Void>() {

                    @Override
                    public Integer doInBackground() {
                        int[] categories = (int[]) dataSet.getColumnValues(categoryIndex);
                        int[] categoryFrequencies = NCGStatUtils.getFrequencies(categories);
                        int categoryMinIndex = NCGStatUtils.getMin(categoryFrequencies);
                        int categoryMinFrequency = categoryFrequencies[categoryMinIndex];
                        return Integer.valueOf(categoryMinFrequency);
                    }

                    @Override
                    public void done() {
                        try {
                            int categoryMinFrequency = get();
                            numNearestNeighbours.removeAllItems();
                            minNumNearestNeighboursCV.removeAllItems();
                            maxNumNearestNeighboursCV.removeAllItems();
                            for (int i = 1; i < categoryMinFrequency; i++) {
                                Integer numNeighbours = Integer.valueOf(i);
                                numNearestNeighbours.addItem(numNeighbours);
                                if (i < (categoryMinFrequency - 1)) {
                                    minNumNearestNeighboursCV.addItem(numNeighbours);
                                    maxNumNearestNeighboursCV.addItem(numNeighbours);
                                }
                            }
                        } catch (ExecutionException e) {
                            logger.severe("unable to population nearest neighbours combo box : " + e.getMessage());
                        } catch (InterruptedException e) {
                            logger.severe("unable to population nearest neighbours combo box : " + e.getMessage());
                        }
                    }
                }).execute();
            }
        } else if (e.getSource() == minNumNearestNeighboursCV) {
            int minNeighIndex = minNumNearestNeighboursCV.getSelectedIndex();
            if (minNeighIndex > -1) {
                int numItems = minNumNearestNeighboursCV.getItemCount();
                maxNumNearestNeighboursCV.removeAllItems();
                for (int i = minNeighIndex; i < numItems; i++) {
                    maxNumNearestNeighboursCV.addItem(minNumNearestNeighboursCV.getItemAt(i));
                }
            }
        } else if (e.getSource() == maxNumNearestNeighboursCV) {
            if ((minNumNearestNeighboursCV.getSelectedIndex() > -1) && (maxNumNearestNeighboursCV.getSelectedIndex() > -1)) {
                int minNumNearestNeighbours = Integer.valueOf((Integer) minNumNearestNeighboursCV.getSelectedItem());
                int maxNumNearestNeighbours = Integer.valueOf((Integer) maxNumNearestNeighboursCV.getSelectedItem());
                int neighbourRange = (maxNumNearestNeighbours - minNumNearestNeighbours);
                stepSizeNumNearestNeighboursCV.removeAllItems();
                for (int i = 1; i <= neighbourRange; i++) {
                    stepSizeNumNearestNeighboursCV.addItem(Integer.valueOf(i));
                }
            }
        } else if (e.getSource() == goButton) {
            (new ClassifierThread()).execute();
        } else if (e.getSource() == doPCA) {
            if (standardize.isSelected() == true) {
                standardize.setSelected(false);
            }
            standardize.setEnabled(!doPCA.isSelected());
            numPCAVars.setEnabled(doPCA.isSelected());
        } else if (e.getSource() == doGWDA) {
            kernelFunctionType.setSelectedIndex(0);
            useCrossValidation.setSelected(false);
            crossValidationMethod.setSelectedIndex(0);
            kernelFunctionType.setEnabled(doGWDA.isSelected());
            numNearestNeighbours.setEnabled(doGWDA.isSelected());
            useCrossValidation.setEnabled(doGWDA.isSelected());
            crossValidationMethod.setEnabled(false);
            minNumNearestNeighboursCV.setEnabled(false);
            maxNumNearestNeighboursCV.setEnabled(false);
            stepSizeNumNearestNeighboursCV.setEnabled(false);
        } else if (e.getSource() == useCrossValidation) {
            crossValidationMethod.setEnabled(useCrossValidation.isSelected());
            numNearestNeighbours.setEnabled(!useCrossValidation.isSelected());
            minNumNearestNeighboursCV.setEnabled(useCrossValidation.isSelected());
            maxNumNearestNeighboursCV.setEnabled(useCrossValidation.isSelected());
            stepSizeNumNearestNeighboursCV.setEnabled(useCrossValidation.isSelected());
        } else if (e.getSource() == resetButton) {
            if (newDataSetFired == true) {
                fireDataSetChanged(dataSet);
                newDataSetFired = false;
                outputInfo.setText("");
                indVarIndices = new int[0];
                categoryIndex = -1;
                numClassifications = 0;
                doPCA.setSelected(false);
                standardize.setSelected(false);
            }
        }
    }

    public void dataSetChanged(DataSetEvent e) {
        dataSet = e.getDataSetForApps();
        categoryIndexMap = new HashMap<Integer, Integer>();
        indVarIndexMap = new HashMap<Integer, Integer>();
        int numVars = dataSet.getColumnCount();
        categoryCombo.removeAllItems();
        numPCAVars.removeAllItems();
        outputInfo.setText("");
        for (int i = 0; i < numVars; i++) {
            if (dataSet.getColumnType(i) == DataSetForApps.TYPE_INTEGER) {
                categoryCombo.addItem(dataSet.getColumnName(i));
                categoryIndexMap.put(Integer.valueOf(categoryIndexMap.size()), Integer.valueOf(i));
                numPCAVars.addItem(Integer.valueOf(categoryIndexMap.size()));
            } else if (dataSet.getColumnType(i) == DataSetForApps.TYPE_DOUBLE) {
                indVarIndexMap.put(Integer.valueOf(indVarIndexMap.size()), Integer.valueOf(i));
            }
        }
        indVarPicker.dataSetChanged(e);
    }

    public void subspaceChanged(SubspaceEvent e) {
        if (e.getSource() == indVarPicker) {
            setIndVarIndices(e.getSubspace());
        }
    }

    /**
	 * adds a DataSetListener
	 */
    public void addDataSetListener(DataSetListener l) {
        listenerList.add(DataSetListener.class, l);
    }

    /**
	 * removes a DataSetListener
	 */
    public void removeDataSetListener(DataSetListener l) {
        listenerList.remove(DataSetListener.class, l);
    }

    /**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 *
	 * @see EventListenerList
	 */
    protected void fireDataSetChanged(DataSetForApps data) {
        Object[] listeners = listenerList.getListenerList();
        DataSetEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == DataSetListener.class) {
                if (e == null) {
                    e = new DataSetEvent(data, this);
                }
                ((DataSetListener) listeners[i + 1]).dataSetChanged(e);
            }
        }
    }

    private void setIndVarIndices(int[] indVarIndices) {
        this.indVarIndices = new int[indVarIndices.length];
        for (int i = 0; i < indVarIndices.length; i++) {
            if (indVarIndexMap.containsKey(Integer.valueOf(indVarIndices[i]))) {
                this.indVarIndices[i] = indVarIndexMap.get(Integer.valueOf(indVarIndices[i]));
            } else {
                logger.severe("Independent Variable Index Map does not contain key " + String.valueOf(indVarIndices[i]));
            }
        }
    }

    private void setCategoryIndex(int categoryIndex) {
        if (!categoryIndexMap.isEmpty()) {
            if (categoryIndexMap.containsKey(Integer.valueOf(categoryIndex))) {
                this.categoryIndex = categoryIndexMap.get(Integer.valueOf(categoryIndex));
            } else {
                logger.severe("Category Index Map does not contain key " + String.valueOf(categoryIndex));
            }
        }
    }
}
