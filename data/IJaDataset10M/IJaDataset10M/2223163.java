package edu.ucla.stat.SOCR.modeler;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import edu.ucla.stat.SOCR.modeler.gui.ModelerConstant;
import edu.ucla.stat.SOCR.util.ObservableWrapper;

public class InverseGaussianFit_Modeler extends Modeler implements ItemListener {

    public edu.ucla.stat.SOCR.distributions.InverseGaussianDistribution InvGausDistr;

    private double[] modelX;

    private double[] modelY;

    private int modelCt = 1;

    private int modelType = CONTINUOUS_DISTRIBUTION_TYPE;

    private static boolean isContinuous = true;

    public static final int SLICE_SIZE = 4001;

    private int numberOfQuantiles = 100;

    public JCheckBox estimateParams = new JCheckBox("Estimate Parameters");

    public JCheckBox userParams = new JCheckBox("User Parameters", true);

    public JLabel meanParamLabel = new JLabel("Mean Parameter");

    public JTextField meanParamField = new JTextField("1.0", 3);

    public JLabel shapeParamLabel = new JLabel("Shape Parameter");

    public JTextField shapeParamField = new JTextField("1.0", 3);

    private ButtonGroup buttonGroup = new ButtonGroup();

    public InverseGaussianFit_Modeler() {
    }

    public InverseGaussianFit_Modeler(JPanel controlpanel) {
        controlpanel.add(estimateParams);
        controlpanel.add(userParams);
        buttonGroup.add(estimateParams);
        buttonGroup.add(userParams);
        estimateParams.addItemListener(this);
        userParams.addItemListener(this);
        addParams(controlpanel);
        controlpanel.repaint();
    }

    public void addParams(JPanel controlpanel) {
        controlpanel.add(meanParamLabel);
        controlpanel.add(meanParamField);
        controlpanel.add(shapeParamLabel);
        controlpanel.add(shapeParamField);
    }

    public void fitCurve(float[] rawDat, double minx, double maxx, JTextArea resultPanelTextArea, boolean rescaleClicked, boolean scaleUp, boolean initReset) {
        fitCurve(rawDat, (float) minx, (float) maxx, resultPanelTextArea, rescaleClicked, scaleUp, initReset);
    }

    public void fitCurve(float[] rawDat, float minx, float maxx, JTextArea resultPanelTextArea, boolean rescaleClicked, boolean scaleUp, boolean initReset) {
        try {
            if (estimateParams.isSelected()) InvGausDistr = new edu.ucla.stat.SOCR.distributions.InverseGaussianDistribution(rawDat); else InvGausDistr = new edu.ucla.stat.SOCR.distributions.InverseGaussianDistribution(Double.parseDouble(meanParamField.getText()), Double.parseDouble(shapeParamField.getText()));
            resultPanelTextArea.setText("Mean Parameter = " + Double.toString(InvGausDistr.getMean()) + "\nShape Parameter = " + Double.toString(InvGausDistr.getLambda()) + "\n\n");
            float ind = 1;
            int number = SLICE_SIZE;
            ind = (maxx - minx) / (number - 2);
            modelX = new double[number];
            modelY = new double[number];
            for (int i = 0; i < number; i++) {
                modelX[i] = minx + ind * i;
                modelY[i] = InvGausDistr.getDensity(modelX[i]);
            }
            double[] temp = new double[rawDat.length];
            double[] x = new double[numberOfQuantiles];
            double[] y = new double[numberOfQuantiles];
            for (int i = 0; i < numberOfQuantiles; i++) y[i] = InvGausDistr.getQuantile((i + 0.5) / numberOfQuantiles);
            for (int i = 0; i < rawDat.length; i++) temp[i] = (double) rawDat[i];
            x = edu.ucla.stat.SOCR.util.AnalysisUtility.getQuantileArray(temp);
            resultPanelTextArea.append(getKSModelTestString(InvGausDistr.getName(), numberOfQuantiles, x, y));
        } catch (Exception e) {
            resultPanelTextArea.setText(e.getMessage());
        }
    }

    public double[] generateSamples(int sampleCount) {
        InvGausDistr = new edu.ucla.stat.SOCR.distributions.InverseGaussianDistribution(Double.parseDouble(meanParamField.getText()), Double.parseDouble(shapeParamField.getText()));
        double[] dat = new double[sampleCount];
        for (int i = 0; i < sampleCount; i++) dat[i] = InvGausDistr.simulate();
        return dat;
    }

    public String getDescription() {
        String desc = new String();
        desc = "URL: http://socr.ucla.edu/htmls/dist/InverseGaussian_Distribution.html";
        return desc;
    }

    public double getGraphLowerLimit() {
        return ModelerConstant.GRAPH_LOWER_LIMIT;
    }

    public double getGraphUpperLimit() {
        return ModelerConstant.GRAPH_UPPER_LIMIT;
    }

    public String getInstructions() {
        String instructions = new String();
        instructions = "\tInstructions on using the modeler.";
        return instructions;
    }

    public double getLowerLimit() {
        return Double.NEGATIVE_INFINITY;
    }

    public int getModelCount() {
        return modelCt;
    }

    public int getModelType() {
        return modelType;
    }

    public String getResearch() {
        String research = new String();
        research = "\tresearch field returned by the modeler.";
        return research;
    }

    public double getUpperLimit() {
        return Double.POSITIVE_INFINITY;
    }

    public boolean isContinuous() {
        return InverseGaussianFit_Modeler.isContinuous;
    }

    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == userParams || event.getSource() == estimateParams) {
            if (estimateParams.isSelected()) toggleParams(false); else toggleParams(true);
        }
    }

    public void registerObservers(ObservableWrapper o) {
        o.addJCheckBox(estimateParams);
        o.addJCheckBox(userParams);
        o.addJTextField(meanParamField);
        o.addJTextField(shapeParamField);
    }

    public double[] returnModelX() {
        return modelX;
    }

    public double[] returnModelY() {
        return modelY;
    }

    public void toggleParams(boolean istrue) {
        if (istrue) {
            meanParamLabel.setVisible(true);
            meanParamField.setVisible(true);
            shapeParamLabel.setVisible(true);
            shapeParamField.setVisible(true);
        } else {
            meanParamLabel.setVisible(false);
            meanParamField.setVisible(false);
            shapeParamLabel.setVisible(false);
            shapeParamField.setVisible(false);
        }
    }

    public boolean useInitButton() {
        return false;
    }
}
