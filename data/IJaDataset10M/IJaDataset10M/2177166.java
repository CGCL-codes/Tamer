package com.csol.chem.util.pattern;

import java.util.*;
import com.csol.chem.core.*;

public class PatternMolecule {

    protected ArrayList<PatternAtom> patternAtoms = new ArrayList<PatternAtom>();

    public PatternMolecule() {
    }

    public PatternMolecule(Collection<PatternAtom> patternAtoms) {
        this.patternAtoms.addAll(patternAtoms);
    }

    public PatternMolecule(PatternMolecule patternMolecule) {
        this(patternMolecule.patternAtoms);
    }

    public PatternAtom getPatternAtom(int index) {
        return patternAtoms.get(index);
    }

    public Collection<PatternAtom> getAllPatternAtoms() {
        return patternAtoms;
    }

    public void addPatternAtom(PatternAtom patternAtom) {
        patternAtoms.add(patternAtom);
    }

    public void delPatternAtom(PatternAtom patternAtom) {
        if (!patternAtoms.remove(patternAtom)) {
            throw new IllegalArgumentException("" + "You tried to remove PatternAtom: " + patternAtom + " from " + "the Pattern Molecule, but it was not found");
        }
    }

    /**
     * Removes a pattern atom from the pattern, and unbinds it from the
     * pattern atom graph.
     * @param patternAtom
     */
    public void removeAndUnlinkPatternAtom(PatternAtom patternAtom) {
        delPatternAtom(patternAtom);
        patternAtom.unbindAll();
    }

    /**
     * Search for matches, using the whole molecule as start atoms.
     * @param target
     * @return
     */
    public Collection<Map<PatternAtom, Atom>> matches(Molecule target) {
        Collection<Map<PatternAtom, Atom>> matches = StructureMatcher.multimatch(this, target);
        return matches;
    }

    /**
     * Search for matches using the specified atoms as start atoms.
     * @param target
     * @param startAtoms
     * @return
     */
    public Collection<Map<PatternAtom, Atom>> matches(Molecule target, Collection<Atom> startAtoms) {
        Collection<Map<PatternAtom, Atom>> matches = StructureMatcher.multimatch(this, startAtoms);
        return matches;
    }

    /**
     * Search for matches using only the specified atom as start atom.
     * @param target
     * @param startAtom
     * @return
     */
    public Collection<Map<PatternAtom, Atom>> matches(Molecule target, Atom startAtom) {
        Collection<Map<PatternAtom, Atom>> matches = StructureMatcher.multimatch(this, startAtom);
        return matches;
    }

    public static LinkedList<Map<PatternAtom, Atom>> removeConflicting(Collection<Map<PatternAtom, Atom>> matches) {
        return removeConflicting(matches, null);
    }

    /**
     * Removes all matches that have overlaps, i.e. matching at least one target
     * atom that a previously selected match also has matched.
     * The algorithm checks matches in the provided order, if the match contains
     * any already "occupied" target atoms, then the match is discarded,
     * otherwise it is added to the list of unconflicting matches and will be
     * returned at the end of execution. 
     * @param matches All matches that are to be screened for conflicts.
     * @param selected collection of matches that has been selected as "occupied",
     *        atoms in those matches will not be allowed in any new matches.
     *        This parameter can be null, in which case it will be regarded as
     *        an empty selection.
     * @return a list of non conflicting matches.
     *         The returned list will never contain the original selected
     *         matches.
     */
    public static LinkedList<Map<PatternAtom, Atom>> removeConflicting(Collection<Map<PatternAtom, Atom>> matches, Collection<Map<PatternAtom, Atom>> selected) {
        if (selected == null) selected = new LinkedList<Map<PatternAtom, Atom>>();
        LinkedList<Map<PatternAtom, Atom>> clean = new LinkedList<Map<PatternAtom, Atom>>();
        HashSet<Atom> occupied = new HashSet<Atom>();
        for (Map<PatternAtom, Atom> match : selected) {
            occupied.addAll(match.values());
        }
        CHECKMATCHES: for (Map<PatternAtom, Atom> match : matches) {
            for (Atom atom : match.values()) {
                if (occupied.contains(atom)) {
                    continue CHECKMATCHES;
                }
            }
            clean.add(match);
            occupied.addAll(match.values());
        }
        return clean;
    }
}
