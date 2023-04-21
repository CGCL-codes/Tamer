package org.wijiscommons.cdcl.gatepoint.integration.semanticregistry;

/**
 * TODO: Add Java Doc
 *
 * @author Pattabi Doraiswamy (http://pattabidoraiswamy.com)
 * @since Jan 19, 2009
 */
public class SemanticRegistryRuntimeException extends RuntimeException {

    /**
     * @param message
     */
    public SemanticRegistryRuntimeException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public SemanticRegistryRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public SemanticRegistryRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
