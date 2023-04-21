package joelib2.feature.types.bondlabel;

import joelib2.data.BasicAromaticityTyper;
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
import joelib2.feature.types.atomlabel.AtomInAromaticSystem;
import joelib2.molecule.Bond;
import joelib2.molecule.BondHelper;
import joelib2.molecule.Molecule;
import joelib2.molecule.types.BondProperties;
import joelib2.util.BasicProperty;
import java.util.Map;
import org.apache.log4j.Category;

/**
 * Is this atom negatively charged atom.
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.12 $, $Date: 2005/03/03 07:13:49 $
 */
public class BondInAromaticSystem implements Feature {

    private static final String VENDOR = "http://joelib.sf.net";

    private static final String RELEASE_VERSION = "$Revision: 1.12 $";

    private static final String RELEASE_DATE = "$Date: 2005/03/03 07:13:49 $";

    private static Category logger = Category.getInstance(BondInAromaticSystem.class.getName());

    private static final Class[] DEPENDENCIES = new Class[] { BasicAromaticityTyper.class };

    public BasicFeatureInfo descInfo;

    public BondInAromaticSystem() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initialize " + this.getClass().getName());
        }
        descInfo = FeatureHelper.generateFeatureInfo(this.getClass(), BasicFeatureInfo.TYPE_NO_COORDINATES, null, "joelib2.feature.result.BondDynamicResult");
    }

    public static Class[] getDependencies() {
        return DEPENDENCIES;
    }

    public static String getName() {
        return BondInAromaticSystem.class.getName();
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
    public static boolean isAromatic(Bond bond) {
        boolean isAromatic = false;
        Molecule mol = bond.getParent();
        BondProperties btCache;
        if (bond.getParent().getModificationCounter() == 0) {
            if (!mol.hasData(BondInAromaticSystem.getName())) {
                assignAromaticity(mol);
            }
            btCache = (BondProperties) mol.getData(getName());
            if (btCache != null) {
                if (btCache.getIntValue(bond.getIndex()) != 0) {
                    isAromatic = true;
                }
            } else {
                logger.error("No aromatic informations available.");
            }
        } else {
            throw new RuntimeException("Could not access bond property. Modification counter is not zero.");
        }
        return isAromatic || (bond.getBondOrder() == BondHelper.AROMATIC_BO);
    }

    /**
     *  Returns <tt>true</tt> if this is a ring atom.
     *
     * @return    <tt>true</tt> if this is a ring atom
     */
    public static void setAromatic(Bond bond, boolean isAromatic) {
        if (bond != null) {
            Molecule mol = bond.getParent();
            if (mol.getModificationCounter() == 0) {
                if (!mol.hasData(BondInAromaticSystem.getName())) {
                    assignAromaticity(mol);
                }
                BondProperties bIsArom = (BondProperties) mol.getData(BondInAromaticSystem.getName());
                if (bIsArom != null) {
                    if (isAromatic) {
                        bIsArom.setIntValue(bond.getIndex(), 1);
                    } else {
                        bIsArom.setIntValue(bond.getIndex(), 0);
                    }
                } else {
                    logger.error("No automatic aromatic informations available.");
                }
            } else {
                throw new RuntimeException("Could not access bond property. Modification counter is not zero.");
            }
        }
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
                if (!mol.hasData(getName())) {
                    AtomDynamicResult aromAtoms = new AtomDynamicResult();
                    BasicAromaticityTyper.instance().assignAromaticFlags(mol, aromAtoms, result);
                    aromAtoms.addCMLProperty(IdentifierExpertSystem.instance().getKernelID());
                    aromAtoms.setDataDescription(FeatureHelper.VERSION_IDENTIFIER + " " + String.valueOf(IdentifierExpertSystem.getDependencyTreeHash(AtomInAromaticSystem.getName())));
                    mol.addData(aromAtoms);
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

    private static void assignAromaticity(Molecule mol) {
        AtomDynamicResult aromAtoms = new AtomDynamicResult();
        BondDynamicResult aromBonds = new BondDynamicResult();
        BasicAromaticityTyper.instance().assignAromaticFlags(mol, aromAtoms, aromBonds);
        aromAtoms.addCMLProperty(IdentifierExpertSystem.instance().getKernelID());
        aromAtoms.setDataDescription(FeatureHelper.VERSION_IDENTIFIER + " " + String.valueOf(IdentifierExpertSystem.getDependencyTreeHash(AtomInAromaticSystem.getName())));
        aromBonds.addCMLProperty(IdentifierExpertSystem.instance().getKernelID());
        aromBonds.setDataDescription(FeatureHelper.VERSION_IDENTIFIER + " " + String.valueOf(IdentifierExpertSystem.getDependencyTreeHash(BondInAromaticSystem.getName())));
        mol.addData(aromAtoms);
        mol.addData(aromBonds);
    }
}
