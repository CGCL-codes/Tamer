package joelib2.util.iterator;

import java.util.List;

/**
 * Gets an iterator over all conformers in a molecule.
 *
 * <blockquote><pre>
 * ConformerIterator cit = mol.conformerIterator();
 * double conformer[];
 * while (cit.hasNext())
 * {
 *   conformer = cit.nextConformer();
 *
 * }
 * </pre></blockquote>
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.3 $, $Date: 2005/02/17 16:48:42 $
 * @see VectorIterator
 * @see joelib2.molecule.Molecule#conformerIterator()
 *
 */
public class BasicConformerIterator extends BasicListIterator implements ConformerIterator {

    public BasicConformerIterator(List v) {
        super(v);
    }

    public double[] nextConformer() {
        return (double[]) next();
    }
}
