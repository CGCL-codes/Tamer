package joelib2.feature.types.atomlabel;

import joelib2.data.BasicProtonationModel;
import joelib2.data.IdentifierExpertSystem;
import joelib2.feature.AbstractDoubleAtomProperty;
import joelib2.feature.BasicFeatureInfo;
import joelib2.feature.FeatureException;
import joelib2.feature.FeatureHelper;
import joelib2.molecule.Atom;
import joelib2.molecule.Molecule;
import joelib2.molecule.charge.GasteigerMarsili;
import joelib2.molecule.types.AtomProperties;
import joelib2.molecule.types.AtomPropertyHelper;
import org.apache.log4j.Category;

/**
 * Partial charge calculation.
 *
 * @.author     wegnerj
 * @.license    GPL
 * @.cvsversion    $Revision: 1.13 $, $Date: 2005/02/17 16:48:31 $
 * @.cite gm78
 */
public class AtomPartialCharge extends AbstractDoubleAtomProperty {

    private static final String VENDOR = "http://joelib.sf.net";

    private static final String RELEASE_VERSION = "$Revision: 1.13 $";

    private static final String RELEASE_DATE = "$Date: 2005/02/17 16:48:31 $";

    private static Category logger = Category.getInstance(AtomPartialCharge.class.getName());

    private static final Class[] DEPENDENCIES = new Class[] { BasicProtonationModel.class, GasteigerMarsili.class };

    /**
     *  Constructor for the partial charge calculation.
     */
    public AtomPartialCharge() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initialize " + this.getClass().getName());
        }
        descInfo = FeatureHelper.generateFeatureInfo(this.getClass(), BasicFeatureInfo.TYPE_NO_COORDINATES, null, "joelib2.feature.result.AtomDoubleResult");
    }

    public static Class[] getDependencies() {
        return DEPENDENCIES;
    }

    public static String getName() {
        return AtomPartialCharge.class.getName();
    }

    public static double getPartialCharge(Atom atom) {
        double partialCharge = 0;
        try {
            partialCharge = AtomPropertyHelper.getDoubleAtomProperty(atom, getName());
        } catch (FeatureException e1) {
            logger.error(e1.getMessage());
        }
        return partialCharge;
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
     *  Returns <tt>true</tt> if this is a ring atom.
     *
     * @return    <tt>true</tt> if this is a ring atom
     */
    public static void setPartialCharge(Atom atom, double pCharge) {
        if (atom != null) {
            Molecule mol = atom.getParent();
            if (mol.getModificationCounter() == 0) {
                AtomProperties atCache;
                try {
                    atCache = (AtomProperties) FeatureHelper.instance().featureFrom(atom.getParent(), getName());
                } catch (FeatureException e1) {
                    throw new RuntimeException(e1.getMessage());
                }
                if (atCache != null) {
                    atCache.setDoubleValue(atom.getIndex(), pCharge);
                } else {
                    logger.error("No automatic partial charge informations available.");
                }
            } else {
                throw new RuntimeException("Could not access atom property. Modification counter is not zero.");
            }
        }
    }

    public double[] getDoubleAtomProperties(Molecule mol) {
        double[] pCharges = new double[mol.getAtomsSize()];
        BasicProtonationModel.instance().assignSeedPartialCharge(mol, pCharges);
        GasteigerMarsili gc = new GasteigerMarsili();
        gc.assignPartialCharges(mol, pCharges);
        return pCharges;
    }

    public int hashedDependencyTreeVersion() {
        return IdentifierExpertSystem.getDependencyTreeHash(getName());
    }
}
