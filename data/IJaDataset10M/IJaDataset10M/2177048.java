package hu.gbalage.owlforms.api;

import java.net.URI;
import java.util.Set;

/**
 * A Field on the form
 * @author Grill Balazs (balage.g@gmail.com)
 *
 */
public interface Field extends Entity {

    /**
	 * @return the form which contains this Field
	 */
    public Form getForm();

    /**
	 * @return true only if this field contains primitive
	 * typed data
	 */
    public boolean isPrimitive();

    /**
	 * @return true only if this field contains a complex data structure
	 * and is a ComplexField
	 */
    public boolean isComplex();

    /**
	 * @return true if this field is derivative, therefore the user shouldn't give values
	 * to this field because values of this field is calculated outside of the scope of 
	 * OWLForms API. 
	 */
    public boolean isDerivative();

    /**
	 * @return the cardinality of the field
	 */
    public FieldCardinality getCardinality();

    /**
	 * @return the set of the types which are accepted by
	 * this field
	 */
    public Set<URI> getAcceptedTypes();

    /**
	 * @return this field as a ComplexField. Returns a valid value only if
	 * isComplex() is true. Returns null otherwise.
	 */
    public ComplexField asComplexField();
}
