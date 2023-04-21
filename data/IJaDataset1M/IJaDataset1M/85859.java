package net.jdebate.core;

import java.util.Collection;

public interface Debate<O extends Option<O>> {

    /**
	 * creates a new Literal
	 * @return
	 */
    public Literal<O> newLiteral();

    /**
	 * creates a new Clause consisting of the given literals.
	 * @param literals
	 * @return
	 */
    public Clause<O> newClause(Collection<Literal<O>> literals);

    /**
	 * creates a new option vector initialized with the given option
	 * @param option
	 * @return
	 */
    public OptionVector<O> newOptionVector(O option);

    public ClauseManager<O> getClauseManager();
}
