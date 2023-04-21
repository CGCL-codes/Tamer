package moa.streams.generators;

import weka.core.DenseInstance;
import weka.core.Instance;
import moa.core.InstancesHeader;
import moa.core.ObjectRepository;
import moa.options.IntOption;
import moa.tasks.TaskMonitor;

/**
 * Stream generator for the problem of predicting one of three waveform types with drift.
 *
 * @author Albert Bifet (abifet at cs dot waikato dot ac dot nz)
 * @version $Revision: 7 $
 */
public class WaveformGeneratorDrift extends WaveformGenerator {

    private static final long serialVersionUID = 1L;

    public IntOption numberAttributesDriftOption = new IntOption("numberAttributesDrift", 'd', "Number of attributes with drift.", 0, 0, TOTAL_ATTRIBUTES_INCLUDING_NOISE);

    protected int[] numberAttribute;

    @Override
    public String getPurposeString() {
        return "Generates a problem of predicting one of three waveform types with drift.";
    }

    @Override
    protected void prepareForUseImpl(TaskMonitor monitor, ObjectRepository repository) {
        super.prepareForUseImpl(monitor, repository);
        int numAtts = this.addNoiseOption.isSet() ? TOTAL_ATTRIBUTES_INCLUDING_NOISE : NUM_BASE_ATTRIBUTES;
        this.numberAttribute = new int[numAtts];
        for (int i = 0; i < numAtts; i++) {
            this.numberAttribute[i] = i;
        }
        int randomInt = this.instanceRandom.nextInt(numAtts);
        int offset = this.instanceRandom.nextInt(numAtts);
        for (int i = 0; i < this.numberAttributesDriftOption.getValue(); i++) {
            this.numberAttribute[(i + randomInt) % numAtts] = (i + offset) % numAtts;
            this.numberAttribute[(i + offset) % numAtts] = (i + randomInt) % numAtts;
        }
    }

    @Override
    public Instance nextInstance() {
        InstancesHeader header = getHeader();
        Instance inst = new DenseInstance(header.numAttributes());
        inst.setDataset(header);
        int waveform = this.instanceRandom.nextInt(NUM_CLASSES);
        int choiceA = 0, choiceB = 0;
        switch(waveform) {
            case 0:
                choiceA = 0;
                choiceB = 1;
                break;
            case 1:
                choiceA = 0;
                choiceB = 2;
                break;
            case 2:
                choiceA = 1;
                choiceB = 2;
                break;
        }
        double multiplierA = this.instanceRandom.nextDouble();
        double multiplierB = 1.0 - multiplierA;
        for (int i = 0; i < NUM_BASE_ATTRIBUTES; i++) {
            inst.setValue(this.numberAttribute[i], (multiplierA * hFunctions[choiceA][i]) + (multiplierB * hFunctions[choiceB][i]) + this.instanceRandom.nextGaussian());
        }
        if (this.addNoiseOption.isSet()) {
            for (int i = NUM_BASE_ATTRIBUTES; i < TOTAL_ATTRIBUTES_INCLUDING_NOISE; i++) {
                inst.setValue(this.numberAttribute[i], this.instanceRandom.nextGaussian());
            }
        }
        inst.setClassValue(waveform);
        return inst;
    }

    @Override
    public void getDescription(StringBuilder sb, int indent) {
    }
}
