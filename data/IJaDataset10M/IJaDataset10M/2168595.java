package ch.fhnw.wi.fit.ruleengine.abstraction.rule.atoms.buildins;

import ch.fhnw.wi.fit.ruleengine.abstraction.rule.atoms.IBuildinAtom;

/**
 * Represents a equal buildin atom. 
 * 
 * It extends IBuildinAtom and store the the first argument, the
 * second argument or the value and type of the property.
 * 
 * In SWRL the buildin satisfy if the first argument and the second argument
 * (or in this case the value) are the same.
 * 
 * @author daniela.feldkamp
 *
 */
public interface IEqualBuildinAtom extends IBuildinAtom {
}
