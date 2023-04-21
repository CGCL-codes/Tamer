package joelib.desc.types;

import org.apache.log4j.Category;
import joelib.data.JOEElementTable;
import joelib.desc.AbstractAtomsCounter;
import joelib.desc.DescriptorHelper;
import joelib.desc.DescriptorInfo;

/**
 * Number of nitrogen atoms.
 *
 * @author     wegnerj
 * @license GPL
 * @cvsversion    $Revision: 1.14 $, $Date: 2004/09/24 15:06:15 $
 */
public class NumberOfN extends AbstractAtomsCounter {

    /**
     *  Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance("joelib.desc.types.NumberOfN");

    private static int[] DEFAULT = new int[] { JOEElementTable.instance().getAtomicNum("N") };

    public static final String DESC_KEY = "Number_of_N_atoms";

    public NumberOfN() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initialize " + this.getClass().getName());
        }
        descInfo = DescriptorHelper.generateDescInfo(DESC_KEY, this.getClass(), DescriptorInfo.TYPE_NO_COORDINATES, null, "joelib.desc.result.IntResult");
    }

    /**
     * Gets the defaultAtoms attribute of the NumberOfC object
     *
     * @return   The defaultAtoms value
     */
    public int[] getDefaultAtoms() {
        return DEFAULT;
    }
}
