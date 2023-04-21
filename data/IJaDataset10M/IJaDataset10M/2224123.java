package net.sf.orcc.df;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * This class defines an action tag as a list of strings.
 * 
 * @author Matthieu Wipliez
 * @model
 */
public interface Tag extends EObject {

    /**
	 * Adds an identifier to this tag.
	 * 
	 * @param identifier
	 *            an identifier
	 */
    void add(String identifier);

    /**
	 * Returns the identifier at the given index.
	 * 
	 * @param index
	 *            index of the identifier to return
	 * @return the identifier at the given index
	 */
    String get(int index);

    /**
	 * Returns identifiers of the tag.
	 * 
	 * @return a list of identifier
	 * @model dataType="org.eclipse.emf.ecore.EString" upper="-1"
	 */
    EList<String> getIdentifiers();

    /**
	 * Returns true if this tag is empty.
	 * 
	 * @return true if this tag is empty
	 */
    boolean isEmpty();

    /**
	 * Returns the number of identifiers in this tag.
	 * 
	 * @return the number of identifiers in this tag
	 */
    int size();
}
