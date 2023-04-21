package joelib2.feature.types.bondlabel;

import joelib2.data.IdentifierExpertSystem;
import joelib2.feature.BasicFeatureDescription;
import joelib2.feature.BasicFeatureInfo;
import joelib2.feature.Feature;
import joelib2.feature.FeatureDescription;
import joelib2.feature.FeatureException;
import joelib2.feature.FeatureHelper;
import joelib2.feature.FeatureResult;
import joelib2.feature.ResultFactory;
import joelib2.feature.result.AtomDynamicResult;
import joelib2.feature.result.BondDynamicResult;
import joelib2.feature.types.atomlabel.AtomInRing;
import joelib2.molecule.Bond;
import joelib2.molecule.Molecule;
import joelib2.molecule.types.BondProperties;
import joelib2.ring.RingDetector;
import joelib2.util.BasicProperty;
import java.util.Map;
import org.apache.log4j.Category;

/**
 * Is this atom negatively charged atom.
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.9 $, $Date: 2005/02/17 16:48:32 $
 */
public class BondInRing implements Feature {

    private static final String VENDOR = "http://joelib.sf.net";

    private static final String RELEASE_VERSION = "$Revision: 1.9 $";

    private static final String RELEASE_DATE = "$Date: 2005/02/17 16:48:32 $";

    private static Category logger = Category.getInstance(BondInRing.class.getName());

    private static final Class[] DEPENDENCIES = new Class[] { RingDetector.class };

    public BasicFeatureInfo descInfo;

    public BondInRing() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initialize " + this.getClass().getName());
        }
        descInfo = FeatureHelper.generateFeatureInfo(this.getClass(), BasicFeatureInfo.TYPE_NO_COORDINATES, null, "joelib2.feature.result.BondDynamicResult");
    }

    public static Class[] getDependencies() {
        return DEPENDENCIES;
    }

    public static String getName() {
        return BondInRing.class.getName();
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
    public static boolean isInRing(Bond bond) {
        boolean isInRing = false;
        Molecule mol = bond.getParent();
        if (bond.getParent().getModificationCounter() == 0) {
            if (!mol.hasData(BondInRing.getName()) || !mol.hasData(AtomInRing.getName())) {
                AtomDynamicResult ringAtoms = new AtomDynamicResult();
                BondDynamicResult ringBonds = new BondDynamicResult();
                RingDetector.findRingAtomsAndBonds(mol, ringAtoms, ringBonds);
                ringAtoms.addCMLProperty(IdentifierExpertSystem.instance().getKernelID());
                ringAtoms.setDataDescription(FeatureHelper.VERSION_IDENTIFIER + " " + String.valueOf(IdentifierExpertSystem.getDependencyTreeHash(AtomInRing.getName())));
                ringBonds.addCMLProperty(IdentifierExpertSystem.instance().getKernelID());
                ringBonds.setDataDescription(FeatureHelper.VERSION_IDENTIFIER + " " + String.valueOf(IdentifierExpertSystem.getDependencyTreeHash(BondInRing.getName())));
                mol.addData(ringAtoms);
                mol.addData(ringBonds);
            }
            BondProperties bInRing = (BondProperties) mol.getData(getName());
            if (bInRing != null) {
                if (bInRing.getIntValue(bond.getIndex()) != 0) {
                    isInRing = true;
                }
            } else {
                logger.error("No bond-in-ring informations available.");
            }
        } else {
            throw new RuntimeException("Could not access bond property. Modification counter is not zero.");
        }
        return isInRing;
    }

    public BasicProperty[] acceptedProperties() {
        return null;
    }

    /**
     * Calculate descriptor for this molecule.
     *
     * @param mol                      molecule for which this descriptor should be calculated
     * @return                         the descriptor calculation result for this molecule
     * @exception FeatureException  descriptor calculation exception
     */
    public FeatureResult calculate(Molecule mol) throws FeatureException {
        FeatureResult result = ResultFactory.instance().getFeatureResult(descInfo.getName());
        return calculate(mol, result, null);
    }

    /**
     * Calculate descriptor for this molecule.
     *
     * @param mol                      molecule for which this descriptor should be calculated
     * @param initData                 initialization properties
     * @return                         the descriptor calculation result for this molecule
     * @exception FeatureException  descriptor calculation exception
     */
    public FeatureResult calculate(Molecule mol, Map properties) throws FeatureException {
        FeatureResult result = ResultFactory.instance().getFeatureResult(descInfo.getName());
        return calculate(mol, result, properties);
    }

    /**
     * Calculate descriptor for this molecule.
     *
     * It should be faster, if we can can use an already initialized result class,
     * because this must not be get by Java reflection. Ensure that you will clone
     * this result class before you store these results in molecules, or the next molecule will
     * overwrite this result.
     *
     * @param mol                      molecule for which this descriptor should be calculated
     * @param descResult               the descriptor result class in which the result should be stored
     * @return                         the descriptor calculation result for this molecule
     * @exception FeatureException  descriptor calculation exception
     */
    public FeatureResult calculate(Molecule mol, FeatureResult descResult) throws FeatureException {
        return calculate(mol, descResult, null);
    }

    /**
     * Calculate descriptor for this molecule.
     *
     * It should be faster, if we can can use an already initialized result class,
     * because this must not be get by Java reflection. Ensure that you will clone
     * this result class before you store these results in molecules, or the next molecule will
     * overwrite this result.
     *
     * @param mol                      molecule for which this descriptor should be calculated
     * @param descResult               the descriptor result class in which the result should be stored
     * @param initData                 initialization properties
     * @return                         the descriptor calculation result for this molecule
     * @exception FeatureException  descriptor calculation exception
     */
    public FeatureResult calculate(Molecule mol, FeatureResult descResult, Map properties) throws FeatureException {
        BondDynamicResult result = null;
        if (!(descResult instanceof BondDynamicResult)) {
            logger.error(descInfo.getName() + " result should be of type " + BondDynamicResult.class.getName() + " but it's of type " + descResult.getClass().toString());
        } else {
            if (initialize(properties)) {
                result = (BondDynamicResult) descResult;
                if (!mol.hasData(BondInRing.getName())) {
                    RingDetector.findRingAtomsAndBonds(mol, null, result);
                }
                result.addCMLProperty(IdentifierExpertSystem.instance().getKernelID());
                result.setDataDescription(FeatureHelper.VERSION_IDENTIFIER + " " + String.valueOf(this.hashedDependencyTreeVersion()));
            }
        }
        return result;
    }

    /**
     * Clear descriptor calculation method for a new molecule.
     */
    public void clear() {
    }

    /**
     * Gets the descriptor informations for this descriptor.
     *
     * @return   the descriptor information
     */
    public BasicFeatureInfo getDescInfo() {
        return descInfo;
    }

    /**
     * Gets the descriptor description.
     *
     * @return   the descriptor description
     */
    public FeatureDescription getDescription() {
        return new BasicFeatureDescription(descInfo.getDescriptionFile());
    }

    public int hashedDependencyTreeVersion() {
        return IdentifierExpertSystem.getDependencyTreeHash(getName());
    }

    /**
     * Initialize descriptor calculation method for all following molecules.
     *
     * @param initData  initialization properties
     * @return <tt>true</tt> if the initialization was successfull
     */
    public boolean initialize(Map properties) {
        return true;
    }

    /**
     * Test the implementation of this descriptor.
     *
     * @return <tt>true</tt> if the implementation is correct
     */
    public boolean testDescriptor() {
        return true;
    }
}
