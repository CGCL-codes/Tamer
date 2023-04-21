package hotair.management.strategies;

/**
 * Class to alter the allocation of Processors to Providers
 *
 * @author daithi
 * @version $Id$
 */
public class AllocationStrategy extends ManagementStrategy {

    private System system;

    /** Creates a new instance of AllocationStrategy */
    public AllocationStrategy(System system) {
        this.system = system;
    }

    public void applyStrategy() {
    }
}
