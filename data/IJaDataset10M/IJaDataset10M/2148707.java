package net.sourceforge.javautil.grammar.impl.ast;

import net.sourceforge.javautil.grammar.lexer.GrammarToken;

/**
 * The contract for managers of state related to {@link IASTPatternComposite}'s.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public interface IASTPatternCompositeState {

    /**
	 * @param pattern The patter that the state is for
	 * @param patternIndex The index of the sub pattern for which state is desired
	 * @return A state manager for the particular sub pattern
	 */
    <STATE extends IASTPatternCompositeState, COMP extends IASTPatternComposite<? extends IASTPatternCompositeLogic<STATE>>> STATE getStateFor(COMP pattern, int patternIndex);

    /**
	 * This requires that the unique index of the pattern be known in some other implementation specific way.
	 * 
	 * @param <STATE> The actual state type
	 * @param <COMP> The composite that deals with such a state type
	 * @param pattern The pattern for which state is needed
	 * @return The state related to the pattern
	 * 
	 * @see #getStateFor(IASTPatternComposite, int)
	 */
    <STATE extends IASTPatternCompositeState, COMP extends IASTPatternComposite<? extends IASTPatternCompositeLogic<STATE>>> STATE getStateFor(COMP pattern);

    /**
	 * @return Creates definition state for this composite
	 */
    IASTDefinitionState getDefinitionState(IASTDefinition definition, IASTDefinitionState parent);

    /**
	 * Clear all the state and sub state related to this state manager
	 */
    void clear();
}
