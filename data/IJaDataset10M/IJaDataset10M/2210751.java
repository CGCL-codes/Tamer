package org.openscience.cdk.interfaces;

/**
 * Represents a mapping of two atoms.
 * 
 * @cdk.module  interfaces
 * @cdk.githash
 * @author      kaihartmann
 * @cdk.created 2006-02-15
 */
public interface IMapping extends IChemObject {

    /**
     * Returns an {@link Iterable} to the two IChemObjects.
     *
     * @return An {@link Iterable} to two IChemObjects that define the mapping
     */
    public Iterable<IChemObject> relatedChemObjects();

    /**
     * Retrieve the first or second of the related IChemObjects.
     * 
     * @param   pos  The position of the IChemObject.
     * @return  The IChemObject to retrieve.
     */
    public IChemObject getChemObject(int pos);
}
