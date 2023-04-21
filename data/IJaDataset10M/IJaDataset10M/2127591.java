package emast.test.and.antenna;

import emast.algorithm.PPFERG;
import emast.algorithm.planner.DefaultAgentPlanner;
import emast.algorithm.planner.ERGPlanner;
import emast.model.solution.Plan;
import emast.test.AbstractTest;
import java.util.List;

/**
 *
 * @author anderson
 */
public class AntennaNoCommCoverageTest extends AbstractTest<AntennaCoverageModel, AntennaCoverageProblem, List<Plan>, ERGPlanner<AntennaCoverageModel, AntennaCoverageProblem, PPFERG<AntennaCoverageModel, AntennaCoverageProblem>>> {

    @Override
    protected AntennaCoverageProblem createProblem() {
        return new AntennaCoverageProblem();
    }

    public static void main(final String[] pArgs) {
        new AntennaNoCommCoverageTest().run();
    }

    @Override
    protected ERGPlanner<AntennaCoverageModel, AntennaCoverageProblem, PPFERG<AntennaCoverageModel, AntennaCoverageProblem>> createAlgorithm(final AntennaCoverageProblem pProblem) {
        return new ERGPlanner<AntennaCoverageModel, AntennaCoverageProblem, PPFERG<AntennaCoverageModel, AntennaCoverageProblem>>();
    }

    @Override
    protected void printResult(final ERGPlanner<AntennaCoverageModel, AntennaCoverageProblem, PPFERG<AntennaCoverageModel, AntennaCoverageProblem>> pAlg, final AntennaCoverageProblem pProblem, final List<Plan> pResult) {
        if (pResult != null) {
            System.out.println("\nInitial Policy: ");
            System.out.println(pProblem.toString(pAlg.getInitialPolicy()));
            System.out.println("\nTime: ");
            System.out.println(pAlg.getMsecs());
        }
        final List<DefaultAgentPlanner<AntennaCoverageModel, AntennaCoverageProblem>> planners = pAlg.getPlanners();
        System.out.println("Plans: \n");
        if (pResult != null) {
            for (final DefaultAgentPlanner<AntennaCoverageModel, AntennaCoverageProblem> planner : planners) {
                System.out.println("------------------------------------------------------------------\n");
                System.out.println("Agent " + planner.getAgent() + ": ");
                System.out.println("- Time: " + planner.getMsecs());
                System.out.println("- Plan: " + planner.getPlan());
                System.out.println("- Reward: " + planner.getTotalReward());
                System.out.println("- Preserv. Goal: " + planner.getProblem().getPreservationGoal());
                System.out.println();
                System.out.println(pProblem.toString(planner.getAgent(), planner.getPlan()));
            }
        } else {
            System.out.println("EMPTY");
        }
    }
}
