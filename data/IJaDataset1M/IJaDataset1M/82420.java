package org.openscience.cdk.tools.manipulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.openscience.cdk.interfaces.Atom;
import org.openscience.cdk.interfaces.AtomContainer;
import org.openscience.cdk.interfaces.Bond;
import org.openscience.cdk.interfaces.ElectronContainer;
import org.openscience.cdk.interfaces.Reaction;
import org.openscience.cdk.interfaces.SetOfMolecules;
import org.openscience.cdk.interfaces.SetOfReactions;

/**
 * @cdk.module standard
 *
 * @see ChemModelManipulator
 */
public class SetOfReactionsManipulator {

    public static void removeAtomAndConnectedElectronContainers(SetOfReactions set, Atom atom) {
        Reaction[] reactions = set.getReactions();
        for (int i = 0; i < reactions.length; i++) {
            Reaction reaction = reactions[i];
            ReactionManipulator.removeAtomAndConnectedElectronContainers(reaction, atom);
            return;
        }
    }

    public static void removeElectronContainer(SetOfReactions set, ElectronContainer electrons) {
        Reaction[] reactions = set.getReactions();
        for (int i = 0; i < reactions.length; i++) {
            Reaction reaction = reactions[i];
            ReactionManipulator.removeElectronContainer(reaction, electrons);
            return;
        }
    }

    public static AtomContainer getAllInOneContainer(SetOfReactions set) {
        AtomContainer container = set.getBuilder().newAtomContainer();
        Reaction[] reactions = set.getReactions();
        for (int i = 0; i < reactions.length; i++) {
            Reaction reaction = reactions[i];
            container.add(ReactionManipulator.getAllInOneContainer(reaction));
        }
        return container;
    }

    public static SetOfMolecules getAllMolecules(SetOfReactions set) {
        SetOfMolecules moleculeSet = set.getBuilder().newSetOfMolecules();
        Reaction[] reactions = set.getReactions();
        for (int i = 0; i < reactions.length; i++) {
            Reaction reaction = reactions[i];
            moleculeSet.add(ReactionManipulator.getAllMolecules(reaction));
        }
        return moleculeSet;
    }

    public static Vector getAllIDs(SetOfReactions set) {
        Vector IDlist = new Vector();
        Reaction[] reactions = set.getReactions();
        for (int i = 0; i < reactions.length; i++) {
            Reaction reaction = reactions[i];
            IDlist.addAll(ReactionManipulator.getAllIDs(reaction));
        }
        return IDlist;
    }

    /**
     * Returns all the AtomContainer's of a Reaction.
     */
    public static AtomContainer[] getAllAtomContainers(SetOfReactions set) {
        return SetOfMoleculesManipulator.getAllAtomContainers(getAllMolecules(set));
    }

    public static Reaction getRelevantReaction(SetOfReactions set, Atom atom) {
        Reaction[] reactions = set.getReactions();
        for (int i = 0; i < reactions.length; i++) {
            Reaction reaction = reactions[i];
            AtomContainer container = ReactionManipulator.getRelevantAtomContainer(reaction, atom);
            if (container != null) {
                return reaction;
            }
        }
        return null;
    }

    public static Reaction getRelevantReaction(SetOfReactions set, Bond bond) {
        Reaction[] reactions = set.getReactions();
        for (int i = 0; i < reactions.length; i++) {
            Reaction reaction = reactions[i];
            AtomContainer container = ReactionManipulator.getRelevantAtomContainer(reaction, bond);
            if (container != null) {
                return reaction;
            }
        }
        return null;
    }

    public static AtomContainer getRelevantAtomContainer(SetOfReactions set, Atom atom) {
        Reaction[] reactions = set.getReactions();
        for (int i = 0; i < reactions.length; i++) {
            Reaction reaction = reactions[i];
            AtomContainer container = ReactionManipulator.getRelevantAtomContainer(reaction, atom);
            if (container != null) {
                return container;
            }
        }
        return null;
    }

    public static AtomContainer getRelevantAtomContainer(SetOfReactions set, Bond bond) {
        Reaction[] reactions = set.getReactions();
        for (int i = 0; i < reactions.length; i++) {
            Reaction reaction = reactions[i];
            AtomContainer container = ReactionManipulator.getRelevantAtomContainer(reaction, bond);
            if (container != null) {
                return container;
            }
        }
        return null;
    }

    public static void setAtomProperties(SetOfReactions set, Object propKey, Object propVal) {
        Reaction[] reactions = set.getReactions();
        for (int i = 0; i < reactions.length; i++) {
            Reaction reaction = reactions[i];
            ReactionManipulator.setAtomProperties(reaction, propKey, propVal);
        }
    }

    public static List getAllChemObjects(SetOfReactions set) {
        ArrayList list = new ArrayList();
        Reaction[] reactions = set.getReactions();
        for (int i = 0; i < reactions.length; i++) {
            Reaction reaction = reactions[i];
            list.addAll(ReactionManipulator.getAllChemObjects(reaction));
        }
        return list;
    }
}
