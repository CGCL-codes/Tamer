package com.rapidminer.test.samples;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.MissingIOObjectException;

/**
 * Class to test the parameter-iteration. 
 * The experiment iterates over an example-set-generator and its parameters 
 * "target-funtions" and "number of examples". This class tests if the number
 * of generated example-sets and its number of examples are right.
 *
 * @author Marcin Skirzynski
 */
public class ParameterIterationDataSampleTest extends OperatorDataSampleTest {

    private int number_of_examples;

    private int[] values;

    private double[] first_value_in_the_examplesets;

    private String attributeName;

    public ParameterIterationDataSampleTest(String file, int number_of_examples, int[] values, double[] first_value_in_the_examplesets) {
        super(file);
        this.number_of_examples = number_of_examples;
        this.values = values;
        this.first_value_in_the_examplesets = first_value_in_the_examplesets;
    }

    @Override
    public void checkOutput(IOContainer output) throws MissingIOObjectException {
        attributeName = "label";
        assertEquals(number_of_examples, output.size());
        for (int i = 0; i < output.size(); i++) {
            ExampleSet exampleSet = output.get(ExampleSet.class, i);
            assertEquals(exampleSet.size(), values[i]);
            Attribute attribute = exampleSet.getAttributes().get(attributeName);
            assertEquals(exampleSet.getExample(0).getValue(attribute), first_value_in_the_examplesets[i]);
        }
    }
}
