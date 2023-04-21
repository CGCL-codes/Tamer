package org.openscience.cdk.qsar;

import org.openscience.cdk.Atom;
import org.openscience.cdk.Element;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.tools.*;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.config.IsotopeFactory;
import org.openscience.cdk.qsar.result.*;
import junit.framework.AssertionFailedError;
import java.util.Map;
import java.util.Hashtable;
import java.util.ArrayList;

/**
 *  Atomic valence connectivity index (order 0):
 *  http://www.edusoft-lc.com/molconn/manuals/400/chaptwo.html
 *  http://www.chemcomp.com/Journal_of_CCG/Features/descr.htm#KH
 *  returned values are:
 *  chi0v is the Atomic valence connectivity index (order 0),
 *  chi0_C is the Carbon valence connectivity index (order 0);
 *  valence is the number of s and p valence electrons of atom.
 *
 * @author      mfe4
 * @cdk.created 2004-11-03
 * @cdk.module	qsar
 * @cdk.set     qsar-descriptors
 */
public class ValenceConnectivityOrderZeroDescriptor implements Descriptor {

    /**
	 *  Constructor for the ValenceConnectivityOrderZeroDescriptor object
	 */
    public ValenceConnectivityOrderZeroDescriptor() {
    }

    /**
	 *  Gets the specification attribute of the
	 *  ValenceConnectivityOrderZeroDescriptor object
	 *
	 *@return    The specification value
	 */
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification("http://qsar.sourceforge.net/dicts/qsar-descriptors:chiValuesVCOZ", this.getClass().getName(), "$Id: ValenceConnectivityOrderZeroDescriptor.java 3617 2005-01-12 15:22:38Z egonw $", "The Chemistry Development Kit");
    }

    /**
	 *  Sets the parameters attribute of the
	 *  ValenceConnectivityOrderZeroDescriptor object
	 *
	 *@param  params            The new parameters value
	 *@exception  CDKException  Description of the Exception
	 */
    public void setParameters(Object[] params) throws CDKException {
    }

    /**
	 *  Gets the parameters attribute of the
	 *  ValenceConnectivityOrderZeroDescriptor object
	 *
	 *@return    The parameters value
	 */
    public Object[] getParameters() {
        return (null);
    }

    /**
	 *  calculates the chiOv and chiOv_C descriptors for an atom container
	 *
	 *@param  ac                AtomContainer
	 *@return                   chi0v and chi0_C returned as arrayList
	 *@exception  CDKException  Possible Exceptions
	 */
    public DescriptorResult calculate(AtomContainer ac) throws CDKException {
        Hashtable valences = new Hashtable();
        valences.put("Li", new Integer(1));
        valences.put("Be", new Integer(2));
        valences.put("B", new Integer(3));
        valences.put("C", new Integer(4));
        valences.put("N", new Integer(5));
        valences.put("O", new Integer(6));
        valences.put("F", new Integer(7));
        valences.put("Na", new Integer(1));
        valences.put("Mg", new Integer(2));
        valences.put("Al", new Integer(3));
        valences.put("Si", new Integer(4));
        valences.put("P", new Integer(5));
        valences.put("S", new Integer(6));
        valences.put("Cl", new Integer(7));
        valences.put("K", new Integer(1));
        valences.put("Ca", new Integer(2));
        valences.put("Ga", new Integer(3));
        valences.put("Ge", new Integer(4));
        valences.put("As", new Integer(5));
        valences.put("Se", new Integer(6));
        valences.put("Br", new Integer(7));
        valences.put("Rb", new Integer(1));
        valences.put("Sr", new Integer(2));
        valences.put("In", new Integer(3));
        valences.put("Sn", new Integer(4));
        valences.put("Sb", new Integer(5));
        valences.put("Te", new Integer(6));
        valences.put("I", new Integer(7));
        valences.put("Cs", new Integer(1));
        valences.put("Ba", new Integer(2));
        valences.put("Tl", new Integer(3));
        valences.put("Pb", new Integer(4));
        valences.put("Bi", new Integer(5));
        valences.put("Po", new Integer(6));
        valences.put("At", new Integer(7));
        valences.put("Fr", new Integer(1));
        valences.put("Ra", new Integer(2));
        int valence = 0;
        int atomicNumber = 0;
        int hcount = 0;
        int atomValue = 0;
        DoubleArrayResult chiValuesVCOZ = new DoubleArrayResult(2);
        double chi0v = 0;
        double chi0v_C = 0;
        Atom[] atoms = ac.getAtoms();
        Atom[] neighatoms = null;
        Element element = null;
        IsotopeFactory elfac = null;
        String symbol = null;
        for (int i = 0; i < atoms.length; i++) {
            symbol = atoms[i].getSymbol();
            if (!symbol.equals("H")) {
                try {
                    elfac = IsotopeFactory.getInstance();
                } catch (Exception exc) {
                    throw new AssertionFailedError("Problem instantiating IsotopeFactory: " + exc.toString());
                }
                try {
                    element = elfac.getElement(symbol);
                } catch (Exception exc) {
                    throw new AssertionFailedError("Problem getting isotope " + symbol + " from ElementFactory: " + exc.toString());
                }
                atomicNumber = element.getAtomicNumber();
                valence = ((Integer) valences.get(symbol)).intValue();
                hcount = 0;
                atomValue = 0;
                neighatoms = ac.getConnectedAtoms(atoms[i]);
                for (int a = 0; a < neighatoms.length; a++) {
                    if (neighatoms[a].getSymbol().equals("H")) {
                        hcount += 1;
                    }
                }
                hcount += ac.getAtomAt(i).getHydrogenCount();
                atomValue = (valence - hcount) / (atomicNumber - valence - 1);
                if (atomValue > 0) {
                    if (symbol.equals("C")) {
                        chi0v_C += (1 / (Math.sqrt(atomValue)));
                    }
                    chi0v += (1 / (Math.sqrt(atomValue)));
                }
            }
        }
        chiValuesVCOZ.add(chi0v);
        chiValuesVCOZ.add(chi0v_C);
        return chiValuesVCOZ;
    }

    /**
	 *  Gets the parameterNames attribute of the
	 *  ValenceConnectivityOrderZeroDescriptor object
	 *
	 *@return    The parameterNames value
	 */
    public String[] getParameterNames() {
        return (null);
    }

    /**
	 *  Gets the parameterType attribute of the
	 *  ValenceConnectivityOrderZeroDescriptor object
	 *
	 *@param  name  Description of the Parameter
	 *@return       The parameterType value
	 */
    public Object getParameterType(String name) {
        return (null);
    }
}
