package de.fraunhofer.isst.eastadl.datatypes;

import de.fraunhofer.isst.eastadl.elements.EAElement;

/**
 * <!-- BEGIN-EAST-ADL2-SPEC -->
 * <strong>EnumerationLiteral</strong> (from Datatypes)
 * <p>
 * <strong>Generalizations</strong>
 * <br> EAElement (from Elements)
 * <p>
 * <strong>Description</strong>
 * <br> An enumeration literal is a user-defined data value for an enumeration.
 * <p>
 * <strong>Attributes</strong>
 * <br> No additional attributes
 * <p>
 * <strong>Associations</strong>
 * <br> No additional associations
 * <p>
 * <strong>Constraints</strong>
 * <br> No additional constraints
 * <p>
 * <strong>Semantics</strong>
 * <br> An EnumerationLiteral defines an element of the run-time extension of an enumeration data type.
 *      An EnumerationLiteral has a name (inherited from EAElement) that can be used to identify it within
 *      its Enumeration datatype. The EnumerationLiteral name is scoped and must therefore be unique
 *      within its Enumeration. EnumerationLiteral names are not global and must be qualified for general
 *      use. The run-time values corresponding to EnumerationLiterals can be compared for equality.
 * <!-- END-EAST-ADL2-SPEC -->
 * 
 * @author dprenzel
 *
 * @model
 */
public interface EnumerationLiteral extends EAElement {
}
