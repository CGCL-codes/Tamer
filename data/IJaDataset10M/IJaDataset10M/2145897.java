package recoder.java.statement;

/**
 * Branch statement.
 * 
 * @author AL
 * @author <TT>AutoDoc</TT>
 */
public abstract class BranchStatement extends JavaStatement {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * Branch statement.
     */
    public BranchStatement() {
    }

    /**
     * Branch statement.
     * 
     * @param proto
     *            a branch statement.
     */
    protected BranchStatement(BranchStatement proto) {
        super(proto);
    }

    /**
     * Get the number of branches in this container.
     * 
     * @return the number of branches.
     */
    public abstract int getBranchCount();

    public abstract Branch getBranchAt(int index);
}
