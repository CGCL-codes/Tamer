package opennlp.tools.util.eval;

import java.io.IOException;
import opennlp.tools.util.ObjectStream;

/**
 * The {@link Evaluator} is an abstract base class for evaluators.
 *
 * Evaluation results are the arithmetic mean of the
 * scores calculated for each reference sample.
 */
public abstract class Evaluator<T> {

    /**
   * Evaluates the given reference object.
   *
   * The implementation has to update the score after every invocation.
   *
   * @param sample the sample to be evaluated
   */
    public abstract void evaluateSample(T sample);

    /**
   * Reads all sample objects from the stream
   * and evaluates each sample object with
   * {@link #evaluateSample(Object)} method.
   *
   * @param samples the stream of reference which
   * should be evaluated.
   */
    public void evaluate(ObjectStream<T> samples) throws IOException {
        T sample;
        while ((sample = samples.read()) != null) {
            evaluateSample(sample);
        }
    }
}
