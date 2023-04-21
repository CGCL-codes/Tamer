package de.tuberlin.cs.cis.ocl.model.check;

/**
 * Describes an enumeration type of the model. An enumeration type is 
 * a data type whose values are the elements of a finite set of
 * enumerators. The enumeration type is specified by defining the
 * ordered set of enumeration labels.
 * 
 * @author fchabar
 *
 */
public interface EnumerationTypeDescriptor extends ClassifierDescriptor {

    /**
	 * Returns the labels of the enumeration type. The label elements 
	 * must be unique. 
	 * @return the labels of the enumeration type.
	 */
    String[] getLabels();
}
