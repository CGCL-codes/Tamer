package mulan.classifier.neural;

import java.util.Map;

public interface ModelUpdateRule {

    /**
     * Process the training example and performs a model update when suitable.
     * The decision when to perform model update is carried by the update rule
     * (e.g. when the model response is not within an acceptable boundaries from
     * the true output for given example).
     *
     * @param example the input example
     * @param params the additional parameters for an update.
     * @return the error measure of the model response for given input pattern
     * and specified true output.
     */
    public double process(DataPair example, Map<String, Object> params);
}
