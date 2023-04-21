package com.csol.chem.util.pattern.patterns;

import java.util.*;
import java.util.Collection;
import java.util.Map;
import com.csol.chem.core.*;
import com.csol.chem.core.Atom;
import com.csol.chem.core.Molecule;
import com.csol.chem.util.pattern.*;
import com.csol.chem.util.pattern.PatternAtom;
import com.csol.chem.util.pattern.PatternMolecule;

/**
 *
 */
public class PyranoseRing extends PatternMolecule {

    public PatternAtom c1 = new PatternAtom(0, 0, 0, "C");

    public PatternAtom c2 = new PatternAtom(0, 0, 0, "C");

    public PatternAtom c3 = new PatternAtom(0, 0, 0, "C");

    public PatternAtom c4 = new PatternAtom(0, 0, 0, "C");

    public PatternAtom c5 = new PatternAtom(0, 0, 0, "C");

    public PatternAtom o5 = new PatternAtom(0, 0, 0, "O");

    protected Collection<Map<PatternAtom, Atom>> matches = null;

    /**
     * Constructs a PyranoseRing pattern molecule.
     */
    public PyranoseRing() {
        super();
        c2.bindPatternAtom(c1);
        c3.bindPatternAtom(c2);
        c4.bindPatternAtom(c5);
        c5.bindPatternAtom(o5);
        o5.bindPatternAtom(c1);
        c1.addOkNrBonds(4);
        c1.delOkNrBonds(2);
        c2.addOkNrBonds(4);
        c2.delOkNrBonds(2);
        c3.addOkNrBonds(4);
        c3.delOkNrBonds(2);
        c4.addOkNrBonds(4);
        c4.delOkNrBonds(2);
        c5.addOkNrBonds(4);
        c5.delOkNrBonds(2);
        o5.addOkNrBonds(2);
        this.addPatternAtom(c1);
        this.addPatternAtom(c2);
        this.addPatternAtom(c3);
        this.addPatternAtom(c4);
        this.addPatternAtom(c5);
    }

    /**
     * @see com.csol.chem.util.pattern.PatternMolecule
     */
    @Override
    public Collection<Map<PatternAtom, Atom>> matches(Molecule target) {
        if (!target.hasAnalyzedRingStructures()) throw new IllegalArgumentException("" + "Target molecule does not have a ring structure analysis");
        Collection<Map<PatternAtom, Atom>> tmpMatches = StructureMatcher.multimatch(this, target);
        matches = new LinkedList<Map<PatternAtom, Atom>>();
        for (Map<PatternAtom, Atom> match : tmpMatches) {
            if (checkRingStructure(target, match)) {
                matches.add(match);
            }
        }
        return matches;
    }

    @Override
    public Collection<Map<PatternAtom, Atom>> matches(Molecule target, Collection<Atom> startAtoms) {
        if (!target.hasAnalyzedRingStructures()) throw new IllegalArgumentException("" + "Target molecule does not have a ring structure analysis");
        Collection<Map<PatternAtom, Atom>> tmpMatches = StructureMatcher.multimatch(this, startAtoms);
        matches = new LinkedList<Map<PatternAtom, Atom>>();
        for (Map<PatternAtom, Atom> match : tmpMatches) {
            if (checkRingStructure(target, match)) {
                matches.add(match);
            }
        }
        return matches;
    }

    @Override
    public Collection<Map<PatternAtom, Atom>> matches(Molecule target, Atom startAtom) {
        if (!target.hasAnalyzedRingStructures()) throw new IllegalArgumentException("" + "Target molecule does not have a ring structure analysis");
        Collection<Map<PatternAtom, Atom>> tmpMatches = StructureMatcher.multimatch(this, startAtom);
        matches = new LinkedList<Map<PatternAtom, Atom>>();
        for (Map<PatternAtom, Atom> match : tmpMatches) {
            if (checkRingStructure(target, match)) {
                matches.add(match);
            }
        }
        return matches;
    }

    /**
     * Checks that the ring structure found is one six membered ring and nothing
     * else. 
     * @param target
     * @param match
     * @return true if found one six membered ring, false otherwise.
     */
    protected boolean checkRingStructure(Molecule target, Map<PatternAtom, Atom> match) {
        ArrayList<Atom> atoms = new ArrayList<Atom>(6);
        atoms.add(match.get(c1));
        atoms.add(match.get(c2));
        atoms.add(match.get(c3));
        atoms.add(match.get(c4));
        atoms.add(match.get(c5));
        atoms.add(match.get(o5));
        TreeSet<Ring> rings = target.getRings(atoms);
        if (rings.size() != 1) return false;
        if (rings.first().size() != 6) return false;
        return true;
    }

    /**
     * 
     * @return true if the match is the reducing end
     */
    public boolean isReducingEnd(Molecule target, Map<PatternAtom, Atom> match) {
        SaccharideRingReducingEnd re = new SaccharideRingReducingEnd();
        Collection<Map<PatternAtom, Atom>> reMatches = re.matches(target, match.get(c1));
        if (reMatches.size() != 1) return false;
        return true;
    }
}
