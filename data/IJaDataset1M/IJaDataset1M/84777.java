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
import edu.ucla.stat.SOCR.analyses.example.OneTExamples;

public class OneT extends Analysis implements PropertyChangeListener {

    private JToolBar toolBar;

    private Frame frame;

    int df = 0;

    double sampleMeanInput = 0, sampleMeanDiff = 0, sampleVar = 0;

    double tStat = 0, pValueOneSided = 0, pValueTwoSided = 0;

    int yLength = 0;

    double t_stat = 0, p_value = 0;

    double testMean = 0;

    String dependentHeader = null;

    OneTResult result;

    /**Initialize the Analysis*/
    public void init() {
        super.init();
        analysisType = AnalysisType.ONE_T;
        analysisName = "One Sample T Test";
        useInputExample = false;
        useLocalExample = false;
        useRandomExample = true;
        useServerExample = false;
        useStaticExample = OneTExamples.availableExamples;
        depMax = 1;
        indMax = 1;
        resultPanelTextArea.setFont(new Font(outputFontFace, Font.BOLD, outputFontSize));
        frame = getFrame(this);
        setName(analysisName);
        toolBar = new JToolBar();
        createActionComponents(toolBar);
        this.getContentPane().add(toolBar, BorderLayout.NORTH);
        tools2.remove(addButton2);
        tools2.remove(removeButton2);
        tools2.remove(removeButton2);
        depLabel.setText(VARIABLE);
        indLabel.setText("");
        listIndepRemoved.setBackground(Color.LIGHT_GRAY);
        mappingInnerPanel.remove(listIndepRemoved);
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
        try {
            dependentHeader = columnModel.getColumn(dependentIndex).getHeaderValue().toString().trim();
        } catch (Exception e) {
        }
        if (!hasExample) {
            JOptionPane.showMessageDialog(this, DATA_MISSING_MESSAGE);
            return;
        }
        if (dependentIndex < 0) {
            JOptionPane.showMessageDialog(this, VARIABLE_MISSING_MESSAGE);
            return;
        }
        Data data = new Data();
        String cellValue = null;
        ArrayList<String> yList = new ArrayList<String>();
        yLength = 0;
        try {
            for (int k = 0; k < dataTable.getRowCount(); k++) {
                try {
                    cellValue = ((String) dataTable.getValueAt(k, dependentIndex)).trim();
                    if (cellValue != null && !cellValue.equals("")) {
                        yList.add(yLength, cellValue);
                        yLength++;
                    } else {
                        continue;
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
        double[] y = new double[yLength];
        for (int i = 0; i < yLength; i++) {
            y[i] = Double.parseDouble((String) yList.get(i));
        }
        String testMeanMessage = "Enter a number for test mean. Default is zero.";
        String testMeanWarning = "You didn't enter a number. Default zero will be used. \nClick on CALCULATE if you'd like to change it. \nOr, click on RESULT to see the results.";
        try {
            testMean = Double.parseDouble(JOptionPane.showInputDialog(testMeanMessage));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, testMeanWarning);
        } catch (Exception e) {
        }
        data.appendY("X", y, DataType.QUANTITATIVE);
        data.appendY("X", y, DataType.QUANTITATIVE);
        data.setParameter(analysisType, edu.ucla.stat.SOCR.analyses.model.OneT.TEST_MEAN, testMean + "");
        result = null;
        try {
            result = (OneTResult) data.getAnalysis(AnalysisType.ONE_T);
        } catch (Exception e) {
        }
        try {
            sampleMeanInput = result.getSampleMeanInput();
        } catch (Exception e) {
        }
        try {
            sampleMeanDiff = result.getSampleMean();
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }
        try {
            sampleVar = result.getSampleVariance();
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }
        try {
            df = result.getDF();
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }
        try {
            tStat = result.getTStat();
        } catch (Exception e) {
        }
        try {
            pValueOneSided = result.getPValueOneSided();
        } catch (Exception e) {
        }
        try {
            pValueTwoSided = result.getPValueTwoSided();
        } catch (Exception e) {
        }
        String p_valueS = null;
        t_stat = sampleMeanDiff / Math.sqrt(sampleVar / (df + 1));
        p_value = 2 * (1 - (new edu.ucla.stat.SOCR.distributions.StudentDistribution(df)).getCDF(Math.abs(t_stat)));
        p_valueS = AnalysisUtility.enhanceSmallNumber(p_value);
        updateResults();
    }

    public void updateResults() {
        if (result == null) return;
        result.setDecimalFormat(dFormat);
        setDecimalFormat(dFormat);
        resultPanelTextArea.setText("\n");
        resultPanelTextArea.append("\n\tSample size = " + yLength + " \n");
        resultPanelTextArea.append("\n\tVariable  = " + dependentHeader + " \n");
        resultPanelTextArea.append("\n\tTest against " + result.getFormattedDouble(testMean) + " \n");
        resultPanelTextArea.append("\n\tResult of One Sample T-Test:\n");
        resultPanelTextArea.append("\n\tSample Mean of " + dependentHeader + " = " + result.getFormattedDouble(sampleMeanInput));
        resultPanelTextArea.append("\n\tSample Mean of Difference        = " + result.getFormattedDouble(sampleMeanDiff));
        resultPanelTextArea.append("\n\n\tSample Variance     = " + result.getFormattedDouble(sampleVar));
        resultPanelTextArea.append("\n\tStandard Error     = " + result.getFormattedDouble(Math.sqrt(sampleVar / yLength)));
        resultPanelTextArea.append("\n\tDegrees of Freedom  = " + df);
        resultPanelTextArea.append("\n\tT-Statistics             = " + result.getFormattedDouble(tStat));
        resultPanelTextArea.append("\n\tOne-Sided P-Value = " + result.getFormattedDouble(pValueOneSided));
        resultPanelTextArea.append("\n\tTwo-Sided P-Value = " + result.getFormattedDouble(pValueTwoSided));
        resultPanelTextArea.setForeground(Color.BLUE);
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
        return new String("http://en.wikipedia.org/wiki/T_test");
    }
}
