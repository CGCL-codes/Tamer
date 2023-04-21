package org.encog.ml.genetic.genome;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.encog.ml.genetic.genes.Gene;

/**
 * Implements a chromosome to genetic algorithm. This is an abstract class.
 * Other classes are provided in this book that use this base class to train
 * neural networks or provide an answer to the traveling salesman problem. 
 * Chromosomes are made up of genes. 
 * 
 * Genomes in this genetic algorithm consist of one or more chromosomes. 
 * 
 */
public class Chromosome implements Serializable {

    /**
	 * Serial id.
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * The individual elements of this chromosome.
	 */
    private final List<Gene> genes = new ArrayList<Gene>();

    /**
	 * Add a gene.
	 * @param gene The gene to add.
	 */
    public final void add(final Gene gene) {
        genes.add(gene);
    }

    /**
	 * Get an individual gene.
	 * @param i The index of the gene.
	 * @return The gene.
	 */
    public final Gene get(final int i) {
        return genes.get(i);
    }

    /**
	 * Get the specified gene.
	 * 
	 * @param gene
	 *            The specified gene.
	 * @return The gene specified.
	 */
    public final Gene getGene(final int gene) {
        return genes.get(gene);
    }

    /**
	 * Used the get the entire gene list.
	 * 
	 * @return the genes
	 */
    public final List<Gene> getGenes() {
        return genes;
    }

    /**
	 * @return The number of genes in this chromosome.
	 */
    public final int size() {
        return genes.size();
    }
}
