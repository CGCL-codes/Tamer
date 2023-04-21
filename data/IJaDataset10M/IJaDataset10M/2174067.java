package EDU.Washington.grad.gjb.cassowary;

/**
 * Base implementation of {@link EDU.Washington.grad.gjb.cassowary.ISimpleVariable}, which implements
 * isXxx methods
 * @author gpothier
 */
public abstract class ClAbstractSimpleVariable implements ISimpleVariable {

    public boolean isDummy() {
        return false;
    }

    public boolean isExternal() {
        return true;
    }

    public boolean isPivotable() {
        return false;
    }

    public boolean isRestricted() {
        return false;
    }
}
