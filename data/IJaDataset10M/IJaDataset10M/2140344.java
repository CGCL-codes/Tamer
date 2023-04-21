package net.boogie.calamari.genetic.function.mating;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.boogie.calamari.genetic.model.IGene;
import net.boogie.calamari.genetic.model.IGenome;

public class CrossoverMatingFunction extends AbstractMatingFunction<IGenome> {

    private static Random s_rnd = new Random();

    private int _maxGenesPerGenome;

    public CrossoverMatingFunction(int maxGenesPerGenome, double failureRate) {
        super(failureRate);
        _maxGenesPerGenome = maxGenesPerGenome;
    }

    @Override
    protected final IGenome[] doMate(IGenome[] parents) {
        IGenome mother = parents[0];
        IGenome father = parents[1];
        if (mother.equals(father)) {
            return new IGenome[] {};
        }
        IGenome daughter = doCrossover(mother, father);
        IGenome son = doCrossover(father, mother);
        return new IGenome[] { daughter, son };
    }

    protected final IGenome doCrossover(IGenome mother, IGenome father) {
        List<IGene> motherGenes = mother.getGenes();
        List<IGene> fatherGenes = father.getGenes();
        int motherGeneCount = motherGenes == null ? 0 : motherGenes.size();
        int fatherGeneCount = fatherGenes == null ? 0 : fatherGenes.size();
        if (motherGeneCount == 0) {
            return father.createClone(false);
        }
        if (fatherGeneCount == 0) {
            return mother.createClone(false);
        }
        int motherStartIndex = 0;
        int motherEndIndex = s_rnd.nextInt(motherGeneCount);
        int fatherStartIndex = s_rnd.nextInt(fatherGeneCount);
        int fatherEndIndex = fatherGeneCount - 1;
        int maxGenesPerGenome;
        if (s_rnd.nextBoolean()) {
            maxGenesPerGenome = getMaxGenesPerGenome();
        } else {
            maxGenesPerGenome = motherGeneCount;
        }
        List<IGene> childGenes = Collections.synchronizedList(new ArrayList<IGene>());
        for (int motherGeneIndex = motherStartIndex; motherGeneIndex <= motherEndIndex; motherGeneIndex++) {
            if (childGenes.size() >= maxGenesPerGenome) {
                break;
            }
            IGene motherGene = motherGenes.get(motherGeneIndex);
            IGene childGene = motherGene;
            childGenes.add(childGene);
        }
        for (int fatherGeneIndex = fatherStartIndex; fatherGeneIndex <= fatherEndIndex; fatherGeneIndex++) {
            if (childGenes.size() >= maxGenesPerGenome) {
                break;
            }
            IGene fatherGene = fatherGenes.get(fatherGeneIndex);
            IGene childGene = fatherGene;
            childGenes.add(childGene);
        }
        IGenome child = mother.createClone(true);
        child.setGenes(childGenes);
        return child.createClone(false);
    }

    protected int getMaxGenesPerGenome() {
        return _maxGenesPerGenome;
    }

    @Override
    protected int getMaximumParentCount() {
        return 2;
    }

    @Override
    protected int getMinimumParentCount() {
        return 2;
    }
}
