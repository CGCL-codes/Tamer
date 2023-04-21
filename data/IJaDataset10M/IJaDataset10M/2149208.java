package net.codigofuerte.classify.extractors;

/**
 * Exception Class for custom error handling of training processor for the classifiers.
 *
 * @author Sergio Cruz Moral (scmoral@codigofuerte.net)
 */
public class FeatureExtractorException extends Exception {

    public FeatureExtractorException(Throwable cause) {
        super(cause);
    }

    public FeatureExtractorException(String message, Throwable cause) {
        super(message, cause);
    }

    public FeatureExtractorException(String message) {
        super(message);
    }

    public FeatureExtractorException() {
    }
}
