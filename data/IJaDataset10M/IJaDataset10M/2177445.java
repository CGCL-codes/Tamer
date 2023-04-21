package com.rapidminer.operator.preprocessing.ie.features.tools;

import java.util.Iterator;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.OperatorChain;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.SubprocessTransformRule;
import com.rapidminer.tools.LogService;

/**
 * This operator is a chain for all preprocessing operators. This chain is used
 * to iterate over the exampleset and to use every operator in this chain for
 * every example currently processing.
 * 
 * @author Felix Jungermann
 * @version $Id
 * 
 */
public class PreprocessingChain extends OperatorChain {

    private InputPort exampleSetInput = getInputPorts().createPort("example set input");

    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set output");

    private InputPort innerExampleSink = getSubprocess(0).getInnerSinks().createPort("example set sink");

    private OutputPort innerExampleSource = getSubprocess(0).getInnerSources().createPort("example set source");

    public PreprocessingChain(OperatorDescription description) {
        super(description, "Preprocess Operators");
        getTransformer().addGenerationRule(innerExampleSource, ExampleIteration.class);
        getTransformer().addRule(new SubprocessTransformRule(getSubprocess(0)));
        getTransformer().addPassThroughRule(exampleSetInput, exampleSetOutput);
    }

    @Override
    public void doWork() throws OperatorException {
        try {
            ExampleSet eSet = exampleSetInput.getData();
            Iterator<Example> iter = eSet.iterator();
            int size = eSet.size();
            int counter = 0;
            int tenCounter = 1;
            ExampleIteration exIter = new ExampleIteration(eSet, iter);
            while (exIter.hasNext()) {
                exIter.next();
                innerExampleSource.deliver(exIter);
                getSubprocess(0).execute();
                exIter = innerExampleSink.getData();
                counter++;
                if ((new Double(counter) / new Double(size)) >= 0.1) {
                    LogService.getGlobal().logNote(tenCounter++ + "0% of Preprocessing done!");
                    counter = 0;
                }
            }
            if (tenCounter != 11) {
                LogService.getGlobal().logNote("100% of Preprocessing done!");
            }
            ExampleSet temp = exIter.getExampleSet();
            temp.recalculateAllAttributeStatistics();
            exampleSetOutput.deliver(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
