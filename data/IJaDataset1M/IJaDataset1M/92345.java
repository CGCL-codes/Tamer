package com.rapidminer.operator.features.construction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Tools;
import com.rapidminer.example.set.AttributeWeightedExampleSet;
import com.rapidminer.generator.FeatureGenerator;
import com.rapidminer.tools.RandomGenerator;

/**
 * This PopulationOperator generates new attributes in an individual's example
 * table. Given a generation probability
 * <tt>pGenerate</p> and the maximal number of new attributes it generates on average
 *  <tt>pGenerate</tt> * <tt>numberOfNewAttributes</tt> new attributes using generators from the list
 *  <tt>generatorList</tt> <br/>
 *
 *  This operator can never handle value series but only single attributes.
 *
 *  @author Ingo Mierswa
 */
public class AttributeGenerator extends ExampleSetBasedIndividualOperator {

    /**
	 * Probability to generate a new attribute.
	 */
    private double pGenerate;

    /**
	 * Maximal number of newly generated attributes.
	 */
    private int numberOfNewAttributes;

    /**
	 * The total maximum number of new attributes.
	 */
    private int totalMaxNumberOfAttributes;

    /**
	 * A list of applicable generators.
	 */
    private List<FeatureGenerator> generatorList;

    private RandomGenerator random;

    /**
	 * Creates a new <tt>AttributeGenerator</tt> with given parameters.
	 */
    public AttributeGenerator(double pGenerate, int numberOfNewAttributes, int totalMaxNumberOfAttributes, List<FeatureGenerator> generatorList, RandomGenerator random) {
        this.pGenerate = pGenerate;
        this.numberOfNewAttributes = numberOfNewAttributes;
        this.totalMaxNumberOfAttributes = totalMaxNumberOfAttributes;
        this.generatorList = generatorList;
        this.random = random;
    }

    /**
	 * Determines the applicable generators and generates up to
	 * <tt>numberOfNewAttributes</tt> new attributes.
	 */
    @Override
    public List<ExampleSetBasedIndividual> operate(ExampleSetBasedIndividual individual) throws Exception {
        AttributeWeightedExampleSet exampleSet = individual.getExampleSet();
        ArrayList<FeatureGenerator> selectedGeneratorList = new ArrayList<FeatureGenerator>();
        if ((totalMaxNumberOfAttributes < 0) || (exampleSet.getAttributes().size() < totalMaxNumberOfAttributes)) {
            for (int h = 0; h < numberOfNewAttributes; h++) {
                if (random.nextDouble() < pGenerate) {
                    FeatureGenerator generator = FeatureGenerator.selectGenerator(exampleSet, generatorList, new String[0], random);
                    if (generator != null) {
                        generator = generator.newInstance();
                        Attribute[] args = Tools.getRandomCompatibleAttributes(exampleSet, generator, new String[0], random);
                        generator.setArguments(args);
                        selectedGeneratorList.add(generator);
                    }
                }
            }
            if (selectedGeneratorList.size() > 0) {
                List<Attribute> newAttributes = FeatureGenerator.generateAll(exampleSet.getExampleTable(), selectedGeneratorList);
                for (Attribute newAttribute : newAttributes) exampleSet.getAttributes().addRegular(newAttribute);
            }
        }
        List<ExampleSetBasedIndividual> result = new LinkedList<ExampleSetBasedIndividual>();
        result.add(new ExampleSetBasedIndividual(exampleSet));
        return result;
    }
}
