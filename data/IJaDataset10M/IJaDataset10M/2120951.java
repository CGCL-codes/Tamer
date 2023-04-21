package jmetal.metaheuristics.singleObjective.evolutionStrategy;

import jmetal.core.*;
import java.util.Comparator;
import jmetal.util.comparators.*;
import jmetal.util.*;

/** 
 * Class implementing a (mu + lambda) ES. Lambda must be divisible by mu
 */
public class ElitistES extends Algorithm {

    private int mu_;

    private int lambda_;

    /**
  * Constructor
  * Create a new ElitistES instance.
  * @param problem Problem to solve.
  * @mu Mu
  * @lambda Lambda
  */
    public ElitistES(Problem problem, int mu, int lambda) {
        super(problem);
        mu_ = mu;
        lambda_ = lambda;
    }

    /**
  * Execute the ElitistES algorithm
 * @throws JMException 
  */
    public SolutionSet execute() throws JMException, ClassNotFoundException {
        int maxEvaluations;
        int evaluations;
        SolutionSet population;
        SolutionSet offspringPopulation;
        Operator mutationOperator;
        Comparator comparator;
        comparator = new ObjectiveComparator(0);
        maxEvaluations = ((Integer) this.getInputParameter("maxEvaluations")).intValue();
        population = new SolutionSet(mu_);
        offspringPopulation = new SolutionSet(mu_ + lambda_);
        evaluations = 0;
        mutationOperator = this.operators_.get("mutation");
        System.out.println("(" + mu_ + " + " + lambda_ + ")ES");
        Solution newIndividual;
        for (int i = 0; i < mu_; i++) {
            newIndividual = new Solution(problem_);
            problem_.evaluate(newIndividual);
            evaluations++;
            population.add(newIndividual);
        }
        int offsprings;
        offsprings = lambda_ / mu_;
        while (evaluations < maxEvaluations) {
            for (int i = 0; i < mu_; i++) {
                for (int j = 0; j < offsprings; j++) {
                    Solution offspring = new Solution(population.get(i));
                    mutationOperator.execute(offspring);
                    problem_.evaluate(offspring);
                    offspringPopulation.add(offspring);
                    evaluations++;
                }
            }
            for (int i = 0; i < mu_; i++) {
                offspringPopulation.add(population.get(i));
            }
            population.clear();
            offspringPopulation.sort(comparator);
            for (int i = 0; i < mu_; i++) population.add(offspringPopulation.get(i));
            System.out.println("Evaluation: " + evaluations + " Fitness: " + population.get(0).getObjective(0));
            offspringPopulation.clear();
        }
        SolutionSet resultPopulation = new SolutionSet(1);
        resultPopulation.add(population.get(0));
        return resultPopulation;
    }
}
