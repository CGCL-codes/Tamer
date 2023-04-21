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
import joelib.desc.result.DoubleMatrixResult;
import joelib.molecule.JOEMol;
import joelib.util.JOEProperty;

/**
 * Geometrical distance matrix.
 *
 * @author     wegnerj
 * @license GPL
 * @cvsversion    $Revision: 1.10 $, $Date: 2004/07/25 20:43:14 $
 */
public class GeomDistanceMatrix implements Descriptor {

    /**
     *  Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance("joelib.desc.types.GeomDistanceMatrix");

    public static final String DESC_KEY = "Geometrical_distance_matrix";

    private DescriptorInfo descInfo;

    /**
     *  Constructor for the DistanceMatrix object
     */
    public GeomDistanceMatrix() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initialize " + this.getClass().getName());
        }
        descInfo = DescriptorHelper.generateDescInfo(DESC_KEY, this.getClass(), DescriptorInfo.TYPE_GEOMETRICAL, null, "joelib.desc.result.DoubleMatrixResult");
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
     *  Gets the description attribute of the Descriptor object
     *
     * @return    The description value
     */
    public DescDescription getDescription() {
        return new DescDescription(descInfo.getDescriptionFile());
    }

    public JOEProperty[] acceptedProperties() {
        return null;
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
        if (mol.empty()) {
            logger.error("Empty molecule '" + mol.getTitle() + "'.");
            return null;
        }
        if (!(descResult instanceof DoubleMatrixResult)) {
            logger.error(descInfo.getName() + " result should be of type " + DoubleMatrixResult.class.getName() + " but it's of type " + descResult.getClass().toString());
        }
        if (!initialize(properties)) {
            return null;
        }
        double dist;
        double[][] matrix = new double[mol.numAtoms()][mol.numAtoms()];
        for (int i = 1; i < mol.numAtoms(); i++) {
            for (int k = 0; k < i; k++) {
                dist = Math.sqrt(Math.pow(mol.getAtom(i + 1).getX() - mol.getAtom(k + 1).getX(), 2.0) + Math.pow(mol.getAtom(i + 1).getY() - mol.getAtom(k + 1).getY(), 2.0) + Math.pow(mol.getAtom(i + 1).getZ() - mol.getAtom(k + 1).getZ(), 2.0));
                matrix[i][k] = dist;
                matrix[k][i] = dist;
            }
        }
        DoubleMatrixResult result = (DoubleMatrixResult) descResult;
        result.value = matrix;
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
