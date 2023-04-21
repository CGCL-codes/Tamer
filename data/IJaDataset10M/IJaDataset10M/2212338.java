package org.openscience.cdk;

import java.util.Enumeration;

/**
 *  An Enumeration of the Atoms in a particular AtomContainer.
 *  The typical use is:
 *
 *  <pre>
 *  AtomEnumeration atoms = ((Molecule)molecule).atoms();
 *  while (atoms.hasMoreElements()) {
 *      Atom a = (Atom)atoms.nextElement();
 *      // do something with atom
 *  }
 *  </pre>
 *
 *  <p>The Enumeration does not clone the AtomContainer from which
 *  it is constructed, which might lead to errors.
 *
 * @cdk.module data
 *
 * @author     steinbeck
 * @cdk.created    2000-10-02
 */
public class AtomEnumeration implements java.io.Serializable, Cloneable, Enumeration {

    /**
     * Determines if a de-serialized object is compatible with this class.
     *
     * This value must only be changed if and only if the new version
     * of this class is imcompatible with the old version. See Sun docs
     * for <a href=http://java.sun.com/products/jdk/1.1/docs/guide
     * /serialization/spec/version.doc.html>details</a>.
	 */
    private static final long serialVersionUID = -1792810428672771080L;

    /** Counts the current element. */
    private int atomEnumerationCounter = 0;

    /** Contains the atoms to enumerate. */
    private AtomContainer container;

    /**
     *  Constructs a new AtomEnumeration.
     *
     *  @param  container  AtomContainer which contains the atoms
     */
    public AtomEnumeration(AtomContainer container) {
        this.container = container;
    }

    /**
     *  Returns true if the Enumeration still has atoms left.
     */
    public boolean hasMoreElements() {
        if (container.getAtomCount() > atomEnumerationCounter) {
            return true;
        }
        return false;
    }

    /**
     *  Returns next atom in Enumeration.
     *
     *  @return Uncasted Atom class.
     */
    public Object nextElement() {
        atomEnumerationCounter++;
        return container.getAtomAt(atomEnumerationCounter - 1);
    }
}
