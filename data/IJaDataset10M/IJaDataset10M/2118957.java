package joelib.io.types.cml;

import java.util.List;
import joelib.molecule.JOEMol;

/**
 * Interface for a CML molecule writer.
 *
 * @author     wegnerj
 * @license GPL
 * @cvsversion    $Revision: 1.13 $, $Date: 2004/08/29 14:10:11 $
 * @cite rr99b
 * @cite mr01
 * @cite gmrw01
 * @cite wil01
 * @cite mr03
 * @cite mrww04
 */
public interface CMLMoleculeWriter {

    public void writeMolecule(JOEMol mol, boolean writePairData, List attribs2write);
}
