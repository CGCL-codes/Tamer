package org.scilab.forge.jlatexmath;

/**
 * A "composed atom": an atom that consists of child atoms that will be displayed 
 * next to each other horizontally with glue between them.
 */
public interface Row {

    /**
     * Sets the given dummy containing the atom that comes just before
     * the first child atom of this "composed atom". This method will allways be called
     * by another composed atom, so this composed atom will be a child of it (nested). 
     * This is necessary to determine the glue to insert between the first child atom 
     * of this nested composed atom and the atom that the dummy contains. 
     * 
     * @param dummy the dummy that comes just before this "composed atom"
     */
    public void setPreviousAtom(Dummy dummy);
}
