package org.openscience.cdk.qsar;

import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.charges.GasteigerMarsiliPartialCharges;
import java.util.Map;
import java.util.Hashtable;
import java.util.ArrayList;

/**
 *  The calculation of partial charges of an heavy atom and its protons is based on Gasteiger Marsili (PEOE)
 *
 *@author         mfe4
 *@cdk.created    2004-11-03
 * @cdk.module qsar
 */
public class ProtonTotalPartialChargeDescriptor implements Descriptor {

    private int atomPosition = 0;

    /**
	 *  Constructor for the ProtonTotalPartialChargeDescriptor object
	 */
    public ProtonTotalPartialChargeDescriptor() {
    }

    /**
	 *  Gets the specification attribute of the ProtonTotalPartialChargeDescriptor
	 *  object
	 *
	 *@return    The specification value
	 */
    public Map getSpecification() {
        Hashtable specs = new Hashtable();
        specs.put("Specification-Reference", "http://qsar.sourceforge.net/dicts/qsar-descriptors:protonPartialCharge");
        specs.put("Implementation-Title", this.getClass().getName());
        specs.put("Implementation-Identifier", "$Id: ProtonTotalPartialChargeDescriptor.java 3296 2004-11-29 11:07:02Z egonw $");
        specs.put("Implementation-Vendor", "The Chemistry Development Kit");
        return specs;
    }

    /**
	 *  Sets the parameters attribute of the ProtonTotalPartialChargeDescriptor
	 *  object
	 *
	 *@param  params            The new parameters value
	 *@exception  CDKException  Description of the Exception
	 */
    public void setParameters(Object[] params) throws CDKException {
        if (params.length > 1) {
            throw new CDKException("ProtonTotalPartialChargeDescriptor only expects one parameter");
        }
        if (!(params[0] instanceof Integer)) {
            throw new CDKException("The parameter must be of type Integer");
        }
        atomPosition = ((Integer) params[0]).intValue();
    }

    /**
	 *  Gets the parameters attribute of the ProtonTotalPartialChargeDescriptor
	 *  object
	 *
	 *@return    The parameters value
	 */
    public Object[] getParameters() {
        Object[] params = new Object[1];
        params[0] = new Integer(atomPosition);
        return params;
    }

    /**
	 *  The method returns partial charges assigned to an heavy atom and its protons through Gasteiger Marsili
	 *  It is needed to call the addExplicitHydrogensToSatisfyValency method from the class tools.HydrogenAdder.
	 *
	 *@param  ac                AtomContainer
	 *@return                   an array of doubles with partial charges of [heavy, proton_1 ... proton_n]
	 *@exception  CDKException  Possible Exceptions
	 */
    public Object calculate(AtomContainer ac) throws CDKException {
        int counter = 1;
        Molecule mol = new Molecule(ac);
        try {
            GasteigerMarsiliPartialCharges peoe = new GasteigerMarsiliPartialCharges();
            peoe.assignGasteigerMarsiliPartialCharges(mol, true);
        } catch (Exception ex1) {
            throw new CDKException("Problems with assignGasteigerMarsiliPartialCharges due to " + ex1.toString());
        }
        Atom target = mol.getAtomAt(atomPosition);
        Atom[] neighboors = mol.getConnectedAtoms(target);
        ArrayList protonPartialCharge = new ArrayList(neighboors.length + 1);
        protonPartialCharge.add(new Double(target.getCharge()));
        for (int i = 0; i < neighboors.length; i++) {
            if (neighboors[i].getSymbol().equals("H")) {
                protonPartialCharge.add(new Double(neighboors[i].getCharge()));
                counter++;
            }
        }
        return protonPartialCharge;
    }

    /**
	 *  Gets the parameterNames attribute of the ProtonTotalPartialChargeDescriptor
	 *  object
	 *
	 *@return    The parameterNames value
	 */
    public String[] getParameterNames() {
        String[] params = new String[1];
        params[0] = "The position of the atom whose protons calculate total partial charge";
        return params;
    }

    /**
	 *  Gets the parameterType attribute of the ProtonTotalPartialChargeDescriptor
	 *  object
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
