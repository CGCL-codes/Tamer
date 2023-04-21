package eulergui.n3model;

/**
 * immutable statement
 *
 * @author luc peuvrier
 *
 */
public interface IStatement extends Comparable<IStatement> {

    IValue getSubject();

    IVerbResource getPredicate();

    IValue getObject();

    public LinesAndCols getLinesAndCols();

    /**
	 * accept a visitor
	 *
	 * @param visitor
	 */
    void accept(IN3ModelVisitor visitor);
}
