package weka.filters.supervised.instance;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.SyntheticInstance;
import weka.core.TechnicalInformation;
import weka.core.TechnicalInformationHandler;
import weka.core.Utils;
import weka.core.Capabilities.Capability;
import weka.core.TechnicalInformation.Field;
import weka.core.TechnicalInformation.Type;
import weka.filters.SimpleBatchFilter;
import weka.filters.SupervisedFilter;

public class SamplingHypercubes extends SimpleBatchFilter implements SupervisedFilter, TechnicalInformationHandler {

    private int attrCount;

    private double[] max;

    private double[] min;

    /**
	 * 
	 */
    private static final long serialVersionUID = -2703039882958523000L;

    /** Number of references */
    private double U_percentToUndersample;

    private int w_minorityClassIndex = 0;

    /** Random number seed for reduced-error pruning. */
    private long m_Seed = -1;

    private int percentToSample = 50;

    public SamplingHypercubes() {
    }

    /**
	 * Returns the description of the classifier.
	 * 
	 * @return description of the KNN class.
	 */
    public String globalInfo() {
        return "Filter for generation of over sampling guided by a genetic algorithm " + " in order to improve the classification " + "with imbalanced datasets.\n\n" + "For more information see:\n\n" + getTechnicalInformation().toString();
    }

    protected Instances determineOutputFormat(Instances inputFormat) throws Exception {
        return inputFormat;
    }

    public Instances process(Instances data) throws Exception {
        attrCount = data.numAttributes();
        min = new double[attrCount];
        max = new double[attrCount];
        calculateDataStatistics(data, min, max);
        drawCube2(data, 0, new int[attrCount]);
        return data;
    }

    public void drawCube(Instances data, int level) {
        int classIndex = data.classIndex();
        double[][] extremes = new double[][] { min, max };
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; ++j) {
                for (int k = 0; k < 2; ++k) {
                    for (int l = 0; l < 2; ++l) {
                        SyntheticInstance instance = new SyntheticInstance(1, new double[] { extremes[i][0], extremes[j][1], extremes[k][2], extremes[l][3], 0 });
                        instance.setDataset(data);
                        data.add(instance);
                    }
                }
            }
        }
    }

    public void drawCube2(Instances data, int level, int pointers[]) {
        double[][] extremes = new double[][] { min, max };
        for (int i = 0; i < 2; i++) {
            if (level < attrCount) {
                pointers[level] = i;
                drawCube2(data, level + 1, pointers);
            } else {
                int classIndex = data.classIndex();
                double[] values = new double[attrCount];
                for (int j = 0; j < attrCount; j++) {
                    if (j == classIndex) {
                        continue;
                    }
                    values[j] = extremes[pointers[j]][j];
                }
                SyntheticInstance instance = new SyntheticInstance(1, values);
                instance.setDataset(data);
                data.add(instance);
            }
        }
    }

    public void drawCircle2(Instances data, double radiusStep) {
        double maxRadius = 5;
        double radius = radiusStep;
        double angle = 0.0;
        double angle_stepsize = 0.1;
        int numAttributes = data.numAttributes();
        while (angle < 2 * Math.PI) {
            double values[] = new double[numAttributes];
            double values2[] = new double[numAttributes];
            for (int i = 0; i < numAttributes; ++i) {
                if (i == data.classIndex()) {
                    continue;
                }
                double cos;
                double sen;
                cos = radius * Math.cos(angle);
                sen = radius * Math.sin(angle);
                if (i % 2 == 0) {
                    values[i] = cos;
                } else {
                    values[i] = sen;
                }
                if (i < numAttributes / 2) values2[i] = cos; else values2[i] = sen;
            }
            angle += angle_stepsize;
            Instance instance = new Instance(1, values);
            instance.setDataset(data);
            instance.setClassValue(0);
            data.add(instance);
            instance = new Instance(1, values2);
            instance.setDataset(data);
            instance.setClassValue(0);
            data.add(instance);
        }
    }

    public Capabilities getCapabilities() {
        Capabilities result = super.getCapabilities();
        result.enable(Capability.NOMINAL_ATTRIBUTES);
        result.enable(Capability.NUMERIC_ATTRIBUTES);
        result.enable(Capability.DATE_ATTRIBUTES);
        result.enable(Capability.RELATIONAL_ATTRIBUTES);
        result.enable(Capability.NOMINAL_CLASS);
        result.enable(Capability.MISSING_CLASS_VALUES);
        return result;
    }

    public TechnicalInformation getTechnicalInformation() {
        TechnicalInformation result;
        result = new TechnicalInformation(Type.INPROCEEDINGS);
        result.setValue(Field.AUTHOR, "Nitesh V. Chawla, Kevin W. Bowyer, Lawrence O. Hall, W. Philip Kegelmeyer." + "\nImplemented in Weka by Marcelo Beckmann - Federal University of Rio de Janeiro - COPPE/PEC");
        result.setValue(Field.TITLE, "\nSMOTE: Synthetic Minority Over-sampling TEchnique");
        result.setValue(Field.BOOKTITLE, "Journal of Artificial Inteligence Research 16");
        result.setValue(Field.EDITOR, "AI Access Foundation and Morgan Kaufmann");
        result.setValue(Field.YEAR, "2002");
        result.setValue(Field.PAGES, "321-357");
        return result;
    }

    public String percentToUndersampleTipText() {
        return "Percent of instances to be undersampled.";
    }

    public String kTipText() {
        return "Number of Nearest Neighbors.";
    }

    public String amountOfSMOTETipText() {
        return "Amount of SMOTEFilter N% to be created. Use multiples of 100.";
    }

    public String minorityClassTipText() {
        return "Index of minority class, starting with 0.";
    }

    public Enumeration listOptions() {
        Vector result = new Vector();
        result.addElement(new Option("\tNumber of Nearest Neighbors (default 2).", "K", 0, "-K <number of references>"));
        result.addElement(new Option("\tAmount of SMOTEFilter N% to be created (default 100). Use multiples of 100.", "N", 0, "-N <percent of SMOTEd instances to be created>"));
        result.addElement(new Option("\tIndex of minority class, starting with 0 (default 0).", "w", 0, "-w <Index of minority class>"));
        return result.elements();
    }

    public void setOptions(String[] options) throws Exception {
        String option = Utils.getOption('k', options);
        option = Utils.getOption('N', options);
        if (option.length() != 0) percentToSample = Integer.parseInt(option); else percentToSample = 100;
        option = Utils.getOption('w', options);
        if (option.length() != 0) w_minorityClassIndex = Integer.parseInt(option); else w_minorityClassIndex = 10;
        option = Utils.getOption('U', options);
        if (option.length() != 0) U_percentToUndersample = Integer.parseInt(option); else U_percentToUndersample = 10;
        option = Utils.getOption('E', options);
        option = Utils.getOption('A', options);
        option = Utils.getOption('X', options);
        String seedString = Utils.getOption('Q', options);
        if (seedString.length() != 0) {
            m_Seed = Integer.parseInt(seedString);
        } else {
            m_Seed = 1;
        }
    }

    /**
	 * Gets the current option settings for the OptionHandler.
	 * 
	 * @return the list of current option settings as an array of strings
	 */
    public String[] getOptions() {
        Vector result;
        result = new Vector();
        result.add("-N");
        result.add("" + percentToSample);
        result.add("-w");
        result.add("" + w_minorityClassIndex);
        result.add("-U");
        result.add("" + U_percentToUndersample);
        return (String[]) result.toArray(new String[result.size()]);
    }

    public int getMinorityClassIndex() {
        return w_minorityClassIndex;
    }

    public void setMinorityClassIndex(int w) {
        this.w_minorityClassIndex = w;
    }

    /**
	 * Main method for testing this class.
	 * 
	 * @param argv
	 *            should contain arguments to the filter: use -h for help
	 */
    public static void main(String[] argv) {
        runFilter(new SamplingRangeGA(), argv);
    }

    public String getRevision() {
        return "No revision";
    }

    /**
	 * Get the value of Seed.
	 * 
	 * @return Value of Seed.
	 */
    public long getSeed() {
        return m_Seed;
    }

    /**
	 * Set the value of Seed.
	 * 
	 * @param newSeed
	 *            Value to assign to Seed.
	 */
    public void setSeed(long newSeed) {
        m_Seed = newSeed;
    }

    public int getPercentToSample() {
        return percentToSample;
    }

    public void setPercentToSample(int percentToSample) {
        this.percentToSample = percentToSample;
    }

    public void calculateDataStatistics(Instances input, double min[], double max[]) {
        int classIndex = input.classIndex();
        for (int i = 0; i < attrCount; i++) {
            if (classIndex == i) {
                continue;
            }
            if (input.attribute(i).isNumeric()) {
                min[i] = Double.NaN;
            }
        }
        int count = input.numInstances();
        attrCount = input.numAttributes();
        for (int j = 0; j < count; j++) {
            Instance instance = input.instance(j);
            double[] value = instance.toDoubleArray();
            for (int i = 0; i < attrCount; i++) {
                if (classIndex == i) {
                    continue;
                }
                if (input.attribute(i).isNumeric()) {
                    if (!Instance.isMissingValue(value[i])) {
                        if (Double.isNaN(min[i])) {
                            min[i] = max[i] = value[i];
                        } else {
                            if (value[i] < min[i]) {
                                min[i] = value[i];
                            }
                            if (value[i] > max[i]) {
                                max[i] = value[i];
                            }
                        }
                    }
                } else if (input.attribute(i).isNominal()) {
                } else {
                    throw new RuntimeException("invalid attribute type " + i);
                }
            }
        }
    }
}
