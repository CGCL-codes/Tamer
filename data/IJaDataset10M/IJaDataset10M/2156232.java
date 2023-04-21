package dynamicClustering.ClusteringMethods;

import java.util.Vector;
import javax.swing.JOptionPane;
import org.tigr.microarray.mev.cluster.gui.IClusterGUIAutoExec;
import org.tigr.microarray.mev.cluster.gui.IDistanceMenu;
import org.tigr.microarray.mev.cluster.gui.impl.sota.SOTAInitDialog;
import dynamicClustering.DataTracking.dataProcessing;
import dynamicClustering.GUI.progressBarGUI;
import dynamicClustering.Options.Option;
import dynamicClustering.clusterSignificance.TMEVBridge;

public class SOTAClusteringMethod extends ClusterMethod {

    private boolean useVariance;

    private boolean runToMaxCycles;

    private boolean setMaxClusterDiv;

    private boolean calcClusterHCL;

    private boolean calcFullTreeSampleHCL;

    private boolean clusterGenes;

    private boolean absoluteDistance;

    private int maxCycles = 10;

    private int maxEpochsPerCycle = 1000;

    private int neighborhoodLevel = 5;

    private Double maxClusterDiv = 0.01;

    private double startCellDiversity;

    private double finishCellDiversity;

    private double SOTAinterval;

    private float epochStopCriteria = (float) 0.0001;

    private float maxTreeDiv = (float) 0.01;

    private float migFactor_w = (float) 0.01;

    private float migFactor_p = (float) 0.005;

    private float migFactor_s = (float) 0.001;

    private float pValue = (float) 0.05;

    @Override
    public void GUI() {
        IDistanceMenu menu = dataFramework.getDistanceMenu();
        int distFactor = 1;
        switch(metric) {
            case 1:
                distFactor = 1;
                break;
            case 2:
                distFactor = -1;
                break;
            case 3:
                distFactor = -1;
                break;
            case 4:
                distFactor = 1;
                break;
            case 5:
                distFactor = -1;
                break;
            case 6:
                distFactor = -1;
                break;
            case 7:
                distFactor = -1;
                break;
            case 8:
                distFactor = 1;
                break;
            case 9:
                distFactor = -1;
                break;
            case 10:
                distFactor = -1;
                break;
            default:
                distFactor = 1;
                break;
        }
        SOTAInitDialog sota_dialog = new SOTAInitDialog(dataFramework.getFrame(), distFactor, this.getMetricString(), menu.isAbsoluteDistance());
        if (sota_dialog.showModal() != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            absoluteDistance = sota_dialog.isAbsoluteDistance();
            clusterGenes = sota_dialog.getBoolean("clusterGenes");
            maxCycles = sota_dialog.getInt("maxCycles");
            maxEpochsPerCycle = sota_dialog.getInt("maxEpochsPerCycle");
            epochStopCriteria = sota_dialog.getFloat("epochStopCriteria");
            migFactor_w = sota_dialog.getFloat("migFactor_w");
            migFactor_p = sota_dialog.getFloat("migFactor_p");
            migFactor_s = sota_dialog.getFloat("migFactor_s");
            neighborhoodLevel = sota_dialog.getInt("neighborhood-level");
            useVariance = sota_dialog.getBoolean("useVariance");
            if (useVariance) pValue = sota_dialog.getFloat("pValue"); else maxTreeDiv = sota_dialog.getFloat("maxTreeDiv");
            runToMaxCycles = sota_dialog.getBoolean("runToMaxCycles");
            setMaxClusterDiv = sota_dialog.getBoolean("setMaxClusterDiv");
            Float temp = sota_dialog.getFloat("maxClusterDiv");
            maxClusterDiv = temp.doubleValue();
            calcFullTreeSampleHCL = false;
            calcClusterHCL = sota_dialog.getBoolean("calcClusterHCL");
            if (migFactor_w <= 0 || migFactor_w <= 0 || migFactor_w <= 0) {
                JOptionPane.showMessageDialog(dataFramework.getFrame(), "Migration weights should be > 0", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            if (pValue <= 0) {
                JOptionPane.showMessageDialog(dataFramework.getFrame(), "p-value should be > 0", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(dataFramework.getFrame(), "Invalid input parameters!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    @Override
    public Vector<String> checkOptions() {
        return null;
    }

    @Override
    public void cluster(TMEVBridge tmev, progressBarGUI progress, dataProcessing dataProcess) {
    }

    @Override
    public String getClusterMethodString() {
        return "SOTA";
    }

    @Override
    public IClusterGUIAutoExec getMevGUI() {
        return null;
    }

    @Override
    public int getNumberOfSteps() {
        return 0;
    }

    @Override
    protected void initOptions() {
        String[] set = new String[4];
        set[0] = "SOTA";
        set[1] = "Start Cell Diversity";
        set[2] = "";
        set[3] = "0.001";
        option.saveValueSet("startCellDiversity", set);
        set[0] = "SOTA";
        set[1] = "Finish Cell Diversity";
        set[2] = "";
        set[3] = "0.1";
        option.saveValueSet("finishCellDiversity", set);
        set[0] = "SOTA";
        set[1] = "Interval";
        set[2] = "";
        set[3] = "0.001";
        option.saveValueSet("SOTAinterval", set);
        saveOptions(option);
    }

    @Override
    public void saveOptions(Option option) {
    }

    public boolean getBoolean(String key) {
        boolean value = false;
        if (key.equals("useVariance")) {
            value = useVariance;
        } else if (key.equals("runToMaxCycles")) {
            value = runToMaxCycles;
        } else if (key.equals("setMaxClusterDiv")) {
            value = setMaxClusterDiv;
        } else if (key.equals("calcClusterHCL")) {
            value = calcClusterHCL;
        } else if (key.equals("calcFullTreeSampleHCL")) {
            value = calcFullTreeSampleHCL;
        } else if (key.equals("clusterGenes")) {
            value = clusterGenes;
        } else if (key.equals("absoluteDistance")) {
            value = absoluteDistance;
        }
        return value;
    }

    public float getFloat(String key) {
        float value = 0;
        if (key.equals("maxClusterDiv")) {
            value = maxClusterDiv.floatValue();
        } else if (key.equals("epochStopCriteria")) {
            value = epochStopCriteria;
        } else if (key.equals("maxTreeDiv")) {
            value = maxTreeDiv;
        } else if (key.equals("migFactor_w")) {
            value = migFactor_w;
        } else if (key.equals("migFactor_p")) {
            value = migFactor_p;
        } else if (key.equals("migFactor_s")) {
            value = migFactor_s;
        } else if (key.equals("pValue")) {
            value = pValue;
        }
        return value;
    }

    public int getInt(String key) {
        int value = 0;
        if (key.equals("maxCycles")) {
            value = maxCycles;
        } else if (key.equals("maxEpochsPerCycle")) {
            value = maxEpochsPerCycle;
        } else if (key.equals("neighborhoodLevel")) {
            value = neighborhoodLevel;
        } else if (key.equals("BCF")) {
            value = BCF;
        }
        return value;
    }

    public float getCellDiversity() {
        return maxClusterDiv.floatValue();
    }

    public void setCellDiversity(Double cellDiv) {
        maxClusterDiv = cellDiv;
    }
}
