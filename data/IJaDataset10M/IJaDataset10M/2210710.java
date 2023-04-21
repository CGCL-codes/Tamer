package algorithms.genetic;

public class Evolution {

    private final Population population;

    private final int maxGenerations;

    public Evolution(Population population, int maxGenerations) {
        this.population = population;
        this.maxGenerations = maxGenerations;
    }

    public Individual getBest() {
        return population.getBest();
    }

    public void evolve() {
        int gen;
        for (gen = 0; gen < maxGenerations; ++gen) {
            if (getBest().isIdeal()) break;
            population.nextGeneration();
        }
    }

    public void reset() {
        this.population.reset();
    }
}
