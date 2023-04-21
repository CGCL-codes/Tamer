package org.openscience.cdk.tools;

import java.util.Hashtable;
import java.util.Map;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.annotations.TestClass;
import org.openscience.cdk.annotations.TestMethod;
import org.openscience.cdk.config.AtomTypeFactory;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IPseudoAtom;

/**
 * Adds implicit hydrogens based on atom type definitions. The class assumes
 * that CDK atom types are already detected. A full code example is:
 * <pre>
 *   IMolecule methane = new Molecule();
 *   IAtom carbon = new Atom("C");
 *   methane.addAtom(carbon);
 *   CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(methane.getNewBuilder());
 *   for (IAtom atom : methane.atoms) {
 *     IAtomType type = matcher.findMatchingAtomType(methane, atom);
 *     AtomTypeManipulator.configure(atom, type);
 *   }
 *   CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(methane.getNewBuilder());
 *   adder.addImplicitHydrogens(methane);
 * </pre>
 *
 * <p>If you want to add the hydrogens to a specific atom only,
 * use this example:
 * <pre>
 *   IMolecule ethane = new Molecule();
 *   IAtom carbon1 = new Atom("C");
 *   IAtom carbon2 = new Atom("C");
 *   ethane.addAtom(carbon1);
 *   ethane.addAtom(carbon2);
 *   CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(ethane.getNewBuilder());
 *   IAtomType type = matcher.findMatchingAtomType(ethane, carbon1);
 *   AtomTypeManipulator.configure(carbon1, type);
 *   CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(ethane.getNewBuilder());
 *   adder.addImplicitHydrogens(ethane, carbon1);
 * </pre>
 * 
 * @author     egonw
 * @cdk.module valencycheck
 * @cdk.githash
 */
@TestClass("org.openscience.cdk.tools.CDKHydrogenAdderTest")
public class CDKHydrogenAdder {

    private AtomTypeFactory atomTypeList;

    private static final String ATOM_TYPE_LIST = "org/openscience/cdk/dict/data/cdk-atom-types.owl";

    private static Map<String, CDKHydrogenAdder> tables = new Hashtable<String, CDKHydrogenAdder>(3);

    private CDKHydrogenAdder(IChemObjectBuilder builder) {
        if (atomTypeList == null) atomTypeList = AtomTypeFactory.getInstance(ATOM_TYPE_LIST, builder);
    }

    @TestMethod("testInstance")
    public static CDKHydrogenAdder getInstance(IChemObjectBuilder builder) {
        if (!tables.containsKey(builder.getClass().getName())) tables.put(builder.getClass().getName(), new CDKHydrogenAdder(builder));
        return tables.get(builder.getClass().getName());
    }

    /**
	 * Sets implicit hydrogen counts for all atoms in the given IAtomContainer.
	 * 
	 * @param  container The molecule to which H's will be added
	 * @throws CDKException Throws if insufficient information is present
	 *
	 * @cdk.keyword hydrogens, adding
	 */
    @TestMethod("testMethane,testFormaldehyde,testHCN")
    public void addImplicitHydrogens(IAtomContainer container) throws CDKException {
        for (IAtom atom : container.atoms()) {
            if (!(atom instanceof IPseudoAtom)) {
                addImplicitHydrogens(container, atom);
            }
        }
    }

    /**
	 * Sets the implicit hydrogen count for the indicated IAtom in the given IAtomContainer.
	 * If the atom type is "X", then the atom is assigned zero implicit hydrogens.
	 * 
	 * @param  container  The molecule to which H's will be added
	 * @param  atom         IAtom to set the implicit hydrogen count for
	 * @throws CDKException Throws if insufficient information is present
	 */
    @TestMethod("testImpHByAtom")
    public void addImplicitHydrogens(IAtomContainer container, IAtom atom) throws CDKException {
        if (atom.getAtomTypeName() == null) throw new CDKException("IAtom is not typed! " + atom.getSymbol());
        if ("X".equals(atom.getAtomTypeName())) {
            atom.setImplicitHydrogenCount(0);
            return;
        }
        IAtomType type = atomTypeList.getAtomType(atom.getAtomTypeName());
        if (type == null) throw new CDKException("Atom type is not a recognized CDK atom type: " + atom.getAtomTypeName());
        if (type.getFormalNeighbourCount() == CDKConstants.UNSET) throw new CDKException("Atom type is too general; cannot decide the number of implicit hydrogen to add for: " + atom.getAtomTypeName());
        atom.setImplicitHydrogenCount(type.getFormalNeighbourCount() - container.getConnectedAtomsCount(atom));
    }
}
