package joelib2.feature.types.bondlabel;

import joelib2.data.IdentifierExpertSystem;
import joelib2.feature.AbstractDynamicBondProperty;
import joelib2.feature.BasicFeatureInfo;
import joelib2.feature.FeatureException;
import joelib2.feature.FeatureHelper;
import joelib2.feature.result.DynamicArrayResult;
import joelib2.molecule.BasicConformerMolecule;
import joelib2.molecule.Bond;
import joelib2.molecule.KekuleHelper;
import joelib2.molecule.Molecule;
import joelib2.molecule.types.BondProperties;
import org.apache.log4j.Category;

/**
 * Atom type (JOELib internal).
 *
 * This atom property stores the JOELib internal atom type, which can be used via the
 * look-up table in {@link joelib2.data.BasicAtomTypeConversionHolder} to export molecules to other
 * formats, like Synyl MOL2, MM2, Tinker, etc.
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.10 $, $Date: 2005/02/17 16:48:32 $
 */
public class BondKekuleType extends AbstractDynamicBondProperty {

    private static final String VENDOR = "http://joelib.sf.net";

    private static final String RELEASE_VERSION = "$Revision: 1.10 $";

    private static final String RELEASE_DATE = "$Date: 2005/02/17 16:48:32 $";

    private static Category logger = Category.getInstance(BondKekuleType.class.getName());

    private static final Class[] DEPENDENCIES = new Class[] { KekuleHelper.class };

    /**
     *  Constructor for the AtomType object
     */
    public BondKekuleType() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initialize " + this.getClass().getName());
        }
        descInfo = FeatureHelper.generateFeatureInfo(this.getClass(), BasicFeatureInfo.TYPE_NO_COORDINATES, null, "joelib2.feature.result.BondDynamicResult");
    }

    public static Class[] getDependencies() {
        return DEPENDENCIES;
    }

    public static int getKekuleType(Bond bond) {
        int kekule = 0;
        BondProperties btCache;
        if (bond.getParent().getModificationCounter() == 0) {
            if (bond.getParent() instanceof BasicConformerMolecule) {
                if (((BasicConformerMolecule) bond.getParent()).isOccuredKekulizationError()) {
                    kekule = -1;
                } else {
                    try {
                        btCache = (BondProperties) FeatureHelper.instance().featureFrom(bond.getParent(), getName());
                    } catch (FeatureException e1) {
                        throw new RuntimeException(e1.getMessage());
                    }
                    if (btCache != null) {
                        kekule = btCache.getIntValue(bond.getIndex());
                    } else {
                        logger.warn("No kekule informations available.");
                        kekule = -1;
                    }
                }
            }
        } else {
            throw new RuntimeException("Could not access bond property. Modification counter is not zero.");
        }
        return kekule;
    }

    public static String getName() {
        return BondKekuleType.class.getName();
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

    public Object getBondPropertiesArray(Molecule mol) {
        int[] kekule = (int[]) DynamicArrayResult.getNewArray(DynamicArrayResult.INT, mol.getBondsSize());
        if (KekuleHelper.perceiveKekuleBonds(mol, kekule)) {
            return kekule;
        } else {
            return null;
        }
    }

    public int hashedDependencyTreeVersion() {
        return IdentifierExpertSystem.getDependencyTreeHash(getName());
    }
}
