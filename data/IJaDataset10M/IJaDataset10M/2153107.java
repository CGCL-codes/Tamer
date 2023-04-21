package joelib.desc.types;

import org.apache.log4j.Category;
import joelib.desc.DescResult;
import joelib.desc.DescriptorException;
import joelib.desc.DescriptorHelper;
import joelib.desc.DescriptorInfo;
import joelib.desc.AbstractIntDesc;
import joelib.desc.result.IntMatrixResult;
import joelib.molecule.JOEMol;

/**
 * Calculates the topological radius.
 *
 * @author     wegnerj
 * @license    GPL
 * @cvsversion    $Revision: 1.12 $, $Date: 2004/09/24 15:06:15 $
 */
public class TopologicalRadius extends AbstractIntDesc {

    /**
     *  Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance("joelib.desc.types.TopologicalRadius");

    public static final String DESC_KEY = "Topological_radius";

    public TopologicalRadius() {
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
    public int getIntValue(JOEMol mol) {
        if (mol.empty()) {
            logger.warn("Empty molecule '" + mol.getTitle() + "'. " + this.DESC_KEY + " was set to 0.");
            return 0;
        }
        DescResult tmpResult = null;
        String distanceMatrixKey = "Distance_matrix";
        try {
            tmpResult = DescriptorHelper.instance().descFromMol(mol, distanceMatrixKey);
        } catch (DescriptorException ex) {
            logger.error(ex.toString());
            logger.error("Can not calculate distance matrix for " + DESC_KEY + ".");
            return 0;
        }
        if (!(tmpResult instanceof IntMatrixResult)) {
            logger.error("Needed descriptor '" + distanceMatrixKey + "' should be of type " + IntMatrixResult.class.getName() + ". " + DESC_KEY + " can not be calculated.");
            return 0;
        }
        IntMatrixResult distResult = (IntMatrixResult) tmpResult;
        int[][] distances = distResult.value;
        int topologicalRadius = Integer.MAX_VALUE;
        for (int i = 0; i < distances.length; i++) {
            for (int ii = 0; ii < i; ii++) {
                if (topologicalRadius > distances[i][ii]) {
                    topologicalRadius = distances[i][ii];
                }
            }
        }
        return topologicalRadius;
    }
}
