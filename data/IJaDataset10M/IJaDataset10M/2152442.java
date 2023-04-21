package joelib2.feature.types;

import jmat.data.Matrix;
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
import joelib2.feature.result.DynamicArrayResult;
import joelib2.feature.types.atomlabel.AtomValence;
import joelib2.molecule.Atom;
import joelib2.molecule.Bond;
import joelib2.molecule.Molecule;
import joelib2.molecule.types.AtomProperties;
import joelib2.molecule.types.BondProperties;
import joelib2.util.BasicProperty;
import joelib2.util.PropertyHelper;
import java.util.Map;
import org.apache.log4j.Category;

/**
 * Calculates the coefficients for the characteristic polynomial of a weighted graph.
 *
 * The coefficients are calculated using the LeVerrier-Faddeev-Frame method.
 *
 * The characteristic polynomial of a graph can be seen as the analytical interpretation of
 * the eigenvalues of the (weighted) adjaceny matrix.
 *
 * @.author     wegnerj
 * @.wikipedia Characteristic polynomial
 * @.wikipedia Graph theory
 * @.cite br90
 * @.cite tri92
 * @.license GPL
 * @.cvsversion    $Revision: 1.4 $, $Date: 2005/02/24 16:58:58 $
 */
public class CharacteristicPolynomialCoefficients implements Feature {

    private static final String VENDOR = "http://joelib.sf.net";

    private static final String RELEASE_VERSION = "$Revision: 1.4 $";

    private static final String RELEASE_DATE = "$Date: 2005/02/24 16:58:58 $";

    private static Category logger = Category.getInstance(CharacteristicPolynomial.class.getName());

    public static final String ATOM_PROPERTY = "ATOM_PROPERTY";

    public static final String BOND_PROPERTY = "BOND_PROPERTY";

    private static final BasicProperty[] ACCEPTED_PROPERTIES = new BasicProperty[] { new BasicProperty(BOND_PROPERTY, "java.lang.String", "Bond property to use.", true, null), new BasicProperty(ATOM_PROPERTY, "java.lang.String", "Atom property to use.", true, null) };

    private static final Class[] DEPENDENCIES = new Class[] { AtomValence.class, DistanceMatrix.class };

    private String atomLabelName;

    private String bondLabelName;

    private BasicFeatureInfo descInfo;

    /**
     *  Constructor for the GlobalTopologicalChargeIndex object
     */
    public CharacteristicPolynomialCoefficients() {
        descInfo = FeatureHelper.generateFeatureInfo(this.getClass(), BasicFeatureInfo.TYPE_NO_COORDINATES, null, joelib2.feature.result.AtomDynamicResult.class.getName());
    }

    public static Class[] getDependencies() {
        return DEPENDENCIES;
    }

    public static String getName() {
        return CharacteristicPolynomialCoefficients.class.getName();
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

    public BasicProperty[] acceptedProperties() {
        return ACCEPTED_PROPERTIES;
    }

    /**
     *  Description of the Method
     *
     * @param  mol                      Description of the Parameter
     * @return                          Description of the Return Value
     * @exception  FeatureException  Description of the Exception
     */
    public FeatureResult calculate(Molecule mol) throws FeatureException {
        FeatureResult result = ResultFactory.instance().getFeatureResult(descInfo.getName());
        return calculate(mol, result, null);
    }

    /**
     *  Description of the Method
     *
     * @param  mol                      Description of the Parameter
     * @param  initData                 Description of the Parameter
     * @return                          Description of the Return Value
     * @exception  FeatureException  Description of the Exception
     */
    public FeatureResult calculate(Molecule mol, Map properties) throws FeatureException {
        FeatureResult result = ResultFactory.instance().getFeatureResult(descInfo.getName());
        return calculate(mol, result, properties);
    }

    /**
     *  Description of the Method
     *
     * @param  mol                      Description of the Parameter
     * @param  descResult               Description of the Parameter
     * @return                          Description of the Return Value
     * @exception  FeatureException  Description of the Exception
     */
    public FeatureResult calculate(Molecule mol, FeatureResult descResult) throws FeatureException {
        return calculate(mol, descResult, null);
    }

    /**
     *  Description of the Method
     *
     * @param  initData                 Description of the Parameter
     * @param  descResult               Description of the Parameter
     * @param  molOriginal              Description of the Parameter
     * @return                          Description of the Return Value
     * @exception  FeatureException  Description of the Exception
     */
    public FeatureResult calculate(Molecule mol, FeatureResult descResult, Map properties) throws FeatureException {
        AtomDynamicResult result = null;
        if (!(descResult instanceof AtomDynamicResult)) {
            logger.error(descInfo.getName() + " result should be of type " + AtomDynamicResult.class.getName() + " but it's of type " + descResult.getClass().toString());
            return null;
        } else {
            result = (AtomDynamicResult) descResult;
        }
        if (!initialize(properties)) {
            return null;
        }
        if (mol.isEmpty()) {
            return null;
        }
        AtomProperties atomProperties = null;
        if (atomLabelName != null) {
            FeatureResult tmpPropResult;
            tmpPropResult = FeatureHelper.instance().featureFrom(mol, atomLabelName);
            if (tmpPropResult instanceof AtomProperties) {
                atomProperties = (AtomProperties) tmpPropResult;
            } else {
                logger.error("Property '" + atomLabelName + "' must be an atom type to calculate the " + getName() + ".");
                return null;
            }
        }
        BondProperties bondProperties = null;
        if (bondLabelName != null) {
            FeatureResult tmpPropResult;
            tmpPropResult = FeatureHelper.instance().featureFrom(mol, bondLabelName);
            if (tmpPropResult instanceof BondProperties) {
                bondProperties = (BondProperties) tmpPropResult;
            } else {
                logger.error("Property '" + bondLabelName + "' must be an bond type to calculate the " + getName() + ".");
                return null;
            }
        }
        Matrix weightedAdjacency = new Matrix(mol.getAtomsSize(), mol.getAtomsSize());
        Atom atom;
        for (int i = 1; i <= mol.getAtomsSize(); i++) {
            atom = mol.getAtom(i);
            if (atomLabelName != null) {
                weightedAdjacency.set(atom.getIndex() - 1, atom.getIndex() - 1, atomProperties.getDoubleValue(atom.getIndex()));
            } else {
                weightedAdjacency.set(atom.getIndex() - 1, atom.getIndex() - 1, 0.0);
            }
        }
        Bond bond;
        for (int i = 0; i < mol.getBondsSize(); i++) {
            bond = mol.getBond(i);
            if (bondLabelName != null) {
                weightedAdjacency.set(bond.getBeginIndex() - 1, bond.getEndIndex() - 1, bondProperties.getDoubleValue(bond.getIndex()));
                weightedAdjacency.set(bond.getEndIndex() - 1, bond.getBeginIndex() - 1, bondProperties.getDoubleValue(bond.getIndex()));
            } else {
                weightedAdjacency.set(bond.getEndIndex() - 1, bond.getBeginIndex() - 1, 1.0);
                weightedAdjacency.set(bond.getBeginIndex() - 1, bond.getEndIndex() - 1, 1.0);
            }
        }
        Matrix A_n = (Matrix) weightedAdjacency.clone();
        double[] coefficients = (double[]) DynamicArrayResult.getNewArray(DynamicArrayResult.DOUBLE, mol.getAtomsSize());
        for (int i = 0; i < mol.getAtomsSize(); i++) {
            double a_n = A_n.trace() / (double) (i + 1);
            coefficients[i] = a_n;
            Matrix identity = Matrix.identity(mol.getAtomsSize(), mol.getAtomsSize(), a_n);
            Matrix B_n = A_n.minus(identity);
            Matrix A_n_next = weightedAdjacency.times(B_n);
            A_n = A_n_next;
        }
        result.setArray(coefficients);
        return result;
    }

    /**
     *  Description of the Method
     */
    public void clear() {
    }

    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    public BasicFeatureInfo getDescInfo() {
        return descInfo;
    }

    /**
     *  Sets the descriptionFile attribute of the Descriptor object
     *
     *  Gets the description attribute of the Descriptor object
     *
     * @return            The description value
     * @return            The description value
     */
    public FeatureDescription getDescription() {
        return new BasicFeatureDescription(descInfo.getDescriptionFile());
    }

    public int hashedDependencyTreeVersion() {
        return IdentifierExpertSystem.getDependencyTreeHash(getName());
    }

    /**
     *  Description of the Method
     *
     * @param  initData  Description of the Parameter
     * @return           Description of the Return Value
     */
    public boolean initialize(Map properties) {
        if (!PropertyHelper.checkProperties(this, properties)) {
            logger.error("Empty property definition or missing property entry.");
            return false;
        }
        atomLabelName = (String) PropertyHelper.getProperty(this, ATOM_PROPERTY, properties);
        bondLabelName = (String) PropertyHelper.getProperty(this, BOND_PROPERTY, properties);
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
