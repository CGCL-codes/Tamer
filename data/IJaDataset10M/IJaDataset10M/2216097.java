package configuration.classifiers;

import game.data.GlobalData;
import org.ytoh.configurations.annotations.Property;
import org.ytoh.configurations.ui.CheckBox;

/**
 * Abstract class for config methods
 * Author: cernyjn
 */
public abstract class ClassifierConfigBase implements ClassifierConfig {

    @Property(name = "Model allowed", description = "Include/exclude this object from the construction process")
    @CheckBox
    protected boolean allowed;

    @Property(name = "Maximum learning vectors", description = "Limit the number of learning vectors used.")
    protected int maxLearningVectors;

    @Property(name = "Maximum inputs number", description = "Limit the number of input attributes used in model.")
    protected int maxInputsNumber;

    @Property(name = "Model name")
    protected String name;

    /**
     * Constructor with default values.
     */
    public ClassifierConfigBase() {
        maxLearningVectors = -1;
        maxInputsNumber = -1;
        allowed = true;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    /**
     * Maximum number of learning vectors used.
     */
    public int getMaxLearningVectors() {
        return maxLearningVectors;
    }

    public void setMaxLearningVectors(int numberOfVectors) {
        maxLearningVectors = numberOfVectors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxInputsNumber() {
        return maxInputsNumber;
    }

    public void setMaxInputsNumber(int maxInputsNumber) {
        this.maxInputsNumber = maxInputsNumber;
    }
}
