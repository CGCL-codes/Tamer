package org.openscience.cdk.qsar;

import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.AtomType;
import org.openscience.cdk.atomtype.HybridizationStateATMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.qsar.result.IntegerResult;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

/**
 *  This class returns the hybridization of an atom.
 *
 * <p>This descriptor uses these parameters:
 * <table border="1">
 *   <tr>
 *     <td>Name</td>
 *     <td>Default</td>
 *     <td>Description</td>
 *   </tr>
 *   <tr>
 *     <td>targetPosition</td>
 *     <td>0</td>
 *     <td>The position of the target atom</td>
 *   </tr>
 * </table>
 *
 *@author         mfe4
 *@cdk.created    2004-11-13
 *@cdk.module     qsar
 *@cdk.set        qsar-descriptors
 * @cdk.dictref qsar-descriptors:atomHybridization
 */
public class AtomHybridizationDescriptor implements Descriptor {

    private int targetPosition = 0;

    AtomTypeManipulator atman = null;

    HybridizationStateATMatcher atm = null;

    Atom atom = null;

    AtomType matched = null;

    /**
	 *  Constructor for the AtomHybridizationDescriptor object
	 */
    public AtomHybridizationDescriptor() {
    }

    /**
	 *  Gets the specification attribute of the AtomHybridizationDescriptor object
	 *
	 *@return    The specification value
	 */
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification("http://qsar.sourceforge.net/dicts/qsar-descriptors:atomHybridization", this.getClass().getName(), "$Id: AtomHybridizationDescriptor.java 4699 2005-08-16 09:26:34Z egonw $", "The Chemistry Development Kit");
    }

    /**
	 *  Sets the parameters attribute of the AtomHybridizationDescriptor object
	 *
	 *@param  params            The parameter is the atom position
	 *@exception  CDKException  Description of the Exception
	 */
    public void setParameters(Object[] params) throws CDKException {
        if (params.length > 1) {
            throw new CDKException("AtomHybridizationDescriptor only expects one parameter");
        }
        if (!(params[0] instanceof Integer)) {
            throw new CDKException("The parameter must be of type Integer");
        }
        targetPosition = ((Integer) params[0]).intValue();
    }

    /**
	 *  Gets the parameters attribute of the AtomHybridizationDescriptor object
	 *
	 *@return    The parameters value
	 */
    public Object[] getParameters() {
        Object[] params = new Object[1];
        params[0] = new Integer(targetPosition);
        return params;
    }

    /**
	 *  This method calculates the hybridization of an atom.
	 *
	 *@param  container         Parameter is the atom container.
	 *@return                   The hybridization
	 *@exception  CDKException  Description of the Exception
	 */
    public DescriptorValue calculate(AtomContainer container) throws CDKException {
        atom = container.getAtomAt(targetPosition);
        atm = new HybridizationStateATMatcher();
        matched = atm.findMatchingAtomType(container, atom);
        if (matched == null) {
            throw new CDKException("The matched atom type was null");
        }
        AtomTypeManipulator.configure(atom, matched);
        int atomHybridization = atom.getHybridization();
        return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(), new IntegerResult(atomHybridization));
    }

    /**
	 *  Gets the parameterNames attribute of the AtomHybridizationDescriptor object
	 *
	 *@return    The parameterNames value
	 */
    public String[] getParameterNames() {
        String[] params = new String[1];
        params[0] = "targetPosition";
        return params;
    }

    /**
	 *  Gets the parameterType attribute of the AtomHybridizationDescriptor object
	 *
	 *@param  name  Description of the Parameter
	 *@return       The parameterType value
	 */
    public Object getParameterType(String name) {
        Object[] paramTypes = new Object[1];
        paramTypes[0] = new Integer(1);
        return paramTypes;
    }
}
