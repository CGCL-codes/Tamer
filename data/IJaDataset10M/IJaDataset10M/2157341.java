package org.openscience.cdk.qsar;

import org.openscience.cdk.Atom;
import org.openscience.cdk.Bond;
import org.openscience.cdk.Element;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.qsar.result.*;
import org.openscience.cdk.config.IsotopeFactory;

/**
 * Sum of the absolute value of the difference between atomic polarizabilities 
 *  of all bonded atoms in the molecule (including implicit hydrogens) with polarizabilities taken from
 * http://www.sunysccc.edu/academic/mst/ptable/p-table2.htm .
 * This class need explicit hydrogens.
 *
 * @author      mfe4
 * @cdk.created 2004-11-13
 * @cdk.module  qsar
 * @cdk.set     qsar-descriptors
 */
public class BPolDescriptor implements Descriptor {

    /**
	 *  Constructor for the BPolDescriptor object
	 */
    public BPolDescriptor() {
    }

    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification("http://qsar.sourceforge.net/dicts/qsar-descriptors:bpol", this.getClass().getName(), "$Id: BPolDescriptor.java 3617 2005-01-12 15:22:38Z egonw $", "The Chemistry Development Kit");
    }

    ;

    /**
	 *  Sets the parameters attribute of the BPolDescriptor object
	 *
	 *@param  params            The new parameters value
	 *@exception  CDKException  Description of the Exception
	 */
    public void setParameters(Object[] params) throws CDKException {
    }

    /**
	 *  Gets the parameters attribute of the BPolDescriptor object
	 *
	 *@return    The parameters value
	 */
    public Object[] getParameters() {
        return (null);
    }

    /**
	 *  This method calculate the sum of the absolute value of 
	 *  the difference between atomic polarizabilities of all bonded atoms in the molecule
	 *
	 *@param  container  Parameter is the atom container.
	 *@return            The sum of atomic polarizabilities
	 */
    public DescriptorResult calculate(AtomContainer container) throws CDKException {
        double[] polarizabilities = { 0, 0.666793, 0.204956, 24.3, 5.6, 3.03, 1.76, 1.1, 0.802, 0.557, 0.3956, 23.6, 10.6, 6.8, 5.38, 3.63, 2.9, 2.18, 1.6411, 43.4, 22.8, 17.8, 14.6, 12.4, 11.6, 9.4, 8.4, 7.5, 6.8, 6.1, 7.1, 8.12, 6.07, 4.31, 3.77, 3.05, 2.4844, 47.3, 27.6, 22.7, 17.9, 15.7, 12.8, 11.4, 9.6, 8.6, 4.8, 7.2, 7.2, 10.2, 7.7, 6.6, 5.5, 5.35, 4.044, 59.6, 39.7, 31.1, 29.6, 28.2, 31.4, 30.1, 28.8, 27.7, 23.5, 25.5, 24.5, 23.6, 22.7, 21.8, 21, 21.9, 16.2, 13.1, 11.1, 9.7, 8.5, 7.6, 6.5, 5.8, 5.7, 7.6, 6.8, 7.4, 6.8, 6, 5.3, 48.7, 38.3, 32.1, 32.1, 25.4, 27.4, 24.8, 24.5, 23.3, 23, 22.7, 20.5, 19.7, 23.8, 18.2, 17.5 };
        double bpol = 0;
        int atomicNumber0 = 0;
        int atomicNumber1 = 0;
        double difference = 0;
        try {
            IsotopeFactory ifac = IsotopeFactory.getInstance();
            Element element0 = null;
            Element element1 = null;
            Bond[] bonds = container.getBonds();
            Atom[] atoms = null;
            String symbol0 = null;
            String symbol1 = null;
            for (int i = 0; i < bonds.length; i++) {
                atoms = container.getBondAt(i).getAtoms();
                symbol0 = atoms[0].getSymbol();
                symbol1 = atoms[1].getSymbol();
                element0 = ifac.getElement(symbol0);
                element1 = ifac.getElement(symbol1);
                atomicNumber0 = element0.getAtomicNumber();
                atomicNumber1 = element1.getAtomicNumber();
                difference = polarizabilities[atomicNumber0] - polarizabilities[atomicNumber1];
                bpol += Math.abs(difference);
            }
            return new DoubleResult(bpol);
        } catch (Exception ex1) {
            throw new CDKException("Problems with IsotopeFactory due to " + ex1.toString());
        }
    }

    /**
	 *  Gets the parameterNames attribute of the BPolDescriptor object
	 *
	 *@return    The parameterNames value
	 */
    public String[] getParameterNames() {
        return (null);
    }

    /**
	 *  Gets the parameterType attribute of the BPolDescriptor object
	 *
	 *@param  name  Description of the Parameter
	 *@return       The parameterType value
	 */
    public Object getParameterType(String name) {
        return (null);
    }
}
