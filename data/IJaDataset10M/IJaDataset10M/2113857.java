package net.sf.openforge.optimize;

import net.sf.openforge.lim.Visitable;

/**
 * Implemented by all optimizations invoked by {@link Optimizer}.
 *
 * @version $Id: Optimization.java 2 2005-06-09 20:00:48Z imiller $
 */
public interface Optimization {

    /**
     * Applies this optimization to a given target.
     *
     * @param target the target on which to run this optimization
     */
    public void run(Visitable target);

    /**
     * Method called prior to performing the optimization, should use
     * Job (info, verbose, etc) to report to the user what action is
     * being performed.
     */
    public void preStatus();

    /**
     * Method called after performing the optimization, should use
     * Job (info, verbose, etc) to report to the user the results
     * (if any) of running the optimization
     */
    public void postStatus();

    /**
     * Should return true if the optimization modified the LIM
     * <b>and</b> that other optimizations in its grouping should be
     * re-run
     */
    public boolean didModify();

    /**
     * The clear method is called after each complete visit to the
     * optimization and should free up as much memory as possible, and
     * reset any per run status gathering.
     */
    public void clear();
}
