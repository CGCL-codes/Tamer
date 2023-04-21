package joelib2.math.similarity;

import joelib2.molecule.Molecule;
import java.util.List;

/**
 * @.author wegnerj
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface FeatureMetrics {

    public double[] compare(Molecule mol);

    public double compare(Molecule source, Molecule target);

    /**
     *  Description of the Method. <code>Double.NaN</code> is returned if no
     *  comparison value could be calulated
     *
     * @param  mol              Description of the Parameter
     * @param  _doubleDescName  Stores the distance value at the double value
     *      descriptor with this name.
     * @return                  double distance value
     */
    public double[] compare(Molecule mol, String _distResultName, double[] distances);

    public List getTargetMols();

    /**
     *  Sets the comparisonDescriptor attribute of the ComparisonHelper object
     *
     * @param  _descriptor  The new comparisonDescriptor value
     * @return              Description of the Return Value
     */
    public boolean setComparisonDescriptor(String _descriptor);

    /**
     *  Sets the comparisonDescriptors attribute of the ComparisonHelper object
     *
     * @param  _descriptors  The new comparisonDescriptors value
     * @return               Description of the Return Value
     */
    public boolean setComparisonDescriptors(String[] _descriptors);
}
