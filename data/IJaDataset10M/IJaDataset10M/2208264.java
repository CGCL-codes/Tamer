package edu.petrnalevka.relaxed.storage;

import edu.petrnalevka.relaxed.error.AbstractValidatorErrorHandler;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;

/**
 * This class is an implementation of the AbstractReuseableDocumentStorage, which
 * is storing the validated document in the memory.
 */
public class MemoryReuseableDocumentStorage extends AbstractReuseableDocumentStorage {

    String storage;

    public MemoryReuseableDocumentStorage(Properties filterProperties, AbstractValidatorErrorHandler errorHandler) throws Exception {
        super(filterProperties, errorHandler);
    }

    public void storeImpl(String content) {
        this.storage = content;
    }

    public Reader getReader() {
        return new StringReader(storage);
    }
}
