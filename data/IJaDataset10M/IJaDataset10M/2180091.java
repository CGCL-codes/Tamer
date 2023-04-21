package cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces;

import cz.cuni.mff.ksi.jinfer.attrstats.experiments.Experiment;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Pair;
import java.util.List;

/**
 * Interface encapsulating the decision whether to stop running the metaheuristics.
 *
 * This is based on the total time already spent as well as any properties of
 * the solutions found so far.
 *
 * @author vektor
 */
public interface TerminationCriterion {

    /**
   * Returns a flag whether to terminate the iterations of the metaheuristic.
   *
   * @param experiment Experiment in context of which to measure the quality.
   * @param time Total time taken so far.
   * @param solutions The pool solutions that were produced in the last run.
   * @return The first item of the pair is <code>true</code> if the
   * metaheuristic should be terminated, <code>false<code> otherwise. The second
   * is a string describing why the metaheuristics was terminated, in
   * user-friendly language.
   */
    Pair<Boolean, String> terminate(final Experiment experiment, final long time, final List<IdSet> solutions);
}
