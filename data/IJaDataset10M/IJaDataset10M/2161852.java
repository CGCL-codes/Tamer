package org.openscience.cdk.io;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IChemObject;

/**
 * This class is the interface that all IO readers should implement.
 * Programs need only care about this interface for any kind of IO.
 * Currently, database IO and file IO is supported.
 *
 * <p>The easiest way to implement a new ChemObjectReader is to
 * subclass the DefaultChemObjectReader.
 *
 * <p>I don't know how this should be enforced, but a Reader should
 * also provide an empty constructor so that ClassLoader/getInstance()
 * can be used to instantiate a ChemObjectReader.
 *
 * @cdk.module io
 * @cdk.githash
 *
 * @see DefaultChemObjectReader
 *
 * @author Egon Willighagen <egonw@users.sf.net>
 **/
public interface ISimpleChemObjectReader extends IChemObjectReader {

    /**
     * Reads an IChemObject of type "object" from input. The constructor
     * of the actual implementation may take a Reader as input to get
     * a very flexible reader that can read from string, files, etc.
     * 
     * @param  object    the type of object to return
     * @return returns an object of that contains the content (or 
     *         part) of the input content
     *
     * @exception CDKException it is thrown if
     *            the type of information is not available from 
     *            the input
     **/
    public <T extends IChemObject> T read(T object) throws CDKException;
}
