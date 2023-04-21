package jecl;

/**
 * Stores the gene array and the computed fitness value.
 */
public class Chromosome {

    Object[] genes;

    double fitness;

    public Chromosome(Object[] genes, double fitness) {
        this.genes = genes;
        this.fitness = fitness;
    }

    public Object[] getGenes() {
        return genes;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
}
