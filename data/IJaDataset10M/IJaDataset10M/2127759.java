package jenes.problems.menulayout.gp;

import jenes.AlgorithmEventListener;
import jenes.GenerationEventListener;
import jenes.GeneticAlgorithm;
import jenes.GeneticAlgorithm.ElitismStrategy;
import jenes.chromosome.Chromosome;
import jenes.gp.GPChromosome;
import jenes.gp.builder.GPTreeBuilder;
import jenes.gp.operator.GPConstrainedBidCrossover;
import jenes.gp.operator.GPConstrainedSimpleMutator;
import jenes.population.Individual;
import jenes.population.Population;
import jenes.problems.menulayout.Data.GenerationData;
import jenes.problems.menulayout.MenuProblem;
import jenes.problems.menulayout.common.constraints.PreferenceTable;
import jenes.stage.operator.common.TournamentSelector;
import jenes.tutorials.utils.Utils;

/**
 *
 * @author Roberto
 */
public class MenuGPProblem extends MenuProblem implements AlgorithmEventListener<GPChromosome>, GenerationEventListener<GPChromosome> {

    public MenuGPProblem(String nf, int pop, int generations, double mutation, double crossover, int elitism, double randomization, int tournament, int depth, int minRoot, int maxRoot, int minSubMenu, int maxSubMenu) {
        super(nf, pop, generations, mutation, crossover, elitism, randomization, tournament, depth, minRoot, maxRoot, minSubMenu, maxSubMenu);
    }

    @Override
    public GeneticAlgorithm<Chromosome> getAlgorithm() {
        GPTreeBuilder mb = new GPMenuTreeBuilder(MenuGPProblem.getRootDomain(), MenuGPProblem.getSubMenuDomain(), Menu.values(), MenuItem.values(), preferences, depth, 10);
        Population<GPChromosome> pop = new Population<GPChromosome>(new Individual<GPChromosome>(new GPChromosome(null, MenuGPProblem.getRootDomain())), POPULATION_SIZE);
        GeneticAlgorithm algorithm = new MenuGP(pop, preferences, GENERATION_LIMIT, s);
        algorithm.setElitism(ELITISM);
        algorithm.setElitismStrategy(ElitismStrategy.RANDOM);
        ((MenuGP) algorithm).setBuilder(mb);
        algorithm.addStage(new TournamentSelector<GPChromosome>(TOURNAMENT));
        algorithm.addStage(new GPConstrainedBidCrossover<GPChromosome>(toGPConstraints(preferences), CROSSOVER, 15, depth));
        algorithm.addStage(new GPConstrainedSimpleMutator<GPChromosome>(toGPConstraints(preferences), MUTATION));
        algorithm.addGenerationEventListener(this);
        algorithm.addAlgorithmEventListener(this);
        algorithm.setElitism(ELITISM);
        algorithm.setBiggerIsBetter(true);
        return algorithm;
    }

    public void onAlgorithmStart(GeneticAlgorithm<GPChromosome> ga, long time) {
    }

    public void onAlgorithmStop(GeneticAlgorithm<GPChromosome> ga, long time) {
        Population.Statistics stat = ga.getCurrentPopulation().getStatistics();
        Individual<GPChromosome> solution = stat.getLegalHighestIndividual();
        System.out.println("Current generation: " + ga.getGeneration());
        System.out.println("\tHighest score: " + stat.getLegalHighestScore());
        System.out.println("\tLowest score: " + stat.getLegalLowestScore());
        System.out.println("\tAvg score: " + stat.getLegalScoreAvg());
        Utils.printStatistics(stat);
        if (solution != null) {
            System.out.println();
            System.out.println("Score: " + solution.getScore());
            printMenu(solution.getChromosome().getRoot());
        } else {
            System.out.println("Soluzione non trovata!");
        }
    }

    public void onAlgorithmInit(GeneticAlgorithm<GPChromosome> ga, long time) {
    }

    public void onGeneration(GeneticAlgorithm<GPChromosome> ga, long time) {
        GenerationData inner = new GenerationData();
        Population<GPChromosome> legalPopulation = new Population<GPChromosome>();
        Population<GPChromosome> illegalPopulation = new Population<GPChromosome>();
        Population<GPChromosome> pop = ga.getCurrentPopulation();
        Population<GPChromosome>.Statistics<GPChromosome> stat = pop.getStatistics();
        for (Individual<GPChromosome> i : pop) {
            if (i.isLegal()) {
                legalPopulation.add(i);
            } else {
                illegalPopulation.add(i);
            }
        }
        inner.add((double) ga.getGeneration());
        inner.add((double) legalPopulation.size());
        inner.add((double) illegalPopulation.size());
        if (stat.getLegalHighestIndividual() != null) {
            inner.add(stat.getLegalHighestScore());
        } else {
            inner.add(Double.NaN);
        }
        if (stat.getIllegalHighestIndividual() != null) {
            inner.add(stat.getIllegalHighestScore());
        } else {
            inner.add(Double.NaN);
        }
        if (stat.getLegalHighestIndividual() != null) {
            inner.add(stat.getLegalScoreAvg());
            inner.add(stat.getLegalScoreDev());
        } else {
            inner.add(Double.NaN);
            inner.add(Double.NaN);
        }
        if (stat.getIllegalHighestIndividual() != null) {
            inner.add(stat.getIllegalScoreAvg());
            inner.add(stat.getIllegalScoreDev());
        } else {
            inner.add(Double.NaN);
            inner.add(Double.NaN);
        }
        if (legalPopulation.size() > 3) {
            inner.add(legalPopulation.getIndividual((int) Math.round((double) legalPopulation.size() * 0.25) - 1).getScore());
            inner.add(legalPopulation.getIndividual((int) Math.round((double) legalPopulation.size() * 0.5) - 1).getScore());
            inner.add(legalPopulation.getIndividual((int) Math.round((double) legalPopulation.size() * 0.75) - 1).getScore());
        } else {
            inner.add(Double.NaN);
            inner.add(Double.NaN);
            inner.add(Double.NaN);
        }
        if (illegalPopulation.size() > 3) {
            inner.add(illegalPopulation.getIndividual((int) Math.round((double) illegalPopulation.size() * 0.25) - 1).getScore());
            inner.add(illegalPopulation.getIndividual((int) Math.round((double) illegalPopulation.size() * 0.5) - 1).getScore());
            inner.add(illegalPopulation.getIndividual((int) Math.round((double) illegalPopulation.size() * 0.75) - 1).getScore());
        } else {
            inner.add(Double.NaN);
            inner.add(Double.NaN);
            inner.add(Double.NaN);
        }
        inner.addTime(time);
        lim.add_inner(ga.getGeneration(), inner);
    }

    private static MenuConstraint[] toGPConstraints(PreferenceTable[] preferences) {
        MenuConstraint[] gpconstraints = new MenuConstraint[preferences.length];
        for (int i = 0; i < preferences.length; ++i) {
            gpconstraints[i] = new MenuConstraint(preferences[i]);
        }
        return gpconstraints;
    }
}
