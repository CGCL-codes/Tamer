package joelib.desc.types;

import java.util.Map;
import org.apache.log4j.Category;
import joelib.desc.DescDescription;
import joelib.desc.DescResult;
import joelib.desc.Descriptor;
import joelib.desc.DescriptorException;
import joelib.desc.DescriptorHelper;
import joelib.desc.DescriptorInfo;
import joelib.desc.ResultFactory;
import joelib.desc.result.AtomDynamicResult;
import joelib.desc.result.DynamicArrayResult;
import joelib.desc.result.IntMatrixResult;
import joelib.molecule.JOEMol;
import joelib.molecule.types.AtomProperties;
import joelib.util.JOEHelper;
import joelib.util.JOEProperty;
import joelib.util.JOEPropertyHelper;

/**
 * Electrotolpogical state descriptor (ESTATE).
 *
 * @author     wegnerj
 * @license GPL
 * @cvsversion    $Revision: 1.5 $, $Date: 2004/07/25 20:43:14 $
 */
public class ElectrotopologicalState implements Descriptor {

    /**
     *  Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance("joelib.desc.types.ElectrotopologicalState");

    public static final String DESC_KEY = "Electrotopological_state_index";

    public static final String DISTANCE_INFLUENCE = "DISTANCE_INFLUENCE";

    public static final double DEFAULT_DISTANCE_INFLUENCE = 2.0;

    private static final JOEProperty[] ACCEPTED_PROPERTIES = new JOEProperty[] { new JOEProperty(DISTANCE_INFLUENCE, "java.lang.Double", "Influence of distance (default=2).", true, new Double(DEFAULT_DISTANCE_INFLUENCE)) };

    private DescriptorInfo descInfo;

    private double influenceOfDistance;

    /**
     *  Constructor for the KierShape1 object
     */
    public ElectrotopologicalState() {
        descInfo = DescriptorHelper.generateDescInfo(DESC_KEY, this.getClass(), DescriptorInfo.TYPE_GEOMETRICAL, null, "joelib.desc.result.AtomDynamicResult");
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
    public DescResult calculate(JOEMol molOriginal, DescResult descResult, Map properties) throws DescriptorException {
        AtomDynamicResult result = null;
        JOEMol mol = (JOEMol) molOriginal.clone();
        mol.deleteHydrogens();
        if (!(descResult instanceof AtomDynamicResult)) {
            logger.error(descInfo.getName() + " result should be of type " + AtomDynamicResult.class.getName() + " but it's of type " + descResult.getClass().toString());
            return null;
        } else {
            result = (AtomDynamicResult) descResult;
        }
        if (!initialize(properties)) {
            return null;
        }
        DescResult tmpPropResult;
        tmpPropResult = DescriptorHelper.instance().descFromMol(mol, IntrinsicState.DESC_KEY);
        AtomProperties atomProperties;
        if (JOEHelper.hasInterface(tmpPropResult, "AtomProperties")) {
            atomProperties = (AtomProperties) tmpPropResult;
        } else {
            logger.error("Property '" + IntrinsicState.DESC_KEY + "' must be an atom type to calculate the " + DESC_KEY + ".");
            return null;
        }
        DescResult tmpResult = null;
        String distanceMatrixKey = "Distance_matrix";
        try {
            tmpResult = DescriptorHelper.instance().descFromMol(mol, distanceMatrixKey);
        } catch (DescriptorException ex) {
            logger.error(ex.toString());
            logger.error("Can not calculate distance matrix for " + DESC_KEY + ".");
            return null;
        }
        if (!(tmpResult instanceof IntMatrixResult)) {
            logger.error("Needed descriptor '" + distanceMatrixKey + "' should be of type " + IntMatrixResult.class.getName() + ". " + DESC_KEY + " can not be calculated.");
            return null;
        }
        IntMatrixResult distResult = (IntMatrixResult) tmpResult;
        int[][] distances = distResult.value;
        int s = mol.numAtoms();
        double[] estates = (double[]) DynamicArrayResult.getNewArray(DynamicArrayResult.DOUBLE, s);
        double di;
        double k = influenceOfDistance;
        int i_1;
        int j_1;
        for (int i = 0; i < s; i++) {
            i_1 = i + 1;
            di = 0.0;
            for (int j = 0; j < s; j++) {
                j_1 = j + 1;
                di += ((atomProperties.getDoubleValue(i_1) - atomProperties.getDoubleValue(j_1)) / (Math.pow(distances[i][j] + 1, k)));
            }
            estates[i] = atomProperties.getDoubleValue(i_1) + di;
            if (logger.isDebugEnabled()) {
                logger.debug("istate[" + i_1 + "]=" + atomProperties.getDoubleValue(i_1) + " estate[" + i_1 + "]=" + estates[i]);
            }
        }
        result.setArray(estates);
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
        Double property = (Double) JOEPropertyHelper.getProperty(this, DISTANCE_INFLUENCE, properties);
        if (property == null) {
            influenceOfDistance = DEFAULT_DISTANCE_INFLUENCE;
        } else {
            influenceOfDistance = property.doubleValue();
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
