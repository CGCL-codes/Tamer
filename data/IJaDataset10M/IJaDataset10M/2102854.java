package com.oat.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import com.oat.Algorithm;
import com.oat.AlgorithmExecutor;
import com.oat.Domain;
import com.oat.Problem;
import com.oat.Solution;
import com.oat.domains.psp.PSPDomain;
import com.oat.domains.psp.PSPProblem;
import com.oat.probes.BestSolutionProbe;
import com.oat.probes.EvaluationCounterProbe;
import com.oat.stopcondition.EvaluationsStopCondition;

/**
 * Description: Test PSP in batch
 *  
 * Date: 04/09/2007<br/>
 * @author Jason Brownlee 
 *
 * <br/>
 * <pre>
 * Change History
 * ----------------------------------------------------------------------------
 * 
 * </pre>
 */
public class PSPBatchTests {

    /**
	 * Ensuring that the runs produce the same result
	 */
    @Test
    public void testAllAlgorithmsForConsistency() {
        Domain domain = new PSPDomain();
        Problem problem = new PSPProblem("hphpphhphpphphhpphph");
        AlgorithmExecutor executor = new AlgorithmExecutor();
        BestSolutionProbe solutionProbe = new BestSolutionProbe();
        EvaluationCounterProbe evalsProbe = new EvaluationCounterProbe();
        EvaluationsStopCondition sc = new EvaluationsStopCondition(1000);
        executor.setProblem(problem);
        executor.addRunProbe(solutionProbe);
        executor.addRunProbe(evalsProbe);
        executor.addStopCondition(sc);
        Algorithm[] algorithms = null;
        try {
            algorithms = domain.loadAlgorithmList();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to load algorithms: " + e.getMessage());
        }
        for (int i = 0; i < algorithms.length; i++) {
            executor.setAlgorithm(algorithms[i]);
            Solution s1 = null;
            long e1 = 0;
            Solution s2 = null;
            long e2 = 0;
            try {
                executor.executeAndWait();
                s1 = solutionProbe.getBestSolution();
                e1 = evalsProbe.getCompletedEvaluations();
                executor.executeAndWait();
                s2 = solutionProbe.getBestSolution();
                e2 = evalsProbe.getCompletedEvaluations();
            } catch (Exception e) {
                e.printStackTrace();
                fail("Failed on the execution of: " + algorithms[i].getName() + ": " + e.getMessage());
            }
            assertEquals(e1, e2);
            assertEquals(s1.getScore(), s2.getScore());
            System.out.println("> " + algorithms[i].getName());
        }
    }

    /**
     * Looking for exceptions
     */
    @Test
    public void testAllAlgorithmsOnAllProblems() {
        Domain domain = new PSPDomain();
        AlgorithmExecutor executor = new AlgorithmExecutor();
        BestSolutionProbe solutionProbe = new BestSolutionProbe();
        EvaluationCounterProbe evalsProbe = new EvaluationCounterProbe();
        EvaluationsStopCondition sc = new EvaluationsStopCondition(1000);
        executor.addRunProbe(solutionProbe);
        executor.addRunProbe(evalsProbe);
        executor.addStopCondition(sc);
        Algorithm[] algorithms = null;
        Problem[] problems = null;
        try {
            algorithms = domain.loadAlgorithmList();
            problems = domain.loadProblemList();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to load algorithms and problems: " + e.getMessage());
        }
        for (int i = 0; i < algorithms.length; i++) {
            executor.setAlgorithm(algorithms[i]);
            for (int j = 0; j < problems.length; j++) {
                executor.setProblem(problems[j]);
                try {
                    executor.executeAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Failed on the execution of: " + executor.getAlgorithm().getName() + " on " + executor.getProblem().getName() + ": " + e.getMessage());
                }
                System.out.println("> " + executor.getAlgorithm().getName() + " on " + executor.getProblem().getName());
            }
        }
    }
}
