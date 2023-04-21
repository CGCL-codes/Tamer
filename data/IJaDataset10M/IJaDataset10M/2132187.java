package org.hybridlabs.source.beautifier.replacement;

import org.hybridlabs.source.beautifier.CharacterSequence;

/**
 * Interface defining the a type replacement strategy.
 * 
 * @author Karsten Klein, hybrid labs
 *
 */
public interface TypeReplacementStrategy {

    public String modulateType(String type);

    public String composeMatch(CharacterSequence type);

    public String composeReplace(CharacterSequence type);
}
