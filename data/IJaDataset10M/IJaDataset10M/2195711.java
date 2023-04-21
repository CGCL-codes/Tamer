package au.edu.diasb.annotation.danno.validation;

import au.edu.diasb.annotation.danno.model.AnnoteaTypeException;
import au.edu.diasb.annotation.danno.model.RDFObject;

/**
 * A validator validates an RDF object against a schema of some kind.
 *
 * @author scrawley
 *
 */
public interface RDFValidator {

    /**
     * Validate an RDF object against the schema associated with this validator
     * 
     * @param object the RDF object to be validated.
     * @throws AnnoteaTypeException 
     */
    void validate(RDFObject object) throws AnnoteaTypeException;
}
