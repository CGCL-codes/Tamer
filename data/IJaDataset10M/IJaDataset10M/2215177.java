package joelib.io;

import java.io.IOException;
import java.util.List;
import joelib.molecule.JOEMol;

/**
 * Interface for molecule file formats, which accepts descriptor values.
 *
 * For speed optimization of loading descriptor molecule files have a
 * look at the {@link joelib.desc.ResultFactory}.
 *
 * @author     wegnerj
 * @license GPL
 * @cvsversion    $Revision: 1.11 $, $Date: 2004/08/29 14:10:10 $
 */
public interface PropertyWriter {

    /**
     * Writes a molecule with his <tt>JOEPairData</tt>.
     *
     *@param  mol              the molecule with additional data
     *@param  title            the molecule title or <tt>null</tt> if the title
     *                         from the molecule should be used
     *@param  writePairData    if <tt>true</tt> then the additional molecule
     *                         data is written
     *@param  attrib2write     if <tt>null</tt> all <tt>JOEPairData</tt> elements
     *                         are written, otherwise all data elements are
     *                         written which are listed in <tt>attrib2write</tt>.
     *@return                  <tt>true</tt> if the molecule and the data
     *                         has been succesfully written.
     *@exception  IOException  Description of the Exception
     */
    public boolean write(JOEMol mol, String title, boolean writePairData, List attribs2write) throws IOException, MoleculeIOException;
}
