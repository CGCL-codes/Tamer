package net.sf.openforge.lim;

/**
 * A ControlDependency describes the dependence of a {@link Port Port}
 * on the assertion of the control signal which qualifies the
 * specified {@link Bus}
 *
 * @author  Stephen Edwards
 * @version $Id: ControlDependency.java 2 2005-06-09 20:00:48Z imiller $
 */
public class ControlDependency extends Dependency {

    private static final String rcs_id = "RCS_REVISION: $Rev: 2 $";

    /**
     * Constructs a ControlDependency.
     *
     * @param logicalBus the done bus on which the go port logically
     *          depends
     */
    public ControlDependency(Bus logicalBus) {
        super(logicalBus);
    }

    public Dependency createSameType(Bus logicalBus) {
        return new ControlDependency(logicalBus);
    }

    public boolean equals(Object obj) {
        if (obj instanceof ControlDependency) {
            return super.equals(obj);
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }
}
