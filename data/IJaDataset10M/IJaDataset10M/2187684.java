package joelib.desc.types;

import java.util.Arrays;
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
import joelib.molecule.JOEMol;
import joelib.util.JOEProperty;
import joelib.util.JOEPropertyHelper;

/**
 * Burden modified matrix descriptor (depends on single atom property used).
 *
 * @author     wegnerj
 * @license GPL
 * @cvsversion    $Revision: 1.5 $, $Date: 2004/07/25 20:43:14 $
 */
public class BurdenModifiedEigenvalues implements Descriptor {

    /**
     *  Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance("joelib.desc.types.BurdenModifiedEigenvalues");

    public static final String ATOM_PROPERTY = "ATOM_PROPERTY";

    public static final String ATOM_PROPERTY_WEIGHT = "ATOM_PROPERTY_WEIGHT";

    private static final JOEProperty[] ACCEPTED_PROPERTIES = new JOEProperty[] { new JOEProperty(ATOM_PROPERTY, "java.lang.String", "SMARTS pattern to count.", true, "Gasteiger_Marsili"), new JOEProperty(ATOM_PROPERTY_WEIGHT, "java.lang.Double", "Atom property weight to use.", true, new Double(1.0)) };

    public static final String DESC_KEY = "Burden_modified_eigenvalues";

    private DescriptorInfo descInfo;

    private String propertyName;

    private double atomPropWeight;

    /**
     *  Constructor for the BCUT object
     */
    public BurdenModifiedEigenvalues() {
        descInfo = DescriptorHelper.generateDescInfo(DESC_KEY, this.getClass(), DescriptorInfo.TYPE_NO_COORDINATES, null, "joelib.desc.result.APropDoubleArrResult");
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
        double[] bcutValues = BurdenEigenvalues.getBurdenEigenvalues(mol, propertyName, atomPropWeight);
        if (bcutValues == null) {
            logger.error(BurdenEigenvalues.class.getName() + " not available.");
            return null;
        }
        for (int i = 0; i < bcutValues.length; i++) {
            bcutValues[i] = Math.abs(bcutValues[i]);
        }
        Arrays.sort(bcutValues);
        double[] bcut_sort = new double[bcutValues.length];
        for (int i = 0; i < bcutValues.length; i++) {
            bcut_sort[i] = bcutValues[bcutValues.length - i - 1];
        }
        result.value = bcut_sort;
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
            propertyName = "Gasteiger_Marsili";
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
