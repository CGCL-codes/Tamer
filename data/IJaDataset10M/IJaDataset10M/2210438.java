package fr.ecp.lgi.disaggregation;

import static org.junit.Assert.assertFalse;
import java.io.InputStream;
import java.util.Date;
import org.decisiondeck.jlp.instanciation.LpSolverType;
import org.decisiondeck.jmcda.persist.xmcda2.aggregates.XMCDAGroupSortingProblemReader;
import org.decisiondeck.jmcda.structure.sorting.problem.group_results.IGroupSortingResults;
import org.decisiondeck.jmcda.structure.sorting.problem.results.ISortingResults;
import org.decisiondeck.xmcda_oo.structure.DecisionMaker;
import org.decisiondeck.xmcda_oo.structure.sorting.SortingProblemUtils;
import org.junit.Test;
import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;
import fr.ecp.lgi.disaggregation.infer_single_dm.InferAllSingleDmSolver;

public class TestInferAllSingleDm {

    @Test
    public void testOne() throws Exception {
        final InputSupplier<? extends InputStream> source = Resources.newInputStreamSupplier(getClass().getResource("One good alternative.xml"));
        final IGroupSortingResults group = new XMCDAGroupSortingProblemReader(source).readGroupResults();
        SortingProblemUtils.inferScales(group);
        for (DecisionMaker dm : group.getDms()) {
            final ISortingResults single = group.getResults(dm);
            final InferAllSingleDmSolver infer = new InferAllSingleDmSolver(single);
            infer.getProblem().setProblemName("One good alternative " + dm.getId() + " " + new Date());
            infer.setShouldBeFeasible(true);
            infer.solve();
        }
    }

    @Test
    public void testOneLpSolve() throws Exception {
        final InputSupplier<? extends InputStream> source = Resources.newInputStreamSupplier(getClass().getResource("One good alternative.xml"));
        final IGroupSortingResults group = new XMCDAGroupSortingProblemReader(source).readGroupResults();
        SortingProblemUtils.inferScales(group);
        for (DecisionMaker dm : group.getDms()) {
            final ISortingResults single = group.getResults(dm);
            final InferAllSingleDmSolver infer = new InferAllSingleDmSolver(single);
            infer.getProblem().setProblemName("One good alternative " + dm.getId() + " " + new Date());
            infer.setShouldBeFeasible(true);
            infer.setSolverType(LpSolverType.LP_SOLVE);
            infer.setDebug(true);
            infer.solve();
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testImpossibleThrow() throws Exception {
        final InputSupplier<? extends InputStream> source = Resources.newInputStreamSupplier(getClass().getResource("Impossible - Two mixed criteria.xml"));
        final IGroupSortingResults group = new XMCDAGroupSortingProblemReader(source).readGroupResults();
        SortingProblemUtils.inferScales(group);
        final DecisionMaker dm = group.getDms().iterator().next();
        final ISortingResults single = group.getResults(dm);
        final InferAllSingleDmSolver infer = new InferAllSingleDmSolver(single);
        infer.getProblem().setProblemName("Impossible - Two mixed criteria " + dm.getId() + " " + new Date());
        infer.setShouldBeFeasible(true);
        infer.solve();
    }

    @Test
    public void testImpossible() throws Exception {
        final InputSupplier<? extends InputStream> source = Resources.newInputStreamSupplier(getClass().getResource("Impossible - Two mixed criteria.xml"));
        final IGroupSortingResults group = new XMCDAGroupSortingProblemReader(source).readGroupResults();
        SortingProblemUtils.inferScales(group);
        final DecisionMaker dm = group.getDms().iterator().next();
        final ISortingResults single = group.getResults(dm);
        final InferAllSingleDmSolver infer = new InferAllSingleDmSolver(single);
        infer.getProblem().setProblemName("Impossible - Two mixed criteria " + dm.getId() + " " + new Date());
        assertFalse(infer.solve().isFeasible());
    }
}
