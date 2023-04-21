package joelib2.feature;

import joelib2.data.IdentifierExpertSystem;
import joelib2.feature.result.AtomDynamicResult;
import joelib2.molecule.Molecule;
import joelib2.util.BasicProperty;
import java.util.Map;
import org.apache.log4j.Category;

/**
 * Descriptor class for calculating an atom property (dynamic value type definition).
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.8 $, $Date: 2005/02/17 16:48:29 $
 *
 * @see joelib2.feature.result.AtomDynamicResult
 */
public abstract class AbstractDynamicAtomProperty implements Feature {

    /**
     *  Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance(AbstractDynamicAtomProperty.class.getName());

    public BasicFeatureInfo descInfo;

    /**
     * Gets the dynamic atom property values.
     *
     * The dynamic atom properties can be double, integer or boolean values.
     *
     * @param the molecule
     * @return the dynamic atom property values
     */
    public abstract Object getAtomPropertiesArray(Molecule mol);

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
        AtomDynamicResult result = null;
        if (!(descResult instanceof AtomDynamicResult)) {
            logger.error(descInfo.getName() + " result should be of type " + AtomDynamicResult.class.getName() + " but it's of type " + descResult.getClass().toString());
        } else {
            if (initialize(properties)) {
                result = (AtomDynamicResult) descResult;
                Object array = getAtomPropertiesArray(mol);
                if (array == null) {
                    return null;
                }
                result.setArray(array);
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
