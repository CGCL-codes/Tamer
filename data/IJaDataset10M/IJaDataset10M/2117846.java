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
import edu.ucla.stat.SOCR.analyses.example.DichotomousProportionExamples;

public class DichotomousProportion extends Analysis implements PropertyChangeListener {

    private JToolBar toolBar;

    private Frame frame;

    private JComboBox alphaCombo = new JComboBox(new String[] { "0.1", "0.05", "0.01", "0.001" });

    private JPanel alphaPanel = null;

    private JLabel alphaLabel = new JLabel("Select Significance Level:");

    private double alpha = 0.05;

    DichotomousProportionResult result;

    double sampleProportionP = 0, sampleProportionQ = 0;

    double adjustedProportionP = 0, adjustedProportionQ = 0;

    double sampleSEP = 0, sampleSEQ = 0;

    double adjustedSEP = 0, adjustedSEQ = 0;

    String ciTextPString = null, ciTextQString = null;

    String[] valueList = null;

    int[] sampleProportion = null;

    int yLength = 0;

    String dependentHeader = null;

    /**Initialize the Analysis*/
    public void init() {
        super.init();
        analysisType = AnalysisType.DICHOTOMOUS_PROPORTION;
        analysisName = "Proportion Test for Dichotomous Distribution";
        useInputExample = false;
        useLocalExample = false;
        useRandomExample = true;
        useServerExample = false;
        useStaticExample = DichotomousProportionExamples.availableExamples;
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
        alphaCombo.addActionListener(this);
        alphaCombo.addMouseListener(this);
        alphaCombo.setEditable(false);
        alphaCombo.setSelectedIndex(1);
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
        String[] y = new String[yLength];
        for (int i = 0; i < yLength; i++) {
            y[i] = (String) yList.get(i);
        }
        alpha = 0.05;
        try {
            alpha = Double.parseDouble((String) (alphaCombo.getSelectedItem()));
        } catch (Exception e) {
            alpha = 0.05;
        }
        data.appendX("X", y, DataType.FACTOR);
        data.setParameter(analysisType, edu.ucla.stat.SOCR.analyses.model.DichotomousProportion.SIGNIFICANCE_LEVEL, alpha + "");
        result = null;
        try {
            result = (DichotomousProportionResult) data.getAnalysis(AnalysisType.DICHOTOMOUS_PROPORTION);
        } catch (Exception e) {
        }
        try {
            valueList = (String[]) result.getValueList();
        } catch (Exception e) {
        }
        try {
            sampleProportion = (int[]) result.getSampleProportion();
        } catch (Exception e) {
        }
        try {
            sampleProportionP = result.getSampleProportionP();
        } catch (Exception e) {
        }
        try {
            sampleProportionQ = result.getSampleProportionQ();
        } catch (Exception e) {
        }
        try {
            adjustedProportionP = result.getAdjustedProportionP();
        } catch (Exception e) {
        }
        try {
            adjustedProportionQ = result.getAdjustedProportionQ();
        } catch (Exception e) {
        }
        try {
            sampleSEP = result.getSampleSEP();
        } catch (Exception e) {
        }
        try {
            sampleSEQ = result.getSampleSEQ();
        } catch (Exception e) {
        }
        try {
            adjustedSEP = result.getAdjustedSEP();
        } catch (Exception e) {
        }
        try {
            adjustedSEQ = result.getAdjustedSEQ();
        } catch (Exception e) {
        }
        try {
            ciTextPString = result.getCITextP();
        } catch (Exception e) {
        }
        try {
            ciTextQString = result.getCITextQ();
        } catch (Exception e) {
        }
        updateResults();
    }

    public void updateResults() {
        if (result == null) return;
        result.setDecimalFormat(dFormat);
        resultPanelTextArea.setText("\n");
        resultPanelTextArea.append("\tSample size = " + yLength + " \n");
        resultPanelTextArea.append("\n\tVariable  = " + dependentHeader + " \n");
        resultPanelTextArea.append("\n\tGroup " + valueList[0] + ": \tFrequency = " + sampleProportion[0]);
        resultPanelTextArea.append("\n\tGroup " + valueList[1] + ": \tFrequency = " + sampleProportion[1]);
        resultPanelTextArea.append("\n\n\tResults of Dichotomous Proportion Test:\n");
        resultPanelTextArea.append("\n\tSignificance Level = " + result.getFormattedDouble(alpha));
        resultPanelTextArea.append("\n\n\t********** Without Adjustment **********");
        resultPanelTextArea.append("\n\tGroup " + valueList[0] + ": \n\tProportion = " + result.getFormattedDouble(sampleProportionP) + "\n\tStandard Error = " + result.getFormattedDouble(sampleSEP));
        resultPanelTextArea.append("\n\n\tGroup " + valueList[1] + ": \n\tProportion = " + result.getFormattedDouble(sampleProportionQ) + "\n\tStandard Error = " + result.getFormattedDouble(sampleSEQ));
        String ciString = null;
        try {
            ciTextPString = result.getCiString();
        } catch (Exception e) {
        }
        String ciWidth = null, lowerP = null, upperP = null, lowerQ = null, upperQ = null;
        try {
            ciWidth = result.getFormattedDouble(result.getCiWidth());
            lowerP = result.getFormattedDouble(result.getLowerP());
            upperP = result.getFormattedDouble(result.getUpperP());
            lowerQ = result.getFormattedDouble(result.getLowerQ());
            upperQ = result.getFormattedDouble(result.getUpperQ());
        } catch (Exception e) {
        }
        resultPanelTextArea.append("\n\n\t********** With Adjustment **********");
        resultPanelTextArea.append("\n\tGroup " + valueList[0] + ": \n\tProportion = " + result.getFormattedDouble(adjustedProportionP) + "\n\tStandard Error = " + result.getFormattedDouble(adjustedSEP));
        resultPanelTextArea.append("\n\t" + ciString + "% CI = " + result.getFormattedDouble(adjustedProportionP) + " +/- " + ciWidth + "\n\t= (" + lowerP + ", " + upperP + ")");
        resultPanelTextArea.append("\n\n\tGroup " + valueList[1] + ": \n\tProportion = " + result.getFormattedDouble(adjustedProportionQ) + "\n\tStandard Error = " + result.getFormattedDouble(adjustedSEQ));
        resultPanelTextArea.append("\n\t" + ciString + "% CI = " + result.getFormattedDouble(adjustedProportionQ) + " +/- " + ciWidth + "\n\t= (" + lowerQ + ", " + upperQ + ")");
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
        if (propertyName.equals("DataUpdate")) {
            dataTable = (JTable) (e.getNewValue());
            dataPanel.removeAll();
            dataPanel.add(new JScrollPane(dataTable));
        }
    }

    public Container getDisplayPane() {
        this.getContentPane().add(toolBar, BorderLayout.NORTH);
        return this.getContentPane();
    }

    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Statistical_hypothesis_testing");
    }

    protected void setInputPanel() {
        inputPanel.removeAll();
        inputPanel.setPreferredSize(new Dimension(400, 400));
        alphaPanel = new JPanel();
        alphaPanel.add(alphaCombo);
        inputPanel.add(alphaLabel, BorderLayout.EAST);
        inputPanel.add(alphaPanel, BorderLayout.WEST);
    }

    public void actionPerformed(ActionEvent event) {
        super.actionPerformed(event);
        if (event.getSource() == alphaCombo) {
            alpha = Double.parseDouble((String) (alphaCombo.getSelectedItem()));
        }
    }

    public void mouseClicked(MouseEvent event) {
        super.mouseClicked(event);
        if (event.getSource() == alphaCombo) {
            try {
                alpha = Double.parseDouble((String) (alphaCombo.getSelectedItem()));
            } catch (Exception e) {
            }
        }
    }
}
