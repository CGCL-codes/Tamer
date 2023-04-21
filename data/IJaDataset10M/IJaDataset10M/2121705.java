package joelib2.feature.types.count;

import joelib2.data.BasicElementHolder;
import joelib2.data.IdentifierExpertSystem;
import joelib2.feature.AbstractAtomsCounter;
import joelib2.feature.BasicFeatureInfo;
import joelib2.feature.FeatureHelper;
import org.apache.log4j.Category;

/**
 * Number of nitrogen atoms.
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.9 $, $Date: 2005/02/17 16:48:32 $
 */
public class NumberOfN extends AbstractAtomsCounter {

    private static final String VENDOR = "http://joelib.sf.net";

    private static final String RELEASE_VERSION = "$Revision: 1.9 $";

    private static final String RELEASE_DATE = "$Date: 2005/02/17 16:48:32 $";

    private static Category logger = Category.getInstance(NumberOfN.class.getName());

    private static int[] DEFAULT = new int[] { BasicElementHolder.instance().getAtomicNum("N") };

    private static final Class[] DEPENDENCIES = new Class[] {};

    public NumberOfN() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initialize " + this.getClass().getName());
        }
        descInfo = FeatureHelper.generateFeatureInfo(this.getClass(), BasicFeatureInfo.TYPE_NO_COORDINATES, null, "joelib2.feature.result.IntResult");
    }

    public static Class[] getDependencies() {
        return DEPENDENCIES;
    }

    public static String getName() {
        return NumberOfN.class.getName();
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
    public int[] getDefaultAtoms() {
        return DEFAULT;
    }

    public int hashedDependencyTreeVersion() {
        return IdentifierExpertSystem.getDependencyTreeHash(getName());
    }
}
