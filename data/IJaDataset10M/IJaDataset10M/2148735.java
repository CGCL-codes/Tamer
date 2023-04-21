package joelib2.feature;

import joelib2.data.IdentifierExpertSystem;
import joelib2.feature.result.AtomDoubleResult;
import joelib2.molecule.Molecule;
import joelib2.util.BasicProperty;
import java.util.Map;
import org.apache.log4j.Category;

/**
 * Descriptor class for calculating an atom property which are double values.
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.7 $, $Date: 2005/02/17 16:48:29 $
 * @.cite wy96
 *
 * @see joelib2.feature.result.AtomDoubleResult
 */
public abstract class AbstractDoubleAtomProperty implements Feature {

    /**
     *  Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance(AbstractDoubleAtomProperty.class.getName());

    public BasicFeatureInfo descInfo;

    /**
     * Gets the double atom property values.
     *
     * @param the molecule
     * @return the double atom property values
     */
    public abstract double[] getDoubleAtomProperties(Molecule mol);

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
        AtomDoubleResult result = null;
        if (!(descResult instanceof AtomDoubleResult)) {
            logger.error(descInfo.getName() + " result should be of type " + AtomDoubleResult.class.getName() + " but it's of type " + descResult.getClass().toString());
        } else {
            if (initialize(properties)) {
                result = (AtomDoubleResult) descResult;
                double[] atomProps = getDoubleAtomProperties(mol);
                result.setDoubleArray(atomProps);
                result.addCMLProperty(IdentifierExpertSystem.instance().getKernelID());
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
