package edu.ucla.stat.SOCR.modeler;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import JSci.maths.DoubleVector;
import edu.ucla.stat.SOCR.core.Modeler;
import edu.ucla.stat.SOCR.util.ObservableWrapper;

/**This class Designs a Rice Distribution Fit model fitting curve. */
public class RiceFit_Modeler implements Modeler, ItemListener {

    private ButtonGroup buttonGroup = new ButtonGroup();

    public JCheckBox estimateParams = new JCheckBox("Estimate Parameters");

    public JCheckBox userParams = new JCheckBox("User Parameters", true);

    public edu.ucla.stat.SOCR.distributions.RiceDistribution RiceDistr;

    public float kernalVar = 0;

    private double minX = 0;

    private double maxX = 0;

    private int modelType = CONTINUOUS_DISTRIBUTION_TYPE;

    public JTextField meanParamField = new JTextField("1.0", 3);

    public JLabel meanParamLabel = new JLabel("Center (nu)");

    public JTextField stdevParamField = new JTextField("1.0", 3);

    public JLabel stdevParamLabel = new JLabel("Spread (sigma)");

    private double[] modelX;

    private double[] modelY;

    public DoubleVector coeffs;

    private int dataPts;

    public javax.swing.table.TableColumn clm2;

    private int runs = 0;

    public JTextField kernalVarField = new JTextField("1", 3);

    private JTextArea statsTable = new JTextArea(6, 25);

    private double mn = 0;

    private double sd = 1;

    private int modelCt = 1;

    public JCheckBox rawCheck = new JCheckBox("Raw Data", false);

    private static boolean isContinuous = true;

    public static int SLICE_SIZE = 8001;

    /**This method initializes the applet, including the toolbar,
     * scatterplot, and statistics table.*/
    public RiceFit_Modeler() {
    }

    public RiceFit_Modeler(JPanel controlpanel) {
        controlpanel.add(estimateParams);
        controlpanel.add(userParams);
        buttonGroup.add(estimateParams);
        buttonGroup.add(userParams);
        estimateParams.addItemListener(this);
        userParams.addItemListener(this);
        addParams(controlpanel);
        controlpanel.repaint();
    }

    public void registerObservers(ObservableWrapper o) {
        o.addJCheckBox(estimateParams);
        o.addJCheckBox(userParams);
        o.addJTextField(meanParamField);
        o.addJTextField(stdevParamField);
    }

    public void addParams(JPanel controlpanel) {
        controlpanel.add(meanParamLabel);
        controlpanel.add(meanParamField);
        controlpanel.add(stdevParamLabel);
        controlpanel.add(stdevParamField);
    }

    public int getModelType() {
        return modelType;
    }

    public double getLowerLimit() {
        return Double.NEGATIVE_INFINITY;
    }

    public double getUpperLimit() {
        return Double.POSITIVE_INFINITY;
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
            stdevParamLabel.setVisible(true);
            stdevParamField.setVisible(true);
        } else {
            meanParamLabel.setVisible(false);
            meanParamField.setVisible(false);
            stdevParamLabel.setVisible(false);
            stdevParamField.setVisible(false);
        }
    }

    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == userParams || event.getSource() == estimateParams) {
            if (estimateParams.isSelected()) toggleParams(false); else toggleParams(true);
        }
    }

    public double[] generateSamples(int sampleCount) {
        double[] dat = new double[sampleCount];
        RiceDistr = new edu.ucla.stat.SOCR.distributions.RiceDistribution(Double.parseDouble(meanParamField.getText()), Double.parseDouble(stdevParamField.getText()));
        for (int i = 0; i < sampleCount; i++) dat[i] = RiceDistr.simulate();
        return dat;
    }

    public int getModelCount() {
        return modelCt;
    }

    public void fitCurve(float[] rawDat, double minx, double maxx, JTextArea resultPanelTextArea, boolean rescaleClicked, boolean scaleUp, boolean initReset) {
        fitCurve(rawDat, (float) minx, (float) maxx, resultPanelTextArea, rescaleClicked, scaleUp, initReset);
    }

    public void fitCurve(float[] rawDat, float minx, float maxx, JTextArea resultPanelTextArea, boolean rescaleClicked, boolean scaleUp, boolean initReset) {
        int datalength = 0;
        try {
            int count = 0;
            if (estimateParams.isSelected()) RiceDistr = new edu.ucla.stat.SOCR.distributions.RiceDistribution(rawDat); else RiceDistr = new edu.ucla.stat.SOCR.distributions.RiceDistribution(Double.parseDouble(meanParamField.getText()), Double.parseDouble(stdevParamField.getText()));
            String meanString = Double.toString(RiceDistr.getMean());
            String varString = Double.toString(RiceDistr.getVariance());
            resultPanelTextArea.setText("Center (nu) = " + meanString + "\n SPread (sigma) = " + varString);
            float ind = 1;
            int number = SLICE_SIZE;
            ind = (maxx - minx) / (number - 2);
            modelX = new double[number];
            modelY = new double[number];
            for (int i = 0; i < number; i++) {
                modelX[i] = minx + ind * i;
                modelY[i] = RiceDistr.getDensity(modelX[i]);
            }
            double maxy = modelY[0];
            for (int i = 0; i < modelY.length; i++) {
                maxy = modelY[i];
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultPanelTextArea.setText(e.getMessage());
        }
    }

    public void fitCurve(float[] rawDat, float minx, float maxx, double mu0, double sigma, boolean plotBlocks, boolean scaleUp) {
        int datalength = 0;
        try {
            int count = 0;
            plotBlocks = false;
            if (plotBlocks) RiceDistr = new edu.ucla.stat.SOCR.distributions.RiceDistribution(rawDat); else RiceDistr = new edu.ucla.stat.SOCR.distributions.RiceDistribution(mu0, sigma);
            String meanString = Double.toString(RiceDistr.getMean());
            String varString = Double.toString(RiceDistr.getVariance());
            float ind = 1;
            int number = SLICE_SIZE;
            ind = (maxx - minx) / (number - 2);
            modelX = new double[number];
            modelY = new double[number];
            for (int i = 0; i < number; i++) {
                modelX[i] = minx + ind * i;
                modelY[i] = RiceDistr.getDensity(modelX[i]);
            }
            double maxy = modelY[0];
            for (int i = 0; i < modelY.length; i++) {
                maxy = modelY[i];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDescription() {
        String desc = new String();
        desc = "\t Description field returned by the Rice Distribution SOCR Modeler.";
        return desc;
    }

    /** return the instructions for using this modeler*/
    public String getInstructions() {
        String instructions = new String();
        instructions = "\tInstructions on using the Rice Distribution Modeler.";
        return instructions;
    }

    /** return the references for this modeler*/
    public String getResearch() {
        String research = new String();
        research = "\t research field returned by the Rice Distribution Modeler.";
        return research;
    }

    public boolean isContinuous() {
        return this.isContinuous;
    }

    public double getGraphLowerLimit() {
        return ModelerConstant.GRAPH_LOWER_LIMIT;
    }

    public double getGraphUpperLimit() {
        return ModelerConstant.GRAPH_UPPER_LIMIT;
    }

    public boolean useInitButton() {
        return false;
    }

    public void setSliceSize(int size) {
        SLICE_SIZE = size;
    }
}
