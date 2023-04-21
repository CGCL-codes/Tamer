package org.openscience.cdk.tools;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.exception.CDKException;

/**
 * A common interface for SaturationChecker and ValencyChecker. Mainly created
 * to be able to have HydrogenAdder use both.
 *
 * @author         Egon Willighagen
 * @cdk.created    2004-01-08
 * 
 * @cdk.module     valencycheck
 * @cdk.githash
 */
public interface IValencyChecker {

    public boolean isSaturated(IAtomContainer ac) throws CDKException;

    public boolean isSaturated(IAtom atom, IAtomContainer container) throws CDKException;
}
