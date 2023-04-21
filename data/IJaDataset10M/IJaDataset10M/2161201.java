package com.rapidminer.test.samples;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.MissingIOObjectException;

/**
 * Performs the sample process and checks the output example set.
 * 
 * @author Ingo Mierswa
 *          Exp $
 */
public class ExampleSetSampleTest extends SampleTest {

    private int numberOfExamples;

    private int numberOfAttributes;

    private String[] specialAttributes = null;

    public ExampleSetSampleTest(String file, int numberOfExamples, int numberOfAttributes) {
        this(file, numberOfExamples, numberOfAttributes, null);
    }

    public ExampleSetSampleTest(String file, int numberOfExamples, int numberOfAttributes, String[] specialAttributes) {
        super(file);
        this.numberOfExamples = numberOfExamples;
        this.numberOfAttributes = numberOfAttributes;
        this.specialAttributes = specialAttributes;
    }

    @Override
    public void checkOutput(IOContainer output) throws MissingIOObjectException {
        ExampleSet exampleSet = output.get(ExampleSet.class);
        assertEquals("ExampleSet #examples", numberOfExamples, exampleSet.size(), 0);
        assertEquals("ExampleSet #attributes", numberOfAttributes, exampleSet.getAttributes().size(), 0);
        if (specialAttributes != null) {
            for (int i = 0; i < specialAttributes.length; i++) {
                Attribute special = exampleSet.getAttributes().getSpecial(specialAttributes[i]);
                assertNotNull(specialAttributes[i], special);
            }
        }
    }
}
