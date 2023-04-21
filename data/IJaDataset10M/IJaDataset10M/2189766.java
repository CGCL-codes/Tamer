package uk.ac.ebi.rhea.ws.core.exception;

import uk.ac.ebi.rhea.ws.core.response.RheaWsConstant;

/**
 * An exception that is thrown to indicate the mapping from Java object
 * to Xml failed. The causes can be the unavailability or wrong version of the schema or
 * the temporary file to write to output is not found.
 *
 * @author <a href="mailto:hongcao@ebi.ac.uk">Hong Cao</a>
 * @since 19-10-2010
 */
public class ObjectToSchemaMappingException extends Exception {

    public ObjectToSchemaMappingException(Throwable cause) {
        super(RheaWsConstant.MAPPING_ERROR_MSG, cause);
    }

    public ObjectToSchemaMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectToSchemaMappingException() {
    }

    public ObjectToSchemaMappingException(String message) {
        super(message);
    }
}
