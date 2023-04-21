package org.openscience.jmol;

import java.util.*;

/**
 * A molecular vibration composed of a set of atom vectors.
 * The atom vectors represent forces acting on the atoms. They
 * are specified by double[3] arrays containing the components
 * of the vector.
 *
 * @author Bradley A. Smith (yeldar@home.com)
 * @version 1.0
 */
public class Vibration {

    /**
	 * Create a vibration identified by the label.
	 *
	 * @param label identification for this vibration
	 */
    public Vibration(String label) {
        this.label = label;
    }

    /**
	 * Gets the label identifying this vibration.
	 *
	 * @return label identifying this vibration
	 */
    public String getLabel() {
        return label;
    }

    /**
	 * Adds a atom vector to the vibration.
	 *
	 * @param av atom vector in double[3] array
	 */
    public void addAtomVector(double[] av) {
        Object d3 = (Object) av;
        atomVectors.addElement(d3);
    }

    /**
	 * Gets a atom vector at index given.
	 *
	 * @param index number for the atom vector to be returned
	 * @return atom vector in double[3] array
	 */
    public double[] getAtomVector(int index) {
        return (double[]) atomVectors.elementAt(index);
    }

    /**
	 * Gets the number of atom vectors in the vibration. 
	 *
	 * @return number of atom vectors
	 */
    public int getNumberAtomVectors() {
        return atomVectors.size();
    }

    /**
	 * Returns an Enumeration of the atom vectors of this vibration.
	 *
	 * @return an enumeration of the atom vectors of this vibration
	 */
    public Enumeration getAtomVectors() {
        return atomVectors.elements();
    }

    /**
	 * Removes all atom vectors from this vibration.
	 */
    public void removeAtomVectors() {
        atomVectors.removeAllElements();
    }

    /**
	 * Label identifying this vibration. For example, the
	 * frequency in reciprocal centimeters could be used.
	 */
    private String label;

    /**
	 * List of atom vectors of type double[3]
	 */
    private Vector atomVectors = new Vector();
}
