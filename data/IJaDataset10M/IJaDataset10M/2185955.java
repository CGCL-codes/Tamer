package edu.petrnalevka.relaxed;

import edu.petrnalevka.relaxed.error.AbstractValidatorErrorHandler;
import edu.petrnalevka.relaxed.storage.AbstractReuseableDocumentStorage;
import edu.petrnalevka.relaxed.storage.MemoryReuseableDocumentStorage;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

/**
 * This class provides an unified validation interface to a set of validator implementations.
 * validate() method may be called just onece and si propagated to all contained validators.
 * Same ErrorHandler instance is provided to all underlaying validators, so that messages
 * may be collected from all of them. Before validation is processed, validated document is stored
 * in some AbstractReuseableDocumentStorage implementation for reuseability reasons.
 * This class handles also documents content filtering using a configurable filter chain.
 */
public class ValidatorCollector {

    /**
     * Set of validators.
     */
    Collection validators;

    /**
     * Creates new ValidatorCollector.
     * @param validators
     */
    public ValidatorCollector(Collection validators) {
        this.validators = validators;
    }

    /**
     * This is a unified validation interface method.
     * @param option User validation option which is specified in publicId mapping.
     * @param inputStream InputStream of the validated document.
     * @param errorHandler ErrorHandler which is gathering all messages from all validators.
     * @throws Exception Any exception which may occured during the whole validation process
     */
    public Reader validate(String option, InputStream inputStream, Properties filterProperties, AbstractValidatorErrorHandler errorHandler) throws Exception {
        AbstractReuseableDocumentStorage storage = new MemoryReuseableDocumentStorage(filterProperties, errorHandler);
        storage.store(inputStream);
        for (Iterator iterator = validators.iterator(); iterator.hasNext(); ) {
            AbstractValidator validator = (AbstractValidator) iterator.next();
            validator.validate(option, storage, errorHandler);
        }
        return storage.getOriginalReader();
    }

    public void destroy() {
        for (Iterator iterator = validators.iterator(); iterator.hasNext(); ) {
            AbstractValidator validator = (AbstractValidator) iterator.next();
            validator.destroy();
        }
    }
}
