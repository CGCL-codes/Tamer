package joelib2.feature.types;

import joelib2.algo.contribution.BasicGroupContributions;
import joelib2.algo.contribution.GroupContributionPredictor;
import joelib2.data.BasicGroupContributionHolder;
import joelib2.data.IdentifierExpertSystem;
import joelib2.feature.AbstractDouble;
import joelib2.feature.BasicFeatureInfo;
import joelib2.feature.FeatureHelper;
import joelib2.molecule.Molecule;
import org.apache.log4j.Category;

/**
 * Calculates the molar refractivity (MR).
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.9 $, $Date: 2005/02/17 16:48:31 $
 * @.cite wc99
 */
public class MolarRefractivity extends AbstractDouble {

    private static final String VENDOR = "http://joelib.sf.net";

    private static final String RELEASE_VERSION = "$Revision: 1.9 $";

    private static final String RELEASE_DATE = "$Date: 2005/02/17 16:48:31 $";

    private static Category logger = Category.getInstance(MolarRefractivity.class.getName());

    private static final Class[] DEPENDENCIES = new Class[] { BasicGroupContributionHolder.class, GroupContributionPredictor.class };

    public MolarRefractivity() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initialize " + this.getClass().getName());
        }
        descInfo = FeatureHelper.generateFeatureInfo(this.getClass(), BasicFeatureInfo.TYPE_NO_COORDINATES, null, "joelib2.feature.result.DoubleResult");
    }

    public static Class[] getDependencies() {
        return DEPENDENCIES;
    }

    public static String getName() {
        return MolarRefractivity.class.getName();
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
    public double getDoubleValue(Molecule mol) {
        BasicGroupContributions contrib = null;
        contrib = BasicGroupContributionHolder.instance().getGroupContributions("MR");
        double mr;
        mr = GroupContributionPredictor.predict(contrib, mol);
        return mr;
    }

    public int hashedDependencyTreeVersion() {
        return IdentifierExpertSystem.getDependencyTreeHash(getName());
    }
}
