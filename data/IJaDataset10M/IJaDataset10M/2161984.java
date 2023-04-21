package com.trimga.matingstrategies;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.trimga.Chromosome;
import com.trimga.IllegalChromosomeException;
import com.trimga.Phenotype;

public class UniqueCombinant implements MatingStrategy {

    public Phenotype makeChildFromParents(Phenotype parent1, Phenotype parent2) throws IllegalChromosomeException {
        List<Chromosome> childChromosomeList = ChromosomeUtilities.makeCrossoverChild(parent1.getChromosomes(), parent2.getChromosomes());
        ChromosomeUtilities.randomlyPluckOutChromosomes(childChromosomeList);
        ChromosomeUtilities.randomlyMutateChromosomes(childChromosomeList);
        ChromosomeUtilities.randomlyAddGeneratedChromosomes(childChromosomeList);
        Set<Chromosome> childChromosomeSet = new HashSet<Chromosome>(childChromosomeList);
        List<Chromosome> childChromosomes = new ArrayList<Chromosome>(childChromosomeSet);
        Phenotype childPhenotype = parent1.createPhenotype(childChromosomes);
        return childPhenotype;
    }
}
