package ch.fhnw.wi.fit.ruleengine.abstraction.rule;

import java.util.ArrayList;

/**
 * Represents a body, which presents the condition, of the rule.
 *  
 * @author daniela.feldkamp
 *
 */
public interface IBody {

    /**
	 * Returns a list of all atoms of the rule. 
	 * @return the rul
	 */
    public ArrayList<IAtom> getAllAtoms();

    /**
	 * Adds an atom to the list of atoms.
	 * @param atom a new atom, which should be added to the list.
	 */
    public void addAtom(IAtom atom);

    /**
	 * Sets all atoms of a list.
	 * @param allAtoms
	 */
    public void setListOfAtoms(ArrayList<IAtom> allAtoms);
}
