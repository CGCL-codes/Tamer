package dopisolver.tasks;

/**
 * GreedyMinSimple DOP
 */
public class GreedyMinSimpleDOPTask extends BaseGreedyMinSimpleTask {

    /**
     * @see dopisolver.tasks.BaseGreedyMinSimpleTask#isDOPI()
     */
    @Override
    protected final boolean isDOPI() {
        return false;
    }

    /**
     * @see dopisolver.ISolverTask#getName()
     */
    @Override
    public String getName() {
        return "GreedyMinSimpleDOP";
    }
}
