package joelib.desc.types;

import org.apache.log4j.Category;
import joelib.desc.DescriptorHelper;
import joelib.desc.DescriptorInfo;
import joelib.desc.AbstractDynamicAtomProperty;
import joelib.desc.result.DynamicArrayResult;
import joelib.molecule.JOEAtom;
import joelib.molecule.JOEMol;

/**
 * Is this atom a ring atom.
 *
 * @author     wegnerj
 * @license GPL
 * @cvsversion    $Revision: 1.6 $, $Date: 2004/09/24 15:06:15 $
 */
public class AtomInRing extends AbstractDynamicAtomProperty {

    /**
     *  Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance("joelib.desc.types.AtomInRing");

    public static final String DESC_KEY = "Atom_in_ring";

    /**
     *  Constructor for the KierShape1 object
     */
    public AtomInRing() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initialize " + this.getClass().getName());
        }
        descInfo = DescriptorHelper.generateDescInfo(DESC_KEY, this.getClass(), DescriptorInfo.TYPE_NO_COORDINATES, null, "joelib.desc.result.AtomDynamicResult");
    }

    public Object getAtomPropertiesArray(JOEMol mol) {
        int s = mol.numAtoms();
        boolean[] ring = (boolean[]) DynamicArrayResult.getNewArray(DynamicArrayResult.BOOLEAN, s);
        JOEAtom atom;
        for (int i = 1; i < s; i++) {
            atom = mol.getAtom(i);
            ring[i - 1] = atom.isInRing();
        }
        return ring;
    }
}
