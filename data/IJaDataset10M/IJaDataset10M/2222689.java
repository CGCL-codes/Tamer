package org.openscience.cdk.iupac.parser;

import java.util.Iterator;
import java.util.Vector;
import org.openscience.cdk.Atom;
import org.openscience.cdk.Bond;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.Ring;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IBond.Order;
import org.openscience.cdk.templates.MoleculeFactory;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

/**
 * Takes in parsed Tokens from NomParser and contains rules
 * to convert those tokens to a Molecule.
 *
 * @see Token
 * @author David Robinson
 * @cdk.githash
 * @author Bhupinder Sandhu
 * @author Stephen Tomkinson
 *
 * @cdk.require ant1.6
 */
public class MoleculeBuilder {

    /** The molecule which is worked upon throughout the class and returned at the end */
    private Molecule currentMolecule = new Molecule();

    private IAtom endOfChain;

    /**
     * Builds the main chain which may act as a foundation for futher working groups.
     *
     * @param mainChain The parsed prefix which depicts the chain's length.
     * @param isMainCyclic A flag to show if the molecule is a ring. 0 means not a ring, 1 means is a ring.
     * @return A Molecule containing the requested chain.
     */
    private Molecule buildChain(int length, boolean isMainCyclic) {
        Molecule currentChain;
        if (length > 0) {
            if (isMainCyclic) {
                currentChain = new Molecule();
                currentChain.add(new Ring(length, "C"));
            } else {
                currentChain = MoleculeFactory.makeAlkane(length);
            }
        } else {
            currentChain = new Molecule();
        }
        return currentChain;
    }

    /**
     * Initiates the building of the molecules functional group(s).
     * Adds the functional group to atom 0 if only one group exists or runs
     * down the list of positions adding groups as required.
     *
     * @param attachedGroups A vector of AttachedGroup's representing functional groups.
     * @see #addFunGroup
     */
    private void buildFunGroups(Vector attachedGroups) {
        Iterator groupsIterator = attachedGroups.iterator();
        while (groupsIterator.hasNext()) {
            AttachedGroup attachedGroup = (AttachedGroup) groupsIterator.next();
            Iterator locationsIterator = attachedGroup.getLocations().iterator();
            while (locationsIterator.hasNext()) {
                Token locationToken = (Token) locationsIterator.next();
                addFunGroup(attachedGroup.getName(), Integer.parseInt(locationToken.image) - 1);
            }
        }
    }

    /**
     * Adds a functional group to a given atom in the current molecule.
     *
     * @param funGroupToken The token which denotes this specific functional group.
     * @param addPos The atom to add the group to.
     */
    private void addFunGroup(String funGroupToken, int addPos) {
        if (funGroupToken == "an") {
        } else if (funGroupToken == "en") {
            if (addPos < 0) {
                currentMolecule.getBond(0).setOrder(IBond.Order.DOUBLE);
            } else {
                currentMolecule.getBond(addPos).setOrder(IBond.Order.DOUBLE);
            }
        } else if (funGroupToken == "yn") {
            if (addPos < 0) {
                currentMolecule.getBond(0).setOrder(IBond.Order.TRIPLE);
            } else {
                currentMolecule.getBond(addPos).setOrder(IBond.Order.TRIPLE);
            }
        } else if (funGroupToken == "e") {
        } else if (funGroupToken == "ol" || funGroupToken == "hydroxy") {
            if (addPos < 0) {
                addAtom("O", endOfChain, IBond.Order.SINGLE, 1);
            } else {
                addAtom("O", currentMolecule.getAtom(addPos), IBond.Order.SINGLE, 1);
            }
        } else if (funGroupToken == "al") {
            addAtom("O", endOfChain, IBond.Order.DOUBLE, 0);
        } else if (funGroupToken == "oic acid") {
            addAtom("O", endOfChain, IBond.Order.DOUBLE, 0);
            addAtom("O", endOfChain, IBond.Order.SINGLE, 1);
        } else if (funGroupToken == "oyl chloride") {
            addAtom("O", endOfChain, IBond.Order.DOUBLE, 0);
            addAtom("Cl", endOfChain, IBond.Order.SINGLE, 0);
        } else if (funGroupToken == "chloro") {
            if (addPos < 0) {
                addAtom("Cl", currentMolecule.getFirstAtom(), IBond.Order.SINGLE, 0);
            } else {
                addAtom("Cl", currentMolecule.getAtom(addPos), IBond.Order.SINGLE, 0);
            }
        } else if (funGroupToken == "fluoro") {
            if (addPos < 0) {
                addAtom("F", currentMolecule.getFirstAtom(), IBond.Order.SINGLE, 0);
            } else {
                addAtom("F", currentMolecule.getAtom(addPos), IBond.Order.SINGLE, 0);
            }
        } else if (funGroupToken == "bromo") {
            if (addPos < 0) {
                addAtom("Br", currentMolecule.getFirstAtom(), IBond.Order.SINGLE, 0);
            } else {
                addAtom("Br", currentMolecule.getAtom(addPos), IBond.Order.SINGLE, 0);
            }
        } else if (funGroupToken == "iodo") {
            if (addPos < 0) {
                addAtom("I", currentMolecule.getFirstAtom(), IBond.Order.SINGLE, 0);
            } else {
                addAtom("I", currentMolecule.getAtom(addPos), IBond.Order.SINGLE, 0);
            }
        } else if (funGroupToken == "nitro") {
            if (addPos < 0) {
                addAtom("N", currentMolecule.getFirstAtom(), IBond.Order.SINGLE, 0);
            } else {
                addAtom("N", currentMolecule.getAtom(addPos), IBond.Order.SINGLE, 0);
            }
            IAtom nitrogenAtom = currentMolecule.getLastAtom();
            nitrogenAtom.setFormalCharge(+1);
            addAtom("O", nitrogenAtom, IBond.Order.SINGLE, 0);
            currentMolecule.getLastAtom().setFormalCharge(-1);
            addAtom("O", nitrogenAtom, IBond.Order.DOUBLE, 0);
        } else if (funGroupToken == "oxo") {
            if (addPos < 0) {
                addAtom("O", currentMolecule.getFirstAtom(), IBond.Order.DOUBLE, 0);
            } else {
                addAtom("O", currentMolecule.getAtom(addPos), IBond.Order.DOUBLE, 0);
            }
        } else if (funGroupToken == "nitrile") {
            addAtom("N", currentMolecule.getFirstAtom(), IBond.Order.TRIPLE, 0);
        } else if (funGroupToken == "phenyl") {
            Molecule benzene = MoleculeFactory.makeBenzene();
            try {
                AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(benzene);
                CDKHueckelAromaticityDetector.detectAromaticity(benzene);
            } catch (Exception exc) {
            }
            currentMolecule.add(benzene);
            Bond joiningBond;
            if (addPos < 0) {
                joiningBond = new Bond(currentMolecule.getFirstAtom(), benzene.getFirstAtom());
            } else {
                joiningBond = new Bond(currentMolecule.getAtom(addPos), benzene.getFirstAtom());
            }
            currentMolecule.addBond(joiningBond);
        } else if (funGroupToken == "amino") {
            if (addPos < 0) {
                addAtom("N", currentMolecule.getFirstAtom(), IBond.Order.SINGLE, 2);
            } else {
                addAtom("N", currentMolecule.getAtom(addPos), IBond.Order.SINGLE, 2);
            }
        } else if (funGroupToken == "alumino") {
            if (addPos < 0) {
                addAtom("Al", currentMolecule.getFirstAtom(), IBond.Order.SINGLE, 2);
            } else {
                addAtom("Al", currentMolecule.getAtom(addPos), IBond.Order.SINGLE, 2);
            }
        } else if (funGroupToken == "litho") {
            if (addPos < 0) {
                addAtom("Li", currentMolecule.getFirstAtom(), IBond.Order.SINGLE, 2);
            } else {
                addAtom("Li", currentMolecule.getAtom(addPos), IBond.Order.SINGLE, 2);
            }
        } else if (funGroupToken == "oate") {
            addAtom("O", endOfChain, IBond.Order.DOUBLE, 0);
            addAtom("O", endOfChain, IBond.Order.SINGLE, 0);
            endOfChain = currentMolecule.getLastAtom();
        } else if (funGroupToken == "amine") {
            addAtom("N", endOfChain, IBond.Order.SINGLE, 1);
            endOfChain = currentMolecule.getLastAtom();
        } else if (funGroupToken == "amide") {
            addAtom("O", endOfChain, IBond.Order.DOUBLE, 0);
            addAtom("N", endOfChain, IBond.Order.SINGLE, 1);
            endOfChain = currentMolecule.getLastAtom();
        } else if (funGroupToken == "one") {
            addAtom("O", endOfChain, IBond.Order.DOUBLE, 2);
        } else if (getMetalAtomicSymbol(funGroupToken) != null) {
            currentMolecule.addAtom(new Atom(getMetalAtomicSymbol(funGroupToken)));
            endOfChain = currentMolecule.getLastAtom();
        } else {
        }
    }

    /**
     * Translates a metal's name into it's atomic symbol.
     *
     * @param metalName The name of the metal, e.g. lead
     * @return The given metal's atomic symbol e.g. Pb or null if none exist.
     */
    String getMetalAtomicSymbol(String metalName) {
        if (metalName == "aluminium") {
            return "Al";
        } else if (metalName == "magnesium") {
            return "Mg";
        } else if (metalName == "gallium") {
            return "Ga";
        } else if (metalName == "indium") {
            return "In";
        } else if (metalName == "thallium") {
            return "Tl";
        } else if (metalName == "germanium") {
            return "Ge";
        } else if (metalName == "tin") {
            return "Sn";
        } else if (metalName == "lead") {
            return "Pb";
        } else if (metalName == "arsenic") {
            return "As";
        } else if (metalName == "antimony") {
            return "Sb";
        } else if (metalName == "bismuth") {
            return "Bi";
        }
        return null;
    }

    /**
     * Adds an atom to the current molecule.
     *
     * @param newAtomType The atomic symbol for the atom.
     * @param otherConnectingAtom An atom already in the molecule which
     * the new one should connect to.
     * @param bondOrder The order of the bond to use to join the two atoms.
     * @param hydrogenCount The number of hydrogen atoms connected to this atom.
     */
    private void addAtom(String newAtomType, IAtom otherConnectingAtom, Order bondOrder, int hydrogenCount) {
        Atom newAtom = new Atom(newAtomType);
        newAtom.setImplicitHydrogenCount(hydrogenCount);
        Bond newBond = new Bond(newAtom, otherConnectingAtom, bondOrder);
        currentMolecule.addAtom(newAtom);
        currentMolecule.addBond(newBond);
    }

    /**
     * Adds other chains to the main chain connected at the specified atom.
     *
     * @param attachedSubstituents A vector of AttachedGroup's representing substituents.
     */
    private void addHeads(Vector attachedSubstituents) {
        Iterator substituentsIterator = attachedSubstituents.iterator();
        while (substituentsIterator.hasNext()) {
            AttachedGroup attachedSubstituent = (AttachedGroup) substituentsIterator.next();
            Iterator locationsIterator = attachedSubstituent.getLocations().iterator();
            while (locationsIterator.hasNext()) {
                Token locationToken = (Token) locationsIterator.next();
                int joinLocation = Integer.parseInt(locationToken.image) - 1;
                IAtom connectingAtom;
                if (joinLocation < 0) {
                    connectingAtom = endOfChain;
                } else {
                    connectingAtom = currentMolecule.getAtom(joinLocation);
                }
                Molecule subChain = buildChain(attachedSubstituent.getLength(), false);
                Bond linkingBond = new Bond(subChain.getFirstAtom(), connectingAtom);
                currentMolecule.addBond(linkingBond);
                currentMolecule.add(subChain);
            }
        }
    }

    /**
     * Start of the process of building a molecule from the parsed data. Passes the parsed
     * tokens to other functions which build up the Molecule.
     *
     * @param mainChain The string representation of the length of the main chain.
     * @param attachedSubstituents A vector of AttachedGroup's representing substituents.
     * @param attachedGroups A vector of AttachedGroup's representing functional groups.
     * @param isMainCyclic An indiacation of if the main chain is cyclic.
     * @return The molecule as built from the parsed tokens.
     */
    protected Molecule buildMolecule(int mainChain, Vector attachedSubstituents, Vector attachedGroups, boolean isMainCyclic, String name) throws ParseException, CDKException {
        currentMolecule.setID(name);
        currentMolecule.add(buildChain(mainChain, isMainCyclic));
        if (mainChain != 0) endOfChain = currentMolecule.getLastAtom();
        buildFunGroups(attachedGroups);
        addHeads(attachedSubstituents);
        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(currentMolecule.getBuilder());
        Iterator<IAtom> atoms = currentMolecule.atoms().iterator();
        while (atoms.hasNext()) {
            IAtom atom = atoms.next();
            IAtomType type = matcher.findMatchingAtomType(currentMolecule, atom);
            AtomTypeManipulator.configure(atom, type);
        }
        CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(currentMolecule.getBuilder());
        hAdder.addImplicitHydrogens(currentMolecule);
        return currentMolecule;
    }
}
