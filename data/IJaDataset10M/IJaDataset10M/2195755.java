package org.sti2.elly.api.basics;

import java.util.List;
import org.sti2.elly.api.terms.IVariable;

/**
 * A Rule consists of a Head and a Body, which are themselves Lists of {@link IAtom}s.
 * The logical view is <code>Head &larr; Body</code>.
 * 
 * @author Daniel Winkler
 */
public interface IRule extends IBasicEntity {

    /**
	 * Returns the Head of this Rule.
	 * 
	 * @return the Head of this Rule.
	 */
    public List<IAtom> getHead();

    /**
	 * Returns the Body of this Rule.
	 * 
	 * @return the Body of this Rule.
	 */
    public List<IAtom> getBody();

    /**
	 * Returns the initial {@link IVariable} for the head of this Rule.
	 * <p>
	 * For definition of "initial" see
	 * "ELP: Tractable Rules for OWL 2, Theorem 16".
	 * 
	 * @return the initial Variable for the head of this Rule, or <code>null</code> if
	 *         no initial Variable exists.
	 */
    public IVariable getInitialForHead();

    /**
	 * Returns the final {@link IVariable} for the head of this Rule.
	 * <p>
	 * For definition of "final" see
	 * "ELP: Tractable Rules for OWL 2, Theorem 16".
	 * 
	 * @return the final Variable for the head of this Rule, or <code>null</code> if
	 *         no final Variable exists.
	 */
    public IVariable getFinalForHead();
}
