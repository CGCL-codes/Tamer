package tests;

import java.util.HashMap;
import es.ulpgc.dis.heuriskein.model.solver.Population;
import es.ulpgc.dis.heuriskein.model.solver.initializators.RandomRealCreator;
import es.ulpgc.dis.heuriskein.model.solver.representation.RealIndividual;

public class CloneTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        RealIndividual individual = new RealIndividual();
        RealIndividual clone;
        double gen[] = new double[4];
        for (int i = 0; i < gen.length; i++) {
            gen[i] = 1.0;
        }
        individual.setGen(gen);
        clone = (RealIndividual) individual.clone();
        for (int i = 0; i < gen.length; i++) {
            gen[i] = 0.5;
        }
        individual.setGen(gen);
        Population population = new Population();
        population.setPopulationSize(4);
        Population populationCloned;
        HashMap parameters = new HashMap();
        parameters.put("LowBound", "0.0");
        parameters.put("HighBound", "1.0");
        parameters.put("Size", "4");
        RandomRealCreator initializator = new RandomRealCreator();
        initializator.setParameters(parameters);
        initializator.initialize(population);
        populationCloned = (Population) population.clone();
        RealIndividual unoalazar = (RealIndividual) population.get(population.getPopulationSize() / 2);
        unoalazar.setGen(gen);
        System.out.println("Individuos");
        System.out.println(individual);
        System.out.println(clone);
        System.out.println("Poblaciones");
        System.out.println(population.toHumanReadableString());
        System.out.println(populationCloned.toHumanReadableString());
    }
}
