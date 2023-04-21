package joelib2.feature.types.count;

import joelib2.data.IdentifierExpertSystem;
import joelib2.feature.AbstractSMARTSCounter;
import joelib2.feature.BasicFeatureInfo;
import joelib2.feature.FeatureHelper;
import joelib2.smarts.ProgrammableAtomTyper;
import org.apache.log4j.Category;

/**
 * Number of aliphatic hydroxy groups.
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.9 $, $Date: 2005/02/17 16:48:32 $
 */
public class AliphaticOHGroups extends AbstractSMARTSCounter {

    private static final String VENDOR = "http://joelib.sf.net";

    private static final String RELEASE_VERSION = "$Revision: 1.9 $";

    private static final String RELEASE_DATE = "$Date: 2005/02/17 16:48:32 $";

    private static Category logger = Category.getInstance(AliphaticOHGroups.class.getName());

    public static final String DEFAULT = "[OX2;H1;$(O-C)]";

    private static final Class[] DEPENDENCIES = new Class[] { ProgrammableAtomTyper.class };

    public AliphaticOHGroups() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initialize " + this.getClass().getName() + "with SMARTS pattern: " + DEFAULT);
        }
        descInfo = FeatureHelper.generateFeatureInfo(this.getClass(), BasicFeatureInfo.TYPE_NO_COORDINATES, "joelib2.feature.StringInit", "joelib2.feature.result.IntResult");
    }

    public static Class[] getDependencies() {
        return DEPENDENCIES;
    }

    public static String getName() {
        return AliphaticOHGroups.class.getName();
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

    public String getDefaultSMARTS() {
        return DEFAULT;
    }

    public int hashedDependencyTreeVersion() {
        return IdentifierExpertSystem.getDependencyTreeHash(getName());
    }
}
