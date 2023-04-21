package joelib2.smarts;

import joelib2.molecule.Molecule;
import joelib2.util.BasicBitVector;
import joelib2.util.types.BasicIntIntInt;
import java.io.OutputStream;
import java.util.List;

/**
 * TODO description.
 *
 * @.author     wegnerj
 * @.license    GPL
 * @.cvsversion $Revision: 1.3 $, $Date: 2005/02/17 16:48:39 $
 */
public interface QueryPatternMatcher {

    /**
     * Get list of matching atoms.
     *
     * @return    {@link java.util.Vector} of <tt>int[]</tt>
     */
    List<int[]> getMatches();

    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    int getMatchesSize();

    /**
     * Get unique list of matching atoms.
     *
     * @return    {@link java.util.Vector} of <tt>int[]</tt>
     */
    List<int[]> getMatchesUnique();

    /**
     *  Gets the atomicNum attribute of the JOESmartsPattern object
     *
     * @param  idx  Description of the Parameter
     * @return      The atomicNum value
     */
    int getQueryAtomIndex(int idx);

    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    int getQueryAtomsSize();

    /**
     *  Gets the bond attribute of the JOESmartsPattern object
     *
     * @param  iii  Description of the Parameter
     * @param  idx  Description of the Parameter
     */
    void getQueryBond(BasicIntIntInt iii, int idx);

    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    int getQueryBondsSize();

    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    boolean isEmpty();

    /**
     *  Gets the valid attribute of the JOESmartsPattern object
     *
     * @return    The valid value
     */
    boolean isValid();

    /**
     *  Description of the Method
     *
     * @param  mol  Description of the Parameter
     * @return         <tt>true</tt> SMARTS matching was successfull and pattern occur
     */
    boolean match(Molecule mol);

    /**
     *  Description of the Method
     *
     * @param  mol     Description of the Parameter
     * @param  single  Description of the Parameter
     * @return         <tt>true</tt> SMARTS matching was successfull and pattern occur
     */
    boolean match(Molecule mol, boolean single);

    /**
     * @param  pr   of type <tt>IntInt</tt> -{@link java.util.Vector}
     * @param  mol  Description of the Parameter
     * @return      Description of the Return Value
     */
    boolean restrictedMatch(Molecule mol, List pr);

    /**
     *  Description of the Method
     *
     * @param  mol   Description of the Parameter
     * @param  vres  Description of the Parameter
     * @return       Description of the Return Value
     */
    boolean restrictedMatch(Molecule mol, BasicBitVector vres);

    /**
     * @param  pr      of type <tt>IntInt</tt> -{@link java.util.Vector}
     * @param  mol     Description of the Parameter
     * @param  single  Description of the Parameter
     * @return         Description of the Return Value
     */
    boolean restrictedMatch(Molecule mol, List pr, boolean single);

    /**
     *  Description of the Method
     *
     * @param  mol     Description of the Parameter
     * @param  vres    Description of the Parameter
     * @param  single  Description of the Parameter
     * @return         Description of the Return Value
     */
    boolean restrictedMatch(Molecule mol, BasicBitVector vres, boolean single);

    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    String toString();

    /**
     *  Description of the Method
     *
     * @param  ofs  Description of the Parameter
     */
    void writeMatches(OutputStream ofs);
}
