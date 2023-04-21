package moa.clusterers;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import moa.cluster.Clustering;
import moa.core.InstancesHeader;
import moa.core.Measurement;
import moa.core.ObjectRepository;
import moa.core.StringUtils;
import moa.gui.AWTRenderer;
import moa.options.AbstractOptionHandler;
import moa.options.FlagOption;
import moa.options.IntOption;
import moa.tasks.TaskMonitor;
import weka.core.Instance;
import weka.core.Instances;

public abstract class AbstractClusterer extends AbstractOptionHandler implements Clusterer {

    @Override
    public String getPurposeString() {
        return "MOA Clusterer: " + getClass().getCanonicalName();
    }

    protected InstancesHeader modelContext;

    protected double trainingWeightSeenByModel = 0.0;

    protected int randomSeed = 1;

    protected IntOption randomSeedOption;

    public FlagOption evaluateMicroClusteringOption;

    protected Random clustererRandom;

    protected Clustering clustering;

    public AbstractClusterer() {
        if (isRandomizable()) {
            this.randomSeedOption = new IntOption("randomSeed", 'r', "Seed for random behaviour of the Clusterer.", 1);
        }
        if (implementsMicroClusterer()) {
            this.evaluateMicroClusteringOption = new FlagOption("evaluateMicroClustering", 'M', "Evaluate the underlying microclustering instead of the macro clustering");
        }
    }

    @Override
    public void prepareForUseImpl(TaskMonitor monitor, ObjectRepository repository) {
        if (this.randomSeedOption != null) {
            this.randomSeed = this.randomSeedOption.getValue();
        }
        if (!trainingHasStarted()) {
            resetLearning();
        }
        clustering = new Clustering();
    }

    public void setModelContext(InstancesHeader ih) {
        if ((ih != null) && (ih.classIndex() < 0)) {
            throw new IllegalArgumentException("Context for a Clusterer must include a class to learn");
        }
        if (trainingHasStarted() && (this.modelContext != null) && ((ih == null) || !contextIsCompatible(this.modelContext, ih))) {
            throw new IllegalArgumentException("New context is not compatible with existing model");
        }
        this.modelContext = ih;
    }

    public InstancesHeader getModelContext() {
        return this.modelContext;
    }

    public void setRandomSeed(int s) {
        this.randomSeed = s;
        if (this.randomSeedOption != null) {
            this.randomSeedOption.setValue(s);
        }
    }

    public boolean trainingHasStarted() {
        return this.trainingWeightSeenByModel > 0.0;
    }

    public double trainingWeightSeenByModel() {
        return this.trainingWeightSeenByModel;
    }

    public void resetLearning() {
        this.trainingWeightSeenByModel = 0.0;
        if (isRandomizable()) {
            this.clustererRandom = new Random(this.randomSeed);
        }
        resetLearningImpl();
    }

    public void trainOnInstance(Instance inst) {
        if (inst.weight() > 0.0) {
            this.trainingWeightSeenByModel += inst.weight();
            trainOnInstanceImpl(inst);
        }
    }

    public Measurement[] getModelMeasurements() {
        List<Measurement> measurementList = new LinkedList<Measurement>();
        measurementList.add(new Measurement("model training instances", trainingWeightSeenByModel()));
        measurementList.add(new Measurement("model serialized size (bytes)", measureByteSize()));
        Measurement[] modelMeasurements = getModelMeasurementsImpl();
        if (modelMeasurements != null) {
            for (Measurement measurement : modelMeasurements) {
                measurementList.add(measurement);
            }
        }
        Clusterer[] subModels = getSubClusterers();
        if ((subModels != null) && (subModels.length > 0)) {
            List<Measurement[]> subMeasurements = new LinkedList<Measurement[]>();
            for (Clusterer subModel : subModels) {
                if (subModel != null) {
                    subMeasurements.add(subModel.getModelMeasurements());
                }
            }
            Measurement[] avgMeasurements = Measurement.averageMeasurements(subMeasurements.toArray(new Measurement[subMeasurements.size()][]));
            for (Measurement measurement : avgMeasurements) {
                measurementList.add(measurement);
            }
        }
        return measurementList.toArray(new Measurement[measurementList.size()]);
    }

    public void getDescription(StringBuilder out, int indent) {
        StringUtils.appendIndented(out, indent, "Model type: ");
        out.append(this.getClass().getName());
        StringUtils.appendNewline(out);
        Measurement.getMeasurementsDescription(getModelMeasurements(), out, indent);
        StringUtils.appendNewlineIndented(out, indent, "Model description:");
        StringUtils.appendNewline(out);
        if (trainingHasStarted()) {
            getModelDescription(out, indent);
        } else {
            StringUtils.appendIndented(out, indent, "Model has not been trained.");
        }
    }

    public Clusterer[] getSubClusterers() {
        return null;
    }

    @Override
    public Clusterer copy() {
        return (Clusterer) super.copy();
    }

    public String getClassNameString() {
        return InstancesHeader.getClassNameString(this.modelContext);
    }

    public String getClassLabelString(int classLabelIndex) {
        return InstancesHeader.getClassLabelString(this.modelContext, classLabelIndex);
    }

    public String getAttributeNameString(int attIndex) {
        return InstancesHeader.getAttributeNameString(this.modelContext, attIndex);
    }

    public String getNominalValueString(int attIndex, int valIndex) {
        return InstancesHeader.getNominalValueString(this.modelContext, attIndex, valIndex);
    }

    public static boolean contextIsCompatible(InstancesHeader originalContext, InstancesHeader newContext) {
        if (newContext.numClasses() < originalContext.numClasses()) {
            return false;
        }
        if (newContext.numAttributes() < originalContext.numAttributes()) {
            return false;
        }
        int oPos = 0;
        int nPos = 0;
        while (oPos < originalContext.numAttributes()) {
            if (oPos == originalContext.classIndex()) {
                oPos++;
                if (!(oPos < originalContext.numAttributes())) {
                    break;
                }
            }
            if (nPos == newContext.classIndex()) {
                nPos++;
            }
            if (originalContext.attribute(oPos).isNominal()) {
                if (!newContext.attribute(nPos).isNominal()) {
                    return false;
                }
                if (newContext.attribute(nPos).numValues() < originalContext.attribute(oPos).numValues()) {
                    return false;
                }
            } else {
                assert (originalContext.attribute(oPos).isNumeric());
                if (!newContext.attribute(nPos).isNumeric()) {
                    return false;
                }
            }
            oPos++;
            nPos++;
        }
        return true;
    }

    public AWTRenderer getAWTRenderer() {
        return null;
    }

    public abstract void resetLearningImpl();

    public abstract void trainOnInstanceImpl(Instance inst);

    protected abstract Measurement[] getModelMeasurementsImpl();

    public abstract void getModelDescription(StringBuilder out, int indent);

    protected static int modelAttIndexToInstanceAttIndex(int index, Instance inst) {
        return inst.classIndex() > index ? index : index + 1;
    }

    protected static int modelAttIndexToInstanceAttIndex(int index, Instances insts) {
        return insts.classIndex() > index ? index : index + 1;
    }

    public boolean implementsMicroClusterer() {
        return false;
    }

    public Clustering getMicroClusteringResult() {
        return null;
    }

    ;
}
