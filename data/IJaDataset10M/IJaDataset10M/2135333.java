package com.rapidminer.operator.preprocessing.sampling;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.AttributeRole;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.set.SimpleExampleSet;
import com.rapidminer.example.table.DataRow;
import com.rapidminer.example.table.ExampleTable;
import com.rapidminer.example.table.ListDataRowReader;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeBoolean;
import com.rapidminer.parameter.ParameterTypeDouble;
import com.rapidminer.parameter.ParameterTypeInt;
import com.rapidminer.parameter.UndefinedParameterError;
import com.rapidminer.parameter.conditions.BooleanParameterCondition;
import com.rapidminer.tools.RandomGenerator;

/**
 * Simple sampling operator. This operator performs a random sampling of a given
 * fraction. For example, if the input example set contains 5000 examples and
 * the sample ratio is set to 0.1, the result will have approximately 500
 * examples.
 * 
 * @author Ingo Mierswa
 */
public class SimpleSampling extends AbstractSamplingOperator {

    /** The parameter name for &quot;The fraction of examples which should be sampled&quot; */
    public static final String PARAMETER_SAMPLE_RATIO = "sample_ratio";

    /** The parameter name for &quot;Use the given random seed instead of global random numbers (-1: use global).&quot; */
    public static final String PARAMETER_LOCAL_RANDOM_SEED = "local_random_seed";

    public static final String PARAMETER_USE_LOCAL_RANDOM_SEED = "use_local_random_seed";

    private double fraction = 0.1d;

    public SimpleSampling(OperatorDescription description) {
        super(description);
    }

    public ExampleSet apply(ExampleSet exampleSet) throws OperatorException {
        this.fraction = getParameterAsDouble(PARAMETER_SAMPLE_RATIO);
        List<DataRow> dataList = new LinkedList<DataRow>();
        Iterator<Example> reader = exampleSet.iterator();
        RandomGenerator random = RandomGenerator.getGlobalRandomGenerator();
        if (getParameterAsBoolean(PARAMETER_USE_LOCAL_RANDOM_SEED)) random = RandomGenerator.getRandomGenerator(getParameterAsInt(PARAMETER_LOCAL_RANDOM_SEED));
        while (reader.hasNext()) {
            Example example = reader.next();
            if (accept(example, random)) {
                dataList.add(example.getDataRow());
            }
            checkForStop();
        }
        List<Attribute> attributes = Arrays.asList(exampleSet.getExampleTable().getAttributes());
        ExampleTable exampleTable = new MemoryExampleTable(attributes, new ListDataRowReader(dataList.iterator()));
        List<Attribute> regularAttributes = new LinkedList<Attribute>();
        for (Attribute attribute : exampleSet.getAttributes()) {
            regularAttributes.add(attribute);
        }
        ExampleSet result = new SimpleExampleSet(exampleTable, regularAttributes);
        Iterator<AttributeRole> special = exampleSet.getAttributes().specialAttributes();
        while (special.hasNext()) {
            AttributeRole role = special.next();
            result.getAttributes().setSpecialAttribute(role.getAttribute(), role.getSpecialName());
        }
        return result;
    }

    protected boolean accept(Example example, RandomGenerator random) throws UndefinedParameterError {
        return random.nextDouble() < fraction;
    }

    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        ParameterType type = new ParameterTypeDouble(PARAMETER_SAMPLE_RATIO, "The fraction of examples which should be sampled", 0.0d, 1.0d, 0.1d);
        types.add(type);
        types.add(new ParameterTypeBoolean(PARAMETER_USE_LOCAL_RANDOM_SEED, "Indicates if a local random seed should be used to draw random numbers. Otherwise the global generator will be used.", false));
        type = new ParameterTypeInt(PARAMETER_LOCAL_RANDOM_SEED, "Use the given random seed instead of global random numbers.", 1, Integer.MAX_VALUE, 1);
        type.registerDependencyCondition(new BooleanParameterCondition(this, PARAMETER_USE_LOCAL_RANDOM_SEED, true, true));
        types.add(type);
        return types;
    }
}
