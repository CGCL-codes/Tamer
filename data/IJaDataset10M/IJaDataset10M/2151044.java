package jenes.tutorials.gp3;

import jenes.algorithms.SimpleGP;
import jenes.gp.GPChromosome;
import jenes.gp.GPDomain;
import jenes.gp.GPNode;
import jenes.gp.builder.GPRampedTreeBuilder;
import jenes.gp.operator.GPBidCrossover;
import jenes.gp.operator.GPHoistMutator;
import jenes.gp.operator.GPPermutationMutator;
import jenes.gp.operator.GPShrinkMutator;
import jenes.gp.operator.GPSimpleBidCrossover;
import jenes.gp.problem.ant.Trail.TimeExpiredException;
import jenes.gp.problem.ant.Ground;
import jenes.population.Individual;
import jenes.population.Population;
import jenes.stage.operator.common.TournamentSelector;

/**
 *
 * @author Marco
 */
public class AntGP extends SimpleGP<GPChromosome> {

    Ground ground;

    AntVisitor visitor;

    int totalfood;

    public AntGP(GPDomain domain, int popsize, int genlim, Ground ground) {
        super(new Population<GPChromosome>(new Individual<GPChromosome>(new GPChromosome(null, domain)), popsize), genlim, 1, new TournamentSelector<GPChromosome>(2), new GPSimpleBidCrossover<GPChromosome>(0.8, 10, 7), new GPShrinkMutator<GPChromosome>(0.2), new GPRampedTreeBuilder(domain), ElitismStrategy.RANDOM);
        super.setBiggerIsBetter(true);
        this.ground = ground;
        totalfood = ground.getTrail().getTotalFood();
        visitor = new AntVisitor(ground);
    }

    @Override
    protected void evaluateIndividual(Individual<GPChromosome> individual) {
        ground.reset();
        GPNode root = individual.getChromosome().getRoot();
        try {
            while (true) root.explore(visitor);
        } catch (TimeExpiredException tex) {
            individual.setScore(tex.getFitness());
        }
    }

    @Override
    protected boolean end() {
        jenes.population.Population.Statistics stat = this.getCurrentPopulation().getStatistics();
        return stat.getLegalHighestScore() == totalfood;
    }
}
