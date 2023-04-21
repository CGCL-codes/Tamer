package edu.ucla.stat.SOCR.analyses.gui;

import edu.ucla.stat.SOCR.distributions.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.util.ArrayList;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.util.AnalysisUtility;
import edu.ucla.stat.SOCR.analyses.model.*;
import edu.ucla.stat.SOCR.analyses.example.SurvivalExamples;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;

public class Survival extends Analysis implements PropertyChangeListener {

    private JToolBar toolBar;

    private Frame frame;

    protected JLabel timeLabel = new JLabel("TIME");

    protected JLabel censorLabel = new JLabel("CENSORED");

    protected JLabel groupNameLabel = new JLabel("GROUP NAMES");

    private double[][] survivalTime = null;

    private double[][] survivalRate = null;

    private double[][] upperCI = null;

    private double[][] lowerCI = null;

    private double[][] survivalSE = null;

    private double[][] censoredTime = null;

    private double[][] censoredRate = null;

    private double[] maxTime = null;

    private int[][] atRisk = null;

    private String[] groupNames = null;

    private int plotSize = 0;

    private boolean useGroupNames = true;

    int df = 0;

    String numberCaseCensored = null;

    double sampleMean = 0, sampleVar = 0;

    String timeList = null;

    double t_stat = 0, p_value = 0;

    String p_valueS = null;

    int timeLength = 0;

    SurvivalResult result = null;

    /**Initialize the Analysis*/
    public void init() {
        super.init();
        analysisType = AnalysisType.SURVIVAL;
        analysisName = "Survival Analysis";
        useInputExample = false;
        useLocalExample = false;
        useRandomExample = false;
        useServerExample = false;
        useStaticExample = SurvivalExamples.availableExamples;
        depMax = 1;
        indMax = 1;
        resultPanelTextArea.setFont(new Font(outputFontFace, Font.BOLD, outputFontSize));
        frame = getFrame(this);
        setName(analysisName);
        toolBar = new JToolBar();
        createActionComponents(toolBar);
        this.getContentPane().add(toolBar, BorderLayout.NORTH);
        validate();
        reset();
    }

    /** Create the actions for the buttons */
    protected void createActionComponents(JToolBar toolBar) {
        super.createActionComponents(toolBar);
    }

    /**This method sets up the analysis protocol, when the applet starts*/
    public void start() {
    }

    /**This method defines the specific statistical Analysis to be carried our on the user specified data. ANOVA is done in this case. */
    public void doAnalysis() {
        if (dataTable.isEditing()) dataTable.getCellEditor().stopCellEditing();
        String timeHeader = null;
        String censorHeader = null;
        String groupNameHeader = null;
        try {
            timeHeader = columnModel.getColumn(timeIndex).getHeaderValue().toString().trim();
        } catch (Exception e) {
        }
        try {
            censorHeader = columnModel.getColumn(censorIndex).getHeaderValue().toString().trim();
        } catch (Exception e) {
        }
        try {
            groupNameHeader = columnModel.getColumn(groupNamesIndex).getHeaderValue().toString().trim();
        } catch (Exception e) {
        }
        if (!hasExample) {
            JOptionPane.showMessageDialog(this, DATA_MISSING_MESSAGE);
            return;
        }
        if (timeIndex < 0 || censorIndex < 0) {
            JOptionPane.showMessageDialog(this, VARIABLE_MISSING_MESSAGE);
            return;
        }
        if (groupNamesIndex < 0) {
            useGroupNames = false;
        }
        Data data = new Data();
        String cellValue = null;
        ArrayList<String> timeArrayList = new ArrayList<String>();
        timeLength = 0;
        try {
            for (int k = 0; k < dataTable.getRowCount(); k++) {
                try {
                    cellValue = ((String) dataTable.getValueAt(k, timeIndex)).trim();
                    if (cellValue != null && !cellValue.equals("")) {
                        timeArrayList.add(timeLength, cellValue);
                        timeLength++;
                    } else {
                        continue;
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
        if (timeLength <= 0) {
            JOptionPane.showMessageDialog(this, NULL_VARIABLE_MESSAGE);
            return;
        }
        double[] time = new double[timeLength];
        for (int i = 0; i < timeLength; i++) {
            time[i] = Double.parseDouble((String) timeArrayList.get(i));
        }
        int censorLength = 0;
        ArrayList<String> censorArrayList = new ArrayList<String>();
        try {
            for (int k = 0; k < dataTable.getRowCount(); k++) {
                try {
                    cellValue = ((String) dataTable.getValueAt(k, censorIndex)).trim();
                    if (cellValue != null && !cellValue.equals("")) {
                        censorArrayList.add(censorLength, cellValue);
                        censorLength++;
                    } else {
                        continue;
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
        if (censorLength <= 0) {
            JOptionPane.showMessageDialog(this, NULL_VARIABLE_MESSAGE);
            return;
        }
        byte[] censor = new byte[censorLength];
        for (int i = 0; i < censorLength; i++) {
            censor[i] = Byte.parseByte((String) censorArrayList.get(i));
        }
        int treatLength = 0;
        String[] treat = null;
        if (useGroupNames) {
            ArrayList<String> treatArrayList = new ArrayList<String>();
            try {
                for (int k = 0; k < dataTable.getRowCount(); k++) {
                    try {
                        cellValue = ((String) dataTable.getValueAt(k, groupNamesIndex)).trim();
                        if (cellValue != null && !cellValue.equals("")) {
                            treatArrayList.add(treatLength, cellValue);
                            treatLength++;
                        } else {
                            continue;
                        }
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e) {
            }
            if (treatLength <= 0) {
                JOptionPane.showMessageDialog(this, NULL_VARIABLE_MESSAGE);
                return;
            }
            treat = new String[treatLength];
            for (int i = 0; i < treatLength; i++) {
                treat[i] = (String) treatArrayList.get(i);
            }
        } else {
            treatLength = timeLength;
            treat = new String[treatLength];
            for (int i = 0; i < treatLength; i++) {
                treat[i] = "GROUP_ONE";
            }
        }
        try {
            result = (SurvivalResult) data.getSurvivalResult(time, censor, treat);
        } catch (Exception e) {
        }
        try {
            survivalTime = result.getSurvivalTime();
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }
        try {
            survivalRate = result.getSurvivalRate();
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }
        try {
            upperCI = result.getSurvivalUpperCI();
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }
        try {
            lowerCI = result.getSurvivalLowerCI();
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }
        try {
            atRisk = result.getSurvivalAtRisk();
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }
        try {
            survivalSE = result.getSurvivalSE();
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }
        try {
            groupNames = result.getSurvivalGroupNames();
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }
        try {
            timeList = result.getSurvivalTimeList();
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }
        try {
            censoredTime = result.getCensoredTimeArray();
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }
        try {
            numberCaseCensored = result.getNumberCensored();
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }
        try {
            censoredRate = result.getCensoredRateArray();
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }
        try {
            maxTime = result.getMaxTime();
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }
        for (int i = 0; i < maxTime.length; i++) {
        }
        for (int i = 0; i < censoredTime.length; i++) {
            if (censoredTime[i].length > 0) {
                for (int j = 0; j < censoredTime[i].length; j++) {
                }
            }
        }
        t_stat = sampleMean / Math.sqrt(sampleVar / (df + 1));
        p_value = 1 - (new edu.ucla.stat.SOCR.distributions.StudentDistribution(df)).getCDF(t_stat);
        p_valueS = AnalysisUtility.enhanceSmallNumber(p_value);
        updateResults();
        doGraph();
    }

    public void updateResults() {
        if (result == null) return;
        result.setDecimalFormat(dFormat);
        setDecimalFormat(dFormat);
        resultPanelTextArea.setText("\n");
        resultPanelTextArea.append("\tResult of Survival Analysis Using Kaplan-Meier Model:\n");
        resultPanelTextArea.append("\n\tSamepl Size = " + timeLength);
        resultPanelTextArea.append("\n\tNumber of Censored Cases= " + numberCaseCensored);
        resultPanelTextArea.append("\n\tNumber of Groups Cases= " + groupNames.length);
        resultPanelTextArea.append("\n\tGroups  = ");
        for (int i = 0; i < groupNames.length; i++) {
            resultPanelTextArea.append("   " + groupNames[i] + "\t");
        }
        resultPanelTextArea.append("\n");
        resultPanelTextArea.append("\n\n\n\tSurvival Times (Censored Cases Marked +) = " + timeList);
        resultPanelTextArea.append("\n\n\n\tTime\tNo. At Risk\tRate\tSE of Rate\tUpper CI\tLower CI ");
        for (int i = 0; i < survivalRate.length; i++) {
            resultPanelTextArea.append("\n\n\tGroup = " + groupNames[i] + "\n");
            for (int j = 0; j < survivalRate[i].length; j++) {
                if (upperCI[i][j] == 1) {
                    resultPanelTextArea.append("\n\t" + survivalTime[i][j] + "\t" + atRisk[i][j] + "\t" + result.getFormattedDouble(survivalRate[i][j]) + "\t" + result.getFormattedDouble(survivalSE[i][j]) + "\t" + result.getFormattedDouble(upperCI[i][j]) + "\t" + result.getFormattedDouble(lowerCI[i][j]));
                } else {
                    resultPanelTextArea.append("\n\t" + survivalTime[i][j] + "\t" + atRisk[i][j] + "\t" + result.getFormattedDouble(survivalRate[i][j]) + "\t" + result.getFormattedDouble(survivalSE[i][j]) + "\t" + result.getFormattedDouble(upperCI[i][j]) + "\t" + result.getFormattedDouble(lowerCI[i][j]));
                }
                plotSize++;
            }
        }
        resultPanelTextArea.append("\n\n\n\n");
        resultPanelTextArea.setForeground(Color.BLUE);
    }

    protected void doGraph() {
        graphPanel.removeAll();
        JFreeChart scatterChart = null;
        ChartPanel chartPanel = null;
        Chart chartFactory = new Chart();
        String lineType = "excludesZeroNoShape";
        boolean useCI = true;
        double[][] survivalTimePlot = new double[survivalTime.length][];
        double[][] survivalRatePlot = new double[survivalRate.length][];
        double[][] survivalRateUpperCIPlot = new double[survivalRate.length][];
        double[][] survivalRateLowerCIPlot = new double[survivalRate.length][];
        double tempTime = 0, tempRate = 0, tempRateUpperCI = 0, tempRateLowerCI = 0;
        int increment = 2;
        int arrayLength = 0;
        int k = 0;
        for (int i = 0; i < survivalTime.length; i++) {
            arrayLength = 2 * survivalRate[i].length + increment;
            k = 0;
            survivalTimePlot[i] = new double[arrayLength];
            survivalRatePlot[i] = new double[arrayLength];
            survivalRateUpperCIPlot[i] = new double[arrayLength];
            survivalRateLowerCIPlot[i] = new double[arrayLength];
            survivalTimePlot[i][0] = 0;
            survivalRatePlot[i][0] = 1;
            survivalRateUpperCIPlot[i][0] = 1;
            survivalRateLowerCIPlot[i][0] = 1;
            int j = 1;
            for (j = 1; j < survivalTimePlot[i].length - 1; j++) {
                if (j % 2 == 1) {
                    survivalTimePlot[i][j] = survivalTime[i][k];
                    survivalRatePlot[i][j] = survivalRatePlot[i][j - 1];
                    survivalRateUpperCIPlot[i][j] = survivalRateUpperCIPlot[i][j - 1];
                    survivalRateLowerCIPlot[i][j] = survivalRateLowerCIPlot[i][j - 1];
                    tempTime = survivalTimePlot[i][j];
                    tempRate = survivalRatePlot[i][j];
                    tempRateUpperCI = survivalRateUpperCIPlot[i][j];
                    tempRateLowerCI = survivalRateLowerCIPlot[i][j];
                } else {
                    survivalTimePlot[i][j] = tempTime;
                    survivalRatePlot[i][j] = survivalRate[i][k];
                    survivalRateUpperCIPlot[i][j] = upperCI[i][k];
                    survivalRateLowerCIPlot[i][j] = lowerCI[i][k];
                    k++;
                }
            }
            int index = j;
            survivalTimePlot[i][index] = maxTime[i];
            survivalRatePlot[i][index] = survivalRatePlot[i][index - 1];
            survivalRateUpperCIPlot[i][index] = survivalRateUpperCIPlot[i][index - 1];
            survivalRateLowerCIPlot[i][index] = survivalRateLowerCIPlot[i][index - 1];
        }
        int numberGroups = 0, lineSetCount = 0, tempIndex = 0;
        double[][] finalTimeArray = null;
        double[][] finalRateArray = null;
        String[] finalGroupNames = null;
        int numberOfLines = 0;
        Color[] colorArray = null;
        if (useCI) {
            numberGroups = groupNames.length;
            lineSetCount = 3;
            tempIndex = 0;
            numberOfLines = lineSetCount * numberGroups;
            finalTimeArray = new double[numberOfLines][];
            finalRateArray = new double[numberOfLines][];
            finalGroupNames = new String[numberOfLines];
            colorArray = new Color[numberOfLines];
            for (int i = 0; i < numberGroups; i++) {
                finalTimeArray[tempIndex] = survivalTimePlot[i];
                finalTimeArray[tempIndex + 1] = survivalTimePlot[i];
                finalTimeArray[tempIndex + 2] = survivalTimePlot[i];
                finalRateArray[tempIndex] = survivalRatePlot[i];
                finalRateArray[tempIndex + 1] = survivalRateUpperCIPlot[i];
                finalRateArray[tempIndex + 2] = survivalRateLowerCIPlot[i];
                finalGroupNames[tempIndex] = groupNames[i];
                finalGroupNames[tempIndex + 1] = groupNames[i] + " Upper CI";
                finalGroupNames[tempIndex + 2] = groupNames[i] + " Lower CI";
                if (i % 5 == 0) {
                    colorArray[tempIndex] = Color.RED;
                    colorArray[tempIndex + 1] = Color.PINK;
                    colorArray[tempIndex + 2] = Color.PINK;
                } else if (i % 5 == 1) {
                    colorArray[tempIndex] = Color.BLUE;
                    colorArray[tempIndex + 1] = Color.CYAN;
                    colorArray[tempIndex + 2] = Color.CYAN;
                } else if (i % 5 == 2) {
                    colorArray[tempIndex] = Color.GRAY;
                    colorArray[tempIndex + 1] = Color.LIGHT_GRAY;
                    colorArray[tempIndex + 2] = Color.LIGHT_GRAY;
                } else if (i % 5 == 3) {
                    colorArray[tempIndex] = Color.MAGENTA;
                    colorArray[tempIndex + 1] = Color.PINK;
                    colorArray[tempIndex + 2] = Color.PINK;
                } else {
                    colorArray[tempIndex] = Color.GREEN;
                    colorArray[tempIndex + 1] = Color.YELLOW;
                    colorArray[tempIndex + 2] = Color.YELLOW;
                }
                tempIndex += lineSetCount;
            }
        } else {
            numberGroups = groupNames.length;
            lineSetCount = 2;
            tempIndex = 0;
            finalTimeArray = new double[lineSetCount * numberGroups][];
            finalRateArray = new double[lineSetCount * numberGroups][];
            finalGroupNames = new String[lineSetCount * numberGroups];
            for (int i = 0; i < numberGroups; i++) {
                finalTimeArray[tempIndex] = survivalTimePlot[i];
                finalTimeArray[tempIndex + 1] = censoredTime[i];
                finalRateArray[tempIndex] = survivalRatePlot[i];
                finalRateArray[tempIndex + 1] = censoredRate[i];
                finalGroupNames[tempIndex] = groupNames[i];
                tempIndex += lineSetCount;
            }
        }
        scatterChart = chartFactory.getLineChart("Rate vs. Time", "Time", "Rate", lineSetCount * groupNames.length, finalGroupNames, finalTimeArray, finalRateArray, colorArray, lineType);
        chartPanel = new ChartPanel(scatterChart, false);
        chartPanel.setPreferredSize(new Dimension(plotWidth, plotHeight));
        graphPanel.add(chartPanel);
        graphPanel.validate();
    }

    /** convert a generic string s to a fixed length one. */
    public String monoString(String s) {
        String sAdd = new String(s + "                                      ");
        return sAdd.substring(0, 14);
    }

    /** convert a generic double s to a "nice" fixed length string */
    public String monoString(double s) {
        final double zero = 0.00001;
        Double sD = new Double(s);
        String sAdd = new String();
        if (s > zero) sAdd = new String(sD.toString()); else sAdd = "<0.00001";
        sAdd = sAdd.toLowerCase();
        int i = sAdd.indexOf('e');
        if (i > 0) sAdd = sAdd.substring(0, 4) + "E" + sAdd.substring(i + 1, sAdd.length()); else if (sAdd.length() > 10) sAdd = sAdd.substring(0, 10);
        sAdd = sAdd + "                                      ";
        return sAdd.substring(0, 14);
    }

    /** convert a generic integer s to a fixed length string */
    public String monoString(int s) {
        Integer sD = new Integer(s);
        String sAdd = new String(sD.toString());
        sAdd = sAdd + "                                      ";
        return sAdd.substring(0, 14);
    }

    /** Implementation of PropertyChageListener.*/
    public void propertyChange(PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();
        System.err.println("From RegCorrAnal:: propertyName =" + propertyName + "!!!");
        if (propertyName.equals("DataUpdate")) {
            dataTable = (JTable) (e.getNewValue());
            dataPanel.removeAll();
            dataPanel.add(new JScrollPane(dataTable));
            System.err.println("From RegCorrAnal:: data UPDATED!!!");
        }
    }

    public Container getDisplayPane() {
        this.getContentPane().add(toolBar, BorderLayout.NORTH);
        return this.getContentPane();
    }

    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Survival_analysis");
    }

    protected void setMappingPanel() {
        listIndex = new int[dataTable.getColumnCount()];
        for (int j = 0; j < listIndex.length; j++) listIndex[j] = 1;
        bPanel = new JPanel(new BorderLayout());
        mappingInnerPanel = new JPanel(new GridLayout(1, 3));
        mappingPanel.add(mappingInnerPanel, BorderLayout.CENTER);
        mappingInnerPanel.setBackground(Color.WHITE);
        addButton1.addActionListener(this);
        addButton2.addActionListener(this);
        addButton3.addActionListener(this);
        removeButton1.addActionListener(this);
        removeButton2.addActionListener(this);
        removeButton3.addActionListener(this);
        lModel1 = new DefaultListModel();
        lModel2 = new DefaultListModel();
        lModel3 = new DefaultListModel();
        lModel4 = new DefaultListModel();
        int cellWidth = 10;
        listAdded = new JList(lModel1);
        listAdded.setSelectedIndex(0);
        listTime = new JList(lModel2);
        listCensor = new JList(lModel3);
        listGroupNames = new JList(lModel4);
        paintTable(listIndex);
        listAdded.setFixedCellWidth(cellWidth);
        listTime.setFixedCellWidth(cellWidth);
        listCensor.setFixedCellWidth(cellWidth);
        listGroupNames.setFixedCellWidth(cellWidth);
        listAdded.setBackground(Color.WHITE);
        listTime.setBackground(Color.WHITE);
        listCensor.setBackground(Color.WHITE);
        listGroupNames.setBackground(Color.WHITE);
        tools1.add(timeLabel);
        tools2.add(censorLabel);
        tools3.add(groupNameLabel);
        tools1.add(addButton1);
        tools1.add(removeButton1);
        tools2.add(addButton2);
        tools2.add(removeButton2);
        tools3.add(addButton3);
        tools3.add(removeButton3);
        JPanel panelLeft = new JPanel(new GridLayout(3, 1, 50, 50));
        JPanel panelCenter = new JPanel(new GridLayout(3, 1, 50, 50));
        JPanel panelRight = new JPanel(new GridLayout(3, 1, 50, 50));
        mappingInnerPanel.add(panelLeft, BorderLayout.WEST);
        mappingInnerPanel.add(panelCenter, BorderLayout.CENTER);
        mappingInnerPanel.add(panelRight, BorderLayout.EAST);
        panelLeft.add(new JScrollPane(listAdded));
        panelCenter.add(tools1);
        panelRight.add(new JScrollPane(listTime));
        panelCenter.add(tools2);
        panelRight.add(new JScrollPane(listCensor));
        panelCenter.add(tools3);
        panelRight.add(new JScrollPane(listGroupNames));
    }

    public void paintTable(int[] lstInd) {
        lModel1.clear();
        lModel2.clear();
        lModel3.clear();
        lModel4.clear();
        for (int i = 0; i < lstInd.length; i++) {
            switch(lstInd[i]) {
                case 0:
                    break;
                case 1:
                    lModel1.addElement(columnModel.getColumn(i).getHeaderValue().toString().trim());
                    listAdded.setSelectedIndex(0);
                    break;
                case 2:
                    lModel2.addElement(columnModel.getColumn(i).getHeaderValue().toString().trim());
                    listTime.setSelectedIndex(0);
                    break;
                case 3:
                    lModel3.addElement(columnModel.getColumn(i).getHeaderValue().toString().trim());
                    listCensor.setSelectedIndex(0);
                    break;
                case 4:
                    lModel4.addElement(columnModel.getColumn(i).getHeaderValue().toString().trim());
                    listGroupNames.setSelectedIndex(0);
                    break;
                default:
                    break;
            }
        }
        listAdded.setSelectedIndex(0);
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == addButton1) {
            addButtonTime();
        } else if (event.getSource() == removeButton1) {
            removeButtonTime();
        } else if (event.getSource() == addButton2) {
            addButtonCensor();
        } else if (event.getSource() == removeButton2) {
            removeButtonCensor();
        } else if (event.getSource() == addButton3) {
            addButtonGroupNames();
        } else if (event.getSource() == removeButton3) {
            removeButtonGroupNames();
        }
    }

    protected void addButtonTime() {
        int ct1 = -1;
        int sIdx = listAdded.getSelectedIndex();
        int idx2 = lModel2.getSize();
        if (sIdx > -1 && idx2 < 1) {
            for (int i = 0; i < listIndex.length; i++) {
                if (listIndex[i] == 1) ct1++;
                if (ct1 == sIdx) {
                    timeIndex = i;
                    break;
                }
            }
            listIndex[timeIndex] = 2;
            paintTable(listIndex);
        }
    }

    protected void removeButtonTime() {
        int ct1 = -1;
        int idx1 = 0;
        int sIdx = listTime.getSelectedIndex();
        if (sIdx > -1) {
            for (int i = 0; i < listIndex.length; i++) {
                if (listIndex[i] == 2) ct1++;
                if (ct1 == sIdx) {
                    idx1 = i;
                    break;
                }
            }
            listIndex[idx1] = 1;
            paintTable(listIndex);
        }
    }

    protected void addButtonCensor() {
        int ct1 = -1;
        int sIdx = listAdded.getSelectedIndex();
        int idx3 = lModel3.getSize();
        if (sIdx > -1 && idx3 < 1) {
            for (int i = 0; i < listIndex.length; i++) {
                if (listIndex[i] == 1) ct1++;
                if (ct1 == sIdx) {
                    censorIndex = i;
                    break;
                }
            }
            listIndex[censorIndex] = 3;
            paintTable(listIndex);
        }
    }

    protected void removeButtonCensor() {
        int ct1 = -1;
        int idx1 = 0;
        int sIdx = listCensor.getSelectedIndex();
        if (sIdx > -1) {
            for (int i = 0; i < listIndex.length; i++) {
                if (listIndex[i] == 3) ct1++;
                if (ct1 == sIdx) {
                    idx1 = i;
                    break;
                }
            }
            listIndex[idx1] = 1;
            paintTable(listIndex);
        }
    }

    protected void addButtonGroupNames() {
        int ct1 = -1;
        int sIdx = listAdded.getSelectedIndex();
        int idx4 = lModel4.getSize();
        if (sIdx > -1 && idx4 < 1) {
            for (int i = 0; i < listIndex.length; i++) {
                if (listIndex[i] == 1) ct1++;
                if (ct1 == sIdx) {
                    groupNamesIndex = i;
                    break;
                }
            }
            listIndex[groupNamesIndex] = 4;
            paintTable(listIndex);
        }
    }

    protected void removeButtonGroupNames() {
        int ct1 = -1;
        int idx1 = 0;
        int sIdx = listGroupNames.getSelectedIndex();
        if (sIdx > -1) {
            for (int i = 0; i < listIndex.length; i++) {
                if (listIndex[i] == 4) ct1++;
                if (ct1 == sIdx) {
                    idx1 = i;
                    break;
                }
            }
            listIndex[idx1] = 1;
            paintTable(listIndex);
        }
    }

    public void reset() {
        super.reset();
        hasExample = false;
        timeIndex = -1;
        censorIndex = -1;
        groupNamesIndex = -1;
        removeButtonGroupNames();
        removeButtonCensor();
        removeButtonTime();
    }

    protected void removeButtonIndependentAll() {
        timeIndex = -1;
        censorIndex = -1;
        groupNamesIndex = -1;
    }

    protected void removeButtonDependent() {
        timeIndex = -1;
        censorIndex = -1;
        groupNamesIndex = -1;
    }

    protected void setResultPanel() {
        super.setResultPanel();
        resultPanelTextArea.setRows(30);
        resultPanelTextArea.setColumns(90);
    }
}
