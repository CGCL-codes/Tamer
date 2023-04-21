package joelib.desc.types;

import jmat.data.Matrix;
import jmat.data.matrixDecompositions.EigenvalueDecomposition;
import java.util.Map;
import org.apache.log4j.Category;
import joelib.desc.DescDescription;
import joelib.desc.DescResult;
import joelib.desc.Descriptor;
import joelib.desc.DescriptorException;
import joelib.desc.DescriptorHelper;
import joelib.desc.DescriptorInfo;
import joelib.desc.ResultFactory;
import joelib.desc.result.APropDoubleArrResult;
import joelib.molecule.JOEAtom;
import joelib.molecule.JOEBond;
import joelib.molecule.JOEMol;
import joelib.molecule.types.AtomProperties;
import joelib.util.JOEHelper;
import joelib.util.JOEProperty;
import joelib.util.JOEPropertyHelper;
import joelib.util.iterator.AtomIterator;
import joelib.util.iterator.BondIterator;

/**
 * Burden matrix descriptor (depends on single atom property used).
 *
 * @author     wegnerj
 * @license GPL
 * @cvsversion    $Revision: 1.6 $, $Date: 2004/08/31 14:23:19 $
 */
public class BurdenEigenvalues implements Descriptor {

    /**
     *  Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance("joelib.desc.types.BurdenEigenvalues");

    public static final String ATOM_PROPERTY = "ATOM_PROPERTY";

    public static final String ATOM_PROPERTY_WEIGHT = "ATOM_PROPERTY_WEIGHT";

    private static final JOEProperty[] ACCEPTED_PROPERTIES = new JOEProperty[] { new JOEProperty(ATOM_PROPERTY, "java.lang.String", "Atom property to use.", true, "Atom_valence"), new JOEProperty(ATOM_PROPERTY_WEIGHT, "java.lang.Double", "Atom property weight to use.", true, new Double(1.0)) };

    public static final String DESC_KEY = "Burden_eigenvalues";

    private DescriptorInfo descInfo;

    private String propertyName;

    private double atomPropWeight;

    /**
     *  Constructor for the BCUT object
     */
    public BurdenEigenvalues() {
        descInfo = DescriptorHelper.generateDescInfo(DESC_KEY, this.getClass(), DescriptorInfo.TYPE_NO_COORDINATES, null, "joelib.desc.result.APropDoubleArrResult");
    }

    public static double[] getBurdenEigenvalues(JOEMol mol, String propertyName, double weight) {
        if ((propertyName == null) || (mol == null)) {
            return null;
        }
        JOEMol hDepletedMol = (JOEMol) mol.clone(true, new String[] { propertyName });
        hDepletedMol.deleteHydrogens();
        DescResult tmpPropResult;
        try {
            tmpPropResult = DescriptorHelper.instance().descFromMol(hDepletedMol, propertyName);
        } catch (DescriptorException ex) {
            logger.error(ex.toString());
            return null;
        }
        AtomProperties properties;
        if (JOEHelper.hasInterface(tmpPropResult, "AtomProperties")) {
            properties = (AtomProperties) tmpPropResult;
        } else {
            logger.error("Property '" + propertyName + "' must be an atom type for calculating " + DESC_KEY + " but it's " + tmpPropResult.getClass().getName() + ".");
            return null;
        }
        double[] burdenValues = new double[hDepletedMol.numAtoms()];
        Matrix burden = new Matrix(hDepletedMol.numAtoms(), hDepletedMol.numAtoms(), 0.001);
        JOEAtom atom;
        int atomIndex;
        AtomIterator ait = hDepletedMol.atomIterator();
        while (ait.hasNext()) {
            atom = ait.nextAtom();
            atomIndex = atom.getIdx();
            double x = 0;
            BondIterator bit = atom.bondIterator();
            JOEBond bond;
            while (bit.hasNext()) {
                bond = bit.nextBond();
                if (bond.isAromatic()) {
                    x = 1.5;
                } else {
                    x = (double) bond.getBO();
                }
                x *= 0.1;
                JOEAtom nbrAtom = bond.getNbrAtom(atom);
                if ((atom.getValence() == 1) || (nbrAtom.getValence() == 1)) {
                    x += 0.01;
                }
                burden.set(atomIndex - 1, nbrAtom.getIdx() - 1, x);
            }
            if (Double.isInfinite(properties.getDoubleValue(atomIndex))) {
                logger.error("Atom property contains infinity value.");
                return null;
            }
            burden.set(atomIndex - 1, atomIndex - 1, properties.getDoubleValue(atomIndex) * weight);
        }
        EigenvalueDecomposition eigDecomp = null;
        try {
            eigDecomp = burden.eig();
        } catch (Exception ex) {
            logger.error("Eigenvalues could not be calculated.");
            return null;
        }
        Matrix eigenvalueMatrix = eigDecomp.getD_Real();
        for (int i = 0; i < hDepletedMol.numAtoms(); i++) {
            burdenValues[i] = eigenvalueMatrix.get(i, i);
        }
        return burdenValues;
    }

    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    public DescriptorInfo getDescInfo() {
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
    public DescDescription getDescription() {
        return new DescDescription(descInfo.getDescriptionFile());
    }

    public JOEProperty[] acceptedProperties() {
        return ACCEPTED_PROPERTIES;
    }

    /**
     *  Description of the Method
     *
     * @param  mol                      Description of the Parameter
     * @return                          Description of the Return Value
     * @exception  DescriptorException  Description of the Exception
     */
    public DescResult calculate(JOEMol mol) throws DescriptorException {
        DescResult result = ResultFactory.instance().getDescResult(descInfo.getName());
        return calculate(mol, result, null);
    }

    /**
     *  Description of the Method
     *
     * @param  mol                      Description of the Parameter
     * @param  initData                 Description of the Parameter
     * @return                          Description of the Return Value
     * @exception  DescriptorException  Description of the Exception
     */
    public DescResult calculate(JOEMol mol, Map properties) throws DescriptorException {
        DescResult result = ResultFactory.instance().getDescResult(descInfo.getName());
        return calculate(mol, result, properties);
    }

    /**
     *  Description of the Method
     *
     * @param  mol                      Description of the Parameter
     * @param  descResult               Description of the Parameter
     * @return                          Description of the Return Value
     * @exception  DescriptorException  Description of the Exception
     */
    public DescResult calculate(JOEMol mol, DescResult descResult) throws DescriptorException {
        return calculate(mol, descResult, null);
    }

    /**
     *  Description of the Method
     *
     * @param  mol                      Description of the Parameter
     * @param  initData                 Description of the Parameter
     * @param  descResult               Description of the Parameter
     * @return                          Description of the Return Value
     * @exception  DescriptorException  Description of the Exception
     */
    public DescResult calculate(JOEMol mol, DescResult descResult, Map properties) throws DescriptorException {
        APropDoubleArrResult result = null;
        if (!(descResult instanceof APropDoubleArrResult)) {
            logger.error(descInfo.getName() + " result should be of type " + APropDoubleArrResult.class.getName() + " but it's of type " + descResult.getClass().toString());
            return null;
        } else {
            result = (APropDoubleArrResult) descResult;
        }
        if (!initialize(properties)) {
            return null;
        }
        double[] burdenValues = getBurdenEigenvalues(mol, propertyName, atomPropWeight);
        result.value = burdenValues;
        result.atomProperty = propertyName;
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
     * @param  initData  Description of the Parameter
     * @return           Description of the Return Value
     */
    public boolean initialize(Map properties) {
        if (!JOEPropertyHelper.checkProperties(this, properties)) {
            logger.error("Empty property definition or missing property entry.");
            return false;
        }
        String property = (String) JOEPropertyHelper.getProperty(this, ATOM_PROPERTY, properties);
        if (property == null) {
            propertyName = "Atom_valence";
        } else {
            propertyName = property;
        }
        Double dproperty = (Double) JOEPropertyHelper.getProperty(this, ATOM_PROPERTY_WEIGHT, properties);
        if (dproperty == null) {
            atomPropWeight = 1.0;
        } else {
            atomPropWeight = dproperty.doubleValue();
        }
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
