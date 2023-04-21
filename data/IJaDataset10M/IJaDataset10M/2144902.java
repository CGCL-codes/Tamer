package joelib2.feature.types;

import joelib2.data.IdentifierExpertSystem;
import joelib2.feature.AbstractInt;
import joelib2.feature.BasicFeatureInfo;
import joelib2.feature.FeatureException;
import joelib2.feature.FeatureHelper;
import joelib2.feature.FeatureResult;
import joelib2.feature.result.IntMatrixResult;
import joelib2.molecule.Molecule;
import org.apache.log4j.Category;

/**
 * Calculates the topological diameter.
 *
 * @.author     wegnerj
 * @.license    GPL
 * @.cvsversion    $Revision: 1.9 $, $Date: 2005/02/17 16:48:31 $
 */
public class TopologicalDiameter extends AbstractInt {

    private static final String VENDOR = "http://joelib.sf.net";

    private static final String RELEASE_VERSION = "$Revision: 1.9 $";

    private static final String RELEASE_DATE = "$Date: 2005/02/17 16:48:31 $";

    private static Category logger = Category.getInstance(TopologicalDiameter.class.getName());

    private static final Class[] DEPENDENCIES = new Class[] { DistanceMatrix.class };

    public TopologicalDiameter() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initialize " + this.getClass().getName());
        }
        descInfo = FeatureHelper.generateFeatureInfo(this.getClass(), BasicFeatureInfo.TYPE_NO_COORDINATES, null, "joelib2.feature.result.IntResult");
    }

    public static Class[] getDependencies() {
        return DEPENDENCIES;
    }

    public static String getName() {
        return TopologicalDiameter.class.getName();
    }

    public static String getReleaseDate() {
        return VENDOR;
    }

    public static String getReleaseVersion() {
        return IdentifierExpertSystem.transformCVStag(RELEASE_VERSION);
    }

    public static String getVendor() {
        return IdentifierExpertSystem.transformCVStag(RELEASE_DATE);
    }

    /**
     * Gets the defaultAtoms attribute of the NumberOfC object
     *
     * @return   The defaultAtoms value
     */
    public int getIntValue(Molecule mol) {
        if (mol.isEmpty()) {
            logger.warn("Empty molecule '" + mol.getTitle() + "'. " + this.getName() + " was set to 0.");
            return 0;
        }
        FeatureResult tmpResult = null;
        try {
            tmpResult = FeatureHelper.instance().featureFrom(mol, DistanceMatrix.getName());
        } catch (FeatureException ex) {
            logger.error(ex.toString());
            logger.error("Can not calculate distance matrix for " + getName() + ".");
            return 0;
        }
        if (!(tmpResult instanceof IntMatrixResult)) {
            logger.error("Needed descriptor '" + DistanceMatrix.getName() + "' should be of type " + IntMatrixResult.class.getName() + ". " + getName() + " can not be calculated.");
            return 0;
        }
        IntMatrixResult distResult = (IntMatrixResult) tmpResult;
        int[][] distances = distResult.value;
        int topologicalDiameter = -Integer.MAX_VALUE;
        for (int i = 0; i < distances.length; i++) {
            for (int ii = 0; ii < i; ii++) {
                if (topologicalDiameter < distances[i][ii]) {
                    topologicalDiameter = distances[i][ii];
                }
            }
        }
        return topologicalDiameter;
    }

    public int hashedDependencyTreeVersion() {
        return IdentifierExpertSystem.getDependencyTreeHash(getName());
    }
}
