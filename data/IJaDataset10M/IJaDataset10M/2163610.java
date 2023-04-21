package com.rapidminer.operator.generator;

import java.util.HashSet;
import java.util.Set;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.operator.ports.metadata.AttributeMetaData;
import com.rapidminer.operator.ports.metadata.ExampleSetMetaData;
import com.rapidminer.operator.ports.metadata.SetRelation;
import com.rapidminer.tools.Ontology;
import com.rapidminer.tools.RandomGenerator;
import com.rapidminer.tools.math.container.Range;

/**
 * A target function for classification labels, i.e. non-continous nominal
 * labels.
 * 
 * @author Ingo Mierswa
 */
public abstract class ClassificationFunction implements TargetFunction {

    protected double lower = -10.0d;

    protected double upper = 10.0d;

    private int numberOfExamples = 0;

    private int numberOfAttributes = 0;

    Attribute label = AttributeFactory.createAttribute("label", Ontology.BINOMINAL);

    public ClassificationFunction() {
        label.getMapping().mapString("negative");
        label.getMapping().mapString("positive");
    }

    /** Does nothing. */
    public void init(RandomGenerator random) {
    }

    public void setTotalNumberOfExamples(int number) {
        numberOfExamples = number;
    }

    public int getTotalNumberOfExamples() {
        return numberOfExamples;
    }

    public void setTotalNumberOfAttributes(int number) {
        numberOfAttributes = number;
    }

    public int getTotalNumberOfAttributes() {
        return numberOfAttributes;
    }

    public void setLowerArgumentBound(double lower) {
        this.lower = lower;
    }

    public void setUpperArgumentBound(double upper) {
        this.upper = upper;
    }

    public Attribute getLabel() {
        return label;
    }

    public double[] createArguments(int dimension, RandomGenerator random) {
        double[] args = new double[dimension];
        for (int i = 0; i < args.length; i++) args[i] = random.nextDoubleInRange(lower, upper);
        return args;
    }

    @Override
    public ExampleSetMetaData getGeneratedMetaData() {
        ExampleSetMetaData emd = new ExampleSetMetaData();
        AttributeMetaData amd = new AttributeMetaData("label", Ontology.BINOMINAL, Attributes.LABEL_NAME);
        Set<String> valueSet = new HashSet<String>();
        valueSet.add("negative");
        valueSet.add("positive");
        amd.setValueSet(valueSet, SetRelation.EQUAL);
        emd.addAttribute(amd);
        for (int i = 0; i < numberOfAttributes; i++) {
            amd = new AttributeMetaData("att" + (i + 1), Ontology.REAL);
            amd.setValueRange(new Range(lower, upper), SetRelation.EQUAL);
            emd.addAttribute(amd);
        }
        emd.setNumberOfExamples(numberOfExamples);
        return emd;
    }
}
