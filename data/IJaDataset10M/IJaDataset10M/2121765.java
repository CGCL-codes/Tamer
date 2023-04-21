package joelib.desc.types;

import org.apache.log4j.Category;
import joelib.desc.DescriptorHelper;
import joelib.desc.DescriptorInfo;
import joelib.desc.AbstractDynamicAtomProperty;
import joelib.desc.result.DynamicArrayResult;
import joelib.molecule.JOEAtom;
import joelib.molecule.JOEMol;

/**
 * Is this atom positively charged atom.
 *
 * @author     wegnerj
 * @license GPL
 * @cvsversion    $Revision: 1.6 $, $Date: 2004/09/24 15:06:15 $
 */
public class AtomIsPositive extends AbstractDynamicAtomProperty {

    /**
     *  Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance("joelib.desc.types.AtomIsPositive");

    public static final String DESC_KEY = "Atom_is_positive";

    /**
     *  Constructor for the KierShape1 object
     */
    public AtomIsPositive() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initialize " + this.getClass().getName());
        }
        descInfo = DescriptorHelper.generateDescInfo(DESC_KEY, this.getClass(), DescriptorInfo.TYPE_NO_COORDINATES, null, "joelib.desc.result.AtomDynamicResult");
    }

    public Object getAtomPropertiesArray(JOEMol mol) {
        int s = mol.numAtoms();
        boolean[] positive = (boolean[]) DynamicArrayResult.getNewArray(DynamicArrayResult.BOOLEAN, s);
        JOEAtom atom;
        for (int i = 1; i < s; i++) {
            atom = mol.getAtom(i);
            if (atom.getFormalCharge() < 0) {
                positive[i - 1] = true;
            } else {
                positive[i - 1] = false;
            }
        }
        return positive;
    }
}
