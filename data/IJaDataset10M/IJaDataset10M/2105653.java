package com.rapidminer.operator.meta;

import java.util.List;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.set.SplittedExampleSet;
import com.rapidminer.operator.Model;
import com.rapidminer.operator.OperatorChain;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.UserError;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.ExampleSetMetaData;
import com.rapidminer.operator.ports.metadata.ExampleSetPassThroughRule;
import com.rapidminer.operator.ports.metadata.ExampleSetPrecondition;
import com.rapidminer.operator.ports.metadata.PassThroughRule;
import com.rapidminer.operator.ports.metadata.SetRelation;
import com.rapidminer.operator.ports.metadata.SubprocessTransformRule;
import com.rapidminer.operator.validation.XValidation;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeCategory;
import com.rapidminer.parameter.ParameterTypeDouble;
import com.rapidminer.parameter.UndefinedParameterError;
import com.rapidminer.tools.Ontology;
import com.rapidminer.tools.RandomGenerator;

/**
 * This operator works similar to the {@link LearningCurveOperator}. 
 * In contrast to this, it just splits the ExampleSet according to the
 * parameter "fraction" and learns a model only on the subset. It can be used, 
 * for example, in conjunction with {@link GridSearchParameterOptimizationOperator} 
 * which sets the fraction parameter to values between 0 and 1. The advantage 
 * is, that this operator can then be used inside of a {@link XValidation},
 * which delivers more stable result estimations.
 * 
 * @author Martin Mauch, Ingo Mierswa
 */
public class PartialExampleSetLearner extends OperatorChain {

    private final InputPort exampleSetInput = getInputPorts().createPort("example set");

    private final OutputPort modelOutput = getOutputPorts().createPort("model");

    private final OutputPort exampleSubsetInnerSource = getSubprocess(0).getInnerSources().createPort("example subset");

    private final InputPort modelInnerSink = getSubprocess(0).getInnerSinks().createPort("model", Model.class);

    /** The parameter name for &quot;The fraction of examples which shall be used.&quot; */
    public static final String PARAMETER_FRACTION = "fraction";

    /** The parameter name for &quot;Defines the sampling type (linear = consecutive subsets, shuffled = random subsets, stratified = random subsets with class distribution kept constant)&quot; */
    public static final String PARAMETER_SAMPLING_TYPE = "sampling_type";

    public PartialExampleSetLearner(OperatorDescription description) {
        super(description, "Learning Process");
        exampleSetInput.addPrecondition(new ExampleSetPrecondition(exampleSetInput, new String[0], Ontology.VALUE_TYPE, Attributes.LABEL_NAME));
        getTransformer().addRule(new ExampleSetPassThroughRule(exampleSetInput, exampleSubsetInnerSource, SetRelation.EQUAL) {

            @Override
            public ExampleSetMetaData modifyExampleSet(ExampleSetMetaData metaData) throws UndefinedParameterError {
                metaData.getNumberOfExamples().multiply(getParameterAsDouble(PARAMETER_FRACTION));
                return super.modifyExampleSet(metaData);
            }
        });
        getTransformer().addRule(new SubprocessTransformRule(getSubprocess(0)));
        getTransformer().addRule(new PassThroughRule(modelInnerSink, modelOutput, false));
    }

    @Override
    public void doWork() throws OperatorException {
        ExampleSet originalExampleSet = exampleSetInput.getData();
        double fraction = getParameterAsDouble(PARAMETER_FRACTION);
        if (fraction < 0 || fraction > 1.0) throw new UserError(this, 207, new Object[] { fraction, "fraction", "Cannot use fractions of less than 0.0 or more than 1.0" });
        SplittedExampleSet splitted = new SplittedExampleSet(originalExampleSet, fraction, getParameterAsInt(PARAMETER_SAMPLING_TYPE), getParameterAsBoolean(RandomGenerator.PARAMETER_USE_LOCAL_RANDOM_SEED), getParameterAsInt(RandomGenerator.PARAMETER_LOCAL_RANDOM_SEED));
        splitted.selectSingleSubset(0);
        exampleSubsetInnerSource.deliver(splitted);
        getSubprocess(0).execute();
        modelOutput.deliver(modelInnerSink.getData());
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        ParameterType type = new ParameterTypeDouble(PARAMETER_FRACTION, "The fraction of examples which shall be used.", 0.0d, 1.0d, 0.05);
        type.setExpert(false);
        types.add(type);
        types.add(new ParameterTypeCategory(PARAMETER_SAMPLING_TYPE, "Defines the sampling type (linear = consecutive subsets, shuffled = random subsets, stratified = random subsets with class distribution kept constant)", SplittedExampleSet.SAMPLING_NAMES, SplittedExampleSet.STRATIFIED_SAMPLING));
        types.addAll(RandomGenerator.getRandomGeneratorParameters(this));
        return types;
    }
}
