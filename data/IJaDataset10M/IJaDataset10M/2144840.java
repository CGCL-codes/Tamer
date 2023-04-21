package jmetal.metaheuristics.pesaII;

import jmetal.base.*;
import jmetal.base.archive.AdaptiveGridArchive;
import jmetal.base.operator.selection.PESA2Selection;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 * This class implements the PESA2 algorithm. 
 */
public class PESA2 extends Algorithm {

    /**
   * Stores the problem to solve
   */
    private Problem problem_;

    /**
  * Constructor
  * Creates a new instance of PESA2
  */
    public PESA2(Problem problem) {
        problem_ = problem;
    }

    /**   
  * Runs of the PESA2 algorithm.
  * @return a <code>SolutionSet</code> that is a set of non dominated solutions
  * as a result of the algorithm execution  
   * @throws JMException 
  */
    public SolutionSet execute() throws JMException {
        int archiveSize, bisections, maxEvaluations, evaluations, populationSize;
        AdaptiveGridArchive archive;
        SolutionSet solutionSet;
        Operator crossover, mutation, selection;
        populationSize = ((Integer) (inputParameters_.get("populationSize"))).intValue();
        archiveSize = ((Integer) (inputParameters_.get("archiveSize"))).intValue();
        bisections = ((Integer) (inputParameters_.get("bisections"))).intValue();
        maxEvaluations = ((Integer) (inputParameters_.get("maxEvaluations"))).intValue();
        crossover = operators_.get("crossover");
        mutation = operators_.get("mutation");
        evaluations = 0;
        archive = new AdaptiveGridArchive(archiveSize, bisections, problem_.getNumberOfObjectives());
        solutionSet = new SolutionSet(populationSize);
        selection = new PESA2Selection();
        for (int i = 0; i < populationSize; i++) {
            Solution solution = new Solution(problem_);
            problem_.evaluate(solution);
            problem_.evaluateConstraints(solution);
            evaluations++;
            solutionSet.add(solution);
        }
        for (int i = 0; i < solutionSet.size(); i++) {
            archive.add(solutionSet.get(i));
        }
        solutionSet.clear();
        Solution[] parents = new Solution[2];
        do {
            while (solutionSet.size() < populationSize) {
                parents[0] = (Solution) selection.execute(archive);
                parents[1] = (Solution) selection.execute(archive);
                Solution[] offSpring = (Solution[]) crossover.execute(parents);
                mutation.execute(offSpring[0]);
                problem_.evaluate(offSpring[0]);
                problem_.evaluateConstraints(offSpring[0]);
                evaluations++;
                solutionSet.add(offSpring[0]);
            }
            for (int i = 0; i < solutionSet.size(); i++) archive.add(solutionSet.get(i));
            solutionSet.clear();
        } while (evaluations < maxEvaluations);
        return archive;
    }
}
